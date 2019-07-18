package com.linlic.ccmtv.yx.activity.conference.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.conference.ConferenceIssueVideoActivity;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceDetailBean;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.entry.ListData;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2018/5/8.
 */

public class ConferenceDetailAdapter extends BaseAdapter {

    private Context context;
    private List<ConferenceDetailBean> conferenceDetailDatas;

    public ConferenceDetailAdapter(Context context, List<ConferenceDetailBean> conferenceDatas) {
        this.context = context;
        this.conferenceDetailDatas = conferenceDatas;
    }

    @Override
    public int getCount() {
        return conferenceDetailDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return conferenceDetailDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_conference_detail_list,null);
            holder.tv_item_toptitle= (TextView) convertView.findViewById(R.id.tv_item_toptitle);
            holder.tv_item_more= (TextView) convertView.findViewById(R.id.id_tv_conference_detail_more);
            holder.tv_item_text2= (TextView) convertView.findViewById(R.id.tv_item_text2);
            holder.tv_item_text3= (TextView) convertView.findViewById(R.id.tv_item_text3);
            holder.tv_item_text4= (TextView) convertView.findViewById(R.id.tv_item_text4);
            holder.tv_item_text5= (TextView) convertView.findViewById(R.id.tv_item_text5);
            holder.iv_item_ispay2= (ImageView) convertView.findViewById(R.id.iv_item_ispay2);
            holder.iv_item_ispay3= (ImageView) convertView.findViewById(R.id.iv_item_ispay3);
            holder.iv_item_ispay4= (ImageView) convertView.findViewById(R.id.iv_item_ispay4);
            holder.iv_item_ispay5= (ImageView) convertView.findViewById(R.id.iv_item_ispay5);
            holder.iv_item_img2= (ImageView) convertView.findViewById(R.id.iv_item_img2);
            holder.iv_item_img3= (ImageView) convertView.findViewById(R.id.iv_item_img3);
            holder.iv_item_img4= (ImageView) convertView.findViewById(R.id.iv_item_img4);
            holder.iv_item_img5= (ImageView) convertView.findViewById(R.id.iv_item_img5);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<ImageView> item_img = new ArrayList<ImageView>();
        List<TextView> item_text = new ArrayList<TextView>();
        List<ImageView> item_text_ispay = new ArrayList<ImageView>();
        item_img.add(holder.iv_item_img2);
        item_img.add(holder.iv_item_img3);
        item_img.add(holder.iv_item_img4);
        item_img.add(holder.iv_item_img5);
        item_text.add(holder.tv_item_text2);
        item_text.add(holder.tv_item_text3);
        item_text.add(holder.tv_item_text4);
        item_text.add(holder.tv_item_text5);
        item_text_ispay.add(holder.iv_item_ispay2);
        item_text_ispay.add(holder.iv_item_ispay3);
        item_text_ispay.add(holder.iv_item_ispay4);
        item_text_ispay.add(holder.iv_item_ispay5);
        holder.tv_item_toptitle.setText(conferenceDetailDatas.get(position).getFtitle());
        holder.tv_item_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ConferenceIssueVideoActivity.class);//点击某个议题更多，跳转到该议题所有视频界面
                intent.putExtra("fid", conferenceDetailDatas.get(position).getFid());
                intent.putExtra("title",conferenceDetailDatas.get(position).getFtitle());
                context.startActivity(intent);
            }
        });
        //循环每一个元素添加到首页中
        final List<ListData> listDatas = conferenceDetailDatas.get(position).getVideos();
        for (int i = 0; i < 4; i++) {
            if (i < conferenceDetailDatas.get(position).getVideos().size()) {
                item_img.get(i).setVisibility(View.VISIBLE);
                item_text.get(i).setVisibility(View.VISIBLE);
                final int finalI = i;
                item_img.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String aid=conferenceDetailDatas.get(position).getVideos().get(finalI).getAid();
                        Intent intent = new Intent(context, VideoFive.class);
                        intent.putExtra("aid", aid);
                        if(SharedPreferencesTools.getUidONnull(context).equals("")){
                            context.startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                            LocalApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            context.startActivity(intent);

                        }

                        //getVideoRulest(v, Integer.parseInt(listDatas.get(finalI).getAid()));
                    }
                });
                ImageLoader.load(context, conferenceDetailDatas.get(position).getVideos().get(i).getPicurl(), item_img.get(i));
                item_text.get(i).setText(conferenceDetailDatas.get(position).getVideos().get(i).getTitle());
                /*if (!conferenceDetailDatas.get(position).getListdata().get(i).getMoney().equals("0")) {
                    //收费
                    item_text_ispay.get(i).setVisibility(View.VISIBLE);
                    item_text_ispay.get(i).setImageResource(R.mipmap.charge);
                } else {
                    item_text_ispay.get(i).setVisibility(View.GONE);
                    if (conferenceDetailDatas.get(position).getListdata().get(i).getFlag().equals("3")) {
                        //会员
                        item_text_ispay.get(i).setVisibility(View.VISIBLE);
                        item_text_ispay.get(i).setImageResource(R.mipmap.vip_img);
                    } else {
                        item_text_ispay.get(i).setVisibility(View.GONE);
                    }

                }*/
            } else {
                item_img.get(i).setVisibility(View.GONE);
                item_text.get(i).setVisibility(View.GONE);
                item_text_ispay.get(i).setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    final static class ViewHolder {
        TextView tv_item_toptitle;
        TextView tv_item_more;
        TextView tv_item_text2;
        TextView tv_item_text3;
        TextView tv_item_text4;
        TextView tv_item_text5;
        ImageView iv_item_ispay2;
        ImageView iv_item_ispay3;
        ImageView iv_item_ispay4;
        ImageView iv_item_ispay5;
        ImageView iv_item_img2;
        ImageView iv_item_img3;
        ImageView iv_item_img4;
        ImageView iv_item_img5;
    }
}