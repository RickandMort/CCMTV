package com.linlic.ccmtv.yx.activity.my.download;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name：下载类
 * author：MrSong
 * data：2016/4/1.
 */
public class Down {
    //    public static String MyLog = "下载服务：";
    private static List<String> list;


    /**
     * name：弹出选择框
     * author：MrSong
     * data：2016/4/1 14:47
     */
    public static void MyAlert(final Context context, final String fluentFile, final String SDFile, final String hdefinitionFile,
                               final String title, final String videoId, final String picurl,
                               final TextView video_download_text, final String downloadNumber,
                               String videoClass) {
//        System.out.println("流畅：" + fluentFile + "\n标清：" + SDFile + "\n高清：" + hdefinitionFile + "\n视频名称：" + title);

        list = getVideoPathLength(fluentFile, SDFile, hdefinitionFile);

        String[] str = new String[list.size()];
        for (int i = 0; i < str.length; i++) {
            str[i] = list.get(i);
        }

        if (!MyDbUtils.findVideoExist(context, videoId)) {
            int which = SharedPreferencesTools.getClarity(context);
            if (SharedPreferencesTools.getOnlyWifiDown(context) == true) {
                if (NetUtil.isWifiAvailable(context)) {  //判断wifi
                    downloadVideo(which, downloadNumber, context, videoId, title, fluentFile, picurl, SDFile, hdefinitionFile, video_download_text, videoClass);
                } else {
                    Dialog alertDialog = new AlertDialog.Builder(context).
                            setTitle("系统检测未连接至wifi，是否设置？").
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    context.startActivity(intent);
                                }
                            }).
                            setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(context, "请确认是否连接至wifi", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            }).
                            create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);

                }
            } else {
                downloadVideo(which, downloadNumber, context, videoId, title, fluentFile, picurl, SDFile, hdefinitionFile, video_download_text, videoClass);
            }
        } else {
            Toast.makeText(context, "您已下载了该视频", Toast.LENGTH_SHORT).show();
        }

    }

    private static void downloadVideo(int which, String downloadNumber, Context context, String videoId, String title, String fluentFile, String picurl, String SDFile, String hdefinitionFile, TextView video_download_text, String videoClass) {
        //判断清晰度
        try {
            if (list.get(which).equals("流畅")) {
                if (fluentFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, fluentFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "流畅");
                } else if (SDFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, SDFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "标清");
                } else if (hdefinitionFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, hdefinitionFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "高清");
                } else {
                    Toast.makeText(context, "该视频不支持下载", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (list.get(which).equals("标清")) {
                if (SDFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, SDFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "标清");
                } else if (fluentFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, fluentFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "流畅");
                } else if (hdefinitionFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, hdefinitionFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "高清");
                } else {
                    Toast.makeText(context, "该视频不支持下载", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (list.get(which).equals("高清")) {
                if (hdefinitionFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, hdefinitionFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "高清");
                } else if (SDFile.length() != 0) {
                    MyDbUtils.saveVideoMsg(context, videoId, title, SDFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
//                    Log.e(MyLog, "标清");
                } else if (fluentFile.length() != 0) {
//                    Log.e(MyLog, "流畅");
                    MyDbUtils.saveVideoMsg(context, videoId, title, fluentFile, "0%", DownloadService.download_wait + "", picurl, videoClass);
                } else {
                    Toast.makeText(context, "该视频不支持下载", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } catch (Exception e) {
            Toast.makeText(context,
                    "视频资源正在修复，请稍候再试！",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        //调用接口，让接口+1
        setDownloadNum(context, videoId);
        //保存完数据库后发送广播
        Intent intent = new Intent();
        intent.setAction("download");
        context.sendBroadcast(intent);
        Toast.makeText(context, "已添加到下载列表", Toast.LENGTH_SHORT).show();

    }

    /**
     * name：动态加载弹框内容
     * author：MrSong
     * data：2016/4/1 17:53
     */
    private static List<String> getVideoPathLength(String fluentFile, String SDFile, String hdefinitionFile) {
        List<String> list = new ArrayList<>();
        /*if (fluentFile.length() != 0) {
            list.add("流畅");
        }
        if (SDFile.length() != 0) {
            list.add("标清");
        }
        if (hdefinitionFile.length() != 0) {
            list.add("高清");
        }*/
        list.add("流畅");
        list.add("标清");
        list.add("高清");
        return list;
    }


    private static void setDownloadNum(final Context context, final String videoId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", videoId);
                    obj.put("flg", "downStart");//下载次数加一
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    System.out.println("下载:" + obj + "|" + result);
//                    Log.e("downloadstart111",result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
