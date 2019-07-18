package com.linlic.ccmtv.yx.activity.my;

import android.net.Uri;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView.ScaleType;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.widget.ZoomImageView;

import java.io.File;

/**
 * @author admin 查看大图
 */
public class ShowBigImagesActivity extends BaseActivity {
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    ZoomImageView imageView = null;
    private String urlsString;
    File file;
    //
    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        imageView = (ZoomImageView) findViewById(R.id.smooth_image);
        urlsString = getIntent().getStringExtra("images");
        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();

        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);
        urlsString = getIntent().getStringExtra("images");
        imageView.setScaleType(ScaleType.MATRIX);

        //ImageLoader.getInstance().displayImage(urlsString, imageView);
        // Bitmap bitmap = ImageLoader.getInstance().loadImageSync("file:///storage/emulated/0/tencent/MicroMsg/WeiXin/1461902148349.jpg");
        //Bitmap bitmap =  ImageLoader.getInstance().loadImageSync(urlsString);

       /* new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getBitMBitmap(urlsString);
                uri = saveBitmap(bitmap);
                //ImageLoader.getInstance().displayImage(String.valueOf(uri), imageView);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // imageView.setImageBitmap(bitmap);
                       imageView.setImageBitmap(ImageLoader.getInstance().loadImageSync(String.valueOf(uri)));
                    }
                });
            }
        }).start();*/

        // 设置图片
       /* if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.img_default);
        }*/
        //imageView.setImageBitmap(bitmap);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

}