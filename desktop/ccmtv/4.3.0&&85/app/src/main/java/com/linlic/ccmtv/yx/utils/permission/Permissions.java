package com.linlic.ccmtv.yx.utils.permission;

import android.Manifest;

/**
 * Created by bentley on 2018/10/19.
 */

public class Permissions {
    /**
     * 使用系统日历权限
     */
    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String[] CALENDAR_PERMISSIONS = {WRITE_CALENDAR, READ_CALENDAR};
}
