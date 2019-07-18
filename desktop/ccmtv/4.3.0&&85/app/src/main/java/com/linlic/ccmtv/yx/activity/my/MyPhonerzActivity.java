package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

/**
 * 我的手机认证
 *
 * @author yu
 */
public class MyPhonerzActivity extends BaseActivity {
    TextView activity_title_name;
    EditText edit_mobphone, edit_validate;
    Context context;
    CountDownTimer cdt_codeget;
    Button btn_codeget;
    String Str_mobphone, Str_num;
    String type;//号码已经存在  source findPassword  号码不存在，source  uregister  用这个参数
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HasPhoneNumActivity.class);
                            intent.putExtra("type", "phone_rz");
                            intent.putExtra("Str_phonenum", Str_mobphone);
                            startActivity(intent);
                            finish();
                            Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            cdt_codeget.start();
                            btn_codeget.setEnabled(false);
                            btn_codeget.setBackgroundResource(R.drawable.sendyzm_wait);
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phonerz);
        context = this;
        findId();
        initdata();
        setText();
        cdt_codeget = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btn_codeget.setText(millisUntilFinished / 1000 + "s后重发");

            }

            @Override
            public void onFinish() {
                btn_codeget.setText("发送验证码");
                if (!btn_codeget.isEnabled()) {
                    btn_codeget.setEnabled(true);
                    btn_codeget.setBackgroundResource(R.mipmap.bg_validate);
                    btn_codeget.setTextColor(Color.rgb(111, 207, 244));
                }
            }
        };
    }

    public void findId() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        edit_mobphone = (EditText) findViewById(R.id.edit_mobphone);
        edit_validate = (EditText) findViewById(R.id.edit_validate);
        btn_codeget = (Button) findViewById(R.id.btn_codeget);
    }

    public void initdata() {
        type = getIntent().getStringExtra("type");
        Str_mobphone = getIntent().getStringExtra("Str_phonenum");
        if (!TextUtils.isEmpty(Str_mobphone)) {
            edit_mobphone.setText(Str_mobphone);
            edit_mobphone.setSelection(Str_mobphone.length());
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

    public void setText() {
        if ("has".equals(type)) {
            activity_title_name.setText("修改手机认证");
        } else {
            activity_title_name.setText("手机认证");
        }
    }

    /**
     * name：登陆
     * <p/>
     * author: Mr.song
     * 时间：2016-2-19 上午10:15:05
     *
     * @param view
     */
    public void phonerz(View view) {
        Str_mobphone = edit_mobphone.getText().toString();
        Str_num = edit_validate.getText().toString();
        if (TextUtils.isEmpty(Str_mobphone)) {
            Toast.makeText(context, R.string.phone_toast, Toast.LENGTH_SHORT).show();
            edit_mobphone.requestFocus();
            return;
        } else if (TextUtils.isEmpty(Str_num)) {
            Toast.makeText(context, R.string.num_toast, Toast.LENGTH_SHORT).show();
            edit_validate.requestFocus();
            return;
        }
        if (Str_mobphone.length() != 11) {
            Toast.makeText(context, R.string.regist_hint2, Toast.LENGTH_SHORT).show();
            edit_mobphone.requestFocus();
            return;
        }
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
           return;
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("uid", uid);
                        obj.put("mobphone", Str_mobphone);
                        obj.put("num", Str_num);
                        if ("has".equals(type)) {
                            obj.put("act", URLConfig.mob_ChangeBind);
                        } else {
                            obj.put("act", URLConfig.mob_certification);
                        }
                        String result = HttpClientUtils.sendPost(context,
                                URLConfig.CCMTVAPP, obj.toString());
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

    //获取验证码
    public void getnum(View view) {
        Str_mobphone = edit_mobphone.getText().toString();
        Str_num = edit_validate.getText().toString();
        if (TextUtils.isEmpty(Str_mobphone)) {
            Toast.makeText(context, R.string.regist_hint1, Toast.LENGTH_SHORT).show();
            edit_mobphone.requestFocus();
            return;
        } else if (Str_mobphone.length() != 11) {
            Toast.makeText(context, R.string.regist_hint2, Toast.LENGTH_SHORT).show();
            edit_mobphone.requestFocus();
            return;
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("phone", Str_mobphone);
                        if ("has".equals(type)) {
                            object.put("source", "uregister");
                        } else {
                            object.put("source", "findPassword");
                        }
                        object.put("act", URLConfig.sendMesg);
//                        Log.e("getNum", object.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
//                        Log.e("getNum",result);
                        Message message = new Message();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }
    }
}
