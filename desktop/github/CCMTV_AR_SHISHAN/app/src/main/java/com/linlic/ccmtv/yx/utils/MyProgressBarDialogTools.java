package com.linlic.ccmtv.yx.utils;

import android.content.Context;

/**
 * name：访问网络 进度框 工具类
 * <p/>
 * author: Mr.song
 * 时间：2016-3-9 下午7:01:40
 *
 * @author Administrator
 */
public class MyProgressBarDialogTools {
    private static MyProgressBarDialogCustom dialogCustom;
    private static MyProgressBarDialogCustom2 dialogCustom2;

    /**
     * name：显示  加载中
     * <p/>
     * author: Mr.song
     * 时间：2016-3-9 下午7:01:12
     *
     * @param context
     */
    public static void show(Context context) {
        if (dialogCustom == null) {
            dialogCustom = new MyProgressBarDialogCustom(context);

        }
        if(dialogCustom.isShowing()) {

        }else {
            dialogCustom.show(context);
        }

    }

    /**
     * name：显示  加载中
     * <p/>
     * author: Mr.song
     * 时间：2016-3-9 下午7:01:12
     *
     * @param context
     */
    public static void show2(Context context) {
        if (dialogCustom2 == null) {
            dialogCustom2 = new MyProgressBarDialogCustom2(context);

        }
        if(dialogCustom2.isShowing()) {

        }else {
            dialogCustom2.show(context);
        }


    }

    /**
     * name：隐藏  加载中
     * <p/>
     * author: Mr.song
     * 时间：2016-3-9 下午7:01:12
     */
    public static void hide() {
        if (dialogCustom != null) {
            dialogCustom.DialodDismiss();
        }
    }
    /**
     * name：隐藏  加载中
     * <p/>
     * author: Mr.song
     * 时间：2016-3-9 下午7:01:12
     */
    public static void hide2() {
        if (dialogCustom2 != null) {
            dialogCustom2.DialodDismiss();
        }
    }
}