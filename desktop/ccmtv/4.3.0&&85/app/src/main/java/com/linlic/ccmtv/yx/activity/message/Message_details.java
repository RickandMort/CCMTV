package com.linlic.ccmtv.yx.activity.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.BigImageActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineDetialActivity;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by tom on 2017/9/20.
 * 公告详情
 */
public class Message_details extends BaseActivity implements PlatformActionListener {
    private Context context;
    private String dataurl;
    private TextView viewing_tilte, viewing_time,viewing_name;
    private CircleImageView _icon;
    private WebView viewing_text;
    private WebSettings ws;
    private String master_id,slave_id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject json = dataJson.getJSONObject("data");
                                viewing_tilte.setText(json.getString("title"));
                                viewing_name.setText(Html.fromHtml(json.getString("username")));
                                setImageBitmapGlide(context,_icon,json.getString("icon"));
                                //  加载、并显示HTML代码
                                viewing_text.setWebViewClient(new  MyWebViewClient());
                                viewing_text.setWebChromeClient(new  xWebChromeClient());//设置视屏可以全屏
                                viewing_text.loadDataWithBaseURL(null, appendHtml(json.getString("content")) , "text/html", "utf-8", null);
                                viewing_text.addJavascriptInterface(new MedicineDetialActivity.JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
                                viewing_time.setText(json.getString("create_time"));
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
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
        setContentView(R.layout.viewing_unit_notification2);
        context = this;
        master_id = getIntent().getStringExtra("master_id");
        slave_id = getIntent().getStringExtra("slave_id");
        findId();
        init();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        viewing_tilte = (TextView) findViewById(R.id.viewing_tilte);
        viewing_time = (TextView) findViewById(R.id.viewing_time);
        viewing_name = (TextView) findViewById(R.id.viewing_name);
        _icon = (CircleImageView) findViewById(R.id._icon);
        viewing_text = (WebView) findViewById(R.id.viewing_text);
    }

    public void init(){
        ws = viewing_text.getSettings();//获取webview设置属性
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

        WebSettings webSettings = viewing_text.getSettings();
        webSettings.setJavaScriptEnabled(true);
        viewing_text.addJavascriptInterface(new JiaoHu(), "hello");
        viewing_text.getSettings().setDomStorageEnabled(true);

        viewing_text.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }
    public class JiaoHu {
        @JavascriptInterface
        public void showAndroid() {
//            Toast.makeText(context,"js调用了android的方法",Toast.LENGTH_SHORT).show();
            //点击播放视频即 暂停音频播放
            //停止播放音频
            MainActivity.stopFloatingView(context);

        }
    }

    private String appendHtml(String content) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><head> <style>")
                .append("video::-internal-media-controls-download-button {\n" +
                        "    display:none;\n" +
                        "}\n" +
                        "video::-webkit-media-controls-enclosure {\n" +
                        "    overflow:hidden;\n" +
                        "}\n" +
                        "video::-webkit-media-controls-panel {\n" +
                        "    width: calc(100% + 50px); \n" +
                        "}</style></head><body>")
                .append(content)
                .append( " <script>  document.body.style.lineHeight = 1.5< /script> \\n</body>< /html>");
        return builder.toString();
    }

    public void setImageBitmapGlide(Context context, ImageView view, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Glide.with(context)
                .load(FirstLetter.getSpells(url))
                .apply(options)
                .into(view);

    }
    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.noticeInfoDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("master_id", master_id);
                    obj.put("slave_id", slave_id);
                    LogUtil.e("公告详情",URLConfig.Medical_examination);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Info.html";
        super.onPause();
    }
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;

        @Override
        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            if (webView != null) {
//                webView.setVisibility(View.GONE);
//            }
//            video_view.addView(view);
//            if (video_view != null) {
//                video_view.setVisibility(View.VISIBLE);
//            }

//            ViewGroup parent = (ViewGroup) webView.getParent();
//            parent.removeView(webView);
         /*   detial_content.setVisibility(View.GONE);
            video_view.setVisibility(View.VISIBLE);
            // 设置背景色为黑色
            view.setBackgroundColor(getResources().getColor(R.color.black));
            video_view.addView(view);
            myView = view;
            setFullScreen();*/
        }

        @SuppressLint("NewApi")
        @Override
        // 视频播放退出全屏会被调用的
        public void onHideCustomView() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

           /* if (myView != null) {
                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                detial_content.setVisibility(View.VISIBLE);
                video_view.setVisibility(View.GONE);
                myView = null;
                quitFullScreen();
            }*/

//            video_view.setVisibility(View.GONE);
//            if (webView != null) {
//                webView.setVisibility(View.VISIBLE);
//            }
//            if (ws != null) {
//                ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
//            }
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

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            videoReset();
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            addHrefClickListner();
            MyProgressBarDialogTools.hide();//pdf适配屏幕之后再关闭

            viewing_text.loadUrl("javascript:document.body.style.padding=\"2%\"; void 0");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return true;
        }
    }

    // 注入js函数监听
    private void addHrefClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        viewing_text.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"a\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openHref(this.href,this.title);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        viewing_text.loadUrl("javascript:(function(){" +
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
        viewing_text.loadUrl("javascript:(function(){" +
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
        viewing_text.loadUrl("javascript:(function(){" +
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
            Log.i("TAG", "响应点击事件!");
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, BigImageActivity.class);//BigImageActivity查看大图的类
            context.startActivity(intent);
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openHref(String url, String title) {
            LogUtil.e("JavascriptInterface", title + "响应点击事件!" + url);
            Intent intent = new Intent(context, ActivityWebActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("aid", url);
            context.startActivity(intent);
//            Intent intent = new Intent();
//            intent.putExtra("image", img);
//            intent.setClass(context, BigImageActivity.class);//BigImageActivity查看大图的类
//            context.startActivity(intent);
        }
    }

}
