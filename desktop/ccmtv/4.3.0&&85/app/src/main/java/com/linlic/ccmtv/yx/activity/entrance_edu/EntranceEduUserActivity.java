package com.linlic.ccmtv.yx.activity.entrance_edu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entrance_edu.entity.CycleKsEntity;
import com.linlic.ccmtv.yx.activity.entrance_edu.entity.EntranceEduInfoEntity;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.BigImageActivity;
import com.linlic.ccmtv.yx.utils.DisplayUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by bentley on 2018/12/26.
 */

public class EntranceEduUserActivity extends BaseActivity implements View.OnClickListener{
    private Context context;

    @Bind(R.id.activity_title_name)
    TextView mTitle;
    @Bind(R.id.id_spinner_examination_manage)
    NiceSpinner spinnerEduType;
    @Bind(R.id.id_tv_submit)
    LinearLayout mTvSubmit;
    @Bind(R.id.rb_isread)
    RadioButton mRbIsread;
    @Bind(R.id.detail_webView)
    WebView webView;
    @Bind(R.id.detial_content)
    RelativeLayout detial_content;
    @Bind(R.id.video_view)
    FrameLayout video_view;
    @Bind(R.id.view_comfirm)
    View mViewComfirm;
    @Bind(R.id.lt_nodata1)
    NodataEmptyLayout emptyView;
    @Bind(R.id.detail_scroll)
    ScrollView mScroll;
    @Bind(R.id.tv_edu_tittle)
    TextView tvEduTittle;
    @Bind(R.id.view_KeShi)
    View mViewKeShi;
    @Bind(R.id.view_top)
    View viewTop;
    @Bind(R.id.tv_rk_tittle)
    TextView mTvRkTittle;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tittle_view)
    View subTittleView;


    private boolean isHaveSpinnerDate = false;
    private List<String> eduTypeStringList = new ArrayList<>();//轮转科室科室名
    private List<CycleKsEntity> eduTypeList = new ArrayList<>();//轮转科室科对象
    private WebSettings webSettings;
    private WebSettings ws;
    private boolean is_select = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_entrance_edu);
        context = this;
        ButterKnife.bind(this);
        initData();
        myCycleKs();
    }

    private String id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://获取轮转科室
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONArray typeArray = dataJson.getJSONArray("data");
                                eduTypeStringList.clear();
                                eduTypeList.clear();
                                for (int i = 0; i < typeArray.length(); i++) {
                                    JSONObject typeObject = typeArray.getJSONObject(i);
                                    CycleKsEntity cycleKsEntity = new CycleKsEntity();
                                    cycleKsEntity.setHospital_kid(typeObject.getString("hospital_kid"));
                                    cycleKsEntity.setHospital_kname(typeObject.getString("hospital_kname"));
                                    eduTypeList.add(cycleKsEntity);
                                    eduTypeStringList.add(typeObject.getString("hospital_kname"));
                                }
                                setSpinner();
                                getEduInfo(eduTypeList.get(0).getHospital_kid());
                            } else {
                                //展示空界面
                                mViewKeShi.setVisibility(View.GONE);
                                setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2://根据本院科室id获取当前正在使用的入科教育模板
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                if (!dataJson.isNull("data")) {
                                    JSONObject data = dataJson.getJSONObject("data");
                                    EntranceEduInfoEntity entranceEduInfoEntity = new EntranceEduInfoEntity();
                                    entranceEduInfoEntity.setHospital_kid(data.getString("hospital_kid"));
                                    entranceEduInfoEntity.setId(data.getString("id"));
                                    entranceEduInfoEntity.setTitle(data.getString("title"));
                                    entranceEduInfoEntity.setContent(data.getString("content"));
                                    entranceEduInfoEntity.setHosid(data.getString("hosid"));
                                    entranceEduInfoEntity.setIs_display(data.getInt("is_display"));
                                    entranceEduInfoEntity.setCreatetime(data.getString("createtime"));
                                    entranceEduInfoEntity.setUsername(data.getString("username"));
                                    id = entranceEduInfoEntity.getId();
                                    updateEduInfo(entranceEduInfoEntity);
                                } else {
                                    setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }

                    break;
                case 3://反馈确认
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                Toast.makeText(context, "确认成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void initData() {
        mTitle.setTextSize(16);
        mTitle.setText("入科教育");
        mTvSubmit.setClickable(false);
        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_select){
                    isConfirmRefund(id);
                }
            }
        });


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

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持js

        webView.addJavascriptInterface(new JiaoHu(), "hello");
        webView.getSettings().setDomStorageEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        mRbIsread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTvSubmit.setBackgroundResource(R.mipmap.learning_task04);
                    mTvSubmit.setClickable(true);
                    is_select = true;
                }else{
                    is_select = false;
                }
            }
        });
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

            webView.loadUrl("javascript:document.body.style.padding=\"2%\"; void 0");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            MyProgressBarDialogTools.hide();
            return true;
        }
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

    // 注入js函数监听
    private void addHrefClickListner() {
        // 这段js函数的功能就是，遍历所有的a几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
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
                        "}</style><body>")
                .append(content);
        return builder.toString();
    }

    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     *
     * @author
     */
    private View myView;

    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;

        @Override
        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Log.d("mason","onShowCustomView");
//            if (webView != null) {
//                webView.setVisibility(View.GONE);
//            }
//            video_view.addView(view);
//            if (video_view != null) {
//                video_view.setVisibility(View.VISIBLE);
//            }

//            ViewGroup parent = (ViewGroup) webView.getParent();
//            parent.removeView(webView);
            viewTop.setVisibility(View.GONE);
            detial_content.setVisibility(View.GONE);
            video_view.setVisibility(View.VISIBLE);
            // 设置背景色为黑色
            view.setBackgroundColor(getResources().getColor(R.color.black));
            video_view.addView(view);
            myView = view;
            setFullScreen();
        }

        @SuppressLint("NewApi")
        @Override
        // 视频播放退出全屏会被调用的
        public void onHideCustomView() {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            if (myView != null) {
                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                viewTop.setVisibility(View.VISIBLE);
                detial_content.setVisibility(View.VISIBLE);
                video_view.setVisibility(View.GONE);
                myView = null;
                quitFullScreen();
            }

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

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /***
     * 设置科室选择
     */
    private void setSpinner() {
        isHaveSpinnerDate = true;
        spinnerEduType.attachDataSource(eduTypeStringList);
        spinnerEduType.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getEduInfo(eduTypeList.get(position).getHospital_kid());
            }
        });
    }

    /***
     * 加载入科教育模板
     * @param entranceEduInfoEntity
     */
    private void updateEduInfo(EntranceEduInfoEntity entranceEduInfoEntity) {
        int is_display = entranceEduInfoEntity.getIs_display();//1  显示  0  不显示
        if (is_display == 1) {
            mViewComfirm.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mScroll.getLayoutParams();
            layoutParams.bottomMargin = DisplayUtil.dip2px(80);
            mScroll.setLayoutParams(layoutParams);

        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mScroll.getLayoutParams();
            layoutParams.bottomMargin = DisplayUtil.dip2px(0);
            mScroll.setLayoutParams(layoutParams);
            mViewComfirm.setVisibility(View.GONE);
        }
        tvEduTittle.setText(entranceEduInfoEntity.getTitle());
//        mTvRkTittle.setText(entranceEduInfoEntity.getTitle());
        String username = entranceEduInfoEntity.getUsername();
        String createtime = entranceEduInfoEntity.getCreatetime();
        mTvTime.setText("发布人:" + username + "  " + createtime);
        // 加载定义的代码，并设定编码格式和字符集。
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new xWebChromeClient());//设置视屏可以全屏
        webView.loadDataWithBaseURL(null, appendHtml(entranceEduInfoEntity.getContent() +
                " <script>  document.body.style.lineHeight = 1.5< /script> \\n</body>< /html>"), "text/html", "utf-8", null);
        webView.addJavascriptInterface(new JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
    }

    /**
     * 设置空界面
     * @param code
     */
    private void setResultStatus(int code) {
        if (HttpClientUtils.isNetConnectError(context, code)) {
            emptyView.setNetErrorIcon();
        } else {
            emptyView.setLastEmptyIcon();
        }
        emptyView.setVisibility(View.VISIBLE);
        subTittleView.setVisibility(View.GONE);
    }

    /***
     * 获取其轮转科室
     */
    private void myCycleKs() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myCycleKs);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("mason", "----获取其轮转科室----" + result);
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

    /***
     * 获取对应科室的入科教育
     */
    private void getEduInfo(final String kid) {
        // 加载定义的代码，并设定编码格式和字符集。
//        webView.setWebViewClient(new MyWebViewClient());
//        webView.setWebChromeClient(new xWebChromeClient());//设置视屏可以全屏
//        webView.loadDataWithBaseURL(null, appendHtml("<p><strong><span style=\\\"font-family: 微软雅黑; font-size: 14px; color: rgb(192, 0, 0);\\\">《坦言》——“晚期肾癌靶向治疗十年访谈录”专题栏目<\\/span><\\/strong><\\/p><p><strong><span style=\\\"font-family: 微软雅黑; font-size: 14px; color: rgb(192, 0, 0);\\\">•&nbsp;&nbsp; 记录了肾癌靶向治疗十年发展的每一个重要时刻！<\\/span><\\/strong><\\/p><p><strong><span style=\\\"font-family: 微软雅黑; font-size: 14px; color: rgb(192, 0, 0);\\\">•&nbsp;&nbsp; 蕴含了无数个生命奇迹的诞生！<\\/span><\\/strong><\\/p><p><strong><span style=\\\"font-family: 微软雅黑; font-size: 14px; color: rgb(192, 0, 0);\\\">•&nbsp;&nbsp; 抒写了每一位医生的成长情怀！<\\/span><\\/strong><\\/p><p><span style=\\\"font-family: 微软雅黑; font-size: 14px;\\\">&nbsp;&nbsp;&nbsp;&nbsp; 我们用这种方式，记录下在这十年的时光里，每一位怀抱坚定信念的医生历经的洗礼，在艰难之中演绎的肾癌治疗领域的波澜壮阔！<\\/span><\\/p><p><span style=\\\";font-family:微软雅黑;font-size:14px\\\"><span style=\\\"font-family:微软雅黑\\\">&nbsp;&nbsp;&nbsp;&nbsp; 相信，每位医生的身上流着相同的基因，这份基因将被复制、叠加，不断指引我们做正确的事情，激发我们为肾癌靶向治疗的未来全力以赴<\\/span>!<\\/span><\\/p><p><video class=\\\"edui-upload-video  vjs-default-skin    video-js\\\" controls=\\\"\\\" poster=\\\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/lib\\/ueditor\\/1.4.3\\/images\\/video.jpg\\\" preload=\\\"none\\\" width=\\\"420\\\" height=\\\"280\\\" src=\\\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/2018_upload_files\\/skyvisit\\/video\\/20180211\\/1518333890971526.mp4\\\" data-setup=\\\"{}\\\"><source src=\\\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/2018_upload_files\\/skyvisit\\/video\\/20180211\\/1518333890971526.mp4\\\" type=\\\"video\\/mp4\\\"\\/><\\/video><\\/p><p style=\\\"text-align: center;\\\"><span style=\\\";font-family:微软雅黑;font-size:14px\\\"><span style=\\\"font-family:微软雅黑\\\">第二期<\\/span> <span style=\\\"font-family:微软雅黑\\\">嘉宾介绍<\\/span><\\/span><\\/p><p><img src=\\\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/ccmtvtp\\/Upload\\/skyvisit\\/image\\/20180211\\/1518332802103557.png\\\" title=\\\"1518332802103557.png\\\" alt=\\\"图片.png\\\"\\/><\\/p><p><span style=\\\";font-family:微软雅黑;font-size:16px\\\"><span style=\\\"font-family:微软雅黑\\\">&nbsp;&nbsp;&nbsp; 何志嵩教授和李汉忠教授分享了对于一线治疗失败的患者，后续治疗措施的建议，以及一些二线药物选择上是如何考虑，在更换药物选择上，药物的作用机制及特点对临床患者的作用等。<\\/span><\\/span><\\/p><p><span style=\\\";font-family:微软雅黑;font-size:16px\\\"><span style=\\\"font-family:微软雅黑\\\">&nbsp;&nbsp;&nbsp; 李汉忠教授看好对在肾癌靶向药物治疗方向，相信以后肯定会有更多针对性强，毒副反应少的多靶点的药物生产出来。也希望将来能实现靶向和免疫治疗相结合，那样肾癌晚期的患者的生命还会得到进一步的延长。<\\/span><\\/span><\\/p><p><br\\/><\\/p><script>video.addEventListener(\\\"canplay\\\", function(){alert(111);}<\\/script>" + " <script>  document.body.style.lineHeight = 1.5< /script> \\n</body>< /html>"), "text/html", "utf-8", null);
//        webView.addJavascriptInterface(new JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getHosksRkedu);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("hospital_kid", kid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("mason", "----获取对应科室的入科教育----" + result);
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

    /***
     * 反馈确认
     */
    private void isConfirmRefund(final String id) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.isConfirmRefund);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = 3;
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
    public void onClick(View v) {
        switch (v.getId()){
          /*  case R.id.id_tv_submit://确认提交

                isConfirmRefund(id);
                break;*/
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/rkedu/index.html";
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.loadUrl("about:blank");
        super.onDestroy();
    }

}
