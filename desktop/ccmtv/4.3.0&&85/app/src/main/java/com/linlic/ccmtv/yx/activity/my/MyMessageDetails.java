package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息详情页
 *
 * @author yu
 */
public class MyMessageDetails extends BaseActivity {
    TextView activity_title_name, tv_title;
    private Button btn_delete, btn_send;
    private TextView tv_sendpeo_name, tv_getpeo_name, tv_times;
    Context context;
    String Str_musername, Str_pusername, Str_content, Str_date, Str_mid, FLG;
    WebView webView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject object = dataArray.getJSONObject(i);
                                String customHtml = Base64utils.getFromBase64(object.getString("content"));
                                //一个html代码段，用来显示一段红色的字体。
                                webView.loadData(customHtml, "text/html", "UTF-8");
                                // 加载定义的代码，并设定编码格式和字符集。
                                tv_sendpeo_name.setText(object.getString("username"));
                                tv_times.setText(object.getString("mdate"));
                            }
                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        setContentView(R.layout.activity_messagedetail);
        context = this;
        findId();
        setText();
        onclick();
        initData();// 初始化数据
    }

    /**
     * 初始化数据
     */
    public void initData() {
        Str_musername = getIntent().getStringExtra("Str_musername");  //收
        Str_pusername = getIntent().getStringExtra("Str_pusername");  //发
        Str_content = getIntent().getStringExtra("Str_content");
        Str_date = getIntent().getStringExtra("Str_date");
        Str_mid = getIntent().getStringExtra("Str_mid");
        FLG = getIntent().getStringExtra("FLG");
        tv_title.setText(getIntent().getStringExtra("Str_title"));

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);//设置页面支持JavaScript
        //设置默认缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        webView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        // webView.loadData(data, "text/html", "UTF -8");//API提供的标准用法，无法解决乱码问题
        //webView.loadData(Str_content, "text/html", "UTF -8");//这种写法可以正确解码
        webView.loadDataWithBaseURL(null, Str_content, "text/html", "UTF -8", null);

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

        tv_sendpeo_name.setText(Str_pusername);
        tv_getpeo_name.setText(Str_musername);
        tv_times.setText(Str_date);
    }


    public void findId() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        webView = (WebView) findViewById(R.id.webView);
        tv_sendpeo_name = (TextView) findViewById(R.id.tv_sendpeo_name);
        tv_getpeo_name = (TextView) findViewById(R.id.tv_getpeo_name);
        tv_times = (TextView) findViewById(R.id.tv_times);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void onclick() {
        // TODO Auto-generated method stub
        btn_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, BackMessageActivity.class);
                if ("receiver".equals(FLG)) {
                    intent.putExtra("Str_pusername", Str_pusername);
                    intent.putExtra("resouce", "receiver");   //收件箱
                } else {
                    intent.putExtra("Str_pusername", Str_musername);
                    intent.putExtra("resouce", "addresser");   //发件箱
                }
                startActivity(intent);   //跳转至新建消息
                finish();
            }
        });
        btn_delete.setOnClickListener(new OnClickListener() {                   //删除消息
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = new JSONObject();
                        String result = null;
                        try {
                            object.put("mid", Str_mid);
                            object.put("act", URLConfig.delMessage);
                            result = HttpClientUtils.sendPost(context,
                                    URLConfig.CCMTVAPP, object.toString());
                            final String errorMessage = new JSONObject(result + "").getString("errorMessage");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, MyMessageActivity.class);
                                    intent.putExtra("resouce", FLG);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "删除消息失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }).start();
            }
        });
    }

    public void setText() {
        activity_title_name.setText("邮件内容");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // 处理逻辑
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backs();
        }
        return true;
    }

    private void backs() {
        Intent intent = new Intent(context, MyMessageActivity.class);
        intent.putExtra("resouce", FLG);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        backs();
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

}
