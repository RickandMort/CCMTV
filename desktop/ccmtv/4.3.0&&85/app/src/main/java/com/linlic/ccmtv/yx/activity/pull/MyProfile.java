package com.linlic.ccmtv.yx.activity.pull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity;
import com.linlic.ccmtv.yx.adapter.MyAlert;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：我的资料(注册第二页)
 * <p/>
 * author: Mr.song
 * 时间：2016-2-23 下午1:01:01
 *
 * @author Administrator
 */
public class MyProfile extends BaseActivity {
    private TextView activity_title_name;//顶部title
    private ImageView myprofile_img;//头像
    private Button myprofile_department;//科室
    private Button myprofile_title;//职称
    private Button myprofile_viptype;//职称
    private Button myprofile_sex;//性别
    private Button myprofile_hospital;//医院
    private EditText myprofile_truename;//真实姓名
    private EditText myprofile_address;//地址
    private EditText myprofile_idcard;//身份证号码
    private EditText edit_myprofile_danwei;//单位
    //传入数组或list<String>
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private String DepartmentMaxId;//科室大类id
    private String DepartmentMaxName;//科室大类name
    private String DepartmentMinId;//科室大类id
    private String DepartmentMinName;//科室大类name
    private String TitleId;//职称id
    private String TitleName;//职称name
    private String VipTypeId;//会员类别id
    private String VipTypeName;//会员类别name
    private String SexId;//性别id
    private String SexName;//性别name
    private String provinceId;//省 id
    private String provinceName;//省 name
    private String cityId;//市 id
    private String cityName;//市 name
    private String hospitalId;//医院 id
    private String hospitalName;//医院 name
    private TextView myprofile_department_max, myprofile_department_min;
    private File file;
    private LinearLayout layout_zhicheng, layout_yiyuan, layout_danwei, layout_keshi,profile_layout;
    private String myprofile_danwei;
    Context context;
    private String aid;

    String Str_icon, Str_username, Str_idcard_yz_reason, Str_idcard_imgurl, Str_cityid, Str_hyleibie, Str_cityname, Str_phonenum, Str_bigkeshi, Str_personalmoney, Str_smallkeshi, Str_zhicheng, Str_truename, Str_sexName, Str_address, Str_idcard, Str_sex, Str_danwei;

    //身份证是否显示 0 为隐藏 1为显示
    private String hideTag = "1";
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            final int MyAlertWhat = msg.what;
            if (MyAlertWhat == 150 || MyAlertWhat == 188 || MyAlertWhat == 158) {//弹框
                MyAlert.Builder bu = new MyAlert.Builder(MyProfile.this);
                bu.setTitle(msg.obj + "");
                bu.setMessage(list);
                bu.setPositiveButton("取消");
                bu.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                        TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
                        String id = textview.getText().toString();
                        TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
                        String name = textView.getText().toString();
                        if (MyAlertWhat == 150) {
                            VipTypeId = id;
                            VipTypeName = name;
                            myprofile_viptype.setText(VipTypeName);
                            if (!"医务工作者".equals(VipTypeName)) {
                                layout_keshi.setVisibility(View.GONE);
                                layout_zhicheng.setVisibility(View.GONE);
                                layout_yiyuan.setVisibility(View.GONE);
                                layout_danwei.setVisibility(View.VISIBLE);
                            } else {
                                layout_keshi.setVisibility(View.VISIBLE);
                                layout_zhicheng.setVisibility(View.VISIBLE);
                                layout_yiyuan.setVisibility(View.VISIBLE);
                                layout_danwei.setVisibility(View.GONE);
                            }
                        } else if (MyAlertWhat == 188) {
                            TitleId = id;
                            TitleName = name;
                            myprofile_title.setText(TitleName);
                        } else if (MyAlertWhat == 158) {
                            SexId = id;
                            SexName = name;
                            myprofile_sex.setText(SexName);
                        }
                    }
                });
                bu.create().show();
            }
            switch (msg.what) {
                case 1://

                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            hideTag = result.getJSONObject("data").getString("hide");
                            if(result.getJSONObject("data").getString("hide").equals("0")){
                                //身份证是否显示 0 为隐藏 1为显示
                                profile_layout.setVisibility(View.GONE);
                            }else{
                                profile_layout.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 303:
                    Toast.makeText(context, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    // if (getIntent().getStringExtra("source").equals("register")) {
                    //startActivity(new Intent(MyProfile.this, MainActivity.class));
                    if (getIntent().getStringExtra("source").equals("videoFive")) {
                        Intent intent = new Intent(MyProfile.this, VideoFive.class);
                        intent.putExtra("aid", aid);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MyProfile.this, MainActivity.class);
                        intent.putExtra("type", "register");
                        startActivity(intent);
                    }
                    MyProgressBarDialogTools.hide();
                    MyProfile.this.finish();
                    break;
                case 305:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            SharedPreferencesTools.savePerfectInformation(MyProfile.this, msg.obj + "");
                            JSONObject object = result.getJSONObject("data");
                            Str_hyleibie = object.getString("hyleibie");
                            Str_bigkeshi = object.getString("keshilb");         //科室
                            Str_smallkeshi = object.getString("keshi");
                            Str_zhicheng = object.getString("my_694");          //职称
                            Str_truename = object.getString("truename");        //正式姓名
                            Str_sexName = object.getString("sexName");          //性别
                            Str_address = object.getString("address");          //联系地址
                            Str_idcard = object.getString("idcard");
                            Str_sex = object.getString("sex");                  //性别代号
                            Str_phonenum = object.getString("mobphone");        //手机号码
                            Str_username = object.getString("username");
                            Str_personalmoney = object.getString("personalmoney");//收银台
                            Str_idcard_yz_reason = object.getString("idcard_yz_reason");
                            Str_idcard_imgurl = object.getString("idcard_imgurl");
                            DepartmentMaxId = object.getString("keshilbxb");

                            JSONObject obj = object.getJSONObject("hosName");
                            Str_cityname = obj.getString("name");                //医院
                            Str_cityid = obj.getString("id");                   //医院代号
                            Str_danwei = object.getString("danwei");


                            if (!"医务工作者".equals(Str_hyleibie)) {
                                layout_keshi.setVisibility(View.GONE);
                                layout_zhicheng.setVisibility(View.GONE);
                                layout_yiyuan.setVisibility(View.GONE);
                                layout_danwei.setVisibility(View.VISIBLE);
                            } else {
                                layout_keshi.setVisibility(View.VISIBLE);
                                layout_zhicheng.setVisibility(View.VISIBLE);
                                layout_yiyuan.setVisibility(View.VISIBLE);
                                layout_danwei.setVisibility(View.GONE);
                            }
                            if (("").equals(Str_hyleibie) || Str_hyleibie == null) {
                                myprofile_viptype.setText("请选择");
                            } else {
                                myprofile_viptype.setText(Str_hyleibie);
                            }
                            if (("").equals(Str_cityname) || Str_cityname == null) {
                                myprofile_hospital.setText("请选择");
                            } else {
                                myprofile_hospital.setText(Str_cityname);
                            }
                            if (("").equals(Str_bigkeshi) || Str_bigkeshi == null) {
                                myprofile_department_max.setText("请选择");
                            } else {
                                myprofile_department_max.setText(Str_bigkeshi);
                            }
                            if (("").equals(Str_smallkeshi) || Str_smallkeshi == null) {
                                myprofile_department_min.setText("请选择");
                            } else {
                                myprofile_department_min.setText(Str_smallkeshi);
                            }
                            if (("").equals(Str_zhicheng) || Str_zhicheng == null) {
                                myprofile_title.setText("请选择");
                            } else {
                                myprofile_title.setText(Str_zhicheng);
                            }
                            myprofile_truename.setText(Str_truename);
                            myprofile_sex.setText(Str_sexName);
                            myprofile_address.setText(Str_address);
                            myprofile_idcard.setText(Str_idcard);
                            edit_myprofile_danwei.setText(Str_danwei);
                            hospitalId = Str_cityid;
                            DepartmentMaxName = Str_bigkeshi;
                            DepartmentMinName = Str_smallkeshi;
                            SexId = Str_sex;

                        } else {//失败
                            Toast.makeText(MyProfile.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    //String Str_hyleibie, Str_name, Str_keshi, Str_zhicheng, Str_truename, Str_sex, Str_address, Str_idcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        context = this;
        judgeIsGpRole();
        findViewById();
        //获取跳转数据
        getMyIntent();
        //设置顶部导航文字
        activity_title_name.setText(R.string.myprofile_title_name);
        setclick();
        revisePerfection();
    }

    private void judgeIsGpRole() {
        if (SharedPreferencesTools.getIsdocexam(context)) {
            //用户身份类型  3医考  1规培生 2医院正式员工
//            Log.e("MyFragment2", "onClick: 用户身份类型："+SharedPreferencesTools.getGp_type(context));
            if (SharedPreferencesTools.getGp_type(context).equals("1") || SharedPreferencesTools.getGp_type(context).equals("2")) {
                if (!getIntent().getStringExtra("source").equals("videoFive")) {
                    Intent intent = new Intent(context, GpPersonalInformationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    public void setclick() {
        myprofile_department_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myprofile_department_max.getText().toString().equals("请选择")) {
                    getDepartment(v);
                } else {
                    Intent intent = new Intent(context, DepartmentMin2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("departmentMaxId", DepartmentMaxId);
                    bundle.putString("departmentMaxName", myprofile_department_max.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 20);
                }
            }
        });
    }

    private void getMyIntent() {

        if (getIntent().getStringExtra("source").equals("my")) {
            //我的资料跳转过来
//            setViewText();
            getUserInfo();

        } else if (getIntent().getStringExtra("source").equals("register")) {
            //注册跳转过来
        } else if (getIntent().getStringExtra("source").equals("videoFive")) {
            //从播放页检测到需要完善资料跳转过来
            getUserInfo();
            aid = getIntent().getStringExtra("aid");
        }
    }

    private void getUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", SharedPreferencesTools.getUid(MyProfile.this));
                    object.put("act", URLConfig.getUserInfo);
                    String result = HttpClientUtils.sendPost(MyProfile.this, URLConfig.CCMTVAPP, object.toString());
//                    Log.e("MyProfile", "getUserInfo用户个人信息："+result);
                    Message message = new Message();
                    message.what = 305;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    private void setViewText() {
        String Str_hyleibie = getIntent().getStringExtra("Str_hyleibie");
        if (!"医务工作者".equals(Str_hyleibie)) {
            layout_keshi.setVisibility(View.GONE);
            layout_zhicheng.setVisibility(View.GONE);
            layout_yiyuan.setVisibility(View.GONE);
            layout_danwei.setVisibility(View.VISIBLE);
        } else {
            layout_keshi.setVisibility(View.VISIBLE);
            layout_zhicheng.setVisibility(View.VISIBLE);
            layout_yiyuan.setVisibility(View.VISIBLE);
            layout_danwei.setVisibility(View.GONE);
        }
        if (("").equals(Str_hyleibie) || Str_hyleibie == null) {
            myprofile_viptype.setText("请选择");
        } else {
            myprofile_viptype.setText(Str_hyleibie);
        }
        String Str_cityname = getIntent().getStringExtra("Str_cityname");
        if (("").equals(Str_cityname) || Str_cityname == null || ("null").equals(Str_cityname.trim())) {
            myprofile_hospital.setText("请选择");
        } else {
            myprofile_hospital.setText(Str_cityname);
        }
        String Str_bigkeshi = getIntent().getStringExtra("Str_bigkeshi");
        if (("").equals(Str_bigkeshi) || Str_bigkeshi == null) {
            myprofile_department.setText("请选择");
        } else {
            myprofile_department.setText(getIntent().getStringExtra("Str_bigkeshi") + "  " + getIntent().getStringExtra("Str_smallkeshi"));
        }
        String Str_zhicheng = getIntent().getStringExtra("Str_zhicheng");
        if (("").equals(Str_zhicheng) || Str_zhicheng == null) {
            myprofile_title.setText("请选择");
        } else {
            myprofile_title.setText(getIntent().getStringExtra("Str_zhicheng"));
        }
        myprofile_truename.setText(getIntent().getStringExtra("Str_truename"));
        myprofile_sex.setText(getIntent().getStringExtra("Str_sexName"));
        myprofile_address.setText(getIntent().getStringExtra("Str_address"));
        myprofile_idcard.setText(getIntent().getStringExtra("Str_idcard"));
        edit_myprofile_danwei.setText(getIntent().getStringExtra("Str_danwei"));
        hospitalId = getIntent().getStringExtra("Str_cityid");
        DepartmentMaxName = getIntent().getStringExtra("Str_bigkeshi");
        DepartmentMinName = getIntent().getStringExtra("Str_smallkeshi");
        SexId = getIntent().getStringExtra("Str_sex");
    }

    private void findViewById() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        myprofile_department = (Button) findViewById(R.id.myprofile_department);
        myprofile_title = (Button) findViewById(R.id.myprofile_title);
        myprofile_viptype = (Button) findViewById(R.id.myprofile_viptype);
        myprofile_sex = (Button) findViewById(R.id.myprofile_sex);
        myprofile_hospital = (Button) findViewById(R.id.myprofile_hospital);
        myprofile_img = (ImageView) findViewById(R.id.myprofile_img);
        myprofile_truename = (EditText) findViewById(R.id.myprofile_truename);
        myprofile_address = (EditText) findViewById(R.id.myprofile_address);
        myprofile_idcard = (EditText) findViewById(R.id.myprofile_idcard);
        layout_keshi = (LinearLayout) findViewById(R.id.layout_keshi);
        layout_zhicheng = (LinearLayout) findViewById(R.id.layout_zhicheng);
        layout_yiyuan = (LinearLayout) findViewById(R.id.layout_yiyuan);
        layout_danwei = (LinearLayout) findViewById(R.id.layout_danwei);
        profile_layout = (LinearLayout) findViewById(R.id.profile_layout);
        edit_myprofile_danwei = (EditText) findViewById(R.id.edit_myprofile_danwei);
        myprofile_department_max = (TextView) findViewById(R.id.myprofile_department_max);
        myprofile_department_min = (TextView) findViewById(R.id.myprofile_department_min);

        myprofile_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHospitalMyProfile(v);
            }
        });
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

    /**
     * name：单位选择
     * <p/>
     * author: Mr.song
     * 时间：2016-3-1 上午10:05:30
     *
     * @param view
     */
    public void getHospitalMyProfile(View view) {
        if (!SharedPreferencesTools.getIsdocexam(this) && !myprofile_viptype.getText().toString().equals("请选择")) {
            startActivityForResult(new Intent(MyProfile.this, HospitalProvince.class), 18);
        }
    }

    /**
     * name：科室选择
     * <p/>
     * author: Mr.song
     * 时间：2016-2-25 下午7:00:12
     *
     * @param view
     */
    public void getDepartment(View view) {
        startActivityForResult(new Intent(MyProfile.this, DepartmentMax.class), 16);
    }

    /**
     * name：职称选择
     * <p/>
     * author: Mr.song
     * 时间：2016-2-29 下午3:30:12
     *
     * @param view
     */
    public void getTitle(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.docPositions);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());

                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        //成功
                        JSONArray array = json.getJSONArray("doc_list");
                        list.clear();//使用list前先清空list
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject object = array.getJSONObject(i);
                            map.put("id", object.getString("id"));
                            map.put("name", object.getString("name"));
                            list.add(map);
                        }
                        handler.sendMessage(handler.obtainMessage(188, "请选择职称"));
                    } else {
                        //失败
                        handler.sendMessage(handler.obtainMessage(303, json.getString("errorMessage")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name：会员类型选择
     * <p/>
     * author: Mr.song
     * 时间：2016-2-29 下午3:30:12
     *
     * @param view
     */
    public void getVipType(View view) {
        if(Utils.isFastDoubleClick(view)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.memberStates);

                        String result = HttpClientUtils.sendPost(context,
                                URLConfig.CCMTVAPP, obj.toString());

                        JSONObject json = new JSONObject(result);
                        if (json.getInt("status") == 1) {
                            //成功
                            JSONArray array = json.getJSONArray("member_list");
                            list.clear();//使用list前先清空list
                            for (int i = 0; i < array.length(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                JSONObject object = array.getJSONObject(i);
                                map.put("id", object.getString("id"));
                                map.put("name", object.getString("name"));
                                list.add(map);
                            }
                            handler.sendMessage(handler.obtainMessage(150, "请选择会员类型"));
                        } else {
                            //失败
                            handler.sendMessage(handler.obtainMessage(303, json.getString("errorMessage")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

    /**
     * name：性别选择
     * <p/>
     * author: Mr.song
     * 时间：2016-3-1 上午10:05:30
     *
     * @param view
     */
    public void getSex(View view) {
        list.clear();//使用list前先清空list
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", "1");
        map1.put("name", "男");
        list.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", "2");
        map2.put("name", "女");
        list.add(map2);

        handler.sendMessage(handler.obtainMessage(158, "请选择性别"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 18://单位
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        provinceName = data.getStringExtra("provinceName");
                        cityId = data.getStringExtra("cityId");
                        cityName = data.getStringExtra("cityName");
                        hospitalName = data.getStringExtra("hospitalName");
                        provinceId = data.getStringExtra("provinceId");
                        hospitalId = data.getStringExtra("hospitalId");
                        myprofile_hospital.setText(hospitalName);
                    }
                    break;
                case 16://科室
                    Bundle bun = data.getExtras();
                    if (bun != null) {
                        DepartmentMaxId = data.getStringExtra("departmentMaxId");
                        DepartmentMaxName = data.getStringExtra("departmentMaxName");
                        DepartmentMinId = data.getStringExtra("departmentMinId");
                        DepartmentMinName = data.getStringExtra("departmentMinName");
                        myprofile_department_max.setText(DepartmentMaxName);
                        myprofile_department_min.setText(DepartmentMinName);
//                        myprofile_department.setText(DepartmentMaxName + "  " + DepartmentMinName);
                    }
                    break;
                case 20://科室
                    Bundle bun2 = data.getExtras();
                    if (bun2 != null) {
                        DepartmentMaxId = data.getStringExtra("departmentMaxId");
                        DepartmentMaxName = data.getStringExtra("departmentMaxName");
                        DepartmentMinId = data.getStringExtra("departmentMinId");
                        DepartmentMinName = data.getStringExtra("departmentMinName");
                        myprofile_department_max.setText(DepartmentMaxName);
                        myprofile_department_min.setText(DepartmentMinName);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    /**
     * name：提交
     * <p/>
     * author: Mr.song
     * 时间：2016-3-1 下午6:58:17
     *
     * @param view
     * @throws JSONException
     */
    public void MyprofileSubmit(View view) throws JSONException {
        final String truename = myprofile_truename.getText().toString();
        final String address = myprofile_address.getText().toString();
        final String idcard = myprofile_idcard.getText().toString();
        final String danwei;
        final String yiyuan = myprofile_hospital.getText().toString();
        final String Department_max = myprofile_department_max.getText().toString();
        final String Department_min = myprofile_department_min.getText().toString();
        VipTypeName = myprofile_viptype.getText().toString();

        SexName = myprofile_sex.getText().toString();
        if ("医务工作者".equals(VipTypeName)) {
            danwei = "";
            TitleName = myprofile_title.getText().toString();
        } else {
            danwei = edit_myprofile_danwei.getText().toString();
            DepartmentMaxName = "";
            DepartmentMinName = "";
            TitleName = "";
            hospitalId = "";
        }
        final String uid = SharedPreferencesTools.getUid(MyProfile.this);
        if (uid.equals("")) return;
        //showCustomLoading();
        if (profile_layout.getVisibility() == View.VISIBLE && idcard.trim().length() != 18)  {
            Toast.makeText(MyProfile.this, "身份证号码为18位，请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
            //MyProgressBarDialogTools.show(context);
            if (TextUtils.isEmpty(VipTypeName)) {
                Toast.makeText(context, "会员类别不可为空", Toast.LENGTH_SHORT).show();
                return;
            }
            /*if (TextUtils.isEmpty(danwei) && layout_danwei.getVisibility() == View.VISIBLE) {
                Toast.makeText(context, "单位不可为空", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (TextUtils.isEmpty(hospitalId) && layout_yiyuan.getVisibility() == View.VISIBLE) {
                Toast.makeText(context, "医院不可为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (layout_keshi.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(Department_max) || Department_max.equals("请选择") || TextUtils.isEmpty(Department_min) || Department_min.equals("请选择")) {
                    Toast.makeText(context, "科室不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (layout_zhicheng.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(TitleName) || TitleName.equals("请选择")) {
                    Toast.makeText(context, "职称不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (TextUtils.isEmpty(truename)) {
                Toast.makeText(context, "真实姓名不可为空", Toast.LENGTH_SHORT).show();
                myprofile_truename.requestFocus();
                return;
            }
        /*if (TextUtils.isEmpty(SexId)) {
            Toast.makeText(context, "性别不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(context, "联系地址不可为空", Toast.LENGTH_SHORT).show();
            myprofile_address.requestFocus();
            return;
        }*/
        if (!"0".equals(hideTag)) {
            if (TextUtils.isEmpty(idcard)) {
                Toast.makeText(context, "身份证号码不可为空", Toast.LENGTH_SHORT).show();
                myprofile_idcard.requestFocus();
                return;
            }
        }
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", uid);
                        object.put("act", URLConfig.completeUserinfo);
                        if ("请选择".equals(VipTypeName)) {
                            object.put("hyleibie", "");//会员类别
                        } else {
                            object.put("hyleibie", VipTypeName);//会员类别
                        }
                        if ("请选择".equals(hospitalId)) {
                            object.put("cityid", "");//会员类别
                        } else {
                            object.put("cityid", hospitalId);//医院ID
                        }
                        if ("请选择".equals(Department_max)) {
                            object.put("keshilb", "");//大科室
                        } else {
                            object.put("keshilb", DepartmentMaxName);//大科室
                        }
                        if ("请选择".equals(Department_min)) {
                            object.put("keshi", "");//小科室
                        } else {
                            object.put("keshi", DepartmentMinName);//小科室
                        }
                        if ("请选择".equals(TitleName)) {
                            object.put("my_694", "");//会员类别
                        } else {
                            object.put("my_694", TitleName);//职称
                        }

                        object.put("danwei", danwei);
                        object.put("truename", truename);//真实姓名
                        object.put("sex", SexId);//性别 0女 1男
                        object.put("address", address);//联系地址
                        object.put("idcard", idcard);//身份证号码
                        final String result = HttpClientUtils.sendPost(context,
                                URLConfig.ccmtvapp_Myphoto,
                                object.toString());

                        JSONObject json = new JSONObject(result);
                        if (json.getInt("status") == 1) {//成功
                            Message message = new Message();
                            message.what = 200;
                            message.obj = json;
                            handler.sendMessage(message);
                        } else {//失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        MyProgressBarDialogTools.hide();
                                        Toast.makeText(MyProfile.this, new JSONObject(result).getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            };
            new Thread(runnable).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void back(View view) {
        /*Intent intent = new Intent(MyProfile.this, MainActivity.class);
        intent.putExtra("type", "register");
        startActivity(intent);*/
        //super.back(view);
        this.finish();
    }

    public void revisePerfection() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.revisePerfection);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
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

}
