package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * name：积分任务
 * author：Larry
 * data：2017/3/16.
 */
public class MyIntegralTaskActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    RelativeLayout layout_sharevideo;
    RelativeLayout layout_commentvideo;
    RelativeLayout layout_daily_attendance;
    RelativeLayout layout_invite_registration;
    RelativeLayout layout_upload_video;
    RelativeLayout layout_upload_case;
    RelativeLayout layout_perfect_information;
    private Button layout_daily_attendance_button, layout_sharevideo_button, layout_commentvideo_button, layout_invite_registration_button, layout_upload_video_button, layout_upload_case_button, layout_perfect_information_button;
    String Str_hyleibie, Str_cityname, Str_bigkeshi, Str_smallkeshi, Str_zhicheng, Str_truename, Str_sexName, Str_sex, Str_cityid, Str_address, Str_idcard, Str_danwei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myintegraltask);
        context = this;

        findId();
        setText();
        //获取传过来的数据
        getMyIntent();
    }

    private void getMyIntent() {
        Str_hyleibie = getIntent().getStringExtra("Str_hyleibie");
        Str_cityname = getIntent().getStringExtra("Str_cityname");
        Str_bigkeshi = getIntent().getStringExtra("Str_bigkeshi");
        Str_smallkeshi = getIntent().getStringExtra("Str_smallkeshi");
        Str_zhicheng = getIntent().getStringExtra("Str_zhicheng");
        Str_truename = getIntent().getStringExtra("Str_truename");
        Str_sexName = getIntent().getStringExtra("Str_sexName");
        Str_sex = getIntent().getStringExtra("Str_sex");
        Str_cityid = getIntent().getStringExtra("Str_cityid");
        Str_address = getIntent().getStringExtra("Str_address");
        Str_idcard = getIntent().getStringExtra("Str_idcard");
        Str_danwei = getIntent().getStringExtra("Str_danwei");
    }

    public void findId() {
        super.findId();
        layout_sharevideo = (RelativeLayout) findViewById(R.id.layout_sharevideo);
        layout_commentvideo = (RelativeLayout) findViewById(R.id.layout_commentvideo);
        layout_daily_attendance = (RelativeLayout) findViewById(R.id.layout_daily_attendance);
        layout_invite_registration = (RelativeLayout) findViewById(R.id.layout_invite_registration);
        layout_upload_video = (RelativeLayout) findViewById(R.id.layout_upload_video);
        layout_upload_case = (RelativeLayout) findViewById(R.id.layout_upload_case);
        layout_perfect_information = (RelativeLayout) findViewById(R.id.layout_perfect_information);
        layout_daily_attendance_button = (Button) findViewById(R.id.layout_daily_attendance_button);
        layout_sharevideo_button = (Button) findViewById(R.id.layout_sharevideo_button);
        layout_commentvideo_button = (Button) findViewById(R.id.layout_commentvideo_button);
        layout_invite_registration_button = (Button) findViewById(R.id.layout_invite_registration_button);
        layout_upload_video_button = (Button) findViewById(R.id.layout_upload_video_button);
        layout_upload_case_button = (Button) findViewById(R.id.layout_upload_case_button);
        layout_perfect_information_button = (Button) findViewById(R.id.layout_perfect_information_button);
    }

    private void setText() {
        super.setActivity_title_name("积分攻略");
        layout_sharevideo.setOnClickListener(this);
        layout_commentvideo.setOnClickListener(this);
        layout_daily_attendance.setOnClickListener(this);
        layout_invite_registration.setOnClickListener(this);
        layout_upload_video.setOnClickListener(this);
        layout_upload_case.setOnClickListener(this);
        layout_perfect_information.setOnClickListener(this);
        layout_daily_attendance_button.setOnClickListener(this);
        layout_sharevideo_button.setOnClickListener(this);
        layout_commentvideo_button.setOnClickListener(this);
        layout_invite_registration_button.setOnClickListener(this);
        layout_upload_video_button.setOnClickListener(this);
        layout_upload_case_button.setOnClickListener(this);
        layout_perfect_information_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_sharevideo:
                intent = new Intent(MyIntegralTaskActivity.this, ShareVideoActivity.class);
                break;
            case R.id.layout_sharevideo_button:
                intent = new Intent(MyIntegralTaskActivity.this, ShareVideoActivity.class);
                break;
            case R.id.layout_commentvideo:
                intent = new Intent(MyIntegralTaskActivity.this, CommentVideoActivity.class);
                break;
            case R.id.layout_commentvideo_button:
                intent = new Intent(MyIntegralTaskActivity.this, CommentVideoActivity.class);
                break;
            case R.id.layout_daily_attendance:                                         //每日签到
                intent = new Intent(MyIntegralTaskActivity.this, DailyAttendanceActivity.class);
                break;
            case R.id.layout_daily_attendance_button:                                         //每日签到
                intent = new Intent(MyIntegralTaskActivity.this, DailyAttendanceActivity.class);
                break;
            case R.id.layout_invite_registration:                                      //邀请注册
                intent = new Intent(MyIntegralTaskActivity.this, InviteRegistrationActivity.class);
                break;
            case R.id.layout_invite_registration_button:                                      //邀请注册
                intent = new Intent(MyIntegralTaskActivity.this, InviteRegistrationActivity.class);
                break;
            case R.id.layout_upload_video:
                intent = new Intent(MyIntegralTaskActivity.this, UploadVideoActivity.class);
                break;
            case R.id.layout_upload_video_button:
                intent = new Intent(MyIntegralTaskActivity.this, UploadVideoActivity.class);
                break;
            case R.id.layout_upload_case:
                intent = new Intent(MyIntegralTaskActivity.this, UploadCaseActivity.class);
                break;
            case R.id.layout_upload_case_button:
                intent = new Intent(MyIntegralTaskActivity.this, UploadCaseActivity.class);
                break;
            case R.id.layout_perfect_information:
                intent = new Intent(MyIntegralTaskActivity.this, PerfectInformationActivity.class);
                intent.putExtra("Str_hyleibie", Str_hyleibie);
                intent.putExtra("Str_cityname", Str_cityname);
                intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                intent.putExtra("Str_zhicheng", Str_zhicheng);
                intent.putExtra("Str_truename", Str_truename);
                intent.putExtra("Str_sexName", Str_sexName);
                intent.putExtra("Str_sex", Str_sex);
                intent.putExtra("Str_cityid", Str_cityid);
                intent.putExtra("Str_address", Str_address);
                intent.putExtra("Str_idcard", Str_idcard);
                intent.putExtra("Str_danwei", Str_danwei);
                break;
            case R.id.layout_perfect_information_button:
                intent = new Intent(MyIntegralTaskActivity.this, PerfectInformationActivity.class);
                intent.putExtra("Str_hyleibie", Str_hyleibie);
                intent.putExtra("Str_cityname", Str_cityname);
                intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                intent.putExtra("Str_zhicheng", Str_zhicheng);
                intent.putExtra("Str_truename", Str_truename);
                intent.putExtra("Str_sexName", Str_sexName);
                intent.putExtra("Str_sex", Str_sex);
                intent.putExtra("Str_cityid", Str_cityid);
                intent.putExtra("Str_address", Str_address);
                intent.putExtra("Str_idcard", Str_idcard);
                intent.putExtra("Str_danwei", Str_danwei);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
