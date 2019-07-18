package com.linlic.ccmtv.yx.floatWindow.service;

import android.content.Context;
import android.content.Intent;


/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:与悬浮窗交互的控制类，真正的实现逻辑不在这
 */
public class FloatActionController {

    private FloatActionController() {
    }

    public static FloatActionController getInstance() {
        return LittleMonkProviderHolder.sInstance;
    }

    // 静态内部类
    private static class LittleMonkProviderHolder {
        private static final FloatActionController sInstance = new FloatActionController();
    }

    /**
     * 开启服务悬浮窗
     */
    public void startMonkServer(Context context) {
        Intent intent = new Intent(context, FloatViewService .class);
        context.startService(intent);
    }

    /**
     * 关闭悬浮窗
     */
    public void stopMonkServer(Context context) {
        Intent intent = new Intent(context, FloatViewService.class);
        context.stopService(intent);
    }


}
