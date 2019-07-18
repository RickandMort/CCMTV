package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.os.Bundle;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * name：关于我们
 * author：Larry
 * data：2016/6/1.
 */
public class AboutOwnActivity extends BaseActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutown);
        context = this;
        super.findId();
        super.setActivity_title_name("关于我们");
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
