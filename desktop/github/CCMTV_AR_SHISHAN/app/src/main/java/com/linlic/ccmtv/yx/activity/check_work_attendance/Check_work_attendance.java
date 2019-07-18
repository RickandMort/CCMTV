package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.yxzbjrrom.ScanEmptyActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.QRCodeUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.permission.Permissionutils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.util.List;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_STAYED;

/**
 * Created by tom on 2018/12/10.
 * 考勤 主页
 */

public class Check_work_attendance extends BaseActivity implements LocationSource, AMapLocationListener, View.OnClickListener {
    private Context context;
    @Bind(R.id.attendance_record)//考勤记录
            TextView attendance_record;
    @Bind(R.id.qr_code)//二维码签到记录
            TextView qr_code;
    @Bind(R.id.qr_code_view)//二维码签到记录 间隔线
            TextView qr_code_view;
    @Bind(R.id.show_qr_code)//出示二维码
            TextView show_qr_code;
    @Bind(R.id.show_qr_code_view)//出示二维码 间隔线
            View show_qr_code_view;
    @Bind(R.id.time_01)//时间字段一
            TextView time_01;
    @Bind(R.id.time_02)//时间字段一
            TextView time_02;
    @Bind(R.id.leave)//请假
            TextView leave;
    @Bind(R.id.sign_in_tiem)//签到 标准时间
            TextView sign_in_tiem;
    @Bind(R.id.sign_in)//签到
            TextView sign_in;
    @Bind(R.id.sign_in_text)//签到 时间
            TextView sign_in_text;
    @Bind(R.id.sign_back_tiem)//签退 标准时间
            TextView sign_back_tiem;
    @Bind(R.id.sign_back)//签退
            TextView sign_back;
    @Bind(R.id.sign_back_text)//签退 时间
            TextView sign_back_text;
    @Bind(R.id.amap_layout)//地图容器
            LinearLayout amap_layout;
    @Bind(R.id.add_layout)//底部地址容器
            LinearLayout add_layout;
    @Bind(R.id.map)//地图容器
            MapView mapView;
    @Bind(R.id.address_text1)//底部显示地理位置字段
            TextView address_text1;
    @Bind(R.id.address_text2)//
            TextView address_text2;
    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;//二维码展示区域
    @Bind(R.id.code_img)
    ImageView code_img;//二维码展示区域
    //声明AMapLocationClient类对象
    //初始化地图控制器对象
    AMap aMap;
    private String file_path, file_name;
    private String timestamp = "";
    private JSONArray places = null;//签到范围
    private JSONArray ips = null;//可签到的
    private int is = 0;
    private String ip = "";
    private int GEOFENCE = 0;
    private Double GEOFENCE_lat = 0D;
    private Double GEOFENCE_lng = 0D;
    private String sign_address = "";//签到地址
    java.util.Timer timer = null;
    private Long set_time = 5000L;//二维码刷新时间
    //实例化地理围栏客户端
    GeoFenceClient mGeoFenceClient = null;
    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";

    //2、创建广播监听
    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                //解析广播内容
                //获取Bundle
                Bundle bundle = intent.getExtras();
                //获取围栏行为：
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                //获取自定义的围栏标识：
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                //获取围栏ID:
                String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                //获取当前有触发的围栏对象：
                GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);

                GEOFENCE = status;

                switch (status) {
                    case GEOFENCE_IN://触发围栏行为-进入围栏
//                        Toast.makeText(Check_work_attendance.this, "触发围栏行为-进入围栏" + "   " + status + "  " + customId + "  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    case GEOFENCE_OUT://触发围栏行为-离开围栏
//                        Toast.makeText(Check_work_attendance.this, "触发围栏行为-离开围栏" + "   " + status + "  " + customId + "  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    case GEOFENCE_STAYED://触发围栏行为-停留在围栏内（在围栏内停留10分钟以上）
//                        Toast.makeText(Check_work_attendance.this, "触发围栏行为-停留在围栏内（在围栏内停留10分钟以上）" + "   " + status + "  " + customId + "  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    default:

                        break;
                }

            }
        }
    };

    PowerManager.WakeLock wakeLock = null;
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private Circle mcircle;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                if (dataJson.getJSONObject("data").getString("type").equals("1")) {
                                    sign_back.setClickable(true);
                                    sign_back.setBackground(getResources().getDrawable(R.drawable.anniu43));
                                    sign_in.setVisibility(View.GONE);
                                    sign_in_text.setVisibility(View.VISIBLE);
                                    sign_in_text.setText("已签到(" + dataJson.getJSONObject("data").getString("time") + ")");
                                } else {
                                    sign_back_text.setVisibility(View.VISIBLE);
                                    sign_back_text.setText("已签退(" + dataJson.getJSONObject("data").getString("time") + ")");
                                }
                                Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功

                                time_01.setText(dataJson.getJSONObject("data").getJSONObject("time").getString("week"));
                                time_02.setText(dataJson.getJSONObject("data").getJSONObject("time").getString("date"));
                                set_time = dataJson.getJSONObject("data").has("qrcode_time")?dataJson.getJSONObject("data").getLong("qrcode_time")*1000L:5000L;
                                //考勤记录 二维码签到记录 出示二维码 功能键解析
                                JSONArray nav_list = dataJson.getJSONObject("data").getJSONArray("nav_list");
                                for (int i = 0; i < nav_list.length(); i++) {
                                    switch (nav_list.getJSONObject(i).getString("id")) {
                                        case "1":
                                            attendance_record.setText(nav_list.getJSONObject(i).getString("name"));
                                            attendance_record.setTag(nav_list.getJSONObject(i).getString("id"));
                                            attendance_record.setVisibility(View.VISIBLE);
                                            break;
                                        case "2":
                                            qr_code_view.setVisibility(View.VISIBLE);
                                            qr_code.setVisibility(View.VISIBLE);
                                            qr_code.setText(nav_list.getJSONObject(i).getString("name"));
                                            qr_code.setTag(nav_list.getJSONObject(i).getString("id"));
                                            break;
                                        case "3":
                                            show_qr_code_view.setVisibility(View.VISIBLE);
                                            show_qr_code.setVisibility(View.VISIBLE);
                                            show_qr_code.setText(nav_list.getJSONObject(i).getString("name"));
                                            show_qr_code.setTag(nav_list.getJSONObject(i).getString("id"));
                                            break;
                                    }
                                }
                                //签到签退按钮解析
                                JSONObject btn_a = dataJson.getJSONObject("data").getJSONObject("sign_list").getJSONObject("btn_a");//签到
                                sign_in_tiem.setText(btn_a.getString("msg"));
                                sign_in.setTag(btn_a.getString("type"));
                                //签到 0隐藏 1显示
                                if (btn_a.getString("is_show_sign").equals("1")) {
                                    sign_in.setVisibility(View.VISIBLE);
                                    sign_in_text.setVisibility(View.GONE);
                                } else {
                                    sign_in.setVisibility(View.GONE);
                                    sign_in_text.setVisibility(View.VISIBLE);
                                    sign_in_text.setText(btn_a.getString("sign_time"));
                                }
                                JSONObject btn_b = dataJson.getJSONObject("data").getJSONObject("sign_list").getJSONObject("btn_b");//签退
                                sign_back_tiem.setText(btn_b.getString("msg"));
                                sign_back.setTag(btn_b.getString("type"));
                                //签退 0隐藏 1显示
                                if (btn_b.getString("is_show_sign").equals("1")) {
                                    sign_back.setVisibility(View.VISIBLE);
                                    if (btn_b.getString("sign_time").trim().length() > 0) {
                                        sign_back_text.setVisibility(View.VISIBLE);
                                        sign_back_text.setText(btn_b.getString("sign_time"));
                                    } else {
                                        sign_back_text.setVisibility(View.GONE);
                                    }
                                } else {
                                    sign_back.setVisibility(View.VISIBLE);
                                    if (btn_b.getString("sign_time").trim().length() > 0) {
                                        sign_back_text.setVisibility(View.VISIBLE);
                                        sign_back_text.setText(btn_b.getString("sign_time"));
                                    } else {
                                        sign_back_text.setVisibility(View.GONE);
                                    }

                                }
                                //判断签到是否亮起
                                if (sign_in.getVisibility() == View.VISIBLE) {
                                    sign_back.setBackground(getResources().getDrawable(R.drawable.anniu23));
                                    sign_back.setClickable(false);
                                } else {
                                    sign_back.setClickable(true);
                                    sign_back.setBackground(getResources().getDrawable(R.drawable.anniu43));
                                }
                                //获取可签到范围
                                places = dataJson.getJSONObject("data").getJSONArray("places");
                                //获取可签IP
                                ips = dataJson.getJSONObject("data").getJSONArray("ips");

                                initAmap();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            Intent intent = null;
                            intent = new Intent(context, Qr_code.class);
                            intent.putExtra("status", dataJson.getString("status"));
                            intent.putExtra("statusMsg", dataJson.getString("statusMsg"));

                            startActivity(intent);


                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                Bitmap mBitmap = QRCodeUtil.createQRImage(dataJson.getJSONObject("data").getString("qr_code"), 480, 480);
                                code_img.setImageBitmap(mBitmap);
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_work_attendance);
        context = this;
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        findId();
        initView();
        getUserSignInfo();
    }

    public void initView() {
        setActivity_title_name("考勤");
        submit_layout.setOnClickListener(this);
        show_qr_code.setOnClickListener(this);
        qr_code.setOnClickListener(this);
        attendance_record.setOnClickListener(this);
        add_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amap_layout.setVisibility(View.VISIBLE);
            }
        });
        amap_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amap_layout.setVisibility(View.GONE);
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Leave_list.class);
                startActivity(intent);
            }
        });

        /*签到*/
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ip = intToIp();
                    //先判断IP 如果IP匹配了 那就使用IP签到
                    for (int i = 0; i < ips.length(); i++) {
                        if (ips.getString(i).equals(ip)) {
                            userSign(v.getTag().toString());
                            return;
                        }
                    }
                    //第二步使用地理位置签到
                    if (places != null && places.length() > 0) {
                        //判断两点之间的距离
                        LatLng curr_LatLng = new LatLng(GEOFENCE_lat, GEOFENCE_lng);
                        LatLng mlatLng = null;
                        for (int i = 0; i < places.length(); i++) {
                            mlatLng = new LatLng(places.getJSONObject(i).getDouble("lat"), places.getJSONObject(i).getDouble("lng"));
                            //根据用户指定的两个经纬度坐标点，计算这两个点间的直线距离，单位为米
                            float distance = AMapUtils.calculateLineDistance(curr_LatLng, mlatLng);
                            //当前位置小于等于 管理员设置的半径距离 可进行签到或签退
                            if (distance <= places.getJSONObject(i).getDouble("range")) {
                                userSign(v.getTag().toString());
                                return;
                            }
                        }

                    }
                    Toast.makeText(context
                            , "当前位置不可进行签到！",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*签退*/
        sign_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ip = intToIp();
                    //先判断IP 如果IP匹配了 那就使用IP签到
                    for (int i = 0; i < ips.length(); i++) {
                        if (ips.getString(i).equals(ip)) {
                            userSign(v.getTag().toString());
                            return;
                        }
                    }
                    //第二步使用地理位置签到
                    if (places != null && places.length() > 0) {
                        //判断两点之间的距离
                        LatLng curr_LatLng = new LatLng(GEOFENCE_lat, GEOFENCE_lng);
                        LatLng mlatLng = null;
                        for (int i = 0; i < places.length(); i++) {
                            mlatLng = new LatLng(places.getJSONObject(i).getDouble("lat"), places.getJSONObject(i).getDouble("lng"));
                            //根据用户指定的两个经纬度坐标点，计算这两个点间的直线距离，单位为米
                            float distance = AMapUtils.calculateLineDistance(curr_LatLng, mlatLng);
                            //当前位置小于等于 管理员设置的半径距离 可进行签到或签退
                            if (distance <= places.getJSONObject(i).getDouble("range")) {
                                userSign(v.getTag().toString());
                                return;
                            }
                        }

                    }
                    Toast.makeText(context
                            , "当前位置不可进行签退！",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*初始化高德地图*/
    public void initAmap() {
        try {

            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
            //以下三种模式从5.1.0版本开始提供
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
//            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
            myLocationStyle.showMyLocation(true);
            myLocationStyle.strokeWidth(0);
            myLocationStyle.radiusFillColor(Color.parseColor("#10cccccc"));
            myLocationStyle.strokeColor(Color.parseColor("#10cccccc"));
// 设置定位监听
            aMap.setLocationSource((LocationSource) this);
// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationEnabled(true);
// 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            aMap.setMaxZoomLevel(20);

            //地理围栏start
            //1、创建地理围栏对象 包括设置一些基本参数
            mGeoFenceClient = new GeoFenceClient(getApplicationContext());
            //设置希望侦测的围栏触发行为，默认只侦测用户进入围栏的行为
            //public static final int GEOFENCE_IN 进入地理围栏
            //public static final int GEOFENCE_OUT 退出地理围栏
            //public static final int GEOFENCE_STAYED 停留在地理围栏内10分钟
            mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT | GEOFENCE_STAYED);
            if (places != null && places.length() > 0) {
                for (int i = 0; i < places.length(); i++) {
                    //创建一个中心点坐标
                    DPoint centerPoint = new DPoint();
                    //设置中心点纬度
                    centerPoint.setLatitude(places.getJSONObject(i).getDouble("lat"));
                    //设置中心点经度
                    centerPoint.setLongitude(places.getJSONObject(i).getDouble("lng"));
                    mGeoFenceClient.addGeoFence(centerPoint, places.getJSONObject(i).getLong("range"), "考勤" + i);
                    LogUtil.e("高德地图", places.getJSONObject(i).getDouble("lat") + "   " + places.getJSONObject(i).getDouble("lng") + "  " + places.getJSONObject(i).getLong("range"));
                }

            }
            mGeoFenceClient.setGeoFenceListener(fenceListenter);//设置回调监听
            //创建并设置PendingIntent
            mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
            //3、注册广播
            IntentFilter filter = new IntentFilter(
                    ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(GEOFENCE_BROADCAST_ACTION);
            registerReceiver(mGeoFenceReceiver, filter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建回调监听
    GeoFenceListener fenceListenter = new GeoFenceListener() {

        @Override
        public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
            if (i == GeoFence.ADDGEOFENCE_SUCCESS) {//判断围栏是否创建成功
//                Toast.makeText(Check_work_attendance.this, "添加围栏成功!!" + s, Toast.LENGTH_SHORT).show();
             /*   try{
                    if (places != null && places.length() > 0) {
                        for (int j = 0; j < places.length(); j++) {
                            LatLng latLng = new LatLng(places.getJSONObject(j).getDouble("lat"), places.getJSONObject(j).getDouble("lng"));
                            mcircle = aMap.addCircle(new CircleOptions().
                                    center(latLng).
                                    radius(places.getJSONObject(j).getLong("range")).
                                    fillColor(Color.parseColor("#50ffffff")).
                                    strokeColor(Color.parseColor("#50333333")).
                                    strokeWidth(1));
                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(places.getJSONObject(j).getLong("lat"),places.getJSONObject(j).getLong("lng"))));
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }*/
            } else {

//                Toast.makeText(Check_work_attendance.this, "添加围栏失败!!" + s, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void finish() {

        super.finish();
    }

    private final int REQUEST_CODE = 2;//请求码

    public void requestCameraPermission(final Context context){
        //API <23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Intent intent1 = new Intent(context, CaptureActivity.class);
            startActivityForResult(intent1, 0);
        }else {
            //API >=23
            if (Permissionutils.isOwnPermisson(this, Manifest.permission.CAMERA)) {
                //如果已经拥有改权限
                Intent intent1 = new Intent(context, CaptureActivity.class);
                startActivityForResult(intent1, 0);
            } else {
                //没有改权限，需要进行请求
                Permissionutils.requestPermission(this, Manifest.permission.CAMERA, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent1 = new Intent(context, CaptureActivity.class);
                    startActivityForResult(intent1, 0);
                } else {
                    Log.i("request", "failed");
                }
                return;
            }
        }
    }


    public void clickCaptureActivity(View view) {
        requestCameraPermission(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        LogUtil.e("处理二维码扫描结果","home");
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 0) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if(result.contains("SignIn/userQrCodeSign")){
                        activitiesSign(result);
                    }else {
                        Intent intent = new Intent(context, ScanEmptyActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("token",result);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(context, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void activitiesSign(final String url) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    LogUtil.e("网络访问的地址", URLConfig.CCMTVAPP_kq);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            timestamp = dataJson.getJSONObject("data").getString("timestamp");
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.userSignQrCode);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("timestamp", timestamp);
                            obj.put("param", url + "&uid=" + SharedPreferencesTools.getUid(context));
                            obj.put("token", MD5.md5(URLConfig.key + timestamp + SharedPreferencesTools.getUid(context)));

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            LogUtil.e("扫描签到", result);
                            Message message = new Message();
                            message.what = 4;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /*签到或签退*/
    public void userSign(final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    LogUtil.e("网络访问的地址", URLConfig.CCMTVAPP_kq);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            timestamp = dataJson.getJSONObject("data").getString("timestamp");

                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.userSign);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("token", MD5.md5(URLConfig.key + timestamp + SharedPreferencesTools.getUid(context)));
                            obj.put("timestamp", timestamp);
                            obj.put("type", type);
                            obj.put("ip", ip);
                            obj.put("longitude", GEOFENCE_lng + "$$$" + GEOFENCE_lat);
                            obj.put("sign_address", sign_address);

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);

                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /*考勤首页 数据*/
    public void getUserSignInfo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            timestamp = dataJson.getJSONObject("data").getString("timestamp");
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.getUserSignInfo);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("token", MD5.md5(URLConfig.key + timestamp + SharedPreferencesTools.getUid(context)));
                            obj.put("timestamp", timestamp);
                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 2;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /*老师展示二维码*/
    public void getManageQrCode() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            timestamp = dataJson.getJSONObject("data").getString("timestamp");
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.getManageQrCode);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("token", MD5.md5(URLConfig.key + timestamp + SharedPreferencesTools.getUid(context)));
                            obj.put("timestamp", timestamp);
                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 5;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /*获取系统时间戳*/
    public void getTimestamp() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getTimestamp);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
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

    @Override
    protected void onDestroy() {
        //会清除所有围栏
        if (mGeoFenceClient!=null) {
            mGeoFenceClient.removeGeoFence();
        }

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mapView!=null) {
            mapView.onDestroy();
        }
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if(wakeLock!=null)
            {
                wakeLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        submit_layout.setVisibility(View.GONE);
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "https://yun.ccmtv.cn/admin.php/wx/SignIn";
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                address_text1.setText(amapLocation.getAddress());
//                address_text1.setText("");
                if (is == 0) {
                    is = 1;
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 17f));
                }
                GEOFENCE_lat = amapLocation.getLatitude();
                GEOFENCE_lng = amapLocation.getLongitude();
                sign_address = amapLocation.getAddress();
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                LogUtil.e("高德地图", errText);
            }
        }
    }

    private String intToIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }


    public void submit_img() {

        if (timer != null) {
            //已存在
        } else {
            wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            if (wakeLock != null) {
                wakeLock.acquire();//这句执行后，手机将不会休眠，直到执行wakeLock.release();方法
            }
            timer = new java.util.Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    getManageQrCode();
                }
            };
            //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
            timer.schedule(task, 0, set_time);
        }
        submit_layout.setVisibility(View.VISIBLE);


    }

    public void is_qrcode(View v) {
        Intent intent = null;
        switch (v.getTag().toString()) {
            case "1":
                intent = new Intent(context, Attendance_list.class);
                startActivity(intent);
                break;
            case "2":
                intent = new Intent(context, Code_Recording_list.class);
                startActivity(intent);
                break;
            case "3":
                submit_img();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_qr_code://显示二维码
                is_qrcode(v);
                break;
            case R.id.qr_code://二维码记录
                is_qrcode(v);
                break;
            case R.id.attendance_record://考勤记录
                is_qrcode(v);
                break;
            case R.id.submit_layout://二维码展示区域点击
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if(wakeLock!=null)
                {
                    wakeLock.release();
                }
                submit_layout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }


    @Override
    protected void onStop() {
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if(wakeLock!=null)
            {
                wakeLock.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }
}
