package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.kzbf.widget.ZoomableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BigImageActivity extends BaseActivity {
    private TextView imageTextView = null;
    private String imagePath = null;
    private ZoomableImageView imageView = null;
    Context context;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        context = this;
        imagePath = getIntent().getStringExtra("image");

        imageTextView = (TextView) findViewById(R.id.show_webimage_imagepath_textview);
        imageTextView.setText(imagePath);
        imageView = (ZoomableImageView) findViewById(R.id.show_webimage_imageview);
        setImage();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap(bitmap);
        }
    };

    public void setImage() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = ((BitmapDrawable) loadImageFromUrl(imagePath)).getBitmap();

                    Message msg = new Message();
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public static Drawable loadImageFromUrl(String url) throws IOException {

        URL m = new URL(url);
        InputStream i = (InputStream) m.getContent();
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }
}
