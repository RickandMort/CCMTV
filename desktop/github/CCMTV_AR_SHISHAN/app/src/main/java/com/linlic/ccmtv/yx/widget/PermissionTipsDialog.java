package com.linlic.ccmtv.yx.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * <pre>
 *     author : 戈传光
 *     e-mail : 1944633835@qq.com
 *     time   : 2019/07/01
 *     desc   :
 *     version:
 * </pre>
 */

public class PermissionTipsDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_cancel;
    private TextView tv_confirm;


    private String title;
    private String content;
    private String confirm;


    Context mContext;

    public static final int CLICK_CANCEL = 123;
    public static final int CLICK_CONFIRM = 124;

    private DialogClickListener mDialogClickListener;


    public PermissionTipsDialog(@NonNull Context context, Builder builder) {
        super(context);
        this.title = builder.title;
        this.content = builder.content;
        this.confirm = builder.confirm;
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permisson_tips);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);

        tv_title.setText(title);
        tv_content.setText(content);
        tv_confirm.setText(confirm);


        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (mDialogClickListener != null)
                    mDialogClickListener.onClick(CLICK_CANCEL);
                break;

            case R.id.tv_confirm:
                if (mDialogClickListener != null)
                    mDialogClickListener.onClick(CLICK_CONFIRM);
                break;
        }
    }

    public static class Builder {

        private String title;
        private String content;
        private String confirm;

        private Context context;

        public Builder init(Context context) {
            this.context = context;
            return this;
        }

        public Builder addTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder addContent(String content) {
            this.content = content;
            return this;
        }

        public Builder addConfirm(String confirm) {
            this.confirm = confirm;
            return this;
        }

        public PermissionTipsDialog builder() {
            return new PermissionTipsDialog(context, this);
        }
    }

    public void setClickListener(DialogClickListener dialogClickListener) {
        mDialogClickListener = dialogClickListener;
    }

    public interface DialogClickListener {
        void onClick(int click);
    }

}
