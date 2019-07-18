package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONObject;

public class BindPhoneNumberActivity extends BaseActivity {

    private TextView title_name;
    private LinearLayout llTop;
    private TextView tvOldNumber;
    private EditText etNewNumber;
    private Button btnNext;
    private boolean isShowTop=false;
    private String oldNumberString;
    private String Str_mobphone;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone_number);
        context=this;
        findId();
        initData();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        llTop = (LinearLayout) findViewById(R.id.id_ll_bind_phone_top);
        tvOldNumber = (TextView) findViewById(R.id.id_tv_bind_phone_old_number);
        etNewNumber = (EditText) findViewById(R.id.id_et_bind_phone_new_number);
        btnNext = (Button) findViewById(R.id.id_btn_bind_phone_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Str_mobphone = etNewNumber.getText().toString();
                if (TextUtils.isEmpty(Str_mobphone)) {
                    Toast.makeText(context, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    etNewNumber.requestFocus();
                    return;
                }
                if (Str_mobphone.length() != 11) {
                    Toast.makeText(context, "您输入的号码有误", Toast.LENGTH_SHORT).show();
                    etNewNumber.requestFocus();
                    return;
                }
                getVerificationCode(Str_mobphone);
            }
        });
    }

    //获取短信验证码
    private void getVerificationCode(final String phoneStr) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("phone", phoneStr);
                    obj.put("act", URLConfig.updateTel);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("获取绑定手机验证码result11", result.toString());
                    final JSONObject object = new JSONObject(result);
                    if (object.getInt("status") == 1) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Intent intent=new Intent(BindPhoneNumberActivity.this,BindPhoneInputVrifyCodeActivity.class);
                                    intent.putExtra("phoneNumber",phoneStr);
                                    startActivity(intent);
                                    Toast.makeText(context, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
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
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }


    private void initData() {
        oldNumberString=getIntent().getStringExtra("Str_phonenum");
        tvOldNumber.setText("+86\u3000"+oldNumberString);
        if (getIntent().getStringExtra("is_bind").equals("yes")){
            title_name.setText("改绑手机号");
            llTop.setVisibility(View.VISIBLE);
            etNewNumber.setHint("请输入新手机号");
        }else {
            title_name.setText("绑定手机号");
            llTop.setVisibility(View.GONE);
            etNewNumber.setHint("请输入手机号");
        }
    }

    public void back(View view) {
        finish();
    }

}
