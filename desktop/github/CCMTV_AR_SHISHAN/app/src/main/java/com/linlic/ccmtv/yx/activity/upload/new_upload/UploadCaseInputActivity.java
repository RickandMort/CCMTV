package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadCaseGvAdapter3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Response;

public class UploadCaseInputActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private TextView tvTitleName;
    private TextView tvTitleCancel;
    private TextView tvTitleSave;
    private EditText etInputInfo;
    private GridView gvInputPic;

    private String typeName = "";
    private String typeId = "";
    private List<PhotoInfo> list = new ArrayList<>();
    private List<PhotoInfo> listResult = new ArrayList<>();
    private UploadCaseGvAdapter3 adapter;
    private String tvText = "";
    private PostRequest<String> postRequest;
    private String fileParamName = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                            if (result.has("data")) {
//                                listResult.clear();
                                for (int i = 0; i < listResult.size(); i++) {
                                    if (listResult.get(i).getPhotoPath().equals("icon_add")) {
                                        listResult.remove(i);
                                    }
                                }
                                JSONArray array = result.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject dataObject = array.getJSONObject(i);
                                    PhotoInfo photoInfo = new PhotoInfo();
                                    photoInfo.setPhotoName(dataObject.getString("name"));
                                    photoInfo.setPhotoPath(dataObject.getString("url"));
                                    listResult.add(photoInfo);
                                }
                                if (listResult.size() <= 8) {
                                    PhotoInfo p = new PhotoInfo();
                                    p.setPhotoPath("icon_add");
                                    listResult.add(p);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case_input);
        context = this;
        findId();

        getIntentData();
        init();
    }

    public void findId() {
        tvTitleName = (TextView) findViewById(R.id.id_tv_upload_case_input_titlebar_name);
        tvTitleCancel = (TextView) findViewById(R.id.id_tv_upload_case_input_titlebar_cancel);
        tvTitleSave = (TextView) findViewById(R.id.id_tv_upload_case_input_titlebar_save);
        etInputInfo = (EditText) findViewById(R.id.id_et_upload_case_input);
        gvInputPic = (GridView) findViewById(R.id.id_gv_upload_case_input);

        tvTitleCancel.setOnClickListener(this);
        tvTitleSave.setOnClickListener(this);
        gvInputPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listResult.get(listResult.size() - 1).getPhotoPath().equals("icon_add")) {
                    initImageSelect(1001, listResult);
                } else {
                    Toast.makeText(context, "您已选取了9张图片，如需更改，请先删除现有图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            typeName = intent.getStringExtra("typeName");
            typeId = intent.getStringExtra("typeId");
            listResult = (List<PhotoInfo>) intent.getSerializableExtra("list");
            tvText = intent.getStringExtra("tvText");

            tvTitleName.setText(typeName);
            if (tvText != null && !tvText.isEmpty()) {
                etInputInfo.setText(tvText);
            } else {
                etInputInfo.setHint("请输入" + typeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_upload_case_input_titlebar_cancel:
                Intent intent1 = new Intent();
                setResult(0, intent1);
                finish();
                break;

            case R.id.id_tv_upload_case_input_titlebar_save:
                for (int i = 0; i < listResult.size(); i++) {
                    if (listResult.get(i).getPhotoPath().equals("icon_add")) {
                        listResult.remove(i);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("resultList", (Serializable) listResult);
                intent.putExtra("resultInfo", etInputInfo.getText().toString());
                setResult(1, intent);
                finish();
                break;
        }
    }

    /**
     * name：给页面添加“加号”图片
     * author：MrSong
     * data：2016/10/19 13:18
     */
    private void init() {
        addIcon();
        adapter = new UploadCaseGvAdapter3(this, listResult);
        gvInputPic.setAdapter(adapter);
    }

    private void addIcon() {
        if (listResult.size() <= 8) {
            PhotoInfo p = new PhotoInfo();
            p.setPhotoPath("icon_add");
            for (int i = 0; i < listResult.size(); i++) {
                if (listResult.get(i).getPhotoPath().equals("icon_add")) {
                    listResult.remove(i);
                }
            }
            listResult.add(p);
        }
    }

    /**
     * name：GridView点击事件调用的方法，启动图片选择器
     * author：MrSong
     * data：2016/10/19 14:03
     */
    private void initImageSelect(int requestCode, List<PhotoInfo> list) {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
//        functionConfigBuilder.setSelected(list);//添加过滤集合
        functionConfigBuilder.setMutiSelectMaxSize(9 - (list.size() - 1));
//        functionConfigBuilder.setEnableCamera(true);
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        ImageLoader imageLoader = new GlideImageLoader();

        ThemeConfig.Builder builder = new ThemeConfig.Builder();
        builder.setTitleBarBgColor(Color.parseColor("#3897f9"))
                .setFabNornalColor(Color.parseColor("#3897f9"))
                .setCheckSelectedColor(Color.parseColor("#3897f9"));

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, builder.build())
                .setFunctionConfig(functionConfig).build();

        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(requestCode, functionConfig, mOnHanlderResultCallback);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                resultForList(reqeustCode, resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void resultForList(int reqeustCode, List<PhotoInfo> resultList) {
//        Log.e("resultList", resultList.toString());
        if (resultList.size() <= 8) {
            if (reqeustCode == 1001) {
                refreshView(listResult, adapter, resultList, true);
            }
        } else {
            if (reqeustCode == 1001) {
                refreshView(listResult, adapter, resultList, false);
            }
        }
    }

    //    public void deletePic(final String picName, List<PhotoInfo> resultList){
    public void deletePic(final int position) {
        final String picName = listResult.get(position).getPhotoName();
//        Log.e("删除图片后数组大小", "deletePic: 集合大小：" + listResult.size());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.delCasePicture);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("name", picName);
                    final String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadCase, obj.toString());
//                    LogUtil.e("上传删除图片返回信息数据：", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resultObject = new JSONObject(result);
                                if (resultObject.getInt("status") == 1) { //成功
                                    listResult.remove(position);
                                    adapter.notifyDataSetChanged();
                                    addIcon();
                                    Toast.makeText(context, resultObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, resultObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name：选择完照片后回调然后刷新页面
     * author：MrSong
     * data：2016/10/19 13:57
     */
    private void refreshView(List<PhotoInfo> list, UploadCaseGvAdapter3 adapter, List<PhotoInfo> resultList, boolean isAdd) {
        uploadCasePicture(resultList);
        /*PhotoInfo p = new PhotoInfo();
        p.setPhotoPath("icon_add");
        list.clear();
        list.addAll(resultList);
        if (isAdd) list.add(list.size(), p);
        adapter.notifyDataSetChanged();*/
    }

    private void uploadCasePicture(List<PhotoInfo> resultList) {
        int pictureCount = 0;
        switch (typeId) {
            case "1":
                fileParamName = "upfileA_";
                break;
            case "2":
                fileParamName = "upfileB_";
                break;
            case "3":
                fileParamName = "upfileC_";
                break;
            case "4":
                fileParamName = "upfileD_";
                break;
            case "5":
                fileParamName = "upfileE_";
                break;
            case "6":
                fileParamName = "upfileF_";
                break;
            case "7":
                fileParamName = "upfileG_";
                break;
            case "8":
                fileParamName = "upfileH_";
                break;
        }

        postRequest = OkGo.<String>post(URLConfig.ccmtvapp_uploadCase);

        for (int i = 0; i < resultList.size(); i++) {
            if (!resultList.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                postRequest.params(fileParamName + i,
                        new File(resultList.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        OkUploadCase(pictureCount, resultList);
    }

    private void OkUploadCase(int pictureCount, List<PhotoInfo> resultList) {
        MyProgressBarDialogTools.show(context);
        UploadModel uploadModel = new UploadModel();
        uploadModel.setType("case");
        uploadModel.setPicCount(pictureCount);
        uploadModel.setIconUrl(resultList.get(0).getPhotoPath());
        uploadModel.setPayType("免费");
        JSONObject object = new JSONObject();
        try {
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("act", URLConfig.uploadCasePicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest.params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .converter(new Converter<String>() {
                    @Override
                    public String convertResponse(Response response) throws Throwable {
                        try {
                            String responseString = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body().string()));
//                            Log.e("上传图片返回", "convertResponse: " + responseString);
                            Message message = new Message();
                            message.what = 1;
                            message.obj = responseString;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                        return null;
                    }
                });
        HttpParams params = postRequest.getParams();
//        Log.e("上传参数", "OkUploadCase: 参数："+params.toString());
        UploadTask<String> task = OkUpload.request("上传病例" + typeName + System.currentTimeMillis(), postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2(typeName)
                .save()
                .register(null);
        task.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
