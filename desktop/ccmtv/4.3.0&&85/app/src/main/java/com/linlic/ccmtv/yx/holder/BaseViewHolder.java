package com.linlic.ccmtv.yx.holder;

import android.util.SparseArray;
import android.view.View;

/**
 * 
 * name:万能的viewHolder author:Tom 2016-1-27下午4:53:26
 */
public class BaseViewHolder {
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
