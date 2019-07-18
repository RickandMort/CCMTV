package com.linlic.ccmtv.yx.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.linlic.ccmtv.yx.MD5;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.alipay.SignUtils;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.my.feedback.Feedback_Main;
import com.linlic.ccmtv.yx.adapter.VipTypeAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.CircleImageView;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的开通会员
 *
 * @author yu
 */
public class MyOpenMenberActivity extends BaseActivity {
    TextView activity_title_name, tv_username, tv_showisvip;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    @Bind(R.id.layout_loading)
    LinearLayout layoutLoading;
    @Bind(R.id.vip_myhead)
    CircleImageView vipMyhead;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.iv_isvips)
    ImageView ivIsvips;
    @Bind(R.id.tv_showisvip)
    TextView tvShowisvip;
    @Bind(R.id.ll_one_month)
    LinearLayout llOneMonth;
    @Bind(R.id.ll_three_month)
    LinearLayout llThreeMonth;
    @Bind(R.id.ll_half_year)
    LinearLayout llHalfYear;
    @Bind(R.id.ll_one_year)
    LinearLayout llOneYear;
    @Bind(R.id.iv_zhi)
    ImageView ivZhi;
    @Bind(R.id.pay_zhi_check)
    CheckBox payZhiCheck;
    @Bind(R.id.iv_wei)
    ImageView ivWei;
    @Bind(R.id.pay_wei_check)
    CheckBox payWeiCheck;
    @Bind(R.id.show_viptype)
    MyGridView showViptype;
    @Bind(R.id.iv_video)
    ImageView ivVideo;
    @Bind(R.id.iv_loadvideo)
    ImageView ivLoadvideo;
    @Bind(R.id.iv_vip)
    ImageView ivVip;
    @Bind(R.id.guquanxiaoshou)
    RelativeLayout guquanxiaoshou;
    @Bind(R.id.ll_lianxi)
    LinearLayout llLianxi;
    @Bind(R.id.ll_wenti)
    LinearLayout llWenti;
    @Bind(R.id.tv_pay_now)
    TextView tvPayNow;
    @Bind(R.id.price1)
    TextView price1;
    @Bind(R.id.tv_price_day1)
    TextView tvPriceDay1;
    @Bind(R.id.price2)
    TextView price2;
    @Bind(R.id.tv_price_day2)
    TextView tvPriceDay2;
    @Bind(R.id.price3)
    TextView price3;
    @Bind(R.id.tv_price_day3)
    TextView tvPriceDay3;
    @Bind(R.id.price4)
    TextView price4;
    @Bind(R.id.tv_price_day4)
    TextView tvPriceDay4;
    //用户统计

    private CircleImageView vip_myhead;//头像
    int Str_vipflg;
    String Str_username;
    ImageView iv_isvips;
    MyGridView show_viptype;
    VipTypeAdapter adapter;
    private Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Dialog dialog;
    public String banktype = "alipay";
    private IWXAPI msgApi ;
    private double money = 60;
    private String uid, out_trade_no, orderInfo, trade_no, prepayId, nonceStr;
    private String DZSID = "";
    private String viptitle = "";
    private String vipflg_Str = "";
    //微信支付
    PayReq req;
    StringBuffer sb;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            hideNoData();
                            JSONArray dataArray = result.getJSONArray("data");
                            if ((result.getString("vipendtime").equals("") || result.getString("vipendtime") == null)) {
                                //iv_isvips.setVisibility(View.INVISIBLE);
                                tv_showisvip.setText("您还未开通会员");
                            } else {
                                //iv_isvips.setVisibility(View.VISIBLE);
                                tv_showisvip.setText("有效期：" + result.getString("vipendtime"));
                            }

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject object = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                if (object.getString("type").equals("vippay")) {
                                    map.put("integrationvip", object.getString("integrationvip"));
                                    map.put("money", object.getString("money"));
                                    map.put("moneyx", object.getString("moneyx"));
                                    map.put("vipflg_str", object.getString("vipflg_str"));

                                    data.add(map);
                                }
                            }
                            vipflg_Str = data.get(0).get("vipflg_str").toString();
                            adapter = new VipTypeAdapter(MyOpenMenberActivity.this, data, SharedPreferencesTools.getVipFlag(context) == 1);
                            showViptype.setAdapter(adapter);
                            adapter.setSelect(0);
                            showViptype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    adapter.setSelect(i);
                                    adapter.notifyDataSetChanged();
                                    tvPayNow.setText("立即支付"+data.get(i).get("money").toString()+"元");
                                    vipflg_Str = data.get(i).get("vipflg_str").toString();
                                    money=Double.valueOf(data.get(i).get("money").toString());
                                    viptitle ="VIP"+data.get(i).get("integrationvip").toString()+"个月";
                                }
                            });
                            tvPayNow.setText("立即支付"+data.get(0).get("money").toString()+"元");
                            money=Double.valueOf(data.get(0).get("money").toString());
                            viptitle ="VIP"+data.get(0).get("integrationvip").toString()+"个月";
                            adapter.notifyDataSetChanged();
                        } else {//失败
                            showNoData();
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject obj = new JSONObject(result.getString("resultdata"));
                            prepayId = obj.getString("prepayid");
                            trade_no = obj.getString("trade_no");
                            nonceStr = obj.getString("noncestr");
                            genPayReq();
                        } else {//失败
                            VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject object = new JSONObject(result.getString("data"));
                            out_trade_no = object.getString("out_trade_no");
                            orderInfo = getOrderInfo(viptitle, viptitle, String.valueOf(money));
//                            orderInfo = getOrderInfo(viptitle, viptitle, "0.01");
                            Toast.makeText(getApplicationContext(), "支付宝支付中...", Toast.LENGTH_SHORT).show();
                            /**
                             * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
                             */
                            String sign = sign(orderInfo);
                            try {
                                /**
                                 * 仅需对sign 做URL编码
                                 */
                                sign = URLEncoder.encode(sign, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            /**
                             * 完整的符合支付宝参数规范的订单信息
                             */
                            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(MyOpenMenberActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(payInfo, true);
                                    Message msg = new Message();
                                    msg.what = URLConfig.SDK_PAY_FLAG;
                                    msg.obj = result;
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        } else {//失败
                            VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
//             DisplayMetrics dm = this.getResources().getDisplayMetrics();
//             float screenWidth = dm.widthPixels;
//             float screenHeight = dm.heightPixels;
//             float sss = screenHeight / screenWidth;
//             if (sss > 1.80) {
//                 setContentView(R.layout.activity_openmenber_large);
//             } else {
        setContentView(R.layout.activity_openmenber);
        context = MyOpenMenberActivity.this;
        ButterKnife.bind(this);
        msgApi = WXAPIFactory.createWXAPI(this, URLConfig.APP_ID);
//             }
        findId();
        setText();
        initData();
    }

    public void findId() {
        super.findId();
        uid = SharedPreferencesTools.getUid(MyOpenMenberActivity.this);
        sb = new StringBuffer();
        req = new PayReq();
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_showisvip = (TextView) findViewById(R.id.tv_showisvip);
        vip_myhead = (CircleImageView) findViewById(R.id.vip_myhead);
        iv_isvips = (ImageView) findViewById(R.id.iv_isvips);
        //show_viptype = (MyGridView) findViewById(R.id.show_viptype);
        //默认选中一个月的vip
        //llOneMonth.setBackground(context.getResources().getDrawable(R.mipmap.open_vip_checked));
        payZhiCheck.setChecked(true);
        payZhiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_checked));
        payZhiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    payZhiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_checked));
                    payWeiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_unchecked));
                    payWeiCheck.setChecked(false);
                    banktype = "alipay";
                } else {
                    payZhiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_unchecked));
                    banktype = "";
                }
            }
        });
        payWeiCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    payWeiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_checked));
                    payZhiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_unchecked));
                    payZhiCheck.setChecked(false);
                    banktype = "wechat";
                } else {
                    payWeiCheck.setBackground(context.getResources().getDrawable(R.mipmap.pay_unchecked));
                    banktype = "";
                }
            }
        });


    }

    public void setText() {
        activity_title_name.setText("VIP会员");
    }

    public void initData() {
        MyProgressBarDialogTools.show(context);
        String Str_icon = SharedPreferencesTools.getStricon(context);
        Str_username = SharedPreferencesTools.getUserName(context);
        tv_username.setText("用户" + Str_username);
        //显示头像
        loadImg(vip_myhead, Str_icon);

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("act", URLConfig.costDetails);
                    object.put("uid", SharedPreferencesTools.getUids(context));
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
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        new Carousel_figure(context).loadImageNoCache(FirstLetter.getSpells(path), img);  //无缓存
    }

    @OnClick({R.id.ll_one_month, R.id.ll_three_month, R.id.ll_half_year, R.id.ll_one_year, R.id.ll_lianxi, R.id.ll_wenti, R.id.tv_pay_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_lianxi:
                Intent call_phone = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "400-9202-682");
                call_phone.setData(data);
                startActivity(call_phone);
                break;
            case R.id.ll_wenti:
                Intent intent = new Intent(context, Feedback_Main.class);
                startActivity(intent);
                break;
            case R.id.tv_pay_now:
                if (banktype.equals("")) {
                    toastShort("请选择支付方式");
                    return;
                }
                ShowDialog();
                break;
        }
    }

    public void ShowDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_vip_pay, null);
        ImageView iv_cancle = (ImageView) view1.findViewById(R.id.iv_cancle);
        TextView tv_username_vip = (TextView) view1.findViewById(R.id.tv_username_vip);
        TextView tv_taocan_name = (TextView)view1.findViewById(R.id.tv_taocan_name);
        TextView tv_price = (TextView) view1.findViewById(R.id.tv_price);
        TextView vip_name = (TextView) view1.findViewById(R.id.vip_name);
        Button bt_confirm = (Button) view1.findViewById(R.id.bt_confirm);
        tv_username_vip.setText(SharedPreferencesTools.getUserName(this));
        tv_taocan_name.setText(viptitle);
        tv_price.setText("￥"+money);
        if (banktype.equals("alipay")) {
            vip_name.setText("支付宝支付");
        } else if (banktype.equals("wechat")) {
            vip_name.setText("微信支付");
        }
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ("wechat".equals(banktype)) {
                    if (isFastDoubleClick()) {
                        return;
                    }
                    boolean isPaySupported = msgApi.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT;
                    if (isPaySupported) {
                        wexinpay();
                    } else {
                        Toast.makeText(MyOpenMenberActivity.this, "请安装或升级微信客户端！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //支付宝
                    if (isFastDoubleClick()) {
                        return;
                    }
                    alipaypay();
                }
                dialog.dismiss();
            }
        });
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view1);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //设置dialog的宽高属性
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void alipaypay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("money", money);
                    //金额 0.01 测试使用
                    obj.put("vipflg_str", vipflg_Str);
                    obj.put("type", "vippay");
                    obj.put("banktype", "alipay");
                    if (DZSID.length() > 0) {
                        obj.put("aid", DZSID);
                    }
                    obj.put("act", URLConfig.alipayInfo);
                    String result = HttpClientUtils.sendPost(MyOpenMenberActivity.this, URLConfig.CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * name：微信支付
     * author：Larry
     * data：2016/4/21 21:26
     */
    public void wexinpay() {
        msgApi.registerApp(URLConfig.APP_ID);
        Toast.makeText(MyOpenMenberActivity.this, "微信支付中...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("money", money);
                    obj.put("vipflg_str", vipflg_Str);
                    if (DZSID.length() > 0) {
                        obj.put("aid", DZSID);
                    }
                    obj.put("act", URLConfig.getInformation);
                    String result = HttpClientUtils.sendPost(MyOpenMenberActivity.this, URLConfig.CCMTVAppPayNew, obj.toString());
//                    Log.e("支付",result);
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + URLConfig.PARTNER + "\"";
        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + URLConfig.SELLER + "\"";
        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + URLConfig.alipayNotify_url + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";
        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, URLConfig.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private void genPayReq() {
        req.appId = URLConfig.APP_ID;
        req.partnerId = URLConfig.MCH_ID;
        req.prepayId = prepayId;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonceStr;
        // req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sb.append("sign\n" + req.sign + "\n\n");
        sendPayReq();
    }

    private void sendPayReq() {
        msgApi.registerApp(URLConfig.APP_ID);
        msgApi.sendReq(req);
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(URLConfig.API_KEY);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    //是否是多次点击
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
