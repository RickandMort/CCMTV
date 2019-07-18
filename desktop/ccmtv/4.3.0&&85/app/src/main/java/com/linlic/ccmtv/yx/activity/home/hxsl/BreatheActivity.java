package com.linlic.ccmtv.yx.activity.home.hxsl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ProgressWebView;

import org.json.JSONObject;

/**
 * name：易呼宜吸
 * author：Larry
 * data：2017/2/10.
 */
public class BreatheActivity extends BaseActivity {
    Context context;
    private ProgressWebView webView;
    private String aid, url;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    MyProgressBarDialogTools.hide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathe);
        context = this;
        initWebView();
        TextView textView = (TextView) findViewById(R.id.activity_title_name);
        textView.setText("易呼宜吸");
    }


    private void initWebView() {
        //load本地
        webView = (ProgressWebView) findViewById(R.id.progresswebview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持javascript
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(new JSHook(), "hello"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法
        webView.addJavascriptInterface(new JSHook(), "hello2"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法
        webView.addJavascriptInterface(new JSHook(), "hello3"); //在JSHook类里实现javascript想调用的方法，并将其实例化传入webview, "hello"这个字串告诉javascript调用哪个实例的方法
        // webView.requestFocus();//触摸焦点起作用
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
    }

    @Override
    protected void onResume() {
        url = getIntent().getStringExtra("aid");
        if (SharedPreferencesTools.getUids(BreatheActivity.this) == null || "".equals(SharedPreferencesTools.getUids(BreatheActivity.this))) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl(url + "&lfjuid=" + SharedPreferencesTools.getUids(BreatheActivity.this));
        }
        webView.onResume();
        super.onResume();
    }

    /**
     * 使点击回退按钮不会直接退出整个应用程序而是返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }

    class JSHook {

        @JavascriptInterface
        public void showAndroid() {
            //判断是否登录
            if (SharedPreferencesTools.getUids(BreatheActivity.this) == null) {
                Intent intent = new Intent(BreatheActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            } else {
                final String username = SharedPreferencesTools.getUserName(BreatheActivity.this);
                BreatheActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:show('" + username + "')");
                    }
                });
            }

        }

        @JavascriptInterface
        public void showAndroid2() {
            //判断是否登录
            if (SharedPreferencesTools.getUids(BreatheActivity.this) == null) {
                Intent intent = new Intent(BreatheActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            } else {
                final String uid = SharedPreferencesTools.getUids(BreatheActivity.this);
                BreatheActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:show2('" + uid + "')");
                    }
                });
            }

        }

        @JavascriptInterface
        public String showAndroid3(final String param) {
            //判断是否登录
            if (SharedPreferencesTools.getUids(BreatheActivity.this) == null) {
                Intent intent = new Intent(BreatheActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                BreatheActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aid = param;
                        MyProgressBarDialogTools.show(BreatheActivity.this);
                        getVideo();
                    }
                });
            }
            return "Html call Java : " + param;
        }

    }


    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void getVideo() {

        final String uid = SharedPreferencesTools.getUidToLoginClose(BreatheActivity.this);
        if (uid == null || ("").equals(uid)) {
            MyProgressBarDialogTools.hide();
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.video_play_act);
                    obj.put("uid", uid);
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(BreatheActivity.this,
                            URLConfig.CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 2;
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
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        webView.onPause();
        super.onPause();
    }

}
