package com.linlic.ccmtv.yx.kzbf;

import android.os.Bundle;
import android.view.View;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * 签署协议界面
 */
public class ProtocolActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
    }

    public void back(View v) {
        finish();
    }
}
