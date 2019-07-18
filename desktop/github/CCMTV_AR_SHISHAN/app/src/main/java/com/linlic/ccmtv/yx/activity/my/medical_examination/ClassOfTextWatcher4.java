package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public  class ClassOfTextWatcher4 implements TextWatcher {

    private EditText view;
    private Problem problem ;
    private String eid;

    public ClassOfTextWatcher4(View view, Problem problem , String eid) {

        if (view instanceof EditText)
            this.view = (EditText) view;
        else
            throw new ClassCastException(
                    "view must be an instance Of TextView");

        if (problem instanceof Problem)
            this.problem = (Problem) problem;
        else
            throw new ClassCastException(
                    "view must be an instance Of Examination_paper");
        this.eid = eid;
    }

    @Override
    public void afterTextChanged(Editable s) {
        List<String> stringList = new ArrayList<>();
        LinearLayout parentLayout = (LinearLayout) view.getParent();

        for (int i = 0;i<parentLayout.getChildCount();i++){
            if( parentLayout.getChildAt(i) instanceof EditText){
               EditText editText = (EditText) parentLayout.getChildAt(i);
                if(editText.getText().toString().trim().length()>0){
                    stringList.add(editText.getText().toString().trim());
                }

            }
        }
        if(stringList.size()>0){
            if(stringList.size()>1){
                String sss = ""+stringList.get(0);
                for(int i = 1;i < stringList.size();i++){
                    sss +=  "$$"+stringList.get(i);
                }
                MyDbUtils.saveExamination_script(view.getContext(), eid,problem.getTitle_serial_number(), sss);
            }else{
                MyDbUtils.saveExamination_script(view.getContext(), eid, problem.getTitle_serial_number(), stringList.get(0));
            }
            problem.setUser_answer( stringList.get(0));
        }else{
            problem.setUser_answer("");
            MyDbUtils.deleteExamination_scriptALL(view.getContext(),eid,problem.getTitle_serial_number());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {

    }

}