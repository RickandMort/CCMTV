package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.HeadLargeImgActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.VideoRecordingActivity;
import com.linlic.ccmtv.yx.arcface.activity.PreviewActivity;
import com.linlic.ccmtv.yx.arcface.common.Constants;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CameraUtils;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SDCard;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.SystemUtil;
import com.linlic.ccmtv.yx.widget.PermissionTipsDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 考试须知
 * Created by Administrator on 2017/9/6.
 */
public class Examination_instructions extends BaseActivity {

    private Context context;
    //用户统计
    private String pid;
    private LinearLayout is_cheat;
    private MyListView examination_instructions_list, examination_instructions_list2;
    private TextView number_of_examinations2, examination_instructions_name, examination_instructions, examination_instructions_time, length_of_examination, total_score_of_examination, lost_examination;
    private TextView examination_instructions_buttpm,dialog_2_text;
    private ImageView examination_instructions_buttpm_img;
    private LinearLayout hearderViewLayout, hearderViewLayout2,face_layout,dialog_1,dialog_2;
    BaseListAdapter baseListAdapter, baseListAdapter2;
    private String my_exams_id = "";
    int reset = 0;
    private String paper_type = "0";
    private List<Map<String, Object>> data = new ArrayList<>();
    private List<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
    private String camera_exception_enter = "0";
    //倒计时
    Timer timer = null;
    private int recLen = 11;
    private String is_cheat_bool = "0";
    private boolean is_Face = true;
    private String face_code = "2";
    private String is_camera_errer= "0";
    PermissionTipsDialog cameraDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONObject jsonObject = jsonObjects.getJSONObject("data");
                            /*解析考试其他信息start*/
                            pid = jsonObject.getString("pid");
                            /*考试名称*/
                            examination_instructions_name.setText(jsonObject.getString("paper_name"));
                            /*考试介绍*/
                            examination_instructions.setText(jsonObject.getString("paper_descrip"));
                            /*考试时间*/
                            examination_instructions_time.setText(jsonObject.getString("paper_time"));
                            /*考试时长*/
                            length_of_examination.setText(jsonObject.getString("paper_mins"));
                            /*考试类别*/
                            paper_type = jsonObject.has("is_exercise")?jsonObject.getString("is_exercise"):"0";
                            /*是否开启人脸识别 1开启 0 未开启*/
                            is_Face = ( jsonObjects.has("face_recognition")?jsonObjects.getString("face_recognition"):"0").equals("1")?true:false;
                            if(is_Face){
                                face_layout.setVisibility(View.VISIBLE);
                            }else{
                                face_layout.setVisibility(View.GONE);
                            }
                            /*设置考试按钮文字*/
                            examination_instructions_buttpm.setText(jsonObjects.has("start_msg")?jsonObjects.getString("start_msg"):"开始考试");
                            /*头像校验  1:人像检测成功    2:未检测到头像，请上传    3.头像模糊或不完整，请重新上传    4.上传图片非人像图，请重新上传*/
                            face_code =  jsonObjects.has("face_code")?jsonObjects.getString("face_code"):"2";
                           /*是否作弊*/
                            is_cheat_bool = jsonObjects.has("is_cheat")?jsonObjects.getString("is_cheat"):"0";
                            if(is_cheat_bool.equals("1")){
                                is_cheat.setVisibility(View.VISIBLE);
                            }else{
                                is_cheat.setVisibility(View.GONE);
                            }
                            /*考试总分*/
                            total_score_of_examination.setText(jsonObject.getString("total_score"));
                            /*相机异常时是否可以进去考试*/
                            camera_exception_enter = jsonObject.has("camera_exception_enter")?jsonObject.getString("camera_exception_enter"):"0";
                             /*考试剩余次数*/
                            number_of_examinations2.setText(Html.fromHtml(jsonObject.getString("remain_num")));
                            if (jsonObject.getString("less_num").equals(jsonObject.getString("paper_num").substring(0, jsonObject.getString("paper_num").length() - 1))) {
                                lost_examination.setVisibility(View.GONE);
                            } else {
                                lost_examination.setVisibility(View.VISIBLE);
                            }
                            /*是否还能参加考试*/

                            if (jsonObject.getInt("code") > 0) {
                                examination_instructions_buttpm_img.setImageResource(R.mipmap.examination_instructions_buttpm1);
                                examination_instructions_buttpm.setClickable(true);
                                examination_instructions_buttpm_img.setClickable(true);
                            } else {
                                examination_instructions_buttpm_img.setImageResource(R.mipmap.examination_instructions_buttpm2);
                                examination_instructions_buttpm.setClickable(false);
                                examination_instructions_buttpm_img.setClickable(false);
                            }
                            /*解析考试其他信息end*/
                            /*解析题型列表start*/
                            JSONArray dataArray = jsonObject.getJSONArray("question_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("type_name", customJson.getString("type_name"));
                                map.put("type_detail", customJson.getString("type_detail"));
                                data.add(map);
                            }
                            JSONArray dataArray2 = jsonObject.getJSONArray("exam_history");
                            for (int i = 0; i < dataArray2.length(); i++) {
                                JSONObject customJson = dataArray2.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("length_of_examination1", customJson.getString("eid"));
                                map.put("length_of_examination2", customJson.getString("start_time"));
                                map.put("length_of_examination3", customJson.getString("exam_score"));
                                map.put("length_of_examination4", customJson.getString("show_type"));
                                map.put("is_cheat", customJson.has("is_cheat")?customJson.getString("is_cheat"):"0");
                                map.put("score_show", customJson.has("score_show")?customJson.getString("score_show"):"1");
                                map.put("length_of_examination5", (i + 1) + "");
                                data2.add(map);
                            }
                             /*解析题型列表end*/
                        } else {
                            Toast.makeText(Examination_instructions.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View views = inflater.inflate(R.layout.dialog_item3, null);
                    ListView examination_instructions_list2 = (ListView) views.findViewById(R.id.examination_instructions_list2);
                    hearderViewLayout2 = (LinearLayout) View.inflate(context, is_Face?R.layout.examination_instructions_list_item3:R.layout.examination_instructions_list_item7, null);
                    examination_instructions_list2.addHeaderView(hearderViewLayout2);

                    baseListAdapter2 = new BaseListAdapter(examination_instructions_list2, data2,is_Face? R.layout.examination_instructions_list_item4:R.layout.examination_instructions_list_item6) {

                        @Override
                        public void refresh(Collection datas) {
                            super.refresh(datas);
                        }

                        @Override
                        public void convert(ListHolder helper, Object item, boolean isScrolling) {
                            super.convert(helper, item, isScrolling);

                            helper.setText(R.id.length_of_examination4, ((Map) item).get("length_of_examination5") + "");
                            helper.setText(R.id.length_of_examination5, ((Map) item).get("length_of_examination2") + "");
                            helper.setText(R.id.length_of_examination6, ((Map) item).get("length_of_examination3") + "");

                            if(is_Face){
                                if (((Map) item).get("is_cheat").toString().equals("0")){
                                    helper.setText(R.id.length_of_examination8, "否");
                                    helper.setTextColor2(R.id.length_of_examination8, Color.parseColor("#333333"));

                                }else{
                                    helper.setText(R.id.length_of_examination8, "是");
                                    helper.setTextColor2(R.id.length_of_examination8, Color.parseColor("#FB3D3D"));
                                }
                            }

                            if (((Map) item).get("score_show").toString().equals("0")){
                                helper.setText(R.id.length_of_examination6,  "待公布");
                            }else{
                                helper.setText(R.id.length_of_examination6, ((Map) item).get("length_of_examination3") + "");
                            }
                            if (Integer.parseInt(((Map) item).get("length_of_examination4").toString()) > 0) {
                                helper.setTextColor(R.id.length_of_examination7, R.color.exams_list_item_text_color6);
                                helper.setTag(R.id.length_of_examination7, ((Map) item).get("length_of_examination1") + "");
                                helper.setClickable(R.id.length_of_examination7, true);
                            } else {
                                helper.setTextColor(R.id.length_of_examination7, R.color.exams_list_item_text_color5);
                                helper.setTag(R.id.length_of_examination7, ((Map) item).get("length_of_examination1") + "");
                                helper.setClickable(R.id.length_of_examination7, false);
                            }
                        }
                    };
                    examination_instructions_list2.setAdapter(baseListAdapter2);
                    TextView btn_sure = (TextView) views.findViewById(R.id.i_understand);
                    //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
                    final Dialog dialog = builder.create();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = WindowManager.LayoutParams.FILL_PARENT;
                    lp.height = WindowManager.LayoutParams.FILL_PARENT;
                    dialog.getWindow().setAttributes(lp);
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.getWindow().setContentView(views);//自定义布局应该在这里添加，要在dialog.show()的后面
                    //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                    btn_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });
                    break;
                case 3:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        JSONObject jsonObject = jsonObjects.getJSONObject("data");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            Formal_examination.Whether_or_not_run_backstage = false;
                            Intent intent = new Intent(Examination_instructions.this, Formal_examination.class);
//                            Intent intent = new Intent(Examination_instructions.this, PreviewActivity.class);
                            intent.putExtra("my_exams_id", my_exams_id);
                            intent.putExtra("is_camera_errer", is_camera_errer);
                            intent.putExtra("is_Face", is_Face);
                            intent.putExtra("pid", pid);
                            intent.putExtra("Unlock", jsonObject.getString("remain"));
                            if (jsonObject.getInt("status") == 101) {
                                intent.putExtra("Lock", "0");
                                intent.putExtra("is_Lock", "1");
                            } else if (jsonObject.getInt("status") == 103) {
                                intent.putExtra("Lock", "0");
                                intent.putExtra("is_Lock", "0");
                            } else {
                                intent.putExtra("Lock", "1");
                                intent.putExtra("is_Lock", "1");
                            }
                            startActivity(intent);
                            examination_instructions_buttpm.setClickable(true);
                            examination_instructions_buttpm_img.setClickable(true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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
    protected void onResume() {
        if (reset > 0) {
            data.removeAll(data);
            data2.removeAll(data2);
            setValue2();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + pid;
        reset++;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.examination_instructions3);
        context = this;
        findId();
        initdata();
        setValue2();
    }

    public void findId() {
        examination_instructions_list = (MyListView) findViewById(R.id.examination_instructions_list);
        examination_instructions_name = (TextView) findViewById(R.id.examination_instructions_name);
        examination_instructions = (TextView) findViewById(R.id.examination_instructions);
        examination_instructions_time = (TextView) findViewById(R.id.examination_instructions_time);
        length_of_examination = (TextView) findViewById(R.id.length_of_examination);
        total_score_of_examination = (TextView) findViewById(R.id.total_score_of_examination);
        examination_instructions_buttpm = (TextView) findViewById(R.id.examination_instructions_buttpm);
        examination_instructions_buttpm_img = (ImageView) findViewById(R.id.examination_instructions_buttpm_img);
        number_of_examinations2 = (TextView) findViewById(R.id.number_of_examinations2);
        lost_examination = (TextView) findViewById(R.id.lost_examination);
        is_cheat = (LinearLayout) findViewById(R.id.is_cheat);
        face_layout = (LinearLayout) findViewById(R.id.face_layout);
        dialog_1 = (LinearLayout) findViewById(R.id.dialog_1);
        dialog_2 = (LinearLayout) findViewById(R.id.dialog_2);
        dialog_2_text = (TextView) findViewById(R.id.dialog_2_text);
    }

    public void initdata() {
        dialog_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_2.setVisibility(View.GONE);
                examination_instructions_buttpm_img.setClickable(true);
                LogUtil.e("dialog_2_text",dialog_2_text.getText().toString()+"   "+dialog_2_text.getText().toString().indexOf("上传"));
                if(dialog_2_text.getText().toString().lastIndexOf("上传")>0){
                   Intent intent = new Intent(context, HeadLargeImgActivity.class);
                    intent.putExtra("images", SharedPreferencesTools.getStricon(context));
                    startActivity(intent);
                }
            }
        });
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        baseListAdapter = new BaseListAdapter(examination_instructions_list, data, R.layout.examination_instructions_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id._item_text1, ((Map) item).get("type_name").toString(), "html");
                helper.setText(R.id._item_text2, ((Map) item).get("type_detail").toString(), "html");

            }
        };
        examination_instructions_list.setAdapter(baseListAdapter);
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getExamDetails);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());
//                    LogUtil.e("看看视频数据2", result);

                    MyProgressBarDialogTools.hide();
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

    /*开始考试*/
    public void setValue3() {
        examination_instructions_buttpm.setClickable(false);
        examination_instructions_buttpm_img.setClickable(false);
        examination_instructions_buttpm_img.setImageResource(R.mipmap.examination_instructions_buttpm2);
        int min=3;
        int max=10;
        Random random = new Random();
        recLen = random.nextInt(max)%(max-min+1) + min;
        timer = new Timer();
        timer.schedule(  new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recLen--;

                        if (recLen < 1 ){
                            timer.cancel();
                            timer = null;
                            if (paper_type.equals("1")) {
                                Intent intent = new Intent(Examination_instructions.this, Mock_exam.class);
                                intent.putExtra("my_exams_id", my_exams_id);
                                intent.putExtra("pid", pid);
                                startActivity(intent);

                            } else {
                                MyProgressBarDialogTools.show(context);
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject();
                                            obj.put("act", URLConfig.checkCheat);
                                            obj.put("uid", SharedPreferencesTools.getUid(context));
                                            obj.put("pid", my_exams_id);
                                            obj.put("device_params", "系统版本号:"+ SystemUtil.getSystemVersion()+",手机型号:"+SystemUtil.getSystemModel()+",手机厂商:"+SystemUtil.getDeviceBrand());
                                            String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());
//                    Log.e("防作弊数据", result);

                                            MyProgressBarDialogTools.hide();
                                            Message message = new Message();
                                            message.what = 3;
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
                         /*   examination_instructions_buttpm.setText("开始考试");
                            examination_instructions_buttpm_img.setImageResource(R.mipmap.examination_instructions_buttpm1);
                            examination_instructions_buttpm.setClickable(true);
                            examination_instructions_buttpm_img.setClickable(true);*/

                        }

                        examination_instructions_buttpm.setText("正在进入 " + recLen+" 秒");
                    }
                });
            }
        }, 0, 1000);       // timeTask

    }

    /*进入考试*/
    public void enter_the_examination(View view) {
//        int min=1;
//        int max=10;
//        Random random = new Random();
//        recLen = random.nextInt(max)%(max-min+1) + min;
//        Toast.makeText(context, "随机数："+recLen, Toast.LENGTH_SHORT).show();
        view.setClickable(false);
        //1.检测该考试是否需要进行人脸识别
        if(is_Face){
            dialog_1.setVisibility(View.VISIBLE);
            //检查基准图
           switch (face_code){
               /*头像校验  1:人像检测成功    2:未检测到头像，请上传    3.头像模糊或不完整，请重新上传    4.上传图片非人像图，请重新上传*/
               case "1":
                   AndPermission.with(context)
                           .runtime()
                           .permission(Permission.CAMERA)
                           .onGranted(new Action<List<String>>() {
                               @Override
                               public void onAction(List<String> permissions) {
                                LogUtil.e("剩余空间",  SDCard.getAvailableInternalMemorySize(context));
                                LogUtil.e("剩余空间",  SDCard.readSystem()+"");

                                   activeEngine();
                               }
                           })
                           .onDenied(new Action<List<String>>() {
                               @Override
                               public void onAction(List<String> permissions) {
                                   // 权限拒绝了
                                   if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                                       // 用Dialog展示没有某权限，询问用户是否去设置中授权。
                                       cameraDialog = new PermissionTipsDialog.Builder()
                                               .init(context)
                                               .addTitle("权限申请")
                                               .addContent("因相机权限未开启，该功能尚无法使用，请前往手机设置-应用-权限中开启" + "相机权限" + "")
                                               .addConfirm("设置")
                                               .builder();
                                       dialog_1.setVisibility(View.GONE);
                                       cameraDialog.setClickListener(new PermissionTipsDialog.DialogClickListener() {
                                           @Override
                                           public void onClick(int click) {
                                               if (click == PermissionTipsDialog.CLICK_CONFIRM) {
                                                   AndPermission.with(context)
                                                           .runtime()
                                                           .setting()
                                                           .start(101);// 这里其实不需要回调，随便写个requestCode 即可
                                               }
                                               cameraDialog.dismiss();
                                           }
                                       });
                                       cameraDialog.show();
                                   }
                               }
                           }).start();

                   break;
               case "2":
                   dialog_1.setVisibility(View.GONE);
                   dialog_2.setVisibility(View.VISIBLE);
                   dialog_2_text.setText(Html.fromHtml("未检测到头像，请<font color='#4C77EE'><u>上传</u></font>"));
                   break;
               case "3":
                   dialog_2.setVisibility(View.VISIBLE);
                   dialog_2_text.setText(Html.fromHtml("头像模糊或不完整，请<font color='#4C77EE'><u>重新上传</u></font>"));
                   dialog_1.setVisibility(View.GONE);
                   break;
               case "4":
                   dialog_2.setVisibility(View.VISIBLE);
                   dialog_2_text.setText(Html.fromHtml("上传图片非人像图，请<font color='#4C77EE'><u>重新上传</u></font>"));
                   dialog_1.setVisibility(View.GONE);
                   break;
           }

        }else{
            //随机进入倒计时
            setValue3();
        }




    }

    public void examination(View view) {
        Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);
    }

    /*进入考试*/
    public void enter_the_examination2(View view) {
        view.setClickable(false);
        Intent intent = new Intent(Examination_instructions.this, Check_the_answer_sheet.class);
        intent.putExtra("pid", pid);
        intent.putExtra("my_exams_id", my_exams_id);
        intent.putExtra("my_exams_eid", view.getTag().toString());
        if (paper_type.equals("1")) {
            intent.putExtra("is_mock_exam", "1");
        }else {
            intent.putExtra("is_mock_exam", "0");
        }
        startActivity(intent);
        view.setClickable(true);
    }

    @Override
    public void finish() {
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        super.finish();

    }





    private static final String TAG = "ExaminationActivity";
    private Toast toast = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private FaceEngine faceEngine = new FaceEngine();
    /**
     * 激活引擎
     *
     */
    public void activeEngine( ) {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int activeCode = faceEngine.activeOnline(context, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
//                            showToast(getString(R.string.active_success));


                                // 2.检测手机是否支持人脸识别所需要的环境（检测前置摄像头是否存在 检测前置摄像头是否正常 检测存储空间至少预留50m）
                                List<Integer> cameraList = CameraUtils.getFontCamera();
                                if(cameraList.size()>0){
                                    // 2.1检测前置摄像头存在
                                    if(CameraUtils.isCameraCanUse(cameraList.get(0))){
                                        //2.2检测前置摄像头可用
                                       String str =  SDCard.getAvailableInternalMemorySize(context);
                                        LogUtil.e("内存空间",str.lastIndexOf("GB")+"");
                                        LogUtil.e("内存空间",str.lastIndexOf("TB")+"");
                                        LogUtil.e("内存空间",str.lastIndexOf("M")+"");
                                        LogUtil.e("内存空间","468 MB".substring(0,"468 MB".indexOf(" MB")));
                                        LogUtil.e("内存空间",Integer.parseInt("468 MB".substring(0,"468 MB".indexOf(" MB")))+"");
                                        if(str.lastIndexOf("吉字节")>0||str.lastIndexOf("太字节")>0 ||str.lastIndexOf("GB")>0||str.lastIndexOf("TB")>0 ||(str.lastIndexOf(" M")>0 && Integer.parseInt(str.substring(0,str.indexOf(" M"))) >49)||(str.lastIndexOf("兆字节")>0 && Integer.parseInt(str.substring(0,str.indexOf("兆字节"))) >49)){
                                            //2.3检测手机内部存储空间大于50M
                                            //随机进入倒计时
                                            dialog_1.setVisibility(View.GONE);
                                            setValue3();
                                        }else{
                                            dialog_1.setVisibility(View.GONE);
                                            dialog_2.setVisibility(View.VISIBLE);
                                            dialog_2_text.setText("手机内部存储空间小于50M，请更换设备考试。");
                                            //2.3检测手机内部存储空间小于50M
//                                            toastLong("手机内部存储空间小于50M，请更换设备考试。");
                                        }
                                    }else{
                                        //判断人脸识别环境不支持  是否允许进入考试
                                        if(camera_exception_enter.equals("1")){
                                            //随机进入倒计时
                                            is_camera_errer = "1";
                                            dialog_1.setVisibility(View.GONE);
                                            setValue3();
                                        }else {
                                            dialog_1.setVisibility(View.GONE);
                                            dialog_2.setVisibility(View.VISIBLE);
                                            dialog_2_text.setText( "前置摄像头无法使用，请检查是否开启相机权限或更换设备考试。");
                                            //2.2检测前置摄像头不可用
//                                            toastLong("前置摄像头不可使用，用户手机环境不支持该堂考试");
                                        }
                                    }
                                }else{
                                    //判断人脸识别环境不支持  是否允许进入考试
                                    if(camera_exception_enter.equals("1")){
                                        is_camera_errer = "1";
                                        //随机进入倒计时
                                        dialog_1.setVisibility(View.GONE);
                                        setValue3();
                                    }else{
                                        dialog_1.setVisibility(View.GONE);
                                        dialog_2.setVisibility(View.VISIBLE);
                                        dialog_2_text.setText( "该设备无前置摄像头，请更换设备考试。");
                                        //2.1无前置摄像头，用户手机环境不支持该堂考试
//                                        toastLong("无前置摄像头，用户手机环境不支持该堂考试");
                                    }

                                }

                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
//                            showToast(getString(R.string.already_activated));
                            //1.检测该考试是否需要进行人脸识别

                                // 2.检测手机是否支持人脸识别所需要的环境（检测前置摄像头是否存在 检测前置摄像头是否正常 检测存储空间至少预留50m）
                                List<Integer> cameraList = CameraUtils.getFontCamera();
                                if(cameraList.size()>0){
                                    // 2.1检测前置摄像头存在
                                    if(CameraUtils.isCameraCanUse(cameraList.get(0))){
                                        //2.2检测前置摄像头可用
                                        String str =  SDCard.getAvailableInternalMemorySize(context);
                                        LogUtil.e("内存空间",str.lastIndexOf("GB")+"");
                                        LogUtil.e("内存空间",str.lastIndexOf("TB")+"");
                                        LogUtil.e("内存空间",str.lastIndexOf("M")+"");
                                        LogUtil.e("内存空间","468 MB".substring(0,"468 MB".indexOf(" MB")));
                                        LogUtil.e("内存空间",Integer.parseInt("468 MB".substring(0,"468 MB".indexOf(" MB")))+"");
                                        if(str.lastIndexOf("吉字节")>0||str.lastIndexOf("太字节")>0 ||str.lastIndexOf("GB")>0||str.lastIndexOf("TB")>0 ||(str.lastIndexOf(" M")>0 && Integer.parseInt(str.substring(0,str.indexOf(" M"))) >49)||(str.lastIndexOf("兆字节")>0 && Integer.parseInt(str.substring(0,str.indexOf("兆字节"))) >49)){
                                            //2.3检测手机内部存储空间大于50M
                                            //随机进入倒计时、
                                            dialog_1.setVisibility(View.GONE);
                                            setValue3();
                                        }else{
                                            dialog_1.setVisibility(View.GONE);
                                            dialog_2.setVisibility(View.VISIBLE);
                                            dialog_2_text.setText(  "手机内部存储空间小于50M，请更换设备考试。");
                                            //2.3检测手机内部存储空间小于50M
//                                            toastLong("手机内部存储空间小于50M，用户手机环境不支持该堂考试");
                                        }
                                    }else{
                                        //判断人脸识别环境不支持  是否允许进入考试
                                        if(camera_exception_enter.equals("1")){
                                            //随机进入倒计时
                                            is_camera_errer = "1";
                                            dialog_1.setVisibility(View.GONE);
                                            setValue3();
                                        }else {
                                            dialog_1.setVisibility(View.GONE);
                                            dialog_2.setVisibility(View.VISIBLE);
                                            dialog_2_text.setText(   "前置摄像头无法使用，请检查是否开启相机权限或更换设备考试。");
                                            //2.2检测前置摄像头不可用
//                                            toastLong("前置摄像头不可使用，用户手机环境不支持该堂考试");
                                        }
                                    }
                                }else{
                                    //判断人脸识别环境不支持  是否允许进入考试
                                    if(camera_exception_enter.equals("1")){
                                        //随机进入倒计时
                                        is_camera_errer = "1";
                                        dialog_1.setVisibility(View.GONE);
                                        setValue3();
                                    }else{
                                        dialog_1.setVisibility(View.GONE);
                                        dialog_2.setVisibility(View.VISIBLE);
                                        dialog_2_text.setText(   "该设备无前置摄像头，请更换设备考试。");
                                        //2.1无前置摄像头，用户手机环境不支持该堂考试
//                                        toastLong("无前置摄像头，用户手机环境不支持该堂考试");
                                    }

                                }

                        } else {
//                            showToast(getString(R.string.active_failed, activeCode));
                            //判断人脸识别环境不支持  是否允许进入考试
                            if(camera_exception_enter.equals("1")){
                                //随机进入倒计时
                                is_camera_errer = "1";
                                dialog_1.setVisibility(View.GONE);
                                setValue3();
                            }else{
                                dialog_1.setVisibility(View.GONE);
                                dialog_2.setVisibility(View.VISIBLE);
                                dialog_2_text.setText(   "该设备环境不支持，请更换设备考试。");
                                //2.1无前置摄像头，用户手机环境不支持该堂考试
//                                toastLong("用户手机环境不支持该堂考试");
                            }
                        }


                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = faceEngine.getActiveFileInfo(context,activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                activeEngine( );
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    private void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }
}
