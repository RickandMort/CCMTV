package com.xiao.nicevideoplayer;

import android.util.Log;

/**
 * Created by XiaoJianjun on 2017/5/4.
 * log工具.
 */
public class LogUtil {

    private static final String TAG = "NiceVideoPlayer";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }


    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void e(String TAG, String msg) {
//        int strLength = msg.length();
//        int start = 0;
//        int end = LOG_MAXLENGTH;
//        for (int i = 0; i < 1000; i++) {
//            //剩下的文本还是大于规定长度则继续重复截取并输出
//            if (strLength > end) {
//                Log.e(TAG + i, msg.substring(start, end));
//                start = end;
//                end = end + LOG_MAXLENGTH;
//            } else {
//                Log.e(TAG, msg.substring(start, strLength));
//                break;
//            }
//        }
    }
}
