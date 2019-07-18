package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 文章详情adapter
 */

public class MedicineDetailAdapter extends BaseMultiItemQuickAdapter<DbMedicineDetial, BaseViewHolder> {
    private Context mContext;

    public MedicineDetailAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicineDetial.TYPE_TUIJIAN, R.layout.item_detail_tuijian);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicineDetial item) {
        switch (helper.getItemViewType()) {
            case DbMedicineDetial.TYPE_TUIJIAN://推荐
                helper.setText(R.id.tuijian_title, item.getRecommend_title());
                break;
        }
    }
}
