package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/3/13.
 */

public class ViewIndicator extends LinearLayout {
    private int mMarginLeft =60;//px
    private int mNum;
    private List<ImageView> imageViews = new ArrayList<>();

    public ViewIndicator(Context context) {
        this(context, null);
    }

    public ViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    /**
     * 设置指示点的间距
     *
     * @param marginLeft
     */
    public void setMarginLeft(int marginLeft) {
        mMarginLeft = marginLeft;
    }


    /**
     * 设置indicator指示点的个数以及初始化
     *
     * @param num
     */
    public void setIndicatorViewNum(int num) {
        removeAllViews();
        imageViews.clear();
        mNum = num;
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(this.getContext());
            LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                imageView.setBackground(getResources().getDrawable(R.drawable.dot_gray));
            } else {
                params.leftMargin = mMarginLeft;
                imageView.setBackground(getResources().getDrawable(R.drawable.dot_blue));
            }
            imageViews.add(imageView);
            addView(imageView, params);
        }
    }

    /**
     * 设置当前index的位置
     *
     * @param index
     */
    public void setCurrentIndex(int index) {
        for (int i = 0; i < mNum; i++) {
            if (i == index) {
                imageViews.get(i).setBackground(getResources().getDrawable(R.drawable.dot_gray));
            } else {
                imageViews.get(i).setBackground(getResources().getDrawable(R.drawable.dot_blue));
            }
        }
    }


    public class CustomViewPager extends ViewPager {
        private static final String TAG = "CustomViewPager";
        private MyPagerAdapter mPagerAdapter;
        private int mTotal;//总数
        private int mCurrentPos;//当前位置
        public static final int AUTO_SWITCH = 0;//自动切换
        public static final int MANUAL_SWITCH = 1;//手动切换
        private int mCurSwitchMode = AUTO_SWITCH;
        private static final int DELAY_TIME = 2000;//自动播放间隔时间
        private int mItem = -100;
        private ViewIndicator mViewIndicator;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        public CustomViewPager(Context context) {
            this(context, null);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            mPagerAdapter = new MyPagerAdapter();
            this.setAdapter(mPagerAdapter);
            this.addOnPageChangeListener(mPageChangeListener);
        }

        private OnPageChangeListener mPageChangeListener = new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;
                if (mCurSwitchMode == AUTO_SWITCH) {
                    mHandler.postDelayed(mAutoSwitchRunnable, DELAY_TIME);
                }
                mViewIndicator.setCurrentIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, " THE OPANGE...-->>" + state);
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        mItem = -100;
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mItem = CustomViewPager.this.getCurrentItem();
                        mHandler.removeCallbacks(mAutoSwitchRunnable);
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        int item = CustomViewPager.this.getCurrentItem();
                        Log.d(TAG, "the item-->>" + item + " the mItem-->>" + mItem);
                        if (Math.abs(mItem - item) == 1) {
                            mCurSwitchMode = MANUAL_SWITCH;
                        } else if (mCurSwitchMode == AUTO_SWITCH && mItem != -100) {
                            mHandler.postDelayed(mAutoSwitchRunnable, DELAY_TIME);
                        }
                        break;
                }
            }
        };

        private Runnable mAutoSwitchRunnable = new Runnable() {
            @Override
            public void run() {
                if (mTotal > 1 && mCurSwitchMode == AUTO_SWITCH) {
                    ++mCurrentPos;
                    int item = (mCurrentPos) % mTotal;
                    if (item == 0) {
                        mCurSwitchMode = MANUAL_SWITCH;
                        return;
                    }
                    CustomViewPager.this.setCurrentItem(item, true);
                }
            }
        };


        public void startAutoSwitch() {
            if (mTotal > 1 && mCurSwitchMode == AUTO_SWITCH) {
                mHandler.postDelayed(mAutoSwitchRunnable, DELAY_TIME);
            }
        }

        public void setIndicator(ViewIndicator viewIndicator) {
            mViewIndicator = viewIndicator;
            mViewIndicator.setIndicatorViewNum(mTotal);
        }

        /**
         * 设置切换模式
         *
         * @param mode
         */
        public void setSwitchMode(int mode) {
            mCurSwitchMode = mode;
        }

        public void reset() {
            mCurSwitchMode = AUTO_SWITCH;
            mHandler.removeCallbacks(mAutoSwitchRunnable);
        }

        /**
         * 添加所有子View到ViewPager
         *
         * @param views
         */
        public void addAll(List<View> views) {
            mPagerAdapter.addItemAll(views);
            mTotal = mPagerAdapter.getCount();
        }

        /**
         * 添加指定的View到ViewPager
         *
         * @param view
         */
        public void add(View view) {
            mPagerAdapter.addItem(view);
            mTotal = mPagerAdapter.getCount();
        }

        /**
         * 添加指定View到指定位置
         *
         * @param view
         * @param pos
         */
        public void add(View view, int pos) {
            mPagerAdapter.addItem(view, pos);
            mTotal = mPagerAdapter.getCount();
        }

        /**
         * 移除指定View
         *
         * @param view
         */
        public void remove(View view) {
            mPagerAdapter.removeItem(view);
            mTotal = mPagerAdapter.getCount();
        }

        public void remove(int pos) {
            mPagerAdapter.removeItem(pos);
            mTotal = mPagerAdapter.getCount();
        }

        /**
         * 适配器
         */
        private class MyPagerAdapter extends PagerAdapter {
            private List<View> itemViews = new ArrayList<>();

            public void addItemAll(List<View> views) {
                itemViews.clear();
                itemViews.addAll(views);
                this.notifyDataSetChanged();
            }

            public void addItem(View view) {
                itemViews.add(view);
                this.notifyDataSetChanged();
            }

            public void removeItem(View view) {
                itemViews.remove(view);
                this.notifyDataSetChanged();
            }

            public void removeItem(int pos) {
                itemViews.remove(pos);
                this.notifyDataSetChanged();
            }

            public void addItem(View view, int pos) {
                itemViews.add(pos, view);
                this.notifyDataSetChanged();
            }

            @Override
            public int getCount() {
//            Log.d(TAG,"the count--->>"+itemViews.size());
                return itemViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d(TAG, "====instantiateItem=== " + position);
                container.addView(itemViews.get(position));
                return itemViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d(TAG, "====destroyItem=== " + position);
                container.removeView((View) object);
            }
        }

    }
}