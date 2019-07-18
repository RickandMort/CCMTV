package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StudyRecordMyExaminationActivity extends BaseActivity {
    Context context;
    private TextView title_name;
    @Bind(R.id.chart1)
    PieChart mPieChart1;
    @Bind(R.id.chart2)
    PieChart mPieChart2;
    @Bind(R.id.ll_lengendlayout1)
    LinearLayout mLengendlayout1;
    @Bind(R.id.ll_lengendlayout2)
    LinearLayout mLengendlayout2;
    @Bind(R.id.percent)
    TextView tv_percent;
    @Bind(R.id.spend_total_time)
    TextView tv_spend_total_time;
    @Bind(R.id.spend_per_time)
    TextView tv_spend_per_time;
    @Bind(R.id.exam_total)
    TextView tv_exam_total;
    @Bind(R.id.max_exam_score)
    TextView tv_max_exam_score;
    @Bind(R.id.min_exam_score)
    TextView tv_min_exam_score;

    private TextView tvHospitalTask;
    private TextView tvHospitalExam;

    private int[] colors;//颜色集合
    List<Entry> values;
    private String suc_num, fail_num, percent, spend_total_time, spend_per_time;
    private String exam_total, exam_do_total, max_exam_score, min_exam_score, formal_do_total, makeup_do_total, fail_exam_total;
    private String urlconfig;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            MyProgressBarDialogTools.hide();
                            //任务
                            JSONObject jsonObject1 = jsonObject.getJSONObject("task");
                            suc_num = jsonObject1.getString("suc_num");
                            fail_num = jsonObject1.getString("fail_num");
                            percent = jsonObject1.getString("percent");
                            spend_total_time = jsonObject1.getString("spend_total_time");
                            spend_per_time = jsonObject1.getString("spend_per_time");

                            //考试
                            JSONObject jsonObject2 = jsonObject.getJSONObject("exam");
                            exam_total = jsonObject2.getString("exam_total");
                            exam_do_total = jsonObject2.getString("exam_do_total");
                            max_exam_score = jsonObject2.getString("max_exam_score");
                            min_exam_score = jsonObject2.getString("min_exam_score");
                            formal_do_total = jsonObject2.getString("formal_do_total");
                            makeup_do_total = jsonObject2.getString("makeup_do_total");
                            fail_exam_total = jsonObject2.getString("fail_exam_total");

                            tv_percent.setText(percent);//任务完成率
                            tv_spend_total_time.setText(spend_total_time + "小时");//总耗时
                            tv_spend_per_time.setText(spend_per_time + "小时");//平均耗时

                            tv_exam_total.setText(exam_do_total + "/" + exam_total);//考试数量
                            tv_max_exam_score.setText(max_exam_score + "分");//最高分
                            tv_min_exam_score.setText(min_exam_score + "分");//最低分

                            initData();
                        } else {
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(context, jsonObject.getString(""), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_record_my_examination);

        context = this;
        ButterKnife.bind(this);
        findId();
        if (SharedPreferencesTools.getIsdocexam(context)) {
            urlconfig = URLConfig.UserTongji;
        } else {
            urlconfig = URLConfig.CCMTVAPP;
        }
        setValue();
    }

    private void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myExamine);
                    obj.put("uid", SharedPreferencesTools.getUids(context));

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看我的考核数据", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    MyProgressBarDialogTools.hide();
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        //tvHospitalTask = (TextView) findViewById(R.id.id_tv_hospital_task_ranking);
        //tvHospitalExam = (TextView) findViewById(R.id.id_tv_hospital_exam_ranking);
        title_name.setText("我的考核");
        title_name.setTextSize(20);
    }

    private void initData() {
        mPieChart1.setVisibility(View.VISIBLE);
        mPieChart2.setVisibility(View.VISIBLE);
        //初始化饼状图
        initChart(mPieChart1);
        initChart(mPieChart2);
        //设置饼状图1数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(Integer.parseInt(suc_num), suc_num));//已完成
        entries.add(new PieEntry(Integer.parseInt(fail_num), fail_num));//未完成
        setData1(mPieChart1, entries);
        //设置饼状图2数据
        ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
        entries2.add(new PieEntry(Integer.parseInt(formal_do_total), formal_do_total));//考试通过
        entries2.add(new PieEntry(Integer.parseInt(makeup_do_total), makeup_do_total));//补考通过
        entries2.add(new PieEntry(Integer.parseInt(fail_exam_total), fail_exam_total));//未参考
        setData2(mPieChart2, entries2);
        //设置饼状图下方tab
        String[] labels = {"已完成", "未完成"};//标签文本
        customizeLegend(mPieChart1, labels, mLengendlayout1);
        String[] labels2 = {"考试通过", "补考通过", "未参考"};//标签文本
        customizeLegend(mPieChart2, labels2, mLengendlayout2);
    }

    private void initChart(PieChart mPieChart) {
        mPieChart.setUsePercentValues(true);//设置显示成比例
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(0, 0, 0, 0);
        //是否显示中间圆盘
        mPieChart.setDrawHoleEnabled(false);
        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);
        //true:饼状图中间可以添加文字
        mPieChart.setDrawCenterText(false);
        // 触摸旋转
        mPieChart.setRotationEnabled(false); // no可以手动旋转
        //点击高亮显示
        mPieChart.setHighlightPerTapEnabled(true);//不可以点击
        //设置初始旋转角度
//        mPieChart.setRotationAngle(180);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // 输入标签样式
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);
    }

    /**
     * 定制图例，通过代码生成布局
     * Legend legend = mPieChart.getLegend();
     * LegendEntry[] legendEntries = legend.getEntries();
     * legendEntries[0].label = "aa";
     * legendEntries[1].label = "aa";
     * legend.setCustom(Arrays.asList(legendEntries));
     */
    private void customizeLegend(PieChart mPieChart, String[] labels, LinearLayout mLengendlayout) {
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
        colors = legend.getColors();
//        labels = legend.getLabels();
        for (int i = 0; i < labels.length; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = 1;//设置比重为1
            LinearLayout layout = new LinearLayout(this);//单个图例的布局
            layout.setOrientation(LinearLayout.HORIZONTAL);//水平排列
            layout.setGravity(Gravity.CENTER_VERTICAL);//垂直居中
            layout.setLayoutParams(lp);
            //添加color
            LinearLayout.LayoutParams colorLP = new LinearLayout.LayoutParams(20, 20);
            colorLP.setMargins(0, 0, 20, 0);
            LinearLayout colorLayout = new LinearLayout(this);
            colorLayout.setLayoutParams(colorLP);
            colorLayout.setBackgroundColor(colors[i]);
            layout.addView(colorLayout);
            //添加label
            TextView labelTV = new TextView(this);
            labelTV.setText(labels[i] + " ");
            layout.addView(labelTV);
            //添加data
//            TextView dataTV = new TextView(this);
//            dataTV.setText(datas[i] + "");
//            layout.addView(dataTV);
            mLengendlayout.addView(layout);//legendLayout为外层布局即整个图例布局，是在xml文件中定义

        }
    }

    //设置数据
    private void setData1(PieChart mPieChart, ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setDrawValues(false);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.argb(0xff, 0x37, 0x98, 0xf9));
        colors.add(Color.argb(0xff, 0xf8, 0x93, 0x37));
        dataSet.setColors(colors);
        //设置个饼状图之间的距离
        dataSet.setSliceSpace(2f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    //设置数据
    private void setData2(PieChart mPieChart, ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setDrawValues(false);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.argb(0xff, 0x37, 0x98, 0xf9));
        colors.add(Color.argb(0xff, 0xfa, 0x59, 0x5a));
        colors.add(Color.argb(0xff, 0xf8, 0x93, 0x37));

        dataSet.setColors(colors);
        //设置个饼状图之间的距离
        dataSet.setSliceSpace(2f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    public void back(View view) {
        finish();
    }
}
