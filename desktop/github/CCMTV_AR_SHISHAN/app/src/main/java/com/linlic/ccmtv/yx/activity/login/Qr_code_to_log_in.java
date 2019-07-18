package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/1/11.
 */
public class Qr_code_to_log_in extends BaseActivity {
    Context context;
    private String token;
    private TextView activity_title_name;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            finish();
                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
//                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            finish();
                        } else {//失败
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_to_log_in);
        context = this;

        token = getIntent().getStringExtra("token");
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        setText();
    }

    public void setText() {
        activity_title_name.setText("确认登录");
    }

    public void qr_code(final View view) {
        MyProgressBarDialogTools.show(Qr_code_to_log_in.this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("action", URLConfig.confirm_login);
                    obj.put("token", token);
                    obj.put("confirm", view.getTag());
                    String result = HttpClientUtils.sendPost(Qr_code_to_log_in.this, URLConfig.QR_CCMTVAPP, obj.toString());

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
    public void back(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("action", URLConfig.confirm_login);
                    obj.put("token", token);
                    obj.put("confirm", 2);
                    String result = HttpClientUtils.sendPost(Qr_code_to_log_in.this, URLConfig.QR_CCMTVAPP, obj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
        super.back(view);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("action", URLConfig.confirm_login);
                        obj.put("token", token);
                        obj.put("confirm", 2);
                        String result = HttpClientUtils.sendPost(Qr_code_to_log_in.this, URLConfig.QR_CCMTVAPP, obj.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            };
            new Thread(runnable).start();
        }
        return super.onKeyDown(keyCode, event);
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
