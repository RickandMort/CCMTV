package com.linlic.ccmtv.yx.activity.my.book;

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
import com.linlic.ccmtv.yx.activity.integral_mall.Exchange_success;
import com.linlic.ccmtv.yx.activity.my.OpenMenberActivity2;
import com.linlic.ccmtv.yx.activity.my.OpenMenberActivity3;
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

public class Order_filling extends BaseActivity implements View.OnClickListener {
    private Context context;
    private TextView  title_name;
    private PickerView pickerView;
    //选择器数据实体类封装
    private PickerData data = new PickerData();
    private String[] mProvinceDatas;
    private EditText address;//详细地址
    private EditText receiver;//收件人
    private EditText phone;//手机号码
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
                            JSONObject data =jsonObject.getJSONObject("data");
                            Intent   intent = new Intent(context,
                                    OpenMenberActivity3.class);
//                                    money = Double.valueOf("60.00");
                            Double money = Double.valueOf(data.getString("money"));
                            intent.putExtra("viptitle", "《" + getIntent().getStringExtra("book_name") + "》 书籍");
                            intent.putExtra("vip_time", "X"+getIntent().getStringExtra("num"));
                            intent.putExtra("payfor", "book");//
                            intent.putExtra("vipflg_Str", data.getString("vipflg"));//
                            intent.putExtra("money", money);
                            intent.putExtra("aid", getIntent().getStringExtra("id"));
                            intent.putExtra("cid", data.getString("cid"));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Order_filling.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.order_filling);
        context = this;
        findId();
        init();
    }

    @Override
    public void findId() {
        super.findId();
        title_name = (TextView) findViewById(R.id.title_name);
        address = (EditText) findViewById(R.id.address);
        receiver = (EditText) findViewById(R.id.receiver);
        phone = (EditText) findViewById(R.id.phone);
        remark = (EditText) findViewById(R.id.remark);
        img = (ImageView) findViewById(R.id.img);
    }

    public void init() {
        title_name.setText(getIntent().getStringExtra("book_name"));
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(getIntent().getStringExtra("book_img")), img);


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
                        obj.put("act", URLConfig.createBookOrder);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", getIntent().getStringExtra("id"));
                        obj.put("num", getIntent().getStringExtra("num"));
                        obj.put("recipient", receiver.getText().toString().trim());
                        obj.put("tel", phone.getText().toString().trim());
                        obj.put("address", address.getText().toString().trim());
                        obj.put("remarks", remark.getText().length() > 0 ? remark.getText().toString().trim() : "");
                        String result = HttpClientUtils.sendPost(context, URLConfig.book_url, obj.toString());
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

        if (address.getText().toString().trim().length() < 1)
            return "请填写详细地址";
        if (receiver.getText().toString().trim().length() < 1)
            return "请填写收件人姓名";
        String mPhone = phone.getText().toString().trim();
        if (mPhone.length() < 1)
            return "请填写联系电话";
        if (!Utils.isMobileNO(mPhone))
            return "请填写正确的手机号码";


        return null;
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
