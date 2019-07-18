package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Get_integral_list_item;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.adapter.Get_IntegralAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.CircleImageView;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name:积分获取记录
 * author:Tom
 * 2016-3-2下午7:04:24
 */
public class Get_Integral extends BaseActivity {
    private RecyclerView recyclerView;
    private ListView get_integral_list;
    private String uid, integration, UserName;
    CircleImageView circle_item_img33;
    private Get_IntegralAdapter get_integralAdapter;
    private List<Get_integral_list_item> data = new ArrayList<Get_integral_list_item>();
    private NodataEmptyLayout lintegral_nodata;
    Context context;
    private int page = 1;
    private boolean is = true;
    //领取积分
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                is = false;
                                if (data.size() < 1) {
//                                    showNoData();
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } else {
//                                hideNoData();
                                if (dataArray.length() < 10) {
                                    is = false;
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    data.add(new Get_integral_list_item(dataArray.getJSONObject(i)));
                                }
                                get_integralAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            is = false;
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(data.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
//                        showNoData();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            lintegral_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lintegral_nodata.setNetErrorIcon();
            } else {
                lintegral_nodata.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            lintegral_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.get_integral);
        context = this;
        findId();
//        setText();
        loadData2();
        init();
    }

    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    @Override
    public void findId() {
        // TODO Auto-generated method stub
        super.findId();
     /*   get_integral_list = (ListView) findViewById(R.id.get_integral_list);
        cases_detail_author = (TextView) findViewById(R.id.cases_detail_author);
        cases_detail_time = (TextView) findViewById(R.id.cases_detail_time);
        circle_item_img33 = (CircleImageView) findViewById(R.id.circle_item_img33);*/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
        get_integral_list = (ListView) findViewById(R.id.get_integral_list);
        get_integral_list.setVisibility(View.GONE);
        lintegral_nodata = (NodataEmptyLayout) findViewById(R.id.lintegral_nodata);
    }

    public void setText() {
        super.setActivity_title_name(R.string.get_integral_name);
//        integration = getIntent().getStringExtra("integration");
        String Str_Integral = SharedPreferencesTools.getIntegral(Get_Integral.this);
        UserName = SharedPreferencesTools.getUserName(context);
        //显示头像
        String Str_icon = SharedPreferencesTools.getStricon(context);
        loadImg(circle_item_img33, Str_icon);
    }

    public void init() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        get_integralAdapter = new Get_IntegralAdapter(data);
        recyclerView.setAdapter(get_integralAdapter);
//        我们只需要给recycleview设置一个滚动的监听器即可，在里面通过layoutmanager来判断是否滑动到了底部，到了就自动加载更多

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int lastVisiblePosition = linearManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= layoutManager.getItemCount() - 1) {
                        if (is) {
                            page++;
                            loadData2();
                        }
                    }
                }
            }
        });


    }

    @Override
    public void showNoData() {
//        layout_nodata.setVisibility(View.VISIBLE);
//        btn_nodata.setVisibility(View.GONE);
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                loadData();
                loadData2();
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name:导入初始值 author:Tom 2016-1-28下午3:41:32
     */
    public void loadData() {

        uid = SharedPreferencesTools.getUid(context);
        if ("".equals(uid)) {
            startActivity(new Intent(Get_Integral.this, LoginActivity.class));
        }
        try {
            JSONObject obj = new JSONObject();
            obj.put("uid", uid);
            obj.put("flg", "get");
            obj.put("act", URLConfig.integrationRecord);
            String result = HttpClientUtils.sendPost(context,
                    URLConfig.CCMTVAPP, obj.toString());
            Message message = new Message();
            message.what = 1;
            message.obj = result;
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(500);
        }
    }


    public void loadData2() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                uid = SharedPreferencesTools.getUid(context);
                if ("".equals(uid)) {
                    startActivity(new Intent(Get_Integral.this, LoginActivity.class));
                }
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", uid);
                    obj.put("page", page);
                    obj.put("act", URLConfig.integrationAllRecord);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
//                    LogUtil.e("积分明细",result);
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

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getApplicationContext());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
