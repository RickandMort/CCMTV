package com.linlic.ccmtv.yx.activity.home.ar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.service.DownloadARImgService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：AR主页
 * author：Larry
 * data：2016/8/18.
 */
public class ARmainActivity extends BaseActivity {
    private SimpleAdapter adapter;
    private ListView ar_listview;
    private List<Map<String, Object>> data = new ArrayList<>();
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                JSONObject result = new JSONObject(msg.obj + "");
                if (result.getInt("status") == 1) {//成功
                    JSONArray dataArray = result
                            .getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject object = dataArray.getJSONObject(i);
                        String picurl = URLDecoder.decode(object.getString("picurl"), "UTF-8");
                        String name = picurl.substring(picurl.lastIndexOf("/") + 1, picurl.length() - 4);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("picurl", picurl);
                        map.put("name", name);
                        map.put("aid", object.getString("aid"));
                        map.put("smvp", new JSONObject(object.getString("smvp")).get("fluentFile"));
                        data.add(map);
                    }
                    adapter.notifyDataSetChanged();
                } else {//失败
                    Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, R.string.post_hint3, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_armain);
        context = this;

        super.findId();
        super.setActivity_title_name("AR主页");
        ar_listview = (ListView) findViewById(R.id.ar_listview);

        getdata();
        adapter = new SimpleAdapter(this, data, R.layout.acitvity_armain_item,
                new String[]{"picurl", "name", "smvp"},
                new int[]{R.id.armain_img, R.id.armain_text, R.id.armain_smvp});
        ar_listview.setAdapter(adapter);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView) {
                    Glide.with(context).load(data.toString()).into((ImageView) view);
                    return true;
                } else {
                    return false;
                }
            }
        });
        ar_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,data.get(position).get("aid")+"",Toast.LENGTH_SHORT).show();
                //开启下载AR图片服务
                Intent intent = new Intent(context, DownloadARImgService.class);
                intent.putExtra("picurl",data.get(position).get("picurl").toString());
                startService(intent);
            }
        });
    }

    public void getdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doARData);
                    String res = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = res.toString();
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(context, R.string.post_hint3, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
