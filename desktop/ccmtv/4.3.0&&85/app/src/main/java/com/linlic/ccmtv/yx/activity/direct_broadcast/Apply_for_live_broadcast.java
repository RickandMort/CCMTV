package com.linlic.ccmtv.yx.activity.direct_broadcast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.linlic.ccmtv.yx.R;

/**
 * Created by Administrator on 2018/3/27.
 */

public class Apply_for_live_broadcast extends Activity{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_broadcast_main);
        context = this;

    }
}
