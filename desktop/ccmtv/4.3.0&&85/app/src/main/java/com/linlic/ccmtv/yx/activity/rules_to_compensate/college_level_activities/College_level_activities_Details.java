package com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.File_down;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.QRCodeUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.permission.Permissionutils;
import com.linlic.ccmtv.yx.widget.CustomDatePicker;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/29. 活动详情
 */

public class College_level_activities_Details extends BaseActivity {
    private Context context;
    @Bind(R.id.qr_code_layout)
    LinearLayout qr_code_layout;//二维码 容器
    @Bind(R.id.ed_layout)
    LinearLayout ed_layout;//编辑按钮 容器
    @Bind(R.id.activity_name)
    TextView activity_name;//活动名
    @Bind(R.id.the_activity_type)
    TextView the_activity_type;//活动类型
    @Bind(R.id.start_time)
    TextView start_time;//开始时间
    @Bind(R.id.place)
    TextView place;//地点
    @Bind(R.id.courseware)
    MyGridView courseware;//课件
    @Bind(R.id.user_layout)
    LinearLayout user_layout;//用户列表
    @Bind(R.id.sign)
    LinearLayout sign;//用户列表
    @Bind(R.id.user_num)
    TextView user_num;//用户数
    @Bind(R.id.user_names)
    TextView user_names;//用户名字
    @Bind(R.id.file_num)
    TextView file_num;//w文件数
    @Bind(R.id.sign_in)
    ImageView sign_in;//签到
    @Bind(R.id.sign_back)
    ImageView sign_back;//签退
    @Bind(R.id.code_img)
    ImageView code_img;//二维码
    @Bind(R.id.title)
    TextView title;//
    @Bind(R.id.manual_update)
    TextView manual_update;//手动更新
    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;//二维码展示区域
    @Bind(R.id.sign_in_button)
    TextView sign_in_button;//二维码 签到 按钮
    @Bind(R.id.sign_back_button)
    TextView sign_back_button;//二维码 签退 按钮
    @Bind(R.id.sign_top_layout)
    LinearLayout sign_top_layout;//二维码 签退 按钮
    @Bind(R.id.type2)
    TextView type2;//
    @Bind(R.id.submit_text)
    TextView submit_text;//
    @Bind(R.id.tis_text)
    TextView tis_text;//
    @Bind(R.id.tis_layout)
    LinearLayout tis_layout;//
    private String id = "";//教学活动ID
    private String qr_type = "1";//1签到 2签退
    List<Courseware> coursewares = new ArrayList<>();//课件
    CustomDatePicker customDatePicker1, customDatePicker2;
    java.util.Timer timer = null;
    private Long set_time = 5000L;//二维码刷新时间
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterCoursewares;
    private final int REQUEST_CODE = 2;//请求码
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
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                if(dateJson.has("status_msg") && dateJson.getString("status_msg").length()>0){
                                    tis_text.setText( dateJson.getString("status_msg"));
                                    tis_layout.setVisibility(View.VISIBLE);
                                }else{
                                    tis_layout.setVisibility(View.GONE);
                                }
                                set_time = dateJson.has("qrcode_reload_time")?dateJson.getLong("qrcode_reload_time") * 1000L:5000L;
                                if(dateJson.getString("type").equals("1")){
                                    //发布人
                                    user_layout.setVisibility(View.VISIBLE);
                                    sign.setVisibility(View.GONE);
                                    user_num.setText("("+dateJson.getString("user_count")+"人)");
                                    user_names.setText( dateJson.getString("user_list_name") );
                                }else{
                                    //学生
                                    user_layout.setVisibility(View.GONE);
                                    sign.setVisibility(View.VISIBLE);
                                    if(dateJson.getJSONObject("user_sign").getString("sign").equals("1")){
                                        //已签到
                                        sign_in.setImageResource(R.mipmap.delete_item_select2);
                                    }else{
                                        sign_in.setImageResource(R.mipmap.training_10);
                                    }
                                    if(dateJson.getJSONObject("user_sign").getString("out").equals("1")){
                                        //已签退
                                        sign_back.setImageResource(R.mipmap.delete_item_select2);
                                    }else{
                                        sign_back.setImageResource(R.mipmap.training_10);
                                    }


                                }
                                switch (dateJson.getString("status")){
                                    case "0"://不显示
                                        qr_code_layout.setVisibility(View.GONE);
                                        ed_layout.setVisibility(View.GONE);
                                        break;
                                    case "1"://显示二维码
                                        qr_code_layout.setVisibility(View.VISIBLE);
                                        ed_layout.setVisibility(View.GONE);
                                        break;
                                    case "2"://显示编辑
                                        qr_code_layout.setVisibility(View.GONE);
                                        ed_layout.setVisibility(View.VISIBLE);
                                        break;
                                    default:
                                        qr_code_layout.setVisibility(View.GONE);
                                        ed_layout.setVisibility(View.GONE);
                                        break;
                                }
                                activity_name.setText(dateJson.getString("title"));
                                title.setText(dateJson.getString("title"));
                                type2.setText(dateJson.getString("cate_name"));
                                the_activity_type.setText(dateJson.getString("cate_name"));
                                start_time.setText(dateJson.getString("start_time")+"至"+dateJson.getString("end_time"));
                                place.setText(dateJson.getString("address"));
                                coursewares.clear();
                                for (int i = 0; i < dateJson.getJSONArray("files").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("files").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    courseware_bean.setFile_name(kejianJson.getString("name"));
                                    courseware_bean.setFile_path(kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    coursewares.add(courseware_bean);
                                }
                                file_num.setText("("+coursewares.size()+"个)");

                                baseListAdapterCoursewares.notifyDataSetChanged();


                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                getUrlRulest();
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
        setContentView(R.layout.college_level_activities_details);
        context = this;
        ButterKnife.bind(this);
        initView();
        id = getIntent().getStringExtra("id");
        getUrlRulest();
    }
    public void new_college_level_activities(View view) {
        Intent intent = new Intent(context,New_College_level_activities.class);
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
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

    public void initView() {
        tis_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tis_layout.setVisibility(View.GONE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tis_layout.setVisibility(View.GONE);
            }
        });
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
                if(manual_update.getText().toString().equals("手动更新")) {
                    activitysSignQrCode();
                }
            }
        });




        baseListAdapterCoursewares = new BaseListAdapter(courseware, coursewares, R.layout.item_coursewares3) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Courseware map = (Courseware) item;
                helper.setText(R.id._item_text, map.getFile_name());
            }
        };
        courseware.setAdapter(baseListAdapterCoursewares);
        courseware.setSelector(new ColorDrawable(Color.TRANSPARENT));
        courseware.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Courseware courseware_bean = coursewares.get(position);
                ArrayList urls_huanzhexinxi = null;
                Intent intent = null;
                switch (courseware_bean.getFile_name().substring(courseware_bean.getFile_name().lastIndexOf(".") + 1, courseware_bean.getFile_name().length())) {
                    case "png":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add( courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpg":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add( courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpeg":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add( courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "bmp":
                        urls_huanzhexinxi = new ArrayList();
                        urls_huanzhexinxi.add(  courseware_bean.getFile_path());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls_huanzhexinxi);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    default:
                        Intent intent1 = new Intent(context, File_down.class);
                        intent1.putExtra("file_path",  courseware_bean.getFile_path());
                        intent1.putExtra("file_name", courseware_bean.getFile_path().substring(courseware_bean.getFile_path().lastIndexOf("/") + 1, courseware_bean.getFile_path().length()));
                        startActivity(intent1);
                        break;
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        try {
            customDatePicker1.dismissDialog();
            customDatePicker2.dismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        try {
            if(timer!=null){
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysInfo);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("查看院级活动详情", result);
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
    public void activitysSignQrCode() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysSignQrCode);
                    obj.put("id", id);
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

    public void clickCaptureActivity(View view) {
        requestCameraPermission(this);
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
                    activitiesSign(result);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Activity/index.html";
        super.onPause();
    }

    public void users(View view){
        Intent intent = new Intent(context,College_level_activities_Participant.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }

}
