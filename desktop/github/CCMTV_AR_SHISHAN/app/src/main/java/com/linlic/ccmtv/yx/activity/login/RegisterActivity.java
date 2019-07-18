package com.linlic.ccmtv.yx.activity.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * name：注册
 * <p/>
 * author: Mr.song
 * 时间：2016-2-19 下午7:03:35
 *
 * @author Administrator
 */
public class RegisterActivity extends BaseActivity {
    private EditText user;// 账号
    private EditText password;// 密码
    private EditText repeatPassword;// 重复密码
    private EditText phone;// 重复密码
    private EditText verificationCode;// 验证码
    private EditText edit_regist_invitation_code;//邀请码
    private TextView activity_title_name;//顶部title
    private Button sendCode;//发送验证码
    private CheckBox CheckBoxxiey;//用户协议
    Timer timer;//计时器
    LinearLayout layout_regist, layout_regist_success;
    Context context;
    private String userAccount, pass;
    private ImageView iv_tip;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {
                MyProgressBarDialogTools.hide();
                try {
                    JSONObject result = new JSONObject(msg.obj + "");
                    if (result.getInt("status") == 1) {//成功
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        //finish();
                        layout_regist.setVisibility(View.GONE);
                        SharedPreferencesTools.saveUid(context, result.getString("uid"));
                        SharedPreferencesTools.saveUserName(context, userAccount);
                        SharedPreferencesTools.savePassword(context, pass);
                    } else {//失败
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyProgressBarDialogTools.hide();
                }
            } else if (msg.what == 500) {
                MyProgressBarDialogTools.hide();
                Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
            } else if (msg.what > 0 && msg.what <= 60) {//计时器逻辑
                sendCode.setText(msg.what + getString(R.string.forget_sendcode_OK));
                sendCode.setBackgroundResource(R.drawable.sendyzm_wait);
                sendCode.setTextColor(Color.rgb(153, 153, 153));
                sendCode.setClickable(false);
            } else {
                timer.cancel();
                sendCode.setText(getString(R.string.regist_getcode));
                sendCode.setBackgroundResource(R.drawable.sendyzm);
                sendCode.setTextColor(Color.rgb(102, 203, 255));
                sendCode.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        findViewById();

        activity_title_name.setText(R.string.regist_title_name);
    }

    private void findViewById() {
        user = (EditText) findViewById(R.id.regist_user);
        edit_regist_invitation_code = (EditText) findViewById(R.id.edit_regist_invitation_code);
        password = (EditText) findViewById(R.id.regist_password);
        repeatPassword = (EditText) findViewById(R.id.regist_repeatPassword);
        phone = (EditText) findViewById(R.id.regist_phone);
        verificationCode = (EditText) findViewById(R.id.regist_verificationCode);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        sendCode = (Button) findViewById(R.id.regist_sendCode);
        CheckBoxxiey = (CheckBox) findViewById(R.id.CheckBoxxiey);
        layout_regist = (LinearLayout) findViewById(R.id.layout_regist);
        iv_tip = (ImageView) findViewById(R.id.iv_tip);                                         //邀请码提示  暂时隐藏掉
        iv_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(RegisterActivity.this, R.style.ActionSheetDialogStyle);
                View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_tip_yaoqingma, null);
                dialog.setContentView(view);
                Button btn_know = (Button) view.findViewById(R.id.btn_know);
                btn_know.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * name：发送验证码
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午1:06:20
     *
     * @param view
     */
    public void sendCode(View view) {
        final String phoneStr = phone.getText().toString();
        if (TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(context, R.string.regist_hint1, Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }
        if (phoneStr.length() != 11) {
            Toast.makeText(context, R.string.regist_hint2, Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", phoneStr);
                    obj.put("act", URLConfig.sendMesg);
                    obj.put("source", URLConfig.uregister);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    final JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(context, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        //发送成功后再开启倒计时
                        //添加计时器
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            int a = 60;

                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = a--;
                                handler.sendMessage(message);
                            }
                        }, 0, 1000);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(context, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name：注册
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午1:31:59
     *
     * @param view
     */
    public void register(View view) {
        // handler.sendEmptyMessage(200);
        userAccount = user.getText().toString();
        pass = password.getText().toString();
        final String repeatPass = repeatPassword.getText().toString();
        final String phoneStr = phone.getText().toString();
        final String yzmCode = verificationCode.getText().toString();
        final String yqCode = edit_regist_invitation_code.getText().toString();

        if (TextUtils.isEmpty(userAccount)) {
            Toast.makeText(context, R.string.login_toast_user, Toast.LENGTH_SHORT).show();
            user.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(context, R.string.login_toast_pass, Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(repeatPass)) {
            Toast.makeText(context, R.string.regist_toast_repeatpass, Toast.LENGTH_SHORT).show();
            repeatPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(context, R.string.regist_hint1, Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(yzmCode)) {
            Toast.makeText(context, R.string.forget_toast_code, Toast.LENGTH_SHORT).show();
            verificationCode.requestFocus();
            return;
        }
       /* if (TextUtils.isEmpty(yqCode)) {
            Toast.makeText(context, R.string.invitation_code, Toast.LENGTH_SHORT).show();
            edit_regist_invitation_code.requestFocus();
            return;
        }*/
        if (!pass.equals(repeatPass)) {
            Toast.makeText(context, R.string.regist_toast_pass_repeatpass, Toast.LENGTH_SHORT).show();
            repeatPassword.requestFocus();
            return;
        }
        if (CheckBoxxiey.isChecked() == false) {
            Toast.makeText(context, R.string.regist_xieyi, Toast.LENGTH_SHORT).show();
            return;
        }
        //showCustomLoading();
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("userAccount", userAccount);
                    object.put("password", pass);
                    object.put("rpassword", repeatPass);
                    object.put("phone", phoneStr);
                    object.put("verificationCode", yzmCode);
                    object.put("invite_code", yqCode);
                    object.put("act", URLConfig.uregister);
                    object.put("source", "app-android");
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 200;
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

    /**
     * name：跳转到用户协议页
     * <p/>
     * author: Mr.song
     * 时间：2016-2-22 下午1:31:59
     *
     * @param view
     */
    public void startXieyi(View view) {
        startActivity(new Intent(context, AgreementActivity.class));
    }

    /**
     * name：跳过---暂不完善
     * author：Larry
     * data：2016/3/28 11:19
     */
    public void jump(View view) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("type", "register");
        startActivity(intent);
        this.finish();
    }

    /**
     * name：去完善
     * author：Larry
     * data：2016/3/28 11:21
     */
    public void to_perfect(View view) {
        Intent intent = new Intent(context, MyProfile.class);
        intent.putExtra("source", "register");
        startActivity(intent);
        this.finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }


}
