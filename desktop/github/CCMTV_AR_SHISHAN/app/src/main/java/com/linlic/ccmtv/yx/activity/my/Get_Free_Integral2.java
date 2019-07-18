package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name:积分充值
 * author:tom
 * 2016-03-03 15:45:45
 */
public class Get_Free_Integral2 extends BaseActivity implements View.OnClickListener {
    private String Str_icon, Str_username, viptitle, intenumber;
    private int Str_vipflg;
    private String vipflg_Str;
    Context context;
    private double money = 0.01;
    private LinearLayout layout_buyinte_10, layout_buyinte_20, layout_buyinte_50, layout_buyinte_100;
    private TextView tv_inteprice_one, tv_inteprice_one_vipflg_str, usermoneytext, tv_inteprice_two, tv_inteprice_two_vipflg_str, tv_inteprice_there, tv_inteprice_there_vipflg_str, tv_inteprice_four_vipflg_str, tv_inteprice_four;
    private TextView tv_intenumber_one, tv_intenumber_two, tv_intenumber_there, tv_intenumber_four;

    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Button btn_buyinte;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        MyProgressBarDialogTools.hide();
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
//                            hideNoData();

                            JSONArray dataArray = result.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject object = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                if (object.getString("type").equals("jfpay")) {
                                    map.put("integrationvip", object.getString("integrationvip"));
                                    map.put("money", object.getString("money"));
                                    map.put("vipflg_str", object.getString("vipflg_str"));
                                    data.add(map);
                                }
                            }
                            tv_inteprice_one.setText("售价：" + data.get(0).get("money").toString() + "元");
                            tv_inteprice_two.setText("售价：" + data.get(1).get("money").toString() + "元");
                            tv_inteprice_there.setText("售价：" + data.get(2).get("money").toString() + "元");
                            tv_inteprice_four.setText("售价：" + data.get(3).get("money").toString() + "元");
                            tv_inteprice_one_vipflg_str.setText(data.get(0).get("vipflg_str").toString());
                            tv_inteprice_two_vipflg_str.setText(data.get(1).get("vipflg_str").toString());
                            tv_inteprice_there_vipflg_str.setText(data.get(2).get("vipflg_str").toString());
                            tv_inteprice_four_vipflg_str.setText(data.get(3).get("vipflg_str").toString());
                            tv_intenumber_one.setText(data.get(0).get("integrationvip").toString() + "积分");
                            tv_intenumber_two.setText(data.get(1).get("integrationvip").toString() + "积分");
                            tv_intenumber_there.setText(data.get(2).get("integrationvip").toString() + "积分");
                            tv_intenumber_four.setText(data.get(3).get("integrationvip").toString() + "积分");
                            money = Double.valueOf(data.get(0).get("money").toString());
                            viptitle = "购买" + data.get(0).get("money") + "元积分";
                            intenumber = data.get(0).get("integrationvip").toString();
                            vipflg_Str = data.get(0).get("vipflg_str").toString();
                        } else {//失败
//                            showNoData();
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    showNoData();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_free_integral2);
        context = this;
        findId();
        initData();
        setText();
        setOnClick();
        Str_icon = getIntent().getStringExtra("Str_icon");
        Str_username = getIntent().getStringExtra("Str_username");
        Str_vipflg = getIntent().getIntExtra("Str_vipflg", 0);
    }

    @Override
    public void showNoData() {

    }

    private void setOnClick() {
        layout_buyinte_10.setOnClickListener(this);
        layout_buyinte_20.setOnClickListener(this);
        layout_buyinte_50.setOnClickListener(this);
        layout_buyinte_100.setOnClickListener(this);
        btn_buyinte.setOnClickListener(this);
    }

    private void initData() {
        layout_buyinte_10.setSelected(true);
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("act", URLConfig.costDetails);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    public void setText() {
        super.setActivity_title_name(R.string.integral_main_Button_text1);
        String Str_Integral = SharedPreferencesTools.getIntegral(context);
        usermoneytext.setText(Str_Integral);
    }

    public void findId() {
        super.findId();
        layout_buyinte_10 = (LinearLayout) findViewById(R.id.layout_buyinte_10);
        layout_buyinte_20 = (LinearLayout) findViewById(R.id.layout_buyinte_20);
        layout_buyinte_50 = (LinearLayout) findViewById(R.id.layout_buyinte_50);
        layout_buyinte_100 = (LinearLayout) findViewById(R.id.layout_buyinte_100);
        tv_inteprice_one = (TextView) findViewById(R.id.tv_inteprice_one);
        tv_inteprice_one_vipflg_str = (TextView) findViewById(R.id.tv_inteprice_one_vipflg_str);
        tv_inteprice_two = (TextView) findViewById(R.id.tv_inteprice_two);
        tv_inteprice_two_vipflg_str = (TextView) findViewById(R.id.tv_inteprice_two_vipflg_str);
        tv_inteprice_there = (TextView) findViewById(R.id.tv_inteprice_there);
        tv_inteprice_there_vipflg_str = (TextView) findViewById(R.id.tv_inteprice_there_vipflg_str);
        tv_inteprice_four = (TextView) findViewById(R.id.tv_inteprice_four);
        tv_inteprice_four_vipflg_str = (TextView) findViewById(R.id.tv_inteprice_four_vipflg_str);
        tv_intenumber_one = (TextView) findViewById(R.id.tv_intenumber_one);
        tv_intenumber_two = (TextView) findViewById(R.id.tv_intenumber_two);
        tv_intenumber_there = (TextView) findViewById(R.id.tv_intenumber_there);
        tv_intenumber_four = (TextView) findViewById(R.id.tv_intenumber_four);
        btn_buyinte = (Button) findViewById(R.id.btn_buyinte);
        usermoneytext = (TextView) findViewById(R.id.usermoneytext);
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

    /**
     * name：点击立即上传 天转到上传页面
     * author：Larry
     * data：2016/4/6 16:29
     */
    public void now_toupload(View view) {            //立即上传
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("type", "upload");
        startActivity(intent);
    }

    public void open_vip(View view) {                //开通会员
        Intent intent = new Intent(context, MyOpenMenberActivity.class);
        intent.putExtra("Str_vipflg", Str_vipflg);
        intent.putExtra("Str_icon", Str_icon);
        intent.putExtra("Str_username", Str_username);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_buyinte_10:
                layout_buyinte_10.setSelected(true);
                layout_buyinte_20.setSelected(false);
                layout_buyinte_50.setSelected(false);
                layout_buyinte_100.setSelected(false);

                tv_inteprice_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_inteprice_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_intenumber_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_intenumber_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));

                vipflg_Str = data.get(0).get("vipflg_str").toString();
                money = Double.valueOf(data.get(0).get("money").toString());
                viptitle = "购买" + data.get(0).get("money") + "元积分";
                intenumber = data.get(0).get("integrationvip").toString();
                break;
            case R.id.layout_buyinte_20:
                layout_buyinte_10.setSelected(false);
                layout_buyinte_20.setSelected(true);
                layout_buyinte_50.setSelected(false);
                layout_buyinte_100.setSelected(false);

                tv_inteprice_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_inteprice_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_intenumber_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_intenumber_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));

                vipflg_Str = data.get(1).get("vipflg_str").toString();
                money = Double.valueOf(data.get(1).get("money").toString());
                viptitle = "购买" + data.get(1).get("money") + "元积分";
                intenumber = data.get(1).get("integrationvip").toString();
                break;
            case R.id.layout_buyinte_50:
                layout_buyinte_10.setSelected(false);
                layout_buyinte_20.setSelected(false);
                layout_buyinte_50.setSelected(true);
                layout_buyinte_100.setSelected(false);

                tv_inteprice_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_inteprice_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_intenumber_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_intenumber_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));

                vipflg_Str = data.get(2).get("vipflg_str").toString();
                money = Double.valueOf(data.get(2).get("money").toString());
                viptitle = "购买" + data.get(2).get("money") + "元积分";
                intenumber = data.get(2).get("integrationvip").toString();
                break;
            case R.id.layout_buyinte_100:
                layout_buyinte_10.setSelected(false);
                layout_buyinte_20.setSelected(false);
                layout_buyinte_50.setSelected(false);
                layout_buyinte_100.setSelected(true);

                tv_inteprice_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_inteprice_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_inteprice_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select1));
                tv_intenumber_four.setTextColor(getResources().getColor(R.color.get_free_integral_text_select));
                tv_intenumber_one.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_two.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));
                tv_intenumber_there.setTextColor(getResources().getColor(R.color.get_free_integral_text_no_select2));

                vipflg_Str = data.get(3).get("vipflg_str").toString();
                money = Double.valueOf(data.get(3).get("money").toString());
                viptitle = "购买" + data.get(3).get("money") + "元积分";
                intenumber = data.get(3).get("integrationvip").toString();
                break;
            case R.id.btn_buyinte:
                Intent intent = new Intent(Get_Free_Integral2.this, OpenMenberActivity.class);//跳转至收银柜台
                intent.putExtra("viptitle", viptitle);
                intent.putExtra("vipflg_Str", vipflg_Str);
                intent.putExtra("payfor", "inte");//积分支付
                intent.putExtra("vip_time", intenumber);//积分数量
                intent.putExtra("money", money);//积分价格
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
