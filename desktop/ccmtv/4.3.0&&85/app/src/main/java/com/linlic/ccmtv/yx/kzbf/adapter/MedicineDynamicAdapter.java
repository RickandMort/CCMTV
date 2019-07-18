package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;
import com.linlic.ccmtv.yx.kzbf.utils.GlideRoundTransform;
import com.linlic.ccmtv.yx.kzbf.utils.MyImageSpan;
import com.linlic.ccmtv.yx.utils.CustomImageView;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/25.
 * 药讯动态adapter
 */

@SuppressWarnings("CheckResult")
public class MedicineDynamicAdapter extends BaseMultiItemQuickAdapter<DbMedicine, BaseViewHolder> {
    private Context mContext;
    private SparseArray<Drawable> drawables = new SparseArray<>();


    private ImageView targetImageView;
//    private SimpleTarget target = new SimpleTarget<Bitmap>() {
//        @Override
//        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//            if (targetImageView != null) targetImageView.setImageBitmap(bitmap);
//        }
//    };

    private BitmapImageViewTarget target ;

    public MedicineDynamicAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicine.TYPE_PIC_3, R.layout.item_medicine1);
        addItemType(DbMedicine.TYPE_PIC_0, R.layout.item_medicine2);
        addItemType(DbMedicine.TYPE_PIC_1, R.layout.item_medicine3);
        addItemType(DbMedicine.TYPE_PIC_0_PARTICIPATE, R.layout.item_medicine);//新增 调研
        addItemType(DbMedicine.TYPE_PIC_1_SMALL, R.layout.item_medicine5);//新增 调研
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicine item) {
        switch (helper.getItemViewType()) {
            case DbMedicine.TYPE_PIC_3://3张图片
                RequestOptions options = new RequestOptions();
                options.error(R.mipmap.kzbf_default)
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
                        .setText(R.id.tv_medicine_bottom1, item.getDescribe())
                        .setText(R.id.medicine_eye_num1, formatLookNum(Integer.parseInt(item.getLook_num())))
                        .setText(R.id.medicine_zan_num1, formatLookNum(Integer.parseInt(item.getLaud_num())))
                        .addOnClickListener(R.id.medicine_delete1);

                TextView title1 = helper.getView(R.id.medicine_item_title1);
                title1.setText(getSpan(item.getTitle(), item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read));

                break;
            case DbMedicine.TYPE_PIC_0://没有图片
                RequestOptions options1 = new RequestOptions();
                options1.error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options1)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon2));
                helper.setText(R.id.medicine_helper2, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime2, item.getPosttime())
                        .setText(R.id.tv_medicine_bottom2, item.getDescribe())
                        .setText(R.id.medicine_eye_num2, formatLookNum(Integer.parseInt(item.getLook_num())))
                        .setText(R.id.medicine_zan_num2, formatLookNum(Integer.parseInt(item.getLaud_num())))
                        .addOnClickListener(R.id.medicine_delete2);

                TextView title2 = helper.getView(R.id.medicine_item_title2);
                title2.setText(getSpan(item.getTitle(), item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read));
                break;
            case DbMedicine.TYPE_PIC_1://1张图片
                RequestOptions options2 = new RequestOptions();
                options2.error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options2)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon3));
                RequestOptions options3 = new RequestOptions();
                options3.error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default)
                        .transform(new GlideRoundTransform(mContext, 8));
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options3)
                        .into((ImageView) helper.getView(R.id.iv_img4));
                helper.setText(R.id.medicine_helper3, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime3, item.getPosttime())
                        .setText(R.id.tv_medicine_bottom3, item.getDescribe())
                        .setText(R.id.medicine_eye_num3, formatLookNum(Integer.parseInt(item.getLook_num())))
                        .setText(R.id.medicine_zan_num3, formatLookNum(Integer.parseInt(item.getLaud_num())))
                        .addOnClickListener(R.id.medicine_delete3);

                TextView title3 = helper.getView(R.id.medicine_item_title3);
                title3.setText(getSpan(item.getTitle(), item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read));
                break;

            case DbMedicine.TYPE_PIC_1_SMALL://1张图片(小图)
                RequestOptions options4 = new RequestOptions();
                options4.error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options4)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon5));
                targetImageView = helper.getView(R.id.iv_medicine_single5);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getImg_url1())
                        .apply(options4)
                        .into(new BitmapImageViewTarget(targetImageView){
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                super.onResourceReady(resource, transition);
                                if (targetImageView != null) targetImageView.setImageBitmap(resource);
                            }
                        });
                helper.setText(R.id.medicine_helper_participate5, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime_participate5, item.getPosttime())
                        .setText(R.id.tv_medicine_bottom5, item.getDescribe())
                        .setText(R.id.medicine_eye_num_participate5, formatLookNum(Integer.parseInt(item.getLook_num())))
                        .setText(R.id.medicine_zan_num_participate5, formatLookNum(Integer.parseInt(item.getLaud_num())))
                        .addOnClickListener(R.id.medicine_delete_participate5);

                TextView title5 = helper.getView(R.id.medicine_item_title_participate5);
                title5.setText(getSpan(item.getTitle(), item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read));
                break;
            case DbMedicine.TYPE_PIC_0_PARTICIPATE://新增调研模块
                RequestOptions options5 = new RequestOptions();
                options5.error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options5)
                        .into((CustomImageView) helper.getView(R.id.medicine_head_icon_participate));
                helper.setText(R.id.medicine_helper_participate, item.getHelper() + " | " + item.getCompany() + " " + item.getDrug())
                        .setText(R.id.tv_posttime_participate, item.getPosttime())
                        .setText(R.id.medicine_eye_num_participate, formatLookNum(Integer.parseInt(item.getLook_num())))
                        .setText(R.id.medicine_zan_num_participate, formatLookNum(Integer.parseInt(item.getLaud_num())))
                        .addOnClickListener(R.id.medicine_delete_participate);
                TextView title = helper.getView(R.id.medicine_item_title_participate);
                title.setText(getSpan(item.getTitle(), item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read));
                helper.setText(R.id.tv_participate_point, item.getSurvey_integral() <= 0 ? "" : "+" + item.getSurvey_integral() + "积分");
                if (item.getIs_show_survey_btn() == 1) {
                    helper.setVisible(R.id.btn_participate, true);
                    String des = null;
                    int resId = -1;
                    int textColor = R.color.color_white;
                    switch (item.getIs_show_user_partake()) {
                        case 0:
                            des = "未参与";
                            resId = R.mipmap.button_01;
                            textColor = R.color.color_white;
                            break;
                        case 1:
                            des = "已参与";
                            resId = R.mipmap.button_03;
                            textColor = R.color.black;
                            break;
                        case 2:
                            des = "已结束";
                            textColor = R.color.text_black;
                            resId = -1;
                            break;
                        default:
                            break;
                    }

                    helper.setText(R.id.btn_participate, des)
                            .setBackgroundRes(R.id.btn_participate, resId)
                            .setTextColor(R.id.btn_participate, mContext.getResources().getColor(textColor));
                } else
                    helper.setVisible(R.id.btn_participate, false);


//                helper.setImageResource(R.id.iv_img_unread_participate,
//                        item.getIs_show_red().equals("1") ? R.mipmap.ic_kzbf_unread : R.mipmap.ic_kzbf_read);
                break;

            default:
                break;
        }
    }

    private SpannableStringBuilder getSpan(String content, int resId) {
        Drawable drawable;
        if (drawables.get(resId) == null) {
            drawable = mContext.getResources().getDrawable(resId);
            drawables.put(resId, drawable);
        } else {
            drawable = drawables.get(resId);
        }
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("读取状态 " + content);
        drawable.setBounds(0, 0, dip2px(30), dip2px(14));
        MyImageSpan imageSpan = new MyImageSpan(drawable);
        //文本字体绝对的大小
        stringBuilder.setSpan(imageSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    public static String formatLookNum(int num) {
        if (num > 10000)
            return String.format("%.1f", num / 10000.0) + "万";
        if (num > 1000)
            return String.format("%.1f", num / 1000.0) + "千";
        return String.valueOf(num);
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public int dip2px(int dp) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}
