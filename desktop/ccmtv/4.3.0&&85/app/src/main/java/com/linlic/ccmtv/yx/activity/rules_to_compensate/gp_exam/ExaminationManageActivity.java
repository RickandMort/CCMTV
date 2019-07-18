package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuCheckPeriodicalExamActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规培考试管理界面
 * Created by bentley on 2018/6/26.
 */

public class ExaminationManageActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private TextView title_name;
    private NiceSpinner spinnerExamType;
    private ListView lvExam;
    private NodataEmptyLayout rlNoData;
    private TextView tvPeriodicalExam;

    private List<Map<String, Object>> examTypeList = new ArrayList<>();
    private List<String> examTypeStringList = new ArrayList<>();
    private List<Map<String, Object>> examDataList = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private String fid = "";
    private boolean isHaveSpinnerDate = false;
    private String man_type = "";
    //用户统计
    private String name;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONObject dataList = data.getJSONObject("dataList");
                                man_type = dataList.getString("man_type");
                                if (dataList.has("list")) {
                                    examDataList.clear();
                                    JSONArray listArray = dataList.getJSONArray("list");
                                    for (int i = 0; i < listArray.length(); i++) {
                                        JSONObject listObject = listArray.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("year", listObject.getString("year"));
                                        map.put("month", listObject.getString("month"));
                                        map.put("week", listObject.getString("week"));
                                        map.put("name", listObject.getString("name"));
                                        map.put("exam_type", listObject.getString("exam_type"));
                                        map.put("exam_id", listObject.getString("exam_id"));
                                        examDataList.add(map);
                                    }
                                }
                                if (dataList.has("type") && !isHaveSpinnerDate) {
                                    JSONArray typeArray = dataList.getJSONArray("type");
                                    examTypeStringList.clear();
                                    examTypeList.clear();
                                    for (int i = 0; i < typeArray.length(); i++) {
                                        JSONObject typeObject = typeArray.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("name", typeObject.getString("name"));
                                        map.put("type", typeObject.getString("type"));
                                        examTypeList.add(map);
                                        examTypeStringList.add(typeObject.getString("name"));
                                    }
                                    setSpinner();
                                }

                                baseListAdapter.notifyDataSetChanged();
                            } else {

                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }

//                        if (examDataList.size() > 0) {
//                            lvExam.setVisibility(View.VISIBLE);
//                            rlNoData.setVisibility(View.GONE);
//                        }else {
//                            lvExam.setVisibility(View.GONE);
//                            rlNoData.setVisibility(View.VISIBLE);
//                        }
                        MyProgressBarDialogTools.hide();
                        setResultStatus(examDataList.size() > 0, result.getInt("code"));
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                        setResultStatus(examDataList.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                JSONObject dataObject1 = data.getJSONObject("data");
                                String is_send = dataObject1.getString("is_send");   //1代表 已经发送出科考核通知  然后直接访问出科考核获取学员列表接口  0代表未发送出科考通知  会返回备注数据  userlist 要展现在APP上
                                if (is_send.equals("1")) {
                                    Intent intent = new Intent(context, GpGraduateExamStudentListActivity.class);
                                    intent.putExtra("year", dataObject1.getString("year"));
                                    intent.putExtra("month", dataObject1.getString("month"));
                                    intent.putExtra("week", dataObject1.getString("week"));
                                    intent.putExtra("fid", fid);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(context, GpGraduateExamSendNoticeActivity.class);
                                    intent.putExtra("result", msg.obj.toString());
                                    intent.putExtra("fid", fid);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                        setResultStatus(examDataList.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(examDataList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvExam.setVisibility(View.VISIBLE);
            rlNoData.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rlNoData.setNetErrorIcon();
            } else {
                rlNoData.setLastEmptyIcon();
            }
            lvExam.setVisibility(View.GONE);
            rlNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        context = this;
        name=getIntent().getStringExtra("name");
        findId();
        initListView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ("84".equals(fid)){//考核基地
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/exambase/index.html";
        } else {
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/allexam/index.html";
        }
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        spinnerExamType = (NiceSpinner) findViewById(R.id.id_spinner_examination_manage);
        lvExam = (ListView) findViewById(R.id.id_lv_examination_manage);
        rlNoData = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);
        tvPeriodicalExam = (TextView) findViewById(R.id.id_tv_to_periodical_exam);
        try {
            LogUtil.e("当前身份", SharedPreferencesTools.getGp_type(context).toString());
            if (SharedPreferencesTools.getGp_type(context).equals("1")) {
                tvPeriodicalExam.setVisibility(View.GONE);
            } else {
                tvPeriodicalExam.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvPeriodicalExam.setOnClickListener(this);
    }

    private void initData() {
        title_name.setTextSize(16);
        title_name.setText("考核列表");

        try {
            fid = getIntent().getStringExtra("fid");
            Log.d("snow",fid+"    =================");
//            Log.e(getLocalClassName(), "initData: fid:" + fid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAllGpExamInfo("");
    }

    private void getAllGpExamInfo(final String examType) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dailyExamList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    if (examType.isEmpty()) {
//                        obj.put("data", examType);//考核列表删选；1 所有列表，2日常考核列表，3出科考核列表
                    } else {
                        obj.put("data", examType);//考核列表删选；1 所有列表，2日常考核列表，3出科考核列表
                    }
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培所有考核数据：", result);

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

    private void initListView() {
        /*for (int i=0; i<10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "title" + i);
            examDataList.add(map);
        }*/

        baseListAdapter = new BaseListAdapter(lvExam, examDataList, R.layout.item_gp_exam_manager_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map<String, Object>) item;
                helper.setText(R.id.id_tv_item_gp_exam_manage_list_title, map.get("name").toString());
                helper.setText(R.id.id_tv_item_gp_exam_manage_list_month, map.get("month").toString());
            }
        };

        lvExam.setAdapter(baseListAdapter);

        lvExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (examDataList.get(position).get("exam_type").equals("1")) {  //1是日常考核 2是出科考核
                    if (man_type.equals("1")) { //1学员的考核列表 2 科室的考核列表 3基地的考核列表
                        Intent intent = new Intent(context, GpStuExamScoreListActivity.class);
                        intent.putExtra("exam_type", examDataList.get(position).get("exam_type").toString());
                        intent.putExtra("exam_name", examDataList.get(position).get("name").toString());
                        intent.putExtra("exam_id", examDataList.get(position).get("exam_id").toString());
                        intent.putExtra("year", examDataList.get(position).get("year").toString());
                        intent.putExtra("month", examDataList.get(position).get("month").toString());
                        intent.putExtra("fid", fid);
                        startActivity(intent);
                    } else {
                        if(name.equals("过程考核")){
                            Intent intent = new Intent(context, ProcessExamDailyStudentListActivity.class);
                            intent.putExtra("exam_type", examDataList.get(position).get("exam_type").toString());
                            intent.putExtra("exam_name", examDataList.get(position).get("name").toString());
                            intent.putExtra("exam_id", examDataList.get(position).get("exam_id").toString());
                            intent.putExtra("year", examDataList.get(position).get("year").toString());
                            intent.putExtra("month", examDataList.get(position).get("month").toString());
                            intent.putExtra("fid", fid);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, GpExamStudentListActivity.class);
                            intent.putExtra("exam_type", examDataList.get(position).get("exam_type").toString());
                            intent.putExtra("exam_name", examDataList.get(position).get("name").toString());
                            intent.putExtra("exam_id", examDataList.get(position).get("exam_id").toString());
                            intent.putExtra("year", examDataList.get(position).get("year").toString());
                            intent.putExtra("month", examDataList.get(position).get("month").toString());
                            intent.putExtra("fid", fid);
                            startActivity(intent);
                        }

                    }
                } else {
                    if (man_type.equals("1")) {  //1学员的考核列表 2 科室的考核列表 3基地的考核列表
                        Intent intent = new Intent(context, GpStuExamScoreListActivity.class);
                        intent.putExtra("exam_type", examDataList.get(position).get("exam_type").toString());
                        intent.putExtra("exam_id", examDataList.get(position).get("exam_id").toString());
                        intent.putExtra("year", examDataList.get(position).get("year").toString());
                        intent.putExtra("month", examDataList.get(position).get("month").toString());
                        intent.putExtra("fid", fid);
                        startActivity(intent);
                    } else {
                        if(name.equals("考核(基地)")){
                            Intent intent=new Intent(context,GpBaseDailyExamActivity.class);
                            intent.putExtra("year", examDataList.get(position).get("year").toString());
                            intent.putExtra("month", examDataList.get(position).get("month").toString());
                            intent.putExtra("fid", fid);
                            startActivity(intent);
                        }else {
                            String year = examDataList.get(position).get("year").toString();
                            String month = examDataList.get(position).get("month").toString();
                            String week = examDataList.get(position).get("week").toString();
                            getIsSendNotice(year, month, week); //获取是否发送出科考核信息
                        }
                    }

                    /*Intent intent = new Intent(context, GpExamSubjectActivity.class);
                    intent.putExtra("exam_type", examDataList.get(position).get("exam_type").toString());
                    intent.putExtra("exam_id", examDataList.get(position).get("exam_id").toString());
                    intent.putExtra("year", examDataList.get(position).get("year").toString());
                    intent.putExtra("month", examDataList.get(position).get("month").toString());
                    intent.putExtra("fid", fid);
                    startActivity(intent);*/
                }

            }
        });
    }

    private void getIsSendNotice(final String year, final String month, final String week) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.leaveKsNotice);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("year", year);
                    obj.put("month", month);
                    obj.put("week", week);
                    /*obj.put("uid", "10072888");
                    obj.put("fid", "83");
                    obj.put("year", "2018");
                    obj.put("month", "06");*/
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培是否发送出科考核发送通知数据：", result);

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

    private void setSpinner() {
        isHaveSpinnerDate = true;
        spinnerExamType.attachDataSource(examTypeStringList);
        spinnerExamType.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e(getLocalClassName(), "onItemClick: 点击" + examTypeList.get(position).get("name"));
                getAllGpExamInfo(examTypeList.get(position).get("type").toString());
            }
        });
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_to_periodical_exam:
                Intent intent = new Intent(this, StuCheckPeriodicalExamActivity.class);
                intent.putExtra("fid", fid);
                startActivity(intent);
                break;
        }
    }
}
