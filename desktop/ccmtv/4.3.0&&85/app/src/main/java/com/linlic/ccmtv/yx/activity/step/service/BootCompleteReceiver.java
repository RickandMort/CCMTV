package com.linlic.ccmtv.yx.activity.step.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linlic.ccmtv.yx.LocalApplication;

/**
 * 开机完成广播
 * Created by lenovo on 2017/1/5.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        Intent i=new Intent(context,StepService.class);
        context.startService(i);
    }
}