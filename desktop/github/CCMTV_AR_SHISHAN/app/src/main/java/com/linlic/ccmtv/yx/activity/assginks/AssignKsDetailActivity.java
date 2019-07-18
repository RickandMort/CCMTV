package com.linlic.ccmtv.yx.activity.assginks;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.assginks.entity.AssginKsDetailEntity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/15.
 */

public class AssignKsDetailActivity extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView mTvTittle;
    @Bind(R.id.tv_username)
    TextView mTvUsername;
    @Bind(R.id.tv_edu)
    TextView mTvEdu;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_base)
    TextView mTvBase;
    @Bind(R.id.tv_plan_time)
    TextView mTvPlanTime;
    @Bind(R.id.id_spinner_examination_manage)
    NiceSpinner spinnerEduType;
    @Bind(R.id.id_ll_daily_exam_button_layout)
    View viewSbmit;
    @Bind(R.id.id_tv_daily_exam_cancel)
    TextView mCancel;
    @Bind(R.id.id_tv_daily_exam_submit)
    TextView mSubmit;

    private Context context;
    private String details_id;
    private List<String> eduTypeStringList = new ArrayList<>();//轮转科室科室名
    private List<AssginKsDetailEntity.HospitalListBean> eduTypeList = new ArrayList<>();//轮转科室科对象
    private String hospital_kid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assgin_ks_detail);
        context = this;
        ButterKnife.bind(this);
        initViews();
        getAssignKsEdit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rk/assginKsIndex.html";
        super.onPause();
    }

    private void initViews() {
        details_id = getIntent().getStringExtra("details_id");
        mTvTittle.setText("入科分配科室");
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAssignKsSubmit();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /***
     * 设置科室选择
     */
    private void setSpinner(String kid) {
        spinnerEduType.attachDataSource(eduTypeStringList);
        spinnerEduType.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hospital_kid = eduTypeList.get(position).getHospital_kid();
            }
        });

        if (!TextUtils.isEmpty(kid)) {
            for (int i = 0; i < eduTypeList.size(); i++) {
                AssginKsDetailEntity.HospitalListBean hospitalListBean = eduTypeList.get(i);
                if (kid.equals(hospitalListBean.getHospital_kid())){
                    spinnerEduType.setSelectedIndex(i);
                }
            }

        }
    }

    private void showDetail(AssginKsDetailEntity ksDetailEntity) {
        mTvUsername.setText(ksDetailEntity.getRealname());
        mTvEdu.setText(ksDetailEntity.getEdu_highest_education());
        mTvTime.setText(ksDetailEntity.getLs_enrollment_year());
        mTvBase.setText(ksDetailEntity.getBasename());
        mTvPlanTime.setText(ksDetailEntity.getPlan_starttime() + "~" + ksDetailEntity.getPlan_endtime());
        if ("0".equals(ksDetailEntity.getStatus())) {
            //有
            viewSbmit.setVisibility(View.VISIBLE);
        } else {
            viewSbmit.setVisibility(View.GONE);
        }
        hospital_kid = ksDetailEntity.getCurrent_hospital_kid();
        setSpinner(ksDetailEntity.getCurrent_hospital_kid());
    }

    /**
     * 入科分配科室提交操作
     */
    private void getAssignKsSubmit() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getAssginKsSubmit);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("details_id", details_id);
                    obj.put("hospital_kid", hospital_kid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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
     * 入科分配科室编辑查看
     */
    private void getAssignKsEdit() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getAssginKsEdit);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("details_id", details_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                AssginKsDetailEntity ksDetailEntity = new AssginKsDetailEntity();
                                ksDetailEntity.setRealname(dateJson.getString("realname"));
                                ksDetailEntity.setEdu_highest_education(dateJson.getString("edu_highest_education"));
                                ksDetailEntity.setLs_enrollment_year(dateJson.getString("ls_enrollment_year"));
                                ksDetailEntity.setBasename(dateJson.getString("basename"));
                                ksDetailEntity.setPlan_starttime(dateJson.getString("plan_starttime"));
                                ksDetailEntity.setPlan_endtime(dateJson.getString("plan_endtime"));
                                ksDetailEntity.setCurrent_hospital_kid(dateJson.getString("current_hospital_kid"));
                                ksDetailEntity.setStatus(dateJson.getString("status"));
                                List<AssginKsDetailEntity.HospitalListBean> hospital_list = ksDetailEntity.getHospital_list();
                                eduTypeStringList.clear();
                                eduTypeList.clear();
                                JSONArray hospitalList = dateJson.getJSONArray("hospital_list");
                                for (int i = 0; i < hospitalList.length(); i++) {
                                    JSONObject jsonObject1 = hospitalList.getJSONObject(i);
                                    AssginKsDetailEntity.HospitalListBean hospitalListBean = new AssginKsDetailEntity.HospitalListBean();
                                    hospitalListBean.setHospital_kid(jsonObject1.getString("hospital_kid"));
                                    hospitalListBean.setName(jsonObject1.getString("name"));
                                    hospital_list.add(hospitalListBean);
                                    eduTypeList.add(hospitalListBean);
                                    eduTypeStringList.add(jsonObject1.getString("name"));
                                }
                                showDetail(ksDetailEntity);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                Toast.makeText(context, "分配科室成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
}
