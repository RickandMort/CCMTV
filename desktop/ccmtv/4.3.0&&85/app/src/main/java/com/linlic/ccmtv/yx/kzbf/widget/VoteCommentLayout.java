package com.linlic.ccmtv.yx.kzbf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.GlideCircleTransform;

/**
 * Created by bentley on 2018/11/7.
 */

public class VoteCommentLayout extends LinearLayout {
    private ImageView kzbf_message_user_img;
    private TextView kzbf_message_content;
    private TextView kzbf_message_addtime;
    private TextView kzbf_message_username;

    public VoteCommentLayout(Context context) {
        this(context, null);
    }

    public VoteCommentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteCommentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.kzbf_vote_comment, this);
        kzbf_message_user_img = findViewById(R.id.kzbf_message_user_img);
        kzbf_message_content = findViewById(R.id.kzbf_message_content);
        kzbf_message_addtime = findViewById(R.id.kzbf_message_addtime);
        kzbf_message_username = findViewById(R.id.kzbf_message_username);
    }

    public void setKzbfMessageInfo(String url, String name, String content, String addtime) {
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default2).transform(new GlideCircleTransform(getContext()));
        Glide.with(getContext()).load(url).apply(options).into(kzbf_message_user_img);
        kzbf_message_content.setText(content + "");
        kzbf_message_addtime.setText(addtime + "");
        kzbf_message_username.setText(name + "");
    }
}
