package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.xutils.common.util.MD5;

import java.io.File;

public class GpPersonalInformationActivity5 extends BaseActivity {

    private Context context;
    private TextView title_name;
    private WebView webView;

    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public ValueCallback<Uri> mUploadMessage;
    public final static int FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;// 表单的结果回调

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gp_personal_information);
        context = this;
        title_name = (TextView) findViewById(R.id.activity_title_name);
        webView = (WebView) findViewById(R.id.id_webview_gp_personal_information);

        title_name.setText(getIntent().getStringExtra("name")+"的信息");
        initWeb(URLConfig.GpPersonalInformation5);
    }

    public void initWeb(String url) {
        //post访问需要提交的参数
        String postDate = "uid=" + getIntent().getStringExtra("uid") + "&token=" + MD5.md5(getIntent().getStringExtra("uid")  + "123456");
        String newUrl = url + "?" + postDate;

        WebSettings settings = webView.getSettings();
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setAllowFileAccess(true);// 设置可以访问文件
        settings.setJavaScriptEnabled(true);//支持javascript
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setDefaultTextEncodingName("UTF-8");
        webView.requestFocus();//触摸焦点起作用
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条

        //WebView加载web资源
//        Log.e(getLocalClassName(), "initWeb: get网址" + newUrl);
        if (url.startsWith("http:") || url.startsWith("https:")) {
            MyProgressBarDialogTools.show(context);
            webView.loadUrl(newUrl);
        }

        //H5界面加载进度监听
        webView.setWebChromeClient(new WebChromeClient() {

            // For Android < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            // For Android => 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                             FileChooserParams fileChooserParams) {
                onenFileChooseImpleForAndroid(uploadMsg);
                return true;
            }

        });

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    //防止出现被拦截跳至其他网站的问题
                    view.loadUrl(url);
                    return true;
                } else {
                    MyProgressBarDialogTools.hide();
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MyProgressBarDialogTools.hide();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                MyProgressBarDialogTools.hide();
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


        //由于webView.postUrl(url, postData)中 postData类型为byte[] ，
//通过EncodingUtils.getBytes(data, charset)方法进行转换
//        webView.postUrl(url, EncodingUtils.getBytes(postDate, "BASE64"));


//     webView.loadData(HttpURLConnHelper.loadByteFromURL(url).toString(),"text/html", "UTF-8");
    }

    /**
     * android 5.0 以下开启图片选择（原生）
     * <p>
     * 可以自己改图片选择框架。
     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    /**
     * android 5.0(含) 以上开启图片选择（原生）
     * <p>
     * 可以自己改图片选择框架。
     */
    private void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
        mUploadMessageForAndroid5 = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:  //android 5.0以下 选择图片回调

                if (null == mUploadMessage)
                    return;

                 /*
                        * 若当前版本API为19, 则把Uri路径转换成new File性质的Uri路径
                        * 若非 则按照之前方法调用即可
                        * */
                if (Build.VERSION.SDK_INT == 19) {
                    if (intent == null || intent.getData() == null) {
                        mUploadMessage = null;
                        return;
                    }
                    String realPathFromURI = getRealPathFromURI(intent.getData());
                    File file = new File(realPathFromURI);
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        mUploadMessage.onReceiveValue(uri);
                    }
                } else {
                    mUploadMessage.onReceiveValue(result);
                }
                mUploadMessage = null;

                break;

            case FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5:  //android 5.0(含) 以上 选择图片回调

                if (null == mUploadMessageForAndroid5)
                    return;
                if (result != null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                }
                mUploadMessageForAndroid5 = null;

                break;
        }
    }

    /*
  *
  * 获取Uri图片真实路径的方法
  * */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void back(View view) {
        finish();
    }
}
