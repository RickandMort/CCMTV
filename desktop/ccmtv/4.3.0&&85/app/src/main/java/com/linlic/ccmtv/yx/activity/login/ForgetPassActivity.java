package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * name：忘记密码
 * <p/>
 * author: Mr.song
 * 时间：2016-2-22 下午2:19:58
 *
 * @author Administrator
 */
public class ForgetPassActivity extends BaseActivity {

    private TextView activity_title_name;//顶部title
    private EditText forget_phone;//手机号
    private EditText forget_code;//验证码
    private EditText forget_pass;//新密码
    private TextView sendCode;//发送验证码
    private Button submit_;
    Timer timer;//计时器
    Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {
                try {
                    JSONObject result = new JSONObject(msg.obj + "");
                    if (result.getInt("status") == 1) {//成功
                        Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {//失败
                        Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MyProgressBarDialogTools.hide();
                }
            } else if (msg.what == 500) {
                MyProgressBarDialogTools.hide();
                Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
            } else if (msg.what > 0 && msg.what <= 60) {//计时器逻辑
                sendCode.setText(msg.what + getString(R.string.forget_sendcode_OK));
                sendCode.setTextColor(Color.parseColor("#DCDCDC"));
                sendCode.setClickable(false);
            } else {
                timer.cancel();
                sendCode.setText(getString(R.string.regist_getcode));
                sendCode.setTextColor(Color.parseColor("#5578F6"));
                sendCode.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        context = this;
        findViewById();
        activity_title_name.setText(R.string.forget_title_name);

    }

    private void findViewById() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        forget_phone = (EditText) findViewById(R.id.forget_phone);
        forget_code = (EditText) findViewById(R.id.forget_code);
        forget_pass = (EditText) findViewById(R.id.forget_pass);
        sendCode = (TextView) findViewById(R.id.forget_sendCode);
        submit_ = (Button) findViewById(R.id.submit_);

        forget_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听

                if (forget_phone.getText().toString().trim().length() > 0 &&forget_code.getText().toString().trim().length() > 0&&forget_pass.getText().toString().trim().length() > 0) {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    submit_.setClickable(true);
                } else {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    submit_.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });
        forget_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听

                if (forget_phone.getText().toString().trim().length() > 0 &&forget_code.getText().toString().trim().length() > 0&&forget_pass.getText().toString().trim().length() > 0) {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    submit_.setClickable(true);
                } else {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    submit_.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });
        forget_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听

                if (forget_phone.getText().toString().trim().length() > 0 &&forget_code.getText().toString().trim().length() > 0&&forget_pass.getText().toString().trim().length() > 0) {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    submit_.setClickable(true);
                } else {
                    submit_.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    submit_.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });
    }

    /**
     * name：发送验证码
     * <p/>
     * author: Mr.song
     * 时间：2016-2-23 下午4:40:58
     *
     * @param view
     */
    public void sendCode(View view) {
        final String phone = forget_phone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), R.string.forget_toast_phone, Toast.LENGTH_SHORT).show();
            forget_phone.requestFocus();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(getApplicationContext(), R.string.regist_hint2, Toast.LENGTH_SHORT).show();
            forget_phone.requestFocus();
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", phone);
                    obj.put("act", URLConfig.sendMesg);
                    obj.put("source", URLConfig.findPassword);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    final JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(getApplicationContext(), object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
     * name：找回密码
     * <p/>
     * author: Mr.song
     * 时间：2016-2-23 下午4:40:58
     *
     * @param view
     */
    public void forgetPass(View view) {
        final String phone = forget_phone.getText().toString();
        final String code = forget_code.getText().toString();
        final String pass = forget_pass.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), R.string.forget_toast_phone, Toast.LENGTH_SHORT).show();
            forget_phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(getApplicationContext(), R.string.forget_toast_code, Toast.LENGTH_SHORT).show();
            forget_code.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), R.string.forget_toast_pass, Toast.LENGTH_SHORT).show();
            forget_pass.requestFocus();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(getApplicationContext(), R.string.regist_hint2, Toast.LENGTH_SHORT).show();
            forget_phone.requestFocus();
            return;
        }
        if (code.length() != 4) {
            Toast.makeText(getApplicationContext(), R.string.forget_toast_code_error, Toast.LENGTH_SHORT).show();
            forget_code.requestFocus();
            return;
        }
        //showCustomLoading();
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", phone);
                    obj.put("verificationCode", code);
                    obj.put("password", pass);
                    obj.put("act", URLConfig.findPassword);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
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
