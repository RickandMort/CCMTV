package com.linlic.ccmtv.yx.activity.medal.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalChildsDetialBean;

import java.util.Arrays;
import java.util.List;


/**
 * Created by bentley on 2018/11/20.
 */

public class MedalListItemView extends View {
    private List<Integer> gradeIcons = Arrays.asList(R.mipmap.medal_list_item_medal_grade_01, R.mipmap.medal_list_item_medal_grade_02, R.mipmap.medal_list_item_medal_grade_03,
            R.mipmap.medal_list_item_medal_grade_04, R.mipmap.medal_list_item_medal_grade_05);

    private int DEFAULT_IMG_HEIGHT = 348;//背景图片的宽高
    private int DEFAULT_IMG_WIDTH = 296;//背景图片的宽高

    private float DEFAULT_MEDAL_IMG_HEIGHT = 150 * 1.1f;//勋章图片的宽高
    private float DEFAULT_MEDAL_IMG_WIDTH = 150 * 1.1f;//


    private int DEFAULT_MEDAL_BOX_HEIGHT = 159;//宝箱图片的默认宽高
    private int DEFAULT_MEDAL_BOX_WIDTH = 159;
    private int DEFAULT_MEDAL_BOX_OUTSET = 30;//宝箱默认向下偏移的位置
    private float medal_box_scale = 0.7f;//宝箱图片的缩放

    private int DEFAULT_MEDAL_GRADE_ICON_HEIGHT = 37;//等级图标默认的高度
    private int DEFAULT_MEDAL_GRADE_ICON_WIDTH = 30;//等级图标默认的宽度

    private int NUMBER_OF_PEOPLE_LINE = 18;//绘制获取人数的基准线
    private int CIRLE_PROGRESS_WIDTH = 10;//绘制进度条线的宽度


    private String NUMBER_OF_PEOPLE = "500人获得";
    private String NAME_OF_MEDAL = "成长新星";
    private String[] ASK_OF_MEDALS = {"购买过vip", "观看时长超过20小时"};
    private float currentProgress = 0f;

    /**
     * 绘制图片的矩形
     */
    protected Rect imgRect = new Rect();
    /**
     * 底部文字的画笔
     */
    private Rect textBround;// 文本的绘制范围
    protected Paint mPaint;
    protected Paint mTextPaint;
    private int mWidth;
    private int mHeight;

    private String content = "+5积分";

    private Bitmap mCenterIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.medal_get_status_view_bg);
    private Bitmap mMedalIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.medal_get_status_icon_new);
    private Bitmap mBoxIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.medal_get_status_treasure_box);
    private Bitmap mGradeIcon;


    /**
     * 设置是否展示宝箱和动画
     */
    private boolean isShowBox = false;
    private RectF oval;

    public void setMedalChildBean(MedalChildsDetialBean medalChilds) {
        final MedalChildsDetialBean.MygradeBean mygrade = medalChilds.getMygrade();
        MedalChildsDetialBean.MymedalBean mymedal = medalChilds.getMymedal();
        setNumberOfPeople(Integer.parseInt(medalChilds.getCont()));
        if (mymedal != null) {
            String about = mymedal.getAbout();
            if (!TextUtils.isEmpty(about)) {
                setAskOfMedals(about.split("且"));
            }
            setGradeIcon("0".equals(mygrade.getAchieve()) ? 0
                    : Integer.parseInt(mymedal.getGrade() == null ? "1" : mymedal.getGrade()), mygrade.getStatus());
        }
        setNameOfMedal(medalChilds.getName());

        setBackStatusIcon("1".equals(mygrade.getAchieve()));
        setProgerss("1".equals(mygrade.getAchieve()) ? 1.0f : Float.parseFloat(mygrade.getCompletion()) / 100);
        setShowBox("1".equals(mygrade.getAchieve()) && "0".equals(mygrade.getReward_status()));
//        setShowBox(true);
        invalidate();
    }

    public void setShowBox(boolean showBox) {
        isShowBox = showBox;
        startAnimtion();
    }

    public boolean isShowBox() {
        return isShowBox;
    }

    /**
     * 设置勋章的等级的图标
     *
     * @param i      等级
     * @param status 完成状态
     */
    private void setGradeIcon(int i, String status) {
        if (i > gradeIcons.size() - 1 || i < 1) {
            mGradeIcon = null;
            return;
        }
        mGradeIcon = BitmapFactory.decodeResource(getResources(), gradeIcons.get(i - 1));
    }


    /**
     * 设置勋章完成状态的背景图片
     *
     * @param backStatusIcon
     */
    public void setBackStatusIcon(boolean backStatusIcon) {
        mCenterIcon = BitmapFactory.decodeResource(getResources(), backStatusIcon ? R.mipmap.medal_get_status_view_bg : R.mipmap.medal_get_status_view_bg_noget);
    }

    /**
     * 设置勋章的图片
     */
    public void setMedalIcon(Bitmap medalIcon) {
        mMedalIcon = medalIcon;
        invalidate();
    }

    /**
     * 设置勋章获取的进度
     *
     * @param currentProgress
     */
    public void setProgerss(float currentProgress) {
        this.currentProgress = currentProgress;
    }

    /**
     * 设置获取该勋章的人数
     *
     * @param number
     */
    public void setNumberOfPeople(int number) {
        NUMBER_OF_PEOPLE = number + "人获得";
    }

    /**
     * 设置获取该勋章的要求
     */
    public void setAskOfMedals(String[] asks) {
        ASK_OF_MEDALS = asks;
    }

    /**
     * 设置该勋章的名称
     */
    public void setNameOfMedal(String name) {
        NAME_OF_MEDAL = name;
    }

    /**
     * 图片被放大缩小的比例
     */
    private float ratio;
    private Paint.FontMetrics fontMetrics;//用以测量文字的宽度
    private int pixelWidth;
    private ValueAnimator valueAnimator;
    private Matrix mCurrentMatrix;

    public MedalListItemView(Context context) {
        this(context, null);
    }

    public MedalListItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalListItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textBround = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 消除锯齿
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 设置
        mPaint.setStyle(Paint.Style.STROKE);// 设置空心
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FFE470"));
        mTextPaint.setFakeBoldText(true);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        pixelWidth = dm.widthPixels;

//        setLayerType(LAYER_TYPE_SOFTWARE, null);//对单独的View在运行时阶段禁用硬件加速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension((pixelWidth / 2), widthMeasureSpec);//默认宽度屏幕的1/2.2
        int height = measureDimension((int) ((DEFAULT_IMG_HEIGHT + DEFAULT_MEDAL_BOX_OUTSET) * (width / (DEFAULT_IMG_WIDTH * 1.0f))), heightMeasureSpec);
        //将计算的宽和高设置进去，保存，最后一步一定要有
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBgIcon(canvas);
        DrawMedalIcon(canvas);
        DrawCirleProgress(canvas);
        DrawMedalInfo(canvas);
        DrawTreasureBox(canvas);
    }

    /**
     * 绘制背景的图片
     *
     * @param canvas
     */
    private void DrawBgIcon(Canvas canvas) {
        mWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        //View宽度与图片本身宽度比
        ratio = mWidth / (DEFAULT_IMG_WIDTH * 1.0f);
        mHeight = (int) (DEFAULT_IMG_HEIGHT * ((getWidth() * 1.0f) / DEFAULT_IMG_WIDTH));//图片本身的高度*宽度比
        int line = (int) (NUMBER_OF_PEOPLE_LINE * ratio) + getPaddingTop();//文字绘制的基准线
        imgRect.left = getPaddingLeft();
        imgRect.top = getPaddingTop();
        imgRect.bottom = mHeight + getPaddingTop();
        imgRect.right = mWidth + getPaddingLeft();
        canvas.drawBitmap(mCenterIcon, null, imgRect, mPaint);
        mTextPaint.setTextSize(20 * ratio);
        mTextPaint.setColor(Color.parseColor("#f8f8f8"));
        mTextPaint.getTextBounds(NUMBER_OF_PEOPLE, 0, NUMBER_OF_PEOPLE.length(), textBround);
        float xText = getPaddingLeft() + mWidth / 2 - textBround.width() / 2;
        float yText = line + textBround.height() / 2;
        canvas.drawText(NUMBER_OF_PEOPLE, xText, yText, mTextPaint);
    }


    /**
     * 绘制勋章的Icon
     *
     * @param canvas
     */
    private void DrawMedalIcon(Canvas canvas) {
        float mw = DEFAULT_MEDAL_IMG_WIDTH * ratio;
        float mh = DEFAULT_MEDAL_IMG_HEIGHT * ratio;
        imgRect.left = (int) (mWidth / 2 - mw / 2) + getPaddingLeft();
        imgRect.top = (int) ((NUMBER_OF_PEOPLE_LINE * 2 + 10) * ratio) + getPaddingTop();
        imgRect.bottom = (int) (imgRect.top + mh);
        imgRect.right = (int) (imgRect.left + mw);
        // 绘图
        canvas.drawBitmap(mMedalIcon, null, imgRect, mPaint);
    }

    /**
     * 绘制中间的进度条
     *
     * @param canvas
     */
    private void DrawCirleProgress(Canvas canvas) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(CIRLE_PROGRESS_WIDTH * ratio);
//        绘制阴影   当前的界面需要关闭硬件加速
//        mPaint.setShadowLayer(5F, 0, 0, Color.BLACK);
        float centerY = imgRect.top + (imgRect.bottom - imgRect.top) / 2.0f;
        float mRadius = DEFAULT_MEDAL_IMG_WIDTH * ratio / 2 - 20 * ratio; //往内收一点
        if (oval == null)
            oval = new RectF(getWidth() / 2 - mRadius, centerY - mRadius, getWidth() / 2 + mRadius, centerY + mRadius);
        mPaint.setColor(Color.parseColor("#999999"));
        canvas.drawArc(oval, -270, currentProgress >= 1.0f ? 0 : 360, false, mPaint);
        mPaint.setColor(Color.parseColor("#5A96EA"));
        canvas.drawArc(oval, -270, currentProgress >= 1.0f ? 0 : 360 * currentProgress, false, mPaint);
    }

    /**
     * 绘制勋章相关的信息
     *
     * @param canvas
     */
    private void DrawMedalInfo(Canvas canvas) {
        //绘制勋章的名字
        mTextPaint.setTextSize(24 * ratio);
        mTextPaint.setColor(Color.BLACK);
        float xText = mWidth / 2 - getTextWidth(NAME_OF_MEDAL) / 2 + getPaddingLeft();
        float yText = imgRect.bottom + (15 * ratio) + Math.abs(getTextHeight(NAME_OF_MEDAL));
        canvas.drawText(NAME_OF_MEDAL, xText, yText, mTextPaint);
        yText += 10;
        for (int i = 0; i < ASK_OF_MEDALS.length; i++) {
            //绘制勋章的要求
            mTextPaint.setTextSize(18 * ratio);
            mTextPaint.setColor(Color.parseColor("#939393"));
            xText = mWidth / 2 - getTextWidth(ASK_OF_MEDALS[i]) / 2 + getPaddingLeft();
            yText = yText + Math.abs(getTextHeight(ASK_OF_MEDALS[i]));
            canvas.drawText(ASK_OF_MEDALS[i], xText, yText, mTextPaint);
        }

        if (mGradeIcon == null) return;
        float gradeH = DEFAULT_MEDAL_GRADE_ICON_HEIGHT * ratio;
        float gradeW = DEFAULT_MEDAL_GRADE_ICON_WIDTH * ratio;
        float start = mWidth / 2 + getTextWidth(NAME_OF_MEDAL) / 2;

        imgRect.left = (int) (start + 15 * ratio);
        imgRect.top = (int) (imgRect.bottom + (25 * ratio));
        imgRect.bottom = (int) (imgRect.top + gradeH);
        imgRect.right = (int) (imgRect.left + gradeW);
        canvas.drawBitmap(mGradeIcon, null, imgRect, mPaint);
    }

    /**
     * 绘制宝箱动画
     *
     * @param canvas
     */
    private void DrawTreasureBox(Canvas canvas) {
        if (!isShowBox) return;
        float bw = DEFAULT_MEDAL_BOX_WIDTH * ratio;
        float bh = DEFAULT_MEDAL_BOX_HEIGHT * ratio;
        float offset = 0;
        // 绘图
        mCurrentMatrix = new Matrix();
        mCurrentMatrix.postScale(bw / mBoxIcon.getWidth(), bh / mBoxIcon.getHeight());
        mCurrentMatrix.postTranslate(getWidth() / 2 - bw / 2,
                (getHeight() - getPaddingBottom() - bh - offset));
        mCurrentMatrix.postRotate(medal_box_scale,
                (getWidth() / 2),
                (getHeight() - getPaddingBottom() - bh / 2) - offset);
        canvas.drawBitmap(mBoxIcon, mCurrentMatrix, mPaint);
    }

    public void startAnimtion() {
        if (!isShowBox) {
            if (valueAnimator != null) valueAnimator.cancel();
            invalidate();
            return;
        }
        if (valueAnimator == null) {
            valueAnimator = ObjectAnimator.ofFloat(-3f, 3f);
            valueAnimator.setDuration(150);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    medal_box_scale = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    private float getTextWidth(String content) {
        return mTextPaint.measureText(content);
    }

    private float getTextHeight(String content) {
        fontMetrics = mTextPaint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top;
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


}
