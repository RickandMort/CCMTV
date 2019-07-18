package com.linlic.ccmtv.yx.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.util.MyListView;
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

import butterknife.ButterKnife;


public class MyFocusVideoFragment extends BaseFragment {

    Context context;
    MyListView moremvideosList;
    private BaseListAdapter baseListAdapter;
    private int page = 1;
    private boolean isNoMore = false;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (page == 1) {
                        MyProgressBarDialogTools.hide();
                    }
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                if (page == 1) {
                                    //showNoData();
                                } else {
                                    //暂无更多数据
                                    isNoMore = true;
                                    Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //hideNoData();
                                if (page != 1 && dataArray.length() < 10) {
                                    Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("aid", object.getString("aid"));
                                    map.put("title", object.getString("title"));
                                    map.put("picurl", object.getString("picurl"));
                                    map.put("list", object.getString("list"));
                                    map.put("money", object.getString("money"));
                                    map.put("flag", object.getString("flag"));
                                    map.put("videopaymoney", object.getString("videopaymoney"));
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            //showNoData();
                            isNoMore = true;
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_focus_video, container, false);
        context = getContext();
        moremvideosList = view.findViewById(R.id.moremvideos_list);
        ButterKnife.bind(this, view);
        setText();
        initdata();
        return view;
    }

    public void initdata() {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid",getArguments().getString("Hisuid"));
                    object.put("page", page);
                    object.put("act", URLConfig.upVideo);
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


    /**
     * name:设置控件的文本值
     */
    public void setText() {

        baseListAdapter = new BaseListAdapter(moremvideosList, data, R.layout.recent_browse_file_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.recent_item_title, ((Map) item).get("title") + "");
                helper.setText(R.id.recent_id, ((Map) item).get("aid") + "");
                helper.setText(R.id.recent_times, ((Map) item).get("list") + "");
                helper.setImageBitmap(R.id.recent_item_img, ((Map) item).get("picurl") + "");
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.recent_top_item_img, R.mipmap.charge);
                    helper.setVisibility(R.id.recent_top_item_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.recent_top_item_img, View.GONE);
                    if (((Map) item).get("money").toString().equals("3")) {
                        //会员
                        helper.setImage(R.id.recent_top_item_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.recent_top_item_img, View.VISIBLE);
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
                    View firstVisibleItemView = moremvideosList.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = moremvideosList.getChildAt(moremvideosList.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == moremvideosList.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initdata();
                        }
                    }
                }
            }
        });
        moremvideosList.setAdapter(baseListAdapter);
        // listview点击事件
        moremvideosList.setOnItemClickListener(new casesharing_listListener());

    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.recent_id);
            String aid = textView.getText().toString();
            getVideoRulest(aid);
        }

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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
