package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

/**
 * Created by Administrator on 2017/9/13.
 */
public class Hand_in_the_papers extends BaseActivity {

    private Context context;
    public static String pid;
    private TextView hand_in_the_papers_text1, hand_in_the_papers_point;
    private Button examination_instructions_buttpm, examination_instructions_buttpm1;
    private String my_exams_id = "";
    private String my_exams_eid = "";
    private String hasper = "";
    private String score_show = "";
    private String restart_code = "";
    private String is_mock_exam = "";
    private LinearLayout layout1,layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hand_in_the_papers);
        context = this;
        findId();
        initdata();
    }

    public void findId() {
        hand_in_the_papers_text1 = (TextView) findViewById(R.id.hand_in_the_papers_text1);
        hand_in_the_papers_point = (TextView) findViewById(R.id.hand_in_the_papers_point);
        examination_instructions_buttpm = (Button) findViewById(R.id.examination_instructions_buttpm);
        examination_instructions_buttpm1 = (Button) findViewById(R.id.examination_instructions_buttpm1);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
    }

    public void initdata() {
        MyProgressBarDialogTools.show(context);
        pid = getIntent().getStringExtra("pid");
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        my_exams_eid = getIntent().getStringExtra("my_exams_eid");
        is_mock_exam = getIntent().getStringExtra("is_mock_exam");
        score_show = getIntent().getStringExtra("score_show");
        //是否还有考试机会
        restart_code = getIntent().getStringExtra("restart_code");
                            /*得分字段1*/
        hand_in_the_papers_point.setText(getIntent().getStringExtra("hand_in_the_papers_point"));
                            /*注*/
        hand_in_the_papers_text1.setText(getIntent().getStringExtra("hand_in_the_papers_text1"));
        //是否可查看答题卡
        hasper = getIntent().getStringExtra("hasper");

        if (Integer.parseInt(hasper) > 0) {
            examination_instructions_buttpm1.setBackgroundResource(R.mipmap.button_01);
            examination_instructions_buttpm1.setClickable(true);
            examination_instructions_buttpm1.setTextColor(Color.parseColor("#ffffff"));
        } else {
            examination_instructions_buttpm1.setBackgroundResource(R.mipmap.button_03);
            examination_instructions_buttpm1.setClickable(false);
            examination_instructions_buttpm1.setTextColor(Color.parseColor("#666666"));
        }
        if (Integer.parseInt(restart_code) > 0) {
            examination_instructions_buttpm.setBackgroundResource(R.mipmap.button_01);
            examination_instructions_buttpm.setClickable(true);
            examination_instructions_buttpm.setTextColor(Color.parseColor("#ffffff"));
        } else {
            examination_instructions_buttpm.setBackgroundResource(R.mipmap.button_03);
            examination_instructions_buttpm.setClickable(false);
            examination_instructions_buttpm.setTextColor(Color.parseColor("#666666"));
        }
        if (Integer.parseInt(score_show) > 0) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        } else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyProgressBarDialogTools.hide();
            }
        },3000);
    }

    /*查看答题卡*/
    public void enter_the_examination(View view) {
        if (Integer.parseInt(hasper) > 0) {
            Intent intent = new Intent(Hand_in_the_papers.this, Check_the_answer_sheet.class);
            intent.putExtra("pid", pid);
            intent.putExtra("my_exams_id", my_exams_id);
            intent.putExtra("my_exams_eid", my_exams_eid);
            intent.putExtra("is_mock_exam", is_mock_exam);
            startActivity(intent);
        }
    }

    public void back1(View view) {
//        Log.e("点击按钮","点击");
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + pid;
        super.onPause();
    }
}

