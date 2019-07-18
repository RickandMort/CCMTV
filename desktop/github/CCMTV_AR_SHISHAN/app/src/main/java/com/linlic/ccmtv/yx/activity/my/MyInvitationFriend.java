package com.linlic.ccmtv.yx.activity.my;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * name：邀请好友
 * author：Larry
 * data：2016/3/22 16:54
 */
public class MyInvitationFriend extends BaseActivity implements PlatformActionListener {
    Button btn_invitation;
    TextView activity_title_name;
    Context context;
    //用户统计
    private String type;
    String dataurl;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7:
                    if (msg.obj.toString().contains("WechatClientNotExistException")) {
                        Toast.makeText(getApplicationContext(), "您的微信版本过低或未安装微信，需要安装并启动微信才能使用", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    }
                    break;
                case 100:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            // JSONObject object = result.getJSONObject("data");
                            dataurl = result.getString("data");
                            // Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_invitation_friend);
        context = this;

        type = getIntent().getStringExtra("type");

        btn_invitation = (Button) findViewById(R.id.btn_invitation);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        setText();
        setclick();
        setData();
    }

    public void setText() {
        activity_title_name.setText("邀请好友送积分");
    }

    public void setData() {
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
            return;
        } else {
            MyProgressBarDialogTools.show(context);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", uid);
                        object.put("act", URLConfig.inviteReg);
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());

                        Message message = new Message();
                        message.what = 100;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }
    }

    /**
     * name：
     * author：Larry
     * data：2016/3/22 16:54
     */
    public void setclick() {
        btn_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //,,
                if (!TextUtils.isEmpty(dataurl)) {
                    //分享操作
                    //ShareSDK.initSDK(MyInvitationFriend.this);
                    final ShareDialog shareDialog = new ShareDialog(MyInvitationFriend.this);
                    shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            shareDialog.dismiss();

                        }
                    });
                    shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                            if (item.get("ItemText").equals("微博")) {
                                //2、设置分享内容
                                ShareParams sp = new ShareParams();
                                sp.setText("加入CCMTV，观看海量临床医学视频，还送积分呦！~" + dataurl); //分享文本
                                // sp.setText("输入手机号码，轻松注册领取积分，尽享海量医学视频！"); //分享文本
                                // sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                                //3、非常重要：获取平台对象
                                Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                //sinaWeibo.removeAccount(true);
                                // ShareSDK.removeCookieOnAuthorize(true);
                                sinaWeibo.setPlatformActionListener(MyInvitationFriend.this); // 设置分享事件回调
                                // 执行分享
                                sinaWeibo.share(sp);
                            } else if (item.get("ItemText").equals("微信好友")) {
                                //2、设置分享内容
                                ShareParams sp = new ShareParams();
                                sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                                sp.setTitle("CCMTV临床医学频道");  //分享标题
                                sp.setText("加入CCMTV，观看海量临床医学视频，还送积分呦！");   //分享文本
                                sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                                sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                //3、非常重要：获取平台对象
                                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                                wechat.setPlatformActionListener(MyInvitationFriend.this); // 设置分享事件回调
                                // 执行分享
                                wechat.share(sp);
                            } else if (item.get("ItemText").equals("朋友圈")) {
                                //2、设置分享内容
                                ShareParams sp = new ShareParams();
                                sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                                sp.setTitle("CCMTV临床医学频道");  //分享标题
                                sp.setText("加入CCMTV，观看海量临床医学视频，还送积分呦！");   //分享文本
                                sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                                sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                                //3、非常重要：获取平台对象
                                Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                                wechatMoments.setPlatformActionListener(MyInvitationFriend.this); // 设置分享事件回调
                                // 执行分享
                                wechatMoments.share(sp);
                            } else if (item.get("ItemText").equals("QQ")) {
                                //2、设置分享内容
                                ShareParams sp = new ShareParams();
                                sp.setTitle("CCMTV临床频道");
                                sp.setText("加入CCMTV，观看海量临床医学视频，还送积分呦！");
                                sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");//网络图片rul
                                sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                                //3、非常重要：获取平台对象
                                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                                qq.setPlatformActionListener(MyInvitationFriend.this); // 设置分享事件回调
                                // 执行分享
                                qq.share(sp);
                            } else if (item.get("ItemText").equals("QQ空间")) {
                                ShareParams sp = new ShareParams();
                                sp.setTitle("CCMTV临床医学频道");
                                sp.setTitleUrl(dataurl); // 标题的超链接
                                sp.setText("加入CCMTV，观看海量临床医学视频，还送积分呦！");
                                sp.setImageUrl("http://www.ccmtv.cn/touch-icon-ipad.png");
                                sp.setSite("CCMTV临床医学频道");
                                sp.setSiteUrl(dataurl);

                                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                                qzone.setPlatformActionListener(MyInvitationFriend.this); // 设置分享事件回调
                                // 执行图文分享
                                qzone.share(sp);
                            } else {
                                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                cmb.setText(dataurl.trim());
                                Toast.makeText(MyInvitationFriend.this, "复制成功", Toast.LENGTH_LONG).show();
                            }
                            shareDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        if (arg0.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (arg0.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(2);
        } else if (arg0.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (arg0.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4);
        } else if (arg0.getName().equals(QZone.NAME)) {
            handler.sendEmptyMessage(5);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {//回调的地方是子线程，进行UI操作要用handle处理
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 7;
        //msg.obj = arg2.getMessage();
        msg.obj = arg2.toString();
//        Log.i("msg.obj", arg2.toString());
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(6);
    }

    //在项目出口Activity的onDestroy方法中第一行插入下面的代码：
    @Override
    protected void onDestroy() {
        //ShareSDK.stopSDK(MyInvitationFriend.this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        }
        super.onPause();
    }

}
