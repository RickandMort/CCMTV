package com.linlic.ccmtv.yx.activity.upload;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lidroid.xutils.http.RequestParams;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
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
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * name：上传病例
 * author：MrSong
 * data：2016/3/26 19:37
 * https://github.com/YancyYe/GalleryPick
 */
public class Upload_case extends BaseActivity implements View.OnClickListener {
    private MyUploadCaseService.MyBinder myBinder;
    private ArrayList<String> path1 = new ArrayList<String>();
    private ArrayList<String> path2 = new ArrayList<String>();
    private ArrayList<String> path3 = new ArrayList<String>();
    private ArrayList<String> path4 = new ArrayList<String>();
    private ArrayList<String> path5 = new ArrayList<String>();
    private ArrayList<String> path6 = new ArrayList<String>();
    private ArrayList<String> path7 = new ArrayList<String>();
    private ArrayList<String> path8 = new ArrayList<String>();
    ArrayList<String> pathList1 = new ArrayList<>();
    ArrayList<String> pathList2 = new ArrayList<>();
    ArrayList<String> pathList3 = new ArrayList<>();
    ArrayList<String> pathList4 = new ArrayList<>();
    ArrayList<String> pathList5 = new ArrayList<>();
    ArrayList<String> pathList6 = new ArrayList<>();
    ArrayList<String> pathList7 = new ArrayList<>();
    ArrayList<String> pathList8 = new ArrayList<>();


    private ArrayList<String> path1_must = new ArrayList<>();
    private ArrayList<String> path2_must = new ArrayList<>();
    private ArrayList<String> path7_must = new ArrayList<>();

    private String upfileA_1;//患者信息第一张图片路径（SD卡路径）
    private String upfileA_2;//患者信息第一张图片路径（SD卡路径）
    private String upfileA_3;//患者信息第一张图片路径（SD卡路径）

    private String upfileB_1;//病史第一张图片路径（SD卡路径）
    private String upfileB_2;//病史第一张图片路径（SD卡路径）
    private String upfileB_3;//病史第一张图片路径（SD卡路径）

    private String upfileC_1;//临床表现第一张图片路径（SD卡路径）
    private String upfileC_2;//临床表现第一张图片路径（SD卡路径）
    private String upfileC_3;//临床表现第一张图片路径（SD卡路径）

    private String upfileD_1;//辅助检查第一张图片路径（SD卡路径）
    private String upfileD_2;//辅助检查第一张图片路径（SD卡路径）
    private String upfileD_3;//辅助检查第一张图片路径（SD卡路径）

    private String upfileE_1;//特殊检查第一张图片路径（SD卡路径）
    private String upfileE_2;//特殊检查第一张图片路径（SD卡路径）
    private String upfileE_3;//特殊检查第一张图片路径（SD卡路径）

    private String upfileF_1;//诊断第一张图片路径（SD卡路径）
    private String upfileF_2;//诊断第一张图片路径（SD卡路径）
    private String upfileF_3;//诊断第一张图片路径（SD卡路径）

    private String upfileG_1;//治疗第一张图片路径（SD卡路径）
    private String upfileG_2;//治疗第一张图片路径（SD卡路径）
    private String upfileG_3;//治疗第一张图片路径（SD卡路径）

    private String upfileH_1;//手术第一张图片路径（SD卡路径）
    private String upfileH_2;//手术第一张图片路径（SD卡路径）
    private String upfileH_3;//手术第一张图片路径（SD卡路径）
    private RequestParams params = new RequestParams();
    private ImageView iv_addPatienMsg, iv_addMedicalHistory, iv_LinchuangBiaoxian, iv_FuzhuJiancha, iv_TeshuJiancha, iv_Zhenduan, iv_Zhiliao, iv_Shoushu;
    Context context;
    //用户统计
    private String type;
    private EditText edit_casetitle;
    private LinearLayout layout_addPatienMsg, layout_LinchuangBiaoxian, layout_MedicalHistory, layout_FuzhuJiancha, layout_TeshuJiancha, layout_Zhenduan, layout_Zhiliao, layout_Shoushu;
    private LinearLayout layout_addPatienMsgbig, layout_LinchuangBiaoxianbig, layout_MedicalHistorybig, layout_FuzhuJianchabig, layout_TeshuJianchabig, layout_Zhenduanbig, layout_Zhiliaobig, layout_Shoushubig;
    private PostRequest<String> postRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_case);
        SharedPreferencesTools.getUid(Upload_case.this);
        context = this;

        type = getIntent().getStringExtra("type");

        findId();
        setOnClick();

        postRequest = OkGo.<String>post(URLConfig.ccmtvapp_uploadCase);
    }

    public void findId() {
        super.findId();
        super.setActivity_title_name(R.string.upload_case);

        iv_addPatienMsg = (ImageView) findViewById(R.id.iv_addPatienMsg);
        iv_addMedicalHistory = (ImageView) findViewById(R.id.iv_addMedicalHistory);
        iv_LinchuangBiaoxian = (ImageView) findViewById(R.id.iv_LinchuangBiaoxian);
        iv_FuzhuJiancha = (ImageView) findViewById(R.id.iv_FuzhuJiancha);
        iv_TeshuJiancha = (ImageView) findViewById(R.id.iv_TeshuJiancha);
        iv_Zhenduan = (ImageView) findViewById(R.id.iv_Zhenduan);
        iv_Zhiliao = (ImageView) findViewById(R.id.iv_Zhiliao);
        iv_Shoushu = (ImageView) findViewById(R.id.iv_Shoushu);
        edit_casetitle = (EditText) findViewById(R.id.edit_casetitle);

        layout_addPatienMsg = (LinearLayout) findViewById(R.id.layout_addPatienMsg);
        layout_MedicalHistory = (LinearLayout) findViewById(R.id.layout_MedicalHistory);
        layout_LinchuangBiaoxian = (LinearLayout) findViewById(R.id.layout_LinchuangBiaoxian);
        layout_FuzhuJiancha = (LinearLayout) findViewById(R.id.layout_FuzhuJiancha);
        layout_TeshuJiancha = (LinearLayout) findViewById(R.id.layout_TeshuJiancha);
        layout_Zhenduan = (LinearLayout) findViewById(R.id.layout_Zhenduan);
        layout_Zhiliao = (LinearLayout) findViewById(R.id.layout_Zhiliao);
        layout_Shoushu = (LinearLayout) findViewById(R.id.layout_Shoushu);

        layout_addPatienMsgbig = (LinearLayout) findViewById(R.id.layout_addPatienMsgbig);
        layout_MedicalHistorybig = (LinearLayout) findViewById(R.id.layout_MedicalHistorybig);
        layout_LinchuangBiaoxianbig = (LinearLayout) findViewById(R.id.layout_LinchuangBiaoxianbig);
        layout_FuzhuJianchabig = (LinearLayout) findViewById(R.id.layout_FuzhuJianchabig);
        layout_TeshuJianchabig = (LinearLayout) findViewById(R.id.layout_TeshuJianchabig);
        layout_Zhenduanbig = (LinearLayout) findViewById(R.id.layout_Zhenduanbig);
        layout_Zhiliaobig = (LinearLayout) findViewById(R.id.layout_Zhiliaobig);
        layout_Shoushubig = (LinearLayout) findViewById(R.id.layout_Shoushubig);
    }

    public void setOnClick() {
        //  iv_addPatienMsg.setOnClickListener(this);
        //   iv_addMedicalHistory.setOnClickListener(this);
        //   iv_LinchuangBiaoxian.setOnClickListener(this);
        //   iv_FuzhuJiancha.setOnClickListener(this);
        //   iv_TeshuJiancha.setOnClickListener(this);
        //   iv_Zhenduan.setOnClickListener(this);
        //   iv_Zhiliao.setOnClickListener(this);
        //   iv_Shoushu.setOnClickListener(this);
        layout_addPatienMsgbig.setOnClickListener(this);
        layout_MedicalHistorybig.setOnClickListener(this);
        layout_LinchuangBiaoxianbig.setOnClickListener(this);
        layout_FuzhuJianchabig.setOnClickListener(this);
        layout_TeshuJianchabig.setOnClickListener(this);
        layout_Zhenduanbig.setOnClickListener(this);
        layout_Zhiliaobig.setOnClickListener(this);
        layout_Shoushubig.setOnClickListener(this);

    }
    public void uploadManager(View view) {
        Intent intent1 = new Intent(Upload_case.this, IsUpload2.class);
        intent1.putExtra("TAG", "Video");
        startActivity(intent1);
        finish();
    }


    public void commitCase(View view) {

        path1_must.clear();
        path2_must.clear();
        path7_must.clear();
        int pictureCount = 0;//统计每次上传案例时的图片总数， 用于后面上传列表显示
        if (MyUploadCaseService.now_statu == MyUploadCaseService.upload_progress) {
            Toast.makeText(getApplicationContext(), "当前任务已正在上传，请等待上传完毕后在提交", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(edit_casetitle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "病例名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < path1.size(); i++) {

            if (!path1.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                path1_must.add(path1.get(i));
                params.addBodyParameter("upfileA_" + i, new File(path1.get(i)));
                postRequest.params("upfileA_" + i, new File(path1.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileA_1 = path1.get(i);
                        break;
                    case 1:
                        upfileA_2 = path1.get(i);
                        break;
                    case 2:
                        upfileA_3 = path1.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        if (path1_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "患者信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < path2.size(); i++) {
            if (!path2.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                path2_must.add(path2.get(i));
                params.addBodyParameter("upfileB_" + i, new File(path2.get(i)));
                postRequest.params("upfileB_" + i, new File(path2.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileB_1 = path2.get(i);
                        break;
                    case 1:
                        upfileB_2 = path2.get(i);
                        break;
                    case 2:
                        upfileB_3 = path2.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        if (path2_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "病史不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < path3.size(); i++) {
            if (!path3.get(i).contains("icon_add")) {   //去掉加号
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileC_" + i, new File(path3.get(i)));
                postRequest.params("upfileC_" + i, new File(path3.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileC_1 = path3.get(i);
                        break;
                    case 1:
                        upfileC_2 = path3.get(i);
                        break;
                    case 2:
                        upfileC_3 = path3.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < path4.size(); i++) {
            if (!path4.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileD_" + i, new File(path4.get(i)));
                postRequest.params("upfileD_" + i, new File(path4.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileD_1 = path4.get(i);
                        break;
                    case 1:
                        upfileD_2 = path4.get(i);
                        break;
                    case 2:
                        upfileD_3 = path4.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < path5.size(); i++) {
            if (!path5.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileE_" + i, new File(path5.get(i)));
                postRequest.params("upfileE_" + i, new File(path5.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileE_1 = path5.get(i);
                        break;
                    case 1:
                        upfileE_2 = path5.get(i);
                        break;
                    case 2:
                        upfileE_3 = path5.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < path6.size(); i++) {
            if (!path6.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileF_" + i, new File(path6.get(i)));
                postRequest.params("upfileF_" + i, new File(path6.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileF_1 = path6.get(i);
                        break;
                    case 1:
                        upfileF_2 = path6.get(i);
                        break;
                    case 2:
                        upfileF_3 = path6.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        for (int i = 0; i < path7.size(); i++) {
            if (!path7.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                path7_must.add(path7.get(i));
                params.addBodyParameter("upfileG_" + i, new File(path7.get(i)));
                postRequest.params("upfileG_" + i, new File(path7.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileG_1 = path7.get(i);
                        break;
                    case 1:
                        upfileG_2 = path7.get(i);
                        break;
                    case 2:
                        upfileG_3 = path7.get(i);
                        break;
                    default:
                        break;
                }
            }
        }
        if (path7_must.size() <= 0) {
            Toast.makeText(getApplicationContext(), "治疗不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < path8.size(); i++) {
            if (!path8.get(i).contains("icon_add")) {
                //key+i为上传的参数，后面为图片路径
                params.addBodyParameter("upfileH_" + i, new File(path8.get(i)));
                postRequest.params("upfileH_" + i, new File(path8.get(i)));
                pictureCount++;
                switch (i) {
                    case 0:
                        upfileH_1 = path8.get(i);
                        break;
                    case 1:
                        upfileH_2 = path8.get(i);
                        break;
                    case 2:
                        upfileH_3 = path8.get(i);
                        break;
                    default:
                        break;
                }
            }
        }

        //存储数据  FileSizeUtil.FormetFileSize(TotalSize)
        /*MyDbUtils.saveUploadCaseMsg(Upload_case.this, edit_casetitle.getText().toString(), MyUploadCaseService.upload_wait + "", upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //获取当前保存数据的ID
        int case_id = MyDbUtils.findUploadCaseMsg(Upload_case.this, upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //发送广播
        Intent intent = new Intent();
        intent.setAction("upload_case");
        intent.putExtra("case_title", edit_casetitle.getText().toString());
        intent.putExtra("case_id", case_id);
        sendBroadcast(intent);*/
        //启动服务
        //bindService(new Intent(Upload_case.this, MyUploadCaseService.class), connection, BIND_AUTO_CREATE);

        /**
         * 尝试更改上传为okUpload
         *
         */
        OkUploadCase(pictureCount);

        Toast.makeText(Upload_case.this, "病例已提交，可在上传列表中查看", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(Upload_case.this, IsUpload2.class);
        intent1.putExtra("TAG", "Case");
        startActivity(intent1);
        finish();
    }

    private void OkUploadCase(int pictureCount) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.setName(edit_casetitle.getText().toString());
        uploadModel.setType("case");
        uploadModel.setPicCount(pictureCount);
        uploadModel.setIconUrl(upfileA_1);
        JSONObject object = new JSONObject();
        try {
            object.put("uid", SharedPreferencesTools.getUid(Upload_case.this));
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


    /**
     * name：传输给service
     * author：Larry
     * data：2016/5/4 16:32
     */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            JSONObject object = new JSONObject();
            try {
                object.put("uid", SharedPreferencesTools.getUid(Upload_case.this));
                object.put("title", edit_casetitle.getText().toString());
                object.put("act", URLConfig.uploadCase);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*视频上传*/
            myBinder = (MyUploadCaseService.MyBinder) service;
            myBinder.startUploadCase(params, object.toString());

//            Log.d(MyUploadCaseService.MyLog, "Activity与Service建立关联");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Log.d(MyUploadCaseService.MyLog, "Activity与Service解除关联");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_CANCELED) {
                //返回不做判断，直接finish掉图片选择页
                judge_pathList(requestCode);
                return;
            } else {
                if (requestCode == 101) {
                    pathList1 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList1);
                } else if (requestCode == 102) {
                    pathList2 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList2);
                } else if (requestCode == 103) {
                    pathList3 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList3);
                } else if (requestCode == 104) {
                    pathList4 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList4);
                } else if (requestCode == 105) {
                    pathList5 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList5);
                } else if (requestCode == 106) {
                    pathList6 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList6);
                } else if (requestCode == 107) {
                    pathList7 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList7);
                } else if (requestCode == 108) {
                    pathList8 = data.getStringArrayListExtra("result");
                    judge(requestCode, pathList8);
                }
                return;
            }
        } catch (Exception e) {
            //未选择图片时，走这里
            e.printStackTrace();
            judge_pathList(requestCode);
            return;
        }
    }

    //判断 pathList
    private void judge_pathList(int requestCode) {
        if (requestCode == 101) {
            judge(requestCode, pathList1);
        } else if (requestCode == 102) {
            judge(requestCode, pathList2);
        } else if (requestCode == 103) {
            judge(requestCode, pathList3);
        } else if (requestCode == 104) {
            judge(requestCode, pathList4);
        } else if (requestCode == 105) {
            judge(requestCode, pathList5);
        } else if (requestCode == 106) {
            judge(requestCode, pathList6);
        } else if (requestCode == 107) {
            judge(requestCode, pathList7);
        } else if (requestCode == 108) {
            judge(requestCode, pathList8);
        }
    }

    //判断
    public void judge(int requestCode, ArrayList<String> pathList) {
        switch (requestCode) {
            case 101:
                patientMsgActivityResult(pathList);
                break;
            case 102:
                addMedicalHistoryResult(pathList);
                break;
            case 103:
                linChuangBiaoxianResult(pathList);
                break;
            case 104:
                fuZhuJianchaResult(pathList);
                break;
            case 105:
                teShuJianchaResult(pathList);
                break;
            case 106:
                zhenDuanResult(pathList);
                break;
            case 107:
                zhiLiaoResult(pathList);
                break;
            case 108:
                shouShuResult(pathList);
                break;
            default:
                break;
        }
    }

    //患者信息

    private void patientMsgActivityResult(ArrayList<String> pathList) {

        RequestOptions options = new RequestOptions().centerCrop();

        if (pathList.size() == 0) {
              //iv_addPatienMsg.setVisibility(View.VISIBLE);
        } else {
            path1.clear();
            path1.addAll(pathList);
        }

        if (path1.size() > 0 && path1.size() < 3) {
              //iv_addPatienMsg.setVisibility(View.GONE);
            path1.add(path1.size(), "icon_add");
        } else if (path1.size() >= 3) {
              //iv_addPatienMsg.setVisibility(View.GONE);
            for (String path : path1) {
                if (path.contains("icon_add")) {
                    path1.remove(path);
                }
            }
        }
        layout_addPatienMsg.removeAllViews();
        if (path1.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_addPatienMsg, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_addPatienMsg.addView(view);
        }
        for (int i = 0; i < path1.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_addPatienMsg, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            //动态加载图片
            if ("icon_add".equals(path1.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path1.get(i))
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //之前已经选择，再次打开选择器，清空选择，返回之后，再次点击图片 选择器显示上次选择后的结果
                    for (String path : path1) {
                        if ("icon_add".equals(path)) {
                            path1.remove(path);
                        }
                    }
                  /*  ImageConfig imageConfig1 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path1).requestCode(101).build();
                    ImageSelector.open(Upload_case.this, imageConfig1);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 101);
                }
            });
            layout_addPatienMsg.addView(view);
        }
    }

    //病史
    public void addMedicalHistoryResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //   iv_addMedicalHistory.setVisibility(View.VISIBLE);
        } else {
            path2.clear();
            path2.addAll(pathList);
        }

        if (path2.size() > 0 && path2.size() < 3) {
            //     iv_addMedicalHistory.setVisibility(View.GONE);
            path2.add(path2.size(), "icon_add");
        } else if (path2.size() >= 3) {
            //     iv_addMedicalHistory.setVisibility(View.GONE);
            for (String path : path2) {
                if (("icon_add").equals(path)) {
                    path2.remove(path);
                }
            }
        }

        layout_MedicalHistory.removeAllViews();
        if (path2.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_MedicalHistory, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_MedicalHistory.addView(view);
        }
        for (int i = 0; i < path2.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_MedicalHistory, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if ("icon_add".equals(path2.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path2.get(i))
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path2) {
                        if (path.contains("icon_add")) {
                            path2.remove(path);
                        }
                    }
                 /*   ImageConfig imageConfig2 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path2).requestCode(102).build();
                    ImageSelector.open(Upload_case.this, imageConfig2);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 102);
                }
            });
            layout_MedicalHistory.addView(view);
        }
    }

    //临床表现
    public void linChuangBiaoxianResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //  iv_LinchuangBiaoxian.setVisibility(View.VISIBLE);
        } else {
            path3.clear();
            path3.addAll(pathList);
        }

        if (path3.size() > 0 && path3.size() < 3) {
            //   iv_LinchuangBiaoxian.setVisibility(View.GONE);
            path3.add(path3.size(), "icon_add");
        } else if (path3.size() >= 3) {
            //    iv_LinchuangBiaoxian.setVisibility(View.GONE);
            for (String path : path3) {
                if (("icon_add").equals(path)) {
                    path3.remove(path);
                }
            }
        }
        layout_LinchuangBiaoxian.removeAllViews();
        if (path3.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_LinchuangBiaoxian, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_LinchuangBiaoxian.addView(view);
        }
        for (int i = 0; i < path3.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_LinchuangBiaoxian, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if ("icon_add".equals(path3.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path3.get(i))
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path3) {
                        if (path.contains("icon_add")) {
                            path3.remove(path);
                        }
                    }
                  /*  ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path3).requestCode(103).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 103);
                }
            });
            layout_LinchuangBiaoxian.addView(view);
        }
    }

    //辅助检查
    public void fuZhuJianchaResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //   iv_FuzhuJiancha.setVisibility(View.VISIBLE);
        } else {
            path4.clear();
            path4.addAll(pathList);
        }

        if (path4.size() > 0 && path4.size() < 3) {
            //  iv_FuzhuJiancha.setVisibility(View.GONE);
            path4.add(path4.size(), "icon_add");
        } else if (path4.size() >= 3) {
            //   iv_FuzhuJiancha.setVisibility(View.GONE);
            for (String path : path4) {
                if (("icon_add").equals(path)) {
                    path4.remove(path);
                }
            }
        }
        layout_FuzhuJiancha.removeAllViews();
        if (path4.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_FuzhuJiancha, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_FuzhuJiancha.addView(view);
        }
        for (int i = 0; i < path4.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_FuzhuJiancha, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if (("icon_add").equals(path4.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path4.get(i))
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path4) {
                        if (path.contains("icon_add")) {
                            path4.remove(path);
                        }
                    }
                   /* ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path4).requestCode(104).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 104);
                }
            });
            layout_FuzhuJiancha.addView(view);
        }
    }

    //特殊检查
    public void teShuJianchaResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //   iv_TeshuJiancha.setVisibility(View.VISIBLE);
        } else {
            path5.clear();
            path5.addAll(pathList);
        }

        if (path5.size() > 0 && path5.size() < 3) {
            //  iv_TeshuJiancha.setVisibility(View.GONE);
            path5.add(path5.size(), "icon_add");
        } else if (path5.size() >= 3) {
            //   iv_TeshuJiancha.setVisibility(View.GONE);
            for (String path : path5) {
                if ("icon_add".equals(path)) {
                    path5.remove(path);
                }
            }
        }
        layout_TeshuJiancha.removeAllViews();
        if (path5.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_TeshuJiancha, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_TeshuJiancha.addView(view);
        }
        for (int i = 0; i < path5.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_TeshuJiancha, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if (("icon_add").equals(path5.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path5.get(i))
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path5) {
                        if (path.contains("icon_add")) {
                            path5.remove(path);
                        }
                    }
                /*    ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path5).requestCode(105).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 105);
                }
            });
            layout_TeshuJiancha.addView(view);
        }
    }

    //诊断
    public void zhenDuanResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //   iv_Zhenduan.setVisibility(View.VISIBLE);
        } else {
            path6.clear();
            path6.addAll(pathList);
        }

        if (path6.size() > 0 && path6.size() < 3) {
            //   iv_Zhenduan.setVisibility(View.GONE);
            path6.add(path6.size(), "icon_add");
        } else if (path6.size() >= 3) {
            //   iv_Zhenduan.setVisibility(View.GONE);
            for (String path : path6) {
                if ("icon_add".equals(path)) {
                    path6.remove(path);
                }
            }
        }
        layout_Zhenduan.removeAllViews();
        if (path6.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Zhenduan, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_Zhenduan.addView(view);
        }
        for (int i = 0; i < path6.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Zhenduan, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if ("icon_add".equals(path6.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path6.get(i).toString())
                        .apply(options)
                        .into(img);
            }

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path6) {
                        if (path.contains("icon_add")) {
                            path6.remove(path);
                        }
                    }
                    /*ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path6).requestCode(106).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 106);
                }
            });
            layout_Zhenduan.addView(view);
        }
    }

    //治疗
    public void zhiLiaoResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //   iv_Zhiliao.setVisibility(View.VISIBLE);
        } else {
            path7.clear();
            path7.addAll(pathList);
        }

        if (path7.size() > 0 && path7.size() < 3) {
            //   iv_Zhiliao.setVisibility(View.GONE);
            path7.add(path7.size(), "icon_add");
        } else if (path7.size() >= 3) {
            //   iv_Zhiliao.setVisibility(View.GONE);
            for (String path : path7) {
                if ("icon_add".equals(path)) {
                    path7.remove(path);
                }
            }
        }
        layout_Zhiliao.removeAllViews();
        if (path7.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Zhiliao, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_Zhiliao.addView(view);
        }
        for (int i = 0; i < path7.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Zhiliao, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);

            if ("icon_add".equals(path7.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path7.get(i).toString())
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path7) {
                        if (path.contains("icon_add")) {
                            path7.remove(path);
                        }
                    }
                   /* ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path7).requestCode(107).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 107);
                }
            });
            layout_Zhiliao.addView(view);
        }
    }

    //手术
    public void shouShuResult(ArrayList<String> pathList) {
        RequestOptions options = new RequestOptions().centerCrop();
        if (pathList.size() == 0) {
            //  iv_Shoushu.setVisibility(View.VISIBLE);
        } else {
            path8.clear();
            path8.addAll(pathList);
        }

        if (path8.size() > 0 && path8.size() < 3) {
            //  iv_Shoushu.setVisibility(View.GONE);
            path8.add(path8.size(), "icon_add");
        } else if (path8.size() >= 3) {
            //  iv_Shoushu.setVisibility(View.GONE);
            for (String path : path8) {
                if ("icon_add".equals(path)) {
                    path8.remove(path);
                }
            }
        }
        layout_Shoushu.removeAllViews();
        if (path8.size()<=0){
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Shoushu, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            Glide.with(context)
                    .load(R.mipmap.icon_add)
                    .apply(options)
                    .into(img);
            layout_Shoushu.addView(view);
        }
        for (int i = 0; i < path8.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image,
                    layout_Shoushu, false);
            final ImageView img = (ImageView) view
                    .findViewById(R.id.upload_image);
            if ("icon_add".equals(path8.get(i))) {
                Glide.with(context)
                        .load(R.mipmap.icon_add)
                        .apply(options)
                        .into(img);
            } else {
                Glide.with(context)
                        .load(path8.get(i).toString())
                        .apply(options)
                        .into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (String path : path8) {
                        if (path.contains("icon_add")) {
                            path8.remove(path);
                        }
                    }
                  /*  ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3).pathList(path8).requestCode(108).build();
                    ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
                    // 自由配置选项
                    ISListConfig config = new ISListConfig.Builder()
                            // 是否多选, 默认true
                            .multiSelect(false)
                            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                            .rememberSelected(false)
                            // “确定”按钮背景色
                            .btnBgColor(Color.GRAY)
                            // “确定”按钮文字颜色
                            .btnTextColor(Color.BLUE)
                            // 使用沉浸式状态栏
                            .statusBarColor(Color.parseColor("#3F51B5"))
                            // 返回图标ResId
                            .backResId(R.mipmap.bark)
                            // 标题
                            .title("图片")
                            // 标题文字颜色
                            .titleColor(Color.WHITE)
                            // TitleBar背景色
                            .titleBgColor(Color.parseColor("#3F51B5"))
                            .needCrop(true)
                            // 第一个是否显示相机，默认true
                            .needCamera(false)
                            // 最大选择图片数量，默认9
                            .maxNum(3)
                            .build();

// 跳转到图片选择器
                    ISNav.getInstance().toListActivity(this, config, 108);
                }
            });
            layout_Shoushu.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_addPatienMsgbig:
                one();
                break;
          /*  case R.id.iv_addPatienMsg:
                one();
                break;*/
            case R.id.layout_MedicalHistorybig:
                two();
                break;
          /*  case R.id.iv_addMedicalHistory:
                two();
                break;*/
            case R.id.layout_LinchuangBiaoxianbig:
                three();
                break;
            /*case R.id.iv_LinchuangBiaoxian:
                three();
                break;*/
            case R.id.layout_FuzhuJianchabig:
                four();
                break;
           /*  case R.id.iv_FuzhuJiancha:
                four();
                break;*/
            case R.id.layout_TeshuJianchabig:
                five();
                break;
           /* case R.id.iv_TeshuJiancha:
                five();
                break;*/
            case R.id.layout_Zhenduanbig:
                six();
                break;
          /*  case R.id.iv_Zhenduan:
                six();
                break;*/
            case R.id.layout_Zhiliaobig:
                seven();
                break;
            /*  case R.id.iv_Zhiliao:
                seven();
                break;*/
            case R.id.layout_Shoushubig:
                eight();
                break;
          /*  case R.id.iv_Shoushu:
                eight();
                break;*/
            default:
                break;
        }
    }

    public void one() {
        path1.clear();
      /*  ImageConfig imageConfig1 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path1).requestCode(101).build();
        ImageSelector.open(Upload_case.this, imageConfig1);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 101);
    }


    private void two() {
        path2.clear();
       /* ImageConfig imageConfig2 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path2).requestCode(102).build();
        ImageSelector.open(Upload_case.this, imageConfig2);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 102);
    }

    private void three() {
        path3.clear();
/*        ImageConfig imageConfig3 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path3).requestCode(103).build();
        ImageSelector.open(Upload_case.this, imageConfig3);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 103);
    }

    private void four() {
        path4.clear();
    /*    ImageConfig imageConfig4 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path4).requestCode(104).build();
        ImageSelector.open(Upload_case.this, imageConfig4);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 104);
    }

    private void five() {
        path5.clear();
       /* ImageConfig imageConfig5 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path5).requestCode(105).build();
        ImageSelector.open(Upload_case.this, imageConfig5);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 105);
    }

    private void six() {
        path6.clear();
       /* ImageConfig imageConfig6 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path6).requestCode(106).build();
        ImageSelector.open(Upload_case.this, imageConfig6);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 106);
    }

    private void seven() {
        path7.clear();
     /*   ImageConfig imageConfig7 = new ImageConfig.Builder(new GlideLoader())
                .mutiSelectMaxSize(3).pathList(path7).requestCode(107).build();
        ImageSelector.open(Upload_case.this, imageConfig7);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 107);
    }

    private void eight() {
        path8.clear();
      /*  ImageConfig imageConfig8 = new ImageConfig.Builder(new GlideLoader()).mutiSelectMaxSize(3).pathList(path8).requestCode(108).build();
        ImageSelector.open(Upload_case.this, imageConfig8);   // 开启图片选择器*/
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.bark)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(3)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, 108);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private NumberFormat nf = NumberFormat.getPercentInstance();

    /*private class LogUploadListener<T> extends UploadListener<T> {
        public LogUploadListener() {
            super("LogUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
            Log.e("LogUploadListener", "onStart: 开始上传");
        }

        @Override
        public void onProgress(Progress progress) {
            Log.e("LogUploadListener", "onProgress: 正在上传"+progress);
            double progressNum=progress.fraction;
            nf.setMaximumFractionDigits(1);
            MyNotificationCase(context, "上传任务已添加", "正在上传" + edit_casetitle.getText().toString(), "当前进度:" + nf.format(progressNum), false);
        }

        @Override
        public void onError(Progress progress) {
            Log.e("LogUploadListener", "onError: 上传出错"+progress.exception);
            MyNotificationCase(context, "您有任务上传失败", edit_casetitle.getText().toString()+"上传失败", "上传失败", true);
        }

        @Override
        public void onFinish(T t, Progress progress) {
            Log.e("LogUploadListener", "onFinish: 上传完成");
            MyNotificationCase(context, "1个任务上传完成", edit_casetitle.getText().toString() + "上传成功", "上传完成", true);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

    private void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload2.class);
        intent.putExtra("TAG", "Case");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1).build();

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(3, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }*/
}
