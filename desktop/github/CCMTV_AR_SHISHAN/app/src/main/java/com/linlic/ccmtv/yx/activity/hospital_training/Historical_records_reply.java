package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.my.medical_examination.My_exams_over;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.linlic.ccmtv.yx.widget.ZoomImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 历史记录作答
 * Created by tom on 2017/9/7.
 */
public class Historical_records_reply extends BaseActivity {

    private Context context;
    private ListView examination_instructions_list;
    private TextView examination_instructions_name, check_the_answer_card, examination_instructions, total_score_of_examination, examination_instructions_id;
    BaseListAdapter baseListAdapter;
    private List<Problem> problems = new ArrayList<Problem>();//题目集合
    private String my_exams_id = "";
    private boolean isNoMore = false;
    private String my_exams_eid = "";
    private LinearLayout imageLayout;
    private ZoomImageView matrixImageView;
    private int position = 0;
    private int page = 1;
    public List<String> img_text;
    public List<Integer> imgs;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONArray data = jsonObjects.getJSONArray("list");
                            if (data.length() < 10) {
                                isNoMore = true;
                                LinearLayout formal_examination_butten = (LinearLayout) View.inflate(context, R.layout.direct_broadcast_list_bottom, null);
                                examination_instructions_list.addFooterView(formal_examination_butten);
                            } else {
                                isNoMore = false;
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONArray json = data.getJSONArray(i);
                                for (int j = 0; j < json.length(); j++) {
                                    Problem problem = new Problem(problems.size(), new Examination_paper());
                                    problem.setProblem6(json.getJSONObject(j));
                                    problems.add(problem);
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(Historical_records_reply.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(problems.size() > 0, jsonObjects.getInt("status"));
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
                            Toast.makeText(Historical_records_reply.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Historical_records_reply.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                        baseListAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                img_text.add(customJson.getString("num"));
                                switch (customJson.getInt("status")) {
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
                                }

                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

//                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            problems.get(position).setIs_collect(problems.get(position).getIs_collect().equals("0") ? "1" : "0");
                            baseListAdapter.notifyDataSetChanged();
                        } else {
//                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);

                        e.printStackTrace();
                    }
                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(problems.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);//0 代表网络异常
                    break;

                default:
                    break;
            }

        }
    };
    private NodataEmptyLayout historical_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            examination_instructions_list.setVisibility(View.VISIBLE);
            historical_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                historical_nodata.setNetErrorIcon();
            } else {
                historical_nodata.setLastEmptyIcon();
            }
            examination_instructions_list.setVisibility(View.GONE);
            historical_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.historical_records_reply);
        context = this;
        findId();
        initdata();
        setValue2();
//        setValue3();


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
        historical_nodata = (NodataEmptyLayout) findViewById(R.id.historical_nodata);
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
/*
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        my_exams_eid = getIntent().getStringExtra("my_exams_eid");
        img_text = new ArrayList<>();
        imgs= new ArrayList<>();
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });
        // TODO: 2016/5/17 给按钮设置单击事件监听
        check_the_answer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/5/17 构建一个popupwindow的布局
                View popupView = Historical_records_reply.this.getLayoutInflater().inflate(R.layout.answer_card_layout, null);
                WindowManager wm = Historical_records_reply.this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
                final PopupWindow window = new PopupWindow(popupView, width - 10,height/2);
                // TODO: 2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
                MyGridView lsvMore = (MyGridView) popupView.findViewById(R.id.button_gridview);
                MyGridAdapter myGridAdapter = new MyGridAdapter(context,img_text,imgs);
                lsvMore.setAdapter(myGridAdapter);
                lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                            long arg3) {
                        TextView textView = (TextView) view.findViewById(R.id.tv_item);
//                        textView.getText().toString() 位置
                        Log.e("文本",  textView.getText().toString() );
                    Log.e("文本", examination_paper.getPosition().get(textView.getText().toString()).toString());

                        examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(textView.getText().toString()).toString()));

                        if(window.isShowing()){
                            window. dismiss();
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
        examination_paper = new Examination_paper();*/
        baseListAdapter = new BaseListAdapter(examination_instructions_list, problems, R.layout.check_the_answer_sheet_item2) {

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
                        helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
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
                        helper.setViewVisibility(R.id.janes_answer, View.VISIBLE);//简答题
                        helper.setViewVisibility(R.id.common_answers, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题

                        //设置 题目
                        helper.setText(R.id.janes_answer_type_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                        //设置正确答案
                        helper.setText(R.id.janes_answer_correct, "正确答案：" + problem.getTrue_answer().replaceAll("[$][$]", " "), "html");
                        helper.setVisibility(R.id.janes_answer_my_answer, View.GONE);
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
                        helper.setVisibility(R.id.janes_answer_my_answer, View.VISIBLE);
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
                        helper.setVisibility(R.id.janes_answer_my_answer, View.VISIBLE);
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
                        helper.setoptionProblemAdapter2(R.id.common_answer_question_options, problem, "0");
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
                        helper.setVisibility(R.id.janes_answer_my_answer, View.VISIBLE);
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
                        helper.setVisibility(R.id.janes_answer_my_answer, View.VISIBLE);
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
                    case 12://单选 A2
                        //控制显示 隐藏

                        helper.setViewVisibility(R.id.radios, View.VISIBLE);//单选
                        helper.setViewVisibility(R.id.janes_answer, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_case_analysis_problem, View.GONE);//公用案例分析题
                        helper.setViewVisibility(R.id.judgment_question, View.GONE);//判断题
                        helper.setViewVisibility(R.id.common_answer_question, View.GONE);//简答题
                        helper.setViewVisibility(R.id.public_topic, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题
                        helper.setViewVisibility(R.id.common_case_analysis_exercises, View.GONE);//共用题干题
                        helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
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
                    case 13:// 案例分析题 客观题
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
                    case 113://案例分析题 客观题
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

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = examination_instructions_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = examination_instructions_list.getChildAt(examination_instructions_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == examination_instructions_list.getHeight()) {
                        if (!isNoMore) {
                            isNoMore = true;
                            page += 1;
                            setValue2();
                        }
                    }
                }
            }
        });

    }


    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.historyRecord);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);

                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("历史记录", result);

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


    public void setValue3() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getPaperCard);
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

    public void clickexerciseCollection(final View view) {
        position = Integer.parseInt(((LinearLayout) view.getParent()).getTag().toString());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.exerciseCollection);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("qid", view.getTag().toString());
                    obj.put("bigType", getIntent().getStringExtra("bigType"));
//                    Log.e("医院培训-题目收藏参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-题目收藏", result);
                    Message message = new Message();
                    message.what = 5;
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
        Intent intent = new Intent(Historical_records_reply.this, My_exams_over.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/exercise.html";
        super.onPause();
    }

}
