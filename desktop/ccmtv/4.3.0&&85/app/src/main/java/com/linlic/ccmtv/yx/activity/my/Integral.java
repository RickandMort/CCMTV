package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.RiseNumberTextView;

import org.json.JSONObject;

/**
 * name:积分首页
 * author:Tom
 * 2016-3-2下午7:03:31
 */
public class Integral extends BaseActivity implements OnClickListener {
    private TextView activity_title_name, tv_nowintegration, tv_integarule, tv_addhao;
    private LinearLayout get_integral;
    private TextView getintegral;
    private RiseNumberTextView bigtv_nowintegration;
    private LinearLayout integral_deduction;
    // private ImageView intgral_main_title_middle_img;
    private LinearLayout intgral_main_title_middle_layout;
    private Button get_free_integral, btn_open_vip, exchange_integral;
    private String uid;
    String Str_icon, Str_username;
    int Str_vipflg;
    String integration, integration_front;
    Context context;
    boolean ishas = false, isfirst = true;  //检查权限接口是否正确（数据库查询出错）
    /**
     * 缩小动画
     **/
    Animation mLitteAnimation = null;

    /**
     * 放大动画
     **/
    Animation mBigAnimation = null;

    //领取积分
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject object = new JSONObject(result.getString("data"));
                            integration = object.getString("integration");
                            SharedPreferencesTools.saveIntegral(context, integration);
                            String integral_state = object.getString("integral_state");
                            //  intgral_main_title_middle_img.setVisibility(View.GONE);
                            intgral_main_title_middle_layout.setVisibility(View.VISIBLE);
                            // bigtv_nowintegration.setText(integration);
                            // bigtv_nowintegration.startAnimation(mLitteAnimation);
                            tv_nowintegration.setText("当前可用积分：" + integration);
                            tv_addhao.setText("+");
                            int mm = Integer.parseInt(integration) - Integer.parseInt(integration_front);
                            getintegral.setText(String.valueOf(mm));

                            // getintegral.withNumber(0, Integer.parseInt(integration) - Integer.parseInt(integration_front)).start();
                            /** 设置透明度渐变动画 */
                            AlphaAnimation animation = new AlphaAnimation(0, 1);
                            animation.setDuration(2000);//设置动画持续时间
                            tv_addhao.startAnimation(animation);
                            getintegral.startAnimation(animation);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                                    animation1.setDuration(1000);//设置动画持续时间
                                    // getintegral.setVisibility(View.GONE);
                                    // tv_addhao.setVisibility(View.GONE);
                                    animation1.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            getintegral.setVisibility(View.GONE);
                                            tv_addhao.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                        }
                                    });
                                    getintegral.startAnimation(animation1);
                                    tv_addhao.startAnimation(animation1);
                                    bigtv_nowintegration.withNumber(Integer.parseInt(integration_front), Integer.parseInt(integration)).start();
                                    integration_front = integration;
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            MainActivity.refresh();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:                                                             //获取权限
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            ishas = true;
                            String integral_state = result.getString("integral_state");
                            integration_front = result.getString("money");
                            // Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            if (Integer.parseInt(integral_state) == 0) {                            //不可领取（已领过）
                                //  intgral_main_title_middle_img.setVisibility(View.GONE);
                                intgral_main_title_middle_layout.setVisibility(View.VISIBLE);
                                findViewById(R.id.integer_layout_top).setVisibility(View.VISIBLE);
                            } else {
                                //  intgral_main_title_middle_img.setVisibility(View.VISIBLE);
                                intgral_main_title_middle_layout.setVisibility(View.VISIBLE);       //可领取
                                findViewById(R.id.integer_layout_top).setVisibility(View.INVISIBLE);

                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject();
                                            obj.put("uid", uid);
                                            obj.put("act", URLConfig.getIntegration);
                                            String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

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
                            bigtv_nowintegration.setText(integration_front);
                            tv_nowintegration.setText("当前可用积分：" + integration_front);
                        } else {//失败
                            ishas = false;
                            // intgral_main_title_middle_img.setVisibility(View.VISIBLE);
                            intgral_main_title_middle_layout.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integral_main);
        context = this;
        findId();
        setText();
        //  initData();
        onclike();
    }

    /**
     * name:设置点击事件 author:Tom 2016-2-3上午11:21:03
     */
    public void onclike() {
        get_integral.setOnClickListener(this);
        btn_open_vip.setOnClickListener(this);
        exchange_integral.setOnClickListener(this);
        //  intgral_main_title_middle_img.setOnClickListener(this);
        integral_deduction.setOnClickListener(this);
        //跳转至获取免费积分
        get_free_integral.setOnClickListener(this);
        tv_integarule.setOnClickListener(this);
    }

    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {
        super.findId();
        get_integral = (LinearLayout) findViewById(R.id.get_integral);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        tv_addhao = (TextView) findViewById(R.id.tv_addhao);
        //  intgral_main_title_middle_img = (ImageView) findViewById(R.id.intgral_main_title_middle_img);
        intgral_main_title_middle_layout = (LinearLayout) findViewById(R.id.intgral_main_title_middle_layout);
        integral_deduction = (LinearLayout) findViewById(R.id.integral_deduction);
        get_free_integral = (Button) findViewById(R.id.get_free_integral);
        tv_nowintegration = (TextView) findViewById(R.id.tv_nowintegration);
        bigtv_nowintegration = (RiseNumberTextView) findViewById(R.id.bigtv_nowintegration);
        getintegral = (TextView) findViewById(R.id.getintegral);
        btn_open_vip = (Button) findViewById(R.id.btn_open_vip);
        exchange_integral = (Button) findViewById(R.id.exchange_integral);
        tv_integarule = (TextView) findViewById(R.id.tv_integarule);
    }

    public void setText() {
        activity_title_name.setText(R.string.integral_title_name);
        String Str_Integral = SharedPreferencesTools.getIntegral(context);
        bigtv_nowintegration.setText(Str_Integral);
        tv_nowintegration.setText("当前可用积分：" + Str_Integral);
    }

    public void initData() {
        Str_username = getIntent().getStringExtra("Str_username");
        Str_icon = SharedPreferencesTools.getStricon(context);
        Str_vipflg = SharedPreferencesTools.getVipFlag(context);
        //读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        uid = SharedPreferencesTools.getUid(context);
        if (uid == null || "".equals(uid)) {
            return;
        }
        chcheckIntegration();//检查获取积分权限
        /**加载缩小与放大动画**/
        // mLitteAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
        mLitteAnimation = new ScaleAnimation(0.2f, 1.2f, 0.2f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mLitteAnimation.setDuration(500);
        mLitteAnimation.setFillAfter(false);
        mBigAnimation = new ScaleAnimation(1.2f, 0.2f, 1.2f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mBigAnimation.setDuration(500);
        mBigAnimation.setFillAfter(false);
        mBigAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bigtv_nowintegration.startAnimation(mLitteAnimation);
                //一定要这样做，不然动画在播放完毕的最后一帧会闪烁闪烁
                //初步猜测可能onAnimationEnd函数里面动画还没有播放完毕
                   /* handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bigtv_nowintegration.clearAnimation();
                            mLitteAnimation.reset();
                            bigtv_nowintegration.startAnimation(mLitteAnimation);
                        }
                    }, 10);*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mLitteAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isfirst) {
                    bigtv_nowintegration.startAnimation(mBigAnimation);
                }
                isfirst = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // bigtv_nowintegration.startAnimation(mLitteAnimation);
    }

    public void chcheckIntegration() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("act", URLConfig.checkIntegration);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

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

    @Override
    public void onClick(View v) {
        Intent intent = null;
        final String uid = SharedPreferencesTools.getUid(context);
        switch (v.getId()) {
            case R.id.get_integral:
                //积分获取记录
                intent = new Intent(Integral.this, Get_Integral.class);
                intent.putExtra("integration", integration_front);
                break;
            case R.id.btn_open_vip:
                //跳转至开通会员
                intent = new Intent(Integral.this, MyOpenMenberActivity.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                intent.putExtra("Str_username", Str_username);
                break;
            case R.id.exchange_integral:
                //跳转至兑换积分
                intent = new Intent(Integral.this, Exchange_integral.class);
                break;
            /*case R.id.intgral_main_title_middle_img:
                if (ishas) {
                    //arg0.setVisibility(View.GONE);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("uid", uid);
                                obj.put("act", URLConfig.getIntegration);

                                String result = HttpClientUtils.sendPost(context,
                                        URLConfig.CCMTVAPP, obj.toString());
                                System.out.println("获取积分:" + obj + "|" + result);
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
                } else {
                    Toast.makeText(getApplicationContext(), "对不起，领取失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
                break;*/
            case R.id.integral_deduction:
                intent = new Intent(Integral.this, Integral_deduction.class);
                intent.putExtra("integration", integration_front);
                break;
            case R.id.get_free_integral:
                intent = new Intent(Integral.this, Get_Free_Integral2.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                intent.putExtra("Str_username", Str_username);
                break;
            case R.id.tv_integarule:                         //积分规则
                intent = new Intent(Integral.this, Integral_RuleActivity.class);
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
        initData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }
}