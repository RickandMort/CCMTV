package com.linlic.ccmtv.yx.utils.permission;

import android.content.Context;

import com.linlic.ccmtv.yx.widget.TipsDialog;

/**
 * Created by bentley on 2018/10/22.
 */

public class PermissionCheckLinstenterImpl implements Permissionutils.OnPermissionCheckListener {
    private Context context;
    private TipsDialog tipsDialog;

    public PermissionCheckLinstenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onCheckFail() {
        if (tipsDialog == null) {
            tipsDialog = new TipsDialog(context, "请求获取访问日历权限").setClicklistener(new TipsDialog.ClickListenerInterface() {

                @Override
                public void doConfirm(Context context) {
                    Permissionutils.getAppSetting(context);
                }

                @Override
                public void doCancel(Context context) {

                }
            }).setCancleButton("取消")
                    .setConfirmButton("去设置");
            tipsDialog.setCanceledOnTouchOutside(false);
        }
        tipsDialog.show();
    }


    public void closeCheckDialog() {
        if (tipsDialog != null && tipsDialog.isShowing()) tipsDialog.dismiss();
    }
}
