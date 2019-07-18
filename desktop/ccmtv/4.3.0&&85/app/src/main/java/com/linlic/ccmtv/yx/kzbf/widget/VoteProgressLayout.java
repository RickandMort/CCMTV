package com.linlic.ccmtv.yx.kzbf.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;


/**
 * Created by bentley on 2018/11/7.
 */

public class VoteProgressLayout extends LinearLayout {
    private RichText des_left, des_right, des_progress;
    private MyProgessLine progessLine;
    private Object statusTag;

    public VoteProgressLayout(Context context) {
        this(context, null);
    }

    public VoteProgressLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteProgressLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.vote_recycler_adapter_item, this);
        des_left = findViewById(R.id.tv_vote_des_left);
        des_right = findViewById(R.id.tv_vote_des_right);
        des_progress = findViewById(R.id.tv_vote_progress_des);
        progessLine = findViewById(R.id.mpl_vote_progress);
    }

    public RichText getChoiceText() {
        return des_left;
    }


    /**
     * 设置进度
     *
     * @param vote
     */
    public void setVoteProgess(int vote) {
        if (getStatus() && vote == 0) {
            progessLine.setVisibility(GONE);
            des_progress.setVisibility(GONE);
        } else {
            progessLine.setVisibility(VISIBLE);
            des_progress.setVisibility(VISIBLE);
            progessLine.setProgress(vote);
        }
    }

    /**
     * 设置左边的描述
     */
    public void setLeftDes(String des) {
        des_left.setText(getNotNullString(des));
    }


    /**
     * 设置右边的描述
     *
     * @param des
     */
    public void setRightDes(int des) {
        if (false) {
            des_right.setVisibility(GONE);
        } else {
            des_right.setVisibility(VISIBLE);
            des_right.setText(getNotNullString(String.valueOf(des)) + "票");
        }
    }

    public void setLeftClickable(boolean clickable) {
        des_left.setClickable(clickable);
    }

    /**
     * 设置进度描述
     *
     * @param des
     */
    public void setVoteProgressDes(String des) {
        des_progress.setText(getNotNullString(des));
    }

    private String getNotNullString(String string) {
        return string == null ? "" : string;
    }


    public boolean getStatus() {
        return "1".equals(statusTag);//是否是未提交
    }

    public void setStatusTag(Object statusTag) {
        this.statusTag = statusTag;
        if (!getStatus()) des_left.setDrawable(null);
    }
}
