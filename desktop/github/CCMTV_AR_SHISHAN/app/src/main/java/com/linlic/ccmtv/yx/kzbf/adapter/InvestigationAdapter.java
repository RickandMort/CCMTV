package com.linlic.ccmtv.yx.kzbf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.util.Utils;
import com.linlic.ccmtv.yx.kzbf.adapter.base.BaseRecyclerAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.InvestigationTypeBean;
import com.linlic.ccmtv.yx.kzbf.widget.RichText;
import com.linlic.ccmtv.yx.kzbf.widget.VoteProgressLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by bentley on 2018/11/6.
 * 调研的Adapter
 */

public class InvestigationAdapter extends BaseRecyclerAdapter<InvestigationTypeBean> {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_MULITI = 2;
    public static final int TYPE_QUESTION = 3;
    public static final int TYPE_SUBMIT = 4;
    public static final int TYPE_PROGRESS = 5;

    private static final int DEFAULT_H_WIDTH = 15;
    private static final int DEFAULT_V_HEIGHT = 8;
    public static final int DEFAULT_IMG_WIDTH = 20;
    public static final int DEFAULT_IMG_HEIGHT = 20;

    private SparseArray<HashMap<String, String>> answers = new SparseArray<>();
    private SparseArray<String> mustQs = new SparseArray<>();//必答题目

    public InvestigationAdapter(List<InvestigationTypeBean> types, Context context) {
        super(types, context);
    }


    @Override
    public int getItemViewType(int position) {
        return ts.get(position).getType();
    }

    @Override
    protected BaseViewHolder<InvestigationTypeBean> CreateViewHolder(View itemView, int viewType) {
        BaseViewHolder<InvestigationTypeBean> holder;
        switch (viewType) {
            case TYPE_SINGLE:
                holder = new SingleHolder(itemView);
                break;
            case TYPE_MULITI:
                holder = new MultiHolder(itemView);
                break;
            case TYPE_QUESTION:
                holder = new QuestionHolder(itemView);
                break;
            case TYPE_PROGRESS:
                holder = new ProgessHolder(itemView);
                break;
            case TYPE_SUBMIT:
                holder = new SubmitHolder(itemView);
                break;
            default:
                holder = new TestBaseHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    protected View CreateItemView(ViewGroup parent, Context context, int viewType) {
        LinearLayout layout = new LinearLayout(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.WHITE);
        return layout;
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

    public class TestBaseHolder extends BaseViewHolder<InvestigationTypeBean> {
        protected SparseArray<View> sparseArray = new SparseArray<>();
        protected LinearLayout.LayoutParams layoutParams;
        protected TextView title;
        protected int beforeInitViewCount = 0;//开始初始化实际View之前SparseArray中view 的数量
        protected LinearLayout parent;
        protected int position;

        public TestBaseHolder(View itemView) {
            super(itemView);
            parent = (LinearLayout) itemView;
        }

        @Override
        protected void onBind(final InvestigationTypeBean type) {
            int titleKey = 1000;
            int titleKey_line = 2000;
            position = getAdapterPosition();
            if (sparseArray.get(titleKey) == null) {
                title = new TextView(context);
                title.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                ((LinearLayout) getItemView()).addView(title);
                sparseArray.put(titleKey, title);

                View view = new View(context);//中间的分割线
                view.setBackgroundColor(Color.LTGRAY);
                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                parent.addView(view, viewParams);
                sparseArray.put(titleKey_line, view);
                beforeInitViewCount = 2;
            } else {
                title = (TextView) sparseArray.get(titleKey);
            }

            title.setTextSize(16);
            title.setTextColor(Color.BLACK);
            boolean isVote = type.getType() == TYPE_PROGRESS && position - 1 >= 0 && type.getType() != ts.get(position - 1).getType();
            title.setText(Html.fromHtml((isVote ? position : position + 1) + "." + type.getQuestion()));
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
        }
    }

    /**
     * 单选Holder
     */
    public class SingleHolder extends TestBaseHolder {
        private int viewCount = 2;//View 应该存在的数量
        HashMap<String, String> hashMap;

        public SingleHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(InvestigationTypeBean type) {
            super.onBind(type);
            position = getAdapterPosition();
            hashMap = answers.get(position);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("q_id", String.valueOf(type.getQ_id()));
                hashMap.put("type", String.valueOf(type.getType()));
            }
            initSingleitem(layoutParams, type);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichText text = (RichText) v;
                String value = text.getText().toString();
                int i1 = sparseArray.size() - beforeInitViewCount;//减去标题View
                for (int j = 0; j < i1; j++) {
                    RichText richText1 = (RichText) sparseArray.get(j);
                    richText1.setDrawable(R.mipmap.exams2_07, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                    richText1.setClickable(true);
                }
                text.setDrawable(R.mipmap.exams2_10, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                hashMap.put("answer", value);
                if (TextUtils.isEmpty(value)) {
                    answers.remove(position);
                } else {
                    answers.put(position, hashMap);
                    text.setClickable(false);
                }
            }
        };

        /**
         * 单选
         */
        private void initSingleitem(LinearLayout.LayoutParams params, final InvestigationTypeBean type) {
            try {
                JSONArray option = type.getObject().getJSONArray("option");
                String is_must = type.getObject().getString("is_must");
                if ("1".equals(is_must))
                    mustQs.put(position, type.getQ_id());
                viewCount = option.length();
                for (int i = 0; i < viewCount; i++) {
                    JSONObject object = option.getJSONObject(i);
                    String name = object.getString("name");
                    RichText richText;
                    if (sparseArray.get(i) == null) {
                        richText = getTextView(name + "");
                        richText.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                        ((LinearLayout) getItemView()).addView(richText, params);
                        sparseArray.put(i, richText);
                    } else {
                        richText = (RichText) sparseArray.get(i);
                    }

                    richText.setText(name);
                    richText.setVisibility(View.VISIBLE);
                    boolean isAnswer = hashMap.get("answer") == null ? false : name.equals(hashMap.get("answer"));
                    if (!isAnswer) {
                        richText.setDrawable(R.mipmap.exams2_07, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                        richText.setOnClickListener(clickListener);
                    } else {
                        richText.setText(hashMap.get("answer") + "");
                        richText.setDrawable(R.mipmap.exams2_10, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                        richText.setOnClickListener(null);
                    }
                }

                //注意这里加个过滤  将多余的item从parent remove掉
                //如果size == childCount 不做
                //否则判断多少 然后去add  或者remove
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

    /**
     * 多选holder
     */
    public class MultiHolder extends TestBaseHolder {
        private int viewCount = 5;
        HashMap<String, String> hashMap;
        //        private String KEY_SPLITE = "※™®©";
        private String KEY_SPLITE = "$$$";


        public MultiHolder(View itemView) {
            super(itemView);
            viewCount = new Random().nextInt(6);
        }

        @Override
        protected void onBind(InvestigationTypeBean type) {
            super.onBind(type);
            position = getAdapterPosition();
            hashMap = answers.get(position);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("q_id", String.valueOf(type.getQ_id()));
                hashMap.put("type", String.valueOf(type.getType()));
            }
            initNultiItem(layoutParams, type);
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichText text = (RichText) v;
                String answer = text.getText().toString();
                String mAnswers = hashMap.get("answer");
                boolean isSelect = TextUtils.isEmpty(mAnswers) ? false : mAnswers.contains(answer);
                if (isSelect) {
                    text.setDrawable(R.mipmap.exams2_07, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                    boolean startsWith = mAnswers.startsWith(answer);
                    String replace;
                    if (startsWith && !mAnswers.contains(KEY_SPLITE)) {
                        replace = answer;
                    } else if (!startsWith) {
                        replace = KEY_SPLITE + answer;
                    } else {
                        replace = answer + KEY_SPLITE;
                    }
                    mAnswers = mAnswers.replace(replace, "");
                } else {
                    if (TextUtils.isEmpty(mAnswers)) {
                        mAnswers = answer;
                    } else {
                        mAnswers += (KEY_SPLITE + answer);
                    }
                    text.setDrawable(R.mipmap.exams2_10, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                }
                hashMap.put("answer", mAnswers);
                if (TextUtils.isEmpty(mAnswers)) {
                    answers.remove(position);
                } else {
                    answers.put(position, hashMap);
                }
            }
        };

        /**
         * 多选
         */
        private void initNultiItem(LinearLayout.LayoutParams params, final InvestigationTypeBean type) {
            JSONArray option = null;
            try {
                option = type.getObject().getJSONArray("option");
                String is_must = type.getObject().getString("is_must");
                if ("1".equals(is_must))
                    mustQs.put(position, type.getQ_id());
                viewCount = option.length();
                for (int i = 0; i < viewCount; i++) {
                    JSONObject object = option.getJSONObject(i);
                    String name = object.getString("name");
                    RichText richText;
                    if (sparseArray.get(i) == null) {
                        richText = getTextView(name + "");
                        richText.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                        ((LinearLayout) getItemView()).addView(richText, params);
                        richText.setOnClickListener(clickListener);
                        sparseArray.put(i, richText);
                    } else {
                        richText = (RichText) sparseArray.get(i);
                    }

                    richText.setText(name + "");
                    richText.setVisibility(View.VISIBLE);
                    boolean isAnswer = hashMap.get("answer") == null ? false : hashMap.get("answer").contains(name);
                    if (!isAnswer) {
                        richText.setDrawable(R.mipmap.exams2_07, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                    } else {
                        richText.setDrawable(R.mipmap.exams2_10, dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                    }
                }
                //注意这里加个过滤  将多余的item从parent remove掉
                //如果size == childCount 不做
            /*否则判断多少 然后去add  或者remove
             *****
            经过测试  复用时会选择相同数量的item的holder  但是以防万一  加上一成过滤*/
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

    /**
     * 问答holder
     */
    public class QuestionHolder extends TestBaseHolder implements TextWatcher {
        private int EditTextkey = -1;
        private EditText editText;
        private SparseArray<View> sparseArray = new SparseArray<>();
        HashMap<String, String> hashMap;

        public QuestionHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final InvestigationTypeBean type) {
            super.onBind(type);
            position = getAdapterPosition();
            hashMap = answers.get(position);
            if (hashMap == null) {
                hashMap = new HashMap<>();
                hashMap.put("q_id", String.valueOf(type.getQ_id()));
                hashMap.put("type", String.valueOf(type.getType()));
            }
            try {
                String is_must = null;
                is_must = type.getObject().getString("is_must");
                if ("1".equals(is_must))
                    mustQs.put(position, type.getQ_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (sparseArray.get(EditTextkey) == null) {
                editText = new EditText(context);
                editText.setFocusable(true);
                parent.setFocusableInTouchMode(true);
                editText.setTextSize(14);
                editText.setGravity(Gravity.TOP);
                editText.setBackgroundDrawable(null);
                editText.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(120));
                parent.addView(editText, params);
                int inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
                editText.setInputType(inputType);
                editText.addTextChangedListener(this);
                sparseArray.put(EditTextkey, editText);
            } else {
                editText = (EditText) sparseArray.get(EditTextkey);
            }
            editText.setHint("在此填写遇到的问题");
            if (!TextUtils.isEmpty(hashMap.get("answer")))
                editText.setText(hashMap.get("answer"));
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String trim = String.valueOf(s).trim();
            hashMap.put("answer", trim);
            if (trim.length() > 0) {
                answers.put(position, hashMap);
            } else {
                answers.remove(position);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    public class SubmitHolder extends BaseViewHolder<InvestigationTypeBean> {
        private int buttonkey = -1;
        private SparseArray<View> sparseArray = new SparseArray<>();

        public SubmitHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(final InvestigationTypeBean type) {
            Button compatButton;
            if (sparseArray.get(buttonkey) == null) {
                LinearLayout layout = (LinearLayout) getItemView();
                compatButton = new Button(context);
                compatButton.setText("提交");
                compatButton.setTextSize(18);
                compatButton.setTextColor(context.getResources().getColor(R.color.white));
                compatButton.setPadding(dip2px(50), 0, dip2px(50), 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(0), dip2px(50), dip2px(0), dip2px(50));
                layout.setGravity(Gravity.CENTER);
                layout.addView(compatButton, params);
                sparseArray.put(buttonkey, compatButton);
                compatButton.setBackgroundResource(R.mipmap.learning_task04);
                compatButton.setOnClickListener(listener);
            } else {
                compatButton = (Button) sparseArray.get(buttonkey);
            }
        }


    }

    private RichText getTextView(String message) {
        RichText richText = new RichText(context);
        richText.setCompoundDrawablePadding(dip2px(8));
        richText.setText(message);
        richText.setGravity(Gravity.CENTER_VERTICAL);
        richText.setTextSize(14);
        return richText;
    }

    /**
     * 提交按钮的点击事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (issubmited) {
                Utils.showToast(context, "您已经提交过调研了！");
                return;
            }
            int size = answers.size();
            //先检验必答题是否都已经答过了
            int mustSize = mustQs.size();
            for (int i = 0; i < mustSize; i++) {
                int key = mustQs.keyAt(i);
                HashMap<String, String> map = answers.get(key);
                if (map == null) {
                    Utils.showToast(context, "您还有必答题第" + (key + 1) + "题没有填！");
                    return;
                }
            }
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                HashMap<String, String> hashMap = answers.valueAt(i);
                try {
                    array.put(i, new JSONObject(hashMap));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            oninvestigaSubmit(array.toString());
        }
    };

    private boolean issubmited = false;

    /**
     * 设置是否是已经提交过的状态
     */
    public void setIsSubmited(boolean issubmited) {
        this.issubmited = issubmited;
    }

    /**
     * 投票的布局的holder
     */
    public class ProgessHolder extends TestBaseHolder {
        private int viewCount = 4;
        private String qid;

        public ProgessHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(InvestigationTypeBean type) {
            super.onBind(type);
            initProgressItem(layoutParams, type);
        }

        /**
         * 投票的布局的holder
         *
         * @param layoutParams
         * @param type
         */
        private void initProgressItem(LinearLayout.LayoutParams layoutParams, InvestigationTypeBean type) {
            try {
                qid = type.getQ_id();
                JSONArray array = type.getObject().getJSONArray("option");
                viewCount = array.length();
                for (int i = 0; i < viewCount; i++) {
                    JSONObject object = array.getJSONObject(i);
                    int count = object.getInt("count");
                    String name = object.getString("name");
                    String percentum = object.getString("percentum");

                    VoteProgressLayout voteProgressLayout;
                    if (sparseArray.get(i) == null) {
                        voteProgressLayout = new VoteProgressLayout(context);
                        parent.addView(voteProgressLayout, layoutParams);
                        sparseArray.put(i, voteProgressLayout);
                    } else {
                        voteProgressLayout = (VoteProgressLayout) sparseArray.get(i);
                    }
                    voteProgressLayout.setPadding(dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT), dip2px(DEFAULT_H_WIDTH), dip2px(DEFAULT_V_HEIGHT));
                    voteProgressLayout.setVisibility(View.VISIBLE);
                    voteProgressLayout.setStatusTag(type.getTag());
                    final RichText choiceText = voteProgressLayout.getChoiceText();
                    voteProgressLayout.setVoteProgess((int) Float.parseFloat(percentum));
                    voteProgressLayout.setVoteProgressDes(percentum + "%");
                    voteProgressLayout.setRightDes(count);
                    voteProgressLayout.setLeftDes(name);
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
                                onVoteSubmit(qid, choiceText.getText().toString());
                            }
                        });
                    } else {
                        choiceText.setOnClickListener(null);
                        String is_user_radio = object.getString("is_user_radio");
                        choiceText.setDrawable(("1".equals(is_user_radio) ? R.mipmap.exams2_10 : R.mipmap.exams2_07), dip2px(DEFAULT_IMG_WIDTH), dip2px(DEFAULT_IMG_HEIGHT));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*注意这里加个过滤  将多余的item从parent remove掉
            如果size == childCount 不做
            否则判断多少 然后去add  或者remove
            *****
            经过测试  复用时会选择相同数量的item的holder  但是以防万一  加上一成过滤*/
            if ((parent.getChildCount() - beforeInitViewCount) > viewCount) {
                for (int i = viewCount; i < parent.getChildCount(); i++) {
                    if (sparseArray.get(i) == null) {
                        parent.removeViewAt(i);
                    } else {
                        sparseArray.get(i).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /**
     * 投票内容开始提交
     *
     * @param qid
     * @param answer
     */
    public void onVoteSubmit(String qid, String answer) {

    }

    /**
     * 调研内容开始提交
     *
     * @param data
     */
    public void oninvestigaSubmit(String data) {

    }

}
