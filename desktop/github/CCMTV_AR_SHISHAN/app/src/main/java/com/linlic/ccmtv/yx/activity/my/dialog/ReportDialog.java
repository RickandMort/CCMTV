package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.utils.DisplayUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

/**
 * name：分享弹出框
 * author：Larry
 * data：2016/6/30 15:10
 */
public class ReportDialog {

    private AlertDialog dialog;
    private ImageView mIvReportView;
    private ImageView mIvClose;
    private Context mContext;

    public ReportDialog(Context context, String data) {
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 非常重要：设置对话框弹出的位置
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = DisplayUtil.dip2px(370);
        params.height = DisplayUtil.dip2px(354);
        dialog.getWindow().setAttributes(params);
        window.setBackgroundDrawableResource(R.color.transparent2);
        window.setContentView(R.layout.report_dialog);
        mIvReportView = (ImageView) window.findViewById(R.id.iv_report_dialog);
        mIvClose = (ImageView) window.findViewById(R.id.iv_close);
        Log.d("mason","======"+data);
        mIvReportView.setTag(data);
        mIvReportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject json = new JSONObject(v.getTag().toString());
                    Intent intent1 = null;
                    if (json.getString("urlflg").equals("1")) {
                        intent1 = new Intent(mContext, ActivityWebActivity.class);
                        intent1.putExtra("title", json.getString("activitytitle"));
                        if (json.has("summary")){
                            if ("1".equals(json.getString("summary"))){
                                //1为年终总结，拼接uid
                                intent1.putExtra("aid", json.getString("activityurl")+"?uid="+ SharedPreferencesTools.getUidONnull(mContext));
                            }
                        } else {
                            intent1.putExtra("aid", json.getString("activityurl"));
                        }
                        mContext.startActivity(intent1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    dismiss();
                }
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    public void show(){
        dialog.show();
    }
}