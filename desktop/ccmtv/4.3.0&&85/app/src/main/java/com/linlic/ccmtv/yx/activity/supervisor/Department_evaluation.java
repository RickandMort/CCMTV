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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Department_evaluation_Bean;
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
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**科室评估
 * Created by Administrator on 2019/5/7.
 */

public class Department_evaluation extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    @Bind(R.id.recyclelistview)
    RecyclerView recyclelistview;
    @Bind(R.id.popwindow1)
    LinearLayout popwindow1;
    @Bind(R.id.status_01)
    TextView status_01;
    @Bind(R.id.status_02)
    TextView status_02;
    @Bind(R.id.status_03)
    TextView status_03;
    private Context context;
    public static boolean is_new = false;
    public static List<Department_evaluation_Bean> listData = new ArrayList<>();
    private List<Department_evaluation_Bean> listData2 = new ArrayList<>();
    BaseRecyclerViewAdapter baseRecyclerViewAdapter ;
    private String apply_id = "";
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
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Department_evaluation_Bean s2 = new Department_evaluation_Bean();
                                    s2.setId(dataJson1.getString("hospital_kid"));
                                    s2.setTitle(dataJson1.getString("ksname"));
                                    s2.setCurr_pos(listData.size());
                                    s2.setScore(isNumeric(dataJson1.getString("scores"))?dataJson1.getString("scores")+"分":dataJson1.getString("scores"));
                                    s2.setStatus(dataJson1.getString("status") );
                                    listData.add(s2);

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
    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }
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
        setContentView(R.layout.department_evaluation);
        context = this;
        ButterKnife.bind(this);
//        activityTitleName.setText(getIntent().getStringExtra("name"));
        apply_id = getIntent().getStringExtra("apply_id");
        initview();
        setVideos2();
    }



    public void initview(){
        is_new = false;
        listData.clear();
        baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(R.layout.item_department_evaluation,listData){
            @Override
            public void convert(BaseViewHolder helper, Object item) {
                Department_evaluation_Bean bean = (Department_evaluation_Bean) item;
                helper.setText(R.id.item_title, bean.getTitle());
                helper.setText(R.id.item_score, bean.getScore());
                if(bean.getStatus().equals("2")||bean.getStatus().equals("3")){
                    helper.setTextColor(R.id.item_score, Color.parseColor("#999999"));
                }else{
                    helper.setTextColor(R.id.item_score, Color.parseColor("#3897F9"));
                }
                if(bean.getCurr_pos()%2 ==0){
                    helper.setBackgroundRes(R.id._item_view,R.drawable.anniu11);
                }else{
                    helper.setBackgroundRes(R.id._item_view,R.drawable.anniu3);
                }
            }
        };
        baseRecyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclelistview.setAdapter(baseRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//设置布局管理器
        recyclelistview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        baseRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(listData.get(position).getStatus().equals("2")){
                    Toast.makeText(context
                            , "任务未开始，请在时间开始后进行评估～",
                            Toast.LENGTH_SHORT).show();
                }else if(listData.get(position).getStatus().equals("3")){
                    Toast.makeText(context
                            , "任务已结束，不能进行评估",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context,Department_score.class);
                    intent.putExtra("apply_id",apply_id);
                    intent.putExtra("hospital_kid",listData.get(position).getId());
                    intent.putExtra("curr_pos",listData.get(position).getCurr_pos()+"");
                    startActivity(intent);
                }

            }
        });
        status_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindow1.setVisibility(View.GONE);
                listData2.clear();
                baseRecyclerViewAdapter.setNewData(listData);
                baseRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        status_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindow1.setVisibility(View.GONE);
                listData2.clear();
                for (int i = 0;i<listData.size();i++){
                    if(listData.get(i).getStatus().equals("1")){
                        listData2.add(listData.get(i));
                    }
                }
                baseRecyclerViewAdapter.setNewData(listData2);
                baseRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        status_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popwindow1.setVisibility(View.GONE);
                listData2.clear();
                for (int i = 0;i<listData.size();i++){
                    if(!listData.get(i).getStatus().equals("1")){
                        listData2.add(listData.get(i));
                    }
                }
                baseRecyclerViewAdapter.setNewData(listData2);
                baseRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public  void screen(View view){
        if(popwindow1.getVisibility() == View.VISIBLE){
            popwindow1.setVisibility(View.GONE);
        }else{
            popwindow1.setVisibility(View.VISIBLE);
        }
    }
    public  void ishide(View view){

            popwindow1.setVisibility(View.GONE);

    }
    public void setVideos2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getKsEvalue);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("apply_id", apply_id);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(is_new){
            is_new = false;
            baseRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
