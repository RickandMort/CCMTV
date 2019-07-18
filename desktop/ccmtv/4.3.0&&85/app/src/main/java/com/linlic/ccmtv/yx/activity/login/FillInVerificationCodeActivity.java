package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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

public class FillInVerificationCodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView vc_phone_number, tv_timer;
    private Button btn_next_step;
    private EditText et1, et2, et3, et4;
    private String phoneStr;
    private String verificationCode;
    Context context;
    Timer timer;//计时器
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
//                        layout_regist.setVisibility(View.GONE);
//                        SharedPreferencesTools.saveUid(context, result.getString("uid"));
                    } else {//失败
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 500) {
                MyProgressBarDialogTools.hide();
                Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
            } else if (msg.what > 0 && msg.what <= 60) {//计时器逻辑
                tv_timer.setText(msg.what + getString(R.string.forget_sendcode_OK));
//                btn_get_verification_code.setBackgroundResource(R.drawable.sendyzm_wait);
//                btn_get_verification_code.setTextColor(Color.rgb(153, 153, 153));
                tv_timer.setClickable(false);
            } else if (msg.what == 201) {
                try {
                    JSONObject result = new JSONObject(msg.obj + "");
                    if (result.getInt("status") == 1) {//成功
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SetPasswordActivity.class);
                        intent.putExtra("phoneStr", phoneStr);
                        startActivity(intent);
                    } else {//失败
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MyProgressBarDialogTools.hide();
                }
            } else {
                timer.cancel();
                tv_timer.setText(getString(R.string.regist_getcode_new));
//                btn_get_verification_code.setBackgroundResource(R.drawable.sendyzm);
//                btn_get_verification_code.setTextColor(Color.rgb(102, 203, 255));
                tv_timer.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_verification_code);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        vc_phone_number = (TextView) findViewById(R.id.vc_phone_number);
        tv_timer = (TextView) findViewById(R.id.tv_timer);
        btn_next_step = (Button) findViewById(R.id.btn_next_step);
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);
        et4 = (EditText) findViewById(R.id.et4);

        tv_timer.setOnClickListener(this);
        btn_next_step.setOnClickListener(this);
    }

    private void initData() {
        phoneStr = getIntent().getStringExtra("phoneStr");
        vc_phone_number.setText(phoneStr);
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

//        getVerificationCode(phoneStr);
        et1.requestFocus();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et1.getText().toString())) {
                    et2.requestFocus();
                }
            }
        });
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et2.getText().toString())) {
                    et3.requestFocus();
                }
            }
        });
        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(et3.getText().toString())) {
                    et4.requestFocus();
                }
            }
        });
    }

    //左上角返回键点击事件
    public void back(View v) {
        finish();
    }

    //获取短信验证码
    private void getVerificationCode(final String phoneStr) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", phoneStr);
                    obj.put("act", URLConfig.sendMesg);
                    obj.put("source", URLConfig.uregister);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("result11", result.toString());
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
                                    tv_timer.setText("获取短信验证码");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_timer:
                getVerificationCode(phoneStr);
                break;
            case R.id.btn_next_step:
                //还要判断验证码，才能跳转
                boolean im1 = !TextUtils.isEmpty(et1.getText().toString());
                boolean im2 = !TextUtils.isEmpty(et2.getText().toString());
                boolean im3 = !TextUtils.isEmpty(et3.getText().toString());
                boolean im4 = !TextUtils.isEmpty(et4.getText().toString());
                if (im1 && im2 && im3 && im4) {
                    verificationCode = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString();
                    checkVerificationCode();
                } else {
                    Toast.makeText(context, "验证码不能为空~", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void checkVerificationCode() {
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.codecheck);
                    obj.put("verificationCode", verificationCode);
                    obj.put("phone", phoneStr);
//                    Log.e("resultobj", obj.toString());
                    String result = HttpClientUtils.sendPost(FillInVerificationCodeActivity.this, URLConfig.CCMTVAPP, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("result555", result.toString());
                    Message message = new Message();
                    message.what = 201;
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
