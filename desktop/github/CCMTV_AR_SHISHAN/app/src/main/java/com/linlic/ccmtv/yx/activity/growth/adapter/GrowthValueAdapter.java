package com.linlic.ccmtv.yx.activity.growth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.growth.bean.GrowthRecyclerItemBean;
import com.linlic.ccmtv.yx.activity.growth.bean.GrowthValueBean;
import com.linlic.ccmtv.yx.kzbf.adapter.base.BaseRecyclerAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.simple.SimpleBaseAdapter;
import com.linlic.ccmtv.yx.kzbf.adapter.simple.SimpleBaseHolder;
import com.linlic.ccmtv.yx.kzbf.widget.RichText;
import com.linlic.ccmtv.yx.utils.GlideCircleTransform;

import java.util.List;

import static com.linlic.ccmtv.yx.R.id.growth_banner_item_offical;
import static com.linlic.ccmtv.yx.utils.DisplayUtil.dip2px;

/**
 * Created by bentley on 2018/12/4.
 * 更好的滑动效果参考github
 * https://github.com/kanytu/android-parallax-recyclerview
 */

public class GrowthValueAdapter extends BaseRecyclerAdapter<GrowthRecyclerItemBean> {
    public static final int BANNER_TAG = -1000;
    public static final int BANNER_SHADOW_UPPER = -1001;
    public static final int BANNER_SHADOW_LOWER = -1002;
    public static final int LIST_GROWTH_STRATEGY_TITLE = -1003;
    public static final int LIST_GROWTH_VALUE_TITLE = -1004;
    public static final int LIST_DEFAULT = 1000;


    private int currentGrowthValue;

    public void setCurrentGrowthValue(int currentGrowthValue) {
        this.currentGrowthValue = currentGrowthValue;
    }

    public GrowthValueAdapter(List<GrowthRecyclerItemBean> values, Context context) {
        super(values, context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return BANNER_SHADOW_UPPER;
        if (position == 1) return BANNER_TAG;
        if (position == 2) return BANNER_SHADOW_LOWER;
        if (position < ts.size()) {
            return ts.get(position).getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    protected BaseViewHolder<GrowthRecyclerItemBean> CreateViewHolder(View itemView, int viewType) {
        if (viewType == BANNER_TAG)
            return new BannerHolder(itemView);
        else return new SimpleBaseHolder<GrowthRecyclerItemBean>(itemView) {
            @Override
            public void onBind(GrowthRecyclerItemBean growthValuebean) {
                if (growthValuebean.getObject() == null) return;
                GrowthValueBean.UserRankRuleBean rankRuleBean = (GrowthValueBean.UserRankRuleBean) growthValuebean.getObject();
                RequestOptions options = new RequestOptions()
                        .transform(new GlideCircleTransform(context))
                        .placeholder(R.mipmap.growth_value_demand_default);
                Glide.with(context).load(rankRuleBean.getRule_icon())
                        .apply(options)
                        .into((ImageView) getView(R.id.growth_list_item_icon));
                setText(R.id.growth_list_item_name, rankRuleBean.getRule_name())
                        .setText(R.id.growth_list_item_integral, "+" + rankRuleBean.getJyz() + (TextUtils.isEmpty(rankRuleBean.getUnit()) ? "" : "/" + rankRuleBean.getUnit()))
                        .setText(R.id.growth_list_item_todo, rankRuleBean.getExplain() == null ? "" : rankRuleBean.getExplain());
                getConvertView().setBackgroundResource("0".equals(rankRuleBean.getRule_type()) ? R.mipmap.growth_value_task_bg_strategy : R.mipmap.growth_value_task_bg_value);

            }
        };
    }

    @Override
    protected View CreateItemView(ViewGroup parent, Context context, int viewType) {
        if (viewType == BANNER_TAG) return new RecyclerView(context);
        if (viewType == BANNER_SHADOW_UPPER || viewType == BANNER_SHADOW_LOWER) {
            View view = new View(context);
            view.setBackgroundResource(viewType == BANNER_SHADOW_UPPER ? R.mipmap.growth_value_banner_bg_upper : R.mipmap.growth_value_banner_bg_lower);
            return view;
        }
        if (viewType == LIST_GROWTH_STRATEGY_TITLE || viewType == LIST_GROWTH_VALUE_TITLE) {
            return getTitleView(viewType);
        }
        return LayoutInflater.from(context).inflate(R.layout.growth_value_list_item, parent, false);
    }

    private View getTitleView(int viewType) {
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(context);
        View left = new View(context);
        View right = new View(context);
        RichText view = new RichText(context);
        if (viewType == LIST_GROWTH_STRATEGY_TITLE) {
            params = new LinearLayout.LayoutParams(dip2px(19), dip2px(9));
            view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            view.setTextSize(16);
            view.setTextColor(Color.parseColor("#5B5B5B"));
            view.setText("经验值攻略");
            left.setBackgroundResource(R.mipmap.growth_value_task_title_strategy_left);
            right.setBackgroundResource(R.mipmap.growth_value_task_title_strategy_right);
        } else {
            params = new LinearLayout.LayoutParams(dip2px(123), dip2px(1));
            view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//默认
            view.setTextSize(12);
            view.setTextColor(Color.parseColor("#4E4E4E"));
            view.setText("每日获取经验值最高为:50");
            left.setBackgroundResource(R.mipmap.growth_value_task_title_values);
            right.setBackgroundResource(R.mipmap.growth_value_task_title_values);
        }
        view.setPadding(dip2px(5), 0, dip2px(5), 0);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(dip2px(30), dip2px(12), dip2px(30), dip2px(8));
        layout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(left, params);
        layout.addView(view);
        layout.addView(right, params);
        return layout;
    }


    public class BannerHolder extends BaseViewHolder<GrowthRecyclerItemBean> {
        RecyclerView growth_banner;
        private SimpleBaseAdapter<GrowthValueBean.UserRankInfoBean> simpleBaseAdapter;

        public BannerHolder(View itemView) {
            super(itemView);
            growth_banner = (RecyclerView) itemView;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            growth_banner.setLayoutManager(layoutManager);
            new LinearSnapHelper() {
                @Override
                public View findSnapView(RecyclerView.LayoutManager layoutManager) {
                    int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    if (firstChild == RecyclerView.NO_POSITION) {
                        return null;
                    }
                    //翻到最后一条，解决显示不全的问题
                    if (lastChild == layoutManager.getItemCount() - 1) {
                        View viewByPosition = layoutManager.findViewByPosition(lastChild);
                        int decoratedLeft = layoutManager.getDecoratedLeft(viewByPosition);
                        int decoratedRight = layoutManager.getDecoratedRight(viewByPosition);
                        int viewWidth = decoratedRight - decoratedLeft;
                        if (decoratedLeft - viewWidth < viewWidth / 2) {//表示最后一个条目只滑动出来大于一半
                            return viewByPosition;
                        }
                    }
                    return super.findSnapView(layoutManager);
                }
            }.attachToRecyclerView(growth_banner);
            growth_banner.setNestedScrollingEnabled(false);
        }

        @Override
        protected void onBind(GrowthRecyclerItemBean growthValuebean) {
            List<GrowthValueBean.UserRankInfoBean> userRankInfo = (List<GrowthValueBean.UserRankInfoBean>) growthValuebean.getObject();
            if (simpleBaseAdapter == null) {
                simpleBaseAdapter = new SimpleBaseAdapter<GrowthValueBean.UserRankInfoBean>(userRankInfo, R.layout.growth_value_banner_item) {
                    @Override
                    public void convert(SimpleBaseHolder<GrowthValueBean.UserRankInfoBean> holder, GrowthValueBean.UserRankInfoBean userRankInfoBean) {
                        int upgradejyz = Integer.parseInt(userRankInfoBean.getUpgradejyz());
                        if (currentGrowthValue < upgradejyz) {
                            holder.getConvertView().setBackgroundResource(R.mipmap.growth_value_banner_item_bg_gray);
                            holder.getView(R.id.growth_banner_item_lock).setVisibility(View.VISIBLE);
                            holder.setTextColor(R.id.growth_banner_item_offical, Color.parseColor("#9F8380"))
                                    .setBackground(R.id.growth_banner_item_offical, R.mipmap.growth_value_task_offcial_bg_gray);
                        } else {
                            holder.getView(R.id.growth_banner_item_lock).setVisibility(View.GONE);
                            holder.setTextColor(growth_banner_item_offical, Color.parseColor("#CC0000"));
                            holder.getConvertView().setBackgroundResource(R.mipmap.growth_value_banner_item_bg);
                            holder.setBackground(R.id.growth_banner_item_offical, R.mipmap.growth_value_task_offcial_bg);
                        }
                        holder.setText(growth_banner_item_offical, userRankInfoBean.getGrade());
                        holder.setText(R.id.growth_banner_item_name, userRankInfoBean.getRank_name());
                        holder.setText(R.id.growth_banner_item_value, "经验值:" + userRankInfoBean.getUpgradejyz() + "点");
                        RequestOptions options = new RequestOptions()
                                .placeholder(R.mipmap.growth_value_task_offcial_icon_default);
                        Glide.with(context)
                                .load(currentGrowthValue < upgradejyz ? userRankInfoBean.getHat_gray() : userRankInfoBean.getHat_colour())
                                .apply(options)
                                .into((ImageView) holder.getView(R.id.growth_banner_item_icon));
                    }
//                    @Override
//                    public int getItemCount() {
//                        return Integer.MAX_VALUE;
//                    }
                };
                growth_banner.setAdapter(simpleBaseAdapter);
//                growth_banner.scrollToPosition(userRankInfo.size() * 100000);//开始时的偏移量
            } else {
                simpleBaseAdapter.notifyDataSetChanged();
            }
        }
    }
}
