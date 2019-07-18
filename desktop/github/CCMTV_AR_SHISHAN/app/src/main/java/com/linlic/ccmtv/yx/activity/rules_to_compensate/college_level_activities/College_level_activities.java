package com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.yxzbjrrom.ScanEmptyActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.QRCodeUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.permission.Permissionutils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management.is_new_teaching_activities;

/**院级活动列表
 * Created by tom on 2019/1/10.
 */

public class College_level_activities extends BaseActivity {
    private Context context;
    @Bind(R.id.training_list)
    ListView training_list;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    @Bind(R.id.new_College_level)
    LinearLayout new_College_level;
    @Bind(R.id.manual_update)
    TextView manual_update;//手动更新
    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;//二维码展示区域
    @Bind(R.id.sign_in_button)
    TextView sign_in_button;//二维码 签到 按钮
    @Bind(R.id.type2)
    TextView type2;//弹出层 分类
    @Bind(R.id.title)
    TextView title;//弹出层 活动名称
    @Bind(R.id.code_img)
    ImageView code_img;//二维码manual_update
    @Bind(R.id.sign_back_button)
    TextView sign_back_button;//二维码 签退 按钮
    @Bind(R.id.sign_top_layout)
    LinearLayout sign_top_layout;//二维码 签退 按钮
    java.util.Timer timer = null;
    private Map<String, Object> curr_map = new HashMap<>();
    private String qr_type = "1";//1签到 2签退
    private Long set_time = 5000L;//二维码刷新时间
    public String  lecturer_isshow = "2"; //发布教学活动是否需要添加讲师模块　
    private int page = 1;
    private int pages = 1;
    private int limit = 20;
    private int count = 0;
    private boolean istime_submit = false;
    private String message_new = "";//发布教学活动 所用的备注
    private String fid = "";
    private final int REQUEST_CODE = 2;//请求码
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
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
                                pages = dataJson.getInt("pages");
                                if(dataJson.getString("issuance").equals("1")){
                                    new_College_level.setVisibility(View.VISIBLE);
                                }else{
                                    new_College_level.setVisibility(View.GONE);
                                }
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                if (page == 1) {
                                    listData.clear();
                                    set_time = dataJson.has("qrcode_reload_time")?dataJson.getLong("qrcode_reload_time") * 1000L:5000L;
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", dataJson1.getString("id"));
                                    map.put("title", dataJson1.getString("title"));
                                    map.put("start_time", dataJson1.getString("start_time"));
                                    map.put("end_time", dataJson1.getString("end_time"));
                                    map.put("address", dataJson1.getString("address"));
                                    map.put("cate_name", dataJson1.getString("cate_name"));
                                    map.put("status_name", dataJson1.getString("status_name"));
                                    map.put("year", dataJson1.getString("year"));
                                    map.put("is_show_sign_btn", dataJson1.getString("is_show_sign_btn"));
                                    map.put("is_author", dataJson1.getString("is_author"));
                                    map.put("far_out", dataJson1.getString("far_out"));
                                    map.put("pos", listData.size());
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();

                            } else {
                                if (page == 1) {
                                    listData.clear();
                                }
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(context, College_level_activities_Details.class);
                                intent.putExtra("id", dataJson.getString("id"));
                                intent.putExtra("fid", fid);

                                if (intent != null) {
                                    startActivity(intent);
                                }
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
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                if(dataJson.getJSONObject("data").getString("is_show_qrcode").equals("1")){
                                    Bitmap mBitmap = QRCodeUtil.createQRImage(dataJson.getJSONObject("data").getString("qrcode_url"), 480, 480);
                                    code_img.setImageBitmap(mBitmap);
                                    manual_update.setText("手动更新");
                                }else{
                                    if (timer != null) {
                                        timer.cancel();
                                        timer = null;
                                    }
                                    code_img.setImageResource(R.mipmap.qr_code_over);
                                    manual_update.setText("活动已经结束");
                                }

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
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            training_list.setVisibility(View.VISIBLE);
            tranining2_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }
            training_list.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.college_level_activities);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        initView();
        getUrlRulest();

    }
    public void submit_img(View view) {
        submit_layout.setVisibility(View.VISIBLE);
        if(!qr_type.equals("1")){
            qr_type = "1";
            sign_top_layout.setBackgroundResource(R.mipmap.college_level_icon03);
            sign_in_button.setTextColor(Color.parseColor("#ffffff"));
            sign_back_button.setTextColor(Color.parseColor("#666666"));
        }
        //动态二维码
        if (timer != null) {
            //已存在
            activitysSignQrCode();
        } else {

            timer = new java.util.Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    activitysSignQrCode();
                }
            };
            //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
            timer.schedule(task, 0, set_time);


        }

    }
    public void submit_img2(View view) {
        submit_layout.setVisibility(View.VISIBLE);
        if(!qr_type.equals("2")){
            qr_type = "2";
            sign_top_layout.setBackgroundResource(R.mipmap.college_level_icon04);
            sign_back_button.setTextColor(Color.parseColor("#ffffff"));
            sign_in_button.setTextColor(Color.parseColor("#666666"));

        }
        //动态二维码
        if (timer != null) {
            activitysSignQrCode();
            //已存在
        } else {

            timer = new java.util.Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    activitysSignQrCode();
                }
            };
            //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
            timer.schedule(task, 0, set_time);

        }
    }
    public void pupView(View view){
        submit_layout.setVisibility(View.VISIBLE);
        curr_map = listData.get(Integer.parseInt(view.getTag().toString()) );
        qr_type = "1";
        sign_top_layout.setBackgroundResource(R.mipmap.college_level_icon03);
        sign_in_button.setTextColor(Color.parseColor("#ffffff"));
        sign_back_button.setTextColor(Color.parseColor("#666666"));
        type2.setText(curr_map.get("cate_name").toString());
        title.setText(curr_map.get("title").toString());
        //动态二维码
        if (timer != null) {
            //已存在
        } else {
            timer = new java.util.Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    activitysSignQrCode();
                }
            };
            //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
            timer.schedule(task, 0, set_time);

        }
    }
    public void activitysSignQrCode() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysSignQrCode);
                    obj.put("id", curr_map.get("id"));
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("type",qr_type);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("签到签退二维码", result);
                    Message message = new Message();
                    message.what = 4;
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

    public void initView() {
        submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                submit_layout.setVisibility(View.GONE);
            }
        });
        manual_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manual_update.getText().toString().equals("手动更新")){
                    activitysSignQrCode();
                }
            }
        });

        baseListAdapterVideo = new BaseListAdapter(training_list, listData, R.layout.item_college_level_activities) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                switch (map.get("status_name").toString()){
                    case "已结束":
//                        helper.setTextColor2(R.id.status,map.get("title").toString()+"    ","已结束", Color.parseColor("#eeeeee"),Color.parseColor("#666666"));
                        helper.setTextColor2(R.id.status ,Color.parseColor("#999999"));
                        helper.setBackground_Image(R.id.status ,R.drawable.anniu52);
                        break;
                    case "进行中":
//                        helper.setTextReplacementSpan(R.id.status,map.get("title").toString()+"    ","进行中", Color.parseColor("#4492da"),Color.parseColor("#ffffff"));
                        helper.setTextColor2(R.id.status ,Color.parseColor("#3798F9"));
                        helper.setBackground_Image(R.id.status ,R.drawable.anniu53);
                        break;
                    case "未开始":
//                        helper.setTextReplacementSpan(R.id.status,map.get("title").toString()+"    ","  未开始  ", Color.parseColor("#efc66e"),Color.parseColor("#ffffff"));
                        helper.setTextColor2(R.id.status ,Color.parseColor("#999999"));
                        helper.setBackground_Image(R.id.status ,R.drawable.anniu52);
                        break;

                }
                if(map.get("far_out").toString().equals("1")){
                    helper.setVisibility(R.id._item_new,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_new,View.GONE);
                }
                if(map.get("is_show_sign_btn").toString().equals("1")){
                    helper.setVisibility(R.id._item_sign,View.VISIBLE);
                    helper.setTag(R.id._item_sign,map.get("pos").toString() );
                }else{
                    helper.setVisibility(R.id._item_sign,View.GONE);
                }
                helper.setText(R.id.item_content,map.get("title").toString() );
                helper.setText(R.id.status,map.get("status_name").toString() );
                helper.setText(R.id._item_button, map.get("cate_name").toString());
                helper.setText(R.id.item_year, map.get("year").toString());
                helper.setText(R.id.item_time, map.get("start_time").toString()+" 至 "+map.get("end_time").toString());
                helper.setText(R.id._item_address, map.get("address").toString());

            }
        };
        training_list.setAdapter(baseListAdapterVideo);
        // listview点击事件
        training_list.setOnItemClickListener(new  casesharing_listListener());
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = training_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = training_list.getChildAt(training_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == training_list.getHeight()) {
                        if (page<pages) {
                            page += 1;
                            getUrlRulest();
                        }
                    }
                }
            }
        });
    }




    @Override
    protected void onDestroy() {
        SharedPreferencesTools.saveEvent_details_status(context, "");
        super.onDestroy();
    }



    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listData.get(arg2);
            Intent intent = null;
                intent = new Intent(context, College_level_activities_Details.class);
                intent.putExtra("id", map.get("id").toString());
                intent.putExtra("fid", fid);

            if (intent != null) {
                startActivity(intent);
            }

        }

    }



    public void getUrlRulest() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    obj.put("title", "");

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("取院级活动列表", result);
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



    public void new_teaching_activities(View view) {
        Intent intent = new Intent(this, New_teaching_activities.class);
        intent.putExtra("fid", fid);
        intent.putExtra("message_new", message_new);
        intent.putExtra("lecturer_isshow", lecturer_isshow);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (is_new_teaching_activities) {
            is_new_teaching_activities = false;
            page = 1;
            getUrlRulest();
        }
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Activity/index.html";
        super.onPause();
    }

    public void clickCaptureActivity(View view) {
        requestCameraPermission(this);
    }

    public void new_college_level_activities(View view) {
       Intent intent = new Intent(context,New_College_level_activities.class);

        startActivity(intent);
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
                    if(result.contains("Activity_sign")){
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
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysUserSign);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("qrcode_url", url);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("扫描签到", result);
                    Message message = new Message();
                    message.what = 3;
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
