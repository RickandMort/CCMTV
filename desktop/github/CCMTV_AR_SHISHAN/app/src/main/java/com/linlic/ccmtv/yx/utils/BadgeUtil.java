package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.linlic.ccmtv.yx.service.BadgeIntentService;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by bentley on 2019/3/25.
 * 添加角标的工具类
 */


public class BadgeUtil{
    public static void applyBadgeCount(Context context, int badgeCount) {
        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            // 判断机型是否是小米
            context.startService(new Intent(context, BadgeIntentService.class).putExtra("badgeCount", badgeCount));
        } else {
            ShortcutBadger.applyCount(context, badgeCount);
        }
    }

    public static void removeBadgeCount(Context context) {
        ShortcutBadger.removeCount(context);
    }




}
