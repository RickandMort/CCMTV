package com.linlic.ccmtv.yx.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.pull.DepartmentMax;
import com.linlic.ccmtv.yx.activity.pull.HospitalProvince;
import com.linlic.ccmtv.yx.adapter.MyAlert;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfectInformationActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_skip;
    private EditText pif_truename, pif_danwei, pif_address;
    //    private EditText pif_idcard;
    private Button pif_viptype, btn_finish, myprofile_hospital, myprofile_department, myprofile_title;
    private LinearLayout wszl_male, wszl_female;
    private LinearLayout layout_keshi, layout_zhicheng, layout_yiyuan, layout_danwei;
    private CheckBox wszl_ck1, wszl_ck2;
    private String SexId;//性别id
    private String SexName;//性别name
    private String TitleId;//职称id
    private String TitleName;//职称name
    private String VipTypeId;//会员类别id
    private String VipTypeName;//会员类别name
    private String provinceId;//省 id
    private String provinceName;//省 name
    private String cityId;//市 id
    private String cityName;//市 name
    private String hospitalId;//医院 id
    private String hospitalName;//医院 name
    private String DepartmentMaxId;//科室大类id
    private String DepartmentMaxName;//科室大类name
    private String DepartmentMinId;//科室大类id
    private String DepartmentMinName;//科室大类name
    private String truename, danwei, address, hospital, departmen, title, uid;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Context context;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            final int MyAlertWhat = msg.what;
            if (MyAlertWhat == 150 || MyAlertWhat == 188) {//弹框
                MyAlert.Builder bu = new MyAlert.Builder(PerfectInformationActivity.this);
                bu.setTitle(msg.obj + "");
                bu.setMessage(list);
                bu.setPositiveButton("取消");
                bu.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                        TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
                        String id = textview.getText().toString();
                        TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
                        String name = textView.getText().toString();
                        if (MyAlertWhat == 150) {
                            VipTypeId = id;
                            VipTypeName = name;
                            pif_viptype.setText(VipTypeName);
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
                        }
                    }
                });
                bu.create().show();
            }
            switch (msg.what) {
                case 303:
                    Toast.makeText(context, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfectInformationActivity.this, MainActivity.class);
                    intent.putExtra("type", "register");
                    startActivity(intent);
//                    MyProgressBarDialogTools.hide();
                    PerfectInformationActivity.this.finish();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        context = this;
        initView();
        initData();
    }

    private void initView() {
        tv_skip = (TextView) findViewById(R.id.tv_skip);
        pif_truename = (EditText) findViewById(R.id.pif_truename);
        pif_danwei = (EditText) findViewById(R.id.pif_danwei);
        pif_address = (EditText) findViewById(R.id.pif_address);
//        pif_idcard = (EditText) findViewById(R.id.pif_idcard);
        pif_viptype = (Button) findViewById(R.id.pif_viptype);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        wszl_ck1 = (CheckBox) findViewById(R.id.wszl_ck1);
        wszl_ck2 = (CheckBox) findViewById(R.id.wszl_ck2);
        wszl_male = (LinearLayout) findViewById(R.id.wszl_male);
        wszl_female = (LinearLayout) findViewById(R.id.wszl_female);
        layout_keshi = (LinearLayout) findViewById(R.id.layout_keshi);
        layout_zhicheng = (LinearLayout) findViewById(R.id.layout_zhicheng);
        layout_yiyuan = (LinearLayout) findViewById(R.id.layout_yiyuan);
        layout_danwei = (LinearLayout) findViewById(R.id.layout_danwei);
        myprofile_hospital = (Button) findViewById(R.id.myprofile_hospital);
        myprofile_department = (Button) findViewById(R.id.myprofile_department);
        myprofile_title = (Button) findViewById(R.id.myprofile_title);

        wszl_male.setOnClickListener(this);
        wszl_female.setOnClickListener(this);
        pif_danwei.setOnClickListener(this);
        layout_keshi.setOnClickListener(this);
        layout_zhicheng.setOnClickListener(this);
        layout_yiyuan.setOnClickListener(this);
        tv_skip.setOnClickListener(this);
    }

    private void initData() {
        uid = getIntent().getStringExtra("uid");
        wszl_ck1.setSelected(true);
        setType();
        SetListener();
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wszl_male:
                if (!wszl_ck1.isSelected()) {
                    wszl_ck1.setSelected(true);
                    wszl_ck2.setSelected(false);
                }
                break;
            case R.id.wszl_female:
                if (!wszl_ck2.isSelected()) {
                    wszl_ck2.setSelected(true);
                    wszl_ck1.setSelected(false);
                }
                break;
            case R.id.tv_skip:
                startActivity(new Intent(context, LoginActivity.class));
                break;
            default:
                break;
        }
    }

    //选择会员类型
    public void getVipType(View view) {
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

    //选择医院
    public void getHospital(View view) {
        startActivityForResult(new Intent(PerfectInformationActivity.this, HospitalProvince.class), 18);
    }

    //选择科室点
    public void getDepartment(View view) {
        startActivityForResult(new Intent(PerfectInformationActivity.this, DepartmentMax.class), 16);
    }

    //选择职称
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

    //完善资料提交
    public void regist(View view) throws JSONException {
        truename = pif_truename.getText().toString();//姓名
        if (wszl_ck1.isSelected()) {//性别
            SexName = "男";
            SexId = "1";
        } else {
            SexName = "女";
            SexId = "0";
        }
        VipTypeName = pif_viptype.getText().toString();
        if ("医务工作者".equals(VipTypeName)) {
            hospital = myprofile_hospital.getText().toString();
            departmen = myprofile_department.getText().toString();
            title = myprofile_title.getText().toString();
            address = pif_address.getText().toString();
            if (TextUtils.isEmpty(truename)) {
                Toast.makeText(context, "姓名不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(VipTypeName)) {
                Toast.makeText(context, "会员类别不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(hospital)) {
                Toast.makeText(context, "医院不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(departmen)) {
                Toast.makeText(context, "科室不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(title)) {
                Toast.makeText(context, "职称不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(address)) {
                Toast.makeText(context, "联系地址不可为空", Toast.LENGTH_SHORT).show();
            } else {
                setMedicalWork();
            }
        } else {
            danwei = pif_danwei.getText().toString();
            address = pif_address.getText().toString();
            if (TextUtils.isEmpty(truename)) {
                Toast.makeText(context, "姓名不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(VipTypeName)) {
                Toast.makeText(context, "会员类别不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(danwei)) {
                Toast.makeText(context, "单位不可为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(address)) {
                Toast.makeText(context, "联系地址不可为空", Toast.LENGTH_SHORT).show();
            } else {
                setNoneMedicalWork();
            }
        }
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
                        myprofile_department.setText(DepartmentMaxName + "  " + DepartmentMinName);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setMedicalWork() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("act", URLConfig.perfectInfo);
                    object.put("uid", uid);
                    object.put("truename", truename);//真实姓名
                    object.put("sex", SexId);//性别 0女 1男
                    object.put("hyleibie", VipTypeName);//用户类型
                    object.put("cityid", hospitalId);//医院id
                    object.put("keshilb", DepartmentMaxName);//大科室
                    object.put("keshi", DepartmentMinName);//小科室
                    object.put("my_694", title);//职称
                    object.put("address", address);//地址
//                    Log.e("wszlresult",object.toString());
                    final String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
//                    Log.e("wszlresult",result);
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
//                                    MyProgressBarDialogTools.hide();
                                    Toast.makeText(PerfectInformationActivity.this, new JSONObject(result).getString("errorMessage"), Toast.LENGTH_SHORT).show();
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

    public void setNoneMedicalWork() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("act", URLConfig.perfectInfo);
                    object.put("uid", uid);
                    object.put("truename", truename);//真实姓名
                    object.put("sex", SexId);//性别 0女 1男
                    object.put("hyleibie", VipTypeName);//用户类型
                    object.put("danwei", danwei);//单位
                    object.put("address", address);//地址
                    final String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
//                    Log.e("wszlresult",result);
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
//                                    MyProgressBarDialogTools.hide();
                                    Toast.makeText(PerfectInformationActivity.this, new JSONObject(result).getString("errorMessage"), Toast.LENGTH_SHORT).show();
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

    //根据会员类型切换布局
    public void setType(){
        pif_viptype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    layout_yiyuan.setVisibility(View.VISIBLE);
                    layout_keshi.setVisibility(View.VISIBLE);
                    layout_zhicheng.setVisibility(View.VISIBLE);
                    layout_danwei.setVisibility(View.GONE);
                } else {
                    layout_yiyuan.setVisibility(View.GONE);
                    layout_keshi.setVisibility(View.GONE);
                    layout_zhicheng.setVisibility(View.GONE);
                    layout_danwei.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void SetListener() {
        /*truename = pif_truename.getText().toString();
        hospital = myprofile_hospital.getText().toString();
        departmen = myprofile_department.getText().toString();
        title = myprofile_title.getText().toString();
        danwei = pif_danwei.getText().toString();
        address = pif_address.getText().toString();*/
        pif_truename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
            }
        });
        myprofile_hospital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
            }
        });
        myprofile_department.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
            }
        });
        myprofile_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
            }
        });
        pif_danwei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
            }
        });
        pif_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                truename = pif_truename.getText().toString();
                hospital = myprofile_hospital.getText().toString();
                departmen = myprofile_department.getText().toString();
                title = myprofile_title.getText().toString();
                danwei = pif_danwei.getText().toString();
                address = pif_address.getText().toString();
                if (pif_viptype.getText().toString().equals("医务工作者")) {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(hospital) && !TextUtils.isEmpty(departmen) && !TextUtils.isEmpty(title) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }else if (pif_viptype.getText().toString().equals("")) {
                    btn_finish.setBackgroundResource(R.drawable.anniu60);
                } else {
                    if (!TextUtils.isEmpty(truename) && !TextUtils.isEmpty(danwei) && !TextUtils.isEmpty(address)) {
                        btn_finish.setBackgroundResource(R.drawable.anniu59);
                    } else {
                        btn_finish.setBackgroundResource(R.drawable.anniu60);
                    }
                }
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

}
