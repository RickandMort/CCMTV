package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
import android.util.Log;

import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yu on 2018/4/25.
 */

public class SendSignInfo {



    public static String sendSign(Context context, String tid, String pptid, String signnum) {
        String result="";
        try {
            JSONObject obj = new JSONObject();
            obj.put("act", URLConfig.taskVideoSign);
            obj.put("uid", SharedPreferencesTools.getUid(context));
            obj.put("tid", tid);
            obj.put("aid", pptid);
            obj.put("signnum", signnum);
//            Log.e("看看sign数据", obj.toString());
            result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//            Log.e("看看sign数据", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
