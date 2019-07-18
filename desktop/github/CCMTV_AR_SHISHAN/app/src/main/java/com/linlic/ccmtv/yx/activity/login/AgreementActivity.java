package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import java.io.InputStream;

public class AgreementActivity extends BaseActivity{
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		context = this;

		TextView activity_title_name = (TextView) findViewById(R.id.activity_title_name);
		activity_title_name.setText("用户协议");


		TextView agreement = (TextView) findViewById(R.id.agreement);
		
		InputStream input = getResources().openRawResource(R.raw.xieyi);
		
		String result = HttpClientUtils.getString(input);
		agreement.setText(result);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		enterUrl = "http://www.ccmtv.cn/Member/Index.html";
		super.onPause();
	}
	
}
