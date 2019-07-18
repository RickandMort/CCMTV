package com.linlic.ccmtv.yx.activity.AppointmentCourse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.YKListEntity;
import com.linlic.ccmtv.yx.utils.RoundBackgroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/9.
 */

public class YKListAdapter extends RecyclerView.Adapter<YKListAdapter.ViewHolder> implements View.OnClickListener{
    private List<YKListEntity> data = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public YKListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yk_list, parent, false);
        view.setOnClickListener(this);
        return new YKListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final YKListEntity entrty = data.get(position);
        holder.itemView.setTag(position);
//        holder.mTittle.setText(entrty.getTitle());
        holder.mTvStatusName.setText(entrty.getStatus_name());
        holder.mTvNum.setText(Html.fromHtml("<font color=#FCA962>" + entrty.getEnroll_num()  + "</font><font color=#666666>/"+ entrty.getMax_num()  + "</font>") );
        holder.mTvNoticeName.setText("报名范围："+entrty.getNotice_name());
        holder.mTvUsername.setText("发布人："+entrty.getUsername());
        holder.mTvCreatetime.setText("发布时间："+entrty.getCreatetime());
//21
        String  endText= "  未开始  ";
        switch (entrty.getStatus_name()){
            case "未开始":
                endText= "  未开始  ";
                break;
            case "进行中":
                endText= "  进行中  ";
                break;
            case "已结束":
                endText= "  已结束  ";
                break;
            case "开始报名":
                endText= "  开始报名  ";
                break;
            case "停止报名":
                endText= "  停止报名  ";
                break;
        }
        String startText = "";
        if(entrty.getTitle().length()>27){
              startText = entrty.getTitle().substring(0,27)+"...  ";
        }else{
            startText = entrty.getTitle();
        }
        SpannableString textSpanned1 = new SpannableString(startText +endText);
        //为了显示效果在每个标签文字前加两个空格,后面加三个空格(前两个和后两个填充背景,最后一个作标签分割)
//        textSpanned1
//        textSpanned1.insert(0, "  " + goodsTags.get(i).getTags_name() + "   ");
        int start = startText.length();
        int end = startText.length()+endText.length();
        //稍微设置标签文字小一点
        textSpanned1.setSpan(new RelativeSizeSpan(0.7f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textSpanned1.setSpan(new AbsoluteSizeSpan(14), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSpanned1.setSpan(new StyleSpan(Typeface.BOLD), 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置圆角背景
        int fontSizePx1 = (int)sp2px(holder.mTittle.getContext(), 12);
        Drawable d ;
        ImageSpan span;
        switch (entrty.getStatus_name()){
            case "未开始":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#ef8e10"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                  d = mContext.getResources().getDrawable(R.mipmap.spanned01);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                  span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "进行中":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#eeeeee"),Color.parseColor("#ef8e10"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                  d = mContext.getResources().getDrawable(R.mipmap.spanned02);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                  span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "已结束":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#eeeeee"),Color.parseColor("#666666"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = mContext.getResources().getDrawable(R.mipmap.spanned03);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "开始报名":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#3897f9"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = mContext.getResources().getDrawable(R.mipmap.spanned04);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "停止报名":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#ef8e10"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = mContext.getResources().getDrawable(R.mipmap.spanned05);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }


        holder.mTittle.setText(textSpanned1);

    }
    public static float sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<YKListEntity> newDatas) {
        data.clear();
        data.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void clearData(){
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<YKListEntity> newDatas) {
        if (newDatas != null) {
            int size = newDatas.size();
            data.addAll(newDatas);
            notifyItemRangeInserted(getItemCount(), size);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position, YKListEntity entrty);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag(), data.get((Integer) v.getTag()));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_tittle)
        TextView mTittle;
        @Bind(R.id.tv_status_name)
        TextView mTvStatusName;
        @Bind(R.id.tv_num)
        TextView mTvNum;
        @Bind(R.id.tv_notice_name)
        TextView mTvNoticeName;
        @Bind(R.id.tv_username)
        TextView mTvUsername;
        @Bind(R.id.tv_createtime)
        TextView mTvCreatetime;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
