package com.linlic.ccmtv.yx.kzbf.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by congge on 2018/7/18.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder<T>> implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {
    protected List<T> ts;
    protected Context context;
    private AdapterClickListener<T> listener;

    public void setClickListener(AdapterClickListener<T> listener) {
        this.listener = listener;
    }

    public BaseRecyclerAdapter(List<T> ts, Context context) {
        this.ts = ts;
        this.context = context;
    }

    public BaseRecyclerAdapter(List<T> ts) {
        this.ts = ts;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.context == null) this.context = parent.getContext();
        View itemView = CreateItemView(parent, context, viewType);
        BaseViewHolder<T> holder = CreateViewHolder(itemView, viewType);
        holder.callback = this;
        itemView.setTag(holder);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        T t = null;
        if (position <= ts.size() - 1 && position >= 0)
            t = ts.get(position);
        holder.onBind(t);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return ts == null ? 0 : ts.size();
    }

    /**
     * item对应的Viewholder
     *
     * @param itemView
     * @param viewType
     * @return
     */
    protected abstract BaseViewHolder<T> CreateViewHolder(View itemView, int viewType);

    /**
     * itemView 的布局
     *
     * @param context
     * @param viewType
     * @return
     */
    protected abstract View CreateItemView(ViewGroup parent, Context context, int viewType);

    /**
     * itemView的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        BaseViewHolder viewHolder = (BaseViewHolder) v.getTag();
        if (listener != null && viewHolder != null) {
            int p = viewHolder.getAdapterPosition();
            listener.onItemClick(viewHolder, ts.get(p));
        }
    }

    /**
     * itemView的长点击事件
     *
     * @param v
     */
    @Override
    public boolean onLongClick(View v) {
        BaseViewHolder viewHolder = (BaseViewHolder) v.getTag();
        if (listener != null && viewHolder != null) {
            int p = viewHolder.getAdapterPosition();
            listener.onItemClick(viewHolder, ts.get(p));
            return true;
        }
        return false;
    }

    @Override
    public void update(T t, BaseViewHolder<T> holder) {
        int pos = holder.getAdapterPosition();
        ts.remove(pos);
        ts.add(pos, t);
        notifyItemChanged(pos);
    }


    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
        protected T t;
        private AdapterCallback<T> callback;
        private View view;


        public BaseViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }


        public View getItemView() {
            return view;
        }

        public void updateData(T t) {
            if (this.callback != null) this.callback.update(t, this);
        }

        private void bind(T t) {
            this.t = t;
            onBind(t);
        }

        protected abstract void onBind(T t);
    }
}
