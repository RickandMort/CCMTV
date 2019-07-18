package com.linlic.ccmtv.yx.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;

/**
 * name：加载框
 * author：Larry
 * data：2016/4/20.
 */
public class LoadingDialog {

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_prog_layout, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view
                .findViewById(R.id.dialog_view);

        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.loadingImageView);
        // 页面中显示文本
       // TextView tipText = (TextView) view.findViewById(R.id.tipTextView);
        AnimationDrawable animationDrawable = (AnimationDrawable) img.getBackground();
        animationDrawable.start();
        // 加载动画，动画用户使img图片不停的旋转

        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }
}