package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * name：分享弹出框
 * author：Larry
 * data：2016/6/30 15:10
 */
public class NotSignDialog {

    private AlertDialog dialog;
    private TextView cancelButton;
    private TextView sureButton;

    public NotSignDialog(Context context) {

        try {
            dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            Window window = dialog.getWindow();
            //window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
            window.setContentView(R.layout.not_sign_dialog);
            sureButton = (TextView) window.findViewById(R.id.not_sign_sure);
            cancelButton = (TextView) window.findViewById(R.id.not_sign_cancel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show(){
        if (dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    public void setCancelButtonOnClickListener(OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }

    public void setSureButtonOnClickListener(OnClickListener Listener) {
        sureButton.setOnClickListener(Listener);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}