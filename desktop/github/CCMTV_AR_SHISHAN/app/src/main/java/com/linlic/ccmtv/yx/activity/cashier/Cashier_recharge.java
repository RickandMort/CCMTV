package com.linlic.ccmtv.yx.activity.cashier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.linlic.ccmtv.yx.R.id.tv_num2;

/**
 * Created by tom on 2016/10/27.
 * 充值
 */
public class Cashier_recharge extends BaseActivity {
    Context context;
    @Bind(R.id.tv_num1)
    TextView tvNum1;
    @Bind(tv_num2)
    TextView tvNum2;
    @Bind(R.id.tv_num3)
    TextView tvNum3;
    private TextView cashier_recharge_integral;
    private TextView cashier_recharge_author;
    private CircleImageView cashier_recharge_img;
    private EditText amount_of_charge;
    private String integration, subject, money, vipflg_str;
    public static Cashier_recharge finish;
    private Button btn_recharge;

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
                            subject = data.getString("subject");
                            money = data.getString("money");
                            vipflg_str = data.getString("vipflg_str");

                            Intent intent = new Intent(Cashier_recharge.this, PaymentActivity.class);
                            intent.putExtra("subject", subject);
                            //测试变为0.01
//                            intent.putExtra("money", money);
                            intent.putExtra("money", money);
                            intent.putExtra("vipflg_str", vipflg_str);
                            intent.putExtra("payfor", "recharge");
                            startActivity(intent);
                            btn_recharge.setClickable(true);
                        } else {//失败
                            Toast.makeText(Cashier_recharge.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 500:
                    Toast.makeText(Cashier_recharge.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_recharge);
        ButterKnife.bind(this);
        context = this;
        finish = this;
        findId();
        super.setActivity_title_name("零钱充值");
        setText();
    }

    public void setText() {
        integration = getIntent().getStringExtra("balance");
        integration = TextUtils.isEmpty(integration) ? "0.0" : integration;
        cashier_recharge_integral.setText("账户余额:" + integration + "元");
        /*String UserName = SharedPreferencesTools.getUserName(Cashier_recharge.this);
        cashier_recharge_author.setText(UserName);
        //显示头像
        String Str_icon = SharedPreferencesTools.getStricon(Cashier_recharge.this);
        loadImg(cashier_recharge_img, Str_icon);*/
    }

    @Override
    public void findId() {
        super.findId();
        cashier_recharge_integral = (TextView) findViewById(R.id.cashier_recharge_integral);
        //cashier_recharge_author = (TextView) findViewById(R.id.cashier_recharge_author);
        amount_of_charge = (EditText) findViewById(R.id.amount_of_charge);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        //cashier_recharge_img = (CircleImageView) findViewById(R.id.cashier_recharge_img);

        amount_of_charge.setKeyListener(DigitsKeyListener.getInstance("0123456789"));//EditText设置只能输入整数
    }

    public void cashier_recharge(View view) {
        //第一步 让按钮不可点击
        view.setClickable(false);
        int moeny = 0;
        if (amount_of_charge.getText() == null || amount_of_charge.getText().length() < 1) {
            Toast.makeText(Cashier_recharge.this, "金额不能为空", Toast.LENGTH_SHORT).show();
            view.setClickable(true);
            return;
        } else {
            moeny = Integer.parseInt(amount_of_charge.getText().toString());
            if (moeny >= 50) {
                if (moeny <= 200000) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                            String uid = sharedPreferences.getString("uid", "");
                            if (uid == null || "".equals(uid)) {
                                startActivity(new Intent(Cashier_recharge.this, LoginActivity.class));
                            }
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("uid", uid);
                                obj.put("act", URLConfig.cashierRecharge);
                                obj.put("money", Integer.parseInt(amount_of_charge.getText().toString()));
                                String result = HttpClientUtils.sendPost(Cashier_recharge.this, URLConfig.CCMTVAPP, obj.toString());

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

                } else {
                    Toast.makeText(Cashier_recharge.this, "最高充值金额为200,000元", Toast.LENGTH_SHORT).show();
                    view.setClickable(true);
                    return;
                }
            } else {
                Toast.makeText(Cashier_recharge.this, "请输入大于50的整数，最低50元", Toast.LENGTH_SHORT).show();
                view.setClickable(true);
                return;
            }
        }
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Cashier_recharge.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
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

    @OnClick({R.id.tv_num1, tv_num2, R.id.tv_num3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_num1:
                String num1=tvNum1.getText().toString().trim();
                amount_of_charge.setText(num1.substring(0, num1.indexOf("元")));
                break;
            case tv_num2:
                String num2=tvNum2.getText().toString().trim();
                amount_of_charge.setText(num2.substring(0, num2.indexOf("元")));
                break;
            case R.id.tv_num3:
                String num3=tvNum3.getText().toString().trim();
                amount_of_charge.setText(num3.substring(0, num3.indexOf("元")));
                break;
        }
    }
}
