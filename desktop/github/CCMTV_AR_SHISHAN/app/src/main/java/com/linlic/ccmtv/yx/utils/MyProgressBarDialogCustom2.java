package com.linlic.ccmtv.yx.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.linlic.ccmtv.yx.R;

/**
 * name：自定义一个加载中
 * 第二种思路
 * author：MrSong
 * data：2016/5/11 13:32
 */
public class MyProgressBarDialogCustom2 extends Dialog {
    public static MyProgressBarDialogCustom2 dialog = null;
   public static TextView loading_surpass ;
    public Context context;
    public MyProgressBarDialogCustom2(Context context) {
        super(context);
        this.context = context;
    }

    public MyProgressBarDialogCustom2(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏设置
        /*Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);*/
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param
     * @return
     */
    public static MyProgressBarDialogCustom2 show(Context context) {
        View custom_layout = LayoutInflater.from(context).inflate(R.layout.custom_prog_layout2, null);
         loading_surpass = (TextView) custom_layout.findViewById(R.id.loading_surpass);
//        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.start();



        dialog = new MyProgressBarDialogCustom2(context, R.style.Dialog);
        // 按返回键是否取消
        dialog.setCancelable(false);
        dialog.setContentView(custom_layout);
        dialog.show();
        return dialog;
    }

    public static void DialodDismiss() {
        if (dialog!=null){
            if (dialog.isShowing()){
                dialog.dismiss();
                loading_surpass = null;
            }
        }
    }
}
