package com.linlic.ccmtv.yx.activity.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tom on 2016/10/27.
 * 充值
 */
public class Exchange_integral extends BaseActivity {
    Context context;
    private TextView exchange_integral_errmesage;
    private LinearLayout exchange_integral_errmesage_bg;
    private EditText redeem_code;
    public static Exchange_integral finish;
    private Button btn_recharge;
    private String errMsg = "兑换失败，请重新填写兑换码";
    private JSONObject result = new JSONObject();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        //   Log.e("兑换积分",result.toString());
                        if (result.getInt("status") == 1) {//成功

                            SharedPreferencesTools.saveIntegral(Exchange_integral.this, result.getString("money"));
                            btn_recharge.setClickable(true);
                            finish.finish();
                            Toast.makeText(Exchange_integral.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {//失败
                            errMsg = result.getString("errorMessage");
                            errMessage();
                            btn_recharge.setClickable(true);
//                            Toast.makeText(Exchange_integral.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        errMsg = "兑换失败，请稍候再试！";
                        errMessage();
                        btn_recharge.setClickable(true);
                    }
                    break;
                case 100:
                    //显示错误信息

                    break;
                case 500:
                    Toast.makeText(Exchange_integral.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    btn_recharge.setClickable(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_integral2);
        context = this;
        finish = this;
        findId();
        super.setActivity_title_name("积分兑换");
        setText();
    }

    public void setText() {

    }

    public void errMessage() {
        exchange_integral_errmesage.setText(errMsg);
        exchange_integral_errmesage_bg.setVisibility(View.VISIBLE);
        Timer timer = new Timer();
        TimerTask MyTask = new TimerTask() {
            @Override
            public void run() {
                Exchange_integral.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exchange_integral_errmesage_bg.setVisibility(View.GONE);
                    }
                });
            }
        };
        timer.schedule(MyTask, 2000);
    }

    @Override
    public void findId() {
        super.findId();
        exchange_integral_errmesage = (TextView) findViewById(R.id.exchange_integral_errmesage);
        exchange_integral_errmesage_bg = (LinearLayout) findViewById(R.id.exchange_integral_errmesage_bg);
        redeem_code = (EditText) findViewById(R.id.redeem_code);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        redeem_code.addTextChangedListener(watcher);
        btn_recharge.setClickable(false);
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 0) {
                btn_recharge.setBackground(getResources().getDrawable(R.mipmap.change11));
                btn_recharge.setClickable(true);
            } else {
                btn_recharge.setBackground(getResources().getDrawable(R.mipmap.change12));
                btn_recharge.setClickable(false);
            }
        }
    };


    public void cashier_recharge(View view) {
        //第一步 让按钮不可点击
        view.setClickable(false);

        if (redeem_code.getText() == null || redeem_code.getText().length() < 1) {
//            Toast.makeText(Exchange_integral.this, "金额不能为空", Toast.LENGTH_SHORT).show();
            errMsg = "兑换码不能为空";
            errMessage();
            view.setClickable(true);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                    String uid = sharedPreferences.getString("uid", "");
                    if (uid == null || "".equals(uid)) {
                        startActivity(new Intent(Exchange_integral.this, LoginActivity.class));
                    }
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("uid", uid);
                        obj.put("act", URLConfig.exchangePoints);
                        obj.put("code", redeem_code.getText());
                        String result = HttpClientUtils.sendPost(Exchange_integral.this, URLConfig.CCMTVAPP, obj.toString());

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
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(Exchange_integral.this);
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
