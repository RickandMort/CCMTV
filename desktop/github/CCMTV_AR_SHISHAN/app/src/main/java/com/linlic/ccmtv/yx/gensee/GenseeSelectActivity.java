package com.linlic.ccmtv.yx.gensee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.fastsdk.GenseeLive;
import com.gensee.fastsdk.core.GSFastConfig;
import com.gensee.fastsdk.core.GenseeVod;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.tencent.smtt.sdk.TbsReaderView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/3/22.
 */

public class GenseeSelectActivity extends BaseActivity {
    private Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn";
    @Bind(R.id.live_broadcast)
    TextView live_broadcast;//观看直播
    @Bind(R.id.i_want_live_broadcast)
    TextView i_want_live_broadcast;//我要直播



    public static final String PARAMS_DOMAIN = "PARAMS_DOMAIN";
    public static final String PARAMS_TYPE = "PARAMS_TYPE";
    public static final String PARAMS_NUMBER = "PARAMS_NUMBER";
    public static final String PARAMS_ACCOUNT = "PARAMS_ACCOUNT";
    public static final String PARAMS_PWD = "PARAMS_PWD";
    public static final String PARAMS_NICKNAME = "PARAMS_NICKNAME";
    public static final String PARAMS_JOINPWD = "PARAMS_JOINPWD";
    public static final String PARAMS_SERVICE_TYPE = "PARAMS_SERVICE_TYPE";
    public static final String PARAMS_LIVE_MODE = "PARAMS_LIVE_MODE";
    public static final String PARAMS_PUB_SCREEN_MODE = "PARAMS_PUB_SCREEN_MODE";
    public static final String PARAMS_WATCH_SCREEN_MODE = "PARAMS_WATCH_SCREEN_MODE";
    public static final String PARAMS_HARD_ENCODE = "PARAMS_HARD_ENCODE";
    public static final String PARAMS_QUALITY = "PARAMS_QUALITY";
    public static final String PARAMS_USERID = "PARAMS_USERID";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.genseeselect);
        context = this;
        ButterKnife.bind(this);
        preferences = getSharedPreferences(SettingActivity.SETTING_PREFERENCES_NAME, MODE_PRIVATE);
    }


  /*  protected void joinBtnOnClick() {
        String domain = "ccmtvbj.gensee.com";
        String number = "84151266";
        String account = "";
        String pwd = "";
        String nickName = "移动端直播测试课堂";
        String joinPwd = "676457";
        String k = "";
        String userId = "";
        String money1 = "";
        String money2 = "";
        String money3 = "";
        String money4 = "";
        String money5 = "";
        String money6 = "";

        int[] arr = new int[6];
        arr[0] = Integer.valueOf(TextUtils.isEmpty(money1)?"0":money1);
        arr[1] = Integer.valueOf(TextUtils.isEmpty(money2)?"0":money2);
        arr[2] = Integer.valueOf(TextUtils.isEmpty(money3)?"0":money3);
        arr[3] = Integer.valueOf(TextUtils.isEmpty(money4)?"0":money4);
        arr[4] = Integer.valueOf(TextUtils.isEmpty(money5)?"0":money5);
        arr[5] = Integer.valueOf(TextUtils.isEmpty(money6)?"0":money6);

        if ("".equals(domain) || "".equals(number)) {
            Toast.makeText(context, "域名和编号都不能为空", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        long luserId = 0;
        if(userId.length() >= 10){
            luserId = Long.valueOf(userId);
        }

        preferences.edit().putString(PARAMS_DOMAIN, domain)
                .putString(PARAMS_NUMBER, number)
                .putString(PARAMS_ACCOUNT, account)
                .putString(PARAMS_PWD, pwd)
                .putString(PARAMS_NICKNAME, nickName)
                .putString(PARAMS_JOINPWD, joinPwd)
                .putString(PARAMS_USERID,luserId>1000000000?userId:"")
                .putString(PARAMS_SERVICE_TYPE, serviceType.getValue())
                .putInt(PARAMS_LIVE_MODE, liveMode.getValue())
                .putInt(PARAMS_PUB_SCREEN_MODE, defPubScreenMode)
                .putInt(PARAMS_WATCH_SCREEN_MODE, defWatchScreenMode)
                .putBoolean(PARAMS_HARD_ENCODE, defPubHardEncode)
                .putInt(PARAMS_QUALITY, defQuality)
                .commit();

        InitParam initParam = new InitParam();
        //若一个url为http://test.gensee.com/site/webcast   域名是“test.gensee.com”
        initParam.setDomain(domain);
        //设置对应编号，如果是点播则是点播编号，是直播便是直播编号。
        //请注意不要将id理解为编号。
        //作用等价于id，但不是id。有id可以不用编号，有编号可以不用id
        initParam.setNumber(number);
        //设置站点认证账号 即登录站点的账号
        initParam.setLoginAccount(account);
        //设置站点认证密码 即登录站点的密码,如果后台设置直播需要登录或点播需要登录，那么登录密码要正确  且帐号同时也必须填写正确
        initParam.setLoginPwd(pwd);
        //设置昵称  用于直播间显示或统计   一定要填写
        initParam.setNickName(nickName);
        //可选 如果后台设置了保护密码 请填写对应的口令
        initParam.setJoinPwd(joinPwd);
        //第三方认证K值，如果启用第三方集成的时候必须传入有效的K值
        initParam.setK(k);
        //若一个url为http://test.gensee.com/site/webcast ,serviceType是 ServiceType.WEBCAST,
        //url为http://test.gensee.com/site/training,serviceTypeserviceType是 ServiceType.TRAINING
        initParam.setServiceType(serviceType);
        //自定义userid 大于10亿有效
        initParam.setUserId(luserId);

        if(liveMode == GenseeMainActivity.LIVEMODE.VOD)
        {
            GenseeVod.startVod(this, initParam);
        }
        else{
            boolean isPublishMode = liveMode == GenseeMainActivity.LIVEMODE.PUBLISH;
            gsFastConfig = new GSFastConfig();
            //是否是主播端，false和默认观看端
            gsFastConfig = new GSFastConfig();
            gsFastConfig.setPublish(isPublishMode);
            gsFastConfig.setPublishScreenMode(defPubScreenMode);
            gsFastConfig.setWatchScreenMode(defWatchScreenMode);
            gsFastConfig.setHardEncode(defPubHardEncode);
            gsFastConfig.setPubQuality(defQuality);
            //配置观看端打赏固定金额面板,最多支持6个固定金额,固定金额最高不能超过200000(即2000.00元)
            gsFastConfig.setFixedMoneyArray(arr);
            //分屏观看端,界面配置
            gsFastConfig.setShowDoc(getBooleanFromPrefrences(SettingActivity.GS_DOC));
            gsFastConfig.setShowChat(getBooleanFromPrefrences(SettingActivity.GS_CHAT));
            gsFastConfig.setShowQa(getBooleanFromPrefrences(SettingActivity.GS_QA));
            gsFastConfig.setShowIntro(getBooleanFromPrefrences(SettingActivity.GS_INTRO));
            gsFastConfig.setShowPIP(getBooleanFromPrefrences(SettingActivity.GS_PIP));
            gsFastConfig.setShowHand(getBooleanFromPrefrences(SettingActivity.GS_HAND));
//		gsFastConfig.setShowRateSwitch(getBooleanFromPrefrences(SettingActivity.GS_RATE));
            gsFastConfig.setShownetSwitch(getBooleanFromPrefrences(SettingActivity.GS_NET));
            gsFastConfig.setShowDanmuBtn(getBooleanFromPrefrences(SettingActivity.GS_DANMU));
            gsFastConfig.setShowCloseVideo(getBooleanFromPrefrences(SettingActivity.GS_CLOSE_VIDEO));
            gsFastConfig.setSkinType(getIntFromPrefrences(SettingActivity.GS_SKIN));
            *
             * 自定义按钮，一项一个按钮，分别配置图标、文字和事件回调  需要则照样子增加

		GSMoreItem moreItem1 = new GSMoreItem(getResources().getDrawable(R.drawable.ic_more_share2), new GSMoreItem.IGSItemClickEvent() {
			@Override
			public void itemEvent(View v, String subject, String liveId) {
				Toast.makeText(MainActivity.this, "分享被点击了", Toast.LENGTH_SHORT).show();
			}
		});
		//moreItem1.setText("分享"); //有文字的按钮 需要设置有效的文字文本
		GSMoreItem moreItem2 = new GSMoreItem(getResources().getDrawable(R.drawable.ic_more_report2), new GSMoreItem.IGSItemClickEvent() {
			@Override
			public void itemEvent(View v, String subject, String liveId) {
				Toast.makeText(MainActivity.this, "举报被点击了", Toast.LENGTH_SHORT).show();
			}
		});
		//moreItem1.setText("举报"); //有文字的按钮 需要设置有效的文字文本

		gsFastConfig.addExtraMoreItem(moreItem1);
		gsFastConfig.addExtraMoreItem(moreItem2);
            GenseeLive.startLive(context, gsFastConfig, initParam);
        }
    }*/

}
