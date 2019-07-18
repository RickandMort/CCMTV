package com.linlic.ccmtv.yx.activity.login;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;

public class RegisterActivity2 extends Activity implements View.OnClickListener {

    private Context context;
    private LinearLayout llQuickRegister;
    private LinearLayout llInputPassword;
    private EditText etInputPhoneNumber;
    private EditText etInputVertificationCode;
    private EditText etInputPassword;
    private EditText etInputPasswordAgain;
    private Button btnGetVertificationCode;
    private Button btnRegister;
    private Button btnConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        setContentView(R.layout.activity_register2);
        context = this;
        findId();
    }

    private void findId() {
        llQuickRegister = (LinearLayout) findViewById(R.id.id_ll_register2_quick_register);
        llInputPassword = (LinearLayout) findViewById(R.id.id_ll_register2_input_password);
        etInputPhoneNumber = (EditText) findViewById(R.id.id_et_register2_input_phone_number);
        etInputVertificationCode = (EditText) findViewById(R.id.id_et_register2_input_verification_code);
        etInputPassword = (EditText) findViewById(R.id.id_et_register2_input_password);
        etInputPasswordAgain = (EditText) findViewById(R.id.id_et_register2_input_password_again);
        btnGetVertificationCode = (Button) findViewById(R.id.id_btn_register2_get_verification_code);
        btnRegister = (Button) findViewById(R.id.id_btn_register2_to_register);
        btnConfirmPassword = (Button) findViewById(R.id.id_btn_register2_confirm_password);

        btnGetVertificationCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnConfirmPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_btn_register2_get_verification_code:
                break;
            case R.id.id_btn_register2_to_register:
                llQuickRegister.setVisibility(View.GONE);
                llInputPassword.setVisibility(View.VISIBLE);
                break;
            case R.id.id_btn_register2_confirm_password:
                llQuickRegister.setVisibility(View.VISIBLE);
                llInputPassword.setVisibility(View.GONE);
                break;
        }
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //该参数指布局能延伸到navigationbar，我们场景中不应加这个参数
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT); //设置navigationbar颜色为透明
        }
    }
}
