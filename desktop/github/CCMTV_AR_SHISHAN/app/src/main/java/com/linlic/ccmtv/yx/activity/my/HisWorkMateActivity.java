package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yu
 *         他的同事
 */
public class HisWorkMateActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    ListView follow_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    Context context;
    int page = 1;
    private boolean isNoMore = false;
    CircleImageView hisworkmate_img;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (page == 1) {
                        MyProgressBarDialogTools.hide();
                    }

                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                if (page == 1) {
                                    showNoData();
                                }
                            } else {
                                hideNoData();
                                if (page != 1 && dataArray.length() < 10) {
                                    isNoMore = true;
                                    Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("mate_item_title", object.getString("username"));
                                    map.put("uid", object.getString("uid"));
                                    map.put("mate_icon", object.getString("icon"));
                                    map.put("mate_item_content", object.getString("name"));
                                    map.put("attflg", object.getString("attflg"));
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            isNoMore = true;
                            showNoData();
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hisworkmate);
        context = this;
        findId();
        setText();
        initData();
    }


    public void findId() {
        super.findId();
        follow_list = (ListView) findViewById(R.id.follow_list);
        hisworkmate_img = (CircleImageView) findViewById(R.id.hisworkmate_img);

    }

    public void setText() {
        super.setActivity_title_name("他的同事");
        baseListAdapter = new BaseListAdapter(follow_list, data, R.layout.hisworkmate_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.mate_item_title, ((Map) item).get("mate_item_title") + "");
                helper.setText(R.id.mate_item_content, ((Map) item).get("mate_item_content") + "");
                helper.setImageBitmap(R.id.hisworkmate_img, ((Map) item).get("mate_icon") + "");
                String str = helper.getStr(R.id.tv_addfollow);
                helper.setFollow(((Map) item).get("uid") + "", R.id.tv_addfollow);
                if (str.length() < 1) {
                    if ("1".equals(((Map) item).get("attflg").toString())) {  //1 为已关注  页面上出现的是要 取消关注
                        helper.setText(R.id.tv_addfollow, "已关注");
                    } else {
                        helper.setText(R.id.tv_addfollow, "+ 关注");
                    }
                }
            }
        };

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = follow_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = follow_list.getChildAt(follow_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == follow_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData();
                        }
                    }
                }
            }
        });
        follow_list.setAdapter(baseListAdapter);
    }

    /**
     * name:导入初始值
     */
    public void initData() {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        final String hisuid = getIntent().getStringExtra("Hisuid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", SharedPreferencesTools.getUid(context));
                    object.put("hisuid", hisuid);
                    object.put("page", page);
                    object.put("act", URLConfig.hisColleague);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
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
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page = 1;
        data.removeAll(data);
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        page += 1;
        initData();
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
}
