package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;
import com.linlic.ccmtv.yx.kzbf.bean.DbMessage;
import com.linlic.ccmtv.yx.utils.CustomImageView;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 写消息adapter
 */

public class WriteMessageAdapter extends BaseMultiItemQuickAdapter<DbMedicineDetial, BaseViewHolder> {
    private Context mContext;

    public WriteMessageAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMessage.TYPE_NORMAL, R.layout.item_write_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicineDetial item) {
        switch (helper.getItemViewType()) {
            case DbMessage.TYPE_NORMAL:
                helper.setText(R.id.item_helper, item.getRecommend_title());
                break;
        }
    }
}
