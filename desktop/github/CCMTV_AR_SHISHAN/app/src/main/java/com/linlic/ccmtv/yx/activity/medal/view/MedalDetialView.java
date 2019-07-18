package com.linlic.ccmtv.yx.activity.medal.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalDetialBean;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalShareBean;
import com.linlic.ccmtv.yx.activity.medal.utils.ShareUtils;
import com.linlic.ccmtv.yx.activity.upload.new_upload.Upload_video3;
import com.linlic.ccmtv.yx.kzbf.widget.RichText;
import com.linlic.ccmtv.yx.utils.DisplayUtil;


/**
 * Created by bentley on 2018/11/23.
 */

public class MedalDetialView extends LinearLayout implements View.OnClickListener {
    private int DEFAULT_BG_WIDTH = 397;
    private int DEFAULT_BG_HEIGHT = 569;

    private int DEFAULT_MEDAL_IMG_HEIGHT = 270;
    private int DEFAULT_MEDAL_IMG_WIDTH = 280;

    private int defaultHpadding = 25;
    private int defaultVpadding = 20;
    private int defaultRatio;//默认缩放的比例


    private MedalDetialBean medalDetial;//勋章的信息
    private ImageView medalIcon;
    private TextView number_acquired, name, todo_something;
    private RichText experience_acquired, glod_acquired;
    private CircleBarView cicler_bar;
    private MedalDetialTitleImageView reword_sign;
    private String shareUrl;
    private String shareTittle;

    public MedalDetialView(Context context) {
        this(context, null);
    }

    public MedalDetialView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalDetialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.medal_detial_layout, this);

        medalIcon = (ImageView) findViewById(R.id.medal_detial_icon);
        experience_acquired = (RichText) findViewById(R.id.medal_detial_experience_acquired);
        glod_acquired = (RichText) findViewById(R.id.medal_detial_glod_acquired);
        name = (TextView) findViewById(R.id.medal_detial_name);
        cicler_bar = (CircleBarView) findViewById(R.id.medal_detial_cicler_bar);
        number_acquired = (TextView) findViewById(R.id.medal_detial_number_acquired);
        todo_something = (TextView) findViewById(R.id.medal_detial_todo_something);
        reword_sign = findViewById(R.id.medal_detial_reword_sign_icon);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(DEFAULT_BG_WIDTH, widthMeasureSpec);
        int height = measureDimension((int) (DEFAULT_BG_HEIGHT * (width / (DEFAULT_BG_WIDTH * 1.0f))), heightMeasureSpec);
        defaultRatio = (int) (width / (DEFAULT_BG_WIDTH * 1.0f));
        setPadding(25 * defaultRatio, 20 * defaultRatio, 25 * defaultRatio, 30 * defaultRatio);
    }

    public void setMedalDetial(MedalDetialBean medalDetial) {
        if (medalDetial == null) return;
        this.medalDetial = medalDetial;
        experience_acquired.setText("经验值+" + medalDetial.getData().getExperience());
        glod_acquired.setText("积分+" + medalDetial.getData().getIntegral());
        name.setText(medalDetial.getData().getName());
        number_acquired.setText("已有" + medalDetial.getData().getCount() + "人获得");
        cicler_bar.setProgress(1.0f);
        switch (medalDetial.getData().getReward_status()) {
            //reward_status 0 为 该勋章未完成  reward_status 1 为 该勋章奖励已领取 reward_status 2 为 该勋章第一次完成领取奖励
            case "0":
                setBackgroundResource(R.mipmap.medal_detial_content_bg_gray);
                experience_acquired.setDrawable(R.mipmap.medal_detial_content_experience_gray, DisplayUtil.dip2px(50), DisplayUtil.dip2px(50));
                glod_acquired.setDrawable(R.mipmap.medal_detial_content_integral_gray, DisplayUtil.dip2px(50), DisplayUtil.dip2px(50));
                name.setTextColor(Color.parseColor("#636363"));
                experience_acquired.setTextColor(Color.parseColor("#939393"));
                glod_acquired.setTextColor(Color.parseColor("#939393"));
                reword_sign.setImageResource(R.mipmap.medal_detial_content_my_reword_gray);
                number_acquired.setTextColor(Color.parseColor("#777777"));
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.medal_get_status_icon);
                Glide.with(getContext())
                        .load(medalDetial.getData().getD_icon())
                        .apply(options)
                        .into(medalIcon);
                cicler_bar.setProgress(Float.parseFloat(medalDetial.getData().getCompletion()) / 100);
                todo_something.setVisibility(VISIBLE);
                todo_something.setTag(medalDetial.getData().getType());
                todo_something.setText("1".equals(medalDetial.getData().getType()) ? "去观看" : "去上传");
                todoSomething(todo_something);
                break;
            case "1":
                todo_something.setVisibility(GONE);
            case "2":
                todo_something.setVisibility(VISIBLE);
                RequestOptions options1 = new RequestOptions().placeholder(R.mipmap.medal_get_status_icon);
                Glide.with(getContext())
                        .load(medalDetial.getData().getIcon())
                        .apply(options1)
                        .into(medalIcon);
                todo_something.setOnClickListener(this);
                break;
            default:
                break;
        }
    }

    /***
     * 未完成勋章
     * @param v
     */
    private void todoSomething(View v) {
        //type 0为不需要跳转按钮 1 为 需要跳转观看视频页 2 为需要跳转上传文件页  type为传入tag
        if (v.getTag().equals("0")) v.setVisibility(GONE);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch ((String) v.getTag()) {
                    case "1":
                        intent.setClass(getContext(), MainActivity.class);
                        intent.putExtra("type", "tohome");
                        break;
                    case "2":
                        intent.setClass(getContext(), Upload_video3.class);
                        break;
                    default:
                        break;
                }
                getContext().startActivity(intent);
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * @param defualtSize 设置的默认大小
     * @param measureSpec 父控件传来的widthMeasureSpec，heightMeasureSpec
     * @return 结果
     */
    public int measureDimension(int defualtSize, int measureSpec) {
        int result = defualtSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //1,layout中自定义组件给出来确定的值，比如100dp
        //2,layout中自定义组件使用的是match_parent，但父控件的size已经可以确定了，比如设置的具体的值或者match_parent
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //layout中自定义组件使用的wrap_content
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defualtSize, specSize);//建议：result不能大于specSize
        }
        //UNSPECIFIED,没有任何限制，所以可以设置任何大小
        else {
            result = defualtSize;
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medal_detial_todo_something:
                new ShareUtils.Builder().setContext(getContext())
                        .setShareUrl(shareUrl)
                        .setShareTitle(shareTittle)
//                        .setDescribe("CCMTV")
//                        .setSharePicurl(URLConfig.Interface_URL)
                        .setPlatformActionListener(new ShareUtils.DefaultPlatformActionListener())
                        .build()
                        .startShareDialog();
                break;
            default:
                break;
        }
    }

    public void setShareBean(MedalShareBean medalShareBean){
        shareUrl = medalShareBean.getShareurl();
        shareTittle = medalShareBean.getTitle();
    }
}
