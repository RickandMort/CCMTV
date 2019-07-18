package com.linlic.ccmtv.yx.activity.hospital_training;

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

/**
 * Created by Administrator on 2017/12/26. 医院培训入口
 */

public class Hospital_training_entrance extends BaseActivity {
    private Context context;
    private ListView list;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private int position = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            setActivity_title_name(jsonObject.getString("index_title"));
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("img", customJson.getString("img"));
                                map.put("type", customJson.getString("type"));
                                map.put("type_name", customJson.getString("type_name"));
                                data.add(map);
                            }
                        } else {
                            Toast.makeText(Hospital_training_entrance.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            Intent intent = new Intent(context, Hospital_training_main.class);
                            intent.putExtra("title", data.get(position).get("type_name").toString());
                            intent.putExtra("title_type", data.get(position).get("type").toString());
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(Hospital_training_entrance.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout hosptial_training_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            list.setVisibility(View.VISIBLE);
            hosptial_training_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                hosptial_training_nodata.setNetErrorIcon();
            } else {
                hosptial_training_nodata.setLastEmptyIcon();
            }
            list.setVisibility(View.GONE);
            hosptial_training_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hospital_training_entrance);
        context = this;
        findId();
        initdata();
        setVideos();
    }

    @Override
    protected void onDestroy() {
//        LogUtil.e("结束", "Hospital_training_entrance 页面关闭");
        super.onDestroy();
    }

    @Override
    public void findId() {
        super.findId();
        list = (ListView) findViewById(R.id.list);
        hosptial_training_nodata = (NodataEmptyLayout) findViewById(R.id.hosptial_training_nodata);
    }

    public void initdata() {
        baseListAdapter = new BaseListAdapter(list, data, R.layout.hospital_training_entrance_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.text_type, ((Map) item).get("type") + "");
                helper.setText(R.id.text_name, ((Map) item).get("type_name") + "");
                helper.setImageBitmaps(R.id.img, ((Map) item).get("img") + "");
            }
        };
        list.setAdapter(baseListAdapter);
        // listview点击事件
        list.setOnItemClickListener(new casesharing_listListener());
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            position = arg2;
            if(SharedPreferencesTools.getUid(context).equals("")){

            }else {
              checkYyUser();
            }

        }
    }

    public void setVideos() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yyindex);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-首页数据", result);
                    MyProgressBarDialogTools.hide();

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

    public void checkYyUser() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.checkYyUser);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-验证权限", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 2;
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
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/welcome/index.html";
        super.onPause();
    }

}
