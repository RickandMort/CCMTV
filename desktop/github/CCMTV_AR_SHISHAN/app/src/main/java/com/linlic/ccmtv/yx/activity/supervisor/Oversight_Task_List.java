package com.linlic.ccmtv.yx.activity.supervisor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Supervision_task;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/5/7.
 */

public class Oversight_Task_List  extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    @Bind(R.id.recyclelistview)
    RecyclerView recyclelistview;
    private Context context;
    private List<Supervision_task> listData = new ArrayList<>();
    BaseRecyclerViewAdapter baseRecyclerViewAdapter ;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                listData.clear();
                                String year_str = "0";
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    if(!year_str.equals(dataJson1.getString("year"))){
                                        year_str = dataJson1.getString("year");
                                        Supervision_task map = new Supervision_task();
                                        map.setId(dataJson1.getString("apply_id"));
                                        map.setTitle(dataJson1.getString("year"));
                                        map.setIs_year("1");
                                        map.setTerm("");
                                        map.setCurr_pos(listData.size());
                                        listData.add(map);
                                    }
                                    Supervision_task map = new Supervision_task();
                                    map.setId(dataJson1.getString("apply_id"));
                                    map.setTitle(dataJson1.getString("title"));
                                    map.setIs_year("0");
                                    map.setTerm(dataJson1.getString("v_rate")+"");
                                    map.setNum(dataJson1.getString("ks_nums"));
                                    map.setCurr_num(dataJson1.getString("y_nums"));
                                    map.setCurr_pos(listData.size());
                                    listData.add(map);
                                }
                                baseRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclelistview.setVisibility(View.VISIBLE);
            tranining2_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }
            recyclelistview.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.oversight_task_list);
        context = this;
        ButterKnife.bind(this);
        activityTitleName.setText(getIntent().getStringExtra("title"));
        initview();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVideos2();
    }

    public void initview(){
        baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(R.layout.item_oversight_task_list,listData){
            @Override
            public void convert(BaseViewHolder helper, Object item) {
                Supervision_task bean = (Supervision_task) item;
                if(bean.getIs_year().equals("1")){
                    //年
                    helper.setText(R.id.item_year, bean.getTitle());
                    helper.setGone(R.id.item_year, true);
                    helper.setGone(R.id._item_layout, false);
                    helper.setGone(R.id._item_view, false);
                    if (bean.getCurr_pos() == 0){
                        helper.setGone(R.id._item_view2, false);
                    }else{
                        helper.setGone(R.id._item_view2, true);
                    }
                }else{
                    //非年
                    helper.setText(R.id.item_title, bean.getTitle());
                    helper.setText(R.id.item_num, bean.getTerm());
                    if(bean.getCurr_num().equals(bean.getNum())){
                        helper.setTextColor(R.id.item_num, Color.parseColor("#ef8e10"));
                    }else{
                        helper.setTextColor(R.id.item_num, Color.parseColor("#999999"));
                    }
                    helper.setGone(R.id._item_layout, true);
                    helper.setGone(R.id.item_year, false);
                    helper.setGone(R.id._item_view, true);
                    helper.setGone(R.id._item_view2, false);

                }
            }
        };
        baseRecyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclelistview.setAdapter(baseRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//设置布局管理器
        recyclelistview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        baseRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(listData.get(position).getIs_year().equals("0")){
                    Intent intent = new Intent(context,Department_evaluation.class);
                    intent.putExtra("apply_id",listData.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    public void setVideos2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getSupDutyList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();

    }
}
