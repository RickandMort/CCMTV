package com.linlic.ccmtv.yx.kzbf.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.activity.my.newDownload.LogDownloadListener;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDetailAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDetialCommentAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineComment;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;
import com.linlic.ccmtv.yx.util.IntenetUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 专题指南详情页
 */
public class SpecialGuideDetialActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {
    Context context;
    private List<DbMedicineDetial> tuijianList;
    private List<DbMedicineDetial> listMore = new ArrayList<>();
    private List<DbMedicineComment> commentList;
    private List<DbMedicineComment> commentMore = new ArrayList<>();
    private DbMedicineDetial dbMd;
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private RelativeLayout rl_guide_comment_title, rl_guide_download;
    private RecyclerView guide_tuijian_list, guide_comment_list;
    private ImageView guide_detial_collection_icon, guide_detial_zan_img, guide_detial_download_icon;
    private TextView title, look_num, laud_num, guide_tuijian;
    private TextView guide_posttime, guide_auther, guide_source;
    private TextView guide_more, guide_left_txt;
    private LinearLayout ll_guide_comment_collection, ll_guide_comment_download;
    private String id;
    private String type;
    private String commentId;
    private String is_comment;//是否可以评论 0:可评论 1:不可评论
    private String is_laud;//是否点赞
    private String is_collect;//是否收藏    0:未收藏 1:已收藏
    private String reCommendId;//推荐id
    private String focus_id;
    private String content;
    private String shareUrl;
    private String shareTitle;
    private String sharePicurl;
    private String describe;
    private String upload_file_remind;
    private String file_name, file_url, file_size;
    private int zan_num;
    private int mPosition;
    private int comment_zan_num;
    private EditText et_guide_comment;
    private WebView webView;
    private View guide_view2;
    private MedicineDetailAdapter mdAdapter;
    private MedicineDetialCommentAdapter mdcAdapter;
    private DbMedicineComment dbMc;
    private LinearLayout ll_guide_write_comment;//底部评论
    private VideoModel videoModel;
    private VideoModel videoModel1;
    private Intent intent;
    private List<Progress> fileList;
    private List<Progress> articleList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功

                            tuijianList = new ArrayList<>();
                            JSONObject object = jsonObject.getJSONObject("data");

                            id = object.getString("id");
                            focus_id = object.getString("uid");
                            is_comment = object.getString("is_comment");
                            is_laud = object.getString("is_laud");
                            is_collect = object.getString("is_collect");
                            upload_file_remind = object.getString("upload_file_remind");

                            //显示/隐藏底部评论
                            if (is_comment.equals("1")) {
                                ll_guide_write_comment.setVisibility(View.GONE);
                            } else {
                                ll_guide_write_comment.setVisibility(View.VISIBLE);
                            }
                            //判断是否已收藏
                            if (is_collect.equals("0")) {//变成未收藏
                                guide_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection);
                            } else {//变成已收藏
                                guide_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection2);
                            }
                            //判断是否已点赞
                            if (is_laud.equals("0")) {
                                guide_detial_zan_img.setImageResource(R.mipmap.medicine_zan);
                            } else {
                                guide_detial_zan_img.setImageResource(R.mipmap.medicine_zan2);
                            }

                            int commentCountNum = jsonObject.getInt("comment_count_num");
                            guide_comment.setText("全部评论(" + commentCountNum + ")");

                            //填充页面数据
                            setTextView(object, "title", title);
                            setTextView(object, "look_num", look_num);
                            setTextView(object, "laud_num", laud_num);
                            setTextView(object, "posttime", guide_posttime);
                            setTextView(object, "author", guide_auther);
                            setTextView(object, "source", guide_source);

                            zan_num = Integer.parseInt(object.getString("laud_num"));

                            // webview加载定义的代码，并设定编码格式和字符集。
                            webView.setWebViewClient(new MyWebViewClient());
                            webView.addJavascriptInterface(new MedicineDetialActivity.JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
                            webView.loadDataWithBaseURL(null, object.getString("content") + "<script>  document.body.style.lineHeight = 1.5< /script> \\n< /html>", "text/html", "utf-8", null);
                            //获取推荐列表数据
                            JSONArray dataArray2 = jsonObject.getJSONArray("recommend");
                            if (dataArray2.length() == 0) {//隐藏推荐
                                guide_tuijian.setVisibility(View.GONE);
                                guide_tuijian_list.setVisibility(View.GONE);
                                guide_view2.setVisibility(View.GONE);
                            } else {//显示推荐,加载数据
                                guide_tuijian.setVisibility(View.VISIBLE);
                                guide_tuijian_list.setVisibility(View.VISIBLE);
                                guide_view2.setVisibility(View.VISIBLE);
                                for (int j = 0; j < dataArray2.length(); j++) {
                                    JSONObject customJson2 = dataArray2.getJSONObject(j);
                                    dbMd = new DbMedicineDetial(1);
                                    dbMd.setRecommend_id(customJson2.getString("id"));
                                    dbMd.setRecommend_cid(customJson2.getString("cid"));
                                    dbMd.setRecommend_title(customJson2.getString("title"));
                                    tuijianList.add(dbMd);
                                }
                                listMore.addAll(tuijianList);
                                mdAdapter.notifyDataSetChanged();
                            }
                            //获取评论列表数据
                            commentList = new ArrayList<>();
                            JSONArray dataArray3 = jsonObject.getJSONArray("comment");
                            if (dataArray3.length() == 0) {//隐藏评论
                                rl_guide_comment_title.setVisibility(View.GONE);
                            } else {//显示评论,加载数据
                                rl_guide_comment_title.setVisibility(View.VISIBLE);
                                for (int k = 0; k < dataArray3.length(); k++) {
                                    JSONObject customJson3 = dataArray3.getJSONObject(k);
                                    dbMc = new DbMedicineComment(1);
                                    dbMc.setComment_id(customJson3.getString("id"));
                                    dbMc.setComment_uid(customJson3.getString("uid"));
                                    dbMc.setComment_username(customJson3.getString("username"));
                                    dbMc.setComment_user_img(customJson3.getString("user_img"));
                                    dbMc.setComment_laud_num(customJson3.getString("laud_num"));
                                    dbMc.setComment_addtime(customJson3.getString("addtime"));
                                    dbMc.setComment_content(customJson3.getString("content"));
                                    dbMc.setComment_is_laud(customJson3.getString("is_laud"));
                                    commentList.add(dbMc);
                                }
                                commentMore.addAll(commentList);
                                mdcAdapter.notifyDataSetChanged();
                            }
                            //文件下载数据
                            videoModel = new VideoModel();
                            JSONObject obj = jsonObject.getJSONObject("file_info");
                            if (obj.has("name")) {
                                file_name = obj.getString("name");
                                file_url = obj.getString("url");
                                file_size = obj.getString("size");
                                guide_left_txt.setText("大小：" + file_size);
                                rl_guide_download.setVisibility(View.VISIBLE);
                                ll_guide_comment_download.setVisibility(View.VISIBLE);
                            } else {
                                rl_guide_download.setVisibility(View.INVISIBLE);
                                ll_guide_comment_download.setVisibility(View.GONE);
                            }
                            if (!jsonObject.getString("msg").equals("")) {
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            if (is_collect.equals("1")) {//变成已收藏
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                guide_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection2);
                            } else {//变成未收藏
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                guide_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection);
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {

                        }
                        if (jsonObject.getString("code").equals("0")) {
                            if (jsonObject.getString("is_luad").equals("1")) {//变成已点赞
                                zan_num++;
                                guide_detial_zan_img.setImageResource(R.mipmap.medicine_zan2);
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                guide_detial_zan_img.setClickable(false);
                                Message message = new Message();
                                message.what = LocalApplication.KZBF_ARTICLELAUD_DELAY;
                                message.obj = id;
                                LocalApplication.sendMessage(message);

                            } else {//变成未点赞
                                if (zan_num > 0) {
                                    zan_num--;
                                }
                                guide_detial_zan_img.setImageResource(R.mipmap.medicine_zan);
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                guide_detial_zan_img.setClickable(true);
                            }
                            laud_num.setText(zan_num + "");
                        } else {
                            guide_detial_zan_img.setClickable(true);
                            Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            listMore.clear();
                            commentMore.clear();
                            initData();
                            setValue();
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        //SHARE_MEDIA.SINA,
                        if (result.getString("code").equals("0")) {//成功
                            shareUrl = result.getString("url");
                            shareTitle = result.getString("title");
                            sharePicurl = result.getString("img");
                            describe = result.getString("describe");
                            //分享操作
                            if (!TextUtils.isEmpty(shareUrl)) {
                                //分享操作
                                //ShareSDK.initSDK(SpecialGuideDetialActivity.this);
                                final ShareDialog shareDialog = new ShareDialog(SpecialGuideDetialActivity.this);
                                shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        shareDialog.dismiss();
                                    }
                                });
                                shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                                        if (item.get("ItemText").equals("微博")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setText("医学视频:" + shareTitle + "~" + shareUrl); //分享文本
                                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                            sinaWeibo.setPlatformActionListener(SpecialGuideDetialActivity.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);   //网友点进链接后，可以看到分享的详情
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(SpecialGuideDetialActivity.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);   //网友点进链接后，可以看到分享的详情
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(SpecialGuideDetialActivity.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(shareTitle);
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setTitleUrl(shareUrl);  //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);  //网友点进链接后，可以看到分享的详情
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(SpecialGuideDetialActivity.this); // 设置分享事件回调
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(shareTitle);
                                            sp.setTitleUrl(shareUrl); // 标题的超链接
                                            sp.setImageUrl(sharePicurl);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(shareUrl);
                                            sp.setText(describe);
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(SpecialGuideDetialActivity.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(shareUrl.trim());
                                            Toast.makeText(SpecialGuideDetialActivity.this, "复制成功",
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
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            mPosition = msg.arg1;
                            comment_zan_num = Integer.parseInt(commentMore.get(mPosition).getComment_laud_num());
                            if (jsonObject.getString("is_laud").equals("1")) {//变成已点赞
                                comment_zan_num++;
                                commentMore.get(mPosition).setComment_is_laud("1");
                                commentMore.get(mPosition).setComment_laud_num(comment_zan_num + "");
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {//变成未点赞
                                if (comment_zan_num != 0) {
                                    comment_zan_num--;
                                }
                                commentMore.get(mPosition).setComment_is_laud("0");
                                commentMore.get(mPosition).setComment_laud_num(comment_zan_num + "");
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            mdcAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 7:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            if (jsonObject.getString("is_upload").equals("yes")) {
                                DownloadArticle();
                                type = "1";
                                MinusIntegral();
                                Toast.makeText(context, "文章已添加到下载列表", Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("is_upload").equals("no")) {
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 1001:
//                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 2001:
//                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 3001:
//                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 4001:
//                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 5001:
//                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 6001:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
                    Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private TextView guide_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_guide_detial);
        context = this;

        //下载路径
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/article/");
        //允许同时进行下载的任务数
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        //从数据库中恢复数据
        List<Progress> progressList = com.lzy.okgo.db.DownloadManager.getInstance().getAll();
        OkDownload.restore(progressList);
        checkSDCardPermission();

        initView();
        initData();
        setValue();
    }

    private void initView() {
        guide_tuijian_list = (RecyclerView) findViewById(R.id.guide_tuijian_list);
        guide_comment_list = (RecyclerView) findViewById(R.id.guide_comment_list);
        guide_detial_zan_img = (ImageView) findViewById(R.id.guide_detial_zan_img);
        title = (TextView) findViewById(R.id.guide_detial_item_title);
        look_num = (TextView) findViewById(R.id.guide_detial_eye_num);
        laud_num = (TextView) findViewById(R.id.guide_detial_zan_num);
        webView = (WebView) findViewById(R.id.guide_webView);
        guide_tuijian = (TextView) findViewById(R.id.guide_tuijian);
        rl_guide_comment_title = (RelativeLayout) findViewById(R.id.rl_guide_comment_title);

        guide_more = (TextView) findViewById(R.id.guide_more);
        guide_detial_collection_icon = (ImageView) findViewById(R.id.guide_detial_collection_icon);
        ll_guide_write_comment = (LinearLayout) findViewById(R.id.ll_guide_write_comment);
        guide_detial_download_icon = (ImageView) findViewById(R.id.guide_detial_download_icon);
        rl_guide_download = (RelativeLayout) findViewById(R.id.rl_guide_download);
        guide_view2 = findViewById(R.id.guide_view2);
        guide_posttime = (TextView) findViewById(R.id.guide_posttime);
        guide_auther = (TextView) findViewById(R.id.guide_auther);
        guide_source = (TextView) findViewById(R.id.guide_source);
        ll_guide_comment_collection = (LinearLayout) findViewById(R.id.ll_guide_comment_collection);
        et_guide_comment = (EditText) findViewById(R.id.et_guide_comment);
        guide_left_txt = (TextView) findViewById(R.id.guide_left_txt);

        ll_guide_comment_download = (LinearLayout) findViewById(R.id.ll_guide_comment_download);
        guide_comment = (TextView) findViewById(R.id.guide_comment);
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trendsInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("看看指南详细数据", result);

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

    private void initData() {
        id = getIntent().getStringExtra("id");
//        focus_id = getIntent().getStringExtra("uid");

        guide_more.setOnClickListener(this);
        guide_detial_zan_img.setOnClickListener(this);
        guide_detial_download_icon.setOnClickListener(this);
        rl_guide_download.setOnClickListener(this);
        ll_guide_comment_collection.setOnClickListener(this);
        ll_guide_comment_download.setOnClickListener(this);

        WebSettings webSettings = webView.getSettings();//获取webview设置属性
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
//        webSettings.setBuiltInZoomControls(true); // 显示放大缩小
//        webSettings.setSupportZoom(true); // 可以缩放
        // 添加js交互接口类，并起别名 imagelistner

        guide_tuijian_list.setLayoutManager(new LinearLayoutManager(context));
        mdAdapter = new MedicineDetailAdapter(context, listMore);
        guide_tuijian_list.setAdapter(mdAdapter);
        //相关推荐列表点击事件
        mdAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                reCommendId = listMore.get(position).getRecommend_id();
                if (listMore.get(position).getRecommend_cid().equals("1")) {//1药讯 2指南 3文献
                    intent = new Intent(context, MedicineDetialActivity.class);
                    intent.putExtra("id", reCommendId);
                    intent.putExtra("uid", focus_id);
                } else if (listMore.get(position).getRecommend_cid().equals("2")) {
                    intent = new Intent(context, SpecialGuideDetialActivity.class);
                    intent.putExtra("id", reCommendId);
                    intent.putExtra("uid", focus_id);
                } else if (listMore.get(position).getRecommend_cid().equals("3")) {
                    intent = new Intent(context, LiteratureDetialActivity.class);
                    intent.putExtra("id", reCommendId);
                    intent.putExtra("uid", focus_id);
                }
                startActivity(intent);
                finish();
            }
        });

        guide_comment_list.setLayoutManager(new LinearLayoutManager(context));
        mdcAdapter = new MedicineDetialCommentAdapter(context, commentMore);
        guide_comment_list.setAdapter(mdcAdapter);

        et_guide_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    setComment();
                    et_guide_comment.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        mdcAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                commentId = commentMore.get(position).getComment_id();
                setCommentPraise(position);
            }
        });
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
    private void setTextView(JSONObject jsonObject, String str, TextView textView) throws JSONException {
        if (!jsonObject.getString(str).equals("")) {
            textView.setText(jsonObject.getString(str));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_more://跳转更多评论
                Intent intent = new Intent(context, MoreCommentActivity.class);
                intent.putExtra("aid", id);
                startActivity(intent);
                break;
            case R.id.ll_guide_comment_collection://收藏/取消收藏
                if (is_collect.equals("0")) {
                    is_collect = "1";
                } else {
                    is_collect = "0";
                }
                setCollection();
                break;
            case R.id.guide_detial_zan_img://赞/取消赞
                guide_detial_zan_img.setClickable(false);
                if (is_laud.equals("0")) {
                    is_laud = "1";
                } else {
                    is_laud = "0";
                }
                setPraise();
                break;
            case R.id.ll_guide_comment_download:
                fileList = DownloadManager.getInstance().getAll();
                articleList = new ArrayList<>();
                IsDownload();
                break;
            case R.id.guide_detial_download_icon:
                fileList = DownloadManager.getInstance().getAll();
                articleList = new ArrayList<>();
                IsDownload();
                break;
            case R.id.rl_guide_download:
                fileList = DownloadManager.getInstance().getAll();
                articleList = new ArrayList<>();
                IsDownload();
                break;
        }
    }

    private void IsDownload() {
        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).extra2 != null) {
                VideoModel model = (VideoModel) fileList.get(i).extra2;
                if (model.getType().equals("article")) {
                    articleList.add(fileList.get(i));
                }
            }
        }
        if (articleList.size() > 0) {
            for (int j = 0; j < articleList.size(); j++) {
                videoModel1 = (VideoModel) articleList.get(j).extra2;
                if (videoModel1.getArticleUrl().equals(file_url)) {
                    Toast.makeText(context, "您已下载了该文章", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog();
                    /*DownloadArticle();
                    Toast.makeText(context, "文章已添加到下载列表", Toast.LENGTH_SHORT).show();*/
                    break;
                }
            }
        } else {
            showDialog();
            /*DownloadArticle();
            Toast.makeText(context, "文章已添加到下载列表", Toast.LENGTH_SHORT).show();*/
        }
    }

    //收藏/取消收藏
    private void setCollection() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleCollect);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看指南收藏数据", result);

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

    //赞/取消赞
    private void setPraise() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleLaud);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看点赞数据", result);

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

    //发布评论
    private void setComment() {
        content = et_guide_comment.getText().toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.postComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("content", content);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论数据", result);

                    Message message = new Message();
                    message.what = 4;
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
    * 文章分享
    * */
    public void ShareTitle(View view) {
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
                        object.put("id", id);
                        object.put("act", URLConfig.share);
                        String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, object.toString());
//                        Log.e("看看分享数据", result);

                        Message message = new Message();
                        message.what = 5;
                        message.obj = result;
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
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(6001);
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7001;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1001);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2001);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3001);
        } else if (arg0.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4001);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(5001);
        }
    }

    /**
     * name：分享成功后，检查是否需要增加积分，一天十次机会，每次增加一个积分
     * author：Larry
     * data：2017/5/25 10:16
     */
    private void CheckIntegral() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.shareSuccess);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);

                    Message message = new Message();
                    message.what = 7;
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

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
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
    }

    // 新下载功能
    public void DownloadArticle() {
        switch (IntenetUtil.getNetworkState(context)) {
            case IntenetUtil.NETWORN_NONE:
                Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
                break;
            case IntenetUtil.NETWORN_2G:
                Toast.makeText(context, "当前为2G网络", Toast.LENGTH_SHORT).show();
                downloadAritle();
                break;
            case IntenetUtil.NETWORN_3G:
                Toast.makeText(context, "当前为3G网络", Toast.LENGTH_SHORT).show();
                downloadAritle();
                break;
            case IntenetUtil.NETWORN_4G:
                Toast.makeText(context, "当前为4G网络", Toast.LENGTH_SHORT).show();
                downloadAritle();
                break;
            default:
                downloadAritle();
                break;
        }
    }

    private void downloadAritle() {
        videoModel.setArticleName(file_name);
        videoModel.setArticleSize(file_size);
        videoModel.setArticleUrl(file_url);
        videoModel.setType("article");
        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
        GetRequest<File> request = OkGo.<File>get(file_url)//
                .headers("ccc", "111")//
                .params("ddd", "222");
        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
        OkDownload.request(file_url, request)//
                .fileName(file_name)
                .extra2(videoModel)
                .save()//
                .register(new LogDownloadListener())//
                .start();
    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    //评论赞/取消赞
    private void setCommentPraise(final int position) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.laudComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("id", commentId);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论点赞数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 6;
                    message.arg1 = position;
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

    private void showDialog() {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.dialog_integral, null);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv_confirm = (TextView) view1.findViewById(R.id.tv_sky_confirm);
        TextView tv_title = (TextView) view1.findViewById(R.id.tv_sky_title);
        TextView tv_canal = (TextView) view1.findViewById(R.id.tv_sky_canal);
        tv_title.setText(upload_file_remind);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "0";
                MinusIntegral();
                dialog.dismiss();
            }
        });
        tv_canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void MinusIntegral() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.selUserIntegral);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("type", type);

//                    Log.e("看看下载数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.SkyvisitDownload, obj.toString());
//                    Log.e("看看下载数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 8;
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
