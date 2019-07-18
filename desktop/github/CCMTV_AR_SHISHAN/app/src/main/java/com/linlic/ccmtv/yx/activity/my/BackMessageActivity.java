package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;


/**
 * 回复邮件（写新消息）
 * Created by yu on 2016/3/17.
 */
public class BackMessageActivity extends BaseActivity {
    TextView activity_title_name;
    EditText edit_get_name, edit_title, edit_content;
    Context context;
    String uid, Str_pusername, Str_username;
    String resouce = "receiver";
    String back;
    private static long lastClickTime;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MyMessageActivity.class);
                            intent.putExtra("resouce", "success");
                            startActivity(intent);
                            BackMessageActivity.this.finish();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_backmessage);
        context = this;
        resouce = getIntent().getStringExtra("resouce");
        back = getIntent().getStringExtra("back");
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        edit_get_name = (EditText) findViewById(R.id.edit_get_name);
        edit_get_name.setSelection(edit_get_name.getText().length());
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_content = (EditText) findViewById(R.id.edit_content);
        activity_title_name.setText("写新消息");
        Str_pusername = getIntent().getStringExtra("Str_pusername");
        Str_username = SharedPreferencesTools.getUserName(context);
        edit_get_name.setText(Str_pusername);
    }

    public void send(View view) {
        if (isFastDoubleClick()) {
            return;
        }
        //判断是否登录
        if (SharedPreferencesTools.getUid(context).equals(""))
            return;
        uid = SharedPreferencesTools.getUid(context);

        if (TextUtils.isEmpty(edit_get_name.getText())) {
            Toast.makeText(context, R.string.getname_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edit_title.getText())) {
            Toast.makeText(context, R.string.title_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edit_content.getText())) {
            Toast.makeText(context, R.string.content_null, Toast.LENGTH_SHORT).show();
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("musername", edit_get_name.getText());  //收件人
                    obj.put("pusername", Str_username);
                    obj.put("title", edit_title.getText());
                    obj.put("content", edit_content.getText());
                    obj.put("act", URLConfig.sendMessage);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
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

    public void back(View view) {
        backs();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理逻辑
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backs();
        }
        return true;
    }

    public void backs() {
        if ("dissmiss".equals(back) || "backfromfollow".equals(back)) {
            this.finish();
        } else {
            Intent intent = new Intent(context, MyMessageActivity.class);
            intent.putExtra("resouce", resouce);
            startActivity(intent);
            this.finish();
        }
    }

    //是否是多次点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
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
