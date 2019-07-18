package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.NotSignDialog;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Examination_instructions2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.ResponseCodeUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AudioSignActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private Context context;
    private TimerTextView mTextView;
    private TextView title_name, audio_title, pdf_pageNum, total_page, start_time, totalTime, audio_time,
            sign_t1, sign_t2, sign_t3, sign_t4, video_s3, video_s1, time_empty;
    private ImageView audio_image;
    private ImageView ivPlay;
    private LinearLayout ll_s1, video_sign_i1;
    private String tid = "";
    private String aid = "";
    public String signnum = "";
    public String is_sign = "";
    public String sign_num = "";
    public String be_sign_number = "";
    List<Map<String, Object>> slist = new ArrayList();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private String is_miss = "0";
    private int signCount = 0;
    private ProgressBar pbAudio;
    private Player player;
    private String audioUrl;
    public MediaPlayer mediaPlayer;
    private static final int START = 5;//开始计时消息标志，下面用到
    private static final int STOP = 6;//停止计时消息标志，下面用到
    private boolean isClickSign = false;
    private String totalTimes;
    private List<CountDownTimer> timerList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        JSONObject taskObject = jsonObject.getJSONObject("task_info");
                        audioUrl = jsonObject.getString("filePath");
                        judgeUrlResponseCode(audioUrl);
//                        setAudioUrl();
                        audio_title.setText(taskObject.getString("tasktitle"));
                        start_time.setText("00:00");
                        totalTimes = taskObject.getString("vtime");
                        totalTime.setText(FileSizeUtil.formatLongToTimeStr(taskObject.getString("vtime")));
                        audio_time.setText(taskObject.getString("ppt_time"));
                        video_s1.setText(taskObject.getString("sign_num"));
                        video_s3.setText(jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0");
                        SharedPreferencesTools.saveSign(context, jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0");
                        SharedPreferencesTools.saveSign_in_num(context, "0");
                        sign_num = taskObject.getString("sign_num");
                        is_sign = jsonObject.has("sign") ? jsonObject.getString("sign") : "2";
                        be_sign_number = jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0";
                        is_miss = taskObject.getString("is_miss");
                        String type = taskObject.getString("task_ppt_type");
//                        Log.e("type", type);
                        if (type.equals("3")) {//签到+测试
                            video_sign_i1.setVisibility(View.VISIBLE);
                            if (video_s1.getText().toString().equals(video_s3.getText().toString())) {
                                video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                                video_sign_i1.setClickable(true);
                                video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Examination_instructions2.class);
                                        intent.putExtra("aid", aid);
                                        intent.putExtra("tid", tid);
                                        intent.putExtra("type", "ppt");
                                        intent.putExtra("pptid", aid);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                                video_sign_i1.setClickable(false);
                            }
                        } else if (type.equals("2")) {//不显示签到
                            ll_s1.setVisibility(View.GONE);//签到
                            video_sign_i1.setVisibility(View.VISIBLE);//测试
                            video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                            video_sign_i1.setClickable(true);
                            video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, Examination_instructions2.class);
                                    intent.putExtra("aid", aid);
                                    intent.putExtra("tid", tid);
                                    intent.putExtra("type", "ppt");
                                    intent.putExtra("pptid", aid);
                                    startActivity(intent);
                                }
                            });

                        } else if (type.equals("1")) {//不显示测试
                            video_sign_i1.setVisibility(View.GONE);
                        }

                        if (is_sign.equals("0")) {
                            try {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                Iterator<String> keys = dataObject.keys();
                                while (keys.hasNext()) {
                                    Map<String, Object> map = new HashMap<>();
                                    String str = keys.next();
                                    map.put(str, dataObject.getString(str));
                                    slist.add(map);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //list获取完成后加载数据
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                    break;
                case 2:
                    //子线程请求数据是耗时操作
                    //请求完毕后执行下面的方法
                    String expired1 = getIntent().getStringExtra("expired");
                    String expired = expired1.equals("1") ? expired1 : "2";
                    initData();
                    //判断是不是从已结束列表跳转
                    if (expired.equals("1")) {//是：只能看ppt没有其他功能
                        //time_empty.setVisibility(View.VISIBLE);
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                        ll_s1.setClickable(false);
                        video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                        video_sign_i1.setClickable(false);
                    } else {
                        if (is_sign.equals("0")) {
                            //startTimer();
                            //time_empty.setVisibility(View.GONE);
                        } else {
                            //time_empty.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String type = jsonObject.getString("type" + "");
                        if (jsonObject.getInt("status") == 1) {
//                            SharedPreferencesTools.saveSign_in_num(context,(Integer.parseInt(SharedPreferencesTools.getSign_in_num(context))-1)+"");
                            SharedPreferencesTools.saveSign_in_num(context, "0");
                            if (type.equals("1")) {//显示自测
                                int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                                if (num > Integer.parseInt(sign_num)) {
                                    video_s3.setText(sign_num);
                                } else {
                                    video_s3.setText(num + "");
                                }
                                SharedPreferencesTools.saveSign(context, num + "");
                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_s1.setClickable(false);
                                video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                                video_sign_i1.setClickable(true);
                                video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Examination_instructions2.class);
                                        intent.putExtra("aid", aid);
                                        intent.putExtra("tid", tid);
                                        intent.putExtra("type", "ppt");
                                        intent.putExtra("pptid", aid);
                                        startActivity(intent);
                                    }
                                });
                            } else if (type.equals("0")) {//不显示自测
                                int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                                video_s3.setText(num + "");
                                SharedPreferencesTools.saveSign(context, num + "");
                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_s1.setClickable(false);
                                video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                                video_sign_i1.setClickable(false);
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                        video_s3.setText(num + "");
                        SharedPreferencesTools.saveSign(context, num + "");
                        SharedPreferencesTools.saveSign_in_num(context, "0");
                        Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                        ll_s1.setClickable(false);
                        video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                        video_sign_i1.setClickable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 10:
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "加载音频资源出错", Toast.LENGTH_SHORT).show();
                    ivPlay.setVisibility(View.GONE);
                    ll_s1.setVisibility(View.GONE);
                    video_sign_i1.setVisibility(View.GONE);
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                case START:
                    if (is_sign.equals("0") && mediaPlayer.isPlaying()) {
                        findSignSpot();
                    }
                    if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
                        start_time.setText(formatTime(mediaPlayer.getCurrentPosition() / 1000));
                        pbAudio.setMax(mediaPlayer.getDuration());
                        pbAudio.setProgress(mediaPlayer.getCurrentPosition());
                        handler.sendEmptyMessageDelayed(START, 1000);
                    }
                    break;
                case STOP:
                    handler.removeMessages(START);
                    break;
                default:
                    break;
            }
        }
    };

    //private JSONArray jsonArraySignTime = new JSONArray();
    private JSONObject Json = new JSONObject();
    private boolean pause = true;
    private static final String TAG = "AudioSignActivity";
    private CountDownTimer timer1;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_audio_sign);

        context = this;
        if (mediaPlayer == null) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
//                        Log.d(TAG, "OnError - Error code: " + what + " Extra code: " + extra);
                        try {
                            if (mp != null && mp.isPlaying()) {
                                mp.stop();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(STOP);
                        ivPlay.setImageResource(R.mipmap.float_07);
                        pause = true;
                        return true;
                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        ivPlay.setImageResource(R.mipmap.float_16);
                        pause = false;
                        handler.sendEmptyMessage(START);
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.pause();
                        handler.sendEmptyMessage(STOP);
                        ivPlay.setImageResource(R.mipmap.float_07);
                        pause = true;
                        NotSignShowDialog();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        initView();
        setValue();

        //停止播放音频
        MainActivity.stopFloatingView(context);
    }

    private void judgeUrlResponseCode(final String pdf_url) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int responseCode = ResponseCodeUtils.getResponseCode(pdf_url);
                        if (responseCode != 200) {
                            Message message = new Message();
                            message.what = 10;
                            message.obj = responseCode;
                            handler.sendMessage(message);
                        }else {
                            setAudioUrl();
                        }
                    }
                }).start();
    }

    private void setAudioUrl() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {//确保在prepare()之前调用了stop()
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValue() {
        tid = getIntent().getStringExtra("tid");
        aid = getIntent().getStringExtra("aid");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getAudioInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("aid", aid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看音频数据", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void initData() {
        title_name.setText("音频详情");
        audio_image.setImageResource(R.mipmap.ic_learning_task_audio);
        try {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher) // 在ImageView加载过程中显示图片
                    .showImageForEmptyUri(R.drawable.ic_launcher) // image连接地址为空时
                    .showImageOnFail(R.drawable.ic_launcher) // image加载失败
                    .cacheInMemory(true) // 加载图片时会在内存中加载缓存
                    .cacheOnDisc(true) // 加载图片时会在磁盘中加载
                    .build();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "暂无数据，请稍后尝试", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        ivPlay = (ImageView) findViewById(R.id.id_learning_task_audio_play);
        pbAudio = (ProgressBar) findViewById(R.id.id_pb_audio);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        audio_title = (TextView) findViewById(R.id.id_tv_learning_task_audio_title);
        audio_image = (ImageView) findViewById(R.id.id_iv_learning_task_audio);
        start_time = (TextView) findViewById(R.id.id_tv_audio_time_start);
        totalTime = (TextView) findViewById(R.id.id_tv_audio_totalTime);
        audio_time = (TextView) findViewById(R.id.audio_time);

        ll_s1 = (LinearLayout) findViewById(R.id.ll_s1);
        video_sign_i1 = (LinearLayout) findViewById(R.id.video_sign_i1);
        sign_t1 = (TextView) findViewById(R.id.sign_t1);
        sign_t2 = (TextView) findViewById(R.id.sign_t2);
        sign_t3 = (TextView) findViewById(R.id.sign_t3);
        sign_t4 = (TextView) findViewById(R.id.sign_t4);
        video_s1 = (TextView) findViewById(R.id.video_s1);
        video_s3 = (TextView) findViewById(R.id.video_s3);

        ivPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        //释放mediaplayer
        handler.sendEmptyMessage(STOP);
        mediaPlayer.pause();
        ivPlay.setImageResource(R.mipmap.float_07);
        pause = true;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (notSignDialog != null) {
            notSignDialog.dismiss();
        }
        for (int i = 0; i < timerList.size(); i++) {
            if (timerList.get(i) != null) {
                timerList.get(i).cancel();
            }
        }
        handler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void back(View view) {
        finish();
    }

    private void getTaskPptInfo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    signCount++;

                    JSONArray jsonArraySignTime = new JSONArray();
                    Json.put(signCount + "", System.currentTimeMillis());//JSONObject对象中添加键值对
                    jsonArraySignTime.put(Json);//将JSONObject对象添加到Json数组中
                    if (is_miss.equals("0")) {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", aid);
                        obj.put("signnum", signnum);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);

                        Message message = new Message();
                        message.what = 3;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else if (signCount >= Integer.parseInt(sign_num)) {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", aid);
                        obj.put("signnum", jsonArraySignTime);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);

                        Message message = new Message();
                        message.what = 3;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 4;
                        message.obj = 0;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void startTimer() {
        try {
            String str1 = audio_time.getText().toString();
            long time = Long.parseLong(str1);

            long diff = 60 * 1000 * time;
//            Log.e("diff:", diff + "");
            /*mTextView = (TimerTextView) findViewById(R.id.timer);
            //设置时间
            mTextView.setTimes(diff);*/

            /**
             * 开始倒计时
             */
            //if (!mTextView.isRun()) {
            //mTextView.start();
            long tf = time * 1000 * 60;
//            Log.e("时间11", tf + "");
            CountDownTimer t = new CountDownTimer(tf, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                    setColorGray();
                    ll_s1.setClickable(false);
                }
            }.start();
            timerList.add(t);
            //签到点倒计时
//            Log.e("slist:", slist.toString());
            for (Map<String, Object> map : slist) {
                for (String str : map.keySet()) {
                    if (map.containsKey(str)) {
                        double d = Double.parseDouble(map.get(str).toString());
                        long l = (long) (1000 * d);
                        final String signnum_text = str;
                        CountDownTimer timer = new CountDownTimer(l, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                Intent intent = new Intent();
                                intent.setAction("sign");
                                intent.putExtra("mes", "1");
                                intent.putExtra("tid", tid);
                                intent.putExtra("pptid", aid);
                                intent.putExtra("signnum", signnum_text);
                                intent.putExtra("sign_num", sign_num);
                                sendBroadcast(intent);

                                //到签到点，判断是否点击签到
                                ll_s1.setClickable(true);
                                setColorWhite();
                                ll_s1.setBackgroundResource(R.mipmap.learning_task04);

                                ll_s1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        ll_s1.setClickable(false);
                                        getTaskPptInfo();
                                        signnum = signnum_text;
                                    }
                                });

                                SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) + 1) + "");
                                if (timer1 != null) {
                                    timer1.cancel();
                                }
                                timer1 = new CountDownTimer(60 * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
//                                        Log.e("签到时间倒计时", millisUntilFinished + "");
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) > 0) {
                                            SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) - 1) + "");
                                        } else {
                                            SharedPreferencesTools.saveSign_in_num(context, "0");
                                        }
                                        if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) < 1) {
                                            ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                            setColorGray();
                                            ll_s1.setClickable(false);
                                        }
                                    }
                                }.start();
                                timerList.add(timer1);
                            }
                        }.start();
                        timerList.add(timer);
                    }
                }

            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorWhite() {
        sign_t1.setTextColor(Color.WHITE);
        sign_t2.setTextColor(Color.WHITE);
        sign_t3.setTextColor(Color.WHITE);
        sign_t4.setTextColor(Color.WHITE);
        video_s1.setTextColor(Color.WHITE);
        video_s3.setTextColor(Color.WHITE);
    }

    public void setColorGray() {
        sign_t1.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t2.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t3.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t4.setTextColor(Color.parseColor("#6c6c6c"));
        video_s1.setTextColor(Color.parseColor("#6c6c6c"));
        video_s3.setTextColor(Color.parseColor("#6c6c6c"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_learning_task_audio_play:
                if (pause) {
                    /*if (!prepared){
                        prepared=true;
                    }*/
                    handler.sendEmptyMessage(START);
                    mediaPlayer.start();
                    ivPlay.setImageResource(R.mipmap.float_16);
                    pause = false;
                } else {
                    handler.sendEmptyMessage(STOP);
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.mipmap.float_07);
                    pause = true;
                }
                break;
        }
    }

    private void findSignSpot() {
        //判断当前时间到签到点没有
        for (Map<String, Object> map : slist) {
            for (String str : map.keySet()) {
                if (map.containsKey(str)) {
                    float sign_time = Float.parseFloat(map.get(str).toString());
                    final String signnum_text = str;
                    if (formatTime(mediaPlayer.getCurrentPosition() / 1000).equals(formatTime(sign_time))) {
                        //到签到点，判断是否点击签到
                        ll_s1.setClickable(true);
                        setColorWhite();
                        ll_s1.setBackgroundResource(R.mipmap.learning_task04);

                        ll_s1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isClickSign = true;
                                ll_s1.setClickable(false);
                                getTaskPptInfo();
                                signnum = signnum_text;
                            }
                        });
                        /*if (timer!=null){
                            timer.cancel();
                            NotSignShowDialog();
                        }*/
                        SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) + 1) + "");
                        timer = new CountDownTimer(60 * 1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
//                                Log.e("签到时间倒计时", millisUntilFinished + "");
                            }

                            @Override
                            public void onFinish() {
                                NotSignShowDialog();
                                if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) > 0) {
                                    SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) - 1) + "");
                                } else {
                                    SharedPreferencesTools.saveSign_in_num(context, "0");
                                }
                                if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) < 1) {
                                    ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                    setColorGray();
                                    ll_s1.setClickable(false);
                                }
                            }
                        }.start();
                        timerList.add(timer);
                    }
                }
            }
        }
    }

    private NotSignDialog notSignDialog;

    private void NotSignShowDialog() {
        if (!isClickSign && is_miss.equals("1")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (notSignDialog == null) {
                            notSignDialog = new NotSignDialog(AudioSignActivity.this);
                        }
                        notSignDialog.show();
                        notSignDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notSignDialog.dismiss();
                            }
                        });
                        notSignDialog.setSureButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    notSignDialog.dismiss();
                                    Intent intent = new Intent(AudioSignActivity.this, AudioSignActivity.class);
                                    intent.putExtra("tid", getIntent().getStringExtra("tid"));
                                    intent.putExtra("aid", aid);
                                    intent.putExtra("expired", getIntent().getStringExtra("expired"));
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            isClickSign = false;
//            Log.e("AudioSignActivity", "onFinish: 未签到");
        } else {
            isClickSign = false;
        }
    }

    private String formatTime(float time) {
        Formatter formatter = new Formatter();
        // Log.i("time", "time" + time);
        if (time < 60) {
            String str_time = time + "";
            if (time < 10) {
                return "00:0" + str_time.substring(0, str_time.indexOf("."));  //如果进度为10s以为   则time为6.0，7.0截取。之前的数字，拼接“00：06，00：07”
            } else {
                return "00:" + str_time.substring(0, str_time.indexOf("."));  //如果进度为大于10，小于60，以为   则time为16.0，17.0截取。之前的数字，拼接“00：16，00：17”
            }
        }
        int seconds = (int) (time % 60);
        int minutes = (int) ((time % 3600) / 60);
        int hours = (int) (time / 3600);

        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}