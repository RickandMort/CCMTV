package com.linlic.ccmtv.yx.activity.subscribe.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

public class IllnessItem extends RelativeLayout {
	private LinearLayout relativeLayout1;
	private CheckBox dialog_checkbox;
	private TextView dialog_illness;
	private Boolean isSelected = false;
	private MyItemClicked myItemClicked;

	private String illness;

	public IllnessItem(Context context) {
		this(context, null);
	}

	public IllnessItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.illness_item_layout, this);
		relativeLayout1 = (LinearLayout) findViewById(R.id.relativeLayout1);
		dialog_checkbox = (CheckBox) findViewById(R.id.dialog_checkbox);
		dialog_illness = (TextView) findViewById(R.id.dialog_illness);
		relativeLayout1.setOnClickListener(new MyOnClick());
	}

	public CheckBox getDialog_checkbox() {
		return dialog_checkbox;
	}

	public void setDialog_checkbox(CheckBox dialog_checkbox) {
		this.dialog_checkbox = dialog_checkbox;
	}

	public String getDialog_illness() {
		return illness;
	}

	public void setDialog_illness(String illness) {
		this.illness = illness;
		dialog_illness.setText(illness);
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
		/*if (isSelected) {
			relativeLayout1.setBackgroundResource(R.drawable.selectedborder);
		} else {
			relativeLayout1.setBackgroundResource(R.drawable.border);
		}*/
	}

	private class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
		/*	if (!isSelected) {
				relativeLayout1.setBackgroundResource(R.drawable.selectedborder);
				myItemClicked.myItemClicked();
			} else {
				relativeLayout1.setBackgroundResource(R.drawable.border);
				myItemClicked.myItemClicked();
			}*/
			myItemClicked.myItemClicked();
		}
	}

	public interface MyItemClicked {
		public void myItemClicked();
	}

	public void setMyItemClickedListener(MyItemClicked myItemClicked) {
		this.myItemClicked = myItemClicked;
	}
}
