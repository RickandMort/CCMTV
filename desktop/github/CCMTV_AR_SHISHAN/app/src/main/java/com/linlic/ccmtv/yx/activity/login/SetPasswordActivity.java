package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

public class SetPasswordActivity extends BaseActivity {

    private EditText sp_ed1, sp_ed2;
    private Button regist_finish;
    Context context;
    private String password, rpassword, phoneStr, uid;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(context, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            uid = object.getString("uid");
                            SharedPreferencesTools.saveUid(context, uid);
                            SharedPreferencesTools.saveUserName(context, phoneStr);
                            SharedPreferencesTools.savePassword(context, password);
//                            SharedPreferencesTools.saveUserName(context, "13691459811");
                            Intent intent = new Intent(context, PerfectInformationActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(SetPasswordActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        sp_ed1 = (EditText) findViewById(R.id.sp_ed1);
        sp_ed2 = (EditText) findViewById(R.id.sp_ed2);
        regist_finish = (Button) findViewById(R.id.regist_finish);
    }

    private void initData() {
        phoneStr = getIntent().getStringExtra("phoneStr");
        regist_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePass(sp_ed1.getText().toString())) {
                    Toast.makeText(context, "密码长度应在6-16位~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sp_ed1.getText().toString())) {
                    Toast.makeText(context, "密码不能为空~", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sp_ed2.getText().toString())) {
                    Toast.makeText(context, "重复密码不能为空~", Toast.LENGTH_SHORT).show();
                } else if (!sp_ed1.getText().toString().equals(sp_ed2.getText().toString())) {
                    Toast.makeText(context, "两次输入的密码不一致~", Toast.LENGTH_SHORT).show();
                } else {
                    password = sp_ed1.getText().toString();
                    rpassword = sp_ed2.getText().toString();
                    RegistFinish();
                }
            }
        });
    }

    //左上角返回键点击事件
    public void back(View v) {
        finish();
    }

    /**
     * 验证密码是否合法　６－１６位
     */
    public static boolean validatePass(String password) {
        return password.length() >= 6 && password.length() <= 16;
    }

    public void RegistFinish() {
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.passcheck);
                    obj.put("rpassword", rpassword);
                    obj.put("password", password);
                    obj.put("phone", phoneStr);
//                    Log.e("resultobj", obj.toString());
                    String result = HttpClientUtils.sendPost(SetPasswordActivity.this, URLConfig.CCMTVAPP, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("result555", result.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
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
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }
}
