package com.linlic.ccmtv.yx.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static com.tencent.open.utils.Global.getPackageName;

/**
 * Created by bentley on 2018/10/19.
 */

public class Permissionutils {

    /**
     * 跳转设置 应用设置界面
     *
     * @return
     */
    public static Intent getAppSetting(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            localIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            localIntent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            localIntent.putExtra("app_package", getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
            }
        }
        context.startActivity(localIntent);

//        Intent localIntent = null;
//        if (Build.VERSION.SDK_INT >= 9) {
//            localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
//            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        } else if (Build.VERSION.SDK_INT <= 8) {
//            localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            localIntent.setAction(Intent.ACTION_VIEW);
//            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
//            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
//            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        context.startActivity(localIntent);
        return localIntent;
    }

    /**
     * 权限检查失败的回调
     */
    public interface OnPermissionCheckListener {
        public void onCheckFail();
    }

    /**
     * 返回时、否拥有改权限
     * @param activity
     * @param permission
     * @return
     */
    public static boolean isOwnPermisson(Activity activity, String permission){
        return ContextCompat.checkSelfPermission(activity,
                permission)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestPermission(Activity activity, String permission,int requestCode) {
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},//需要请求多个权限，可以在这里添加
                        requestCode);

            }
        }
    }

}