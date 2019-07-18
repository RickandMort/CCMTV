package com.linlic.ccmtv.yx.receiver;

import android.content.Context;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * Created by Administrator on 2018/9/13.
 */
public class MyPushMsgReceiver extends MzPushMessageReceiver {

    @Override
    @Deprecated
    public void onRegister(Context context, String pushid) {
        //调用PushManager.register(context）方法后，会在此回调注册状态
        //应用在接受返回的pushid
        LogUtil.e("魅族pushid",pushid);
        SharedPreferencesTools.savePush_id(context,pushid);
        SharedPreferencesTools.savePush_Platform(context,"meizu");
//        com.meizu.cloud.pushsdk.PushManager.subScribeAlias(context,"text1",null,null,null);
    }

    @Override
    public void onMessage(Context context, String s) {
        //接收服务器推送的透传消息
        LogUtil.e("魅族透传消息",s);
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        //调用PushManager.unRegister(context）方法后，会在此回调反注册状态
        LogUtil.e("魅族回调反注册状态",b+"");
    }

    //设置通知栏小图标
    @Override
    public void    onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        //重要,详情参考应用小图标自定设置
        pushNotificationBuilder.setmStatusbarIcon(R.mipmap.app_icon);
    }

    @Override
    public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
        //检查通知栏和透传消息开关状态回调
        LogUtil.e("魅族通知栏和透传消息开关状态回调",pushSwitchStatus.toString()+"");
    }

    @Override
    public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
        //调用新版订阅PushManager.register(context,appId,appKey)回调
        SharedPreferencesTools.savePush_id(context,registerStatus.getPushId());
        SharedPreferencesTools.savePush_Platform(context,"meizu");
        LogUtil.e("魅族新版订阅回调",registerStatus.toString()+"");
    }

    @Override
    public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
        //新版反订阅回调
        LogUtil.e("魅族反订阅回调",unRegisterStatus.toString()+"");
    }

    @Override
    public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
        //标签回调
        LogUtil.e("魅族标签回调",subTagsStatus.toString()+"");
    }

    @Override
    public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
        //别名回调
        LogUtil.e("魅族别名回调",subAliasStatus.toString()+"");
    }
    @Override
    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
        //通知栏消息到达回调，flyme6基于android6.0以上不再回调
        LogUtil.e("魅族通知栏消息到达回调",mzPushMessage.toString()+"");
    }

    @Override
    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
        //通知栏消息点击回调
        LogUtil.e("魅族通知栏消息点击回调",mzPushMessage.toString()+"");
    }

    @Override
    public void onNotificationDeleted(Context context, MzPushMessage mzPushMessage) {
        //通知栏消息删除回调；flyme6基于android6.0以上不再回调
        LogUtil.e("魅族通知栏消息删除回调",mzPushMessage.toString()+"");
    }

}