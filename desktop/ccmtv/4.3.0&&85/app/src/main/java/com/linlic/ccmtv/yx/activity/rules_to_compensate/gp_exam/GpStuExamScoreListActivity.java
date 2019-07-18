package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment.GpStuDailyExamScoreListFragment;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment.GpStuGraduateExamArrangeListFragment;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment.GpStuGraduateExamScoreListFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import static com.linlic.ccmtv.yx.R.id.id_ll_select_view_type;
import static com.linlic.ccmtv.yx.R.id.id_ll_select_view_type_arrange;
import static com.linlic.ccmtv.yx.R.id.id_ll_select_view_type_score_report;

/**
 * 规培学员日常考核成绩列表界面
 * Created by bentley on 2018/6/26.
 */
public class GpStuExamScoreListActivity extends FragmentActivity implements View.OnClickListener {

    private Context context;
    private TextView title_name;

    private LinearLayout llSelectType;
    private LinearLayout llSelectTypeArrange;
    private LinearLayout llSelectTypeScoreReport;
    private ImageView ivArrange;
    private ImageView ivScoreReport;
    private View viewLine1;
    private FrameLayout flContainer;
    private GpStuDailyExamScoreListFragment dailyExamScoreListFragment;
    private GpStuGraduateExamArrangeListFragment graduateExamArrangeListFragment;
    private GpStuGraduateExamScoreListFragment graduateExamScoreListFragment;
    private FragmentManager fragmentManager;

    private String exam_type = "";
    private String daily_exam_id = "";
    private String year = "";
    private String month = "";
    private String fid = "";
    private String resultString = "";
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        resultString = msg.obj.toString();
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) { // 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                Bundle bundle = new Bundle();
                                bundle.putString("fid", fid);
                                bundle.putString("resultString", resultString);
                                graduateExamArrangeListFragment.setArguments(bundle);

                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.add(R.id.id_fl_container, graduateExamArrangeListFragment);
                                transaction.commitAllowingStateLoss();
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
        setContentView(R.layout.activity_gp_student_exam_list);
        context = this;
        findId();
        fragmentManager = getSupportFragmentManager();
        getIntentData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //保存推出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        if ("84".equals(fid)){//考核基地
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/exambase/index.html";
        } else {
            enterUrl = "http://yun.ccmtv.cn/admin.php/wx/allexam/index.html";
        }
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
    }

    private void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        llSelectType = (LinearLayout) findViewById(id_ll_select_view_type);
        llSelectTypeArrange = (LinearLayout) findViewById(id_ll_select_view_type_arrange);
        llSelectTypeScoreReport = (LinearLayout) findViewById(id_ll_select_view_type_score_report);
        ivArrange = (ImageView) findViewById(R.id.id_iv_title_arrange);
        ivScoreReport = (ImageView) findViewById(R.id.id_iv_title_score_report);
        viewLine1 = findViewById(R.id.id_view_line1);
        flContainer = (FrameLayout) findViewById(R.id.id_fl_container);


        llSelectTypeArrange.setOnClickListener(this);
        llSelectTypeScoreReport.setOnClickListener(this);

    }

    private void getIntentData() {
        exam_type = getIntent().getStringExtra("exam_type");
        daily_exam_id = getIntent().getStringExtra("exam_id");
        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        fid = getIntent().getStringExtra("fid");
    }

    private void initViews() {
        try {
            dailyExamScoreListFragment = new GpStuDailyExamScoreListFragment();
            graduateExamArrangeListFragment = new GpStuGraduateExamArrangeListFragment();
            graduateExamScoreListFragment = new GpStuGraduateExamScoreListFragment();

            if (exam_type.equals("1")) { //1是日常考核 2是出科考核
                title_name.setText("日常考核");
                llSelectType.setVisibility(View.GONE);
                viewLine1.setVisibility(View.GONE);

                Bundle bundle = new Bundle();
                bundle.putString("daily_exam_id", daily_exam_id);
                bundle.putString("fid", fid);
                dailyExamScoreListFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.id_fl_container, dailyExamScoreListFragment);
                transaction.commitAllowingStateLoss();
            } else {
                title_name.setText("出科考核");
                llSelectType.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.VISIBLE);

                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userlistDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("year", year);
                    obj.put("month", month);

                    /*obj.put("uid", "133");
                    obj.put("year", "2018");
                    obj.put("month", "06");*/
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培出科考核住培生查看数据：", result);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_ll_select_view_type_arrange:
                ivArrange.setVisibility(View.VISIBLE);
                ivScoreReport.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("fid", fid);
                bundle.putString("resultString", resultString);
                graduateExamArrangeListFragment.setArguments(bundle);
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.replace(R.id.id_fl_container, graduateExamArrangeListFragment);
                transaction1.commitAllowingStateLoss();
                break;
            case R.id.id_ll_select_view_type_score_report:
                ivArrange.setVisibility(View.INVISIBLE);
                ivScoreReport.setVisibility(View.VISIBLE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("fid", fid);
                bundle1.putString("resultString", resultString);
                graduateExamScoreListFragment.setArguments(bundle1);
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.id_fl_container, graduateExamScoreListFragment);
                transaction2.commitAllowingStateLoss();
                break;
        }
    }

    public void back(View view) {
        finish();
    }
}
