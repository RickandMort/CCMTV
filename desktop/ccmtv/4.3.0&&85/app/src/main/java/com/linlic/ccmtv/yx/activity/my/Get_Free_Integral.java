package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name:获取免费积分
 * author:larry
 * 2016-03-03 15:45:45
 */
public class Get_Free_Integral extends BaseActivity implements View.OnClickListener {
    private String Str_icon, Str_username, viptitle, intenumber;
    private int Str_vipflg;
    private String vipflg_Str;
    Context context;
    private double money = 0.01;
    private RelativeLayout layout_buyinte_10, layout_buyinte_20, layout_buyinte_50, layout_buyinte_100;
    private TextView tv_inteprice_one, tv_inteprice_one_vipflg_str, tv_inteprice_two, tv_inteprice_two_vipflg_str, tv_inteprice_there, tv_inteprice_there_vipflg_str, tv_inteprice_four_vipflg_str, tv_inteprice_four;
    private TextView tv_intenumber_one, tv_intenumber_two, tv_intenumber_there, tv_intenumber_four;
    private TextView tv_upvideo, tv_upcase, tv_upwenxian, tv_upppt, tv_upzixun;
    private TextView tv_regist, tv_wanshan, tv_pinglun;
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
                            hideNoData();
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
                            tv_inteprice_one.setText(data.get(0).get("money").toString() + "元");
                            tv_inteprice_two.setText(data.get(1).get("money").toString() + "元");
                            tv_inteprice_there.setText(data.get(2).get("money").toString() + "元");
                            tv_inteprice_four.setText(data.get(3).get("money").toString() + "元");
                            tv_inteprice_one_vipflg_str.setText(data.get(0).get("vipflg_str").toString());
                            tv_inteprice_two_vipflg_str.setText(data.get(1).get("vipflg_str").toString());
                            tv_inteprice_there_vipflg_str.setText(data.get(2).get("vipflg_str").toString());
                            tv_inteprice_four_vipflg_str.setText(data.get(3).get("vipflg_str").toString());
                            tv_intenumber_one.setText(data.get(0).get("integrationvip").toString() + "分");
                            tv_intenumber_two.setText(data.get(1).get("integrationvip").toString() + "分");
                            tv_intenumber_there.setText(data.get(2).get("integrationvip").toString() + "分");
                            tv_intenumber_four.setText(data.get(3).get("integrationvip").toString() + "分");
                            money = Double.valueOf(data.get(0).get("money").toString());
                            viptitle = "购买" + data.get(0).get("money") + "元积分";
                            intenumber = data.get(0).get("integrationvip").toString();
                            vipflg_Str = data.get(0).get("vipflg_str").toString();
                        } else {//失败
                            showNoData();
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            hideNoData();
                            JSONObject data = result.getJSONObject("data");
                            tv_regist.setText(data.get("j_register").toString());
                            tv_wanshan.setText(data.get("j_upload").toString());
                            tv_upppt.setText(data.get("j_uploadPpt").toString() + "分");
                            if (!TextUtils.isEmpty(data.get("j_comment_max").toString())) {
                                tv_pinglun.setText(data.get("j_comment_max").toString());
                            } else {
                                tv_pinglun.setText("10");
                            }
                            tv_upvideo.setText(data.get("j_uploadVideo").toString() + "分");
                            tv_upcase.setText(data.get("j_uploadCase").toString() + "分");
                            tv_upwenxian.setText(data.get("j_uploadPpt").toString() + "分");
                            tv_upzixun.setText(data.get("j_uploadInformation").toString() + "分");
                        } else {//失败
                            showNoData();
                        }
                    } catch (Exception e) {
                        showNoData();
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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_free_integral);
        context = this;
        findId();
        initData();
        setText();
        setOnClick();
        Str_icon = getIntent().getStringExtra("Str_icon");
        Str_username = getIntent().getStringExtra("Str_username");
        Str_vipflg = getIntent().getIntExtra("Str_vipflg", 0);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("act", URLConfig.integralRule);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());

                    Message message = new Message();
                    message.what = 2;
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
    }

    public void findId() {
        super.findId();
        layout_buyinte_10 = (RelativeLayout) findViewById(R.id.layout_buyinte_10);
        layout_buyinte_20 = (RelativeLayout) findViewById(R.id.layout_buyinte_20);
        layout_buyinte_50 = (RelativeLayout) findViewById(R.id.layout_buyinte_50);
        layout_buyinte_100 = (RelativeLayout) findViewById(R.id.layout_buyinte_100);
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
        tv_regist = (TextView) findViewById(R.id.tv_regist);
        tv_wanshan = (TextView) findViewById(R.id.tv_wanshan);
        tv_pinglun = (TextView) findViewById(R.id.tv_pinglun);
        tv_upvideo = (TextView) findViewById(R.id.tv_upvideo);
        tv_upcase = (TextView) findViewById(R.id.tv_upcase);
        tv_upwenxian = (TextView) findViewById(R.id.tv_upwenxian);
        tv_upppt = (TextView) findViewById(R.id.tv_upppt);
        tv_upzixun = (TextView) findViewById(R.id.tv_upzixun);
        btn_buyinte = (Button) findViewById(R.id.btn_buyinte);
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
                vipflg_Str = data.get(3).get("vipflg_str").toString();
                money = Double.valueOf(data.get(3).get("money").toString());
                viptitle = "购买" + data.get(3).get("money") + "元积分";
                intenumber = data.get(3).get("integrationvip").toString();
                break;
            case R.id.btn_buyinte:
                Intent intent = new Intent(Get_Free_Integral.this, OpenMenberActivity.class);//跳转至收银柜台
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
