package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.coursewares;
import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.coursewares_pos;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.getDataColumn;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isDownloadsDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isExternalStorageDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isMediaDocument;

/**
 * Created by tom on 2019/2/19.
 * 课件
 */

public class Courseware extends BaseActivity{
    private Context context;
    private String http ="";
    private String fid = "";
    private String id = "";
    private String type = "";
      List<com.linlic.ccmtv.yx.activity.entity.Courseware> coursewares2 = new ArrayList<>();//课件
    @Bind(R.id.openFileMenu)
    LinearLayout openFileMenu;//选择文件
    @Bind(R.id.data_layout)
    LinearLayout data_layout;//
    @Bind(R.id.courseware)
    GridView courseware;//文件列表
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    private BaseListAdapter  baseListAdapterCoursewares;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for (com.linlic.ccmtv.yx.activity.entity.Courseware courseware_ben : coursewares) {
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        coursewares.remove(courseware_ben);
                                    }
                                }
                                baseListAdapterCoursewares.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };
    private void setResultStatus(boolean status, int code) {
        if (status) {
            tranining2_nodata.setVisibility(View.GONE);
            data_layout.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }

            data_layout.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details_courseware);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        http = getIntent().getStringExtra("http");
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if(type.equals("1")){
            //可编辑
            openFileMenu.setVisibility(View.VISIBLE);
        }else{
            //不可编辑
            openFileMenu.setVisibility(View.GONE);

            List<com.linlic.ccmtv.yx.activity.entity.Courseware> rs = (List<com.linlic.ccmtv.yx.activity.entity.Courseware>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
            for (com.linlic.ccmtv.yx.activity.entity.Courseware courseware : rs) {
                coursewares2.add(courseware);
            }
        }

        setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
        baseListAdapterCoursewares = new BaseListAdapter(courseware, type.equals("1")?coursewares:coursewares2, R.layout.item_coursewares) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                com.linlic.ccmtv.yx.activity.entity.Courseware map = (com.linlic.ccmtv.yx.activity.entity.Courseware) item;
                helper.setText(R.id._item_text, map.getFile_name());
                helper.setTag(R.id._item_img, map.getFile_path());
                if (map.is_upload()) {
                    if(type.equals("1")){
                        helper.setVisibility(R.id._item_img, View.VISIBLE);
                    }else{
                        helper.setVisibility(R.id._item_img, View.GONE);
                    }
                    helper.setVisibility(R.id._item_progressbar, View.GONE);
                } else {
                    helper.setVisibility(R.id._item_img, View.GONE);
                    helper.setVisibility(R.id._item_progressbar, View.VISIBLE);
                }
                helper.setprogressbar(R.id._item_progressbar, map.getCurrent_progress());
            }
        };
        courseware.setAdapter(baseListAdapterCoursewares);
        courseware.setSelector(new ColorDrawable(Color.TRANSPARENT));
        courseware.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                com.linlic.ccmtv.yx.activity.entity.Courseware courseware_bean = null;
                courseware_bean =type.equals("1")?coursewares.get(position):coursewares2.get(position) ;
               String str= courseware_bean.getFile_path();
                ArrayList urls_huanzhexinxi = null;
                Intent intent = null;
                if (courseware_bean.is_upload()) {
                    switch (courseware_bean.getFile_name().substring(courseware_bean.getFile_name().lastIndexOf(".") + 1, courseware_bean.getFile_name().length())) {
                        case "png":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpeg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "bmp":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getFile_path());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        default:
                            Intent intent1 = new Intent(context, File_down.class);
                            intent1.putExtra("file_path", http + "" + courseware_bean.getFile_path());
                            intent1.putExtra("file_name", courseware_bean.getFile_path().substring(courseware_bean.getFile_path().lastIndexOf("/") + 1, courseware_bean.getFile_path().length()));
                            startActivity(intent1);
                            break;
                    }
                } else {
                    Toast.makeText(context, "文件正在上传中！", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //打开系统的文件管理器
    public void openFileMenu(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        intent.setType("*/*");
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 添加Category属性
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 1:
                        Uri uri = data.getData();
                        //获得选中文件的路径
//                        LogUtil.e("文件路径",uri.toString() + "\n" + "\n" + FileUtils.getFilePathByUri(getApplicationContext(),uri)+"\n"+uri.getPath());
                        String filePath = "";
//                        filePath = FileUtils.getFilePathByUri(getApplicationContext(),uri);
                        if ("file".equalsIgnoreCase(uri.getScheme())  ) {//使用第三方应用打开
                            filePath = uri.getPath();

                            //return;
                        }else{
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//24以后

                                    filePath =getFilePathFromURI(this, uri);//新的方式

                            } else
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                                filePath = getPath(this, uri);

                            } else {//4.4以下下系统调用方法
                                filePath = getRealPathFromURI(uri);

                            }
                        }

                        //获得选中文件的名称
                        String fileName =
                                filePath.substring(filePath.lastIndexOf("/") + 1);
                        if(fileName.contains("image:")  || !fileName.contains(".")){
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                                filePath = getPath(this, uri);

                            } else {//4.4以下下系统调用方法
                                filePath = getRealPathFromURI(uri);

                            }

                            //获得选中文件的名称
                              fileName =
                                    filePath.substring(filePath.lastIndexOf("/") + 1);
                        }

                        com.linlic.ccmtv.yx.activity.entity.Courseware courseware_bean = new com.linlic.ccmtv.yx.activity.entity.Courseware();
                        courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean.setCurrent_progress(0);
                        courseware_bean.setFile_name(fileName);
                        courseware_bean.setFile_path(filePath);
                        courseware_bean.setIs_upload(false);
                        coursewares.add(courseware_bean);
                        coursewares_pos.put(courseware_bean.getId(), coursewares.size() - 1);
                        //上传文件
                        startUploadFile(coursewares.get(coursewares.size() - 1), filePath);
                        //显示内容
                        baseListAdapterCoursewares.notifyDataSetChanged();
                        setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
                        LogUtil.e("文件路径：", filePath);
                        LogUtil.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
                        break;

                    default:
                        break;
                }
            }
        }

    }
    public void startUploadFile(final com.linlic.ccmtv.yx.activity.entity.Courseware pos, final String file_path) {

        LogUtil.e("文件路径", file_path);
        RequestParams params = new RequestParams();
        params.addBodyParameter("activitiesFile", new File(file_path));

        HttpUtils httpUtils = new HttpUtils();
        //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", URLConfig.activitiesUpload);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("fid", fid);
            if(id!=null && id.length()>0){
                jsonObject.put("id", id);
                jsonObject.put("type", 4);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())));

        httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.teaching_activities_upload, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
                LogUtil.e("上传成功：", result);
//                Log.e("解密后111：", result);
                try {
                    final JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject dateJson = dataJson.getJSONObject("dataList");
                            pos.setFile_path(dateJson.getString("url"));
                            pos.setFile_name(dateJson.getString("url_name"));
                            pos.setIs_upload(true);
                            http = dateJson.getString("http");
                            if (coursewares.get(coursewares_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                baseListAdapterCoursewares.getView(coursewares_pos.get(pos.getId()), courseware.getChildAt(coursewares_pos.get(pos.getId())), courseware);
                            } else {
                                for (int i = 0; i < coursewares.size(); i++) {

                                    if (coursewares.get(i).getId().equals(pos.getId())) {
                                        coursewares_pos.put(coursewares.get(i).getId(), i);
                                    }
                                }
                                baseListAdapterCoursewares.getView(coursewares_pos.get(pos.getId()), courseware.getChildAt(coursewares_pos.get(pos.getId())), courseware);
                            }
                        } else {
                            Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            coursewares.remove(pos);
                            baseListAdapterCoursewares.notifyDataSetChanged();
                        }
                    } else {
                        coursewares.remove(pos);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        baseListAdapterCoursewares.notifyDataSetChanged();
                    }
                    setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                    coursewares.remove(pos);
                    setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
                    baseListAdapterCoursewares.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e("文件路径2", 111 + "  " + file_path);
                LogUtil.e("上传失败：", s);
                LogUtil.e("错误：", e.toString());
                coursewares.remove(pos);
                baseListAdapterCoursewares.notifyDataSetChanged();
                setResultStatus((type.equals("1")?coursewares.size():coursewares2.size()) > 0, 200);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Toast.makeText(context,"正在上传，完成后将退出",Toast.LENGTH_SHORT).show();
                LogUtil.e("上传进度", current + "/" + total + "    状态：" + isUploading);
                LogUtil.e("上传进度", ((float) current / (float) total * 100) + "");


                if (isUploading) {
                    if (current / total == 1) {
                        pos.setCurrent_progress(100);
                    } else {
                        pos.setCurrent_progress((int) ((float) current / (float) total * 100));
                    }

                    if (coursewares_pos.containsKey(pos.getId()) &&  coursewares.get(coursewares_pos.get(pos.getId())).getId().equals(pos.getId())) {
                        baseListAdapterCoursewares.getView(coursewares_pos.get(pos.getId()), courseware.getChildAt(coursewares_pos.get(pos.getId())), courseware);
                    } else {
                        for (int i = 0; i < coursewares.size(); i++) {

                            if (coursewares.get(i).getId().equals(pos.getId())) {
                                coursewares_pos.put(coursewares.get(i).getId(), i);
                            }
                        }
                        if (coursewares_pos.containsKey(pos.getId())){
                                  baseListAdapterCoursewares.getView(coursewares_pos.get(pos.getId()), courseware.getChildAt(coursewares_pos.get(pos.getId())), courseware);
                        }
                    }

                }


            }
        });
    }

    /**
     * 79      * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     * 80
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    public void deleteUpload(final View view) {
       /* if (id!=null && id.length() > 0) {

            for (int i = 0; i < coursewares.size(); i++) {
                com.linlic.ccmtv.yx.activity.entity.Courseware courseware_ben = coursewares.get(i);
                if (view.getTag().toString().equals(courseware_ben.getFile_path())) {
                    del_coursewares.put(courseware_ben.getFile_path());
                    coursewares.remove(courseware_ben);
                }
            }
            baseListAdapterCoursewares.notifyDataSetChanged();
        } else {*/
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.deleteUpload);
                        obj.put("fid", fid);
                        obj.put("id", id);
                        obj.put("type", 4);

                        for (int i = 0; i < coursewares.size(); i++) {
                            com.linlic.ccmtv.yx.activity.entity.Courseware courseware_ben = coursewares.get(i);
                            if (view.getTag().toString().equals(courseware_ben.getFile_path())) {
                                obj.put("filename", courseware_ben.getFile_path());
                            }
                        }

                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("删除文件", result);
                        Message message = new Message();
                        message.what = 6;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            };
            new Thread(runnable).start();
//        }

    }

    @Override
    public void onBackPressed() {
        boolean is = false;
        if(type.equals("1")){
            for(int i = 0;i<coursewares.size();i++){
                if(!coursewares.get(i).is_upload()){
                    is = true;
                }
            }
            if(is){
                Toast.makeText(getApplicationContext(), "还有文件正在上传中，请勿关闭此页面！", Toast.LENGTH_SHORT).show();
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        boolean is = false;
        if(type.equals("1")){
            for(int i = 0;i<coursewares.size();i++){
                if(!coursewares.get(i).is_upload()){
                    is = true;
                }
            }
            if(is){
                Toast.makeText(getApplicationContext(), "还有文件正在上传中，请勿关闭此页面！", Toast.LENGTH_SHORT).show();
            }else{
                super.finish();
            }
        }else{
            super.finish();
        }
    }




    /*使用第三方软件获取文件*/

    public String getFilePathFromURI(Context context, Uri contentUri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }
}
