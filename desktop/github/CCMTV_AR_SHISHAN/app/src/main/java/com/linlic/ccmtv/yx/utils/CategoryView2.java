package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

import com.linlic.ccmtv.yx.R;

import java.util.List;


public class CategoryView2 extends LinearLayout implements OnCheckedChangeListener {
	private LayoutInflater inflater;
	public CategoryView2(Context context) {
		this(context, null);
	}
	public CategoryView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflater = LayoutInflater.from(context);
	}

	/**添加方法*/
	public void add(List<String> list,String str_one,String checkText) {
		if (list.size() > 0) {
			//加载布局
			View view = inflater.inflate(R.layout.category_container, null);
			addView(view);
			RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.container);
			HorizontalScrollView horizontalScrollView = (HorizontalScrollView)  view.findViewById(R.id.horizontalScrollView);
			horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
			RadioButton bt = null;
			/*bt.setTag(str_one);
			radioGroup.addView(bt);*/

			switch (checkText){
				case "手术演示":
					checkText = "手术";
					break;
				case "病例讨论":
					checkText = "病例";
					break;
				case "超级访问":
					checkText = "座谈";
					break;
				case "百家讲坛":
					checkText = "讲座";
					break;
				case "名家视角":
					checkText = "采访";
					break;
				case "最新视频":
					checkText = "最新";
					break;
				case "为我推荐":
					checkText = "最新";
					break;

			}

			// 全部
			for (String str : list) {
				bt = newRadioButton(str);//实例化新的RadioButton
				bt.setTag(str_one);
				radioGroup.addView(bt);
				if(str.equals(list.get(0))){
					// 默认选中 第一位
					radioGroup.check(bt.getId());
				}
				if(str.equals(checkText)){
					radioGroup.check(bt.getId());
				/*	int[] location = new int[2];
					bt.getLocationOnScreen(location);
					Log.e("当前选中的位置", location[0]+"--"+ location[1]);*/

				}

			}
			//为当前RadioGroup设置监听器
			radioGroup.setOnCheckedChangeListener(this);
		}
	}

	/**创建RadioButton*/
	private RadioButton newRadioButton(String text) {
		RadioButton button = new RadioButton(getContext());

		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		//设置内外边距
		params.leftMargin = 6;
		params.rightMargin = 6;
		button.setLayoutParams(params);
		button.setPadding(30, 20, 30, 20);

		//设置背景
//		button.setBackgroundResource(R.drawable.selector_category_bg);
		//去掉左侧默认的圆点
		button.setButtonDrawable(android.R.color.transparent);
		//设置不同状态下文字颜色，通过ColorStateList，对应的selector放在res/color文件目录中，否则没有效果
		button.setTextColor(getResources().getColorStateList(R.color.selector_category_text2));
		button.setGravity(Gravity.CENTER);
		button.setText(text);

		return button;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(mListener != null){
			mListener.click(group, checkedId);
		}
	}

	/**指定监听器*/
	public void setOnClickCategoryListener(OnClickCategoryListener l){
		mListener = l;
	}
	private OnClickCategoryListener mListener;
	/**回掉接口*/
	public interface OnClickCategoryListener{
		/**点击事件发生*/
		void click(RadioGroup group, int checkedId);
	}

}