package com.linlic.ccmtv.yx.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * Created by bentley on 2018/10/19.
 * 数据为空页面的layout
 */
public class NodataEmptyLayout extends RelativeLayout {
    private TextView tvEmptyDes;
    private ImageView ivEmptyIcon;
    private Button btnNodata;
    private View vTopLine;
    private Drawable mEmptyDrawable;
    private String mEmptyDes;
    private String text_color;
    private boolean isTopLine;
    private Context context;


    public NodataEmptyLayout(Context context) {
        this(context, null);
    }

    public NodataEmptyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodataEmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.empty_nodata, this, true);
        ivEmptyIcon = findViewById(R.id.iv_nodata_empty_img);
        tvEmptyDes = findViewById(R.id.tv_empty_nodata_des);
        btnNodata = findViewById(R.id.btn_empty_nodata_ckick);
        vTopLine = findViewById(R.id.v_nodata_empty_top_line);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NodataEmptyLayout);

        mEmptyDrawable = a.getDrawable(R.styleable.NodataEmptyLayout_empty_src);
        mEmptyDes = a.getString(R.styleable.NodataEmptyLayout_empty_des);
        text_color = a.getString(R.styleable.NodataEmptyLayout_text_color);
        isTopLine = a.getBoolean(R.styleable.NodataEmptyLayout_empty_topLine, false);
        vTopLine.setVisibility(isTopLine ? VISIBLE : GONE);
        a.recycle();
//        ivEmptyIcon.post(new Runnable() {
//            @Override
//            public void run() {
//                RelativeLayout.LayoutParams layoutParams = new LayoutParams(ivEmptyIcon.getWidth() / 2, ivEmptyIcon.getHeight() / 2);
//                ivEmptyIcon.setLayoutParams(layoutParams);
//
//            }
//        });
        if (mEmptyDrawable != null) ivEmptyIcon.setImageDrawable(mEmptyDrawable);
        tvEmptyDes.setText(getEmptyDes(mEmptyDes));
        if(text_color!=null && text_color.length()>0){
            tvEmptyDes.setTextColor(Color.parseColor(text_color));
        }
    }

    /**
     * 获取描述文本View
     *
     * @return TextView
     */
    public TextView getTvEmptyDes() {
        return tvEmptyDes;
    }

    /**
     * 获取描述图片的View
     *
     * @return ImageView
     */
    public ImageView getIvEmptyIcon() {
        return ivEmptyIcon;
    }

    /**
     * 获取可操作点击事件的View
     *
     * @return Button
     */
    public Button getBtnNodata() {
        return btnNodata;
    }

    public NodataEmptyLayout getNodataEmptyData() {
        return this;
    }

    /**
     * 设置页面的nodata图片
     *
     * @param imgId
     */
    public void setNetErrorIcon(int imgId) {
//        ivEmptyIcon.setImageDrawable(drawable(imgId));
        ivEmptyIcon.setImageResource(imgId);
        setEmptyDes("当前还没有网络连接哦~");
    }

    /**
     * 设置页面的nodata图片
     */
    public void setNetErrorIcon() {
        setNetErrorIcon(R.mipmap.nodata_nonet);
    }

    /**
     * 设置xml中配置的图片
     *
     * @param imgId
     */
    public void setLastEmptyIcon(int imgId) {
//        ivEmptyIcon.setImageDrawable(drawable(imgId));
        if (mEmptyDrawable != null) {
            ivEmptyIcon.setImageDrawable(mEmptyDrawable);
        } else {
            ivEmptyIcon.setImageResource(imgId);
        }
        setEmptyDes(mEmptyDes);
    }

    /**
     * 设置xml中配置的图片
     */
    public void setLastEmptyIcon() {
        setLastEmptyIcon(R.mipmap.nodata_empty);
    }


    /**
     * 设置页面nodata的描述文字
     *
     * @param des
     */
    public void setEmptyDes(String des) {
        tvEmptyDes.setText(getEmptyDes(des));
    }
    /**
     * 设置页面nodata的描述文字
     *
     * @param des
     */
    public void setEmptyDesColor(String des) {
        tvEmptyDes.setTextColor(Color.parseColor(des));
    }

    private String getEmptyDes(String des) {
        return TextUtils.isEmpty(des) ? "还没有任何数据哦~" : des;
    }

    public Drawable drawable(@DrawableRes int id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, context.getTheme());
        }
        return context.getResources().getDrawable(id);

    }
}
