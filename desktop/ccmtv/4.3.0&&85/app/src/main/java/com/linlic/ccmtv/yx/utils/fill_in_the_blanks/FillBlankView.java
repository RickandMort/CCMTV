package com.linlic.ccmtv.yx.utils.fill_in_the_blanks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Parcel;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Mock_exam;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.List;

/**
 * 填空题
 * Created by tom on 2017/11/10.
 */

public class FillBlankView extends RelativeLayout {

    private TextView tvContent;
    private Context context;
    private BaseListAdapter baseListAdapter;

    // 答案集合
//    private List<String> answerList;
    // 答案范围集合
//    private List<AnswerRange> rangeList;
    // 填空题内容
//    private SpannableStringBuilder content;

    private Problem problem;

    public FillBlankView(Context context) {
        this(context, null);
    }

    public FillBlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillBlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setBaseListAdapter(BaseListAdapter baseListAdapter) {
        this.baseListAdapter = baseListAdapter;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_fill_blank, this);
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    /**
     * 设置数据
     *
     * @param
     */
    public void setData( ) {
//        this.problem = problem;
        if ( this.problem == null ||  this.problem.getSerial_number().length()<0) {
            return;
        }
//        Log.e("答案范围集合", this.problem.getRangeList().toString());
        for (AnswerRange answerRange : this.problem.getRangeList()){
//            Log.e("答案范围",answerRange.start +"-"+answerRange.end);
        }
//        Log.e("答案集合", this.problem.getAnswerList().toString());
//        Log.e("填空题内容", this.problem.getContent().toString());

        for (AnswerRange answerRange : this.problem.getSubs()){
//            Log.e("下标",answerRange.start+"-"+ answerRange.end);
        }
        for (AnswerRange answerRange : this.problem.getSups()){
//            Log.e("上标",answerRange.start+"-"+ answerRange.end);
        }
        // 设置填空处点击事件
        for (int i = 0; i <  this.problem.getRangeList().size(); i++) {
            AnswerRange range =  this.problem.getRangeList().get(i);
            problem.getContent().setSpan(new UnderlineSpan(),
                    range.start, range.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            FillBlankView.BlankClickableSpan blankClickableSpan = new FillBlankView.BlankClickableSpan(i,problem,tvContent);
            this.problem.getContent().setSpan(blankClickableSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 设置此方法后，点击事件才能生效
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(problem.getContent() );
    }

    /**
     * 点击事件
     */
    class BlankClickableSpan extends ClickableSpan {

        private int position;
        private Problem problem;
        TextView tvContent;

        public BlankClickableSpan(int position,Problem problem,TextView textView) {
            this.position = position;
            this.problem = problem;
            this.tvContent = textView;
        }

        @Override
        public void onClick(final View widget) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_input, null);
            final EditText etInput = (EditText) view.findViewById(R.id.et_answer);
            Button btnFillBlank = (Button) view.findViewById(R.id.btn_fill_blank);

            // 显示原有答案
            String oldAnswer = problem.getAnswerList().get(position);
            if (!TextUtils.isEmpty(oldAnswer)) {
                etInput.setText(oldAnswer);
                etInput.setSelection(oldAnswer.length());
            }

            final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, dp2px(40));
            // 获取焦点
            popupWindow.setFocusable(true);
            // 为了防止弹出菜单获取焦点之后，点击Activity的其他组件没有响应
            popupWindow.setBackgroundDrawable(new PaintDrawable());
            // 设置PopupWindow在软键盘的上方
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 弹出PopupWindow
            popupWindow.showAtLocation(tvContent, Gravity.BOTTOM, 0, 0);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);

            btnFillBlank.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 填写答案
                    String answer = etInput.getText().toString();
                    fillAnswer(answer, position);
//
                    popupWindow.setFocusable(false);
                    baseListAdapter.notifyDataSetChanged();
                    // 关闭软键盘
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                      etInput.setFocusable(false);//设置输入框不可聚焦，即失去焦点和光标
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(etInput.getWindowToken(), 0);// 隐藏输入法
                    }

                    popupWindow.dismiss();

                }
            });

            // 显示软键盘
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // 不显示下划线
            ds.setUnderlineText(true);
        }

        /**
         * 填写答案
         *
         * @param answer   当前填空处答案
         * @param position 填空位置
         */
        public void fillAnswer(String answer, int position) {
            answer = " " + answer + " ";
            if(answer.trim().length()<1){
                answer = "__________";
                // 替换答案
                AnswerRange range = problem.getRangeList().get(position);
                problem.getContent().replace(range.start, range.end, answer);

                // 更新当前的答案范围
                AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
                problem.getRangeList().set(position, currentRange);


                // 将答案添加到集合中
                problem.getAnswerList().set(position, "");

                // 更新内容
                tvContent.setText (problem.getContent() );

                for (int i = 0; i < problem.getRangeList().size(); i++) {
                    if (i > position) {
                        // 获取下一个答案原来的范围
                        AnswerRange oldNextRange = problem.getRangeList().get(i);
                        int oldNextAmount = oldNextRange.end - oldNextRange.start;
                        // 计算新旧答案字数的差值
                        int difference = currentRange.end - range.end;

                        // 更新下一个答案的范围
                        AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                                oldNextRange.start + difference + oldNextAmount);
                        problem.getRangeList().set(i, nextRange);
                    }
                }
                LogUtil.e("填空题内容",problem.getAnswerList().toString());
                //更新状态
                if(problem.getAnswerList().size()>0){
                    String sss = "";
                    boolean is = false;
                    for (String str :problem.getAnswerList()){
                        if(str.length()>0){
                            is = true;
                            if(sss.length()>0){
                                sss +="$$"+str;
                            }else {
                                sss +=str;
                            }
                        }else{
                            if(sss.length()>0){
                                sss +="$$ ";
                            }else {
                                sss +=" ";
                            }
                        }
                    }
                    if(!is){
                        sss="";
                    }

                    if(sss.length()>0){
                        MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), sss);
                        problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,1);
                        submit_server( problem.getTitle_serial_number(),sss);
                    }else{
                        MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                        problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                        submit_server( problem.getTitle_serial_number(),"");
                    }

                }else{
                    MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                    problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                    submit_server( problem.getTitle_serial_number(),"");
                }
            }else {
                // 替换答案
                AnswerRange range = problem.getRangeList().get(position);
                problem.getContent().replace(range.start, range.end, answer);

                // 更新当前的答案范围
                AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
                problem.getRangeList().set(position, currentRange);

                // 答案设置下划线
                problem.getContent().setSpan(new UnderlineSpan(),
                        currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Log.e("下划线位置",currentRange.start+"-"+currentRange.end);

                // 将答案添加到集合中
                problem.getAnswerList().set(position, answer.replace(" ", ""));

                // 更新内容
                tvContent.setText (problem.getContent() );

                for (int i = 0; i < problem.getRangeList().size(); i++) {
                    if (i > position) {
                        // 获取下一个答案原来的范围
                        AnswerRange oldNextRange = problem.getRangeList().get(i);
                        int oldNextAmount = oldNextRange.end - oldNextRange.start;
                        // 计算新旧答案字数的差值
                        int difference = currentRange.end - range.end;

                        // 更新下一个答案的范围
                        AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                                oldNextRange.start + difference + oldNextAmount);
                        problem.getRangeList().set(i, nextRange);
                    }
                }
                LogUtil.e("填空题内容",problem.getAnswerList().toString());
                //更新状态
                if(problem.getAnswerList().size()>0){
                    String sss = "";
                    boolean is = false;
                    for (String str :problem.getAnswerList()){
                        if(str.length()>0){
                            is = true;
                            if(sss.length()>0){
                                sss +="$$"+str;
                            }else {
                                sss +=str;
                            }
                        }else{
                            if(sss.length()>0){
                                sss +="$$ ";
                            }else {
                                sss +=" ";
                            }
                        }
                    }
                    if(!is){
                        sss="";
                    }
                    if(problem.getExamination_paper().getEid().length()>0) {
                        if(sss.length()>0){
                            MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), sss);
                            problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number()) - 1, 1);
                            submit_server( problem.getTitle_serial_number(),sss);
                        }else{
                            MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                            problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                            submit_server( problem.getTitle_serial_number(),"");
                        }
                    }
                }else{
                    MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                    problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                    submit_server( problem.getTitle_serial_number(),"");
                }
            }

        }
    }

    public void submit_server(final String qid, final String answer){
        if(Mock_exam.is_disasterSave<1){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.disasterSave);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("qid",qid);
                        obj.put("redis_key",problem.getExamination_paper().getRedis_key());
                        obj.put("answer",answer);
                        obj.put("remain_time",problem.getExamination_paper().getSs()*60+problem.getExamination_paper().getMinss());

                        LogUtil.e("用户作答-提交服务器上行",obj.toString());
                        //测试暂时封掉
                        String result = HttpClientUtils.sendPost(context,
                                URLConfig.Medical_examination, obj.toString());
                        LogUtil.e("用户作答-提交服务器下行",result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        }
    }

    /**
     * 获取答案列表
     *
     * @return 答案列表
     */
    public List<String> getAnswerList() {
        return problem.getAnswerList();
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 填写答案
     *
     * @param answer   当前填空处答案
     * @param position 填空位置
     */
    public void fillAnswer(String answer, int position) {
        answer = " " + answer + " ";
        if(answer.trim().length()<1){
            answer = "__________";
            // 替换答案
            AnswerRange range = problem.getRangeList().get(position);
            problem.getContent().replace(range.start, range.end, answer);

            // 更新当前的答案范围
            AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
            problem.getRangeList().set(position, currentRange);


            // 将答案添加到集合中
            problem.getAnswerList().set(position, "");

            // 更新内容
            tvContent.setText (problem.getContent() );

            for (int i = 0; i < problem.getRangeList().size(); i++) {
                if (i > position) {
                    // 获取下一个答案原来的范围
                    AnswerRange oldNextRange = problem.getRangeList().get(i);
                    int oldNextAmount = oldNextRange.end - oldNextRange.start;
                    // 计算新旧答案字数的差值
                    int difference = currentRange.end - range.end;

                    // 更新下一个答案的范围
                    AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                            oldNextRange.start + difference + oldNextAmount);
                    problem.getRangeList().set(i, nextRange);
                }
            }
            LogUtil.e("填空题内容",problem.getAnswerList().toString());
            //更新状态
            if(problem.getAnswerList().size()>0){
                String sss = "";
                boolean is = false;
                for (String str :problem.getAnswerList()){
                    if(str.length()>0){
                        is = true;
                        if(sss.length()>0){
                            sss +="$$"+str;
                        }else {
                            sss +=str;
                        }
                    }else{
                        if(sss.length()>0){
                            sss +="$$ ";
                        }else {
                            sss +=" ";
                        }
                    }
                }
                if(!is){
                    sss="";
                }

                if(sss.length()>0){
                    MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), sss);
                    problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,1);
                    submit_server( problem.getTitle_serial_number(),sss);
                }else{
                    MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                    problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                    submit_server( problem.getTitle_serial_number(),"");
                }

            }else{
                MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                submit_server( problem.getTitle_serial_number(),"");
            }
        }else {
            // 替换答案
            AnswerRange range = problem.getRangeList().get(position);
            problem.getContent().replace(range.start, range.end, answer);

            // 更新当前的答案范围
            AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
            problem.getRangeList().set(position, currentRange);

            // 答案设置下划线
            problem.getContent().setSpan(new UnderlineSpan(),
                    currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            Log.e("下划线位置",currentRange.start+"-"+currentRange.end);

            // 将答案添加到集合中
            problem.getAnswerList().set(position, answer.replace(" ", ""));

            // 更新内容
            tvContent.setText (problem.getContent() );

            for (int i = 0; i < problem.getRangeList().size(); i++) {
                if (i > position) {
                    // 获取下一个答案原来的范围
                    AnswerRange oldNextRange = problem.getRangeList().get(i);
                    int oldNextAmount = oldNextRange.end - oldNextRange.start;
                    // 计算新旧答案字数的差值
                    int difference = currentRange.end - range.end;

                    // 更新下一个答案的范围
                    AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                            oldNextRange.start + difference + oldNextAmount);
                    problem.getRangeList().set(i, nextRange);
                }
            }
            LogUtil.e("填空题内容",problem.getAnswerList().toString());
            //更新状态
            if(problem.getAnswerList().size()>0){
                String sss = "";
                boolean is = false;
                for (String str :problem.getAnswerList()){
                    if(str.length()>0){
                        is = true;
                        if(sss.length()>0){
                            sss +="$$"+str;
                        }else {
                            sss +=str;
                        }
                    }else{
                        if(sss.length()>0){
                            sss +="$$ ";
                        }else {
                            sss +=" ";
                        }
                    }
                }
                if(!is){
                    sss="";
                }
                if(problem.getExamination_paper().getEid().length()>0) {
                    if(sss.length()>0){
                        MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), sss);
                        problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number()) - 1, 1);
                        submit_server( problem.getTitle_serial_number(),sss);
                    }else{
                        MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                        problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                        submit_server( problem.getTitle_serial_number(),"");
                    }
                }
            }else{
                MyDbUtils.saveExamination_script(context, problem.getExamination_paper().getEid(), problem.getTitle_serial_number(), " ");
                problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number())-1,0);
                submit_server( problem.getTitle_serial_number(),"");
            }
        }

    }
}
