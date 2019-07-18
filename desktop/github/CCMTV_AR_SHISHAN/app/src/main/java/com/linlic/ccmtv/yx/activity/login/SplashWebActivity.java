package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.widget.FocuedTextView;
import com.linlic.ccmtv.yx.widget.ProgressWebView;


/**
 * name：活动网页
 * author：Larry
 * data：2016/8/22.
 */
public class SplashWebActivity extends BaseActivity  {
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
                    Toast.makeText(SplashWebActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        super.findId();
        context=this;
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
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        startActivity(new Intent(SplashWebActivity.this, MainActivity.class));
        return false;
    }
    @Override
    public void back(View view) {
        super.back(view);
        startActivity(new Intent(SplashWebActivity.this, MainActivity.class));
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
