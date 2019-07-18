package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;
import com.linlic.ccmtv.yx.kzbf.utils.GlideRoundTransform;
import com.linlic.ccmtv.yx.utils.CustomImageView;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 药讯动态更多adapter
 */

public class MedicineDynamicMoreAdapter extends BaseMultiItemQuickAdapter<DbMedicine, BaseViewHolder> {
    private Context mContext;

    public MedicineDynamicMoreAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicine.TYPE_PIC_3, R.layout.item_medicine1);
        addItemType(DbMedicine.TYPE_PIC_0, R.layout.item_medicine2);
        addItemType(DbMedicine.TYPE_PIC_1, R.layout.item_medicine3);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicine item) {
        switch (helper.getItemViewType()) {
            case DbMedicine.TYPE_PIC_3://3张图片
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon1));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.iv_img1));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url2())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.iv_img2));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url3())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.iv_img3));
                helper.setText(R.id.medicine_helper1, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime1, item.getPosttime())
                        .setText(R.id.medicine_item_title1, item.getTitle())
                        .setText(R.id.tv_medicine_bottom1, item.getDescribe())
                        .setText(R.id.medicine_eye_num1, item.getLook_num())
                        .setText(R.id.medicine_zan_num1, item.getLaud_num())
                        .addOnClickListener(R.id.medicine_delete1)
                        .setVisible(R.id.medicine_delete1, false);

                if (item.getIs_show_red().equals("1")) {
                    //is_show_red   1显示 0不显示
                    helper.setVisible(R.id.iv_img_unread1, true);
                } else {
                    helper.setVisible(R.id.iv_img_unread1, false);
                }
                break;
            case DbMedicine.TYPE_PIC_0://没有图片
                RequestOptions options1 = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options1)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon2));
                helper.setText(R.id.medicine_helper2, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime2, item.getPosttime())
                        .setText(R.id.medicine_item_title2, item.getTitle())
                        .setText(R.id.tv_medicine_bottom2, item.getDescribe())
                        .setText(R.id.medicine_eye_num2, item.getLook_num())
                        .setText(R.id.medicine_zan_num2, item.getLaud_num())
                        .addOnClickListener(R.id.medicine_delete2)
                        .setVisible(R.id.medicine_delete2, false);

                if (item.getIs_show_red().equals("1")) {
                    //is_show_red   1显示 0不显示
                    helper.setVisible(R.id.iv_img_unread2, true);
                } else {
                    helper.setVisible(R.id.iv_img_unread2, false);
                }
                break;
            case DbMedicine.TYPE_PIC_1://1张图片
                RequestOptions options2 = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options2)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon3));
                RequestOptions options3 = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default)
                        .transform(new GlideRoundTransform(mContext, 6));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options3)
                        .into((ImageView) helper.getView(R.id.iv_img4));
                helper.setText(R.id.medicine_helper3, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime3, item.getPosttime())
                        .setText(R.id.medicine_item_title3, item.getTitle())
                        .setText(R.id.tv_medicine_bottom3, item.getDescribe())
                        .setText(R.id.medicine_eye_num3, item.getLook_num())
                        .setText(R.id.medicine_zan_num3, item.getLaud_num())
                        .addOnClickListener(R.id.medicine_delete3)
                        .setVisible(R.id.medicine_delete3, false);

                if (item.getIs_show_red().equals("1")) {
                    //is_show_red   1显示 0不显示
                    helper.setVisible(R.id.iv_img_unread3, true);
                } else {
                    helper.setVisible(R.id.iv_img_unread3, false);
                }
                break;
        }
    }
}
