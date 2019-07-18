package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.YKDetailActivity;
import com.linlic.ccmtv.yx.activity.check_work_attendance.Qr_code;
import com.linlic.ccmtv.yx.activity.home.yxzbjrrom.ScanEmptyActivity;
import com.linlic.ccmtv.yx.activity.login.Qr_code_to_log_in;
import com.linlic.ccmtv.yx.activity.my.book.Video_book_Main;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities.College_level_activities_Details;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Event_Details2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management2;
import com.linlic.ccmtv.yx.activity.videoar.ARFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.MedicineMessageActivity;
import com.linlic.ccmtv.yx.kzbf.SkyProtocolActivity;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.camera.CameraManager;

import org.json.JSONObject;
import org.xutils.common.util.MD5;

public class ScanActivity extends FragmentActivity implements View.OnClickListener {
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    private CaptureFragment captureFragment;
    private ARFragment arFragment;
    private LinearLayout capture, ar;
    private String token;
    private boolean canClick = true;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Intent intent = new Intent(context, Qr_code_to_log_in.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                            finish();
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("0")) {// 成功
                            if (result.getString("sub").equals("1")) {//已签署
                                startActivity(new Intent(context, MedicineMessageActivity.class));
                            } else {// 未签署
                                startActivity(new Intent(context, SkyProtocolActivity.class));
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    canClick = true;
                    break;
                case 7:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            finish();
                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(context, Event_Details2.class);
                                intent.putExtra("id",dataJson.getJSONObject("dataList").getString("id"));
                                intent.putExtra("fid",dataJson.getJSONObject("dataList").getString("fid"));
                                if(intent!=null){
                                    Training_management2.is_new_teaching_activities = true;
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            Intent intent = null;
                            intent = new Intent(context, Qr_code.class);
                            intent.putExtra("status", dataJson.getString("status"));
                            intent.putExtra("statusMsg", dataJson.getString("statusMsg"));

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(context, College_level_activities_Details.class);
                                intent.putExtra("id", dataJson.getString("id"));
                                intent.putExtra("fid", 0);

                                if (intent != null) {
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                Toast.makeText(getApplicationContext(), "签到成功", Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(context, YKDetailActivity.class);
                                intent.putExtra("id", dataJson.getString("id"));
                                intent.putExtra("fid", 0);
                                if (intent != null) {
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        context = this;

        initFragment();
        initView();

        Intent intent = getIntent();
        int defaultType = intent.getIntExtra("defaultType", 0);
        if (defaultType == 0) {
            ivFlashLight.setVisibility(View.VISIBLE);
            changeIconColor(0);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        } else {
            if (isFlashLight) {
                closeLight();
            }
            ivFlashLight.setVisibility(View.GONE);
            changeIconColor(1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, arFragment).commit();
        }
    }

    private void initFragment() {
        captureFragment = new CaptureFragment();

        arFragment = new ARFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    private void initView() {
        capture = (LinearLayout) findViewById(R.id.linear1);
        ar = (LinearLayout) findViewById(R.id.linear2);
        ivScan = (ImageView) findViewById(R.id.id_iv_scan);
        ivAR = (ImageView) findViewById(R.id.id_iv_ar);
        tvScan = (TextView) findViewById(R.id.id_tv_scan);
        tvAR = (TextView) findViewById(R.id.id_tv_ar);
        ivFlashLight = (ImageView) findViewById(R.id.id_iv_flashlight);
        ivFlashLight.setOnClickListener(this);
        capture.setOnClickListener(this);
        ar.setOnClickListener(this);
    }

    private ImageView ivFlashLight;//二维码扫描界面闪光灯图标
    private ImageView ivScan, ivAR;//底部选择扫描和二维码的两个图标
    private TextView tvScan, tvAR;//底部选择扫描和二维码的两个文字
    private Camera.Parameters parameter;
    private Camera m_Camera = null;// 声明Camera对象
    private boolean isFlashLight = false;//初始化默认闪光灯关闭

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear1:
                if (canClick) {
                    canClick = false;
                    ivFlashLight.setVisibility(View.VISIBLE);
                    changeIconColor(0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commitAllowingStateLoss();
                    handler.sendEmptyMessageDelayed(6, 2000);
                } else {
                    Toast.makeText(context, "请不要频繁操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.linear2:
                if (canClick) {
                    canClick = false;
                    if (isFlashLight) {
                        closeLight();
                    }
                    ivFlashLight.setVisibility(View.GONE);
                    changeIconColor(1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, arFragment).commitAllowingStateLoss();
                    handler.sendEmptyMessageDelayed(6, 2000);
                } else {
                    Toast.makeText(context, "请不要频繁操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.id_iv_flashlight:
                //获取到ZXing相机管理器创建的camera
                try {
                    m_Camera = CameraManager.get().getCamera();
                    parameter = m_Camera.getParameters();
                    // TODO 开灯
                    if (!isFlashLight) {
                        Toast.makeText(this, "点击开启闪光灯", Toast.LENGTH_SHORT).show();
                        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        m_Camera.setParameters(parameter);
                        isFlashLight = true;
                        ivFlashLight.setImageResource(R.mipmap.ic_flashlight_on);
                    } else {  // 关灯
                        closeLight();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 改变底部扫描和AR的图标和文字颜色
     *
     * @param position
     */
    private void changeIconColor(int position) {
        if (position == 0) {
            ivScan.setImageResource(R.mipmap.ic_scan_bottom_select);
            ivAR.setImageResource(R.mipmap.ic_ar_bottom_normal);
            tvScan.setTextColor(Color.parseColor("#3897F9"));
            tvAR.setTextColor(Color.WHITE);
        } else {
            ivScan.setImageResource(R.mipmap.ic_scan_bottom_normal);
            ivAR.setImageResource(R.mipmap.ic_ar_bottom_select);
            tvScan.setTextColor(Color.WHITE);
            tvAR.setTextColor(Color.parseColor("#3897F9"));
        }
    }

    public void closeLight() {
        if (m_Camera != null) {
            try {
                Toast.makeText(this, "点击关闭闪光灯", Toast.LENGTH_SHORT).show();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                m_Camera.setParameters(parameter);
                isFlashLight = false;
                ivFlashLight.setImageResource(R.mipmap.ic_flashlight_on);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//            LogUtil.e("处理二维码扫描结果","ScanActivity");
            token = result;
            if(token.contains("activities")){
                SharedPreferencesTools.getUid(context);
                //访问签到接口
                activitiesSign(result);
            }else if(token.equals("http://www.ccmtv.cn//do/ccmtvappandroid/apptemplete/getapp.php")){
                SharedPreferencesTools.getUid(context);
                closeLight();
                Intent  intent = new Intent(context, Video_book_Main.class);
                startActivity(intent);
                finish();
            }else if(token.contains("userQrCodeSign")){
                userSignQrCode(result);
            }else if(token.contains("Activity_sign")){
                activitySign(result);
            }else if (token.contains("Yueke_sign")) {
                YKSign(result);
            }else if(token.contains("Loginreg/Login/scancode.html")){
                scan_valid();
            }else {
                Intent intent = new Intent(context, ScanEmptyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("token",token);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
            closeLight();
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(context, "解析二维码失败", Toast.LENGTH_LONG).show();
            closeLight();
        }
    };

    public void userSignQrCode(final String url) {
        if( SharedPreferencesTools.getUid(context).length()>0){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj_sj = new JSONObject();
                        obj_sj.put("act", URLConfig.getTimestamp);
                        LogUtil.e("网络访问的地址", URLConfig.CCMTVAPP_kq);
                        String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                        JSONObject jsonObject = new JSONObject(result_sj);
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.userSignQrCode);
                                obj.put("uid", SharedPreferencesTools.getUid(context));
                                obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                                obj.put("param", url+"&uid="+SharedPreferencesTools.getUid(context));
                                obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));

                                String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                                LogUtil.e("扫描签到", result);
                                Message message = new Message();
                                message.what = 8;
                                message.obj = result;
                                handler.sendMessage(message);
                            } else {
                                Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            };
            new Thread(runnable).start();
        }else{
            finish();
        }

    }
    public void activitiesSign(final  String url) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesSign);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("url",url);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("扫描签到", result);
                    Message message = new Message();
                    message.what = 7;
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
    public void scan_valid() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    token = token.substring(token.indexOf("token=") + 6, token.length());
                    obj.put("action", URLConfig.scan_valid);
                    obj.put("token", token);
                    obj.put("uid", uid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.QR_CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 4;
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

    /***
     * 约课签到
     */
    private void YKSign(final  String url){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykSign);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("url",url);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("约课签到", result);
                    Message message = new Message();
                    message.what = 10;
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

    public void activitySign(final String url) {
        if( SharedPreferencesTools.getUid(context).length()>0){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.activitysUserSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("qrcode_url", url);

                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("扫描签到", result);
                        Message message = new Message();
                        message.what = 9;
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

    }

    @Override
    protected void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //保存退出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        closeLight();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        timer.cancel();
//        timers.cancel();
//        Log.e("timerCancel3", "timerCancel3");
    }
}
