
package com.linlic.ccmtv.yx.kzbf.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMall;
import com.linlic.ccmtv.yx.activity.message.New_message;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.InvestigaResultAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.InvestigationAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDetailAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDetialCommentAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDynamicAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineComment;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;
import com.linlic.ccmtv.yx.kzbf.bean.InvestigationTypeBean;
import com.linlic.ccmtv.yx.kzbf.utils.OrdinaryDecoration;
import com.linlic.ccmtv.yx.kzbf.widget.InvestigationSucessPop;
import com.linlic.ccmtv.yx.kzbf.widget.TopPopWindow;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 药讯详情
 */
//item_medicine_comment.xml
@SuppressLint("SetJavaScriptEnabled")
public class MedicineDetialActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {
    Context context;
    //用户统计
    private InvestigationAdapter investigationAdapter;
    private InvestigaResultAdapter investigaResultAdapter;
    private List<InvestigationTypeBean> investigations = new ArrayList<>();//问卷调研
    private List<InvestigationTypeBean> moreComments = new ArrayList<>();//问卷调研结果  查看更多
    private String currentQid;
    private int currentQidPage = 1;

    private List<DbMedicineDetial> tuijianList;
    private List<DbMedicineDetial> listMore = new ArrayList<>();
    private List<DbMedicineComment> commentList;
    private List<DbMedicineComment> commentMore = new ArrayList<>();
    private DbMedicineDetial dbMd;
    private RelativeLayout rl_comment_title;
    private RecyclerView medicine_tuijian_list, detial_comment_list, detial_investigation_list;
    private ImageView user_img, medicine_detial_collection_icon, medicine_detial_zan_img;
    private TextView helper, posttime, title, look_num, laud_num, medicine_tuijian;
    private TextView comment_more;
    private ImageView medicine_detial_send;
    private String id;//文章id
    private String commentId;//评论id
    private String reCommendId;//推荐id
    private String is_comment;//是否可以评论
    private String is_laud;//是否点赞
    private String is_collect;//是否收藏    0:未收藏 1:已收藏
    private String s_helper;//作者
    private String content;
    private String shareUrl;
    private String shareTitle;
    private String sharePicurl;
    private String focus_id;
    private String describe;
    private int zan_num;//文章点赞数
    private int comment_zan_num;//评论点赞数
    private int mPosition;
    private WebView webView;
    private MedicineDetailAdapter mdAdapter;
    private MedicineDetialCommentAdapter mdcAdapter;
    private DbMedicineComment dbMc;
    private LinearLayout ll_medicine_write_comment;//底部评论
    private LinearLayout ll_comment_collection;//收藏
    private View view;
    private EditText et_detial_comment;
    private Intent intent;
    private FrameLayout video_view;
    private RelativeLayout detial_content;
    private WebSettings ws;

    private HashMap<String, InvestigationTypeBean> moreHolders = new HashMap<>();//问题留言加载更多的Holder
    private HashMap<String, Integer> morePage = new HashMap<>();//问题留言page

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功

                            int commentCountNum = jsonObject.getInt("comment_count_num");
                            medicine_comment.setText("全部评论(" + commentCountNum + ")");
                            tuijianList = new ArrayList<>();
                            JSONObject object = jsonObject.getJSONObject("data");

                            id = object.getString("id");
                            focus_id = object.getString("uid");
                            is_comment = object.getString("is_comment");
                            is_laud = object.getString("is_laud");
                            is_collect = object.getString("is_collect");
                            describe = object.getString("describe");


                            activity_title_name.setText("药讯详情");
                            //不能给自己发送站内信
                            if (focus_id.equals(SharedPreferencesTools.getUids(context))) {
                                medicine_detial_send.setVisibility(View.GONE);
                            }

                            //显示/隐藏底部评论
                            if (is_comment.equals("1")) {
                                ll_medicine_write_comment.setVisibility(View.GONE);
                            } else {
                                ll_medicine_write_comment.setVisibility(View.VISIBLE);
                            }
                            //判断是否已收藏
                            if (is_collect.equals("0")) {//变成未收藏
                                medicine_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection);
                            } else {//变成已收藏
                                medicine_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection2);
                            }
                            //判断是否已点赞
                            if (is_laud.equals("0")) {
                                medicine_detial_zan_img.setImageResource(R.mipmap.medicine_zan);
                                medicine_detial_zan_img.setClickable(true);
                            } else {
                                medicine_detial_zan_img.setImageResource(R.mipmap.medicine_zan2);
                                medicine_detial_zan_img.setClickable(false);
                            }
                            //填充页面数据
                            RequestOptions options = new RequestOptions().error(R.mipmap.img_default).placeholder(R.mipmap.img_default);
                            Glide.with(context).asBitmap().load(object.getString("user_img")).apply(options).into(user_img);
                            s_helper = object.getString("helper");
                            helper.setText(object.getString("helper") + " " + object.getString("company") + " " + object.getString("drug"));
                            setTextView(object, "posttime", posttime);
                            setTextView(object, "title", MedicineDetialActivity.this.title);
                            look_num.setText(MedicineDynamicAdapter.formatLookNum(Integer.parseInt(object.getString("look_num"))));
                            zan_num = Integer.parseInt(object.getString("laud_num"));
                            laud_num.setText(MedicineDynamicAdapter.formatLookNum(zan_num));
                            getWindow().setFlags(//强制打开GPU渲染
                                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

                            // 加载定义的代码，并设定编码格式和字符集。
                            webView.setWebViewClient(new MyWebViewClient());
                            webView.setWebChromeClient(new xWebChromeClient());//设置视屏可以全屏
                            webView.loadDataWithBaseURL(null, appendHtml(object.getString("content") + " <script>  document.body.style.lineHeight = 1.5< /script> \\n</body>< /html>"), "text/html", "utf-8", null);
                            webView.addJavascriptInterface(new JavaScriptInterface(context), "imagelistner");//这个是给图片设置点击监听
//                            webView.addJavascriptInterface(new JsObject(context), "console");//给视频设置监听

                            JSONArray dataArray2 = jsonObject.getJSONArray("recommend");
                            if (dataArray2.length() == 0) {
                                medicine_tuijian.setVisibility(View.GONE);
                                medicine_tuijian_list.setVisibility(View.GONE);
                                view.setVisibility(View.GONE);
                            } else {
                                medicine_tuijian.setVisibility(View.VISIBLE);
                                medicine_tuijian_list.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
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
                            if (commentList == null)
                                commentList = new ArrayList<>();
                            else
                                commentList.clear();
                            JSONArray dataArray3 = jsonObject.getJSONArray("comment");
                            if (dataArray3.length() == 0) {
                                rl_comment_title.setVisibility(View.GONE);
                            } else {
                                rl_comment_title.setVisibility(View.VISIBLE);
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
                                commentMore.clear();
                                commentMore.addAll(commentList);
                                mdcAdapter.notifyDataSetChanged();
                            }
                            /**问卷调研**/
                            investigations.clear();
                            //问卷模块
                            String is_show_survey = jsonObject.getString("is_show_survey");
                            if (!"0".equals(is_show_survey)) {
                                JSONArray survey = jsonObject.getJSONArray("survey");
                                int length = survey.length();
                                if (length > 0) {
                                    ll_medicine_write_comment.setVisibility(View.GONE);
                                    detial_investigation_list.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < length; i++) {
                                        JSONObject object1 = survey.getJSONObject(i);
                                        InvestigationTypeBean type = new InvestigationTypeBean();
                                        if ("2".equals(is_show_survey) && object1.getInt("type") == InvestigaResultAdapter.TYPE_COMMNET) {//如果已经参与过调查，展示数据和评论
                                            JSONArray list = object1.getJSONArray("list");
                                            int listLength = list.length();
                                            for (int j = 0; j < listLength; j++) {
                                                InvestigationTypeBean listType = new InvestigationTypeBean();
                                                listType.setQ_id(object1.getString("q_id"));
                                                if (j == 0) {
                                                    listType.setTag(object1.getString("total"));
                                                }
                                                listType.setType(InvestigaResultAdapter.TYPE_COMMNET);
                                                listType.setQuestion(object1.getString("question"));
                                                listType.setObject(list.getJSONObject(j));
                                                investigations.add(listType);
                                            }
                                            if (!"0".equals(object1.getString("is_show_more"))) {//查看更多Holder
                                                InvestigationTypeBean bottonType = new InvestigationTypeBean();
                                                bottonType.setQ_id(object1.getString("q_id"));
                                                bottonType.setQuestion(object1.getString("question"));
                                                bottonType.setType(InvestigaResultAdapter.TYPE_BOTTOM);
                                                bottonType.setTag(investigations.size());//记录查看更多Holder的Position
                                                investigations.add(bottonType);
                                                moreHolders.put(bottonType.getQ_id(), bottonType);
                                            }
                                        } else {
                                            type.setQ_id(object1.getString("q_id"));
                                            type.setType(object1.getInt("type"));
                                            type.setQuestion(object1.getString("question"));
                                            type.setObject(object1);
                                            investigations.add(type);
                                        }
                                    }
                                    if ("1".equals(is_show_survey)) {
                                        InvestigationTypeBean submit = new InvestigationTypeBean();
                                        submit.setType(InvestigationAdapter.TYPE_SUBMIT);
                                        submit.setQuestion("");
                                        investigations.add(submit);//提交的Holder
                                    }
                                }
                            }
                            //投票模块
                            String isShowVote = jsonObject.getString("is_show_vote");
                            if (!"0".equals(isShowVote)) {
                                detial_investigation_list.setVisibility(View.VISIBLE);
                                JSONArray votes = jsonObject.getJSONArray("vote");
                                int voteLength = votes.length();
                                for (int i = 0; i < voteLength; i++) {
                                    JSONObject vote = votes.getJSONObject(i);
                                    InvestigationTypeBean voteType = new InvestigationTypeBean();
                                    voteType.setQ_id(vote.getString("q_id"));
                                    voteType.setType(InvestigationAdapter.TYPE_PROGRESS);
                                    voteType.setQuestion(vote.getString("question"));
                                    voteType.setObject(vote);
                                    voteType.setTag(isShowVote);
                                    investigations.add(voteType);
                                }
                            }
                            //控制右上角图标的显示与隐藏
                            if (!"0".equals(isShowVote) || !"0".equals(is_show_survey)) {
                                if ("1".equals(isShowVote) || "1".equals(is_show_survey)) {
                                    medicine_detial_menu.setVisibility(View.VISIBLE);
                                    medicine_detial_search.setVisibility(View.VISIBLE);
                                    medicine_detial_share.setVisibility(View.GONE);
                                } else {
                                    medicine_detial_menu.setVisibility(View.GONE);
                                    medicine_detial_search.setVisibility(View.GONE);
                                    medicine_detial_share.setVisibility(View.VISIBLE);
                                }
                                activity_title_name.setText(TextUtils.isEmpty(object.getString("title")) ? "药讯详情" : object.getString("title"));
                            } else {
                                medicine_detial_menu.setVisibility(View.GONE);
                                medicine_detial_search.setVisibility(View.GONE);
                                medicine_detial_share.setVisibility(View.GONE);
                            }

                            if ("2".equals(is_show_survey)) {//问卷已经提交过
                                if (investigaResultAdapter == null) {
                                    investigaResultAdapter = new InvestigaResultAdapter(investigations, context) {
                                        @Override
                                        public void onLoadMoreMessage(String q_id) {
                                            super.onLoadMoreMessage(q_id);
                                            checkMoreMessages(q_id);
                                        }

                                        @Override
                                        public void onVoteSubmit(String q_id, String string) {
                                            super.onVoteSubmit(q_id, string);
                                            submitVoteInfo(q_id, string);
                                        }
                                    };
                                    detial_investigation_list.setAdapter(investigaResultAdapter);
                                } else {
                                    investigaResultAdapter.notifyDataSetChanged();
                                }
                            } else {//问卷、投票未提交过或者投票已提交过
                                if (investigationAdapter == null) {
                                    OrdinaryDecoration decoration = new OrdinaryDecoration(10) {
                                        @Override
                                        public int getDecorationColor(int positon) {
                                            return Color.parseColor("#EFEFEF");
                                        }
                                    };
                                    investigationAdapter = new InvestigationAdapter(investigations, context) {
                                        @Override
                                        public void onVoteSubmit(String qid, String answer) {
                                            super.onVoteSubmit(qid, answer);
                                            submitVoteInfo(qid, answer);
                                        }

                                        @Override
                                        public void oninvestigaSubmit(String data) {
                                            super.oninvestigaSubmit(data);
                                            submitInvestigaInfo(data);
                                        }
                                    };
                                    detial_investigation_list.addItemDecoration(decoration);
                                    detial_investigation_list.setAdapter(investigationAdapter);
                                } else {
                                    investigationAdapter.notifyDataSetChanged();
                                }
                                investigationAdapter.setIsSubmited(false);
                            }
                            /**问卷调研结束**/

                            if (!jsonObject.getString("msg").equals("")) {
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
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            if (is_collect.equals("1")) {//变成已收藏
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                medicine_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection2);
                            } else {//变成未收藏
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                medicine_detial_collection_icon.setImageResource(R.mipmap.icon_comment_collection);
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
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {

                            if (jsonObject.getString("is_luad").equals("1")) {//变成已点赞
                                zan_num++;
                                medicine_detial_zan_img.setImageResource(R.mipmap.medicine_zan2);
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                medicine_detial_zan_img.setClickable(false);

                                Message message = new Message();
                                message.what = LocalApplication.KZBF_ARTICLELAUD_DELAY;
                                message.obj = id;
                                LocalApplication.sendMessage(message);
                            } else {//变成未点赞
                                if (zan_num > 0) {
                                    zan_num--;
                                }
                                medicine_detial_zan_img.setImageResource(R.mipmap.medicine_zan);
                                Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                medicine_detial_zan_img.setClickable(true);
                            }
                            laud_num.setText(zan_num + "");
                        } else {
                            medicine_detial_zan_img.setClickable(true);
                            Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            listMore.clear();
                            commentMore.clear();
                            initData();
                            setValue();
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.
                                    LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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
                            //分享操作
                            if (!TextUtils.isEmpty(shareUrl)) {
                                //分享操作
                                //ShareSDK.initSDK(MedicineDetialActivity.this);
                                final ShareDialog shareDialog = new ShareDialog(MedicineDetialActivity.this);
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
                                            sinaWeibo.setPlatformActionListener(MedicineDetialActivity.this); // 设置分享事件回调
                                            sinaWeibo.share(sp);
                                        } else if (item.get("ItemText").equals("微信好友")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);
                                            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                            wechat.setPlatformActionListener(MedicineDetialActivity.this); // 设置分享事件回调
                                            wechat.share(sp);
                                        } else if (item.get("ItemText").equals("朋友圈")) {
                                            //2、设置分享内容
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                            sp.setTitle(shareTitle);  //分享标题
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);
                                            Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                            wechatMoments.setPlatformActionListener(MedicineDetialActivity.this); // 设置分享事件回调
                                            wechatMoments.share(sp);
                                        } else if (item.get("ItemText").equals("QQ")) {
                                            Platform.ShareParams sp = new Platform.ShareParams();
                                            sp.setTitle(shareTitle);
                                            sp.setImageUrl(sharePicurl);//网络图片rul
                                            sp.setTitleUrl(shareUrl);  //网友点进链接后，可以看到分享的详情
                                            sp.setText(describe);
                                            Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                            qq.setPlatformActionListener(MedicineDetialActivity.this); // 设置分享事件回调
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
                                            qzone.setPlatformActionListener(MedicineDetialActivity.this); // 设置分享事件回调
                                            qzone.share(sp);
                                        } else {
                                            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                            cmb.setText(shareUrl.trim());
                                            Toast.makeText(MedicineDetialActivity.this, "复制成功",
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
                            switch (msg.arg1) {
                                case 101://投票成功
                                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    setValue();
                                    break;
                                case 102:
                                    MyProgressBarDialogTools.hide();
                                    initPopupWindowOrShow();
                                    sucessPop.setSucessIntegralSign("+" + jsonObject.getString("integral") + "积分");
                                    if (investigationAdapter != null)
                                        investigationAdapter.setIsSubmited(true);
                                    break;
                                default:
                                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    MyProgressBarDialogTools.hide();
                                    break;
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 8:
                    try {
                        JSONObject object = new JSONObject(msg.obj + "");
                        moreComments.clear();
                        if (object.getInt("code") == 0 && currentQidPage <= object.getInt("pages")) {
                            JSONArray data = object.getJSONArray("data");
                            int length = data.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject message = data.getJSONObject(i);
                                InvestigationTypeBean bean = new InvestigationTypeBean();
                                bean.setQ_id(currentQid);
                                bean.setObject(message);
                                bean.setType(InvestigaResultAdapter.TYPE_COMMNET);
                                moreComments.add(bean);
                            }
                            InvestigationTypeBean bean = moreHolders.get(currentQid);
                            if (bean != null) {
                                int position = (int) bean.getTag();
                                investigations.addAll(position, moreComments);
                                for (Map.Entry<String, InvestigationTypeBean> entry : moreHolders.entrySet()) {
                                    InvestigationTypeBean typeBean = entry.getValue();
                                    int typeBeanTag = (int) typeBean.getTag();
                                    if (typeBeanTag >= position) {
                                        typeBean.setTag(typeBeanTag + moreComments.size());
                                        moreHolders.put(entry.getKey(), typeBean);
                                    }
                                }
                                currentQidPage++;
                                morePage.put(currentQid, currentQidPage);
                                if (currentQidPage > object.getInt("pages")) {
                                    InvestigationTypeBean remove = moreHolders.remove(currentQid);
                                    investigations.remove(remove);
                                }
                                investigaResultAdapter.notifyDataSetChanged();
                            }
                        } else {
                            InvestigationTypeBean remove = moreHolders.remove(currentQid);
                            investigations.remove(remove);
                            Toast.makeText(context, "暂无更多留言", Toast.LENGTH_SHORT).show();
                            investigaResultAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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

    private InvestigationSucessPop sucessPop;
    private ImageView medicine_detial_share;
    private ImageView medicine_detial_search;
    private ImageView medicine_detial_menu;
    private TopPopWindow topPopWindow;
    private TextView activity_title_name;
    private ScrollView detail_scroll;

    private void initPopupWindowOrShow() {
        if (sucessPop == null) {
            sucessPop = new InvestigationSucessPop(this);
            sucessPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });
            sucessPop.setOnShareClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareTitle(v);
                }
            });

            sucessPop.setOnCheckMoreResult(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sucessPop.dismiss();
                    MyProgressBarDialogTools.show(context);
                    setValue();
                }
            });
        }
        if (!sucessPop.isShowing()) {
            sucessPop.showAsDropDown(detial_content, 0, -detial_content.getHeight());
            lightoff();
        }
    }


    //PopupWindow消失时，使屏幕恢复正常
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    //查看更多留言
    private void checkMoreMessages(final String q_id) {
        currentQid = q_id;
        MyProgressBarDialogTools.show(context);
        currentQidPage = morePage.get(q_id) == null ? 0 : morePage.get(q_id);
        if (currentQidPage < 1) currentQidPage = 1;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.surveyUserList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("q_id", q_id);
                    obj.put("page", currentQidPage);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论点赞数据", result);
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

    //投票提交
    private void submitVoteInfo(final String q_id, final String answer) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.voteUserAnswer);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("q_id", q_id);
                    obj.put("answer", answer);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论点赞数据", result);
                    Message message = new Message();
                    message.what = 7;
                    message.arg1 = 101;
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

    //调研提交
    private void submitInvestigaInfo(final String data) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.surveyUserAnswer);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", id);
                    obj.put("data", data);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论点赞数据", result);

                    Message message = new Message();
                    message.what = 7;
                    message.arg1 = 102;
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


    private TextView medicine_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detial);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        activity_title_name = findViewById(R.id.activity_title_name);
        medicine_tuijian_list = (RecyclerView) findViewById(R.id.medicine_tuijian_list);
        detial_comment_list = (RecyclerView) findViewById(R.id.detial_comment_list);
        user_img = (ImageView) findViewById(R.id.medicine_detial_head_icon);
        medicine_detial_zan_img = (ImageView) findViewById(R.id.medicine_detial_zan_img);
        helper = (TextView) findViewById(R.id.medicine_detial_helper);
        posttime = (TextView) findViewById(R.id.medicine_detial_posttime);
        title = (TextView) findViewById(R.id.medicine_detial_item_title);
        look_num = (TextView) findViewById(R.id.medicine_detial_eye_num);
        laud_num = (TextView) findViewById(R.id.medicine_detial_zan_num);
        webView = (WebView) findViewById(R.id.detail_webView);
        medicine_tuijian = (TextView) findViewById(R.id.medicine_tuijian);
        rl_comment_title = (RelativeLayout) findViewById(R.id.rl_comment_title);
        et_detial_comment = (EditText) findViewById(R.id.et_detial_comment);
        medicine_comment = (TextView) findViewById(R.id.medicine_comment);

        comment_more = (TextView) findViewById(R.id.comment_more);
        medicine_detial_collection_icon = (ImageView) findViewById(R.id.medicine_detial_collection_icon);
        ll_medicine_write_comment = (LinearLayout) findViewById(R.id.ll_medicine_write_comment);
        view = findViewById(R.id.medicine_view);
        ll_comment_collection = (LinearLayout) findViewById(R.id.ll_comment_collection);
        medicine_detial_send = (ImageView) findViewById(R.id.medicine_detial_send);

        video_view = (FrameLayout) findViewById(R.id.video_view);
        detial_content = (RelativeLayout) findViewById(R.id.detial_content);

        detial_investigation_list = (RecyclerView) findViewById(R.id.detial_investigation_list);
        medicine_detial_share = findViewById(R.id.medicine_detial_share);
        medicine_detial_search = findViewById(R.id.medicine_detial_search);
        medicine_detial_menu = findViewById(R.id.medicine_detial_menu);
        detail_scroll = findViewById(R.id.detail_scroll);

        detial_investigation_list.setLayoutManager(new LinearLayoutManager(context) {
            public boolean canScrollVertically() {
                return false;
            }
        });
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
                    LogUtil.e("看看文章详细数据", result);

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

        comment_more.setOnClickListener(this);
        medicine_detial_collection_icon.setOnClickListener(this);
        ll_comment_collection.setOnClickListener(this);
        medicine_detial_zan_img.setOnClickListener(this);
        user_img.setOnClickListener(this);
        medicine_detial_send.setOnClickListener(this);

        medicine_detial_search.setOnClickListener(this);
        medicine_detial_share.setOnClickListener(this);
        medicine_detial_menu.setOnClickListener(this);

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

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JiaoHu(), "hello");
        webView.getSettings().setDomStorageEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.setBackgroundColor(0);

        medicine_tuijian_list.setLayoutManager(new LinearLayoutManager(context));
        mdAdapter = new MedicineDetailAdapter(context, listMore);
        medicine_tuijian_list.setAdapter(mdAdapter);
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

        detial_comment_list.setLayoutManager(new LinearLayoutManager(context));
        mdcAdapter = new MedicineDetialCommentAdapter(context, commentMore);
        detial_comment_list.setAdapter(mdcAdapter);

        et_detial_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    if (TextUtils.isEmpty(et_detial_comment.getText().toString())) {
                        Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        setComment();
                        et_detial_comment.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
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

    public class JiaoHu {
        @JavascriptInterface
        public void showAndroid() {
//            Toast.makeText(context,"js调用了android的方法",Toast.LENGTH_SHORT).show();
            //点击播放视频即 暂停音频播放
            //停止播放音频
            MainActivity.stopFloatingView(context);

        }
    }


    @Override
    protected void onResume() {
        //保存进入的日期
        MyProgressBarDialogTools.show(this);
        setValue();
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
    private void setTextView(JSONObject customJson1, String s, TextView textView) throws JSONException {
        if (!customJson1.getString(s).equals("")) {
            textView.setText(customJson1.getString(s));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_more://更多评论
                Intent intent = new Intent(context, MoreCommentActivity.class);
                intent.putExtra("aid", id);
                startActivity(intent);
                break;
            case R.id.ll_comment_collection://收藏
                if (is_collect.equals("0")) {
                    is_collect = "1";
                } else {
                    is_collect = "0";
                }
                setCollection();
                break;
            case R.id.medicine_detial_collection_icon:
                if (is_collect.equals("0")) {
                    is_collect = "1";
                } else {
                    is_collect = "0";
                }
                setCollection();
                break;
            case R.id.medicine_detial_zan_img://赞
                medicine_detial_zan_img.setClickable(false);
                setPraise();
                break;
            case R.id.medicine_detial_head_icon://跳转个人中心
                Intent intent1 = new Intent(context, PersonalHomeActivity.class);
                intent1.putExtra("fuid", focus_id);
                startActivity(intent1);
                break;
            case R.id.medicine_detial_send:

//                Intent intent2 = new Intent(context, MessageActivity.class);
//                intent2.putExtra("addressee_uid", focus_id);
//                intent2.putExtra("helper", s_helper);
//                intent2.putExtra("assistant", "0");
//                startActivity(intent2);
                Intent intent2 = new Intent(context, New_message.class);
                intent2.putExtra("uid", focus_id);
                intent2.putExtra(New_message.FROM_KZBF_KEY, "FROM_KZBF_KEY");
                intent2.putExtra("username", s_helper);
                startActivity(intent2);
                break;
            case R.id.medicine_detial_search://搜索
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.medicine_detial_share://分享
                ShareTitle(v);
                break;
            case R.id.medicine_detial_menu://菜单
                showTopRightPopMenu();
                break;
            case R.id.medicine_menu://菜单按钮
                showTopRightPopMenu();
                break;
            case R.id.ll_popmenu_msg://消息
                startActivity(new Intent(context, MessageActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_fouce://我的关注
                startActivity(new Intent(context, MyFouseActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_collection://我的收藏
                startActivity(new Intent(context, MyCollectionActivity.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_integrated_mall://积分商城
                startActivity(new Intent(context, IntegralMall.class));
                topPopWindow.dismiss();
                break;
            case R.id.ll_popmenu_download://我的下载
                startActivity(new Intent(context, MyDownloadActivity.class));
                topPopWindow.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 显示右上角popup菜单
     */
    private void showTopRightPopMenu() {
        if (topPopWindow == null) {
            //(activity,onclicklistener,width,height)
            //dialog大小
            topPopWindow = new TopPopWindow(MedicineDetialActivity.this, this, 560, 1300);
            //监听窗口的焦点事件，点击窗口外面则取消显示
            topPopWindow.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        topPopWindow.dismiss();
                    }
                }
            });
        }
        //设置默认获取焦点
        topPopWindow.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        topPopWindow.showAsDropDown(medicine_detial_menu, 0, -60);//dialog位置
        //如果窗口存在，则更新
        topPopWindow.update();
    }

    //收藏/取消收藏
    private void setCollection() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleCollect);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看收藏数据", result);
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

    //赞/取消赞
    private void setPraise() {
        MyProgressBarDialogTools.show(context);
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

    //发布评论
    private void setComment() {
        MyProgressBarDialogTools.show(context);
        content = et_detial_comment.getText().toString();
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
                    MyProgressBarDialogTools.hide();

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
//                    Log.e("看看积分数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看积分数据", result);

                    Message message = new Message();
                    message.what = 7;
                    message.arg1 = 103;
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

            webView.loadUrl("javascript:document.body.style.padding=\"2%\"; void 0");
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

    @Override
    protected void onDestroy() {
        webView.loadUrl("about:blank");
        super.onDestroy();
    }
}
