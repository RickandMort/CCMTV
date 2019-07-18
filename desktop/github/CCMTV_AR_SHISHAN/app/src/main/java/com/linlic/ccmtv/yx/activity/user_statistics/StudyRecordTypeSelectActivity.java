package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

public class StudyRecordTypeSelectActivity extends BaseActivity implements View.OnClickListener{

    private TextView title_name;
    private ImageView ivTitleRight;
    private ImageView ivTitleLeft;
    private LinearLayout llActivityTitle;

    private LinearLayout llVideo;
    private LinearLayout llAudio;
    private LinearLayout llArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_record_type_select);

        llActivityTitle = (LinearLayout) findViewById(R.id.id_ll_activity_title_8);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        ivTitleLeft = (ImageView) findViewById(R.id.id_iv_activity_title_8);
        llVideo = (LinearLayout) findViewById(R.id.id_ll_study_record_type_video);
        llAudio = (LinearLayout) findViewById(R.id.id_ll_study_record_type_audio);
        llArticle = (LinearLayout) findViewById(R.id.id_ll_study_record_type_article);

        llActivityTitle.setBackgroundColor(Color.parseColor("#f3f3f3"));
        title_name.setText("记录分类");
        title_name.setTextSize(20);
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleLeft.setVisibility(View.GONE);
        ivTitleRight.setImageResource(R.mipmap.study_record_type_close);

        ivTitleRight.setOnClickListener(this);
        llVideo.setOnClickListener(this);
        llAudio.setOnClickListener(this);
        llArticle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_iv_activity_title_8_right:
                finish();
                break;
            case R.id.id_ll_study_record_type_video:
                Intent intent1=new Intent(StudyRecordTypeSelectActivity.this,StudyRecordListActivity.class);
                intent1.putExtra("type","video");
                startActivity(intent1);
                break;

            case R.id.id_ll_study_record_type_audio:
                Intent intent2=new Intent(StudyRecordTypeSelectActivity.this,StudyRecordListActivity.class);
                intent2.putExtra("type","audio");
                startActivity(intent2);
                break;

            case R.id.id_ll_study_record_type_article:
                Intent intent3=new Intent(StudyRecordTypeSelectActivity.this,StudyRecordListActivity.class);
                intent3.putExtra("type","article");
                startActivity(intent3);
                break;
        }
    }

    public void back(View view) {
        finish();
    }
}
