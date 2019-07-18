package com.linlic.ccmtv.yx.activity.medical_database;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.entity.ArticleCommentEntity;
import com.linlic.ccmtv.yx.activity.entity.Video_menu_comment_entry2;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_expert;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.adapter.Video_Experts_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.BaseListAdapter2;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.kzbf.activity.BigImageActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineDetialActivity;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.linlic.ccmtv.yx.widget.NoScrollWebView;
import com.tencent.connect.share.QQShare;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
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
 * Created by Administrator on 2018/11/23.
 */

public class Article_details extends BaseActivity implements PlatformActionListener {
    private static final int COMMENT_LIMIT = 10;//每页显示多少评论
    private Context context;
    //评论每页显示多少条
//    final String uid = SharedPreferencesTools.getUid(this);
    private WebSettings ws;
    @Bind(R.id.share_layout)
    LinearLayout share_layout;
    @Bind(R.id.detial_content)
    FrameLayout detial_content;
    @Bind(R.id.video_view)
    FrameLayout video_view;
    @Bind(R.id.viewing_tilte)//文件名字
            TextView viewing_tilte;
    @Bind(R.id._icon)//用户头像
            CircleImageView _icon;
    @Bind(R.id.viewing_name)//发布者
            TextView viewing_name;
    @Bind(R.id.viewing_time)//发布时间
            TextView viewing_time;
    @Bind(R.id.viewing_text)//WEBView
            NoScrollWebView viewing_text;
    @Bind(R.id.file_layout)//文件 容器区域
            LinearLayout file_layout;
    @Bind(R.id.relevant_list_layout)//更多推荐 容器区域
            LinearLayout relevant_list_layout;
    @Bind(R.id.zambia_num)//点赞数
            TextView zambia_num;
    @Bind(R.id.medicine_detial_eye_num)//阅读数
            TextView medicine_detial_eye_num;
    @Bind(R.id.collection_num)//收藏数
            TextView collection_num;
    @Bind(R.id.video_no_zambia_img)//点赞图标
            ImageView video_no_zambia_img;
    @Bind(R.id.medicine_detial_eye_img)//阅读量图片
            ImageView medicine_detial_eye_img;
    @Bind(R.id.video_icon_collection_img)//收藏图标
            ImageView video_icon_collection_img;
    @Bind(R.id.auto_wrap_line_layout)//关键词
            AutoWrapLineLayout auto_wrap_line_layout;
    @Bind(R.id.video_Experts_list)//作者
            MyGridView video_Experts_list;
    @Bind(R.id.video_Expert_list)//作者layout
            LinearLayout video_Expert_list;
    @Bind(R.id.video_menu_relevant_list)//更多推荐
            MyListView video_menu_relevant_list;
    @Bind(R.id.video_menu_comment_edittext_f)//评论输入框
            TextView video_menu_comment_edittext_f;
    @Bind(R.id.title2)//文件名
            TextView title2;
    @Bind(R.id.comment_num)//评论数
            TextView comment_num;
    @Bind(R.id.video_menu_comment_list)
    MyListView video_menu_comment_list;// 评论数据加载
    @Bind(R.id.video_comment_list)
    LinearLayout video_comment_list;
    @Bind(R.id.qbcomment)
    TextView qbcomment;
    @Bind(R.id.ll_comment_empty)
    LinearLayout ll_comment_empty;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Bind(R.id.video_menu_comment_reply)
    TextView video_menu_comment_reply;
    @Bind(R.id.video_menu_comment_edittext_ff)
    TextView video_menu_comment_edittext_ff;
    @Bind(R.id.video_menu_comment_item_times)
    TextView video_menu_comment_item_times;
    @Bind(R.id.video_menu_comment_item_name)
    TextView video_menu_comment_item_name;
    @Bind(R.id.video_menu_comment_item_cont)
    TextView video_menu_comment_item_cont;
    @Bind(R.id.video_menu_comment_item_id)
    TextView video_menu_comment_item_id;
    ArrayList<String> url_list = new ArrayList<>();
    @Bind(R.id.item_comment_layout)
    LinearLayout item_comment_layout;
    @Bind(R.id.item_MyComment)
    MyListView item_MyComment;
    @Bind(R.id.video_menu_comment_item_img)
    CustomImageView video_menu_comment_item_img;
    @Bind(R.id.icon_x_comment)
    ImageView icon_x_comment;
    private String shareUrl;//分享地址
    private String shareTitle;//分享title
    private String sharePicurl;//分享图片
    private String is_colle = "";
    public static String aid = "";
    private String file_path = "";
    private Map<String, Object> curr_Map = new HashMap<>();
    private List<ConferenceBean> conferenceBeanList = new ArrayList<>();
    private BaseListAdapter baseListAdapter;
    private BaseListAdapter2 baseListAdapter3;
    private BaseListAdapter2 baseListCommentAdapter;
    private Video_Experts_GridAdapter video_experts_gridAdapter;
    private List<Video_menu_expert> experts = new ArrayList<Video_menu_expert>();//专家
    public static int currPage = 1, onfling = 1;
    private boolean isNoMore = false;
    public List<ArticleCommentEntity> commentList = new ArrayList<ArticleCommentEntity>();
    public List<ArticleCommentEntity.ChildrenBean> chridCommentList = new ArrayList<ArticleCommentEntity.ChildrenBean>();
    public List<Video_menu_comment_entry2> datalist = new ArrayList<Video_menu_comment_entry2>();
    private String getAllReplyData_id = "";
    private String getAllReplyData_strid = "";
    private int getAllReplyData_position = 0;
    private Dialog dialog;
    private View layout_view;
    private String type = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            viewing_tilte.setText(dataJson.getString("title"));

                            viewing_time.setText(dataJson.getString("posttime"));
                            zambia_num.setText(dataJson.getString("digg_num"));
                            medicine_detial_eye_num.setText(dataJson.getString("hits"));
                            collection_num.setText(dataJson.getString("colle_sum"));
                            is_colle = dataJson.getString("is_colle");

                            viewing_name.setText(dataJson.getJSONObject("add_info").getString("name"));

                            if (dataJson.getJSONObject("share").getString("is_show").trim().length() > 0) {
                                share_layout.setVisibility(View.VISIBLE);
                            } else {
                                share_layout.setVisibility(View.GONE);
                            }

                            if (dataJson.getJSONObject("add_info").getString("add_photo").trim().length() > 0) {
                                RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default).error(R.mipmap.img_default);
                                Glide.with(context)
                                        .load(FirstLetter.getSpells(dataJson.getJSONObject("add_info").getString("add_photo")))
                                        .apply(options)
                                        .into(_icon);
                            }
                            if (dataJson.getString("keywords").trim().length() < 1) {
                                auto_wrap_line_layout.setVisibility(View.GONE);
                            } else {
                                auto_wrap_line_layout.setVisibility(View.VISIBLE);
                                String[] keywords = dataJson.getString("keywords").split(" ");
                                for (String str : keywords) {
                                    if (!containSpace(str.trim())) {
                                        View home_center = LayoutInflater.from(context).inflate(R.layout.auto_wrap_line_layout_item, null);
                                        TextView _item_text = (TextView) home_center.findViewById(R.id._item_text);
                                        _item_text.setText(str);
                                        auto_wrap_line_layout.addView(_item_text);

                                        TextView textView = new TextView(context);
                                        textView.setText(" ");
                                        auto_wrap_line_layout.addView(textView);
                                    }

                                }
                            }

                            if (dataJson.getString("is_colle").trim().length() > 0) {
                                video_icon_collection_img.setImageResource(R.mipmap.collection_02);
                            } else {
                                video_icon_collection_img.setImageResource(R.mipmap.collection_01);
                            }
                            if (dataJson.getString("is_zan").trim().length() > 0) {
                                video_no_zambia_img.setImageResource(R.mipmap.medicine_zan2);
                            } else {
                                video_no_zambia_img.setImageResource(R.mipmap.medicine_zan);
                            }
                            type = dataJson.getString("type");
                            switch (dataJson.getString("type")) {
                                case "1"://文件
                                    file_path = dataJson.getJSONArray("url").getString(0);
                                    if(file_path.trim().length()>0){
                                        title2.setText(dataJson.getString("title")+file_path.substring(file_path.lastIndexOf("."),file_path.length()));
                                    }else{
                                        title2.setText(dataJson.getString("title") );
                                    }

                                    file_layout.setVisibility(View.VISIBLE);
                                    viewing_text.setVisibility(View.GONE);
                                    break;
                                case "2"://文字内容

                                    file_layout.setVisibility(View.GONE);
                                    viewing_text.setVisibility(View.VISIBLE);
                                    // 加载定义的代码，并设定编码格式和字符集。
                                    viewing_text.setWebViewClient(new MyWebViewClient());
                                    viewing_text.setWebChromeClient(new xWebChromeClient());//设置视屏可以全屏
                                    viewing_text.loadDataWithBaseURL(null, appendHtml( dataJson.getJSONArray("url").getString(0) + " <script>  document.body.style.lineHeight = 1.5< /script> \\n</body>< /html>"), "text/html", "utf-8", null);
                                    viewing_text.addJavascriptInterface(new MedicineDetialActivity.JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
                                    break;
                                case "3"://幻灯
                                    file_path = dataJson.getJSONArray("url").getString(0);
                                    for(int i = 0 ;i<dataJson.getJSONArray("url").length();i++){
                                        url_list.add(dataJson.getJSONArray("url").getString(i));
                                    }
                                    if(file_path.trim().length()>0){
                                        title2.setText(dataJson.getString("title")+".ppt");
                                    }else{
                                        title2.setText(dataJson.getString("title") );
                                    }

                                    file_layout.setVisibility(View.VISIBLE);
                                    viewing_text.setVisibility(View.GONE);
                                    break;
                                default://未知
                                    file_path = dataJson.getJSONArray("url").getString(0);
                                    if(file_path.trim().length()>0){
                                        title2.setText(dataJson.getString("title")+file_path.substring(file_path.lastIndexOf("."),file_path.length()));
                                    }else{
                                        title2.setText(dataJson.getString("title") );
                                    }
                                    file_layout.setVisibility(View.VISIBLE);
                                    viewing_text.setVisibility(View.GONE);
                                    break;

                            }

                            if (dataJson.has("manage_info") && dataJson.getJSONArray("manage_info").length() > 0) {

                                experts.clear();
                                for (int i = 0; i < dataJson.getJSONArray("manage_info").length(); i++) {
                                    JSONObject data = dataJson.getJSONArray("manage_info").getJSONObject(i);
                                    Video_menu_expert expert = new Video_menu_expert();
//                                        expert.setVideo_menu_expert_cont(data.getString("content"));
                                    expert.setVideo_menu_expert_id(data.getString("uid"));
                                    expert.setVideo_menu_expert_img(data.getString("photo"));
                                    expert.setVideo_menu_expert_name(data.getString("name"));
                                    experts.add(expert);
                                }

                                video_Expert_list.setVisibility(View.VISIBLE);
                                video_Experts_list.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
                                video_experts_gridAdapter = new Video_Experts_GridAdapter(context, experts);
                                video_Experts_list.setAdapter(video_experts_gridAdapter);
                                video_Experts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        if (experts.get(position).getVideo_menu_expert_id().length() > 0) {
                                            Intent intentExpert = new Intent(context, PlayVideo_expert.class);
                                            intentExpert.putExtra("fid", aid);
                                            intentExpert.putExtra("aid", experts.get(position).getVideo_menu_expert_id());
                                            startActivity(intentExpert);
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
                            } else {
                                video_Expert_list.setVisibility(View.GONE);
                            }

                            if (dataJson.has("recommend") && dataJson.getJSONArray("recommend").length() > 0) {
                                relevant_list_layout.setVisibility(View.VISIBLE);
                                JSONArray dataConference = dataJson.getJSONArray("recommend");
                                for (int i = 0; i < dataConference.length(); i++) {
                                    JSONObject conferenceBannerObject = dataConference.getJSONObject(i);
                                    ConferenceBean conferenceBean = new ConferenceBean();
                                    conferenceBean.setId(conferenceBannerObject.getString("aid"));
                                    conferenceBean.setTitle(conferenceBannerObject.getString("title"));
                                    conferenceBean.setIconUrl(conferenceBannerObject.getString("picurl"));
                                    conferenceBean.setTime(conferenceBannerObject.getString("posttime"));
                                    conferenceBean.setCollectStatus(conferenceBannerObject.getString("videoclass"));
                                    conferenceBeanList.add(conferenceBean);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            } else {
                                relevant_list_layout.setVisibility(View.GONE);
                            }





                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();

                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 1) { // 成功
                            zambia_num.setText(jsonObject.getJSONObject("data").getString("digg_num"));
                            if (jsonObject.getJSONObject("data").getString("is_zan").trim().length() > 0) {
                                video_no_zambia_img.setImageResource(R.mipmap.medicine_zan2);
                            } else {
                                video_no_zambia_img.setImageResource(R.mipmap.medicine_zan);
                            }
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();

                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 1) { // 成功
                            collection_num.setText(jsonObject.getJSONObject("data").getString("colle_sum"));
                            is_colle = jsonObject.getJSONObject("data").getString("is_colle");
                            if (jsonObject.getJSONObject("data").getString("is_colle").trim().length() > 0) {
                                video_icon_collection_img.setImageResource(R.mipmap.collection_02);
                            } else {
                                video_icon_collection_img.setImageResource(R.mipmap.collection_01);
                            }
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();

                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        //SHARE_MEDIA.SINA,
                        if (result.getString("code").equals("0")) {//成功
//                            shareUrl = result.getString("url");
//                            shareTitle = result.getString("title");
//                            sharePicurl = result.getString("img");
                            //分享操作
                            if (!TextUtils.isEmpty(shareUrl)) {
                                //分享操作
                                //ShareSDK.initSDK(Article_details.this);
                                final ShareDialog shareDialog = new ShareDialog(Article_details.this);
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
                                            sinaWeibo.setPlatformActionListener(Article_details.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText("");
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(Article_details.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText("");
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(Article_details.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(shareTitle);
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setTitleUrl(shareUrl);  //网友点进链接后，可以看到分享的详情
                                            sp.setText("");
                                            sp.set(QQShare.SHARE_TO_QQ_APP_NAME, "CCMTV");
                                            sp.setAuthor("CCMTV");
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(Article_details.this); // 设置分享事件回调
                                            qq.share(sp);
                                        } else if (item.get("ItemText").equals("QQ空间")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(shareTitle);
                                            sp.setTitleUrl(shareUrl); // 标题的超链接
                                            sp.setImageUrl(sharePicurl);
                                            sp.setSite("CCMTV临床医学频道");
                                            sp.setSiteUrl(shareUrl);
                                            sp.setText("");
                                            Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                            qzone.setPlatformActionListener(Article_details.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(shareUrl.trim());
                                            Toast.makeText(Article_details.this, "复制成功",
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

                    baseListCommentAdapter.notifyDataSetChanged();
                    baseListAdapter3.notifyDataSetChanged();


                    break;
                case 17://添加新评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        LogUtil.e("mason","添加新评论"+result);
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(Article_details.this,  result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
                            video_comment_list.setVisibility(View.VISIBLE);
                            ll_comment_empty.setVisibility(View.GONE);//评论成功，隐藏无评论界面
                            video_menu_comment_list.setVisibility(View.VISIBLE);
                            setcomment();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(Article_details.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 24://添加子评论
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(Article_details.this,  result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            currPage = 1;
                            initcomment_new();
                            // video_menu_comment_list.setSelection(2);
                        } else {// 失败
                            Toast.makeText(Article_details.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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

                    baseListCommentAdapter.notifyDataSetChanged();
                    baseListAdapter3.notifyDataSetChanged();
                    isNoMore = false;
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

    /**
     * 包括空格判断
     *
     * @param input
     * @return
     */
    public static boolean containSpace(CharSequence input) {
        return Pattern.compile("\\s+").matcher(input).find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.article_details);
        context = this;
        ButterKnife.bind(this);
        aid = getIntent().getStringExtra("aid");
        currPage = 1;
        findId();
        initView();
        initData();
        initComment();
        setcomment();
        getUrlRulest();

    }

    public void initView() {
        viewing_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //评论详情X
        icon_x_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_comment_layout.setVisibility(View.GONE);
            }
        });

        video_no_zambia_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addZan();
            }
        });
        video_icon_collection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCollection();
            }
        });

        file_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(type.equals("3")){
                    intent = new Intent(context, Ppt_lock.class);
                    intent.putStringArrayListExtra("url_list",url_list);
                }else{
                    intent = new Intent(context, File_down.class);
                    intent.putExtra("file_path",file_path);
                    intent.putExtra("title",title2.getText().toString());
                }

                startActivity(intent);
            }
        });
        ws = viewing_text.getSettings();//获取webview设置属性
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

        WebSettings webSettings = viewing_text.getSettings();
        webSettings.setJavaScriptEnabled(true);
        viewing_text.addJavascriptInterface(new JiaoHu(), "hello");
        viewing_text.getSettings().setDomStorageEnabled(true);

        viewing_text.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        baseListAdapter = new BaseListAdapter(video_menu_relevant_list, conferenceBeanList, R.layout.item_medical_database) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                ConferenceBean conferenceBean = (ConferenceBean) item;
                helper.setVisibility(R.id._item_type_img, View.GONE);
                helper.setText(R.id._item_title, ((ConferenceBean) item).getTitle());

                helper.setText(R.id._item_time, ((ConferenceBean) item).getTime());

                if (conferenceBean.getCollectStatus().length() > 0) {
                    helper.setVisibility(R.id._item_type, View.VISIBLE);
                    helper.setText(R.id._item_type, ((ConferenceBean) item).getCollectStatus());
                } else {
                    helper.setVisibility(R.id._item_type, View.GONE);
                }
                if (conferenceBean.getIconUrl().length() > 0) {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                    helper.setImageBitmapGlide(context, R.id._item_img, ((ConferenceBean) item).getIconUrl());
                } else {
                    helper.setVisibility(R.id._item_img, View.GONE);
                }
            }
        };

        video_menu_relevant_list.setAdapter(baseListAdapter);
        video_menu_relevant_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conferenceId = conferenceBeanList.get(position).getId();
                Intent intent = new Intent(context, Article_details.class);
                intent.putExtra("aid", conferenceId);
                startActivity(intent);
                finish();
            }
        });

        video_menu_comment_list.setVerticalScrollBarEnabled(true);//不管有没有活动都隐藏
    }

    /**
     * 评论列表
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
                    obj.put("page", currPage);
                    obj.put("limit", COMMENT_LIMIT);
                    String result = HttpClientUtils.sendPost(Article_details.this, URLConfig.CCMTVAPP, obj.toString());
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
                                        baseListCommentAdapter.notifyDataSetChanged();
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
     * 获取评论  加载更多
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
                    String result = HttpClientUtils.sendPost(Article_details.this, URLConfig.CCMTVAPP, obj.toString());
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
     * name：回复功能
     */
    public void getcommentStumit(final View view, final String content, final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addRecomment);
                    obj.put("uid", SharedPreferencesTools.getUid(Article_details.this));
                    obj.put("username", SharedPreferencesTools.getUserName(Article_details.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(Article_details.this,
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
                    obj.put("uid", SharedPreferencesTools.getUid(Article_details.this));
                    obj.put("username", SharedPreferencesTools.getUserName(Article_details.this));
                    obj.put("aid", aid);
                    //最外层评论id
                    obj.put("master_pid", view.getTag());
                    //直属上级的id
                    obj.put("slave_pid", view.getTag());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", type);
                    String result = HttpClientUtils.sendPost(Article_details.this,
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

    private void initData(){
        video_menu_comment_reply.setTag("0");
        video_menu_comment_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_edit(v, "", "0");
            }
        });


        video_menu_comment_edittext_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出输入框
                v.setTag(0);
                show_edit(v, "", "1");
            }

        });

        video_menu_comment_edittext_ff.setTag("0");
        video_menu_comment_edittext_ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出输入框
                show_edit2(v, "", "0");
            }

        });
    }

    private void initComment(){
        baseListCommentAdapter = new BaseListAdapter2(video_menu_comment_list, commentList, R.layout.video_menu_comment_item) {
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
                video_menu_comment_reply1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setTag(((ArticleCommentEntity) item).getId());
                        show_edit(v, "回复 " + ((ArticleCommentEntity) item).getUsername() + " :", "0");
                    }
                });
                helper.setTag(R.id.video_menu_comment_reply, ((ArticleCommentEntity) item).getId());
                //删除
                helper.setArticleCommentAdapterDelOnClick(R.id.video_menu_comment_delete, Article_details.this);
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
                t.setOnClickListener(new View.OnClickListener() {
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
//                                                        video_menu_comment_reply.setOnClickListener(new View.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(View v) {
//                                                                show_edit2(v, ((ArticleCommentEntity) mitem).getUsername(), "0");
//                                                            }
//                                                        });
//                                                        video_menu_comment_edittext_ff.setTag(((ArticleCommentEntity) mitem).getId());
//                                                        video_menu_comment_edittext_ff.setOnClickListener(new View.OnClickListener() {
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
//                                                        baseListCommentAdapter.notifyDataSetChanged();
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
        video_menu_comment_list.setAdapter(baseListCommentAdapter);

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
                ImageView ivHead = helper.getView(R.id.video_menu_comment_item_img);
//                ImageLoader.getInstance().displayImage(((ArticleCommentEntity.ChildrenBean) item).getIcon(), ivHead);
                new Carousel_figure(context).loadImageNoCache(((ArticleCommentEntity.ChildrenBean) item).getIcon(), ivHead);
            }
        };


        item_MyComment.setAdapter(baseListAdapter3);
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
            video_editext_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!video_menu_comment_edittext.getText().toString().trim().equals("")) {
                        MyProgressBarDialogTools.show(context);
                        getcommentStumit(view, video_menu_comment_edittext.getText().toString(), type);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Article_details.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
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
            video_editext_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!video_menu_comment_edittext.getText().toString().trim().equals("")) {
                        MyProgressBarDialogTools.show(context);
                        getcommentStumit2(view, video_menu_comment_edittext.getText().toString(), type);
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Article_details.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
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


    public class JiaoHu {
        @JavascriptInterface
        public void showAndroid() {
//            Toast.makeText(context,"js调用了android的方法",Toast.LENGTH_SHORT).show();
            //点击播放视频即 暂停音频播放
            //停止播放音频
            MainActivity.stopFloatingView(context);

        }
    }


    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articelInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
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

    public void addZan() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addZan);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
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

    public void addCollection() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addCollection);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("is_colle", is_colle);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
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

    public void yearGoodTeacherDelVote(final String id, final String manage_id, final String comment) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yearGoodTeacherAddVote);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);
                    obj.put("manage_id", manage_id);
                    obj.put("comment", comment);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("年度评优三：投票接口", result);
                    Message message = new Message();
                    message.what = 6;
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
            videoReset();
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            addHrefClickListner();
            MyProgressBarDialogTools.hide();//pdf适配屏幕之后再关闭

            viewing_text.loadUrl("javascript:document.body.style.padding=\"2%\"; void 0");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return true;
        }
    }

    // 注入js函数监听
    private void addHrefClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        viewing_text.loadUrl("javascript:(function(){" +
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
        viewing_text.loadUrl("javascript:(function(){" +
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
        viewing_text.loadUrl("javascript:(function(){" +
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
        viewing_text.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('video'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var video = objs[i];   " +
                "    video.style.maxWidth = '100%'; video.style.height = 'auto';  " +
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
//            if (webView != null) {
//                webView.setVisibility(View.GONE);
//            }
//            video_view.addView(view);
//            if (video_view != null) {
//                video_view.setVisibility(View.VISIBLE);
//            }

//            ViewGroup parent = (ViewGroup) webView.getParent();
//            parent.removeView(webView);
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
                        "}</style><body style=\"max-width:500px\"   >")
                .append(content);
        return builder.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setcomment();
    }

    @Override
    protected void onDestroy() {
        viewing_text.loadUrl("about:blank");
        super.onDestroy();
    }

    /*
   * 文章分享
   * */
    public void ShareTitle(View view) {
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
            return;
        } else {
            Message message = new Message();
            message.what = 5;
            handler.sendMessage(message);
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

}
