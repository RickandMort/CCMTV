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
import java.util.Map;

public class GalleryAdapter2 extends
        RecyclerView.Adapter<GalleryAdapter2.ViewHolder>
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
    private List<Map<String ,Object>>  mDatas;

    public GalleryAdapter2(Context context, List<Map<String ,Object>> datats)
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
        private ImageView im;
        private ImageView iv_pic_top;
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
        View view = mInflater.inflate(R.layout.horizontallistview_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.im = (ImageView) view
                .findViewById(R.id.iv_pic);
        viewHolder.iv_pic_top = (ImageView) view
                .findViewById(R.id.iv_pic_top);
        viewHolder.title = (TextView) view
                .findViewById(R.id.tv_name);
        viewHolder.department_id = (TextView) view
                .findViewById(R.id.department_id);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        loadImg(viewHolder.im, mDatas.get(i).get("departemnt_item_img").toString());
        viewHolder.title.setText(mDatas.get(i).get("departemnt_item_title").toString());
        viewHolder.department_id.setText(mDatas.get(i).get("department_id").toString());
        //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
        if( !mDatas.get(i).get("videopaymoney").toString() .equals("0") ){
            //收费
            viewHolder.iv_pic_top.setVisibility(View.VISIBLE);
            viewHolder.iv_pic_top.setImageResource(R.mipmap.charge);
        }else {
            viewHolder.iv_pic_top.setVisibility(View.GONE);
            if(mDatas.get(i).get("money").toString().equals("3")){
                //会员
                viewHolder.iv_pic_top.setVisibility(View.VISIBLE);
                viewHolder.iv_pic_top.setImageResource(R.mipmap.vip_img);
            }
        }
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