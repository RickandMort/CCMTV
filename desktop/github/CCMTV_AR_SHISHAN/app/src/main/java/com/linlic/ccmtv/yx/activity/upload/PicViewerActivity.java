package com.linlic.ccmtv.yx.activity.upload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.my.learning_task.PPTSignActivity;
import com.linlic.ccmtv.yx.activity.my.learning_task.SendSignInfo;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Examination_instructions2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ViewPagerFixed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * name：带滑动的图片放大器
 * author：Larry
 * data：2016/5/9 19:19
 */

/**
 * 图片缩放时java.lang.IllegalArgumentException: pointerIndex out of range解决方案
 * http://blog.csdn.net/nnmmbb/article/details/28419779
 */
public class PicViewerActivity extends FragmentActivity {
    Context context;
    //用户统计
    private String entertime, leavetime;
    private String enterUrl, type;
    private ViewPagerFixed viewPager;  //自定义ViewPager
    private boolean mReceiverTag = false;   //广播接受者标识
    private TextView tv_indicator, pic_sign1, pic_sign2, tv_pic_sign;
    private ArrayList<String> urlList;
    private String tid, pptid, signnum, sign_num, be_sign_number, taskid;
    private int current_index;
    public static PicViewerActivity picActivity;
    private LinearLayout ll_pic_sign, ll_pic_sign2, ll_pic_sign3;
    private Intent intent;
    private int mPosition = 0;
    private int clickNum = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String type = jsonObject.getString("type" + "");
                        if (jsonObject.getInt("status") == 1) {
                            SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) - 1) + "");
                            if (type.equals("1")) {//显示自测
                                int num = Integer.parseInt(pic_sign1.getText().toString()) + 1;
                                clickNum = 0;
                                if (num > Integer.parseInt(sign_num)) {
                                    pic_sign1.setText(sign_num);
                                } else {
                                    pic_sign1.setText(num + "");
                                }
                                SharedPreferencesTools.saveSign(context, pic_sign1.getText().toString());

                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                tv_pic_sign.setText("您可以进行测试了~");
                                ll_pic_sign3.setVisibility(View.VISIBLE);
                                ll_pic_sign2.setVisibility(View.GONE);
                                ll_pic_sign3.setClickable(true);
                                ll_pic_sign2.setClickable(false);
                                ll_pic_sign3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Examination_instructions2.class);
                                        intent.putExtra("aid", pptid);
                                        intent.putExtra("tid", tid);
                                        intent.putExtra("type", "ppt");
                                        intent.putExtra("pptid", pptid);
                                        startActivity(intent);
                                    }
                                });
                            } else if (type.equals("0")) {//不显示自测
                                int num = Integer.parseInt(pic_sign1.getText().toString()) + 1;
                                pic_sign1.setText(num + "");
                                SharedPreferencesTools.saveSign(context, pic_sign1.getText().toString());
                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                clickNum = 0;
                                ll_pic_sign2.setClickable(false);
                                ll_pic_sign.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) - 1) + "");
                        int num = Integer.parseInt(pic_sign1.getText().toString()) + 1;
                        pic_sign1.setText(num + "");
                        SharedPreferencesTools.saveSign(context, pic_sign1.getText().toString());
                        Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                        clickNum = 0;
                        ll_pic_sign2.setClickable(false);
                        ll_pic_sign.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };
    private int signCount;
    private JSONObject Json = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_viewer);
        context = this;

        taskid = getIntent().getStringExtra("tid");
        type = getIntent().getStringExtra("type");

        if (!mReceiverTag) {
            mReceiverTag = true;
            IntentFilter myIntentFilter = new IntentFilter();
            myIntentFilter.addAction("sign");
            //注册广播
            registerReceiver(mBroadcastReceiver, myIntentFilter);
        }

        picActivity = this;

        urlList = getIntent().getStringArrayListExtra("urls_case");
        current_index = getIntent().getIntExtra("current_index", 0);
        sign_num = getIntent().getStringExtra("sign_num");//签到总数
        be_sign_number = getIntent().getStringExtra("be_sign_number");//已签到数

        try {
            if (type.equals("ppt")) {
                is_miss = getIntent().getStringExtra("is_miss");
                String jsonString = SharedPreferencesTools.getSign_Time_Json(context);
                if (jsonString != null && !"".equals(jsonString)) {
                    Json = new JSONObject(jsonString);
                }else {
                    Json = new JSONObject();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //urlList = new ArrayList<>();
        // Collections.addAll(urlList, urls);

        viewPager = (ViewPagerFixed) findViewById(R.id.viewpager);
        tv_indicator = (TextView) findViewById(R.id.tv_indicator);
        ll_pic_sign = (LinearLayout) findViewById(R.id.ll_pic_sign);
        pic_sign1 = (TextView) findViewById(R.id.pic_sign1);
        pic_sign2 = (TextView) findViewById(R.id.pic_sign2);
        tv_pic_sign = (TextView) findViewById(R.id.tv_pic_sign);
        ll_pic_sign2 = (LinearLayout) findViewById(R.id.ll_pic_sign2);
        ll_pic_sign3 = (LinearLayout) findViewById(R.id.ll_pic_sign3);

        pic_sign1.setText(SharedPreferencesTools.getSign(context));
        pic_sign2.setText(sign_num);

//        SharedPreferencesTools.saveSign(context, be_sign_number);

        intent = new Intent();

        tv_indicator.setText(String.valueOf(current_index + 1) + "/" + urlList.size());
        //getSupportFragmentManager()
        viewPager.setAdapter(new PictureSlidePagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(current_index);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_indicator.setText(String.valueOf(position + 1) + "/" + urlList.size());
                mPosition = position;
                /*intent.putExtra("position", position);
                setResult(1, intent);*/
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //保存推出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        if (type.equals("my_case")) {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        } else if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        }
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        super.onPause();
    }

    private class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {

        public PictureSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PictureSlideFragment.newInstance(urlList.get(position));
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }

    @Override
    public void finish() {
        picActivity = null;
        intent = new Intent();
        intent.putExtra("be_sign_number", SharedPreferencesTools.getSign(context));
        intent.putExtra("position", mPosition);
        intent.putExtra("click_num", clickNum);
        setResult(2, intent);
        super.finish();
    }

    private String is_miss;
    private String signTimeString;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("sign")) {
                tid = intent.getStringExtra("tid");
                pptid = intent.getStringExtra("pptid");
                signnum = intent.getStringExtra("signnum");
                ll_pic_sign.setVisibility(View.VISIBLE);
                clickNum = 1;

                try {
                    if (type.equals("ppt")) {
                        is_miss = getIntent().getStringExtra("is_miss");
                        String jsonString = SharedPreferencesTools.getSign_Time_Json(context);
                        if (jsonString != null && !"".equals(jsonString)) {
                            Json = new JSONObject(jsonString);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ll_pic_sign2.setClickable(true);
                ll_pic_sign2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTaskPptInfo();
                    }
                });
                CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        ll_pic_sign.setVisibility(View.GONE);
                    }
                }.start();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (mReceiverTag) {
            mReceiverTag = false;
            unregisterReceiver(mBroadcastReceiver);
        }
    }

    private void getTaskPptInfo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /*try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.taskVideoSign);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("aid", pptid);
                    obj.put("signnum", signnum);
                    Log.e("看看sign数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
                    Log.e("看看sign数据", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }*/
                try {
                    signCount = Integer.parseInt(SharedPreferencesTools.getSign(PicViewerActivity.this) + 1);

                    Json.put(signCount + "", System.currentTimeMillis());//JSONObject对象中添加键值对
                    SharedPreferencesTools.saveSign_Time_Json(context, Json.toString());
                    if (is_miss.equals("0")) {
                        String result = SendSignInfo.sendSign(PicViewerActivity.this, tid, pptid, signnum);

                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else if (signCount >= Integer.parseInt(sign_num)) {
                        JSONArray jsonArraySignTime = new JSONArray();
                        jsonArraySignTime.put(Json);//将JSONObject对象添加到Json数组中
                        String result = SendSignInfo.sendSign(PicViewerActivity.this, tid, pptid, jsonArraySignTime.toString());
                        /*JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", pptid);
                        obj.put("signnum", jsonArraySignTime);
                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
                        Log.e("看看sign数据", result);*/

                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = 0;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }
}
