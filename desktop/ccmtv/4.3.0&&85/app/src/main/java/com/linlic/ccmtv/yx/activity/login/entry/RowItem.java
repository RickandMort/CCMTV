package com.linlic.ccmtv.yx.activity.login.entry;

import android.graphics.Bitmap;
/*
* AR视频所需
* */

public class RowItem {
	private Bitmap bitmapImage;


	public RowItem(Bitmap bitmap) {
		this.bitmapImage = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmapImage;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmapImage = bitmap;
	}

}