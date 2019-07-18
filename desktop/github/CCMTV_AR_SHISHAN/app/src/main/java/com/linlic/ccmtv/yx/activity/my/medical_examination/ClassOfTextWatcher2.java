package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper_text;
import com.linlic.ccmtv.yx.widget.AutoLinefeedLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */
public  class ClassOfTextWatcher2 implements TextWatcher {

    private EditText view;
    private Examination_paper examination_paper ;
    private AutoLinefeedLayout linearLayout;
    public ClassOfTextWatcher2(View view,Examination_paper examination_paper,AutoLinefeedLayout linearLayout) {

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

        if (linearLayout instanceof AutoLinefeedLayout)
            this.linearLayout = (AutoLinefeedLayout) linearLayout;
        else
            throw new ClassCastException(
                    "view must be an instance Of Examination_paper");
    }



    @Override
    public void afterTextChanged(Editable s) {
        List<String> stringList = new ArrayList<>();
        AutoLinefeedLayout parentLayout = (AutoLinefeedLayout) view.getParent();
//        LinearLayout parentLayout = (LinearLayout) view.getParent();
        TextView textView1 = (TextView)parentLayout.getChildAt(0);
        TextView textView2 = (TextView)parentLayout.getChildAt(1);
        int j = 0 ;
        for (int i = 0;i<parentLayout.getChildCount();i++){
            if( parentLayout.getChildAt(i) instanceof EditText){
                j++;
               EditText editText = (EditText) parentLayout.getChildAt(i);
                if(editText.getText().toString().trim().length()>0){
                    stringList.add(editText.getText().toString().trim());
                    if(editText == view){
                        examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).getEdtext().set(j-1,editText.getText().toString().trim());
                    }
                }else{
                    if(editText == view){
                        examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).getEdtext().set(j-1,"");
                    }
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
            examination_paper.getStatus().set(Integer.parseInt(examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).getSerial_number())-1,1);
        }else{
            MyDbUtils.saveExamination_script(view.getContext(), textView2.getText().toString(), textView1.getText().toString(), " ");

            examination_paper.getStatus().set(Integer.parseInt(examination_paper.getProblems().get(Integer.parseInt(linearLayout.getTag().toString())).getSerial_number())-1,0);
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