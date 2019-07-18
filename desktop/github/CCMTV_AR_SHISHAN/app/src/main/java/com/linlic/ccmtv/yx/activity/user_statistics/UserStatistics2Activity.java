package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.user_statistics.utils.ChartUtils;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserStatistics2Activity extends BaseActivity implements View.OnClickListener {

    private TextView title_name;
    private TextView tvTitle1, tvTitle2, tvTitle3;
    private ImageView ivTitle1, ivTitle2, ivTitle3;
    private TextView tvTotalTime, tvContinueDay;
    private LinearLayout llStudyDescription;
    private Context context;
    private String urlconfig;
    private LineChart mLineChart;

    private int[] colors;//颜色集合
    List<Entry> values;
    private List<String> loginList = new ArrayList<>();
    private String total_online_time, video_num, continueDays, article_num, depict;
    private String suc_num, fail_num, percent, spend_total_time, spend_per_time;
    private String exam_total, exam_do_total, max_exam_score, min_exam_score, formal_do_total, makeup_do_total, fail_exam_total;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            total_online_time = jsonObject.getString("total_online_time");
                            video_num = jsonObject.getString("video_num");
                            continueDays = jsonObject.getString("continue");
                            article_num = jsonObject.getString("article_num");
                            depict = jsonObject.getString("depict");
                            values = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("study_time");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                values.add(new Entry(i, jsonArray.getInt(i)));
                            }
                            /*values.add(new Entry(0, 80));
                            values.add(new Entry(1, 80));
                            values.add(new Entry(2, 80));
                            values.add(new Entry(3, 1));
                            values.add(new Entry(4, 1));
                            values.add(new Entry(5, 2));
                            values.add(new Entry(6, 80));*/

                            JSONArray jsonArrayDate = jsonObject.getJSONArray("date");
                            dateX = new String[jsonArrayDate.length()];
                            for (int i = 0; i < jsonArrayDate.length(); i++) {
                                dateX[i]= (String) jsonArrayDate.get(i);
                            }
                            JSONArray jsonArrayLoginList = jsonObject.getJSONArray("loginlist");
                            for (int i = 0; i < jsonArrayLoginList.length(); i++) {
                                loginList.add(jsonArrayLoginList.getString(i));
                            }
                            setText();
                            MyProgressBarDialogTools.hide();
                            initLineBar();
                        } else {
                            ChartUtils.initChart(mLineChart,context);
                            ChartUtils.notifyDataSetChanged(mLineChart, getData(), ChartUtils.dayValue);
                            MyProgressBarDialogTools.hide();
                        }
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

    private String[] dateX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_statistics2);
        context=this;
        findId();
        if (SharedPreferencesTools.getIsdocexam(context)) {
            urlconfig = URLConfig.UserTongji;
        } else {
            urlconfig = URLConfig.CCMTVAPP;
        }

        setValue();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        llStudyDescription = (LinearLayout) findViewById(R.id.id_ll_study_description);
        tvTotalTime = (TextView) findViewById(R.id.id_tv_study_record_total_time);
        tvContinueDay = (TextView) findViewById(R.id.id_tv_study_record_continue_day);

        title_name.setText("学习记录");
        title_name.setTextSize(20);

        tvTitle1 = (TextView) findViewById(R.id.id_tv_study_record_title1);
        tvTitle2 = (TextView) findViewById(R.id.id_tv_study_record_title2);
        tvTitle3 = (TextView) findViewById(R.id.id_tv_study_record_title3);

        ivTitle1 = (ImageView) findViewById(R.id.id_iv_study_record_title1);
        ivTitle2 = (ImageView) findViewById(R.id.id_iv_study_record_title2);
        ivTitle3 = (ImageView) findViewById(R.id.id_iv_study_record_title3);

        mLineChart = (LineChart) findViewById(R.id.linechart);

        tvTitle1.setOnClickListener(this);
        tvTitle2.setOnClickListener(this);
        tvTitle3.setOnClickListener(this);
        llStudyDescription.setOnClickListener(this);

        if (!SharedPreferencesTools.getIsdocexam(this)){
            tvTitle3.setVisibility(View.GONE);
            ivTitle3.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_tv_study_record_title1:
                tvTitle1.setTextColor(Color.parseColor("#3897F9"));
                tvTitle2.setTextColor(Color.parseColor("#3e3e3e"));
                tvTitle3.setTextColor(Color.parseColor("#3e3e3e"));
                ivTitle1.setVisibility(View.VISIBLE);
                ivTitle2.setVisibility(View.INVISIBLE);
                ivTitle3.setVisibility(View.INVISIBLE);
                break;

            case R.id.id_tv_study_record_title2:
                tvTitle1.setTextColor(Color.parseColor("#3e3e3e"));
                tvTitle2.setTextColor(Color.parseColor("#3897F9"));
                tvTitle3.setTextColor(Color.parseColor("#3e3e3e"));
                ivTitle1.setVisibility(View.INVISIBLE);
                ivTitle2.setVisibility(View.VISIBLE);
                ivTitle3.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(UserStatistics2Activity.this, StudyRecordListActivity.class);
                intent1.putExtra("type","video");
                startActivity(intent1);
                break;

            case R.id.id_tv_study_record_title3:
                tvTitle1.setTextColor(Color.parseColor("#3e3e3e"));
                tvTitle2.setTextColor(Color.parseColor("#3e3e3e"));
                tvTitle3.setTextColor(Color.parseColor("#3897F9"));
                ivTitle1.setVisibility(View.INVISIBLE);
                ivTitle2.setVisibility(View.INVISIBLE);
                ivTitle3.setVisibility(View.VISIBLE);
                Intent intent2 = new Intent(UserStatistics2Activity.this, StudyRecordMyExaminationActivity.class);
                intent2.putExtra("type","video");
                startActivity(intent2);
                break;

            case R.id.id_ll_study_description:
                Intent intent = new Intent(UserStatistics2Activity.this, StudySummaryActivity.class);
                intent.putStringArrayListExtra("loginList", (ArrayList<String>) loginList);
                intent.putExtra("video_num", video_num);
                intent.putExtra("article_num", article_num);
                intent.putExtra("total_online_time", total_online_time);
                intent.putExtra("continueDays", continueDays);
                intent.putExtra("depict", depict);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setValue() {
        MyProgressBarDialogTools.show(context);

        String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || "".equals(uid)) {
            startActivity(new Intent(UserStatistics2Activity.this, LoginActivity.class));
            finish();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.studySummary);
                    obj.put("uid", SharedPreferencesTools.getUids(context));

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看学习记录：", result);

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

    private void setText() {
        tvTotalTime.setText(total_online_time + "小时");
        tvContinueDay.setText(continueDays + "天");
    }

    private void initLineBar() {
        if(values.size()>0){
            mLineChart.setVisibility(View.VISIBLE);
            ChartUtils.initChart(mLineChart,context);
            ChartUtils.notifyDataSetChanged(mLineChart, values, dateX);
        }
    }

    private List<Entry> getData() {
        List<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 0));
        values.add(new Entry(1, 0));
        values.add(new Entry(2, 0));
        values.add(new Entry(3, 0));
        values.add(new Entry(4, 0));
        values.add(new Entry(5, 0));
        values.add(new Entry(6, 0));
        return values;
    }

    private List<String> getXData(){
        List<String> dataList=new ArrayList<>();

        return dataList;
    }

    public void back(View view) {
        finish();
    }
}
