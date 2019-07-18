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
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.RiseNumberTextView;

import org.json.JSONObject;

/**
 * Created by tom on 2016/10/27.
 * 收银台
 */
public class CashierActivity extends BaseActivity  implements View.OnClickListener{
    Context context;
    private LinearLayout cashier_revenue_log,cashier_present_record,cashier_records_of_consumptionlayout,cashier_present_application,cashier_present_application2,cashier_recharge;

    private String Str_personalmoney;
    private RiseNumberTextView riseNumberTextView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject data  = result
                                    .getJSONObject("data");
                            Str_personalmoney = data.getString("personalmoney");
                            riseNumberTextView.setText(Str_personalmoney+"元");
                        } else {//失败
                            Toast.makeText(CashierActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                    break;
                case 500:
                    Toast.makeText(CashierActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashier);
        context = this;
        findId();
        super.setActivity_title_name("我的收银台");
        setmsgdb();
        setValues();
        setAllonClick();

    }

    public void setValues(){

//        Str_personalmoney =  getIntent().getStringExtra("Str_personalmoney");

    }

    @Override
    public void findId() {
        super.findId();
        cashier_revenue_log = (LinearLayout) findViewById(R.id.cashier_revenue_log);
        cashier_present_record = (LinearLayout) findViewById(R.id.cashier_present_record);
        cashier_present_application = (LinearLayout) findViewById(R.id.cashier_present_application);
        cashier_present_application2 = (LinearLayout) findViewById(R.id.cashier_present_application2);
        cashier_recharge = (LinearLayout) findViewById(R.id.cashier_recharge);
        cashier_records_of_consumptionlayout = (LinearLayout) findViewById(R.id.cashier_records_of_consumptionlayout);
        riseNumberTextView = (RiseNumberTextView) findViewById(R.id.bigtv_nowintegration);

    }

    private void setAllonClick() {
        cashier_revenue_log.setOnClickListener(this);
        cashier_records_of_consumptionlayout.setOnClickListener(this);
        cashier_present_record.setOnClickListener(this);
        cashier_recharge.setOnClickListener(this);
        cashier_present_application2.setOnClickListener(this);
        cashier_present_application.setOnClickListener(this);

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(CashierActivity.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        //判断是否登录
        if (SharedPreferencesTools.getUids(CashierActivity.this) == null) {
            intent = new Intent(CashierActivity.this, LoginActivity.class);
            intent.putExtra("source", "my");
            startActivity(intent);
            return;
        }
        switch (v.getId()) {

            case R.id.cashier_revenue_log://收入记录
                 intent = new Intent(CashierActivity.this, Cashier_revenue_log.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            case R.id.cashier_recharge://充值
                 intent = new Intent(CashierActivity.this, Cashier_recharge.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            case R.id.cashier_records_of_consumptionlayout://消费记录
                 intent = new Intent(CashierActivity.this, Cashier_records_of_consumption.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            case R.id.cashier_present_application2://提现申请
                 intent = new Intent(CashierActivity.this, Cashier_present_application.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            case R.id.cashier_present_application://提现申请
                 intent = new Intent(CashierActivity.this, Cashier_present_application.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            case R.id.cashier_present_record://提现记录
                 intent = new Intent(CashierActivity.this, Cashier_present_record.class);
                 intent.putExtra("balance",Str_personalmoney);
                break;

            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
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
                    startActivity(new Intent(CashierActivity.this, LoginActivity.class));
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("act", URLConfig.getMyCashier);

                    String result = HttpClientUtils.sendPost(CashierActivity.this,
                            URLConfig.CCMTVAPP, obj.toString());
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
    protected void onResume() {
        setmsgdb();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
