package com.linlic.ccmtv.yx.activity.amap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_STAYED;

public class AmapActivity1 extends BaseActivity implements  AMap.OnMapLoadedListener,AMap.OnMyLocationChangeListener, PoiSearch.OnPoiSearchListener, View.OnClickListener,AMapLocationListener {
    private ListView listView;
    private MapView mapView;
    private Marker locationMarker, checkinMarker;
    private LatLonPoint searchLatlonPoint;
    private List<PoiItem> resultData;
    private SearchResultAdapter searchResultAdapter;
    private WifiManager mWifiManager;
    private PoiSearch poisearch;
    private Circle mcircle;
    private LatLng checkinpoint, mlocation;
    private Button locbtn, checkinbtn;
    private boolean isItemClickAction, isLocationAction;
    //声明AMapLocationClient类对象
    //初始化地图控制器对象
    AMap aMap;
    MyLocationStyle myLocationStyle;
    GeoFenceClient mGeoFenceClient = null;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    //定义接收广播的action字符串
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gd_main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        resultData = new ArrayList<>();
        init();
        //初始化定位
        initLocation();
        //开始定位
        startLocation();
    }

    private void init() {

//        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mWifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);

        listView = (ListView) findViewById(R.id.listview);
        searchResultAdapter = new SearchResultAdapter(AmapActivity1.this);
        searchResultAdapter.setData(resultData);
        listView.setAdapter(searchResultAdapter);

        listView.setOnItemClickListener(onItemClickListener);

        locbtn = (Button) findViewById(R.id.locbtn);
        locbtn.setOnClickListener(this);
        checkinbtn = (Button) findViewById(R.id.checkinbtn);
        checkinbtn.setOnClickListener(this);
    }

    /**
     * 列表点击监听
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position != searchResultAdapter.getSelectedPosition()) {
                PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
                LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
                isItemClickAction = true;
                searchResultAdapter.setSelectedPosition(position);
                searchResultAdapter.notifyDataSetChanged();

            }
        }
    };

    /**
     * 初始化定位，设置回调监听
     */
    private void initLocation() {
        mlocationClient = new AMapLocationClient(this);

//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位参数
        mlocationClient.setLocationOption(getOption());
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
//        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setMaxZoomLevel(20);
        aMap.setOnMyLocationChangeListener(this);

        /*蓝点*/
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.showMyLocation(true);
        myLocationStyle.strokeColor(Color.parseColor("#01ffffff"));
        myLocationStyle.radiusFillColor(Color.parseColor("#01ffffff"));
        myLocationStyle.strokeWidth(0.1f);
        // 1、创建地理围栏对象
        //实例化地理围栏客户端
        mGeoFenceClient = new GeoFenceClient(getApplicationContext());
        //设置希望侦测的围栏触发行为，默认只侦测用户进入围栏的行为
        //public static final int GEOFENCE_IN 进入地理围栏
        //public static final int GEOFENCE_OUT 退出地理围栏
        //public static final int GEOFENCE_STAYED 停留在地理围栏内10分钟
        mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT | GEOFENCE_STAYED);
        //2、创建高德POI地理围栏
        //创建一个中心点坐标
        DPoint centerPoint = new DPoint();
        //设置中心点纬度
        centerPoint.setLatitude(31.232031D);
        //设置中心点经度
        centerPoint.setLongitude(121.467688D);
        mGeoFenceClient.addGeoFence (centerPoint,50F,"androidkey");

        //创建回调监听
        GeoFenceListener fenceListenter = new GeoFenceListener() {

            @Override
            public void onGeoFenceCreateFinished(List<GeoFence> list, int i, String s) {
                if(i == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
                    Toast.makeText(AmapActivity1.this, "添加围栏成功~"+s, Toast.LENGTH_SHORT).show();
                    LatLng latLng = new LatLng(31.232031D,121.467688D);
                    mcircle = aMap.addCircle(new CircleOptions().
                            center(latLng).
                            radius(50).
                            fillColor(Color.parseColor("#01ffffff")).
                            strokeColor(Color.parseColor("#333333")).
                            strokeWidth(1));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(31.232031D,121.467688D)));
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.232031D,121.467688D), 16f));
                    //geoFenceList是已经添加的围栏列表，可据此查看创建的围栏

                } else {

                    Toast.makeText(AmapActivity1.this, "添加围栏失败~"+s, Toast.LENGTH_SHORT).show();
                }
            }


        };
        mGeoFenceClient.setGeoFenceListener(fenceListenter);//设置回调监听

        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        //3、注册广播
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);
    }
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
                LogUtil.e("围栏",status+"  "+customId +"  " + fenceId);

                switch (status){
                    case GEOFENCE_IN://触发围栏行为-进入围栏
                        Toast.makeText(AmapActivity1.this,"触发围栏行为-进入围栏"+"   "+status+"  "+customId +"  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    case GEOFENCE_OUT://触发围栏行为-离开围栏
                        Toast.makeText(AmapActivity1.this,"触发围栏行为-离开围栏"+"   "+status+"  "+customId +"  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    case GEOFENCE_STAYED://触发围栏行为-停留在围栏内（在围栏内停留10分钟以上）
                        Toast.makeText(AmapActivity1.this,"触发围栏行为-停留在围栏内（在围栏内停留10分钟以上）"+"   "+status+"  "+customId +"  " + fenceId, Toast.LENGTH_SHORT).show();
                        break;
                    default:

                        break;
                }

                searchLatlonPoint = new LatLonPoint(fence.getCenter().getLatitude(), fence.getCenter().getLongitude());
                doSearchQuery(searchLatlonPoint);


            }
        }
    };

    /**
     * 设置定位参数
     * @return 定位参数类
     */
    private AMapLocationClientOption getOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setLocationCacheEnable(false);//设置是否返回缓存中位置，默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        return mOption;
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null
                && aMapLocation.getErrorCode() == 0) {
        /*    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
            aMapLocation.getLatitude();//获取纬度
            aMapLocation.getLongitude();//获取经度
            aMapLocation.getAccuracy();//获取精度信息
            aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            aMapLocation.getCountry();//国家信息
            aMapLocation.getProvince();//省信息
            aMapLocation.getCity();//城市信息
            aMapLocation.getDistrict();//城区信息
            aMapLocation.getStreet();//街道信息
            aMapLocation.getStreetNum();//街道门牌号信息
            aMapLocation.getCityCode();//城市编码
            aMapLocation.getAdCode();//地区编码
            aMapLocation.getAoiName();//获取当前定位点的AOI信息
            aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
            aMapLocation.getFloor();//获取当前室内定位的楼层
            aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
//获取定位时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(aMapLocation.getTime());
            df.format(date);
            */
            Toast.makeText(AmapActivity1.this,"位置"+"   "+aMapLocation.getLatitude()+"  "+aMapLocation.getLongitude()  , Toast.LENGTH_SHORT).show();
            mlocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            Point screenPosition = aMap.getProjection().toScreenLocation(mlocation);
            searchLatlonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());

            isLocationAction = true;
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mlocation, 16f));
            doSearchQuery(searchLatlonPoint);
            locationMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f,0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
            locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr",errText);
        }
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        checkWifiSetting();
        //设置定位参数

    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
            //会清除所有围栏
        mGeoFenceClient.removeGeoFence();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;}
    }

    /**
     * 检查wifi，并提示用户开启wifi
     */
    private void checkWifiSetting() {
        if (mWifiManager.isWifiEnabled()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("开启WIFI模块会提升定位准确性"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent); // 打开系统设置界面
            }
        });
        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    /**
     * 地图加载完成回调
     */
    @Override
    public void onMapLoaded() {
        addMarkerInScreenCenter();
    }

    /**
     * 添加选点marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        destroyLocation();
    }


    /**
     * 搜索周边poi
     *
     * @param centerpoint
     */
    private void doSearchQuery(LatLonPoint centerpoint) {
        PoiSearch.Query query = new PoiSearch.Query("", "", "");
        query.setPageSize(20);
        query.setPageNum(0);
        poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(this);
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 1, true));
        poisearch.searchPOIAsyn();
    }

    /**
     * 搜索Poi回调
     *
     * @param poiResult  搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {

        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getPois().size() > 0) {
                List<PoiItem> poiItems = poiResult.getPois();
                resultData.addAll(poiItems);
                searchResultAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(AmapActivity1.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AmapActivity1.this, "搜索失败，错误 " + resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ID搜索poi的回调
     *
     * @param poiItem    搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int resultCode) {

    }

    /**
     * Button点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.locbtn://重新定位
                startLocation();
                break;
            case R.id.checkinbtn://签到
                checkin();
                break;
            default:
                break;
        }
    }

    /**
     * 顶点签到，将签到点标注在地图上
     */
    private void checkin() {
        if (checkinMarker == null) {
            if (checkinpoint != null) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());
//                checkinMarker = aMap.addMarker(new MarkerOptions().position(checkinpoint).title("签到").snippet(date));
                Toast.makeText(AmapActivity1.this, "签到成功", Toast.LENGTH_SHORT).show();
            } else {
                startLocation();
                Toast.makeText(AmapActivity1.this, "请定位后重试，定位中。。。", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(AmapActivity1.this, "今日已签到", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
    }
}
