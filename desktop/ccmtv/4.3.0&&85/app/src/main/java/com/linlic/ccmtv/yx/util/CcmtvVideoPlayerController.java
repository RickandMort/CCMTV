package com.linlic.ccmtv.yx.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.xiao.nicevideoplayer.ChangeClarityDialog;
import com.xiao.nicevideoplayer.Clarity;
import com.xiao.nicevideoplayer.INiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceUtil;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by bentley on 2018/8/9.
 */

public class CcmtvVideoPlayerController extends NiceVideoPlayerController implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, ChangeClarityDialog.OnClarityChangedListener {
    private Context mContext;
    private ImageView mImage;
    private ImageView mCenterStart;
    private LinearLayout mTop;
    private ImageView mBack;
    private TextView mTitle;
    private LinearLayout mBatteryTime;
    private ImageView mBattery;
    private TextView mTime;
    private LinearLayout mBottom;
    private ImageView mRestartPause;
    private TextView mPosition;
    private TextView mDuration;
    private SeekBar mSeek;
    private TextView mClarity;
    private ImageView mFullScreen;
    private TextView mLength;
    private LinearLayout mLoading;
    private TextView mLoadText;
    private LinearLayout mChangePositon;
    private TextView mChangePositionCurrent;
    private ProgressBar mChangePositionProgress;
    private LinearLayout mChangeBrightness;
    private ProgressBar mChangeBrightnessProgress;
    private LinearLayout mChangeVolume;
    private ProgressBar mChangeVolumeProgress;
    private LinearLayout mError;
    private TextView mRetry;
    private LinearLayout mCompleted;
    private TextView mReplay;
    private TextView mShare;
    private boolean topBottomVisible;
    private CountDownTimer mDismissTopBottomCountDownTimer;
    private List<Clarity> clarities;
    private int defaultClarityIndex;
    private ChangeClarityDialog mClarityDialog;
    private boolean hasRegisterBatteryReceiver;
    Intent intent = new Intent();

    private LinearLayout ll_video_sign;
    private LinearLayout ll_sign2;
    private TextView video_sign4;
    private TextView video_sign2;
    private TextView video_sign_img2;

    private float mDownX;
    private float mDownY;
    private boolean mNeedChangePosition;
    private boolean mNeedChangeVolume;
    private boolean mNeedChangeBrightness;
    private long mGestureDownPosition;
    private float mGestureDownBrightness;
    private int mGestureDownVolume;
    private long mNewPosition;

    private BroadcastReceiver mBatterReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra("status", 1);
            if(status == 2) {
                CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_charging);
            } else if(status == 5) {
                CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_full);
            } else {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 0);
                int percentage = (int)((float)level / (float)scale * 100.0F);
                if(percentage <= 10) {
                    CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_10);
                } else if(percentage <= 20) {
                    CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_20);
                } else if(percentage <= 50) {
                    CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_50);
                } else if(percentage <= 80) {
                    CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_80);
                } else if(percentage <= 100) {
                    CcmtvVideoPlayerController.this.mBattery.setImageResource(com.xiao.nicevideoplayer.R.drawable.battery_100);
                }
            }

        }
    };

    public CcmtvVideoPlayerController(Context context) {
        super(context);
        this.mContext = context;
        this.init();
        intent.setAction("cn.cc.sdk.progress.changed");
    }

    private void init() {
        LayoutInflater.from(this.mContext).inflate(R.layout.ccmtv_video_palyer_controller, this, true);
        this.mCenterStart = (ImageView)this.findViewById(R.id.center_start);
        this.mImage = (ImageView)this.findViewById(R.id.image);
        this.mTop = (LinearLayout)this.findViewById(R.id.top);
        this.mBack = (ImageView)this.findViewById(R.id.back);
        this.mTitle = (TextView)this.findViewById(R.id.title);
        this.mBatteryTime = (LinearLayout)this.findViewById(R.id.battery_time);
        this.mBattery = (ImageView)this.findViewById(R.id.battery);
        this.mTime = (TextView)this.findViewById(R.id.time);
        this.mBottom = (LinearLayout)this.findViewById(R.id.bottom);
        this.mRestartPause = (ImageView)this.findViewById(R.id.restart_or_pause);
        this.mPosition = (TextView)this.findViewById(R.id.position);
        this.mDuration = (TextView)this.findViewById(R.id.duration);
        this.mSeek = (SeekBar)this.findViewById(R.id.seek);
        this.mFullScreen = (ImageView)this.findViewById(R.id.full_screen);
        this.mClarity = (TextView)this.findViewById(R.id.clarity);
        this.mLength = (TextView)this.findViewById(R.id.length);
        this.mLoading = (LinearLayout)this.findViewById(R.id.loading);
        this.mLoadText = (TextView)this.findViewById(R.id.load_text);
        this.mChangePositon = (LinearLayout)this.findViewById(R.id.change_position);
        this.mChangePositionCurrent = (TextView)this.findViewById(R.id.change_position_current);
        this.mChangePositionProgress = (ProgressBar)this.findViewById(R.id.change_position_progress);
        this.mChangeBrightness = (LinearLayout)this.findViewById(R.id.change_brightness);
        this.mChangeBrightnessProgress = (ProgressBar)this.findViewById(R.id.change_brightness_progress);
        this.mChangeVolume = (LinearLayout)this.findViewById(R.id.change_volume);
        this.mChangeVolumeProgress = (ProgressBar)this.findViewById(R.id.change_volume_progress);
        this.mError = (LinearLayout)this.findViewById(R.id.error);
        this.mRetry = (TextView)this.findViewById(R.id.retry);
        this.mCompleted = (LinearLayout)this.findViewById(R.id.completed);
        this.mReplay = (TextView)this.findViewById(R.id.replay);
        this.mShare = (TextView)this.findViewById(R.id.share);
        this.mCenterStart.setOnClickListener(this);
        this.mBack.setOnClickListener(this);
        this.mRestartPause.setOnClickListener(this);
        this.mFullScreen.setOnClickListener(this);
        this.mClarity.setOnClickListener(this);
        this.mRetry.setOnClickListener(this);
        this.mReplay.setOnClickListener(this);
        this.mShare.setOnClickListener(this);
        this.mSeek.setOnSeekBarChangeListener(this);
        this.setOnClickListener(this);

        this.ll_video_sign = this.findViewById(R.id.ll_video_sign);
        this.ll_sign2 = this.findViewById(R.id.ll_sign2);
        this.video_sign4 = this.findViewById(R.id.video_sign4);
        this.video_sign2 = this.findViewById(R.id.video_sign2);
        this.video_sign_img2 = this.findViewById(R.id.video_sign_img2);
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public ImageView imageView() {
        return this.mImage;
    }

    public void setImage(@DrawableRes int resId) {
        this.mImage.setImageResource(resId);
    }

    public void setLenght(long length) {
        this.mLength.setText(NiceUtil.formatTime(length));
    }

    public void setNiceVideoPlayer(INiceVideoPlayer niceVideoPlayer) {
        super.setNiceVideoPlayer(niceVideoPlayer);
        if(this.clarities != null && this.clarities.size() > 1) {
            this.mNiceVideoPlayer.setUp(((Clarity)this.clarities.get(this.defaultClarityIndex)).videoUrl, (Map)null);
        }

    }

    public void setClarity(List<Clarity> clarities, int defaultClarityIndex) {
        if(clarities != null && clarities.size() > 1) {
            this.clarities = clarities;
            this.defaultClarityIndex = defaultClarityIndex;
            ArrayList clarityGrades = new ArrayList();
            Iterator var4 = clarities.iterator();

            while(var4.hasNext()) {
                Clarity clarity = (Clarity)var4.next();
                clarityGrades.add(clarity.grade + " " + clarity.p);
            }

            this.mClarity.setText(((Clarity)clarities.get(defaultClarityIndex)).grade);
            this.mClarityDialog = new ChangeClarityDialog(this.mContext);
            this.mClarityDialog.setClarityGrade(clarityGrades, defaultClarityIndex);
            this.mClarityDialog.setOnClarityCheckedListener(this);
            if(this.mNiceVideoPlayer != null) {
                this.mNiceVideoPlayer.setUp(((Clarity)clarities.get(defaultClarityIndex)).videoUrl, (Map)null);
            }
        }

    }

    protected void onPlayStateChanged(int playState) {
        switch(playState) {
            case -1:
                this.cancelUpdateProgressTimer();
                this.setTopBottomVisible(false);
                this.mTop.setVisibility(0);
                this.mError.setVisibility(0);
            case 0:
            default:
                break;
            case 1:
                this.mImage.setVisibility(8);
                this.mLoading.setVisibility(0);
                this.mLoadText.setText("正在准备...");
                this.mError.setVisibility(8);
                this.mCompleted.setVisibility(8);
                this.mTop.setVisibility(8);
                this.mBottom.setVisibility(8);
                this.mCenterStart.setVisibility(8);
                this.mLength.setVisibility(8);
                break;
            case 2:
                this.startUpdateProgressTimer();
                break;
            case 3:
                this.mLoading.setVisibility(8);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_pause);
                this.startDismissTopBottomTimer();
                break;
            case 4:
                this.mLoading.setVisibility(8);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_start);
                this.cancelDismissTopBottomTimer();
                break;
            case 5:
                this.mLoading.setVisibility(0);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_pause);
                this.mLoadText.setText("正在缓冲...");
                this.startDismissTopBottomTimer();
                break;
            case 6:
                this.mLoading.setVisibility(0);
                this.mRestartPause.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_start);
                this.mLoadText.setText("正在缓冲...");
                this.cancelDismissTopBottomTimer();
                break;
            case 7:
                this.cancelUpdateProgressTimer();
                this.setTopBottomVisible(false);
                this.mImage.setVisibility(0);
                this.mCompleted.setVisibility(0);
        }

    }

    protected void onPlayModeChanged(int playMode) {
        switch(playMode) {
            case 10:
                this.mBack.setVisibility(8);
                this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
                this.mFullScreen.setVisibility(0);
                this.mClarity.setVisibility(8);
                this.mBatteryTime.setVisibility(8);
                if(this.hasRegisterBatteryReceiver) {
                    this.mContext.unregisterReceiver(this.mBatterReceiver);
                    this.hasRegisterBatteryReceiver = false;
                }
                break;
            case 11:
                this.mBack.setVisibility(0);
                this.mFullScreen.setVisibility(8);
                this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_shrink);
                if(this.clarities != null && this.clarities.size() > 1) {
                    this.mClarity.setVisibility(0);
                }

                this.mBatteryTime.setVisibility(0);
                if(!this.hasRegisterBatteryReceiver) {
                    this.mContext.registerReceiver(this.mBatterReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
                    this.hasRegisterBatteryReceiver = true;
                }
                break;
            case 12:
                this.mBack.setVisibility(0);
                this.mClarity.setVisibility(8);
        }

    }

    protected void reset() {
        this.topBottomVisible = false;
        this.cancelUpdateProgressTimer();
        this.cancelDismissTopBottomTimer();
        this.mSeek.setProgress(0);
        this.mSeek.setSecondaryProgress(0);
        this.mCenterStart.setVisibility(0);
        this.mImage.setVisibility(0);
        this.mBottom.setVisibility(8);
        this.mFullScreen.setImageResource(com.xiao.nicevideoplayer.R.drawable.ic_player_enlarge);
        this.mLength.setVisibility(0);
        this.mTop.setVisibility(0);
        this.mBack.setVisibility(8);
        this.mLoading.setVisibility(8);
        this.mError.setVisibility(8);
        this.mCompleted.setVisibility(8);
    }

    public void onClick(View v) {
        if(v == this.mCenterStart) {
            if(this.mNiceVideoPlayer.isIdle()) {
                this.mNiceVideoPlayer.start();
            }
        } else if(v == this.mBack) {
            if(this.mNiceVideoPlayer.isFullScreen()) {
                this.mNiceVideoPlayer.exitFullScreen();
            } else if(this.mNiceVideoPlayer.isTinyWindow()) {
                this.mNiceVideoPlayer.exitTinyWindow();
            }
        } else if(v == this.mRestartPause) {
            if(!this.mNiceVideoPlayer.isPlaying() && !this.mNiceVideoPlayer.isBufferingPlaying()) {
                if(this.mNiceVideoPlayer.isPaused() || this.mNiceVideoPlayer.isBufferingPaused()) {
                    this.mNiceVideoPlayer.restart();
                }
            } else {
                this.mNiceVideoPlayer.pause();
            }
        } else if(v == this.mFullScreen) {
            if(!this.mNiceVideoPlayer.isNormal() && !this.mNiceVideoPlayer.isTinyWindow()) {
                if(this.mNiceVideoPlayer.isFullScreen()) {
                    this.mNiceVideoPlayer.exitFullScreen();
                }
            } else {
                this.mNiceVideoPlayer.enterFullScreen();
            }
        } else if(v == this.mClarity) {
            this.setTopBottomVisible(false);
            this.mClarityDialog.show();
        } else if(v == this.mRetry) {
            this.mNiceVideoPlayer.restart();
        } else if(v == this.mReplay) {
            this.mRetry.performClick();
        } else if(v == this.mShare) {
            Toast.makeText(this.mContext, "分享", 0).show();
        } else if(v == this && (this.mNiceVideoPlayer.isPlaying() || this.mNiceVideoPlayer.isPaused() || this.mNiceVideoPlayer.isBufferingPlaying() || this.mNiceVideoPlayer.isBufferingPaused())) {
            this.setTopBottomVisible(!this.topBottomVisible);
        }

    }

    public void onClarityChanged(int clarityIndex) {
        Clarity clarity = (Clarity)this.clarities.get(clarityIndex);
        this.mClarity.setText(clarity.grade);
        long currentPosition = this.mNiceVideoPlayer.getCurrentPosition();
        this.mNiceVideoPlayer.releasePlayer();
        this.mNiceVideoPlayer.setUp(clarity.videoUrl, (Map)null);
        this.mNiceVideoPlayer.start(currentPosition);
    }

    public void onClarityNotChanged() {
        this.setTopBottomVisible(true);
    }

    private void setTopBottomVisible(boolean visible) {
        this.mTop.setVisibility(visible?0:8);
        this.mBottom.setVisibility(visible?0:8);
        this.topBottomVisible = visible;
        if(visible) {
            if(!this.mNiceVideoPlayer.isPaused() && !this.mNiceVideoPlayer.isBufferingPaused()) {
                this.startDismissTopBottomTimer();
            }
        } else {
            this.cancelDismissTopBottomTimer();
        }

    }

    private void startDismissTopBottomTimer() {
        this.cancelDismissTopBottomTimer();
        if(this.mDismissTopBottomCountDownTimer == null) {
            this.mDismissTopBottomCountDownTimer = new CountDownTimer(8000L, 8000L) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    CcmtvVideoPlayerController.this.setTopBottomVisible(false);
                }
            };
        }

        this.mDismissTopBottomCountDownTimer.start();
    }

    private void cancelDismissTopBottomTimer() {
        if(this.mDismissTopBottomCountDownTimer != null) {
            this.mDismissTopBottomCountDownTimer.cancel();
        }

    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if(this.mNiceVideoPlayer.isBufferingPaused() || this.mNiceVideoPlayer.isPaused()) {
            this.mNiceVideoPlayer.restart();
        }

        long position = (long)((float)(this.mNiceVideoPlayer.getDuration() * (long)seekBar.getProgress()) / 100.0F);
        this.mNiceVideoPlayer.seekTo(position);
        this.startDismissTopBottomTimer();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!this.mNiceVideoPlayer.isFullScreen()) {
            return false;
        } else if(!this.mNiceVideoPlayer.isIdle() && !this.mNiceVideoPlayer.isError() && !this.mNiceVideoPlayer.isPreparing() && !this.mNiceVideoPlayer.isPrepared() && !this.mNiceVideoPlayer.isCompleted()) {
            float x = event.getX();
            float y = event.getY();
            switch(event.getAction()) {
                case 0:
                    this.mDownX = x;
                    this.mDownY = y;
                    //this.mNeedChangePosition = false;
                    this.mNeedChangeVolume = false;
                    this.mNeedChangeBrightness = false;
                    break;
                case 1:
                case 3:
                    /*if(this.mNeedChangePosition) {
                        this.mNiceVideoPlayer.seekTo(this.mNewPosition);
                        this.hideChangePosition();
                        this.startUpdateProgressTimer();
                        return true;
                    }*/

                    if(this.mNeedChangeBrightness) {
                        this.hideChangeBrightness();
                        return true;
                    }

                    if(this.mNeedChangeVolume) {
                        this.hideChangeVolume();
                        return true;
                    }
                    break;
                case 2:
                    float deltaX = x - this.mDownX;
                    float deltaY = y - this.mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if(!this.mNeedChangeVolume && !this.mNeedChangeBrightness) {
                        /*if(absDeltaX >= 80.0F) {
                            this.cancelUpdateProgressTimer();
                            this.mNeedChangePosition = true;
                            this.mGestureDownPosition = this.mNiceVideoPlayer.getCurrentPosition();
                        } else */
                            if(absDeltaY >= 80.0F) {
                            if(this.mDownX < (float)this.getWidth() * 0.5F) {
                                this.mNeedChangeBrightness = true;
                                this.mGestureDownBrightness = NiceUtil.scanForActivity(this.mContext).getWindow().getAttributes().screenBrightness;
                            } else {
                                this.mNeedChangeVolume = true;
                                this.mGestureDownVolume = this.mNiceVideoPlayer.getVolume();
                            }
                        }
                    }

                    int newBrightnessProgress;
                    /*if(this.mNeedChangePosition) {
                        long maxVolume = this.mNiceVideoPlayer.getDuration();
                        long newVolume = (long)((float)this.mGestureDownPosition + (float)maxVolume * deltaX / (float)this.getWidth());
                        this.mNewPosition = Math.max(0L, Math.min(maxVolume, newVolume));
                        newBrightnessProgress = (int)(100.0F * (float)this.mNewPosition / (float)maxVolume);
                        this.showChangePosition(maxVolume, newBrightnessProgress);
                    }*/

                    if(this.mNeedChangeBrightness) {
                        deltaY = -deltaY;
                        float maxVolume1 = deltaY * 3.0F / (float)this.getHeight();
                        float deltaVolume = this.mGestureDownBrightness + maxVolume1;
                        deltaVolume = Math.max(0.0F, Math.min(deltaVolume, 1.0F));
                        WindowManager.LayoutParams newVolumeProgress = NiceUtil.scanForActivity(this.mContext).getWindow().getAttributes();
                        newVolumeProgress.screenBrightness = deltaVolume;
                        NiceUtil.scanForActivity(this.mContext).getWindow().setAttributes(newVolumeProgress);
                        newBrightnessProgress = (int)(100.0F * deltaVolume);
                        this.showChangeBrightness(newBrightnessProgress);
                    }

                    if(this.mNeedChangeVolume) {
                        deltaY = -deltaY;
                        int maxVolume2 = this.mNiceVideoPlayer.getMaxVolume();
                        int deltaVolume1 = (int)((float)maxVolume2 * deltaY * 3.0F / (float)this.getHeight());
                        int newVolume1 = this.mGestureDownVolume + deltaVolume1;
                        newVolume1 = Math.max(0, Math.min(maxVolume2, newVolume1));
                        this.mNiceVideoPlayer.setVolume(newVolume1);
                        int newVolumeProgress1 = (int)(100.0F * (float)newVolume1 / (float)maxVolume2);
                        this.showChangeVolume(newVolumeProgress1);
                    }
            }

            return false;
        } else {
            this.hideChangePosition();
            this.hideChangeBrightness();
            this.hideChangeVolume();
            return false;
        }
    }

    protected void updateProgress() {

        //赋值 播放时间  处理接收视频流出问题后 用户点击而已续播
        mNiceVideoPlayer.ceshi();
        //////////////////////////////////

        long position = this.mNiceVideoPlayer.getCurrentPosition();
        long duration = this.mNiceVideoPlayer.getDuration();
        int bufferPercentage = this.mNiceVideoPlayer.getBufferPercentage();
        this.mSeek.setSecondaryProgress(bufferPercentage);
        int progress = (int)(100.0F * (float)position / (float)duration);
        this.mSeek.setProgress(progress);
        this.mPosition.setText(NiceUtil.formatTime(position));
        this.mDuration.setText(NiceUtil.formatTime(duration));
        this.mTime.setText((new SimpleDateFormat("HH:mm", Locale.CHINA)).format(new Date()));
        intent.putExtra("progress", (float) (position/1000));
        mContext.sendBroadcast(intent);

    }

    protected void showChangePosition(long duration, int newPositionProgress) {
        this.mChangePositon.setVisibility(0);
        long newPosition = (long)((float)(duration * (long)newPositionProgress) / 100.0F);
        this.mChangePositionCurrent.setText(NiceUtil.formatTime(newPosition));
        this.mChangePositionProgress.setProgress(newPositionProgress);
        this.mSeek.setProgress(newPositionProgress);
        this.mPosition.setText(NiceUtil.formatTime(newPosition));
    }

    protected void hideChangePosition() {
        this.mChangePositon.setVisibility(8);
    }

    protected void showChangeVolume(int newVolumeProgress) {
        this.mChangeVolume.setVisibility(0);
        this.mChangeVolumeProgress.setProgress(newVolumeProgress);
    }

    protected void hideChangeVolume() {
        this.mChangeVolume.setVisibility(8);
    }

    protected void showChangeBrightness(int newBrightnessProgress) {
        this.mChangeBrightness.setVisibility(0);
        this.mChangeBrightnessProgress.setProgress(newBrightnessProgress);
    }

    protected void hideChangeBrightness() {
        this.mChangeBrightness.setVisibility(8);
    }

    public void showSeekBar() {
        this.mSeek.setVisibility(View.VISIBLE);
    }

    public void hideSeekBar() {
        this.mSeek.setVisibility(View.INVISIBLE);
    }

    public View getLlVideoSign() {
        return this.ll_video_sign;
    }

    public View getLlsign2() {
        return this.ll_sign2;
    }

    public View getVideoSign2() {
        return this.video_sign2;
    }

    public View getVideoSign4() {
        return this.video_sign4;
    }

    public View getVideoSignImg2() {
        return this.video_sign_img2;
    }
}
