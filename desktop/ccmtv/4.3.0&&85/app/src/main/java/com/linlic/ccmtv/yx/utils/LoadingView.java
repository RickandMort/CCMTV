package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
	
	private Paint mPaint;
	private Shape mCurrentShape = Shape.RECT; //��һ�λ��ƾ���
	private Path mPath;
	
	//����һ��ö�٣�������ʶ��ǰ��Ҫ���Ƶ���״����
	public enum Shape{
		CIRCLE,
		RECT,
		RACTANGLE
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		//һЩ��ʼ������
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPath = new Path();
	}

	public LoadingView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public LoadingView(Context context) {
		this(context,null);
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//ʹ��switch���жϵ�ǰ��Ҫ����ͼ�ε���״
		switch (mCurrentShape) {
		case RECT:
			mPaint.setColor(Color.parseColor("#FF9999"));
			canvas.drawRect(0,0,getWidth(),getHeight(), mPaint);
			break;
		case CIRCLE:
			int circleRadius = Math.min(getWidth(), getHeight()) / 2;
			mPaint.setColor(Color.parseColor("#99FFCC"));
			canvas.drawCircle(getWidth() / 2, getHeight() / 2 ,circleRadius, mPaint);
			break;
		case RACTANGLE:
			mPaint.setColor(Color.parseColor("#99CCFF"));
			mPath.reset();
			mPath.moveTo(getWidth() / 2,0);
			mPath.lineTo(0,getHeight());
			mPath.lineTo(getWidth(), getHeight());
			mPath.close();
			canvas.drawPath(mPath, mPaint);
			break;
		default:
			break;
		}
	}
	
	/**
	 * �ı䵱ǰ��Ҫ���Ƶ���״���ýӿ����ṩ����ߵ��õ�
	 * @param currentShape ��ǰ�Ѿ����Ƶ���״
	 */
	public void changeShape(Shape currentShape) {
		if (currentShape == Shape.RECT) {
			mCurrentShape = Shape.CIRCLE;
		} else	if (currentShape == Shape.CIRCLE) {
			mCurrentShape = Shape.RACTANGLE;
		} else 	if (currentShape == Shape.RACTANGLE) {
			mCurrentShape = Shape.RECT;
		}
		//�������ͼ�ε���״֮�󣬼ǵ��ػ�
		invalidate();
	}
	
	/**
	 * ��õ�ǰ���Ƶ���״
	 * @return
	 */
	public Shape getCurrentShape() {
		return mCurrentShape;
	}
	
}
