package com.linlic.ccmtv.yx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.linlic.ccmtv.yx.config.URLConfig;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		// app注册到微信
		api.registerApp(URLConfig.APP_ID);
	}
}
