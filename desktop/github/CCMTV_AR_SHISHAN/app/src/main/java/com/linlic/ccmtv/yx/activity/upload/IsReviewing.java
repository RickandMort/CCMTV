package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：正在审核
 * author：MrSong
 * data：2016/3/30.
 */
public class IsReviewing extends BaseActivity {
    private View viewOne;
    private View viewTwo;
    private ListView listView;
    private int page;//当前页码
    private String isPageState = URLConfig.video;
    private Context context;
    //用户统计
    BaseListAdapter baseListAdapter_video;
    BaseListAdapter baseListAdapter_case;
    TextView tv_isreviewVideo, tv_isreviewCase;
    private boolean isNoMore = false;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0 && page == 1) {//后续加上页码为1
                                showNoData();
                                return;
                            }
                            if (page != 1 && dataArray.length() < 10) {
                                isNoMore = true;
                                Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                            }

                            for (int i = 0; i < dataArray.length(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                JSONObject object = dataArray.getJSONObject(i);
//                                Log.i("hhhhhh", "object" + object.toString());
                                if (isPageState.equals(URLConfig.medicalrecord)) {
                                    map.put("mvtitle", object.getString("mvtitle"));
                                    map.put("imgurl", object.getString("imgurl"));
                                    map.put("row_add_time", object.getString("row_add_time"));

                                } else {
                                    map.put("mvtitle", object.getString("mvtitle"));
                                    map.put("imgurl", object.getString("imgurl"));
                                    map.put("row_add_time", object.getString("row_add_time"));
                                }
                                map.put("shenhe", "正在审核中");
                                data.add(map);
                            }


                            if (isPageState.equals(URLConfig.video)) {
                                listView.setAdapter(baseListAdapter_video);
                                baseListAdapter_video.notifyDataSetChanged();
                            } else {
                                listView.setAdapter(baseListAdapter_case);
                                baseListAdapter_case.notifyDataSetChanged();
                            }
                            hideNoData();
                        } else {
                            if (result.getInt("status") == 0 && result.getString("errorMessage").equals("暂无数据!")) {
                                showNoData();
                                isNoMore = true;
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isreview);
        context = this;
        //第一次进入调用一下，不然会报错
        if (SharedPreferencesTools.getUid(context).equals("")) return;
        findViewById();
        setText();
        page = 1;
        initData(isPageState);
        setListener();

    }


    private void findViewById() {
        super.findId();
        super.setClick();
        super.setActivity_btnnodata(R.string.btnnodata_toup);
        super.setActivity_tvnodata(R.string.tvnodata_toup);
        super.setActivity_title_name(R.string.is_the_audit);
        viewOne = findViewById(R.id.selectItemOptions_one);
        viewTwo = findViewById(R.id.selectItemOptions_two);
        listView = (ListView) findViewById(R.id.upload_selectPageList);
        tv_isreviewVideo = (TextView) findViewById(R.id.tv_isreviewVideo);
        tv_isreviewCase = (TextView) findViewById(R.id.tv_isreviewCase);
        tv_isreviewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                isPageState = URLConfig.video;
                page = 1;
                isNoMore = false;
                initData(isPageState);
                viewOne.setVisibility(View.VISIBLE);
                viewTwo.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        tv_isreviewCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                isPageState = URLConfig.medicalrecord;
                page = 1;
                isNoMore = false;
                initData(isPageState);
                viewOne.setVisibility(View.INVISIBLE);
                viewTwo.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setText() {
        baseListAdapter_video = new BaseListAdapter(listView, data, R.layout.uploadvideo_review) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.upload_item_title, ((Map) item).get("mvtitle") + "");
                helper.setText(R.id.upload_times, ((Map) item).get("row_add_time") + "");
                // helper.setText(R.id.upload_on_demand, ((Map) item).get("shenhe") + "");
//                Log.i("hhhhhh", ((Map) item).get("imgurl") + "");
                helper.setImageBitmap(R.id.upload_item_img, ((Map) item).get("imgurl") + "");
            }
        };
        baseListAdapter_case = new BaseListAdapter(listView, data, R.layout.uploadcase_review) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.dupload_names, ((Map) item).get("mvtitle") + "");
                helper.setText(R.id.upload_tiems, ((Map) item).get("row_add_time") + "");
                helper.setImageBitmap(R.id.iv_upcase, ((Map) item).get("imgurl") + "");
            }
        };

    }

    private void setListener() {

        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (isPageState.equals(URLConfig.upVideo)) {
                    intent.setClass(IsReviewing.this, Upload_video.class);
                } else {
                    intent.setClass(IsReviewing.this, Upload_case.class);
                }
                startActivity(intent);
            }
        });

        baseListAdapter_video.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        page += 1;
                        initData(isPageState);
                    }
                }
            }
        });

        baseListAdapter_case.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listView.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData(isPageState);
                        }

                    }
                }
            }
        });

    }


    /**
     * name：加载数据
     * author：Larry
     * data：2016/5/19 17:19
     */
    private void initData(final String act) {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("act", URLConfig.reviewVM);
                    object.put("uid", SharedPreferencesTools.getUid(getApplicationContext()));
                    object.put("flg", act);
                    object.put("page", page);
                    String json = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                    handler.sendMessage(handler.obtainMessage(1, json));
                } catch (Exception e) {
                    handler.sendEmptyMessage(500);
                    e.printStackTrace();
                }
            }
        }).start();
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

}
