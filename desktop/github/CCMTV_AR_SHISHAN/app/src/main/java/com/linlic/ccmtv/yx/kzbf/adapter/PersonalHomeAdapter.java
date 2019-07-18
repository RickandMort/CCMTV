package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMessage;
import com.linlic.ccmtv.yx.kzbf.bean.DbPersonal;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 消息adapter
 */

public class PersonalHomeAdapter extends BaseMultiItemQuickAdapter<DbPersonal, BaseViewHolder> {
    private Context mContext;

    public PersonalHomeAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbPersonal.TYPE_PERSONAL, R.layout.item_medicine_personal);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbPersonal item) {
        switch (helper.getItemViewType()) {
            case DbMessage.TYPE_NORMAL:
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.medicine_personal_icon));
                String laudNum = item.getLaud_num();
                helper.setText(R.id.personal_title, item.getTitle())
                        .setText(R.id.personal_addtime, item.getPosttime())
                        .setText(R.id.personal_zan_num, laudNum);
//                        .addOnClickListener(R.id.personal_zan_img);
//                if (item.getIs_laud().equals("0")) {
//                    helper.setImageResource(R.id.personal_zan_img, R.mipmap.medicine_zan);
//                } else {
//                    helper.setImageResource(R.id.personal_zan_img, R.mipmap.medicine_zan2);
//                }
                break;
        }
    }
}
