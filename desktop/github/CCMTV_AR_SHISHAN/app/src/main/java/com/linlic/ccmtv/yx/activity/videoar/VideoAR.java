package com.linlic.ccmtv.yx.activity.videoar;//package com.linlic.ccmtv.yx.activity.videoar;
//
///**
// * Created by Administrator on 2016/8/8.
// * <p/>
// * Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// * EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// * and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
// * <p/>
// * Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// * EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// * and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
// * <p/>
// * Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// * EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// * and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
// * <p/>
// * Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// * EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// * and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
// */
///**
// * Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
// * EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
// * and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
// */
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Looper;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.linlic.ccmtv.yx.R;
//import com.linlic.ccmtv.yx.activity.login.entry.RowItem;
//import com.linlic.ccmtv.yx.config.URLConfig;
//import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
//import com.linlic.ccmtv.yx.utils.FirstLetter;
//import com.linlic.ccmtv.yx.utils.HttpClientUtils;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import cn.easyar.engine.EasyAR;
//
//public class VideoAR extends Activity {
//    Context context;
//    //用户统计
//    private String entertime, leavetime;
//    public static String enterUrl = "http://www.ccmtv.cn";
//    private RelativeLayout layout_prog;
//    private ImageView layout_img;
//    private TextView layout_prog_text;
//    private Timer timer = new Timer(true);
//    private Timer timers = new Timer(true);
//    public VideoAR videoar;
//    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//    private LinearLayout btn_sync;
//    private List<String> list = new ArrayList<String>();
//    private List<String> list_aid = new ArrayList<String>();
//    private String[] toBeStored = null;
//    private String picurl, picaid = null;
//    private static long lastClickTime;
//
//    /*
//    * Steps to create the key for this sample:
//    *  1. login www.easyar.com
//    *  2. create app with
//    *      Name: HelloARVideo
//    *      Package Name: cn.easyar.samples.helloarvideo
//    *  3. find the created item in the list and show key
//    *  4. set key string bellow
//    */
//    static String key = "ayFwYLCeYtqrbhjqxHwKtxUCg3DnQYLhqUiS2Ue9zc2oYGEHLkrzTsYtw5MWPOjE67rahNegDDC9u9qRjV7hnMv6C9O3fUhHZcN624c5b2266b1cb1ff98d9baf4d626b087ClvSggrDQUqqggmoCiqkfxlJT5E0S0BwoDVd4QVLt88FfYWekmqdQBPv9ZGypWyj4ZYL";
//
//    static {
//        System.loadLibrary("EasyAR");
//        System.loadLibrary("videoAR");
//    }
//
//    public static native void nativeInitGL();
//
//    public static native void nativeResizeGL(int w, int h);
//
//    public static native void nativeRender();
//
//    public native boolean nativeInit(String s);
//
//    public native void nativeInitaa(String s);
//
//    private native void nativeDestory();
//
//    private native void nativeRotationChange(boolean portrait);
//
//    private native int nativeStartLoading();
//
//    private native int nativeStopLoading();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.videoar);
//        context = this;
//        videoar = this;
//        btn_sync = (LinearLayout) findViewById(R.id.btn_sync);
//        layout_prog = (RelativeLayout) findViewById(R.id.layout_prog);
//        layout_img = (ImageView) findViewById(R.id.layout_img);
//        layout_prog_text = (TextView) findViewById(R.id.layout_prog_text);
//
//        btn_sync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isFastDoubleClick()) {
//                    return;
//                }
//
//                list.clear();
//                list_aid.clear();
//                layout_prog.setVisibility(View.VISIBLE);
//                layout_prog_text.setText("正在加载...");
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject obj = new JSONObject();
//                            obj.put("act", URLConfig.doARData);
//                            String res = HttpClientUtils.sendPost(VideoAR.this,
//                                    URLConfig.CCMTVAPP, obj.toString());
////                            Log.i("AR下载图片", res.toString());
//                            final JSONObject result = new JSONObject(res);
//                            if (result.getInt("status") == 1) {//成功
////                                Log.i("AR下载图片", result.toString());
//                                JSONArray dataArray = result
//                                        .getJSONArray("data");
//                                for (int i = 0; i < dataArray.length(); i++) {
//                                    JSONObject object = dataArray.getJSONObject(i);
//                                    File file = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", object.getString("aid") + "." + object.getString("picurl").substring(object.getString("picurl").lastIndexOf(".") + 1));
//                                    if (object.getString("isupdate").equals("0")) {//判断当前文件是否存在，不存在就下载当前图片
//                                        Map<String, Object> map = new HashMap<String, Object>();
//                                        map.put("picurl", object.getString("picurl"));
//                                        map.put("aid", object.getString("aid"));
//                                        map.put("smvp", new JSONObject(object.getString("smvp")).get("fluentFile"));
//                                        data.add(map);
//                                    } else if (object.getString("isupdate").equals("1")) {//删除原图片，下载当前图片
//
//                                        if (file.exists()) {
//                                            file.delete();
//                                        }
//                                        Map<String, Object> map = new HashMap<String, Object>();
//                                        map.put("picurl", object.getString("picurl"));
//                                        map.put("aid", object.getString("aid"));
//                                        map.put("smvp", new JSONObject(object.getString("smvp")).get("fluentFile"));
//                                        data.add(map);
//                                    } else if (object.getString("isupdate").equals("2")) {//删除当前图片
//
//                                        if (file.exists()) {
//                                            file.delete();
//                                        }
//                                    }
//
//                                    if (data.size() == 0) {
//                                        Looper.prepare();
//                                        Toast.makeText(VideoAR.this, "已经为最新资源", Toast.LENGTH_SHORT).show();
//                                        layout_prog.setVisibility(View.GONE);
//                                        Looper.loop();
//                                    } else {
//                                        picurl = data.get(i).get("picurl").toString();
//                                        picaid = data.get(i).get("aid").toString();
//                                        //这是一个下载图片的方法 xutils
//                                        // getImageURI_Name(data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1), data.get(i).get("picurl").toString());
//                                        list.add(FirstLetter.getSpells(picurl));
//                                        list_aid.add(picaid);
//                                    }
//                                }
//
//
//                                toBeStored = list.toArray(new String[list.size()]);
//                                DownloadTask task = new DownloadTask(VideoAR.this, list_aid);
//                                //DownloadTask task = new DownloadTask(VideoAR.this, picaid + "." + picurl.substring(picurl.lastIndexOf(".") + 1));
//                                task.execute(toBeStored);
//
//
//                                /**
//                                 * 拼接Json字符串
//                                 */
//
//                                JSONArray indexInfoArray = new JSONArray();
//                                for (int i = 0; i < data.size(); i++) {
//                                    String picName = URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8");
//                                    JSONObject parent_plot = new JSONObject();
//                                    parent_plot.put("image", data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1));
//                                    parent_plot.put("name", data.get(i).get("aid"));
//                                    parent_plot.put("uid", data.get(i).get("smvp"));
//                                //    parent_plot.put("picurl", URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8"));
//                                    indexInfoArray.put(parent_plot);
//                                }
//                                JSONObject object = new JSONObject();
//                                object.put("images", indexInfoArray);
//                                /**
//                                 * 将Json数据保存至本地
//                                 */
//                                File filepath = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");  //存储位置为URLConfig.ccmtvapp_basesdcardpath，非固定路径。可选择内置或者外置内存卡
//                                //判断文件夹是否存在,如果不存在则创建文件夹
//                                if (!filepath.exists()) {
//                                    filepath.mkdir();
//                                }
//                                try {
//                                    File jsonfile = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", "targets.json");  //存储位置为URLConfig.ccmtvapp_basesdcardpath，非固定路径。可选择内置或者外置内存卡
//                                    FileWriter fileWritter = new FileWriter(jsonfile);
//                                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//                                    bufferWritter.write(object.toString());
////                                    Log.i("JsonImage",object.toString());
//                                    bufferWritter.close();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            } else {//失败
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            layout_prog.setVisibility(View.GONE);
//                                            Toast.makeText(VideoAR.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
//                                        }catch (Exception e){
//                                        }
//                                    }
//                                });
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    layout_prog.setVisibility(View.GONE);
//                                    Toast.makeText(VideoAR.this, R.string.post_hint3, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//                }
//
//                ).start();
//
//            }
//        });
//
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        Glide.with(this).load(R.mipmap.ar_loading).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(layout_img);
//
//        EasyAR.initialize(this, key);
//        nativeInit(URLConfig.ccmtvapp_basesdcardpath + "/arImage/targets.json");
//
//        GLView glView = new GLView(this);
//        glView.setRenderer(new Renderer());
//        glView.setZOrderMediaOverlay(true);
//
//        ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        nativeRotationChange(getWindowManager().getDefaultDisplay().getRotation() == android.view.Surface.ROTATION_0);
//
//
//        //启动线程无限加载
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                //加载完毕状态为110，没有成功的话是100
//                int aaa = nativeStartLoading();
////                System.out.println("start:"+aaa);
//                if (aaa == 110) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            layout_prog.setVisibility(View.VISIBLE);
//                            layout_prog_text.setText("正在加载...");
//                        }
//                    });
//                    timer.cancel();
////                    Log.e("timersCancel","timersCancel");
//
//                    TimerTask task1 = new TimerTask() {
//                        @Override
//                        public void run() {
//                            int bbb = nativeStopLoading();
//                            if (bbb == 110) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        layout_prog.setVisibility(View.GONE);
//                                    }
//                                });
//                                timers.cancel();
////                                Log.e("timersCancel1","timersCancel1");
//                            }
////                            System.out.println("stop:"+bbb);
//                        }
//                    };
//                    timers.schedule(task1, 1000, 1000);
////                    Log.e("timersRunning1","timersRunning1");
//                }
//            }
//        };
//        timer.schedule(task, 1000, 1000);
////        Log.e("timersRunning","timersRunning");
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration config) {
//        super.onConfigurationChanged(config);
//        nativeRotationChange(getWindowManager().getDefaultDisplay().getRotation() == android.view.Surface.ROTATION_0);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        nativeDestory();
//    }
//
//    @Override
//    protected void onResume() {
//        //保存进入的日期
//        entertime = SkyVisitUtils.getCurrentTime();
//        EasyAR.onResume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        //保存退出的日期
//        leavetime = SkyVisitUtils.getCurrentTime();
//        //保存日期到服务器
//        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
//        EasyAR.onPause();
//        super.onPause();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    public void back(View view) {
//        finish();
//        timer.cancel();
//        timers.cancel();
////        Log.e("timersCancel2","timersCancel2");
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        timer.cancel();
//        timers.cancel();
////        Log.e("timersCancel3","timersCancel3");
//    }
//
//
//    /**
//     * 异步任务：AsyncTask<Params, Progress, Result>
//     * 1.Params:UI线程传过来的参数。
//     * 2.Progress:发布进度的类型。
//     * 3.Result:返回结果的类型。耗时操作doInBackground的返回结果传给执行之后的参数类型。
//     * 执行流程：
//     * 1.onPreExecute()
//     * 2.doInBackground()-->onProgressUpdate()
//     * 3.onPostExecute()
//     */
//    class DownloadTask extends
//            AsyncTask<String, Integer, List<RowItem>> {
//
//        private Activity context;
//        private List<String> picNames;
//        List<RowItem> rowItems;
//        int taskCount;
//
//        public DownloadTask(Activity context, List<String> picNames) {
//            this.context = context;
//            this.picNames = picNames;
//        }
//
//        @Override
//        protected List<RowItem> doInBackground(String... urls) {
//            taskCount = urls.length;
//            rowItems = new ArrayList<RowItem>();
//            Bitmap map = null;
//            int i = 0;
//            for (String url : urls) {
//                i++;
//                map = downloadImage(url);
//                // saveImageBitmap(context, map, picNames.get(i - 1).toString() + "." + url.substring(url.lastIndexOf(".") + 1));
//                saveMyBitmap(context, map, picNames.get(i - 1).toString() + "." + url.substring(url.lastIndexOf(".") + 1));
//                rowItems.add(new RowItem(map));
//            }
//            return rowItems;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            // progressDialog.setProgress(progress[0]);
//            if (rowItems != null) {
//                layout_prog_text.setText("当前进度 " + (rowItems.size() + 1)
//                        + "/" + taskCount);
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<RowItem> rowItems) {
//            layout_prog.setVisibility(View.GONE);
//           /*
//            EasyAR.onResume();*/
//            videoar.nativeInitaa(URLConfig.ccmtvapp_basesdcardpath + "/arImage/targets.json");
//            Toast.makeText(VideoAR.this, "同步成功", Toast.LENGTH_SHORT).show();
//
//        }
//
//        /**
//         * 下载Image
//         *
//         * @param urlString
//         * @return
//         */
//        private Bitmap downloadImage(String urlString) {
//            int count = 0;
//            Bitmap bitmap = null;
//
//            URL url;
//            InputStream in = null;
//            BufferedOutputStream out = null;
//
//            try {
//                url = new URL(urlString);
//                URLConnection conn = url.openConnection();
//                int lengthOfFile = conn.getContentLength();
//
//                in = new BufferedInputStream(url.openStream());
//                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//                out = new BufferedOutputStream(dataStream);
//
//                byte[] data = new byte[1024];
//                long total = 0L;
//                while ((count = in.read(data)) != -1) {
//                    total += count;
//                    publishProgress((int) ((total * 100) / lengthOfFile));
//                    out.write(data, 0, count);
//                }
//                out.flush();
//
//                byte[] bytes = dataStream.toByteArray();
//                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //  Bitmap bitmap = ImageLoader.getInstance().loadImageSync(urlString);
//
//
//            return bitmap;
//        }
//
//    }
//
//
//    //保存图片bitMap
//    public void saveMyBitmap(Context context, Bitmap mBitmap, String bitName) {
//        File file = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", bitName);
//
//        // 如果图片存在本地缓存目录，则不去服务器下载
//        if (file.exists()) {
////            Log.i("ffffff", file.toString() + "   本地缓存      " + bitName);
//        } else {
////            Log.i("ffffff", "hhhhhhhhhhhhhhhhhhhhhhhhh服务器" + bitName);
//            FileOutputStream fOut = null;
//            try {
//                fOut = new FileOutputStream(file);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//            try {
//                fOut.flush();
//                fOut.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    //是否是多次点击
//    public boolean isFastDoubleClick() {
//        long time = System.currentTimeMillis();
//        long timeD = time - lastClickTime;
//        if (0 < timeD && timeD < 5000) {
//            Toast.makeText(VideoAR.this, "请不要频繁操作", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        lastClickTime = time;
//        return false;
//    }
//}
