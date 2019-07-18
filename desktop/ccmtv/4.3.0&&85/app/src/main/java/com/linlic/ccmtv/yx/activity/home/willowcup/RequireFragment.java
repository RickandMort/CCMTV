package com.linlic.ccmtv.yx.activity.home.willowcup;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.widget.ProgressWebView;

import org.json.JSONObject;

/**
 * name：作品要求
 * author：Larry
 * data：2017/3/27.
 */
public class RequireFragment extends BaseLazyFragment {
    private boolean isInit = false;
    private ProgressWebView webView;
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) {// 成功
                            webView.loadUrl(dataObject.getString("url"));
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_require, container, false);
        webView = (ProgressWebView) view.findViewById(R.id.webView);
        context = getActivity();
        initWebView();
        isInit = true;
        LazyLoad();    //让第二个页面加载数据
        return view;
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

    @Override
    public void LazyLoad() {
        if (isInit && isVisible) {
            setmsgdb();
            isInit = true;      //若为false数据仅加载一次

        } else {
            setmsgdb();
        }

    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.lybIndex);
                    obj.put("type", Type.ZPYQ.getIndex());

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
//                    System.out.println("加载数据出错了！");
                }
            }
        };
        new Thread(runnable).start();
    }


}
