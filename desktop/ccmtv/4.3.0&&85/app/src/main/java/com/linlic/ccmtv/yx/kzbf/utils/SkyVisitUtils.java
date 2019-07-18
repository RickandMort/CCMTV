package com.linlic.ccmtv.yx.kzbf.utils;

import android.content.Context;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.bean.DbSearchArticle;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created Niklaus yu on 2018/1/24.
 */

public class SkyVisitUtils {

    /**
     * 获取当前日期、时间
     *
     * @return 2018-1-24 12:25:50
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static void OnlineStatistical(final Context context, final String url, final String entertime, final String leavetime) {
        if (SharedPreferencesTools.getUids(context) != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.onlineRecord);
                        obj.put("uid", SharedPreferencesTools.getUids(context));
                        obj.put("url", url);
                        obj.put("entertime", entertime);
                        obj.put("leavetime", leavetime);
                        obj.put("ip", "");

//                        Log.e("看看用户统计数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                        Log.e("看看用户统计数据", result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

}
