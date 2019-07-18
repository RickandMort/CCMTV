package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.adapter.SwipeAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.SwipeListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 *
 * @author yu
 */
public class MyCollectionActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    SwipeAdapter adapter;
    SwipeListView mycollection_list;
    private int pageIndex = 1, positions;
    private String uid;
    Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    Map<String, Object> map;
    PullToRefreshScrollView mPullRefreshScrollView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (pageIndex == 1) {
                        MyProgressBarDialogTools.hide();
                    }
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                if (pageIndex == 1) {
                                    showNoData();
                                } else {
                                    //暂无更多数据
                                    Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                hideNoData();
                                if (pageIndex != 1 && dataArray.length() < 10) {
                                    Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                data.clear();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    map = new HashMap<String, Object>();
                                    map.put("collect_item_title", object.getString("title"));
                                    map.put("collect_on_demand", object.getString("hits"));
                                    map.put("collect_item_img", object.getString("picurl"));
                                    map.put("collect_times", object.getString("posttime"));
                                    map.put("aid", object.getString("aid"));
                                    map.put("id", object.getString("id"));
                                    map.put("money", object.getString("money"));
                                    map.put("flag", object.getString("flag"));
                                    map.put("videopaymoney", object.getString("videopaymoney"));
                                    data.add(map);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            showNoData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            // 设置隐藏当前项右侧布局
                            data.remove(positions);
                            mycollection_list.hiddenRight(SwipeListView.mCurrentItemView);
                            adapter.notifyDataSetChanged();
                            if (data.size() == 0) {
                                showNoData();
                            }
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mycollection);
        context = this;
        findById();
        setText();

    }

    /**
     * name:查询XML控件
     */
    public void findById() {
        super.findId();
        super.setClick();
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCollectionActivity.this, CustomActivity.class);
                intent.putExtra("type", "my");
                startActivity(intent);
            }
        });
        mycollection_list = (SwipeListView) findViewById(R.id.mycollection_list);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ImageView imgNodata = (ImageView) findViewById(R.id.img_show_no);
        imgNodata.setImageResource(R.mipmap.nodata_follow);
        mycollection_list.setOnItemClickListener(new casesharing_listListener());
        mPullRefreshScrollView.setOnRefreshListener(this);
    }

    /**
     * name:设置控件的文本值
     */
    public void setText() {
        super.setActivity_title_name(R.string.my_myCollection);
        super.setActivity_btnnodata(R.string.btnnodata_recent);
//        super.setActivity_tvnodata(R.string.tvnodata_recent);
        super.setActivity_tvnodata("当前还没有收藏哦~");
        adapter = new SwipeAdapter(context, data, mycollection_list.getRightViewWidth());
        adapter.setOnRightItemClickListener(new SwipeAdapter.onRightItemClickListener() {

                                                @Override
                                                public void onRightItemClick(View v, final int position) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                JSONObject obj = new JSONObject();
                                                                obj.put("uid", uid);
                                                                positions = position;
                                                                obj.put("aid", data.get(position).get("aid").toString());
                                                                obj.put("act", URLConfig.delMyCollection);
                                                                String result = HttpClientUtils.sendPost(getApplicationContext(),
                                                                        URLConfig.CCMTVAPP, obj.toString());
                                                                Message message = new Message();
                                                                message.what = 2;
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
        );
        mycollection_list.setAdapter(adapter);
    }

    /**
     * name:设置listview中的值
     */
    public void setmsgdb() {
        loadData();
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            getVideoRulest(data.get(arg2).get("aid").toString());
        }
    }

    /**
     * name:导入初始值 author:Tom 2016-1-28下午3:41:32
     */
    public void loadData() {
        if (pageIndex == 1) {
            MyProgressBarDialogTools.show(context);
        }
        //判断是否登录
        if (SharedPreferencesTools.getUid(context).equals("")) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        uid = SharedPreferencesTools.getUid(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("uid", uid);
                    object.put("act", URLConfig.myCollection);
                    object.put("page", pageIndex);
                    String result = HttpClientUtils.sendPost(MyCollectionActivity.this, URLConfig.CCMTVAPP, object.toString());

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        BitmapUtils bitmapUtils = new BitmapUtils(MyCollectionActivity.this);
        // 加载网络图片
        bitmapUtils.display(img, FirstLetter.getSpells(path));

        mycollection_list.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pageIndex = 1;
        data.removeAll(data);
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        pageIndex += 1;
        loadData();
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

    @Override
    public void onResume() {
        setmsgdb();
        super.onResume();
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
