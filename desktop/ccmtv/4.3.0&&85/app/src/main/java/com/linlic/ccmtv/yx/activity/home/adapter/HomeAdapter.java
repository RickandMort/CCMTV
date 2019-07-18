package com.linlic.ccmtv.yx.activity.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.entry.KeshiData;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;


import java.util.ArrayList;
import java.util.List;

/**
 * name：首页列表数据适配器
 * author：Larry
 * data：2017/6/29.
 */
public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<KeshiData> keshiDatas = new ArrayList<>();

    public HomeAdapter(Context context, List<KeshiData> keshiDatas) {
        this.context = context;
        this.keshiDatas = keshiDatas;
    }

    public int getCount() {
        return keshiDatas.size();
    }

    public Object getItem(int pos) {
        return keshiDatas.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.home_buttom_item
                    , null);
            viewHolder.layout_bigitem = (LinearLayout) view.findViewById(R.id.layout_bigitem);
            viewHolder.tv_item_toptitle = (TextView) view.findViewById(R.id.tv_item_toptitle);
            viewHolder.tv_item_text1 = (TextView) view.findViewById(R.id.tv_item_text1);
            viewHolder.tv_item_text2 = (TextView) view.findViewById(R.id.tv_item_text2);
            viewHolder.tv_item_text3 = (TextView) view.findViewById(R.id.tv_item_text3);
            viewHolder.tv_item_text4 = (TextView) view.findViewById(R.id.tv_item_text4);
            viewHolder.tv_item_text5 = (TextView) view.findViewById(R.id.tv_item_text5);
            viewHolder.more = (TextView) view.findViewById(R.id.more);
            viewHolder.iv_item_ispay1 = (ImageView) view.findViewById(R.id.iv_item_ispay1);
            viewHolder.iv_item_ispay2 = (ImageView) view.findViewById(R.id.iv_item_ispay2);
            viewHolder.iv_item_ispay3 = (ImageView) view.findViewById(R.id.iv_item_ispay3);
            viewHolder.iv_item_ispay4 = (ImageView) view.findViewById(R.id.iv_item_ispay4);
            viewHolder.iv_item_ispay5 = (ImageView) view.findViewById(R.id.iv_item_ispay5);
            viewHolder.iv_item_buttomimg = (ImageView) view.findViewById(R.id.iv_item_buttomimg);
            viewHolder.iv_item_buttomimg_layout = (LinearLayout) view.findViewById(R.id.iv_item_buttomimg_layout);
            viewHolder.iv_item_img1 = (ImageView) view.findViewById(R.id.iv_item_img1);
            viewHolder.iv_item_img2 = (ImageView) view.findViewById(R.id.iv_item_img2);
            viewHolder.iv_item_img3 = (ImageView) view.findViewById(R.id.iv_item_img3);
            viewHolder.iv_item_img4 = (ImageView) view.findViewById(R.id.iv_item_img4);
            viewHolder.iv_item_img5 = (ImageView) view.findViewById(R.id.iv_item_img5);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        List<ImageView> item_img = new ArrayList<ImageView>();
        List<TextView> item_text = new ArrayList<TextView>();
        List<ImageView> item_text_ispay = new ArrayList<ImageView>();
        item_img.add(viewHolder.iv_item_img1);
        item_img.add(viewHolder.iv_item_img2);
        item_img.add(viewHolder.iv_item_img3);
        item_img.add(viewHolder.iv_item_img4);
        item_img.add(viewHolder.iv_item_img5);
        item_text.add(viewHolder.tv_item_text1);
        item_text.add(viewHolder.tv_item_text2);
        item_text.add(viewHolder.tv_item_text3);
        item_text.add(viewHolder.tv_item_text4);
        item_text.add(viewHolder.tv_item_text5);
        item_text_ispay.add(viewHolder.iv_item_ispay1);
        item_text_ispay.add(viewHolder.iv_item_ispay2);
        item_text_ispay.add(viewHolder.iv_item_ispay3);
        item_text_ispay.add(viewHolder.iv_item_ispay4);
        item_text_ispay.add(viewHolder.iv_item_ispay5);
        viewHolder.tv_item_toptitle.setText(keshiDatas.get(pos).getTitle());
        viewHolder.more.setTag(keshiDatas.get(pos).getTitle());
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideo_more(v);
            }
        });
        //循环每一个元素添加到首页中
        for (int i = 0; i < keshiDatas.get(pos).getListdata().size(); i++) {
            final int anInt = i;
            //收费
            item_img.get(i).setVisibility(View.VISIBLE);
            item_text.get(i).setTag(keshiDatas.get(pos).getListdata().get(i).getAid());

            item_img.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getVideoRulest(v, Integer.parseInt(keshiDatas.get(pos).getListdata().get(anInt).getAid()));
                }
            });
            item_text.get(i).setVisibility(View.VISIBLE);
            ImageLoader.load(context, keshiDatas.get(pos).getListdata().get(i).getPicurl(), item_img.get(i));
            item_text.get(i).setText(keshiDatas.get(pos).getListdata().get(i).getTitle());


            /*
            * 角标显示
            * 1.优先判断是否是收费视频，如果是收费视频即 显示收费角标
            * 2.非收费视频，在进行判断是否为VIP 视频
            * 3. 积分/免费视频 不显示角标
            * */
            // 先判断是否是收费视频 Videopaymoney  等于0 代表非收费视频  非0 即为收费视频
            if (!keshiDatas.get(pos).getListdata().get(i).getVideopaymoney().equals("0")) {
                //收费
                item_text_ispay.get(i).setVisibility(View.VISIBLE);
                item_text_ispay.get(i).setImageResource(R.mipmap.charge);
            } else{
                item_text_ispay.get(i).setVisibility(View.GONE);
                //判断 Flag 是否等于3  3为VIP视频  非3为非会员视频
                if (keshiDatas.get(pos).getListdata().get(i).getFlag().equals("3")) {
                    //会员
                    item_text_ispay.get(i).setVisibility(View.VISIBLE);
                    item_text_ispay.get(i).setImageResource(R.mipmap.vip_img);
                }else {
                    item_text_ispay.get(i).setVisibility(View.GONE);
                }
            }

        }
        if (keshiDatas.get(pos).getGg().getImg() == null || "".equals(keshiDatas.get(pos).getGg().getImg())) {
            viewHolder.iv_item_buttomimg_layout.setVisibility(View.GONE);
        } else {
            viewHolder.iv_item_buttomimg_layout.setVisibility(View.VISIBLE);
            ImageLoader.load(context, keshiDatas.get(pos).getGg().getImg(), viewHolder.iv_item_buttomimg);
            viewHolder.iv_item_buttomimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("title", keshiDatas.get(pos).getGg().getTitle());
                    intent.putExtra("aid",  keshiDatas.get(pos).getGg().getUrl());
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    final static class ViewHolder {
        LinearLayout layout_bigitem;
        TextView tv_item_toptitle;
        TextView tv_item_text1;
        TextView tv_item_text2;
        TextView tv_item_text3;
        TextView tv_item_text4;
        TextView tv_item_text5;
        ImageView iv_item_ispay1;
        ImageView iv_item_ispay2;
        ImageView iv_item_ispay3;
        ImageView iv_item_ispay4;
        ImageView iv_item_ispay5;
        ImageView iv_item_img1;
        ImageView iv_item_img2;
        ImageView iv_item_img3;
        ImageView iv_item_img4;
        ImageView iv_item_img5;
        ImageView iv_item_buttomimg;
        LinearLayout iv_item_buttomimg_layout;
        TextView more;
    }

    public void getVideoRulest(View v, int aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(v.getContext(), VideoFive.class);
        intent.putExtra("aid", aid + "");
        v.getContext().startActivity(intent);
    }
    public void getVideo_more(View v) {

        Intent intent = new Intent(v.getContext(), CustomActivity.class);
        intent.putExtra("type","home");
        intent.putExtra("disease_class", v.getTag().toString());
        intent.putExtra("mode", "4");
        v.getContext().startActivity(intent);
    }
}
