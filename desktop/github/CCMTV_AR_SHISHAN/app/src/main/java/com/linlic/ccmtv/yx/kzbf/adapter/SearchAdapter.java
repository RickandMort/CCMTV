package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.SearchMultiltemEntity;
import com.linlic.ccmtv.yx.utils.CustomImageView;

import java.util.List;

/**
 * Project: tocapp-android
 * Author:  Niklaus
 * Version:  1.0
 * Date:    2017/12/16
 * Copyright notice:
 */
public class SearchAdapter extends BaseMultiItemQuickAdapter<SearchMultiltemEntity, BaseViewHolder> {

    private Context mContext;

    public SearchAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(SearchMultiltemEntity.TYPE_ITEM_ALONE, R.layout.item_search_alone);
        addItemType(SearchMultiltemEntity.TYPE_ITEM_BASE, R.layout.item_search_base);
        addItemType(SearchMultiltemEntity.TYPE_ITEM_YAODAI, R.layout.item_search_yaodai);
        addItemType(SearchMultiltemEntity.TYPE_ITEM_HOT, R.layout.item_hot_search);
        addItemType(SearchMultiltemEntity.TYPE_ITEM_HISTORY, R.layout.item_history_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchMultiltemEntity item) {
        switch (helper.getItemViewType()) {
            case SearchMultiltemEntity.TYPE_ITEM_ALONE:
                helper.setText(R.id.tv_title, item.getBean().getTitle())
                        .setText(R.id.tv_alone_posttime, item.getBean().getPosttime())
                        .setText(R.id.tv_alone_helper, item.getBean().getHelper() + " " + item.getBean().getCompany() + " " + item.getBean().getDrug());
                break;
            case SearchMultiltemEntity.TYPE_ITEM_BASE:
                helper.setText(R.id.tv_flag, item.getBean().getCid()
                        .equals("1") ? "药讯" : item.getBean().getCid()
                        .equals("2") ? "指南" : "文献")
                        .setText(R.id.tv_title, item.getBean().getTitle())
                        .setText(R.id.tv_base_posttime, item.getBean().getPosttime())
                        .setText(R.id.tv_base_helper, item.getBean().getHelper() + " " + item.getBean().getCompany() + " " + item.getBean().getDrug());
                break;
            case SearchMultiltemEntity.TYPE_ITEM_YAODAI:
                helper.setText(R.id.tv_yd_helper, item.getBean().getHelper())
                        .setText(R.id.tv_yd_company, item.getBean().getCompany() + " " + item.getBean().getDrug())
                        .addOnClickListener(R.id.ll_yd_focus)
                        .addOnClickListener(R.id.tv_yd_consult);
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getBean().getUser_img())
                        .apply(options)
                        .into((CustomImageView) helper.getView(R.id.tv_yd_img));
                if (item.getBean().getIs_foncus().equals("0")) {
                    helper.setVisible(R.id.ll_yd_focus, true)
                            .setVisible(R.id.tv_yd_consult, false);
                } else {
                    helper.setVisible(R.id.ll_yd_focus, false)
                            .setVisible(R.id.tv_yd_consult, true);
                }
                break;
            case SearchMultiltemEntity.TYPE_ITEM_HOT:
                helper.setText(R.id.hot_search_title, item.getHotTitle())
                        .addOnClickListener(R.id.ll_item_hot);
                break;
            case SearchMultiltemEntity.TYPE_ITEM_HISTORY:
                helper.setText(R.id.hot_history_title, item.getHotTitle())
                        .addOnClickListener(R.id.ll_item_history)
                        .addOnClickListener(R.id.iv_history_delete);
                break;
            default:
                break;
        }
    }

}

