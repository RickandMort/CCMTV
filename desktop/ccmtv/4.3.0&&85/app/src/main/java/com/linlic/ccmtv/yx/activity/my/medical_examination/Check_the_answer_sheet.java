package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Examination_script;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.adapter.GroupedListAdapter;
import com.linlic.ccmtv.yx.adapter.GroupedListAdapter2;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.ZoomImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/7.
 */
public class Check_the_answer_sheet extends BaseActivity {

    private Context context;
    public static String  pid;
    private ListView examination_instructions_list;
    private TextView examination_instructions_name, check_the_answer_card, examination_instructions, total_score_of_examination, examination_instructions_id,curr_num_text;
    private LinearLayout arrow_back2;
    private GroupedListAdapter2 adapter;
    BaseListAdapter baseListAdapter;
    Examination_paper examination_paper;
    private String my_exams_id = "";
    private String my_exams_eid = "";
    private String is_mock_exam = "0";
    private LinearLayout imageLayout,examination_instructions_timing3;
    private ZoomImageView matrixImageView;
    public List<String> img_text;
    public List<Integer> imgs;
    public Map<String,Integer> map  = new HashMap<>();
    private RecyclerView lsvMore;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
//                        LogUtil.e("题型", msg.obj.toString());
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            examination_paper.setExamination_paper(jsonObjects);
                            /*解析考试其他信息start*/
                            examination_instructions_name.setText(jsonObjects.getString("paper_name"));
                             /*解析题型列表end*/
                        } else {
                            Toast.makeText(Check_the_answer_sheet.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
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
                            Toast.makeText(Check_the_answer_sheet.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Check_the_answer_sheet.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                img_text.add(customJson.getString("num"));
                                map.put(customJson.getString("num"),customJson.getInt("status"));
                               /* switch (customJson.getInt("status")) {
                                    case 1:
                                        imgs.add(1);//正确
                                        break;
                                    case 2:
                                        imgs.add(2);//错误
                                        break;
                                    case 3:
                                        imgs.add(3);//主观题
                                        break;
                                    default:
                                        break;
                                }*/
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_the_answer_sheet);
        context = this;
        findId();
        is_mock_exam = getIntent().getStringExtra("is_mock_exam");
        initdata();
        setValue2();
        setValue3();
    }

    @Override
    public void findId() {
        super.findId();
        examination_instructions_list = (ListView) findViewById(R.id.examination_instructions_list);
        examination_instructions_name = (TextView) findViewById(R.id.examination_instructions_name);
        examination_instructions = (TextView) findViewById(R.id.examination_instructions);
        check_the_answer_card = (TextView) findViewById(R.id.check_the_answer_card);
        total_score_of_examination = (TextView) findViewById(R.id.total_score_of_examination);
        examination_instructions_id = (TextView) findViewById(R.id.examination_instructions_id);
        matrixImageView = (ZoomImageView) findViewById(R.id.matrixImageView);
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);

        lsvMore = (RecyclerView) findViewById(R.id.rv_list);
        curr_num_text = (TextView) findViewById(R.id.curr_num_text);
        examination_instructions_timing3 = (LinearLayout) findViewById(R.id.examination_instructions_timing3);
        arrow_back2 = (LinearLayout) findViewById(R.id.arrow_back2);
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

    /*进入考试*/
    public void examination_script(View view) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.handPaper);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", examination_paper.getEid());
                    JSONObject answer = new JSONObject();
                    for (Examination_script examination_script : MyDbUtils.findExamination_script_All(context, examination_instructions_id.getText().toString())) {
                        answer.put(examination_script.getOption_id(), examination_script.getOption_name());
                    }
                    obj.put("answer", answer);

                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());

//                    MyProgressBarDialogTools.hide();
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

    /**
     * scroll Vertical
     *
     * @param
     * @param y 垂直滑动的距离
     */
    public void scrollVertical(final ListView listView, Activity activity, final int y) {
        if (listView == null)
            return;
        Check_the_answer_sheet.this.runOnUiThread(new Runnable() { //执行自动化测试的时候模拟滑动需要进入UI线程操作
            @Override
            public void run() {
                invokeMethod(listView, "trackMotionScroll", new Object[]{-y, -y}, new Class[]{int.class, int.class});
            }
        });
    }

    /**
     * 遍历当前类以及父类去查找方法，例子，写的比较简单
     *
     * @param object
     * @param methodName
     * @param params
     * @param paramTypes
     * @return
     */
    public Object invokeMethod(Object object, String methodName, Object[] params, Class[] paramTypes) {
        Object returnObj = null;
        if (object == null) {
            return null;
        }
        Class cls = object.getClass();
        Method method = null;
        for (; cls != Object.class; cls = cls.getSuperclass()) { //因为取的是父类的默认修饰符的方法，所以需要循环找到该方法
            try {
                method = cls.getDeclaredMethod(methodName, paramTypes);
                break;
            } catch (NoSuchMethodException e) {
//					e.printStackTrace();
            } catch (SecurityException e) {
//					e.printStackTrace();
            }
        }
        if (method != null) {
            method.setAccessible(true);
            try {
                returnObj = method.invoke(object, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return returnObj;
    }

    public void initdata() {
        pid = getIntent().getStringExtra("pid");
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        my_exams_eid = getIntent().getStringExtra("my_exams_eid");
        examination_paper = new Examination_paper();
        arrow_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOut(examination_instructions_timing3);
//                examination_instructions_timing3.setVisibility(View.GONE);
            }
        });
        img_text = new ArrayList<>();
        imgs = new ArrayList<>();
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        // TODO: 2016/5/17 给按钮设置单击事件监听
        /*初始化答题卡start*/
        lsvMore.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroupedListAdapter2(context,examination_paper.getGroups(),examination_paper,map);
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

                    examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(examination_paper.getGroups().get(groupPosition).getChildren().get(childPosition).getChild()).toString()));


                fadeOut(examination_instructions_timing3);

                if (Integer.parseInt(examination_paper.getPosition().get(examination_paper.getGroups().get(groupPosition).getChildren().get(childPosition).getChild()).toString()) < examination_paper.getProblems().size()){
//                    examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(textView.getText().toString()).toString()));
                    examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(examination_paper.getGroups().get(groupPosition).getChildren().get(childPosition).getChild()).toString()));
                }
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

                curr_num_text.setText("共"+examination_paper.getTitle_num().size()+"题");
            }
        });

//                MyGridAdapter2 myGridAdapter = new MyGridAdapter2(context, examination_paper.getTitle_num(), examination_paper.getStatus());
        lsvMore.setAdapter(adapter);
        //直接使用GroupedGridLayoutManager实现子项的Grid效果
        GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(context, 5, adapter);
        lsvMore.setLayoutManager(gridLayoutManager);

        baseListAdapter = new BaseListAdapter(examination_instructions_list, examination_paper.getProblems(), R.layout.check_the_answer_sheet_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Problem problem = (Problem) item;
                System.out.println("看看罗" + problem.toString());
                switch (Integer.parseInt(problem.getQuestion_type())) {
                    case 1://单选
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//判断题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setVisibility(R.id.radio_correct, View.GONE);
                        helper.setMyGridView(R.id.radios_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.radio_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        //循环增加选项
                        try {
                            for (int i = 0; i < problem.getOptions().size(); i++) {
                                switch (i) {
                                    case 0:
                                        if (problem.getOptions().get(0).getOption_type().length() > 0) {
                                            //0未选中 并且 非正确答案   1 选中 并且 正确答案 2 选中 并且 非正确答案 3 未选中 并且 正确答案
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text01, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            }
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
                                        if (problem.getOptions().get(1).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text02, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 2:
                                        if (problem.getOptions().get(2).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text03, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 3:
                                        if (problem.getOptions().get(3).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text04, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 4:
                                        if (problem.getOptions().get(4).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text05, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 5:
                                        if (problem.getOptions().get(5).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text06, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 6:
                                        if (problem.getOptions().get(6).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text07, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 7:
                                        if (problem.getOptions().get(7).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text08, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            }
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
                    case 2://复选框
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setVisibility(R.id.radio_correct, View.GONE);
                        helper.setMyGridView(R.id.radios_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.radio_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        //循环增加选项
                        try {
                            for (int i = 0; i < problem.getOptions().size(); i++) {
                                switch (i) {
                                    case 0:
                                        if (problem.getOptions().get(0).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text01, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            }
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
                                        if (problem.getOptions().get(1).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text02, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 2:
                                        if (problem.getOptions().get(2).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text03, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 3:
                                        if (problem.getOptions().get(3).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text04, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 4:
                                        if (problem.getOptions().get(4).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text05, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 5:
                                        if (problem.getOptions().get(5).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text06, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 6:
                                        if (problem.getOptions().get(6).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text07, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 7:
                                        if (problem.getOptions().get(7).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text08, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            }
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
                    case 3://判断题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.VISIBLE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.judgment_question_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setMyGridView(R.id.judgment_question_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.judgment_question_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        //循环增加选项
                        try {
                            for (int i = 0; i < problem.getOptions().size(); i++) {
                                switch (i) {
                                    case 0:
                                        if (problem.getOptions().get(0).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setText(R.id.judgment_question_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.judgment_question_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setText(R.id.judgment_question_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.judgment_question_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 2) {
                                                helper.setText(R.id.judgment_question_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.judgment_question_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 3) {
                                                helper.setText(R.id.judgment_question_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.judgment_question_layout_01, View.VISIBLE);
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (problem.getOptions().get(1).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setText(R.id.judgment_question_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.judgment_question_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setText(R.id.judgment_question_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.judgment_question_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 2) {
                                                helper.setText(R.id.judgment_question_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.judgment_question_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 3) {
                                                helper.setText(R.id.judgment_question_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.judgment_question_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.judgment_question_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.judgment_question_layout_02, View.VISIBLE);
                                            }
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
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        //设置  题型描述
                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        //设置您的答案
                        helper.setText(R.id.janes_answer_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.janes_answer_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 5://名词解释
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        //设置您的答案
                        helper.setText(R.id.janes_answer_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.janes_answer_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 6://案例分析题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        //设置您的答案
                        helper.setText(R.id.janes_answer_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.janes_answer_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 7://共用题干题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//简答题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.VISIBLE);//共用题干题
                        //设置 题目 public_topic_type_problem
                        if (problem.getProblem2().length() > 0) {
                            helper.setText(R.id.public_topic_type_problem, problem.getProblem2(), "html");
                            helper.setViewVisibility(R.id.public_topic_type_problem, View.VISIBLE);//简答题
                        } else {
                            helper.setViewVisibility(R.id.public_topic_type_problem, View.GONE);//简答题
                        }
                        helper.setText(R.id.public_topic_type_problem2, problem.getProblem(), "html");
                        helper.setMyGridView(R.id.public_topic_gridview, problem.getThumbnails(), problem.getPictures());
                        //循环增加选项
                        try {
                            for (int i = 0; i < problem.getOptions().size(); i++) {
                                switch (i) {
                                    case 0:
                                        helper.setText(R.id.public_topic_radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_01, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type01, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id01, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text01, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id01, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text01, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id01, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text01, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id01, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text01, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_02, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_03, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 1:
                                        helper.setText(R.id.public_topic_radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_02, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type02, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id02, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text02, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id02, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text02, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id02, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text02, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id02, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text02, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_03, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 2:
                                        helper.setText(R.id.public_topic_radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_03, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type03, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id03, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text03, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id03, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text03, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id03, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text03, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id03, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text03, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_04, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 3:
                                        helper.setText(R.id.public_topic_radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_04, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type04, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id04, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text04, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id04, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text04, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id04, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text04, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id04, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text04, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_05, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 4:
                                        helper.setText(R.id.public_topic_radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_05, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type05, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id05, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text05, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id05, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text05, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id05, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text05, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id05, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text05, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 5:
                                        helper.setText(R.id.public_topic_radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_06, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type06, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id06, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text06, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id06, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text06, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id06, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text06, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id06, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text06, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.GONE);
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 6:
                                        helper.setText(R.id.public_topic_radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_07, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type07, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id07, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text07, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id07, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text07, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id07, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text07, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id07, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text07, R.color.check_the_answer_sheet_item_text);
                                        }
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.GONE);
                                        break;
                                    case 7:
                                        helper.setText(R.id.public_topic_radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                        helper.setViewVisibility(R.id.public_topic_radios_layout_08, View.VISIBLE);
                                        helper.setText(R.id.public_topic_radio_type08, problem.getQuestion_type());
                                        //设置颜色
                                        if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id08, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text08, R.color.no_select_text_color);
                                        } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id08, R.mipmap.exams2_09);
                                            helper.setTextColor(R.id.public_topic_radio_text08, R.color.check_the_answer_sheet_item_text);
                                        } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 2) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id08, R.mipmap.exams2_08);
                                            helper.setTextColor(R.id.public_topic_radio_text08, R.color.exams_list_item_text_color8);
                                        } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 3) {
                                            helper.setBackground_Image(R.id.public_topic_radio_id08, R.mipmap.exams2_07);
                                            helper.setTextColor(R.id.public_topic_radio_text08, R.color.check_the_answer_sheet_item_text);
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
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.common_answer_question_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        if (problem.getProblem2().length() > 0) {
                            helper.setText(R.id.common_answer_question_type_problem, problem.getProblem2(), "html");
                            helper.setViewVisibility(R.id.common_answer_question_type_problem, View.VISIBLE);
                        } else {
                            helper.setViewVisibility(R.id.common_answer_question_type_problem, View.GONE);
                        }
                        helper.setoptionProblemAdapter2(R.id.common_answer_question_options, problem, examination_paper.getEid());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.GONE);
                        break;
                    case 9://问答题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        //设置您的答案
                        helper.setText(R.id.janes_answer_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.janes_answer_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 10://简答题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        //设置您的答案
                        helper.setText(R.id.janes_answer_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.janes_answer_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 11://公用案例分析题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.VISIBLE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        //设置题目序号 title_serial_number

                        //设置 题目
                        if (problem.getProblem2().length() > 0) {
                            helper.setText(R.id.public_case_analysis_problem_type_problem, problem.getProblem2(), "html");
                            helper.setViewVisibility(R.id.common_practice, View.VISIBLE);//
                        } else {
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//
                        }
//                        helper.setMyGridView(R.id.public_case_analysis_problem_gridview, problem.getThumbnails(), problem.getPictures());
//                        helper.setPublic_case_analysis_problem2(R.id.public_case_analysis_problem_gridview_problems,problem.getProblems(),problem.getPosition(),examination_instructions_id.getText().toString());
                        helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());
                        helper.setText(R.id.public_case_analysis_problem_eid, my_exams_eid);
                        helper.setText(R.id.public_case_analysis_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setText(R.id.public_case_analysis_my_answer, "您的答案：" + problem.getUser_answer(), "html");
                        helper.setText(R.id.public_case_analysis_correct, "正确答案：" + problem.getTrue_answer(), "html");
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.GONE);
                        break;
                    case 13://复选框
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        //设置 题目
                        helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setVisibility(R.id.radio_correct, View.GONE);
                        helper.setMyGridView(R.id.radios_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setMyGridView(R.id.radio_correct_imgs, problem.getCorrect_answer_thumbnails(), problem.getCorrect_answer_pictures());
                        //循环增加选项
                        try {
                            for (int i = 0; i < problem.getOptions().size(); i++) {
                                switch (i) {
                                    case 0:
                                        if (problem.getOptions().get(0).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text01, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(0).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text01, problem.getOptions().get(0).getOption_id() + "、" + problem.getOptions().get(0).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id01, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text01, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_01, View.VISIBLE);
                                            }
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
                                        if (problem.getOptions().get(1).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text02, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(1).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text02, problem.getOptions().get(1).getOption_id() + "、" + problem.getOptions().get(1).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id02, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text02, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_02, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_03, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 2:
                                        if (problem.getOptions().get(2).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text03, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(2).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text03, problem.getOptions().get(2).getOption_id() + "、" + problem.getOptions().get(2).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id03, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text03, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_03, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_04, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 3:
                                        if (problem.getOptions().get(3).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text04, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(3).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text04, problem.getOptions().get(3).getOption_id() + "、" + problem.getOptions().get(3).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id04, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text04, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_04, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_05, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 4:
                                        if (problem.getOptions().get(4).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text05, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(4).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text05, problem.getOptions().get(4).getOption_id() + "、" + problem.getOptions().get(4).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id05, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text05, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_05, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_06, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 5:
                                        if (problem.getOptions().get(5).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text06, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(5).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text06, problem.getOptions().get(5).getOption_id() + "、" + problem.getOptions().get(5).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id06, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text06, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_06, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_07, View.GONE);
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 6:
                                        if (problem.getOptions().get(6).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text07, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(6).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text07, problem.getOptions().get(6).getOption_id() + "、" + problem.getOptions().get(6).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id07, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text07, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_07, View.VISIBLE);
                                            }
                                        }
                                        helper.setVisibility(R.id.radios_layout_08, View.GONE);
                                        break;
                                    case 7:
                                        if (problem.getOptions().get(7).getOption_type().length() > 0) {
                                            if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 0) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.no_select_text_color);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 1) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_09));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 2) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_08));
                                                helper.setTextColor(R.id.radio_text08, R.color.exams_list_item_text_color8);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            } else if (Integer.parseInt(problem.getOptions().get(7).getOption_type()) == 3) {
                                                helper.setText(R.id.radio_text08, problem.getOptions().get(7).getOption_id() + "、" + problem.getOptions().get(7).getOption_text(), "html");
                                                helper.setViewBG(R.id.radio_id08, getResources().getDrawable(R.mipmap.exams2_07));
                                                helper.setTextColor(R.id.radio_text08, R.color.check_the_answer_sheet_item_text);
                                                helper.setVisibility(R.id.radios_layout_08, View.VISIBLE);
                                            }
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
                        helper.setViewVisibility(R.id.vh2, View.GONE);
                        break;
                    case 107://公用案例分析题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
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
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.VISIBLE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        //设置题目序号 title_serial_number

                        //设置 题目 common_answers_gridview_problems
                        helper.setText(R.id.common_answers_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setoptionProblemAdapter(R.id.common_answers_gridview_problems, problem.getOptions());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 111://公用案例分析题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.VISIBLE);//共用题干题
                        //设置题目序号 title_serial_number

                        //设置 题目
                        helper.setText(R.id.common_case_analysis_exercises_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        helper.setMyGridView(R.id.common_case_analysis_exercises_gridview, problem.getThumbnails(), problem.getPictures());
                        helper.setViewVisibility(R.id.vh, View.GONE);
                        helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                        break;
                    case 113://公用案例分析题
                        //控制显示 隐藏
                        helper.setViewVisibility(R.id.radios, View.GONE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//共用答案题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.VISIBLE);//共用题干题
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
                    if(is_mock_exam.equals("0")){
                        obj.put("act", URLConfig.getPaperCardDetails);
                    }else{
                        obj.put("act", URLConfig.getSimulationPaperCardDetail);
                    }
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", my_exams_eid);
                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context, URLConfig.Medical_examination, obj.toString());

//                    MyProgressBarDialogTools.hide();
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

    public void setValue3() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    if(is_mock_exam.equals("0")){
                        obj.put("act", URLConfig.getPaperCard);
                    }else{
                        obj.put("act", URLConfig.getSimulationPaperCard);
                    }
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", my_exams_eid);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    Log.e("题号", result);
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

    /*进入考试*/
    public void enter_the_examination(View view) {
        Intent intent = new Intent(Check_the_answer_sheet.this, My_exams_over.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + pid;
        super.onPause();
    }
}
