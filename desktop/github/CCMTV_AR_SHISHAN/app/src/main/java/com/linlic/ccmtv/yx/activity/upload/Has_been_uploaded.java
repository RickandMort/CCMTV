package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
 * name：已经上传
 * author：MrSong
 * data：2016/3/29 19:55
 */
public class Has_been_uploaded extends BaseActivity {
    private View viewOne;
    private View viewTwo;
    private ListView listView;
    private String isPageState = URLConfig.upVideo;
    private Context context;
    BaseListAdapter baseListAdapter_video;
    BaseListAdapter baseListAdapter_case;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private int page = 1;
    JSONObject object;
    TextView tv_selectVideo, tv_selectCase;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MyProgressBarDialogTools.hide();
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject((String) msg.obj);
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {//后续加上页码为1
                                showNoData();
                                return;
                            }
                            data.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                object = dataArray.getJSONObject(i);
                                if (isPageState.equals(URLConfig.upVideo)) {
                                    map.put("mvtitle", object.getString("title"));
                                    map.put("imgurl", object.getString("picurl"));
                                    map.put("row_add_time", object.getString("list"));
                                    map.put("id", object.getString("aid"));
                                    map.put("money", object.getString("money"));
                                    map.put("videopaymoney", object.getString("videopaymoney"));
                                } else {
                                    map.put("mvtitle", object.getString("mvtitle"));
                                    map.put("id", object.getString("id"));
                                    map.put("shenhe", object.getString("row_add_time"));
                                    map.put("upfileA", object.getJSONArray("upfileA").get(0));
                                }
                                data.add(map);
                            }
                            if (isPageState.equals(URLConfig.upVideo)) {
                                baseListAdapter_video.notifyDataSetChanged();
                            } else {
                                baseListAdapter_case.notifyDataSetChanged();
                            }

                            hideNoData();
                        } else {
                            if (result.getInt("status") == 0 && result.getString("errorMessage").equals("暂无数据!")) {
                                showNoData();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 500:
                    Toast.makeText(context, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.has_been_uploaded);

        context = this;

        //第一次进入调用一下，不然会报错
        if (SharedPreferencesTools.getUid(context).equals("")) return;

        findViewById();

        baseListAdapter_video = new BaseListAdapter(listView, data, R.layout.upload_item1) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.upload_item_title, ((Map) item).get("mvtitle") + "");
                if (!TextUtils.isEmpty(((Map) item).get("row_add_time") + "")) {
                    helper.setText(R.id.upload_times, ((Map) item).get("row_add_time") + "");
                }

                if (!"null".equals(((Map) item).get("shenhe") + "")) {
                    helper.setText(R.id.upload_on_demand, ((Map) item).get("shenhe") + "");
                }
                helper.setImageBitmap(R.id.upload_item_img, ((Map) item).get("imgurl") + "");
            }
        };

        baseListAdapter_case = new BaseListAdapter(listView, data, R.layout.upload_item2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.dupload_names, ((Map) item).get("mvtitle") + "");
                helper.setText(R.id.upload_tiems, ((Map) item).get("shenhe") + "");
//                Log.i("iv_upcase", ((Map) item).get("imgurl") + "");
                helper.setImageBitmap(R.id.iv_upcase, ((Map) item).get("upfileA") + "");
            }
        };
        listView.setAdapter(baseListAdapter_video);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isPageState.equals(URLConfig.upMedicalrecord)) {
                   /* map.put("mvtitle", object.getString("mvtitle"));
                    map.put("id", object.getString("id"));
                    map.put("shenhe", object.getString("row_add_time"));*/
//                    Log.i("hhahah", "obj" + object.toString());
                    Intent intent = new Intent(Has_been_uploaded.this, MyHasUpCaseActivity.class);
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("obj", object.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String aid = data.get(position).get("id").toString();
                    MyProgressBarDialogTools.show(context);
                    getVideoRulest(aid);
                }
            }
        });
        openURL(isPageState, 1);
        setListener();
    }

    /**
     * name：点击查看某个视频的详细
     * author：Larry
     * data：2016/4/5 16:41
     */
    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);

    }

    private void findViewById() {
        super.findId();
        super.setClick();
        super.setActivity_btnnodata(R.string.btnnodata_toup);
        super.setActivity_tvnodata(R.string.tvnodata_toupreview);
        super.setActivity_title_name(R.string.has_been_uploaded);

        viewOne = findViewById(R.id.selectItemOptions_one);
        viewTwo = findViewById(R.id.selectItemOptions_two);
        listView = (ListView) findViewById(R.id.upload_selectPageList);
        tv_selectVideo = (TextView) findViewById(R.id.tv_selectVideo);
        tv_selectCase = (TextView) findViewById(R.id.tv_selectCase);
        tv_selectCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                isPageState = URLConfig.upMedicalrecord;
                listView.setAdapter(baseListAdapter_case);
                viewOne.setVisibility(View.INVISIBLE);
                viewTwo.setVisibility(View.VISIBLE);
                openURL(isPageState, page);
                findViewById(R.id.layout_nodata).setVisibility(View.GONE);
            }
        });
        tv_selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                isPageState = URLConfig.upVideo;
                listView.setAdapter(baseListAdapter_video);
                viewOne.setVisibility(View.VISIBLE);
                viewTwo.setVisibility(View.INVISIBLE);
                openURL(isPageState, page);
                listView.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    public void topRightTxt(View view) {
        startActivity(new Intent(context, IsUpload.class));
    }

    private void setListener() {

        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Has_been_uploaded.this.finish();
                Intent intent = new Intent();
                if (isPageState.equals(URLConfig.upVideo)) {
                    intent.setClass(Has_been_uploaded.this, Upload_video.class);
                } else {
                    intent.setClass(Has_been_uploaded.this, Upload_case.class);
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
                        openURL(isPageState, page);
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
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
                        page += 1;
                        openURL(isPageState, page);
                    }
                }
            }
        });
    }

    /**
     * name：访问网络
     * author：MrSong
     * data：2016/3/30 16:17
     */
    private void openURL(final String act, final int page) {
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("act", act);
                    object.put("page", page);
                    object.put("uid", SharedPreferencesTools.getUid(context));
                    String json = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                    handler.sendMessage(handler.obtainMessage(1, json));
                } catch (Exception e) {
                    handler.sendEmptyMessage(500);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
