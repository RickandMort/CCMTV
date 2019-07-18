package com.linlic.ccmtv.yx.activity.my.book;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.YKListActivity;
import com.linlic.ccmtv.yx.activity.CustomActivity2;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.assginks.AssginKsIndexAcivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.bigcase.BigCaseActivity;
import com.linlic.ccmtv.yx.activity.check_work_attendance.Check_work_attendance;
import com.linlic.ccmtv.yx.activity.comment360.AppraiseIndexActivity;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_Entity;
import com.linlic.ccmtv.yx.activity.entrance_edu.EntranceEduUserActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.hospital_training.Practice_Main;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.message.Message_management;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.activity.my.feedback.Feedback_Main;
import com.linlic.ccmtv.yx.activity.my.learning_task.LearningTaskActivity;
import com.linlic.ccmtv.yx.activity.my.medical_examination.My_exams_LearningTaskActivity;
import com.linlic.ccmtv.yx.activity.my.our_video.My_Our_Resources_Activity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.audit.Audit_list;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities.College_level_activities;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.ExaminationManageActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.SingleStationExamActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.rotation.Rotation;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.students.Students_month;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management.Entry_month_selection;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.the_teachers_management.The_teachers_management;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management3;
import com.linlic.ccmtv.yx.activity.supervisor.Oversight_Task_List;
import com.linlic.ccmtv.yx.activity.tutor.Tutor_students;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.gensee.GenseeMainActivity;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.FocuedTextView;
import com.linlic.ccmtv.yx.widget.ProgressWebView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.linlic.ccmtv.yx.activity.MainActivity.curr_num;


/**
 * name：实体书
 */
public class Video_book2 extends BaseActivity  {
    Context context;
    WebView webView;
    TextView purchase;
    FocuedTextView activity_title_name;
    String url;
    String title;
    WebChromeClient mWebChromeClient;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 8:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject data = jsonObject.getJSONObject("data");

//                            setActivity_title_name(data.getString("booktitle"));
                            //WebView加载web资源
                            webView.loadUrl(data.getString("bookurl"));
//        webView.loadUrl("file:///android_asset/index.html");
                            webView.addJavascriptInterface(new JavaScriptinterface(context),
                                    "android");
                            activity_title_name.requestFocus();
                        } else {//失败
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(Video_book2.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private String shareimgurl;//分享图片
    private String activitycontent;//分享文案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_book2);
        context = this;
        super.findId();
        url = getIntent().getExtras().getString("aid");
        shareimgurl = getIntent().getStringExtra("shareimgurl");
        activitycontent = getIntent().getStringExtra("activitycontent");

        title = getIntent().getStringExtra("title");
        purchase = findViewById(R.id.purchase);
        activity_title_name = (FocuedTextView) findViewById(R.id.activity_title_name);
        //activity_title_name.setText(getIntent().getStringExtra("title"));
//        getWindow().setFlags(//强制打开GPU渲染
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        webView = (ProgressWebView) findViewById(R.id.progresswebview);
        //load本地
        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        // 设置 缓存模式

            webView.getSettings().setCacheMode(
                    WebSettings.LOAD_NO_CACHE);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // webView.requestFocus();//触摸焦点起作用
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });*/
        webView.setWebViewClient(new MyWebViewClient());
        mWebChromeClient = new WebChromeClient();
        webView.setWebChromeClient(mWebChromeClient);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        initview();
        setmsgdb();
    }

    public void initview(){
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isfast=checkDoubleClick();
                if(!isfast){
                    Intent intent = new Intent(context,Order_filling.class);
                    intent.putExtra("id",getIntent().getStringExtra("book_id"));
                    intent.putExtra("book_name",getIntent().getStringExtra("book_name"));
                    intent.putExtra("book_img",getIntent().getStringExtra("book_img"));
                    intent.putExtra("num","1");
                    startActivity(intent);
                }
            }
        });
    }



    public class JavaScriptinterface {
        Context context;
        public JavaScriptinterface(Context c) {
            context= c;
        }


        /*用户ID*/
        @JavascriptInterface
        public String getUid() {
              String uid = SharedPreferencesTools.getUidONnull(context);
          /*  runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:getAppUidFromApp("+uid+")");
                }
            });*/

          return uid;
        }
        /*跳转个人中心*/
        @JavascriptInterface
        public void myCenter () {
            curr_num= 4;
            Intent intent = new Intent(context, MainActivity.class);

            startActivity(intent);
            finish();
        }
        /*跳转播放器*/
        @JavascriptInterface
        public void palyVideo(String aid) {
              String uid = SharedPreferencesTools.getUid(context);
            Intent intent = new Intent(context, VideoFive.class);
            intent.putExtra("aid",aid);
            startActivity(intent);

        }
        /*跳转会议*/
        @JavascriptInterface
        public void metting (String title_name,String bannerid) {
              String uid = SharedPreferencesTools.getUid(context);
            Intent intent = new Intent(context, ConferenceDetailActivity.class);
            intent.putExtra("conferenceId", bannerid);
            intent.putExtra("title",title_name);
            startActivity(intent);

        }
        /*跳转搜索页*/
        @JavascriptInterface
        public void search(String str) {
            Intent intent = new Intent(context, CustomActivity2.class);
            intent.putExtra("type","home");
            intent.putExtra("custom_title", str);
            intent.putExtra("mode", "1");
            startActivity(intent);
        }
        /*跳转搜索页*/
        @JavascriptInterface
        public void search2(String type,String disease_class) {
            Intent intent = null;

                    intent = new Intent(context, CustomActivity2.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("video_class", type);
                    intent.putExtra("disease_class",disease_class);
                    intent.putExtra("mode", "6");
                startActivity(intent);

        }

        /*跳转云管家某一个功能*/
        @JavascriptInterface
        public void yun_x(String type,String name,String id,String url) {
            LogUtil.e("网页传值", type +"-type "+name+"-name "+id+"-id "+url+"-url");
            boolean isfast=checkDoubleClick();
            Intent intent = null;
            //判断是否登录
            if (SharedPreferencesTools.getUids(context) == null) {
                intent = new Intent(context, LoginActivity.class);
                intent.putExtra("source", "my");
                startActivity(intent);
                return;
            }
            switch (type) {
                case "task":
                    intent = new Intent(context, LearningTaskActivity.class);
                    break;
                case "supervisor":
                    intent = new Intent(context, Oversight_Task_List.class);
                    intent.putExtra("title", name);
                    break;
                case "exam":
                    intent = new Intent(context, My_exams_LearningTaskActivity.class);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject();
                                object.put("cuid", SharedPreferencesTools.getUids(context));
                                object.put("act", URLConfig.checkHosUser);
                                String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, object.toString());
                                Message message = new Message();
                                message.what = 4;
                                message.obj = result;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }).start();
                    break;
                case "notice":
                    intent = new Intent(context, Message_management.class);
                    break;
                case "tutor":
                    intent = new Intent(context, Tutor_students.class);
                    intent.putExtra("title", name);
                    break;
                case "resource":
                    intent = new Intent(context, My_Our_Resources_Activity.class);
                    break;
                case "test":
                    intent = new Intent(context, Practice_Main.class);
                    break;
                case "plan"://轮转计划
                    intent = new Intent(context, Rotation.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("title", name);
                    break;
                case "appraise":
                    switch (SharedPreferencesTools.getGp_type(context)) {
                        case "3":
                            break;
                        case "1":
                            intent = new Intent(context, Evaluation.class);
                            intent.putExtra("fid", id);
                            break;
                        case "2":
                            intent = new Intent(context, Evaluation2.class);
                            intent.putExtra("fid", id);
                            break;
                    }

                    break;
                case "rk"://入科管理
                    intent = new Intent(context, Entry_month_selection.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name", name);
                    break;
                case "examine":
                    intent = new Intent(context, ExaminationManageActivity.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name", name);
                    break;
                case "reflect":
                    if(SharedPreferencesTools.getUid(context).length()>0){
                        intent = new Intent(context, Feedback_Main.class);
                    }
                    break;
                case "faculty"://师资管理
                    intent = new Intent(context, The_teachers_management.class);
                    intent.putExtra("fid", id);
                    break;
                case "activities"://教学活动
                    intent = new Intent(context, Training_management.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name", name);
                    break;
                case "tealist":
                    intent = new Intent(context, Training_management3.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name", name);
                    break;
                case "my_activities":
                    intent = new Intent(context, Training_management2.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name", name);
                    break;
                case "student":
                    intent = new Intent(context, Students_month.class);
                    intent.putExtra("fid", id);
                    break;
                case "verify":
                    intent = new Intent(context, Audit_list.class);
                    intent.putExtra("fid", id);
                    break;
                case "stageExam":
//                        intent = new Intent(context, PeriodicalExamListActivity.class);
//                        intent.putExtra("fid", id);
                    intent = new Intent(context, SingleStationExamActivity.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("icon","stageExam");
                    break;
                case "sign"://考勤签到
                    intent = new Intent(context, Check_work_attendance.class);
                    intent.putExtra("fid", id);
//                        intent = new Intent(context, YKListActivity.class);
                    break;
                case "activity"://院级活动
                    intent = new Intent(context, College_level_activities.class);
                    intent.putExtra("fid", id);
                    break;
                case "big_case"://大病历
                    intent = new Intent(context, BigCaseActivity.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("name",name);
                    break;
                case "rkedu"://入科教育
                    intent = new Intent(context, EntranceEduUserActivity.class);
                    break;
                case "yueke"://约课
                    intent = new Intent(context, YKListActivity.class);
                    intent.putExtra("fid", id);
                    break;
                case "rkks":
                    //入科分配科室
                    intent = new Intent(context, AssginKsIndexAcivity.class);
                    break;
                case "appraiseT":
                    //评价住院医师
                    intent = new Intent(context, AppraiseIndexActivity.class);
                    intent.putExtra("fid", id);
                    break;
                case "live":
                    //直播
                    intent = new Intent(context, GenseeMainActivity.class);
                    intent.putExtra("fid", id);
                    break;
                case "osce":
                    intent = new Intent(context, SingleStationExamActivity.class);
                    intent.putExtra("fid", id);
                    intent.putExtra("icon","osce");
                    break;
            }

            //先判断url是否为空，不为空则跳转轮转手册网页
            if (url.length() > 0) {//轮转手册
//                    intent = new Intent(context, GpWebViewActivity.class);
//                    intent.putExtra("url", url);
//                    intent.putExtra("name", map.get("name").toString());
                intent = new Intent(context, Video_book2.class);
                intent.putExtra("title", name);
                intent.putExtra("aid", url);
            }
            if (intent != null&&isfast==false) {
                startActivity(intent);
            }
        }
    }
    /** 判断是否是快速点击 */
    private static long lastClickTime;
    public static boolean checkDoubleClick(){
        //点击时间
        long clickTime = SystemClock.uptimeMillis();
        //如果当前点击间隔小于500毫秒
        if (lastClickTime >= clickTime - 500) {
            return true;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        return false;
    }
    boolean isRedirect = false; //是否是重定向
    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            String uid = SharedPreferencesTools.getUid(context);
            webView.loadUrl("javascript:getIsApp(1)");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            isRedirect = true;
        }
    }





    //在项目出口Activity的onDestroy方法中第一行插入下面的代码：
    @Override
    protected void onDestroy() {
        //ShareSDK.stopSDK(ActivityWebActivity.this);
        super.onDestroy();
        webView.loadUrl("about:blank");
    }

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        webView.onPause();
        super.onPause();
    }

  /*  @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }*/

    @Override
//设置当点击后退按钮时不是退出Activity，而是让WebView后退一页。也可以通过webview.setOnKeyListener设置
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
                   return true;
                   }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void back(View view) {
        if (webView.canGoBack()) {
            webView.goBack();//返回上个页面
            return;
        } else {
            finish();
        }
    }
    public void onfinish(View view) {

            finish();
    }


    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.BooksDetails);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", getIntent().getStringExtra("book_id"));

                    String result = HttpClientUtils.sendPost(context, URLConfig.book_url, obj.toString());
//                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("电子书",result);

                    Message message = new Message();
                    message.what = 8;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
