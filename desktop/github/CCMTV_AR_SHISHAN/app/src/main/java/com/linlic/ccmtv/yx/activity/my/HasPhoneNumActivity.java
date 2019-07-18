package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * Created by yu on 2016/3/14.
 */
public class HasPhoneNumActivity extends BaseActivity {
    TextView tv_show_renzheng;
    TextView activity_title_name;
    String Str_phonenum;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hasphone);
        context = this;
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        activity_title_name.setText("手机认证");
        Str_phonenum = getIntent().getStringExtra("Str_phonenum");
        tv_show_renzheng = (TextView) findViewById(R.id.tv_show_renzheng);
        tv_show_renzheng.setText("您的手机号码:"+'\"'+Str_phonenum+'\"');
    }

    public void phonechange(View view){
        Intent intent = new Intent(context,MyPhonerzActivity.class);
        intent.putExtra("type","has");
        startActivity(intent);
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
