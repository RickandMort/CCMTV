package com.linlic.ccmtv.yx.activity.my.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/**
 * Created by tom on 2019/1/4.
 */

public class My_feedback extends BaseActivity {

    private Context context;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    @Bind(R.id.management_nodata)
    NodataEmptyLayout management_nodata;
    @Bind(R.id.listView)//日期img
            ListView listView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dateJson = jsonObject.getJSONArray("data") ;
                            for (int i = 0; i < dateJson.length(); i++) {
                                JSONObject dataJson1 = dateJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataJson1.getString("id"));
                                map.put("reply", dataJson1.getString("reply"));
                                map.put("content", dataJson1.getString("content"));
                                map.put("post_time", dataJson1.getString("post_time"));
                                map.put("is_reply", dataJson1.getString("is_reply"));
                                listData.add(map);
                            }
                            baseListAdapter.notifyDataSetChanged();

//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            management_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                management_nodata.setNetErrorIcon();
            } else {
                management_nodata.setLastEmptyIcon();
            }

            listView.setVisibility(View.GONE);
            management_nodata.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_feedback);
        context = this;
        ButterKnife.bind(this);

        findId();
        initView();
        myRefundList();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Refund.html";
        super.onPause();
    }

    public void initView() {

        baseListAdapter = new BaseListAdapter(listView, listData, R.layout.item_my_feedback) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item_title,  map.get("content").toString());
                helper.setText(R.id._item_time,  map.get("post_time").toString());
                if( map.get("is_reply").toString().equals("1")){
                    helper.setText(R.id._item_status,  "已回复");
                }else{
                    helper.setText(R.id._item_status,  "待回复");
                }

            }
        };
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = listData.get(position);
                Intent intent = new Intent(context,My_Feedback_details.class);
                intent.putExtra("id",map.get("id").toString());
                startActivity(intent);
            }
        });


    }

    private void myRefundList() {
        if(SharedPreferencesTools.getUid(context).length()>0){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.myRefundList);
                        obj.put("uid",  SharedPreferencesTools.getUid(context));
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("会议科室与时间数据：", result);

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
}
