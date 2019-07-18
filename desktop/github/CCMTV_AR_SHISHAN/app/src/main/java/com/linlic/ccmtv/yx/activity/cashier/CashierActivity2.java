package com.linlic.ccmtv.yx.activity.cashier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMall;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.my.Exchange_integral;
import com.linlic.ccmtv.yx.activity.my.Get_Free_Integral2;
import com.linlic.ccmtv.yx.activity.my.Get_Integral;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tom on 2016/10/27.
 * 收银台
 */
public class CashierActivity2 extends BaseActivity {
    Context context;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tv_qian)
    TextView tvQian;
    @Bind(R.id.tv_jifen)
    TextView tvJifen;
    @Bind(R.id.tv_money_num)
    TextView tvMoneyNum;
    @Bind(R.id.tv_leiji)
    TextView tvLeiji;
    @Bind(R.id.tv_tixian)
    TextView tvTixian;
    @Bind(R.id.tv_mingxi)
    TextView tvMingxi;
    @Bind(R.id.tv_chongzhi)
    TextView tvChongzhi;
    @Bind(R.id.rl_vip)
    RelativeLayout rlVip;
    @Bind(R.id.rl_jifen)
    RelativeLayout rlJifen;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.tv_jifen_chongzhi)
    TextView tvJifenChongzhi;
    @Bind(R.id.tv_type)
    TextView tvType;
    private String Str_personalmoney;
    int Str_vipflg;
    String Str_icon, Str_username,Str_countmoney,Str_countpersonalmoney;
    private int pause = 0;
    private String flag = "1";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject data = result.getJSONObject("data");
                            Str_personalmoney = data.getString("personalmoney");
                            tvMoneyNum.setText("￥" + Str_personalmoney);
                        } else {//失败
                            Toast.makeText(CashierActivity2.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 500:
                    Toast.makeText(CashierActivity2.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_main2);
        ButterKnife.bind(this);
        context = this;
        findId();
        super.setActivity_title_name("钱包");
        setmsgdb();
        setValues();
    }

    public void setValues() {
//        Str_personalmoney =  getIntent().getStringExtra("Str_personalmoney");
    }


    @Override
    public void findId() {
        super.findId();
//        cashier_present_application = (LinearLayout) findViewById(R.id.cashier_present_application);
//        layout_openmenber = (LinearLayout) findViewById(R.id.layout_openmenber);
//        get_free_integral = (LinearLayout) findViewById(R.id.get_free_integral);
//        exchange_integral = (LinearLayout) findViewById(R.id.exchange_integral);
//        get_integral = (LinearLayout) findViewById(R.id.get_integral_cashier);
//
//        cashier_recharge = (LinearLayout) findViewById(R.id.cashier_recharge);
//        cashier_records_of_consumptionlayout = (LinearLayout) findViewById(R.id.cashier_records_of_consumptionlayout);
//        riseNumberTextView = (RiseNumberTextView) findViewById(R.id.bigtv_nowintegration);
//        tv_nowintegration = (TextView) findViewById(R.id.tv_nowintegration);

        Str_username = getIntent().getStringExtra("Str_username");
        Str_countmoney = getIntent().getStringExtra("Str_countmoney");
        Str_countpersonalmoney = getIntent().getStringExtra("Str_countpersonalmoney");
        tvLeiji.setText("累计收入"+Str_countpersonalmoney+"元");
        Str_icon = SharedPreferencesTools.getStricon(CashierActivity2.this);
        Str_vipflg = SharedPreferencesTools.getVipFlag(CashierActivity2.this);
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(CashierActivity2.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }



    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        loadData();
    }

    /**
     * name:导入初始值 author:Tom 2016-1-28下午3:41:32
     */
    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                String uid = sharedPreferences.getString("uid", "");
                if (uid == null || "".equals(uid)) {
                    startActivity(new Intent(CashierActivity2.this, LoginActivity.class));
                }
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("act", URLConfig.getMyCashier);
                    String result = HttpClientUtils.sendPost(CashierActivity2.this, URLConfig.CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        if (pause > 0) {
            //setmsgdb();
            pause = 0;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        pause++;
        super.onPause();
    }

    @OnClick({R.id.tv_qian, R.id.tv_jifen, R.id.tv_tixian, R.id.tv_mingxi, R.id.tv_chongzhi, R.id.rl_vip, R.id.rl_jifen, R.id.tv_jifen_chongzhi})
    public void onViewClicked(View view) {

        Intent intent = null;

        switch (view.getId()) {
            case R.id.tv_qian:
                tvQian.setBackground(this.getResources().getDrawable(R.mipmap.qian_check));
                tvJifen.setBackground(this.getResources().getDrawable(R.mipmap.qian_nocheck));
                tvTixian.setVisibility(View.VISIBLE);
                tvJifenChongzhi.setVisibility(View.GONE);
                ivImg.setBackground(this.getResources().getDrawable(R.mipmap.qian_bao));
                tvMingxi.setText("零钱明细查询>");
                tvChongzhi.setText("零钱充值");
                tvType.setText("账户余额（元）");
                tvMoneyNum.setText("￥" + Str_personalmoney);
                tvLeiji.setText("累计收入"+Str_countpersonalmoney+"元");
                flag = "1";
                break;
            case R.id.tv_jifen:
                tvQian.setBackground(this.getResources().getDrawable(R.mipmap.qian_nocheck1));
                tvJifen.setBackground(this.getResources().getDrawable(R.mipmap.qian_check1));
                String Str_Integral = SharedPreferencesTools.getIntegral(CashierActivity2.this);
                tvTixian.setVisibility(View.GONE);
                tvJifenChongzhi.setVisibility(View.VISIBLE);
                ivImg.setBackground(this.getResources().getDrawable(R.mipmap.qian_jifen));
                tvMingxi.setText("积分明细查询>");
                tvChongzhi.setText("兑换码充值");
                tvType.setText("积分余额（分）");
                tvMoneyNum.setText("" + Str_Integral);
                tvLeiji.setText("累计收入"+Str_countmoney+"分");
                flag = "2";
                break;
            case R.id.tv_tixian://提现
                intent = new Intent(CashierActivity2.this, Cashier_present_application.class);
                intent.putExtra("balance", Str_personalmoney);
                break;
            case R.id.tv_mingxi:
                if (flag.equals("1")) {//零钱明细
                    intent = new Intent(CashierActivity2.this, Cashier_records_of_consumption.class);
                    intent.putExtra("balance", Str_personalmoney);
                } else if (flag.equals("2")) {//积分明细
                    intent = new Intent(CashierActivity2.this, Get_Integral.class);
                }
                break;
            case R.id.tv_chongzhi:
                if (flag.equals("1")) {//零钱充值
                    intent = new Intent(CashierActivity2.this, Cashier_recharge.class);
                    intent.putExtra("balance", Str_personalmoney);
                } else if (flag.equals("2")) {//兑换码充值
                    intent = new Intent(CashierActivity2.this, Exchange_integral.class);
                }
                break;
            case R.id.rl_vip://vip会员
                intent = new Intent(CashierActivity2.this, MyOpenMenberActivity.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                break;
            case R.id.rl_jifen://积分商城
                intent = new Intent(CashierActivity2.this, IntegralMall.class);
                break;
            case R.id.tv_jifen_chongzhi:
                intent = new Intent(CashierActivity2.this, Get_Free_Integral2.class);//积分充值
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                intent.putExtra("Str_username", Str_username);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }

    }
}
