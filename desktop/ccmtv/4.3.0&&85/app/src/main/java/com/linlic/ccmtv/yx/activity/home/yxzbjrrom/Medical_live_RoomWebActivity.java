package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.widget.FocuedTextView;
import com.linlic.ccmtv.yx.widget.ProgressWebView;

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
 * name：医学直播间
 * author：Larry
 * data：2016/8/22.
 */
public class Medical_live_RoomWebActivity extends BaseActivity implements PlatformActionListener {
    Context context;
    WebView webView;
    FocuedTextView activity_title_name;
    String url;
    String title;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    if (msg.obj.toString().contains("WechatClientNotExistException")) {
                        Toast.makeText(getApplicationContext(), "您的微信版本过低或未安装微信，需要安装并启动微信才能使用", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(Medical_live_RoomWebActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.medical_live_room_webview);
        context = this;
        super.findId();
        url = getIntent().getExtras().getString("aid");
        title = getIntent().getStringExtra("title");
        activity_title_name = (FocuedTextView) findViewById(R.id.activity_title_name);

        //load本地
        webView = (ProgressWebView) findViewById(R.id.progresswebview);
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
//        webView.addJavascriptInterface(new JSHook(), "hello"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条

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
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


        //WebView加载web资源
        webView.loadUrl(url);
        activity_title_name.setText(title);
        activity_title_name.requestFocus();
    }

    //分享url
    public void ShareWeb(View view) {
        //分享操作
        //ShareSDK.initSDK(Medical_live_RoomWebActivity.this);
        final ShareDialog shareDialog = new ShareDialog(Medical_live_RoomWebActivity.this);
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
                    sp.setText("医学视频:" + title + "~" + url); //分享文本
                    // sp.setText("输入手机号码，轻松注册领取积分，尽享海量医学视频！"); //分享文本
                    sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    //3、非常重要：获取平台对象
                    Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    //sinaWeibo.removeAccount(true);
                    // ShareSDK.removeCookieOnAuthorize(true);
                    sinaWeibo.setPlatformActionListener(Medical_live_RoomWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    sinaWeibo.share(sp);

                } else if (item.get("ItemText").equals("微信好友")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                    sp.setTitle(title);  //分享标题
                    sp.setText(title);   //分享文本
                    sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(Medical_live_RoomWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);
                } else if (item.get("ItemText").equals("朋友圈")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                    sp.setTitle(title);  //分享标题
                    sp.setText(title);   //分享文本
                    sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(Medical_live_RoomWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);

                } else if (item.get("ItemText").equals("QQ")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setTitle(title);
                    sp.setText(title);
                    sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    sp.setTitleUrl(url);  //网友点进链接后，可以看到分享的详情
                    //3、非常重要：获取平台对象
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(Medical_live_RoomWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);

                } else if (item.get("ItemText").equals("QQ空间")) {

                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setTitle(title);
                    sp.setTitleUrl(url); // 标题的超链接
                    sp.setText(title);
                    sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");
                    sp.setSite("CCMTV临床医学频道");
                    sp.setSiteUrl(url);

                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(Medical_live_RoomWebActivity.this); // 设置分享事件回调
                    // 执行图文分享
                    qzone.share(sp);

                } else {
                    ClipboardManager cmb = (ClipboardManager) Medical_live_RoomWebActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(url.trim());
                    Toast.makeText(Medical_live_RoomWebActivity.this, "复制成功",
                            Toast.LENGTH_LONG).show();
                }
                shareDialog.dismiss();
            }
        });
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(5);
        }

    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7;
        //msg.obj = arg2.getMessage();
        msg.obj = arg2.toString();
        handler.sendMessage(msg);
    }


    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(6);
    }

    @Override
    protected void onDestroy() {
        //ShareSDK.stopSDK(Medical_live_RoomWebActivity.this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

}
