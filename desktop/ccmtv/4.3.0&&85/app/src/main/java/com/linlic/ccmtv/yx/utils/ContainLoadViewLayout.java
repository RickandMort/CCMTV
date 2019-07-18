package com.linlic.ccmtv.yx.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;

public class ContainLoadViewLayout extends LinearLayout {
	
	private LoadingView mLoadingView;
	private ImageView mOval;
	
	private int mFallDistance;

	public ContainLoadViewLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ContainLoadViewLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public ContainLoadViewLayout(Context context) {
		this(context,null);
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.loading2,null);
		mLoadingView = (LoadingView) view.findViewById(R.id.shapeLoadingView); //��ǰ���ڻ��Ƶ�ͼ��
		mOval = (ImageView) view.findViewById(R.id.bottom_shadow); //������ʾ�ײ�����Ӱ
		addView(view);
		
		mFallDistance = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 70, getContext().getResources()
						.getDisplayMetrics());
		
		//������ɲ��ֽ����Ժ���������Ķ���
		startFall();
	}
	
	
	/**
	 * ����Ķ���
	 */
	public void startFall() {
		ObjectAnimator rectAnimator = ObjectAnimator.ofFloat(mLoadingView, "translationY", mFallDistance,0);
		ObjectAnimator ovalAnimator = ObjectAnimator.ofFloat(mOval, "scaleX", 1.0f,0.3f);
		rectAnimator.setInterpolator(new DecelerateInterpolator());
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(1000);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
		    public void onAnimationEnd(Animator animation) {
				startUp();
		    }
		});
		
		animatorSet.play(rectAnimator).with(ovalAnimator);
		animatorSet.start();
	}
	
	/**
	 * ���������Ķ���
	 */
	public void startUp() {
		ObjectAnimator rectUpAnimator = ObjectAnimator.ofFloat(mLoadingView, "translationY", 0,mFallDistance);
		ObjectAnimator rectRotateAnimator = ObjectAnimator.ofFloat(mLoadingView, "rotation", 0,180);
		if (mLoadingView.getCurrentShape() == LoadingView.Shape.RACTANGLE) {//����������Σ�������ת
			rectRotateAnimator = ObjectAnimator.ofFloat(mLoadingView, "rotation", 0,-180);
		}
		ObjectAnimator ovalAnimator = ObjectAnimator.ofFloat(mOval, "scaleX", 0.3f,1.0f);
		rectUpAnimator.setInterpolator(new AccelerateInterpolator());
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(1000);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
		    public void onAnimationEnd(Animator animation) {
				//�ڵ����������֮�󣬵���changeShape���÷������������õ�ǰ��Ҫ���Ƶ�ͼ�Σ����ػ��ͼ��
				mLoadingView.changeShape(mLoadingView.getCurrentShape());
				//����ִ������������Ķ���
				startFall();
		    }
		});
		
		animatorSet.play(rectUpAnimator).with(rectRotateAnimator).with(ovalAnimator);
		animatorSet.start();
	}
	
	
}
