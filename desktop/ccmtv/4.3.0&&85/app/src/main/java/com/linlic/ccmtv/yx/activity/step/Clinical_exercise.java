package com.linlic.ccmtv.yx.activity.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Date_Weekdays;
import com.linlic.ccmtv.yx.activity.entity.StepData;
import com.linlic.ccmtv.yx.activity.step.config.Constant;
import com.linlic.ccmtv.yx.activity.step.service.StepService;
import com.linlic.ccmtv.yx.activity.step.utils.DbUtils;
import com.linlic.ccmtv.yx.utils.DateUtil;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by Administrator on 2017/12/7.
 */

public class Clinical_exercise  extends BaseActivity   {
    Context context;
    private static int stepnum = 0;
    //循环取当前时刻的步数中间的时间间隔
    private long TIME_INTERVAL = 500;
    //控件
    private TextView text_step,historical_step_number,calorie;    //显示走的步数
    private Messenger messenger;
    private Handler delayHandler;
    private RelativeLayout monday_layout,tuesday_layout,wednesday_layout,thursday_layout,friday_layout,saturday_layout,sunday_layout;
    private View monday_view,tuesday_view,wednesday_view,thursday_view,friday_view,saturday_view,sunday_view;
    private ProgressBar monday_progressbar,tuesday_progressbar,wednesday_progressbar,thursday_progressbar,friday_progressbar,saturday_progressbar,sunday_progressbar;
    private TextView monday,tuesday,wednesday,thursday,friday,saturday,sunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.clinical_exercise);
        context = this;
        findId();
        init();
    }

    @Override
    public void findId() {
        super.findId();
        text_step = (TextView) findViewById(R.id.main_text_step);
        calorie = (TextView) findViewById(R.id.calorie);
        historical_step_number = (TextView) findViewById(R.id.historical_step_number);
        monday_layout =(RelativeLayout) findViewById(R.id.monday_layout);
        tuesday_layout =(RelativeLayout) findViewById(R.id.tuesday_layout);
        wednesday_layout =(RelativeLayout) findViewById(R.id.wednesday_layout);
        thursday_layout =(RelativeLayout) findViewById(R.id.thursday_layout);
        friday_layout =(RelativeLayout) findViewById(R.id.friday_layout);
        saturday_layout =(RelativeLayout) findViewById(R.id.saturday_layout);
        sunday_layout =(RelativeLayout) findViewById(R.id.sunday_layout);
        monday_view = (View) findViewById(R.id.monday_view);
        tuesday_view = (View) findViewById(R.id.tuesday_view);
        wednesday_view = (View) findViewById(R.id.wednesday_view);
        thursday_view = (View) findViewById(R.id.thursday_view);
        friday_view = (View) findViewById(R.id.friday_view);
        saturday_view = (View) findViewById(R.id.saturday_view);
        sunday_view = (View) findViewById(R.id.sunday_view);
        monday_progressbar = (ProgressBar) findViewById(R.id.monday_progressbar);
        tuesday_progressbar = (ProgressBar) findViewById(R.id.tuesday_progressbar);
        wednesday_progressbar = (ProgressBar) findViewById(R.id.wednesday_progressbar);
        thursday_progressbar = (ProgressBar) findViewById(R.id.thursday_progressbar);
        friday_progressbar = (ProgressBar) findViewById(R.id.friday_progressbar);
        saturday_progressbar = (ProgressBar) findViewById(R.id.saturday_progressbar);
        sunday_progressbar = (ProgressBar) findViewById(R.id.sunday_progressbar);
        monday = (TextView) findViewById(R.id.monday);
        tuesday = (TextView) findViewById(R.id.tuesday);
        wednesday = (TextView) findViewById(R.id.wednesday);
        thursday = (TextView) findViewById(R.id.thursday);
        friday = (TextView) findViewById(R.id.friday);
        saturday = (TextView) findViewById(R.id.saturday);
        sunday = (TextView) findViewById(R.id.sunday);
    }

    public void init(){
        //在创建方法中有判断，如果数据库已经创建了不会二次创建的
        DbUtils.createDb(this, Constant.DB_NAME);
        List<StepData>  data = DbUtils.getQueryAll(StepData.class);
        DbUtils.getQueryAll2(StepData.class);
        List<Date_Weekdays> date_weekdayses = DateUtil.printWeekdays();
        for (int i = 0;i<data.size();i++){
            for (Date_Weekdays date_weekday:date_weekdayses){
                if(data.get(i).getToday().equals(date_weekday.getDate())){
                    if(Integer.parseInt(data.get(i).getStep())>stepnum){
                        stepnum = Integer.parseInt(data.get(i).getStep());
                    }
                    date_weekday.setStepnum(Integer.parseInt(data.get(i).getStep()));
                }
            }
        }

        for (Date_Weekdays date_weekday:date_weekdayses){
            // 创建一个数值格式化对象
            NumberFormat numberFormat = NumberFormat.getInstance();
            switch (date_weekday.getName()){
                case "周一":
                    if(date_weekday.getIs() == 1){
                        monday.setText("今天");
                    }else{
                        monday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            monday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            monday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        monday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周二":
                    if(date_weekday.getIs() == 1){
                        tuesday.setText("今天");
                    }else{
                        tuesday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            tuesday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            tuesday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        tuesday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周三":
                    if(date_weekday.getIs() == 1){
                        wednesday.setText("今天");
                    }else{
                        wednesday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            wednesday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            wednesday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        wednesday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周四":
                    if(date_weekday.getIs() == 1){
                        thursday.setText("今天");
                    }else{
                        thursday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            thursday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            thursday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        thursday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周五":
                    if(date_weekday.getIs() == 1){
                        friday.setText("今天");
                    }else{
                        friday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            friday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            friday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        friday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周六":
                    if(date_weekday.getIs() == 1){
                        saturday.setText("今天");
                    }else{
                        saturday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            saturday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            saturday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        saturday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case "周日":
                    if(date_weekday.getIs() == 1){
                        sunday.setText("今天");
                    }else{
                        sunday.setText(date_weekday.getName());
                    }
                    if(date_weekday.getStepnum()>0) {
                        // 设置精确到小数点后2位
                        numberFormat.setMaximumFractionDigits(2);
                        String str = numberFormat.format((float) date_weekday.getStepnum() / (float) stepnum * 100);
                        if(str.indexOf(".")>0) {
                            sunday_progressbar.setProgress(Integer.parseInt(str.substring(0, str.indexOf("."))));
                        }else{
                            sunday_progressbar.setProgress(Integer.parseInt(str ));
                        }
                    }else{
                        sunday_view.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }

        historical_step_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clinical_exercise.this,Historical_step_number.class);
                startActivity(intent);
            }
        });

        //在创建方法中有判断，如果数据库已经创建了不会二次创建的
        DbUtils.createDb(this,Constant.DB_NAME);
        //获取当天的数据
        List<StepData> list=DbUtils.getQueryByWhere(StepData.class,"today",new String[]{StepService.getTodayDate()});
        if(list!=null && list.size()>0){
            //更新步数
            text_step.setText(list.get(0).getStep() + "");
            //        0.043512*步数=消耗能量（千卡）
            BigDecimal bg = new BigDecimal(0.043512*Integer.parseInt(list.get(0).getStep()) );
            double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            calorie.setText("消耗了"+ f1+"卡路里");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setupService();
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

    /**
     * 开启服务
     */
    private void setupService() {

    }

    @Override
    protected void onDestroy() {
        //取消服务绑定
        super.onDestroy();
    }


}

