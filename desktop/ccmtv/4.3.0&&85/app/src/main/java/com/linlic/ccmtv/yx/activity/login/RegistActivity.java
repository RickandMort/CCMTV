package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
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

public class RegistActivity extends BaseActivity implements View.OnClickListener {

    private EditText login_phonenumber;
    private CheckBox login_checkbox;
    private TextView btn_get_verification_code, user_agreement;
    private Context context;
    private String phoneStr;
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
                btn_get_verification_code.setText(msg.what + getString(R.string.forget_sendcode_OK));
//                btn_get_verification_code.setBackgroundResource(R.drawable.sendyzm_wait);
//                btn_get_verification_code.setTextColor(Color.rgb(153, 153, 153));
                btn_get_verification_code.setClickable(false);
            } else {
                timer.cancel();
                btn_get_verification_code.setText(getString(R.string.regist_getcode_new));
//                btn_get_verification_code.setBackgroundResource(R.drawable.sendyzm);
//                btn_get_verification_code.setTextColor(Color.rgb(102, 203, 255));
                btn_get_verification_code.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        login_phonenumber = (EditText) findViewById(R.id.login_phonenumber);
        btn_get_verification_code = (TextView) findViewById(R.id.btn_get_verification_code);
        login_checkbox = (CheckBox) findViewById(R.id.login_checkbox);
        user_agreement = (TextView) findViewById(R.id.user_agreement);
        user_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        user_agreement.getPaint().setAntiAlias(true);//抗锯齿
        user_agreement.setOnClickListener(this);
        btn_get_verification_code.setOnClickListener(this);
    }

    private void initData() {
        //进入页面，清空手机号码
        login_phonenumber.setText("");
        user_agreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        login_phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (login_phonenumber.getText().toString().trim().length() == 11) {
                    btn_get_verification_code.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    btn_get_verification_code.setClickable(true);
                } else {
                    btn_get_verification_code.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    btn_get_verification_code.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //用户协议
            case R.id.user_agreement:
                startActivity(new Intent(context, AgreementActivity.class));
                break;
            //获取验证码
            case R.id.btn_get_verification_code:
                if (TextUtils.isEmpty(login_phonenumber.getText().toString())) {
                    Toast.makeText(context, "手机号码不能为空~", Toast.LENGTH_SHORT).show();
                    login_phonenumber.requestFocus();
                } else {
                    if (!validatePhoneNumber(login_phonenumber.getText().toString())) {
                        Toast.makeText(context, "请输入正确的手机号~", Toast.LENGTH_SHORT).show();
                    } else {
                        phoneStr = login_phonenumber.getText().toString();
                        if (login_checkbox.isChecked() == true) {
                            getVerificationCode(login_phonenumber.getText().toString());
                            /*Intent intent = new Intent(context,FillInVerificationCodeActivity.class);
                            intent.putExtra("phoneStr",phoneStr);
                            startActivity(intent);*/
                        } else {
                            Toast.makeText(context, "请勾选用户协议", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机号码是否合法
     */
    public static boolean validatePhoneNumber(String mobiles) {
//        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return mobiles.length() == 11?true:false;
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
                                    Intent intent = new Intent(context,FillInVerificationCodeActivity.class);
                                    intent.putExtra("phoneStr",phoneStr);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    btn_get_verification_code.setText("获取短信验证码");
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

}
