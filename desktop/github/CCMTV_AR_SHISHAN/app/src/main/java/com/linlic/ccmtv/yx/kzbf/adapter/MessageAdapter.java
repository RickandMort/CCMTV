package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMessage;
import com.linlic.ccmtv.yx.utils.CustomImageView;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 消息adapter
 */

public class MessageAdapter extends BaseMultiItemQuickAdapter<DbMessage, BaseViewHolder> {
    private Context mContext;

    public MessageAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMessage.TYPE_NORMAL, R.layout.item_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMessage item) {
        switch (helper.getItemViewType()) {
            case DbMessage.TYPE_NORMAL:
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default).placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext).asBitmap().load(item.getUser_img()).apply(options).into((CustomImageView) helper.getView(R.id.message_user_img));
                helper.setText(R.id.message_helper, item.getHelper());
                helper.setText(R.id.message_addtime, item.getAddtime());
                helper.setText(R.id.message_title, item.getTitle());
                if (item.getU_is_look().equals("1")) {
                    helper.setVisible(R.id.tv_red_dot, false);
                } else {
                    helper.setVisible(R.id.tv_red_dot, true);
                }
                helper.addOnClickListener(R.id.message_detial);
                helper.addOnClickListener(R.id.right_delete);
                break;
        }
    }
}
