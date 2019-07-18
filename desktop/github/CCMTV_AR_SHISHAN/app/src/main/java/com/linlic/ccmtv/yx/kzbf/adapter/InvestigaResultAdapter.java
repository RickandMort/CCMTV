package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.adapter.base.BaseRecyclerAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.InvestigationTypeBean;
import com.linlic.ccmtv.yx.kzbf.widget.RichText;
import com.linlic.ccmtv.yx.kzbf.widget.VoteCommentLayout;
import com.linlic.ccmtv.yx.kzbf.widget.VoteProgressLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.linlic.ccmtv.yx.kzbf.adapter.InvestigationAdapter.DEFAULT_IMG_HEIGHT;
import static com.linlic.ccmtv.yx.kzbf.adapter.InvestigationAdapter.DEFAULT_IMG_WIDTH;


/**
 * Created by bentley on 2018/11/7.
 * 已经参与过调研的Adapter
 */

public class InvestigaResultAdapter extends BaseRecyclerAdapter<InvestigationTypeBean> {
    private RecyclerView.LayoutParams recyclerRarams;
    public static final int TYPE_STATISTICS = 1;
    public static final int TYPE_STATISTICS_1 = 2;
    public static final int TYPE_COMMNET = 3;
    public static final int TYPE_BOTTOM = 4;

    public static final int TYPE_VOTE = 5;// 投票

    private static int defaultHPadding = 15;
    private static int defaultVPadding = 8;

    @Override
    public int getItemViewType(int position) {
        return ts.get(position).getType();
    }

    public InvestigaResultAdapter(List<InvestigationTypeBean> types, Context context) {
        super(types, context);
    }

    @Override
    protected BaseViewHolder<InvestigationTypeBean> CreateViewHolder(View itemView, int viewType) {
        BaseViewHolder<InvestigationTypeBean> holder = null;
        switch (viewType) {
            case TYPE_STATISTICS:
            case TYPE_STATISTICS_1:
            case TYPE_VOTE:
                holder = new StatisticsHolder(itemView);
                break;
            case TYPE_COMMNET:
                holder = new CommentHolder(itemView);
                break;
            case TYPE_BOTTOM:
                holder = new LoadMoreHolder(itemView);
                break;
            default:
                holder = new LoadMoreHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    protected View CreateItemView(ViewGroup parent, Context context, int viewType) {
        View v = null;
        recyclerRarams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (viewType) {
            case TYPE_STATISTICS:
            case TYPE_STATISTICS_1:
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                v = layout;
                break;
            case TYPE_COMMNET:
                VoteCommentLayout commentLayout = new VoteCommentLayout(context);
                v = commentLayout;
                break;
            case TYPE_BOTTOM:
            default:
                LinearLayout defalutlayout = new LinearLayout(context);
                defalutlayout.setOrientation(LinearLayout.VERTICAL);
                v = defalutlayout;
                break;
        }
        v.setLayoutParams(recyclerRarams);
        return v;
    }

    /**
     * 投票分析的Holder
     */
    public class StatisticsHolder extends BaseViewHolder<InvestigationTypeBean> {
        private LinearLayout parent;
        private SparseArray<View> sparseArray = new SparseArray<>();
        private LinearLayout.LayoutParams params;
        private int titlekey = -1;
        private TextView titleView;
        private int beforeInitViewCount = 0;
        private int viewCount = 4;

        public StatisticsHolder(View itemView) {
            super(itemView);
            viewCount = new Random().nextInt(4);
            parent = (LinearLayout) itemView;
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(dip2px(defaultHPadding), dip2px(defaultVPadding), dip2px(defaultHPadding), dip2px(defaultVPadding));
        }

        @Override
        protected void onBind(final InvestigationTypeBean type) {
            if (sparseArray.get(titlekey) == null) {
                titleView = new TextView(context);
                titleView.setBackgroundColor(Color.parseColor("#EFEFEF"));
                titleView.setTextColor(Color.BLACK);
                titleView.setTextSize(14);
                titleView.setText(type.getQuestion());
                titleView.setPadding(dip2px(defaultHPadding), dip2px(defaultVPadding), dip2px(defaultHPadding), dip2px(defaultVPadding));
                parent.addView(titleView);
                sparseArray.put(titlekey, titleView);
                beforeInitViewCount += 1;
            } else {
                titleView = (TextView) sparseArray.get(titlekey);
            }
            JSONArray array = null;
            try {
                array = type.getObject().getJSONArray("option");
                viewCount = array.length();
                for (int i = 0; i < viewCount; i++) {
                    JSONObject object = array.getJSONObject(i);
                    String name = object.getString("name");
                    String percentum = object.getString("percentum");
                    VoteProgressLayout voteProgressLayout;
                    if (sparseArray.get(i) == null) {
                        voteProgressLayout = new VoteProgressLayout(context);
                        parent.addView(voteProgressLayout, params);
                        sparseArray.put(i, voteProgressLayout);
                    } else {
                        voteProgressLayout = (VoteProgressLayout) sparseArray.get(i);
                    }
//                    voteProgressLayout.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                    voteProgressLayout.setVisibility(View.VISIBLE);
                    voteProgressLayout.setVoteProgess((int) Float.parseFloat(percentum));
                    voteProgressLayout.setVoteProgressDes(percentum + "%");
                    voteProgressLayout.setLeftDes(name);
                    voteProgressLayout.setStatusTag("2");
                    if (type.getType() == TYPE_VOTE) {
                        final RichText choiceText = voteProgressLayout.getChoiceText();
                        if ("1".equals(type.getTag())) {//未提交
                            choiceText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    choiceText.setDrawable(R.mipmap.exams2_10, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                                    int i1 = sparseArray.size() - beforeInitViewCount;//减去标题View
                                    for (int j = 0; j < i1; j++) {
                                        VoteProgressLayout VoteProgressLayout = (com.linlic.ccmtv.yx.kzbf.widget.VoteProgressLayout) sparseArray.get(j);
                                        VoteProgressLayout.setLeftClickable(false);
                                    }
                                    onVoteSubmit(type.getQ_id(), choiceText.getText().toString());
                                }
                            });
                        } else {
                            choiceText.setOnClickListener(null);
                            String is_user_radio = object.getString("is_user_radio");
                            choiceText.setDrawable(("1".equals(is_user_radio) ? R.mipmap.exams2_10 : R.mipmap.exams2_07), dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                        }

                        int count = object.getInt("count");
                        voteProgressLayout.setRightDes(count);
                        String is_user_radio = object.getString("is_user_radio");
                        voteProgressLayout.getChoiceText().setDrawable(("1".equals(is_user_radio) ? R.mipmap.exams2_10 : R.mipmap.exams2_07),
                                dip2px(DEFAULT_IMG_HEIGHT), dip2px(DEFAULT_IMG_HEIGHT));
                    }
                }
                if ((parent.getChildCount() - beforeInitViewCount) > viewCount) {
                    for (int i = viewCount; i < parent.getChildCount(); i++) {
                        if (sparseArray.get(i) == null) {
                            parent.removeViewAt(i);
                        } else {
                            sparseArray.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onVoteSubmit(String q_id, String string) {

    }

    /**
     * 评论的Holder
     */
    public class CommentHolder extends BaseViewHolder<InvestigationTypeBean> {
        private VoteCommentLayout parent;
        private SparseArray<View> sparseArray = new SparseArray<>();
        private int titlekey = -1;
        private int numberkey = -2;

        private TextView titleView;
        private TextView numberView;

        LinearLayout.LayoutParams params;

        public CommentHolder(View itemView) {
            super(itemView);
            parent = (VoteCommentLayout) itemView;
            parent.setPadding(dip2px(0), dip2px(0), dip2px(0), dip2px(defaultVPadding));
        }

        @Override
        protected void onBind(InvestigationTypeBean type) {
            int position = getAdapterPosition();
            String question = type.getQuestion();
            String total = (String) type.getTag();
            if (!TextUtils.isEmpty(total)) {
                if (sparseArray.get(titlekey) == null) {
                    titleView = new TextView(context);
                    titleView.setBackgroundColor(Color.parseColor("#EFEFEF"));
                    titleView.setTextColor(Color.BLACK);
                    titleView.setTextSize(16);
                    titleView.setPadding(dip2px(defaultHPadding), dip2px(defaultVPadding), dip2px(defaultHPadding), dip2px(defaultVPadding));
                    parent.addView(titleView, 0);
                    sparseArray.put(titlekey, titleView);

                    numberView = new TextView(context);
                    numberView.setTextSize(14);
                    SpannableString spannableString = new SpannableString("参与人数:" + total + "人");
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
                    spannableString.setSpan(colorSpan, 5, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    numberView.setText(spannableString);
                    numberView.setPadding(dip2px(defaultHPadding), dip2px(defaultVPadding), dip2px(defaultHPadding), dip2px(defaultVPadding));
                    parent.addView(numberView, 1);
                    sparseArray.put(numberkey, numberView);

                    View view = new View(context);//中间的分割线
                    view.setBackgroundColor(Color.LTGRAY);
                    LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                    parent.addView(view, 2, viewParams);
                } else {
                    titleView = (TextView) sparseArray.get(titlekey);
                    numberView = (TextView) sparseArray.get(numberkey);
                }
                titleView.setVisibility(View.VISIBLE);
                numberView.setVisibility(View.VISIBLE);
                titleView.setText(question);
            } else {
                if (titleView != null) titleView.setVisibility(View.GONE);
                if (numberView != null) numberView.setVisibility(View.GONE);
            }
            try {
                JSONObject object = type.getObject();
                String imgUrl = object.getString("img");
                String username = object.getString("username");
                String addtime = object.getString("addtime");
                String content = object.getString("content");
                parent.setKzbfMessageInfo(imgUrl, username, content, addtime);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 查看更多的Holder
     */
    public class LoadMoreHolder extends BaseViewHolder<InvestigationTypeBean> {
        private LinearLayout parent;
        private SparseArray<View> sparseArray = new SparseArray<>();
        private int morekey = -1;
        private LinearLayout.LayoutParams params;
        private TextView moreview;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout) itemView;
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        protected void onBind(final InvestigationTypeBean type) {
            if (sparseArray.get(morekey) == null) {
                moreview = new TextView(context);
                moreview.setBackgroundColor(Color.parseColor("#EFEFEF"));
                moreview.setTextColor(Color.parseColor("#599af6"));
                moreview.setGravity(Gravity.CENTER);
                moreview.setTextSize(16);
                moreview.setText("查看更多评论");
                moreview.setPadding(dip2px(defaultHPadding), dip2px(defaultVPadding), dip2px(defaultHPadding), dip2px(defaultVPadding));
                parent.addView(moreview, params);
                sparseArray.put(morekey, moreview);

                TextView desView = new TextView(context);
                desView.setGravity(Gravity.CENTER);
                desView.setTextSize(14);
                desView.setPadding(dip2px(defaultHPadding), dip2px(15), dip2px(defaultHPadding), dip2px(50));
                desView.setText("这是实时统计结果，最终结果请在调研结束查看！");
                parent.addView(desView, params);

                moreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoadMoreMessage(type.getQ_id());
                    }
                });
            } else {
                moreview = (TextView) sparseArray.get(morekey);
            }
        }
    }


    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public int dip2px(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * sp转换px
     */
    public int sp2px(int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void onLoadMoreMessage(String q_id) {

    }
}
