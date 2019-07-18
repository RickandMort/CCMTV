package com.linlic.ccmtv.yx.activity.login.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.DownLoadImageUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下载AR图片
 */
public class DownloadARImgService extends Service {
    Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    // OnDownFinishListener onDownFinishListener;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      //  Log.i("AR下载图片", "--onStartCommand");
        final boolean isClicked = intent.getBooleanExtra("isClicked", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doARData);
                    String res = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
             //       Log.i("AR图片result", res.toString());
                    JSONObject result = new JSONObject(res);
                    if (result.getInt("status") == 1) {//成功
               //         Log.i("AR图片", result.toString());
                        JSONArray dataArray = result
                                .getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject object = dataArray.getJSONObject(i);
                            File file = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", object.getString("aid") + "." + object.getString("picurl").substring(object.getString("picurl").lastIndexOf(".") + 1));
                            if (object.getString("isupdate").equals("0")) {//判断当前文件是否存在，不存在就下载当前图片
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("picurl", object.getString("picurl"));
                                map.put("aid", object.getString("aid"));
                                map.put("smvp", new JSONObject(object.getString("smvp")).get("fluentFile"));
                                data.add(map);
                            } else if (object.getString("isupdate").equals("1")) {//删除原图片，下载当前图片

                                if (file.exists()) {
                                    file.delete();
                                }
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("picurl", object.getString("picurl"));
                                map.put("aid", object.getString("aid"));
                                map.put("smvp", new JSONObject(object.getString("smvp")).get("fluentFile"));
                                data.add(map);
                            } else if (object.getString("isupdate").equals("2")) {//删除当前图片

                                if (file.exists()) {
                                    file.delete();
                                }
                            }

                            String picName = data.get(i).get("picurl").toString();
                            DownLoadImageUtil.getImageURI_Name(data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1), data.get(i).get("picurl").toString());
                        //    Log.i("AR下载图片", "图片名字" + data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1));
                        }
                        /**
                         * 拼接Json字符串
                         */

                        JSONArray indexInfoArray = new JSONArray();
                        for (int i = 0; i < data.size(); i++) {
                            String picName = URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8");
                            JSONObject parent_plot = new JSONObject();
                            parent_plot.put("image", data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1));
                            parent_plot.put("name", data.get(i).get("aid"));
//                                parent_plot.put("size", "[20.56, 5.4]");
                            parent_plot.put("uid", data.get(i).get("smvp"));
                           // parent_plot.put("picurl", URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8"));
                            indexInfoArray.put(parent_plot);
                        }
                        JSONObject object = new JSONObject();
                        object.put("images", indexInfoArray);

                        /**
                         * 将Json数据保存至本地
                         */
                        File filepath = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");  //存储位置为URLConfig.ccmtvapp_basesdcardpath，非固定路径。可选择内置或者外置内存卡
                        //判断文件夹是否存在,如果不存在则创建文件夹
                        if (!filepath.exists()) {
                            filepath.mkdir();
                        }
                        try {
                            File jsonfile = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", "targets.json");  //存储位置为URLConfig.ccmtvapp_basesdcardpath，非固定路径。可选择内置或者外置内存卡
                            FileWriter fileWritter = new FileWriter(jsonfile);
                            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                            bufferWritter.write(object.toString());
                            bufferWritter.close();
                       //     Log.i("AR图片", "JSONArray保存至SD卡     " + object.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (isClicked) {
                            //  onDownFinishListener.DownFinish(true);
                            Intent i = new Intent();
                            i.setAction("down_AR_success");
                            sendBroadcast(i);
                        }
                        stopSelf();


                    } else {//失败
                        stopSelf();
                        Looper.prepare();
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    stopSelf();
                    Looper.prepare();
                    Toast.makeText(context, R.string.post_hint3, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /*public static void setOnDownFinishListener(OnDownFinishListener onDownFinishListener) {
        onDownFinishListener = onDownFinishListener;
    }*/
}
