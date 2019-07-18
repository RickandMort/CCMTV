package com.linlic.ccmtv.yx.activity.my.feedback;

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
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
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

/**意见反馈首页
 * Created by tom on 2019/1/4.
 */

public class Feedback_Main extends BaseActivity {

    private Context context;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

    @Bind(R.id.listView)//日期img
            ListView listView;
    @Bind(R.id.new_feedback)//日期img
            LinearLayout new_feedback;
    @Bind(R.id.history_list)//日期img
            LinearLayout history_list;

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
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("status", dataJson1.getString("status"));
                                    map.put("sort", dataJson1.getString("sort"));
                                    listData.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.feedback_main);
        context = this;
        ButterKnife.bind(this);

        findId();
        initView();
        commonQuestionList();

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

        baseListAdapter = new BaseListAdapter(listView, listData, R.layout.item_feedbace_main) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item_text,  map.get("title").toString());
            }
        };
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = listData.get(position);
                Intent intent = new Intent(context,Feedback_details.class);
                intent.putExtra("id",map.get("id").toString());
                startActivity(intent);
            }
        });
        new_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MyYjfkActivity.class);
                startActivity(intent);
            }
        });
        history_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fastClick()){
                    Intent intent = new Intent(context,My_feedback.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void commonQuestionList() {
        if(SharedPreferencesTools.getUid(context).length()>0){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.commonQuestionList);
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
