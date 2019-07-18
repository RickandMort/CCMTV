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

public class Evaluation_department_list3 extends BaseActivity {
    private Context context;

    @Bind(R.id.listView)
    ListView listView;
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

                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("id",dataJson1.getString("id") );
                                    map.put("department",dataJson1.getString("department") );
                                    map.put("evaluation",dataJson1.getString("evaluation") );
                                    listData.add(map);
                                }
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.evaluation_department_list2);
        context = this;
        ButterKnife.bind(this);
        findId();
        this.setActivity_title_name("科室评价");
        initDatas();
        initViews();
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

    private void initDatas() {
        for(int i = 0 ;i<10;i++){
            Map<String ,Object> map = new HashMap<>();
            map.put("id",i+"");
            map.put("department","本院带教（aaaa）"+i);
            map.put("evaluation","未评价");
            listData.add(map);
        }
    }

    private void initViews() {
        //增加头部
 /*       item_evaluation_department_list_top = (LinearLayout) View.inflate(this, R.layout.item_evaluation_department_list_top, null);
        listView.addHeaderView(item_evaluation_department_list_top);*/
        //增加底部
        item_the_evaluation_of_teaching_list_buttom = (LinearLayout) View.inflate(this, R.layout.item_evaluation_department_list2_buttom2, null);
        listView.addFooterView(item_the_evaluation_of_teaching_list_buttom);
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
                helper.setText(R.id._item_department,map.get("department").toString());
                helper.setText(R.id._item_evaluation,map.get("evaluation").toString());
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
                    obj.put("act", URLConfig.workFlowList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("审核列表", result);

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
