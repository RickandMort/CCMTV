package com.linlic.ccmtv.yx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;

public class SharedPreferencesTools {


    /**
     * name：保存用户ID
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param uid
     */
    public static void saveUid(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.commit();
    }

    /**
     * name： 提醒
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param uid
     */
    public static void savecodezd(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("codezd", uid);
        editor.commit();
    }

    /**
     * name：获取提醒
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getcodezd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String Str_personalmoney = sp.getString("codezd", "");
        return Str_personalmoney;
    }

    /***
     * 年度报告弹窗是否显示(接口控制)
     * @param context
     * @return
     */
    public static boolean isReportDialogShow(Context context){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        boolean isShow = sp.getBoolean("report_dialog", false);
        return isShow;
    }

    public static void saveIsReportDialogShow(Context context, boolean isShow){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("report_dialog", isShow);
        editor.commit();
    }

    /***
     * 年度报告弹窗是否显示过
     * @param context
     * @return
     */
    public static boolean isReportDialogHasShow(Context context){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        boolean isShow = sp.getBoolean("report_dialog_has_show", true);
        return isShow;
    }

    public static void saveIsReportDialogHasShow(Context context, boolean isShow){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("report_dialog_has_show", isShow);
        editor.commit();
    }

    /**
     * name： 提醒
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param uid
     */
    public static void savepractice_codezd(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("practice_codezd", uid);
        editor.commit();
    }

    /**
     * name：获取提醒
     * author: tom
     * 时间：2018-8-24 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getpractice_codezd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String Str_personalmoney = sp.getString("practice_codezd", "");
        return Str_personalmoney;
    }

    /**
     * name：我的收银台
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param Str_personalmoney
     */
    public static void savePersonalmoney(Context context, String Str_personalmoney) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Str_personalmoney", Str_personalmoney);
        editor.commit();
    }

    /**
     * name：我的收银台
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getPersonalmoney(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String Str_personalmoney = sp.getString("Str_personalmoney", "");
        if (Str_personalmoney == null || Str_personalmoney.equals("")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            Toast.makeText(context, "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
            return "";
        }
        return Str_personalmoney;
    }

    /**
     * name：获取用户ID
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getUid(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        if (uid == null || uid.equals("")) {
            context.startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
            LocalApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            });
            return "";
        }
        return uid;
    }

    public static String getUidToLoginClose(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        if (uid == null || uid.equals("")) {
            context.startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
            Toast.makeText(context, "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
            return "";
        }
        return uid;
    }

    public static String getUids(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        if (uid == null || uid.equals("")) {
            return null;
        }
        return uid;
    }

    /**
     * name：获取用户ID
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getUidONnull(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        if (uid == null || uid.equals("")) {

            return "";
        }
        return uid;
    }

    /**
     * name：保存用户名
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param UserName
     */
    public static void saveUserName(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserName", UserName);
        editor.commit();
    }

    /**
     * name：获取用户名
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String UserName = sp.getString("UserName", "");
        if (UserName == null || UserName.equals("")) {
            context.startActivity(new Intent(context, LoginActivity.class));
            Toast.makeText(context, "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
            return "";
        }
        return UserName;
    }

    /**
     * name：保存是否点击记住密码
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveIsMemory(Context context, String userName, String passWord, String isMemory) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("userName", userName);
        editor.putString("passWord", Base64utils.getBase64(Base64utils.getBase64(passWord)));
        editor.putString("isMemory", isMemory);
        editor.commit();
    }

    /**
     * name：读取是否点击记住密码
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static String getIsMemory(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("isMemory", LoginActivity.NO);
    }


    /**
     * name：保存视频ID
     * author：MrSong
     * data：2016/4/5 13:44
     */
    /*public static void saveVideoId(Context context, String videoId) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("aid", videoId);
        editor.commit();
    }*/


    /**
     * name：获取视频ID
     * author：MrSong
     * data：2016/4/5 13:44
     */
   /* public static String getVideoId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("aid", "");
    }*/


    /**
     * name：保存Vip状态
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveVipFlag(Context context, int VipFlag) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("vip", VipFlag);
        editor.commit();
    }

    /**
     * name：读取Vip状态
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static int getVipFlag(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getInt("vip", 0);
    }

    /**
     * name：保存头像
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveStricon(Context context, String Str_icon) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("icon", Str_icon);
        editor.commit();
    }


    /**
     * name：ICON 头像
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static String getStricon(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("icon", "");
    }

    /**
     * name：保存科室
     * author：Larry
     * data：2016/5/23 18:13
     */
    public static void saveStrKeShi(Context context, String Str_bigkeshi, String Str_smallkeshi) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("bigkeshi", Str_bigkeshi);
        editor.putString("smallkeshi", Str_smallkeshi);
        editor.commit();
    }

    /**
     * name：读取科室
     * author：Larry
     * data：2016/5/23 18:14
     */
    public static String getStrBigKeShi(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("bigkeshi", "");
    }

    public static String getStrSmallKeShi(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("smallkeshi", "");
    }

    /**
     * name：保存医院名字
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveStrHos(Context context, String Str_hos) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Str_hos", Str_hos);
        editor.commit();
    }


    /**
     * name：读取医院名字
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static String getStrhos(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("Str_hos", "");
    }


    /**
     * name：保存密码
     * author：Larry
     * data：2016/4/20 18:38
     */
    public static void savePassword(Context context, String Str_Password) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Password", Base64utils.getBase64(Base64utils.getBase64(Str_Password)));
        editor.commit();
    }

    /**
     * name：获取搜索记录序号
     * author：tom
     * data：2017/4/20 10:56
     */
    public static int getHot_search_id(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        int id = sp.getInt("hot_search_id", 1);
        saveHot_search_id(context, id);
        return id;
    }


    /**
     * name：保存搜索记录序号
     * author：tom
     * data：2017/4/20 18:38
     */
    public static void saveHot_search_id(Context context, int id) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("hot_search_id", id);
        editor.commit();
    }

    /**
     * name：保存是否是快捷登录
     * author：Larry
     * data：2016/4/20 18:38
     */
    public static void saveWhether_the_quick_login(Context context, String Str_Password) {
        SharedPreferences sp = context.getSharedPreferences("whether_the_quick_login", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("whether_the_quick_login", Base64utils.getBase64(Base64utils.getBase64(Str_Password)));
        editor.commit();
    }

    /**
     * name：是否是快捷登录
     * author：Larry
     * data：2016/4/20 18:39
     */
    public static String getWhether_the_quick_login(Context context) {
        SharedPreferences sp = context.getSharedPreferences("whether_the_quick_login", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("whether_the_quick_login", "")));
    }

    /**
     * name：读取密码
     * author：Larry
     * data：2016/4/20 18:39
     */
    public static String getPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("Password", "")));
    }

    /**
     * name：保存saveVipEndTime
     * author：Larry
     * data：2016/4/20 18:38
     */
    public static void saveVipEndTime(Context context, String Str_Password) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("VipEndTime", Base64utils.getBase64(Base64utils.getBase64(Str_Password)));
        editor.commit();
    }

    /**
     * name：读取saveVipEndTime
     * author：Larry
     * data：2016/4/20 18:39
     */
    public static String getVipEndTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("VipEndTime", "")));
    }

    /**
     * name：保存设置下载清晰度
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveClarity(Context context, int type) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("clarity", type);
        editor.commit();
    }

    /**
     * name：读取设置下载清晰度
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static int getClarity(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getInt("clarity", 0);
    }

    /**
     * name：保存设置只能wifi下载
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveOnlyWifiDown(Context context, boolean iswifi) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("onlywifi", iswifi);
        editor.commit();
    }

    /**
     * name：读取是否只能wifi下载
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static Boolean getOnlyWifiDown(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("onlywifi", true);
    }

    /**
     * name：保存下载路径
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static void saveCachePath(Context context, String CachePath) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("CachePath", CachePath);
        editor.commit();
    }

    /**
     * name：获取下载路径
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static String getCachePath(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String homePageData = sp.getString("CachePath", URLConfig.ccmtvapp_basesdcardpath);
        return homePageData;
    }

    /**
     * name：保存下载路径
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static void savememberUserDataPath(Context context, String memberUserData) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("memberUserData", memberUserData);
        editor.commit();
    }

    /**
     * name：获取下载路径
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static String getmemberUserDataPath(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String homePageData = sp.getString("memberUserData", "");
        return homePageData;
    }

    /**
     * name：保存非wifi播放是否每次提醒
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static void saveIsEveryReminder(Context context, Boolean isEvery) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isEvery", isEvery);
        editor.commit();
    }

    /**
     * name：保存非wifi播放是否只提醒一次
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static void saveIsOneReminder(Context context, Boolean IsOne) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("IsOne", IsOne);
        editor.commit();
    }

    /**
     * name：获取非wifi播放是否只提醒一次
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static Boolean getIsOneReminder(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("IsOne", false);
    }

    /**
     * name：获取非wifi播放是否每次提醒
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static Boolean getIsEveryReminder(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("isEvery", true);
    }


    public static void saveIsFrist(Context context, Boolean isFirst) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("First", isFirst);
        editor.commit();
    }

    /**
     * name：获取非wifi播放是否只提醒一次
     * author：Larry
     * data：2016/6/3 12:25
     */
    public static Boolean getIsFrist(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("First", true);
    }

    /**
     * name:保存积分数量
     * author：Larry
     * data：2016/7/18 14:40
     */
    public static void saveIntegral(Context context, String Integral) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Integral", Integral);
        editor.commit();
    }

    /**
     * name：获取积分数量
     * author：Larry
     * data：2016/7/18 14:36
     */

    public static String getIntegral(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("Integral", "0");
    }


    /**
     * name:保存APP配置信息
     * author：Larry
     * data：2016/7/18 14:40
     */
    public static void saveAppConfig(Context context, String config) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("config", config);
        editor.commit();
    }

    /**
     * name：获取积分数量
     * author：Larry
     * data：2016/7/18 14:36
     */

    public static String getAppConfig(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getString("config", "");
    }

    /**
     * name：保存是否推送
     * author：Larry
     * data：2016/9/6
     */

    public static void saveIsPush(Context context, Boolean IsPush) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("IsPush", IsPush);
        editor.commit();
    }

    /**
     * name：获取是否推送
     * author：Larry
     * data：2016/9/6
     */
    public static Boolean getIsPush(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("IsPush", true);
    }


    /**
     * name：保存搜索页选中的值
     * author：Larry
     * data：2016/4/20 10:50
     */
    public static void saveCheckedId(Context context, int checkedId) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("checkedId", checkedId);
        editor.commit();
    }

    /**
     * name：讀取搜索页选中的值
     * author：Larry
     * data：2016/4/20 10:56
     */
    public static int getCheckedId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getInt("checkedId", 100);
    }


    /**
     * name：保存是否是快捷登录
     * author：Larry
     * data：2016/4/20 18:38
     */
    public static void saveMobphone(Context context, String Str_Password) {
        SharedPreferences sp = context.getSharedPreferences("mobphone", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("mobphone", Base64utils.getBase64(Base64utils.getBase64(Str_Password)));
        editor.commit();
    }

    /**
     * name：是否是快捷登录
     * author：Larry
     * data：2016/4/20 18:39
     */
    public static String getMobphone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("mobphone", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("mobphone", "")));
    }


    /**
     * name：保存同步AR资源的时间
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param UserName
     */
    public static void saveARAssertTime(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("ARAssertTime", UserName);
        editor.commit();
    }

    /**
     * name：获取同步AR资源的时间
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getARAssertTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String UserName = sp.getString("ARAssertTime", "");

        return UserName;
    }

    /**
     * name：保存 是否 是医考用户
     * author: tom
     * 时间：2017-9-9 下午6:08:07
     *
     * @param context
     * @param UserName
     */
    public static void saveIsdocexam(Context context, Boolean UserName) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("isdocexam", UserName);
        editor.commit();
    }

    /**
     * name：获取 是否 是医考用户
     * author: Mr.song
     * 时间：2017-9-9 下午6:08:07
     *
     * @param context
     * @return
     */
    public static Boolean getIsdocexam(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return sp.getBoolean("isdocexam", false);
    }

    /**
     * name：保存学习任务任务类型状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static void saveType(Context context, String type) {
        SharedPreferences sp = context.getSharedPreferences("type", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("type", Base64utils.getBase64(Base64utils.getBase64(type)));
        editor.commit();
    }

    public static void saveType1(Context context, String type) {
        SharedPreferences sp = context.getSharedPreferences("type1", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("type1", Base64utils.getBase64(Base64utils.getBase64(type)));
        editor.commit();
    }

    public static void saveType2(Context context, String type) {
        SharedPreferences sp = context.getSharedPreferences("type2", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("type2", Base64utils.getBase64(Base64utils.getBase64(type)));
        editor.commit();
    }

    /**
     * name：获取学习任务任务类型状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static String getType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("type", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("type", "")));
    }

    public static String getType1(Context context) {
        SharedPreferences sp = context.getSharedPreferences("type1", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("type1", "")));
    }

    public static String getType2(Context context) {
        SharedPreferences sp = context.getSharedPreferences("type2", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("type2", "")));
    }

    /**
     * name：保存学习任务任务合格状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static void saveQualified(Context context, String type) {
        SharedPreferences sp = context.getSharedPreferences("qualified", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("qualified", Base64utils.getBase64(Base64utils.getBase64(type)));
        editor.commit();
    }

    /**
     * name：获取学习任务任务合格状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static String getQualified(Context context) {
        SharedPreferences sp = context.getSharedPreferences("qualified", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("qualified", "")));
    }

    /**
     * name：保存本地上次观看记录时长
     * author：Niklaus
     * data：2017/11/21
     */
    public static void saveLastTime(Context context, String time) {
        SharedPreferences sp = context.getSharedPreferences("lasttime", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("lasttime", Base64utils.getBase64(Base64utils.getBase64(time)));
        editor.commit();
    }

    /**
     * name：获取本地上次观看记录时长
     * author：Niklaus
     * data：2017/11/21
     */
    public static String getLastTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences("lasttime", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("lasttime", "")));
    }

    /**
     * name：视频已下载标志
     * author：Niklaus
     * data：2017/11/21
     */
    public static void setDownloadState(Context context, String state) {
        SharedPreferences sp = context.getSharedPreferences("downloadstate", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("downloadstate", Base64utils.getBase64(Base64utils.getBase64(state)));
        editor.commit();
    }

    /**
     * name：获取视频已下载标志
     * author：Niklaus
     * data：2017/11/21
     */
    public static String getDownloadState(Context context) {
        SharedPreferences sp = context.getSharedPreferences("downloadstate", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("downloadstate", "")));
    }

    /**
     * name：保存个人信息String
     * author：Niklaus
     * data：2017/11/6
     */
    public static void savePerfectInformation(Context context, String perfectInformation) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("perfectInformation", Base64utils.getBase64(Base64utils.getBase64(perfectInformation)));
        editor.commit();
    }

    /**
     * name：获取学习任务任务合格状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static String getPerfectInformation(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("perfectInformation", "")));
    }

    /**
     * name：是否签到
     * author：Niklaus
     * data：2017/11/6
     */
    public static void savecurrentDayMonyFlg(Context context, String perfectInformation) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("currentDayMonyFlg", Base64utils.getBase64(Base64utils.getBase64(perfectInformation)));
        editor.commit();
    }

    /**
     * name：获取学习任务任务合格状态
     * author：Niklaus
     * data：2017/11/6
     */
    public static String getcurrentDayMonyFlg(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        return Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("currentDayMonyFlg", "")));
    }

    /**
     * name：保存签到数
     * author: Niklaus
     *
     * @param context
     * @param num
     */
    public static void saveSign(Context context, String num) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("num", num);
        editor.commit();
    }

    /**
     * name：获取签到数
     * author: Niklaus
     *
     * @param context
     * @return
     */
    public static String getSign(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String num = sp.getString("num", "");
        return num;
    }

    /**
     * name：保存可签到数
     * author: Niklaus
     *
     * @param context
     * @param num
     */
    public static void saveSign_in_num(Context context, String num) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("sign_in_num", num);
        editor.commit();
    }

    /**
     * name：获取可签到数
     * author: Niklaus
     *
     * @param context
     * @return
     */
    public static String getSign_in_num(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String num = sp.getString("sign_in_num", "");
        return num;
    }

    /**
     * name：保存签到时间json
     * author: bentley
     *
     * @param context
     * @param json
     */
    public static void saveSign_Time_Json(Context context, String json) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("sign_time_json", json);
        editor.commit();
    }

    /**
     * name：获取签到时间json
     * author: bentley
     *
     * @param context
     * @return
     */
    public static String getSign_Time_Json(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String json = sp.getString("sign_time_json", "");
        return json;
    }

    /**
     * name：保存cookies
     * author: bentley
     *
     * @param context
     * @param cookies
     */
    public static void saveCookies(Context context, String cookies) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("cookies", cookies);
        editor.commit();
    }

    /**
     * name：获取cookies
     * author: bentley
     *
     * @param context
     * @return
     */
    public static String getCookies(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String cookies = sp.getString("cookies", "");
        return cookies;
    }

    /**
     * name：保存用户身份类型
     * author: tom
     * 3 医考  1规培生 2医院正式员工
     *
     * @param context
     * @param cookies
     */
    public static void saveGp_type(Context context, String cookies) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("gp_type", cookies);
        editor.commit();
    }

    /**
     * name：用户身份类型
     * author: bentley
     * 3 医考  1规培生 2医院正式员工
     *
     *
     * @param context
     * @return
     */
    public static String getGp_type(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String cookies = sp.getString("gp_type", "");
        return cookies;
    }

    /**
     * name：保存用户身份列表
     * author: tom
     *
     * @param context
     * @param cookies
     */
    public static void saveRoleList(Context context, String cookies) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("roleList", cookies);
        editor.commit();
    }

    /**
     * name：用户身份列表
     * author: bentley
     *
     * @param context
     * @return
     */
    public static String getRoleList(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String cookies = sp.getString("roleList", "");
        return cookies;
    }

    /**
     * name：保存用户当前身份
     * author: tom
     *
     * @param context
     * @param cookies
     */
    public static void saveRole(Context context, String cookies) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("role", cookies);
        editor.commit();
    }

    /**
     * name：用户当前身份
     * author: bentley
     *
     * @param context
     * @return
     */
    public static String getRole(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String cookies = sp.getString("role", "");
        return cookies;
    }

    /**
     * name：保存用户姓名
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param UserTrueName
     */
    public static void saveUserTrueName(Context context, String UserTrueName) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserTrueName", UserTrueName);
        editor.commit();
    }

    /**
     * name：获取用户姓名
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getUserTrueName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String UserTrueName = sp.getString("UserTrueName", "");
        return UserTrueName;
    }

    /**
     * name：保存规培用户科室
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param UserIdentity
     */
    public static void saveUserIdentity(Context context, String UserIdentity) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("UserIdentity", UserIdentity);
        editor.commit();
    }

    /**
     * name：获取规培用户科室
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     *
     * @param context
     * @return
     */
    public static String getUserIdentity(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String UserIdentity = sp.getString("UserIdentity", "");
        return UserIdentity;
    }


    /**
     * name：保存是否显示引导图
     * author: Mr.song
     * 时间：2016-3-9 下午6:08:07
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void saveIsShowGuidePic(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("IsShowGuidePic", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取是否显示引导图
     * author: Mr.song
     * 时间：2016-3-9 下午6:18:25
     * 1为引导页开启   0为关闭引导页
     *
     * @param context
     * @return
     */
    public static String getIsShowGuidePic(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("IsShowGuidePic", "");
        return isShowGuidePic;
    }

    /**
     * name：保存是否显示引导图  一屏一题提醒 1
     *
     *
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void saveIsShowPattern(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("IsShowPattern", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取是否显示引导图 一屏一题提醒 1
     *
     *
     *
     *
     * @param context
     * @return
     */
    public static String getIsShowPattern(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("IsShowPattern", "");
        return isShowGuidePic;
    }
    /**
     * name：保存是否显示引导图  一屏一题提醒 2
     *
     *
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void saveIsShowPattern2(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("IsShowPattern2", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取是否显示引导图 一屏一题提醒 2
     *
     *
     *
     *
     * @param context
     * @return
     */
    public static String getIsShowPattern2(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("IsShowPattern2", "");
        return isShowGuidePic;
    }

    /**
     * name：保存教学活动状态
     * author: tom.li
     * 时间：2018-9-26 下午6:08:07
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void saveEvent_details_status(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Event_details_status", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取教学活动状态
     * author:tom.li
     * 时间：2018-9-26 下午6:08:07
     * 避免列表重复刷新
     *
     * @param context
     * @return
     */
    public static String getEvent_details_status(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("Event_details_status", "");
        return isShowGuidePic;
    }

    /**
     * name：保存推送ID
     * author: tom.li
     * 时间：2018-9-26 下午6:08:07
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void savePush_id(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Push_id", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取推送ID
     * author:tom.li
     * 时间：2018-9-26 下午6:08:07
     * 避免列表重复刷新
     *
     * @param context
     * @return
     */
    public static String getPush_id(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("Push_id", "");
        return isShowGuidePic;
    }
    /**
     * name：保存小米推送ID
     * author: tom.li
     * 时间：2018-9-26 下午6:08:07
     *
     * @param context
     * @param isShowGuidePic
     */
    public static void saveXMPush_id(Context context, String isShowGuidePic) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("XMPush_id", isShowGuidePic);
        editor.commit();
    }

    /**
     * name：获取推送ID
     * author:tom.li
     * 时间：2018-9-26 下午6:08:07
     * 避免列表重复刷新
     *
     * @param context
     * @return
     */
    public static String getXMPush_id(Context context) {
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("XMPush_id", "");
        return isShowGuidePic;
    }
    /**
     * name：保存推送平台
     */
    public static void savePush_Platform(Context context,String Platform){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("Platform_name", Platform);
        editor.commit();
    }
    /**
     * name：获取推送平台
     */
    public static String getPush_Platform(Context context){
        SharedPreferences sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        String isShowGuidePic = sp.getString("Platform_name", "");
        return isShowGuidePic;
    }

}
