package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.HomeKeyWatcher;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.cashier.PaymentActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.ArticleCommentEntity;
import com.linlic.ccmtv.yx.activity.entity.Video_menu_comment_entry;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_abstract;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_expert;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.VideoService;
import com.linlic.ccmtv.yx.activity.home.util.LocalConstants;
import com.linlic.ccmtv.yx.activity.home.util.MyLogger;
import com.linlic.ccmtv.yx.activity.my.Get_Free_Integral;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.NotSignDialog;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Examination_instructions2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.fragment.DetailFragment;
import com.linlic.ccmtv.yx.holder.BaseListAdapter2;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.util.CcmtvVideoPlayerController;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.linlic.ccmtv.yx.widget.FocuedTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiao.nicevideoplayer.Clarity;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
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
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * name:视频签到
 * author:Niklaus
 * 2017-10-25
 */
public class VideoSignActivity extends AppCompatActivity implements DetailFragment.Callback, OnGestureListener, PlatformActionListener, OnClickListener {
    public static String payfor = "VideoSignActivity";
    private static final int COMMENT_LIMIT = 10;//每页显示多少评论
    private static int TIME_LONG = 300;
    public final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
    private LinearLayout hearderViewLayout, list_buttom, arrow_back2, video_comment_dilong, layout_praise, networkLinearLayout;
    private ProgressReceiver mProgressRecevier;
    Context context;
    VideoSignActivity videoSign;
    //用户统计
    private String  Aid, fid;
    private FocuedTextView activity_title_name;
    private List<Video_menu_expert> experts = new ArrayList<Video_menu_expert>();//专家
    private LinearLayout promptLinearLayout, ll_video_sign, ll_sign1, ll_sign2;
    public static int payBoolean = 0;
    public static String payBoolean_S = "";
    public static String expired = "";
    private VideoService mVideoService;
    public static VideoView mVideoView;
    public String signnum = "";
    private String sign_num = "";//签到总数
    private static TransCodingInformation mInformation;
    private ArrayList<SimplePlayerProperty> mPlayerList;
    private DefinitionDialog mDialog;
    private LinearLayout video_expert_layout;
    private static final String LOG_TAG = VideoSignActivity.class.getSimpleName();
    //视频aid
    public static String aid, tid, hits, money, down_num, digg_num, picurl, zanflg, downflg, collectionflg, my_video_id;
    //当前登录账户积分数量
    private int userMoney = 0, positions, votenum, NETWORK = 0, previewNetwork = 0;
    public static int currPage = 1, onfling = 1;
    //是否是投票
    private boolean isVote;
    //视频第一帧图,视频播放按钮/收藏,下载,/赞
    private ImageView video_play_img, play_img, video_icon_collection_img, video_zambia_img, MyImg2, video_header_comment_img;
    //视频播放器Layout
    private FrameLayout video_play_layout;
    private TextView promptText1, promptText2, tv_votenum, video_menu_comment_edittext_f, video_menu_comment_edittext_ff, video_play_count_text, video_no_zambia_Text, networkText1, networkText2;
    //投票数量
    private Button promptopen, promptopen_vip;
    private String mvurl, videoplaystyle, smvpurl, video_title, dataurl, videoaId, videoStartId, isFortyEight, videoClass, personalmoney, videopaymoney, vipflg_str, subject, defaultDefinition;
    private WebView video_abstract;
    static VideoSignActivity isFinish;
    private LinearLayout related_drugs, video_sign_img1;
    private LinearLayout video_comment_list;
    private ListView video_menu_comment_list;// 评论数据加载
    public List<Video_menu_comment_entry> datalist = new ArrayList<Video_menu_comment_entry>();
    public List<ArticleCommentEntity> commentList = new ArrayList<ArticleCommentEntity>();
    public List<ArticleCommentEntity.ChildrenBean> chridCommentList = new ArrayList<ArticleCommentEntity.ChildrenBean>();
    BaseListAdapter2 baseListAdapter;
    BaseListAdapter2 baseListAdapter3;
    private Dialog dialog;
    private View layout_view;
    private String getAllReplyData_id = "";
    private String getAllReplyData_strid = "";
    private int getAllReplyData_position = 0;
    private boolean isNoMore = false, NETWORK_MOBILE_PAY = false;
    private FrameLayout video_frame;
    private long playprogress;
    private long myPlayprogress;
    private String relevantContent = "";
    private String isSign = "";
    private LinearLayout video_comment_dilong2;
    private LinearLayout video_icon_collection_layout1;
    private LinearLayout layout_vote;
    private LinearLayout videoexpert;
    private TextView zambia_text, video_sign1, video_sign2, video_sign3, video_sign4, video_sign_img2, video_sign_t1;
    private List<Map<String, Object>> list = new ArrayList();
    private int flag = 0;
    private boolean isClickSign = false;
    private List<CountDownTimer> timerList = new ArrayList<>();
    private TextView qbcomment, comment_num, video_menu_comment_item_id;
    private LinearLayout ll_comment_empty;
    private MyListView item_MyComment;// 单个评论 全部
    private CustomImageView video_menu_comment_item_img;//单个评论全部  顶层用户图片
    private TextView video_menu_comment_item_name; //单个评论全部 顶层用户名称
    private TextView video_menu_comment_item_times; //单个评论全部 顶层发布时间
    private TextView video_menu_comment_item_cont; //单个评论全部 顶层发布内容
    private TextView video_menu_comment_reply; //单个评论全部 顶层 回复他按钮
    private LinearLayout item_comment_layout; //单个评论全部 Layout
    private NestedScrollView nestedScrollView;
    private ImageView icon_x_comment;

    public static NiceVideoPlayer video_view2;
    List<Clarity> clarities = new ArrayList<>();
    private boolean pressedHome;
    private HomeKeyWatcher mHomeKeyWatcher;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //全屏后底部点赞view
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            //存贮对象声明(把视频ID存起来)
                            //  SharedPreferencesTools.saveVideoId(context, dataArray.getString("aid"));//存储ID
                            Aid = dataArray.getString("aid");
                            fid = dataArray.getString("fid");
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
//                            activity_keywords.setText(dataArray.getString("keywords"));
                            personalmoney = dataArray.getString("personalmoney");//用户收银台余额
                            videopaymoney = dataArray.getString("videopaymoney");//视频收费的金额（如果不为0代表改视频为收费）
                            vipflg_str = dataArray.getString("vipflg_str");//产品编号 （收费视频必传）
                            subject = dataArray.getString("subject");//产品编号 （收费视频必传）
                            flag = dataArray.getInt("flag");// 为3代表VIP视频  其他值不用管


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
                            videoSign();

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

                            video_sign_t1.setText(hits);

                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(picurl), video_play_img);
//                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(SharedPreferencesTools.getStricon(VideoSignActivity.this)), MyImg);
                            ImageLoader.getInstance().displayImage(FirstLetter.getSpells(SharedPreferencesTools.getStricon(VideoSignActivity.this)), MyImg2);
                            initFragment();
                            if (video_title != null && video_title.length() > 0) {
                                activity_title_name.setText(video_title);
                            } else {
                                activity_title_name.setText(R.string.activity_title_video);
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
//                            video_no_zambia_Text.setText(digg_num);
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
//                            video_no_zambia_Text.setText(digg_num);
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                                //第一步先查看石山视频ID 是否为空
                                if (my_video_id != null && my_video_id.length() > 0) {
                                    //不为空 则初始化石山视频播放器
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
                            } else
                                Toast.makeText(context, "视频资源正在修复，请稍候再试~", Toast.LENGTH_SHORT).show();
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
                                //第一步先查看石山视频ID 是否为空
                                if (my_video_id != null && my_video_id.length() > 0) {
                                    //不为空 则初始化石山视频播放器
                                    try {
                                        initPlayer();
                                    } catch (Exception e) {
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
                            } else
                                Toast.makeText(context, "视频资源正在修复，请稍候再试~", Toast.LENGTH_SHORT).show();
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
                                //ShareSDK.initSDK(VideoSignActivity.this);
                                final ShareDialog shareDialog = new ShareDialog(VideoSignActivity.this);
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
                                            sinaWeibo.setPlatformActionListener(VideoSignActivity.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(video_title);  //分享标题
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(VideoSignActivity.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(video_title);  //分享标题
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(VideoSignActivity.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(video_title);
                                            sp.setImageUrl(picurl);//网络图片rul
                                            sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(VideoSignActivity.this); // 设置分享事件回调
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(video_title);
                                            sp.setTitleUrl(dataurl); // 标题的超链接
                                            sp.setImageUrl(picurl);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(dataurl);
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(VideoSignActivity.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(dataurl.trim());
                                            Toast.makeText(VideoSignActivity.this, "复制成功", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(context, "视频资源正在修复，请稍候再试~", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(VideoSignActivity.this, VideoSignActivity.class);
                            intent.putExtra("aid", dataArray.getString("aid"));
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                            VideoSignActivity.isFinish.finish();
                        } else {// 失败
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 16://评论列表

                    if (currPage == 1 && commentList.size() < 1) {
//                        video_comment_list.setVisibility(View.GONE);
                    } else {
//                        video_comment_list.setVisibility(View.VISIBLE);
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
                        LogUtil.e("mason","添加新评论"+result);
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(VideoSignActivity.this,result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
//                            video_comment_list.setVisibility(View.VISIBLE);
                            ll_comment_empty.setVisibility(View.GONE);//评论成功，隐藏无评论界面
                            video_menu_comment_list.setVisibility(View.VISIBLE);
                            setcomment();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                            VideoFive.toast(context, result.getString("errorMessage"));
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                            final TextView nameView = (TextView) videoexpert.findViewById(R.id.name_view);
                            nameView.setTag(expert.getVideo_menu_expert_id());
                            nameView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (nameView.getText().toString() == "未知") {
                                        nameView.setClickable(false);
                                    } else {
                                        Intent intent = new Intent(VideoSignActivity.this, PlayVideo_expert.class);
                                        intent.putExtra("aid", v.getTag().toString());
                                        intent.putExtra("fid", fid);
                                        VideoSignActivity.this.startActivity(intent);
                                    }
                                }
                            });
                            if (!expert.getVideo_menu_expert_name().isEmpty()) {
                                nameView.setText(expert.getVideo_menu_expert_name());
                            } else {
                                nameView.setText("未知");
                            }
                            video_expert_layout.addView(videoexpert);
                        }
                    }
                    break;
                case 22:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 有权限
                            playVideo();
                        } else {
                            Toast.makeText(context, "该视频医院没有播放权限,请联系销售~", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 23:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        //视频签到参数
                        String type = jsonObject.has("type") ? jsonObject.getString("type") : "";
                        String sign = jsonObject.getString("sign");
                        String ex = getIntent().getStringExtra("expired");
                        sign_num = jsonObject.has("sign_num") ? jsonObject.getString("sign_num") : "0";
                        expired = ex.equals("1") ? getIntent().getStringExtra("expired") : "2";
                        is_miss = jsonObject.getString("is_miss");
                        String task_video_type = jsonObject.getString("task_video_type");
                        SharedPreferencesTools.saveSign_in_num(context, "0");
//                        Log.e("handleMessage", "handleMessage:expired: "+expired);
                        if (jsonObject.getInt("status") == 1) { // 成功
                            if(task_video_type.equals("1")){//task_video_type 代表任务类型  1：单签到  2：单测试  3：签到+测试
                                ll_sign1.setVisibility(View.VISIBLE);
                                video_sign_img1.setVisibility(View.GONE);
                            }else if(task_video_type.equals("2")){
                                ll_sign1.setVisibility(View.GONE);
                                video_sign_img1.setVisibility(View.VISIBLE);
                            }else if(task_video_type.equals("3")){
                                ll_sign1.setVisibility(View.VISIBLE);
                                video_sign_img1.setVisibility(View.VISIBLE);
                            }
                            if (sign.equals("0")) {//签到未完成
                                if (expired.equals("1")) {//已过期，不能签到
                                    Log.d("snow","已过期，不能签到");
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    ll_sign1.setClickable(false);
                                } else {
                                    if (type.equals("0")) {//任务未完成
                                        video_sign1.setText(jsonObject.getString("sign_num"));
                                        video_sign2.setText(jsonObject.getString("sign_num"));
                                        video_sign3.setText(jsonObject.getString("be_sign_num"));
                                        video_sign4.setText(jsonObject.getString("be_sign_num"));
                                        ll_sign1.setClickable(false);
                                        video_sign_img1.setClickable(false);
                                        video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
//                                        video_sign_img1.setVisibility(View.GONE);
                                        video_sign_img2.setVisibility(View.GONE);
                                    }
                                }
                            } else if (sign.equals("1")) {//签到完成
                                if (expired.equals("1")) {//已过期，不能签到
                                    video_sign1.setText(jsonObject.getString("sign_num"));
                                    video_sign3.setText(jsonObject.getString("be_sign_num"));
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    Log.d("snow","已过期，不能签到2");
                                    ll_sign1.setClickable(false);
                                    video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                    video_sign_img1.setClickable(false);
                                } else {
                                    if (type.equals("0")) {//单签到，任务完成
                                        video_sign1.setText(jsonObject.getString("sign_num"));
                                        video_sign2.setText(jsonObject.getString("sign_num"));
                                        video_sign3.setText(jsonObject.getString("be_sign_num"));
                                        video_sign4.setText(jsonObject.getString("be_sign_num"));
                                        ll_sign1.setClickable(false);
                                        video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                        video_sign_img1.setClickable(false);
                                       // video_sign_img1.setVisibility(View.GONE);
                                        video_sign_img2.setVisibility(View.GONE);
                                    } else if (type.equals("1")) { //签到完成，进入测试
                                        video_sign1.setText(jsonObject.getString("sign_num"));
                                        video_sign2.setText(jsonObject.getString("sign_num"));
                                        video_sign3.setText(jsonObject.getString("be_sign_num"));
                                        video_sign4.setText(jsonObject.getString("be_sign_num"));
                                        ll_sign1.setClickable(false);
//                                        video_sign_img1.setVisibility(View.VISIBLE);
                                        video_sign_img1.setClickable(true);
                                        video_sign_img1.setBackgroundResource(R.mipmap.learning_task04);
                                        video_sign_img1.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //进入测试
                                                Intent intent = new Intent(context, Examination_instructions2.class);
                                                intent.putExtra("aid", aid);
                                                intent.putExtra("tid", tid);
                                                intent.putExtra("type", "video");
                                                intent.putExtra("pptid", "");
                                                startActivity(intent);
                                            }
                                        });

                                        boolean isFullScreenSign = false;
                                        if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                            if (!mVideoView.isFullScreen()) {      //不是全屏
                                                isFullScreenSign = false;
                                            } else {
                                                isFullScreenSign = true;
                                            }
                                        } else {
                                            if (!video_view2.isFullScreen()) {      //不是全屏
                                                isFullScreenSign = false;
                                            } else {
                                                isFullScreenSign = true;
                                            }
                                        }
                                        if (!isFullScreenSign) {
                                            ll_video_sign.setVisibility(View.VISIBLE);
                                            video_sign_img2.setOnClickListener(new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //进入测试
                                                    Intent intent = new Intent(context, Examination_instructions2.class);
                                                    intent.putExtra("aid", aid);
                                                    intent.putExtra("tid", tid);
                                                    intent.putExtra("type", "video");
                                                    intent.putExtra("pptid", "");
                                                    startActivity(intent);
                                                }
                                            });
                                        } else {
                                            ll_sign1.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            } else if (sign.equals("2")) { //自测
                                if (expired.equals("1")) {//已过期，不能进入测试
                                    ll_sign1.setVisibility(View.GONE);
//                                    video_sign_img1.setVisibility(View.VISIBLE);
                                    video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                    video_sign_img1.setClickable(false);
                                } else {
                                    ll_sign1.setVisibility(View.GONE);
//                                    video_sign_img1.setVisibility(View.VISIBLE);
                                    video_sign_img1.setClickable(true);
                                    video_sign_img1.setBackgroundResource(R.mipmap.learning_task04);
                                    video_sign_img1.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //进入测试
                                            Intent intent = new Intent(context, Examination_instructions2.class);
                                            intent.putExtra("aid", aid);
                                            intent.putExtra("tid", tid);
                                            intent.putExtra("type", "video");
                                            intent.putExtra("pptid", "");
                                            startActivity(intent);
                                        }
                                    });
                                    video_sign_img2.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //进入测试
                                            Intent intent = new Intent(context, Examination_instructions2.class);
                                            intent.putExtra("aid", aid);
                                            intent.putExtra("tid", tid);
                                            intent.putExtra("type", "video");
                                            intent.putExtra("pptid", "");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                            isSign = jsonObject.getString("sign");
                            if (jsonObject.getString("sign_num").equals(jsonObject.getString("be_sign_num"))) {
                                //两个一致不需要去解析 签到点
                            } else {
                                //两个不一致 需要去解析 签到点
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                Iterator<String> keys = dataObject.keys();
                                while (keys.hasNext()) {
                                    Map<String, Object> map = new HashMap<>();
                                    String str = keys.next();
                                    map.put(str, dataObject.getString(str));
                                    list.add(map);
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 24:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {

                            SharedPreferencesTools.saveSign_in_num(context, "0");

                            ll_sign1.setVisibility(View.VISIBLE);
                            String type = jsonObject.getString("type");
                            String expired = getIntent().getStringExtra("expired");

                            boolean isFullScreenSign = false;
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                if (!mVideoView.isFullScreen()) {      //不是全屏
                                    isFullScreenSign = false;
                                } else {
                                    isFullScreenSign = true;
                                }
                            } else {
                                if (!video_view2.isFullScreen()) {      //不是全屏
                                    isFullScreenSign = false;
                                } else {
                                    isFullScreenSign = true;
                                }
                            }
                            if (!isFullScreenSign) {//不是全屏
                                if (type.equals("0")) {//不显示自测
                                    int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                                    if (num > Integer.parseInt(sign_num)) {
                                        video_sign3.setText(sign_num);
                                        video_sign4.setText(sign_num);
                                    } else {
                                        video_sign3.setText(num + "");
                                        video_sign4.setText(num + "");
                                    }
                                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                    ll_sign1.setClickable(false);
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    Log.d("snow","已过期，不能签到3");
                                    video_sign_img2.setVisibility(View.GONE);
                                } else if (type.equals("1")) {//显示自测
                                    int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                                    video_sign3.setText(num + "");
                                    video_sign4.setText(num + "");
                                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                    ll_sign1.setClickable(false);
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    Log.d("snow","已过期，不能签到4");
                                    video_sign_img1.setVisibility(View.VISIBLE);
                                    video_sign_img1.setClickable(true);
                                    video_sign_img1.setBackgroundResource(R.mipmap.learning_task04);
                                    video_sign_img1.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //进入测试
                                            Intent intent = new Intent(context, Examination_instructions2.class);
                                            intent.putExtra("aid", aid);
                                            intent.putExtra("tid", tid);
                                            intent.putExtra("type", "video");
                                            intent.putExtra("pptid", "");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } else {//全屏
                                if (type.equals("0")) {//不显示自测
                                    int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                                    video_sign3.setText(num + "");
                                    video_sign4.setText(num + "");
                                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    Log.d("snow","已过期，不能签到8");
                                    ll_sign1.setClickable(false);
                                    video_sign_img2.setVisibility(View.GONE);
                                    ll_video_sign.setVisibility(View.GONE);
                                } else if (type.equals("1")) {//显示自测
                                    int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                                    video_sign3.setText(num + "");
                                    video_sign4.setText(num + "");
                                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                    video_sign_img2.setVisibility(View.VISIBLE);
                                    video_sign_img2.setClickable(true);
                                    video_sign_img2.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //进入测试
                                            Intent intent = new Intent(context, Examination_instructions2.class);
                                            intent.putExtra("aid", aid);
                                            intent.putExtra("tid", tid);
                                            intent.putExtra("type", "video");
                                            intent.putExtra("pptid", "");
                                            startActivity(intent);
                                        }
                                    });
                                    ll_sign1.setClickable(false);
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                                    Log.d("snow","已过期，不能签到9");
                                    video_sign_img1.setVisibility(View.VISIBLE);
                                    video_sign_img1.setClickable(true);
                                    video_sign_img1.setBackgroundResource(R.mipmap.learning_task04);
                                    video_sign_img1.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //进入测试
                                            Intent intent = new Intent(context, Examination_instructions2.class);
                                            intent.putExtra("aid", aid);
                                            intent.putExtra("tid", tid);
                                            intent.putExtra("type", "video");
                                            intent.putExtra("pptid", "");
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                            signCount++;
                            if (timer != null) {
                                timer.cancel();
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
                case 25://一次性签到所用
                    try {
                        SharedPreferencesTools.saveSign_in_num(context, "0");
                        ll_sign1.setVisibility(View.VISIBLE);
                        String expired = getIntent().getStringExtra("expired");

                        boolean isFullScreenSign = false;
                        if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                            if (!mVideoView.isFullScreen()) {      //不是全屏
                                isFullScreenSign = false;
                            } else {
                                isFullScreenSign = true;
                            }
                        } else {
                            if (!video_view2.isFullScreen()) {      //不是全屏
                                isFullScreenSign = false;
                            } else {
                                isFullScreenSign = true;
                            }
                        }
                        if (!isFullScreenSign) {//不是全屏
                            int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                            if (num > Integer.parseInt(sign_num)) {
                                video_sign3.setText(sign_num);
                                video_sign4.setText(sign_num);
                            } else {
                                video_sign3.setText(num + "");
                                video_sign4.setText(num + "");
                            }
                            Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                            ll_sign1.setClickable(false);
                            ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                            Log.d("snow","已过期，不能签到5");
                            video_sign_img2.setVisibility(View.GONE);

                        } else {//全屏
                            int num = Integer.parseInt(video_sign3.getText().toString()) + 1;
                            video_sign3.setText(num + "");
                            video_sign4.setText(num + "");
                            Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                            ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
                            Log.d("snow","已过期，不能签到10");
                            ll_sign1.setClickable(false);
                            video_sign_img2.setVisibility(View.GONE);
                            ll_video_sign.setVisibility(View.GONE);
                        }
                        signCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 26:
                    //加载更多评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        LogUtil.e("mason","评论列表更多"+result);
                        if (result.getInt("status") == 1 && result.has("count")) {// 成功
                            qbcomment.setText("全部评论（" + result.getString("count") + "）");
                            comment_num.setText(result.getString("count"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    baseListAdapter.notifyDataSetChanged();
                    baseListAdapter3.notifyDataSetChanged();
                    isNoMore = false;
                    break;
                case 27://添加子评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
                            initcomment_new();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(VideoSignActivity.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 1001:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 2001:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 3001:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 4001:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 5001:
                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    CheckIntegral();
                    break;
                case 6001:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
                    Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    private NotSignDialog notSignDialog;
    private CountDownTimer timer;
    private CcmtvVideoPlayerController controller;
    private TelephonyManager telephony;
    private OnePhoneStateListener phoneStateListener;
    private boolean phoneTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_sign);
        videoSign = this;
        findId();
        initdata();
        setOnClick();
        initVideo_Play();
        initComment();
//        setrelevant();
//        setcomment();

        loadWeb();
        //启动时判断网络状态
        NETWORK = NetUtil.getNetWorkState(context);

        getExpertRulest();
        createPhoneListener();

        mHomeKeyWatcher = new HomeKeyWatcher(this);
        mHomeKeyWatcher.setOnHomePressedListener(new HomeKeyWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                pressedHome = true;
            }
        });
        pressedHome = false;
        mHomeKeyWatcher.startWatch();
        phoneTag = false;
    }

    public void initNiceVideo() {
        mVideoView = (VideoView) findViewById(R.id.video_view2);
        if (clarities.size() > 1) {
            video_view2.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
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
            controller = new CcmtvVideoPlayerController(this);
            controller.setTitle("");
            controller.setImage(R.mipmap.ys);
            video_view2.setController(controller);
        }

        if (!"smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
            ll_video_sign = (LinearLayout) controller.getLlVideoSign();
            ll_sign2 = (LinearLayout) controller.getLlsign2();
            video_sign2 = (TextView) controller.getVideoSign2();
            video_sign4 = (TextView) controller.getVideoSign4();
            video_sign_img2 = (TextView) controller.getVideoSignImg2();
        }
    }


    private void setOnClick() {
        networkText2.setOnClickListener(this);
        play_img.setOnClickListener(this);
        promptopen.setOnClickListener(this);
        promptopen_vip.setOnClickListener(this);
        video_icon_collection_img.setOnClickListener(this);
        video_header_comment_img.setOnClickListener(this);
        layout_praise.setOnClickListener(this);
        related_drugs.setOnClickListener(this);
        video_icon_collection_layout1.setOnClickListener(this);
        layout_vote.setOnClickListener(this);
        icon_x_comment.setOnClickListener(this);
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
                helper.setCommentAdapterDelOnClick3(R.id.video_menu_comment_delete, VideoSignActivity.this);
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

        //子评论
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
                helper.setImageBitmap2(R.id.video_menu_comment_item_img, ((ArticleCommentEntity.ChildrenBean) item).getIcon());
            }
        };


        item_MyComment.setAdapter(baseListAdapter3);
    }

    /**
     * name:设置listview中的值 author:Tom 2016-2-19下午2:11:17
     */
    public void setcomment() {
        //    Log.e("看看数据","进入到评论视频加载中");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getRecommentList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("page", 1);
                    obj.put("limit", COMMENT_LIMIT);
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this, URLConfig.CCMTVAPP, obj.toString());
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

    /***
     * 主评论+所有其子评论
     */
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
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this, URLConfig.CCMTVAPP, obj.toString());
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
    /*public void setrelevant() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoRelevant);
                    obj.put("aid", aid);
                    obj.put("page", 1);
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);
                    if (jsonresult.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonresult
                                .getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("departemnt_item_title", jsonObject.getString("title"));
                            map.put("department_id", jsonObject.getString("aid"));
                            map.put("department_on_demand", jsonObject.getString("hits") + "次");
                            map.put("department_times", jsonObject.getString("posttime"));
                            map.put("departemnt_item_img", jsonObject.getString("picurl"));
                            map.put("money", jsonObject.getString("money"));
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
                                    if (SharedPreferencesTools.getVipFlag(VideoSignActivity.this) == 1) {
                                        //VIP
                                        if (Integer.parseInt(((Map) item).get("money").toString()) > 0) {
                                            //会员
                                            helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                                            helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                                        } else {
                                            //免费
                                            helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                       *//* helper.setImage(R.id.departemnt_item_top_img, R.mipmap.free_img_blue);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);*//*
                                        }
                                    } else {
                                        //非vip
                                        if (Integer.parseInt(((Map) item).get("money").toString()) > 0) {
                                            //积分
                                            helper.setImage(R.id.departemnt_item_top_img, R.mipmap.integral_img);
                                            helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                                        } else {
                                            //免费
                                            helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                        *//*helper.setImage(R.id.departemnt_item_top_img, R.mipmap.free_img_blue);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);*//*
                                        }
                                    }
                                }


                            }
                        };


                                // listview点击事件
                        video_menu_relevant_list
                        .setOnItemClickListener(new casesharing_listListener());

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
    }*/

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
                    obj.put("uid", SharedPreferencesTools.getUid(VideoSignActivity.this));
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this, URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);
                    if (jsonresult.getInt("status") == 1) {// 成功
                        //  Log.i("VideoFive", "分享成功" + obj.toString() + "|" + result);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    //   System.out.println("加载数据出错了！");
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
            // MyProgressBarDialogTools.show(context);
            getVideoRulest(id);

        }

    }

    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoSignActivity.class);
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
        context = this;
        payBoolean_S = "";
        payBoolean = 1;
        isFinish = this;
        Intent intent = getIntent();
        aid = intent.getExtras().getString("aid");              //视频aid
        tid = intent.getExtras().getString("tid");              //视频tid
        videoaId = intent.getExtras().getString("aid");              //视频aid
        isVote = intent.getExtras().getBoolean("isVote", false);        //是否从投票过来
        votenum = intent.getExtras().getInt("voteNum");        //投票数量
        positions = intent.getExtras().getInt("positions");
        if (intent.getStringExtra("my_our_video").equals("my_our_video")) {
            ll_sign1.setVisibility(View.GONE);
            video_sign_img1.setVisibility(View.GONE);
        }
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.video_play_act);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    //    Log.e("看看视频数据1",obj.toString()+"-"+aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("看看视频数据2", result);
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
            video_icon_collection_layout1.setVisibility(View.GONE);
//            video_icon_download_layout.setVisibility(View.GONE);
            layout_praise.setVisibility(View.GONE);
            layout_vote.setVisibility(View.VISIBLE);
            tv_votenum.setText(votenum + "票");
        } else {
            video_icon_collection_layout1.setVisibility(View.VISIBLE);
//            video_icon_download_layout.setVisibility(View.VISIBLE);
            layout_praise.setVisibility(View.VISIBLE);
            layout_vote.setVisibility(View.GONE);
        }

        if (intent.getExtras().getString("videoClass") == null) {
            videoClass = "其 他";
        }

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
     * 获取视频播放权限
     */
    public void getVideoPlayPermission() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getVideoPower);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看videosign播放权限数据：", result);

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
        NETWORK = NetUtil.getNetWorkState(context);
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
            MyLogger.w(LOG_TAG, "Exception:", e);
        }
        /*===============视频所需end======================*/
    }


    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {
        arrow_back2 = (LinearLayout) findViewById(R.id.arrow_back2);
        video_frame = (FrameLayout) findViewById(R.id.video_frame);
        video_menu_comment_list = (ListView) findViewById(R.id.video_menu_comment_list);
        hearderViewLayout = (LinearLayout) View.inflate(this, R.layout.video_sign_listview_header2, null);
        list_buttom = (LinearLayout) View.inflate(this, R.layout.list_button, null);
        video_menu_comment_list.addHeaderView(hearderViewLayout);
        video_menu_comment_list.setVerticalScrollBarEnabled(true);//不管有没有活动都隐藏
        activity_title_name = (FocuedTextView) findViewById(R.id.activity_title_name);
        promptLinearLayout = (LinearLayout) findViewById(R.id.promptLinearLayout);
        related_drugs = (LinearLayout) findViewById(R.id.related_drugs);
        video_play_img = (ImageView) findViewById(R.id.video_play_img);
        video_play_layout = (FrameLayout) findViewById(R.id.video_play_layout);
        play_img = (ImageView) findViewById(R.id.play_img);
        promptText1 = (TextView) findViewById(R.id.promptText1);
        promptText2 = (TextView) findViewById(R.id.promptText2);
        promptopen = (Button) findViewById(R.id.promptopen);
        promptopen_vip = (Button) findViewById(R.id.promptopen_vip);
        video_play_count_text = (TextView) findViewById(R.id.video_play_count_text);
        //收藏
        video_icon_collection_img = (ImageView) findViewById(R.id.video_icon_collection_img);
        //赞
        video_zambia_img = (ImageView) findViewById(R.id.video_no_zambia_img);
        video_header_comment_img = (ImageView) findViewById(R.id.video_header_comment_img);
        video_menu_comment_edittext_f = (TextView) findViewById(R.id.video_menu_comment_edittext_f);
        video_menu_comment_edittext_ff = (TextView) findViewById(R.id.video_menu_comment_edittext_ff);
        video_abstract = (WebView) findViewById(R.id.video_abstract);
        video_comment_dilong = (LinearLayout) findViewById(R.id.video_comment_dilong);
        video_no_zambia_Text = (TextView) findViewById(R.id.video_no_zambia_Text);
//        MyImg = (ImageView) findViewById(R.id.MyImg);
        MyImg2 = (ImageView) findViewById(R.id.MyImg2);
        networkLinearLayout = (LinearLayout) findViewById(R.id.networkLinearLayout);
        video_expert_layout = (LinearLayout) findViewById(R.id.video_expert_layout);
        tv_votenum = (TextView) findViewById(R.id.tv_votenum);
        layout_praise = (LinearLayout) findViewById(R.id.layout_praise);
        video_sign_t1 = (TextView) findViewById(R.id.video_sign_t1);
        networkText1 = (TextView) findViewById(R.id.networkText1);
        networkText2 = (TextView) findViewById(R.id.networkText2);
        video_comment_list = (LinearLayout) findViewById(R.id.video_comment_list);
        video_comment_dilong2 = (LinearLayout) findViewById(R.id.video_comment_dilong2);
        video_icon_collection_layout1 = (LinearLayout) findViewById(R.id.video_icon_collection_layout1);
        zambia_text = (TextView) findViewById(R.id.zambia_text);
        layout_vote = (LinearLayout) findViewById(R.id.layout_vote);
        video_sign1 = (TextView) findViewById(R.id.video_sign1);
        video_sign2 = (TextView) findViewById(R.id.video_sign2);
        video_sign3 = (TextView) findViewById(R.id.video_sign3);
        video_sign4 = (TextView) findViewById(R.id.video_sign4);
        video_sign_img1 = (LinearLayout) findViewById(R.id.video_sign_img1);
        video_sign_img2 = (TextView) findViewById(R.id.video_sign_img2);
        ll_video_sign = (LinearLayout) findViewById(R.id.ll_video_sign);
        ll_sign1 = (LinearLayout) findViewById(R.id.ll_sign1);//签到
        ll_sign2 = (LinearLayout) findViewById(R.id.ll_sign2);
        video_view2 = (NiceVideoPlayer) findViewById(R.id.video_view3);

        qbcomment = (TextView) findViewById(R.id.qbcomment);
        comment_num = (TextView) findViewById(R.id.comment_num);
        ll_comment_empty = (LinearLayout) findViewById(R.id.ll_comment_empty);
        video_menu_comment_item_img = (CustomImageView) findViewById(R.id.video_menu_comment_item_img);
        video_menu_comment_item_name = (TextView) findViewById(R.id.video_menu_comment_item_name);
        video_menu_comment_item_times = (TextView) findViewById(R.id.video_menu_comment_item_times);
        video_menu_comment_reply = (TextView) findViewById(R.id.video_menu_comment_reply);
        video_menu_comment_item_cont = (TextView) findViewById(R.id.video_menu_comment_item_cont);
        item_comment_layout = (LinearLayout) findViewById(R.id.item_comment_layout);
        video_menu_comment_item_id = (TextView) findViewById(R.id.video_menu_comment_item_id);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        item_MyComment = (MyListView) findViewById(R.id.item_MyComment);
        icon_x_comment = (ImageView) findViewById(R.id.icon_x_comment);
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
            case R.id.networkText2:     //网络波动 视频播放提醒
                //启动时判断网络状态
                if (NETWORK == NetUtil.NETWORK_NONE) {
                    //没有网络
                    //   Log.e("网络", "没有网络");
                } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
                    //移动网络】、
                    //  Log.e("网络", "移动网络");
                    if (mVideoView != null) {
                        if (mVideoView.getDuration() > 0) {
//                            Log.e("网络", "networkText2点击继续播放"+mVideoView.getId());
                            networkLinearLayout.setVisibility(View.GONE);
                            video_play_layout.setVisibility(View.GONE);
                            video_frame.setVisibility(View.VISIBLE);
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                mVideoView.start();
                            } else {
                                initPlayer2();
                            }
//                            Log.e("网络", "networkText2点击继续播放"+mVideoView.getCurrentStatus()+"视频长度："+mVideoView.getDuration());
                            NETWORK_MOBILE_PAY = true;
                        } else {
//                            Log.e("网络", "networkText2点击继续播放 ,mVideoView未初始化成功过，重新初始化");
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
                    if (mVideoView != null) {
                        //        Log.e("网络", "WIFI");
                        networkLinearLayout.setVisibility(View.GONE);
                        video_play_layout.setVisibility(View.GONE);
                        video_frame.setVisibility(View.VISIBLE);
                        if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                            mVideoView.start();
                        } else {
                            initPlayer2();
                        }
                    } else {
                        Message message = new Message();
                        message.what = 12;
                        handler.sendMessage(message);
                    }

                }
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
                // getIntegraldDeductionRulest();
                //判断视频播放权限 status: 0 没有权限 1 有权限
                try {
                    getVideoPlayPermission();
                    //视频签到开始计时
                    if (isSign != "1") {
                        startTiming();
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
                    Intent intent = new Intent(VideoSignActivity.this, Get_Free_Integral.class);
                    intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(VideoSignActivity.this));
                    intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(VideoSignActivity.this));
                    intent.putExtra("Str_username", SharedPreferencesTools.getUserName(VideoSignActivity.this));
                    startActivity(intent);
                    finish();
                } else if (view.getText().equals("余额支付")) {
                    getBalance_paymentRulest();
                }
                break;
            case R.id.promptopen_vip:
                TextView view1 = (TextView) v;
                if (view1.getText().equals("在线支付")) {
                    Intent intent = new Intent(VideoSignActivity.this, PaymentActivity.class);
                    intent.putExtra("subject", subject);
                    //测试变为0.01
                    // intent.putExtra("money", money);
                    intent.putExtra("money", videopaymoney);
                    intent.putExtra("vipflg_str", vipflg_str);
                    intent.putExtra("payfor", "VideoFive");
                    intent.putExtra("aid", aid);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(VideoSignActivity.this, MyOpenMenberActivity.class);
                    intent.putExtra("Str_vipflg", SharedPreferencesTools.getVipFlag(VideoSignActivity.this));
                    intent.putExtra("Str_icon", SharedPreferencesTools.getStricon(VideoSignActivity.this));
                    intent.putExtra("Str_username", SharedPreferencesTools.getUserName(VideoSignActivity.this));
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
            case R.id.video_icon_collection_layout1:
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
            case R.id.layout_praise:                //点赞
                final String flg1 = video_zambia_img.getTag().toString();
                video_zambia_img.setClickable(false);
                Runnable runnable1 = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.doVideo);
                            obj.put("uid", SharedPreferencesTools.getUid(VideoSignActivity.this));
                            obj.put("aid", aid);
                            obj.put("flg", flg1);
                            String result = HttpClientUtils.sendPost(VideoSignActivity.this, URLConfig.CCMTVAPP, obj.toString());

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

            default:
                break;

        }
    }

    /**
     * 计时器,视频计时点
     */
    private void startTiming() {
        for (Map<String, Object> map : list) {
            for (String str : map.keySet()) {
                if (map.containsKey(str)) {
                    final double d = Double.parseDouble(map.get(str).toString());
                    final long l = (long) (1000 * d);
                    final String signnum_text = str;
                    CountDownTimer timer = new CountDownTimer(l, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            boolean isFullScreenSign = false;
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                                if (!mVideoView.isFullScreen()) {      //不是全屏
                                    isFullScreenSign = false;
                                } else {
                                    isFullScreenSign = true;
                                }
                            } else {
                                if (!video_view2.isFullScreen()) {      //不是全屏
                                    isFullScreenSign = false;
                                } else {
                                    isFullScreenSign = true;
                                }
                            }
                            if (!isFullScreenSign) {//不是全屏
                                Sign_num_record(signnum_text,formatTime(Float.valueOf(String.valueOf(d))));
                                ll_sign1.setBackgroundResource(R.mipmap.learning_task04);
                                ll_sign1.setClickable(true);
                                ll_video_sign.setVisibility(View.GONE);
//                                video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_sign1.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean isfast=checkDoubleClick();
                                        if(isfast==false){
                                            ll_sign1.setClickable(false);
                                            takeVideoSign();
                                            signnum = signnum_text;
                                        }
                                    }
                                });
                                ll_sign2.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        takeVideoSign();
                                        signnum = signnum_text;
                                    }
                                });
                            } else {//全屏
                                ll_sign1.setBackgroundResource(R.mipmap.learning_task04);
                                ll_sign1.setClickable(true);
//                                video_sign_img1.setClickable(true);
//                                video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_video_sign.setVisibility(View.VISIBLE);
                                ll_sign1.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean isfast=checkDoubleClick();
                                        if(isfast==false){
                                            takeVideoSign();
                                            signnum = signnum_text;
                                        }

                                    }
                                });
                                ll_sign2.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        takeVideoSign();
                                        signnum = signnum_text;
                                    }
                                });
                            }
                            CountDownTimer timer1 = new CountDownTimer(60 * 1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
//                                    video_sign_img1.setClickable(false);
                                    ll_video_sign.setVisibility(View.GONE);
                                }
                            }.start();
                            timerList.add(timer1);
                        }
                    }.start();
                    timerList.add(timer);
                }
            }

        }
    }

    private void Sign_num_record(final String sign_count,final String v_light_time){
        final String tid = getIntent().getStringExtra("tid");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.signLight);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("aid", aid);
                    obj.put("signnum",sign_count);
                    obj.put("v_light_time",v_light_time);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Video_sign_light, obj.toString());

                   Log.e("看看videosign数据:result", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    private JSONObject Json = new JSONObject();
    private int signCount = 0;
    private String is_miss = "0";

    private void takeVideoSign() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /*try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.taskVideoSign);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("aid", aid);
                    obj.put("signnum", signnum);
                    Log.e("看看sign数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
                    Log.e("看看sign数据", result);

                    Message message = new Message();
                    message.what = 24;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }*/

                try {

                    Json.put(signCount + "", System.currentTimeMillis());//JSONObject对象中添加键值对
                    //SharedPreferencesTools.saveSign_Time_Json(context, Json.toString());
                    if (is_miss.equals("0")) {
                        //String result = SendSignInfo.sendSign(VideoSignActivity.this, tid, aid, signnum);
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", aid);
                        obj.put("signnum", signnum);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);
                        Message message = new Message();
                        message.what = 24;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else if (signCount >= Integer.parseInt(sign_num) - 1) {
                        JSONArray jsonArraySignTime = new JSONArray();
                        jsonArraySignTime.put(Json);//将JSONObject对象添加到Json数组中
                        //String result = SendSignInfo.sendSign(VideoSignActivity.this, tid, aid, jsonArraySignTime.toString());
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", aid);
                        obj.put("signnum", jsonArraySignTime);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);

                        Message message = new Message();
                        message.what = 24;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 25;
                        message.obj = 0;
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

    private void videoSign() {
        final String tid = getIntent().getStringExtra("tid");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getSignNum);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("aid", aid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看videosign数据:result", result);

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
     * 观看视频5分钟，获取经验值
     */
    public void videoGrowth(final long playprogress) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
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
                    obj.put("uid", SharedPreferencesTools.getUid(VideoSignActivity.this));
                    obj.put("username", SharedPreferencesTools.getUserName(VideoSignActivity.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this,
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

    public void getcommentStumit2(final View view, final String content, final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addRecomment);
                    obj.put("uid", SharedPreferencesTools.getUid(VideoSignActivity.this));
                    obj.put("username", SharedPreferencesTools.getUserName(VideoSignActivity.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 27;
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
     * 回复评论
     * @param view
     * @param hint
     * @param type   1主评论（一级评论）0子评论（n级评论>1）
     */
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
                                Toast.makeText(VideoSignActivity.this, "内容不能为空~", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(VideoSignActivity.this, "内容不能为空~", Toast.LENGTH_SHORT).show();
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

    public void toAbstract(View v) {
        Intent intent = new Intent(VideoSignActivity.this, PlayVideo_abstract.class);
        intent.putExtra("aid", aid);
        intent.putExtra("fid", fid);
        startActivity(intent);
    }


    public void back(View view) {
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
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (mVideoView != null) {
                if (mVideoView.rotatedFromBtn()) {
                    if (mVideoView.isFullScreen()) {
                        //变小屏播放
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        //变大屏播放
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    return;
                } else {
                    if (mVideoView.isFullScreen()) {
                        //变小屏播放
                        runOnUiThread(new Runnable() {
                            public void run() {
                                arrow_back2.setVisibility(View.VISIBLE);
                            }
                        });
                        mVideoView.toMiniScreen();
                    } else {
                        //变大屏播放
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
            MyLogger.w(LOG_TAG, "onConfigurationChanged exception");
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
            return new AlertDialog.Builder(getActivity()).setTitle(title).setSingleChoiceItems(definitions, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index = which;
                }
            }).setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String definition = mInformation.getDefinitionEN(getActivity(), definitions[index]);
                        ((VideoSignActivity) getActivity()).download(definition);
                        dialog.dismiss();
                    } catch (Exception e) {
                        MyLogger.w(LOG_TAG, "fragment_download exception", e);
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
            MyLogger.w(LOG_TAG, "fragment_download exception", e);
        }
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
//        video_download_img.setTag("down");

    }

    /**
     * 将视频放入播放器
     */
    private void initPlayer2() {

//        LogUtil.e("视频播放数据",clarities.size()+"");
//        LogUtil.e("视频播放数据",clarities.get(0).videoUrl);
        play_img.setVisibility(View.GONE);
        mVideoView.setVisibility(View.GONE);
        video_view2.setVisibility(View.VISIBLE);
        promptLinearLayout.setVisibility(View.GONE);
        video_play_layout.setVisibility(View.GONE);
        video_frame.setVisibility(View.VISIBLE);
//        video_view2.start();

        if (getIntent().getStringExtra("my_our_video").equals("my_our_video")) {
            video_view2.start();
            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                mVideoView.showProgressBar(true);
            } else {
                controller.showSeekBar();
            }
        } else {
            video_view2.start(0L);
            video_view2.continueFromLastPosition(false);
            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                mVideoView.showProgressBar(false);
            } else {
                controller.hideSeekBar();
            }
        }

    }

    /**
     * 将视频放入播放器
     */
    private void initPlayer() {
        try {
            final String videoId = my_video_id;
            if (videoId == null || TextUtils.isEmpty(videoId)) {
                MyLogger.w(LOG_TAG, "videoId can't be null or empty");
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
            mVideoView = (VideoView) findViewById(R.id.video_view2);
            //续播弹框
          /*  if (MyDbUtils.findRecordVideo(context, my_video_id) > 0) {

                final Dialog dialog = new Dialog(VideoSignActivity.this, R.style.ActionSheetDialogStyle);
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
                        mVideoView.showProgressBar(false);
                        mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
                        mVideoView.seekToPosition(MyDbUtils.findRecordVideo(context, my_video_id) * 1000);
                        dialog.dismiss();
                    }
                });
                tv_canal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mVideoView.showProgressBar(false);
                        mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
                    }
                });
//                dialog.show();
            } else {
                mVideoView.showProgressBar(false);
                mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
            }*/
            if (getIntent().getStringExtra("my_our_video").equals("my_our_video")) {
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    mVideoView.showProgressBar(true);
                } else {
                    controller.showSeekBar();
                }
            } else {
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    mVideoView.showProgressBar(false);
                } else {
                    controller.hideSeekBar();
                }
            }
            mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
            mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    NotSignShowDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        isFinish = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ll_video_sign.setVisibility(View.GONE);

        if (!phoneTag) {
            if (mVideoView != null) {
                mVideoView.onActivityResume();
                if (mVideoView.isFullScreen()) {
                    //变小屏
                    VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    ll_video_sign.setVisibility(View.GONE);
                }
            }
        }
//        initdata();
        //加载评论
        setcomment();
        //
        registerReceiver();
        if (!phoneTag) {

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
        } else {
            if (mVideoView != null && mVideoView.isPlaying()) {
                if ("smvp".equals(videoplaystyle)) {
                    mVideoView.pause();
                }
                mVideoView.onActivityResume();
            }

            if (video_view2 != null && video_view2.isPlaying()) {
                if (!"smvp".equals(videoplaystyle)) {
                    video_view2.pause();
                }
            }
            phoneTag = false;
        }
    }


    @Override
    protected void onPause() {
        //enterUrl = "http://yy.ccmtv.cn/Video/play?aid=" + Aid;
        if (notSignDialog != null) {
            notSignDialog.dismiss();
        }
        LogUtil.e("mason","onPause   "+phoneTag);
        if (!phoneTag) {
            unregisterReceiver(mProgressRecevier);

        }
        super.onPause();
        if (mVideoView != null) {
            mVideoView.onActivityPause();
        }
        MyProgressBarDialogTools.hide();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在OnStop中是release还是suspend播放器，需要看是不是因为按了Home键
        LogUtil.e("mason","onStop   "+phoneTag);
        if (!phoneTag) {
            if (pressedHome) {
                NiceVideoPlayerManager.instance().suspendNiceVideoPlayer();
            } else {
                NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
            }
            mHomeKeyWatcher.stopWatch();
        }
    }

    @Override
    protected void onRestart() {
        mHomeKeyWatcher.startWatch();
        pressedHome = false;
        super.onRestart();
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer();
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
            return formatter.format("%d:%02d:%02d", minutes, seconds).toString();
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
                videoGrowth(myPlayprogress);
            }
            MyDbUtils.saveRecordVideo(context, my_video_id, playprogress);
            String text = getString(R.string.current_play_progress, formatTime(progress));
            //停止播放音频
            MainActivity.stopFloatingView(context);
            boolean isFullScreenSign = false;
            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                if (!mVideoView.isFullScreen()) {      //不是全屏
                    isFullScreenSign = false;
                } else {
                    isFullScreenSign = true;
                }
            } else {
                if (!video_view2.isFullScreen()) {      //不是全屏
                    isFullScreenSign = false;
                } else {
                    isFullScreenSign = true;
                }
            }
            if (!isFullScreenSign) {
                ll_video_sign.setVisibility(View.GONE);
            }

            //判断是不是从已结束进入
//            String expired = getIntent().getStringExtra("expired");
            if (expired.equals("1")) {//没有签到功能

            } else {
                //判断当前时间到签到点没有
                for (Map<String, Object> map : list) {
                    for (String str : map.keySet()) {
                        if (map.containsKey(str)) {
                            float sign_time = Float.parseFloat(map.get(str).toString());
                            final String signnum_text = str;
                            Log.d("mason","sign time ----"+formatTime(sign_time));
                            if (formatTime(progress).equals(formatTime(sign_time))) {
                                if (!isFullScreenSign) {//不是全屏
                                    ll_sign1.setBackgroundResource(R.mipmap.learning_task04);
                                    ll_sign1.setClickable(true);
                                    ll_video_sign.setVisibility(View.GONE);
//                                video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                    ll_sign1.setOnClickListener(new IClickListener() {
                                        @Override
                                        public void onIClick(View v) {
                                            isClickSign = true;
                                            takeVideoSign();
                                            signnum = signnum_text;

                                        }
                                    });
                                    ll_sign2.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isClickSign = true;
                                            takeVideoSign();
                                            signnum = signnum_text;
                                        }
                                    });
                                } else {//全屏
                                    ll_sign1.setBackgroundResource(R.mipmap.learning_task04);
                                    ll_sign1.setClickable(true);
//                                video_sign_img1.setClickable(true);
//                                video_sign_img1.setBackgroundResource(R.mipmap.video_sign2);
                                    ll_video_sign.setVisibility(View.VISIBLE);
                                    ll_sign1.setOnClickListener(new IClickListener() {
                                        @Override
                                        public void onIClick(View v) {
                                            isClickSign = true;
                                            takeVideoSign();
                                            signnum = signnum_text;
                                        }
                                    });
                                    ll_sign2.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isClickSign = true;
                                            takeVideoSign();
                                            signnum = signnum_text;
                                        }
                                    });
                                }
                                //                                    video_sign_img1.setClickable(false);
                                /*if (timer!=null){
                                    timer.cancel();
                                    NotSignShowDialog();
                                }*/
                                SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) + 1) + "");
                                timer = new CountDownTimer(60 * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        NotSignShowDialog();
                                        if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(VideoSignActivity.this)) > 0) {//保存签到数量
                                            SharedPreferencesTools.saveSign_in_num(VideoSignActivity.this, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(VideoSignActivity.this)) - 1) + "");
                                        } else {
                                            SharedPreferencesTools.saveSign_in_num(VideoSignActivity.this, "0");
                                        }
                                        if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(VideoSignActivity.this)) < 1) {
                                            Log.d("snow","onFinish");
//                                            ll_sign1.setBackgroundResource(R.mipmap.video_sign2);
//                                    video_sign_img1.setClickable(false);
                                            ll_video_sign.setVisibility(View.GONE);
                                        }
                                    }
                                }.start();
                                timerList.add(timer);
                            }
                        }
                    }
                }
            }

            //这是要设置是否需要收费 如果需要收费那么就 收费
            //判断该视频是收费视频才监听
            /*if (!"0".equals(videopaymoney)) {
                //小屏就显示按钮
                if (payBoolean > 0) {
                    if (progress > 50) {
                        mVideoView.pause();
                        //先判断是否全屏 如果是全屏就变小屏
                        if (mVideoView.isFullScreen()) {
                            //变小屏
                            VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
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
            }*/

            NETWORK = NetUtil.getNetWorkState(VideoSignActivity.this);
            previewNetwork = NETWORK;
            if (NETWORK == NetUtil.NETWORK_NONE) {

                //没有网络
                //  Log.e("网络", "没有网络");
                if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                    mVideoView.pause();
                } else {
                    video_view2.pause();
                }

                //先判断是否全屏 如果是全屏就变小屏
                if (isFullScreenSign) {
                    //变小屏
                    VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失

                    } else {
                        video_view2.exitFullScreen();
                    }
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
                    if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                        mVideoView.pause();
                    } else {
                        video_view2.pause();
                    }
                    //先判断是否全屏 如果是全屏就变小屏
                    if (isFullScreenSign) {
                        //变小屏
                        VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
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

            } else if (NETWORK == NetUtil.NETWORK_WIFI && previewNetwork != NetUtil.NETWORK_WIFI) {
                //WIFI
                //   Log.e("网络", "WIFI");
                Message message = new Message();
                message.what = 12;
                handler.sendMessage(message);
            }
        }
    }

    private void NotSignShowDialog() {
        if (!isClickSign && is_miss.equals("1")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (notSignDialog == null) {
                            notSignDialog = new NotSignDialog(VideoSignActivity.this);
                        }
                        notSignDialog.show();
                        notSignDialog.setCancelButtonOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notSignDialog.dismiss();
                            }
                        });
                        notSignDialog.setSureButtonOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    notSignDialog.dismiss();
                                    Intent intentOld = getIntent();
                                    aid = intentOld.getExtras().getString("aid");              //视频aid
                                    tid = intentOld.getExtras().getString("tid");              //视频tid
                                    videoaId = intentOld.getExtras().getString("aid");              //视频aid
                                    isVote = intentOld.getExtras().getBoolean("isVote", false);        //是否从投票过来
                                    votenum = intentOld.getExtras().getInt("voteNum");        //投票数量
                                    positions = intentOld.getExtras().getInt("positions");
                                    String myOurVideo = intentOld.getStringExtra("my_our_video");
                                    Intent intent = new Intent(context, VideoSignActivity.class);
                                    /*intent.putExtra("aid", aid);
                                    intent.putExtra("tid", tid);
                                    intent.putExtra("aid", videoaId);
                                    intent.putExtra("isVote", isVote);
                                    intent.putExtra("voteNum", votenum);
                                    intent.putExtra("positions", positions);
                                    intent.putExtra("my_our_video", myOurVideo);
                                    intent.putExtra("expired", getIntent().getStringExtra("expired"));*/

                                    intent.putExtra("aid", aid);
                                    intent.putExtra("tid", tid);
                                    intent.putExtra("expired", expired);
                                    intent.putExtra("my_our_video", "videosign");
                                    startActivity(intent);
                                    videoSign.finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            isClickSign = false;
//            Log.e("VideoSignActivity", "onFinish: 未签到");
        } else {
            isClickSign = false;
        }
    }

    public void onNetChange(int netMobile) {
        //网络状态变化时的操作
        NETWORK = netMobile;
        if (NETWORK == NetUtil.NETWORK_NONE) {
            Message message = new Message();
            message.what = 20;
            handler.sendMessage(message);
        } else if (NETWORK == NetUtil.NETWORK_MOBILE) {
            Message message = new Message();
            message.what = 19;
            handler.sendMessage(message);
        }
        if (NETWORK == NetUtil.NETWORK_WIFI) {
            //WIFI
            //   Log.e("网络", "WIFI");
            //当前网络为wifi 判断当前该视频是否是播放了 是不是播放中网络波动过还是用户自己点击的暂停
            if (View.VISIBLE == networkLinearLayout.getVisibility()) {
                if (mVideoView != null) {
                    networkLinearLayout.setVisibility(View.GONE);
                    video_play_layout.setVisibility(View.GONE);
                    video_frame.setVisibility(View.VISIBLE);
                    if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失
                        mVideoView.start();
                    } else {
                        initPlayer2();
                    }
                } else {
                    Message message = new Message();
                    message.what = 12;
                    handler.sendMessage(message);
                }
            }
        }

    }


    @Override
    public void onDownloadBtnClick() {

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
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
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
        if (notSignDialog != null) {
            notSignDialog.dismiss();
        }
        for (int i = 0; i < timerList.size(); i++) {
            if (timerList.get(i) != null) {
                timerList.get(i).cancel();
            }
        }

        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        MyDbUtils.updateRecordVideo(context, my_video_id, playprogress);
    }


    public void getExpertRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getVideoExpert);
                    obj.put("aid", videoaId);
                    String result = HttpClientUtils.sendPost(VideoSignActivity.this, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("VideoSignActivity", "getExpertRulest: "+result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject.getJSONArray("data");
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


    public void loadWeb() {
        video_abstract.loadUrl("http://www.ccmtv.cn/do/ccmtvappandroid/getVideoRemark.php?" + aid);
        video_abstract.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    public abstract class IClickListener implements View.OnClickListener {
        private long mLastClickTime = 0;
        public static final int TIME_INTERVAL = 1000;

        @Override
        public final void onClick(View v) {
            if (System.currentTimeMillis() - mLastClickTime >= TIME_INTERVAL) {
                onIClick(v);
                mLastClickTime = System.currentTimeMillis();
            }
        }

        protected abstract void onIClick(View v);
    }

    /**
     * 按钮-监听电话
     */
    public void createPhoneListener() {
        telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new OnePhoneStateListener();
        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 电话状态监听.
     *
     * @author stephen
     */
    private class OnePhoneStateListener extends PhoneStateListener {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    phoneTag = true;
                case TelephonyManager.CALL_STATE_IDLE:
                case TelephonyManager.CALL_STATE_OFFHOOK://接听
                    if (mVideoView != null && mVideoView.isPlaying()) {
                        if ("smvp".equals(videoplaystyle)) {
                            mVideoView.pause();
                        }
                        mVideoView.onActivityResume();
                        if (mVideoView.isFullScreen()) {
                            //变小屏
                            VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                            ll_video_sign.setVisibility(View.GONE);
                        }
                    }

                    if (video_view2 != null && video_view2.isPlaying()) {
                        if (!"smvp".equals(videoplaystyle)) {
                            video_view2.pause();
                        }
                        if (video_view2.isFullScreen()) {
                            //变小屏
                            VideoSignActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                            if ("smvp".equals(videoplaystyle)) {            //smvp 为石山播放器  ccmtv为本地播放器noresource无数据及视频资源丢失

                            } else {
                                video_view2.exitFullScreen();
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    arrow_back2.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
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
}


//7.0以上系统  会弹出2次框  为避免
              /*   if (getSDKVersionNumber() >= 24) {
                    mVideoView.showProgressBar(true);
                    mVideoView.playVideo(videoManager, videoId, LocalConstants.CHARGE_PLAYER_ID, defaultDefinition);
                } else {   } */

/* public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }*/