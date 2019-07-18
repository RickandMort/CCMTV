package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDetialCommentAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineComment;
import com.linlic.ccmtv.yx.kzbf.bean.MoreCommentBean;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 更多评论
 */
public class MoreCommentActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    private RecyclerView recyclerView;
    private int page = 1;
    private int count;
    private int itemCount;
    private List<DbMedicineComment> listMore = new ArrayList<>();
    private NodataEmptyLayout lt_nodata1;
    private MedicineDetialCommentAdapter mdcAdapter;
    private DbMedicineComment dbMc;
    private String is_laud;//是否点赞
    private String pid;
    private String content;
    private int zan_num;
    private String aid;
    private String uid;
    private String id;
    private String commentId;
    private int mPosition;
    private TextView title, comment_submit;
    private EditText et_comment;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = mResponse.getCode();
                        if (code.equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));
                            recyclerView.setVisibility(View.VISIBLE);
                            lt_nodata1.setVisibility(View.GONE);
                            JSONObject obj = jsonObject.getJSONObject("article");
                            title.setText(obj.getString("title"));
                            comment_all.setText("全部评论(" + obj.getInt("comment_count_num") + ")");
                            initCommentList(jsonObject);//填充评论列表
                            mdcAdapter.loadMoreComplete();
                        } else {
                            mdcAdapter.loadMoreFail();
                            recyclerView.setVisibility(View.GONE);
                            lt_nodata1.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        mdcAdapter.notifyDataSetChanged();
                        if ("code".equals(code)) {
                            setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        } else {
                            setResultStatus(listMore.size() > 0, Integer.parseInt(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            mPosition = msg.arg1;
                            if (is_laud.equals("0")) {//变成已点赞
                                zan_num++;

                                //测试--bentley
                                itemMedicineComment.getCommentItemBean().setIs_laud("1");
                                itemMedicineComment.getCommentItemBean().setLaud_num(zan_num + "");

                                /*mResponse.getData().get(mPosition).setIs_laud("1");
                                mResponse.getData().get(mPosition).setLaud_num(zan_num + "");*/
                            } else {//变成未点赞
                                if (zan_num != 0) {
                                    zan_num--;
                                }

                                //测试--bentley修改点击带有子评论下方的评论 赞和回复出现的问题
                                itemMedicineComment.getCommentItemBean().setIs_laud("0");
                                itemMedicineComment.getCommentItemBean().setLaud_num(zan_num + "");

                                /*mResponse.getData().get(mPosition).setIs_laud("0");
                                mResponse.getData().get(mPosition).setLaud_num(zan_num + "");*/
                            }
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        mdcAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            listMore.clear();
                            initData();
                            setValue();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            listMore.clear();
                            initData();
                            setValue();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };
    private TextView comment_all;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    private DbMedicineComment itemMedicineComment;

    //填充评论列表
    private void initCommentList(JSONObject jsonObject) throws JSONException {
        List<MoreCommentBean.DataBean> CommentList = mResponse.getData();//评论列表
        for (int i = 0; i < CommentList.size(); i++) {
            MoreCommentBean.DataBean commentItemBean = CommentList.get(i);
            dbMc = new DbMedicineComment(2);
            dbMc.setCommentItemBean(commentItemBean);
            listMore.add(dbMc);
            List<MoreCommentBean.DataBean.TwoCommentBean> twoCommentList = commentItemBean.getTwo_comment();
            if (twoCommentList.size() != 0) {
                for (int j = 0; j < twoCommentList.size(); j++) {
                    if (j > 1) {//当评论多余2条时候，显示展开更多条目
                        dbMc = new DbMedicineComment(4);
                        dbMc.setCommentItemBean(commentItemBean);//将全部本条评论的全部数据加入，方便点击展开全部后处理
                        listMore.add(dbMc);
                        break;
                    }
                    MoreCommentBean.DataBean.TwoCommentBean commentItemChileBean = twoCommentList.get(j);
                    dbMc = new DbMedicineComment(3);
                    dbMc.setCommentItemChileBean(commentItemChileBean);
                    listMore.add(dbMc);
                }
            }
            dbMc = new DbMedicineComment(5);
            listMore.add(dbMc);
        }
        itemCount = mdcAdapter.getItemCount();//总item数
        mdcAdapter.notifyDataSetChanged();
    }

    private MoreCommentBean mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_comment);
        context = this;

        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.all_comment_list);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.rl_more_comment_nodata1);
        et_comment = (EditText) findViewById(R.id.et_comment);
        comment_submit = (TextView) findViewById(R.id.comment_submit);
    }


    HashMap<String, List<DbMedicineComment>> childComments = new HashMap<>();

    private void initData() {
        page = 1;
        comment_submit.setOnClickListener(this);

        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(context));
        mdcAdapter = new MedicineDetialCommentAdapter(context, listMore);
        recyclerView.setAdapter(mdcAdapter);
        setHeader(recyclerView);

        mdcAdapter.disableLoadMoreIfNotFullPage(recyclerView);//取消第一次进入加载 下拉加载更多方法
        mdcAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position + 1)) {//如果点击的是展开全部条目
                    case DbMedicineComment.TYPE_EXPAND_ALL:
//                        Toast.makeText(getApplicationContext(), "展开全部", Toast.LENGTH_SHORT).show();
                        DbMedicineComment commentBean = listMore.get(position);
                        MoreCommentBean.DataBean commentItemBean = commentBean.getCommentItemBean();
                        List<DbMedicineComment> newList = childComments.get(commentBean.getComment_id());
                        if (newList == null) {
                            newList = new ArrayList<DbMedicineComment>();
                            List<MoreCommentBean.DataBean.TwoCommentBean> twoCommentList = commentItemBean.getTwo_comment();//全部回复列表
                            for (int i = 2; i < twoCommentList.size(); i++) {
                                MoreCommentBean.DataBean.TwoCommentBean commentItemChileBean = twoCommentList.get(i);
                                dbMc = new DbMedicineComment(3);
                                dbMc.setCommentItemChileBean(commentItemChileBean);
                                newList.add(dbMc);


                                DbMedicineComment medicineComment = new DbMedicineComment(6);
                                medicineComment.setComment_id(commentBean.getComment_id());
                                medicineComment.setCommentItemBean(commentItemBean);
                                newList.add(medicineComment);//点击收起更多评论的条目
                            }
                            childComments.put(commentBean.getComment_id(), newList);
                        }
                        listMore.remove(position);
                        listMore.addAll(position, newList);
                        adapter.notifyDataSetChanged();
                        break;
                    case DbMedicineComment.TYPE_UNEXPAND_ALL:
                        DbMedicineComment dbMedicineComment = listMore.get(position);
                        List<DbMedicineComment> medicineComments = childComments.get(dbMedicineComment.getComment_id());

                        DbMedicineComment medicineComment = new DbMedicineComment(4);
                        medicineComment.setComment_id(dbMedicineComment.getComment_id());
                        medicineComment.setCommentItemBean(medicineComment.getCommentItemBean());
                        if (medicineComments != null) {
                            listMore.removeAll(medicineComments);
                            position = position - (medicineComments.size() - 1);
                        } else {
                            listMore.remove(position);
                        }
                        listMore.add(position, medicineComment);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

        et_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    if (TextUtils.isEmpty(et_comment.getText().toString())) {
                        Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (et_comment.getText().toString().equals("写评论...")) {
                            setComment();
                        } else {
                            setReply();
                        }
                        et_comment.setText("");
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
                itemMedicineComment = (DbMedicineComment) adapter.getItem(position);
                if (position != 0) {
                    position = position / 2;
                }
                switch (view.getId()) {
                    case R.id.medicine_comment_zan_img://点赞

                        //测试--bentley
                        is_laud = itemMedicineComment.getCommentItemBean().getIs_laud();
                        id = itemMedicineComment.getCommentItemBean().getAid();
                        uid = itemMedicineComment.getCommentItemBean().getUid();
                        commentId = itemMedicineComment.getCommentItemBean().getId();
                        zan_num = Integer.parseInt(itemMedicineComment.getCommentItemBean().getLaud_num());

                       /* is_laud = mResponse.getData().get(position).getIs_laud();
                        id = mResponse.getData().get(position).getAid();
                        uid = mResponse.getData().get(position).getUid();
                        commentId = mResponse.getData().get(position).getId();
                        zan_num = Integer.parseInt(mResponse.getData().get(position).getLaud_num());*/
                        setCommentPraise(position);
                        break;
                    case R.id.medicine_comment_reply://回复
                        //测试--bentley
                        pid = itemMedicineComment.getCommentItemBean().getId();
                        et_comment.setHint("回复 " + itemMedicineComment.getCommentItemBean().getUsername() + ":");

                        /*pid = mResponse.getData().get(position).getId();
                        et_comment.setHint("回复 " + mResponse.getData().get(position).getUsername() + ":");*/
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        et_comment.setFocusable(true);
                        et_comment.requestFocus();
                        break;
                }
            }
        });

        //上拉加载更多
        mdcAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            mdcAdapter.loadMoreEnd();
                        } else {
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    private void setValue() {
        aid = getIntent().getStringExtra("aid");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.infoComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("page", page);

//                    Log.e("看看上行文章评论详2细数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
                    Gson gson = new Gson();
                    mResponse = gson.fromJson(result, MoreCommentBean.class);

//                    Log.e("看看文章评论详2细数据", result);
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

    @Override
    protected void onResume() {
        listMore.clear();
        initData();
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

    //评论赞/取消赞
    private void setCommentPraise(final int position) {
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

                    Message message = new Message();
                    message.what = 2;
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

    //发布评论
    private void setComment() {
        content = et_comment.getText().toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.postComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("content", content);

//                    Log.e("看看上行评论数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看评论数据", result);

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

    //发布回复
    private void setReply() {
        content = et_comment.getText().toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.postComment);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("content", content);
                    obj.put("pid", pid);

//                    Log.e("看看上行回复数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看回复数据", result);

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

    //设置头部
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(context).inflate(R.layout.item_comment_head, view, false);
        comment_all = header.findViewById(R.id.tv_comment_all);
        title = (TextView) header.findViewById(R.id.tv_comment_item_title);
        mdcAdapter.setHeaderView(header);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_submit:
                if (TextUtils.isEmpty(et_comment.getText().toString())) {
                    Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_comment.getText().toString().equals("写评论...")) {
                        setComment();
                    } else {
                        setReply();
                    }
                    et_comment.setText("");
                }
                break;
        }
    }
}
