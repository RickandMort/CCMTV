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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.util.Utils;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONObject;

/**
 * Created by tom on 2016/10/27.
 * 提现申请
 */
public class Cashier_present_application extends BaseActivity {
    Context context;
    private EditText cashier_present_application_name;
    private EditText cashier_present_application_bank;
    private EditText cashier_present_application_bank_account;
    private EditText cashier_present_application_money;
    private EditText cashier_present_application_phone;
    private EditText cashier_present_application_bank_of_deposit;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            Toast.makeText(Cashier_present_application.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {//失败
                            Toast.makeText(Cashier_present_application.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 500:
                    Toast.makeText(Cashier_present_application.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private String username;
    private String bankName;
    private String bankCardCode;
    private String forwordMoney;
    private String phoneNumber;
    private String bankOfDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier_present_application);
        context = this;

        findId();
        super.setActivity_title_name("提现申请");
        setAllonClick();
    }

    @Override
    public void findId() {
        super.findId();
//        cashier_revenue_log = (LinearLayout) findViewById(R.id.cashier_revenue_log);
        cashier_present_application_name = (EditText) findViewById(R.id.cashier_present_application_name);
        cashier_present_application_bank = (EditText) findViewById(R.id.cashier_present_application_bank);
        cashier_present_application_bank_account = (EditText) findViewById(R.id.cashier_present_application_bank_account);
        cashier_present_application_money = (EditText) findViewById(R.id.cashier_present_application_money);
        cashier_present_application_phone = (EditText) findViewById(R.id.cashier_present_application_phone);
        cashier_present_application_bank_of_deposit = (EditText) findViewById(R.id.cashier_present_application_bank_of_deposit);
    }

    private void setAllonClick() {
//        cashier_revenue_log.setOnClickListener(this);
    }

    public void MyprofileSubmit(View view) {

        username = cashier_present_application_name.getText().toString().trim();//姓名
        bankName = cashier_present_application_bank.getText().toString().trim();//银行名字
        bankCardCode = cashier_present_application_bank_account.getText().toString().trim();//银行卡号
        cashier_present_application_bank_account.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        forwordMoney = cashier_present_application_money.getText().toString().trim();//提现金额
        cashier_present_application_money.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        phoneNumber = cashier_present_application_phone.getText().toString().trim();//手机号码
        cashier_present_application_phone.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        bankOfDeposit = cashier_present_application_bank_of_deposit.getText().toString().trim();//开户行

        String checkParams = checkParams();
        if (checkParams == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                    String uid = sharedPreferences.getString("uid", "");
                    if (uid == null || "".equals(uid)) {
                        startActivity(new Intent(Cashier_present_application.this, LoginActivity.class));
                    }
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("uid", uid);
                        obj.put("act", URLConfig.cashierWithdraw);
                        obj.put("username", username);
                        obj.put("bankname", bankName);
                        obj.put("cardnum", bankCardCode);
                        obj.put("drawmoney", forwordMoney);
                        obj.put("phone", phoneNumber);
                        obj.put("openbank", bankOfDeposit);
                        String result = HttpClientUtils.sendPost(Cashier_present_application.this, URLConfig.CCMTVAPP, obj.toString());

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
            Toast.makeText(context, checkParams, Toast.LENGTH_SHORT).show();
        }
    }

    private String checkParams() {
        if (TextUtils.isEmpty(username)) return "姓名不能为空";
        if (TextUtils.isEmpty(bankName)) return "银行不能为空";
        if (TextUtils.isEmpty(bankCardCode)) return "卡号不能为空";
        if (bankCardCode.length() < 16 || bankCardCode.length() > 19) return "请输入正确的银行卡号";
        if (TextUtils.isEmpty(forwordMoney)) return "提现金额不能为空";
        if (Integer.parseInt(forwordMoney) < 100) return "提现金额不能小于100";
        if (TextUtils.isEmpty(phoneNumber)) return "联系电话不能为空";
        if (!Utils.isMobileNO(phoneNumber)) return "请输入正确的电话号码";
        if (TextUtils.isEmpty(bankOfDeposit)) return "开户行不能为空";
        return null;
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Cashier_present_application.this);
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

}