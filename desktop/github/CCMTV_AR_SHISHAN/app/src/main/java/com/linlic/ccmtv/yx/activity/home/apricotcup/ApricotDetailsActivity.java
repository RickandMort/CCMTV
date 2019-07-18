package com.linlic.ccmtv.yx.activity.home.apricotcup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ProgressWebView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * name：文章详情页
 * author：Larry
 * data：2017/4/19.
 */
public class ApricotDetailsActivity extends BaseActivity {
    private ProgressWebView webView;
    //用户统计
    private Context context;
    private String aid, picurl, title;
    private String videourl;//分享链接
    private String xqurl; //详情链接
    private static long lastClickTime;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        //SHARE_MEDIA.SINA,
                        if (result.getInt("status") == 1) {//成功
                            // JSONObject object = result.getJSONObject("data");
                            //分享操作
                            if (!TextUtils.isEmpty(videourl)) {
                                //分享操作
                                //ShareSDK.initSDK(context);
                                final ShareDialog shareDialog = new ShareDialog(context);
                                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        shareDialog.dismiss();
                                    }
                                });
                                shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                                            int arg2, long arg3) {
                                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                                        if (item.get("ItemText").equals("微博")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setText("医学视频:" + title + "~" + videourl); //分享文本
                                            // sp.setText("输入手机号码，轻松注册领取积分，尽享海量医学视频！"); //分享文本
                                            //sp.setImageUrl(picurl);//网络图片rul
                                            //3、非常重要：获取平台对象
                                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                            //sinaWeibo.removeAccount(true);
                                            // ShareSDK.removeCookieOnAuthorize(true);
                                            sinaWeibo.setPlatformActionListener(new PlatformActionListener() {
                                                @Override
                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                                    Toast.makeText(context, "微博分享成功", Toast.LENGTH_LONG).show();
                                                    CheckIntegral();
                                                }

                                                @Override
                                                public void onError(Platform platform, int i, Throwable throwable) {
                                                    Message msg = new Message();
                                                    msg.what = 7001;
                                                    msg.obj = throwable.getMessage();
                                                    handler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onCancel(Platform platform, int i) {
                                                    handler.sendEmptyMessage(6001);
                                                }
                                            }); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(title);  //分享标题
                                            // sp.setText(video_title);   //分享文本
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setUrl(videourl);   //网友点进链接后，可以看到分享的详情
                                            //3、非常重要：获取平台对象
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(new PlatformActionListener() {
                                                @Override
                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                                    Toast.makeText(context, "微信好友分享成功", Toast.LENGTH_LONG).show();
                                                    CheckIntegral();
                                                }

                                                @Override
                                                public void onError(Platform platform, int i, Throwable throwable) {
                                                    Message msg = new Message();
                                                    msg.what = 7001;
                                                    msg.obj = throwable.getMessage();
                                                    handler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onCancel(Platform platform, int i) {
                                                    handler.sendEmptyMessage(6001);
                                                }
                                            }); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(title);  //分享标题
                                            // sp.setText(video_title);   //分享文本
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setUrl(videourl);   //网友点进链接后，可以看到分享的详情
                                            //3、非常重要：获取平台对象
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(new PlatformActionListener() {
                                                @Override
                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                                    Toast.makeText(context, "朋友圈分享成功", Toast.LENGTH_LONG).show();
                                                    CheckIntegral();
                                                }

                                                @Override
                                                public void onError(Platform platform, int i, Throwable throwable) {
                                                    Message msg = new Message();
                                                    msg.what = 7001;
                                                    msg.obj = throwable.getMessage();
                                                    handler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onCancel(Platform platform, int i) {
                                                    handler.sendEmptyMessage(6001);
                                                }
                                            }); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(title);
                                            //  sp.setText(video_title);
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setTitleUrl(videourl);  //网友点进链接后，可以看到分享的详情
                                            //3、非常重要：获取平台对象
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(new PlatformActionListener() {
                                                @Override
                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                                    Toast.makeText(context, "QQ分享成功", Toast.LENGTH_LONG).show();
                                                    CheckIntegral();
                                                }

                                                @Override
                                                public void onError(Platform platform, int i, Throwable throwable) {
                                                    Message msg = new Message();
                                                    msg.what = 7001;
                                                    msg.obj = throwable.getMessage();
                                                    handler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onCancel(Platform platform, int i) {
                                                    handler.sendEmptyMessage(6001);
                                                }
                                            }); // 设置分享事件回调
                                            // 执行分享
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(title);
                                            sp.setTitleUrl(videourl); // 标题的超链接
                                            //  sp.setText(video_title);
                                            sp.setImageUrl(picurl);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(videourl);
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(new PlatformActionListener() {
                                                @Override
                                                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                                    Toast.makeText(context, "QQ空间分享成功", Toast.LENGTH_LONG).show();
                                                    CheckIntegral();
                                                }

                                                @Override
                                                public void onError(Platform platform, int i, Throwable throwable) {
                                                    Message msg = new Message();
                                                    msg.what = 7001;
                                                    msg.obj = throwable.getMessage();
                                                    handler.sendMessage(msg);
                                                }

                                                @Override
                                                public void onCancel(Platform platform, int i) {
                                                    handler.sendEmptyMessage(6001);
                                                }
                                            }); // 设置分享事件回调
                                            // 执行分享
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context
                                                    .getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(videourl.trim());
                                            Toast.makeText(context, "复制成功",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        shareDialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6001:
                    Toast.makeText(context, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
                    Toast.makeText(context, "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apricotdetail);
        context = this;
        webView = (ProgressWebView) findViewById(R.id.webview_details);
        //初始化
        initWebView();
        getIntents();

    }

    private void getIntents() {
        aid = getIntent().getStringExtra("aid");
        picurl = getIntent().getStringExtra("picurl");
        title = getIntent().getStringExtra("title");
        xqurl = getIntent().getStringExtra("xqurl");
        videourl = getIntent().getStringExtra("videourl");
        webView.loadUrl(xqurl);
        super.findId();
        super.setActivity_title_name("文章详情");
    }

    private void initWebView() {
        //load本地
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        // webView.requestFocus();//触摸焦点起作用
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        //设置是否缓存
        webView.getSettings().setAppCacheEnabled(false);
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }


    //投票
    private void vote() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", uid);
                    object.put("aid", getIntent().getStringExtra("aid"));
                    object.put("type", "2");
                    object.put("act", URLConfig.lybVote);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    //分享
    private void share() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", uid);
                    object.put("aid", getIntent().getStringExtra("aid"));
                    object.put("act", URLConfig.videoShare);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    public void Vote(View view) {
        if (isFastDoubleClick()) {
            return;
        }
        vote();

    }

    public void Share(View view) {
        if (isFastDoubleClick()) {
            return;
        }
        share();

    }

    //是否是多次点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //分享成功后，检查是否需要增加积分，一天十次机会，每次增加一个积分
    private void CheckIntegral() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoShareSuc);
                    obj.put("aid", aid);
                    obj.put("uid", SharedPreferencesTools.getUidToLoginClose(context));
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);
                    if (jsonresult.getInt("status") == 1) {// 成功
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void back(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        setResult(17, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(17, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
