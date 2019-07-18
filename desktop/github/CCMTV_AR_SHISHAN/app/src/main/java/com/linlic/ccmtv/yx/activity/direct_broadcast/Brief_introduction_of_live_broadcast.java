package com.linlic.ccmtv.yx.activity.direct_broadcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Direct_broadcast;
import com.linlic.ccmtv.yx.kzbf.activity.BigImageActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineDetialActivity;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by Administrator on 2018/3/15.
 */

public class Brief_introduction_of_live_broadcast extends BaseActivity implements View.OnClickListener, PlatformActionListener {
    Context context;
    private FrameLayout video_view;
    private WebView webView;
    private Direct_broadcast direct_broadcast ;
    private WebSettings ws;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brief_introduction_of_live_broadcast);
        context = this;
        findId();
        initData();

    }

    @Override
    public void findId() {
        super.findId();
        webView = (WebView) findViewById(R.id.detail_webView);
        video_view = (FrameLayout) findViewById(R.id.video_view);
    }



    private void initData() {

        direct_broadcast = (Direct_broadcast) getIntent().getSerializableExtra("direct_broadcast");
        super.setActivity_title_name(direct_broadcast.getDirect_broadcast_item_title());
        ws = webView.getSettings();//获取webview设置属性
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
        ws.setJavaScriptEnabled(true);//支持js
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        ws.setLoadWithOverviewMode(true);// 缩放至屏幕的大小

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.setBackgroundColor(0);


        getWindow().setFlags(//强制打开GPU渲染
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // 加载定义的代码，并设定编码格式和字符集。
        webView.setWebViewClient(new Brief_introduction_of_live_broadcast.MyWebViewClient());
        webView.setWebChromeClient(new Brief_introduction_of_live_broadcast.xWebChromeClient());//设置视屏可以全屏
        webView.loadDataWithBaseURL(null, direct_broadcast.getAbout() + "<script>  document.body.style.lineHeight = 1.5< /script> \\n< /html>", "text/html", "utf-8", null);
        webView.addJavascriptInterface(new MedicineDetialActivity.JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
//                            webView.addJavascriptInterface(new JsObject(context), "console");//给视频设置监听

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

    public void back(View v) {
        finish();
    }

    //给TextView设置数据
    private void setTextView(JSONObject customJson1, String s, TextView textView) throws JSONException {
        if (!customJson1.getString(s).equals("")) {
            textView.setText(customJson1.getString(s));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理

    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理

    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            videoReset();
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            MyProgressBarDialogTools.hide();//pdf适配屏幕之后再关闭
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    /**
     * 对视频进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void videoReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('video'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var video = objs[i];   " +
                "    video.style.maxWidth = '100%'; video.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    public static class JavaScriptInterface {

        private Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
//            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, BigImageActivity.class);//BigImageActivity查看大图的类
            context.startActivity(intent);
        }
    }



    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     *
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;

        @Override
        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (webView != null) {
                webView.setVisibility(View.GONE);
            }
            video_view.addView(view);
            if (video_view != null) {
                video_view.setVisibility(View.VISIBLE);
            }
        }

        @SuppressLint("NewApi")
        @Override
        // 视频播放退出全屏会被调用的
        public void onHideCustomView() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            video_view.setVisibility(View.GONE);
            if (webView != null) {
                webView.setVisibility(View.VISIBLE);
            }
            if (ws != null) {
                ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
            }
        }

        // 视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(getResources(), R.mipmap.login_logo);
            }
            return xdefaltvideo;
        }

        // 网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            // a.setTitle(title)
            //view.getSettings().setBlockNetworkImage(false);
        }

        @Override
        // 当WebView进度改变时更新窗口进度
        public void onProgressChanged(WebView view, int newProgress) {
            getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
        }
    }

    @Override
    protected void onDestroy() {
        webView.loadUrl("about:blank");
        super.onDestroy();
    }
}
