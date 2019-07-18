package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public  class ClassOfTextWatcher5 implements TextWatcher {

    private EditText view;
    private Examination_paper examination_paper ;
    private LinearLayout linearLayout;

    public ClassOfTextWatcher5(View view, Examination_paper examination_paper, LinearLayout linearLayout) {

        if (view instanceof EditText)
            this.view = (EditText) view;
        else
            throw new ClassCastException(
                    "view must be an instance Of TextView");

        if (examination_paper instanceof Examination_paper)
            this.examination_paper = (Examination_paper) examination_paper;
        else
            throw new ClassCastException(
                    "view must be an instance Of Examination_paper");

        if (linearLayout instanceof LinearLayout)
            this.linearLayout = (LinearLayout) linearLayout;
        else
            throw new ClassCastException(
                    "view must be an instance Of Examination_paper");
    }

    @Override
    public void afterTextChanged(Editable s) {
        List<String> stringList = new ArrayList<>();

        stringList.add(s.toString().trim());
        if(stringList.size()>0){
            examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).setUser_answer( stringList.get(0));
        }else{
            examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).setUser_answer("");
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