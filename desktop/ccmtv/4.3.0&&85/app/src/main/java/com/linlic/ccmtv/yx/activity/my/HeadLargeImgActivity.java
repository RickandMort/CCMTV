package com.linlic.ccmtv.yx.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.shizhefei.view.largeimage.LargeImageView;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;


/**
 * name：修改头像（查看大图）
 * author：Larry
 * data：2017/1/17.
 */
public class HeadLargeImgActivity extends BaseActivity implements View.OnClickListener {
    private LargeImageView largeimg;
    private ImageView iv_showdialog;
    private RingProgressBar progress_bar;
    private Bitmap bitmap1;
    private Dialog dialog;
    private LinearLayout choosephoto, savephoto;
    private ArrayList<String> path = new ArrayList<>();
    private Handler handler = new Handler();
    BitmapUtils Utils;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headimg);
        findId();
        initView();
        onClicks();
    }

    public void findId() {
        largeimg = (LargeImageView) findViewById(R.id.largeimg);
        progress_bar = (RingProgressBar) findViewById(R.id.progress_bar);
        iv_showdialog = (ImageView) findViewById(R.id.iv_showdialog);
    }

    private void initView() {
        String url = getIntent().getStringExtra("images");
        Utils = new BitmapUtils(HeadLargeImgActivity.this);
        //     String url = "http://img.tuku.cn/file_big/201502/3d101a2e6cbd43bc8f395750052c8785.jpg";
        Log.i("url", "url" + url);
        Utils.display(largeimg, url, new DefaultBitmapLoadCallBack<LargeImageView>() {


            @Override
            public void onLoadCompleted(LargeImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {

                largeimg.setImage(bitmap);
                bitmap1 = bitmap;
                progress_bar.setVisibility(View.GONE);
            }

            @Override
            public void onLoading(LargeImageView container, String uri, BitmapDisplayConfig config, final long total, final long current) {

                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int p = 0;
                        if (total > 0) {
                            p = (int) (100 * current / total);
                        }
                        progress_bar.setVisibility(View.VISIBLE);
                        progress_bar.setProgress(p);
                    }
                });*/
                int p = 0;
                if (total > 0) {
                    p = (int) (100 * current / total);
                }
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setProgress(p);
            }
        });

    }

    @Override
    protected void onResume() {
        Utils.closeCache();
        Utils.clearDiskCache();
        Utils.clearCache();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    private void onClicks() {
        iv_showdialog.setOnClickListener(this);
    }

    public void ShowDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view1 = LayoutInflater.from(this).inflate(R.layout.changehead_dialog, null);
        choosephoto = (LinearLayout) view1.findViewById(R.id.choosephoto);
        savephoto = (LinearLayout) view1.findViewById(R.id.savephoto);
        choosephoto.setOnClickListener(this);
        savephoto.setOnClickListener(this);
        dialog.setContentView(view1);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //设置dialog的宽高属性
        dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_showdialog:
                ShowDialog();
                break;
            case R.id.choosephoto:
                if (dialog != null) {
                    dialog.dismiss();
                }

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
        /*        ImageConfig imageConfig
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
                        .requestCode(100)
                        .build();*/
// 跳转到图片选择器
                ISNav.getInstance().toListActivity(this, config, 100);
//                ImageSelector.open(HeadLargeImgActivity.this, imageConfig);   // 开启图片选择器

                break;
            case R.id.savephoto:
                if (dialog != null) {
                    dialog.dismiss();
                }
                try {
                    Uri uri = SDCardUtils.saveBitmap(HeadLargeImgActivity.this, bitmap1);
                    Toast.makeText(HeadLargeImgActivity.this, "图片已保存至" + uri, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            List<String> pathList = data.getStringArrayListExtra("result");
            path.clear();
            path.addAll(pathList);
            File file = null;
            try {
                file = new File(path.get(path.size() - 1));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "头像未修改", Toast.LENGTH_SHORT).show();
                HeadLargeImgActivity.this.finish();
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
                MyProgressBarDialogTools.show(HeadLargeImgActivity.this);
            }
            JSONObject object = new JSONObject();
            try {
                object.put("uid", SharedPreferencesTools.getUid(HeadLargeImgActivity.this));
                object.put("act", URLConfig.myPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * 保存数据到服务器
             */
            String strBase64 = Base64utils.getBase64(Base64utils.getBase64(object.toString()));
            //上传单个文件
            OkGo.<String>post(URLConfig.ccmtvapp_Myphoto)
                    .tag(this)
                    .params("data", strBase64)
                    .params("fileSource", file)
                    .isMultipart(true)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(response.body()));
                                Toast.makeText(getApplicationContext(),
                                        HttpClientUtils.encodingtoStr(new JSONObject(base64 + "")
                                                .getString("errorMessage")), Toast.LENGTH_LONG).show();
                               /* Intent intent = new Intent(HeadLargeImgActivity.this, MainActivity.class);
                                intent.putExtra("type", "register");
                                startActivity(intent);*/
                                HeadLargeImgActivity.this.finish();
                                MyProgressBarDialogTools.hide();
                            } catch (Exception e) {
                                e.printStackTrace();
                                MyProgressBarDialogTools.hide();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Toast.makeText(HeadLargeImgActivity.this,
                                    "上传失败", Toast.LENGTH_LONG).show();
                            MyProgressBarDialogTools.hide();
                            Intent intent = new Intent(HeadLargeImgActivity.this, MainActivity.class);
                            intent.putExtra("type", "register");
                            startActivity(intent);
                            HeadLargeImgActivity.this.finish();
                        }
                    });

//            HttpUtils httpUtils = new HttpUtils();
//            RequestParams params = new RequestParams();
//            // 加密二次
//            params.addBodyParameter("data", strBase64);
//            params.addBodyParameter("fileSource", file);
//            httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.ccmtvapp_Myphoto, params,
//                    new RequestCallBack<String>() {
//                        @Override
//                        public void onFailure(HttpException arg0, String arg1) {
//                            Toast.makeText(HeadLargeImgActivity.this,
//                                    "上传失败", Toast.LENGTH_LONG).show();
//                            MyProgressBarDialogTools.hide();
//                            Intent intent = new Intent(HeadLargeImgActivity.this, MainActivity.class);
//                            intent.putExtra("type", "register");
//                            startActivity(intent);
//                            HeadLargeImgActivity.this.finish();
//                        }
//
//                        @Override
//                        public void onSuccess(ResponseInfo<String> info) {
//                            try {
//                                String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
//                                Toast.makeText(getApplicationContext(),
//                                        HttpClientUtils.encodingtoStr(new JSONObject(base64 + "")
//                                                .getString("errorMessage")), Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(HeadLargeImgActivity.this, MainActivity.class);
//                                intent.putExtra("type", "register");
//                                startActivity(intent);
//                                HeadLargeImgActivity.this.finish();
//                                MyProgressBarDialogTools.hide();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                MyProgressBarDialogTools.hide();
//                            }
//                        }
//                    });

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.closeCache();
        Utils.clearDiskCache();
        Utils.clearCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeadLargeImgActivity.this.finish();
    }
}