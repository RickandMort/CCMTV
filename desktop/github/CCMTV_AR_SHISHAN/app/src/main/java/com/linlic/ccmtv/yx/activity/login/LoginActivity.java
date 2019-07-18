package com.linlic.ccmtv.yx.activity.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbUser;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.PhoneCode;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * name：登陆
 * <p/>
 * author: Mr.song
 * 时间：2016-2-19 上午10:00:09
 *
 * @author Administrator
 */
public class LoginActivity extends BaseActivity {
    private boolean passType = false;//当前密码显示状态（false密文 true明文）
    private EditText user;
    private TimeCount time;
    private EditText pass;
    private String userStr, passStr;
    private ImageView iv_showpopwindow, iv_pwd, iv_showpopwindow2;
    Context context;
    private boolean isShow = false;
    private PopupWindow popupWindow;
    List<DbUser> DbUsers;
    SharedPreferences sp;
    View view1;
    private CheckBox checkBox1;
    public static String YES = "yes";
    public static String NO = "no";
    SimpleAdapter adapter;
    private String isMemory = "";//isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
    private String source, str_logo, str_bg;
    private LinearLayout layout_login;//登录背景
    private ImageView login_logo;//登录logo
    /*=======快捷登录、帐号登陆start========*/
    private LinearLayout account_login_layout;
    private LinearLayout quick_login_layout;
    private RelativeLayout remember_the_password, quick_t;
    private TextView quick_login_img;
    private TextView quick_login_text;
    private TextView login_forget_password;
    private EditText quick_login_phone;
    private TextView send_verification_code;
    private TextView phone_hint_text;
    private ImageView login_x, login2_x;
    private Button login_Button;
    private LinearLayout login_layout1, login_layout2;
    private TextView pwd_ed_hint;
    private PhoneCode pc_1;

    /*=======快捷登录、帐号登陆end========*/
//    private ImageView account_login_img;
//    private TextView account_login_text;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            SharedPreferencesTools.saveUid(context, result.getString("uid"));
                            SharedPreferencesTools.saveUserName(context, result.getString("userName"));
                            final String uid = result.getString("uid");
                            SharedPreferencesTools.savePassword(context, passStr);


                            SharedPreferencesTools.saveWhether_the_quick_login(context, "account_login");
                            SharedPreferencesTools.saveMobphone(context, "");
                            if (MyDbUtils.findUserInfo(context, userStr) == null || "".equals(MyDbUtils.findUserInfo(context, userStr))) {
                                MyDbUtils.saveUserInfo(context, userStr);
                            }


                            if (result.has("isdocexam")) {
                                if (result.getString("isdocexam").equals("1")) {
                                    SharedPreferencesTools.saveIsdocexam(context, true);
                                    getgpRole();
                                } else {
                                    //第一步保存身份类型 3 医考  1规培生 2医院正式员工
                                    SharedPreferencesTools.saveGp_type(context, null);
                                    //第二步保存用户身份
                                    SharedPreferencesTools.saveRoleList(context, null);
                                    SharedPreferencesTools.saveIsdocexam(context, false);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("type", "phone_rz");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                //第一步保存身份类型 3 医考  1规培生 2医院正式员工
                                SharedPreferencesTools.saveGp_type(context, null);
                                //第二步保存用户身份
                                SharedPreferencesTools.saveRoleList(context, null);
                                SharedPreferencesTools.saveIsdocexam(context, false);
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("type", "phone_rz");
                                startActivity(intent);
                                finish();
                            }


                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            time.start();
                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            SharedPreferencesTools.saveUid(context, result.getString("uid"));
                            SharedPreferencesTools.saveUserName(context, result.getString("userName"));
                            final String uid = result.getString("uid");

                            SharedPreferencesTools.savePassword(context, "");
                            SharedPreferencesTools.saveWhether_the_quick_login(context, "quick_login");
                            SharedPreferencesTools.saveMobphone(context, userStr);
                            if (MyDbUtils.findUserInfo(context, userStr) == null || "".equals(MyDbUtils.findUserInfo(context, userStr))) {
                                MyDbUtils.saveUserInfo(context, userStr);
                            }
                         /*   JPushInterface.resumePush(context);                               //登录成功开启推送
                            JPushInterface.setAliasAndTags(getApplicationContext(), result.getString("uid"), null, new TagAliasCallback() {
                                @Override
                                public void gotResult(int i, String s, Set<String> set) {
                                    String logs = "Set alias success" + uid;
                                }
                            });*/

                            if (result.has("isdocexam")) {
                                if (result.getString("isdocexam").equals("1")) {
                                    SharedPreferencesTools.saveIsdocexam(context, true);
                                    getgpRole();
                                } else {
                                    //第一步保存身份类型 3 医考  1规培生 2医院正式员工
                                    SharedPreferencesTools.saveGp_type(context, null);
                                    //第二步保存用户身份
                                    SharedPreferencesTools.saveRoleList(context, null);
                                    SharedPreferencesTools.saveIsdocexam(context, false);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("type", "phone_rz");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                //第一步保存身份类型 3 医考  1规培生 2医院正式员工
                                SharedPreferencesTools.saveGp_type(context, null);
                                //第二步保存用户身份
                                SharedPreferencesTools.saveRoleList(context, null);
                                SharedPreferencesTools.saveIsdocexam(context, false);
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("type", "phone_rz");
                                startActivity(intent);
                                finish();
                            }

                           /* if (result.has("isdocexam")) {
                                if (result.getString("isdocexam").equals("1")) {
                                    SharedPreferencesTools.saveIsdocexam(context, true);
                                    getgpRole();
                                } else {
                                    SharedPreferencesTools.saveIsdocexam(context, false);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("type", "phone_rz");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                SharedPreferencesTools.saveIsdocexam(context, false);
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("type", "phone_rz");
                                startActivity(intent);
                                finish();
                            }*/

                        } else {//失败
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    user.setText("");
                    break;
                case 5:
                    quick_login_phone.setText("");
                    pc_1.setPhoneCode();
                    break;
                case 6:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            JSONObject jsonObject = result.getJSONObject("data");
                            str_logo = jsonObject.getString("icon");
//                            Drawable drawable1 = new BitmapDrawable(Utils.returnBitmap(jsonObject.getString("icon")));
                            str_bg = jsonObject.getString("imgurl");
                            //设置logo
                            new Carousel_figure(context).loadImageNoCache(str_logo, login_logo);  //无缓存
                            //设置背景
                            new DownloadImageTask().execute(str_bg);
                        } else {//失败
//                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("code") == 200) {
                            JSONObject dataJson = result.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { //成功
                                JSONObject datas = dataJson.getJSONObject("data");
                                //第一步保存身份类型 3 医考  1规培生 2医院正式员工
                                SharedPreferencesTools.saveGp_type(context, datas.getString("gp_type"));
                                //第二步保存用户身份
                                SharedPreferencesTools.saveRoleList(context, datas.getString("roleList"));

                                finish();
                            } else {//失败
                                SharedPreferencesTools.saveUid(LoginActivity.this, "");
                                SharedPreferencesTools.saveUserName(LoginActivity.this, "");
                                SharedPreferencesTools.savePassword(LoginActivity.this, "");
                                SharedPreferencesTools.saveVipFlag(LoginActivity.this, 0);
                                SharedPreferencesTools.saveIntegral(LoginActivity.this, "0");
                                Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            SharedPreferencesTools.saveUid(LoginActivity.this, "");
                            SharedPreferencesTools.saveUserName(LoginActivity.this, "");
                            SharedPreferencesTools.savePassword(LoginActivity.this, "");
                            SharedPreferencesTools.saveVipFlag(LoginActivity.this, 0);
                            SharedPreferencesTools.saveIntegral(LoginActivity.this, "0");
                            Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(getApplicationContext(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            send_verification_code.setBackgroundColor(Color.parseColor("#65b2f8"));
            send_verification_code.setClickable(false);
            send_verification_code.setText(millisUntilFinished / 1000 + "秒再获取");
        }

        @Override
        public void onFinish() {
            send_verification_code.setText("获取验证码");
            send_verification_code.setClickable(true);
//            send_verification_code.setBackgroundColor(Color.parseColor("#4f9fe8"));

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        source = getIntent().getStringExtra("source");
        sp = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
        DbUsers = MyDbUtils.findAllUserInfo(context);
        findViewById();
        time = new TimeCount(60000, 1000);
        initdata();
        getLoginImgUrl();
        initpop();
        setOnClick();
    }

    /**
     * name：跳转到用户协议页
     * <p/>
     * author: Mr.song
     * 时间：2016-2-22 下午1:31:59
     *
     * @param view
     */
    public void startXieyi(View view) {
        startActivity(new Intent(context, AgreementActivity.class));
    }

    //更改账号后，情空密码
    private void setOnClick() {

        //动态密码登录
        quick_login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quick_login_phone.getText().toString().trim().length() > 10) {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    login_Button.setClickable(true);
                } else {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    login_Button.setClickable(false);
                }


                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
                account_login_layout.setVisibility(View.GONE);
                quick_login_layout.setVisibility(View.VISIBLE);
                remember_the_password.setVisibility(View.GONE);
                quick_login_img.setVisibility(View.GONE);
                quick_login_text.setVisibility(View.VISIBLE);
            }
        });
        quick_login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().trim().length() > 0) {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    login_Button.setClickable(true);
                } else {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    login_Button.setClickable(false);
                }

                handler.sendEmptyMessage(4);
                handler.sendEmptyMessage(5);
                account_login_layout.setVisibility(View.VISIBLE);
                quick_login_layout.setVisibility(View.GONE);
                remember_the_password.setVisibility(View.VISIBLE);
                quick_login_img.setVisibility(View.VISIBLE);
                quick_login_text.setVisibility(View.GONE);

            }
        });

        //游客登录
        /*account_login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,FillInVerificationCodeActivity.class));
            }
        });*/
        /*account_login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void initdata() {

        TextPaint paint = phone_hint_text.getPaint();
        paint.setFakeBoldText(true);

        login_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        login2_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        quick_login_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                if (quick_login_phone.getText().toString().trim().length() > 10) {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    login_Button.setClickable(true);
                } else {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    login_Button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });

        //注册事件回调（根据实际需要，可写，可不写）
        pc_1.setOnInputListener(new PhoneCode.OnInputListener() {
            @Override
            public void onSucess(String code) {
                //TODO: 例如底部【下一步】按钮可点击
                LogUtil.e("onSucess", code);
                passStr = code;
                login();
            }

            @Override
            public void onInput() {
                //TODO:例如底部【下一步】按钮不可点击
//                ToastUtils.makeText(context,"2222222222222");
            }
        });


        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入前的监听

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
                pass.setText("");
                if (user.getText().toString().trim().length() > 0) {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu59));
                    login_Button.setClickable(true);
                } else {
                    login_Button.setBackground(getResources().getDrawable(R.drawable.anniu60));
                    login_Button.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
            }
        });

        isMemory = SharedPreferencesTools.getIsMemory(context);
        //进入界面时，这个if用来判断SharedPreferences里面name和password有没有数据，有的话则直接打在EditText上面
        String name = "", password = "";
        if (isMemory.equals(YES)) {
            name = sp.getString("userName", "");
            password = Base64utils.getFromBase64(Base64utils.getFromBase64(sp.getString("passWord", "")));
            user.setText(name);
            pass.setText(password);
        } else {
            name = sp.getString("userName", "");
            user.setText(name);
            user.setSelection(name.length());
        }
        SharedPreferencesTools.saveIsMemory(context, name, password, isMemory);
    }

    public void remenber() {
        if (checkBox1.isChecked()) {
            SharedPreferencesTools.saveIsMemory(context, user.getText().toString(), pass.getText().toString(), YES);
        } else if (!checkBox1.isChecked()) {
            SharedPreferencesTools.saveIsMemory(context, user.getText().toString(), "", NO);
        }
    }

    private void findViewById() {
        user = (EditText) findViewById(R.id.login_user);
        pass = (EditText) findViewById(R.id.login_pass);
        phone_hint_text = (TextView) findViewById(R.id.phone_hint_text);
        view1 = findViewById(R.id.view1);
        iv_showpopwindow = (ImageView) findViewById(R.id.iv_showpopwindow);
        iv_showpopwindow2 = (ImageView) findViewById(R.id.iv_showpopwindow2);
        iv_pwd = (ImageView) findViewById(R.id.iv_pwd);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox);
        account_login_layout = (LinearLayout) findViewById(R.id.account_login_layout);
        quick_login_layout = (LinearLayout) findViewById(R.id.quick_login_layout);
        remember_the_password = (RelativeLayout) findViewById(R.id.remember_the_password);
        quick_t = (RelativeLayout) findViewById(R.id.quick_t);
        quick_login_img = (TextView) findViewById(R.id.quick_login_img);
        quick_login_text = (TextView) findViewById(R.id.quick_login_text);
        login_forget_password = (TextView) findViewById(R.id.login_forget_password);
        quick_login_phone = (EditText) findViewById(R.id.quick_login_phone);
        send_verification_code = (TextView) findViewById(R.id.send_verification_code);
        login_logo = (ImageView) findViewById(R.id.login_logo);
        layout_login = (LinearLayout) findViewById(R.id.layout_login);
        login_Button = (Button) findViewById(R.id.login_Button);
        login_x = (ImageView) findViewById(R.id.login_x);
        login2_x = (ImageView) findViewById(R.id.login2_x);
        login_layout1 = (LinearLayout) findViewById(R.id.login_layout1);
        login_layout2 = (LinearLayout) findViewById(R.id.login_layout2);
        pwd_ed_hint = (TextView) findViewById(R.id.pwd_ed_hint);

        pc_1 = (PhoneCode) findViewById(R.id.pc_1);


        send_verification_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_verification_code_onclick(v);
            }
        });


        TextPaint paint = user.getPaint();
        paint.setFakeBoldText(true);
        TextPaint paint2 = pass.getPaint();
        paint2.setFakeBoldText(true);
        TextPaint paint3 = quick_login_phone.getPaint();
        paint3.setFakeBoldText(true);

    }


    public void initpop() {
        final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        int i;
        if (DbUsers == null || "".equals(DbUsers)) {
            return;
        } else {
            for (i = 0; i <= DbUsers.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (i != DbUsers.size()) {
                    String username = DbUsers.get(i).getUsername();
                    map.put("username", username);
                    list.add(map);
                } else {
                    if (DbUsers.size() > 0) {
                        map.put("username", "清空搜索历史");
                        list.add(map);
                    }
                }
            }
        }
        adapter = new SimpleAdapter(context, list, R.layout.history_user, new String[]{"username"}, new int[]{R.id.history_username});
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(R.layout.drop_down_layout, null);
        ListView listview_history = (ListView) contentView.findViewById(R.id.listview_history);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopAnimStyle);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                isShow = false;
                                                iv_showpopwindow.setImageResource(R.mipmap.login_more);
                                                iv_showpopwindow2.setImageResource(R.mipmap.login_more);
                                                return false;
                                                // 这里如果返回true的话，touch事件将被拦截
                                                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                                            }
                                        }
        );

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(
                getResources().getDrawable(R.drawable.custom_bg_gv)
        );

        listview_history.setAdapter(adapter);
        listview_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getCount() == position + 1) {
                    MyDbUtils.deleteAllUserInfo(context);
                    DbUsers = MyDbUtils.findAllUserInfo(context);
                    list.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    user.setText(list.get(position).get("username"));
                    user.setSelection(list.get(position).get("username").length());
                    quick_login_phone.setText(list.get(position).get("username"));
                    quick_login_phone.setSelection(list.get(position).get("username").length());
                }
                popupWindow.dismiss();
            }
        });
    }


    /**
     * name：获取动态验证码
     * <p/>
     * author: Mr.song
     * 时间：2016-2-19 上午10:15:05
     *
     * @param view
     */
    public void send_verification_code_onclick(View view) {
        if (isChinaPhoneLegal(quick_login_phone.getText().toString())) {
            remenber();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("action", URLConfig.app_sendsms);
                        obj.put("mobphone", quick_login_phone.getText());
                        String result = HttpClientUtils.sendPost(context, URLConfig.QR_CCMTVAPP, obj.toString());
//                        Log.e("看看动态登录",result);
                        Message message = new Message();
                        message.what = 2;
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
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        return str.length() == 11 ? true : false;
    }

    /**
     * name：登陆
     * <p/>
     * author: Mr.song
     * 时间：2016-2-19 上午10:15:05
     *
     * @param view
     */
    public void login(View view) {
        if (account_login_layout.getVisibility() == View.VISIBLE) {
            userStr = user.getText().toString();
            passStr = pass.getText().toString();
            if (TextUtils.isEmpty(userStr)) {
                Toast.makeText(getApplicationContext(), R.string.login_toast_user, Toast.LENGTH_SHORT).show();
                user.requestFocus();
                return;
            } else if (TextUtils.isEmpty(passStr)) {
                //Toast.makeText(getApplicationContext(), R.string.login_toast_pass, Toast.LENGTH_SHORT).show();
                toastMessage("密码不能为空", 3000);
                pass.requestFocus();
                return;
            }
            MyProgressBarDialogTools.show(context);
            remenber();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("userAccount", userStr);
                        obj.put("password", passStr);
                        obj.put("act", URLConfig.ulogin);

                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                        Log.e("loginresult1", result);
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
            userStr = quick_login_phone.getText().toString();
//            passStr = verification_code.getText().toString();
            if (TextUtils.isEmpty(userStr)) {
                Toast.makeText(getApplicationContext(), "手机号码不能为空！", Toast.LENGTH_SHORT).show();
                user.requestFocus();
                return;
            }

            login_layout1.setVisibility(View.GONE);
            login_layout2.setVisibility(View.VISIBLE);
            pwd_ed_hint.setText("验证码已发送至 (+86)" + userStr);

            send_verification_code_onclick(view);


        }

    }

    public void login() {
        MyProgressBarDialogTools.show(context);
        remenber();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("mobphone", userStr);
                    obj.put("num", passStr);
                    obj.put("action", URLConfig.app_sendsms_valid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.QR_CCMTVAPP, obj.toString());
//                        Log.e("loginresult2", result);
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

    /**
     * name：密码明文密文切换
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午7:34:24
     *
     * @param view
     */
    public void lookPass(View view) {
        if (passType == false) {
            iv_pwd.setImageResource(R.mipmap.pwd_open);
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passType = true;
        } else {
            passType = false;
            iv_pwd.setImageResource(R.mipmap.pwd_close);
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    /**
     * name：显示已登录过得用户名
     * author：Larry
     * data：2016/4/13 18:03
     */
    public void showpopwindow(View view) {
        if (isShow == false) {
            iv_showpopwindow.setImageResource(R.mipmap.login_mores);
            iv_showpopwindow2.setImageResource(R.mipmap.login_mores);
            isShow = true;
            // 设置好参数之后再show
            try {
                popupWindow.showAsDropDown(view1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            iv_showpopwindow.setImageResource(R.mipmap.login_more);
            iv_showpopwindow2.setImageResource(R.mipmap.login_more);
            isShow = false;
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * name：跳转注册页
     * <p/>
     * author: Mr.song
     * 时间：2016-2-22 上午11:23:47
     *
     * @param view
     */

    public void startRegist(View view) {
//        startActivity(new Intent(context, RegisterActivity.class));
        startActivity(new Intent(context, RegistActivity.class));
    }

    /**
     * name：跳转忘记密码页
     * <p/>
     * author: Mr.song
     * 时间：2016-2-22 上午11:23:47
     *
     * @param view
     */
    public void startPass(View view) {
        startActivity(new Intent(context, ForgetPassActivity.class));
    }

    /**
     * name：记住密码
     * author：Larry
     * data：2016/6/1 10:00
     */
    public void remeberPass(View view) {
        if (checkBox1.isChecked() == false) {
            checkBox1.setChecked(true);
        } else {
            checkBox1.setChecked(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (("my").equals(source)) {
                intent.putExtra("type", "register");
            } else if (("up").equals(source)) {
                intent.putExtra("type", "upload");
            } else {
                intent.putExtra("type", "changepass");
            }
            startActivity(intent);*/
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取节日图片
    public void getLoginImgUrl() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activityimg);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("loginresult", result);
                    Message message = new Message();
                    message.what = 6;
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

    //获取身份
    public void getgpRole() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.gpRole);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
//                    Log.e("getgpRole接口访问地址", URLConfig.CCMTVAPP_GpApi);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
//                    Log.e("getgpRole", result);
                    Message message = new Message();
                    message.what = 7;
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

    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        protected void onPostExecute(Drawable result) {
            layout_login.setBackgroundDrawable(result);
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过第二个参数(文件名)来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);
        } catch (IOException e) {
//            Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
//            Log.d("skythinking", "null drawable");
        } else {
//            Log.d("skythinking", "not null drawable");
        }
        return drawable;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
