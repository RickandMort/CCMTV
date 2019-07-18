package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/8.
 * 选项
 */
public class Option {
    private String option_id="";
    private String option_text="";
    private String option_type="";
    private Problem problem;
    private Examination_paper examination_paper;

    public void  setOption(JSONObject json,Examination_paper examination_paper,Problem problem){
        try {
            this.option_id  = json.has("option_id")?json.getString("option_id"):"";
            this.option_text  = json.has("option_name")?json.getString("option_name"):"" ;
            this.option_type =  json.has("option_type") ? json.getString("option_type"):"0";
            this.option_type =  json.has("checked") ? json.getString("checked"):"0";
            this.examination_paper = examination_paper;
            this.problem = problem;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void  setOption2(JSONObject json,Examination_paper examination_paper,Problem problem){
        try {
            this.option_id  = json.has("option_id")?json.getString("option_id"):"";
            this.option_text  = json.has("option_name")?json.getString("option_name"):"" ;
            this.option_type =  json.has("option_type") ? json.getString("option_type"):"0";
            this.examination_paper = examination_paper;
            this.problem = problem;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public Option(String option_id,String option_type,String option_text ){
        this.option_id = option_id;
        this.option_type = option_type;
        this.option_text = option_text;
    }

    public Option(){
        super();
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_text() {
        return option_text;
    }

    public void setOption_text(String option_text) {
        this.option_text = option_text;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
        if(!this.problem.getQuestion_type().equals("2")) {//判断是否等于2
            if(this.option_type.equals("0")){//未选中
                this.examination_paper.getStatus().set(Integer.parseInt(this.problem.getSerial_number())-1, 0);
            }else{//选中
                this.examination_paper.getStatus().set(Integer.parseInt(this.problem.getSerial_number())-1, 1);
            }
        }
        if(this.problem.getQuestion_type().equals("2") ||this.problem.getQuestion_type().equals("13")){
            //类型 2   多选框
            int i = 0;
            for(Option option:this.problem.getOptions()){//循环题目下的所有选项
                if(option.getOption_type().equals("1")){//判断选项状态
                    i++;
                }
            }
            if(i > 0){//判断 复选框 是否有大于一个被选中
                this.examination_paper.getStatus().set(Integer.parseInt(this.problem.getSerial_number())-1, 1);
            }else{
                this.examination_paper.getStatus().set(Integer.parseInt(this.problem.getSerial_number())-1, 0);
            }
        }

    }
    public void setOption_type2(String option_type) {
        this.option_type = option_type;
    }


    @Override
    public String toString() {
        return "Option{" +
                "option_id='" + option_id + '\'' +
                ", option_text='" + option_text + '\'' +
                ", option_type='" + option_type + '\'' +
                '}';
    }
}
