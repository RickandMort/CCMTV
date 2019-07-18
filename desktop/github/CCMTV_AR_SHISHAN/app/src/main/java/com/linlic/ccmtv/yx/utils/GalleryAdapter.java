package com.linlic.ccmtv.yx.utils;

/**
 * Created by Administrator on 2016/7/8.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.List;

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<Video_menu_expert>  mDatas;

    public GalleryAdapter(Context context, List<Video_menu_expert> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }


        private TextView department_id;
        private TextView title ;
        private TextView tv_ks ;
        private TextView tv_zc ;
        private ImageView im;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.horizontallistview_item2,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.im = (ImageView) view
                .findViewById(R.id.iv_pic);
        viewHolder.title = (TextView) view
                .findViewById(R.id.tv_name);
        viewHolder.department_id = (TextView) view
                .findViewById(R.id.department_id);
        viewHolder.tv_ks = (TextView) view
                .findViewById(R.id.tv_ks);
        viewHolder.tv_zc = (TextView) view
                .findViewById(R.id.tv_zc);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        loadImg(viewHolder.im, mDatas.get(i).getVideo_menu_expert_img());
        viewHolder.title.setText(mDatas.get(i).getVideo_menu_expert_name());
        viewHolder.department_id.setText(mDatas.get(i).getVideo_menu_expert_id());
        viewHolder.tv_ks.setText(mDatas.get(i).getVideo_menu_expert_keywords());
        viewHolder.tv_zc.setText(mDatas.get(i).getVideo_menu_expert_smalltitle());
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }
    }
    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(img.getContext());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));

    }
}