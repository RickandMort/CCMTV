package com.linlic.ccmtv.yx.activity.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.linlic.ccmtv.yx.MD5;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.alipay.PayResult;
import com.linlic.ccmtv.yx.activity.alipay.SignUtils;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * 收银台
 *
 * @author yu
 */
public class PaymentActivity extends BaseActivity implements OnClickListener {
    Context context;
    //微信
    public static String nonceStr, trade_no;
    public static PaymentActivity finish;
    TextView activity_title_name, tv_username_vip, vip_name, tv_vip_money;
    Button btn_topay;
    //http://www.open-open.com/lib/view/open1451975281698.html
    //支付宝
    private String orderInfo, Str_username, out_trade_no;
    private String viptitle, Str_money, vipflg_str, prepayId, aid;
    ImageView iv_pay_zhifubao, iv_pay_weixin;
    //    private double money;
    private String uid;
    public static String payfor;
    /**
     * 支付方式
     */
    public String banktype = "wechat";
    private static final String TAG = "weiXinPayActivity";
    //微信支付
    PayReq req;
    private IWXAPI msgApi ;
    StringBuffer sb;
    private static long lastClickTime;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case URLConfig.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                       /* Toast.makeText(OpenMenberActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject();
                                    obj.put("uid", uid);
                                    obj.put("out_trade_no", out_trade_no);
                                    obj.put("act", URLConfig.completePayment);
                                    String result = HttpClientUtils.sendPost(PaymentActivity.this,
                                            URLConfig.CCMTVAPP, obj.toString());
                                    Message message = new Message();
                                    message.what = 3;
                                    message.obj = result;
                                    mHandler.sendMessage(message);

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PaymentActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(PaymentActivity.this, "支付取消",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PaymentActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }

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
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    btn_topay.setClickable(true);
                    break;
                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            if (payfor.equals("recharge")) {
                                Cashier_recharge.finish.finish();
                                VideoFive.toast(PaymentActivity.this, "充值成功！");
                            } else if (payfor.equals("VideoFive")) {
                                Intent intent = new Intent();
                                intent.putExtra("payResult", "ok");
                                setResult(RESULT_OK, intent);
                                VideoFive.toast(PaymentActivity.this, "支付宝支付成功！");
                            }
                            PaymentActivity.this.finish();

//                            Toast.makeText(getApplicationContext(), "支付宝支付成功！", Toast.LENGTH_SHORT).show();
                        } else {//失败
//                            Toast.makeText(getApplicationContext(), "支付宝支付失败！", Toast.LENGTH_SHORT).show();
                            VideoFive.toast(PaymentActivity.this, "支付宝支付失败！");
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
                            //测试修改为0.01
                            orderInfo = getOrderInfo(viptitle, viptitle, Str_money);
//                            orderInfo = getOrderInfo(viptitle, viptitle, "0.01");
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
                            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                                    + getSignType();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(PaymentActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(payInfo, true);
                                    Message msg = new Message();
                                    msg.what = URLConfig.SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            }).start();
                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    btn_topay.setClickable(true);
                    break;
                default:
                    break;
            }
        }


    };

 /*   public static void weixinPay(){
        Intent intent=new Intent();
        intent.putExtra("payResult","ok");
        setResult(RESULT_OK, intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        context = this;
        finish = this;
        findId();
        onclick();
        initData();
        setText();
    }

    public void findId() {
        // TODO Auto-generated method stub
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        btn_topay = (Button) findViewById(R.id.btn_topay);
        tv_username_vip = (TextView) findViewById(R.id.tv_username_vip);
        vip_name = (TextView) findViewById(R.id.vip_name);
        tv_vip_money = (TextView) findViewById(R.id.tv_vip_money);
        iv_pay_weixin = (ImageView) findViewById(R.id.iv_pay_weixin);
        iv_pay_zhifubao = (ImageView) findViewById(R.id.iv_pay_zhifubao);
    }

    public void setText() {
        // TODO Auto-generated method stub
        activity_title_name.setText("收银台");

        vip_name.setText(viptitle);

        tv_vip_money.setText(Str_money + "元");
    }

    public void onclick() {
        // TODO Auto-generated method stub
        btn_topay.setOnClickListener(this);
        iv_pay_weixin.setOnClickListener(this);
        iv_pay_zhifubao.setOnClickListener(this);
    }

    public void initData() {
        req = new PayReq();
        sb = new StringBuffer();
        uid = SharedPreferencesTools.getUid(PaymentActivity.this);
        payfor = getIntent().getStringExtra("payfor");
        vipflg_str = getIntent().getStringExtra("vipflg_str");
        if (payfor.equals("VideoFive")) {
            aid = getIntent().getStringExtra("aid");
        }
//        money = getIntent().getIntExtra("money", 0);
        Str_money = getIntent().getStringExtra("money");
        viptitle = getIntent().getStringExtra("subject");
        Str_username = SharedPreferencesTools.getUserName(PaymentActivity.this);
        tv_username_vip.setText(Str_username);
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

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (arg0.getId()) {
            case R.id.btn_topay:
                if (isFastDoubleClick()) {
                    return;
                }
                if ("".equals(banktype)) {
                    Toast.makeText(PaymentActivity.this, "请选择支付方式！", Toast.LENGTH_LONG).show();
                } else if ("wechat".equals(banktype)) {
                    if (isFastDoubleClick()) {
                        return;
                    }
                    boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                    if (isPaySupported) {
                        wexinpay();
                    } else {
                        Toast.makeText(PaymentActivity.this, "请安装或升级微信客户端！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //支付宝
                    if (isFastDoubleClick()) {
                        return;
                    }
                    alipaypay();
                }
                break;
            case R.id.iv_pay_weixin:// 选择微信
                iv_pay_weixin
                        .setImageResource(R.mipmap.delete_item_select2);
                iv_pay_zhifubao
                        .setImageResource(R.mipmap.ic_round_notselected);
                banktype = "wechat";
                break;
            case R.id.iv_pay_zhifubao:// 选择支付宝
                iv_pay_weixin
                        .setImageResource(R.mipmap.ic_round_notselected);
                iv_pay_zhifubao
                        .setImageResource(R.mipmap.delete_item_select2);
                banktype = "alipay";
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    //是否是多次点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 5000) {
            Toast.makeText(finish, "请不要频繁操作", Toast.LENGTH_SHORT).show();
            return true;
        }
        lastClickTime = time;
        return false;
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
                    if (payfor.equals("recharge")) {
                        obj.put("money", Integer.parseInt(Str_money));
                    }
                    obj.put("type", "vippay");
                    obj.put("banktype", "alipay");
                    if (payfor.equals("VideoFive")) {
                        obj.put("aid", aid);
                    }
                    obj.put("vipflg_str", vipflg_str);
                    obj.put("act", URLConfig.alipayInfo);
                    String result = HttpClientUtils.sendPost(PaymentActivity.this, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    mHandler.sendMessage(message);
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
        orderInfo += "&notify_url=" + "\"" + URLConfig.alipayNotify_url_new
                + "\"";

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

    /**
     * name：微信支付
     * author：Larry
     * data：2016/4/21 21:26
     */
    public void wexinpay() {
        msgApi.registerApp(URLConfig.APP_ID);
        Toast.makeText(PaymentActivity.this, "微信支付中...", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    if (payfor.equals("recharge")) {
                        obj.put("money", Integer.parseInt(Str_money));
                    }
                    if (payfor.equals("VideoFive")) {
                        obj.put("aid", aid);
                    }
                    obj.put("vipflg_str", vipflg_str);
                    obj.put("act", URLConfig.getInformation);
                    String result = HttpClientUtils.sendPost(PaymentActivity.this, URLConfig.CCMTVAppPayNew, obj.toString());
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    mHandler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
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
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

}
