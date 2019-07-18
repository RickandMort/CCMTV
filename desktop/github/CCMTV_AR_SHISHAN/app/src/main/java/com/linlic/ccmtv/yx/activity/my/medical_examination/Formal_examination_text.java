package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ZoomImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/9/7.
 */
public class Formal_examination_text extends BaseActivity {

    private Context context;
    private ListView examination_instructions_list;
    private TextView examination_instructions_time, examination_instructions_name, examination_instructions, total_score_of_examination, examination_instructions_id, schedule_text;
    private TextView examination_instructions_buttpm, check_the_answer_card;
    BaseListAdapter baseListAdapter;
    Examination_paper examination_paper;
    private LinearLayout examination_instructions_timing, formal_examination_top, formal_examination_butten;
    private ZoomImageView matrixImageView;
    private LinearLayout imageLayout;
    private String aid, tid, pptid;
    private String type;
    private String my_exams_id = "";
    int minss = 0;
    int ss = 0;
    public volatile boolean exit = false;
    Thread thread = new Thread(new ThreadShow());

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功

                            examination_paper.setExamination_paper(jsonObjects);
                            examination_paper.setEid(tid);
                            /*解析考试其他信息start*/
                            /*考试提交ID*/
                            examination_instructions_id.setText(examination_paper.getEid());
                            /*考试名称*/
//                           String str="默认颜色<br/><font color='#FF0000'>红颜色</font>";

                            examination_instructions_name.setText(examination_paper.getExamination_instructions_name());
//                            /*题目数量*/
//                            schedule_text.setText(0+"/"+examination_paper.getNumber_of_topics());
                            /*考试介绍*/
//                            examination_instructions.setText(examination_instructions.getText()+examination_paper.getExamination_instructions());
                            /*考试总分*/
//                           total_score_of_examination.setText(total_score_of_examination.getText()+examination_paper.getTotal_score_of_examination());
                            /*解析考试其他信息end*/

                            if (jsonObjects.getInt("time") > 0) {
                                ss = jsonObjects.getInt("time");
                                thread.start();
                            }


                            examination_instructions_buttpm.setClickable(true);
                             /*解析题型列表end*/
                        } else {
                            examination_instructions_buttpm.setClickable(false);
                            Toast.makeText(Formal_examination_text.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            Toast.makeText(Formal_examination_text.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, Test_End.class);
                            intent.putExtra("answer_yes_num", jsonObjects.getString("answer_yes_num") + "个");
                            intent.putExtra("is_yes_no", jsonObjects.getString("is_yes_no"));
                            intent.putExtra("task_qualified", jsonObjects.getString("task_qualified"));
                            intent.putExtra("user_qualified", jsonObjects.getString("user_qualified"));
                            intent.putExtra("tid", tid);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Formal_examination_text.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                        baseListAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:

                    if (!exit) {
                        if (minss > 0) {
                            minss--;
                            examination_instructions_time.setText((ss > 9 ? ss : "0" + ss) + ":" + (minss > 9 ? minss : "0" + minss));
                        } else if (minss == 0) {
                            //秒为0时 判断是否还有分
                            if (ss > 0) {
                                //有分
                                ss--;
                                minss = 60;
                                examination_instructions_time.setText((ss > 9 ? ss : "0" + ss) + ":" + (minss > 9 ? minss : "0" + minss));
                            } else {
                                //无分
                                exit = true;
                                examination_instructions_time.setText("00:00");
                                examination_instructions_timing.setVisibility(View.VISIBLE);
                                examination_instructions_buttpm.setVisibility(View.GONE);
                                examination_script1(examination_instructions_buttpm);

                            }
                        }
                    }

                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.formal_examination);
        context = this;
        findId();
        initdata();
        setValue2();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(Formal_examination_text.this).setTitle("系统提示")//设置对话框标题

                .setMessage("您还未提交考卷，是否交卷？")//设置显示的内容

                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        // TODO Auto-generated method stub
                        examination_script1(examination_instructions_buttpm);

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件


            }

        }).show();//在按键响应事件中显示此对话框

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        super.onPause();
    }

    @Override
    public void back(View view) {
        new AlertDialog.Builder(Formal_examination_text.this).setTitle("系统提示")//设置对话框标题

                .setMessage("您还未提交考卷，是否交卷？")//设置显示的内容

                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        // TODO Auto-generated method stub
                        examination_script1(examination_instructions_buttpm);

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件


            }

        }).show();//在按键响应事件中显示此对话框

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit = true;
    }

    // 线程类
    class ThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!exit) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
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
        //增加尾部
        formal_examination_butten = (LinearLayout) View.inflate(this, R.layout.layout_buttn, null);
        examination_instructions_list.addFooterView(formal_examination_butten);

        examination_instructions_name = (TextView) findViewById(R.id.examination_instructions_name);
        check_the_answer_card = (TextView) findViewById(R.id.check_the_answer_card);
        examination_instructions_time = (TextView) findViewById(R.id.examination_instructions_time);
//        total_score_of_examination = (TextView) findViewById(R.id.total_score_of_examination);
        examination_instructions_id = (TextView) findViewById(R.id.examination_instructions_id);
        schedule_text = (TextView) findViewById(R.id.schedule_text);
        examination_instructions_buttpm = (TextView) findViewById(R.id.examination_instructions_buttpm);
        examination_instructions_timing = (LinearLayout) findViewById(R.id.examination_instructions_timing);
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        matrixImageView = (ZoomImageView) findViewById(R.id.matrixImageView);
    }

    /*进入考试*/
    public void examination_script(View view) {

        new AlertDialog.Builder(Formal_examination_text.this).setTitle("系统提示")//设置对话框标题

                .setMessage("是否交卷？")//设置显示的内容

                .setPositiveButton("交卷", new DialogInterface.OnClickListener() {//添加确定按钮


                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                        // TODO Auto-generated method stub
//                        finish();
                        examination_script1(examination_instructions_buttpm);

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮

            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件


            }

        }).show();//在按键响应事件中显示此对话框
    }

    /*交卷*/
    public void examination_script1(View view) {
        MyProgressBarDialogTools.show(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    if (type.equals("ppt")) {
                        obj.put("act", URLConfig.postPptPunAnswer);
                    } else {
                        obj.put("act", URLConfig.postAnswer);
                    }
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("tid", tid);
                    obj.put("pptid", pptid);
                    JSONObject answer = new JSONObject();
                    for (Problem problem_text : examination_paper.getProblems()) {
                        String str = "";
                        switch (problem_text.getQuestion_type()) {
                            case "1":
                                for (Option option_text : problem_text.getOptions()) {
                                    if (option_text.getOption_type().equals("1")) {
                                        str = option_text.getOption_text();
                                    }
                                }
                                break;
                            case "2":
                                for (Option option_text : problem_text.getOptions()) {
                                    if (option_text.getOption_type().equals("1")) {
                                        if (str.length() > 0) {
                                            str += "$$" + option_text.getOption_text();
                                        } else {
                                            str = option_text.getOption_text();
                                        }
                                    }
                                }
                                break;
                            case "3":
                                for (Option option_text : problem_text.getOptions()) {
                                    if (option_text.getOption_type().equals("1")) {
                                        str = option_text.getOption_text();
                                    }
                                }
                                break;
                            case "4":
                                for (String text : problem_text.getAnswerList()) {
                                    if (text.length() > 0) {
                                        if (str.length() > 0) {
                                            str += "$$" + text;
                                        } else {
                                            str = text;
                                        }
                                    }
                                }
                                break;
                            case "5":
                                str = problem_text.getUser_answer();
                                break;
                            case "6":
                                str = problem_text.getUser_answer();
                                break;
                            case "7":
                                for (Option option_text : problem_text.getOptions()) {
                                    if (option_text.getOption_type().equals("1")) {
                                        str = option_text.getOption_text();
                                    }
                                }
                                break;
                            case "8":
                                for (Option option_text : problem_text.getOptions()) {
                                    if (option_text.getOption_type().equals("1")) {
                                        str = option_text.getOption_text();
                                    }
                                }
                                break;
                            case "9":
                                str = problem_text.getUser_answer();
                                break;
                            case "10":
                                str = problem_text.getUser_answer();
                                break;
                            case "11":
                                str = problem_text.getUser_answer();
                                break;
                            default:
                                break;
                        }

                        answer.put(problem_text.getTitle_serial_number(), str + "");
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
            case 2:
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
        if (Integer.parseInt(textView3.getText().toString()) == 2 && textView2.getCurrentTextColor() == vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4)) {

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

        if (Integer.parseInt(textView3.getText().toString()) == 2) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < parentLayout.getChildCount(); i++) {
                if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                    if (((TextView) jLayout.getChildAt(1)).getCurrentTextColor() == vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4)) {
                        String str = ((TextView) jLayout.getChildAt(1)).getText().toString();
                        if (str.indexOf("、") == 1) {
                            list.add(str.substring(2, str.length()));
                        } else {
                            list.add(str);
                        }

                    }
                }
            }

            if (list.size() > 0) {
                if (list.size() > 1) {
                    String str = list.get(0);
                    for (int i = 1; i < list.size(); i++) {
                        str += "$$" + list.get(i);
                    }
                    MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), str);
                } else {
                    MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), list.get(0));
                }
            }

        } else {
            String str = textView2.getText().toString();
            if (str.indexOf("、") == 1) {
                MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), str.substring(2, str.length()));
            } else {
                MyDbUtils.saveExamination_script(context, examination_instructions_id.getText().toString(), view.getTag().toString(), str);
            }

        }


    }


    public void initdata() {
        super.setActivity_title_name("测试");
        aid = getIntent().getStringExtra("aid");
        tid = getIntent().getStringExtra("tid");
        type = getIntent().getStringExtra("type");
        pptid = getIntent().getStringExtra("pptid");
        examination_paper = new Examination_paper();
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        check_the_answer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/5/17 构建一个popupwindow的布局
                View popupView = Formal_examination_text.this.getLayoutInflater().inflate(R.layout.answer_card_layout2, null);
                WindowManager wm = Formal_examination_text.this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();

                // TODO: 2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
                MyGridView lsvMore = (MyGridView) popupView.findViewById(R.id.button_gridview);
                MyGridAdapter2 myGridAdapter = new MyGridAdapter2(context, examination_paper.getTitle_num(), examination_paper.getStatus());
                lsvMore.setAdapter(myGridAdapter);
                // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
                final PopupWindow window = new PopupWindow(popupView, width - 10, height / 2);
                lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                            long arg3) {
                        TextView textView = (TextView) view.findViewById(R.id.tv_item);
//                        textView.getText().toString() 位置
//                        Log.e("文本", textView.getText().toString());
//                        Log.e("文本", examination_paper.getPosition().get(textView.getText().toString()).toString());

                        examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(textView.getText().toString()).toString()));

                        if (window.isShowing()) {
                            window.dismiss();
                        }
                    }
                });
                myGridAdapter.notifyDataSetChanged();
                // TODO: 2016/5/17 设置动画
//                window.setAnimationStyle(R.style.popup_window_anim);
                // TODO: 2016/5/17 设置背景颜色
                window.setBackgroundDrawable(getResources().getDrawable(R.mipmap.answer_card_icon1));
                // TODO: 2016/5/17 设置可以获取焦点
                window.setFocusable(true);
                // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(false);
                // TODO：更新popupwindow的状态
                window.update();
                // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(check_the_answer_card, 0, 20);
            }
        });

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
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    switch (i) {
                                        case 0:
//                                            helper.setText(R.id.radio_id01,problem.getOptions().get(0).getOption_id());
                                            helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_01, problem.getTitle_serial_number());
                                            helper.setTag(R.id.radio_text01, "0");
                                            helper.setTag(R.id.radio_id01, problem.getPosition() + "");
                                            helper.setText(R.id.radio_type01, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text01, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.radios_layout_02, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 1:
//                                            helper.setText(R.id.radio_id02,problem.getOptions().get(1).getOption_id());
                                            helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_02, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type02, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id02, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text02, "1");
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text02, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 2:
//                                            helper.setText(R.id.radio_id03,problem.getOptions().get(2).getOption_id());
                                            helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_03, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type03, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id03, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text03, "2");
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text03, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 3:
//                                            helper.setText(R.id.radio_id04, problem.getOptions().get(3).getOption_id());
                                            helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_04, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type04, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id04, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text04, "3");
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text04, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 4:
//                                            helper.setText(R.id.radio_id05,problem.getOptions().get(4).getOption_id());
                                            helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_05, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type05, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id05, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text05, "4");
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text05, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 5:
//                                            helper.setText(R.id.radio_id06, problem.getOptions().get(5).getOption_id());
                                            helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_06, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type06, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id06, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text06, "5");
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text06, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 6:
//                                            helper.setText(R.id.radio_id07, problem.getOptions().get(6).getOption_id());
                                            helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_07, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type07, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id07, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text07, "6");
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text07, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 7:
//                                            helper.setText(R.id.radio_id08,problem.getOptions().get(7).getOption_id());
                                            helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_08, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type08, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id08, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text08, "7");
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text08, R.color.exams_list_item_text_color4);
                                            }

                                            break;
                                        default:
                                            break;
                                    }

                                }

                            /*开始配置选中事件end*/

                            /*开始配置 图片*/
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
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    switch (i) {
                                        case 0:
//                                            helper.setText(R.id.radio_id01,problem.getOptions().get(0).getOption_id());
                                            helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_01, problem.getTitle_serial_number());
                                            helper.setTag(R.id.radio_text01, "0");
                                            helper.setTag(R.id.radio_id01, problem.getPosition() + "");
                                            helper.setText(R.id.radio_type01, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text01, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.radios_layout_02, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 1:
//                                            helper.setText(R.id.radio_id02,problem.getOptions().get(1).getOption_id());
                                            helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_02, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type02, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id02, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text02, "1");
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text02, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 2:
//                                            helper.setText(R.id.radio_id03,problem.getOptions().get(2).getOption_id());
                                            helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_03, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type03, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id03, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text03, "2");
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text03, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 3:
//                                            helper.setText(R.id.radio_id04, problem.getOptions().get(3).getOption_id());
                                            helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_04, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type04, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id04, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text04, "3");
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text04, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 4:
//                                            helper.setText(R.id.radio_id05,problem.getOptions().get(4).getOption_id());
                                            helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_05, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type05, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id05, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text05, "4");
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text05, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 5:
//                                            helper.setText(R.id.radio_id06, problem.getOptions().get(5).getOption_id());
                                            helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_06, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type06, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id06, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text06, "5");
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text06, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 6:
//                                            helper.setText(R.id.radio_id07, problem.getOptions().get(6).getOption_id());
                                            helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_07, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type07, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id07, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text07, "6");
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text07, R.color.exams_list_item_text_color4);
                                            }
                                            helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                            break;
                                        case 7:
//                                            helper.setText(R.id.radio_id08,problem.getOptions().get(7).getOption_id());
                                            helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                            helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            helper.setTag(R.id.radios_layout_08, problem.getTitle_serial_number());
                                            helper.setText(R.id.radio_type08, problem.getQuestion_type());
                                            helper.setTag(R.id.radio_id08, problem.getPosition() + "");
                                            helper.setTag(R.id.radio_text08, "7");
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.radio_text08, R.color.exams_list_item_text_color4);
                                            }

                                            break;
                                        default:
                                            break;
                                    }

                                }

                            /*开始配置选中事件end*/

                            /*开始配置 图片*/
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
                            try {
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    switch (i) {
                                        case 0:
//                                            helper.setText(R.id.judgment_question_id01,);
                                            helper.setText(R.id.judgment_question_text01, problem.getOptions().get(0).getOption_text(), "html");
                                            helper.setVisibility(R.id.judgment_question_layout_01, View.VISIBLE);
                                            helper.setTag(R.id.judgment_question_layout_01, problem.getTitle_serial_number());
                                            helper.setText(R.id.judgment_question_type01, problem.getQuestion_type());
                                            helper.setTag(R.id.judgment_question_text01, "0");
                                            helper.setTag(R.id.judgment_question_id01, problem.getPosition() + "");
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.exams_list_item_text_color4);
                                            }


                                            break;
                                        case 1:
//                                            helper.setText(R.id.judgment_question_id02,problem.getOptions().get(1).getOption_id());
                                            helper.setText(R.id.judgment_question_text02, problem.getOptions().get(1).getOption_text(), "html");
                                            helper.setVisibility(R.id.judgment_question_layout_02, View.VISIBLE);
                                            helper.setTag(R.id.judgment_question_layout_02, problem.getTitle_serial_number());
                                            helper.setText(R.id.judgment_question_type02, problem.getQuestion_type());
                                            helper.setTag(R.id.judgment_question_text02, "1");
                                            helper.setTag(R.id.judgment_question_id02, problem.getPosition() + "");
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.exams_list_item_text_color4);
                                            }


                                            break;

                                        default:
                                            break;
                                    }

                                }

                            /*开始配置选中事件end*/

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
                                for (int i = 0; i < problem.getOptions().size(); i++) {
                                    switch (i) {
                                        case 0:
//                                            helper.setText(R.id.radio_id01,problem.getOptions().get(0).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_01, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_01, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text01, "0");
                                            helper.setTag(R.id.public_topic_radio_id01, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type01, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text01, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id01, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text01, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_02, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 1:
//                                            helper.setText(R.id.radio_id02,problem.getOptions().get(1).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_02, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_02, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text02, "1");
                                            helper.setTag(R.id.public_topic_radio_id02, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type02, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text02, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id02, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text02, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_03, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 2:
//                                            helper.setText(R.id.radio_id03,problem.getOptions().get(2).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_03, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_03, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text03, "2");
                                            helper.setTag(R.id.public_topic_radio_id03, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type03, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text03, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id03, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text03, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 3:
//                                            helper.setText(R.id.radio_id04, problem.getOptions().get(3).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_04, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_04, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text04, "3");
                                            helper.setTag(R.id.public_topic_radio_id04, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type04, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text04, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id04, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text04, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 4:
//                                            helper.setText(R.id.radio_id05,problem.getOptions().get(4).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_05, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_05, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text05, "4");
                                            helper.setTag(R.id.public_topic_radio_id05, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type05, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text05, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id05, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text05, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 5:
//                                            helper.setText(R.id.radio_id06, problem.getOptions().get(5).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_06, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_06, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text06, "5");
                                            helper.setTag(R.id.public_topic_radio_id06, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type06, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text06, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id06, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text06, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 6:
//                                            helper.setText(R.id.radio_id07, problem.getOptions().get(6).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_07, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_07, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text07, "6");
                                            helper.setTag(R.id.public_topic_radio_id07, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type07, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text07, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id07, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text07, R.color.exams_list_item_text_color4);
                                            }

                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                            break;
                                        case 7:
//                                            helper.setText(R.id.radio_id08,problem.getOptions().get(7).getOption_id());
                                            helper.setText(R.id.public_topic_radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                            helper.setVisibility(R.id.public_topic_radios_layout_08, View.VISIBLE);
                                            helper.setTag(R.id.public_topic_radios_layout_08, problem.getTitle_serial_number());
                                            helper.setTag(R.id.public_topic_radio_text08, "7");
                                            helper.setTag(R.id.public_topic_radio_id08, problem.getPosition() + "");
                                            helper.setText(R.id.public_topic_radio_type08, problem.getQuestion_type());
                                            //设置颜色
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setViewBG(R.id.public_topic_radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.public_topic_radio_text08, R.color.no_select_text_color);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setViewBG(R.id.public_topic_radio_id08, getResources().getDrawable(R.mipmap.exams2_10));
                                                helper.setTextColor(R.id.public_topic_radio_text08, R.color.exams_list_item_text_color4);
                                            }

                                            break;
                                        default:
                                            break;
                                    }
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

                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;

                        case 107://公用案例分析题
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
                        default:
                            break;
                    }
                } else {
                    helper.setViewVisibility(R.id.layout, View.GONE);
                }


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
                    if (type.equals("ppt")) {
                        obj.put("act", URLConfig.getPptTestExam);
                    } else {
                        obj.put("act", URLConfig.getTaskExam);
                    }
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid", aid);
                    obj.put("tid", tid);
                    obj.put("pptid", pptid);

//                    LogUtil.e("考卷", obj.toString());
                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    LogUtil.e("考卷", result);

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

}
