package com.linlic.ccmtv.yx.activity.my.feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.Util;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.GlideImageLoader;
import com.linlic.ccmtv.yx.activity.upload.adapter.Upload_case_adapter2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.NetUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 我的意见反馈
 *
 * @author yu
 */
public class MyYjfkActivity extends BaseActivity implements   AdapterView.OnItemClickListener{
    private TextView activity_title_name, edit_body;
    private EditText phone;
    private String uid;
    Context context;
    private ProgressDialog pd;
    private RequestParams params = new RequestParams();
    private int screenWidth, screenHeight;
    private MyGridView Feedback;
    private List<PhotoInfo> list1 = new ArrayList<>();
    private Upload_case_adapter2 adapter1;
    /**
     * name：hander
     * author：Larry
     * data：2016/3/26 15:39
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            Toast.makeText(context, "感谢您的反馈！", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yjfk);
        context = this;
        findId();
        setText();
        initData();
        setOnclick();
    }


    public void setOnclick(){
        Feedback.setOnItemClickListener(this);
    }


    public void initData() {
        uid = SharedPreferencesTools.getUid(context);
        if ("".equals(uid) || uid == null) {
            return;
        }
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        PhotoInfo p = new PhotoInfo();
        p.setPhotoPath("icon_add");
        list1.add(p);
        adapter1 = new Upload_case_adapter2(context, list1);
        Feedback.setAdapter(adapter1);
    }


    public void findId() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        edit_body = (TextView) findViewById(R.id.edit_body);
        phone = (EditText) findViewById(R.id.phone);
        Feedback = (MyGridView) findViewById(R.id.Feedback);

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
        activity_title_name.setText("意见反馈");
    }


    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                resultForList(reqeustCode, resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void resultForList(int reqeustCode, List<PhotoInfo> resultList) {
//        Log.e("resultList", resultList.toString());
        if (resultList.size() <= 7) {
            if (reqeustCode == 1001) {
                refreshView(list1, adapter1, resultList, true);
            }
        } else {
            if (reqeustCode == 1001) {
                refreshView(list1, adapter1, resultList, false);
            }
        }
    }

    /**
     * name：选择完照片后回调然后刷新页面
     * author：MrSong
     * data：2016/10/19 13:57
     */
    private void refreshView(List<PhotoInfo> list, Upload_case_adapter2 adapter, List<PhotoInfo> resultList, boolean isAdd) {
        PhotoInfo p = new PhotoInfo();
        p.setPhotoPath("icon_add");
        list.clear();
        list.addAll(resultList);
        if (isAdd) list.add(list.size(), p);
        adapter.notifyDataSetChanged();
    }

    /**
     * name：GridView点击事件
     * author：MrSong
     * data：2016/10/19 14:02
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.Feedback:
                initImageSelect(1001, list1);
                break;

        }
    }

    /**
     * name：GridView点击事件调用的方法，启动图片选择器
     * author：MrSong
     * data：2016/10/19 14:03
     */
    private void initImageSelect(int requestCode, List<PhotoInfo> list) {
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setSelected(list);//添加过滤集合
        functionConfigBuilder.setMutiSelectMaxSize(9);
        functionConfigBuilder.setEnableCamera(false);
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        ImageLoader imageLoader = new GlideImageLoader();
        ThemeConfig themeConfig = ThemeConfig.DEFAULT;
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
                .setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(requestCode, functionConfig, mOnHanlderResultCallback);
    }


    /**
     * name：提交
     * author：MrSong
     * data：2016/10/19 14:11
     */
    public void commit(View view) {
        final TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String content = edit_body.getText().toString();

        String NetType = null;
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, R.string.my_yijian, Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone.getText().toString().trim().equals("")){
            Toast.makeText(context,"请留下您的联系方式！",Toast.LENGTH_SHORT).show();
            return;
        }
        //1：WIFI网络2：wap网络3：net网络
        if (NetUtil.getConnectedType(context) == 1) {
            NetType = "WIFI网络";
        } else if (NetUtil.getConnectedType(context) == 2) {
            NetType = "wap网络";
        } else if (NetUtil.getConnectedType(context) == 3) {
            NetType = "net网络";
        }
        final String finalNetType = NetType;
        /**
         * name：访问接口
         * author：MrSong
         * data：2016/10/13 17:59
         */
        pd = new ProgressDialog(context);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("加载中...");
        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        pd.show();
        JSONObject object = new JSONObject();
        JSONObject obj = new JSONObject();
        try {
            object.put("uid", uid);
            object.put("content", content);
            object.put("act", URLConfig.feedBack);
            object.put("version", Util.getVersion(context));  //当前app版本号
            object.put("model", Util.getModel());  //获取手机型号
            object.put("phonecs", Util.getChangshang());   //获取手机厂商
            object.put("phonecc", screenWidth + "*" + screenHeight);   //获取手机尺寸（分辨率）
            object.put("operator", "");   //运行商的名称
            object.put("networktype", finalNetType + ",手机Ip:" + NetUtil.getPhoneIp());   //网络的类型
            object.put("systemversion", Util.getXiTong());   //获取手机系统版本
            object.put("localmodel", "China");   //地方型号  （国际化区域名称）
            object.put("serialNo", TelephonyMgr.getDeviceId());   //手机序列号
            object.put("note", phone.getText().length()>0?phone.getText():"");   //手机序列号

            obj.put("userAccount", SharedPreferencesTools.getUserName(context));
            obj.put("password", SharedPreferencesTools.getPassword(context));
            obj.put("sourceflag", SharedPreferencesTools.getWhether_the_quick_login(context));
            obj.put("mobphone", SharedPreferencesTools.getMobphone(context));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //添加参数
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getPhotoPath().equals("icon_add")) {
                params.addBodyParameter("fileSourceAll_" + i, new File(list1.get(i).getPhotoPath()));
            }
        }

        HttpUtils httpUtils = new HttpUtils();
        params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(object.toString())));
        params.addBodyParameter("datacheck", Base64utils.getBase64(Base64utils.getBase64(obj.toString())));
        LogUtil.e("上传数据",params.toString());
        LogUtil.e("上传数据",object.toString());
        httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.CCMTVAPP, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
//                        Log.d(MyLog, "上传成功：" + responseInfo.result);
//                        Log.d(MyLog, "解密后：" + result);
                        try {
//                            Log.d(MyLog, "Message：" + new JSONObject(result).getString("errorMessage"));
                            pd.dismiss();
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        /*    new AlertDialog.Builder(context).setTitle("提示").setMessage(new JSONObject(result).getString("errorMessage"))
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            finish();
                                        }
                                    }).show();*/
                        } catch ( Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
//                        Log.d(MyLog, "上传失败：" + s);
                        pd.dismiss();
                        Toast.makeText(context, "提交失败：" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        if (isUploading == true) {
                            double cu = current;
                            double to = total;
                            double cuto = cu / to;
                            if (cuto <= 0) {
                                cuto = 0 + cuto;
                            }
                            String result = "0%";
                            if (cuto != 0) {
                                result = new DecimalFormat("#0.00").format(cuto * 100) + "%";
                            }
//                            Log.d(MyLog, "除以计算：" + result);
                            pd.setMessage("当前进度：" + result);
//                            Log.i("上传病例", "total" + total + "        当前" + current);
                            if (result.equals("100.00%")) {
                                pd.setMessage("数据解析中，请稍后...");
                            }
                        }
                    }
                });
    }

}
