package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.DoubleTimeSelectDialog;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/12/11.
 */

public class Attendance_list extends BaseActivity {

    private Context context;

    @Bind(R.id.date_img)//日期img
            ImageView date_img;
    @Bind(R.id.the_activity_type)//日期img
            NiceSpinner the_activity_type;
    @Bind(R.id.time_text)
    TextView time_text;
    @Bind(R.id.time_start)
    TextView time_start;
    @Bind(R.id.attendance_addr)
    TextView attendance_addr;
    @Bind(R.id.recording_list)
    ListView recording_list;
    @Bind(R.id.amap_layout)//地图容器
            LinearLayout amap_layout;
    @Bind(R.id.map)//地图容器
            MapView mapView;
    //初始化地图控制器对象
    AMap aMap;
    private List<String> spinner_list = new ArrayList<>(),allStatus_list = new ArrayList<>();//活动类型数据
    Map<String, Object> spinner_map = new HashMap<>();
    private BaseListAdapter baseListAdapterRecording;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private int page = 1;
    private int pages = 0;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
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



                                JSONArray dateJson = dataJson.getJSONArray("data") ;
                                if (page == 1) {
                                    JSONArray status_list = dataJson.getJSONArray("status_list") ;
                                    if(status_list.length()>0){
                                        spinner_list.clear();
                                        spinner_map.clear();
                                        for(int i = 0; i<status_list.length();i++){
                                            JSONObject dataJson1 = status_list.getJSONObject(i);
                                            spinner_map.put(dataJson1.getString("status_name"),dataJson1.getString("status_id") );
                                            spinner_list.add(dataJson1.getString("status_name"));
                                        }
                                    }
                                    listData.clear();
                                }
                                pages = dataJson. getInt("pages");

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("date", dataJson1.getString("date"));
                                    map.put("status", dataJson1.getString("status"));
                                    map.put("start_time", dataJson1.getString("start_time"));
                                    map.put("start_date_time", dataJson1.getString("start_date_time"));
                                    map.put("start_longitude", dataJson1.getString("start_longitude"));
                                    map.put("start_sign_address", dataJson1.getString("start_sign_address"));
                                    map.put("start_sign_port", dataJson1.getString("start_sign_port"));
                                    map.put("end_time", dataJson1.getString("end_time"));
                                    map.put("end_date_time", dataJson1.getString("end_date_time"));
                                    map.put("end_longitude", dataJson1.getString("end_longitude"));
                                    map.put("end_sign_address", dataJson1.getString("end_sign_address"));
                                    map.put("end_sign_port", dataJson1.getString("end_sign_port"));
                                    map.put("position", listData.size());

                                    if(i>0){
                                        map.put("is", i%2==0?0:1);
                                    }else{
                                        map.put("is", 0);
                                    }
                                    listData.add(map);
                                }
                                baseListAdapterRecording.notifyDataSetChanged();

                            } else {
                                if (page == 1) {
                                    listData.clear();
                                }
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.attendance_list);
        context = this;
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        findId();
        initView();


    }

    public void initView() {
        time_text.setText(DateUtil.dataToStr(DateUtil.getFirstDayByMonth(DateUtil.getCurrentDate("yyyy-MM-dd")),"yyyy-MM-dd")+"~"+DateUtil.getCurrDate("yyyy-MM-dd"));
        LinearLayout  hearderViewLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_attendance_top,null);
        recording_list.addHeaderView(hearderViewLayout);

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                String now = sdf.format(new Date());
//                customDatePicker1.show(now);

                showCustomTimePicker(  time_text);
            }
        });
        amap_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amap_layout.setVisibility(View.GONE);
                aMap.clear();
            }
        });
        baseListAdapterRecording = new BaseListAdapter(recording_list, listData, R.layout.item_attendance_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                if(map.get("is").toString().equals("1")){
                    helper.setBackground_color(R.id._item_layout, Color.parseColor("#F0F4F7"));
                }else{
                    helper.setBackground_color(R.id._item_layout, Color.parseColor("#FFFFFF"));
                }
                helper.setText(R.id._item_text1, map.get("date").toString());
                helper.setText(R.id._item_text2, map.get("status").toString());
                helper.setText(R.id._item_text3,  map.get("start_time").toString());
                helper.setTag(R.id._item_text3,  map.get("position").toString());
                helper.setText(R.id._item_text4,  map.get("end_time").toString());
                helper.setTag(R.id._item_text4,  map.get("position").toString());
            }
        };
        recording_list.setAdapter(baseListAdapterRecording);
        baseListAdapterRecording.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = recording_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = recording_list.getChildAt(recording_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == recording_list.getHeight()) {
                        if (pages>page) {
                            page += 1;
                            userSignLogList();
                        }
                    }
                }
            }
        });
        initthe_activity_type();
        if(time_text.getText().toString().trim().length()>0){
            page = 1;
            userSignLogList();
        }
    }

    public void start_qd(View view){
        Map map = (Map)listData.get(Integer.parseInt(view.getTag().toString()));
        LogUtil.e("点击查看",map.toString());
        ArrayList<MarkerOptions> options = new ArrayList<MarkerOptions>();
        if(map.get("start_longitude").toString().trim().length()>0 && !map.get("start_longitude").toString().equals("- -")){
            String startLatAndLng[] =map.get("start_longitude").toString().split("[$$$]");
            List<String> startLatAndLngList = new ArrayList<String>();
            for(String str:startLatAndLng){
                LogUtil.e("点击查看",str);
                if(str.trim().length()>0){
                    startLatAndLngList.add(str);
                }
            }
            if(startLatAndLngList.size()>1){
                Double startlat =Double.parseDouble(startLatAndLngList.get(1));
                Double startlng =Double.parseDouble(startLatAndLngList.get(0));
                LatLng latLng = new LatLng(startlat,startlng);
                options.add(new MarkerOptions().position(latLng).title("签到").snippet(map.get("start_date_time").toString()).draggable(false));
            }

        }

        if(map.get("start_time").toString().equals("- -") ){
            amap_layout.setVisibility(View.GONE);
            Toast.makeText(context
                    , "无考勤信息",
                    Toast.LENGTH_SHORT).show();
        }else{
            amap_layout.setVisibility(View.VISIBLE);
        }
        if(options.size()>0){
            ArrayList<Marker> marker = aMap.addMarkers(options,false);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom( marker.get(0).getPosition(), 16f));
            mapView.setVisibility(View.VISIBLE);
        }else {
            mapView.setVisibility(View.GONE);
        }
        time_start.setText("时间："+map.get("start_date_time").toString() );
        attendance_addr.setText("地点："+map.get("start_sign_address").toString());
    }

    public void end_qd(View view){
        Map map = (Map)listData.get(Integer.parseInt(view.getTag().toString()));
        ArrayList<MarkerOptions> options = new ArrayList<MarkerOptions>();

        if(map.get("end_longitude").toString().trim().length()>0 && !map.get("start_longitude").toString().equals("- -") ) {
            String endLatAndLng[] = map.get("end_longitude").toString().split("[$$$]");
            List<String> endLatAndLngList = new ArrayList<String>();
            for(String str:endLatAndLng){
                LogUtil.e("点击查看",str);
                if(str.trim().length()>0){
                    endLatAndLngList.add(str);
                }
            }
            if(endLatAndLngList.size()>1){
                Double endlat = Double.parseDouble(endLatAndLngList.get(1));
                Double endlng = Double.parseDouble(endLatAndLngList.get(0));
                LatLng latLng2 = new LatLng(endlat, endlng);
                options.add(new MarkerOptions().position(latLng2).title("签退").snippet(map.get("end_date_time").toString()).draggable(false));
            }

        }
        if( map.get("end_time").toString().equals("- -") ){
            amap_layout.setVisibility(View.GONE);
            Toast.makeText(context
                    , "无考勤信息",
                    Toast.LENGTH_SHORT).show();
        }else{
            amap_layout.setVisibility(View.VISIBLE);
        }
        if(options.size()>0){
            ArrayList<Marker> marker = aMap.addMarkers(options,false);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom( marker.get(0).getPosition(), 16f));
            mapView.setVisibility(View.VISIBLE);
        }else {
            mapView.setVisibility(View.GONE);
        }
        time_start.setText("时间："+map.get("end_date_time").toString() );
        attendance_addr.setText("地点："+map.get("end_sign_address").toString());
    }

    public void userSignLogList() {
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
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.userSignLogList);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                            obj.put("time", time_text.getText().toString());
                            obj.put("page", page);
                            obj.put("status_id", spinner_map.containsKey(the_activity_type.getText().toString())?spinner_map.get(the_activity_type.getText().toString()):0);
                            obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));

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

    public void showCustomTimePicker(final TextView textView) {
        String beginDeadTime = "2000-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, DateUtil.dataToStr(DateUtil.getFirstDayByMonth(DateUtil.getCurrentDate("yyyy-MM-dd")),"yyyy-MM-dd"), DateUtil.getCurrDate("yyyy-MM-dd"));
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    textView.setText(startTime+ "~" + endTime );
                    page = 1;
                    userSignLogList();
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }

    public void initthe_activity_type(){
        spinner_list.add("全部");
        the_activity_type.setTextColor(Color.BLACK);
        the_activity_type.attachDataSource(spinner_list);
        the_activity_type.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        the_activity_type.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                page = 1;
                userSignLogList();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
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
}
