package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 专题指南adapter
 */

public class SpecialGuideAdapter extends BaseMultiItemQuickAdapter<DbMedicine, BaseViewHolder> {
    private Context mContext;

    public SpecialGuideAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicine.TYPE_PIC_3, R.layout.item_literature1);
        addItemType(DbMedicine.TYPE_PIC_0, R.layout.item_guide2);
        addItemType(DbMedicine.TYPE_PIC_1, R.layout.item_literature3);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicine item) {
        switch (helper.getItemViewType()) {
            case DbMedicine.TYPE_PIC_3://3张图片
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.literature_eye_img1));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url2())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.literature_eye_img2));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url3())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.literature_eye_img3));
                helper.setText(R.id.literature_posttime1, item.getPosttime())
                        .setText(R.id.literature_title1, item.getTitle())
                        .setText(R.id.literature_describe1, item.getDescribe())
                        .setText(R.id.literature_eye_num1, item.getLook_num());
                if (item.getDescribe().equals("")) {
                    helper.setVisible(R.id.literature_describe1, false);
                }


                ((ImageView) helper.getView(R.id.iv_img_unread1)).setImageResource(item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read);
//                if (item.getIs_show_red().equals("1")) {
//                    //is_show_red   1显示 0不显示
//                    helper.setVisible(R.id.iv_img_unread1, true);
//                } else {
//                    helper.setVisible(R.id.iv_img_unread1, false);
//                }
                break;
            case DbMedicine.TYPE_PIC_0://没有图片
                helper.setText(R.id.guide_posttime2, item.getPosttime())
                        .setText(R.id.guide_title2, item.getTitle())
                        .setText(R.id.guide_describe2, item.getAuthor())
                        .setText(R.id.guide_describe22, item.getSource())
                        .setText(R.id.guide_eye_num2, item.getLook_num());
                /*if (item.getDescribe().equals("")) {
                    helper.setVisible(R.id.guide_describe2, false);
                }*/

                ((ImageView) helper.getView(R.id.iv_img_unread2)).setImageResource(item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read);

//                if (item.getIs_show_red().equals("1")) {
//                    //is_show_red   1显示 0不显示
//                    helper.setVisible(R.id.iv_img_unread2, true);
//                } else {
//                    helper.setVisible(R.id.iv_img_unread2, false);
//                }
                break;
            case DbMedicine.TYPE_PIC_1://1张图片
                RequestOptions options1 = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options1)
                        .into((ImageView) helper.getView(R.id.literature_eye_img4));
                helper.setText(R.id.literature_posttime3, item.getPosttime())
                        .setText(R.id.literature_title3, item.getTitle())
                        .setText(R.id.literature_describe3, item.getDescribe())
                        .setText(R.id.literature_eye_num3, item.getLook_num());
                if (item.getDescribe().equals("")) {
                    helper.setVisible(R.id.literature_describe3, false);
                }

                ((ImageView) helper.getView(R.id.iv_img_unread3)).setImageResource(item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read);
//
//                if (item.getIs_show_red().equals("1")) {
//                    //is_show_red   1显示 0不显示
//                    helper.setVisible(R.id.iv_img_unread3, true);
//                } else {
//                    helper.setVisible(R.id.iv_img_unread3, false);
//                }
                break;
        }
    }
}
