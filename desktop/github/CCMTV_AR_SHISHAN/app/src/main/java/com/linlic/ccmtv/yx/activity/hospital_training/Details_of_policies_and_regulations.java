package com.linlic.ccmtv.yx.activity.hospital_training;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.PDFViewerActivity;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

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

/**
 * 政策法规详情页
 * Created by Administrator on 2017/12/27.
 */

public class Details_of_policies_and_regulations extends BaseActivity implements PlatformActionListener {
    private Context context;
    private String aid;
    private String fid = "9527";
    private LinearLayout arrow_participate;//分享
    private TextView time_text;//时间和作者
    private TextView see;//观看次数
    private WebView web_cont;//内容
    private LinearLayout house;//收藏
    private LinearLayout assist;//点赞
    private LinearLayout llBottom;
    private ImageView house_img;//收场图片
    private ImageView assist_img;//点赞图片
    private TextView house_text;//收藏文字
    private TextView assist_text;//点赞文字、
    private String dataurl;//分享地址
    private String img;//图片地址
    private String title;//标题
    private String pdfurl;
    private ScrollView scrollView;
    private LinearLayout ll_look_pdf;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private int p;
    private float DownY;
    private float moveY;
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/";
    private ProgressDialog progressDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            Details_of_policies_and_regulations.super.setActivity_title_name(jsonObject.getString("title"));
                            title = jsonObject.getString("title");
                            time_text.setText("CCMTV  " + jsonObject.getString("posttime"));
                            see.setText(jsonObject.getString("hits"));
                            web_cont.loadData(getHtmlData(jsonObject.getString("content")), "text/html;charset=utf-8", "utf-8");
                            if (jsonObject.getString("is_collection").equals("1")) {
                                house_text.setText("已收藏");
                            } else {
                                house_text.setText("收藏");
                            }
                            if (jsonObject.getString("is_digg").equals("1")) {
                                assist_text.setText("已点赞");
                            } else {
                                assist_text.setText("点赞");
                            }
                            if (jsonObject.has("pdfurl") && jsonObject.getString("pdfurl").length() > 0) {
                                ll_look_pdf.setVisibility(View.VISIBLE);
                                pdfurl = jsonObject.getString("pdfurl");
                                ll_look_pdf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, PDFViewerActivity.class);
                                        intent.putExtra("url", pdfurl);
                                        startActivity(intent);
                                    }
                                });
//                                initPDF(jsonObject.getString("pdfurl"));
                            } else {
                                ll_look_pdf.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Details_of_policies_and_regulations.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://收藏
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

                            if (jsonObject.getString("is_collection").equals("1")) {
                                house_text.setText("已收藏");
                            } else if (jsonObject.getString("is_collection").equals("0")) {
                                house_text.setText("收藏");
                            } else {

                            }

                        } else {
                            Toast.makeText(Details_of_policies_and_regulations.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3://点赞
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

                            if (jsonObject.getString("is_detail").equals("1")) {
                                assist_text.setText("已点赞");
                            } else if (jsonObject.getString("is_detail").equals("0")) {
                                assist_text.setText("点赞");
                            } else {

                            }

                        } else {
                            Toast.makeText(Details_of_policies_and_regulations.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 10:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        //SHARE_MEDIA.SINA,
                        if (result.getInt("status") == 1) {//成功
                            dataurl = result.getString("videourl");
                            //分享操作
                            if (!TextUtils.isEmpty(dataurl)) {
                                //分享操作
                                //ShareSDK.initSDK(context);
                                final ShareDialog shareDialog = new ShareDialog(context);
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
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setText("医学视频:" + title + "~" + dataurl); //分享文本
                                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                            sinaWeibo.setPlatformActionListener(Details_of_policies_and_regulations.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(title);  //分享标题
                                            sp.setImageUrl(img);//网络图片rul
//                                            sp.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg");//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
//                                            sp.setUrl("https://www.baidu.com/");   //网友点进链接后，可以看到分享的详情
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(Details_of_policies_and_regulations.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(title);  //分享标题
                                            sp.setImageUrl(img);//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(Details_of_policies_and_regulations.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(title);
                                            sp.setImageUrl(img);//网络图片rul
                                            sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(Details_of_policies_and_regulations.this); // 设置分享事件回调
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(title);
                                            sp.setTitleUrl(dataurl); // 标题的超链接
                                            sp.setImageUrl(img);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(dataurl);
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(Details_of_policies_and_regulations.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context
                                                    .getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(dataurl.trim());
                                            Toast.makeText(Details_of_policies_and_regulations.this, "复制成功",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        shareDialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                case 1001:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2001:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3001:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4001:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5001:
                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6001:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
//                    Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.details_of_policies_and_regulations);
        context = this;
        findId();
        getIntentData();
        setVideos();


    }

    private void getIntentData() {
        if (getIntent().getStringExtra("nocollect") != null) {
            if (getIntent().getStringExtra("nocollect").equals("yes")) {
                llBottom.setVisibility(View.GONE);
            }
        }
    }

    public void initPDF(final String url) {
        //获取动态权限
        getPermission();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int myPage = 1;
                    //选择pdf

                   /* pdfView.fromBytes(HttpURLConnHelper.loadByteFromURL(url))
//                .pages(0, 2, 3, 4, 5); // 把0 , 2 , 3 , 4 , 5 过滤掉
                            //是否允许翻页，默认是允许翻页
                            .enableSwipe(true)
                            //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                            .swipeHorizontal(true)
                            //
                            .enableDoubletap(true)
                            //设置默认显示第0页
                            .defaultPage(myPage)
                            //允许在当前页面上绘制一些内容，通常在屏幕中间可见。
//                .onDraw(onDrawListener)
//                // 允许在每一页上单独绘制一个页面。只调用可见页面
//                .onDrawAll(onDrawListener)
                            //设置加载监听
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    pageTv.setText(nbPages + "");
                                    pageTv1.setText(myPage + "/");
                                }
                            })
                            //设置翻页监听
                            .onPageChange(new OnPageChangeListener() {

                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    p = page;
                                    pageTv1.setText(page + "/");
                                }
                            })
                            //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
                            // 首次提交文档后调用。
//                .onRender(onRenderListener)
                            // 渲染风格（就像注释，颜色或表单）
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            // 改善低分辨率屏幕上的渲染
                            .enableAntialiasing(true)
                            // 页面间的间距。定义间距颜色，设置背景视图
                            .spacing(0)
                            .load();*/

//                    MyProgressBarDialogTools.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void getPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(Details_of_policies_and_regulations.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(Details_of_policies_and_regulations.this,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            ActivityCompat.requestPermissions(Details_of_policies_and_regulations.this,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        while ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void findId() {
        super.findId();
        arrow_participate = (LinearLayout) findViewById(R.id.arrow_participate);
        house = (LinearLayout) findViewById(R.id.house);
        assist = (LinearLayout) findViewById(R.id.assist);
        time_text = (TextView) findViewById(R.id.time_text);
        assist_text = (TextView) findViewById(R.id.assist_text);
        house_text = (TextView) findViewById(R.id.house_text);
        see = (TextView) findViewById(R.id.see);
        web_cont = (WebView) findViewById(R.id.web_cont);
        house_img = (ImageView) findViewById(R.id.house_img);
        assist_img = (ImageView) findViewById(R.id.assist_img);
//        pdflayout = (LinearLayout) findViewById(R.id.pdflayout);
//        pdfView = (PDFView) findViewById(R.id.pdfView);
//        pageTv = (TextView) findViewById(R.id.pageTv);
//        pageTv1 = (TextView) findViewById(R.id.pageTv1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        ll_look_pdf = (LinearLayout) findViewById(R.id.ll_look_pdf);

        llBottom = (LinearLayout) findViewById(R.id.id_ll_article_bottom);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/article/" + fid + "/" + aid + ".html";
        super.onPause();
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>html{padding:15px;} body{word-wrap:break-word;font-size:13px;padding:0px;margin:0px} p{padding:0px;margin:0px;font-size:13px;color:#222222;line-height:1.3;} img{padding:0px,margin:0px;max-width:100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public void setVideos() {
        aid = getIntent().getStringExtra("aid");
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainArticleInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", getIntent().getStringExtra("aid"));

                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("医院培训文章详细", result);
//                    MyProgressBarDialogTools.hide();
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

    public void clickcollection(View view) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.collection);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", getIntent().getStringExtra("aid"));

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("医院培训文章收藏", result);
                    MyProgressBarDialogTools.hide();
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

    public void clickdetail(View view) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.detail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", getIntent().getStringExtra("aid"));

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("医院培训文章点赞", result);

                    MyProgressBarDialogTools.hide();

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

    /*
  * 分享视频
  * */
    public void Sharedetail(View view) {
       /* dataurl = "http://www.ccmtv.cn/video/" + aid + "/" + aid + ".html";*/
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", uid);
                        object.put("aid", getIntent().getStringExtra("aid"));
                        object.put("act", URLConfig.videoShare);
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                        Message message = new Message();
                        message.what = 4;
                        message.obj = result;
//                        Log.e("fenxiang", result.toString());
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();

        }

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1001);
        } else if (platform.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2001);
        } else if (platform.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3001);
        } else if (platform.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4001);
        } else if (platform.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(5001);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 7001;
        msg.obj = throwable.getMessage();
//        Log.e("aaarg1", throwable.toString());
//        Log.e("aaarg2", throwable.toString());
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
