package com.linlic.ccmtv.yx.activity.my;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 医生执照认证
 *
 * @author yu
 */
public class MyYszzrzActivity extends BaseActivity {
    TextView activity_title_name;
    ImageView iv_choosezz, iv_showzz;
    Button carema, album, give_up;
    private File file;
    Context context;
    EditText edit_truename, edit_zjnum;
    public static final int REQUEST_CODE = 1000;
    private static ArrayList<String> path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yszzrz);
        context = this;
        findId();
        setClick();
        setText();
    }

    public void findId() {
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        iv_showzz = (ImageView) findViewById(R.id.iv_showzz);
        iv_choosezz = (ImageView) findViewById(R.id.iv_choosezz);
        edit_truename = (EditText) findViewById(R.id.edit_truename);
        edit_zjnum = (EditText) findViewById(R.id.edit_zjnum);
    }

    public void setClick() {
        iv_choosezz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showDialogs();
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
                ImageSelector.open(MyYszzrzActivity.this, imageConfig);   // 开启图片选择器*/

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
        });
    }

    public void setText() {
        activity_title_name.setText("医生执照认证");
    }

    // 更换头像点击事件
    private void showDialogs() {
        View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        carema = (Button) view.findViewById(R.id.camera);
        album = (Button) view.findViewById(R.id.album);
        give_up = (Button) view.findViewById(R.id.give_up);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        carema.setOnClickListener(new View.OnClickListener() {
            // 调用相机
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                /**  压缩版 */
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 20);
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            // 扫描本地相册
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                /**  压缩版 */
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        give_up.setOnClickListener(new View.OnClickListener() {

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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            List<String> pathList = data.getStringArrayListExtra("result");
            path.clear();
            path.addAll(pathList);
            File file = new File(path.get(path.size() - 1));
//            Log.i("file", file.toString() + "path" + path.get(path.size() - 1));
            Bitmap bit = BitmapFactory.decodeFile(path.get(path.size() - 1));
            saveBitmap(bit);
            iv_showzz.setVisibility(View.VISIBLE);
            iv_showzz.setImageBitmap(bit);
            iv_choosezz.setVisibility(View.GONE);
       /* try {
            System.out.println(requestCode + "|" + resultCode + "|" + data);
            switch (requestCode) {
                case 20://相机
                    System.out.println("相机");

                    *//**  压缩版 *//*
                    Bitmap bitmap = data.getParcelableExtra("data");//Extras().getParcelable("data");
                    //saveImgForSD(bitmap);

                    Uri uri = saveBitmap(bitmap);
                    startImageZoom(uri);//图像裁剪

                    break;
                case 10://图库
                    System.out.println("图库");
                    if (data != null) {

                        *//**  压缩版 *//*
                        Uri ur = data.getData();
                        startImageZoom(convertUri(ur));//图像裁剪

                    }
                    break;
                case 30://图像裁剪
                    System.out.println("图像裁剪");
                    if (data != null) {
                        if (data.getExtras() != null) {
                            Bitmap bit = data.getExtras().getParcelable("data");
                            saveBitmap(bit);
                            iv_showzz.setVisibility(View.VISIBLE);
                            iv_showzz.setImageBitmap(bit);
                            iv_choosezz.setVisibility(View.GONE);
                            iv_showzz.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialogs();
                                }
                            });
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }*/
        }
    }

    /**
     * name：保存图片到内存卡(图片裁剪调用)
     *
     * @param bitmap
     * @return
     */
    private Uri saveBitmap(Bitmap bitmap) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/ccmtvCache");
            if (!dir.exists())
                dir.mkdirs();
            String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
            file = new File(dir, name);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                return Uri.fromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.myhead_sd_is_null, Toast.LENGTH_LONG).show();
        }
        return null;
    }

    /**
     * name：图像裁剪(方形)
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //小米问题就卡在这
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 30);
    }

    private Uri convertUri(Uri uri) {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * name：修改头像
     *
     * @param view
     * @throws JSONException
     */
    public void Submit_ZZ(View view) throws JSONException {
        if (TextUtils.isEmpty(edit_truename.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.truename_toast, Toast.LENGTH_SHORT).show();
            edit_truename.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edit_zjnum.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.zjnum_toast, Toast.LENGTH_SHORT).show();
            edit_zjnum.requestFocus();
            return;
        }
        final String uid = SharedPreferencesTools.getUid(context);
        if (uid == null || ("").equals(uid)) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            if (file == null) {
                Toast.makeText(getApplicationContext(), "未选择执照", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject object = new JSONObject();
            object.put("uid", uid);
            object.put("truename", edit_truename.getText().toString());
            object.put("idcard", edit_zjnum.getText().toString());
            object.put("act", URLConfig.license_certification);

            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();

            // 加密二次
            String strBase64 = Base64utils.getBase64(Base64utils.getBase64(object.toString()));
            params.addBodyParameter("data", strBase64);
            params.addBodyParameter("fileSource", file);
            httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.ccmtvapp_uploadCase, params, new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(getApplicationContext(), "上传失败！", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> info) {
                            try {
                                String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
                                Toast.makeText(context, HttpClientUtils.encodingtoStr(new JSONObject(base64 + "").getString("errorMessage")), Toast.LENGTH_LONG).show();
                                MyProgressBarDialogTools.hide();
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("type", "register");
                                startActivity(intent);
                                MyYszzrzActivity.this.finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
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
