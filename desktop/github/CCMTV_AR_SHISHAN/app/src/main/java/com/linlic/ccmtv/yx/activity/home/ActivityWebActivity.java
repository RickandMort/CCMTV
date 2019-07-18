package com.linlic.ccmtv.yx.activity.home;

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
import com.linlic.ccmtv.yx.activity.entrance_edu.EntranceEduUserActivity;
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
 * name：活动网页
 * author：Larry
 * data：2016/8/22.
 */
public class ActivityWebActivity extends BaseActivity implements PlatformActionListener {
    Context context;
    WebView webView;
    FocuedTextView activity_title_name;
    String url;
    String title;
    WebChromeClient mWebChromeClient;

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
                    Toast.makeText(ActivityWebActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_webview);
        context = this;
        super.findId();
        url = getIntent().getExtras().getString("aid");
        shareimgurl = getIntent().getStringExtra("shareimgurl");
        activitycontent = getIntent().getStringExtra("activitycontent");

        title = getIntent().getStringExtra("title");
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

        //WebView加载web资源
        webView.loadUrl(url);
//        webView.loadUrl("file:///android_asset/index.html");
        webView.addJavascriptInterface(new JavaScriptinterface(this),
                "android");
        activity_title_name.setText(title);
        activity_title_name.requestFocus();
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
                intent = new Intent(context, ActivityWebActivity.class);
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

    //分享url
    public void ShareWeb(View view) {
        //分享操作
        //ShareSDK.initSDK(ActivityWebActivity.this);
        final ShareDialog shareDialog = new ShareDialog(ActivityWebActivity.this);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();

            }
        });
        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                if (item.get("ItemText").equals("微博")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    if (!TextUtils.isEmpty(activitycontent)){
                        sp.setText(activitycontent); //分享文本
                    } else {
                        sp.setText("医学视频:" + title + "~" + url); //分享文本
                    }
                    // sp.setText("输入手机号码，轻松注册领取积分，尽享海量医学视频！"); //分享文本
                    if (!TextUtils.isEmpty(shareimgurl)){
                        sp.setImageUrl(shareimgurl);//网络图片rul
                    } else {
                        sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    }
                    //3、非常重要：获取平台对象
                    Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    //sinaWeibo.removeAccount(true);
                    // ShareSDK.removeCookieOnAuthorize(true);
                    sinaWeibo.setPlatformActionListener(ActivityWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    sinaWeibo.share(sp);

                } else if (item.get("ItemText").equals("微信好友")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                    if (!TextUtils.isEmpty(activitycontent)){
                        sp.setText(activitycontent);
                        sp.setTitle("CCMTV-2018年终学习报告");  //分享标题
                    } else {
                        sp.setText(title);   //分享文本
                        sp.setTitle("CCMTV临床医学频道");  //分享标题
                    }
                    if (!TextUtils.isEmpty(shareimgurl)){
                        sp.setImageUrl(shareimgurl);
                    } else {
                        sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    }
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(ActivityWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);
                } else if (item.get("ItemText").equals("朋友圈")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性

                    if (!TextUtils.isEmpty(activitycontent)){
                        sp.setText(activitycontent);
                        sp.setTitle("CCMTV-2018年终学习报告");  //分享标题
                    } else {
                        sp.setTitle("CCMTV临床医学频道");  //分享标题
                        sp.setText(title);   //分享文本
                    }
                    if (!TextUtils.isEmpty(shareimgurl)){
                        sp.setImageUrl(shareimgurl);
                    } else {
                        sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    }
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(ActivityWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);

                } else if (item.get("ItemText").equals("QQ")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();

                    if (!TextUtils.isEmpty(activitycontent)){
                        sp.setText(activitycontent);
                        sp.setTitle("CCMTV-2018年终学习报告");  //分享标题
                    } else {
                        sp.setText(title);   //分享文本
                        sp.setTitle("CCMTV临床频道");
                    }
                    if (!TextUtils.isEmpty(shareimgurl)){
                        sp.setImageUrl(shareimgurl);
                    } else {
                        sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    }
                    sp.setTitleUrl(url);  //网友点进链接后，可以看到分享的详情
                    //3、非常重要：获取平台对象
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(ActivityWebActivity.this); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);

                } else if (item.get("ItemText").equals("QQ空间")) {

                    Platform.ShareParams sp = new Platform.ShareParams();

                    sp.setTitleUrl(url); // 标题的超链接
                    if (!TextUtils.isEmpty(activitycontent)){
                        sp.setText(activitycontent);
                        sp.setTitle("CCMTV-2018年终学习报告");  //分享标题
                    } else {
                        sp.setText(title);   //分享文本
                        sp.setTitle("CCMTV临床医学频道");
                    }
                    if (!TextUtils.isEmpty(shareimgurl)){
                        sp.setImageUrl(shareimgurl);
                    } else {
                        sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                    }
                    sp.setSite("CCMTV临床医学频道");
                    sp.setSiteUrl(url);

                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(ActivityWebActivity.this); // 设置分享事件回调
                    // 执行图文分享
                    qzone.share(sp);

                } else {
                    ClipboardManager cmb = (ClipboardManager) ActivityWebActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(url.trim());
                    Toast.makeText(ActivityWebActivity.this, "复制成功",
                            Toast.LENGTH_LONG).show();
                }
                shareDialog.dismiss();
            }
        });
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(5);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7;
        //msg.obj = arg2.getMessage();
        msg.obj = arg2.toString();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(6);
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
}
