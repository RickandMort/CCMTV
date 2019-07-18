package com.linlic.ccmtv.yx.activity.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadCaseGridViewAdapter;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadCaseService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class UploadCase2 extends BaseActivity implements AdapterView.OnItemClickListener{
    public static final String MyLog = "上传病例服务：";
    private TextView title_name;
    private Context context;
    private EditText edit_casetitle;
    private ProgressDialog pd;
    private RequestParams params = new RequestParams();
    private GridView gridview1, gridview2, gridview3, gridview4, gridview5, gridview6, gridview7, gridview8;
    private List<PhotoInfo> list1 = new ArrayList<>();
    private List<PhotoInfo> list2 = new ArrayList<>();
    private List<PhotoInfo> list3 = new ArrayList<>();
    private List<PhotoInfo> list4 = new ArrayList<>();
    private List<PhotoInfo> list5 = new ArrayList<>();
    private List<PhotoInfo> list6 = new ArrayList<>();
    private List<PhotoInfo> list7 = new ArrayList<>();
    private List<PhotoInfo> list8 = new ArrayList<>();

    private UploadCaseGridViewAdapter adapter1;
    private UploadCaseGridViewAdapter adapter2;
    private UploadCaseGridViewAdapter adapter3;
    private UploadCaseGridViewAdapter adapter4;
    private UploadCaseGridViewAdapter adapter5;
    private UploadCaseGridViewAdapter adapter6;
    private UploadCaseGridViewAdapter adapter7;
    private UploadCaseGridViewAdapter adapter8;

    private List<String> path1_must = new ArrayList<>();
    private List<String> path2_must = new ArrayList<>();
    private List<String> path3_must = new ArrayList<>();

    private PostRequest<String> postRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case2);

        context = this;
        findId();
        init();
        setOnClick();

        postRequest = OkGo.<String>post(URLConfig.ccmtvapp_uploadCase);
    }

    public void uploadManager(View view) {
        Intent intent1 = new Intent(UploadCase2.this, IsUpload2.class);
        intent1.putExtra("TAG", "Video");
        startActivity(intent1);
        finish();
    }

    /**
     * name：给页面添加“加号”图片
     * author：MrSong
     * data：2016/10/19 13:18
     */
    private void init() {
        PhotoInfo p = new PhotoInfo();
        p.setPhotoPath("icon_add");
        list1.add(p);
        list2.add(p);
        list3.add(p);
        list4.add(p);
        list5.add(p);
        list6.add(p);
        list7.add(p);
        list8.add(p);
        adapter1 = new UploadCaseGridViewAdapter(context, list1);
        adapter2 = new UploadCaseGridViewAdapter(context, list2);
        adapter3 = new UploadCaseGridViewAdapter(context, list3);
        adapter4 = new UploadCaseGridViewAdapter(context, list4);
        adapter5 = new UploadCaseGridViewAdapter(context, list5);
        adapter6 = new UploadCaseGridViewAdapter(context, list6);
        adapter7 = new UploadCaseGridViewAdapter(context, list7);
        adapter8 = new UploadCaseGridViewAdapter(context, list8);
        gridview1.setAdapter(adapter1);
        gridview2.setAdapter(adapter2);
        gridview3.setAdapter(adapter3);
        gridview4.setAdapter(adapter4);
        gridview5.setAdapter(adapter5);
        gridview6.setAdapter(adapter6);
        gridview7.setAdapter(adapter7);
        gridview8.setAdapter(adapter8);
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        edit_casetitle = (EditText) findViewById(R.id.edit_casetitle);
        gridview1 = (GridView) findViewById(R.id.gridview1);
        gridview2 = (GridView) findViewById(R.id.gridview2);
        gridview3 = (GridView) findViewById(R.id.gridview3);
        gridview4 = (GridView) findViewById(R.id.gridview4);
        gridview5 = (GridView) findViewById(R.id.gridview5);
        gridview6 = (GridView) findViewById(R.id.gridview6);
        gridview7 = (GridView) findViewById(R.id.gridview7);
        gridview8 = (GridView) findViewById(R.id.gridview8);

        title_name.setText("上传病例");
    }

    private void setOnClick() {
        gridview1.setOnItemClickListener(this);
        gridview2.setOnItemClickListener(this);
        gridview3.setOnItemClickListener(this);
        gridview4.setOnItemClickListener(this);
        gridview5.setOnItemClickListener(this);
        gridview6.setOnItemClickListener(this);
        gridview7.setOnItemClickListener(this);
        gridview8.setOnItemClickListener(this);
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
        if (resultList.size() <= 7) {
            if (reqeustCode == 1001) {
                refreshView(list1, adapter1, resultList, true);
            } else if (reqeustCode == 1002) {
                refreshView(list2, adapter2, resultList, true);
            } else if (reqeustCode == 1003) {
                refreshView(list3, adapter3, resultList, true);
            } else if (reqeustCode == 1004) {
                refreshView(list4, adapter4, resultList, true);
            } else if (reqeustCode == 1005) {
                refreshView(list5, adapter5, resultList, true);
            } else if (reqeustCode == 1006) {
                refreshView(list6, adapter6, resultList, true);
            } else if (reqeustCode == 1007) {
                refreshView(list7, adapter7, resultList, true);
            } else if (reqeustCode == 1008) {
                refreshView(list8, adapter8, resultList, true);
            }
        } else {
            if (reqeustCode == 1001) {
                refreshView(list1, adapter1, resultList, false);
            } else if (reqeustCode == 1002) {
                refreshView(list2, adapter2, resultList, false);
            } else if (reqeustCode == 1003) {
                refreshView(list3, adapter3, resultList, false);
            } else if (reqeustCode == 1004) {
                refreshView(list4, adapter4, resultList, false);
            } else if (reqeustCode == 1005) {
                refreshView(list5, adapter5, resultList, false);
            } else if (reqeustCode == 1006) {
                refreshView(list6, adapter6, resultList, false);
            } else if (reqeustCode == 1007) {
                refreshView(list7, adapter7, resultList, false);
            } else if (reqeustCode == 1008) {
                refreshView(list8, adapter8, resultList, false);
            }
        }
    }

    /**
     * name：选择完照片后回调然后刷新页面
     * author：MrSong
     * data：2016/10/19 13:57
     */
    private void refreshView(List<PhotoInfo> list, UploadCaseGridViewAdapter adapter, List<PhotoInfo> resultList, boolean isAdd) {
        PhotoInfo p = new PhotoInfo();
        p.setPhotoPath("icon_add");
        list.clear();
        list.addAll(resultList);
        if (isAdd) list.add(list.size(), p);
        adapter.notifyDataSetChanged();
    }

    /**
     * name：GridView点击事件
     * author：MrSong
     * data：2016/10/19 14:02
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gridview1:
                initImageSelect(1001, list1);
                break;
            case R.id.gridview2:
                initImageSelect(1002, list2);
                break;
            case R.id.gridview3:
                initImageSelect(1003, list3);
                break;
            case R.id.gridview4:
                initImageSelect(1004, list4);
                break;
            case R.id.gridview5:
                initImageSelect(1005, list5);
                break;
            case R.id.gridview6:
                initImageSelect(1006, list6);
                break;
            case R.id.gridview7:
                initImageSelect(1007, list7);
                break;
            case R.id.gridview8:
                initImageSelect(1008, list8);
                break;
        }
    }

    /**
     * name：GridView点击事件调用的方法，启动图片选择器
     * author：MrSong
     * data：2016/10/19 14:03
     */
    private void initImageSelect(int requestCode, List<PhotoInfo> list) {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setSelected(list);//添加过滤集合
        functionConfigBuilder.setMutiSelectMaxSize(8);
//        functionConfigBuilder.setEnableCamera(true);
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        ImageLoader imageLoader = new GlideImageLoader();
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(requestCode, functionConfig, mOnHanlderResultCallback);
    }

    /**
     * name：提交
     * author：MrSong
     * data：2016/10/19 14:11
     */
    public void commitCase(View view) {

        path1_must.clear();
        path2_must.clear();
        path3_must.clear();
        int pictureCount = 0;//统计每次上传案例时的图片总数， 用于后面上传列表显示
        if (MyUploadCaseService.now_statu == MyUploadCaseService.upload_progress) {
            Toast.makeText(getApplicationContext(), "当前任务已正在上传，请等待上传完毕后在提交", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edit_casetitle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "病例名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < list1.size(); i++) {

            if (!list1.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                path1_must.add(list1.get(i).getPhotoPath());
                params.addBodyParameter("upfileA_" + i, new File(list1.get(i).getPhotoPath()));
                postRequest.params("upfileA_" + i, new File(list1.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        if (path1_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "患者信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < list2.size(); i++) {
            if (!list2.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                path2_must.add(list2.get(i).getPhotoPath());
                params.addBodyParameter("upfileB_" + i, new File(list2.get(i).getPhotoPath()));
                postRequest.params("upfileB_" + i, new File(list2.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        if (path2_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "病史不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < list3.size(); i++) {
            if (!list3.get(i).getPhotoPath().contains("icon_add")) {   //去掉加号
                //key+i为上传的参数，后面为图片路径
                path3_must.add(list3.get(i).getPhotoPath());
                params.addBodyParameter("upfileC_" + i, new File(list3.get(i).getPhotoPath()));
                postRequest.params("upfileC_" + i, new File(list3.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        if (path3_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "治疗不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < list4.size(); i++) {
            if (!list4.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileD_" + i, new File(list4.get(i).getPhotoPath()));
                postRequest.params("upfileD_" + i, new File(list4.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        for (int i = 0; i < list5.size(); i++) {
            if (!list5.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileE_" + i, new File(list5.get(i).getPhotoPath()));
                postRequest.params("upfileE_" + i, new File(list5.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        for (int i = 0; i < list6.size(); i++) {
            if (!list6.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileF_" + i, new File(list6.get(i).getPhotoPath()));
                postRequest.params("upfileF_" + i, new File(list6.get(i).getPhotoPath()));
                pictureCount++;
            }
        }
        for (int i = 0; i < list7.size(); i++) {
            if (!list7.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileG_" + i, new File(list7.get(i).getPhotoPath()));
                postRequest.params("upfileG_" + i, new File(list7.get(i).getPhotoPath()));
                pictureCount++;
            }
        }

        for (int i = 0; i < list8.size(); i++) {
            if (!list8.get(i).getPhotoPath().contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileH_" + i, new File(list8.get(i).getPhotoPath()));
                postRequest.params("upfileH_" + i, new File(list8.get(i).getPhotoPath()));
                pictureCount++;
            }
        }

        //存储数据  FileSizeUtil.FormetFileSize(TotalSize)
        /*MyDbUtils.saveUploadCaseMsg(context, edit_casetitle.getText().toString(), MyUploadCaseService.upload_wait + "", upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //获取当前保存数据的ID
        int case_id = MyDbUtils.findUploadCaseMsg(context, upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //发送广播
        Intent intent = new Intent();
        intent.setAction("upload_case");
        intent.putExtra("case_title", edit_casetitle.getText().toString());
        intent.putExtra("case_id", case_id);
        sendBroadcast(intent);*/
        //启动服务
        //bindService(new Intent(context, MyUploadCaseService.class), connection, BIND_AUTO_CREATE);

        /**
         * 尝试更改上传为okUpload
         *
         */
        OkUploadCase(pictureCount);

        Toast.makeText(context, "病例已提交，可在上传列表中查看", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, IsUpload2.class);
        intent1.putExtra("TAG", "Case");
        startActivity(intent1);
        finish();
    }

    private void OkUploadCase(int pictureCount) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.setName(edit_casetitle.getText().toString());
        uploadModel.setType("case");
        uploadModel.setPicCount(pictureCount);
        uploadModel.setIconUrl(list1.get(0).getPhotoPath());
        JSONObject object = new JSONObject();
        try {
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("title", edit_casetitle.getText().toString());
            object.put("act", URLConfig.uploadCase);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest.params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .converter(new StringConvert());
        HttpParams params=postRequest.getParams();
        UploadTask<String> task= OkUpload.request(object.toString(),postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2(edit_casetitle.getText().toString()+"")
                .save()
                .register(new LogUploadListener<String>());
        task.start();
    }

    @Override
    public void onBackPressed() {
        if (pd != null) {
            if (!pd.isShowing()) {
                finish();
            }
        }
        super.onBackPressed();
    }

    public void back(View view) {
        finish();
    }
}
