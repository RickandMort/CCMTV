package com.linlic.ccmtv.yx.activity.base;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

/**
 * Created by tom on 2018/12/19.
 */

public class JavaScriptinterface {
    Context context;
    public JavaScriptinterface(Context c) {
        context= c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void showToast(String ssss) {

        Toast.makeText(context, ssss, Toast.LENGTH_LONG).show();
    }
    /*用户ID*/
    @JavascriptInterface
    public String  getUid() {
        return SharedPreferencesTools.getUidONnull(context);
    }
    /*用户名*/
    @JavascriptInterface
    public String  getUserName() {
        return SharedPreferencesTools.getUserName(context);
    }
    /*明文密码*/
    @JavascriptInterface
    public String  getPassword() {
        return SharedPreferencesTools.getPassword(context);
    }
    /*是否登录*/
    @JavascriptInterface
    public Boolean  isLogin() {
        return SharedPreferencesTools.getUidONnull(context).length()>0?true:false;
    }
    /*是否登录*/
    @JavascriptInterface
    public void  Login() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}