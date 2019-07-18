package com.linlic.ccmtv.yx.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.cashier.Cashier_recharge;
import com.linlic.ccmtv.yx.activity.cashier.PaymentActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.my.Integral;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.my.OpenMenberActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
//                        Log.e("支付信息",result.toString());
                        if (result.getInt("status") == 1) {//成功
                            Intent intent = new Intent();
                            if ("vip".equals(PaymentActivity.payfor)) {
                                intent.setClass(WXPayEntryActivity.this, MyOpenMenberActivity.class);
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                            } else if ("inte".equals(PaymentActivity.payfor)) {
                                intent.setClass(WXPayEntryActivity.this, Integral.class);
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                            } else if ("recharge".equals(PaymentActivity.payfor)) {
                                WXPayEntryActivity.this.finish();
                                Cashier_recharge.finish.finish();
                                PaymentActivity.finish.finish();
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                                return;
                            }else if( "VideoFive".equals(PaymentActivity.payfor)){
                                VideoFive.payBoolean_S = "success";
                                VideoFive.payBoolean = 0;
                                PaymentActivity.finish.finish();
                                WXPayEntryActivity.this.finish();
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                                return;
                            }else if( "book".equals(PaymentActivity.payfor)){
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                                WXPayEntryActivity.this.finish();
                                return;
                            }else {
                                intent.setClass(WXPayEntryActivity.this, MainActivity.class);
                                intent.putExtra("type", "register");
                                VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                            }
                            startActivity(intent);
                            WXPayEntryActivity.this.finish();
                            // JSONObject object = new JSONObject(result.getString("data"));
                            // Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            //finish();
                        } else {//失败
                            VideoFive.toast(getApplicationContext(), result.getString("errorMessage"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
                    Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, URLConfig.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "errCode = " + resp.errCode);

        // 支付成功后回调
        if (resp.errCode == 0) {
            // 付款成功
            queryWXPayOrder();
            // Toast.makeText(WXPayEntryActivity.this, "支付回调 0000", Toast.LENGTH_LONG).show();
        } else if (resp.errCode == -1) {
            VideoFive.toast(getApplicationContext(), "支付失败");
          /*  Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
            intent.putExtra("type", "register");
            startActivity(intent);*/
            finish();
        } else if (resp.errCode == -2) {
            VideoFive.toast(getApplicationContext(), "取消支付");
          /*  Intent intent = new Intent(WXPayEntryActivity.this, MainActivity.class);
            intent.putExtra("type", "register");
            startActivity(intent);*/
            /*Intent intent = new Intent(WXPayEntryActivity.this, OpenMenberActivity.class);
            startActivity(intent);*/
            finish();
        }
    }

    public void queryWXPayOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();


                    String result = "";
                    if ("recharge".equals(PaymentActivity.payfor)){
                        obj.put("uid", SharedPreferencesTools.getUid(WXPayEntryActivity.this));
                        obj.put("trade_no", PaymentActivity.trade_no);
                        obj.put("noncestr", PaymentActivity.nonceStr);
                        obj.put("act", URLConfig.checkInformation);
                        result = HttpClientUtils.sendPost(WXPayEntryActivity.this,
                                URLConfig.weixinpayurlNEW, obj.toString());
                    }else  if ("VideoFive".equals(PaymentActivity.payfor)){
                        obj.put("uid", SharedPreferencesTools.getUid(WXPayEntryActivity.this));
                        obj.put("trade_no", PaymentActivity.trade_no);
                        obj.put("noncestr", PaymentActivity.nonceStr);
                        obj.put("act", URLConfig.checkInformation);
                        result = HttpClientUtils.sendPost(WXPayEntryActivity.this,
                                URLConfig.weixinpayurlNEW, obj.toString());
                    }else  if ("book".equals(PaymentActivity.payfor)){
                        obj.put("uid", SharedPreferencesTools.getUid(WXPayEntryActivity.this));
                        obj.put("trade_no", PaymentActivity.trade_no);
                        obj.put("noncestr", PaymentActivity.nonceStr);
                        obj.put("act", URLConfig.checkInformation);
                        result = HttpClientUtils.sendPost(WXPayEntryActivity.this,
                                URLConfig.weixinpayurlNEW, obj.toString());
                    }else {
                        obj.put("uid", SharedPreferencesTools.getUid(WXPayEntryActivity.this));
                        obj.put("trade_no", OpenMenberActivity.trade_no);
                        obj.put("noncestr", OpenMenberActivity.nonceStr);
                        obj.put("act", URLConfig.checkInformation);
                        result = HttpClientUtils.sendPost(WXPayEntryActivity.this,
                                URLConfig.weixinpayurl, obj.toString());
                    }


                    System.out.println("微信支付完成调用接口：" + obj + "|" + result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }
}