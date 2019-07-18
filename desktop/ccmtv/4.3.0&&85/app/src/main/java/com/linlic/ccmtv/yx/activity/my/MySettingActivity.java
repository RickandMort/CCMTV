package com.linlic.ccmtv.yx.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.Util;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.ClearCacheCustomDialog;
import com.linlic.ccmtv.yx.activity.my.dialog.UpdateCustomDialog;
import com.linlic.ccmtv.yx.activity.my.feedback.Feedback_Main;
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.BadgeUtil;
import com.linlic.ccmtv.yx.utils.DataCleanManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * name：设置页面
 * author：Larry
 * data：2016/6/1.
 */
public class MySettingActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    private LinearLayout layout_yjfk, layout_aboutown, my_bofanganddown, layout_callkefu, my_arassert, my_password, my_phone, my_Personal;
    private LinearLayout layout_checknew, layout_clearcache, layou_setpush;
    private TextView tv_showversion, tv_cache, tv_arcache;
    private TextView tvPhoneNumber;
    private ImageView iv_openpush;
    private Dialog dialog;
    private int Str_IsYZ;
    String Str_icon, Str_username, Str_idcard_yz_reason, Str_idcard_imgurl, Str_cityid, Str_hyleibie, Str_cityname, Str_phonenum, Str_bigkeshi, Str_personalmoney, Str_smallkeshi, Str_zhicheng, Str_truename, Str_sexName, Str_address, Str_idcard, Str_sex, Str_danwei;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    try {
                        JSONObject obj = new JSONObject(new JSONObject(json).getString("data"));
                        if (obj.getInt("version") <= Util.getVersion(MySettingActivity.this)) {//版本一致
                            Toast.makeText(getApplicationContext(), "暂无新版本", Toast.LENGTH_SHORT).show();

                        } else {//版本不一致
                            UpdateCustomDialog dialog_up = new UpdateCustomDialog(MySettingActivity.this,
                                    R.style.mystyle, R.layout.upcustomdialog, obj.getString("des"));
                            dialog_up.setCancelable(false);
                            dialog_up.show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.post_hint5, Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        context = this;

        findId();
        init();
        setOnclick();
        setText();

    }

    public void init() {
        Str_IsYZ = Integer.parseInt(getIntent().getStringExtra("Str_IsYZ"));
        Str_phonenum = getIntent().getStringExtra("Str_phonenum");
        Str_hyleibie = getIntent().getStringExtra("Str_hyleibie");
        Str_cityname = getIntent().getStringExtra("Str_cityname");
        Str_bigkeshi = getIntent().getStringExtra("Str_bigkeshi");
        Str_smallkeshi = getIntent().getStringExtra("Str_smallkeshi");

        Str_zhicheng = getIntent().getStringExtra("Str_zhicheng");
        Str_truename = getIntent().getStringExtra("Str_truename");
        Str_sexName = getIntent().getStringExtra("Str_sexName");
        Str_sex = getIntent().getStringExtra("Str_sex");
        Str_cityid = getIntent().getStringExtra("Str_cityid");

        Str_address = getIntent().getStringExtra("Str_address");
        Str_idcard = getIntent().getStringExtra("Str_idcard");
        Str_danwei = getIntent().getStringExtra("Str_danwei");

    }

    public void findId() {
        super.findId();
        tvPhoneNumber = (TextView) findViewById(R.id.id_tv_mysetting_phonenumber);
        layout_yjfk = (LinearLayout) findViewById(R.id.layout_yjfk);
        my_password = (LinearLayout) findViewById(R.id.my_password);
        layout_aboutown = (LinearLayout) findViewById(R.id.layout_aboutown);
        my_bofanganddown = (LinearLayout) findViewById(R.id.my_bofanganddown);
        layou_setpush = (LinearLayout) findViewById(R.id.layou_setpush);
        my_arassert = (LinearLayout) findViewById(R.id.my_arassert);
        layout_callkefu = (LinearLayout) findViewById(R.id.layout_callkefu);
        layout_checknew = (LinearLayout) findViewById(R.id.layout_checknew);
        layout_clearcache = (LinearLayout) findViewById(R.id.layout_clearcache);
        my_phone = (LinearLayout) findViewById(R.id.my_phone);
        my_Personal = (LinearLayout) findViewById(R.id.my_Personal);
        tv_showversion = (TextView) findViewById(R.id.tv_showversion);
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        tv_arcache = (TextView) findViewById(R.id.tv_arcache);
        iv_openpush = (ImageView) findViewById(R.id.iv_openpush);
    }

    private void setText() {
        super.setActivity_title_name("设置");
        if (Str_phonenum!=null && !Str_phonenum.isEmpty()){
            tvPhoneNumber.setText(Str_phonenum);
        }else {
            tvPhoneNumber.setText("暂无");
        }

        //是否推送
      /*  boolean Ispush = SharedPreferencesTools.getIsPush(MySettingActivity.this);
        if (Ispush) {                           //接收推送
            iv_openpush.setSelected(true);
            Set<Integer> days = new HashSet<Integer>();
            days.add(0);
            days.add(1);
            days.add(2);
            days.add(3);
            days.add(4);
            days.add(5);
            days.add(6);
            JPushInterface.setPushTime(getApplicationContext(), days, 0, 23);
        } else {                                 //不接收推送
            iv_openpush.setSelected(false);
            Set<Integer> days = new HashSet<Integer>();
            days.add(0);
            days.add(1);
            days.add(2);
            days.add(3);
            days.add(4);
            days.add(5);
            days.add(6);
            JPushInterface.setPushTime(getApplicationContext(), days, 0, 0);
        }*/
        //设置版本号
        String edition = "";
        try {
            edition = MySettingActivity.this.getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        tv_showversion.setText("当前版本 v" + edition);
        File file = new File(String.valueOf(MySettingActivity.this.getCacheDir()));
        if (!file.exists())
            file.mkdirs();
        String CacheSize = null;
        try {
            CacheSize = DataCleanManager.getCacheSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_cache.setText(CacheSize);
        File file_AR = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
        if (!file_AR.exists())
            file_AR.mkdirs();
        String ARCacheSize = null;
        try {
            ARCacheSize = DataCleanManager.getCacheSize(file_AR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_arcache.setText(ARCacheSize);
    }

    private void setOnclick() {
        layout_yjfk.setOnClickListener(this);
        layout_aboutown.setOnClickListener(this);
        my_bofanganddown.setOnClickListener(this);
        layou_setpush.setOnClickListener(this);
        my_arassert.setOnClickListener(this);
        layout_callkefu.setOnClickListener(this);
        layout_checknew.setOnClickListener(this);
        layout_clearcache.setOnClickListener(this);
        my_password.setOnClickListener(this);
        my_phone.setOnClickListener(this);
        my_Personal.setOnClickListener(this);
    }

    //退出登录
    public void quit_login(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.dialog_quit, null);
        dialog = new Dialog(MySettingActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() - 240); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view1);
        TextView tv_confirm = (TextView) view1.findViewById(R.id.tv_confirm);
        TextView tv_canal = (TextView) view1.findViewById(R.id.tv_canal);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesTools.saveUid(MySettingActivity.this, "");
                SharedPreferencesTools.saveUserName(MySettingActivity.this, "");
                SharedPreferencesTools.savePassword(MySettingActivity.this, "");
                SharedPreferencesTools.saveVipFlag(MySettingActivity.this, 0);
                SharedPreferencesTools.saveIntegral(MySettingActivity.this, "0");
                //SharedPreferencesTools.saveXMPush_id(MySettingActivity.this,"");//停止小米推送
                //SharedPreferencesTools.savePush_id(MySettingActivity.this,"");//停止华为推送、个信推送、魅族推送、vivo推送、oppo推送
                BadgeUtil.removeBadgeCount(MySettingActivity.this);
                Intent intent = new Intent(MySettingActivity.this, MainActivity.class);
                intent.putExtra("type", "quit");
                startActivity(intent);
                SharedPreferencesTools.saveIsdocexam(MySettingActivity.this, false);
                dialog.dismiss();
            }
        });
        tv_canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        File dir = new File(MySettingActivity.this.getExternalCacheDir().getPath());
        if (!dir.exists())
            dir.mkdirs();
        String CacheSize = null;
        try {
            CacheSize = DataCleanManager.getCacheSize(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_cache.setText(CacheSize);

        File file_AR = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage");
        if (!file_AR.exists())
            file_AR.mkdirs();
        String ARCacheSize = null;
        try {
            ARCacheSize = DataCleanManager.getCacheSize(file_AR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_arcache.setText(ARCacheSize);
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.layout_yjfk: // 意见反馈
                if(SharedPreferencesTools.getUid(context).length()>0){
                    intent = new Intent(context, Feedback_Main.class);
                }
                break;
            case R.id.layout_aboutown: // 关于我们
                intent = new Intent(MySettingActivity.this, AboutOwnActivity.class);
                break;
            case R.id.my_bofanganddown:  //播放和下载
                intent = new Intent(MySettingActivity.this, BofangAndDownActivity.class);
                break;
            case R.id.layou_setpush:  //是否取消通知
              /*  if (iv_openpush.isSelected()) {
                    iv_openpush.setSelected(false);
                    SharedPreferencesTools.saveIsPush(MySettingActivity.this, false);
                    Log.i("Ispush", "Ispush" + "取消推送");
                    Set<Integer> days = new HashSet<Integer>();
                    days.add(0);
                    days.add(1);
                    days.add(2);
                    days.add(3);
                    days.add(4);
                    days.add(5);
                    days.add(6);
                    JPushInterface.setPushTime(getApplicationContext(), days, 0, 0);
                } else {
                    iv_openpush.setSelected(true);
                    SharedPreferencesTools.saveIsPush(MySettingActivity.this, true);
                    Log.i("Ispush", "Ispush" + "推送");
                    Set<Integer> days = new HashSet<Integer>();
                    days.add(0);
                    days.add(1);
                    days.add(2);
                    days.add(3);
                    days.add(4);
                    days.add(5);
                    days.add(6);
                    JPushInterface.setPushTime(getApplicationContext(), days, 0, 23);
                }*/
                break;
            case R.id.my_arassert:  // AR资源管理
                intent = new Intent(MySettingActivity.this, ARAssertManagerActivity.class);
                break;
            case R.id.layout_callkefu:  //联系客服

                intent = new Intent(MySettingActivity.this, MyContact_us.class);
                break;
            case R.id.layout_checknew:  //检查更新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url = URLConfig.CCMTVAPP + "?source=ANDROID";
                            String result = HttpClientUtils.sendPost(MySettingActivity.this, url, new JSONObject().put("act", URLConfig.updateDownApp).toString());
                            final JSONObject object = new JSONObject(result);
                            if (object.getInt("status") == 1) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = object.toString();
                                handler.sendMessage(msg);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(MySettingActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(MySettingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Toast.makeText(MySettingActivity.this, getResources().getString(R.string.post_hint3), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.layout_clearcache:  //清除缓存
                ClearCacheCustomDialog dialog_clearcache = new ClearCacheCustomDialog(MySettingActivity.this,
                        R.style.mystyle, R.layout.clearcachecustomdialog, new ClearCacheCustomDialog.IOnClearCacheListener() {
                    @Override
                    public void ClearCache(String cacheSize) {
                        tv_cache.setText(cacheSize);
                    }
                });
                dialog_clearcache.show();
                break;
            case R.id.my_password:  //修改密码
                intent = new Intent(MySettingActivity.this, MyChangePassActivity.class);
                break;
            case R.id.my_phone:  //改绑手机
                if (Str_IsYZ == 1) {
                    //intent = new Intent(MySettingActivity.this, HasPhoneNumActivity.class);   //已认证
                    intent = new Intent(MySettingActivity.this, BindPhoneNumberActivity.class);   //已认证
                    intent.putExtra("Str_phonenum", Str_phonenum);
                    intent.putExtra("is_bind","yes");
                } else {
                    intent = new Intent(MySettingActivity.this, BindPhoneNumberActivity.class);      //未认证
                    intent.putExtra("Str_phonenum", Str_phonenum);
                    intent.putExtra("is_bind","no");
                    intent.putExtra("type","has");
                }
                break;
            case R.id.my_Personal://个人资料
                //首先判断是否是医考用户，如果是再判断身份（规培生或员工），如果不是，则跳转原来的我的资料
                if (SharedPreferencesTools.getIsdocexam(context)){
                    //用户身份类型  3医考  1规培生 2医院正式员工
//                    Log.e("MyFragment2", "onClick: 用户身份类型："+SharedPreferencesTools.getGp_type(context));
                    if (SharedPreferencesTools.getGp_type(context).equals("1") || SharedPreferencesTools.getGp_type(context).equals("2")){
                        intent = new Intent(context, GpPersonalInformationActivity.class);
                    }else {
                        //判断是否登录
                        intent = new Intent(MySettingActivity.this, MyProfile.class);
                        intent.putExtra("source", "my");
                        intent.putExtra("Str_hyleibie", Str_hyleibie);
                        intent.putExtra("Str_cityname", Str_cityname);
                        intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                        intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                        intent.putExtra("Str_zhicheng", Str_zhicheng);
                        intent.putExtra("Str_truename", Str_truename);
                        intent.putExtra("Str_sexName", Str_sexName);
                        intent.putExtra("Str_sex", Str_sex);
                        intent.putExtra("Str_cityid", Str_cityid);
                        intent.putExtra("Str_address", Str_address);
                        intent.putExtra("Str_idcard", Str_idcard);
                        intent.putExtra("Str_danwei", Str_danwei);
                    }
                }else {
                    //判断是否登录
                    intent = new Intent(MySettingActivity.this, MyProfile.class);
                    intent.putExtra("source", "my");
                    intent.putExtra("Str_hyleibie", Str_hyleibie);
                    intent.putExtra("Str_cityname", Str_cityname);
                    intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                    intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                    intent.putExtra("Str_zhicheng", Str_zhicheng);
                    intent.putExtra("Str_truename", Str_truename);
                    intent.putExtra("Str_sexName", Str_sexName);
                    intent.putExtra("Str_sex", Str_sex);
                    intent.putExtra("Str_cityid", Str_cityid);
                    intent.putExtra("Str_address", Str_address);
                    intent.putExtra("Str_idcard", Str_idcard);
                    intent.putExtra("Str_danwei", Str_danwei);
                }
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }


}
