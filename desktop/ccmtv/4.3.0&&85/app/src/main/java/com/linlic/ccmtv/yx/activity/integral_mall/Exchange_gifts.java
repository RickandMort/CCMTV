package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.util.Utils;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.Validate;
import com.youth.picker.PickerView;
import com.youth.picker.entity.PickerData;
import com.youth.picker.listener.OnPickerClickListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/27.
 */

public class Exchange_gifts extends BaseActivity implements View.OnClickListener {
    private Context context;
    private TextView provinceData, title_name, integral;
    private PickerView pickerView;
    //选择器数据实体类封装
    private PickerData data = new PickerData();
    private String[] mProvinceDatas;
    private EditText address;//详细地址
    private EditText zip_code;//邮编
    private EditText receiver;//收件人
    private EditText phone;//手机号码
    private EditText mailbox;//邮箱
    private EditText remark;//备注
    private ImageView img;
    private Map<String, String[]> mCitisDatasMap = new HashMap<>();
    private Map<String, String[]> mDistrictDatasMap = new HashMap<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            SharedPreferencesTools.saveIntegral(Exchange_gifts.this, jsonObject.getString("money"));
                            Intent intent = new Intent(Exchange_gifts.this, Exchange_success.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Exchange_gifts.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.exchange_gifts);
        context = this;
        findId();
        init();
    }

    @Override
    public void findId() {
        super.findId();
        provinceData = (TextView) findViewById(R.id.provinceData);
        title_name = (TextView) findViewById(R.id.title_name);
        integral = (TextView) findViewById(R.id.integral);
        address = (EditText) findViewById(R.id.address);
        zip_code = (EditText) findViewById(R.id.zip_code);
        receiver = (EditText) findViewById(R.id.receiver);
        phone = (EditText) findViewById(R.id.phone);
        mailbox = (EditText) findViewById(R.id.mailbox);
        remark = (EditText) findViewById(R.id.remark);
        img = (ImageView) findViewById(R.id.img);
    }

    public void init() {
        title_name.setText(getIntent().getStringExtra("title_name"));
        integral.setText((getIntent().getIntExtra("integral", 0) * Integer.parseInt(getIntent().getStringExtra("purchase_quantity"))) + "");

        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(getIntent().getStringExtra("img")), img);
        try {
            JSONArray provincesList = new JSONArray(getJson("allprovinces.json", context));
            mProvinceDatas = new String[provincesList.length()];
            for (int i = 0; i < provincesList.length(); i++) {
                JSONObject provinces = provincesList.getJSONObject(i);
//                Log.e("provinces", provinces.toString());
                mProvinceDatas[i] = provinces.get("name").toString();
                JSONArray Citys = provinces.getJSONArray("city");
                String[] citys = new String[Citys.length()];
                for (int j = 0; j < Citys.length(); j++) {
                    JSONObject city = Citys.getJSONObject(j);
                    citys[j] = city.get("name").toString();
                    JSONArray area = city.getJSONArray("area");
//                    Log.e("area", area.toString());
                    String[] areas = new String[area.length()];
                    for (int k = 0; k < area.length(); k++) {
                        areas[k] = area.getString(k);
                    }
                    mDistrictDatasMap.put(citys[j], areas);
                }
                mCitisDatasMap.put(mProvinceDatas[i], citys);
            }

            //设置数据，有多少层级自己确定
            data.setFirstDatas(mProvinceDatas);
            data.setSecondDatas(mCitisDatasMap);
            data.setThirdDatas(mDistrictDatasMap);
            data.setFourthDatas(new HashMap<String, String[]>());
            //设置弹出框的高度
            data.setHeight(1000);
            //设置初始化默认显示的菜单(此方法可以选择传参数量1到4个)
            data.setInitSelectText("北京市", "北京市", "东城区");

            //初始化选择器
            pickerView = new PickerView(this, data);

            provinceData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示选择器
                    pickerView.show(provinceData);
                }
            });

            //选择器点击回调
            pickerView.setOnPickerClickListener(new OnPickerClickListener() {
                //选择列表时触发的事件（手动关闭）
                @Override
                public void OnPickerClick(PickerData pickerData) {
//                    Log.e("选择数据", pickerData.getSecondText());
//                    Log.e("选择数据", pickerData.getSelectText());
                    provinceData.setText(pickerData.getSelectText());
                    pickerView.dismiss();//关闭选择器
                }

                //点击确定按钮触发的事件（自动关闭）
                @Override
                public void OnPickerConfirmClick(PickerData pickerData) {
//                    Log.e("数据", pickerData.getSelectText());
                    provinceData.setText(pickerData.getSelectText());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void convert(View view) {
        String validate = validate();
        if (validate == null) {
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.doOrder);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", getIntent().getStringExtra("id"));
                        obj.put("num", getIntent().getStringExtra("purchase_quantity"));
                        obj.put("city", provinceData.getText().toString().trim());
                        obj.put("add_detail", address.getText().toString().trim());
                        obj.put("postcode", zip_code.getText().length() > 0 ? zip_code.getText().toString().trim() : "");
                        obj.put("email", mailbox.getText().length() > 0 ? mailbox.getText().toString().trim() : "");
                        obj.put("receiver", receiver.getText().toString().trim());
                        obj.put("telphone", phone.getText().toString().trim());
                        obj.put("name", getIntent().getStringExtra("title_name"));
                        obj.put("total", integral.getText().toString().trim());
                        obj.put("note", remark.getText().length() > 0 ? remark.getText().toString().trim() : "");
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_Commodity, obj.toString());
                        MyProgressBarDialogTools.hide();

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
        } else {
            Utils.showToast(context, validate);
        }
    }

    public String validate() {
        if (provinceData.getText().toString().trim().length() < 1)
            return "请选择所在地区";
        if (address.getText().toString().trim().length() < 1)
            return "请填写详细地址";
        if (receiver.getText().toString().trim().length() < 1)
            return "请填写收件人姓名";
        String mPhone = phone.getText().toString().trim();
        if (mPhone.length() < 1)
            return "请填写联系电话";
        if (!Utils.isMobileNO(mPhone))
            return "请填写正确的手机号码";
        if (provinceData.getText().toString().trim().length() < 1)
            return "请选择所在地区";
        String mZipCode = zip_code.getText().toString().trim();
        if (!TextUtils.isEmpty(mZipCode) && !Validate.isZipNO(mZipCode))
            return "请填写正确的邮编";
        String mMailBox = mailbox.getText().toString().trim();
        if (!TextUtils.isEmpty(mMailBox) && !Validate.isEmail(mMailBox))
            return "请填写有效的邮箱";
        return null;

//        if (provinceData.getText().toString().trim().length() < 1) {
//            Toast.makeText(context, "请选择所在地区", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (address.getText().toString().trim().length() < 1) {
//            Toast.makeText(context, "请填写详细地址", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (receiver.getText().toString().trim().length() < 1) {
//            Toast.makeText(context, "请填写收件人姓名", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (phone.getText().toString().trim().length() < 1) {
//            Toast.makeText(context, "请填写联系电话", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (zip_code.getText().toString().trim().length() > 0) {
//            if (!Validate.isZipNO(zip_code.getText().toString().trim())) {
//                Toast.makeText(context, "请填写正确的邮编", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
//
//        if (mailbox.getText().toString().trim().length() > 0) {
//            if (!Validate.isEmail(mailbox.getText().toString().trim())) {
//                Toast.makeText(context, "请填写有效的邮箱", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }
//        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.purchase_quantity1:

                break;
            default:
                break;
        }
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
