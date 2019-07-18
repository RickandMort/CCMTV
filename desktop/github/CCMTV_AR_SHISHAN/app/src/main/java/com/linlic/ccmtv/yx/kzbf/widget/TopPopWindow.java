package com.linlic.ccmtv.yx.kzbf.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.linlic.ccmtv.yx.R;

/**
 * Created Niklaus yu on 2017/12/20.
 */

public class TopPopWindow extends PopupWindow {
    private View mView;
    private LinearLayout ll_popmenu_msg;                //消息
    private LinearLayout ll_popmenu_fouce;             //我的关注
    private LinearLayout ll_popmenu_collection;       //我的收藏
    private LinearLayout ll_popmenu_integrated_mall; //积分商城
    private LinearLayout ll_popmenu_download;        //我的下载

    public TopPopWindow(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2) {
        /*LayoutInflater inflater = (LayoutInflater) paramActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_kzbf_menu, null);*/
        mView = LayoutInflater.from(paramActivity).inflate(R.layout.pop_kzbf_menu, null);
        ll_popmenu_msg = (LinearLayout) mView.findViewById(R.id.ll_popmenu_msg);
        ll_popmenu_fouce = (LinearLayout) mView.findViewById(R.id.ll_popmenu_fouce);
        ll_popmenu_collection = (LinearLayout) mView.findViewById(R.id.ll_popmenu_collection);
        ll_popmenu_integrated_mall = (LinearLayout) mView.findViewById(R.id.ll_popmenu_integrated_mall);
        ll_popmenu_download = (LinearLayout) mView.findViewById(R.id.ll_popmenu_download);
        if (paramOnClickListener != null) {
            //设置点击监听
            ll_popmenu_msg.setOnClickListener(paramOnClickListener);
            ll_popmenu_fouce.setOnClickListener(paramOnClickListener);
            ll_popmenu_collection.setOnClickListener(paramOnClickListener);
            ll_popmenu_integrated_mall.setOnClickListener(paramOnClickListener);
            ll_popmenu_download.setOnClickListener(paramOnClickListener);
            setContentView(mView);
            //设置宽度
            setWidth(paramInt1);
            //设置高度
            setHeight(paramInt2);
            //设置显示隐藏动画
            setAnimationStyle(R.style.ActionSheetDialogAnimation);
            //设置背景透明
            setBackgroundDrawable(new ColorDrawable(0));
        }
    }
}
