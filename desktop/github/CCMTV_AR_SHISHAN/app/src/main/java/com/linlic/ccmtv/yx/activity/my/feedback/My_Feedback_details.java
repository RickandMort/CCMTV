package com.linlic.ccmtv.yx.activity.my.feedback;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.JavaScriptinterface;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2019/1/4.
 */

public class My_Feedback_details extends BaseActivity{
    private Context context;
    private String id = "";
    @Bind(R.id.title)
            TextView title;
    @Bind(R.id.time)
            TextView time;
    @Bind(R.id.status)
            TextView status;
    @Bind(R.id.webView)
            WebView webView;
    @Bind(R.id.feedback_null)//
            ImageView feedback_null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 1) { // 成功

                            title.setText(jsonObject.getJSONObject("data").getString("content"));
                            time.setText(jsonObject.getJSONObject("data").getString("post_time"));
                            if(jsonObject.getJSONObject("data").getString("is_reply").equals("1")){
                                status.setText("已回复");
                                feedback_null.setVisibility(View.GONE);
                                webView.setVisibility(View.VISIBLE);
                            }else{
                                status.setText("待回复");
                                feedback_null.setVisibility(View.VISIBLE);
                                webView.setVisibility(View.GONE);
                            }

                            //  加载、并显示HTML代码
                            webView.loadDataWithBaseURL(null, jsonObject.getJSONObject("data").getString("reply_content") + "<script>  document.body.style.lineHeight = 1.5< /script> \\n< /html>", "text/html", "utf-8", null);

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.my_feedback_details);
        context = this;
        ButterKnife.bind(this);

        findId();
        initView();
        myRefundDetail();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Refund.html";
        super.onPause();
    }

    public void initView() {
        id = getIntent().getStringExtra("id");

        //load本地
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        webView.getSettings().setDomStorageEnabled(true);
        // webView.requestFocus();//触摸焦点起作用
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        webView.setWebViewClient(new  MyWebViewClient());
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        //WebView加载web资源
//        webView.loadUrl(url);
//        webView.loadUrl("file:///android_asset/index.html");
        webView.addJavascriptInterface(new JavaScriptinterface(this),
                "android");

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();
            addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
                return true;
            }else{
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);*/
                return true;
            }
        }
    }
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
    private void myRefundDetail() {
        if(SharedPreferencesTools.getUid(context).length()>0){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.myRefundDetail);
                        obj.put("uid",  SharedPreferencesTools.getUid(context));
                        obj.put("id", id );
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("会议科室与时间数据：", result);

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

    }

}
