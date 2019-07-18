package com.linlic.ccmtv.yx.adapter;

/**
 * Created by bentley on 2019/4/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by caobotao on 15/12/20.
 */
public class MyImgAdapter extends RecyclerView.Adapter<MyImgAdapter.ViewHolder> {
    private List<String> mList;//数据源
    private LayoutInflater mInflater;//布局装载器对象
    private Context context;

    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象
    public MyImgAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_bigcase_img, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mList.size() != 0) {
            RequestOptions options = new RequestOptions().centerCrop();
            Glide.with(context)
                    .load(mList.get(position).toString())
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.ivImg);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_img)
        ImageView ivImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
