package com.linlic.ccmtv.yx.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.CourseCatalogBean;

import java.util.List;

/**
 * Created by 蔡超玄 on 2019/6/6.
 * 描述：分组布局
 */

public class SectionAdapter extends BaseSectionQuickAdapter<CourseCatalogBean,BaseViewHolder>{

    public SectionAdapter(int layoutResId, int sectionHeadResId, List<CourseCatalogBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, CourseCatalogBean item) {

    }

    @Override
    protected void convert(BaseViewHolder helper, CourseCatalogBean item) {

    }
}