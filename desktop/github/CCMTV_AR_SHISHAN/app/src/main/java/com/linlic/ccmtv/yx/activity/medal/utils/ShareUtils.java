package com.linlic.ccmtv.yx.activity.medal.utils;

import android.content.Context;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by bentley on 2018/11/29.
 */

public class ShareUtils {
    private Context context;
    private String shareTitle;
    private String shareUrl;
    private String sharePicurl;
    private String describe;
    private PlatformActionListener actionListener;

    public static class Builder {
        private ShareUtils user = new ShareUtils();

        public Builder setContext(Context context) {
            user.context = context;
            return this;
        }

        public Builder setShareTitle(String shareTitle) {
            user.shareTitle = shareTitle;
            return this;
        }

        public Builder setShareUrl(String shareUrl) {
            user.shareUrl = shareUrl;
            return this;
        }

        public Builder setSharePicurl(String sharePicurl) {
            user.sharePicurl = sharePicurl;
            return this;
        }

        public Builder setDescribe(String describe) {
            user.describe = describe;
            return this;
        }

        public Builder setPlatformActionListener(PlatformActionListener actionListener) {
            user.actionListener = actionListener;
            return this;
        }

        public ShareUtils build() {
            return user;
        }
    }

    public void startShareDialog() {
        if (context == null) return;
        if (TextUtils.isEmpty(shareUrl)) {
            Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        //分享操作
        //ShareSDK.initSDK(context);
        final ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                if (item.get("ItemText").equals("微博")) {
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setText("医学视频:" + shareTitle + "~" + shareUrl); //分享文本
                    Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    sinaWeibo.setPlatformActionListener(actionListener); // 设置分享事件回调
                    sinaWeibo.share(sp);
                } else if (item.get("ItemText").equals("微信好友")) {
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                    sp.setTitle(shareTitle);  //分享标题
                    sp.setImageUrl(sharePicurl);//网络图片rul
                    sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                    sp.setText(describe);
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(actionListener); // 设置分享事件回调
                    wechat.share(sp);
                } else if (item.get("ItemText").equals("朋友圈")) {
                    //2、设置分享内容
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                    sp.setTitle(shareTitle);  //分享标题
                    sp.setImageUrl(sharePicurl);//网络图片rul
                    sp.setUrl(shareUrl);   //网友点进链接后，可以看到分享的详情
                    sp.setText(describe);
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(actionListener); // 设置分享事件回调
                    wechatMoments.share(sp);
                } else if (item.get("ItemText").equals("QQ")) {
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setTitle(shareTitle);
                    sp.setImageUrl(sharePicurl);//网络图片rul
                    sp.setTitleUrl(shareUrl);  //网友点进链接后，可以看到分享的详情
                    sp.setText(describe);
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(actionListener); // 设置分享事件回调
                    qq.share(sp);
                } else if (item.get("ItemText").equals("QQ空间")) {
                    Platform.ShareParams sp = new Platform.ShareParams();
                    sp.setTitle(shareTitle);
                    sp.setTitleUrl(shareUrl); // 标题的超链接
                    sp.setImageUrl(sharePicurl);
                    sp.setSite("CCMTV临床医学频道");
                    sp.setSiteUrl(shareUrl);
                    sp.setText(describe);
                    Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                    qzone.setPlatformActionListener(actionListener); // 设置分享事件回调
                    qzone.share(sp);
                } else {
                    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(shareUrl.trim());
                    Toast.makeText(context, "复制成功",
                            Toast.LENGTH_LONG).show();
                }
                shareDialog.dismiss();
            }
        });
    }


    public static class DefaultPlatformActionListener implements PlatformActionListener {

        @Override
        public void onCancel(Platform platform, int i) {
            handleShareMessageResult(6001);
        }

        @Override
        public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
            arg2.printStackTrace();
            handleShareMessageResult(7001, arg2.getMessage());
        }

        @Override
        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
            final String name = arg0.getName();
            LocalApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (name.equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
                        handleShareMessageResult(1001);
                    } else if (name.equals(Wechat.NAME)) {
                        handleShareMessageResult(2001);
                    } else if (name.equals(WechatMoments.NAME)) {
                        handleShareMessageResult(3001);
                    } else if (name.equals(QQ.NAME)) {
                        handleShareMessageResult(4001);
                    } else if (name.equals(QZone.NAME)) {
                        handleShareMessageResult(5001);
                    }
                }
            });
        }

        private void handleShareMessageResult(int resultCode) {
            handleShareMessageResult(resultCode, "");
        }

        private void handleShareMessageResult(final int resultCode, final String resultMessage) {
            Context applicationContext = LocalApplication.getAppContext();
            switch (resultCode) {
                case 1001:
                    Toast.makeText(applicationContext, "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2001:
                    Toast.makeText(applicationContext, "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3001:
                    Toast.makeText(applicationContext, "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4001:
                    Toast.makeText(applicationContext, "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5001:
                    Toast.makeText(applicationContext, "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6001:
                    Toast.makeText(LocalApplication.getAppContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
                    Toast.makeText(LocalApplication.getAppContext(), "分享失败" + resultMessage, Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }


}
