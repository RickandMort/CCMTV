package com.linlic.ccmtv.yx.activity.user_statistics.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.linlic.ccmtv.yx.R;

import java.util.Calendar;
import java.util.List;

/**
 * 图表工具
 * Created by yangle on 2016/11/29.
 */
public class ChartUtils {

    public static int dayValue = 0;
    public static int weekValue = 1;
    public static int monthValue = 2;

    public static Context mContext;

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart) {
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-15);
        chart.setExtraBottomOffset(15);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(true);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(15);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(10);
        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.parseColor("#00000000"));
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setAxisMinimum(0);

        //Matrix matrix = new Matrix();
        // x轴缩放1.5倍
        //matrix.postScale(1.5f, 1f);
        // 在图表动画显示之前进行缩放
        //chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);
        chart.invalidate();
        return chart;
    }

    /**
     * 初始化图表
     *
     * @param chart 原始图表
     * @return 初始化后的图表
     */
    public static LineChart initChart(LineChart chart,Context context) {
        mContext=context;
        // 不显示数据描述
        chart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        chart.setNoDataText("暂无数据");
        // 不显示表格颜色
        chart.setDrawGridBackground(false);
        // 不可以缩放
        chart.setScaleEnabled(false);
        // 不显示y轴右边的值
        chart.getAxisRight().setEnabled(false);
        // 不显示图例
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        // 向左偏移15dp，抵消y轴向右偏移的30dp
        chart.setExtraLeftOffset(-30);
        chart.setExtraBottomOffset(15);

        XAxis xAxis = chart.getXAxis();
        // 不显示x轴
        xAxis.setDrawAxisLine(false);
        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(11);
        xAxis.setGridColor(Color.parseColor("#30FFFFFF"));
        // 设置x轴数据偏移量
        xAxis.setYOffset(20);
        YAxis yAxis = chart.getAxisLeft();
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.parseColor("#00000000"));
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        yAxis.setXOffset(30);
        yAxis.setAxisMinimum(0);

        //Matrix matrix = new Matrix();
        // x轴缩放1.5倍
        //matrix.postScale(1.5f, 1f);
        // 在图表动画显示之前进行缩放
        //chart.getViewPortHandler().refresh(matrix, chart, false);
        // x轴执行动画
        //chart.animateX(2000);
        chart.invalidate();
        return chart;
    }

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    /*public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, "");
            // 设置曲线颜色
            lineDataSet.setColor(Color.parseColor("#FFFFFF"));
            // 设置平滑曲线
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // 不显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            lineDataSet.setCircleColorHole(Color.WHITE);
            lineDataSet.setColor(Color.argb(0xff,0xff,0xff,0xff));// 显示颜色
            lineDataSet.setCircleRadius(5f);//显示的圆环大小
            lineDataSet.setCircleColor(Color.argb(0x66,0xff,0xff,0xff));
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(true);
            lineDataSet.setValueTextColor(Color.WHITE);
            lineDataSet.setValueTextSize(13f);
            // 不显示定位线
            lineDataSet.setHighlightEnabled(false);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }*/

    /**
     * 设置图表数据
     *
     * @param chart  图表
     * @param values 数据
     */
    public static void setChartData(LineChart chart, List<Entry> values) {
        LineDataSet lineDataSet;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(values, "");
            // 设置曲线颜色
            lineDataSet.setColors(new int[] {R.color.chart_line1,R.color.chart_line2,R.color.chart_line3,R.color.chart_line4,R.color.chart_line5,R.color.chart_line6},mContext);
            /*lineDataSet.setColors(new int[] {mContext.getResources().getColor(R.color.chart_line1),mContext.getResources().getColor(R.color.chart_line2),
                    mContext.getResources().getColor(R.color.chart_line3),mContext.getResources().getColor(R.color.chart_line4),
                    mContext.getResources().getColor(R.color.chart_line5),mContext.getResources().getColor(R.color.chart_line6),},mContext);*/
            //lineDataSet.setColor(ContextCompat.getColor(mContext,R.drawable.study_record_shape_gradient));
            lineDataSet.setLineWidth(1.5f);
            // 设置平滑曲线
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            // 不显示坐标点的小圆点
            lineDataSet.setDrawCircles(true);
            lineDataSet.setCircleColorHole(Color.WHITE);
            lineDataSet.setCircleHoleRadius(3f);
            //lineDataSet.setColor(Color.argb(0xff,0xff,0xff,0xff));// 显示颜色
            lineDataSet.setCircleRadius(6f);//显示的圆环大小
            lineDataSet.setCircleColor(Color.parseColor("#FF86C9FE"));
            // 不显示坐标点的数据
            lineDataSet.setDrawValues(true);
            lineDataSet.setValueTextColor(Color.parseColor("#3897f9"));
            lineDataSet.setValueTextSize(13f);
            // 不显示定位线
            lineDataSet.setHighlightEnabled(false);

            LineData data = new LineData(lineDataSet);
            chart.setData(data);
            chart.invalidate();
        }
    }

    /**
     * 更新图表
     *
     * @param chart     图表
     * @param values    数据
     * @param valueType 数据类型
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
                                            final int valueType) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String[] week = {"5/12", "5/13", "5/14", "5/15", "5/16", "5/17", "5/18"};
                return week[(int) value];
//                return xValuesProcess[(int) value];
            }
        });

        chart.invalidate();
        setChartData(chart, values);
    }

    /**
     * 更新图表X轴
     *
     * @param chart     图表
     * @param values    数据
     * @param
     */
    public static void notifyDataSetChanged(LineChart chart, List<Entry> values,
                                            final String[] dateX) {
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //String[] week = {"5/12", "5/13", "5/14", "5/15", "5/16", "5/17", "5/18"};
                return dateX[(int) value];
//                return xValuesProcess[(int) value];
            }
        });

        chart.invalidate();
        setChartData(chart, values);
    }

    /**
     * x轴数据处理
     *
     * @param valueType 数据类型
     * @return x轴数据
     */
    private static String[] xValuesProcess(int valueType) {
        String[] week = {"一", "二", "三", "四", "五", "六", "日"};

        if (valueType == dayValue) { // 今日
            String[] dayValues = new String[7];
            long currentTime = System.currentTimeMillis();
            for (int i = 6; i >= 0; i--) {
                dayValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_day);
                currentTime -= (3 * 60 * 60 * 1000);
            }
            return dayValues;

        } else if (valueType == weekValue) { // 本周
            String[] weekValues = new String[7];
            Calendar calendar = Calendar.getInstance();
            int currentWeek = calendar.get(Calendar.DAY_OF_WEEK);

            for (int i = 6; i >= 0; i--) {
                weekValues[i] = week[currentWeek - 1];
                if (currentWeek == 1) {
                    currentWeek = 7;
                } else {
                    currentWeek -= 1;
                }
            }
            return weekValues;

        } else if (valueType == monthValue) { // 本月
            String[] monthValues = new String[7];
            long currentTime = System.currentTimeMillis();
            for (int i = 6; i >= 0; i--) {
                monthValues[i] = TimeUtils.dateToString(currentTime, TimeUtils.dateFormat_month);
                currentTime -= (4 * 24 * 60 * 60 * 1000);
            }
            return monthValues;
        }
        return new String[]{};
    }
}
