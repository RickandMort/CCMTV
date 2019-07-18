package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbCollection;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 消息adapter
 */

public class MyCollectionAdapter extends BaseMultiItemQuickAdapter<DbCollection, BaseViewHolder> {
    private Context mContext;

    public MyCollectionAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbCollection.TYPE_LITERATURE, R.layout.item_collection);
        addItemType(DbCollection.TYPE_DYNAMICS, R.layout.item_collection);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbCollection item) {
        switch (helper.getItemViewType()) {
            case DbCollection.TYPE_LITERATURE:
                helper.setVisible(R.id.c_uesr_img,false);
                helper.setText(R.id.c_content, item.getTitle());
                helper.setText(R.id.c_addtime, item.getPosttime());
                helper.setText(R.id.c_drug, item.getDrug());
                helper.addOnClickListener(R.id.right_delete);
                helper.addOnClickListener(R.id.content);
                break;
            case DbCollection.TYPE_DYNAMICS:
                helper.setVisible(R.id.c_uesr_img,true);
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default).placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext).asBitmap().load(item.getImg_url()).apply(options).into((ImageView) helper.getView(R.id.c_uesr_img));
                helper.setText(R.id.c_content, item.getTitle());
                helper.setText(R.id.c_addtime, item.getPosttime());
                helper.setText(R.id.c_drug, item.getDrug());
                helper.addOnClickListener(R.id.right_delete);
                helper.addOnClickListener(R.id.content);
                break;
        }
    }
}
