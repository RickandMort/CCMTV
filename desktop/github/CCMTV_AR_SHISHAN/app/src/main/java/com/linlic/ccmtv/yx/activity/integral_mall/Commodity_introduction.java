package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.MyIntegralTaskActivity;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */

public class Commodity_introduction extends BaseActivity implements View.OnClickListener {

    private Context context;
    private String id, num;
    private int integral_int;
    private Banner banner;
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private TextView help_text, title_name, integral, number_of_purchases, activity_title_name, purchase_quantity1, purchase_quantity, purchase_quantity2, immediate_exchange;
    private WebView commodity_abstract;
    private LinearLayout help_layout, help;//帮助
    private LinearLayout vip_layout;//vip
    private LinearLayout my_integralTask;//获取积分
    private int money;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            num = dataJson.getString("num");
                            title_name.setText(dataJson.getString("name"));
                            activity_title_name.setText(dataJson.getString("name"));
                            integral.setText(dataJson.getString("money"));
                            money = dataJson.getInt("money");
                            number_of_purchases.setText(dataJson.getString("duihuan"));
                            for (int i = 0; i < dataJson.getJSONArray("api_detail_imgs").length(); i++) {
                                images.add(dataJson.getJSONArray("api_detail_imgs").get(i).toString());
                            }
                            initbanner();
                            commodity_abstract.loadData(dataJson.getString("pro"), "text/html; charset=UTF-8", null);
                        } else {
                            Toast.makeText(Commodity_introduction.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.commodity_introduction);
        context = this;
        findId();
        init();
        setValue();
    }

    @Override
    public void findId() {
        super.findId();
        title_name = (TextView) findViewById(R.id.title_name);
        integral = (TextView) findViewById(R.id.integral);
        number_of_purchases = (TextView) findViewById(R.id.number_of_purchases);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        purchase_quantity1 = (TextView) findViewById(R.id.purchase_quantity1);
        purchase_quantity = (TextView) findViewById(R.id.purchase_quantity);
        purchase_quantity2 = (TextView) findViewById(R.id.purchase_quantity2);
        immediate_exchange = (TextView) findViewById(R.id.immediate_exchange);
        help_text = (TextView) findViewById(R.id.help_text);
        help_layout = (LinearLayout) findViewById(R.id.help_layout);
        vip_layout = (LinearLayout) findViewById(R.id.vip_layout);
        my_integralTask = (LinearLayout) findViewById(R.id.my_integralTask);
        help = (LinearLayout) findViewById(R.id.help);
        banner = (Banner) findViewById(R.id.banner);
        commodity_abstract = (WebView) findViewById(R.id.commodity_abstract);
    }

    public void init() {
        id = getIntent().getStringExtra("id");
        integral_int = Integer.parseInt(SharedPreferencesTools.getIntegral(Commodity_introduction.this));
        purchase_quantity1.setOnClickListener(this);
        purchase_quantity2.setOnClickListener(this);
        immediate_exchange.setOnClickListener(this);
        help.setOnClickListener(this);
        help_text.setOnClickListener(this);
        vip_layout.setOnClickListener(this);
        my_integralTask.setOnClickListener(this);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void initbanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        if(num.equals("0")){
            immediate_exchange.setText("已兑完");
            immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon19));
        }else {
            if (money <= integral_int) {
                immediate_exchange.setText("立即兑换");
                immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon16));
            } else {
                immediate_exchange.setText("积分不足");
                immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon19));
            }
        }


    }

    public void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.proDetail);
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_Commodity, obj.toString());
                    MyProgressBarDialogTools.hide();

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
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.purchase_quantity1:
                if (Integer.parseInt(purchase_quantity.getText().toString()) > 1) {
                    purchase_quantity.setText((Integer.parseInt(purchase_quantity.getText().toString()) - 1) + "");
                    if ((Integer.parseInt(purchase_quantity.getText().toString()) * money) <= integral_int) {
                        immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon16));
                        immediate_exchange.setText("立即兑换");
                    } else {
                        immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon19));
                        immediate_exchange.setText("积分不足");
                    }
                }
                break;
            case R.id.purchase_quantity2:
                if (Integer.parseInt(purchase_quantity.getText().toString()) < 999) {
                    if ((Integer.parseInt(purchase_quantity.getText().toString()) * money) <= integral_int) {
                        purchase_quantity.setText((Integer.parseInt(purchase_quantity.getText().toString()) + 1) + "");
                        immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon16));
                        immediate_exchange.setText("立即兑换");
                    } else {
                        immediate_exchange.setBackground(getResources().getDrawable(R.mipmap.integral_mall_icon19));
                        immediate_exchange.setText("积分不足");
                    }
                } else {
                    Toast.makeText(Commodity_introduction.this, "一次最多兑换999件礼品", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.immediate_exchange:
                if (num.equals("0")) {
                    Toast.makeText(context,"商品数量不足",Toast.LENGTH_SHORT).show();
                } else {
                    if ((Integer.parseInt(purchase_quantity.getText().toString()) * money) <= integral_int) {
                        intent = new Intent(context, Exchange_gifts.class);
                        intent.putExtra("id", id);
                        intent.putExtra("purchase_quantity", purchase_quantity.getText().toString().trim());
                        intent.putExtra("title_name", title_name.getText().toString().trim());
                        intent.putExtra("integral", money);
                        intent.putExtra("img", images.get(0));
                        startActivity(intent);
                    }
                }
                break;
            case R.id.help://帮助
                help_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.help_text://帮助-知道了
                help_layout.setVisibility(View.GONE);
                break;
            case R.id.vip_layout:
                intent = new Intent(Commodity_introduction.this, MyOpenMenberActivity.class);
                intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Commodity_introduction.this));
                intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Commodity_introduction.this));
                startActivity(intent);
                break;
            case R.id.my_integralTask:
                intent = new Intent(Commodity_introduction.this, MyIntegralTaskActivity.class);
                try {
                    JSONObject result = new JSONObject(SharedPreferencesTools.getPerfectInformation(Commodity_introduction.this));
                    JSONObject object = result.getJSONObject("data");
                    JSONObject obj = object.getJSONObject("hosName");
                    intent.putExtra("Str_hyleibie", object.getString("hyleibie"));
                    intent.putExtra("Str_cityname", obj.getString("name"));
                    intent.putExtra("Str_bigkeshi", object.getString("keshilb"));
                    intent.putExtra("Str_smallkeshi", object.getString("keshi"));
                    intent.putExtra("Str_zhicheng", object.getString("my_694"));
                    intent.putExtra("Str_truename", object.getString("truename"));
                    intent.putExtra("Str_sexName", object.getString("sexName"));
                    intent.putExtra("Str_sex", object.getString("sex"));
                    intent.putExtra("Str_cityid", obj.getString("id"));
                    intent.putExtra("Str_address", object.getString("address"));
                    intent.putExtra("Str_idcard", object.getString("idcard"));
                    intent.putExtra("Str_danwei", object.getString("danwei"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
