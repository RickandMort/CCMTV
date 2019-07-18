package com.linlic.ccmtv.yx.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * name：我的头像
 * <p/>
 * author: Mr.song
 * 时间：2016-3-3 下午6:40:01
 *
 * @author Administrator
 */
public class MyHead extends BaseActivity {
    private CircleImageView myhead_img;
    private String uid, Str_icon;
    Button carema, album, give_up;
    Context context;
    private String appDir;// 存储文件夹目录地址;
    private String newImagePath;// 拍照得到的新图片地址;
    private File file;
    public Uri uri;
    public static String fileName;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_myhead);
        context = this;
        super.findId();
        super.setActivity_title_name("我的头像");
        myhead_img = (CircleImageView) findViewById(R.id.myhead_img);
        Str_icon = getIntent().getStringExtra("Str_icon");
        uid = SharedPreferencesTools.getUid(context);
        //显示头像
        if (!TextUtils.isEmpty(Str_icon)) {
            new Carousel_figure(context).loadImageNoCache(Str_icon, myhead_img);  //无缓存
        }
        myhead_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialogs();
            }
        });
    }

    // 更换头像点击事件
    private void showDialogs() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
                null);
        carema = (Button) view.findViewById(R.id.camera);
        album = (Button) view.findViewById(R.id.album);
        give_up = (Button) view.findViewById(R.id.give_up);
        final Dialog dialog = new Dialog(this,
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画R.style.main_menu_animstyle
        window.setWindowAnimations(R.style.main_menu_animstyle);
        android.view.WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        carema.setOnClickListener(new OnClickListener() {
            // 调用相机
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                if (SDCardUtils.isExistSDCard()) {
                    appDir = SDCardUtils.createAppDir();
                } else {
                    appDir = MyHead.this.getFilesDir().getPath();
                }
                // 调用系统的拍照功能
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                newImagePath = appDir + "/" + getPhotoFileName();
                File file = new File(newImagePath);
                uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            }
        });

        album.setOnClickListener(new OnClickListener() {
            // 扫描本地相册
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

        give_up.setOnClickListener(new OnClickListener() {

            @Override
        public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO: // 拍照
                    startPhotoZoom(uri, 150);
                    break;

                case PHOTO_REQUEST_GALLERY: // 相册选择
                    if (data != null)
                        startPhotoZoom(data.getData(), 150);
                    break;

                case PHOTO_REQUEST_CUT:
                    if (data != null)
                        setPicToView(data);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    // 头像裁切
    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            // 将文件写入流中
            String rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            fileName = rootPath + "/" + System.currentTimeMillis() + ".jpg";
            file = new File(fileName);
            try {
                file.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            myhead_img.setImageBitmap(bitmap);
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * name：修改头像
     * <p/>
     * author: Mr.song
     * 时间：2016-3-3 下午6:43:55
     *
     * @param view
     * @throws JSONException
     */
    public void updateMyHead(View view) throws JSONException {
        if (file == null) {
            showDialogs();
            return;
        }
        JSONObject object = new JSONObject();
        object.put("uid", uid);
        object.put("act", URLConfig.myPhoto);

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        // 加密二次
        String strBase64 = Base64utils.getBase64(Base64utils.getBase64(object.toString()));
        params.addBodyParameter("data", strBase64);
        params.addBodyParameter("fileSource", file);
        httpUtils.send(HttpMethod.POST, URLConfig.ccmtvapp_Myphoto, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> info) {
                        try {
                            String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
                            Toast.makeText(getApplicationContext(),
                                    HttpClientUtils.encodingtoStr(new JSONObject(base64 + "")
                                            .getString("errorMessage")), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MyHead.this, MainActivity.class);
                            intent.putExtra("type", "register");
                            startActivity(intent);
                            MyHead.this.finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getApplicationContext());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
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
