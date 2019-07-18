package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Courseware;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/8/29. 活动详情
 */

public class Event_Details4 extends BaseActivity {

    private Context context;
    @Bind(R.id.activity_name)
    TextView activity_name;//活动名
    @Bind(R.id.the_activity_type)
    TextView the_activity_type;//活动类型
    @Bind(R.id.start_time)
    TextView start_time;//开始时间
    @Bind(R.id.end_time)
    TextView end_time;//结束时间
    @Bind(R.id.place)
    TextView place;//地点
    @Bind(R.id.lecturer_layout)
    LinearLayout lecturer_layout;//讲师 模块 容器

    @Bind(R.id.lecturer_num)
    TextView lecturer_num;//讲师 人数字段
    @Bind(R.id.teaching_num)
    TextView teaching_num;//带教 人数字段
    @Bind(R.id.teaching_layout)
    LinearLayout teaching_layout;//带教 模块  容器
    public  List<Resident> teaching_data = new ArrayList<>();//带教 数据
    List<Courseware> coursewares = new ArrayList<>();//课件
    List<Courseware> accompanying_notes_datas = new ArrayList<>();//随堂笔记 data
    Map<String, Integer> accompanying_notes_pos = new HashMap<>();
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();//活动类型数据
    JSONObject result, data;
    private String fid = "";
    private String id = "";
    private String http = "";
    private String score = "";
    private Boolean accompanying_notes_isShow = true;//是否显示随堂笔记模块
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    List<Resident> lecturer_data = new ArrayList<>();//讲师 数据
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
                                JSONObject dateJson = dataJson.getJSONObject("dataList");
                                activity_name.setText(dateJson.getString("name"));
                                the_activity_type.setText(dateJson.getString("type_name"));
                                start_time.setText(dateJson.getString("add_time"));
                                end_time.setText(dateJson.getString("end_time"));
                                place.setText(dateJson.getString("place"));
                                coursewares.clear();
                                for (int i = 0; i < dateJson.getJSONArray("kejian").length(); i++) {
                                    JSONObject kejianJson = dateJson.getJSONArray("kejian").getJSONObject(i);
                                    Courseware courseware_bean = new Courseware();
                                    http =  kejianJson.getString("http");
                                    courseware_bean.setFile_name(kejianJson.getString("url_name"));
                                    courseware_bean.setFile_path( kejianJson.getString("url"));
                                    courseware_bean.setIs_upload(true);
                                    coursewares.add(courseware_bean);
                                }



                                //讲师
                                if(dateJson.getString("is_speaker").equals("1") && dateJson.has("speaker")){
                                    for (int i = 0; i < dateJson.getJSONArray("speaker").length(); i++) {
                                        JSONObject dataJson1 = dateJson.getJSONArray("speaker").getJSONObject(i);
                                        Resident resident = new Resident();
                                        resident.setId(dataJson1.getString("uid"));
                                        resident.setIs_select(false);
                                        resident.setName(dataJson1.getString("realname"));
                                        resident.setUsername(dataJson1.getString("username"));
                                        lecturer_data.add(resident);
                                    }
                                    if(lecturer_data.size()>0){
                                        lecturer_layout.setVisibility(View.VISIBLE);
                                    }else{
                                        lecturer_layout.setVisibility(View.GONE);
                                    }
                                    lecturer_num.setText(lecturer_data.size()+"人");
                                }else{
                                    lecturer_layout.setVisibility(View.GONE);
                                }

                                //带教
                                if(dateJson.has("is_teachers") && dateJson.getString("is_teachers").equals("1")  ){
                                    if( dateJson.has("teachers")){
                                        for (int i = 0; i < dateJson.getJSONArray("teachers").length(); i++) {
                                            JSONObject dataJson1 = dateJson.getJSONArray("teachers").getJSONObject(i);
                                            Resident resident = new Resident();
                                            resident.setId(dataJson1.getString("uid"));
                                            resident.setIs_select(false);
                                            resident.setName(dataJson1.getString("realname"));
                                            resident.setUsername(dataJson1.getString("username"));
                                            teaching_data.add(resident);
                                        }
                                        teaching_num.setText(teaching_data.size()+"人");
                                        teaching_layout.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    teaching_layout.setVisibility(View.GONE);
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        MyProgressBarDialogTools.hide();
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();

                                getUrlRulest();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for(int i = 0; i < accompanying_notes_datas.size() ; i++){
                                    if (dataJson.getString("dataList").equals(accompanying_notes_datas.get(i).getFile_path())) {
                                        accompanying_notes_datas.remove(accompanying_notes_datas.get(i));
                                    }
                                }

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
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
        setContentView(R.layout.event_details4);
        context = this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        fid = getIntent().getStringExtra("fid");
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUrlRulest();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/activities/index.html";
        super.onPause();
    }


    public void initView() {
        the_activity_type.setTextColor(Color.BLACK);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getActivitiesDetail);
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("查看教学活动详情", result);
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

    //打开系统的文件管理器
    public void openFileMenu(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //系统调用Action属性
        intent.setType("*/*");
        //设置文件类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 添加Category属性
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }



    public void open_Coursewares(View view){
        Intent intent = new Intent(context, com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Courseware.class);
        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("http",http);
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)coursewares);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据

        startActivity(intent);
    }
    public void selectLecturer(View view){
        Intent intent = new Intent(context, Event_Details_lecturer.class);
        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)lecturer_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }
    public void selectTeaching(View view){
        Intent intent = new Intent(context, Event_Details_Teaching.class);

        //把返回数据存入Intent
        Bundle bundle=new Bundle();
        bundle.putString("fid",fid);
        bundle.putString("id",id);
        bundle.putString("type","2");//1代表新发布或者活动未开始可编辑 2.代表活动已开始 不可编辑
        bundle.putSerializable("list",(Serializable)teaching_data);//序列化,要注意转化(Serializable)
        intent.putExtras(bundle);//发送数据
        startActivity(intent);
    }
       /*使用第三方软件获取文件*/

    public String getFilePathFromURI(Context context, Uri contentUri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }
}
