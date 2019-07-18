package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerTextView extends TextView implements Runnable {
	// 时间变量
	private int hour, minute, second;
	// 当前计时器是否运行
	private boolean isRun = false;

	public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public TimerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerTextView(Context context) {
		super(context);
	}

	/**
	 * 将倒计时时间毫秒数转换为自身变量
	 * 
	 * @param time
	 *            时间间隔毫秒数
	 */
	public void setTimes(long time) {
		//将毫秒数转化为时间
				this.second = (int) (time / 1000) % 60;
				this.minute = (int) (time / (60 * 1000) % 60);
//				this.hour = (int) (time / (60 * 60 * 1000) % 24);
	}

	/**
	 * 显示当前时间
	 * 
	 * @return
	 */
	public String showTime() {
		StringBuilder time = new StringBuilder();
//		time.append(hour);
//		time.append("时");
		if (minute < 10) {
			time.append("0").append(minute);
		}else {
			time.append(minute);
		}
		time.append(":");
		if(second < 10) {
			time.append("0").append(second);
		}else {
			time.append(second);
		}
		return time.toString();
//		String str = hour + "小时" + minute + "分钟" + second +"秒";
//		return str;
	}

	/**
	 * 实现倒计时
	 */
	private void countdown() {
		if (second == 0) {
			if (minute == 0) {
//				if (hour == 0) {
						//当时间归零时停止倒计时
						isRun = false;
						return;
//				} else {
//					hour--;
//				}
//				minute = 59;
			} else {
				minute--;
			}
			second = 60;
		}
		second--;
	}

	public boolean isRun() {
		return isRun;
	}

	/**
	 * 开始计时
	 */
	public void start() {
		isRun = true;
		run();
	}

	/**
	 * 结束计时
	 */
	public void stop() {
		isRun = false;
	}

	/**
	 * 实现计时循环
	 */
	@Override
	public void run() {
		if (isRun) {
			// Log.d(TAG, "Run");
			countdown();
			this.setText(showTime());
			postDelayed(this, 1000);
		} else {
			removeCallbacks(this);
		}
	}

	public boolean getTimeText() {
		if (minute == 0) {
			if (second == 0){
				return true;
			}
		}
		return false;
	}

}
