package com.linlic.ccmtv.yx.activity.message;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/10/18.  评论/留言
 */

public class Comment extends BaseActivity {

    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    private Context context;

    @Bind(R.id.message_list)
    ListView message_list;
    @Bind(R.id.comment_nodata)
    NodataEmptyLayout comment_nodata;
    private String catid1 = "";
    private String catid2 = "";
    private int page = 1;
    private int limit = 5;
    private int count = 0;
    private String is_Fan = "";
    private Dialog dialog;
    private View layout_view;
    private BaseListAdapter baseListAdapterVideo;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private Map<String, Object> curr_Map = new HashMap<>();
    private String is_message_flg;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (page == 1) {
                                    JSONObject data = dataJson.getJSONObject("data");
                                    try {
                                        count = data.getInt("count");
                                    } catch (Exception e) {
                                        count = 0;
                                    }
                                    listData.clear();
                                }

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("aid", dataJson1.getString("aid"));
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("is_read", dataJson1.getString("is_read"));
                                    map.put("user_icon", dataJson1.getString("user_icon"));
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("picurl", dataJson1.getString("picurl"));
                                    map.put("description", dataJson1.getString("description"));
                                    map.put("zan_icon", dataJson1.getString("zan_icon"));
                                    map.put("reply_content", dataJson1.getString("reply_content"));
                                    map.put("position", listData.size());
                                    map.put("slave_id", dataJson1.getString("slave_id"));
                                    map.put("recomment_slave_pid", dataJson1.getString("recomment_slave_pid"));
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
               /*     // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(Comment.this);
                    view = inflater.inflate(R.layout.dialog_item8, null);

                    // 对话框
                    dialog = new Dialog(Comment.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth()-100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(view);
                    dialog.setCancelable(false);
                    final TextView btn_sure = (TextView) view.findViewById(R.id.i_understand);// 取消
                    final EditText _item_edit = (EditText) view.findViewById(R.id._item_edit);// 输入框
                    final TextView i_understand1 = (TextView) view.findViewById(R.id.i_understand1);// 输入框
                    final TextView _dialog_title = (TextView) view.findViewById(R.id._dialog_title);// 输入框
                    _dialog_title.setText("回复:"+curr_Map.get("username").toString());
                    btn_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            view = null;
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });
                    i_understand1.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //提交评价内容
                            if(_item_edit.getText().toString().trim().length()>0){
                                yearGoodTeacherDelVote(curr_map.get("id").toString(),curr_map.get("manage_id").toString(),_item_edit.getText().toString());
                            }else {
                                Toast.makeText(getApplicationContext(), "评语不能少于15个字！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
*/
                    break;
                case 17:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Toast.makeText(Comment.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

//                            MyProgressBarDialogTools.hide();
                        } else {// 失败
                            Toast.makeText(Comment.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            message_list.setVisibility(View.VISIBLE);
            comment_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                comment_nodata.setNetErrorIcon();
            } else {
                comment_nodata.setLastEmptyIcon();
            }
            message_list.setVisibility(View.GONE);
            comment_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment_list);
        context = this;
        ButterKnife.bind(this);
        is_Fan = getIntent().getStringExtra("is_Fan");
        catid1 = getIntent().getStringExtra("catid1");
        catid2 = getIntent().getStringExtra("catid2");
        is_message_flg = getIntent().getStringExtra("is_message_flg");
        if (is_message_flg.equals("1")) {//留言
            activityTitleName.setText("留言");
        } else {//评论
            activityTitleName.setText("评论");
        }
        findId();
        initView();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Info.html";
        super.onPause();
    }

    public void initView() {

        baseListAdapterVideo = new BaseListAdapter(message_list, listData, R.layout.item_comment) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                if (is_Fan.equals("1")) {
                    helper.setViewVisibility(R.id._item_fan_icon, View.VISIBLE);
//                    helper.setViewVisibility(R.id._item_rp, View.GONE);
                } else {
                    helper.setViewVisibility(R.id._item_fan_icon, View.GONE);
//                    helper.setViewVisibility(R.id._item_rp, View.GONE);
                }
                helper.setImageBitmapGlide(context, R.id._item_icon, map.get("user_icon").toString());
                helper.setImageBitmapGlide(context, R.id.iv_item_img2, map.get("picurl").toString());
                helper.setText(R.id._item_name, map.get("username").toString());
                helper.setText(R.id._item_time, map.get("create_time").toString());
                helper.setTag(R.id._item_rp, map.get("position").toString());
                helper.setText(R.id._item_content, map.get("content").toString());
                helper.setText(R.id.tv_item_text2, map.get("title").toString(), "html");
                helper.setText(R.id.tv_item_text3, map.get("description").toString(), "html");
                helper.setTag(R.id.video_layout, map.get("position").toString());
                if (is_message_flg.equals("1")) {//is_message_flg 0 不是留言 1 是留言
                    helper.setVisibility(R.id.video_layout, View.GONE);
                } else if (is_message_flg.equals("0")) {
                    helper.setVisibility(R.id.video_layout, View.VISIBLE);
                }
            }
        };
        message_list.setAdapter(baseListAdapterVideo);
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list.getChildAt(message_list.getChildCount() - 1);
                    LogUtil.e("lastVisibleItemView", lastVisibleItemView + "");
                    LogUtil.e(" lastVisibleItemView.getBottom()", lastVisibleItemView.getBottom() + "");
                    LogUtil.e("message_list2.getHeight()", message_list.getHeight() + "");
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list.getHeight()) {
                        if (count > (page * limit)) {
                            page += 1;
                            getUrlRulest();
                        }
                    }
                }
            }
        });


    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.giveMyZansAndComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("catid1", catid1);
                    obj.put("catid2", catid2);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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


    public void rp(View view) {
        curr_Map = listData.get(Integer.parseInt(view.getTag().toString()));
        /*Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);*/

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

            video_menu_comment_edittext.setHint("回复:" + curr_Map.get("username").toString());
            //消息模块评论回复
            video_editext_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"".equals(video_menu_comment_edittext.getText().toString().trim())) {
                        MyProgressBarDialogTools.show(context);
                        getcommentStumit(video_menu_comment_edittext.getText().toString());
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Comment.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
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
     * name：留言回复功能
     */
    public void getcommentStumit(final String content) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.addRecomment);
                    obj.put("uid", SharedPreferencesTools.getUid(Comment.this));
                    obj.put("username", SharedPreferencesTools.getUserName(Comment.this));
                    obj.put("aid", curr_Map.get("aid").toString());
                    //最外层评论id
//                    obj.put("master_pid", master_id);
                    //直属上级的id
                    obj.put("slave_pid", curr_Map.get("recomment_slave_pid").toString());
                    obj.put("content", content);
                    // 1主评论（一级评论）0子评论（n级评论>1）
                    obj.put("type", "0");
                    obj.put("from", "0");
                    String result = HttpClientUtils.sendPost(Comment.this,
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


    public void get_video(View view) {
        Map<String, Object> map = listData.get(Integer.parseInt(view.getTag().toString()));
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        if (VideoFive.isFinish == null) {
            Intent intent = new Intent(context, VideoFive.class);
            intent.putExtra("aid", map.get("aid").toString());
            startActivity(intent);
        }
    }

}
