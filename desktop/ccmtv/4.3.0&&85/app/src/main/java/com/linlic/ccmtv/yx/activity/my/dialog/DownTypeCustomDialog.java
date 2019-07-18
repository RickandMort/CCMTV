package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * name：下载类型选择
 * author：Larry
 * data：2016/6/1.
 */
public class DownTypeCustomDialog extends Dialog implements
        View.OnClickListener {

    /**
     * 布局文件
     **/
    int layoutRes;
    /**
     * 上下文对象
     **/
    Context context;
    /**
     * 取消按钮
     **/
    private Button cancelBtn;
    private TextView tv_gaoqingtype, tv_liuchangtype, tv_biaoqingtype;
    OnChooseDownTypeListener onChooseDownTypeListener;

    public interface OnChooseDownTypeListener {
        public void ChooseDownType(String type);

    }


    public DownTypeCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义布局的构造方法
     */
    public DownTypeCustomDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;
    }

    /**
     * 自定义主题及布局的构造方法
     */
    public DownTypeCustomDialog(final Context context, int theme, int resLayout) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
    }

    /**
     * 自定义主题及布局的构造方法
     */
    public DownTypeCustomDialog(final Context context, int theme, int resLayout, OnChooseDownTypeListener onChooseDownTypeListener) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
        this.onChooseDownTypeListener = onChooseDownTypeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 指定布局
        this.setContentView(layoutRes);

        // 根据id在布局中找到控件对象
        cancelBtn = (Button) findViewById(R.id.cancel_btns);
        tv_gaoqingtype = (TextView) findViewById(R.id.tv_gaoqingtype);
        tv_liuchangtype = (TextView) findViewById(R.id.tv_liuchangtype);
        tv_biaoqingtype = (TextView) findViewById(R.id.tv_biaoqingtype);
        // 设置按钮的文本颜色
        cancelBtn.setTextColor(0xff1E90FF);
        // 为按钮绑定点击事件监听器
        cancelBtn.setOnClickListener(this);
        tv_gaoqingtype.setOnClickListener(this);
        tv_liuchangtype.setOnClickListener(this);
        tv_biaoqingtype.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_gaoqingtype:
                onChooseDownTypeListener.ChooseDownType("高清");
                DownTypeCustomDialog.this.dismiss();
                break;
            case R.id.tv_liuchangtype:
                onChooseDownTypeListener.ChooseDownType("流畅");
                DownTypeCustomDialog.this.dismiss();
                break;
            case R.id.tv_biaoqingtype:
                onChooseDownTypeListener.ChooseDownType("标清");
                DownTypeCustomDialog.this.dismiss();
                break;
            case R.id.cancel_btns:
                // 点击了取消按钮
                DownTypeCustomDialog.this.dismiss();
                break;
            default:
                break;
        }
    }

}
