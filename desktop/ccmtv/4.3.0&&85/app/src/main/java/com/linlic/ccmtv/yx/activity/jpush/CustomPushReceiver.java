package com.linlic.ccmtv.yx.activity.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/*
* 推送接收器
* */
public class CustomPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    public CustomPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
   //     Log.i(TAG, "[PushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

   /*     if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
     //       Log.i(TAG, "[PushReceiver] 接收Registeration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
         //   Log.i(TAG, "[PushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        //    Log.i(TAG, "[PushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        //    Log.i(TAG, "[PushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
       //     Log.i(TAG, "[PushReceiver] 用户点击打开了通知");

            String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
       //     Log.i(TAG, "type:" + type);
            try {
                JSONObject jsonObject = new JSONObject(type);
                if (jsonObject.has("jumpKey")) {
                    String str = jsonObject.getString("jumpKey");
                    if (str.equals("1")) {
                        //打开自定义的Activity
                        Intent i = new Intent(context, Has_been_uploaded.class);
                        bundle.putInt("index", 1);
                        i.putExtras(bundle);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else if (str.equals("2")) {
                        String activityurl = jsonObject.getString("activityurl");  //活动title
                        String activitytitle = jsonObject.getString("activitytitle");  //活动url
                        //打开自定义的Activity
                        Intent i = new Intent(context, ActivityWebActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("aid", activityurl);
                        i.putExtra("title", activitytitle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else if (str.equals("3")) {
                        //打开自定义的Activity
                        Intent i = new Intent(context, MyMessageActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else if (str.equals("4")) {
                        //打开自定义的Activity
                        Intent i = new Intent(context, Integral.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else if (str.equals("5")) {
                        //打开自定义的Activity
                        Intent i = new Intent(context, MainActivity.class);
                        bundle.putInt("index", 1);
                        i.putExtras(bundle);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }else if (str.equals("6")){
                        //跳转视频播放页
                        //打开自定义的Activity
                        String uid = SharedPreferencesTools.getUidToLoginClose(context);
                        Intent i = new Intent(context, VideoFive.class);
                        i.putExtra("aid",  jsonObject.getString("videoId"));

                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }else {
                        //打开自定义的Activity
                        Intent i = new Intent(context, MainActivity.class);
                        bundle.putInt("index", 1);
                        i.putExtras(bundle);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                } else {
                    //打开自定义的Activity
                    Intent i = new Intent(context, MainActivity.class);
                    bundle.putInt("index", 1);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
         //   Log.i(TAG, "[PushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
          //  Log.i(TAG, "[PushReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
          //  Log.i(TAG, "[PushReceiver] Unhandled intent - " + intent.getAction());
        }*/
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
      /*  for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }*/
        return sb.toString();

    }
}
