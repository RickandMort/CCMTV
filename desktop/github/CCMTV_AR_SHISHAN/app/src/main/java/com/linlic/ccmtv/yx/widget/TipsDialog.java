package com.linlic.ccmtv.yx.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * Created by bentley on 2018/10/19.
 */

public class TipsDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String message;
    private TextView tvCancle;
    private TextView tvConfirm;
    private TextView tvTips;
    private String cancle;
    private String confirm;
    private ImageView tipsIcon;
    private FrameLayout fl_dialog;
    private LinearLayout ll_tips;

    public TipsDialog(@NonNull Context context, String message) {
        super(context, R.style.myupgrade);
        this.context = context;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tips, null);
        setContentView(view);

        tvCancle = findViewById(R.id.tv_cancle);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTips = findViewById(R.id.tv_tips_message);
        tipsIcon = findViewById(R.id.iv_tips_icon);

        tvTips.setText(message);
        tvCancle.setText(cancle);
        tvConfirm.setText(confirm);
        tvCancle.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        measureHeight();
    }

    private void measureHeight() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height = (int) (d.heightPixels * 0.3); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        fl_dialog = findViewById(R.id.fl_dialog);
        ll_tips = findViewById(R.id.ll_tips);
        fl_dialog.post(new Runnable() {
            @Override
            public void run() {
                int height = fl_dialog.getHeight() - fl_dialog.getPaddingBottom() - fl_dialog.getPaddingTop();
                int width = fl_dialog.getWidth() - fl_dialog.getPaddingLeft() - fl_dialog.getPaddingRight();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(height / 2, height / 2);
                params.setMargins(width / 2 - height / 4, 0, 0, 0);
                tipsIcon.setLayoutParams(params);

                FrameLayout.LayoutParams llparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                llparams.setMargins(0, height / 4, 0, 0);
                ll_tips.setLayoutParams(llparams);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                if (clickListenerInterface != null) clickListenerInterface.doCancel(context);
                dismiss();
                break;
            case R.id.tv_confirm:
                if (clickListenerInterface != null) clickListenerInterface.doConfirm(context);
                dismiss();
                break;
            default:
                break;
        }
    }

    private ClickListenerInterface clickListenerInterface;

    public interface ClickListenerInterface {

        public void doConfirm(Context context);

        public void doCancel(Context context);
    }


    public TipsDialog setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
        return this;
    }

    public TipsDialog setCancleButton(String cancle) {
        this.cancle = cancle;
//        tvCancle.setText(cancle);
        return this;
    }

    public TipsDialog setConfirmButton(String confirm) {
        this.confirm = confirm;
//        tvConfirm.setText(confirm);
        return this;
    }

    public TipsDialog setTipsMessage(String message) {
        this.message = message;
//        tvCancle.setText(message);
        return this;
    }
}
