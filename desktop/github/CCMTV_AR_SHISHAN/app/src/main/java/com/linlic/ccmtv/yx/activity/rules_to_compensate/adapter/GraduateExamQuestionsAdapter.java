package com.linlic.ccmtv.yx.activity.rules_to_compensate.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerPopWindow;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.GraduateExamActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GraduateExamQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by yu on 2018/6/20.
 */

public class GraduateExamQuestionsAdapter extends BaseAdapter implements View.OnTouchListener, View.OnFocusChangeListener, View.OnClickListener, View.OnLongClickListener {

   /* private GraduateExamActivity context;
    private List<Map<String, String>> list;

    public GraduateExamQuestionsAdapter(GraduateExamActivity context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_graduate_exam_questions, parent, false);
            holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id.id_tv_questions_item_title);
            holder.llQuestionContent = (LinearLayout) convertView.findViewById(R.id.id_ll_item_graduate_exam_questions);
            holder.tvQuestionContent = (TextView) convertView.findViewById(R.id.id_tv_questions_item_content);
            holder.llDeductionReason = (LinearLayout) convertView.findViewById(R.id.id_ll_graduate_exam_item_deduction_reason);
            holder.tvDeduction = (TextView) convertView.findViewById(R.id.id_tv_graduate_exam_questions_deduction);
            holder.tvStandardScore = (TextView) convertView.findViewById(R.id.id_tv_graduate_exam_questions_standard_score);
            holder.etDeductionReason = (EditText) convertView.findViewById(R.id.id_et_graduate_exam_item_deduction_reason);
            holder.rlSpinner = (RelativeLayout) convertView.findViewById(R.id._item_grade);
            holder.tvSpinner = (TextView) convertView.findViewById(R.id.tv_value);
            holder.llScore = (LinearLayout) convertView.findViewById(R.id.id_ll_item_graduate_exam_questions_score);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> map = list.get(position);
        List<String> spinnerList = new ArrayList<>();

        if (map.get("dataType").equals("title")) {
            holder.tvQuestionTitle.setText(map.get("name"));
            holder.llQuestionContent.setVisibility(View.GONE);
            holder.tvQuestionTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvQuestionContent.setText(map.get("name"));
            holder.tvStandardScore.setText("标准分：" + map.get("grade"));
            holder.llQuestionContent.setVisibility(View.VISIBLE);
            holder.tvQuestionTitle.setVisibility(View.GONE);

            if (map.get("grade") != null) {
                for (int i = 0; i <= Integer.parseInt(map.get("grade")); i++) {
                    spinnerList.add(i + "");
                }
            }

            setSpinner(holder.rlSpinner, holder.tvSpinner, holder.llDeductionReason, map, spinnerList);
        }
        return convertView;
    }

    private void setSpinner(final RelativeLayout rlSpinner, final TextView tvSpinner, final LinearLayout llDeductionReason, final Map<String,String> map, final List<String> spinnerList) {
        rlSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*SpinerPopWindow mSpinerPopWindow = new SpinerPopWindow(context);
                SpinerAdapter mAdapter = new SpinerAdapter(context, spinnerList);
                mAdapter.refreshData(spinnerList, 0);
                //初始化PopWindow
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(rlSpinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(rlSpinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        tvSpinner.setText(spinnerList.get(pos));
                        map.put("score", spinnerList.get(pos));
                        if (Integer.parseInt(spinnerList.get(pos)) > 0) {
                            llDeductionReason.setVisibility(View.VISIBLE);
                        } else {
                            llDeductionReason.setVisibility(View.GONE);
                        }
                        //然后循环 计算分数
                        int count = 0;
                        for (Map<String, String> map1 : list) {
                            if (map1.get("dataType").equals("content")) {
                                count += Integer.parseInt(map1.get("score"));
                            }
                        }
                        //更新
                        context.setTotalScore(100 - count);
                    }
                });*//*

                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(context, spinnerList);
                mAdapter.refreshData(spinnerList, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(context);
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(rlSpinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(rlSpinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        map.put("deductionScore",spinnerList.get(pos));
                        if (Integer.parseInt(map.get("deductionScore"))>0) {
                            llDeductionReason.setVisibility(View.VISIBLE);
                        }else {
                            llDeductionReason.setVisibility(View.GONE);
                        }
                        tvSpinner.setText(spinnerList.get(pos));
                        //然后循环 计算分数
                        int count= 0 ;
                        int totalScore = 0;
                        for (Map<String,String> graduateExamQuestion : list) {
                            if (graduateExamQuestion.get("dataType").equals("content")){
                                totalScore += Integer.parseInt(graduateExamQuestion.get("grade"));
                                if (graduateExamQuestion.get("deductionScore") != null) {
                                    count += Integer.parseInt(graduateExamQuestion.get("grade"));
                                }
                            }
                        }
                        //更新
                        final int finalCount = totalScore - count;
                        context.setTotalScore(finalCount);
                        *//*((Activity)viewScore.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewScore.setText(finalCount +"");
                            }
                        });*//*
                    }
                });

            }
        });
    }

    class ViewHolder {
        TextView tvQuestionTitle;
        RelativeLayout rlSpinner;
        TextView tvSpinner;
        LinearLayout llQuestionContent;
        LinearLayout llDeductionReason;
        TextView tvQuestionContent;
        TextView tvStandardScore;
        TextView tvDeduction;
        EditText etDeductionReason;
        LinearLayout llScore;
    }*/

    private GraduateExamActivity context;
    private List<GraduateExamQuestion> list;
    //定义hashMap 用来存放之前创建的每一项item
    private HashMap<Integer, View> lmap = new HashMap<Integer, View>();
    private String marking = "";
    private int selectedEditTextPosition = -1;
    private int selectedRlSpinnerPosition = -1;
    private int touchWhat = -1;     //判断点击（触摸那个空间）  1为输入框，2为spinner

    public GraduateExamQuestionsAdapter(GraduateExamActivity context, List<GraduateExamQuestion> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        GraduateExamQuestion map = list.get(position);
        final GraduateExamQuestion finalMap = map;
        if (lmap.get(position) == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_graduate_exam_questions, parent, false);
            holder.tvQuestionTitle = (TextView) convertView.findViewById(R.id.id_tv_questions_item_title);
            holder.llQuestionContent = (LinearLayout) convertView.findViewById(R.id.id_ll_item_graduate_exam_questions);
            holder.tvQuestionContent = (TextView) convertView.findViewById(R.id.id_tv_questions_item_content);
            holder.llDeductionReason = (LinearLayout) convertView.findViewById(R.id.id_ll_graduate_exam_item_deduction_reason);
            //holder.tvDeduction = (TextView) convertView.findViewById(R.id.id_tv_graduate_exam_questions_deduction);
            holder.tvStandardScore = (TextView) convertView.findViewById(R.id.id_tv_graduate_exam_questions_standard_score);
            holder.etDeductionReason = (EditText) convertView.findViewById(R.id.id_et_graduate_exam_item_deduction_reason);
            holder.rlSpinner = (RelativeLayout) convertView.findViewById(R.id._item_grade);
            holder.tvSpinner = (TextView) convertView.findViewById(R.id.tv_value);
            //holder.llScore = (LinearLayout) convertView.findViewById(R.id.id_ll_item_graduate_exam_questions_score);

            /*holder.etDeductionReason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    finalMap.setDeductionReason(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });*/
            convertView.setTag(holder);
            lmap.put(position,convertView);
        } else {
            convertView = lmap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        holder.etDeductionReason.setOnTouchListener(this); // 正确写法
        holder.etDeductionReason.setOnFocusChangeListener(this);
        holder.etDeductionReason.setTag(position);
        holder.rlSpinner.setOnTouchListener(this); // 正确写法
        holder.rlSpinner.setOnFocusChangeListener(this);
        holder.rlSpinner.setTag(position);

        if (touchWhat == 1) {
            holder.rlSpinner.clearFocus();
            if (selectedEditTextPosition != -1 && position == selectedEditTextPosition) { // 保证每个时刻只有一个EditText能获取到焦点
                holder.etDeductionReason.requestFocus();
            } else {
                holder.etDeductionReason.clearFocus();
            }
        }else {
            holder.etDeductionReason.clearFocus();
            if (selectedRlSpinnerPosition != -1 && position == selectedRlSpinnerPosition) { // 保证每个时刻只有一个EditText能获取到焦点
                holder.rlSpinner.requestFocus();
            } else {
                holder.rlSpinner.clearFocus();
            }
        }


        String text = map.getDeductionReason();
        holder.etDeductionReason.setText(text);
        holder.etDeductionReason.setSelection(holder.etDeductionReason.length());

        convertView.setTag(R.id.item_root, position); // 应该在这里让convertView绑定position
        convertView.setOnClickListener(this);
        convertView.setOnLongClickListener(this);

        List<String> spinnerList = new ArrayList<>();
        marking = map.getMarking();

        if (map.getDataType().equals("title")) {
            holder.tvQuestionTitle.setText(map.getQuestionContent());
            holder.llQuestionContent.setVisibility(View.GONE);
            holder.tvQuestionTitle.setVisibility(View.VISIBLE);
        } else {
            if (map.getDisabled().equals("0")){       //是否可以编辑  1、不可编辑   0、可以编辑
                holder.rlSpinner.setClickable(true);
                holder.rlSpinner.setEnabled(true);
                holder.etDeductionReason.setEnabled(true);
                holder.etDeductionReason.setClickable(true);
            }else {
                holder.rlSpinner.setClickable(false);
                holder.rlSpinner.setEnabled(false);
                holder.etDeductionReason.setEnabled(false);
                holder.etDeductionReason.setClickable(false);
            }

            if (map.getStandardScore().isEmpty()) {
                holder.tvQuestionContent.setText(map.getQuestionContent());
                holder.llQuestionContent.setVisibility(View.VISIBLE);
                holder.tvQuestionTitle.setVisibility(View.GONE);
                holder.llScore.setVisibility(View.GONE);
            }else {
                holder.tvQuestionContent.setText(map.getQuestionContent());
                holder.tvStandardScore.setText("标准分：" + map.getStandardScore());
                if (map.getDeductionScore() != null && !map.getDeductionScore().isEmpty()) {
                    holder.tvSpinner.setText(map.getDeductionScore());
                }

                if (Integer.parseInt(map.getDeductionScore())>0) {
                    holder.llDeductionReason.setVisibility(View.VISIBLE);
                }else {
                    holder.llDeductionReason.setVisibility(View.GONE);
                }

                if (map.getDeductionReason() != null && !map.getDeductionReason().isEmpty()) {
                    holder.etDeductionReason.setText(map.getDeductionReason());
                }

                if (marking.equals("deduct_marks")) {
                    holder.tvDeduction.setText("扣分");
                    holder.llQuestionContent.setVisibility(View.VISIBLE);
                    holder.tvQuestionTitle.setVisibility(View.GONE);

                    if (map.getStandardScore() != null) {
                        for (int i = 0; i <= Integer.parseInt(map.getStandardScore()); i++) {
                            spinnerList.add(i + "");
                        }
                    }
                } else {
                    holder.tvDeduction.setText("得分");
                    holder.llQuestionContent.setVisibility(View.VISIBLE);
                    holder.tvQuestionTitle.setVisibility(View.GONE);

                    if (map.getStandardScore() != null) {
                        for (int i = Integer.parseInt(map.getStandardScore()); i >=0; i--) {
                            spinnerList.add(i + "");
                        }
                    }
                }

//                setSpinner(holder.rlSpinner, holder.tvSpinner, holder.llDeductionReason, map, spinnerList);
                final ViewHolder finalViewHolder = holder;
                final List<String> finalSpinnerList = spinnerList;

                holder.rlSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpinerPopWindow mSpinerPopWindow;
                        SpinerAdapter mAdapter;
                        mAdapter = new SpinerAdapter(context, finalSpinnerList);
                        mAdapter.refreshData(finalSpinnerList, 0);
                        //初始化PopWindow
                        mSpinerPopWindow = new SpinerPopWindow(context);
                        mSpinerPopWindow.setAdatper(mAdapter);
                        //设置mSpinerPopWindow显示的宽度
                        mSpinerPopWindow.setWidth(finalViewHolder.rlSpinner.getWidth());
                        //设置显示的位置在哪个控件的下方
                        mSpinerPopWindow.showAsDropDown(finalViewHolder.rlSpinner);
                        mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                            @Override
                            public void onItemClick(int pos) {
                                //每次更换 分数   先赋值给当前item bean
                                finalMap.setDeductionScore(finalSpinnerList.get(pos));
                                finalViewHolder.tvSpinner.setText(finalSpinnerList.get(pos));
                                //然后循环 计算分数
                                //deduct_marks扣分 score得分
                                if (marking.equals("deduct_marks")) {
                                    if (Integer.parseInt(finalMap.getDeductionScore())>0) {
                                        finalViewHolder.llDeductionReason.setVisibility(View.VISIBLE);
                                    }else {
                                        finalViewHolder.llDeductionReason.setVisibility(View.GONE);
                                    }
                                    int count= 0 ;
                                    int totalScore = 0;
                                    for (GraduateExamQuestion graduateExamQuestion : list) {
                                        if (graduateExamQuestion.getDataType().equals("content")){
                                            totalScore += Integer.parseInt(graduateExamQuestion.getStandardScore());
                                            if (graduateExamQuestion.getDeductionScore() != null) {
                                                count += Integer.parseInt(graduateExamQuestion.getDeductionScore());
                                            }
                                        }
                                    }
                                    //更新
                                    final int finalCount = totalScore - count;
                                    context.setTotalScore(finalCount);
                                } else {
                                    finalViewHolder.llDeductionReason.setVisibility(View.GONE);
                                    int count= 0 ;
                                    for (GraduateExamQuestion graduateExamQuestion : list) {
                                        count +=Integer.parseInt(graduateExamQuestion.getDeductionScore());
                                    }
                                    //更新
                                    final int finalCount = count;
                                    context.setTotalScore(finalCount);
                                }
                            }
                        });
                    }
                });
            }
        }
        return convertView;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedEditTextPosition != -1) {
//                Log.w("MyEditAdapter", "onTextPosiotion " + selectedEditTextPosition);
                GraduateExamQuestion itemTest = (GraduateExamQuestion) getItem(selectedEditTextPosition);
                itemTest.setDeductionReason(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.id_et_graduate_exam_item_deduction_reason:      //触摸扣分原因输入框
                    EditText editText = (EditText) v;
                    selectedEditTextPosition = (int) editText.getTag();
                    touchWhat = 1;
                    break;
                case R.id._item_grade:  //触摸spinner
                    RelativeLayout relativeLayout = (RelativeLayout) v;
                    selectedRlSpinnerPosition = (int) relativeLayout.getTag();
                    touchWhat = 2;
                    break;
            }
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.id_et_graduate_exam_item_deduction_reason:      //触摸扣分原因输入框
                EditText editText = (EditText) v;
                if (hasFocus) {
                    editText.addTextChangedListener(mTextWatcher);
                } else {
                    editText.removeTextChangedListener(mTextWatcher);
                }
                break;
            case R.id._item_grade:  //触摸spinner
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_root) {
            int position = (int) view.getTag(R.id.item_root);
//            Toast.makeText(context, "点击第 " + position + " 个item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.item_root) {
            int position = (int) view.getTag(R.id.item_root);
//            Toast.makeText(context, "长按第 " + position + " 个item", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void setSpinner(final RelativeLayout rlSpinner, final TextView tvSpinner, final LinearLayout llDeductionReason, final GraduateExamQuestion map, final List<String> spinnerList) {
        rlSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(context, spinnerList);
                mAdapter.refreshData(spinnerList, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(context);
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(rlSpinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(rlSpinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        map.setDeductionScore(spinnerList.get(pos));
                        if (Integer.parseInt(map.getDeductionScore())>0) {
                            llDeductionReason.setVisibility(View.VISIBLE);
                        }else {
                            llDeductionReason.setVisibility(View.GONE);
                        }
                        tvSpinner.setText(spinnerList.get(pos));
                        //然后循环 计算分数
                        //deduct_marks扣分 score得分
                        if (marking.equals("deduct_marks")) {
                            int count= 0 ;
                            int totalScore = 0;
                            for (GraduateExamQuestion graduateExamQuestion : list) {
                                if (graduateExamQuestion.getDataType().equals("content")){
                                    totalScore += Integer.parseInt(graduateExamQuestion.getStandardScore());
                                    if (graduateExamQuestion.getDeductionScore() != null) {
                                        count += Integer.parseInt(graduateExamQuestion.getDeductionScore());
                                    }
                                }
                            }
                            //更新
                            final int finalCount = totalScore - count;
                            context.setTotalScore(finalCount);
                        } else {

                        }

                    }
                });

            }
        });
    }

    class ViewHolder {
        TextView tvQuestionTitle;
        RelativeLayout rlSpinner;
        TextView tvSpinner;
        LinearLayout llQuestionContent;
        LinearLayout llDeductionReason;
        TextView tvQuestionContent;
        TextView tvStandardScore;
        TextView tvDeduction;
        EditText etDeductionReason;
        LinearLayout llScore;
    }

}
