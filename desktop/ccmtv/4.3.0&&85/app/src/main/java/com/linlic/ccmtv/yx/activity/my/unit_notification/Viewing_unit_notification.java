package com.linlic.ccmtv.yx.activity.my.unit_notification;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by tom on 2017/9/20.
 * 公告详情
 */
public class Viewing_unit_notification extends BaseActivity implements PlatformActionListener {
    private Context context;
    private String dataurl;
    private TextView viewing_tilte, viewing_time;
    private WebView viewing_text;
    private String nid;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONObject json = jsonObjects.getJSONObject("data");
                            viewing_tilte.setText(json.getString("title"));
//                            viewing_text.setText(Html.fromHtml(json.getString("content")));
                            //  加载、并显示HTML代码
                            viewing_text.loadDataWithBaseURL(null, json.getString("content") + "<script>  document.body.style.lineHeight = 1.5< /script> \\n< /html>", "text/html", "utf-8", null);
                            viewing_time.setText(json.getString("createtime"));
                             /*解析题型列表end*/
                        } else {
                            Toast.makeText(Viewing_unit_notification.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.viewing_unit_notification);
        context = this;
        findId();
        initdata();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        viewing_tilte = (TextView) findViewById(R.id.viewing_tilte);
        viewing_time = (TextView) findViewById(R.id.viewing_time);
        viewing_text = (WebView) findViewById(R.id.viewing_text);
    }

    public void initdata() {
        nid = getIntent().getStringExtra("nid");
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getNoticeInfoNew);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("nid", nid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());

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

    /*
     * 分享视频
     * */
    public void ShareVideo(View view) {
       /* dataurl = "http://www.ccmtv.cn/video/" + aid + "/" + aid + ".html";*/
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", uid);
                        object.put("act", URLConfig.videoShare);
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());

                        Message message = new Message();
                        message.what = 10;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
