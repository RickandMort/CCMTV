package com.linlic.ccmtv.yx.activity.my;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.SmoothImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin 查看大图
 */
public class ShowBigImageActivity extends BaseActivity {
    Context context;
    SmoothImageView imageView = null;
    private String urlsString;
    Uri uri = null;
    File file;
    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;
    //加载中
    private EditText loadingEditText;
    private AnimationDrawable anim;
    private ImageView loadingImageView;
    //定义标题栏弹窗按钮
    private ImageView iv_showPop;
    private static ArrayList<String> path = new ArrayList<>();
    public static final int REQUEST_CODE = 1000;
    String uid;
    IntentFilter filter;
    //广播接收器
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) //onReceive函数不能做耗时的事情，参考值：10s以内
        {
            String action = intent.getAction();
            if (action.equals("showpop")) {
            /*    ImageConfig imageConfig
                        = new ImageConfig.Builder(
                        // GlideLoader 可用自己用的缓存库
                        new GlideLoader())
                        .singleSelect()
                        .crop()
                        // 多选时的最大数量   （默认 9 张）
                        .mutiSelectMaxSize(9)
                        // 已选择的图片路径
                        .pathList(path)
                        // 拍照后存放的图片路径（默认 /temp/picture）
                        .filePath(URLConfig.ccmtvapp_basesdcardpath)
                        // 开启拍照功能 （默认开启）
                        .showCamera()
                        .requestCode(REQUEST_CODE)
                        .build();

                ImageSelector.open(ShowBigImageActivity.this, imageConfig);   // 开启图片选择器*/
                // 自由配置选项
                ISListConfig config = new ISListConfig.Builder()
                        // 是否多选, 默认true
                        .multiSelect(false)
                        // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                        .rememberSelected(false)
                        // “确定”按钮背景色
                        .btnBgColor(Color.GRAY)
                        // “确定”按钮文字颜色
                        .btnTextColor(Color.BLUE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5"))
                        // 返回图标ResId
                        .backResId(R.mipmap.bark)
                        // 标题
                        .title("图片")
                        // 标题文字颜色
                        .titleColor(Color.WHITE)
                        // TitleBar背景色
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        // 裁剪大小。needCrop为true的时候配置
                        .cropSize(1, 1, 200, 200)
                        .needCrop(true)
                        // 第一个是否显示相机，默认true
                        .needCamera(false)
                        // 最大选择图片数量，默认9
                        .maxNum(9)
                        .build();

// 跳转到图片选择器
                ISNav.getInstance().toListActivity(this, config, REQUEST_CODE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        context = this;
        imageView = (SmoothImageView) findViewById(R.id.smooth_image);
        loadingEditText = (EditText) findViewById(R.id.loading_editText_info);
        loadingEditText.setInputType(InputType.TYPE_NULL);//屏蔽软键盘
        loadingImageView = (ImageView) findViewById(R.id.loading_imageView_info);
        anim = (AnimationDrawable) loadingImageView.getBackground();
        loadingEditText.setOnFocusChangeListener(editSetOnFocus);
        filter = new IntentFilter();
        filter.addAction("showpop");
        ShowBigImageActivity.this.registerReceiver(mBroadcastReceiver, filter);
        uid = SharedPreferencesTools.getUid(this);
        //实例化标题栏按钮并设置监听
        iv_showPop = (ImageView) findViewById(R.id.iv_showPop);
        iv_showPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        urlsString = getIntent().getStringExtra("images");
        imageView.setScaleType(ScaleType.FIT_CENTER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //final Bitmap bitmap = getBitMBitmap(urlsString);
                // uri = saveBitmap(bitmap);   String name = "ccmtv_app_headimg.png";
                uri = SDCardUtils.saveImageBitmap(ShowBigImageActivity.this, ImageLoader.getInstance().loadImageSync(urlsString), "ccmtv_app_headimg.png");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (String.valueOf(uri).equals("null")) {
                            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                                    R.mipmap.img_default));
                            anim.stop();
                            loadingImageView.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.load_fail, Toast.LENGTH_LONG).show();
                        } else {
                            imageView.setImageBitmap(ImageLoader.getInstance().loadImageSync(String.valueOf(uri)), window_width, window_height);
                            anim.stop();
                            loadingImageView.setVisibility(View.GONE);

                        }
                    }
                });
            }
        }).start();

        // 设置图片
       /* if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.img_default);
        }*/

        imageView.setmActivity(this);// 注入Activity.
        /** 测量状态栏高度 **/
        viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (state_height == 0) {
                            // 获取状况栏高度
                            Rect frame = new Rect();
                            getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(frame);
                            state_height = frame.top;
                            imageView.setScreen_H(window_height
                                    - state_height);
                            imageView.setScreen_W(window_width);
                        }

                    }
                });
        // setContentView(imageView);

    }

    private void showPopupWindow(View v) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.title_popup, null);
        TextView tv_upheadimg = (TextView) contentView.findViewById(R.id.tv_upheadimg);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopAnimStyle);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_function_bg1));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(v);
        tv_upheadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击子类项后，弹窗消失
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("showpop");
                ShowBigImageActivity.this.sendBroadcast(intent);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 当输入框获取焦点，则运行动画
     */
    private View.OnFocusChangeListener editSetOnFocus = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            anim.start();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            List<String> pathList = data.getStringArrayListExtra("result");
            path.clear();
            path.addAll(pathList);
            File file = null;
            try {
                file = new File(path.get(path.size() - 1));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "头像未修改", Toast.LENGTH_SHORT).show();
                ShowBigImageActivity.this.finish();
                return;
            }
            /**
             * name：修改头像
             */
            if (file == null) {
                Toast.makeText(getApplicationContext(), "头像未修改", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Toast.makeText(ShowBigImageActivity.this, "正在上传中...", Toast.LENGTH_SHORT).show();
                MyProgressBarDialogTools.show(ShowBigImageActivity.this);
            }
            JSONObject object = new JSONObject();
            try {
                object.put("uid", uid);
                object.put("act", URLConfig.myPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            // 加密二次
            String strBase64 = Base64utils.getBase64(Base64utils.getBase64(object.toString()));
            params.addBodyParameter("data", strBase64);
            params.addBodyParameter("fileSource", file);
            httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.ccmtvapp_Myphoto, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(ShowBigImageActivity.this,
                                    "上传失败", Toast.LENGTH_LONG).show();
                            MyProgressBarDialogTools.hide();
                            Intent intent = new Intent(ShowBigImageActivity.this, MainActivity.class);
                            intent.putExtra("type", "register");
                            startActivity(intent);
                            ShowBigImageActivity.this.finish();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> info) {
                            try {
                                String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
                                Toast.makeText(getApplicationContext(),
                                        HttpClientUtils.encodingtoStr(new JSONObject(base64 + "")
                                                .getString("errorMessage")), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ShowBigImageActivity.this, MainActivity.class);
                                intent.putExtra("type", "register");
                                startActivity(intent);
                                ShowBigImageActivity.this.finish();
                                MyProgressBarDialogTools.hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                                MyProgressBarDialogTools.hide();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
        super.onPause();
    }
}