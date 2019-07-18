package com.linlic.ccmtv.yx.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.FaceDetector;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
//import android.widget.ImageView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Formal_examination;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/** 悬浮窗 图片服务
 * Created by dongzhong on 2018/5/30.
 */

public class FloatingImageDisplayService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
//    private TextureView cView;
//    private Camera mCamera;

    private boolean is_save = false;
    private View displayView;
//    private CameraView cameraView;
//    private RectanglesView rectanglesView;

    private boolean hasCameraPermission;



    private int imageIndex = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 250;
        layoutParams.height = 400;
        layoutParams.x = 300;
        layoutParams.y = 300;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getIntExtra("type", -1)) {
            case 1:
                is_save = true;
//                addCallBack();
                break;
            default:
                showFloatingWindow();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {
        if(Build.VERSION.SDK_INT >= 23 ) {
            if (Settings.canDrawOverlays(this)) {
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                displayView = layoutInflater.inflate(R.layout.image_display, null);
                displayView.setOnTouchListener(new FloatingOnTouchListener());

//                cView = displayView.findViewById(R.id.texture_view);
//            cameraView = displayView.findViewById(R.id.cameraView);
//            rectanglesView = displayView.findViewById(R.id.rectanglesView);
                windowManager.addView(displayView, layoutParams);
//                cView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//                    @Override
//                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                        // 打开相机 0后置 1前置
//                        mCamera = Camera.open(1);
//                        if (mCamera != null) {
//                            // 设置相机预览宽高，此处设置为TextureView宽高
//                            Camera.Parameters params = mCamera.getParameters();
//                            params.setPreviewSize(width, height);
//                            // 设置自动对焦模式
//                            List<String> focusModes = params.getSupportedFocusModes();
//                            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//                                mCamera.setParameters(params);
//                            }
//                            try {
//                                mCamera.setDisplayOrientation(90);// 设置预览角度，并不改变获取到的原始数据方向
//                                // 绑定相机和预览的View
//                                mCamera.setPreviewTexture(surface);
//                                // 开始预览
//                                mCamera.startPreview();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//                    }
//
//                    @Override
//                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                        mCamera.stopPreview();
//                        mCamera.release();
//                        return false;
//                    }
//
//                    @Override
//                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//                    }
//                });




            }
        }else{
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.image_display, null);
            displayView.setOnTouchListener(new FloatingOnTouchListener());
//            cView = displayView.findViewById(R.id.texture_view);
//            cameraView = displayView.findViewById(R.id.cameraView);
//            rectanglesView = displayView.findViewById(R.id.rectanglesView);


          /*  cView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    // 打开相机 0后置 1前置
                    mCamera = Camera.open(1);
                    if (mCamera != null) {
                        // 设置相机预览宽高，此处设置为TextureView宽高
                        Camera.Parameters params = mCamera.getParameters();
                        params.setPreviewSize(width, height);
                        // 设置自动对焦模式
                        List<String> focusModes = params.getSupportedFocusModes();
                        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                            mCamera.setParameters(params);
                        }
                        try {
                            mCamera.setDisplayOrientation(90);// 设置预览角度，并不改变获取到的原始数据方向
                            // 绑定相机和预览的View
                            mCamera.setPreviewTexture(surface);
                            // 开始预览
                            mCamera.startPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    mCamera.stopPreview();
                    mCamera.release();
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });*/
        }

    }

   /* private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                imageIndex++;
                if (imageIndex >= 5) {
                    imageIndex = 0;
                }
                if (displayView != null) {
                    ((ImageView) displayView.findViewById(R.id.image_display_imageview)).setImageResource(images[imageIndex]);
                }

                changeImageHandler.sendEmptyMessageDelayed(0, 2000);
            }
            return false;
        }
    };*/


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }







    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (hasCameraPermission) {
        }
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }





}
