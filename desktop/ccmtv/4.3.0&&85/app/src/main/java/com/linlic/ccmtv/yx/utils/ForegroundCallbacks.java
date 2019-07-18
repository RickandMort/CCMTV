package com.linlic.ccmtv.yx.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.linlic.ccmtv.yx.activity.my.medical_examination.Formal_examination;
import com.linlic.ccmtv.yx.floatWindow.service.FloatViewService;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatLayout;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ForegroundCallbacks implements Application.ActivityLifecycleCallbacks {
    public static final long CHECK_DELAY = 100;
    //    public static final String TAG = ForegroundCallbacks.class.getName();
    public static final String TAG = "APP状态";

    public interface Listener {
        public void onBecameForeground();

        public void onBecameBackground();
    }

    private static ForegroundCallbacks instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;

    public static ForegroundCallbacks init(Application application) {
        if (instance == null) {
            instance = new ForegroundCallbacks();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static ForegroundCallbacks get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static ForegroundCallbacks get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static ForegroundCallbacks get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null)
            handler.removeCallbacks(check);
        if (wasBackground) {
//            if (!Settings.canDrawOverlays(activity)) {
//                // 设置中的悬浮窗权限 没打开
//            } else {
//                //启动服务，播放音乐
//                Intent intent = new Intent(activity, FloatViewService.class);
//                intent.putExtra("type", FloatLayout.SHOW);
//                activity.startService(intent);
//            }

            Intent intent = new Intent(activity, FloatViewService.class);
            intent.putExtra("type", FloatLayout.SHOW);
            activity.startService(intent);

            LogUtil.e(TAG, "went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    LogUtil.e(TAG, "Listener threw exception!: " + exc);
                }
            }
        } else {
            LogUtil.e(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        paused = true;
        if (check != null)
            handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
//                    if (!Settings.canDrawOverlays(activity)) {
//                        // 设置中的悬浮窗权限 没打开
//                    } else {
//                        //按home键会直接关闭悬浮窗
//                        Intent intent2 = new Intent(activity, FloatViewService.class);
//                        intent2.putExtra("type", FloatLayout.HIDE);
//                        activity.startService(intent2);
//                    }
//
                    //按home键会直接关闭悬浮窗
                    Intent intent2 = new Intent(activity, FloatViewService.class);
                    intent2.putExtra("type", FloatLayout.HIDE);
                    activity.startService(intent2);

                    Formal_examination.Whether_or_not_run_backstage = true;
                    LogUtil.e(TAG, "went background");
                    for (Listener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            LogUtil.e(TAG, "Listener threw exception!: " + exc);
                        }
                    }
                } else {
                    LogUtil.e(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
