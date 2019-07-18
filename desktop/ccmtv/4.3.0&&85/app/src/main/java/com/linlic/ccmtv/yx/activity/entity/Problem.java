package com.linlic.ccmtv.yx.activity.entity;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.fill_in_the_blanks.AnswerRange;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class Problem {
    private String title_serial_number = "";
    private String question_type="";
    private String question_type_text="";
    private String problem="";
    private String problem2="";
    private String true_answer="";
    private String user_answer="";
    private List<Option> options = new ArrayList<Option>();
    private List<String> answerList = new ArrayList<String>();//填空题答案集合类
    private List<String> edtext = new ArrayList<String>();
    private List<String> thumbnails = new ArrayList<String>();
    private List<String> pictures = new ArrayList<String>();
    private List<String> correct_answer_thumbnails = new ArrayList<String>();
    private List<String> correct_answer_pictures = new ArrayList<String>();
    private List<Problem> problems = new ArrayList<>();
    private String serial_number="";
    private int position = 0;
    private SpannableStringBuilder content = new SpannableStringBuilder();
    private List<AnswerRange> rangeList = new ArrayList<>();
    private List<AnswerRange> subs = new ArrayList<>();
    private List<AnswerRange> sups =  new ArrayList<>();
    private Examination_paper examination_paper;
    private String color="#";
    private String is_collect = "0";

    public Problem(int position,Examination_paper examination_paper){
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
        this.examination_paper = examination_paper;

    }
    public void setProblem(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")||this.question_type.equals("13")||this.question_type.equals("113")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("true_answer")?json.getString("true_answer"):"";
            this.user_answer = json.has("user_answer") ? json.getString("user_answer"):"";
            this.user_answer = json.has("my_answer") ? json.getString("my_answer"):"";

            if(this.user_answer.length()>0){
                this.examination_paper.getStatus().set(Integer.parseInt(this.serial_number)-1,1);
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
            if(json.has("title_config")){
                for (int i =0;i<json.getJSONArray("title_config").length();i++) {
                    JSONObject optionJson = json.getJSONArray("title_config").getJSONObject(i);
//                    LogUtil.e("选项答案",optionJson.toString());
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8 ||Integer.parseInt(this.question_type) ==12 ||Integer.parseInt(this.question_type) ==13){
              if(json.has("config")){
                  for (int i =0;i<json.getJSONArray("config").length();i++) {
                      JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                      LogUtil.e("选项答案",optionJson.toString());
                      Option option = new Option();
                      option.setOption(optionJson,examination_paper,this);
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

    public void setProblem2(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.is_collect = json.has("is_collect")?json.getString("is_collect"):"0"  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem3( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("answer")?json.getString("answer"):"";
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
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8||Integer.parseInt(this.question_type) ==12){
              if(json.has("config")){
                  for (int i =0;i<json.getJSONArray("config").length();i++) {
                      JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                      LogUtil.e("选项答案",optionJson.toString());
                      Option option = new Option();
                      option.setOption(optionJson,examination_paper,this);
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
    public void setProblem3(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.is_collect = json.has("is_collect")?json.getString("is_collect"):"0"  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("answer")?json.getString("answer"):"";
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
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8||Integer.parseInt(this.question_type) ==12){
              if(json.has("config")){
                  for (int i =0;i<json.getJSONArray("config").length();i++) {
                      JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                      LogUtil.e("选项答案",optionJson.toString());
                      Option option = new Option();
                      option.setOption(optionJson,examination_paper,this);
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
    public void setProblem5(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.is_collect = json.has("is_collect")?json.getString("is_collect"):"0"  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem5( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")||this.question_type.equals("113")||this.question_type.equals("13")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("answer")?json.getString("answer"):"";
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
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8||Integer.parseInt(this.question_type) ==12||Integer.parseInt(this.question_type) ==13){
                if(json.has("config")){
                    for (int i =0;i<json.getJSONArray("config").length();i++) {
                        JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                        LogUtil.e("选项答案",optionJson.toString());
                        Option option = new Option();
                        option.setOption(optionJson,examination_paper,this);
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
    public void setProblem6(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.is_collect = json.has("is_collect")?json.getString("is_collect"):"0"  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem5( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")||this.question_type.equals("113")||this.question_type.equals("13")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("answer")?json.getString("answer"):"";
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
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8||Integer.parseInt(this.question_type) ==12||Integer.parseInt(this.question_type) ==13){
                if(json.has("config")){
                    for (int i =0;i<json.getJSONArray("config").length();i++) {
                        JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                        LogUtil.e("选项答案",optionJson.toString());
                        Option option = new Option();
                        option.setOption2(optionJson,examination_paper,this);
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

    public void setProblem4(JSONObject json){
        try{
//            Log.e("setProblem",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.is_collect = json.has("is_collect")?json.getString("is_collect"):"0"  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem4( json.has("question")?json.getString("question"):"" );
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")|| this.question_type.equals("107")||this.question_type.equals("111")||this.question_type.equals("108")){
                this.setProblem( this.problem + (json.has("title")?json.getString("title"):""));
            }
            this.problem2  = json.has("title_a")?json.getString("title_a"):""   ;
            this.true_answer = json.has("answer")?json.getString("answer"):"";
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
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
                    this.options.add(option);
                }
            }
            if(json.has("questions")){
                JSONArray  jsonarray =  json.getJSONArray("questions");
                for (int i = 0 ; i<jsonarray.length();i++) {
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i),this.options);
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8||Integer.parseInt(this.question_type) ==12){
                if(json.has("config")){
                    for (int i =0;i<json.getJSONArray("config").length();i++) {
                        JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                        LogUtil.e("选项答案",optionJson.toString());
                        Option option = new Option();
                        option.setOption(optionJson,examination_paper,this);
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

    public void setProblem(JSONObject json,List<Option> options1){
        try{
//            Log.e("看看题目",json.toString());
            this.title_serial_number =json.has("id")?json.getString("id"):"";
            this.question_type = json.has("type")?json.getString("type"):""  ;
            this.serial_number = json.has("number")?json.getString("number"):""  ;
            this.question_type_text =json.has("type_name")?json.getString("type_name"):""  ;
            this.setProblem(json.has("question")?json.getString("question"):"" );
            this.problem2 = json.has("")?json.getString(""):""   ;
            if(this.question_type.equals("7")||this.question_type.equals("11")||this.question_type.equals("8")){
                this.setProblem(this.problem+(json.has("title")?json.getString("title"):"" ));
            }
            this.true_answer = json.has("true_answer")?json.getString("true_answer"):"";
            this.user_answer = json.has("user_answer") ? json.getString("user_answer"):"";
//            this.setOptions(options1);
            if(json.has("config")) {
                if(json.getJSONArray("config").length() < options1.size() ) {
                    for (Option option : options1) {
                        Option optionNew = new Option(option.getOption_id(), option.getOption_type(), option.getOption_text());
                        this.options.add(optionNew);
                    }
                }
            }else{
                for (Option option : options1) {
                    Option optionNew = new Option(option.getOption_id(), option.getOption_type(), option.getOption_text());
                    this.options.add(optionNew);
                }
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
                    Problem problem = new Problem(i,examination_paper);
                    problem.setProblem(jsonarray.getJSONObject(i));
                    problems.add(problem);
                }
            }

            if(Integer.parseInt(this.question_type) ==1 ||Integer.parseInt(this.question_type) ==2 ||Integer.parseInt(this.question_type) ==3 ||Integer.parseInt(this.question_type) ==7 ||Integer.parseInt(this.question_type) ==8){
                if(json.has("config")){
                    for (int i =0;i<json.getJSONArray("config").length();i++) {
                        JSONObject optionJson = json.getJSONArray("config").getJSONObject(i);
//                        LogUtil.e("选项答案",optionJson.toString());
                        Option option = new Option();
                        option.setOption(optionJson,examination_paper,this);
                        this.options.add(option);
                    }
                }
            }
            if(json.has("title_config")){
                for (int i =0;i<json.getJSONArray("title_config").length();i++) {
                    JSONObject optionJson = json.getJSONArray("title_config").getJSONObject(i);
//                    LogUtil.e("选项答案",optionJson.toString());
                    Option option = new Option();
                    option.setOption(optionJson,examination_paper,this);
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

    public List<AnswerRange> getSubs() {
        return subs;
    }

    public void setSubs(List<AnswerRange> subs) {
        this.subs = subs;
    }

    public List<AnswerRange> getSups() {
        return sups;
    }

    public void setSups(List<AnswerRange> sups) {
        this.sups = sups;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public String getProblem2() {
        return problem2;
    }

    public void setProblem2(String problem2) {
        this.problem2 = problem2;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public void setProblems(List<Problem> problems) {
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
        if(this.question_type.equals("4")){
            String font = this.question_type_text.substring(this.question_type_text.indexOf("<font color="),this.question_type_text.indexOf("[填空题]"));

            this.color = font.substring(13,20);
//            Log.e("截取",font+"========="+this.color);
            this.question_type_text = this.getQuestion_type_text().replaceFirst(font,"");
            this.question_type_text = this.getQuestion_type_text().replaceFirst("</font>","");
            String str = this.getQuestion_type_text()+this.getProblem();
            String[] tags = str.split("[$][$]");
            for(int i = 0;i <tags.length;i++){
                //找出所有的下标位置
                String context = tags[i];
                String subkey = "<sub>";
                String subkey2 = "</sub>";
                int a = context.indexOf(subkey);//*第一个出现的索引位置
                while (a >0) {
                    context = context.replaceFirst(subkey,"");
                    this.subs.add(new AnswerRange(a+ (this.content.length()>0? this.content.length():0), (this.content.length()>0? this.content.length():0)+ context.indexOf(subkey2)));
                    context = context.replaceFirst(subkey2,"");
                    a = context.indexOf(subkey);//*从这个索引往后开始第一个出现的位置
                }
                //找出所有的上标位置
                String supkey = "<sup>";
                String supkey2 = "</sup>";
                int p = context.indexOf(supkey);//*第一个出现的索引位置
                while (p >0) {
                    context = context.replaceFirst(supkey,"");
                    this.sups.add(new AnswerRange( (this.content.length()>0? this.content.length():0)+p,  (this.content.length()>0? this.content.length():0)+context.indexOf(supkey2)));
                    context = context.replaceFirst(supkey2,"");
                    p = context.indexOf(supkey);//*从这个索引往后开始第一个出现的位置
                }
                //将所有的数据处理完后复制给元数据
                tags[i] = context;
                //开始处理点击事件位置
                this.content.append(tags[i]);
                if(i != tags.length-1) {
                    int start = this.content.length();
                    this.content.append("________");
                    int end = this.content.length();
                    this.rangeList.add(new AnswerRange(start, end));
                }
            }

            // 设置文字颜色
            for (int i = 0; i<this.getRangeList().size();i++) {
                AnswerRange range  =  this.getRangeList().get(i);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
                this.getContent().setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 答案集合
            for (int i = 0; i < this.getRangeList().size(); i++) {
                this.getAnswerList().add("");
            }

            //设置类型文字描述颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
            this.getContent().setSpan(colorSpan, this.getContent().toString().indexOf("、")+1,this.getContent().toString().indexOf("、")+6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            // 设置下标
            for (int i = 0; i < this.getSubs().size(); i++) {
                AnswerRange range = this.getSubs().get(i);
                this.getContent().setSpan(new SubscriptSpan(), range.start, range.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                this.getContent().setSpan(new AbsoluteSizeSpan(12, true), range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 设置上标
            for (int i = 0; i < this.getSups().size(); i++) {
                AnswerRange range = this.getSups().get(i);
                SuperscriptSpan superscriptSpan = new SuperscriptSpan( );
                this.getContent().setSpan(superscriptSpan,  range.start, range.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        }
    }
    public void setProblem3(String problem) {
        this.problem = problem;
        if(this.question_type.equals("4")){

            String font = this.problem.substring(this.problem.indexOf("<font color="),this.problem.indexOf("[填空题]"));

            this.color = font.substring(13,20);
//            Log.e("截取",font+"========="+this.color);
            this.problem = this.problem.replaceFirst(font,"");
            this.problem = this.problem.replaceFirst("</font>","");
            String str =  this.getProblem();
            String[] tags = str.split("[$][$]");


           /* String str =this.getProblem();
            String[] tags = str.split("[$][$]");*/
            for(int i = 0;i <tags.length;i++){
                //找出所有的下标位置
                String context = tags[i];
                String subkey = "<sub>";
                String subkey2 = "</sub>";
                int a = context.indexOf(subkey);//*第一个出现的索引位置
                while (a >0) {
                    context = context.replaceFirst(subkey,"");
                    this.subs.add(new AnswerRange(a+ (this.content.length()>0? this.content.length():0), (this.content.length()>0? this.content.length():0)+ context.indexOf(subkey2)));
                    context = context.replaceFirst(subkey2,"");
                    a = context.indexOf(subkey);//*从这个索引往后开始第一个出现的位置
                }
                //找出所有的上标位置
                String supkey = "<sup>";
                String supkey2 = "</sup>";
                int p = context.indexOf(supkey);//*第一个出现的索引位置
                while (p >0) {
                    context = context.replaceFirst(supkey,"");
                    this.sups.add(new AnswerRange( (this.content.length()>0? this.content.length():0)+p,  (this.content.length()>0? this.content.length():0)+context.indexOf(supkey2)));
                    context = context.replaceFirst(supkey2,"");
                    p = context.indexOf(supkey);//*从这个索引往后开始第一个出现的位置
                }
                //将所有的数据处理完后复制给元数据
                tags[i] = context;
                //开始处理点击事件位置
                this.content.append(tags[i]);
                if(i != tags.length-1) {
                    int start = this.content.length();
                    this.content.append("__________");
                    int end = this.content.length();
                    this.rangeList.add(new AnswerRange(start, end));
                }
            }

            // 设置文字颜色
            for (int i = 0; i<this.getRangeList().size();i++) {
                AnswerRange range  =  this.getRangeList().get(i);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
                this.getContent().setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 答案集合
            for (int i = 0; i < this.getRangeList().size(); i++) {
                this.getAnswerList().add("");
            }




            // 设置下标
            for (int i = 0; i < this.getSubs().size(); i++) {
                AnswerRange range = this.getSubs().get(i);
                this.getContent().setSpan(new SubscriptSpan(), range.start, range.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                this.getContent().setSpan(new AbsoluteSizeSpan(12, true), range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 设置上标
            for (int i = 0; i < this.getSups().size(); i++) {
                AnswerRange range = this.getSups().get(i);
                SuperscriptSpan superscriptSpan = new SuperscriptSpan( );
                this.getContent().setSpan(superscriptSpan,  range.start, range.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        }
    }
    public void setProblem4(String problem) {
        this.problem = problem;
        if(this.question_type.equals("4")){
            String font = this.getProblem().substring(this.getProblem().indexOf("<font color="),this.getProblem().indexOf("[填空题]"));

            this.color = font.substring(13,20);
//            Log.e("截取",font+"========="+this.color);
            this.problem = this.getProblem().replaceFirst(font,"");
            this.problem =this.getProblem().replaceFirst("</font>","");
            String str = this.getProblem();
            String[] tags = str.split("[$][$]");
            for(int i = 0;i <tags.length;i++){
                //找出所有的下标位置
                String context = tags[i];
                String subkey = "<sub>";
                String subkey2 = "</sub>";
                int a = context.indexOf(subkey);//*第一个出现的索引位置
                while (a >0) {
                    context = context.replaceFirst(subkey,"");
                    this.subs.add(new AnswerRange(a+ (this.content.length()>0? this.content.length():0), (this.content.length()>0? this.content.length():0)+ context.indexOf(subkey2)));
                    context = context.replaceFirst(subkey2,"");
                    a = context.indexOf(subkey);//*从这个索引往后开始第一个出现的位置
                }
                //找出所有的上标位置
                String supkey = "<sup>";
                String supkey2 = "</sup>";
                int p = context.indexOf(supkey);//*第一个出现的索引位置
                while (p >0) {
                    context = context.replaceFirst(supkey,"");
                    this.sups.add(new AnswerRange( (this.content.length()>0? this.content.length():0)+p,  (this.content.length()>0? this.content.length():0)+context.indexOf(supkey2)));
                    context = context.replaceFirst(supkey2,"");
                    p = context.indexOf(supkey);//*从这个索引往后开始第一个出现的位置
                }
                //将所有的数据处理完后复制给元数据
                tags[i] = context;
                //开始处理点击事件位置
                this.content.append(tags[i]);
                if(i != tags.length-1) {
                    int start = this.content.length();
                    this.content.append("________");
                    int end = this.content.length();
                    this.rangeList.add(new AnswerRange(start, end));
                }
            }

            // 设置文字颜色
            for (int i = 0; i<this.getRangeList().size();i++) {
                AnswerRange range  =  this.getRangeList().get(i);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
                this.getContent().setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 答案集合
            for (int i = 0; i < this.getRangeList().size(); i++) {
                this.getAnswerList().add("");
            }

            //设置类型文字描述颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
            this.getContent().setSpan(colorSpan, this.getContent().toString().indexOf("、")+1,this.getContent().toString().indexOf("、")+6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            // 设置下标
            for (int i = 0; i < this.getSubs().size(); i++) {
                AnswerRange range = this.getSubs().get(i);
                this.getContent().setSpan(new SubscriptSpan(), range.start, range.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                this.getContent().setSpan(new AbsoluteSizeSpan(12, true), range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 设置上标
            for (int i = 0; i < this.getSups().size(); i++) {
                AnswerRange range = this.getSups().get(i);
                SuperscriptSpan superscriptSpan = new SuperscriptSpan( );
                this.getContent().setSpan(superscriptSpan,  range.start, range.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        }

    }
    public void setProblem5(String problem) {
        this.problem = problem;
        if(this.question_type.equals("4")){
            String font = this.getProblem().substring(this.getProblem().indexOf("<font color="),this.getProblem().indexOf("[填空题]"));

            this.color = font.substring(13,20);
//            Log.e("截取",font+"========="+this.color);
            this.problem = this.getProblem().replaceFirst(font,"");
            this.problem =this.getProblem().replaceFirst("</font>","");
            String str = this.getProblem();
            String[] tags = str.split("[$][$]");
            for(int i = 0;i <tags.length;i++){
                //找出所有的下标位置
                String context = tags[i];
                String subkey = "<sub>";
                String subkey2 = "</sub>";
                int a = context.indexOf(subkey);//*第一个出现的索引位置
                while (a >0) {
                    context = context.replaceFirst(subkey,"");
                    this.subs.add(new AnswerRange(a+ (this.content.length()>0? this.content.length():0), (this.content.length()>0? this.content.length():0)+ context.indexOf(subkey2)));
                    context = context.replaceFirst(subkey2,"");
                    a = context.indexOf(subkey);//*从这个索引往后开始第一个出现的位置
                }
                //找出所有的上标位置
                String supkey = "<sup>";
                String supkey2 = "</sup>";
                int p = context.indexOf(supkey);//*第一个出现的索引位置
                while (p >0) {
                    context = context.replaceFirst(supkey,"");
                    this.sups.add(new AnswerRange( (this.content.length()>0? this.content.length():0)+p,  (this.content.length()>0? this.content.length():0)+context.indexOf(supkey2)));
                    context = context.replaceFirst(supkey2,"");
                    p = context.indexOf(supkey);//*从这个索引往后开始第一个出现的位置
                }
                //将所有的数据处理完后复制给元数据
                tags[i] = context;
                //开始处理点击事件位置
                this.content.append(tags[i]);
                if(i != tags.length-1) {
                    int start = this.content.length();
                    this.content.append("________");
                    int end = this.content.length();
                    this.rangeList.add(new AnswerRange(start, end));
                }
            }

            // 设置文字颜色
            for (int i = 0; i<this.getRangeList().size();i++) {
                AnswerRange range  =  this.getRangeList().get(i);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
                this.getContent().setSpan(colorSpan, range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            // 答案集合
            for (int i = 0; i < this.getRangeList().size(); i++) {
                this.getAnswerList().add("");
            }

            //设置类型文字描述颜色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(this.color));
            this.getContent().setSpan(colorSpan, this.getContent().toString().indexOf("、")+1,this.getContent().toString().indexOf("、")+6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            // 设置下标
            for (int i = 0; i < this.getSubs().size(); i++) {
                AnswerRange range = this.getSubs().get(i);
                this.getContent().setSpan(new SubscriptSpan(), range.start, range.end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                this.getContent().setSpan(new AbsoluteSizeSpan(12, true), range.start, range.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // 设置上标
            for (int i = 0; i < this.getSups().size(); i++) {
                AnswerRange range = this.getSups().get(i);
                SuperscriptSpan superscriptSpan = new SuperscriptSpan( );
                this.getContent().setSpan(superscriptSpan,  range.start, range.end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        }

    }
    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
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
        LogUtil.e("输入框的变化",this.serial_number+"-"+this.user_answer);
        if(this.user_answer.length()>0){
            examination_paper.getStatus().set(Integer.parseInt(this.serial_number)-1,1);
        }else{
            examination_paper.getStatus().set(Integer.parseInt(this.serial_number)-1,0);
        }
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

    public SpannableStringBuilder getContent() {
        return content;
    }

    public void setContent(SpannableStringBuilder content) {
        this.content = content;
    }

    public List<AnswerRange> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<AnswerRange> rangeList) {
        this.rangeList = rangeList;
    }

    public Examination_paper getExamination_paper() {
        return examination_paper;
    }

    public void setExamination_paper(Examination_paper examination_paper) {
        this.examination_paper = examination_paper;
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
                ", context=" + content.toString() +
                ", serial_number='" + serial_number + '\'' +
                ", position=" + position +
                '}';
    }


}
