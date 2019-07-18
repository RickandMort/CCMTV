package com.linlic.ccmtv.yx.activity.videoar;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.login.entry.RowItem;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cn.easyar.Engine;
import cn.easyar.engine.EasyAR;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class ARFragment extends Fragment {

    private View view;
    private RelativeLayout layout_prog;
    private ImageView layout_img;
    private TextView layout_prog_text;
    public static Timer timer;
    public static Timer timers;
    public ARFragment videoar;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private LinearLayout btn_sync;
    private List<String> list = new ArrayList<String>();
    private List<String> list_aid = new ArrayList<String>();
    private String[] toBeStored = null;
    private String picurl, picaid = null;
    private static long lastClickTime;

    private GLView glView;

//    static String key = "ayFwYLCeYtqrbhjqxHwKtxUCg3DnQYLhqUiS2Ue9zc2oYGEHLkrzTsYtw5MWPOjE67rahNegDDC9u9qRjV7hnMv6C9O3fUhHZcN624c5b2266b1cb1ff98d9baf4d626b087ClvSggrDQUqqggmoCiqkfxlJT5E0S0BwoDVd4QVLt88FfYWekmqdQBPv9ZGypWyj4ZYL";
    static String key = "ihve3QWPC2DM4liCqNI5y2r3UzBOOtjSWqZ01sRIywJUMuqTjAw5GA8iKVxEAqXfAW2eA1ovV9GwAiQn8tF8HOmVQzeHVEqRXYgGuJp8rIvGEwFWED9e0Hwq6c5zHzvmHVQvGaeCcvtKWkiTnJB7kd6arYR3cUxVf8lnwEFhTY19EoHPqdwus6XavVw0xniEjp7IBE97";

   /* static {
        System.loadLibrary("EasyAR");
        System.loadLibrary("videoAR");
    }*/

   /* public static native void nativeInitGL();

    public static native void nativeResizeGL(int w, int h);

    public static native void nativeRender();

    public native boolean nativeInit(String s);

    public native void nativeInitaa(String s);

    private native void nativeDestory();

    private native void nativeRotationChange(boolean portrait);

    private native int nativeStartLoading();

    private native int nativeStopLoading();*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ar, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    private void initView() {
        timer = new Timer(true);
        timers = new Timer(true);

        videoar = this;
        btn_sync = (LinearLayout) view.findViewById(R.id.btn_sync2);
        layout_prog = (RelativeLayout) view.findViewById(R.id.layout_prog2);
        layout_img = (ImageView) view.findViewById(R.id.layout_img2);
        layout_prog_text = (TextView) view.findViewById(R.id.layout_prog_text2);

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }

                list.clear();
                list_aid.clear();
                layout_prog.setVisibility(View.VISIBLE);
                layout_prog_text.setText("正在加载...");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doARData);
                            String res = HttpClientUtils.sendPost(getContext(), URLConfig.CCMTVAPP, obj.toString());
//                            Log.i("AR下载图片", res.toString());
                            final JSONObject result = new JSONObject(res);
                            if (result.getInt("status") == 1) {//成功
//                                Log.i("AR下载图片", result.toString());
                                JSONArray dataArray = result.getJSONArray("data");
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
                                    if (data.size() == 0) {
                                        Looper.prepare();
                                        Toast.makeText(getContext(), "已经为最新资源", Toast.LENGTH_SHORT).show();
                                        layout_prog.setVisibility(View.GONE);
                                        Looper.loop();
                                    } else {
                                        picurl = data.get(i).get("picurl").toString();
                                        picaid = data.get(i).get("aid").toString();
                                        //这是一个下载图片的方法 xutils
                                        // getImageURI_Name(data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1), data.get(i).get("picurl").toString());
                                        list.add(FirstLetter.getSpells(picurl));
                                        list_aid.add(picaid);
                                    }
                                }

                                toBeStored = list.toArray(new String[list.size()]);
                                DownloadTask task = new DownloadTask(getActivity(), list_aid);
                                //DownloadTask task = new DownloadTask(VideoAR.this, picaid + "." + picurl.substring(picurl.lastIndexOf(".") + 1));
                                task.execute(toBeStored);

                                /**
                                 * 拼接Json字符串
                                 */
                                JSONArray indexInfoArray = new JSONArray();
                                for (int i = 0; i < data.size(); i++) {
                                    String picName = URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8");
                                    JSONObject parent_plot = new JSONObject();
                                    parent_plot.put("image", data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1));
                                    parent_plot.put("name", data.get(i).get("aid"));
                                    parent_plot.put("uid", data.get(i).get("smvp"));
                                    //    parent_plot.put("picurl", URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8"));
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
//                                    Log.i("JsonImage", object.toString());
                                    bufferWritter.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {//失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            layout_prog.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    layout_prog.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.post_hint3, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                ).start();
            }
        });

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this)
                .asGif()
                .load(R.mipmap.ar_loading)
                .apply(options)
                .into(layout_img);

        if (!Engine.initialize(getActivity(), key)) {
            Log.e("HelloAR", "Initialization Failed.");
        }else{
            glView = new GLView(getActivity());
        }




        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) getActivity().findViewById(R.id.preview2)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }

            @Override
            public void onFailure() {
            }
        });

//        EasyAR.initialize(getActivity(), key);
//        nativeInit(URLConfig.ccmtvapp_basesdcardpath + "/arImage/targets.json");


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timers.cancel();
//        Log.e("timerCancel31", "timerCancel31");
    }




    @Override
    public void onDetach() {
        super.onDetach();
        EasyAR.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    class DownloadTask extends
            AsyncTask<String, Integer, List<RowItem>> {

        private Activity context;
        private List<String> picNames;
        List<RowItem> rowItems;
        int taskCount;

        public DownloadTask(Activity context, List<String> picNames) {
            this.context = context;
            this.picNames = picNames;
        }

        @Override
        protected List<RowItem> doInBackground(String... urls) {
            taskCount = urls.length;
            rowItems = new ArrayList<RowItem>();
            Bitmap map = null;
            int i = 0;
            for (String url : urls) {
                i++;
                map = downloadImage(url);
                // saveImageBitmap(context, map, picNames.get(i - 1).toString() + "." + url.substring(url.lastIndexOf(".") + 1));
                saveMyBitmap(context, map, picNames.get(i - 1).toString() + "." + url.substring(url.lastIndexOf(".") + 1));
                rowItems.add(new RowItem(map));
            }
            return rowItems;
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressDialog.setProgress(progress[0]);
            if (rowItems != null) {
                layout_prog_text.setText("当前进度 " + (rowItems.size() + 1) + "/" + taskCount);
            }
        }

        @Override
        protected void onPostExecute(List<RowItem> rowItems) {
            layout_prog.setVisibility(View.GONE);
           /*
            EasyAR.onResume();*/
//            videoar.nativeInitaa(URLConfig.ccmtvapp_basesdcardpath + "/arImage/targets.json");
//            Engine.loadLibraries();
            glView.helloAR.loadAllFromJsonFile2(URLConfig.ccmtvapp_basesdcardpath + "/arImage/targets.json");
            Toast.makeText(getActivity(), "同步成功", Toast.LENGTH_SHORT).show();

        }

        /**
         * 下载Image
         *
         * @param urlString
         * @return
         */
        private Bitmap downloadImage(String urlString) {
            int count = 0;
            Bitmap bitmap = null;

            URL url;
            InputStream in = null;
            BufferedOutputStream out = null;

            try {
                url = new URL(urlString);
                URLConnection conn = url.openConnection();
                int lengthOfFile = conn.getContentLength();

                in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                out = new BufferedOutputStream(dataStream);

                byte[] data = new byte[1024];
                long total = 0L;
                while ((count = in.read(data)) != -1) {
                    total += count;
                    publishProgress((int) ((total * 100) / lengthOfFile));
                    out.write(data, 0, count);
                }
                out.flush();
                byte[] bytes = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  Bitmap bitmap = ImageLoader.getInstance().loadImageSync(urlString);
            return bitmap;
        }
    }

    //保存图片bitMap
    public void saveMyBitmap(Context context, Bitmap mBitmap, String bitName) {
        File file = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", bitName);

        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
        } else {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //是否是多次点击
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 5000) {
            Toast.makeText(getActivity(), "请不要频繁操作", Toast.LENGTH_SHORT).show();
            return true;
        }
        lastClickTime = time;
        return false;
    }



    private interface PermissionCallback
    {
        void onSuccess();
        void onFailure();
    }
    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;
    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    public void onPause()
    {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }



    public boolean onCreateOptionsMenu(Menu menu)
    {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
