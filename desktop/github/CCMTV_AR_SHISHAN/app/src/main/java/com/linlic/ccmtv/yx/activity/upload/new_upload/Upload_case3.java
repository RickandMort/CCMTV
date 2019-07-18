package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.upload.LogUploadListener;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadCaseGridViewAdapter;
import com.linlic.ccmtv.yx.activity.upload.service.MyUploadCaseService;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class Upload_case3 extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context context;
    private LinearLayout ll_title;
    private TextView title_name;
    private TextView title_rules;
    private TextView tvShowMoreInfo;
    private LinearLayout llMoreInfo;
    private EditText etCaseTitle;
    private TextView tvSureUpload;
    private GridView gvPatientInfo;  //患者信息图片列表
    private GridView gvMedicalHistory;  //病史图片列表
    private GridView gvTreatment;  //治疗图片列表
    private GridView gvClinical;  //临床表现图片列表
    private GridView gvAssistInspect;  //辅助检查图片列表
    private GridView gvSpecialInspect;  //特殊检查图片列表
    private GridView gvDiagnosis;  //诊断图片列表
    private GridView gvSurgery;  //手术图片列表
    private LinearLayout llPatientInfo, llMedicalHistory, llTreatment, llClinical, llAssistInspect, llSpecialInspect, llDiagnosis, llSurgery;
    private LinearLayout llPatientInfoInner, llMedicalHistoryInner, llTreatmentInner, llClinicalInner, llAssistInspectInner, llSpecialInspectInner,
            llDiagnosisInner, llSurgeryInner;
    private TextView tvPatientInfo, tvMedicalHistory, tvTreatment, tvClinical, tvAssistInspect, tvSpecialInspect, tvDiagnosis, tvSurgery;

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
    private RequestParams params = new RequestParams();
    private JSONArray jsonArray;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            String dataString = jsonObject.getString("data");
                            showRulesPopupWindow(dataString);
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_case3);

        context = this;
        findId();
        init();
        postRequest = OkGo.<String>post(URLConfig.ccmtvapp_uploadCase);
    }

    public void findId() {
        ll_title = findViewById(R.id.id_ll_activity_title_8);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        title_rules = findViewById(R.id.activity_title_upload_rules);
        tvShowMoreInfo = (TextView) findViewById(R.id.id_tv_upload_case_show_more);
        llMoreInfo = (LinearLayout) findViewById(R.id.id_ll_upload_case_more_info);
        etCaseTitle = (EditText) findViewById(R.id.id_et_upload_case_title);
        tvSureUpload = (TextView) findViewById(R.id.id_tv_upload_video_sure);
        gvPatientInfo = (GridView) findViewById(R.id.id_gv_upload_case_patient_info);
        gvMedicalHistory = (GridView) findViewById(R.id.id_gv_upload_case_medical_history);
        gvTreatment = (GridView) findViewById(R.id.id_gv_upload_case_treatment);
        gvClinical = (GridView) findViewById(R.id.id_gv_upload_case_clinical);
        gvAssistInspect = (GridView) findViewById(R.id.id_gv_upload_case_assist_inspect);
        gvSpecialInspect = (GridView) findViewById(R.id.id_gv_upload_case_special_inspect);
        gvDiagnosis = (GridView) findViewById(R.id.id_gv_upload_case_diagnosis);
        gvSurgery = (GridView) findViewById(R.id.id_gv_upload_case_surgery);
        llPatientInfo = (LinearLayout) findViewById(R.id.id_ll_upload_case_patient_info);
        llMedicalHistory = (LinearLayout) findViewById(R.id.id_ll_upload_case_medical_history);
        llTreatment = (LinearLayout) findViewById(R.id.id_ll_upload_case_treatment);
        llClinical = (LinearLayout) findViewById(R.id.id_ll_upload_case_clinical);
        llAssistInspect = (LinearLayout) findViewById(R.id.id_ll_upload_case_assist_inspect);
        llSpecialInspect = (LinearLayout) findViewById(R.id.id_ll_upload_case_special_inspect);
        llDiagnosis = (LinearLayout) findViewById(R.id.id_ll_upload_case_diagnosis);
        llSurgery = (LinearLayout) findViewById(R.id.id_ll_upload_case_surgery);

        llPatientInfoInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_patient_info_inner);
        llMedicalHistoryInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_medical_history_inner);
        llTreatmentInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_treatment_inner);
        llClinicalInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_clinical_inner);
        llAssistInspectInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_assist_inspect_inner);
        llSpecialInspectInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_special_inspect_inner);
        llDiagnosisInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_diagnosis_inner);
        llSurgeryInner = (LinearLayout) findViewById(R.id.id_ll_upload_case_surgery_inner);

        tvPatientInfo = (TextView) findViewById(R.id.id_tv_upload_case_patient_info);
        tvMedicalHistory = (TextView) findViewById(R.id.id_tv_upload_case_medical_history);
        tvTreatment = (TextView) findViewById(R.id.id_tv_upload_case_treatment);
        tvClinical = (TextView) findViewById(R.id.id_tv_upload_case_clinical);
        tvAssistInspect = (TextView) findViewById(R.id.id_tv_upload_case_assist_inspect);
        tvSpecialInspect = (TextView) findViewById(R.id.id_tv_upload_case_special_inspect);
        tvDiagnosis = (TextView) findViewById(R.id.id_tv_upload_case_diagnosis);
        tvSurgery = (TextView) findViewById(R.id.id_tv_upload_case_surgery);

        title_name.setText("上传病例");
        title_rules.setText("上传规则");
        title_rules.setVisibility(View.VISIBLE);
        title_rules.setOnClickListener(this);
        tvShowMoreInfo.setOnClickListener(this);
        tvSureUpload.setOnClickListener(this);
        llPatientInfo.setOnClickListener(this);
        llMedicalHistory.setOnClickListener(this);
        llTreatment.setOnClickListener(this);
        llClinical.setOnClickListener(this);
        llAssistInspect.setOnClickListener(this);
        llSpecialInspect.setOnClickListener(this);
        llDiagnosis.setOnClickListener(this);
        llSurgery.setOnClickListener(this);

        gvPatientInfo.setOnItemClickListener(this);
        gvMedicalHistory.setOnItemClickListener(this);
        gvTreatment.setOnItemClickListener(this);
        gvClinical.setOnItemClickListener(this);
        gvAssistInspect.setOnItemClickListener(this);
        gvSpecialInspect.setOnItemClickListener(this);
        gvDiagnosis.setOnItemClickListener(this);
        gvSurgery.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_upload_case_show_more:
                tvShowMoreInfo.setVisibility(View.GONE);
                llMoreInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.id_tv_upload_video_sure:
                commitCase(view);
                break;
            /*case R.id.id_ll_upload_case_patient_info:
                Intent intent = new Intent(context,UploadCaseInputActivity.class);

                startActivity(intent);
                break;*/
            case R.id.id_ll_upload_case_patient_info:
                startIntentToInputActivity("患者信息", "1", list1, 1001, tvPatientInfo.getText().toString());
                break;
            case R.id.id_ll_upload_case_medical_history:
                /*Intent intent2 = new Intent(context, UploadCaseInputActivity.class);
                intent2.putExtra("typeName", "病史");
                intent2.putExtra("typeId", "2");
                intent2.putExtra("list", (Serializable) list2);
                startActivityForResult(intent2, 1002);*/
                startIntentToInputActivity("病史", "2", list2, 1002, tvMedicalHistory.getText().toString());
                break;
            case R.id.id_ll_upload_case_treatment:
                /*Intent intent3 = new Intent(context, UploadCaseInputActivity.class);
                intent3.putExtra("typeName", "治疗");
                intent3.putExtra("typeId", "3");
                intent3.putExtra("list", (Serializable) list3);
                startActivityForResult(intent3, 1003);*/
                startIntentToInputActivity("治疗", "3", list3, 1003, tvTreatment.getText().toString());
                break;
            case R.id.id_ll_upload_case_clinical:
                /*Intent intent4 = new Intent(context, UploadCaseInputActivity.class);
                intent4.putExtra("typeName", "临床表现");
                intent4.putExtra("typeId", "4");
                intent4.putExtra("list", (Serializable) list4);
                startActivityForResult(intent4, 1004);*/
                startIntentToInputActivity("临床表现", "4", list4, 1004, tvClinical.getText().toString());
                break;
            case R.id.id_ll_upload_case_assist_inspect:
                /*Intent intent5 = new Intent(context, UploadCaseInputActivity.class);
                intent5.putExtra("typeName", "辅助检查");
                intent5.putExtra("typeId", "5");
                intent5.putExtra("list", (Serializable) list5);
                startActivityForResult(intent5, 1005);*/
                startIntentToInputActivity("辅助检查", "5", list5, 1005, tvAssistInspect.getText().toString());
                break;
            case R.id.id_ll_upload_case_special_inspect:
                /*Intent intent6 = new Intent(context, UploadCaseInputActivity.class);
                intent6.putExtra("typeName", "特殊检查");
                intent6.putExtra("typeId", "6");
                intent6.putExtra("list", (Serializable) list6);
                startActivityForResult(intent6, 1006);*/
                startIntentToInputActivity("特殊检查", "6", list6, 1006, tvSpecialInspect.getText().toString());
                break;
            case R.id.id_ll_upload_case_diagnosis:
                /*Intent intent7 = new Intent(context, UploadCaseInputActivity.class);
                intent7.putExtra("typeName", "诊断");
                intent7.putExtra("typeId", "7");
                intent7.putExtra("list", (Serializable) list7);
                startActivityForResult(intent7, 1007);*/
                startIntentToInputActivity("诊断", "7", list7, 1007, tvDiagnosis.getText().toString());
                break;
            case R.id.id_ll_upload_case_surgery:
                /*Intent intent8 = new Intent(context, UploadCaseInputActivity.class);
                intent8.putExtra("typeName", "手术");
                intent8.putExtra("typeId", "8");
                intent8.putExtra("list", (Serializable) list8);
                startActivityForResult(intent8, 1008);*/
                startIntentToInputActivity("手术", "8", list8, 1008, tvSurgery.getText().toString());
                break;
            case R.id.activity_title_upload_rules:
                getUploadRules();
                break;
        }
    }

    private void getUploadRules() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUploadRules);
                    String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadCase, obj.toString());
//                    Log.e(getLocalClassName(), "获取上传规则: "+result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 2;
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

    /**
     * name：显示上传规则
     * author：bentley
     * data：2018/7/17 10:27
     * @param dataString
     */
    private void showRulesPopupWindow(String dataString) {
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        // 用于PopupWindow的View
        View contentViewRules = LayoutInflater.from(context).inflate(R.layout.popupwindow_upload_rules, null, false);
        final PopupWindow popupWindowRules = new PopupWindow(contentViewRules, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        TextView tvUploadRules = contentViewRules.findViewById(R.id.id_tv_upload_rules);
        TextView tvUploadRulesClose = contentViewRules.findViewById(R.id.id_tv_upload_rules_close);
        WebView webView = contentViewRules.findViewById(R.id.webview);
        webView.setBackgroundColor(2);
        webView.loadDataWithBaseURL(null, dataString, "text/html", "utf-8", null);
        tvUploadRules.setText(Html.fromHtml(dataString));
        tvUploadRulesClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowRules.dismiss();
            }
        });
//        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindowRules.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindowRules.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindowRules.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        popupWindowRules.showAsDropDown(ll_title,0,0);
//        popupWindowRules.showAtLocation(contentView, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);

        popupWindowRules.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
            }
        });
    }

    private void startIntentToInputActivity(String typeName, String typeId, List<PhotoInfo> list, int requestCode, String tvText) {
        Intent intent = new Intent(Upload_case3.this, UploadCaseInputActivity.class);
        intent.putExtra("typeName", typeName);
        intent.putExtra("typeId", typeId);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("tvText", tvText);
        startActivityForResult(intent, requestCode);
    }

    /**
     * name：GridView点击事件
     * author：MrSong
     * data：2016/10/19 14:02
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.id_gv_upload_case_patient_info:
                startIntentToInputActivity("患者信息", "1", list1, 1001, tvPatientInfo.getText().toString());
                break;
            case R.id.id_gv_upload_case_medical_history:
                startIntentToInputActivity("病史", "2", list2, 1002, tvMedicalHistory.getText().toString());
                break;
            case R.id.id_gv_upload_case_treatment:
                startIntentToInputActivity("治疗", "3", list3, 1003, tvTreatment.getText().toString());
                break;
            case R.id.id_gv_upload_case_clinical:
                startIntentToInputActivity("临床表现", "4", list4, 1004, tvClinical.getText().toString());
                break;
            case R.id.id_gv_upload_case_assist_inspect:
                startIntentToInputActivity("辅助检查", "5", list5, 1005, tvAssistInspect.getText().toString());
                break;
            case R.id.id_gv_upload_case_special_inspect:
                startIntentToInputActivity("特殊检查", "6", list6, 1006, tvSpecialInspect.getText().toString());
                break;
            case R.id.id_gv_upload_case_diagnosis:
                startIntentToInputActivity("诊断", "7", list7, 1007, tvDiagnosis.getText().toString());
                break;
            case R.id.id_gv_upload_case_surgery:
                startIntentToInputActivity("手术", "8", list8, 1008, tvSurgery.getText().toString());
                break;
        }
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
        gvPatientInfo.setAdapter(adapter1);
        gvMedicalHistory.setAdapter(adapter2);
        gvTreatment.setAdapter(adapter3);
        gvClinical.setAdapter(adapter4);
        gvAssistInspect.setAdapter(adapter5);
        gvSpecialInspect.setAdapter(adapter6);
        gvDiagnosis.setAdapter(adapter7);
        gvSurgery.setAdapter(adapter8);
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

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 0) {
//            Toast.makeText(context, "resultCode为0", Toast.LENGTH_SHORT).show();
            return;
        }
        List<PhotoInfo> resultList = (List<PhotoInfo>) data.getSerializableExtra("resultList");
        String resultInfo = data.getStringExtra("resultInfo");
        if (requestCode == 1001) {
//                llPatientInfoInner.setVisibility(View.VISIBLE);
//                tvPatientInfo.setText(resultInfo);
            refreshView(list1, adapter1, resultList, llPatientInfoInner, tvPatientInfo, resultInfo);
        } else if (requestCode == 1002) {
//                llMedicalHistoryInner.setVisibility(View.VISIBLE);
//                tvMedicalHistory.setText(resultInfo);
            refreshView(list2, adapter2, resultList, llMedicalHistoryInner, tvMedicalHistory, resultInfo);
        } else if (requestCode == 1003) {
//                llTreatmentInner.setVisibility(View.VISIBLE);
//                tvTreatment.setText(resultInfo);
            refreshView(list3, adapter3, resultList, llTreatmentInner, tvTreatment, resultInfo);
        } else if (requestCode == 1004) {
//                llClinicalInner.setVisibility(View.VISIBLE);
//                tvClinical.setText(resultInfo);
            refreshView(list4, adapter4, resultList, llClinicalInner, tvClinical, resultInfo);
        } else if (requestCode == 1005) {
//                llAssistInspectInner.setVisibility(View.VISIBLE);
//                tvAssistInspect.setText(resultInfo);
            refreshView(list5, adapter5, resultList, llAssistInspectInner, tvAssistInspect, resultInfo);
        } else if (requestCode == 1006) {
//                llSpecialInspectInner.setVisibility(View.VISIBLE);
//                tvSpecialInspect.setText(resultInfo);
            refreshView(list6, adapter6, resultList, llSpecialInspectInner, tvSpecialInspect, resultInfo);
        } else if (requestCode == 1007) {
//                llDiagnosisInner.setVisibility(View.VISIBLE);
//                tvDiagnosis.setText(resultInfo);
            refreshView(list7, adapter7, resultList, llDiagnosisInner, tvDiagnosis, resultInfo);
        } else if (requestCode == 1008) {
//                llSurgeryInner.setVisibility(View.VISIBLE);
//                tvSurgery.setText(resultInfo);
            refreshView(list8, adapter8, resultList, llSurgeryInner, tvSurgery, resultInfo);
        }
        /*if (resultList.size() <= 7) {
        } else {
            if (requestCode == 1001) {
                refreshView(list1, adapter1, resultList, false);
            } else if (requestCode == 1002) {
                refreshView(list2, adapter2, resultList, false);
            } else if (requestCode == 1003) {
                refreshView(list3, adapter3, resultList, false);
            } else if (requestCode == 1004) {
                refreshView(list4, adapter4, resultList, false);
            } else if (requestCode == 1005) {
                refreshView(list5, adapter5, resultList, false);
            } else if (requestCode == 1006) {
                refreshView(list6, adapter6, resultList, false);
            } else if (requestCode == 1007) {
                refreshView(list7, adapter7, resultList, false);
            } else if (requestCode == 1008) {
                llSurgeryInner.setVisibility(View.VISIBLE);
                refreshView(list8, adapter8, resultList, false);
            }
        }*/
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
     * name：选择完照片后回调然后刷新页面
     * author：MrSong
     * data：2016/10/19 13:57
     */
    private void refreshView(List<PhotoInfo> list, UploadCaseGridViewAdapter adapter, List<PhotoInfo> resultList, LinearLayout linearLayout, TextView textView, String resultInfo) {
        linearLayout.setVisibility(View.VISIBLE);
        textView.setText(resultInfo);
        list.clear();
        list.addAll(resultList);
        adapter.notifyDataSetChanged();
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

        if (TextUtils.isEmpty(etCaseTitle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "病例名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            JSONArray jsonArrayPic1 = new JSONArray();
            for (int i = 0; i < list1.size(); i++) {

                if (!list1.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    path1_must.add(list1.get(i).getPhotoPath());
                    params.addBodyParameter("upfileA_" + i, new File(list1.get(i).getPhotoPath()));
//                    postRequest.params("upfileA_" + i, new File(list1.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic1.put(list1.get(i).getPhotoPath());
                }
            }
            jsonObject1.put("info", tvPatientInfo.getText());
            jsonObject1.put("icon", "患者信息");
            jsonObject1.put("img", jsonArrayPic1);
            jsonArray.put(jsonObject1);
            if (path1_must.size() <= 0) {
                Toast.makeText(getApplicationContext(), "患者信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject2 = new JSONObject();
            JSONArray jsonArrayPic2 = new JSONArray();
            for (int i = 0; i < list2.size(); i++) {
                if (!list2.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    path2_must.add(list2.get(i).getPhotoPath());
                    params.addBodyParameter("upfileB_" + i, new File(list2.get(i).getPhotoPath()));
//                    postRequest.params("upfileB_" + i, new File(list2.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic2.put(list2.get(i).getPhotoPath());
                }
            }
            jsonObject2.put("info", tvMedicalHistory.getText());
            jsonObject2.put("icon", "病史");
            jsonObject2.put("img", jsonArrayPic2);
            jsonArray.put(jsonObject2);
            if (path2_must.size() <= 0) {
                Toast.makeText(getApplicationContext(), "病史不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject3 = new JSONObject();
            JSONArray jsonArrayPic3 = new JSONArray();
            for (int i = 0; i < list3.size(); i++) {
                if (!list3.get(i).getPhotoPath().contains("icon_add")) {   //去掉加号
                    //key+i为上传的参数，后面为图片路径
                    path3_must.add(list3.get(i).getPhotoPath());
                    params.addBodyParameter("upfileC_" + i, new File(list3.get(i).getPhotoPath()));
//                    postRequest.params("upfileC_" + i, new File(list3.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic3.put(list3.get(i).getPhotoPath());
                }
            }
            jsonObject3.put("info", tvTreatment.getText());
            jsonObject3.put("icon", "治疗");
            jsonObject3.put("img", jsonArrayPic3);
            jsonArray.put(jsonObject3);
            if (path3_must.size() <= 0) {
                Toast.makeText(getApplicationContext(), "治疗不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject4 = new JSONObject();
            JSONArray jsonArrayPic4 = new JSONArray();
            for (int i = 0; i < list4.size(); i++) {
                if (!list4.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    params.addBodyParameter("upfileD_" + i, new File(list4.get(i).getPhotoPath()));
//                    postRequest.params("upfileD_" + i, new File(list4.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic4.put(list4.get(i).getPhotoPath());
                }
            }
            jsonObject4.put("info", tvClinical.getText());
            jsonObject4.put("icon", "临床表现");
            jsonObject4.put("img", jsonArrayPic4);
            jsonArray.put(jsonObject4);

            JSONObject jsonObject5 = new JSONObject();
            JSONArray jsonArrayPic5 = new JSONArray();
            for (int i = 0; i < list5.size(); i++) {
                if (!list5.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    params.addBodyParameter("upfileE_" + i, new File(list5.get(i).getPhotoPath()));
//                    postRequest.params("upfileE_" + i, new File(list5.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic5.put(list5.get(i).getPhotoPath());
                }
            }
            jsonObject5.put("info", tvAssistInspect.getText());
            jsonObject5.put("icon", "辅助检查");
            jsonObject5.put("img", jsonArrayPic5);
            jsonArray.put(jsonObject5);

            JSONObject jsonObject6 = new JSONObject();
            JSONArray jsonArrayPic6 = new JSONArray();
            for (int i = 0; i < list6.size(); i++) {
                if (!list6.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    params.addBodyParameter("upfileF_" + i, new File(list6.get(i).getPhotoPath()));
//                    postRequest.params("upfileF_" + i, new File(list6.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic6.put(list6.get(i).getPhotoPath());
                }
            }
            jsonObject6.put("info", tvSpecialInspect.getText());
            jsonObject6.put("icon", "特殊检查");
            jsonObject6.put("img", jsonArrayPic6);
            jsonArray.put(jsonObject6);

            JSONObject jsonObject7 = new JSONObject();
            JSONArray jsonArrayPic7 = new JSONArray();
            for (int i = 0; i < list7.size(); i++) {
                if (!list7.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    params.addBodyParameter("upfileG_" + i, new File(list7.get(i).getPhotoPath()));
//                    postRequest.params("upfileG_" + i, new File(list7.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic7.put(list7.get(i).getPhotoPath());
                }
            }
            jsonObject7.put("info", tvDiagnosis.getText());
            jsonObject7.put("icon", "诊断");
            jsonObject7.put("img", jsonArrayPic7);
            jsonArray.put(jsonObject7);

            JSONObject jsonObject8 = new JSONObject();
            JSONArray jsonArrayPic8 = new JSONArray();
            for (int i = 0; i < list8.size(); i++) {
                if (!list8.get(i).getPhotoPath().contains("icon_add")) {
                    //key+i为上传的参数，后面为图片路径
                    params.addBodyParameter("upfileH_" + i, new File(list8.get(i).getPhotoPath()));
//                    postRequest.params("upfileH_" + i, new File(list8.get(i).getPhotoPath()));
                    pictureCount++;
                    jsonArrayPic8.put(list8.get(i).getPhotoPath());
                }
            }
            jsonObject8.put("info", tvSurgery.getText());
            jsonObject8.put("icon", "手术");
            jsonObject8.put("img", jsonArrayPic8);
            jsonArray.put(jsonObject8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //存储数据  FileSizeUtil.FormetFileSize(TotalSize)
        /*MyDbUtils.saveUploadCaseMsg(context, etCaseTitle.getText().toString(), MyUploadCaseService.upload_wait + "", upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //获取当前保存数据的ID
        int case_id = MyDbUtils.findUploadCaseMsg(context, upfileA_1, upfileA_2, upfileA_3, upfileB_1, upfileB_2, upfileB_3, upfileC_1, upfileC_2, upfileC_3, upfileD_1, upfileD_2, upfileD_3, upfileE_1, upfileE_2,
                upfileE_3, upfileF_1, upfileF_2, upfileF_3, upfileG_1, upfileG_2, upfileG_3, upfileH_1, upfileH_2, upfileH_3);
        //发送广播
        Intent intent = new Intent();
        intent.setAction("upload_case");
        intent.putExtra("case_title", etCaseTitle.getText().toString());
        intent.putExtra("case_id", case_id);
        sendBroadcast(intent);*/
        //启动服务
        //bindService(new Intent(context, MyUploadCaseService.class), connection, BIND_AUTO_CREATE);

        /**
         * 尝试更改上传为okUpload
         *
         */
//        OkUploadCase(pictureCount);
        /**
         * 图片单独上传，最后统一上传信息
         */
        uploadCaseNormal(pictureCount);

        Toast.makeText(context, "病例已提交，可在上传列表中查看", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, IsUpload3.class);
        intent1.putExtra("TAG", "Case");
        startActivity(intent1);
        finish();
    }

    private void uploadCaseNormal(int pictureCount) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", SharedPreferencesTools.getUid(context));
                    object.put("title", etCaseTitle.getText().toString());
                    object.put("act", URLConfig.uploadAllCase);
                    object.put("list", jsonArray);
                    final String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadCase, object.toString());
//                    LogUtil.e("上传病例返回信息数据：", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject resultObject = new JSONObject(result);
                                if (resultObject.getInt("status") == 1) { //成功
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
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void OkUploadCase(int pictureCount) {
        UploadModel uploadModel = new UploadModel();
        uploadModel.setName(etCaseTitle.getText().toString());
        uploadModel.setType("case");
        uploadModel.setPicCount(pictureCount);
        uploadModel.setIconUrl(list1.get(0).getPhotoPath());
        uploadModel.setPayType("免费");
        JSONObject object = new JSONObject();
        try {
            object.put("uid", SharedPreferencesTools.getUid(context));
            object.put("title", etCaseTitle.getText().toString());
            object.put("act", URLConfig.uploadAllCase);
            object.put("list", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        postRequest.params("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())))
                .converter(new StringConvert());
        HttpParams params = postRequest.getParams();
        UploadTask<String> task = OkUpload.request(object.toString(), postRequest)
                .priority(35)
                .extra1(uploadModel)
                .extra2(etCaseTitle.getText().toString() + "")
                .save()
                .register(new LogUploadListener<String>());
        task.start();
    }

    public void back(View view) {
        finish();
    }
}
