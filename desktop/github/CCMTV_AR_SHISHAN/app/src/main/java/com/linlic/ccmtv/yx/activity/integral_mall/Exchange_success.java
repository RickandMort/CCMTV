package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/11/28.
 */

public class Exchange_success extends BaseActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exchange_success);
        context = this;
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
