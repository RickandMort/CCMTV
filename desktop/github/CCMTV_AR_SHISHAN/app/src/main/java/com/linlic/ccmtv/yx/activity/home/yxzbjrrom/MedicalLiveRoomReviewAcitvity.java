package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Live;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.MyGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 医学直播间---更多精彩回顾
 * Created by larry.li on 2017/3/22.
 */
public class MedicalLiveRoomReviewAcitvity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    private MyGridView medical_live_room_more_gridlist;
    // BaseListAdapter baseListAdapter2;
    private int currPage = 1;
    private String type;
    private String style;
    Context context;
    PullToRefreshScrollView mPullRefreshScrollView;
    private List<Live> list, lists;
    private GridViewAdapter gridViewAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (currPage == 1) {
                        MyProgressBarDialogTools.hide();
                    }
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            list = new Gson().fromJson(result.getJSONArray("data").toString(),
                                    new TypeToken<List<Live>>() {
                                    }.getType());
                            if (list.size() == 0) {
                                if (currPage == 1) {
                                    showNoData();
                                } else {
                                    //暂无更多数据
                                    Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                hideNoData();
                                if (currPage != 1 && list.size() < 6) {
                                    Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                lists.addAll(list);
                                gridViewAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            showNoData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_live_room_review_more);
        context = this;
        Intent intent = getIntent();
        style = intent.getExtras().getString("style");
        type = intent.getExtras().getString("type");
        findId();
        setText();
        setmsgdb();
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        // MyProgressBarDialogTools.show(MedicalLiveRoomTrailerAcitvity.this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.directSeedingMore);
                    obj.put("type", type);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("style", style);
                    obj.put("page", currPage);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }

            }
        };
        new Thread(runnable).start();
    }


    @Override
    public void findId() {
        super.findId();
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        medical_live_room_more_gridlist = (MyGridView) findViewById(R.id.medical_live_room_more_gridlist);
        mPullRefreshScrollView.setOnRefreshListener(this);
        medical_live_room_more_gridlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MedicalLiveRoomReviewAcitvity.this, Medical_live_RoomWebActivity.class);
                intent.putExtra("title", lists.get(position).getLivename());
                intent.putExtra("aid", lists.get(position).getLiveurl());
                startActivity(intent);
            }
        });
    }

    public void setText() {
        this.setActivity_title_name("精彩回顾");
        list = new ArrayList<>();
        lists = new ArrayList<>();
        gridViewAdapter = new GridViewAdapter(MedicalLiveRoomReviewAcitvity.this, lists);
        //medical_live_room_more_gridlist.setAdapter(baseListAdapter2);
        medical_live_room_more_gridlist.setAdapter(gridViewAdapter);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        currPage = 1;
        lists.clear();
        setmsgdb();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        currPage += 1;
        setmsgdb();
    }

    class GridViewAdapter extends BaseAdapter {

        Context context;
        List<Live> list;

        public GridViewAdapter(Context context, List<Live> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int pos) {
            return this.list.get(pos);
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(final int pos, View view, ViewGroup group) {
            final ViewHolder viewHolder;
            final Live mContent = list.get(pos);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(context).inflate(R.layout.medical_live_room_item2, null);
                viewHolder.tv_live_history_1 = (TextView) view.findViewById(R.id.tv_live_history_1);
                viewHolder.live_history_1 = (ImageView) view.findViewById(R.id.live_history_1);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_live_history_1.setText(mContent.getLivename());
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default);
            Glide.with(context)
                    .load(mContent.getImgurl())
                    .apply(options)
                    .into(viewHolder.live_history_1);
            return view;
        }

        class ViewHolder {
            TextView tv_live_history_1;
            ImageView live_history_1;
        }
    }

    @Override
    protected void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
