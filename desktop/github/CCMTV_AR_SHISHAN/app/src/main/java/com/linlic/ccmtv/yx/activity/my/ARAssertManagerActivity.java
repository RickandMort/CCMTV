package com.linlic.ccmtv.yx.activity.my;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.entry.RowItem;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.DataCleanManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：设置--AR资源管理
 * author：Larry
 * data：2016/8/30.
 */
public class ARAssertManagerActivity extends BaseActivity {
    Context context;
    private LinearLayout layout_sync_asset, layout_clear_asset;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<String> list = new ArrayList<String>();
    private List<String> list_aid = new ArrayList<String>();
    private String[] toBeStored = null;
    private String picurl, picaid = null;
    private RelativeLayout layout_prog;
    private TextView layout_prog_text,tv_arcache,tv_arcache2;
    private ImageView layout_img;
    private static long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arassert);
        context = this;

        findId();
        setText();
        setOnClick();
    }

    public void findId() {
        super.findId();
        layout_sync_asset = (LinearLayout) findViewById(R.id.layout_sync_asset);
        layout_clear_asset = (LinearLayout) findViewById(R.id.layout_clear_asset);
        layout_prog = (RelativeLayout) findViewById(R.id.layout_prog);
        layout_img = (ImageView) findViewById(R.id.layout_img);
        layout_prog_text = (TextView) findViewById(R.id.layout_prog_text);
        tv_arcache = (TextView) findViewById(R.id.tv_arcache);
        tv_arcache2 = (TextView) findViewById(R.id.tv_arcache2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    private void setText() {
        super.setActivity_title_name("AR资源管理");
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(this)
                .asGif()
                .load(R.mipmap.ar_loading)
                .apply(options)
                .into(layout_img);

        File file_AR = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
        String ARCacheSize = null;
        try {
            ARCacheSize = DataCleanManager.getCacheSize(file_AR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_arcache.setText(ARCacheSize);

        if(SharedPreferencesTools.getARAssertTime(ARAssertManagerActivity.this).trim().length()>0){
            tv_arcache2.setText("上次同步时间："+SharedPreferencesTools.getARAssertTime(ARAssertManagerActivity.this));
        }

    }

    private void setOnClick() {
        layout_sync_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }
                list.clear();
                list_aid.clear();

               /* progressDialog = CustomProgressDialog.createDialog(ARAssertManagerActivity.this);
                progressDialog.setMessage("正在同步...");
                progressDialog.setCancelable(false);
                progressDialog.show();*/

                layout_prog.setVisibility(View.VISIBLE);
                layout_prog_text.setText("正在加载...");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doARData);
                            String res = HttpClientUtils.sendPost(ARAssertManagerActivity.this,
                                    URLConfig.CCMTVAPP, obj.toString());
                            JSONObject result = new JSONObject(res);
                            if (result.getInt("status") == 1) { //成功
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

                                    picurl = data.get(i).get("picurl").toString();
                                    picaid = data.get(i).get("aid").toString();
                                    //这是一个下载图片的方法 xutils
                                    // getImageURI_Name(data.get(i).get("aid") + "." + picName.substring(picName.lastIndexOf(".") + 1), data.get(i).get("picurl").toString());
                                    list.add(picurl);
                                    list_aid.add(picaid);
                                }

                                toBeStored = list.toArray(new String[list.size()]);
                                DownloadTask task = new DownloadTask(ARAssertManagerActivity.this, list_aid);
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
                                    parent_plot.put("picurl", URLDecoder.decode(data.get(i).get("picurl").toString(), "UTF-8"));
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy:MM:dd");
                                Date curDate =  new Date(System.currentTimeMillis());
                                String   time_str   =   formatter.format(curDate);
                                SharedPreferencesTools.saveARAssertTime(ARAssertManagerActivity.this,time_str);

                                if(SharedPreferencesTools.getARAssertTime(ARAssertManagerActivity.this).trim().length()>0){
                                    tv_arcache2.setText("上次同步时间："+SharedPreferencesTools.getARAssertTime(ARAssertManagerActivity.this));
                                }

                            } else {//失败
                                Looper.prepare();
                                Toast.makeText(ARAssertManagerActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(ARAssertManagerActivity.this, R.string.post_hint3, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }

                ).start();

            }
        });

        layout_clear_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanManager.cleanCustomCache(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
                File file_AR = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
                String ARCacheSize = null;
                try {
                    ARCacheSize = DataCleanManager.getCacheSize(file_AR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_arcache.setText(ARCacheSize);
                Toast.makeText(ARAssertManagerActivity.this, "AR资源文件已删除", Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 异步任务：AsyncTask<Params, Progress, Result>
     * 1.Params:UI线程传过来的参数。
     * 2.Progress:发布进度的类型。
     * 3.Result:返回结果的类型。耗时操作doInBackground的返回结果传给执行之后的参数类型。
     * 执行流程：
     * 1.onPreExecute()
     * 2.doInBackground()-->onProgressUpdate()
     * 3.onPostExecute()
     */
    private class DownloadTask extends
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
                saveMyBitmap(context, map, picNames.get(i - 1).toString() + "." + url.substring(url.lastIndexOf(".") + 1));
                rowItems.add(new RowItem(map));
            }
            return rowItems;
        }

        protected void onProgressUpdate(Integer... progress) {
            // progressDialog.setProgress(progress[0]);
            if (rowItems != null) {
                /*progressDialog.setMessage("加载 " + (rowItems.size() + 1)
                        + "/" + taskCount);*/
                layout_prog_text.setText("当前进度 " + (rowItems.size() + 1)
                        + "/" + taskCount);
            }
        }

        @Override
        protected void onPostExecute(List<RowItem> rowItems) {
//            progressDialog.dismiss();
            layout_prog.setVisibility(View.GONE);
            File file_AR = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
            String ARCacheSize = null;
            try {
                ARCacheSize = DataCleanManager.getCacheSize(file_AR);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv_arcache.setText(ARCacheSize);
            Toast.makeText(ARAssertManagerActivity.this, "同步成功", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ARAssertManagerActivity.this,"请不要频繁操作",Toast.LENGTH_SHORT).show();
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
