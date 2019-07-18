package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineAssistant;

import java.util.List;

/**
 * Created by yu on 2017/12/27.
 */

public class MedicineAssistantAdapter extends BaseMultiItemQuickAdapter<DbMedicineAssistant, BaseViewHolder> {
    Context mContext;

    public MedicineAssistantAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicineAssistant.TYPE_FOUSE, R.layout.item_fouse_consult);
        addItemType(DbMedicineAssistant.TYPE_CONSULT, R.layout.item_fouse);
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicineAssistant item) {
        switch (helper.getItemViewType()) {
            case DbMedicineAssistant.TYPE_FOUSE:
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.consult_fouse_head_icon));
                helper.setText(R.id.consult_fouse_helper, item.getHelper())
                        .setText(R.id.consult_fouse_company, item.getCompany() + " " + item.getDrug())
                        .setText(R.id.consult_fouse_article_title, item.getArticle_title())
                        .addOnClickListener(R.id.right)
                        .addOnClickListener(R.id.fouse_consult)
                        .addOnClickListener(R.id.rl_consult_content);
                if (item.getLook_num().equals("0")) {
                    helper.setVisible(R.id.tv_look_num, false);
                } else {
                    helper.setVisible(R.id.tv_look_num, true);
                    helper.setText(R.id.tv_look_num, item.getLook_num());
                }
                if (item.getArticle_title().equals("")) {
                    helper.setVisible(R.id.mid_view, false)
                            .setVisible(R.id.consult_fouse_article_title, false);
                }
                break;
            case DbMedicineAssistant.TYPE_CONSULT:
                RequestOptions options1 = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getUser_img())
                        .apply(options1)
                        .into((ImageView) helper.getView(R.id.fouse_user_img));
                helper.setText(R.id.fouse_helper, item.getHelper())
                        .setText(R.id.fouse_company, item.getCompany() + " " + item.getDrug())
                        .setText(R.id.fouse_article_title, item.getArticle_title())
                        .addOnClickListener(R.id.rl_fouse)
                        .addOnClickListener(R.id.rl_focus_content);
                if (item.getArticle_title().equals("")) {
                    helper.setVisible(R.id.fouse_article_title, false)
                            .setVisible(R.id.mid_view, false);
                }

                if (item.getIsShowRedDot().equals("1")) {
                    //is_show_red   1显示 0不显示
                    helper.setVisible(R.id.id_iv_red_dot, true);
                } else {
                    helper.setVisible(R.id.id_iv_red_dot, false);
                }
                break;
        }
    }
}
