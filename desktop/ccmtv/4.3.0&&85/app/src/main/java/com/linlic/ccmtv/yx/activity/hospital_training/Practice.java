package com.linlic.ccmtv.yx.activity.hospital_training;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter5;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CategoryView;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.fill_in_the_blanks.FillBlankView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tom on 2018/1/2. 练习
 */

public class Practice extends BaseActivity implements View.OnClickListener {
    private String section_result = "";
    private Context context;
    private CategoryView category;
    private String section_type = "科室";
    private ListView department_list;
    BaseListAdapter baseListAdapter;
    Examination_paper examination_paper;
    private String errorMessage;
    private LinearLayout transparent_layer, transparent_layer1, transparent_layout, complete_testing_layout, check_the_answer_card;
    private TextView transparent_title, transparent_text, count_text, transparent_text2, transparent_text1, tvLookAnswer, tvRestart, tvChangeQuestion;
    private EditText transparent_ed, transparent_ed2;
    private JSONArray qid_list;
    private JSONArray answerSheet_data;
    private int page = 1;
    private int perid = 1;
    private String paperid = "";
    public static boolean is_show = false;
    PopupWindow window = null;
    private Dialog dialog;
    private View view;
    private ImageView practice_hint;
    private GestureDetector mDetector;
    private List<String> section_name = new ArrayList<>();
    private Map<String, Object> Section = new HashMap<>();

    int flag = 0;//0表示左边选中，1表示右边选中
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray keshi_list = jsonObject.getJSONArray("data");
                            for (int i = 0; i < keshi_list.length(); i++) {
                                JSONObject keshi = keshi_list.getJSONObject(i);
                                if (!keshi.getString("num").equals("0")) {
                                    section_name.add(keshi.getString("type_name"));
                                    Section.put(keshi.getString("type_name"), keshi.getString("type"));
                                }
                            }
                            category.add(section_name, section_type, "");

                        } else {
                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        section_result = section_name.get(0);
                        initdata();
                        setTitle();
                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MyProgressBarDialogTools.hide();
                            }
                        }, 500);
//                        transparent_layer.setVisibility(View.GONE);
                        if (jsonObjects.getInt("status") == 1) { // 成功


                            count_text.setText(page + "/" + jsonObjects.getString("count"));
                            if (jsonObjects.getString("isdo").equals("1")) {
                                is_show = true;
                            } else {
                                is_show = false;
                            }

                            if (examination_paper != null && examination_paper.getProblems().size() > 0) {
                                examination_paper.getProblems().clear();
                            }
                            examination_paper.setExamination_paper5(jsonObjects);
                            qid_list = jsonObjects.has("qid_list") ? jsonObjects.getJSONArray("qid_list") : qid_list;
                            baseListAdapter.notifyDataSetChanged();
                            department_list.setSelection(0);
                        } else if (jsonObjects.getInt("status") == 11) {
                            page--;
                            transparent_layer.setVisibility(View.VISIBLE);
                            complete_testing_layout.setVisibility(View.VISIBLE);
                            transparent_layer.setBackgroundColor(Color.parseColor("#40000000"));
                        } else {
                            errorMessage = jsonObjects.getString("errorMessage");
//                            Toast.makeText(Formal_examination.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            AlertDialog dialog = new AlertDialog.Builder(context)

                                    .setTitle("提示")

                                    .setMessage(errorMessage)

                                    .setPositiveButton("重新加载", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                            setValue2();
                                            setTitle();
                                        }
                                    })
                                    .show();
                            //设置点击对话框外部区域不关闭对话框
                            dialog.setCancelable(false);
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            transparent_ed2.setText("");

                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);

                        e.printStackTrace();
                    } finally {
                        transparent_layer1.setVisibility(View.GONE);
                        transparent_layer.setVisibility(View.GONE);
                        if (transparent_layer.getTag().toString().equals("1")) {
                            transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
                        } else {
                            transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
                            transparent_layer.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功

//                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            examination_paper.getProblems().get(0).setIs_collect(examination_paper.getProblems().get(0).getIs_collect().equals("0") ? "1" : "0");
                            baseListAdapter.notifyDataSetChanged();
                        } else {
//                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            answerSheet_data = jsonObject.getJSONArray("data");
                            initcheck_the_answer_card();
                            MyProgressBarDialogTools.hide();
                        } else {
                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 7:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            perid = 1;
                            page = jsonObject.getInt("last");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            paperid = jsonObject1.getString("paperid");
                            MyProgressBarDialogTools.hide();
                            setTitle();
//                            initCard();
                            if (window != null) {
                                window.dismiss();
                            }
                        } else {
                            Toast.makeText(Practice.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    } finally {

                    }
                    break;
                case 500:

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
        setContentView(R.layout.practice);
        context = this;
        findId();
//        initdata();
        page = Integer.parseInt(getIntent().getStringExtra("lastPage"));
        paperid = getIntent().getStringExtra("paperid");
        initibaseListAdapter();
        setTitle();
        code_tx_visi();
    }

    public void code_tx_visi() {
        if (SharedPreferencesTools.getpractice_codezd(this).equals("1")) {
            practice_hint.setVisibility(View.GONE);
        } else {
            practice_hint.setVisibility(View.VISIBLE);
        }
    }
    /** 判断是否是快速点击 */
    private static long lastClickTime;
    public static boolean checkDoubleClick(){
        //点击时间
        long clickTime = SystemClock.uptimeMillis();
        //如果当前点击间隔小于500毫秒
        if (lastClickTime >= clickTime - 500) {
            return true;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        return false;
    }
    public void codezd(View view) {

        SharedPreferencesTools.savepractice_codezd(context, "1");//暂时处理方式为把原来为1的值变为2,后续如有更好处理方式再更改
        practice_hint.setVisibility(View.GONE);

    }

    public void departments(View view) {
        Intent intent = new Intent(context, Departments.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void findId() {
        super.findId();
        category = (CategoryView) findViewById(R.id.category2);
        department_list = (ListView) findViewById(R.id.department_list);
        //增加尾部
        LinearLayout formal_examination_butten = (LinearLayout) View.inflate(this, R.layout.layout_buttn, null);
        department_list.addFooterView(formal_examination_butten);
        transparent_layer = (LinearLayout) findViewById(R.id.transparent_layer);
        transparent_layer1 = (LinearLayout) findViewById(R.id.transparent_layer1);
        check_the_answer_card = (LinearLayout) findViewById(R.id.check_the_answer_card);
        complete_testing_layout = (LinearLayout) findViewById(R.id.complete_testing_layout);
        transparent_layout = (LinearLayout) findViewById(R.id.transparent_layout);
        transparent_title = (TextView) findViewById(R.id.transparent_title);
        transparent_text = (TextView) findViewById(R.id.transparent_text);
        transparent_text2 = (TextView) findViewById(R.id.transparent_text2);
        transparent_text1 = (TextView) findViewById(R.id.transparent_text1);
        count_text = (TextView) findViewById(R.id.count_text);
        transparent_ed = (EditText) findViewById(R.id.transparent_ed);
        transparent_ed2 = (EditText) findViewById(R.id.transparent_ed2);

        tvLookAnswer = (TextView) findViewById(R.id.id_tv_practice_look_answer);
        tvRestart = (TextView) findViewById(R.id.id_tv_practice_restart);
        tvChangeQuestion = (TextView) findViewById(R.id.id_tv_practice_change_question);
        practice_hint = (ImageView) findViewById(R.id.practice_hint);

        tvLookAnswer.setOnClickListener(this);
        tvRestart.setOnClickListener(this);
        tvChangeQuestion.setOnClickListener(this);
        transparent_text1.setOnClickListener(this);
        transparent_text2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transparent_text1:
                clicktransparent_text(v);
                break;
            case R.id.transparent_text2:
                clicktransparent_text(v);
                break;
            case R.id.id_tv_practice_look_answer:
                check_out_answers(v);
                break;
            case R.id.id_tv_practice_restart:
                reStart_3(v);
                break;
            case R.id.id_tv_practice_change_question:
                reStart_1(v);
                break;
        }
    }

    public void initQuestion() {
        MyGridView lsvMore = (MyGridView) findViewById(R.id.button_gridview);
//        MyGridAdapter myGridAdapter = new MyGridAdapter(context, img_text, imgs);
//        lsvMore.setAdapter(myGridAdapter);
        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                TextView textView = (TextView) view.findViewById(R.id.tv_item);
//                        textView.getText().toString() 位置
//                Log.e("文本", textView.getText().toString());
//                Log.e("文本", examination_paper.getPosition().get(textView.getText().toString()).toString());

//                examination_instructions_list.setSelection(Integer.parseInt(examination_paper.getPosition().get(textView.getText().toString()).toString()));

            }
        });
//        myGridAdapter.notifyDataSetChanged();
    }

    /*进入考试*/
   /* public void examination_script1(View view){
        MyProgressBarDialogTools.show(context);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.handPaper);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid",my_exams_id);
                    obj.put("eid",examination_paper.getEid());
                    JSONObject answer = new JSONObject();
                    List<Examination_script> examination_scripts = null;
                    examination_scripts =  MyDbUtils.findExamination_script_All(context,examination_instructions_id.getText().toString());
                    if(examination_scripts!=null && examination_scripts.size()>0){
                        for(Examination_script examination_script:MyDbUtils.findExamination_script_All(context,examination_instructions_id.getText().toString())){
                            answer.put(examination_script.getOption_id(),examination_script.getOption_name());
                        }
                    }
                    obj.put("answer",answer);

                    LogUtil.e("交卷",obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());

                    Log.e("交卷",result);

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
    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/exercise.html";
        super.onPause();
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
                is_show = true;

                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }


                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");

                    //第一步 判断 是否是选中的 选项
                    if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                        //是用户选中的选项
                        //判断用户选项是否正确
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("1");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("2");
                        }
                    } else {
                        //不是用户选中的选项
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("3");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");
                        }
                    }

                }

//                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");

                baseListAdapter.notifyDataSetChanged();

                break;
            case 12:

                is_show = true;

                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }


                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");

                    //第一步 判断 是否是选中的 选项
                    if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                        //是用户选中的选项
                        //判断用户选项是否正确
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("1");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("2");
                        }
                    } else {
                        //不是用户选中的选项
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("3");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");
                        }
                    }

                }

//                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");

                baseListAdapter.notifyDataSetChanged();
                break;
            case 2:
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");
                //多选题 重复点击取消选择
                if (Integer.parseInt(textView3.getText().toString()) == 2 && textView2.getCurrentTextColor() == vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4)) {

                    textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                    textView2.setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("0");
                } else {
                    textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_10));
                    for (int i = 0; i < vLayout.getChildCount(); i++) {
                        if (vLayout.getChildAt(i) instanceof TextView) {
                            ((TextView) vLayout.getChildAt(i)).setTextColor(vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4));
                        }
                    }
                }


                break;
            case 13:
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");
                //多选题 重复点击取消选择
                if (Integer.parseInt(textView3.getText().toString()) == 2 && textView2.getCurrentTextColor() == vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4)) {

                    textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                    textView2.setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("0");
                } else {
                    textView.setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_10));
                    for (int i = 0; i < vLayout.getChildCount(); i++) {
                        if (vLayout.getChildAt(i) instanceof TextView) {
                            ((TextView) vLayout.getChildAt(i)).setTextColor(vLayout.getContext().getResources().getColor(R.color.exams_list_item_text_color4));
                        }
                    }
                }


                break;

            case 3:

                is_show = true;

                for (int i = 0; i < parentLayout.getChildCount(); i++) {
                    if (parentLayout.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout jLayout = (LinearLayout) parentLayout.getChildAt(i);
                        ((TextView) jLayout.getChildAt(0)).setBackground(view.getContext().getResources().getDrawable(R.mipmap.exams2_07));
                        ((TextView) jLayout.getChildAt(1)).setTextColor(view.getContext().getResources().getColor(R.color.no_select_text_color));
                    }
                }


                for (int i = 0; i < examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().size(); i++) {
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");

                    //第一步 判断 是否是选中的 选项
                    if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                        //是用户选中的选项
                        //判断用户选项是否正确
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("1");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("2");
                        }
                    } else {
                        //不是用户选中的选项
                        if (examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getTrue_answer().equals(examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).getOption_text())) {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("3");
                        } else {
                            examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");
                        }
                    }

                }

//                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");

                baseListAdapter.notifyDataSetChanged();
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
                    examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(i).setOption_type2("0");
                }
                examination_paper.getProblems().get(Integer.parseInt(textView.getTag().toString())).getOptions().get(Integer.parseInt(textView2.getTag().toString())).setOption_type2("1");
//                Log.e("选中了位置", textView2.getTag().toString()+textView.getTag().toString());
                baseListAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }


    }


    public void initdata() {

        category.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("科室")) {
                    section_result = button.getText().toString();
                }
                if (examination_paper != null && 1 == 1) {
                    examination_paper.setEid("");
                }
                perid = page;
                page = Integer.parseInt(getIntent().getStringExtra("lastPage"));
                is_show = false;
                qid_list = new JSONArray();
                setTitle();
//                setVideos();
            }
        });
    }

    public void initcheck_the_answer_card() {
        // TODO: 2016/5/17 构建一个popupwindow的布局
        View popupView = Practice.this.getLayoutInflater().inflate(R.layout.answer_card_layout3, null);
        WindowManager wm = Practice.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        // TODO: 2016/5/17 为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
        MyGridView lsvMore = (MyGridView) popupView.findViewById(R.id.button_gridview);
        TextView reStart_2 = (TextView) popupView.findViewById(R.id.reStart_2);
        MyGridAdapter5 myGridAdapter = new MyGridAdapter5(context, answerSheet_data);
        lsvMore.setAdapter(myGridAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window = new PopupWindow(popupView, width - 10, height / 2);
        //        设置Popupwindow显示位置（从底部弹出）
        reStart_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initcheck_the_answer_card2();
            }
        });
        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                TextView textView = (TextView) view.findViewById(R.id.tv_item);
//                        textView.getText().toString() 位置
//                Log.e("文本", textView.getText().toString());
                perid = page;
                page = Integer.parseInt(textView.getText().toString());
                is_show = false;
                setTitle();
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
        window.showAtLocation(popupView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // TODO：更新popupwindow的状态
        window.update();
        // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
        window.showAsDropDown(check_the_answer_card, 0, 0);
    }

    public void initcheck_the_answer_card2() {
        // TODO: 2016/5/17 构建一个popupwindow的布局
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_item7, null);

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
        final TextView i_understand1 = (TextView) view.findViewById(R.id.i_understand1);// title

        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
                view = null;
            }
        });
        i_understand1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reStart_2();
                dialog.dismiss();
                dialog = null;
                view = null;
            }
        });

    }

    public void initCard() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.answerSheet);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", paperid);

//                    Log.e("医院培训-答题卡参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-答题卡收藏", result);
                    Message message = new Message();
                    message.what = 6;
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

    public void initibaseListAdapter() {
//        Log.e("显示",getIntent().getStringExtra("title"));
        check_the_answer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressBarDialogTools.show(context);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.answerSheet);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("pid", paperid);

//                            Log.e("医院培训-答题卡参数", obj.toString());
                            String result = HttpClientUtils.sendPost(context,
                                    URLConfig.Hospital_training, obj.toString());
//                            Log.e("医院培训-答题卡收藏", result);
                            Message message = new Message();
                            message.what = 6;
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
        });
        examination_paper = new Examination_paper();
        baseListAdapter = new BaseListAdapter(department_list, examination_paper.getProblems(), R.layout.formal_examination_list_item2) {

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
                    if (is_show) {

                        //获取答案显示容器
                        LinearLayout answer_layout_add = helper.getView(R.id.answer_layout_add);
                        //清除容器中的所有view
                        answer_layout_add.removeAllViews();
                        //开始增加View
                        TextView textView = null;
                        switch (Integer.parseInt(problem.getQuestion_type())) {
                            case 1://A1提醒 单选题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().equals(option.getOption_text())) {
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                    }
                                }
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 2://X题型  多选题
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().contains(option.getOption_text())) {
                                        textView = new TextView(context);
                                        textView.setTextColor(Color.parseColor("#30913B"));
                                        textView.setPadding(0, 7, 0, 7);
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                        answer_layout_add.addView(textView);
                                    }
                                }
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 3://判断题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().equals(option.getOption_text())) {
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                    }
                                }
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 4://填空题
                                String[] true_answers = problem.getTrue_answer().split("[$][$]");
                                for (int i = 0; i < true_answers.length; i++) {
                                    String true_answer = true_answers[i];
                                    LinearLayout linearLayout = new LinearLayout(context);
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    linearLayout.setLayoutParams(param);
                                    TextView textView1 = new TextView(context);
                                    textView1.setTextColor(Color.parseColor("#30913B"));
                                    textView1.setText("空格" + (i + 1) + "：");
                                    linearLayout.addView(textView1);
                                    TextView textView2 = new TextView(context);
                                    textView2.setTextColor(Color.parseColor("#30913B"));
                                    textView2.setText(Html.fromHtml(true_answer));
                                    textView2.setPadding(0, 7, 0, 7);
                                    linearLayout.addView(textView2);
                                    answer_layout_add.addView(linearLayout);
                                }
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 5://名词解释题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setText(Html.fromHtml(problem.getTrue_answer()));
                                textView.setPadding(0, 7, 0, 7);
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer());

                                break;
                            case 6://案例分析题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setText(Html.fromHtml(problem.getTrue_answer()));
                                textView.setPadding(0, 7, 0, 7);
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer());
                                break;
                            case 7://A3 A4题公共题干题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().equals(option.getOption_text())) {
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                    }
                                }
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 8://B型题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().equals(option.getOption_text())) {
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                    }
                                }
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 9://问答题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                textView.setText(Html.fromHtml(problem.getTrue_answer()));
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);

                                break;
                            case 10://简答题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                textView.setText(Html.fromHtml(problem.getTrue_answer()));
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                helper.setText(R.id.janes_answer_editText, problem.getUser_answer());
                                break;
                            case 11://公共案例分析题
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                textView.setText(Html.fromHtml(problem.getTrue_answer()));
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                helper.setText(R.id.public_case_analysis_problem_editText, problem.getUser_answer());
                                break;
                            case 12://A2题型
                                textView = new TextView(context);
                                textView.setTextColor(Color.parseColor("#30913B"));
                                textView.setPadding(0, 7, 0, 7);
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().equals(option.getOption_text())) {
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                    }
                                }
                                answer_layout_add.addView(textView);
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            case 13://案例分析题 客观题  多选题
                                for (Option option : problem.getOptions()) {
                                    if (problem.getTrue_answer().contains(option.getOption_text())) {
                                        textView = new TextView(context);
                                        textView.setTextColor(Color.parseColor("#30913B"));
                                        textView.setPadding(0, 7, 0, 7);
                                        textView.setText(Html.fromHtml(option.getOption_id() + "、" + option.getOption_text()));
                                        answer_layout_add.addView(textView);
                                    }
                                }
                                helper.setViewVisibility(R.id.answer_layout, View.VISIBLE);
                                break;
                            default:
                                helper.setViewVisibility(R.id.answer_layout, View.GONE);
                                break;
                        }
                    } else {
                        helper.setViewVisibility(R.id.answer_layout, View.GONE);
                    }

                    helper.setText(R.id.question_type, problem.getQuestion_type());//题目序号
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
                            helper.setTag(R.id.radios_Collection, problem.getTitle_serial_number());
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon01);
                            }
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (is_show) {
                                helper.setViewVisibility(R.id.radios_show, View.VISIBLE);

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
                                            radio_id.setBackgroundResource(R.mipmap.exams2_09);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 2) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_08);
                                            radio_text.setTextColor(Color.parseColor("#FF0000"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 3) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                helper.setViewVisibility(R.id.radios_show, View.GONE);
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
                            helper.setTag(R.id.radios_Collection, problem.getTitle_serial_number());
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon01);
                            }
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            if (is_show) {
                                helper.setViewVisibility(R.id.radios_show, View.VISIBLE);

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
                                            radio_id.setBackgroundResource(R.mipmap.exams2_09);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 2) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_08);
                                            radio_text.setTextColor(Color.parseColor("#FF0000"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 3) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                helper.setViewVisibility(R.id.radios_show, View.GONE);
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
                            if (is_show) {
                                helper.setViewVisibility(R.id.radios_show, View.VISIBLE);

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
                                            radio_id.setBackgroundResource(R.mipmap.exams2_09);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 2) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_08);
                                            radio_text.setTextColor(Color.parseColor("#FF0000"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 3) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                helper.setViewVisibility(R.id.radios_show, View.GONE);
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

                            }
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.radios_Collection, R.mipmap.collection_icon01);
                            }
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            helper.setTag(R.id.radios_Collection, problem.getTitle_serial_number());
//                            //设置  题型描述
//                            helper.setText(R.id.radios_type," <font color='#FF0000'>"+problem.getQuestion_type_text()+"</font>","html");
                            //设置 题目
                            helper.setText(R.id.radios_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");


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
                            if (is_show) {
                                helper.setViewVisibility(R.id.judgment_question_show, View.VISIBLE);

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

                            } else {
                                helper.setViewVisibility(R.id.judgment_question_show, View.GONE);
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
                            }
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.judgment_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.judgment_Collection, R.mipmap.collection_icon01);
                            }
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            //设置  题型描述
                            helper.setText(R.id.judgment_question_type, problem.getQuestion_type_text());
                            //设置 题目
                            helper.setText(R.id.judgment_question_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
 /*开始配置 图片*/
                            helper.setMyGridView(R.id.judgment_question_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setTag(R.id.judgment_Collection, problem.getTitle_serial_number());

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
                            helper.setViewVisibility(R.id.common_practice, View.GONE);//共用题干题   //测试 填空题
                            helper.setFillInTheBlanks(R.id.fbv_content, problem, baseListAdapter);
                            FillBlankView fillBlankView = helper.getView(R.id.fbv_content);

                            if (is_show) {
                                helper.setViewVisibility(R.id.li_blank_show, View.VISIBLE);
                                helper.setViewVisibility(R.id.fbv_content, View.VISIBLE);
                                helper.setViewVisibility(R.id.fbv_content2, View.GONE);
                                //测试 填空题
                              /*  String str = "";
                                for (int i = 0;i<problem.getAnswerList().size();i++){
                                    str += problem.getAnswerList().get(i);
                                }
                                if(str.trim().length()>0){
                                    helper.setFillInTheBlanks(R.id.fbv_content, problem,baseListAdapter);
                                    helper.setViewVisibility(R.id.fbv_content, View.VISIBLE);
                                }else {
                                    helper.setText(R.id.fbv_content2, problem.getProblem(),"html");
                                }
*/
                                String[] Userstr = problem.getUser_answer().split("[$][$]");
                                for (int i = 0; i < Userstr.length; i++) {
                                    if (Userstr[i].length() > 0) {
                                        fillBlankView.fillAnswer(Userstr[i], i);
                                    }
                                }

                            } else {
                                helper.setViewVisibility(R.id.li_blank_show, View.GONE);
                                helper.setViewVisibility(R.id.fbv_content2, View.GONE);
                                helper.setViewVisibility(R.id.fbv_content, View.VISIBLE);
                           /*     //测试 填空题
                                helper.setFillInTheBlanks(R.id.fbv_content, problem,baseListAdapter);*/
                            }

                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.li_blank_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.li_blank_Collection, R.mipmap.collection_icon01);
                            }
                            //设置题目序号 title_serial_number
                            helper.setText(R.id.title_serial_number, problem.getTitle_serial_number());//题目序号
                            //测试 填空题
                            helper.setFillInTheBlanks(R.id.fbv_content, problem, baseListAdapter);
                            helper.setTag(R.id.li_blank_Collection, problem.getTitle_serial_number());
                            //设置  题型描述
                            helper.setText(R.id.li_blank_type, problem.getQuestion_type_text());
                            helper.setMyGridView(R.id.li_blank_gridview, problem.getThumbnails(), problem.getPictures());

                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            helper.setTag(R.id.li_blank_Collection, problem.getTitle_serial_number());
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
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon01);
                            }
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

                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            helper.setTag(R.id.janes_answer_Collection, problem.getTitle_serial_number());
                            if (is_show) {
                                helper.setVisibility(R.id.janes_answer_show, View.VISIBLE);
                            } else {
                                helper.setVisibility(R.id.janes_answer_show, View.GONE);
                                helper.setFocus5(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            }

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
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon01);
                            }
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

                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            helper.setTag(R.id.janes_answer_Collection, problem.getTitle_serial_number());
                            if (is_show) {
                                helper.setVisibility(R.id.janes_answer_show, View.VISIBLE);
                            } else {
                                helper.setVisibility(R.id.janes_answer_show, View.GONE);
                                helper.setFocus5(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            }

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
                            if (is_show) {
                                helper.setViewVisibility(R.id.public_topic_show, View.VISIBLE);

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
                                            radio_id.setBackgroundResource(R.mipmap.exams2_09);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 2) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_08);
                                            radio_text.setTextColor(Color.parseColor("#FF0000"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 3) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                helper.setViewVisibility(R.id.public_topic_show, View.GONE);
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
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            //设置 题目
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.public_topic_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.GONE);//
                            }
                            helper.setText(R.id.public_topic_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");


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
                            if (is_show) {
                                helper.setEnabled(R.id.common_answer_question_show, false);
                                helper.setoptionProblemAdapter2(R.id.common_answer_question_options, problem, examination_paper.getEid());
                            } else {
                                helper.setEnabled(R.id.common_answer_question_show, true);
                                helper.setoptionProblemAdapter(R.id.common_answer_question_options, problem, examination_paper.getEid());
                            }

                            //设置 题目
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.common_answer_question_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.common_answer_question_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.common_answer_question_type_problem, View.GONE);//
                            }

                            helper.setText(R.id.common_answer_question_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");
//                            helper.setMyGridView(R.id.common_answer_question_gridview_img, problem.getThumbnails(), problem.getPictures());

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
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon01);
                            }
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

                            helper.setMyGridView(R.id.janes_answer_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            helper.setTag(R.id.janes_answer_Collection, problem.getTitle_serial_number());
                            if (is_show) {
                                helper.setVisibility(R.id.janes_answer_show, View.VISIBLE);
                            } else {
                                helper.setVisibility(R.id.janes_answer_show, View.GONE);
                                helper.setFocus5(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            }

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
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.janes_answer_Collection, R.mipmap.collection_icon01);
                            }
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

                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            helper.setTag(R.id.janes_answer_Collection, problem.getTitle_serial_number());
                            if (is_show) {
                                helper.setVisibility(R.id.janes_answer_show, View.VISIBLE);
                            } else {
                                helper.setVisibility(R.id.janes_answer_show, View.GONE);
                                helper.setFocus5(R.id.janes_answer_editText, examination_paper, R.id.janes_answer);
                            }

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
                            LogUtil.e("我想要看的值",is_show+"");

                            if (is_show) {
                                helper.setVisibility(R.id.public_case_analysis_problem_show, View.VISIBLE);
                                helper.setText(R.id.public_case_analysis_problem_editText, problem.getUser_answer() + " ");
                                helper.setFocus3(R.id.public_case_analysis_problem_editText );
                            } else {
                                helper.setVisibility(R.id.public_case_analysis_problem_show, View.GONE);
                                if (helper.getStr(R.id.public_case_analysis_problem_editText) != null && helper.getStr(R.id.public_case_analysis_problem_editText) != problem.getUser_answer()) {
                                    helper.setText(R.id.public_case_analysis_problem_editText, problem.getUser_answer() + " ");
                                }
                                helper.setFocus5(R.id.public_case_analysis_problem_editText, examination_paper, R.id.public_case_analysis_problem);

                            }


                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.GONE);
                            break;
                        case 13://案例分析题 客观题
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
                            if (is_show) {
                                helper.setViewVisibility(R.id.radios_show, View.VISIBLE);

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
                                            radio_id.setBackgroundResource(R.mipmap.exams2_09);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 2) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_08);
                                            radio_text.setTextColor(Color.parseColor("#FF0000"));
                                        }else if (Integer.parseInt(problem.getOptions().get(i).getOption_type()) == 3) {
                                            radio_id.setBackgroundResource(R.mipmap.exams2_07);
                                            radio_text.setTextColor(Color.parseColor("#30913B"));
                                        }
                                        helper.addview2(R.id.radios_add_layout,radios);

                                    }
                                    helper.setMyGridView(R.id.gridview, problem.getThumbnails(), problem.getPictures());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                helper.setViewVisibility(R.id.radios_show, View.GONE);
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

                            }

                            //设置 题目
                            if (problem.getProblem2().length() > 0) {
                                helper.setText(R.id.public_topic_type_problem, problem.getProblem2(), "html");
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.VISIBLE);//
                            } else {
                                helper.setViewVisibility(R.id.public_topic_type_problem, View.GONE);//
                            }
                            helper.setText(R.id.public_topic_type_problem2, problem.getQuestion_type_text() + problem.getProblem(), "html");


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
                            helper.setTag(R.id.public_topic_Collection, problem.getTitle_serial_number());
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.public_topic_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.public_topic_Collection, R.mipmap.collection_icon01);
                            }
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
                            helper.setTag(R.id.common_answer_question_Collection, problem.getTitle_serial_number());
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.common_answer_question_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.common_answer_question_Collection, R.mipmap.collection_icon01);
                            }
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
                            helper.setTag(R.id.public_case_analysis_problem_Collection, problem.getTitle_serial_number());
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.public_case_analysis_problem_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.public_case_analysis_problem_Collection, R.mipmap.collection_icon01);
                            }
                            //设置 题目
                            helper.setText(R.id.common_case_analysis_exercises_problem, problem.getQuestion_type_text() + problem.getProblem(), "html");
                            helper.setMyGridView(R.id.common_case_analysis_exercises_gridview, problem.getThumbnails(), problem.getPictures());
                            helper.setViewVisibility(R.id.vh, View.GONE);
                            helper.setViewVisibility(R.id.vh2, View.VISIBLE);
                            break;
                        case 113:// 案例分析题 客观题 题干
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
                            helper.setTag(R.id.public_case_analysis_problem_Collection, problem.getTitle_serial_number());
                            if (problem.getIs_collect().equals("1")) {
                                helper.setImage(R.id.public_case_analysis_problem_Collection, R.mipmap.collection_icon02);
                            } else {
                                helper.setImage(R.id.public_case_analysis_problem_Collection, R.mipmap.collection_icon01);
                            }
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
        department_list.setAdapter(baseListAdapter);
        //创建手势监听器对象
        mDetector = new GestureDetector(getApplicationContext(), new MyGestureListener());
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

    boolean isFling = false;
    long startFlingTime = 0;

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
            if (e1 != null && e2 != null) {
                //左滑动
                if (e1.getX() - e2.getX() > 100 && Math.abs(e1.getY() - e2.getY()) < 100) {
//                    ToastUtil.shortToast(GestureTestActivity.this, "向左");
//                    LogUtil.e("滑动","向左");
                    isFling = true;
                    startFlingTime = System.currentTimeMillis();
                    switch_questions(transparent_layer);
                    return true;
                }
                //右滑动
                else if (e1.getX() - e2.getX() < -100 && Math.abs(e1.getY() - e2.getY()) < 100) {
//                    ToastUtil.shortToast(GestureTestActivity.this, "向右");
//                    LogUtil.e("滑动","向右");
                    isFling = true;
                    startFlingTime = System.currentTimeMillis();
                    switch_questions1(transparent_layer);
                    return true;
                }
            }
            startFlingTime = System.currentTimeMillis();
            isFling = false;
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public void setVideos() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getQuestion);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", paperid);
                    obj.put("qid", 1);
//                    Log.e("医院培训-题目分类参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-题目分类", result);
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

    public void setreportError() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.reportError);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("qid", examination_paper.getProblems().get(0).getTitle_serial_number());
                    obj.put("content", transparent_ed2.getText().toString().trim());
//                    Log.e("医院培训-题目报错参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-题目报错", result);
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

    public void setTitle() {
        try {
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.getQuestion);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("pid", paperid);
                        obj.put("qid", page);
                        obj.put("perid", perid);
                        obj.put("istop", 2);
                        obj.put("sheet", is_show ? 1 : 0);
                        is_show = false;
                        Boolean bool = false;
                        if (examination_paper != null && 1 == 1) {
                            JSONObject json = new JSONObject();
                            for (Problem problem : examination_paper.getProblems()) {
                                JSONArray array = new JSONArray();
                                switch (problem.getQuestion_type()) {
                                    case "1":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "2":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "13":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "3":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "4":
                                        boolean is = false;
                                        for (String str : problem.getAnswerList()) {
                                            if (str.length() > 0) {
                                                is = true;
                                            }
                                        }
                                        if (is) {
                                            ArrayList arrayList = new ArrayList();
                                            for (int i = 0; i < problem.getAnswerList().size(); i++) {
                                                if (problem.getAnswerList().get(i) == null || problem.getAnswerList().get(i).length() < 1) {
                                                    problem.getAnswerList().set(i, " ");
                                                    arrayList.add("");
                                                } else {
                                                    arrayList.add(problem.getAnswerList().get(i).toString());
                                                }
                                            }
//                                            json.put(problem.getTitle_serial_number(),new JSONArray(problem.getAnswerList().toString()));
                                            json.put(problem.getTitle_serial_number(), new JSONArray(arrayList));
                                        }
                                        break;
                                    case "5":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "6":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "7":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), array);
                                        break;
                                    case "8":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), array);
                                        break;
                                    case "9":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "10":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "11":
                                        if (problem.getUser_answer().length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        break;
                                    case "12":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (json.length() > 0) {
                                if (examination_paper.getProblems().get(0).getQuestion_type().equals("107") || examination_paper.getProblems().get(0).getQuestion_type().equals("108") || examination_paper.getProblems().get(0).getQuestion_type().equals("111")|| examination_paper.getProblems().get(0).getQuestion_type().equals("113")) {
                                    if (bool) {
                                        obj.put("answer", json);
                                    }
                                } else {
                                    obj.put("answer", json);
                                }
                            }
                        }

                        if (qid_list != null && 1 == 1 && qid_list.length() > 0) {
                            if (page <= qid_list.length()) {
                                obj.put("qid", qid_list.getString(page - 1));
                            }
                        }

//                        Log.e("医院培训-题目参数", obj.toString());
                        String result = HttpClientUtils.sendPost(context,
                                URLConfig.Hospital_training, obj.toString());

//                        LogUtil.e("医院培训-题目", result);
                        Message message = new Message();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyProgressBarDialogTools.hide();
                }
            }, 500);
            e.printStackTrace();
        }


    }

    public void setTitle2() {
        try {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.getQuestion);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("pid", paperid);
                        obj.put("qid", page);
                        obj.put("perid", perid);
                        obj.put("sheet", is_show ? 1 : 0);
                        is_show = false;
                        Boolean bool = false;
                        if (examination_paper != null && 1 == 1) {
                            JSONObject json = new JSONObject();
                            for (Problem problem : examination_paper.getProblems()) {
                                JSONArray array = new JSONArray();
                                switch (problem.getQuestion_type()) {
                                    case "1":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "2":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "3":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "4":
                                        boolean is = false;
                                        for (String str : problem.getAnswerList()) {
                                            if (str.length() > 0) {
                                                is = true;
                                            }
                                        }
                                        if (is) {
                                            ArrayList arrayList = new ArrayList();
                                            for (int i = 0; i < problem.getAnswerList().size(); i++) {
                                                if (problem.getAnswerList().get(i) == null || problem.getAnswerList().get(i).length() < 1) {
                                                    problem.getAnswerList().set(i, " ");
                                                    arrayList.add("");
                                                } else {
                                                    arrayList.add(problem.getAnswerList().get(i).toString());
                                                }
                                            }
//                                            json.put(problem.getTitle_serial_number(),new JSONArray(problem.getAnswerList().toString()));
                                            json.put(problem.getTitle_serial_number(), new JSONArray(arrayList));
                                        }
                                        break;
                                    case "5":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "6":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "7":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), array);
                                        break;
                                    case "8":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), array);
                                        break;
                                    case "9":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "10":
                                        if (problem.getUser_answer().length() > 0) {
                                            json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        }
                                        break;
                                    case "11":
                                        if (problem.getUser_answer().length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), new JSONArray().put(problem.getUser_answer()));
                                        break;
                                    case "12":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            json.put(problem.getTitle_serial_number(), array);
                                        }
                                        break;
                                    case "13":
                                        for (Option option : problem.getOptions()) {
                                            if (option.getOption_type().equals("1") || option.getOption_type().equals("2")) {
                                                array.put(option.getOption_text());
                                            }
                                        }
                                        if (array.length() > 0) {
                                            bool = true;
                                        }
                                        json.put(problem.getTitle_serial_number(), array);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (json.length() > 0) {
                                if (examination_paper.getProblems().get(0).getQuestion_type().equals("107") || examination_paper.getProblems().get(0).getQuestion_type().equals("108") || examination_paper.getProblems().get(0).getQuestion_type().equals("111")|| examination_paper.getProblems().get(0).getQuestion_type().equals("113")) {
                                    if (bool) {
                                        obj.put("answer", json);
                                    }
                                } else {
                                    obj.put("answer", json);
                                }
                            }
                        }

                        if (qid_list != null && 1 == 1 && qid_list.length() > 0) {
                            if (page <= qid_list.length()) {
                                obj.put("qid", qid_list.getString(page - 1));
                            }
                        }

//                        Log.e("医院培训-题目参数", obj.toString());
                        String result = HttpClientUtils.sendPost(context,
                                URLConfig.Hospital_training, obj.toString());

//                        LogUtil.e("医院培训-题目", result);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyProgressBarDialogTools.hide();
                }
            }, 500);
            e.printStackTrace();
        }

    }


    public void switch_questions1(View view) {
        if(!checkDoubleClick() ){
            if (page > 1) {
                perid = page;
                page--;
                setTitle();
            } else {
                Toast.makeText(Practice.this, "已到第一题，请勿重复点击！", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void switch_questions(View view) {
        if(!checkDoubleClick() ){
            perid = page;
            page++;
            setTitle();
        }

    }

    public void check_out_answers(View view) {
        if (!is_show) {
            is_show = true;

            for (Problem problem : examination_paper.getProblems()) {
                switch (problem.getQuestion_type()) {
                    case "2"://关于选项的 Option_type 有两重含义  第一种 在未查看答案前 该值只有0或1 代表用户选中未选中  第二种是用户点击查看答案后  该值表示选项   0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                        //循环题目选项
                        String[] answerStr = problem.getTrue_answer().split("[&nbsp;]");

                        for (Option option : problem.getOptions()) {
                            //判断用户选项是否正确
                            int iii = 0;
                            for (String str : answerStr) {
                                if (str.trim().equals(option.getOption_text().trim())) {
                                    iii = iii + 1;
                                }
                            }
                            if (iii > 0) {
                                //判断用户是否选中了该选项
                                if (option.getOption_type().equals("1")) {
                                    //  0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                                    option.setOption_type2("1");
                                } else {
                                    option.setOption_type2("3");
                                }
                            } else {
                                if (option.getOption_type().equals("1")) {
                                    option.setOption_type2("2");
                                } else {
                                    option.setOption_type2("0");
                                }
                            }


                        }
                        break;
                    case "13"://关于选项的 Option_type 有两重含义  第一种 在未查看答案前 该值只有0或1 代表用户选中未选中  第二种是用户点击查看答案后  该值表示选项   0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                        //循环题目选项
                        String[] answerStr1 = problem.getTrue_answer().split("[&nbsp;]");

                        for (Option option : problem.getOptions()) {
                            //判断用户选项是否正确
                            int iii = 0;
                            for (String str : answerStr1) {
                                if (str.trim().equals(option.getOption_text().trim())) {
                                    iii = iii + 1;
                                }
                            }
                            if (iii > 0) {
                                //判断用户是否选中了该选项
                                if (option.getOption_type().equals("1")) {
                                    //  0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                                    option.setOption_type2("1");
                                } else {
                                    option.setOption_type2("3");
                                }
                            } else {
                                if (option.getOption_type().equals("1")) {
                                    option.setOption_type2("2");
                                } else {
                                    option.setOption_type2("0");
                                }
                            }


                        }
                        break;
                    case "7"://关于选项的 Option_type 有两重含义  第一种 在未查看答案前 该值只有0或1 代表用户选中未选中  第二种是用户点击查看答案后  该值表示选项   0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                        //循环题目选项
                        for (Option option : problem.getOptions()) {
                            //判断用户选项是否正确
                            if (problem.getTrue_answer().equals(option.getOption_text())) {
                                //判断用户是否选中了该选项
                                if (option.getOption_type().equals("1")) {
                                    //  0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                                    option.setOption_type2("1");
                                } else {
                                    option.setOption_type2("3");
                                }
                            } else {
                                if (option.getOption_type().equals("1")) {
                                    option.setOption_type2("2");
                                } else {
                                    option.setOption_type2("0");
                                }
                            }

                        }
                        break;
                    case "8"://关于选项的 Option_type 有两重含义  第一种 在未查看答案前 该值只有0或1 代表用户选中未选中  第二种是用户点击查看答案后  该值表示选项   0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                        //循环题目选项
                        for (Option option : problem.getOptions()) {
                            //判断用户选项是否正确
                            if (problem.getTrue_answer().equals(option.getOption_text())) {
                                //判断用户是否选中了该选项
                                if (option.getOption_type().equals("1")) {
                                    //  0 初始状态   1 正确答案并选中  2 选中 错误答案  3  正确答案 未选中
                                    option.setOption_type2("1");
                                } else {
                                    option.setOption_type2("3");
                                }
                            } else {
                                if (option.getOption_type().equals("1")) {
                                    option.setOption_type2("2");
                                } else {
                                    option.setOption_type2("0");
                                }
                            }

                        }
                        break;
                    default:

                        break;
                }
            }

            baseListAdapter.notifyDataSetChanged();
        }
    }

    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
//            Log.d("返回", "onKeyDown()");
            setTitle2();
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    // 捕获返回键的方法2
    @Override
    public void onBackPressed() {
        setTitle2();
        finish();
    }

    @Override
    public void back(View view) {
        setTitle2();
        super.back(view);
    }

    public void clicktransparent_text(View view) {
        if (view.getId() == transparent_text.getId()) {
            //我知道了
            transparent_layer1.setVisibility(View.GONE);
            transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
            transparent_layer.setTag("1");
            transparent_layer.setVisibility(View.GONE);
            is_show = true;
            baseListAdapter.notifyDataSetChanged();
        } else if (view.getId() == transparent_text2.getId()) {
            //取消
            transparent_layer1.setVisibility(View.GONE);

            transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
            transparent_layer.setVisibility(View.GONE);
        } else if (view.getId() == transparent_text1.getId()) {
            //提交
            if (transparent_ed2.getText().toString().trim().length() > 0) {
                setreportError();
            } else {
                Toast.makeText(Practice.this, "请提供您的建议！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickComplete_testing(View view) {

        //关闭
        transparent_layer1.setVisibility(View.GONE);
        transparent_layer.setVisibility(View.GONE);
        complete_testing_layout.setVisibility(View.GONE);
        is_show = true;
        baseListAdapter.notifyDataSetChanged();


    }

    public void clickcomplete_testing(View view) {

        //重新开始
        transparent_layer1.setVisibility(View.GONE);
        transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
        transparent_layer.setTag("1");
        transparent_layer.setVisibility(View.GONE);
        complete_testing_layout.setVisibility(View.GONE);
        is_show = false;
        examination_paper.getProblems().clear();
        examination_paper.setEid("");
        qid_list = new JSONArray();
        perid = page;
        page = 1;
        setTitle();


    }

    public void clickWrong_title(View view) {
        transparent_layer.setVisibility(View.VISIBLE);
        transparent_layer1.setVisibility(View.VISIBLE);
        transparent_layer.setBackgroundColor(Color.parseColor("#40000000"));
        transparent_title.setText("题目报错");
        transparent_text.setVisibility(View.GONE);
        transparent_layout.setVisibility(View.VISIBLE);
        transparent_ed.setVisibility(View.GONE);
        transparent_ed2.setVisibility(View.VISIBLE);
    }

    public void clickHistorical_records_reply(View view) {
        Intent intent = new Intent(context, Historical_records_reply.class);
        intent.putExtra("bigType", getIntent().getStringExtra("bigType"));
        startActivity(intent);
    }

    public void clickexerciseCollection(final View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.quesCollect);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("questionid", view.getTag().toString());
                    TextView textView = (TextView) view.getRootView().findViewById(R.id.question_type);
                    obj.put("type", textView.getText().toString());
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

    public void reStart_1(View view) {
        MyProgressBarDialogTools.show(context);
        //重新开始
        transparent_layer1.setVisibility(View.GONE);
        transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
        transparent_layer.setTag("1");
        transparent_layer.setVisibility(View.GONE);
        complete_testing_layout.setVisibility(View.GONE);
        is_show = false;
        examination_paper.getProblems().clear();
        examination_paper.setEid("");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.reStart);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", paperid);
                    obj.put("type", 1);

//                    Log.e("医院培训-答题卡参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-答题卡收藏", result);
                    Message message = new Message();
                    message.what = 7;
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

    public void reStart_2() {
        MyProgressBarDialogTools.show(context);
        //重新开始
        transparent_layer1.setVisibility(View.GONE);
        transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
        transparent_layer.setTag("1");
        transparent_layer.setVisibility(View.GONE);
        complete_testing_layout.setVisibility(View.GONE);
        is_show = false;
        examination_paper.getProblems().clear();
        examination_paper.setEid("");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.reStart);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", paperid);
                    obj.put("type", 2);

//                    Log.e("医院培训-答题卡参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-答题卡收藏", result);
                    Message message = new Message();
                    message.what = 7;
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

    public void reStart_3(View view) {
        MyProgressBarDialogTools.show(context);
        //重新开始
        transparent_layer1.setVisibility(View.GONE);
        transparent_layer.setBackgroundColor(Color.parseColor("#00000000"));
        transparent_layer.setTag("1");
        transparent_layer.setVisibility(View.GONE);
        complete_testing_layout.setVisibility(View.GONE);
        is_show = false;
        examination_paper.getProblems().clear();
        examination_paper.setEid("");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.reStart);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", paperid);
                    obj.put("type", 2);

//                    Log.e("医院培训-答题卡参数", obj.toString());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训-答题卡收藏", result);
                    Message message = new Message();
                    message.what = 7;
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
