package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.util.RxFileUtil;
import com.linlic.ccmtv.yx.utils.DataCleanManager;

import java.io.File;

/**
 * name：清除缓存
 * author：Larry
 * data：2016/6/1.
 */
public class ClearCacheCustomDialog extends Dialog implements
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
     * 确定按钮
     **/
    private Button confirmBtn;
    /**
     * 取消按钮
     **/
    private Button cancelBtn;
    /**
     * Toast时间
     **/
    private TextView tv_showphone;
    IOnClearCacheListener onClearCacheListener;

    public interface IOnClearCacheListener {
        public void ClearCache(String cacheSize);
    }

    public ClearCacheCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 自定义布局的构造方法
     */
    public ClearCacheCustomDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;
    }

    /**
     * 自定义主题及布局的构造方法
     */
    public ClearCacheCustomDialog(Context context, int theme, int resLayout, IOnClearCacheListener onClearCacheListener) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
        this.onClearCacheListener = onClearCacheListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 指定布局
        this.setContentView(layoutRes);

        // 根据id在布局中找到控件对象
        confirmBtn = (Button) findViewById(R.id.confirm_btns);
        cancelBtn = (Button) findViewById(R.id.cancel_btns);
        tv_showphone = (TextView) findViewById(R.id.tv_showphone);
        // 为按钮绑定点击事件监听器
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.confirm_btns:
                // 点击了确认按钮
                // File file = new File(URLConfig.ccmtvapp_basesdcardpath);
                File file = new File(context.getExternalCacheDir().getPath());
                try {
                    /*DataCleanManager.deleteFilesByDirectory(context.getCacheDir());
                    DataCleanManager.cleanInternalCache(context);*/
                    DataCleanManager.cleanApplicationData(context);
                    // 教学视频录制的 缓存目录
                    String appDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ccmtvCache" + File.separator + "videoDir";
                    boolean b = RxFileUtil.deleteDirectory(appDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    onClearCacheListener.ClearCache(DataCleanManager.getCacheSize(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ClearCacheCustomDialog.this.dismiss();
                break;

            case R.id.cancel_btns:
                // 点击了取消按钮
                ClearCacheCustomDialog.this.dismiss();
                break;

            default:
                break;
        }
    }
}
