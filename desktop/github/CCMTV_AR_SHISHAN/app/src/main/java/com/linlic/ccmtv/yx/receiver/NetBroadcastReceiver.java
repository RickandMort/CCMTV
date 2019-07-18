package com.linlic.ccmtv.yx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.my.book.Video_book_Five;
import com.linlic.ccmtv.yx.utils.NetUtil;

/**
 * Created by cheng on 2016/11/28.
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvevt evevt = BaseActivity.evevt;
    public NetEvevt evevt1 = VideoFive.evevt;
    public NetEvevt evevt2 = Video_book_Five.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            if(BaseActivity.evevt!=null && 1==1) {
                evevt.onNetChange(netWorkState);
            }
            if(VideoFive.evevt!=null && 1==1) {
                evevt1.onNetChange(netWorkState);
            }
            if(Video_book_Five.evevt!=null && 1==1) {
                evevt2.onNetChange(netWorkState);
            }
        }
    }


    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(int netMobile);
    }
}
