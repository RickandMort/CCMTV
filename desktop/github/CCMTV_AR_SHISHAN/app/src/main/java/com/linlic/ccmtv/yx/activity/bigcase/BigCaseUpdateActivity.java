package com.linlic.ccmtv.yx.activity.bigcase;

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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.R.id.Feedback;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.getDataColumn;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isDownloadsDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isExternalStorageDocument;
import static com.linlic.ccmtv.yx.utils.GetPathFromUri4kitkat.isMediaDocument;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseUpdateActivity extends BaseActivity{
    @Bind(R.id.edit_bigcase_name)
    EditText mEditBigcaseName;
    @Bind(Feedback)
    MyGridView photo;
    @Bind(R.id.tv_submit)
    TextView mTvSubmit;

    private Context context;
    private String caseId;
    private String uid;
    List<Courseware> photos = new ArrayList<>();
    Map<String, Integer> photos_pos = new HashMap<>();
    private BaseListAdapter baseListAdapterphoto;
    private String http = "";
    private String id;
    private String deletePath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://查看
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONObject dateJson1 = dataJson.getJSONObject("data");
//                                listData.clear();
                                http = dateJson1.getString("img_url");

                                JSONObject data = dateJson1.getJSONObject("data");
                                mEditBigcaseName.setText(data.getString("title"));
//                                id = data.getString("id");
                                JSONArray imgs = data.getJSONArray("img");
                                LogUtil.e("ddd",imgs.toString());
                                for (int i = 0; i < imgs.length(); i++) {
                                    String imgData = imgs.getString(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_path(imgData);
//                                    if (dateJson.getString("status_sub").equals("1") || dateJson.getString("status_sub").equals("2") || dateJson.getString("status_sub").equals("3")) {
                                        courseware_bean.setIs_upload(true);
//                                    } else {
//                                        courseware_bean.setIs_upload(false);
//                                    }
                                    if (is_edit!=0) {
                                        courseware_bean.setId("" + (i + 1));
                                    }else {
                                        courseware_bean.setId("" + i);
                                    }
                                    photos.add(courseware_bean);
                                    photos_pos.put(courseware_bean.getId(), photos.size() - 1);
                                }



                                baseListAdapterphoto.notifyDataSetChanged();

//                                if (photos.size() < 1) {
//                                    photo_layout.setVisibility(View.GONE);
//                                } else {
//                                    photo_layout.setVisibility(View.VISIBLE);
//                                }
                                baseListAdapterphoto.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3://删除图片
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for (int i = 0; i < photos.size(); i++) {
                                    Courseware courseware_ben = photos.get(i);
                                    //返回删除图片---------
                                    if (deletePath.equals(courseware_ben.getFile_path())) {
                                        photos.remove(courseware_ben);
                                    }
                                }
                                baseListAdapterphoto.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4://大病例添加
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                MonthBigCaseActivity.is_new = 1;
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 5://大病例编辑
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(getApplicationContext(), "编辑成功", Toast.LENGTH_SHORT).show();
                                MonthBigCaseActivity.is_new = 1;
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    private String addPath;
    private int is_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bigcaseupdate);
        context = this;
        ButterKnife.bind(this);
        caseId = getIntent().getStringExtra("case_id");
        id = getIntent().getStringExtra("id");
        is_edit = getIntent().getIntExtra("is_edit", 1);//0查看  1可编辑
        initData();
        if (!TextUtils.isEmpty(id)) {
            //查看
            getBigCaseInfo();
        }
        if (is_edit == 0){
            mEditBigcaseName.setEnabled(false);
            mTvSubmit.setVisibility(View.GONE);
        } else {
            mEditBigcaseName.setEnabled(true);
            mTvSubmit.setVisibility(View.VISIBLE);
        }
    }

    private void initData(){
        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEditBigcaseName.getText())) {
                    if (!TextUtils.isEmpty(id)){
                        //编辑
                        editBigCase();
                    } else {
                        //添加
                        addBigCase();
                    }
                } else {
                    ToastUtils.makeText(BigCaseUpdateActivity.this,"请输入名称");
                }
            }
        });

        baseListAdapterphoto = new BaseListAdapter(photo, photos, R.layout.item_photo) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Courseware map = (Courseware) item;
                helper.setTag(R.id._item_del, map.getId());
                if (map.getId().equals("0")) {
                    if (is_edit != 0) {
                        helper.setImage(R.id.item_photo, R.mipmap.upload_addpic);
                    } else {
                        helper.setImageBitmap(R.id.item_photo, http + "" + map.getFile_path());
                    }
                } else {
                    //图片为半路径
                    helper.setImageBitmap(R.id.item_photo, http + "" + map.getFile_path());
                }
                if (map.is_upload()) {
                    if (is_edit != 0) {
                        helper.setVisibility(R.id._item_del, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id._item_del, View.GONE);
                    }
                } else {
                    if (is_edit == 0){
                        helper.setVisibility(R.id.item_photo, View.GONE);
                    }
                    helper.setVisibility(R.id._item_del, View.GONE);
                }
            }
        };
        photo.setAdapter(baseListAdapterphoto);
        photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = 0;
                if (position != 0) {
                    if (photos.get(position).is_upload()) {
                        ArrayList urls_huanzhexinxi = new ArrayList();
                        for (Courseware courseware : photos) {
                            if (courseware.is_upload()) {
                                urls_huanzhexinxi.add(http + "" + courseware.getFile_path());
                                if (courseware.getId().equals(photos.get(position).getId())) {
                                    pos = urls_huanzhexinxi.size() - 1;
                                }
                            }
                        }
                        Intent intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", pos);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "图片上传中", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (is_edit!=0) {
                        if (photos.get(position).is_upload()) {
                            ArrayList urls_huanzhexinxi = new ArrayList();
                            for (Courseware courseware : photos) {
                                if (courseware.is_upload()) {
                                    urls_huanzhexinxi.add(courseware.getFile_path());
                                    if (courseware.getId().equals(photos.get(position).getId())) {
                                        pos = urls_huanzhexinxi.size() - 1;
                                    }
                                }
                            }
                            Intent intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", pos);
                            startActivity(intent);
                        } else {
                            //添加图片
                            openFileMenu(photo, 1);
                        }
                    } else {
                        if (photos.get(position).is_upload()) {
                            ArrayList urls_huanzhexinxi = new ArrayList();
                            for (Courseware courseware : photos) {
                                if (courseware.is_upload()) {
                                    urls_huanzhexinxi.add(http + "" + courseware.getFile_path());
                                    if (courseware.getId().equals(photos.get(position).getId())) {
                                        pos = urls_huanzhexinxi.size() - 1;
                                    }
                                }
                            }
                            Intent intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", pos);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "图片上传中", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

//        status_img.setImageResource(R.mipmap.training_34);
//        approval_layout.setVisibility(View.VISIBLE);
//        approval_status.setText("进行中");
//        reason_for_review.setVisibility(View.GONE);
//        qr_code_layout.setVisibility(View.VISIBLE);
//        close_layout.setVisibility(View.VISIBLE);
//        submit_layout2.setVisibility(View.VISIBLE);
        Courseware courseware_bean1 = new Courseware();
        courseware_bean1.setIs_upload(false);
        courseware_bean1.setId("0");

        if (is_edit != 0) {
            photos.add(courseware_bean1);
            photos_pos.put(courseware_bean1.getId(), photos.size() - 1);
            baseListAdapterphoto.notifyDataSetChanged();
        }
    }

    //打开系统的文件管理器
    public void openFileMenu(View view, int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        intent.setType("image/*");
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 添加Category属性
        try {
            startActivityForResult(intent, code);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                //获得选中文件的路径
                String filePath = "";
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    filePath = uri.getPath();

                    //return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    filePath = getPath(this, uri);

                } else {//4.4以下下系统调用方法
                    filePath = getRealPathFromURI(uri);

                }

                //获得选中文件的名称
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                Courseware courseware_bean = null;
                switch (requestCode) {
                    case 1:

                        courseware_bean = new Courseware();
                        courseware_bean.setId(DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss"));
                        courseware_bean.setCurrent_progress(0);
                        courseware_bean.setFile_name(fileName);///upload/photo/big_case/21/xh1/154459326153.jpg
                        courseware_bean.setFile_path(filePath);///storage/emulated/0/DCIM/Screenshots/SmartSelectImage_2018-09-12-14-31-53.png
                        courseware_bean.setIs_upload(false);
//                        courseware_bean.setFile_path(addPath);
                        photos.add(courseware_bean);
                        photos_pos.put(courseware_bean.getId(), photos.size() - 1);
                        //上传文件
                        startUploadFile(photos.get(photos.size() - 1), filePath);

                        //显示内容
                        baseListAdapterphoto.notifyDataSetChanged();

//                        Log.e("文件路径：", filePath);
//                        Log.e("文件名称：", filePath.substring(filePath.lastIndexOf("/") + 1));
                        break;
                    default:
                        break;
                }

            }
        }
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

    public void startUploadFile(final Courseware pos, String file_path) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", URLConfig.userUploadImg);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 保存数据到服务器
         */
        //上传单个文件
        OkGo.<String>post(URLConfig.CCMTVAPP_GpApi)
                .tag(this)
                .params("activitiesFile", new File(file_path))
                .params("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())))
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String result = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body()));
//                Log.e("上传成功：", responseInfo.result);
                Log.e("解密后图片上传：", result);
                        try {
                            final JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getInt("code") == 200) {
                                JSONObject dataJson = jsonObject.getJSONObject("data");
                                if (dataJson.getInt("status") == 1) { // 成功
                                    JSONObject dateJson = dataJson.getJSONObject("data");
                                    http = dateJson.getString("img_url");
                                    JSONArray img_lists = dateJson.getJSONArray("img_list");
                                    addPath = img_lists.getString(0);
//                                    pos.setFile_path(dateJson.getString("img_list"));
                                    pos.setFile_path(addPath);

//                                    pos.setFile_name(dateJson.getString("url_name"));
                                    pos.setIs_upload(true);

//                                    photos.add(courseware_bean);
//                                    photos_pos.put(courseware_bean.getId(), photos.size() - 1);

                                    if (photos.get(photos_pos.get(pos.getId())).getId().equals(pos.getId())) {
                                        baseListAdapterphoto.getView(photos_pos.get(pos.getId()), photo.getChildAt(photos_pos.get(pos.getId())), photo);
                                    } else {
                                        for (int i = 0; i < photos.size(); i++) {

                                            if (photos.get(i).getId().equals(pos.getId())) {
                                                photos_pos.put(photos.get(i).getId(), i);
//                                                pos.setId(i+"");
                                            }
                                        }
                                        baseListAdapterphoto.getView(photos_pos.get(pos.getId()), photo.getChildAt(photos_pos.get(pos.getId())), photo);
//                                        photos.remove(photos_pos.get(pos.getId()));
//                                        photos.add(pos);
//                                        baseListAdapterphoto.getView(photos_pos.get(pos.getId()), photo.getChildAt(photos_pos.get(pos.getId())), photo);
                                    }
//                                    baseListAdapterphoto.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                    photos.remove(pos);
                                    baseListAdapterphoto.notifyDataSetChanged();
                                }
                            } else {
                                photos.remove(pos);
                                baseListAdapterphoto.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            photos.remove(pos);
                            baseListAdapterphoto.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        LogUtil.e("上传失败：", response.message());
                        LogUtil.e("错误：", response.getException().toString());
                        photos.remove(pos);
                        baseListAdapterphoto.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 大病历用户已上传列表查看
     */
    private void getBigCaseInfo(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseUserUploadInfo);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("大病历用户已上传列表查看", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /***
     * 删除图片
     * @param view
     */
    public void deleteUpload(final View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userDelImg);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    for (int i = 0; i < photos.size(); i++) {
                        Courseware courseware_ben = photos.get(i);
                        if (view.getTag().toString().equals(courseware_ben.getId())) {
                            obj.put("img", courseware_ben.getFile_path());
                            deletePath = courseware_ben.getFile_path();
                        }
                    }
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /***
     * 大病例添加
     */
    public void addBigCase() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userAddBigCase);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("case_id", caseId);
                    //tittle不可为空
                    obj.put("title", mEditBigcaseName.getText());
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < photos.size(); i++) {
                        Courseware courseware_ben = photos.get(i);
                        if (!TextUtils.isEmpty(courseware_ben.getFile_path())) {
                            jsonArray.put(courseware_ben.getFile_path());
                        }

                        //["\/upload\/photo\/big_case\/21\/xh1\/1545130660_195326.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_659374.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_438831.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_216163.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_253235.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_822897.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_925782.png"]
                    }
                    obj.put("img", jsonArray.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /***
     * 大病例编辑
     */
    public void editBigCase() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userEditBigCase);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    //tittle不可为空
                    obj.put("title", mEditBigcaseName.getText());
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < photos.size(); i++) {
                        Courseware courseware_ben = photos.get(i);
                        if (!TextUtils.isEmpty(courseware_ben.getFile_path())) {
                            jsonArray.put(courseware_ben.getFile_path());
                        }

                        //["\/upload\/photo\/big_case\/21\/xh1\/1545130660_195326.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_659374.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_438831.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_216163.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_253235.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_822897.png","\/upload\/photo\/big_case\/21\/xh1\/1545130660_925782.png"]
                    }

                    obj.put("img", jsonArray.toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    Message message = new Message();
                    message.what = 5;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Bigcase/index.html";
        super.onPause();
    }

}
