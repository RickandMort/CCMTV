package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public  class ClassOfTextWatcher implements TextWatcher {

    private EditText view;
    private Examination_paper examination_paper ;
    private LinearLayout linearLayout;

    public ClassOfTextWatcher(View view,Examination_paper examination_paper,LinearLayout linearLayout) {

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
        LinearLayout parentLayout = (LinearLayout) view.getParent();
        TextView textView1 = (TextView)parentLayout.getChildAt(0);
        TextView textView2 = (TextView)parentLayout.getChildAt(1);
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
                MyDbUtils.saveExamination_script(view.getContext(), textView2.getText().toString(), textView1.getText().toString(), sss);
            }else{
                MyDbUtils.saveExamination_script(view.getContext(), textView2.getText().toString(), textView1.getText().toString(), stringList.get(0));
            }
            examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).setUser_answer( stringList.get(0));
//            submit_server(view.getContext(),textView1.getText().toString(), stringList.get(0));
        }else{
            examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).setUser_answer("");
            MyDbUtils.deleteExamination_scriptALL(view.getContext(), textView2.getText().toString(), textView1.getText().toString());
//            submit_server(view.getContext(),textView1.getText().toString(), "");
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