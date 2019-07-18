package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.SearchAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbSearchArticle;
import com.linlic.ccmtv.yx.kzbf.bean.SearchDataBean;
import com.linlic.ccmtv.yx.kzbf.bean.SearchMultiltemEntity;
import com.linlic.ccmtv.yx.util.KeyboardUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {
    @Bind(R.id.btn_yaoxun)
    Button mBtnYaoxun;
    @Bind(R.id.btn_yaodai)
    Button mBtnYaodai;
    @Bind(R.id.btn_zhinan)
    Button mBtnZhinan;
    @Bind(R.id.btn_wenxian)
    Button mBtnWenxian;
    @Bind(R.id.rv_search)
    RecyclerView mRvSearch;
    private TextView load_more;
    private final int YAOXUN = 1001;
    private final int YAODAI = 1002;
    private final int ZHINAN = 1003;
    private final int WENXIAN = 1004;
    private String yd = "1";
    private String yx = "1";
    private String zn = "1";
    private String wx = "1";
    private boolean once = false;
    private int count;
    private int itemCount;
    private int historyNum;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.tv_dismiss)
    TextView mTvDismiss;
    @Bind(R.id.rl_search_nodata)
    NodataEmptyLayout rl_search_nodata;
    Context context;
    private View footer;
    private int page = 1;
    public static String keyword = "";
    public static String cid = "";
    private ArrayList<SearchMultiltemEntity> mList;
    private ArrayList<SearchMultiltemEntity> mAllList;
    private ArrayList<SearchMultiltemEntity> historyList;
    private List<DbSearchArticle> articleAll;
    private SearchAdapter mAdapter;
    private String focus_id;
    private SearchMultiltemEntity entity;
    private Intent intent;
    private boolean isEnter = true;
    private boolean isHisEnter = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));
                            if (keyword.equals("")) {
                                mRvSearch.setVisibility(View.VISIBLE);
                                rl_search_nodata.setVisibility(View.GONE);
                                reqList((String) msg.obj);
                                //判断有无历史记录，有就加到集合中
                                historyList();
                            } else {
                                reqData((String) msg.obj);
                            }
                        } else {
                            mAdapter.loadMoreFail();
                        }
//                        if ("code".equals(code)) {
//                            setResultStatus(keyword.equals(""), HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
//                        } else {
//                            setResultStatus(keyword.equals(""), Integer.valueOf(code));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        setResultStatus(!keyword.equals(""), HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            int focusPosition = msg.arg1;
                            mList.get(focusPosition).getBean().setIs_foncus("1");
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
//                    setResultStatus(false, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            mRvSearch.setVisibility(View.VISIBLE);
            rl_search_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_search_nodata.setNetErrorIcon();
            } else {
                rl_search_nodata.setLastEmptyIcon();
            }
            mRvSearch.setVisibility(View.GONE);
            rl_search_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;

        initView();
        initData();
        setValue();
        initListener();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void initData() {
        initRecycler();
    }

    @Override
    protected void onResume() {
        isEnter = true;
        isHisEnter = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.search);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("key", keyword.length() > 0 ? keyword : "");
                    obj.put("cid", cid.length() > 0 ? cid : "");

//                    Log.e("看看搜索数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看搜索数据", result);

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

    private void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //回车键按下时要执行的操作
                    KeyboardUtils.hideKeyboard(SearchActivity.this);
                    keyword = mEtSearch.getText().toString();
                    if (yx.equals("1") && yd.equals("1") && zn.equals("1") && wx.equals("1")) {
                        cid = "";
                    } else if (yx.equals("1")) {
                        cid = "1";
                    } else if (yd.equals("1")) {
                        cid = "4";
                    } else if (zn.equals("1")) {
                        cid = "2";
                    } else if (wx.equals("1")) {
                        cid = "3";
                    }
                    if (mList.size() != 0) {
                        mList.clear();
                    }
                    setValue();
                    //判断keyword是否为空，不为空就保存到历史中
                    if (!keyword.equals("")) {
                        MyDbUtils.saveAtricle(context, keyword);
                    }
                }
                return false;
            }
        });
    }

    private void reqData(String rsp) {
//        rsp = "{    \"code\": \"0\",    \"data\": [        {            \"cid\": \"2\",            \"id\": \"84\",            \"title\": \"附件测试\"        },        {            \"cid\": \"1\",            \"id\": \"68\",            \"title\": \"短信测试文章\"        },        {            \"cid\": \"2\",            \"id\": \"83\",            \"title\": \"测试测试测试测试测试测试测试测试测试测试\"        },        {            \"cid\": \"1\",            \"id\": \"57\",            \"title\": \"测试测试测试测试测试测试测试测试测试测试\"        },        {            \"cid\": \"2\",            \"id\": \"92\",            \"title\": \"测试测试测试测试\"        },        {            \"cid\": \"1\",            \"id\": \"97\",            \"title\": \"测试测试测试上传\"        },        {            \"cid\": \"1\",            \"id\": \"55\",            \"title\": \"测试测试\"        },        {            \"cid\": \"2\",            \"id\": \"23\",            \"title\": \"测试文章测试1\"        },        {            \"cid\": \"1\",            \"id\": \"24\",            \"title\": \"测试数据\"        },        {            \"cid\": \"4\",            \"company\": \"测试测试测试\",            \"drug\": \"测试测试测试\",            \"helper\": \"测试帐号12\",            \"uid\": \"10158805\",            \"user_img\": \"http://www.ccmtv.cn/images/default/noface.gif\"        }    ],    \"msg\": \"\",    \"r_list\": [        \"测试\",        \"小赛\",        \"测试测试\"    ]}";
        Gson gson = new Gson();
        SearchDataBean dataBean = gson.fromJson(rsp, SearchDataBean.class);
        List<SearchDataBean.DataBean> beanList = dataBean.getData();
        if (beanList.size() > 0) {
            mRvSearch.setVisibility(View.VISIBLE);
            rl_search_nodata.setVisibility(View.GONE);
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).getCid().equals("4")) {//用户状态代表药代
                    entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_YAODAI);
                } else {
                    entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_BASE);
                }
                entity.setBean(beanList.get(i));
                mList.add(entity);
            }
            itemCount = mAdapter.getItemCount();
            mAdapter.loadMoreComplete();
            mAdapter.notifyDataSetChanged();
            mAllList = new ArrayList<>();
            mAllList.addAll(mList);
        } else {
            mRvSearch.setVisibility(View.GONE);
            rl_search_nodata.setVisibility(View.VISIBLE);
        }
//        setResultStatus(beanList.size() > 0, 0);
    }

    private void reqList(String rsp) {
        Gson gson = new Gson();
        SearchDataBean dataBean = gson.fromJson(rsp, SearchDataBean.class);
        List<String> r_list = dataBean.getR_list();
        for (int i = 0; i < r_list.size(); i++) {
            entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_HOT);
            entity.setHotTitle(r_list.get(i));
            mList.add(entity);
        }
        mAdapter.notifyDataSetChanged();
    }

    //判断有无历史记录，有就加到集合中
    private void historyList() {
        articleAll = MyDbUtils.findArticleAll(context);
        historyNum = articleAll.size();
        if (historyNum > 0) {
            for (int i = 0; i < historyNum; i++) {
                entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_HISTORY);
                entity.setHotTitle(articleAll.get(i).getArticleName());
                entity.setHotId(articleAll.get(i).getId() + "");
                mList.add(entity);
            }
            /*if (historyNum < 10) {
                for (int i = 0; i < historyNum; i++) {
                    entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_HISTORY);
                    entity.setHotTitle(articleAll.get(i).getArticleName());
                    entity.setHotId(articleAll.get(i).getId() + "");
                    mList.add(entity);
                }
                setFooter(mRvSearch);
                load_more.setText("没有更多数据");
            } else {
                setFooter(mRvSearch);
                load_more.setText("点击加载更多");
                for (int i = 0; i < 9; i++) {
                    entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_HISTORY);
                    entity.setHotTitle(articleAll.get(i).getArticleName());
                    entity.setHotId(articleAll.get(i).getId() + "");
                    mList.add(entity);
                }
            }*/
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRecycler() {
        mList = new ArrayList<>();
        mRvSearch.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new SearchAdapter(context, mList);
        mRvSearch.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEnter) {
                    switch (mList.get(position).getBean().getCid()) {
                        case "1"://动态
                            intent = new Intent(context, MedicineDetialActivity.class);
                            intent.putExtra("id", mList.get(position).getBean().getId());
                            startActivity(intent);
                            break;
                        case "2"://指南
                            intent = new Intent(context, SpecialGuideDetialActivity.class);
                            intent.putExtra("id", mList.get(position).getBean().getId());
                            startActivity(intent);
                            break;
                        case "3"://文献
                            intent = new Intent(context, LiteratureDetialActivity.class);
                            intent.putExtra("id", mList.get(position).getBean().getId());
                            startActivity(intent);
                            break;
                        case "4"://药代
                            intent = new Intent(context, PersonalHomeActivity.class);
                            intent.putExtra("fuid", mList.get(position).getBean().getUid());
                            startActivity(intent);
                            break;
                    }
                    isEnter = false;
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRvSearch.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            page++;
                            setValue();
                        }
                    }
                }, 1500);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_yd_focus:
                        focus_id = mList.get(position).getBean().getUid();
                        setFouse(position);
                        break;
                    case R.id.tv_yd_consult:
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("addressee_uid", mList.get(position).getBean().getUid());
                        intent.putExtra("helper", mList.get(position).getBean().getHelper());
                        intent.putExtra("assistant", "0");
                        startActivity(intent);
                        break;
                    case R.id.ll_item_hot:
                        changeColorGray();
                        keyword = mList.get(position).getHotTitle();
                        cid = "";
                        mEtSearch.setText(keyword);
                        if (mList.size() != 0) {
                            mList.clear();
                        }
                        setValue();
                        break;
                    case R.id.ll_item_history:
                        if (TextUtils.isEmpty(mEtSearch.getText().toString())) {
                            if (isHisEnter) {
                                changeColorGray();
                                keyword = mList.get(position).getHotTitle();
                                cid = "";
                                mEtSearch.setText(keyword);
                                if (mList.size() != 0) {
                                    mList.clear();
                                }
                                setValue();
                                isHisEnter = false;
                            }
                        }
                        break;
                    case R.id.iv_history_delete:
                        MyDbUtils.deleteArticle(context, mList.get(position).getHotId());
                        mAdapter.remove(position);
                        break;
                }
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_dismiss, R.id.btn_yaoxun, R.id.btn_yaodai, R.id.btn_zhinan, R.id.btn_wenxian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                cancelAll();
                finish();
                break;
            case R.id.tv_dismiss:
                isHisEnter = true;
                cancelAll();
                page = 1;
                break;
            case R.id.btn_yaoxun:
                choice(YAOXUN);
                changeList("1");
                yx = yx.equals("0") ? "1" : "0";
                break;
            case R.id.btn_yaodai:
                choice(YAODAI);
                changeList("4");
                yd = yd.equals("0") ? "1" : "0";
                break;
            case R.id.btn_zhinan:
                choice(ZHINAN);
                changeList("2");
                zn = zn.equals("0") ? "1" : "0";
                break;
            case R.id.btn_wenxian:
                choice(WENXIAN);
                changeList("3");
                wx = wx.equals("0") ? "1" : "0";
                break;
        }
    }

    private void cancelAll() {
        changeColorGray();
        mEtSearch.setText("");
        keyword = "";
        if (mList.size() != 0) {
            mList.clear();
        }
        if (mAllList != null) {
            mAllList.clear();
        }
        setValue();
        mAdapter.notifyDataSetChanged();
    }

    private void changeColorGray() {
        mBtnYaodai.setTextColor(Color.parseColor("#88000000"));
        yd = "1";
        mBtnYaoxun.setTextColor(Color.parseColor("#88000000"));
        yx = "1";
        mBtnZhinan.setTextColor(Color.parseColor("#88000000"));
        zn = "1";
        mBtnWenxian.setTextColor(Color.parseColor("#88000000"));
        wx = "1";
    }

    private void changeList(String cid) {
        if (mAllList != null) {
            if (mAllList.size() != 0) {
                mList.clear();
                for (int i = 0; i < mAllList.size(); i++) {
                    if (mAllList.get(i).getBean().getCid().equals(cid)) {
                        SearchMultiltemEntity entity;
                        if (cid.equals("4")) {//如果是药代
                            entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_YAODAI);
                        } else {
                            entity = new SearchMultiltemEntity(SearchMultiltemEntity.TYPE_ITEM_ALONE);
                        }
                        entity.setBean(mAllList.get(i).getBean());
                        mList.add(entity);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    rl_search_nodata.setVisibility(View.VISIBLE);
                    mRvSearch.setVisibility(View.GONE);
                } else {
                    rl_search_nodata.setVisibility(View.GONE);
                    mRvSearch.setVisibility(View.VISIBLE);
                }
            } else {
                rl_search_nodata.setVisibility(View.VISIBLE);
                mRvSearch.setVisibility(View.GONE);
            }
        }
    }

    private void choice(int i) {
        mBtnYaoxun.setTextColor(0x88000000);
        mBtnYaodai.setTextColor(0x88000000);
        mBtnZhinan.setTextColor(0x88000000);
        mBtnWenxian.setTextColor(0x88000000);
        if (!once) {
            switch (i) {
                case YAOXUN:
                    mBtnYaoxun.setTextColor(0xff3899FA);
                    break;
                case YAODAI:
                    mBtnYaodai.setTextColor(0xff3899FA);
                    break;
                case ZHINAN:
                    mBtnZhinan.setTextColor(0xff3899FA);
                    break;
                case WENXIAN:
                    mBtnWenxian.setTextColor(0xff3899FA);
                    break;
            }
        } else {
            switch (i) {
                case YAOXUN:
                    mBtnYaoxun.setTextColor(0x88000000);
                    break;
                case YAODAI:
                    mBtnYaodai.setTextColor(0x88000000);
                    break;
                case ZHINAN:
                    mBtnZhinan.setTextColor(0x88000000);
                    break;
                case WENXIAN:
                    mBtnWenxian.setTextColor(0x88000000);
                    break;
            }
        }
    }

    private void setFouse(final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_id", focus_id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看关注数据", result);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelAll();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e("TestResult", "ActivityDestory");

        test();
    }

    private void test() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.inbox);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看收件箱数据", result);

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("code").equals("0")) {
//                        Log.e("TestResult", "TestSuccess");
                    } else {
//                        Log.e("TestResult", "TestFail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Log.e("TestResult", "TestException");
                }
            }
        };
        new Thread(runnable).start();
    }


}
