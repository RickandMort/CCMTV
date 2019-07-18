package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/20.
 */

public class Evaluation_department_list2 extends BaseActivity {
    private Context context;

    @Bind(R.id.listView)
    ListView listView;
    private TextView _item_the_average_scores,_item_the_number_of_bad_review,_item_msg;
    private LinearLayout item_evaluation_department_list_top,item_the_evaluation_of_teaching_list_buttom;

    JSONObject result, data;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
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
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                listData.clear();
                                _item_msg.setText(dataJson.getJSONObject("data").getString("alert_msg"));
                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("id",dataJson1.getString("id") );
                                    map.put("status",dataJson1.getString("status") );
                                    map.put("contribution",dataJson1.getString("contribution") );
                                    map.put("score",dataJson1.getString("score") );
                                    map.put("year",dataJson1.getString("year") );
                                    map.put("month",dataJson1.getString("month") );
                                    map.put("username",dataJson1.getString("username") );
                                    map.put("realname",dataJson1.getString("realname") );
                                    if (!dataJson1.isNull("pj_status")) {
                                        map.put("pj_status", dataJson1.getString("pj_status"));
                                    }
                                    listData.add(map);
                                }

//                                if(listData.size()<1){
//                                    layout_nodata.setVisibility(View.VISIBLE);
//                                }else {
//                                    layout_nodata.setVisibility(View.GONE);
//                                }

                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }
    };
    private String fid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.evaluation_department_list2);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        this.setActivity_title_name("科室评价");
        initViews();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
        super.onPause();
    }

    private void initViews() {
        //增加头部
 /*       item_evaluation_department_list_top = (LinearLayout) View.inflate(this, R.layout.item_evaluation_department_list_top, null);
        listView.addHeaderView(item_evaluation_department_list_top);*/
        //增加底部
        item_the_evaluation_of_teaching_list_buttom = (LinearLayout) View.inflate(this, R.layout.item_evaluation_department_list2_buttom, null);
        _item_the_average_scores = item_the_evaluation_of_teaching_list_buttom.findViewById(R.id._item_the_average_scores);
        _item_the_number_of_bad_review = item_the_evaluation_of_teaching_list_buttom.findViewById(R.id._item_the_number_of_bad_review);
        _item_msg = item_the_evaluation_of_teaching_list_buttom.findViewById(R.id._item_msg);

        _item_the_average_scores.setText(getIntent().getStringExtra("v_score"));
        _item_the_number_of_bad_review.setText(getIntent().getStringExtra("c_num"));

        listView.addFooterView(item_the_evaluation_of_teaching_list_buttom,null,false);
        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_evaluation_department_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_department,map.get("realname").toString()+"("+map.get("username").toString()+")");
                helper.setText(R.id._item_evaluation,map.get("score").toString());
                helper.setText(R.id._item_id,map.get("id").toString());
            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new  casesharing_listListener());
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Intent intent = null;

                    intent = new Intent(context, Evaluation_in_detail.class);
            intent.putExtra("fid", fid);
            intent.putExtra("detailid", listData.get(arg2).get("id").toString());
            if(intent!=null){
                startActivity(intent);
            }

        }

    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allAppraiseUserList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", getIntent().getStringExtra("fid"));
                    obj.put("gps_ids", getIntent().getStringExtra("gps_ids"));

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("基地或科室查看评价列表", result);

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
