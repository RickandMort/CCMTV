package com.linlic.ccmtv.yx.activity.entity;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class Examination_paper_text {
    private String examination_instructions_name="";
    private String examination_instructions="";
    private String total_score_of_examination="";
    private String eid="";
    private int number_of_topics= 0;//题目数量
    private List<Problem_text> problems = new ArrayList<Problem_text>();
    public void setExamination_paper(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("eid")?json.getString("eid"):"" ;

            if(json.has("data")){
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
                    Problem_text problem =new Problem_text(i);
                    problem.setProblem(problemJson);
                    this.problems.add(problem);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }

    }

    public String getExamination_instructions_name() {
        return examination_instructions_name;
    }

    public void setExamination_instructions_name(String examination_instructions_name) {
        this.examination_instructions_name = examination_instructions_name;
    }

    public int getNumber_of_topics() {
        return number_of_topics;
    }

    public void setNumber_of_topics(int number_of_topics) {
        this.number_of_topics = number_of_topics;
    }

    public String getExamination_instructions() {
        return examination_instructions;
    }

    public void setExamination_instructions(String examination_instructions) {
        this.examination_instructions = examination_instructions;
    }

    public String getTotal_score_of_examination() {
        return total_score_of_examination;
    }

    public void setTotal_score_of_examination(String total_score_of_examination) {
        this.total_score_of_examination = total_score_of_examination;
    }

    public List<Problem_text> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem_text> problems) {
        this.problems = problems;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    @Override
    public String toString() {
        return "Examination_paper{" +
                "examination_instructions_name='" + examination_instructions_name + '\'' +
                ", examination_instructions='" + examination_instructions + '\'' +
                ", total_score_of_examination='" + total_score_of_examination + '\'' +
                ", eid='" + eid + '\'' +
                ", number_of_topics=" + number_of_topics +
                ", problems=" + problems +
                '}';
    }
}
