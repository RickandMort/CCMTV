package com.linlic.ccmtv.yx.activity.entity;

import android.util.Log;

import com.linlic.ccmtv.yx.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class Problem_text {
    private String title_serial_number = "";
    private String question_type="";
    private String question_type_text="";
    private String problem="";
    private String true_answer="";
    private String user_answer="";
    private List<Option_text> options = new ArrayList<Option_text>();
    private List<String> edtext = new ArrayList<String>();
    private List<String> thumbnails = new ArrayList<String>();
    private List<String> pictures = new ArrayList<String>();
    private List<String> correct_answer_thumbnails = new ArrayList<String>();
    private List<String> correct_answer_pictures = new ArrayList<String>();
    private List<Problem_text> problems = new ArrayList<>();
    private String serial_number="";
    private int position = 0;

    public Problem_text(int position){
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.edtext.add("");
        this.position = position;
    }
    public void setProblem(JSONObject json){
        try{

            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.problem = json.has("question")?json.getString("question"):""   ;
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")){
                this.problem += json.has("title")?json.getString("title"):""   ;
            }
            this.true_answer = json.has("true_answer")?json.getString("true_answer"):"";
            this.user_answer = json.has("user_answer") ? json.getString("user_answer"):"";

            if(json.has("img_url")){
               JSONArray  jsonarray =  json.getJSONArray("m_img_url");
               JSONArray  jsonarray2 =  json.getJSONArray("img_url");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    thumbnails.add(jsonarray.getString(i));
                    pictures.add(jsonarray2.getString(i));
                }
            }

            if(json.has("correct_answer_img_url")){
                JSONArray  jsonarray =  json.getJSONArray("correct_answer_m_img_url");
                JSONArray  jsonarray2 =  json.getJSONArray("correct_answer_img_url");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    correct_answer_thumbnails.add(jsonarray.getString(i));
                    correct_answer_pictures.add(jsonarray2.getString(i));
                }
            }
            if(json.has("title_config")){
                for (int i =0;i<json.getJSONArray("title_config").length();i++) {
                    JSONObject optionJson = json.getJSONArray("title_config").getJSONObject(i);
//                    LogUtil.e("选项答案",optionJson.toString());
                    Option_text option = new Option_text();
                    option.setOption(optionJson);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem_text problem = new Problem_text(i);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8){
              if(json.has("config")){
                  for (int i =0;i<json.getJSONArray("config").length();i++) {
                      JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                      LogUtil.e("选项答案",optionJson.toString());
                      Option_text option = new Option_text();
                      option.setOption(optionJson);
                      this.options.add(option);
                  }
              }
            }



            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
        }catch (Exception e){
            e.printStackTrace();
        }
    
    }

    public void setProblem(JSONObject json,List<Option_text> options1){
        try{
//            Log.e("看看题目",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.problem = json.has("question")?json.getString("question"):""   ;
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")){
                this.problem += json.has("title")?json.getString("title"):""   ;
            }
            this.true_answer = json.has("true_answer")?json.getString("true_answer"):"";
            this.user_answer = json.has("user_answer") ? json.getString("user_answer"):"";
//            this.setOptions(options1);
            for (Option_text option: options1) {
                Option_text optionNew = new Option_text(option.getOption_id(),option.getOption_type(),option.getOption_text());
                this.options.add(optionNew);
            }
            if(json.has("img_url")){
                JSONArray  jsonarray =  json.getJSONArray("m_img_url");
                JSONArray  jsonarray2 =  json.getJSONArray("img_url");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    thumbnails.add(jsonarray.getString(i));
                    pictures.add(jsonarray2.getString(i));
                }
            }

            if(json.has("correct_answer_img_url")){
                JSONArray  jsonarray =  json.getJSONArray("correct_answer_m_img_url");
                JSONArray  jsonarray2 =  json.getJSONArray("correct_answer_img_url");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    correct_answer_thumbnails.add(jsonarray.getString(i));
                    correct_answer_pictures.add(jsonarray2.getString(i));
                }
            }

            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem_text problem = new Problem_text(i);
                    problem.setProblem(jsonarray.getJSONObject(i));
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8){
                if(json.has("config")){
                    for (int i =0;i<json.getJSONArray("config").length();i++) {
                        JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                        LogUtil.e("选项答案",optionJson.toString());
                        Option_text option = new Option_text();
                        option.setOption(optionJson);
                        this.options.add(option);
                    }
                }
            }
            if(json.has("title_config")){
                for (int i =0;i<json.getJSONArray("title_config").length();i++) {
                    JSONObject optionJson = json.getJSONArray("title_config").getJSONObject(i);
//                    LogUtil.e("选项答案",optionJson.toString());
                    Option_text option = new Option_text();
                    option.setOption(optionJson);
                    this.options.add(option);
                }
            }


            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
            this.edtext.add("");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Problem_text> getProblems() {
        return problems;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public void setProblems(List<Problem_text> problems) {
        this.problems = problems;
    }

    public List<String> getEdtext() {
        return edtext;
    }

    public void setEdtext(List<String> edtext) {
        this.edtext = edtext;
    }

    public String getTitle_serial_number() {
        return title_serial_number;
    }

    public void setTitle_serial_number(String title_serial_number) {
        this.title_serial_number = title_serial_number;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion_type_text() {
        return question_type_text;
    }

    public void setQuestion_type_text(String question_type_text) {
        this.question_type_text = question_type_text;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public List<Option_text> getOptions() {
        return options;
    }

    public void setOptions(List<Option_text> options) {
        this.options = options;
    }

    public String getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(String true_answer) {
        this.true_answer = true_answer;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<String> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<String> getCorrect_answer_thumbnails() {
        return correct_answer_thumbnails;
    }

    public void setCorrect_answer_thumbnails(List<String> correct_answer_thumbnails) {
        this.correct_answer_thumbnails = correct_answer_thumbnails;
    }

    public List<String> getCorrect_answer_pictures() {
        return correct_answer_pictures;
    }

    public void setCorrect_answer_pictures(List<String> correct_answer_pictures) {
        this.correct_answer_pictures = correct_answer_pictures;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "title_serial_number='" + title_serial_number + '\'' +
                ", question_type='" + question_type + '\'' +
                ", question_type_text='" + question_type_text + '\'' +
                ", problem='" + problem + '\'' +
                ", true_answer='" + true_answer + '\'' +
                ", user_answer='" + user_answer + '\'' +
                ", options=" + options +
                ", edtext=" + edtext +
                ", thumbnails=" + thumbnails +
                ", pictures=" + pictures +
                ", correct_answer_thumbnails=" + correct_answer_thumbnails +
                ", correct_answer_pictures=" + correct_answer_pictures +
                ", problems=" + problems +
                ", serial_number='" + serial_number + '\'' +
                ", position=" + position +
                '}';
    }
}
