package com.linlic.ccmtv.yx.enums;

import java.security.Permission;

/**
 * <pre>
 *     author : 戈传光
 *     e-mail : 1944633835@qq.com
 *     time   : 2019/07/03
 *     desc   :
 *     version:
 * </pre>
 */

public enum PermissionEnum {


    WRITE_EXTERNAL_STORAGE("android.permission.WRITE_EXTERNAL_STORAGE", "存储权限"),
    CAMERA("android.permission.CAMERA", "相机权限"),
    RECORD_AUDIO("android.permission.RECORD_AUDIO", "录音权限"),;


    PermissionEnum(String permission, String description) {
        this.permission = permission;
        this.description = description;
    }

    public String permission;
    public String description;


    public static String findDescByPermission(String permission) {
        PermissionEnum[] values = PermissionEnum.values();
        for (PermissionEnum value : values) {
            if (value.permission.equals(permission)) {

                return value.description;
            }
        }
        return "";
    }
}
