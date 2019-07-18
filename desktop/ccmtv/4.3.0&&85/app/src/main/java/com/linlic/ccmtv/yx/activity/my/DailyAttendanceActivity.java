package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;


/**
 * name：每日签到
 * author：Larry
 * data：2017/3/16.
 */
public class DailyAttendanceActivity extends BaseActivity {
    Context context;
    private ImageView img;
    private TextView text1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
//                            Toast.makeText(DailyAttendanceActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            text1.setText(result.getJSONObject("data").getString("integration"));
                            SharedPreferencesTools.savecurrentDayMonyFlg(DailyAttendanceActivity.this,"1");
                            SharedPreferencesTools.saveIntegral(context,result.getJSONObject("data").getString("integration"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(R.mipmap.integral_introduction_icon10).into(img);
                                    img.setClickable(false);
                                }
                            },3000);


                        } else {//失败
                            Toast.makeText(DailyAttendanceActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(R.mipmap.integral_introduction_icon07).into(img);
                                    img.setClickable(true);
                                }
                            },3000);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(DailyAttendanceActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance);
        context = this;
         findId();
        initView();
    }

    @Override
    public void findId() {
        img = (ImageView) findViewById(R.id.img);
        text1 = (TextView) findViewById(R.id.text1);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setClickable(false);
                RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(context)
                        .asGif()
                        .load(R.mipmap.integral_introduction_icon09)
                        .apply(options)
                        .into(img);
                chcheckIntegration();
            }
        });
        //判断用户是否已签到
        if (SharedPreferencesTools.getcurrentDayMonyFlg(DailyAttendanceActivity.this).equals("1")) {//已签到
            Glide.with(context).load(R.mipmap.integral_introduction_icon10).into(img);
            img.setClickable(false);
        } else {//未签到
            Glide.with(context).load(R.mipmap.integral_introduction_icon07).into(img);
            img.setClickable(true);
        }
    }

    public void initView(){
//        lean. setmDegrees(45);
        text1.setText(SharedPreferencesTools.getIntegral(context));
    }

    public void chcheckIntegration( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", SharedPreferencesTools.getUid(DailyAttendanceActivity.this));
                    obj.put("act", URLConfig.getIntegration);
                    String result = HttpClientUtils.sendPost(DailyAttendanceActivity.this, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("积分签到",result);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
