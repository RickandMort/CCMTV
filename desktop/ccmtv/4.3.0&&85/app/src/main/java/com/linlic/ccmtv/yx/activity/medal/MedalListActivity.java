package com.linlic.ccmtv.yx.activity.medal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.util.Utils;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalBannerInfoBean;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalChildsDetialBean;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalDetialBean;
import com.linlic.ccmtv.yx.activity.medal.utils.MyHandler;
import com.linlic.ccmtv.yx.activity.medal.view.MedalBannerItemRatingbar;
import com.linlic.ccmtv.yx.activity.medal.view.MedalListItemView;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.simple.SimpleBaseAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.simple.SimpleBaseHolder;
import com.linlic.ccmtv.yx.kzbf.adapter.simple.SimpleViewBaseAdapter;
import com.linlic.ccmtv.yx.utils.DisplayUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MedalListActivity extends BaseActivity {
    private Context context;
    private RecyclerView recycler_banner;
    private RecyclerView mRecycler;
    private List<MedalBannerInfoBean> bannerList = new ArrayList<>();
    private List<MedalChildsDetialBean> medalsList = new ArrayList<>();
    private List<Integer> bannerBgImgs = Arrays.asList(R.mipmap.medal_banner_bg_01, R.mipmap.medal_banner_bg_02, R.mipmap.medal_banner_bg_03);
    private List<Integer> bannerIconsImgs = Arrays.asList(R.mipmap.medal_banner_item_medal_icon_01,
            R.mipmap.medal_banner_item_medal_icon_02, R.mipmap.medal_banner_item_medal_icon_03);
    private int mCurrentPostition = 1;
    private int mLastPostition = -1;
    private MedalListItemView currentItemView;
    private SimpleBaseAdapter<MedalBannerInfoBean> medalBannerInfoAdapter;
    private SimpleViewBaseAdapter<MedalChildsDetialBean> childsMedalAdapter;
    private SparseArray<MedalListItemView> showBoxViews = new SparseArray<>();

    private boolean isRefresh = false;
    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //获取勋章列表
                    bannerList.clear();
                    medalsList.clear();
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if ("1".equals(jsonObject.getString("status"))) {
                            Gson gson = new Gson();
                            JSONArray medals = jsonObject.getJSONArray("medal");
                            for (int i = 0; i < medals.length(); i++) {
                                MedalBannerInfoBean medalBannerInfoBean = gson.fromJson(medals.getJSONObject(i).toString(), MedalBannerInfoBean.class);
                                bannerList.add(medalBannerInfoBean);
                            }
                            medalBannerInfoAdapter.notifyDataSetChanged();
                            JSONArray datas = jsonObject.getJSONArray("data");
                            for (int i = 0; i < datas.length(); i++) {
                                MedalChildsDetialBean childsDetialBean = gson.fromJson(datas.getJSONObject(i).toString(), MedalChildsDetialBean.class);
                                medalsList.add(childsDetialBean);
                            }
                            if (isRefresh)
                                childsMedalAdapter.notifyDataSetChanged();
                            else
                                mRecycler.setAdapter(childsMedalAdapter);
                        } else {
                            Utils.showToast(context, jsonObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 1:
                    MyProgressBarDialogTools.hide();
                    MedalDetialBean medalDetialBean = new Gson().fromJson(String.valueOf(msg.obj), MedalDetialBean.class);
                    //创建Intent对象
                    if (medalDetialBean != null && "1".equals(medalDetialBean.getStatus())) {
                        if (currentItemView != null) {
                            int indexOfValue = showBoxViews.indexOfValue(currentItemView);
                            if (indexOfValue != -1) {
                                showBoxViews.removeAt(indexOfValue);
                                isRefresh = true;
                            }
                            currentItemView.setShowBox(false);
                        }
                        Intent intent = new Intent();
                        intent.setClass(context, MedalDetialActivity.class);
                        intent.putExtra(MedalDetialActivity.DETIAL_KEY, medalDetialBean);
                        startActivity(intent);
                    } else {
                        Utils.showToast(context, medalDetialBean.getErrorMessage());
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
    private LinearLayout home_title_paent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_medal_list);
        //沉浸式状态栏
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

        recycler_banner = (RecyclerView) findViewById(R.id.medal_recycler_banner);
        mRecycler = (RecyclerView) findViewById(R.id.medal_recycler_list);
        home_title_paent = (LinearLayout) findViewById(R.id.ll_personal_home_titlebar);
        setTitlebarPadding();
        initData();
        getMedalListData(mCurrentPostition);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0, nsize = showBoxViews.size(); i < nsize; i++) {
            MedalListItemView itemView = showBoxViews.valueAt(i);
            itemView.setShowBox(true);
        }
        if (isRefresh) {
            isRefresh = false;
            if(mLastPostition!=-1){
                getMedalListData(mLastPostition+1);
            }else {
                getMedalListData(mCurrentPostition);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0, nsize = showBoxViews.size(); i < nsize; i++) {
            MedalListItemView itemView = showBoxViews.valueAt(i);
            itemView.setShowBox(false);
        }
    }

    /**
     * 为titlebar设置一个statusbar高度的padding
     */
    private void setTitlebarPadding() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        home_title_paent.setPadding(0, height, 0, 0);
    }

    /**
     * 设置数据
     */
    public void initData() {
        /**
         * 列表初始化
         */
        mRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        childsMedalAdapter = new SimpleViewBaseAdapter<MedalChildsDetialBean>(medalsList) {
            @Override
            public void convert(SimpleBaseHolder<MedalChildsDetialBean> holder, final MedalChildsDetialBean detialBean) {
                final MedalListItemView itemView = (MedalListItemView) holder.getConvertView();
                itemView.setMedalChildBean(detialBean);
                if (detialBean.getMymedal() != null) {
                    //勋章是否点亮    achieve ： 勋章是否为亮 1 为亮 0 为暗
                    Glide.with(context).asBitmap().load("1".equals(detialBean.getMygrade().getAchieve()) ? detialBean.getMymedal().getIcon() :
                            detialBean.getMymedal().getD_icon()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            itemView.setMedalIcon(resource);
                        }
                    });
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(fastClick()){
                            //查看勋章详情
                            currentItemView = itemView;
                            getMedalDetial(detialBean.getMygrade().getId());
                        }
                    }
                });
                if (itemView.isShowBox()) showBoxViews.put(holder.getAdapterPosition(), itemView);
            }

            @Override
            protected View CreateItemView(ViewGroup parent, Context context, int viewType) {
                MedalListItemView listItemView = new MedalListItemView(context);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(DisplayUtil.dip2px(5), 0, DisplayUtil.dip2px(5), 0);
                listItemView.setLayoutParams(layoutParams);
                return listItemView;
            }
        };
        mRecycler.setAdapter(childsMedalAdapter);

        /**
         * 轮播图初始化
         */
        recycler_banner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PagerSnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                mCurrentPostition = position;
                return position;
            }
        };
        recycler_banner.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildLayoutPosition(view);
                if (position == 0) {
                    outRect.left = DisplayUtil.dip2px(MedalListActivity.this, 30);
                } else if (position == bannerList.size() - 1) {
                    outRect.right = DisplayUtil.dip2px(MedalListActivity.this, 30);
                }
            }
        });
        snapHelper.attachToRecyclerView(recycler_banner);
        medalBannerInfoAdapter = new SimpleBaseAdapter<MedalBannerInfoBean>(bannerList, R.layout.medal_banner_item) {
            @Override
            public void convert(SimpleBaseHolder<MedalBannerInfoBean> holder, MedalBannerInfoBean medalBannerInfoBean) {
                MedalBannerItemRatingbar ratingbar = holder.getView(R.id.medal_banner_rating);
                String[] split = medalBannerInfoBean.getGrade().split(",");
                holder.setText(R.id.medal_banner_item_name, medalBannerInfoBean.getName());
                holder.getConvertView().setBackgroundResource(bannerBgImgs.get(Integer.parseInt(medalBannerInfoBean.getId()) - 1));
                holder.setImageResource(R.id.medal_banner_item_medal_icon, bannerIconsImgs.get(Integer.parseInt(medalBannerInfoBean.getId()) - 1));
                holder.setText(R.id.medal_banner_item_count, "已获得:" + medalBannerInfoBean.getCont() + "个");
                ratingbar.setRatingCount(medalBannerInfoBean.getAllnum());
                ratingbar.setRatingRatio(Integer.parseInt(medalBannerInfoBean.getCont()));
            }
        };
        recycler_banner.setAdapter(medalBannerInfoAdapter);
        recycler_banner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    if (mLastPostition != mCurrentPostition) {
                        mLastPostition = mCurrentPostition;
                        getMedalListData(mCurrentPostition + 1);
                    }
                }
            }
        });
    }

    /***
     *
     * @param classify
     */
    private void getMedalListData(final int classify) {
        final String uid = SharedPreferencesTools.getUid(context);
        if (TextUtils.isEmpty(uid)) return;
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getMyMedal);
                    obj.put("uid", uid);
                    obj.put("classify", String.valueOf(classify));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 0;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /***
     * 查看勋章详情
     * @param id  该勋章完成记录id
     */
    private void getMedalDetial(final String id) {
        final String uid = SharedPreferencesTools.getUid(context);
        if (TextUtils.isEmpty(uid)) return;
        MyProgressBarDialogTools.show(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.seeMedal);
                    obj.put("uid", uid);
                    obj.put("id", String.valueOf(id));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
