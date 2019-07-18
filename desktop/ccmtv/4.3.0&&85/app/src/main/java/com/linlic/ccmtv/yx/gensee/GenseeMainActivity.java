package com.linlic.ccmtv.yx.gensee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.fastsdk.GenseeLive;
import com.gensee.fastsdk.core.GSFastConfig;
import com.gensee.fastsdk.core.GenseeVod;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * MainActivity 测试启动，实际是构造InitParam和GSFastConfig
 * 分别如下
 *  isPublishMode
 * 是否是发布端，true=发布端，false=观看端
 *   domain 设置域名
	 * <p>
	 * 若一个url为http://test.gensee.com/site/training   域名是“test.gensee.com”</p>
 *   number 直播或点播编号<p>设置对应编号，如果是点播则是点播编号，是直播便是直播编号。
	 * 请注意不要将id理解为编号。
	 * 作用等价于id，但不是id。有id可以不用编号，有编号可以不用id</p>
 *   loginAccount 站点登录账号
 * <p> 设置站点认证账号 即登录站点的账号
	 *  loginPwd 站点登录密码
	 * <p> 设置站点认证密码 即登录站点的密码
	 * 可选，如果后台设置直播需要登录或点播需要登录，那么登录密码要正确  且帐号同时也必须填写正确 </p>
 *   nickName 昵称
 * <p>设置昵称  用于直播间显示或统计   一定要填写</p>
 *  joinPwd 直播口令
 * <p>设置口令 即直播的保护密码
	 * 可选 如果后台设置了保护密码 请填写对应的口令</p>
 *   k
 * 第三方认证K值
 *   serviceType
 * 设置服务类型   webcast站点对应 WEBCAST   training 对应 TRAINING
 */

public class GenseeMainActivity extends BaseActivity {

	private Spinner mSpinner;
	private Spinner spinner2;
	private Spinner spPubSreenMode;
	private Spinner spPubHardEncode;
	private Spinner spPubQuality;
	private TextView tvVersion;
	private EditText mEditDomain;
	private EditText mEditNumber;
	private EditText mEidtAccount;
	private EditText mEidtAccountPwd;
	private EditText mEditNickName;
	private EditText mEditJoinPwd;
	private EditText mEditK;
	private EditText mEditUserId;
	private EditText etMoney1;
	private EditText etMoney2;
	private EditText etMoney3;
	private EditText etMoney4;
	private EditText etMoney5;
	private EditText etMoney6;
	private Button mBtnJoin;
	TextView live_broadcast;//观看直播
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

	private LIVEMODE liveMode ;
	private ServiceType serviceType;
	private int defPubScreenMode ;
	private int defWatchScreenMode ;
	private boolean defPubHardEncode ;
	private int defQuality;
	private String defDomain = "";
	private String defNumber = "";
	private String defNickName = "";
	private String defJoinPwd = "";
	private String defAcc = "";
	private String defAccPwd = "";
	private String defUserId = "";

	private Context context;
	private SharedPreferences preferences;
	private View fixedMoneyLy;
	private GSFastConfig gsFastConfig;
	private View btnMoreSetting;
	private View pubParamSetLy;
	private View watchParamSetLy;
	private Spinner spWatchScreenMode;
	private String fid = "";
	private String user_type = "";
	private String domain_name = "";
	private String room_number = "";
	private String room_name = "";
	private String live_pass = "";
	private String look_pass = "";
	enum LIVEMODE{
		PUBLISH(0), WATCH(1), VOD(2);

		private int value;

		 LIVEMODE(int value)
		{
			this.value = value;
		}
		public int getValue() {
			return value;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		int strId1 = getResources().getIdentifier("gensee_main", "layout", "com.linlic.ccmtv.yx");
		setContentView(strId1);
		fid = getIntent().getStringExtra("fid");
		preferences = getSharedPreferences(SettingActivity.SETTING_PREFERENCES_NAME, MODE_PRIVATE);
		initWidget();
		getUrlRulest();
	}

	private void initWidget() {
		//固定金额配置,支持0-6个
		fixedMoneyLy = findViewById(R.id.gs_ll_fixed_money);
		btnMoreSetting = findViewById(R.id.gs_bnt_more_setting);
		btnMoreSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,SettingActivity.class));
			}
		});
		pubParamSetLy = findViewById(R.id.pub_param_set_ly);
		watchParamSetLy = findViewById(R.id.watch_param_set_ly);
		etMoney1 = (EditText)findViewById(R.id.et_money1);
		etMoney2 = (EditText)findViewById(R.id.et_money2);
		etMoney3 = (EditText)findViewById(R.id.et_money3);
		etMoney4 = (EditText)findViewById(R.id.et_money4);
		etMoney5 = (EditText)findViewById(R.id.et_money5);
		etMoney6 = (EditText)findViewById(R.id.et_money6);

		tvVersion = ((TextView) findViewById(R.id.version_tv));
		tvVersion.setText(getVersion());

		spinner2 = (Spinner)findViewById(R.id.spinner2);
		List<String> list2 = new ArrayList<String>();
		list2.add(getString(R.string.watch_mode));//看
		list2.add(getString(R.string.publish_mode));//发布
		list2.add(getString(R.string.vod_mode));//点播
		final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list2);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				switch (arg2) {
					case 0:
						liveMode = LIVEMODE.WATCH;
						fixedMoneyLy.setVisibility(View.VISIBLE);
						pubParamSetLy.setVisibility(View.GONE);
						watchParamSetLy.setVisibility(View.VISIBLE);
                        if(spWatchScreenMode.getSelectedItemPosition() == 0){
                            btnMoreSetting.setVisibility(View.VISIBLE);
                        }
						break;
					case 1:
						liveMode = LIVEMODE.PUBLISH;
						fixedMoneyLy.setVisibility(View.GONE);
						pubParamSetLy.setVisibility(View.VISIBLE);
						watchParamSetLy.setVisibility(View.GONE);
                        btnMoreSetting.setVisibility(View.GONE);
						break;
					case 2:
						liveMode = LIVEMODE.VOD;
						btnMoreSetting.setVisibility(View.GONE);
						watchParamSetLy.setVisibility(View.GONE);
						fixedMoneyLy.setVisibility(View.GONE);
						pubParamSetLy.setVisibility(View.GONE);
						break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/*观看端模式选择*/
		spWatchScreenMode = (Spinner) findViewById(R.id.sp_watch_screen_mode);
		List<String> watchModelist = new ArrayList<String>();
		watchModelist.add("分屏观看(文档+视频)");
		watchModelist.add("竖屏观看");
		final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, watchModelist);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWatchScreenMode.setAdapter(adapter3);
		spWatchScreenMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				switch (arg2) {
					case 0:
						defWatchScreenMode = GSFastConfig.WATCH_SCREEN_MODE_VIDEO_DOC;
                        btnMoreSetting.setVisibility(liveMode == LIVEMODE.WATCH ? View.VISIBLE : View.GONE);
						break;
					case 1:
						defWatchScreenMode = GSFastConfig.WATCH_SCREEN_MODE_PORTRAIT;
                        btnMoreSetting.setVisibility(View.GONE );
						break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});


		mSpinner = (Spinner) findViewById(R.id.Spinner01);
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.webcast_type));
		list.add(getString(R.string.training_type));
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				switch (arg2) {
					case 0:
						serviceType = ServiceType.WEBCAST;
						break;
					case 1:
						serviceType = ServiceType.TRAINING;
						break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/*发布端屏幕方向*/
		spPubSreenMode = (Spinner) findViewById(R.id.sp_pub_screen_mode);
		List<String> listScreen = new ArrayList<String>();
		listScreen.add(getString(R.string.pub_mode_portrait));
		listScreen.add(getString(R.string.pub_mode_landscape));
		final ArrayAdapter<String> adapterScreen = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listScreen);
		adapterScreen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPubSreenMode.setAdapter(adapterScreen);
		spPubSreenMode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				switch (arg2) {
					case 0:
						defPubScreenMode = GSFastConfig.PUB_SCREEN_MODE_PORTRAIT;
						break;
					case 1:
						defPubScreenMode = GSFastConfig.PUB_SCREEN_MODE_LANDSCAPE;
						break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/*发布端硬编/软编*/
		spPubHardEncode = (Spinner) findViewById(R.id.sp_pub_hard_encode);
		List<String> listHardEncode = new ArrayList<String>();
		listHardEncode.add(getString(R.string.pub_hard_encode));
		listHardEncode.add(getString(R.string.pub_soft_encode));
		final ArrayAdapter<String> adapterHE = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listHardEncode);
		adapterHE.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPubHardEncode.setAdapter(adapterHE);
		spPubHardEncode.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				defPubHardEncode =  arg2 == 0;
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/*发布端分辨率 高清,普清*/
		spPubQuality = (Spinner) findViewById(R.id.sp_pub_quality);
		List<String> listQuality = new ArrayList<String>();
		listQuality.add(getString(R.string.pub_quality_hd));
		listQuality.add(getString(R.string.pub_quality_sd));
		final ArrayAdapter<String> adapterQuality = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listQuality);
		adapterQuality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPubQuality.setAdapter(adapterQuality);
		spPubQuality.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				if(arg2==0){
					defQuality = GSFastConfig.PUB_QUALITY_HD;
				}else{
					defQuality = GSFastConfig.PUB_QUALITY_SD;
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});



		mEditDomain = (EditText) findViewById(R.id.gs_domin);
		mEditNumber = (EditText) findViewById(R.id.gs_numble);
		mEidtAccount = (EditText) findViewById(R.id.gs_account);
		mEidtAccountPwd = (EditText) findViewById(R.id.gs_account_psw);
		mEditNickName = (EditText) findViewById(R.id.gs_nickroom);
		mEditJoinPwd = (EditText) findViewById(R.id.gs_nickname_psw);
		mEditK = (EditText) findViewById(R.id.gs_k);
		mEditUserId = (EditText) findViewById(R.id.gs_userId);
		live_broadcast = (TextView) findViewById(R.id.live_broadcast);
		i_want_live_broadcast = (TextView) findViewById(R.id.i_want_live_broadcast);

		i_want_live_broadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int version = Build.VERSION.SDK_INT;
				if(version<21){//21，安卓5.0
					Toast.makeText(GenseeMainActivity.this,"本直播暂不支持操作系统为5.0以下的安卓手机，若需直播，请更换设备观看",Toast.LENGTH_SHORT).show();
				}else {
					spinner2.setSelection(1);
					mSpinner.setSelection(1);
					serviceType = ServiceType.TRAINING;
					liveMode = LIVEMODE.PUBLISH;
					fixedMoneyLy.setVisibility(View.GONE);
					pubParamSetLy.setVisibility(View.VISIBLE);
					watchParamSetLy.setVisibility(View.GONE);
					btnMoreSetting.setVisibility(View.GONE);
					mEditDomain.setText(domain_name);
					mEditNumber.setText(room_number);
					mEditNickName.setText(SharedPreferencesTools.getUserName(GenseeMainActivity.this));
					mEditJoinPwd.setText(live_pass);
					joinBtnOnClick();
				}

			}
		});
		live_broadcast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int version = Build.VERSION.SDK_INT;
				if(version<21){//21，安卓5.0
					Toast.makeText(GenseeMainActivity.this,"本直播暂不支持操作系统为5.0以下的安卓手机，若需看直播，请更换设备观看",Toast.LENGTH_SHORT).show();
				}else {
					spinner2.setSelection(0);
					mSpinner.setSelection(1);
					serviceType = ServiceType.TRAINING;
					liveMode = LIVEMODE.WATCH;
					fixedMoneyLy.setVisibility(View.VISIBLE);
					pubParamSetLy.setVisibility(View.GONE);
					watchParamSetLy.setVisibility(View.VISIBLE);
					if(spWatchScreenMode.getSelectedItemPosition() == 0){
						btnMoreSetting.setVisibility(View.VISIBLE);
					}
					mEditDomain.setText(domain_name);
					mEditNumber.setText(room_number);
					mEditNickName.setText(SharedPreferencesTools.getUserName(GenseeMainActivity.this));
					mEditJoinPwd.setText(look_pass);
					joinBtnOnClick();
				}

			}
		});

		mBtnJoin = (Button) findViewById(R.id.gs_bnt_room_join);
		mBtnJoin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				joinBtnOnClick();
			}
		});

//		defDomain = "192.168.1.108";
//		defNumber = "35816231";
//		defNickName = "fastsdk_test_android";
//		defJoinPwd = "333333";
//		defAcc = "admin@gensee.com";
//		defAccPwd = "888888";

		mEditDomain.setText(preferences.getString(PARAMS_DOMAIN,defDomain
				));
		mEditNumber.setText(preferences.getString(PARAMS_NUMBER,
				defNumber));
		mEidtAccount.setText(preferences.getString(PARAMS_ACCOUNT,defAcc
				));
		mEidtAccountPwd.setText(preferences.getString(PARAMS_PWD,defAccPwd
				));
		mEditNickName.setText(preferences.getString(PARAMS_NICKNAME,
				defNickName));
		mEditJoinPwd.setText(preferences.getString(PARAMS_JOINPWD,
				defJoinPwd));
		mEditUserId.setText(preferences.getString(PARAMS_USERID,
				defUserId));
		String service = preferences.getString(PARAMS_SERVICE_TYPE,
				"training");
		if ("webcast".equals(service)) {
			mSpinner.setSelection(0);
		} else if ("training".equals(service)) {
			mSpinner.setSelection(1);
		}

        int pubScreenMode = preferences.getInt(PARAMS_PUB_SCREEN_MODE,0);
        if(pubScreenMode == GSFastConfig.PUB_SCREEN_MODE_PORTRAIT){
            spPubSreenMode.setSelection(0);
        } else if (pubScreenMode == GSFastConfig.PUB_SCREEN_MODE_LANDSCAPE){
            spPubSreenMode.setSelection(1);
        }

		int watchScreenMode = preferences.getInt(PARAMS_WATCH_SCREEN_MODE,0);
		if(watchScreenMode == GSFastConfig.WATCH_SCREEN_MODE_VIDEO_DOC){
			spWatchScreenMode.setSelection(0);
		} else if (watchScreenMode == GSFastConfig.WATCH_SCREEN_MODE_PORTRAIT){
			spWatchScreenMode.setSelection(1);
		}

        boolean isHardEncode = preferences.getBoolean(PARAMS_HARD_ENCODE,true);
        if(isHardEncode){
            spPubHardEncode.setSelection(0);
        } else{
            spPubHardEncode.setSelection(1);
        }

        int pubQuality = preferences.getInt(PARAMS_QUALITY,0);
        if(pubQuality == GSFastConfig.PUB_QUALITY_HD){
            spPubQuality.setSelection(0);
        } else if (pubQuality == GSFastConfig.PUB_QUALITY_SD){
            spPubQuality.setSelection(1);
        }

        liveMode = LIVEMODE.WATCH;
		spinner2.setSelection(0);
        int mode = preferences.getInt(PARAMS_LIVE_MODE,
				LIVEMODE.WATCH.getValue());
        if(mode == LIVEMODE.PUBLISH.getValue())
		{
			liveMode = LIVEMODE.PUBLISH;
			spinner2.setSelection(1);
		}else if(mode == LIVEMODE.VOD.getValue())
		{
			liveMode = LIVEMODE.VOD;
			spinner2.setSelection(2);
		}
	}


	protected void joinBtnOnClick() {
		String domain = mEditDomain.getText().toString();
		String number = mEditNumber.getText().toString();
		String account = mEidtAccount.getText().toString();
		String pwd = mEidtAccountPwd.getText().toString();
		String nickName = mEditNickName.getText().toString();
		String joinPwd = mEditJoinPwd.getText().toString();
		String k = mEditK.getText().toString().trim();
		String userId = mEditUserId.getText().toString().trim();
		String money1 = etMoney1.getText().toString();
		String money2 = etMoney2.getText().toString();
		String money3 = etMoney3.getText().toString();
		String money4 = etMoney4.getText().toString();
		String money5 = etMoney5.getText().toString();
		String money6 = etMoney6.getText().toString();

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

		if(liveMode == LIVEMODE.VOD)
		{
			GenseeVod.startVod(this, initParam);
		}
		else{
			boolean isPublishMode = liveMode == LIVEMODE.PUBLISH;
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
//			gsFastConfig.setShowQa(getBooleanFromPrefrences(SettingActivity.GS_QA));
			gsFastConfig.setShowQa(false);
			gsFastConfig.setShowIntro(true);
			gsFastConfig.setShowPIP(getBooleanFromPrefrences(SettingActivity.GS_PIP));
//			gsFastConfig.setShowHand(getBooleanFromPrefrences(SettingActivity.GS_HAND));
			gsFastConfig.setShowHand(false);
//		gsFastConfig.setShowRateSwitch(getBooleanFromPrefrences(SettingActivity.GS_RATE));
			gsFastConfig.setShownetSwitch(getBooleanFromPrefrences(SettingActivity.GS_NET));
			gsFastConfig.setShowDanmuBtn(getBooleanFromPrefrences(SettingActivity.GS_DANMU));
			gsFastConfig.setShowCloseVideo(getBooleanFromPrefrences(SettingActivity.GS_CLOSE_VIDEO));
			gsFastConfig.setSkinType(getIntFromPrefrences(SettingActivity.GS_SKIN));
			/**
			 * 自定义按钮，一项一个按钮，分别配置图标、文字和事件回调  需要则照样子增加
			 */
		/*GSMoreItem moreItem1 = new GSMoreItem(getResources().getDrawable(R.drawable.ic_more_share2), new GSMoreItem.IGSItemClickEvent() {
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
		gsFastConfig.addExtraMoreItem(moreItem2);*/
			GenseeLive.startLive(context, gsFastConfig, initParam);
		}
	}

	private boolean getBooleanFromPrefrences(String key) {
		return preferences.getBoolean(key,true);
	}

	private int getIntFromPrefrences(String key) {
		return preferences.getInt(key,0);
	}

	private String getVersion() {
		PackageManager manager;
		PackageInfo info = null;
		manager = this.getPackageManager();
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
			return info.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void back(View view){
		finish();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					try {
						MyProgressBarDialogTools.hide();
						final JSONObject jsonObject = new JSONObject(msg.obj + "");
						if (jsonObject.getInt("code") == 200) {
							JSONObject dataJson = jsonObject.getJSONObject("data");
							if (dataJson.getInt("status") == 1) { // 成功
								 user_type = dataJson.getJSONObject("data").getString("user_type");
								domain_name = dataJson.getJSONObject("data").getString("domain_name");
								room_number = dataJson.getJSONObject("data").getString("room_number");
								room_name = dataJson.getJSONObject("data").getString("room_name");
								live_pass = dataJson.getJSONObject("data").getString("live_pass");
								look_pass = dataJson.getJSONObject("data").getString("look_pass");
								if(live_pass!=null && live_pass.length()>0){
									i_want_live_broadcast.setVisibility(View.VISIBLE);
								}else{
									i_want_live_broadcast.setVisibility(View.GONE);
								}
								if(look_pass!=null && look_pass.length()>0){
									live_broadcast.setVisibility(View.VISIBLE);
								}else{
									live_broadcast.setVisibility(View.GONE);
								}

							} else {
								Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case 500:
					Toast.makeText(context
							, R.string.post_hint1,
							Toast.LENGTH_SHORT).show();
					MyProgressBarDialogTools.hide();
					break;

				default:
					break;
			}
		}
	};

	public void getUrlRulest() {
		MyProgressBarDialogTools.show(context);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("act", URLConfig.getLiveInfo   );
					obj.put("fid", fid);
					obj.put("uid", SharedPreferencesTools.getUidONnull(context));
					String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
					LogUtil.e("直播按钮权限", result);
					Message message = new Message();
					message.what = 1;
					message.obj = result;
					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(500);
				}
			}
		};
		new Thread(runnable).start();
	}
}
