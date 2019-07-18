package com.linlic.ccmtv.yx.activity.base;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.receiver.NetBroadcastReceiver;
import com.linlic.ccmtv.yx.utils.CustomToast;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

/**
 * name:非主界面可继承
 * author:Tom
 * 2016-3-2下午7:06:34
 */
public class BaseActivity extends Activity implements NetBroadcastReceiver.NetEvevt {
    private TextView activity_title_name;
    public TextView tv_nodata;
    public NodataEmptyLayout layout_nodata;
    public Button btn_nodata;
    public ImageView iv_nodata;
    public static NetBroadcastReceiver.NetEvevt evevt;
    protected Context context = this;
    /**
     * 网络类型
     */
    private int netMobile;
    /***封装toast对象**/
    private static Toast toast;
    /***自定义toast样式对象**/
    private CustomToast custom_toast;
    public static String enterUrl = "";
    private String entertime, leavetime;


    private NetworkChangeReceiver networkChangeRecever;

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connecttivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//使用getSystemService得到ConnectivityManager实例
            NetworkInfo networkInfo = connecttivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                // 这里需要判断，虽然连接了，但是网络仍然不可访问
                if (!NetUtil.isNetworkOnline()) {
                    Toast.makeText(context, R.string.post_hint4, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("PRETTY_LOGGER", "onReceive() returned: " + "当前网络可以用");
                }
            } else {
                Toast.makeText(context,  R.string.post_hint4, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initBaidu();
        context = this;
        evevt = this;
        inspectNet();


        // 注册一个网络状态监听的广播
        //@author eric
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");//添加广播
        networkChangeRecever = new NetworkChangeReceiver();
        registerReceiver(networkChangeRecever, intentFilter);
    }

    private void initBaidu() {
        //初始化百度统计
        // 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
        StatService.setSessionTimeOut(30);
        // setOn也可以在AndroidManifest.xml文件中填写，BaiduMobAd_EXCEPTION_LOG，打开崩溃错误收集，默认是关闭的
        StatService.setOn(this, StatService.EXCEPTION_LOG);
        /*
         * 设置启动时日志发送延时的秒数<br/> 单位为秒，大小为0s到30s之间<br/> 注：请在StatService.setSendLogStrategy之前调用，否则设置不起作用
         *
         * 如果设置的是发送策略是启动时发送，那么这个参数就会在发送前检查您设置的这个参数，表示延迟多少S发送。<br/> 这个参数的设置暂时只支持代码加入，
         * 在您的首个启动的Activity中的onCreate函数中使用就可以。<br/>
         */
        StatService.setLogSenderDelayed(0);
        /*
         * 用于设置日志发送策略<br /> 嵌入位置：Activity的onCreate()函数中 <br />
         *
         * 调用方式：StatService.setSendLogStrategy(this,SendStrategyEnum. SET_TIME_INTERVAL, 1, false); 第二个参数可选：
         * SendStrategyEnum.APP_START SendStrategyEnum.ONCE_A_DAY SendStrategyEnum.SET_TIME_INTERVAL 第三个参数：
         * 这个参数在第二个参数选择SendStrategyEnum.SET_TIME_INTERVAL时生效、 取值。为1-24之间的整数,即1<=rtime_interval<=24，以小时为单位 第四个参数：
         * 表示是否仅支持wifi下日志发送，若为true，表示仅在wifi环境下发送日志；若为false，表示可以在任何联网环境下发送日志
         */
        StatService.setSendLogStrategy(this, SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(true);

        String sdkVersion = StatService.getSdkVersion();

    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void findId() {
        activity_title_name = findViewById(R.id.activity_title_name);
        layout_nodata = findViewById(R.id.nodata_empty_layout);
        if (layout_nodata == null) {
            View view = findViewById(R.id.layout_nodata);
            if (view instanceof NodataEmptyLayout)
                layout_nodata = (NodataEmptyLayout) view;//会有一些布局既有Linearlayout的nodata 又有NodataEmptyLayout  这里会出现强转异常
        }
        if (layout_nodata != null) {
            tv_nodata = layout_nodata.getTvEmptyDes();
            btn_nodata = layout_nodata.getBtnNodata();
            iv_nodata = layout_nodata.getIvEmptyIcon();
        } else {
        }
    }


    /**
     * name:查找值 author:Tom 2016-2-2下午5:29:04
     */
    public void setClick() {
        btn_nodata.setVisibility(View.VISIBLE);
    }

    public TextView getActivity_title_name() {
        return activity_title_name;
    }

    public void setActivity_title_name(int str) {
        this.activity_title_name.setText(str);
    }

    public void setActivity_btnnodata(int str) {
        if (this.btn_nodata != null)
            this.btn_nodata.setText(str);
    }

    public void setActivity_title_name(String str) {
        this.activity_title_name.setText(str);
    }

    public void setActivity_btnnodata(String str) {
        if (this.btn_nodata != null)
            this.btn_nodata.setText(str);
    }

    public void setActivity_tvnodata(int str) {
        if (this.tv_nodata != null)
            this.tv_nodata.setText(str);
    }

    public void setActivity_tvnodata(String str) {
        if (this.tv_nodata != null)
            this.tv_nodata.setText(str);
    }


    /**
     * 显示nodata（）
     */
    public void showNoData(int resultCode) {
        if (layout_nodata != null) {
            if (HttpClientUtils.isNetConnectError(context, resultCode)) {
                layout_nodata.setNetErrorIcon();
            } else {
                layout_nodata.setLastEmptyIcon();
            }
            layout_nodata.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 隐藏nodata（）
     */
    public void hideNoData() {
        if (layout_nodata != null) {
            layout_nodata.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏nodata（）
     */
    public void showNoData() {
        showNoData(HttpClientUtils.UNKONW_EXCEPTION_CODE);
    }


    /**
     * 设置点击edittext之外键盘消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);
            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
            return onTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        entertime = SkyVisitUtils.getCurrentTime();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        leavetime = SkyVisitUtils.getCurrentTime();
        if (!enterUrl.equals("")) {
            SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        }


        StatService.onPause(this);
    }


    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);

        return isNetConnect();

    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netMobile) {
        // TODO Auto-generated method stub
        this.netMobile = netMobile;
        isNetConnect();

    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == NetUtil.NETWORK_WIFI) {
            // 还需要判断连接的wifi 是否可访问
            return true;
        } else if (netMobile == NetUtil.NETWORK_MOBILE) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_NONE) {
            return false;

        }
        return false;
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public int isNetConnect2() {
        return netMobile;
    }


    /**
     * 快速点击的限制
     */
    private static long lastClickTime;

    public static boolean fastClick() {
        //点击时间
        long clickTime = SystemClock.uptimeMillis();
        //如果当前点击间隔小于1000毫秒
        if (lastClickTime >= clickTime - 1000) {
            return false;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        return true;
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 无数据的页面跳转
     */
    public void startActivity(Class<? extends Activity> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    /**
     * 显示长Toast
     *
     * @param msg
     */
    public void toastLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 显示短Toast
     *
     * @param msg
     */
    public void toastShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 自定义Toast样式
     */
    public void toastMessage(String content, int duration) {
        if (custom_toast != null) {
            custom_toast.hide();
        }
        custom_toast = new CustomToast(this, (ViewGroup) this.findViewById(R.id.toast_custom_parent));
        custom_toast.show(content, duration);
    }


    @Override
    protected void onDestroy() {
        if (networkChangeRecever != null)
            this.unregisterReceiver(networkChangeRecever);
        super.onDestroy();
    }
}
