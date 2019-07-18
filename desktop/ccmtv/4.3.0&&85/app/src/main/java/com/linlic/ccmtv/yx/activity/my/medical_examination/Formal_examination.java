package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Examination_script;
import com.linlic.ccmtv.yx.activity.entity.Examination_script_paper;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.adapter.GroupedListAdapter;
import com.linlic.ccmtv.yx.arcface.model.DrawInfo;
import com.linlic.ccmtv.yx.arcface.util.ConfigUtil;
import com.linlic.ccmtv.yx.arcface.util.DrawHelper;
import com.linlic.ccmtv.yx.arcface.util.camera.CameraHelper;
import com.linlic.ccmtv.yx.arcface.util.camera.CameraListener;
import com.linlic.ccmtv.yx.arcface.widget.FaceRectView;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.service.FloatingImageDisplayService;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.PhotoBitmapUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ZoomImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.coursewares;
import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.coursewares_pos;


/**正式考试
 * Created by Administrator on 2017/9/7.
 */
public class Formal_examination extends BaseActivity   implements ViewTreeObserver.OnGlobalLayoutListener{
    //    private FunGameRefreshView refreshView;
    private DragView camera_layout;
    public static boolean Whether_or_not_run_backstage = false;//是否后台运行
    public static Date Telephone_hang_up_time;
    private Context context;
    private Formal_examination formal_examination;
    private Dialog dialog;
    private View view;
    public static String pid;
    public  Activity activity;
    private ImageView pattern_icon;
    private ImageView practice_hint;
    private LinearLayout arrow_back2;
    private LinearLayout practice_hint2;
    private ListView examination_instructions_list;
    private TextView examination_instructions_time, examination_instructions_name, examination_instructions, total_score_of_examination, examination_instructions_id, schedule_text;
    private TextView examination_instructions_buttpm, check_the_answer_card;
    private TextView remain,curr_num_text;
    private EditText checkCode_ed, checkCode_ed1, checkCode_ed2, checkCode_ed3;
    BaseListAdapter baseListAdapter;
    Examination_paper examination_paper;
    private LinearLayout examination_instructions_timing, examination_instructions_timing2,examination_instructions_timing3, formal_examination_top, formal_examination_butten;
    private ZoomImageView matrixImageView;
    private  GroupedListAdapter adapter;
    private RecyclerView lsvMore;
    private LinearLayout imageLayout;
    public String errorMessage = "";
    private Date startTime;
    private Date endTime;
    private String my_exams_id = "";
    private String is_Lock = "0";
    private Problem curr_Problem;
    private List<Problem> curr_Problem_data  = new ArrayList<Problem>();//一屏一题 题目集合
    private boolean pattern = true;// 是否是 传统 模式     一屏展示所有题  为传统模式   false 为一屏一题 新模式
    public volatile boolean exit = false;
    Thread thread = new Thread(new ThreadShow());
    private GestureDetector mDetector;
    private String is_camera_errer = "0";
    private Boolean is_Face = false;
    private int last_num= 0;
    private int upload = 0 ;//0 不截图 1.截图  2.上传中
    private List<Long> randomlong_list = new ArrayList();//截图随机时间
    private int  is_last = 0;//交卷 截图开关
    private int  is_top = 0;//交卷 截图开关

    CameraListener cameraListener;
    int iii = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功

                            examination_paper.setExamination_paper(jsonObjects);
                            /*解析考试其他信息start*/
                            /*考试提交ID*/
                            examination_instructions_id.setText(examination_paper.getEid());
                            /*考试名称*/
//                           String str="默认颜色<br/><font color='#FF0000'>红颜色</font>";

                            examination_instructions_name.setText(examination_paper.getExamination_instructions_name()+"(共"+ examination_paper.getTitle_num().size()+"题)");
                            /*题目数量*/
                            schedule_text.setText(0 + "/" + examination_paper.getNumber_of_topics());
                            /*redis_key 用户作答保存到服务器使用*/
                            examination_paper.setRedis_key(jsonObjects.has("redis_key") ? jsonObjects.getString("redis_key") : "");
                            /*考试介绍*/
//                            examination_instructions.setText(examination_instructions.getText()+examination_paper.getExamination_instructions());
                            /*考试总分*/
//                           total_score_of_examination.setText(total_score_of_examination.getText()+examination_paper.getTotal_score_of_examination());
                            /*解析考试其他信息end*/
                            examination_paper.setSs(jsonObjects.getInt("paper_mins"));
                            startTime = DateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss");
                            Examination_script_paper examination_script_papers = MyDbUtils.findExamination_script_paper(context, examination_paper.getEid());
                            if (examination_script_papers != null && examination_script_papers.getEid().length() > 0) {
//                                LogUtil.e("查看试卷时间数据", examination_script_papers.toString());
                                int initmin = examination_paper.getSs() * 60;
                                int startmin = initmin - Integer.parseInt(examination_script_papers.getDatetiem_count());
                                if (startmin > 0) {
                                    examination_paper.setSs(startmin / 60);
                                    examination_paper.setMinss(startmin % 60);
                                } else {
                                    examination_paper.setSs(0);
                                    examination_paper.setMinss(0);
                                }
                            } else {
//                                LogUtil.e("查看试卷时间数据", "本地没有数据保存");
                            }
                            thread.start();
                            examination_instructions_buttpm.setClickable(true);
                             /*解析题型列表end*/
                        } else if (jsonObjects.getInt("status") == 11) {
                            examination_instructions_buttpm.setClickable(false);
                            errorMessage = jsonObjects.getString("errorMessage");
//                            Toast.makeText(Formal_examinat ion.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage(errorMessage)
                                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                            //设置点击对话框外部区域不关闭对话框
                            dialog.setCancelable(false);
                        } else {
                            examination_instructions_buttpm.setClickable(false);
                            errorMessage = jsonObjects.getString("errorMessage");
//                            Toast.makeText(Formal_examination.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage(errorMessage)
                                    .setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            setValue2();
                                        }
                                    })
                                    .show();
                            //设置点击对话框外部区域不关闭对话框
                            dialog.setCancelable(false);
                        }
                        MyProgressBarDialogTools.hide();
                        setUser_answer();
                        baseListAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            Toast.makeText(Formal_examination.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            List<Examination_script> examination_scripts = null;
                            examination_scripts = MyDbUtils.findExamination_script_All(context, examination_paper.getEid());
//                            LogUtil.e("试卷-本地保存的答案试卷ID：", examination_paper.getEid());
                            if (examination_scripts != null && examination_scripts.size() > 0) {
                                for (Examination_script examination_script : examination_scripts) {
                                    MyDbUtils.deleteExamination_scriptALL(context, examination_paper.getEid(), examination_script.getOption_id());
                                }
                            }
                            MyProgressBarDialogTools.show(context);
                            if (cameraHelper != null) {
                                cameraHelper.release();
                                cameraHelper = null;
                            }
                            unInitEngine();
                            Intent intent = new Intent(context, Hand_in_the_papers.class);
                            intent.putExtra("pid", pid);
                            intent.putExtra("hand_in_the_papers_point", jsonObjects.getString("total_fen"));
                            intent.putExtra("hand_in_the_papers_text1", jsonObjects.getString("msg"));
                            intent.putExtra("hasper", jsonObjects.getString("hasper"));//1 可以查看答题卡 0 不可查看答题卡
                            intent.putExtra("my_exams_id", my_exams_id);
                            intent.putExtra("my_exams_eid", examination_paper.getEid());
                            intent.putExtra("is_mock_exam","0" );
                            intent.putExtra("score_show",jsonObjects.has("score_show")?jsonObjects.getString("score_show"):"1");
                            intent.putExtra("restart_code",jsonObjects.has("restart_code")?jsonObjects.getString("restart_code"):"1");

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Formal_examination.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    if (!exit) {
                        if (examination_paper.getMinss() > 0) {
                            last_num ++;
                            if(upload == 0 &&randomlong_list.size()>0&& randomlong_list.get(0)<last_num){
                                upload = 1;
                            }
                            examination_paper.setMinss(examination_paper.getMinss() - 1);
                            examination_instructions_time.setText((examination_paper.getSs() > 9 ? examination_paper.getSs() : "0" + examination_paper.getSs()) + ":" + (examination_paper.getMinss() > 9 ? examination_paper.getMinss() : "0" + examination_paper.getMinss()));
                        } else if (examination_paper.getMinss() == 0) {
                            //秒为0时 判断是否还有分
                            if (examination_paper.getSs() > 0) {
                                //有分
                                last_num ++;
                                if(upload == 0 &&randomlong_list.size()>0&& randomlong_list.get(0)<last_num){
                                    upload = 1;
                                }
                                examination_paper.setSs(examination_paper.getSs() - 1);
                                examination_paper.setMinss(59);
                                examination_instructions_time.setText((examination_paper.getSs() > 9 ? examination_paper.getSs() : "0" + examination_paper.getSs()) + ":" + (examination_paper.getMinss() > 9 ? examination_paper.getMinss() : "0" + examination_paper.getMinss()));
                            } else {
                                //无分
                                exit = true;
                                if (PicViewerActivity.picActivity != null && 1 == 1) {
                                    PicViewerActivity.picActivity.finish();
                                }
                                examination_instructions_time.setText("00:00");
                                examination_instructions_timing.setVisibility(View.VISIBLE);
                                examination_instructions_timing2.setVisibility(View.GONE);
                                examination_instructions_buttpm.setVisibility(View.GONE);
                               //交卷
                                if(is_Face && is_camera_errer.equals("0")){
                                    //交卷
                                    MyProgressBarDialogTools.show(context);
                                    is_last = 1;
                                }else{
                                    examination_script1(examination_instructions_buttpm);
                                }
                            }
                        }
                        MyDbUtils.saveExamination_script_paper(context, examination_paper.getEid(), examination_paper.getConfig(), 1);
                    }

                    break;
                case 4:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        JSONObject datajson = jsonObjects.getJSONObject("data");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            if (datajson.getString("status").equals("101")) {
                                Toast.makeText(Formal_examination.this, datajson.getString("msg"), Toast.LENGTH_SHORT).show();
                                examination_instructions_timing2.setVisibility(View.GONE);
                                Telephone_hang_up_time = null;
                                Whether_or_not_run_backstage = false;
                                checkCode_ed.setText("");
                                checkCode_ed1.setText("");
                                checkCode_ed2.setText("");
                                checkCode_ed3.setText("");
                                remain.setText(datajson.getString("remain") + "次");
                            } else if (datajson.getString("status").equals("102")) {
                                // 弹出自定义dialog
                                LayoutInflater inflater = LayoutInflater.from(context);
                                view = inflater.inflate(R.layout.dialog_item5, null);

                                // 对话框
                                dialog = new Dialog(context);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                                // 设置宽度为屏幕的宽度
                                WindowManager windowManager = getWindowManager();
                                Display display = windowManager.getDefaultDisplay();
                                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                                lp.width = (int) (display.getWidth() - 100); // 设置宽度
                                dialog.getWindow().setAttributes(lp);
                                dialog.getWindow().setContentView(view);
                                dialog.setCancelable(false);
                                final TextView btn_sure = (TextView) view.findViewById(R.id.i_understand);// 取消
                                final TextView name = (TextView) view.findViewById(R.id.name);// title
                                name.setText(datajson.getString("msg"));

                                btn_sure.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                       /* dialog.dismiss();
                                        dialog = null;
                                        view = null;*/
                                        if(is_Face && is_camera_errer.equals("0")){
                                            //交卷
                                            MyProgressBarDialogTools.show(context);
                                            is_last = 1;
                                        }else{
                                            examination_script1(examination_instructions_buttpm);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(Formal_examination.this, datajson.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(Formal_examination.this, datajson.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 6:

                        byte[] nv21 = (byte[]) msg.obj;
                   /* //在这种处理下虽然不可以处理内存溢出，但耗时比较久   耗时达到了260ms
                  */
                        if (yuvType == null)
                        {
                            yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
                            in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

                            rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX( previewSize.width ).setY(   previewSize.height);
                            out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);
                        }

                        in.copyFrom(nv21);

                        yuvToRgbIntrinsic.setInput(in);
                        yuvToRgbIntrinsic.forEach(out);

                    Matrix m = new Matrix();
                    m.postRotate(-90);
                    Bitmap bmpout1 = Bitmap.createBitmap( previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888);

                        out.copyTo(bmpout1);
                    bmpout1 = compressImage(bmpout1);

                    Bitmap bmpout = Bitmap.createBitmap(bmpout1,0,0,previewSize.width, previewSize.height,m,true);

                        Calendar now = new GregorianCalendar();
                        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                        String fileName = simpleDate.format(now.getTime());
                        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                        String subForder = "";
                        if(hasSDCard){
                            subForder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/examination/"+examination_paper.getEid()+"/";
                        }

                        try{
                            File foder = new File(subForder);
                            if (!foder.exists()) {
                                foder.mkdirs();
                            }
                            File myCaptureFile = new File(subForder, fileName + ".jpg");
                            Log .e("文件地址",myCaptureFile.getPath().toString());
                            if (!myCaptureFile.exists()) {

                                myCaptureFile.createNewFile();

                            }

                            FileOutputStream  os = new FileOutputStream(myCaptureFile);
                            bmpout.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();


                            startUploadFile(subForder+ fileName + ".jpg");
                        }catch (Exception e){
                            upload = 1;
                            e.printStackTrace();
                        }



                    break;

                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    /*
    判断是否是图片
   */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }
    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 1024) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.formal_examination);
        context = this;
        activity = this;
        findId();
        click();
        remain.setText(getIntent().getStringExtra("Unlock") + "次");
        is_Lock = getIntent().getStringExtra("is_Lock");
        if (getIntent().getStringExtra("Lock").equals("0")) {
            examination_instructions_timing2.setVisibility(View.GONE);
        } else {
            examination_instructions_timing2.setVisibility(View.VISIBLE);
        }
        initdata();
        setValue2();
//        startFloatingImageDisplayService();

       if(is_Face && is_camera_errer.equals("0")){
//               启动人脸
            initArcFace();
            camera_layout.setVisibility(View.VISIBLE);
        }else {
            camera_layout.setVisibility(View.GONE);
        }
        is_top = 1;

    }





    /*人脸识别 悬浮窗*/
    public void startFloatingImageDisplayService( ) {
        if (FloatingImageDisplayService.isStarted) {
            return;
        }
        if(Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            // if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        } else {
            // another similar method that supports device have API < 23
//            startService(new Intent(context, FloatingImageDisplayService.class));

            Intent intent =   new Intent(context, FloatingImageDisplayService.class);
            startService(intent);
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 1) {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
               if (!Settings.canDrawOverlays(this)) {
                   Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                   startService(new Intent(context, FloatingImageDisplayService.class));
               }
           }
        }
    }

    public void setUser_answer() {
        Map<String, Object> map = new HashMap<>();
        List<Examination_script> examination_scripts = null;
        examination_scripts = MyDbUtils.findExamination_script_All(context, examination_paper.getEid());
//        LogUtil.e("试卷-本地保存的答案试卷ID：", examination_paper.getEid());
        if (examination_scripts != null && examination_scripts.size() > 0) {
            for (Examination_script examination_script : examination_scripts) {
//                LogUtil.e("试卷-本地保存的答案", examination_script.getOption_id() + ":" + examination_script.getOption_name());
                map.put(examination_script.getOption_id(), examination_script.getOption_name());
            }
        }
        examination_paper.setUser_answer(map);
    }

    @Override
    protected void onResume() {
//        LogUtil.e("锁屏值",Whether_or_not_run_backstage+"    时间："+Telephone_hang_up_time+"  当前时间："+DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss")+"   相差:"+DateUtil.subtract_seconds(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"), Telephone_hang_up_time));
        if (is_Lock.equals("1")) {
            if (Whether_or_not_run_backstage) {
                if (Telephone_hang_up_time != null && 1 == 1) {
                    if (DateUtil.subtract_seconds(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"), Telephone_hang_up_time) > 15) {
                        examination_instructions_timing2.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Formal_examination.this, "十五秒内，当前可以继续考试", Toast.LENGTH_SHORT).show();
                    }
                    Whether_or_not_run_backstage = false;
                    Telephone_hang_up_time = null;
                } else {
                    examination_instructions_timing2.setVisibility(View.VISIBLE);
                    Whether_or_not_run_backstage = false;
                    Telephone_hang_up_time = null;
                }
            }
        }
   /*     if(is_Face && is_camera_errer.equals("0")){
               *//*启动人脸*//*
            initArcFace();
            camera_layout.setVisibility(View.VISIBLE);
        }else {
            camera_layout.setVisibility(View.GONE);
        }*/
        if(iii>0){
            if(is_Face && is_camera_errer.equals("0")){
                cameraHelper = new CameraHelper.Builder()
                        .previewViewSize(new Point(800,480))
                        .rotation(getWindowManager().getDefaultDisplay().getRotation())
                        .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                        .isMirror(false)
                        .previewOn(previewView)
                        .cameraListener(cameraListener)
                        .build();
                cameraHelper.init();
                cameraHelper.start();
            }

        }


//        faceEngine.
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + pid;
        iii ++;
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
//        unInitEngine();
        super.onPause();
    }

    @Override
    public void back(View view) {
        new AlertDialog.Builder(Formal_examination.this).setTitle("系统提示")//设置对话框标题
                .setMessage("您还未提交考卷，是否交卷？")//设置显示的内容
                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        //交卷
                        if(is_Face && is_camera_errer.equals("0")){
                            //交卷
                            MyProgressBarDialogTools.show(context);
                            is_last = 1;
                        }else{
                            examination_script1(examination_instructions_buttpm);
                        }
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }

    @Override
    protected void onDestroy() {
        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }
        unInitEngine();
        super.onDestroy();
        exit = true;
        if (PicViewerActivity.picActivity != null && 1 == 1) {
            PicViewerActivity.picActivity.finish();
        }
        endTime = DateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss");
        MyDbUtils.saveExamination_script_paper(context, examination_paper.getEid(), examination_paper.getConfig(), DateUtil.subtract_seconds(startTime, endTime));
    }

    // 线程类
    class ThreadShow implements Runnable {
        @Override
        public void run() {
            while (!exit) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void findId() {
        super.findId();
        examination_instructions_list = (ListView) findViewById(R.id.examination_instructions_list);
        //增加头
//        formal_examination_top = (LinearLayout) View.inflate(this, R.layout.formal_examination_top, null);
//        examination_instructions_list.addHeaderView(formal_examination_top);
        pattern_icon = (ImageView) findViewById(R.id.pattern_icon) ;
        practice_hint = (ImageView) findViewById(R.id.practice_hint) ;
        practice_hint2 = (LinearLayout) findViewById(R.id.practice_hint2) ;
        //增加尾部
        formal_examination_butten = (LinearLayout) View.inflate(this, R.layout.layout_buttn, null);
        examination_instructions_list.addFooterView(formal_examination_butten);
          lsvMore = (RecyclerView) findViewById(R.id.rv_list);
        check_the_answer_card = (TextView) findViewById(R.id.check_the_answer_card);
        examination_instructions_name = (TextView) findViewById(R.id.examination_instructions_name);
//        examination_instructions = (TextView) findViewById(R.id.examination_instructions);
        examination_instructions_time = (TextView) findViewById(R.id.examination_instructions_time);
        remain = (TextView) findViewById(R.id.remain);
        curr_num_text = (TextView) findViewById(R.id.curr_num_text);
//        total_score_of_examination = (TextView) findViewById(R.id.total_score_of_examination);
        examination_instructions_id = (TextView) findViewById(R.id.examination_instructions_id);
        schedule_text = (TextView) findViewById(R.id.schedule_text);
        examination_instructions_buttpm = (TextView) findViewById(R.id.examination_instructions_buttpm);
        examination_instructions_timing = (LinearLayout) findViewById(R.id.examination_instructions_timing);
        examination_instructions_timing2 = (LinearLayout) findViewById(R.id.examination_instructions_timing2);
        examination_instructions_timing3 = (LinearLayout) findViewById(R.id.examination_instructions_timing3);
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        arrow_back2 = (LinearLayout) findViewById(R.id.arrow_back2);
        matrixImageView = (ZoomImageView) findViewById(R.id.matrixImageView);
        checkCode_ed = (EditText) findViewById(R.id.checkCode_ed);
        checkCode_ed1 = (EditText) findViewById(R.id.checkCode_ed1);
        checkCode_ed2 = (EditText) findViewById(R.id.checkCode_ed2);
        checkCode_ed3 = (EditText) findViewById(R.id.checkCode_ed3);

        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
          camera_layout = findViewById(R.id.camera_layout);

    }
    //继承了简单的手势类
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        //做手势判断
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Log.i("isFling", "onFling");
            if (!pattern) {
                if (e1 != null && e2 != null) {
                    //左滑动
                    if (e1.getX() - e2.getX() > 100 && Math.abs(e1.getY() - e2.getY()) < 100) {
//                    ToastUtil.shortToast(GestureTestActivity.this, "向左");
//                    LogUtil.e("滑动","向左");
                        if (curr_Problem_data.get(curr_Problem_data.size() - 1).getPosition() < examination_paper.getProblems().size()-1) {
                            curr_Problem = examination_paper.getProblems().get(curr_Problem_data.get(curr_Problem_data.size()-1).getPosition()+1);
                            //判断将夜切换的题目是否是公共题型的题干
                            switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                                case 107://公用题干题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 7) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 108://共用答案题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 8) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 111://公用案例分析题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 11) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 113://案例分析题 客观题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 13) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                default:
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(0);
                                    break;
                            }

                        } else {
                            Toast.makeText(context, "这已经是最后一题啦~", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                    //右滑动
                    else if (e1.getX() - e2.getX() < -100 && Math.abs(e1.getY() - e2.getY()) < 100) {
                        if (curr_Problem_data.get(0).getPosition() > 0) {
                            curr_Problem = examination_paper.getProblems().get(curr_Problem_data.get(0).getPosition() - 1);
                            switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                                case 7:
                                    for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 107) {
                                            curr_Problem = examination_paper.getProblems().get(i);
                                            break;//跳出循环
                                        }
                                    }
                                    break;
                                case 8:
                                    for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 108) {
                                            curr_Problem = examination_paper.getProblems().get(i);
                                            break;//跳出循环
                                        }
                                    }
                                    break;
                                case 11:
                                    for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 111) {
                                            curr_Problem = examination_paper.getProblems().get(i);
                                            break;//跳出循环
                                        }
                                    }
                                    break;
                                case 13:
                                    for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 113) {
                                            curr_Problem = examination_paper.getProblems().get(i);
                                            break;//跳出循环
                                        }
                                    }
                                    break;
                            }

                            //判断将夜切换的题目是否是公共题型的题干
                            switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                                case 107://公用题干题

                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 7) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 108://共用答案题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 8) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 111://公用案例分析题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 11) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                case 113://案例分析题 客观题
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    for (int i = curr_Problem.getPosition() + 1; i < examination_paper.getProblems().size(); i++) {
                                        if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 13) {
                                            curr_Problem_data.add(examination_paper.getProblems().get(i));
                                        } else {
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                            return true;
                                        }
                                        if((i+1) == examination_paper.getProblems().size()){
                                            baseListAdapter.refresh(curr_Problem_data);
                                            examination_instructions_list.setSelection(0);
                                        }
                                    }

                                    break;
                                default:
                                    curr_Problem_data.clear();
                                    curr_Problem_data.add(curr_Problem);
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(0);
                                    break;
                            }
                        } else {
                            Toast.makeText(context, "这是第一题，前边没有题啦~", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


    //分发事件执行的入口，一定会首先执行
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //开启手势结束的动作
        mDetector.onTouchEvent(ev);//手势监听
        //isFling = false;
//        Log.i("isFling", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }


    public void click() {
        if(SharedPreferencesTools.getIsShowPattern2(context).length()>0){
            practice_hint2.setVisibility(View.GONE);
        }else{
            practice_hint2.setVisibility(View.VISIBLE);
            SharedPreferencesTools.saveIsShowPattern2(context,"1");
        }
        practice_hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                practice_hint.setVisibility(View.GONE);
                SharedPreferencesTools.saveIsShowPattern(context,"1");
            }
        });
        practice_hint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                practice_hint2.setVisibility(View.GONE);
                SharedPreferencesTools.saveIsShowPattern2(context,"1");
            }
        });
        //创建手势监听器对象
        mDetector = new GestureDetector(getApplicationContext(), new  MyGestureListener());
        pattern_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pattern =  pattern?false:true;
                if(curr_Problem == null){
                    curr_Problem = examination_paper.getProblems().get(0);
                    curr_Problem_data.add(curr_Problem);
                }
                SharedPreferencesTools.saveIsShowPattern2(context,"1");
                if(pattern){
                    pattern_icon.setImageResource(R.mipmap.examination_instructions_icon17);
                    //传统模式
                    baseListAdapter.refresh(examination_paper.getProblems());
                    if(curr_Problem_data.size()>1){
                        //当 新模式切换 到传统模式 需要找到转换前显示的哪一题    如果curr_Problem 没有题号 那么 代表这是一个题干数据  如果有 那么这是一个题目数
                        if(curr_Problem.getSerial_number().length()>0){
                            examination_instructions_list.setSelection(curr_Problem.getPosition());
                        }else{
                            examination_instructions_list.setSelection(curr_Problem.getPosition());
                        }
                    }else{
                        if(examination_paper.getProblems().size()>0){
                            examination_instructions_list.setSelection(curr_Problem.getPosition());
                        }else{
                            Toast.makeText(Formal_examination.this, "题目源数据有误，请联系管理员~", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(SharedPreferencesTools.getIsShowPattern(context).length()>0){
                        practice_hint.setVisibility(View.GONE);
                    }else{
                        practice_hint.setVisibility(View.VISIBLE);
                        SharedPreferencesTools.saveIsShowPattern(context,"1");
                    }
                    pattern_icon.setImageResource(R.mipmap.examination_instructions_icon19);
                    //新模式 一屏一题
                    //获取当前 可见区域 题目是否为null   如果为null即切换模式到第一题
                    int curr_pos = 0;
                    if(curr_Problem!=null){
                        curr_pos = curr_Problem.getPosition();
                        switch (Integer.parseInt(curr_Problem.getQuestion_type())){
                            case 7:
                                for (int i = curr_Problem.getPosition(); i >= 0 ;i--){
                                    if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 107){
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 8:
                                for (int i = curr_Problem.getPosition(); i >= 0 ;i--){
                                    if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 108){
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 11:
                                for (int i = curr_Problem.getPosition(); i >= 0 ;i--){
                                    if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 111){
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 13:
                                for (int i = curr_Problem.getPosition(); i >= 0 ;i--){
                                    if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 113){
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                        }

                    }else {
                        curr_Problem = examination_paper.getProblems().get(0);
                    }

                    //判断将夜切换的题目是否是公共题型的题干
                    switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                        case 107://公用题干题

                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);

                            for (int i = curr_Problem.getPosition()+1,j = 1; i <examination_paper.getProblems().size();i++,j++){
                                if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 7){
                                    if(curr_pos == examination_paper.getProblems().get(i).getPosition()){
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                }else{
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                    return;
                                }
                                if((i+1) == examination_paper.getProblems().size()){
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                }
                            }

                            break;
                        case 108://共用答案题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);

                            for (int i =curr_Problem.getPosition()+1,j = 1; i <examination_paper.getProblems().size();i++,j++){
                                if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 8){
                                    if(curr_pos == examination_paper.getProblems().get(i).getPosition()){
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                }else{
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                    return;
                                }
                                if((i+1) == examination_paper.getProblems().size()){
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                }
                            }

                            break;
                        case 111://公用案例分析题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i =curr_Problem.getPosition()+1,j = 1; i <examination_paper.getProblems().size();i++,j++){
                                if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 11){
                                    if(curr_pos == examination_paper.getProblems().get(i).getPosition()){
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                }else{
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                    return;
                                }
                                if((i+1) == examination_paper.getProblems().size()){
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                }
                            }

                            break;
                        case 113://案例分析题 客观题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i =curr_Problem.getPosition()+1,j = 1; i <examination_paper.getProblems().size();i++,j++){
                                if(Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 13){
                                    if(curr_pos == examination_paper.getProblems().get(i).getPosition()){
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                }else{
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                    return;
                                }
                                if((i+1) == examination_paper.getProblems().size()){
                                    if(curr_pos == curr_Problem.getPosition()){
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
                                }
                            }

                            break;
                        default:
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            baseListAdapter.refresh(curr_Problem_data);
                            examination_instructions_list.setSelection(curr_pos);
                            break;
                    }
                }
            }
        });
        checkCode_ed3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    checkCode_ed3.setText("");
                    if (checkCode_ed2.getText().length() > 0) {
                        checkCode_ed3.clearFocus();//取消焦点
                        checkCode_ed2.setFocusable(true);
                        checkCode_ed2.setFocusableInTouchMode(true);
                        checkCode_ed2.requestFocus();
                    } else if (checkCode_ed1.getText().length() > 0) {
                        checkCode_ed3.clearFocus();//取消焦点
                        checkCode_ed1.setFocusable(true);
                        checkCode_ed1.setFocusableInTouchMode(true);
                        checkCode_ed1.requestFocus();
                    } else {
                        checkCode_ed3.clearFocus();//取消焦点
                        checkCode_ed.setFocusable(true);
                        checkCode_ed.setFocusableInTouchMode(true);
                        checkCode_ed.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        checkCode_ed2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    checkCode_ed2.setText("");
                    if (checkCode_ed1.getText().length() > 0) {
                        checkCode_ed2.clearFocus();//取消焦点
                        checkCode_ed1.setFocusable(true);
                        checkCode_ed1.setFocusableInTouchMode(true);
                        checkCode_ed1.requestFocus();
                    } else {
                        checkCode_ed2.clearFocus();//取消焦点
                        checkCode_ed.setFocusable(true);
                        checkCode_ed.setFocusableInTouchMode(true);
                        checkCode_ed.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });
        checkCode_ed1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    checkCode_ed1.setText("");
                    checkCode_ed1.clearFocus();//取消焦点
                    checkCode_ed.setFocusable(true);
                    checkCode_ed.setFocusableInTouchMode(true);
                    checkCode_ed.requestFocus();

                    return true;
                }
                return false;
            }
        });

        checkCode_ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                if (s.toString().length() > 0) {
                    if (isVerification_code(s.toString())) {
                        //匹配
                        checkCode_ed.clearFocus();//取消焦点
                        checkCode_ed1.setFocusable(true);
                        checkCode_ed1.setFocusableInTouchMode(true);
                        checkCode_ed1.requestFocus();
                    } else {
                        //不匹配
                        Toast.makeText(Formal_examination.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                        checkCode_ed.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");

            }
        });
        checkCode_ed1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                if (s.toString().length() > 0) {
                    if (isVerification_code(s.toString())) {
                        //匹配
                        checkCode_ed1.clearFocus();//取消焦点
                        checkCode_ed2.setFocusable(true);
                        checkCode_ed2.setFocusableInTouchMode(true);
                        checkCode_ed2.requestFocus();
                    } else {
                        //不匹配
                        Toast.makeText(Formal_examination.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                        checkCode_ed1.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");
            }
        });
        checkCode_ed2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                if (s.toString().length() > 0) {
                    if (isVerification_code(s.toString())) {
                        //匹配
                        checkCode_ed2.clearFocus();//取消焦点
                        checkCode_ed3.setFocusable(true);
                        checkCode_ed3.setFocusableInTouchMode(true);
                        checkCode_ed3.requestFocus();
                    } else {
                        //不匹配
                        Toast.makeText(Formal_examination.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                        checkCode_ed2.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");

            }
        });
        checkCode_ed3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
//                Log.e("输入过程中执行该方法", "文字变化");
                if (s.toString().length() > 0) {
                    if (isVerification_code(s.toString())) {
                        //匹配
                        checkCode_ed3.clearFocus();//取消焦点
                        examination_checkCode();
                    } else {
                        //不匹配
                        Toast.makeText(Formal_examination.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                        checkCode_ed2.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
//                Log.e("输入前确认执行该方法", "开始输入");

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
//                Log.e("输入结束执行该方法", "输入结束");

            }
        });

        arrow_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(examination_instructions_timing3);
//                examination_instructions_timing3.setVisibility(View.GONE);
            }
        });
    }

    public static boolean isVerification_code(String email) {
        String str = "^([a-zA-Z0-9])$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
//        Log.e("匹配",m.matches() + "---");
        return m.matches();
    }

    /*进入考试*/
    public void examination_script(View view) {
        new AlertDialog.Builder(Formal_examination.this).setTitle("系统提示")//设置对话框标题
                .setMessage("是否交卷？")//设置显示的内容
                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        if(is_Face && is_camera_errer.equals("0")){
                            //交卷
                            MyProgressBarDialogTools.show(context);
                            is_last = 1;
                        }else{
                            examination_script1(examination_instructions_buttpm);
                        }

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }

    /*进入考试*/
    public void examination_checkCode() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Examination_script_paper examination_script_papers = MyDbUtils.findExamination_script_paper(context, examination_paper.getEid());
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.checkCode);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", examination_paper.getEid());
                    obj.put("code", checkCode_ed.getText().toString() + checkCode_ed1.getText().toString() + checkCode_ed2.getText().toString() + checkCode_ed3.getText().toString());

//                    LogUtil.e("解锁", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    Log.e("解锁", result);

                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 4;
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

    /*交卷*/
    public void examination_script1(View view) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Examination_script_paper examination_script_papers = MyDbUtils.findExamination_script_paper(context, examination_paper.getEid());
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.handPaper);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", examination_paper.getEid());
                    obj.put("datetime", examination_script_papers.getDatetiem_count());
                    JSONObject answer = new JSONObject();
                    List<Examination_script> examination_scripts = null;
                    examination_scripts = MyDbUtils.findExamination_script_All(context, examination_instructions_id.getText().toString());
                    if (examination_scripts != null && examination_scripts.size() > 0) {
                        for (Examination_script examination_script : MyDbUtils.findExamination_script_All(context, examination_instructions_id.getText().toString())) {
                            answer.put(examination_script.getOption_id(), examination_script.getOption_name());
                        }
                    }
                    obj.put("answer", answer);

//                    LogUtil.e("交卷", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());

//                    Log.e("交卷", result);

                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 2;
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
    /*选择按钮处理*/
    public void viewclick(View view) {
        TextView textView = (TextView) ((LinearLayout) view).getChildAt(0);
        TextView textView2 = (TextView) ((LinearLayout) view).getChildAt(1);
        TextView textView3 = (TextView) ((LinearLayout) view).getChildAt(2);
//        Log.e("选中了", textView.getText().toString());

        LinearLayout parentLayout = (LinearLayout) view.getParent();
        LinearLayout vLayout = (LinearLayout) view;

        switch (Integer.parseInt(textView3.getText().toString())) {
            case 1:
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }

                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type("0");
                }

                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
                break;
            case 12:
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }

                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type("0");
                }

                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
                break;
            case 2:
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
                break;
            case 13:
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
                break;
            case 3:
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }
                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type("0");
                }
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
                break;
            case 7:
                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }
                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type("0");
                }
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("1");
//                Log.e("选中了位置", textView2.getTag().toString() + textView.getTag().toString());
                break;
            default:
                break;
        }

        //多选题 重复点击取消选择
        if ((Integer.parseInt(textView3.getText().toString()) == 2 ||Integer.parseInt(textView3.getText().toString()) == 13) && textView2.getCurrentTextColor() == vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4)) {
            textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
            textView2.setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type("0");
        } else {
            textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_10));
            for (int i = 0; i < vLayout.getChildCount(); i++) {
                if (vLayout.getChildAt(i) instanceof TextView) {
                    ((TextView) vLayout.getChildAt(i)).setTextColor(vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4));
                }
            }
        }

        List<String> list = new ArrayList<>();

        for (Option option : examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions()) {
            if (option.getOption_type().equals("1")) {
                list.add(option.getOption_text());
            }
        }

        if (list.size() > 0) {
            if (list.size() > 1) {
                String str = list.get(0);
                for (int i = 1; i < list.size(); i++) {
                    str += "$$" + list.get(i);
                }
                MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), str);
                submit_server(view.getTag().toString(), str);
            } else {
                MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), list.get(0));
                submit_server(view.getTag().toString(), list.get(0));
            }
        }
    }
    public static void fadeIn(View view, float startAlpha, float endAlpha, long duration) {
        if (view.getVisibility() == View.VISIBLE) return;

        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(startAlpha, endAlpha);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void fadeIn(View view) {
        fadeIn(view, 0F, 1F, 400);

        // We disabled the button in fadeOut(), so enable it here.
        view.setEnabled(true);
    }

    public static void fadeOut(View view) {
        if (view.getVisibility() != View.VISIBLE) return;

        // Since the button is still clickable before fade-out animation
        // ends, we disable the button first to block click.
        view.setEnabled(false);
        Animation animation = new AlphaAnimation(1F, 0F);
        animation.setDuration(400);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    public void initdata() {
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        pid = getIntent().getStringExtra("pid");
        is_camera_errer = getIntent().getStringExtra("is_camera_errer");
        is_Face = getIntent().getBooleanExtra("is_Face",false);
        examination_paper = new Examination_paper();
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        if(is_Face && is_camera_errer.equals("0")) {
            randomlong_list.add(50L);
        /*生成随机数 */
            for (int i = 0; i<3 ;i++){
               int random = (int)(1+Math.random()*(5-1+1));
                Long curr_num;
                if(i>0){
                      curr_num = randomlong_list.get(randomlong_list.size()-1)+random*60L+4L*60L;
                }else{
                    curr_num = random*60L+4L*60L;
                }
                randomlong_list.add(curr_num);
            }
        }

        LogUtil.e("拍照时间",randomlong_list.toString());
         /*初始化答题卡start*/
        lsvMore.setLayoutManager(new LinearLayoutManager(context));
          adapter = new GroupedListAdapter(context,examination_paper.getGroups(),examination_paper);
      /*  adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                Toast.makeText(context, "组头：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });*/
        /*adapter.setOnFooterClickListener(new GroupedRecyclerViewAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                Toast.makeText(context, "组尾：groupPosition = " + groupPosition,
                        Toast.LENGTH_LONG).show();
            }
        });*/
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
               /* Toast.makeText(context, "子项：groupPosition = " + groupPosition
                                + ", childPosition = " + childPosition,
                        Toast.LENGTH_LONG).show();*/
//                        textView.getText().toString() 位置
//                        Log.e("文本", textView.getText().toString());
//                        Log.e("文本", examination_paper.getPosition().get(textView.getText().toString()).toString());
                if (pattern) {
                    examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(examination_paper.getGroups().get(groupPosition).getChildren().get(childPosition).getChild()).toString()));
                } else {
                    curr_Problem = examination_paper.getProblems().get(Integer.parseInt(examination_paper.getPosition().get(examination_paper.getGroups().get(groupPosition).getChildren().get(childPosition).getChild()).toString()));
                    //新模式 一屏一题
                    //获取当前 可见区域 题目是否为null   如果为null即切换模式到第一题
                    int curr_pos = 0;
                    if (curr_Problem != null) {
                        curr_pos = curr_Problem.getPosition();
                        switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                            case 7:
                                for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                    if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 107) {
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 8:
                                for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                    if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 108) {
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 11:
                                for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                    if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 111) {
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                            case 13:
                                for (int i = curr_Problem.getPosition(); i >= 0; i--) {
                                    if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 113) {
                                        curr_Problem = examination_paper.getProblems().get(i);
                                        break;//跳出循环
                                    }
                                }
                                break;
                        }

                    } else {
                        curr_Problem = examination_paper.getProblems().get(0);
                    }

                    //判断将夜切换的题目是否是公共题型的题干
                    switch (Integer.parseInt(curr_Problem.getQuestion_type())) {
                        case 107://公用题干题

                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i = curr_Problem.getPosition() + 1, j = 1; i < examination_paper.getProblems().size(); i++, j++) {
                                if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 7) {
                                    if (curr_pos == examination_paper.getProblems().get(i).getPosition()) {
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                } else {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                                if ((i + 1) == examination_paper.getProblems().size()) {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                            }

                            break;
                        case 108://共用答案题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i = curr_Problem.getPosition() + 1, j = 1; i < examination_paper.getProblems().size(); i++, j++) {
                                if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 8) {
                                    if (curr_pos == examination_paper.getProblems().get(i).getPosition()) {
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                } else {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                                if ((i + 1) == examination_paper.getProblems().size()) {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                            }

                            break;
                        case 111://公用案例分析题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i = curr_Problem.getPosition() + 1, j = 1; i < examination_paper.getProblems().size(); i++, j++) {
                                if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 11) {
                                    if (curr_pos == examination_paper.getProblems().get(i).getPosition()) {
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                } else {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                                if ((i + 1) == examination_paper.getProblems().size()) {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                            }

                            break;
                        case 113://公用案例分析题
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            for (int i = curr_Problem.getPosition() + 1, j = 1; i < examination_paper.getProblems().size(); i++, j++) {
                                if (Integer.parseInt(examination_paper.getProblems().get(i).getQuestion_type()) == 13) {
                                    if (curr_pos == examination_paper.getProblems().get(i).getPosition()) {
                                        curr_pos = j;
                                    }
                                    curr_Problem_data.add(examination_paper.getProblems().get(i));
                                } else {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                                if ((i + 1) == examination_paper.getProblems().size()) {
                                    if (curr_pos == curr_Problem.getPosition()) {
                                        curr_pos = 0;
                                    }
                                    baseListAdapter.refresh(curr_Problem_data);
                                    examination_instructions_list.setSelection(curr_pos);
//                                    examination_instructions_timing3.setVisibility(View.GONE);
                                    fadeOut(examination_instructions_timing3);
                                    return;
                                }
                            }

                            break;
                        default:
                            curr_Problem_data.clear();
                            curr_Problem_data.add(curr_Problem);
                            baseListAdapter.refresh(curr_Problem_data);
                            examination_instructions_list.setSelection(curr_pos);
                            break;
                    }
                }

//                examination_instructions_timing3.setVisibility(View.GONE);
                fadeOut(examination_instructions_timing3);
            }
        });

        examination_instructions_timing3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                examination_instructions_timing3.setVisibility(View.GONE);
                fadeOut(examination_instructions_timing3);
            }
        });


           check_the_answer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 examination_instructions_timing3.setVisibility(View.VISIBLE);
                fadeIn(examination_instructions_timing3);
                adapter.notifyDataSetChanged();
                int num = 0;
                for (int i = 0 ; i <examination_paper.getStatus().size();i++){
                    if(examination_paper.getStatus().get(i)>0){
                        num++;
                    }
                }
                curr_num_text.setText(Html.fromHtml("<font  color='#3897F9'>"+num+"</font>/"+examination_paper.getTitle_num().size()));
            }
        });

//                MyGridAdapter2 myGridAdapter = new MyGridAdapter2(context, examination_paper.getTitle_num(), examination_paper.getStatus());
        lsvMore.setAdapter(adapter);
        //直接使用GroupedGridLayoutManager实现子项的Grid效果
        GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(context, 5, adapter);
        lsvMore.setLayoutManager(gridLayoutManager);
         /*初始化答题卡end*/
        baseListAdapter = new BaseListAdapter(examination_instructions_list, examination_paper.getProblems(), R.layout.formal_examination_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                Problem problem = (Problem) item;
                System.out.println("看看罗" + problem.toString());
                if (problem.getQuestion_type().length() > 0) {

                    helper.setViewVisibility(R.id.layout, View.VISIBLE);

                    switch (Integer.parseInt(problem.getQuestion_type())) {
                        case 1://单选
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");

                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.radios_add_layout);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.radios_add_layout,radios);

                                }
                                helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 12://单选
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");

                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.radios_add_layout);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.radios_add_layout,radios);

                                }
                                helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 2://复选框
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.radios_add_layout);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.radios_add_layout,radios);

                                }
                                helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 3://判断题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.VISIBLE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            //设置  题型描述
                            helper.setText(R.id.judgment_question_type, problem.getQuestion_type_text());
                            //设置 题目
                            helper.setText(R.id.judgment_question_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
 /*开始配置 图片*/
                            helper.setMyGridView(R.id.judgment_question_gridview, problem.getThumbnails(), problem.getPictures());
                            //循环增加选项
                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.public_topic3);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.public_topic3,radios);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 4://填空题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.VISIBLE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            //测试 填空题
                            helper.setFillInTheBlanks(R.id.fbv_content, problem, baseListAdapter);
                            //设置  题型描述
                            helper.setText(R.id.li_blank_type, problem.getQuestion_type_text());
                            helper.setMyGridView(R.id.li_blank_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 5://名词解释
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_eid, examination_paper.getEid());//考卷EID
                            helper.setTag(R.id.janes_answer, problem.getPosition() + "");
                            //设置  题型描述
                            helper.setText(R.id.janes_answer_type, problem.getQuestion_type_text());
                            helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                            //设置 题目
                            helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (helper.getStr(R.id.janes_answer_editText) != null && helper.getStr(R.id.janes_answer_editText) != problem.getUser_answer()) {
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer() + " ");
                            }
                            //改变监听
                            helper.setFocus(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            helper.setOnFocusChangeListener(R.id.janes_answer_editText, examination_paper, problem.getTitle_serial_number());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 6://案例分析题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_eid, examination_paper.getEid());//考卷EID
                            helper.setTag(R.id.janes_answer, problem.getPosition() + "");
                            //设置  题型描述
                            helper.setText(R.id.janes_answer_type, problem.getQuestion_type_text());
                            helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                            //设置 题目
                            helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (helper.getStr(R.id.janes_answer_editText) != null && helper.getStr(R.id.janes_answer_editText) != problem.getUser_answer()) {
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer() + " ");
                            }
                            //改变监听
                            helper.setFocus(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            helper.setOnFocusChangeListener(R.id.janes_answer_editText, examination_paper, problem.getTitle_serial_number());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 7://共用题干题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.VISIBLE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置 题目
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.public_topic_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.GONE);//
                            }
                            helper.setText(R.id.public_topic_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.public_topic2);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.public_topic2,radios);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;
                        case 8://共用答案题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.VISIBLE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置 题目
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.common_answer_question_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.common_answer_question_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.common_answer_question_type_problem, View.GONE);//
                            }
                            helper.setText(R.id.common_answer_question_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
//                            helper.setMyGridView(R.id.common_answer_question_gridview_img, problem.getThumbnails(), problem.getPictures());
                            helper.setoptionProblemAdapter(R.id.common_answer_question_options, problem, examination_paper.getEid());
//                            helper.setoptionProblemAdapter(R.id.common_answer_question_gridview, problem.getOptions());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;
                        case 9://问答题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_eid, examination_paper.getEid());//考卷EID
                            helper.setTag(R.id.janes_answer, problem.getPosition() + "");
                            //设置  题型描述
                            helper.setText(R.id.janes_answer_type, problem.getQuestion_type_text());
                            //设置 题目
                            helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (helper.getStr(R.id.janes_answer_editText) != null && helper.getStr(R.id.janes_answer_editText) != problem.getUser_answer()) {
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer() + " ");
                            }
                            //改变监听
                            helper.setFocus(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            helper.setOnFocusChangeListener(R.id.janes_answer_editText, examination_paper, problem.getTitle_serial_number());
                            helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 10://简答题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_eid, examination_paper.getEid());//考卷EID
                            helper.setTag(R.id.janes_answer, problem.getPosition() + "");
                            //设置  题型描述
                            helper.setText(R.id.janes_answer_type, problem.getQuestion_type_text());
                            //设置 题目
                            helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (helper.getStr(R.id.janes_answer_editText) != null && helper.getStr(R.id.janes_answer_editText) != problem.getUser_answer()) {
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer() + " ");
                            }
                            helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                            //改变监听
                            helper.setFocus(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            helper.setOnFocusChangeListener(R.id.janes_answer_editText, examination_paper, problem.getTitle_serial_number());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 11://公共案例分析题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.VISIBLE);//公用案例分析题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.janes_answer_eid, examination_paper.getEid());//考卷EID
                            //设置小题干
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.public_case_analysis_problem_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.public_case_analysis_problem_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.public_case_analysis_problem_type_problem, View.GONE);//
                            }
                            //设置 题目
                            helper.setText(R.id.public_case_analysis_problem_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            helper.setMyGridView(R.id.public_case_analysis_problem_gridview, problem.getThumbnails(), problem.getPictures());
//                            helper.setPublic_case_analysis_problem(R.id.public_case_analysis_problem_gridview_problems,problem.getProblems(),problem.getPosition(),examination_instructions_id.getText().toString());
                            //改变监听
                            helper.setText(R.id.public_case_analysis_number, problem.getTitle_serial_number());//题目序号
                            helper.setText(R.id.public_case_analysis_eid, examination_paper.getEid());//考卷EID
                            helper.setTag(R.id.public_case_analysis_problem, problem.getPosition() + "");
                            if (helper.getStr(R.id.public_case_analysis_problem_editText) != null && helper.getStr(R.id.public_case_analysis_problem_editText) != problem.getUser_answer()) {
                                helper.setText(R.id.public_case_analysis_problem_editText, problem.getUser_answer() + " ");
                            }
                            helper.setFocus(R.id.public_case_analysis_problem_editText, examination_paper, R.id.public_case_analysis_problem);
                            helper.setOnFocusChangeListener(R.id.public_case_analysis_problem_editText, examination_paper, problem.getTitle_serial_number());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;
                        case 13://复选框
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//填空题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            //循环增加选项
                            try {
                                helper.removeViews2(R.id.radios_add_layout);
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    View radios = LayoutInflater.from(context).inflate(R.layout.radios_add_layout , null , false) ;
                                    TextView radio_id = radios.findViewById(R.id.radio_id);
                                    TextView radio_text = radios.findViewById(R.id.radio_text);
                                    TextView radio_type = radios.findViewById(R.id.radio_type);
                                    LinearLayout radios_layout = radios.findViewById(R.id.radios_layout);
                                    radio_text.setText(Html.fromHtml(problem.getOptions().get(i).getOption_id() + "、" + problem.getOptions().get(i).getOption_text()));
                                    radios_layout.setTag(problem.getTitle_serial_number());
                                    radio_text.setTag(""+i);
                                    radio_id.setTag(problem.getPosition() + "");
                                    radio_type.setText( problem.getQuestion_type());
                                    //设置颜色
                                    if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 0) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                        radio_text.setTextColor(Color.parseColor("#333333"));
                                    } else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 1) {
                                        radio_id.setBackgroundResource(R.mipmap.exams2_10);
                                        radio_text.setTextColor(Color.parseColor("#3698F9"));
                                    }
                                    helper.addview2(R.id.radios_add_layout,radios);

                                }
                            /*开始配置选中事件end*/


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;
                        case 107://公用题干题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.VISIBLE);//共用题干题
                            //设置题目序号 title_serial_number
                            //设置 题目
                            helper.setText(R.id.common_practice_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            helper.setMyGridView(R.id.common_practice_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 108://共用答案题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.VISIBLE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            //设置 题目 common_answers_gridview_problems
                            helper.setText(R.id.common_answers_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
//                            helper.setoptionProblemAdapter(R.id.common_answers_gridview_problems, problem.getOptions());
                            helper.setoptionProblemAdapter2(R.id.common_answers_gridview_problems, problem.getOptions());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 111://公用案例分析题
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.VISIBLE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                            //设置题目序号 title_serial_number
                            //设置 题目
                            helper.setText(R.id.common_case_analysis_exercises_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            helper.setMyGridView(R.id.common_case_analysis_exercises_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 113://案例分析题（客观题）  题干
                            //控制显示 隐藏
                            helper.setViewVisibility(R.id.judgment_question, View.GONE);//单选
                            helper.setViewVisibility(R.id.radios, View.GONE);//单选
                            helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                            helper.setViewVisibility(R.id.li_blank, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_topic, View.GONE);//填空题
                            helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                            helper.setViewVisibility(R.id.common_answer_question, View.GONE);//共用答案题
                            helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                            helper.setViewVisibility(R.id.common_practice, View.VISIBLE);//共用题干题
                            //设置题目序号 title_serial_number
                            //设置 题目
                            helper.setText(R.id.common_practice_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            helper.setMyGridView(R.id.common_practice_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                } else {
                    helper.setViewVisibility(R.id.layout, View.GONE);
                }
                if(problem.getPosition() == 0){
                    helper.setViewVisibility(R.id.vh, View.GONE);
                    helper.setViewVisibility(R.id.vh2, View.GONE);
                }
                if(!pattern){
                    helper.setViewVisibility(R.id.vh, View.GONE);
                    helper.setViewVisibility(R.id.vh2, View.GONE);
                }
            }
        };
        examination_instructions_list.setAdapter(baseListAdapter);
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                LogUtil.e("题目位置信息","firstVisibleItem:"+firstVisibleItem+"  visibleItemCount："+visibleItemCount+"  totalItemCount:"+totalItemCount+"  mListView.getChildAt(0).getTop():"+examination_instructions_list.getChildAt(0).getTop() +"  mListView.getPaddingTop():"+examination_instructions_list.getPaddingTop());
                //可见区域 第一个item 顶部距离listview 顶部 小于-100 代表 显示不全 即表示用户不是正在看此题
                if(pattern){
                    if (examination_instructions_list.getChildAt(0).getTop() <-100) {
                        if(examination_paper.getProblems().size()> (firstVisibleItem+1)){
                            if(examination_paper!=null && examination_paper.getProblems().size()>0){
                                curr_Problem =  examination_paper.getProblems().get(firstVisibleItem+1);
                            }
                        }

                    }else{
                        if(examination_paper!=null && examination_paper.getProblems().size()>0){
                            curr_Problem =  examination_paper.getProblems().get(firstVisibleItem);
                        }
                    }
                }else{
                    if (examination_instructions_list.getChildAt(0).getTop() <-100) {
                        if(curr_Problem_data.size()>1){
                            curr_Problem =  curr_Problem_data.get(firstVisibleItem+1);
                        }else{
                            curr_Problem =  curr_Problem_data.get(firstVisibleItem);
                        }
                    }else{
                        if(curr_Problem_data!=null && curr_Problem_data.size()>0){
                            curr_Problem =  curr_Problem_data.get(firstVisibleItem);
                        }
                    }
                }



            }
        });
    }

    public void submit_server(final String qid, final String answer) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.disasterSave);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("qid", qid);
                    obj.put("redis_key", examination_paper.getRedis_key());
                    obj.put("answer", answer);
                    obj.put("remain_time", examination_paper.getSs() * 60 + examination_paper.getMinss());

//                    LogUtil.e("用户作答-提交服务器上行", obj.toString());
                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    LogUtil.e("用户作答-提交服务器下行", result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doPaperStarts);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("source", "Android");
//                    LogUtil.e("考卷访问地址", URLConfig.Medical_examination );
//                    LogUtil.e("考卷访问内容", obj.toString());
                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
                    LogUtil.e("考卷", result);

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

    /*进入考试*/
    public void enter_the_examination(View view) {
        Intent intent = new Intent(Formal_examination.this, My_exams_over.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
//            Log.d("返回", "onKeyDown()");
            new AlertDialog.Builder(Formal_examination.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("您还未提交考卷，是否交卷？")//设置显示的内容
                    .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            //交卷
                            if(is_Face && is_camera_errer.equals("0")){
                                //交卷
                                MyProgressBarDialogTools.show(context);
                                is_last = 1;
                            }else{
                                examination_script1(examination_instructions_buttpm);
                            }
                        }

                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件
                }

            }).show();//在按键响应事件中显示此对话框
        }
        return super.onKeyDown(keyCode, event);
    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Formal_examination.this).setTitle("系统提示")//设置对话框标题
                .setMessage("您还未提交考卷，是否交卷？")//设置显示的内容
                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        if(is_Face && is_camera_errer.equals("0")){
                            //交卷
                            MyProgressBarDialogTools.show(context);
                            is_last = 1;
                        }else{
                            examination_script1(examination_instructions_buttpm);
                        }

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }



    private static final String TAG = "FormalActivity";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    private Integer rgbCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private FaceEngine faceEngine;
    private int afCode = -1;
    private int processMask = FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS;
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    private FaceRectView faceRectView;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    private RenderScript rs;
    private ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic;
    private Type.Builder yuvType, rgbaType;
    private Allocation in, out;
    private void initEngine() {
        faceEngine = new FaceEngine();
//        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_IMAGE, ConfigUtil.getFtOrient(this),
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, 20, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);
        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }

    private void unInitEngine() {

        if (afCode == 0) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }




    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this.getApplicationContext(), neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

          cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Log.i(TAG, "onCameraOpened: " + cameraId + "  " + displayOrientation + " " + isMirror);
                previewSize = camera.getParameters().getPreviewSize();

//                previewSize = new Camera.Size(800,480);
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror,false,false);
            }


            @Override
            public void onPreview(byte[] nv21, Camera camera) {
                Log.i(TAG, "111111111111111111111111111 " );
                if(is_last == 1 || is_top== 1){
                    if(is_last == 1){
                        is_last = 2;
                    }
                    if(is_top == 1){
                        is_top = 2;
                    }
                    Message message = new Message();
                    message.what = 6;
                    message.obj = nv21;
                    handler.sendMessage(message);
                }
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }

                List<FaceInfo> faceInfoList = new ArrayList<>();
                int code = faceEngine.detectFaces(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList);
                if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                    code = faceEngine.process(nv21, previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, faceInfoList, processMask);
                    if (code != ErrorInfo.MOK) {
                        return;
                    }
                }else {
                    return;
                }


                if(upload == 1){
                    upload = 2;
                    Message message = new Message();
                    message.what = 6;
                    message.obj = nv21;
                    handler.sendMessage(message);
                }




                List<AgeInfo> ageInfoList = new ArrayList<>();
                List<GenderInfo> genderInfoList = new ArrayList<>();
                List<Face3DAngle> face3DAngleList = new ArrayList<>();
                List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
                int ageCode = faceEngine.getAge(ageInfoList);
                int genderCode = faceEngine.getGender(genderInfoList);
                int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
                int livenessCode = faceEngine.getLiveness(faceLivenessInfoList);

                //有其中一个的错误码不为0，return
                if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
                    return;
                }
                if (faceRectView != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < faceInfoList.size(); i++) {
                        drawInfoList.add(new DrawInfo(drawHelper.adjustRect(faceInfoList.get(i).getRect()), genderInfoList.get(i).getGender(), ageInfoList.get(i).getAge(), faceLivenessInfoList.get(i).getLiveness(), null));
                    }
                    drawHelper.draw(faceRectView, drawInfoList);
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(800,480))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraId != null ? rgbCameraId : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
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
                initEngine();
                initCamera();
                if (cameraHelper != null) {
                    cameraHelper.start();
                }
            } else {
                Toast.makeText(this.getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 在{@link #previewView}第一次布局完成后，去除该监听，并且进行引擎和相机的初始化
     */
    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }
    }


    public void initArcFace(){
        //在布局结束后才做初始化操作
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        rs = RenderScript.create(this);
        yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
    }


    /*
    * 人脸识别 保存图片
    * 1.建立需要上传图片 随机数 数组
    * 2.随机数建立 （1-5之间的随机数 随机数固定+4分钟）
    * 3.除了首张图片与交卷图片 中间的图片需要检测到人脸才能提交（使用参数控制）
    * */

    public void startUploadFile(final String file_path) {

        LogUtil.e("文件路径", file_path);
        RequestParams params = new RequestParams();
        params.addBodyParameter("examFile", new File(file_path));

        HttpUtils httpUtils = new HttpUtils();
        //设置线程数
//                httpUtils.configRequestThreadPoolSize(1);

        JSONObject jsonObject = new JSONObject();
        String datacheck ="";
        try {
            jsonObject.put("act", URLConfig.examFaceRecord);
            jsonObject.put("uid", SharedPreferencesTools.getUid(context));
            jsonObject.put("pid", pid);
            jsonObject.put("eid", examination_paper.getEid());

            JSONObject obj = new JSONObject();
            obj.put("userAccount", SharedPreferencesTools.getUserName(context));
            obj.put("password", SharedPreferencesTools.getPassword(context));
            obj.put("sourceflag", SharedPreferencesTools.getWhether_the_quick_login(context));
            obj.put("mobphone", SharedPreferencesTools.getMobphone(context));
            Log.e("datacheck", obj.toString());
              datacheck = Base64utils.getBase64(Base64utils.getBase64(obj.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.e("图片上传参数",jsonObject.toString());
        params.addBodyParameter("data", Base64utils.getBase64(Base64utils.getBase64(jsonObject.toString())));
        params.addBodyParameter("datacheck", datacheck);


        httpUtils.send(HttpRequest.HttpMethod.POST, URLConfig.Learning_task, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = Base64utils.getFromBase64(Base64utils.getFromBase64(responseInfo.result));
                LogUtil.e("上传成功：", result);
//                {"status":"2","errorMessage":"\u8d26\u53f7\u4fe1\u606f\u4e0d\u5b58\u5728\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\uff01"}
                try {
                    final JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {

                            upload = 0;
                            if(randomlong_list.size()>0 && randomlong_list.get(0)<last_num){
                                randomlong_list.remove(0);
                            }
                            deleteFile(file_path);

                            //判断是否需要交卷
                            if(is_last == 2){
                                //判断是否图片已经上传完成
                                getFileName();
                            }

                    } else {

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e("上传失败：", s);
                LogUtil.e("错误：", e.toString());


            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Toast.makeText(context,"正在上传，完成后将退出",Toast.LENGTH_SHORT).show();
                LogUtil.e("上传进度", current + "/" + total + "    状态：" + isUploading);
                LogUtil.e("上传进度", ((float) current / (float) total * 100) + "");

            }
        });
    }

    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


    public  void getFileName() {
        String fileAbsolutePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/examination/"+examination_paper.getEid()+"/";
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String path = subFile[iFileLength].getPath();
                Log.e("eee","文件名 ： " + path);
                startUploadFile(path);
                return;
            }
        }
        //没有图片需要上传了 可以进行交卷
        examination_script1(examination_instructions_buttpm);
    }
}
