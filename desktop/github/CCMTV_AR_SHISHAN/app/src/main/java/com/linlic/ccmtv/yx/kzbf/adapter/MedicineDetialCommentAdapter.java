package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineComment;
import com.linlic.ccmtv.yx.kzbf.bean.MoreCommentBean;

import java.util.List;

/**
 * Created by Niklaus on 2017/12/28.
 */

public class MedicineDetialCommentAdapter extends BaseMultiItemQuickAdapter<DbMedicineComment, BaseViewHolder> {
    Context mContext;

    public MedicineDetialCommentAdapter(Context context, List data) {
        super(data);
        mContext = context;
        addItemType(DbMedicineComment.TYPE_DETAIL_COMMENT, R.layout.item_medicine_comment);
        addItemType(DbMedicineComment.TYPE_MORE_COMMENT, R.layout.item_medicine_comment);//评论详情布局
        addItemType(DbMedicineComment.TYPE_TWO_COMMENT, R.layout.item_medicine_comment_two);//回复评论布局
        addItemType(DbMedicineComment.TYPE_EXPAND_ALL, R.layout.item_medicine_comment_expand_all);//展开全部布局
        addItemType(DbMedicineComment.TYPE_UNEXPAND_ALL, R.layout.item_medicine_comment_expand_all);//收起全部布局
        addItemType(DbMedicineComment.TYPE_UNDER_LINE, R.layout.item_medicine_comment_under_linel);//下划线
    }

    @Override
    protected void convert(BaseViewHolder helper, DbMedicineComment item) {
        switch (helper.getItemViewType()) {
            case DbMedicineComment.TYPE_DETAIL_COMMENT:
                RequestOptions options = new RequestOptions().error(R.mipmap.kzbf_default)
                        .placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(item.getComment_user_img())
                        .apply(options)
                        .into((ImageView) helper.getView(R.id.medicine_comment_img));
                helper.setText(R.id.medicine_comment_name, item.getComment_username())
                        .setText(R.id.medicine_comment_times, item.getComment_addtime())
                        .setText(R.id.medicine_comment_content, item.getComment_content())
                        .setText(R.id.medicine_comment_zan_num, item.getComment_laud_num())
                        .setVisible(R.id.medicine_comment_reply, false);
                helper.addOnClickListener(R.id.medicine_comment_zan_img);
                if (item.getComment_is_laud().equals("0")) {
                    helper.setImageResource(R.id.medicine_comment_zan_img, R.mipmap.medicine_zan);

                } else {
                    helper.setImageResource(R.id.medicine_comment_zan_img, R.mipmap.medicine_zan2);
//                    helper.getView(R.id.medicine_comment_zan_img).setClickable(false);
                }
                break;
            case DbMedicineComment.TYPE_MORE_COMMENT:
                MoreCommentBean.DataBean bean = item.getCommentItemBean();
                RequestOptions options1 = new RequestOptions().error(R.mipmap.kzbf_default).placeholder(R.mipmap.kzbf_default);
                Glide.with(mContext)
                        .asBitmap()
                        .load(bean.getUser_img())
                        .apply(options1)
                        .into((ImageView) helper.getView(R.id.medicine_comment_img));
                helper.setText(R.id.medicine_comment_name, bean.getUsername())
                        .setText(R.id.medicine_comment_times, bean.getAddtime())
                        .setText(R.id.medicine_comment_content, bean.getContent())
                        .setText(R.id.medicine_comment_zan_num, bean.getLaud_num())
                        .addOnClickListener(R.id.medicine_comment_zan_img)
                        .addOnClickListener(R.id.medicine_comment_reply)
                        .setVisible(R.id.medicine_comment_reply, true)
                ;
                if (bean.getIs_laud().equals("0")) {
                    helper.setImageResource(R.id.medicine_comment_zan_img, R.mipmap.medicine_zan);
                } else {
                    helper.setImageResource(R.id.medicine_comment_zan_img, R.mipmap.medicine_zan2);
                }
                break;

            case DbMedicineComment.TYPE_TWO_COMMENT:
                MoreCommentBean.DataBean.TwoCommentBean chileBean = item.getCommentItemChileBean();
                helper.setText(R.id.tv_twocomment_username, chileBean.getUsername() + "：")
                        .setText(R.id.tv_twocomment_content, chileBean.getContent());
                break;

            case DbMedicineComment.TYPE_UNEXPAND_ALL:
                helper.setText(R.id.expand_info, "收起更多评论");
                break;

        }
    }
}
