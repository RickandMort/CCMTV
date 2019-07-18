package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class StudySummaryActivity extends BaseActivity implements View.OnClickListener,
        CalendarView.OnYearChangeListener, CalendarView.OnDateSelectedListener {

    private TextView title_name;
    private TextView tvData;
    private TextView tvStudyDuration;
    private TextView tvHint;
    private ImageView ivMonthToLeft, ivMonthToRight;
    private LinearLayout llLookVideoCount, llListenAudioCount, llLookArticleCount, llStudyContinuityCount;
    private TextView tvLookVideoCount, tvListenAudioCount, tvLookArticleCount, tvStudyContinuityCount;
    private CalendarView mCalendarView;
    private int mYear;
    private int mMonth;
    private Intent intent;
    private List<String> loginList = new ArrayList<>();
    private String total_online_time, video_num, continueDays, article_num, depict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_summary);

        intent = getIntent();
        findId();
        initView();
        initData();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        tvData = (TextView) findViewById(R.id.id_tv_study_summary_year_month);
        tvStudyDuration = (TextView) findViewById(R.id.id_tv_study_summary_duration);
        tvHint = (TextView) findViewById(R.id.id_tv_study_summary_hint);
        ivMonthToLeft = (ImageView) findViewById(R.id.id_iv_study_summary_month_left);
        ivMonthToRight = (ImageView) findViewById(R.id.id_iv_study_summary_month_right);

        llLookVideoCount = (LinearLayout) findViewById(R.id.id_ll_study_summary_look_video_count);
        llListenAudioCount = (LinearLayout) findViewById(R.id.id_ll_study_summary_listen_audio_count);
        llLookArticleCount = (LinearLayout) findViewById(R.id.id_ll_study_summary_look_article_count);
        llStudyContinuityCount = (LinearLayout) findViewById(R.id.id_ll_study_summary_continuity_count);

        tvLookVideoCount = (TextView) findViewById(R.id.id_tv_study_summary_look_video_count);
        tvListenAudioCount = (TextView) findViewById(R.id.id_tv_study_summary_listen_audio_count);
        tvLookArticleCount = (TextView) findViewById(R.id.id_tv_study_summary_look_article_count);
        tvStudyContinuityCount = (TextView) findViewById(R.id.id_tv_study_summary_continuity_count);

        ivMonthToLeft.setOnClickListener(this);
        ivMonthToRight.setOnClickListener(this);
        llLookVideoCount.setOnClickListener(this);
        llListenAudioCount.setOnClickListener(this);
        llLookArticleCount.setOnClickListener(this);
        llStudyContinuityCount.setOnClickListener(this);

        mCalendarView.setSelected(false);

        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnClickListener(this);
    }

    private void initView() {
        try {
            loginList = intent.getStringArrayListExtra("loginList");
            total_online_time = intent.getStringExtra("total_online_time");
            video_num = intent.getStringExtra("video_num");
            continueDays = intent.getStringExtra("continueDays");
            article_num = intent.getStringExtra("article_num");
            depict = intent.getStringExtra("depict");
            title_name.setText("学习汇总");
            title_name.setTextSize(20);
            mYear = mCalendarView.getCurYear();
            mMonth = mCalendarView.getCurMonth();
            tvData.setText(mYear + "年" + mMonth + "月");
            tvStudyDuration.setText(total_online_time+"小时");
            tvLookVideoCount.setText(video_num);
            tvLookArticleCount.setText(article_num);
            tvStudyContinuityCount.setText(continueDays);
            tvHint.setText(depict);
        }catch (Exception e){
            e.printStackTrace();
        }
        mCalendarView.setSchemeColor(Color.parseColor("#3698f9"), Color.parseColor("#ffffff"), Color.parseColor("#00ffffff"));
    }

    protected void initData() {
        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        for (int i = 0; i < loginList.size(); i++) {
            String date = loginList.get(i);
            String[] dates = date.split("-");
            schemes.add(getSchemeCalendar(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]), 0, ""));
            mCalendarView.setSchemeDate(schemes);
        }

        /*schemes.add(getSchemeCalendar(year, month, 3, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 6, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 9, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 13, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 14, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 15, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 18, 0, ""));
        schemes.add(getSchemeCalendar(year, month, 25, 0, ""));
        mCalendarView.setSchemeDate(schemes);*/
    }

    @SuppressWarnings("all")
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_iv_study_summary_month_left:
                mCalendarView.scrollToPre();
                /*mYear = mCalendarView.getCurYear();
                mMonth = mCalendarView.getCurMonth();*/
                //mCalendarView.postInvalidateOnAnimation();
                /*if (mMonth == 1) {
                    mYear--;
                    mMonth = 12;
                } else {
                    mMonth--;
                }*/
                tvData.setText(mYear + "年" + mMonth + "月");
                break;
            case R.id.id_iv_study_summary_month_right:
                mCalendarView.scrollToNext();
                /*mYear = mCalendarView.getCurYear();
                mMonth = mCalendarView.getCurMonth();*/
                /*if (mMonth == 12) {
                    mYear++;
                    mMonth = 1;
                } else {
                    mMonth++;
                }*/
                tvData.setText(mYear + "年" + mMonth + "月");
                break;
            case R.id.id_ll_study_summary_look_video_count:
                Intent intent1 = new Intent(StudySummaryActivity.this, StudyRecordListActivity.class);
                intent1.putExtra("type", "video");
                startActivity(intent1);
                break;

            case R.id.id_ll_study_summary_listen_audio_count:
                Intent intent2 = new Intent(StudySummaryActivity.this, StudyRecordListActivity.class);
                intent2.putExtra("type", "audio");
                startActivity(intent2);
                break;

            case R.id.id_ll_study_summary_look_article_count:
                Intent intent3 = new Intent(StudySummaryActivity.this, StudyRecordListActivity.class);
                intent3.putExtra("type", "article");
                startActivity(intent3);
                break;

            default:
                break;
        }
    }

    /*@Override
    public void onMonthChange(int year, int month) {
        tvData.setText(year + "年" + month + "月");
    }*/

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        mMonth = calendar.getMonth();
        tvData.setText(mYear + "年" + mMonth + "月");
    }

    @Override
    public void onYearChange(int year) {

    }
}
