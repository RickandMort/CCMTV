package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

/**
 * name：修改密码
 * author：Larry
 * data：2016/4/19 14:04
 */
public class MyChangePassActivity extends BaseActivity {
    String uid,username;
    Context context;
    EditText edit_oldpass, edit_newpass, edit_confirmpass;
    /**
     * name：hander
     * author：Larry
     * data：2016/3/26 15:39
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            Toast.makeText(context, "修改密码成功", Toast.LENGTH_SHORT).show();
                            SharedPreferencesTools.saveUid(context, "");
                            SharedPreferencesTools.saveUserName(context, "");
                            SharedPreferencesTools.savePassword(context, "");
                            startActivity(new Intent(context, LoginActivity.class));
                            finish();
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
        setContentView(R.layout.activity_my_change_pass);
        context = this;
        findId();
        initData();
        setText();
    }

    public void initData() {
        uid = SharedPreferencesTools.getUid(context);
        if ("".equals(uid) || uid == null) {
            return;
        }
        username = SharedPreferencesTools.getUserName(context);
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

    public void findId() {
        super.findId();
        edit_oldpass = (EditText) findViewById(R.id.edit_oldpass);
        edit_newpass = (EditText) findViewById(R.id.edit_newpass);
        edit_confirmpass = (EditText) findViewById(R.id.edit_confirmpass);
    }

    public void setText() {
        super.setActivity_title_name("修改密码");
    }

    public void ChangePass(View view) {
        final String oldpass = edit_oldpass.getText().toString();
        if (TextUtils.isEmpty(oldpass)) {
            Toast.makeText(context, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String newpass = edit_newpass.getText().toString();
        if (TextUtils.isEmpty(newpass)) {
            Toast.makeText(context, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final String confirmpass = edit_confirmpass.getText().toString();
        if (TextUtils.isEmpty(confirmpass)) {
            Toast.makeText(context, "确认密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newpass.equals(confirmpass)){
            Toast.makeText(context, "两次新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("userAccount", username);
                    obj.put("password", oldpass);
                    obj.put("newpassword", newpass);
                    obj.put("cnewpassword", confirmpass);
                    obj.put("act", URLConfig.modifyPasswordApp);
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

}
