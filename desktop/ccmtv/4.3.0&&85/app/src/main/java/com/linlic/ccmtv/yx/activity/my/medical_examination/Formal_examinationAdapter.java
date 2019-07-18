package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.entry.KeshiData;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.MyFlowLayout;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.ArrayList;
import java.util.List;

/**
 * name：首页列表数据适配器
 * author：Larry
 * data：2017/6/29.
 */
public class Formal_examinationAdapter extends BaseAdapter {
    private Context context;
    private List<Problem> keshiDatas = new ArrayList<>();
    private int screenWidth;
    private List<LinearLayout> radios_layout_list = new ArrayList<>();
    private List<TextView> radio_id = new ArrayList<>();
    private List<TextView> radio_text = new ArrayList<>();
    public Formal_examinationAdapter(Context context, List<Problem> keshiDatas, int screenWidth) {
        this.context = context;
        this.keshiDatas = keshiDatas;
        this.screenWidth =screenWidth;

    }

    public int getCount() {
        return keshiDatas.size();
    }

    public Object getItem(int pos) {
        return keshiDatas.get(pos);
    }

    public long getItemId(int pos) {
        return pos;
    }

    public View getView(final int pos, View view, ViewGroup group) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.formal_examination_list_item
                    , null);
            viewHolder.title_serial_number = (TextView) view.findViewById(R.id.title_serial_number);
            viewHolder.radios = (LinearLayout) view.findViewById(R.id.radios);
            viewHolder.radios_type = (TextView) view.findViewById(R.id.radios_type);
            viewHolder.radios_problem = (TextView) view.findViewById(R.id.radios_problem);
            viewHolder.janes_answer_type = (TextView) view.findViewById(R.id.janes_answer_type);
            viewHolder.janes_answer_type_problem = (TextView) view.findViewById(R.id.janes_answer_type_problem);
            viewHolder.li_blank_type = (TextView) view.findViewById(R.id.li_blank_type);
            viewHolder.janes_answer = (LinearLayout) view.findViewById(R.id.janes_answer);
            viewHolder.janes_answer_editText = (EditText) view.findViewById(R.id.janes_answer_editText);
            viewHolder.li_blank = (LinearLayout) view.findViewById(R.id.li_blank);
//            viewHolder.flowLayout = (MyFlowLayout) view.findViewById(R.id.flowLayout);
            viewHolder.radios_layout_01 = (LinearLayout) view.findViewById(R.id.radios_layout_01);
            viewHolder.radios_layout_02 = (LinearLayout) view.findViewById(R.id.radios_layout_02);
            viewHolder.radios_layout_03 = (LinearLayout) view.findViewById(R.id.radios_layout_03);
            viewHolder.radios_layout_04 = (LinearLayout) view.findViewById(R.id.radios_layout_04);
            viewHolder.radios_layout_05 = (LinearLayout) view.findViewById(R.id.radios_layout_05);
            viewHolder.radios_layout_06 = (LinearLayout) view.findViewById(R.id.radios_layout_06);
            viewHolder.radios_layout_07 = (LinearLayout) view.findViewById(R.id.radios_layout_07);
            viewHolder.radios_layout_08 = (LinearLayout) view.findViewById(R.id.radios_layout_08);
            radios_layout_list.add(viewHolder.radios_layout_01);
            radios_layout_list.add(viewHolder.radios_layout_02);
            radios_layout_list.add(viewHolder.radios_layout_03);
            radios_layout_list.add(viewHolder.radios_layout_04);
            radios_layout_list.add(viewHolder.radios_layout_05);
            radios_layout_list.add(viewHolder.radios_layout_06);
            radios_layout_list.add(viewHolder.radios_layout_07);
            radios_layout_list.add(viewHolder.radios_layout_08);

            viewHolder.radio_id01 = (TextView) view.findViewById(R.id.radio_id01);
            viewHolder.radio_id02 = (TextView) view.findViewById(R.id.radio_id02);
            viewHolder.radio_id03 = (TextView) view.findViewById(R.id.radio_id03);
            viewHolder.radio_id04 = (TextView) view.findViewById(R.id.radio_id04);
            viewHolder.radio_id05 = (TextView) view.findViewById(R.id.radio_id05);
            viewHolder.radio_id06 = (TextView) view.findViewById(R.id.radio_id06);
            viewHolder.radio_id07 = (TextView) view.findViewById(R.id.radio_id07);
            viewHolder.radio_id08 = (TextView) view.findViewById(R.id.radio_id08);
            radio_id.add(viewHolder.radio_id01);
            radio_id.add(viewHolder.radio_id02);
            radio_id.add(viewHolder.radio_id03);
            radio_id.add(viewHolder.radio_id04);
            radio_id.add(viewHolder.radio_id05);
            radio_id.add(viewHolder.radio_id06);
            radio_id.add(viewHolder.radio_id07);
            radio_id.add(viewHolder.radio_id08);

            viewHolder.radio_text01 = (TextView) view.findViewById(R.id.radio_text01);
            viewHolder.radio_text02 = (TextView) view.findViewById(R.id.radio_text02);
            viewHolder.radio_text03 = (TextView) view.findViewById(R.id.radio_text03);
            viewHolder.radio_text04 = (TextView) view.findViewById(R.id.radio_text04);
            viewHolder.radio_text05 = (TextView) view.findViewById(R.id.radio_text05);
            viewHolder.radio_text06 = (TextView) view.findViewById(R.id.radio_text06);
            viewHolder.radio_text07 = (TextView) view.findViewById(R.id.radio_text07);
            viewHolder.radio_text08 = (TextView) view.findViewById(R.id.radio_text08);
            radio_text.add(viewHolder.radio_text01 );
            radio_text.add(viewHolder.radio_text02 );
            radio_text.add(viewHolder.radio_text03 );
            radio_text.add(viewHolder.radio_text04 );
            radio_text.add(viewHolder.radio_text05 );
            radio_text.add(viewHolder.radio_text06 );
            radio_text.add(viewHolder.radio_text07 );
            radio_text.add(viewHolder.radio_text08 );


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Problem problem = keshiDatas.get(pos);
        switch (Integer.parseInt(problem.getQuestion_type())){
            case 1://单选
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.VISIBLE);//单选
                viewHolder.janes_answer.setVisibility( View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.radios_type.setText( problem.getQuestion_type_text());
                //设置 题目
                viewHolder.radios_problem.setText( problem.getProblem());

                //循环增加选项
                try {
                        for(int i = 0;i<problem.getOptions().size();i++){
                            radio_text.get(i).setText(problem.getOptions().get(i).getOption_text());
                            radio_id.get(i).setText(problem.getOptions().get(i).getOption_id());
                            radios_layout_list.get(i).setVisibility(View.VISIBLE);
                        }

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 2://复选框
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.VISIBLE);//单选
                viewHolder.janes_answer.setVisibility(View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.radios_type.setText( problem.getQuestion_type_text());
                //设置 题目
                viewHolder.radios_problem.setText( problem.getProblem());

                //循环增加选项
                try {
                    for(int i = 0;i<problem.getOptions().size();i++){
                        radio_text.get(i).setText(problem.getOptions().get(i).getOption_text());
                        radio_id.get(i).setText(problem.getOptions().get(i).getOption_id());
                        radios_layout_list.get(i).setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 3://判断题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.VISIBLE);//单选
                viewHolder.janes_answer.setVisibility(View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.radios_type.setText( problem.getQuestion_type_text());
                //设置 题目
                viewHolder.radios_problem.setText( problem.getProblem());

                //循环增加选项
                try {
                    for(int i = 0;i<problem.getOptions().size();i++){
                        radio_text.get(i).setText(problem.getOptions().get(i).getOption_text());
                        radio_id.get(i).setText(problem.getOptions().get(i).getOption_id());
                        radios_layout_list.get(i).setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 4://填空题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.GONE);//单选
                viewHolder.janes_answer.setVisibility(View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.VISIBLE);//填空题
                //设置题目序号 title_serial_number
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.li_blank_type.setText(problem.getQuestion_type_text());
                //设置 题目
                String str = problem.getProblem();
                String[] tags = str.split("空");
                for (int i = 0; i < tags.length; i++) {

                    if (i%2 ==0 && i!=0) {
                        EditText ed = new EditText(context);
                        ed.setTextColor(Color.BLACK);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(0, -45, 0, 0);
                        ed.setLayoutParams(params2);
                        ed.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        ed.setMaxLines(1);
                        viewHolder.flowLayout.addView(ed);

                        TextView tv = new TextView(context);
                        tv.setText(tags[i]);
                        tv.setTextColor(Color.BLACK);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        tv.setLayoutParams(params);
                        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        tv.setMaxWidth(screenWidth - 20);//这句话是为了限制过长的内容顶出屏幕而设置的

                        viewHolder.flowLayout.addView(tv);
                    } else {
                        TextView tv = new TextView(context);
                        tv.setText(tags[i]);
                        tv.setTextColor(Color.BLACK);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        tv.setLayoutParams(params);
                        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        tv.setMaxWidth(screenWidth - 20);//这句话是为了限制过长的内容顶出屏幕而设置的
                        viewHolder.flowLayout.addView(tv);
                    }
                }
                break;
            case 5://名词解释
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.GONE);//单选
                viewHolder.janes_answer.setVisibility(View.VISIBLE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.janes_answer_type.setText(problem.getQuestion_type_text());
                //设置 题目
                viewHolder.janes_answer_type_problem.setText(problem.getProblem());

                break;
            case 6://案例分析题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.GONE);//单选
                viewHolder.janes_answer.setVisibility(View.VISIBLE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.janes_answer_type.setText(problem.getQuestion_type_text());
                //设置 题目
                viewHolder.janes_answer_type_problem.setText(problem.getProblem());
                break;
            case 7://共用题干题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.VISIBLE);//单选
                viewHolder.janes_answer.setVisibility(View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.radios_type.setText( problem.getQuestion_type_text());
                //设置 题目
                viewHolder.radios_problem.setText( problem.getProblem());

                //循环增加选项
                try {
                    for(int i = 0;i<problem.getOptions().size();i++){
                        radio_text.get(i).setText(problem.getOptions().get(i).getOption_text());
                        radio_id.get(i).setText(problem.getOptions().get(i).getOption_id());
                        radios_layout_list.get(i).setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 8:
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.VISIBLE);//单选
                viewHolder.janes_answer.setVisibility(View.GONE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.radios_type.setText( problem.getQuestion_type_text());
                //设置 题目
                viewHolder.radios_problem.setText( problem.getProblem());

                //循环增加选项
                try {
                    for(int i = 0;i<problem.getOptions().size();i++){
                        radio_text.get(i).setText(problem.getOptions().get(i).getOption_text());
                        radio_id.get(i).setText(problem.getOptions().get(i).getOption_id());
                        radios_layout_list.get(i).setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 9://问答题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.GONE);//单选
                viewHolder.janes_answer.setVisibility(View.VISIBLE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.janes_answer_type.setText(problem.getQuestion_type_text());
                //设置 题目
                viewHolder.janes_answer_type_problem.setText(problem.getProblem());
                break;
            case 10://简答题
                //控制显示 隐藏
                viewHolder.radios.setVisibility(View.GONE);//单选
                viewHolder.janes_answer.setVisibility(View.VISIBLE);//简答题
                viewHolder.li_blank.setVisibility(View.GONE);//填空题
                viewHolder.title_serial_number.setText(problem.getTitle_serial_number());
                //设置  题型描述
                viewHolder.janes_answer_type.setText(problem.getQuestion_type_text());
                //设置 题目
                viewHolder.janes_answer_type_problem.setText(problem.getProblem());
                break;

            default:
                break;
        }


        return view;
    }

    final static class ViewHolder {
        TextView title_serial_number;/*考题编号*/
        LinearLayout radios;/*单选 多选*/
        TextView radios_type;/*题目类型Text*/
        TextView radios_problem;/*题目*/
        LinearLayout radios_layout_01;
        LinearLayout radios_layout_02;
        LinearLayout radios_layout_03;
        LinearLayout radios_layout_04;
        LinearLayout radios_layout_05;
        LinearLayout radios_layout_06;
        LinearLayout radios_layout_07;
        LinearLayout radios_layout_08;
        TextView radio_id01;
        TextView radio_id02;
        TextView radio_id03;
        TextView radio_id04;
        TextView radio_id05;
        TextView radio_id06;
        TextView radio_id07;
        TextView radio_id08;
        TextView radio_text01;
        TextView radio_text02;
        TextView radio_text03;
        TextView radio_text04;
        TextView radio_text05;
        TextView radio_text06;
        TextView radio_text07;
        TextView radio_text08;


        LinearLayout janes_answer;/*简答题*/
        TextView janes_answer_type;/*简答题 类型*/
        TextView janes_answer_type_problem;/*简答题 题目*/
        EditText janes_answer_editText;/*简答题 编辑框*/
        LinearLayout li_blank;/*填空题*/
        TextView li_blank_type;/*填空题 类型*/
        MyFlowLayout flowLayout;/*填空题 题目*/

    }

    public void getVideoRulest(View v, int aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(v.getContext(), VideoFive.class);
        intent.putExtra("aid", aid + "");
        v.getContext().startActivity(intent);
    }
}
