package com.linlic.ccmtv.yx.activity.my.book;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.cashier.PaymentActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.ArticleCommentEntity;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.entity.Video_menu_comment_entry2;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_abstract;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_expert;
import com.linlic.ccmtv.yx.activity.home.VideoService;
import com.linlic.ccmtv.yx.activity.home.util.LocalConstants;
import com.linlic.ccmtv.yx.activity.my.Get_Free_Integral;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.activity.my.newDownload.LogDownloadListener;
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
import com.linlic.ccmtv.yx.adapter.Video_Experts_GridAdapter;
import com.linlic.ccmtv.yx.adapter.Videos_expertAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.floatWindow.widget.FloatLayout;
import com.linlic.ccmtv.yx.fragment.DetailFragment;
import com.linlic.ccmtv.yx.holder.BaseListAdapter2;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.receiver.NetBroadcastReceiver;
import com.linlic.ccmtv.yx.util.CcmtvVideoPlayerController;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.linlic.ccmtv.yx.widget.HorizontalListView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiao.nicevideoplayer.Clarity;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cc.android.sdk.DownloadManager;
import cn.cc.android.sdk.VideoManager;
import cn.cc.android.sdk.callback.DownloadListener;
import cn.cc.android.sdk.impl.DownloadData;
import cn.cc.android.sdk.impl.SimplePlayerProperty;
import cn.cc.android.sdk.impl.TransCodingInformation;
import cn.cc.android.sdk.util.SDKConstants;
import cn.cc.android.sdk.view.VideoView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.wolfspider.autowraplinelayout.AutoWrapLineLayout;

/**
 * name:视频播放
 * author:Tom
 * 2016-3-2下午7:02:48
 */
public class Video_book_Five extends BaseActivity implements DetailFragment.Callback, OnGestureListener, PlatformActionListener, OnClickListener, NetBroadcastReceiver.NetEvevt {
    public static String payfor = "Video_book_Five";
    private static int TIME_LONG = 300;
    private static final int COMMENT_LIMIT = 10;//每页显示多少评论
    private LinearLayout list_buttom, arrow_back2, video_comment_dilong, layout_praise, networkLinearLayout, synopsis_layout_web;
    private ProgressReceiver mProgressRecevier;
    Context context;
    private MyGridView video_Experts_list, video_Experts_list2;
    private AutoWrapLineLayout auto_wrap_line_layout;
    private TextView activity_title_name, zambia_num, video_menu_comment_item_id;
    private List<Map<String, Object>> relevants = new ArrayList<Map<String, Object>>();//最新
    private List<Video_menu_expert> experts = new ArrayList<Video_menu_expert>();//专家
    private List<Video_menu_expert> experts2 = new ArrayList<Video_menu_expert>();//专家
    private Video_Experts_GridAdapter video_experts_gridAdapter, video_experts_gridAdapter2;
    private LinearLayout promptLinearLayout;
    public static int payBoolean = 0;
    public static String payBoolean_S = "";
    private VideoService mVideoService;
    public static VideoView mVideoView;
    private static TransCodingInformation mInformation;
    private ArrayList<SimplePlayerProperty> mPlayerList;
    private DefinitionDialog mDialog;
    private LinearLayout downloadVideo_layout;//下载layout
    private TextView downloadVideo_High_definition;//下载高清
    private TextView downloadVideo_Biao_z_clear;//下载标清
    private TextView downloadVideo_flow;//下载标清
    Dialog dialog;
    private View layout_view;
    private static final String LOG_TAG = Video_book_Five.class.getSimpleName();
    //视频aid
    public static String aid, fid, hits, money, down_num, digg_num, picurl, zanflg, downflg, collectionflg, my_video_id;
    //视频播放总时长
    public static String vtime = "00:00:00";
    //当前登录账户积分数量
    private int userMoney = 0, positions, votenum, NETWORK = 0;
    public static int currPage = 1, onfling = 1;
    //是否是投票
    private boolean isVote;
    public static NetBroadcastReceiver.NetEvevt evevt;
    //视频第一帧图,视频播放按钮/收藏,下载,/赞
    private ImageView video_play_img, play_img, video_icon_collection_img, video_zambia_img, MyImg, MyImg2, video_header_comment_img, icon_x_comment;
    //视频播放器Layout
    private FrameLayout video_play_layout;
    private TextView promptText1, promptText2, tv_votenum, video_menu_comment_edittext_f, video_play_count_text, video_no_zambia_Text, networkText1, networkText2;
    //投票数量
    private Button promptopen, promptopen_vip;
    private String mvurl, videoplaystyle, smvpurl, video_title, dataurl, videoaId, videoStartId, isFortyEight, videoClass, personalmoney, videopaymoney, vipflg_str, subject, defaultDefinition;
    private WebView video_abstract;
    public static Video_book_Five isFinish;
    private LinearLayout related_drugs;
    private LinearLayout video_comment_list;
    private MyListView video_menu_comment_list;// 评论数据加载
    private MyListView video_menu_relevant_list;// 相关数据加载
    private MyListView item_MyComment;// 单个评论 全部
    private CustomImageView video_menu_comment_item_img;//单个评论全部  顶层用户图片
    private TextView video_menu_comment_item_name; //单个评论全部 顶层用户名称
    private TextView video_menu_comment_item_times; //单个评论全部 顶层发布时间
    private TextView video_menu_comment_item_cont; //单个评论全部 顶层发布内容
    private TextView video_menu_comment_reply; //单个评论全部 顶层 回复他
    private LinearLayout item_comment_layout; //单个评论全部 Layout
    public List<Video_menu_comment_entry2> datalist = new ArrayList<Video_menu_comment_entry2>();
    public List<Video_menu_comment_entry2> datalist2 = new ArrayList<Video_menu_comment_entry2>();
    public List<ArticleCommentEntity> commentList = new ArrayList<ArticleCommentEntity>();
    public List<ArticleCommentEntity.ChildrenBean> chridCommentList = new ArrayList<ArticleCommentEntity.ChildrenBean>();
    BaseListAdapter2 baseListAdapter;
    BaseListAdapter2 baseListAdapter2;
    BaseListAdapter2 baseListAdapter3;
    private boolean isNoMore = false, NETWORK_MOBILE_PAY = false;
    private FrameLayout video_frame;
    private long playprogress;
    private long myPlayprogress;
    private String relevantContent = "";
    private String downloadUrl;
    private LinearLayout video_comment_dilong2;
    private LinearLayout video_header_comment_layout, video_icon_collection_layout;
    private LinearLayout video_icon_download_layout, layout_vote;
    private LinearLayout videoexpert;
    private LinearLayout ll_comment_empty;
    private LinearLayout float_layout;
    private LinearLayout synopsis_layout;
    private TextView tv_say_something, video_title2;
    private TextView video_menu_comment_edittext_ff;
    private ImageView icon_x;
    private TextView qbcomment, zambia_text, video_play_count;
    private int flag = 0;
    private NestedScrollView nestedScrollView;
    private boolean isdowen = false;
    private TextView synopsis;
    private List<Progress> apks;
    private List<Progress> video = new ArrayList<>();
    private static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public VideoModel videoModel = new VideoModel();
    public VideoModel videoModel1;
    private String float_url = "";
    private ImageView video_download_img;
    private HorizontalListView horizontalListView;
    private Videos_expertAdapter home_videos_gridAdapter;
    public List<Home_Video> videodata = new ArrayList<>();

    private LinearLayout expert_layout;//专家详细信息层
    private TextView expert_name;//专家名称
    private ImageView expert_icon_x;//专家层关闭按钮
    private CustomImageView video_expert_img;//专家图片
    private TextView video_expert_Title;//专家职称
    private TextView video_hospital_expert, comment_num;//专家医院
    private TextView video_expert_cont;//专家介绍

    private String getAllReplyData_id = "";
    private String getAllReplyData_strid = "";
    private int getAllReplyData_position = 0;
    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;
    private InputMethodManager imm;
    // 声明平移动画
    private TranslateAnimation animation;

    public static NiceVideoPlayer video_view2;
    List<Clarity> clarities = new ArrayList<>();
    private CcmtvVideoPlayerController controller;

    private Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void handleMessage(Message msg) {
            //全屏后底部点赞view
            switch (msg.what) {
                case 1:
                    try {
                        MyProgressBarDialogTools.hide();
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { // 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            //存贮对象声明(把视频ID存起来)
                            //  SharedPreferencesTools.saveVideoId(context, dataArray.getString("aid"));//存储ID
                            aid = dataArray.getString("aid");//视频aid
                            fid = dataArray.getString("fid");//视频fid
                            hits = dataArray.getString("hits");            //视频浏览量
                            money = dataArray.getString("money");          //视频播放收取积分数量
                            userMoney = dataArray.getInt("userMoney");     //用户账号积分
                            down_num = dataArray.getString("down_num");    //视频下载次数
                            digg_num = dataArray.getString("digg_num");    //视频点赞数量
                            picurl = dataArray.get("picurl").toString();        //视频图片
                            zanflg = dataArray.get("zanflg").toString();        //为1的时候代表该用户已经赞过该视频为2的时候表示该用户没有赞过该视频
                            downflg = dataArray.get("downflg").toString();      //为1的时候代表该用户已经下载过该视频为2的时候表示该用户没有下载过该视频
                            collectionflg = dataArray.get("collectionflg").toString();//为1的时候代表该用户已经收藏过该视频为2的时候表示该用户没有收藏过该视频
                            my_video_id = dataArray.getString("my_video_id");      //该视频对应的石山id
                            mvurl = dataArray.getString("mvurl");                  //本地视频路径（部分视频为分段视频及多个本地路径）
                            videoplaystyle = dataArray.getString("videoplaystyle");//播放器选择标识
                            smvpurl = dataArray.getString("smvpurl");              // smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                            video_title = dataArray.getString("title");
                            isFortyEight = dataArray.getString("isFortyEight");    //1）.如果为收费视频 1代表48小时内已经支付过视频的费用可直接观看不需要再次支付如果为0需要再次支付
                            // 2）.如果为收积分观看1代表48小时内已经支付过视频的积分可直接观看不需要再次支付如果为0需要再次支付
                            videoClass = dataArray.getString("video_class");
                            personalmoney = dataArray.getString("personalmoney");//用户收银台余额
                            videopaymoney = dataArray.getString("videopaymoney");//视频收费的金额（如果不为0代表改视频为收费）
                            vipflg_str = dataArray.getString("vipflg_str");//产品编号 （收费视频必传）
                            subject = dataArray.getString("subject");//产品编号 （收费视频必传）
                            flag = dataArray.getInt("flag");// 为3代表VIP视频  其他值不用管
                            vtime = dataArray.has("vtime") ? dataArray.getString("vtime") : "";//视频总时长
                            float_url = dataArray.has("audiourl") ? dataArray.getString("audiourl") : "";//视频总时长
                            video_title2.setText(video_title);
                            zambia_num.setText(digg_num);
                            synopsis.setText(Html.fromHtml(dataArray.getString("content")));
                            video_play_count.setText(hits);
                            clarities.clear();


                            JSONObject smvpurl_json = dataArray.getJSONObject("smvpurl");
                            //流畅 石山视频路径
                            String fluentFile = smvpurl_json.getString("fluentFile");
                            //标清石山视频路径
                            String SDFile = smvpurl_json.getString("SDFile");
                            //高清石山视频路径
                            String hdefinitionFile = smvpurl_json.getString("hdefinitionFile");
                            if (fluentFile.trim().length() > 0) {
                                clarities.add(new Clarity("标清", "270P", fluentFile.trim().toString()));
                            }
                            if (SDFile.trim().length() > 0) {
                                clarities.add(new Clarity("高清", "480P", SDFile.trim()));
                            }
                            if (hdefinitionFile.trim().length() > 0) {
                                clarities.add(new Clarity("超清", "720P", hdefinitionFile.trim()));
                            }
                            if (fluentFile.trim().length() < 1 && SDFile.trim().length() < 1 && hdefinitionFile.trim().length() < 1) {
                                clarities.add(new Clarity("标清", "270P", dataArray.getJSONArray("mvurl").get(0).toString().trim()));
                            }


                            initNiceVideo();
                            if (dataArray.getString("keywords").trim().length() < 1) {
                                auto_wrap_line_layout.setVisibility(View.GONE);
                            } else {
                                auto_wrap_line_layout.setVisibility(View.VISIBLE);
                                String[] keywords = dataArray.getString("keywords").split(" ");
                                for (String str : keywords) {
                                    View home_center = LayoutInflater.from(context).inflate(R.layout.auto_wrap_line_layout_item, null);
                                    TextView _item_text = (TextView) home_center.findViewById(R.id._item_text);
                                    _item_text.setText(str);
                                    auto_wrap_line_layout.addView(_item_text);

                                    TextView textView = new TextView(context);
                                    textView.setText(" ");
                                    auto_wrap_line_layout.addView(textView);
                                }
                            }
                            if (flag == 5) {
                                video_download_img.setImageResource(R.mipmap.video_download2);
                            } else {
                                video_download_img.setImageResource(R.mipmap.video_download1);
                            }
                            //判断该视频是否有音频文件
                            if (dataArray.has("audiourl") && dataArray.getString("audiourl").trim().length() > 0) {
                                //显示音频入口
                                float_url = dataArray.getString("audiourl");
                                float_layout.setVisibility(View.VISIBLE);
                            } else {
                                //隐藏音频入口
                                float_layout.setVisibility(View.GONE);
                            }

                            if (dataArray.getString("relevantContent") != null && dataArray.getString("relevantContent").length() > 0) {
                                related_drugs.setVisibility(View.VISIBLE);
                                relevantContent = dataArray.getString("relevantContent");
                            } else {
                                related_drugs.setVisibility(View.GONE);
                            }
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            if (videopaymoney.equals("0")) {            //videopaymoney视频收费的金额（如果不为0代表改视频为收费）
                                payBoolean = 0;
                            } else {
                                payBoolean = 1;
                            }
                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(picurl), video_play_img);
                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(SharedPreferencesTools.getStricon(Video_book_Five.this)), MyImg);
                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(SharedPreferencesTools.getStricon(Video_book_Five.this)), MyImg2);
                            initFragment();
                            if (video_title != null && video_title.length() > 0) {
                                //图文混排
                                Drawable drawable = null;
                                if (videopaymoney.equals("0")) {
                                    //非收费
                                    if (flag == 3) {
                                        //VIP
                                        drawable = getResources().getDrawable(R.mipmap.video_vip);
                                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());//这里后两位不要填写int类参数，否则会出现在大屏手机上显示不整齐的情况
                                        ImageSpan is = new ImageSpan(drawable);
                                        String space = " ";
                                        video_title = space + video_title;
                                        int strLength = video_title.length();
                                        SpannableString ss = new SpannableString(video_title);
                                        ss.setSpan(is, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                        activity_title_name.setText(ss.subSequence(0, strLength));
                                    } else {
                                        if (!money.equals("0")) {
                                            //积分
                                            drawable = getResources().getDrawable(R.mipmap.video_integral);
                                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());//这里后两位不要填写int类参数，否则会出现在大屏手机上显示不整齐的情况
                                            ImageSpan is = new ImageSpan(drawable);
                                            String space = " ";
                                            video_title = space + video_title;
                                            int strLength = video_title.length();
                                            SpannableString ss = new SpannableString(video_title);
                                            ss.setSpan(is, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                            activity_title_name.setText(ss.subSequence(0, strLength));
                                        } else {
                                            activity_title_name.setText(video_title);
                                        }
                                    }
                                } else {
                                    //收费
                                    drawable = getResources().getDrawable(R.mipmap.video_charge);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());//这里后两位不要填写int类参数，否则会出现在大屏手机上显示不整齐的情况
                                    ImageSpan is = new ImageSpan(drawable);
                                    String space = " ";
                                    video_title = space + video_title;
                                    int strLength = video_title.length();
                                    SpannableString ss = new SpannableString(video_title);
                                    ss.setSpan(is, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                                    activity_title_name.setText(ss.subSequence(0, strLength));
                                }

                            } else {
                                activity_title_name.setText(R.string.activity_title_video);
                            }
//                            initPlayer();
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3://赞
                    video_zambia_img.setClickable(true);
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            video_zambia_img.setTag("cancelZan");
                            video_zambia_img.setImageResource(R.mipmap.video_paly3_21);
                            digg_num = digg_num.indexOf("万") > 0 ? digg_num : (Integer.parseInt(digg_num) + 1) + "";
                            zambia_num.setText(digg_num);
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this,
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4://取消赞
                    video_zambia_img.setClickable(true);
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            video_zambia_img.setTag("zan");
                            video_zambia_img.setImageResource(R.mipmap.video_paly3_05);

                            digg_num = digg_num.indexOf("万") > 0 ? digg_num : (Integer.parseInt(digg_num) - 1) + "";
                            zambia_num.setText(digg_num);
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        video_icon_collection_img.setClickable(true);
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            video_icon_collection_img.setImageResource(R.mipmap.video_paly3_20);
                            video_icon_collection_img.setTag("cancelCollection");
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        video_icon_collection_img.setClickable(true);
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            video_icon_collection_img.setImageResource(R.mipmap.video_paly3_07);
                            video_icon_collection_img.setTag("collection");
                            Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7://下载
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            TextView video_download_text = (TextView) findViewById(R.id.video_download_text);
                            //   DownloadVideo();
                            //下载逻辑
                            JSONObject jsonObject = new JSONObject(smvpurl);
                            // 下载功能
//                                Down.MyAlert(context, fluentFile, SDFile, hdefinitionFile, video_title, aid, picurl, video_download_text, down_num, videoClass);
                            if ((jsonObject.has("fluentFile") && !jsonObject.getString("fluentFile").isEmpty())
                                    || (jsonObject.has("SDFile") && !jsonObject.getString("SDFile").isEmpty())
                                    || (jsonObject.has("hdefinitionFile") && !jsonObject.getString("hdefinitionFile").isEmpty())) {
                                //流畅 石山视频路径
                                String fluentFile = jsonObject.getString("fluentFile");
                                //标清石山视频路径
                                String SDFile = jsonObject.getString("SDFile");
                                //高清石山视频路径
                                String hdefinitionFile = jsonObject.getString("hdefinitionFile");
                                //下载前判断是否为2G，3G，4G网络
                                judgeNetWork(fluentFile, SDFile, hdefinitionFile);

                            } else {
                                Toast.makeText(context, "该视频暂不支持下载，如有需要请登录官网或联系客服！谢谢！", Toast.LENGTH_SHORT).show();
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {  // 成功
                            //判断是否是收费视频
                            if (!"0".equals(videopaymoney)) { //是收费视频
                                payBoolean = 0;
                            }
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                video_play_layout.setVisibility(View.GONE);
                                promptLinearLayout.setVisibility(View.GONE);
                                video_frame.setVisibility(View.VISIBLE);
                                // Log.e("查看", "第一步先查看石山视频ID 是否为空");
                                //第一步先查看石山视频ID 是否为空
                                if (my_video_id != null && my_video_id.length() > 0) {
                                    //不为空 则初始化石山视频播放器
                                    //  Log.e("查看", "不为空 则初始化石山视频播放器");
                                    try {
                                        initPlayer();
                                    } catch (Exception e) {
                                        //   Log.e("查看", " 初始化失败");
                                        //初始化失败 第一步 检查本地视频是否为空
                                        e.printStackTrace();
                                        initPlayer2();
                                    }
                                } else {
                                    initPlayer2();
                                }
                            } else if ("ccmtv".equals(videoplaystyle)) {
                                initPlayer2();
                            } else {
                                Toast.makeText(context, "视频资源正在修复，请稍候再试！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (result.getInt("status") == 5) {
                            promptText1.setText("本次观看需要扣除" + money + "积分，您的积分已不足");
                            promptText2.setText("是否购买积分？");
                            promptopen.setText("购买积分");
                            promptopen_vip.setVisibility(View.VISIBLE);
                            promptopen.setVisibility(View.VISIBLE);
                        } else if (result.getInt("status") == 6) {
                            //收费视频需要收费  此地方是试看
                            payBoolean = 1;
                            if (videoplaystyle.equals("smvp")) {
                                video_play_layout.setVisibility(View.GONE);
                                promptLinearLayout.setVisibility(View.GONE);
                                video_frame.setVisibility(View.VISIBLE);
                                //Log.e("查看", "第一步先查看石山视频ID 是否为空");
                                //第一步先查看石山视频ID 是否为空
                                if (my_video_id != null && my_video_id.length() > 0) {
                                    //不为空 则初始化石山视频播放器
                                    // Log.e("查看", "不为空 则初始化石山视频播放器");
                                    try {
                                        initPlayer();
                                    } catch (Exception e) {
                                        //     Log.e("查看", " 初始化失败");
                                        //初始化失败  第一步 检查本地视频是否为空
                                        e.printStackTrace();
                                        //显示 购买层
                                        promptText1.setText("本次观看需要扣除" + videopaymoney + "元，您的余额为" + personalmoney + "元。");
                                        promptText2.setText("48小时内可重复观看当前视频。");
                                        promptopen.setText("余额支付");
                                        promptopen_vip.setText("在线支付");
                                        promptopen_vip.setVisibility(View.VISIBLE);
                                        promptopen.setVisibility(View.VISIBLE);
                                        promptLinearLayout.setVisibility(View.VISIBLE);
                                        video_play_layout.setVisibility(View.VISIBLE);
                                        video_frame.setVisibility(View.GONE);
                                    }
                                } else {
                                    //显示 购买层
                                    promptText1.setText("本次观看需要扣除" + videopaymoney + "元，您的余额为" + personalmoney + "元。");
                                    promptText2.setText("48小时内可重复观看当前视频。");
                                    promptopen.setText("余额支付");
                                    promptopen_vip.setText("在线支付");
                                    promptopen_vip.setVisibility(View.VISIBLE);
                                    promptopen.setVisibility(View.VISIBLE);
                                    promptLinearLayout.setVisibility(View.VISIBLE);
                                    video_play_layout.setVisibility(View.VISIBLE);
                                    video_frame.setVisibility(View.GONE);
                                }
                            } else if ("ccmtv".equals(videoplaystyle)) {
                                //显示 购买层
                                promptText1.setText("本次观看需要扣除" + videopaymoney + "元，您的余额为" + personalmoney + "元。");
                                promptText2.setText("48小时内可重复观看当前视频。");
                                promptopen.setText("余额支付");
                                promptopen_vip.setText("在线支付");
                                promptopen_vip.setVisibility(View.VISIBLE);
                                promptopen.setVisibility(View.VISIBLE);
                                promptLinearLayout.setVisibility(View.VISIBLE);
                                video_play_layout.setVisibility(View.VISIBLE);
                                video_frame.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(context, "视频资源正在修复，请稍候再试！", Toast.LENGTH_SHORT).show();
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                                //ShareSDK.initSDK(Video_book_Five.this);
                                final ShareDialog shareDialog = new ShareDialog(Video_book_Five.this);
                                shareDialog.setCancelButtonOnClickListener(new OnClickListener() {

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
                                            sp.setText("医学视频:" + video_title + "~" + dataurl); //分享文本
                                            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                            sinaWeibo.setPlatformActionListener(Video_book_Five.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(video_title);  //分享标题
                                            sp.setImageUrl(picurl);//网络图片rul
//                                            sp.setImageUrl("http://f1.webshare.mob.com/dimgs/1c950a7b02087bf41bc56f07f7d3572c11dfcf36.jpg");//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
//                                            sp.setUrl("https://www.baidu.com/");   //网友点进链接后，可以看到分享的详情
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(Video_book_Five.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(video_title);  //分享标题
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(Video_book_Five.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(video_title);
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(Video_book_Five.this); // 设置分享事件回调
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(video_title);
                                            sp.setTitleUrl(dataurl); // 标题的超链接
                                            sp.setImageUrl(picurl);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(dataurl);
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(Video_book_Five.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(dataurl.trim());
                                            Toast.makeText(Video_book_Five.this, "复制成功", Toast.LENGTH_LONG).show();
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
                case 11:                                        //投票
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            String str_num = tv_votenum.getText().toString();
                            int a = Integer.parseInt(str_num.substring(0, str_num.indexOf("票")));
                            tv_votenum.setText(a + 1 + "票");
                            votenum = a + 1;
                        } else {
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    try {
                        if (videoplaystyle.equals("smvp")) {
                            promptLinearLayout.setVisibility(View.GONE);
                            video_play_layout.setVisibility(View.GONE);
                            video_frame.setVisibility(View.VISIBLE);
                            //第一步先查看石山视频ID 是否为空
                            if (my_video_id != null && my_video_id.length() > 0) {
                                //不为空 则初始化石山视频播放器
                                try {
                                    initPlayer();
                                } catch (Exception e) {
                                    // 初始化失败
                                    // 第一步 检查本地视频是否为空
                                    e.printStackTrace();
                                    initPlayer2();
                                }
                            } else {
                                initPlayer2();
                            }
                        } else if (videoplaystyle.equals("ccmtv")) {
                            initPlayer2();
                        } else
                            Toast.makeText(context, "视频资源正在修复，请稍候再试！", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 14:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            String uid = SharedPreferencesTools.getUidToLoginClose(context);
                            //跳转页面
                            Intent intent = new Intent(Video_book_Five.this, Video_book_Five.class);
                            intent.putExtra("aid", dataArray.getString("aid"));
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                            Video_book_Five.isFinish.finish();
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 16://评论列表

                    if (currPage == 1 && commentList.size() < 1) {
                        video_comment_list.setVisibility(View.GONE);
                    } else {
                        video_comment_list.setVisibility(View.VISIBLE);
                    }
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        LogUtil.e("mason","评论列表"+result);
                        if (result.getInt("status") == 1 && result.has("data")) {
                            JSONObject data = result.getJSONObject("data");
                            if (!data.isNull("count")) {
                                qbcomment.setText("全部评论（" + data.getString("count") + "）");
                                comment_num.setText(data.getString("count"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    baseListAdapter.notifyDataSetChanged();
                    baseListAdapter3.notifyDataSetChanged();
                    break;
                case 17://添加新评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
                            video_comment_list.setVisibility(View.VISIBLE);
                            ll_comment_empty.setVisibility(View.GONE);//评论成功，隐藏无评论界面
                            video_menu_comment_list.setVisibility(View.VISIBLE);
                            setcomment();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 18:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            payBoolean = 0;
                            promptLinearLayout.setVisibility(View.GONE);
                            video_play_layout.setVisibility(View.GONE);
                            video_frame.setVisibility(View.VISIBLE);
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                mVideoView.start();
                            } else {
                                initPlayer2();
                            }
                            Video_book_Five.toast(Video_book_Five.this, result.getString("errorMessage"));
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 19:
                    networkText1.setText("您正在使用非wifi网络，播放将产生流量费用");
                    networkText2.setText("继续播放");
                    break;
                case 20:
                    networkText1.setText("网络未连接，请检查网络设置");
                    networkText2.setText("点此重试");
                    break;
                case 21:
                    if (experts.size() > 0) {
                        for (Video_menu_expert expert : experts) {

                            videoexpert = (LinearLayout) View.inflate(context, R.layout.video_listview_header2_getvideoexpert, null);
                            TextView nameView = (TextView) videoexpert.findViewById(R.id.name_view);
                            nameView.setTag(expert.getVideo_menu_expert_id());
                            nameView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Video_book_Five.this, PlayVideo_expert.class);
                                    intent.putExtra("aid", v.getTag().toString());
                                    intent.putExtra("fid", fid);
                                    Video_book_Five.this.startActivity(intent);
                                }
                            });
                            nameView.setText(expert.getVideo_menu_expert_name());
//                            video_expert_layout.addView(videoexpert);
                        }
                        video_Experts_list.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
                        video_experts_gridAdapter = new Video_Experts_GridAdapter(context, experts);
                        video_Experts_list.setAdapter(video_experts_gridAdapter);
                        video_Experts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (experts.get(position).getVideo_menu_expert_id().length() > 0) {
                                    getExpertRulest2(experts.get(position).getVideo_menu_expert_id());
                                } else {
                                    new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage("专家信息正在补充中")
                                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create().show();
                                }
                            }
                        });
                        video_Experts_list.setVisibility(View.VISIBLE);
                    } else {
                        video_Experts_list.setVisibility(View.GONE);
                    }
                    break;
                case 22:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {  // 成功
                            MainActivity.showFloatingView(context);
                        } else if (result.getInt("status") == 5) {
                            // 积分不足 购买积分
                            AlertDialog dialog22 = new AlertDialog.Builder(context)
//                        .setIcon(R.mipmap.icon)//设置标题的图片
                                    .setTitle("提示")//设置对话框的标题
                                    .setMessage("本次观看需要扣除" + money + "积分，您剩余" + userMoney + "积分,48小时内可重复观看当前视频。")//设置对话框的内容
                                    //设置对话框的按钮
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("购买积分", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "点击了确定的按钮", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            Intent intent = new Intent(Video_book_Five.this, Get_Free_Integral.class);
                                            intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                                            intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                                            intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).create();
                            dialog22.show();

                        } else if (result.getInt("status") == 6) {
                            //收费视频需要收费  此地方是试看

                            AlertDialog dialog22 = new AlertDialog.Builder(context)
//                        .setIcon(R.mipmap.icon)//设置标题的图片
                                    .setTitle("提示")//设置对话框的标题
                                    .setMessage("本次观看需要扣除" + videopaymoney + "元，您的余额为" + personalmoney + "元,48小时内可重复观看当前视频。")//设置对话框的内容
                                    //设置对话框的按钮
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("余额支付", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getBalance_paymentRulest2();
                                        }
                                    }).setPositiveButton("在线支付", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Video_book_Five.this, PaymentActivity.class);
                                            intent.putExtra("subject", subject);
                                            //测试变为0.01
                                            // intent.putExtra("money", money);
                                            intent.putExtra("money", videopaymoney);
                                            intent.putExtra("vipflg_str", vipflg_str);
                                            intent.putExtra("payfor", "Video_book_Five");
                                            intent.putExtra("aid", aid);
                                            startActivityForResult(intent, 1);
                                        }
                                    }).create();
                            dialog22.show();

                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 23:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {

                            Video_book_Five.toast(Video_book_Five.this, result.getString("errorMessage"));
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 24://添加子评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
                            initcomment_new();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(Video_book_Five.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 25:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            expert_layout.setVisibility(View.VISIBLE);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            if (!dataObject.isNull("data")) {

                            } else {

                                if (!dataObject.isNull("picurl")) {
                                    ImageLoader.getInstance().displayImage(FirstLetter.getSpells(dataObject.getString("picurl")), video_expert_img);
                                }
                                if (!dataObject.isNull("title")) {
                                    expert_name.setText(dataObject.getString("title"));
                                }
                                if (!dataObject.isNull("title")) {//专家职称
                                    video_expert_Title.setText(dataObject.getString("title"));
                                }
                                if (!dataObject.isNull("title")) {//专家医院
                                    video_hospital_expert.setText(dataObject.getString("title"));
                                }
                                if (!dataObject.isNull("content")) {
                                    video_expert_cont.setText(Html.fromHtml(dataObject.getString("content")));
                                }
                                if (!dataObject.isNull("relatedinfo")) {
                                    JSONArray dataArray = dataObject.getJSONArray("relatedinfo");
                                    for (int i = 0; i < dataArray.length(); i += 2) {
                                        JSONObject expertJson = dataArray.getJSONObject(i);
                                        videodata.add(new Home_Video(expertJson.getString("aid"), expertJson.getString("title"), expertJson.getString("picurl"), expertJson.getString("flag"), expertJson.getString("videopaymoney"), expertJson.getString("money")));
                                    }
                                }

                                home_videos_gridAdapter = new Videos_expertAdapter(context, videodata);
                                horizontalListView.setAdapter(home_videos_gridAdapter);
                                horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        getVideoRulest(videodata.get(position).getAid());
                                    }
                                });

                                experts2.clear();
                                for (int i = 0; i < dataObject.getJSONArray("zjs").length(); i++) {
                                    JSONObject data = dataObject.getJSONArray("zjs").getJSONObject(i);
                                    Video_menu_expert expert = new Video_menu_expert();
                                    expert.setVideo_menu_expert_cont(data.getString("content"));
                                    expert.setVideo_menu_expert_id(data.getString("aid"));
                                    expert.setVideo_menu_expert_img(data.getString("picurl"));
                                    expert.setVideo_menu_expert_name(data.getString("title"));
                                    experts2.add(expert);
                                }


                                video_Experts_list2.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
                                video_experts_gridAdapter2 = new Video_Experts_GridAdapter(context, experts2);
                                video_Experts_list2.setAdapter(video_experts_gridAdapter2);
                                video_Experts_list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        expert_layout.setVisibility(View.VISIBLE);
                                        getExpertRulest2(experts2.get(position).getVideo_menu_expert_id());
                                    }
                                });
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 26:
                    baseListAdapter.notifyDataSetChanged();
                    baseListAdapter3.notifyDataSetChanged();
                    isNoMore = false;
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isFinish = this;
        setContentView(R.layout.video4);
        videoModel = new VideoModel();
        currPage = 1;
        evevt = this;
        //下载路径
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/video/");
        //允许同时进行下载的任务数
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);
        //从数据库中恢复数据
        List<Progress> progressList = com.lzy.okgo.db.DownloadManager.getInstance().getAll();
        OkDownload.restore(progressList);
        checkSDCardPermission();
        findId();
        initComment();
        initdata();
        setOnClick();
        initVideo_Play();
        setrelevant();
//        setcomment();
        loadWeb();
        //启动时判断网络状态
        NETWORK = NetUtil.getNetWorkState(Video_book_Five.this);
        getExpertRulest();
        getProfileComplete();
    }

    public void initNiceVideo() {
        if (clarities.size() > 1) {
            video_view2.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
//            TxVideoPlayerController2 controller = new TxVideoPlayerController2(this);
            controller = new CcmtvVideoPlayerController(this);
            controller.setTitle("");
            controller.setClarity(clarities, 0);    // 设置清晰度以及默认播放的清晰度
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.ys);
            Glide.with(this)
                    .load(picurl)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(controller.imageView());
            video_view2.setController(controller);
        } else {
            video_view2.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
            video_view2.setUp(clarities.get(0).videoUrl, null);
//            TxVideoPlayerController2 controller = new TxVideoPlayerController2(this);
            controller = new CcmtvVideoPlayerController(this);
            controller.setTitle("");
            controller.setImage(R.mipmap.ys);
            video_view2.setController(controller);
        }
    }

    public void initComment() {
        baseListAdapter = new BaseListAdapter2(video_menu_comment_list, commentList, R.layout.video_menu_comment_item) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, final Object item, boolean isScrolling, final int position) {
                super.convert(helper, item, isScrolling);
                final Object mitem;
                final int mposition;
                final TextView t;
                final LinearLayout listLayout;
                final ListHolder mHelper = helper;
                mposition = position;
                mitem = item;
                helper.setText(R.id.video_menu_comment_item_name, ((ArticleCommentEntity) item).getUsername());
                //???
                helper.setText(R.id.video_menu_comment_item_id, ((ArticleCommentEntity) item).getId());
                helper.setText(R.id.video_menu_comment_item_cid, ((ArticleCommentEntity) item).getId());

                helper.setText(R.id.video_menu_comment_item_times, ((ArticleCommentEntity) item).getCreatetime());
                helper.setTag(R.id.video_menu_comment_delete, ((ArticleCommentEntity) item).getId());
                helper.setText(R.id.video_menu_comment_item_cont, ((ArticleCommentEntity) item).getContent());
                t = helper.getView(R.id.comment_itme4);
                listLayout = helper.getView(R.id.video_comment_item_list);
                helper.setImageBitmaps(R.id.video_menu_comment_item_img, ((ArticleCommentEntity) item).getIcon());
                helper.setVideoCommImgOnClick(R.id.video_menu_comment_item_img, R.id.video_menu_comment_item_name);
                helper.setTag(R.id.video_menu_comment_item_img, ((ArticleCommentEntity) item).getUid());
                String uid = SharedPreferencesTools.getUid(helper.getView(R.id.video_menu_comment_item_id).getContext());
                if (((ArticleCommentEntity) item).getUid().equals(uid)) {
                    helper.setVisibility(R.id.video_menu_comment_delete, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.video_menu_comment_delete, View.GONE);
                }
//                helper.setcommreply(R.id.video_menu_comment_reply, ((Video_menu_comment_entry2) item).getVideo_menu_comment_item_name(), ((Video_menu_comment_entry2) item).getVideo_menu_comment_item_cid());
                //回复1级评论
                TextView video_menu_comment_reply1 = helper.getView(R.id.video_menu_comment_reply);
                video_menu_comment_reply1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setTag(((ArticleCommentEntity) item).getId());
                        show_edit(v, "回复 " + ((ArticleCommentEntity) item).getUsername() + " :", "0");
                    }
                });
                helper.setTag(R.id.video_menu_comment_reply, ((ArticleCommentEntity) item).getId());
                //删除
                helper.setCommentAdapterDelOnClick3(R.id.video_menu_comment_delete, Video_book_Five.this);
                String delid = "";
                helper.delViews(R.id.video_comment_item_list);
                //子评论
                for (int i = 0; i < ((ArticleCommentEntity) item).getChildren().size(); i++) {
                    if (i < 2) {
                        //子评论2条以内
                        ArticleCommentEntity.ChildrenBean _item = ((ArticleCommentEntity) item).getChildren().get(i);
                        TextView tv1 = new TextView(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 0, 10);
                        tv1.setLayoutParams(lp);
                        String p_username = _item.getP_username();
                        if (TextUtils.isEmpty(p_username)){
                            p_username = _item.getUsername();
                        }
                        tv1.setText(Html.fromHtml(_item.getUsername() + "回复" + p_username
                                + ":" + _item.getContent()));
                        helper.addview(tv1, R.id.video_comment_item_list);
                    }
                }
                helper.setText(R.id.comment_itme4, "查看全部");
//                helper.setText(R.id.comment_itme4, "查看全部" + ((ArticleCommentEntity) item).getChildren().size() + "条回复");
                //无子评论
                if (((ArticleCommentEntity) item).getChildren().size() < 1) {
                    helper.setVisibility(R.id.video_comment_item_layout, View.GONE);
                } else {
                    helper.setVisibility(R.id.video_comment_item_layout, View.VISIBLE);
                }
                helper.setTag(R.id.comment_itme4, "");
                //大于2条子评论
                if (((ArticleCommentEntity) item).getChildren().size() > 2) {
                    helper.setVisibility(R.id.comment_itme4, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.comment_itme4, View.GONE);
                }
                //查看全部子评论
                t.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getAllReplyData_id = ((ArticleCommentEntity) mitem).getId();//id
                                    getAllReplyData_strid = t.getTag().toString();
                                    getAllReplyData_position = position;
                                    JSONObject object = new JSONObject();
                                    object.put("act", URLConfig.getFatherAndChilds);
                                    object.put("uid", SharedPreferencesTools.getUid(context));
                                    object.put("id", ((ArticleCommentEntity) mitem).getId());
                                    object.put("aid", aid);

                                    final String data = HttpClientUtils.sendPost(context,
                                            URLConfig.CCMTVAPP, object.toString());
                                    final JSONObject result = new JSONObject(data);
                                    LogUtil.e("mason","所有其子评论"+result);
                                    if (result.getInt("status") == 1) { // 成功
                                        JSONObject jsonObject = result.getJSONObject("data");
                                        if (!jsonObject.isNull("slave_comments")) {
                                            JSONArray slave_comments = jsonObject.getJSONArray("slave_comments");
                                            if (slave_comments.length() > 0) {
                                                commentList.get(mposition).getChildren().removeAll(commentList.get(mposition).getChildren());
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            listLayout.removeAllViews();

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                            for (int i = 0; i < slave_comments.length(); i++) {
                                                JSONObject json = slave_comments.getJSONObject(i);
                                                final ArticleCommentEntity.ChildrenBean childrenBean = new ArticleCommentEntity().new ChildrenBean();
                                                childrenBean.setContent(json.getString("content"));
                                                childrenBean.setId(json.getString("id"));
                                                childrenBean.setPid(json.getString("pid"));
                                                childrenBean.setAid(json.getString("aid"));
                                                childrenBean.setAuthorid(json.getString("authorid"));
                                                childrenBean.setUid(json.getString("uid"));
                                                childrenBean.setUsername(json.getString("username"));
                                                childrenBean.setCreatetime(json.getString("createtime"));
                                                childrenBean.setP_uid(json.getString("p_uid"));
                                                childrenBean.setP_username(json.getString("p_username"));
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        TextView tv1 = new TextView(context);
                                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                        lp.setMargins(0, 0, 0, 10);
                                                        tv1.setLayoutParams(lp);
                                                        String p_username = childrenBean.getP_username();
                                                        if (TextUtils.isEmpty(p_username)){
                                                            p_username = childrenBean.getUsername();
                                                        }
                                                        tv1.setText(Html.fromHtml(childrenBean.getUsername() + "回复" + p_username
                                                                + ":" + childrenBean.getContent()));
                                                        mHelper.addview(tv1, R.id.video_comment_item_list);
                                                        mHelper.setVisibility(R.id.comment_itme4, View.GONE);
                                                    }
                                                });
                                                commentList.get(mposition).getChildren().add(childrenBean);
                                            }
//                                            runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    try {
//                                                        item_comment_layout.setVisibility(View.VISIBLE);
//                                                        //设置 单个评论顶层用户头像
//                                                        ImageLoader.getInstance().displayImage(FirstLetter.getSpells(((ArticleCommentEntity) mitem).getIcon()), video_menu_comment_item_img);
//                                                        //设置 单个评论全部 顶层用户名称
//                                                        video_menu_comment_item_name.setText(((ArticleCommentEntity) mitem).getUsername());
//                                                        //设置//单个评论全部 顶层发布时间
//                                                        video_menu_comment_item_times.setText(((ArticleCommentEntity) mitem).getCreatetime());
//                                                        //设置单个评论全部 顶层发布内容
//                                                        video_menu_comment_item_cont.setText(((ArticleCommentEntity) mitem).getContent());
//                                                        //设置 单个评论顶层ID 到回复按钮上面
//                                                        video_menu_comment_reply.setTag(((ArticleCommentEntity) mitem).getId());
//                                                        //设置 评论ID
//                                                        video_menu_comment_item_id.setText(((ArticleCommentEntity) mitem).getId());
//
//                                                        //回复主评论
//                                                        video_menu_comment_reply.setOnClickListener(new OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                show_edit2(v, ((ArticleCommentEntity) mitem).getUsername(), "0");
//                                                            }
//                                                        });
//                                                        video_menu_comment_edittext_ff.setTag(((ArticleCommentEntity) mitem).getId());
//                                                        video_menu_comment_edittext_ff.setOnClickListener(new OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                //弹出输入框
//                                                                show_edit2(v, ((ArticleCommentEntity) mitem).getUsername(), "0");
//                                                            }
//
//                                                        });
//
//                                                        baseListAdapter3.setmDatas(commentList.get(position).getChildren());
//                                                        baseListAdapter3.notifyDataSetChanged();
//                                                        baseListAdapter.notifyDataSetChanged();
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            });
                                        }


                                    } else {// 失败
                                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                });


            }
        };

        video_menu_comment_list.setAdapter(baseListAdapter);

        //视频列表 底部加载更多
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY = v.getScrollY();//顶端以及滑出去的距离
                int height = v.getHeight();//界面的高度
                int scrollViewMeasuredHeight = v.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if (scrollY == 0) {//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if (!isNoMore) {
                        currPage += 1;
                        setcomment2();
                    }
                } else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                }
            }
        });
/*
        //视频列表 底部加载更多
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v,int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY=v.getScrollY();//顶端以及滑出去的距离
                int height=v.getHeight();//界面的高度
                int scrollViewMeasuredHeight=nestedScrollView.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if(scrollY==0){//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
                }else if((scrollY+height)==scrollViewMeasuredHeight){//当在底部的时候
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if (!isNoMore) {
                        currPage += 1;
                        setcomment2();
                    }
                }else {//当在中间的时候
//                    Toast.makeText(getContext(),"当在中间的时候      ", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        baseListAdapter3 = new BaseListAdapter2(item_MyComment, chridCommentList, R.layout.video_menu_comment_item2) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.video_menu_comment_item_name, ((ArticleCommentEntity.ChildrenBean) item).getUsername());
                helper.setText(R.id.video_menu_comment_item_id, ((ArticleCommentEntity.ChildrenBean) item).getId());
                helper.setText(R.id.video_menu_comment_item_cid, ((ArticleCommentEntity.ChildrenBean) item).getId());
                helper.setText(R.id.video_menu_comment_item_times, ((ArticleCommentEntity.ChildrenBean) item).getCreatetime());
                helper.setText(R.id.video_menu_comment_item_cont, ((ArticleCommentEntity.ChildrenBean) item).getContent());
                ImageView ivHead = helper.getView(R.id.video_menu_comment_item_img);
//                ImageLoader.getInstance().displayImage(((ArticleCommentEntity.ChildrenBean) item).getIcon(), ivHead);
                new Carousel_figure(context).loadImageNoCache(((ArticleCommentEntity.ChildrenBean) item).getIcon(), ivHead);
            }
        };


        item_MyComment.setAdapter(baseListAdapter3);
    }

    /**
     * 检查SD卡权限
     */
    protected void checkSDCardPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限
            } else {
                Toast.makeText(getApplicationContext(), "权限被禁止，无法下载文件！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setOnClick() {
        networkText2.setOnClickListener(this);
        play_img.setOnClickListener(this);
        promptopen.setOnClickListener(this);
        promptopen_vip.setOnClickListener(this);
        video_icon_collection_img.setOnClickListener(this);
        video_icon_download_layout.setOnClickListener(this);
        float_layout.setOnClickListener(this);
        video_header_comment_img.setOnClickListener(this);
        layout_praise.setOnClickListener(this);
        related_drugs.setOnClickListener(this);
        video_icon_collection_layout.setOnClickListener(this);
        video_header_comment_layout.setOnClickListener(this);
        tv_say_something.setOnClickListener(this);
        layout_vote.setOnClickListener(this);
        synopsis_layout.setOnClickListener(this);
        icon_x.setOnClickListener(this);
        icon_x_comment.setOnClickListener(this);
        expert_icon_x.setOnClickListener(this);
    }

  /*  *//**
     * name：留言回复功能
     * author：Larry
     * data：2017/5/25 10:17
     *//*
    public void getcommentStumit() {
        video_menu_comment_edittext_submit1.setClickable(false);
        video_menu_comment_edittext_submit.setClickable(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.newReplyComment);
                    obj.put("id", video_menu_comment_edittext_f.getTag());
                    obj.put("uid", SharedPreferencesTools.getUid(Video_book_Five.this));
                    obj.put("aid", aid);
                    obj.put("content", video_menu_comment_edittext_f.getText() + video_menu_comment_edittext_ff.getText().toString());
                    obj.put("username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                    obj.put("flg", video_menu_comment_edittext_f.getTag() != null && video_menu_comment_edittext_f.getTag().toString().length() > 0 ? 2 : 1);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 17;
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
*/


    /**
     * name:设置listview中的值 author:Tom 2016-2-19下午2:11:17
     */
    public void setcomment() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getRecommentList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("page", currPage);
                    obj.put("limit", COMMENT_LIMIT);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
                    LogUtil.e("mason","========"+result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) { // .成功
                        JSONArray dataArray = new JSONArray();
                        if (!jsonObject.isNull("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (!data.isNull("data")){
                                dataArray = data.getJSONArray("data");
                            } else {
                                isNoMore = true;
                            }
                        } else {
                            isNoMore = true;
                        }
                        if (currPage == 1) {
                            commentList.clear();
                        }
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);
                            //     Log.e("看看数据",data.toString());
                            ArticleCommentEntity commentEntity = new ArticleCommentEntity();
                            commentEntity.setContent(data.getString("content"));
                            commentEntity.setId(data.getString("id"));
                            commentEntity.setPid(data.getString("pid"));
                            commentEntity.setAid(data.getString("aid"));
                            commentEntity.setAuthorid(data.getString("authorid"));
                            commentEntity.setUid(data.getString("uid"));
                            commentEntity.setUsername(data.getString("username"));
                            commentEntity.setIcon(data.getString("icon"));
                            commentEntity.setCreatetime(data.getString("createtime"));
                            commentEntity.setLev(data.getInt("lev"));
                            if (!data.isNull("children")) {
                                JSONArray childrenArray = data.getJSONArray("children");
                                for (int j = 0; j < childrenArray.length(); j++){
                                    JSONObject childrenData = childrenArray.getJSONObject(j);
                                    ArticleCommentEntity.ChildrenBean childrenBean = commentEntity.new ChildrenBean();
                                    childrenBean.setContent(childrenData.getString("content"));
                                    childrenBean.setId(childrenData.getString("id"));
                                    childrenBean.setPid(childrenData.getString("pid"));
                                    childrenBean.setAid(childrenData.getString("aid"));
                                    childrenBean.setAuthorid(childrenData.getString("authorid"));
                                    childrenBean.setUid(childrenData.getString("uid"));
                                    childrenBean.setUsername(childrenData.getString("username"));
                                    childrenBean.setIcon(childrenData.getString("icon"));
                                    childrenBean.setCreatetime(childrenData.getString("createtime"));
                                    childrenBean.setP_uid(childrenData.getString("p_uid"));
                                    childrenBean.setP_username(childrenData.getString("p_username"));
                                    childrenBean.setLev(childrenData.getInt("lev"));
                                    commentEntity.getChildren().add(childrenBean);
                                }
                                commentList.add(commentEntity);
                            }
                        }
                    } else {
                        isNoMore = true;
                    }
                    LogUtil.e("mason",commentList.size()+"------------------");
                    Message message = new Message();
                    message.what = 16;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    LogUtil.e("mason",e.toString());
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    public void initcomment_new() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("act", URLConfig.getFatherAndChilds);
                    object.put("uid", SharedPreferencesTools.getUid(context));
                    object.put("id", getAllReplyData_id);
                    object.put("aid", aid);

                    final String data = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    final JSONObject result = new JSONObject(data);
                    LogUtil.e("mason","&&&&&&"+result);
                    if (result.getInt("status") == 1) { // 成功
                        JSONObject jsonObject = result.getJSONObject("data");
                        if (!jsonObject.isNull("slave_comments")) {
                            JSONArray slave_comments = jsonObject.getJSONArray("slave_comments");
                            if (slave_comments.length() > 0) {
                                commentList.get(getAllReplyData_position).getChildren().clear();
                            }
                            for (int i = 0; i < slave_comments.length(); i++) {
                                JSONObject json = slave_comments.getJSONObject(i);
                                ArticleCommentEntity.ChildrenBean childrenBean = new ArticleCommentEntity().new ChildrenBean();
                                childrenBean.setContent(json.getString("content"));
                                childrenBean.setId(json.getString("id"));
                                childrenBean.setPid(json.getString("pid"));
                                childrenBean.setAid(json.getString("aid"));
                                childrenBean.setAuthorid(json.getString("authorid"));
                                childrenBean.setUid(json.getString("uid"));
                                childrenBean.setUsername(json.getString("username"));
                                childrenBean.setCreatetime(json.getString("createtime"));
                                childrenBean.setP_uid(json.getString("p_uid"));
                                childrenBean.setP_username(json.getString("p_username"));
                                commentList.get(getAllReplyData_position).getChildren().add(childrenBean);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        chridCommentList.clear();
                                        chridCommentList.addAll(commentList.get(getAllReplyData_position).getChildren());
                                        LogUtil.e("mason", "               " + chridCommentList.size());
//                                        baseListAdapter3.setmDatas(commentList.get(getAllReplyData_position).getChildren());
                                        baseListAdapter.notifyDataSetChanged();
                                        baseListAdapter3.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogUtil.e("mason", "               " + e.toString());
                                    }
                                }
                            });
                        }


                    } else {// 失败
                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * name:设置listview中的值 author:Tom 2016-2-19下午2:11:17
     */
    public void setcomment2() {
        isNoMore = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getRecommentList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("page", currPage);
                    obj.put("limit", COMMENT_LIMIT);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("评论数据",result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) { // .成功
                        JSONArray dataArray = new JSONArray();
                        if (!jsonObject.isNull("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            if (!data.isNull("data")){
                                dataArray = data.getJSONArray("data");
                            } else {
                                isNoMore = true;
                            }
                        } else {
                            isNoMore = true;
                        }
                        if (currPage == 1) {
                            commentList.clear();
                        }
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);
                            //     Log.e("看看数据",data.toString());
                            ArticleCommentEntity commentEntity = new ArticleCommentEntity();
                            commentEntity.setContent(data.getString("content"));
                            commentEntity.setId(data.getString("id"));
                            commentEntity.setPid(data.getString("pid"));
                            commentEntity.setAid(data.getString("aid"));
                            commentEntity.setAuthorid(data.getString("authorid"));
                            commentEntity.setUid(data.getString("uid"));
                            commentEntity.setUsername(data.getString("username"));
                            commentEntity.setIcon(data.getString("icon"));
                            commentEntity.setCreatetime(data.getString("createtime"));
                            commentEntity.setLev(data.getInt("lev"));
                            if (!data.isNull("children")) {
                                JSONArray childrenArray = data.getJSONArray("children");
                                for (int j = 0; j < childrenArray.length(); j++){
                                    JSONObject childrenData = childrenArray.getJSONObject(j);
                                    ArticleCommentEntity.ChildrenBean childrenBean = commentEntity.new ChildrenBean();
                                    childrenBean.setContent(childrenData.getString("content"));
                                    childrenBean.setId(childrenData.getString("id"));
                                    childrenBean.setPid(childrenData.getString("pid"));
                                    childrenBean.setAid(childrenData.getString("aid"));
                                    childrenBean.setAuthorid(childrenData.getString("authorid"));
                                    childrenBean.setUid(childrenData.getString("uid"));
                                    childrenBean.setUsername(childrenData.getString("username"));
                                    childrenBean.setIcon(childrenData.getString("icon"));
                                    childrenBean.setCreatetime(childrenData.getString("createtime"));
                                    childrenBean.setP_uid(childrenData.getString("p_uid"));
                                    childrenBean.setP_username(childrenData.getString("p_username"));
                                    childrenBean.setLev(childrenData.getInt("lev"));
                                    commentEntity.getChildren().add(childrenBean);
                                }
                                commentList.add(commentEntity);
                            }
                        }
                    } else {
                        isNoMore = true;
                    }
                    Message message = new Message();
                    message.what = 26;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    /**
     * name:加载相关  将值设置到listview里面
     * author:Tom
     * 2016-2-20上午10:07:01
     */
    public void setrelevant() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.secvideoRelevant);
                    obj.put("aid", aid);
                    obj.put("page", 1);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("相关",result);
                    JSONObject jsonresult = new JSONObject(result);
                    if (jsonresult.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonresult.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("departemnt_item_title", jsonObject.getString("title"));
                            map.put("department_id", jsonObject.getString("aid"));
                            map.put("department_on_demand", jsonObject.getString("hits") + "次");
                            map.put("department_times", jsonObject.getString("posttime"));
                            map.put("departemnt_item_img", jsonObject.getString("picurl"));
                            map.put("money", jsonObject.getString("money"));
                            map.put("flag", jsonObject.getString("flag"));
                            map.put("videopaymoney", jsonObject.getString("videopaymoney"));
                            relevants.add(map);
                        }

                        baseListAdapter2 = new BaseListAdapter2(video_menu_relevant_list, relevants, R.layout.custom_item) {
                            @Override
                            public void refresh(Collection datas) {
                                super.refresh(datas);
                            }

                            @Override
                            public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                                super.convert(helper, item, isScrolling);

                                helper.setText(R.id.departemnt_item_title, ((Map) item).get("departemnt_item_title") + "");
                                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                                helper.setText(R.id.department_on_demand, ((Map) item).get("department_on_demand") + "");
                                helper.setText(R.id.department_times, ((Map) item).get("department_times") + "");
                                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                                if (!((Map) item).get("videopaymoney").equals("0")) {
                                    //收费
                                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                                } else {
                                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                                    if (((Map) item).get("money").toString().equals("3")) {
                                        //会员
                                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);

                                    }
                                }


                            }
                        };


                        // listview点击事件
                        video_menu_relevant_list.setOnItemClickListener(new casesharing_listListener());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                video_menu_relevant_list.setAdapter(baseListAdapter2);
                            }
                        });
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }

    /**
     * name：视频分享成功后，检查是否需要增加积分，一天十次机会，每次增加一个积分
     * author：Larry
     * data：2017/5/25 10:16
     */
    private void CheckIntegral() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoShareSuc);
                    obj.put("aid", aid);
                    obj.put("uid", SharedPreferencesTools.getUid(Video_book_Five.this));
                    String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);
//                    Log.e("jsonresult", jsonresult.toString());
                    if (jsonresult.getInt("status") == 1) {// 成功
                        //  Log.i("Video_book_Five", "分享成功" + obj.toString() + "|" + result);
                        Toast.makeText(context, jsonresult.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 观看视频5分钟，获取经验值
     */
    public void videoGrowth(final long playprogress) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtil.e("snow","   " + playprogress);
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoGetjyz);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("timelong", playprogress);
                    String result1 = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    try {
//                        JSONObject result = new JSONObject(result1);
//                        Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.department_id);
            String id = textView.getText().toString();
            getVideoRulest(id);

        }

    }

    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, Video_book_Five.class);
        intent.putExtra("aid", aid);
        startActivity(intent);
        finish();
    }

    /**
     * name：初始化数据
     * author：Larry
     * data：2017/5/25 10:15
     */
    public void initdata() {

        video_menu_comment_reply.setTag("0");
        video_menu_comment_reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show_edit(v, "", "0");
            }
        });


        video_menu_comment_edittext_f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出输入框
                v.setTag(0);
                show_edit(v, "", "1");
            }

        });

        video_menu_comment_edittext_ff.setTag("0");
        video_menu_comment_edittext_ff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出输入框
                show_edit2(v, "", "0");
            }

        });


        context = this;
        payBoolean_S = "";
        payBoolean = 1;
        isFinish = this;
        Intent intent = getIntent();
        aid = intent.getExtras().getString("aid");              //视频aid
        videoaId = intent.getExtras().getString("aid");              //视频aid
        isVote = intent.getExtras().getBoolean("isVote", false);        //是否从投票过来
        votenum = intent.getExtras().getInt("voteNum");        //投票数量
        positions = intent.getExtras().getInt("positions");
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.video_play_act);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("看看视频数据", result);
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
        if (isVote) {                   //如果是投票 则隐藏收藏和下载
            video_icon_collection_layout.setVisibility(View.GONE);
            video_icon_download_layout.setVisibility(View.GONE);
            layout_praise.setVisibility(View.GONE);
            layout_vote.setVisibility(View.VISIBLE);
            tv_votenum.setText(votenum + "票");
        } else {
            video_icon_collection_layout.setVisibility(View.VISIBLE);
            video_icon_download_layout.setVisibility(View.GONE);
            layout_praise.setVisibility(View.VISIBLE);
            layout_vote.setVisibility(View.GONE);
        }

        if (intent.getExtras().getString("videoClass") == null) {
            videoClass = "其 他";
        }
    }

    public void show_edit(final View view, String hint, final String type) {
        if (dialog == null) {

            // 弹出自定义dialog
            LayoutInflater inflater = LayoutInflater.from(context);
            layout_view = inflater.inflate(R.layout.video_editext_view, null);

            // 对话框
            dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog2) {
                    dialog = null;
                    layout_view = null;
                }
            });
            // 设置宽度为屏幕的宽度
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); // 设置宽度
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setContentView(layout_view);
            dialog.setCancelable(true);
            final EditText video_menu_comment_edittext = (EditText) layout_view.findViewById(R.id.video_menu_comment_edittext);//
            final TextView video_editext_submit = (TextView) layout_view.findViewById(R.id.video_editext_submit);//
            if (hint.length() > 0) {
                video_menu_comment_edittext.setHint(hint);
            }
            video_editext_submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!video_menu_comment_edittext.getText().toString().trim().equals("")) {
                        MyProgressBarDialogTools.show(context);
                        getcommentStumit(view, video_menu_comment_edittext.getText().toString(), type);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Video_book_Five.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(video_menu_comment_edittext);
                }
            }, 300);

        }
    }

    public void show_edit2(final View view, String hint, final String type) {
        if (dialog == null) {

            // 弹出自定义dialog
            LayoutInflater inflater = LayoutInflater.from(context);
            layout_view = inflater.inflate(R.layout.video_editext_view, null);

            // 对话框
            dialog = new Dialog(context);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog2) {
                    dialog = null;
                    layout_view = null;
                }
            });
            // 设置宽度为屏幕的宽度
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); // 设置宽度
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setContentView(layout_view);
            dialog.setCancelable(true);
            final EditText video_menu_comment_edittext = (EditText) layout_view.findViewById(R.id.video_menu_comment_edittext);//
            final TextView video_editext_submit = (TextView) layout_view.findViewById(R.id.video_editext_submit);//
            if (hint.length() > 0) {
                video_menu_comment_edittext.setHint(hint);
            }
            video_editext_submit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!video_menu_comment_edittext.getText().toString().trim().equals("")) {
                        MyProgressBarDialogTools.show(context);
                        getcommentStumit2(view, video_menu_comment_edittext.getText().toString(), type);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Video_book_Five.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard(video_menu_comment_edittext);
                }
            }, 300);

        }
    }

    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void finish() {
        super.finish();
        isFinish = null;
    }

    private void getProfileComplete() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.isPerfectdata);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("用户是否需要完善资料：", result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("data")) {
                        String completeStatus = jsonObject.getString("data");
                        if (completeStatus.equals("need")) {
                            Intent intent = new Intent(Video_book_Five.this, MyProfile.class);
                            intent.putExtra("source", "Video_book_Five");
                            intent.putExtra("aid", aid);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name：收费视频收银台支付
     * author：Larry
     * data：2017/5/25 10:15
     */
    public void getBalance_paymentRulest() {
        video_icon_collection_img.setClickable(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cashierPayMoney);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("vipflg_str", vipflg_str);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 18;
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

    /**
     * name：收费视频收银台支付
     * author：Larry
     * data：2017/5/25 10:15
     */
    public void getBalance_paymentRulest2() {
        video_icon_collection_img.setClickable(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cashierPayMoney);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("vipflg_str", vipflg_str);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 23;
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

    /**
     * name：积分扣除
     * author：Larry
     * data：2017/5/25 10:13
     */
    public void getIntegraldDeductionRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.VideoPlay);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 9;
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

    /**
     * name：查询是否需要扣费
     * author：Larry
     * data：2017/5/25 10:13
     */
    public void getIntegraldDeductionRulest2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.VideoPlay);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 22;
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

    public void playVideo() {
        //启动时判断网络状态
        NETWORK = NetUtil.getNetWorkState(Video_book_Five.this);
        if (NETWORK == NetUtil.NETWORK_NONE) {
            //没有网络
            //   Log.e("网络", "没有网络");
            networkLinearLayout.setVisibility(View.VISIBLE);
            networkText1.setText("网络未连接，请检查网络设置");
            networkText2.setText("点此重试");
            networkText1.setVisibility(View.VISIBLE);
            networkText2.setVisibility(View.VISIBLE);
            video_frame.setVisibility(View.GONE);
            video_play_layout.setVisibility(View.VISIBLE);
        } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
            //移动网络】、
            //   Log.e("网络", "移动网络");
            //判断用户上一次的选择是否是选择使用流量继续播放 如果是 就不要频繁的提醒 容易让人产生烦躁的情绪
            networkLinearLayout.setVisibility(View.VISIBLE);
            networkText1.setText("您正在使用非wifi网络，播放将产生流量费用");
            networkText2.setText("继续播放");
            networkText1.setVisibility(View.VISIBLE);
            networkText2.setVisibility(View.VISIBLE);
            video_frame.setVisibility(View.GONE);
            video_play_layout.setVisibility(View.VISIBLE);
        } else if (NETWORK == NetUtil.NETWORK_WIFI) {
            //移动网络】、
            //    Log.e("网络", "wifi");
            Message message = new Message();
            message.what = 12;
            handler.sendMessage(message);
        }
    }


    /**
     * 获取视频播放器 播放视频
     */
    public void initVideo_Play() {
         /*===============视频所需start======================*/
        try {
            LocalApplication application = (LocalApplication) this.getApplication();
            application.getVideoService(new LocalApplication.ServiceListener() {
                @Override
                public void onServiceDisconnected(VideoService service) {
                    mVideoService = service;
                }
            });
        } catch (Exception e) {
//            MyLogger.w(LOG_TAG, "Exception:", e);
        }
        /*===============视频所需end======================*/
    }


    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {

        expert_layout = (LinearLayout) findViewById(R.id.expert_layout);
        expert_name = (TextView) findViewById(R.id.expert_name);
        qbcomment = (TextView) findViewById(R.id.qbcomment);
        comment_num = (TextView) findViewById(R.id.comment_num);
        expert_icon_x = (ImageView) findViewById(R.id.expert_icon_x);
        video_expert_img = (CustomImageView) findViewById(R.id.video_expert_img);
        video_expert_Title = (TextView) findViewById(R.id.video_expert_Title);
        video_hospital_expert = (TextView) findViewById(R.id.video_hospital_expert);
        video_expert_cont = (TextView) findViewById(R.id.video_expert_cont);

        horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
        auto_wrap_line_layout = (AutoWrapLineLayout) findViewById(R.id.auto_wrap_line_layout);
        video_menu_comment_item_img = (CustomImageView) findViewById(R.id.video_menu_comment_item_img);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        arrow_back2 = (LinearLayout) findViewById(R.id.arrow_back2);
        item_comment_layout = (LinearLayout) findViewById(R.id.item_comment_layout);
        synopsis_layout_web = (LinearLayout) findViewById(R.id.synopsis_layout_web);
        synopsis_layout = (LinearLayout) findViewById(R.id.synopsis_layout);
        video_frame = (FrameLayout) findViewById(R.id.video_frame);
        video_menu_comment_list = (MyListView) findViewById(R.id.video_menu_comment_list);
        video_menu_relevant_list = (MyListView) findViewById(R.id.video_menu_relevant_list);
        item_MyComment = (MyListView) findViewById(R.id.item_MyComment);
        list_buttom = (LinearLayout) View.inflate(this, R.layout.list_button, null);
        video_menu_comment_list.setVerticalScrollBarEnabled(true);//不管有没有活动都隐藏
        video_menu_relevant_list.setVerticalScrollBarEnabled(true);//不管有没有活动都隐藏
        video_play_count = (TextView) findViewById(R.id.video_play_count);
        video_title2 = (TextView) findViewById(R.id.video_title2);
        video_menu_comment_item_id = (TextView) findViewById(R.id.video_menu_comment_item_id);
        video_menu_comment_reply = (TextView) findViewById(R.id.video_menu_comment_reply);
        video_menu_comment_item_cont = (TextView) findViewById(R.id.video_menu_comment_item_cont);
        video_menu_comment_item_times = (TextView) findViewById(R.id.video_menu_comment_item_times);
        video_menu_comment_item_name = (TextView) findViewById(R.id.video_menu_comment_item_name);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        promptLinearLayout = (LinearLayout) findViewById(R.id.promptLinearLayout);
        related_drugs = (LinearLayout) findViewById(R.id.related_drugs);
        video_play_img = (ImageView) findViewById(R.id.video_play_img);
        icon_x_comment = (ImageView) findViewById(R.id.icon_x_comment);
        video_play_layout = (FrameLayout) findViewById(R.id.video_play_layout);
        play_img = (ImageView) findViewById(R.id.play_img);
        icon_x = (ImageView) findViewById(R.id.icon_x);
        promptText1 = (TextView) findViewById(R.id.promptText1);
        promptText2 = (TextView) findViewById(R.id.promptText2);
        promptopen = (Button) findViewById(R.id.promptopen);
        promptopen_vip = (Button) findViewById(R.id.promptopen_vip);
        video_play_count_text = (TextView) findViewById(R.id.video_play_count_text);
        //收藏
        video_icon_collection_img = (ImageView) findViewById(R.id.video_icon_collection_img);
        //下载
        video_icon_download_layout = (LinearLayout) findViewById(R.id.video_icon_download_layout);
        //纯音频
        float_layout = (LinearLayout) findViewById(R.id.float_layout);
        //赞
        video_zambia_img = (ImageView) findViewById(R.id.video_no_zambia_img);
        video_header_comment_img = (ImageView) findViewById(R.id.video_header_comment_img);
        video_menu_comment_edittext_f = (TextView) findViewById(R.id.video_menu_comment_edittext_f);
        video_abstract = (WebView) findViewById(R.id.video_abstract);
        video_comment_dilong = (LinearLayout) findViewById(R.id.video_comment_dilong);
        video_no_zambia_Text = (TextView) findViewById(R.id.video_no_zambia_Text);
        MyImg = (ImageView) findViewById(R.id.MyImg);
        MyImg2 = (ImageView) findViewById(R.id.MyImg2);
        networkLinearLayout = (LinearLayout) findViewById(R.id.networkLinearLayout);
        tv_votenum = (TextView) findViewById(R.id.tv_votenum);
        layout_praise = (LinearLayout) findViewById(R.id.layout_praise);
        networkText1 = (TextView) findViewById(R.id.networkText1);
        networkText2 = (TextView) findViewById(R.id.networkText2);
        video_comment_list = (LinearLayout) findViewById(R.id.video_comment_list);
        video_comment_dilong2 = (LinearLayout) findViewById(R.id.video_comment_dilong2);
        video_header_comment_layout = (LinearLayout) findViewById(R.id.video_header_comment_layout);
        video_icon_collection_layout = (LinearLayout) findViewById(R.id.video_icon_collection_layout);
        zambia_text = (TextView) findViewById(R.id.zambia_text);
        zambia_num = (TextView) findViewById(R.id.zambia_num);
        layout_vote = (LinearLayout) findViewById(R.id.layout_vote);
        video_Experts_list = (MyGridView) findViewById(R.id.video_Experts_list);
        video_Experts_list2 = (MyGridView) findViewById(R.id.video_Experts_list2);
        ll_comment_empty = (LinearLayout) findViewById(R.id.ll_comment_empty);
        tv_say_something = (TextView) findViewById(R.id.tv_say_something);
        synopsis = (TextView) findViewById(R.id.synopsis);
        video_menu_comment_edittext_ff = (TextView) findViewById(R.id.video_menu_comment_edittext_ff);
        video_download_img = (ImageView) findViewById(R.id.video_download_img);
        downloadVideo_layout = (LinearLayout) findViewById(R.id.downloadVideo_layout);
        downloadVideo_High_definition = (TextView) findViewById(R.id.downloadVideo_High_definition);
        downloadVideo_Biao_z_clear = (TextView) findViewById(R.id.downloadVideo_Biao_z_clear);
        downloadVideo_flow = (TextView) findViewById(R.id.downloadVideo_flow);
        video_view2 = (NiceVideoPlayer) findViewById(R.id.video_view2);


        video_icon_download_layout.setVisibility(View.GONE);

    }


    /**
     * 设置点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_x_comment:
                item_comment_layout.setVisibility(View.GONE);
                break;
            case R.id.expert_icon_x:
                expert_layout.setVisibility(View.GONE);
                break;
            case R.id.networkText2:     //网络波动 视频播放提醒
                //启动时判断网络状态
                if (NETWORK == NetUtil.NETWORK_NONE) {
                    //没有网络
                    //   Log.e("网络", "没有网络");
                } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
                    //移动网络】、
                    //  Log.e("网络", "移动网络");
                    if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                        if (mVideoView != null) {
                            if (mVideoView.getDuration() > 0) {
//                                Log.e("网络", "networkText2点击继续播放"+mVideoView.getId());
                                networkLinearLayout.setVisibility(View.GONE);
                                video_play_layout.setVisibility(View.GONE);
                                video_frame.setVisibility(View.VISIBLE);

                                mVideoView.start();

//                                Log.e("网络", "networkText2点击继续播放"+mVideoView.getCurrentStatus()+"视频长度："+mVideoView.getDuration());
                                NETWORK_MOBILE_PAY = true;
                            } else {
//                                Log.e("网络", "networkText2点击继续播放 ,mVideoView未初始化成功过，重新初始化");
                                NETWORK_MOBILE_PAY = true;
                                Message message = new Message();
                                message.what = 12;
                                handler.sendMessage(message);
                            }

                        } else {
                            NETWORK_MOBILE_PAY = true;
                            Message message = new Message();
                            message.what = 12;
                            handler.sendMessage(message);
                        }
                    } else {
                        NETWORK_MOBILE_PAY = true;
                        Message message = new Message();
                        message.what = 12;
                        handler.sendMessage(message);
                    }

                } else if (NETWORK == NetUtil.NETWORK_WIFI) {
                    //WIFI
                    if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                        if (mVideoView != null) {
                            //        Log.e("网络", "WIFI");
                            networkLinearLayout.setVisibility(View.GONE);
                            video_play_layout.setVisibility(View.GONE);
                            video_frame.setVisibility(View.VISIBLE);
                            mVideoView.start();
                        } else {
                            Message message = new Message();
                            message.what = 12;
                            handler.sendMessage(message);
                        }
                    } else {
                        Message message = new Message();
                        message.what = 12;
                        handler.sendMessage(message);
                    }


                }
                break;
            case R.id.video_comment_X:
//                video_comment_list.setVisibility(View.GONE);
                video_menu_relevant_list.setVisibility(View.VISIBLE);
                video_comment_dilong2.setVisibility(View.VISIBLE);
                break;
            case R.id.related_drugs:
                Intent i = new Intent(context, ActivityWebActivity.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("aid", relevantContent);
                i.putExtra("title", "内容相关");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                break;
            case R.id.play_img://点击播放按钮---看是否需要扣积分，然后播放
                if (fid == null || fid.length() < 1 || activity_title_name.getText().toString().trim().length() < 1) {
                    break;
                }
                play_img.setVisibility(View.GONE);

                //不管是否收费，每次都调用此接口，加入浏览记录
//                 getIntegraldDeductionRulest();
                //判断是否是收费视频
                try {
                    //  Log.e("videopaymoney", "videopaymoney" + videopaymoney);
                    if (!"0".equals(videopaymoney)) {
                        //是收费视频
                        getIntegraldDeductionRulest();
                        //       Log.e("IMG", "收费视频");
                    } else {
                        //       Log.e("IMG", "不是收费视频");
                        //不是收费视频
                        if (SharedPreferencesTools.getVipFlag(Video_book_Five.this) == 1) {
                            //VIP
//                            playVideo();
                            getIntegraldDeductionRulest();
                        } else {
                            //非vip
                            if (flag == 3) {
                                promptText1.setText("您好，如需播放VIP专属视频，请先开通VIP。");
//                                promptText2.setText("48小时内可重复观看当前视频。");
                                promptText2.setVisibility(View.GONE);
                                promptopen_vip.setText("开通VIP");
                                promptopen.setVisibility(View.GONE);
                                promptopen_vip.setVisibility(View.VISIBLE);
                                promptLinearLayout.setVisibility(View.VISIBLE);
                            } else if (Integer.parseInt(money) > 0) {
                                try {
                                    if ("1".equals(isFortyEight)) {  //判断扣除积分是否处于48小时内
                                        getIntegraldDeductionRulest();
                                        //        Log.e("Video_book_Five", "视频是否处于48小时内,不在收取积分");
                                    } else {
                                        if (userMoney >= Integer.parseInt(money)) {
                                            promptText1.setText("本次观看需要扣除" + money + "积分，您剩余" + userMoney + "积分。");
                                            promptText2.setText("48小时内可重复观看当前视频。");
                                            promptopen.setText("继续观看");
                                            promptLinearLayout.setVisibility(View.VISIBLE);
                                        } else {
                                            promptText1.setText("本次观看需要扣除" + money + "积分，您剩余" + userMoney + "积分。");
                                            promptText2.setText("48小时内可重复观看当前视频。");
                                            promptopen.setText("购买积分");
                                            promptopen_vip.setVisibility(View.VISIBLE);
                                            promptLinearLayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //免费
//                                playVideo();
                                getIntegraldDeductionRulest();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.promptopen://设置继续观看的点击事件
                TextView view = (TextView) v;
                if (view.getText().equals("继续观看")) {
                    getIntegraldDeductionRulest();
                } else if (view.getText().equals("购买积分")) {
                    Intent intent = new Intent(Video_book_Five.this, Get_Free_Integral.class);
                    intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                    intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                    intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                    startActivity(intent);
                    finish();
                } else if (view.getText().equals("余额支付")) {
                    getBalance_paymentRulest();
                }
                break;
            case R.id.promptopen_vip:
                TextView view1 = (TextView) v;
                if (view1.getText().equals("在线支付")) {
                    Intent intent = new Intent(Video_book_Five.this, PaymentActivity.class);
                    intent.putExtra("subject", subject);
                    //测试变为0.01
                    // intent.putExtra("money", money);
//                    Log.e("videopaymoney",videopaymoney);
                    intent.putExtra("money", videopaymoney);
                    intent.putExtra("vipflg_str", vipflg_str);
                    intent.putExtra("payfor", "Video_book_Five");
                    intent.putExtra("aid", aid);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(Video_book_Five.this, MyOpenMenberActivity.class);
                    intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                    intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                    intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.layout_vote:                                  //投票
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", SharedPreferencesTools.getUid(context));
                            object.put("aid", aid);
                            object.put("type", "1");
                            object.put("act", URLConfig.lybVote);
                            String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                            Message message = new Message();
                            message.what = 11;
                            message.obj = result;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                }).start();
                break;
            case R.id.video_icon_collection_layout:
                final String flg2 = video_icon_collection_img.getTag().toString();
                video_icon_collection_img.setClickable(false);
                Runnable runnable2 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doVideo);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("aid", aid);
                            obj.put("flg", flg2);
                            String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                            if (flg2.equals("collection")) {
                                Message message = new Message();
                                message.what = 5;
                                message.obj = result;
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 6;
                                message.obj = result;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                };
                new Thread(runnable2).start();
                break;
            case R.id.video_icon_collection_img:
                final String flg = video_icon_collection_img.getTag().toString();
                video_icon_collection_img.setClickable(false);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doVideo);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("aid", aid);
                            obj.put("flg", flg);
                            String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                            if (flg.equals("collection")) {
                                Message message = new Message();
                                message.what = 5;
                                message.obj = result;
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 6;
                                message.obj = result;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                };
                new Thread(runnable).start();
                break;
            case R.id.video_header_comment_layout:
                video_comment_list.setVisibility(View.VISIBLE);
                if (baseListAdapter.getCount() > 0) {
                    video_menu_relevant_list.setVisibility(View.GONE);
                    video_comment_dilong2.setVisibility(View.GONE);
                    video_menu_comment_list.setVisibility(View.VISIBLE);
                    ll_comment_empty.setVisibility(View.GONE);//隐藏评论为空时的界面
                } else { //评论为空时的图片
                    video_menu_relevant_list.setVisibility(View.GONE);
                    video_comment_dilong2.setVisibility(View.GONE);
                    video_menu_comment_list.setVisibility(View.GONE);
                    ll_comment_empty.setVisibility(View.VISIBLE);//显示评论为空时的界面
                }
                break;
            case R.id.float_layout:
                //纯音频
                FloatLayout.url = float_url;
                FloatLayout.imgUrl = picurl;
                FloatLayout.aid = aid;
                FloatLayout.title = video_title;
                FloatLayout.videoClass = videoClass;
                FloatLayout.experts = "";
                if (experts.size() > 0) {
                    for (Video_menu_expert expert : experts) {
                        FloatLayout.experts = FloatLayout.experts + " " + expert.getVideo_menu_expert_name();
                    }
                }
                try {
                    getIntegraldDeductionRulest2();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.video_icon_download_layout:
                //flag = 5 代表 该视频无法下载
                if (flag == 5) {

                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(Video_book_Five.this);
                    View views = inflater.inflate(R.layout.dialog_item6, null);

                    // 对话框
                    dialog = new Dialog(Video_book_Five.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth() - 100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(views);
                    dialog.setCancelable(false);
                    TextView i_understand = (TextView) views.findViewById(R.id.i_understand);// 取消
                    i_understand.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                } else {
                    apks = com.lzy.okgo.db.DownloadManager.getInstance().getAll();
                    if (apks.size() == 0) {
                        isForFree();
                        break;
                    }
                    for (int l = 0; l < apks.size(); l++) {
                        VideoModel model = (VideoModel) apks.get(l).extra1;
                        if (model == null) {
                            isForFree();
                            return;
                        } else {
                            if (model.getType() == null) {
                                model.setType("video");
                            }
                            if (model.getType().equals("video")) {
                                video.add(apks.get(l));
                            }
                            if (video.size() > 0) {

                            } else {
                                if (!videopaymoney.equals("0")) {
                                    getDownloadRulest();
                                } else {
                                    if (SharedPreferencesTools.getVipFlag(Video_book_Five.this) == 1) {//是会员
                                        getDownloadRulest();
                                    } else {

                                        isdowen = true;
                                    }
                                }
                            }
                        }
                    }
                    boolean ii = false;
                    for (int k = 0; k < video.size(); k++) {
                        videoModel1 = (VideoModel) video.get(k).extra1;
                        if (!videoModel1.getIconUrl().equals(picurl)) {
                            ii = true;
                        } else {

                            ii = false;
                        }
                    }


                    if (isdowen) {
                        //不是会员
                        AlertDialog.Builder builder = new AlertDialog.Builder(Video_book_Five.this);
                        //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
                        builder.setTitle("提示");
                        builder.setMessage("只有VIP才能下载视频，是否购买？");
                        //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
                        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        //设置取消按钮
                        builder.setNegativeButton("购买", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 执行点击取消按钮的业务逻辑
                                Intent intent = new Intent(Video_book_Five.this, MyOpenMenberActivity.class);
                                intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                                intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                                intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                                startActivity(intent);
                            }
                        });

                        //使用builder创建出对话框对象
                        AlertDialog dialog = builder.create();
                        //显示对话框
                        dialog.show();
                        isdowen = false;
                    }
                    if (ii) {
                        isForFree();
                        break;
                    } else {
                        Toast.makeText(context, "您已下载了该视频", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            case R.id.video_header_comment_img:
                video_comment_list.setVisibility(View.VISIBLE);
                if (baseListAdapter.getCount() > 0) {
                    video_menu_relevant_list.setVisibility(View.GONE);
                    video_comment_dilong2.setVisibility(View.GONE);
                    video_menu_comment_list.setVisibility(View.VISIBLE);
                    ll_comment_empty.setVisibility(View.GONE);//隐藏评论为空时的界面
                } else { //评论为空时的图片
                    video_menu_relevant_list.setVisibility(View.GONE);
                    video_comment_dilong2.setVisibility(View.GONE);
                    video_menu_comment_list.setVisibility(View.GONE);
                    ll_comment_empty.setVisibility(View.VISIBLE);//显示评论为空时的界面
                }
                break;
         /*   case R.id.video_menu_comment_edittext_submit:
                if (!video_menu_comment_edittext_f.getText().toString().trim().equals("")) {
                    getcommentStumit();
                }  else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Video_book_Five.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;*/
           /*    case R.id.video_menu_comment_edittext_submit1:
                if (!video_menu_comment_edittext_ff.getText().toString().trim().equals("")) {
                    getcommentStumit();
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Video_book_Five.this,
                                    "内容不能为空！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;*/
            case R.id.layout_praise:                //点赞
                final String flg1 = video_zambia_img.getTag().toString();
                video_zambia_img.setClickable(false);
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doVideo);
                            obj.put("uid", SharedPreferencesTools.getUid(Video_book_Five.this));
                            obj.put("aid", aid);
                            obj.put("flg", flg1);
                            String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
                            if (flg1.equals("zan")) {
                                Message message = new Message();
                                message.what = 3;
                                message.obj = result;
                                handler.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 4;
                                message.obj = result;
                                handler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(500);
                        }
                    }
                };
                new Thread(runnable1).start();
                break;
            case R.id.tv_say_something:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.synopsis_layout:
                synopsis_layout_web.setVisibility(View.VISIBLE);
                break;
            case R.id.icon_x:
                synopsis_layout_web.setVisibility(View.GONE);
                break;

            default:
                break;

        }
    }

    private void isForFree() {
        if (!videopaymoney.equals("0")) {
            getDownloadRulest();
            return;
        } else {
            if (SharedPreferencesTools.getVipFlag(Video_book_Five.this) == 1) {//是会员
                getDownloadRulest();
            } else {
                //不是会员
                AlertDialog.Builder builder = new AlertDialog.Builder(Video_book_Five.this);
                //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
                builder.setTitle("提示");
                builder.setMessage("只有VIP才能下载视频，是否购买？");
                //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //设置取消按钮
                builder.setNegativeButton("购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 执行点击取消按钮的业务逻辑
                        Intent intent = new Intent(Video_book_Five.this, MyOpenMenberActivity.class);
                        intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                        intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                        intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                        startActivity(intent);
                    }
                });

                //使用builder创建出对话框对象
                AlertDialog dialog = builder.create();
                //显示对话框
                dialog.show();
                isdowen = false;
            }
            return;
        }
    }

    /**
     * name：留言回复功能
     * author：Larry
     * data：2017/5/25 10:17
     */
    public void getcommentStumit(final View view, final String content, final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addRecomment);
                    obj.put("uid", SharedPreferencesTools.getUid(Video_book_Five.this));
                    obj.put("username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 17;
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

    /**
     * name：留言回复功能
     * author：Larry
     * data：2017/5/25 10:17
     */
    public void getcommentStumit2(final View view, final String content, final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addRecomment);
                    obj.put("uid", SharedPreferencesTools.getUid(Video_book_Five.this));
                    obj.put("username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 24;
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

    public void toAbstract(View v) {
        Intent intent = new Intent(Video_book_Five.this, PlayVideo_abstract.class);
        intent.putExtra("aid", aid);
        intent.putExtra("fid", fid);
        startActivity(intent);
    }


    public void back(View view) {
        // TODO Auto-generated method stub
        MyProgressBarDialogTools.hide();
        if (isVote) {
            Intent intent = new Intent();
            intent.putExtra("votenum", votenum);
            intent.putExtra("positions", positions);
            setResult(17, intent);
        }
        finish();

    }

    @Override
    public void onBackPressed() {
        MyProgressBarDialogTools.hide();
        if (isVote) {
            Intent intent = new Intent();
            intent.putExtra("votenum", votenum);
            intent.putExtra("positions", positions);
            setResult(17, intent);
            finish();
        }
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (mVideoView != null) {
                if (mVideoView.rotatedFromBtn()) {
                    if (mVideoView.isFullScreen()) {
//                        System.out.println("变小屏播放======================================================");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.GONE);
                                video_comment_dilong2.setVisibility(View.GONE);
                            }
                        });
                    } else {
//                        System.out.println("变大屏播放======================================================");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.VISIBLE);
                                video_comment_dilong2.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    return;
                } else {
                    if (mVideoView.isFullScreen()) {
//                        System.out.println("变小屏播放======================================================");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.VISIBLE);
                                video_comment_dilong2.setVisibility(View.VISIBLE);
                            }
                        });
                        mVideoView.toMiniScreen();
                    } else {
//                        System.out.println("变大屏播放======================================================");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.GONE);
                                video_comment_dilong2.setVisibility(View.GONE);
                            }
                        });
                        mVideoView.toFullScreen();
                    }
                }
            }
        } catch (Exception e) {
//            MyLogger.w(LOG_TAG, "onConfigurationChanged exception");
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();
        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
            Toast.makeText(this, velocityX + "左滑", Toast.LENGTH_SHORT).show();
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
            Toast.makeText(this, velocityX + "右滑", Toast.LENGTH_SHORT).show();
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
            Toast.makeText(this, velocityX + "上滑", Toast.LENGTH_SHORT).show();
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
            Toast.makeText(this, velocityX + "下滑", Toast.LENGTH_SHORT).show();
        }
        return false;
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
//        Log.e("aaarg1", arg0.toString());
//        Log.e("aaarg2", arg2.toString());
        handler.sendMessage(msg);
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
//        Log.e("aaarg3", arg0.toString());
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


    public static class DefinitionDialog extends DialogFragment {

        private int index = 0;

        public static DefinitionDialog newInstance(String title, String[] definitions) {
            DefinitionDialog definitionDialog = new DefinitionDialog();
            Bundle bundle = new Bundle(2);
            bundle.putString("title", title);
            bundle.putStringArray("definitions", definitions);
            definitionDialog.setArguments(bundle);
            return definitionDialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = getArguments().getString("title");
            final String[] definitions = getArguments().getStringArray("definitions");
            return new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setSingleChoiceItems(definitions, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            index = which;
                        }
                    }).setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String definition = mInformation.getDefinitionEN(getActivity(), definitions[index]);
                                ((Video_book_Five) getActivity()).download(definition);
                                dialog.dismiss();
                            } catch (Exception e) {
//                                MyLogger.w(LOG_TAG, "fragment_download exception", e);
                            }
                        }
                    }).create();

        }

    }

    private void download(String definition) {
        try {
            DownloadManager downloadManager = mVideoService.getDownloadManager();
            String videoId = my_video_id;
            DownloadData downloadData = new DownloadData(videoId, definition);
            downloadData.setDownloadListener(mDownloadListener);
            downloadManager.download(downloadData);
        } catch (Exception e) {
//            MyLogger.w(LOG_TAG, "fragment_download exception", e);
        }
    }

    public void saveData(String fileName, String picurl, String downloadUrl, long playprogress, String vtime, String aid) {
        videoModel.setName(fileName);
        videoModel.setIconUrl(picurl);
        videoModel.setUrl(downloadUrl);
        videoModel.setLast_look_time(playprogress);
        videoModel.setVtime(vtime);
        videoModel.setAid(aid);
        videoModel.setDownload_state("0");
        //给下载添加标识
        videoModel.setType("video");
    }

    DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void onSuccess() {
            showToast("下载成功");
        }

        @Override
        public void onProgressChanged(int progress) {
        }

        @Override
        public void onStatusChanged(int status) {
        }

        @Override
        public void onFailure(Exception e) {
            showToast("下载失败");
        }
    };

    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    private void initFragment() {
     /*   if (hits.length() > 4) {
            if (Integer.parseInt(hits.substring(hits.length() - 4, hits.length() - 3)) > 0) {
                hits = hits.substring(0, hits.length() - 4) + "." + hits.substring(hits.length() - 4, hits.length() - 3) + "万";
            } else {
                hits = hits.substring(0, hits.length() - 4) + "万";
            }
        }
        if (digg_num.length() > 4) {

            if (Integer.parseInt(digg_num.substring(digg_num.length() - 4, digg_num.length() - 3)) > 0) {
                digg_num = digg_num.substring(0, digg_num.length() - 4) + "." + digg_num.substring(digg_num.length() - 4, digg_num.length() - 3) + "万";
            } else {

                digg_num = digg_num.substring(0, digg_num.length() - 4) + "万";
            }

        }
        video_no_zambia_Text.setText(digg_num);
        video_play_count_text.setText(hits + "次");*/
        if (zanflg.equals("1")) {//赞过
            video_zambia_img.setTag("cancelZan");
            video_zambia_img.setImageResource(R.mipmap.video_paly3_21);
        } else {
            video_zambia_img.setTag("zan");
            video_zambia_img.setImageResource(R.mipmap.video_paly3_05);
        }
        if (collectionflg.equals("1")) {//收藏
            video_icon_collection_img.setImageResource(R.mipmap.video_paly3_20);
            video_icon_collection_img.setTag("cancelCollection");
        } else {
            video_icon_collection_img.setImageResource(R.mipmap.video_paly3_07);
            video_icon_collection_img.setTag("collection");
        }
        video_icon_download_layout.setTag("down");

    }


    /**
     * 将视频放入播放器
     */
    private void initPlayer2() {
//        LogUtil.e("视频播放数据",clarities.size()+"");
//        LogUtil.e("视频播放数据",clarities.get(0).videoUrl);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        play_img.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
        video_view2.setVisibility(View.VISIBLE);
        promptLinearLayout.setVisibility(View.GONE);
        video_play_layout.setVisibility(View.GONE);
        video_frame.setVisibility(View.VISIBLE);
        video_view2.start();

    }

    private void initPlayer() {
        try {
            final String videoId = my_video_id;
            if (videoId == null || TextUtils.isEmpty(videoId)) {
//                MyLogger.w(LOG_TAG, "videoId can't be null or empty");
                finish();
            }
            final VideoManager videoManager = mVideoService.getVideoManager();
            defaultDefinition = SDKConstants.DEFINITION_ANDROID_SMOOTH;
            //清晰度选择
            JSONObject jsonObject = new JSONObject(smvpurl);
            //流畅 石山视频路径
            String fluentFile = jsonObject.getString("fluentFile");
            //标清石山视频路径
            String SDFile = jsonObject.getString("SDFile");
            //高清石山视频路径
            String hdefinitionFile = jsonObject.getString("hdefinitionFile");
            if (NETWORK == NetUtil.NETWORK_NONE) {
                //没有网络
                //   Log.e("网络", "没有网络");
            } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
                //移动网络】、
                //   Log.e("网络", "移动网络");
                //判断用户上一次的选择是否是选择使用流量继续播放 如果是 就不要频繁的提醒 容易让人产生烦躁的情绪
                if (SDFile != null && SDFile.length() > 2) {
                    defaultDefinition = SDKConstants.DEFINITION_ANDROID_STANDARD;
                }
            } else if (NETWORK == NetUtil.NETWORK_WIFI) {
                //WIFI
                //  Log.e("网络", "WIFI");
                if (hdefinitionFile != null && hdefinitionFile.length() > 2) {
                    defaultDefinition = SDKConstants.DEFINITION_ANDROID_HD;
                } else {
                    if (SDFile != null && SDFile.length() > 2) {
                        defaultDefinition = SDKConstants.DEFINITION_ANDROID_STANDARD;
                    }
                }
            }
            mVideoView = (VideoView) findViewById(R.id.video_view);
            //续播弹框
            System.out.println("视频ID：" + videoManager + "==" + videoId + "==" + LocalConstants.CHARGE_PLAYER_ID + "==" + defaultDefinition + "isFastShow");
            if (MyDbUtils.findRecordVideo(context, my_video_id) > 0) {

                final Dialog dialog = new Dialog(Video_book_Five.this, R.style.ActionSheetDialogStyle);
                LayoutInflater inflater = LayoutInflater.from(this);
                View view1 = inflater.inflate(R.layout.dialog_xubo, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(false);
                TextView tv_confirm = (TextView) view1.findViewById(R.id.tv_confirm);
                TextView tv_title = (TextView) view1.findViewById(R.id.tv_title);
                TextView tv_canal = (TextView) view1.findViewById(R.id.tv_canal);
                tv_title.setText("您上次已观看到" + formatTime(MyDbUtils.findRecordVideo(context, my_video_id)) + ",是否继续观看?");
                tv_confirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoView.showProgressBar(true);
                        mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
                        mVideoView.seekToPosition(MyDbUtils.findRecordVideo(context, my_video_id) * 1000);
                        dialog.dismiss();
                    }
                });
                tv_canal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mVideoView.showProgressBar(true);
                        mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
                    }
                });
                dialog.show();
            } else {
                mVideoView.showProgressBar(true);
                mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {

        if (mVideoView != null) {
            mVideoView.onActivityResume();
        }
//        initdata();
        //加载评论
        setcomment();
        //
        registerReceiver();
        // MyProgressBarDialogTools.hide();
        if (payBoolean_S.equals("success")) {
            if (payBoolean == 0) {
                promptLinearLayout.setVisibility(View.GONE);
                video_play_layout.setVisibility(View.GONE);
                video_frame.setVisibility(View.VISIBLE);
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    mVideoView.start();
                } else {
                    initPlayer2();
                }
            }
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/video/" + fid + "/" + aid + ".html";//拼接url
        if (mVideoView != null) {
            mVideoView.onActivityPause();
        }

        MyProgressBarDialogTools.hide();
        super.onPause();
    }

    public void changeVideo() {
        String videoId = my_video_id;
        String definition = SDKConstants.DEFINITION_ANDROID_STANDARD;
        mVideoView.changeVideo(videoId, definition);
    }

    private String formatTime(float time) {
        Formatter formatter = new Formatter();
        // Log.i("time", "time" + time);
        if (time < 60) {
            String str_time = time + "";
            if (time < 10) {
                return "00:0" + str_time.substring(0, str_time.indexOf("."));  //如果进度为10s以为   则time为6.0，7.0截取。之前的数字，拼接“00：06，00：07”
            } else {
                return "00:" + str_time.substring(0, str_time.indexOf("."));  //如果进度为大于10，小于60，以为   则time为16.0，17.0截取。之前的数字，拼接“00：16，00：17”
            }
        }
        int seconds = (int) (time % 60);
        int minutes = (int) ((time % 3600) / 60);
        int hours = (int) (time / 3600);

        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void registerReceiver() {
        mProgressRecevier = new ProgressReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SDKConstants.ACTION_PLAY_PROGRESS_CHANGED);
        registerReceiver(mProgressRecevier, filter);
    }

    //获取当前时间
    private class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
//            if (SDKConstants.ACTION_PLAY_PROGRESS_CHANGED == action) {
            String id = intent.getStringExtra(SDKConstants.KEY_ID);
            String title = intent.getStringExtra(SDKConstants.KEY_TITLE);
            float progress = intent.getFloatExtra(SDKConstants.KEY_PROGRESS, 0);
            playprogress = (long) progress;
            myPlayprogress += 1;
            if (myPlayprogress == TIME_LONG) {
                LogUtil.e("snow","onReceive call   == " + myPlayprogress);
                videoGrowth(myPlayprogress);
            }
            MyDbUtils.saveRecordVideo(context, my_video_id, playprogress);
            String text = getString(R.string.current_play_progress, formatTime(progress));
            //这是要设置是否需要收费 如果需要收费那么就 收费
            //判断该视频是收费视频才监听
            //停止播放音频
            MainActivity.stopFloatingView(context);

            if (!"0".equals(videopaymoney)) {
                //小屏就显示按钮
                if (payBoolean > 0) {
                    if (progress > 50) {
                        mVideoView.pause();
                        //先判断是否全屏 如果是全屏就变小屏
                        if (mVideoView.isFullScreen()) {
                            //变小屏
                            Video_book_Five.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    arrow_back2.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        //显示 购买层
                        promptText1.setText("本次观看需要扣除" + videopaymoney + "元，您的余额为" + personalmoney + "元。");
                        promptText2.setText("48小时内可重复观看当前视频。");
                        promptopen.setText("余额支付");
                        promptopen_vip.setText("在线支付");
                        promptopen_vip.setVisibility(View.VISIBLE);
                        promptopen.setVisibility(View.VISIBLE);
                        promptLinearLayout.setVisibility(View.VISIBLE);
                        video_play_layout.setVisibility(View.VISIBLE);
                        video_frame.setVisibility(View.GONE);
                    }
                }
            }
            if (NETWORK == NetUtil.NETWORK_NONE) {
                //没有网络
                //  Log.e("网络", "没有网络");
                mVideoView.pause();
                //先判断是否全屏 如果是全屏就变小屏
                if (mVideoView.isFullScreen()) {
                    //变小屏
                    Video_book_Five.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            arrow_back2.setVisibility(View.VISIBLE);
                        }
                    });
                }
                networkLinearLayout.setVisibility(View.VISIBLE);
                networkText1.setText("网络未连接，请检查网络设置");
                networkText2.setText("点此重试");
                networkText1.setVisibility(View.VISIBLE);
                networkText2.setVisibility(View.VISIBLE);
                video_frame.setVisibility(View.GONE);
                video_play_layout.setVisibility(View.VISIBLE);
            } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
                //移动网络
                //   Log.e("网络", "移动网络");
                //判断用户上一次的选择是否是选择使用流量继续播放 如果是 就不要频繁的提醒 容易让人产生烦躁的情绪
                if (!NETWORK_MOBILE_PAY) {
                    mVideoView.pause();
                    //先判断是否全屏 如果是全屏就变小屏
                    if (mVideoView.isFullScreen()) {
                        //变小屏
                        Video_book_Five.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    networkLinearLayout.setVisibility(View.VISIBLE);
                    networkText1.setText("您正在使用非wifi网络，播放将产生流量费用");
                    networkText2.setText("继续播放");
                    networkText1.setVisibility(View.VISIBLE);
                    networkText2.setVisibility(View.VISIBLE);
                    video_frame.setVisibility(View.GONE);
                    video_play_layout.setVisibility(View.VISIBLE);
                }

            } else if (NETWORK == NetUtil.NETWORK_WIFI) {
                //WIFI
                //   Log.e("网络", "WIFI");
            }

        }

    }

    @Override
    public void onNetChange(int netMobile) {
        //网络状态变化时的操作
        NETWORK = netMobile;
        if (NETWORK == NetUtil.NETWORK_NONE) {
            //没有网络
            //   Log.e("网络", "没有网络");
            networkLinearLayout.setVisibility(View.VISIBLE);
            networkText1.setText("网络未连接，请检查网络设置");
            networkText2.setText("点此重试");
            networkText1.setVisibility(View.VISIBLE);
            networkText2.setVisibility(View.VISIBLE);
            video_frame.setVisibility(View.GONE);
            video_play_layout.setVisibility(View.VISIBLE);
            if (videoplaystyle.equals("smvp")) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                }

            } else if (videoplaystyle.equals("ccmtv")) {
                if (video_view2.isPlaying()) {
                    video_view2.pause();
                }
            }
        } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
            //移动网络】、
            //   Log.e("网络", "移动网络");
            //判断用户上一次的选择是否是选择使用流量继续播放 如果是 就不要频繁的提醒 容易让人产生烦躁的情绪
            networkLinearLayout.setVisibility(View.VISIBLE);
            networkText1.setText("您正在使用非wifi网络，播放将产生流量费用");
            networkText2.setText("继续播放");
            networkText1.setVisibility(View.VISIBLE);
            networkText2.setVisibility(View.VISIBLE);
            video_frame.setVisibility(View.GONE);
            video_play_layout.setVisibility(View.VISIBLE);
            if (videoplaystyle.equals("smvp")) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                }

            } else if (videoplaystyle.equals("ccmtv")) {
                if (video_view2.isPlaying()) {
                    video_view2.pause();
                }
            }
        } else if (NETWORK == NetUtil.NETWORK_WIFI) {
            //当前网络为wifi 判断当前该视频是否是播放了 是不是播放中网络波动过还是用户自己点击的暂停
            if (View.VISIBLE == networkLinearLayout.getVisibility()) {
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    if (mVideoView != null) {
                        networkLinearLayout.setVisibility(View.GONE);
                        video_play_layout.setVisibility(View.GONE);
                        video_frame.setVisibility(View.VISIBLE);

                        mVideoView.start();
                    } else {
                        Message message = new Message();
                        message.what = 12;
                        handler.sendMessage(message);
                    }
                } else {
                    Message message = new Message();
                    message.what = 12;
                    handler.sendMessage(message);
                }

            }
        }

    }


    public void getDownloadRulest() {
        final String flg = video_icon_download_layout.getTag().toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();

                    obj.put("act", URLConfig.doVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("flg", flg);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("dovideo", result.toString());
                    if (flg.equals("down")) {
                        Message message = new Message();
                        message.what = 7;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 8;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }


    @Override
    public void onDownloadBtnClick() {
        if (!MyDbUtils.findVideoExist(context, aid)) {
            if (SharedPreferencesTools.getVipFlag(Video_book_Five.this) == 1) {//是会员
                getDownloadRulest();
            } else {
                //不是会员
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //设置对话框图标，可以使用自己的图片，Android本身也提供了一些图标供我们使用
//                builder.setIcon(android.R.drawable.ic_dialog_info);
                //设置对话框标题
                builder.setTitle("提示");
                //设置对话框内的文本
                builder.setMessage("只有VIP才能下载视频，是否购买？");
                //设置确定按钮，并给按钮设置一个点击侦听，注意这个OnClickListener使用的是DialogInterface类里的一个内部接口
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //设置取消按钮
                builder.setNegativeButton("购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 执行点击取消按钮的业务逻辑
                        Intent intent = new Intent(Video_book_Five.this, MyOpenMenberActivity.class);
                        intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(Video_book_Five.this));
                        intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(Video_book_Five.this));
                        intent.putExtra("Str_username", SharedPreferencesTools.getUserName(Video_book_Five.this));
                        startActivity(intent);
                        finish();
                    }
                });
                //使用builder创建出对话框对象
                AlertDialog dialog = builder.create();
                //显示对话框
                dialog.show();
            }
        } else {
            Toast.makeText(context, "您已下载了该视频", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivatedStatusChanged(boolean status) {
        if (status) {
            mVideoService.getVideoManager().activate(videoStartId);
        } else {
            mVideoService.getVideoManager().deactivate(videoStartId);
        }
    }

    /*
    * 分享视频
    * */
    public void ShareVideo(View view) {
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
                        object.put("aid", aid);
                        object.put("act", URLConfig.videoShare);
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                        Message message = new Message();
                        message.what = 10;
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


    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 设置点击edittext之外键盘消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            if (onfling == 1)
                return super.dispatchTouchEvent(ev);
            else
                return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View v = getCurrentFocus();
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        if (onfling == 1)
            return onTouchEvent(ev);
        else
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getStringExtra("payResult").equals("ok")) {
                //支付成功
                payBoolean = 0;
                promptLinearLayout.setVisibility(View.GONE);
                video_play_layout.setVisibility(View.GONE);
                video_frame.setVisibility(View.VISIBLE);
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    mVideoView.start();
                } else {
                    initPlayer2();
                }

            }
        }
    }

    public static void toast(Context context, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);      //加载布局文件
        TextView textView = (TextView) view.findViewById(R.id.toast_text);    // 得到textview
        textView.setText(content);     //设置文本类荣，就是你想给用户看的提示数据
        Toast toast = new Toast(context);     //创建一个toast
        toast.setDuration(Toast.LENGTH_SHORT);          //设置toast显示时间，整数值
        toast.setGravity(Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);    //toast的显示位置，这里居中显示
        toast.setView(view);     //設置其显示的view,
        toast.show();             //显示toast
    }

    /*===========================视频所需end==============================*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mProgressRecevier);
        MyDbUtils.updateRecordVideo(context, my_video_id, playprogress);
    }


    /**
     * 判断网络之后再下载（是否为2G，3G，4G网络）
     *
     * @param fluentFile
     * @param SDFile
     * @param hdefinitionFile
     */
    private void judgeNetWork(final String fluentFile, final String SDFile, final String hdefinitionFile) {
        if (NETWORK == NetUtil.NETWORK_MOBILE) {
            /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(Video_book_Five.this);
            normalDialog.setTitle("流量提醒");
            normalDialog.setMessage("您正在使用移动数据流量下载，是否继续?");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //弹框选择清晰度
                            SelectClarity(fluentFile, SDFile, hdefinitionFile);
                        }
                    });
            normalDialog.setNegativeButton("取消下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Video_book_Five.this, "取消下载", Toast.LENGTH_SHORT).show();
                        }
                    });
            // 显示
            normalDialog.show();
        } else {
            //弹框选择清晰度
            SelectClarity(fluentFile, SDFile, hdefinitionFile);
        }
    }


    public void getExpertRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getVideoExpert);
                    obj.put("aid", videoaId);
                    String result = HttpClientUtils.sendPost(Video_book_Five.this, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("expert",result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        System.out.println("expert:" + dataArray);
                        experts.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);
                            Video_menu_expert expert = new Video_menu_expert();
                            expert.setVideo_menu_expert_cont(data.getString("content"));
                            expert.setVideo_menu_expert_id(data.getString("aid"));
                            expert.setVideo_menu_expert_img(data.getString("picurl"));
                            expert.setVideo_menu_expert_name(data.getString("title"));
                            experts.add(expert);
                        }

                    }
                    Message message = new Message();
                    message.what = 21;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 选择下载视频清晰度
     *
     * @param fluentFile
     * @param SDFile
     * @param hdefinitionFile
     */
    public void SelectClarity(final String fluentFile, final String SDFile, final String hdefinitionFile) {

        //设置清晰度选择层显示
        downloadVideo_layout.setVisibility(View.VISIBLE);
        //设置 点击空白区域消失
        downloadVideo_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadVideo_layout.setVisibility(View.GONE);
            }
        });
        //设置三种可选择的是否显示
        if (fluentFile.length() > 0) {
            downloadVideo_flow.setVisibility(View.VISIBLE);
        } else {
            downloadVideo_flow.setVisibility(View.GONE);
        }
        if (SDFile.length() > 0) {
            downloadVideo_Biao_z_clear.setVisibility(View.VISIBLE);
        } else {
            downloadVideo_Biao_z_clear.setVisibility(View.GONE);
        }
        if (hdefinitionFile.length() > 0) {
            downloadVideo_High_definition.setVisibility(View.VISIBLE);
        } else {
            downloadVideo_High_definition.setVisibility(View.GONE);
        }
        //设置三种清晰度点击事件
        downloadVideo_flow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadUrl = fluentFile;
                DownloadVideo();
                SharedPreferencesTools.setDownloadState(context, "1");
                Toast.makeText(context, "视频已添加到下载列表", Toast.LENGTH_SHORT).show();
                downloadVideo_layout.setVisibility(View.GONE);
            }
        });
        downloadVideo_Biao_z_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadUrl = SDFile;
                DownloadVideo();
                SharedPreferencesTools.setDownloadState(context, "1");
                Toast.makeText(context, "视频已添加到下载列表", Toast.LENGTH_SHORT).show();
                downloadVideo_layout.setVisibility(View.GONE);
            }
        });
        downloadVideo_High_definition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadUrl = hdefinitionFile;
                DownloadVideo();
                SharedPreferencesTools.setDownloadState(context, "1");
                Toast.makeText(context, "视频已添加到下载列表", Toast.LENGTH_SHORT).show();
                downloadVideo_layout.setVisibility(View.GONE);
            }
        });

    }

    public void DownloadVideo() {
        if (OkDownload.getInstance().hasTask(downloadUrl)) {
            OkDownload.getInstance().removeTask(downloadUrl);
        }
        // 新下载功能
        String fileName = mvurl.substring(mvurl.lastIndexOf("/") + 1, mvurl.length() - 2);
        String downloadPath = Environment.getExternalStorageDirectory() + "/Download/" + fileName;
        saveData(fileName, picurl, downloadUrl, playprogress, vtime, aid);
        //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
        GetRequest<File> request = OkGo.<File>get(downloadUrl)//
                .headers("aaa", "111")//
                .params("bbb", "222");
        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
        OkDownload.request(downloadUrl, request)//
                .fileName(fileName)
                .extra1(videoModel)//
                .save()//
                .register(new LogDownloadListener())//
                .start();
//        setDownloadNum(context,aid);
    }

    /**
     * 下载过该视频，再次点击显示已下载
     *
     * @param context
     * @param videoId
     */
    private static void setDownloadNum(final Context context, final String videoId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", videoId);
                    obj.put("flg", "downStart");//下载次数加一
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    System.out.println("下载:" + obj + "|" + result);
//                    Log.e("downloadstart111", result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void loadWeb() {
        video_abstract.loadUrl("http://www.ccmtv.cn/do/ccmtvappandroid/getVideoRemark.php?" + aid);
        video_abstract.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void getExpertRulest2(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getExpertOnly);
                    obj.put("aid", id);
                    obj.put("page", 1);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("专家详细信息",result);
                    Message message = new Message();
                    message.what = 25;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    //弹出软键盘
    public void showKeyboard(EditText editText) {
        //其中editText为dialog中的输入框的 EditText
        if (editText != null) {
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            //调用系统输入法
//            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
}
