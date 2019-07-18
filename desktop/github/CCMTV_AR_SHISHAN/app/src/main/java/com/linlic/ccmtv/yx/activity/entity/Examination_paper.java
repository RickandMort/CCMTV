package com.linlic.ccmtv.yx.activity.entity;

import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.fill_in_the_blanks.AnswerRange;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/9/8. 考卷
 */
public class Examination_paper {
    private String examination_instructions_name="";
    private String examination_instructions="";
    private String total_score_of_examination="";
    private String eid="";
    private String redis_key = "";
    int minss =0; // 屏幕宽（像素，如：px）
    int ss =0; // 屏幕宽（像素，如：px）
    private String config = "";
    private int number_of_topics= 0;//题目数量
    private Map<String,Object> position = new HashMap<>();//题号 - 题目的位置
    private List<Integer> status = new ArrayList<>();
    private List<String> title_num = new ArrayList<>();
    private List<Problem> problems = new ArrayList<Problem>();//题目集合
    private ArrayList<GroupEntity> groups = new ArrayList<>();//新版题号 数据 分题目类型  20190624 tom

    public void setUser_answer(Map<String,Object> map){
        if (map!=null && map.size()>0   ){
            for(Problem problem:problems){
                if(problem.getTitle_serial_number().length()>0 &&  map.containsKey(problem.getTitle_serial_number())){


                    if(problem.getUser_answer()!=null && problem.getUser_answer().length()>0){
                        //当用户 有网络缓存答案时不作处理
                        String[] tags = null;
                        switch (problem.getQuestion_type()){

                            case "4"://填空题
                                tags = problem.getUser_answer().split("[$][$]");
                                problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number()) - 1, 1);
                                for (int i = 0;i<tags.length;i++) {
//                                problem.getAnswerList().add(i,tags[i]);
                                    String  answer = tags[i];
                                    if(answer.trim().length()<1) {
                                        // 替换答案
                                        answer = "__________";
                                    }
                                    // 替换答案
                                    AnswerRange range = problem.getRangeList().get(i);
                                    problem.getContent().replace(range.start, range.end, answer);

                                    // 更新当前的答案范围
                                    AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
                                    problem.getRangeList().set(i, currentRange);

                                    // 答案设置下划线
                                    problem.getContent().setSpan(new UnderlineSpan(),
                                            currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    if( tags[i].trim().length()<1){
                                        // 将答案添加到集合中
                                        problem.getAnswerList().set(i,"");
                                    }else{
                                        problem.getAnswerList().set(i, answer.replace(" ", ""));
                                    }
                                    for (int j = 0; j < problem.getRangeList().size(); j++) {
                                        if (j > i) {
                                            // 获取下一个答案原来的范围
                                            AnswerRange oldNextRange = problem.getRangeList().get(j);
                                            int oldNextAmount = oldNextRange.end - oldNextRange.start;
                                            // 计算新旧答案字数的差值
                                            int difference = currentRange.end - range.end;

                                            // 更新下一个答案的范围
                                            AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                                                    oldNextRange.start + difference + oldNextAmount);
                                            problem.getRangeList().set(j, nextRange);
                                        }
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }else{
                        //当用户 无网络缓存答案时 作处理
                        String User_answer =  map.get(problem.getTitle_serial_number()).toString();
                        String[] tags = null;
//                    LogUtil.e("题目类型",problem.getTitle_serial_number()+":"+problem.getQuestion_type());
                        switch (problem.getQuestion_type()){
                            case "1"://A1题型
                                for (Option option:problem.getOptions()) {
                                    if(option.getOption_text().equals(User_answer)){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            case "2"://多选题
                                tags = User_answer.split("[$][$]");
                                Map<String,Object> user_answers = new HashMap<>();
                                for (String str:  tags) {
                                    user_answers.put(str,str);
                                }
                                for (Option option:problem.getOptions()) {
                                    if( user_answers.containsKey(option.getOption_text())  ){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            case "3"://判断题
                                for (Option option:problem.getOptions()) {
                                    if(option.getOption_text().equals(User_answer)){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            case "4"://填空题
                                tags = User_answer.split("[$][$]");
                                problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number()) - 1, 1);
                                for (int i = 0;i<tags.length;i++) {
//                                problem.getAnswerList().add(i,tags[i]);
                                    String  answer = tags[i];
                                    if(answer.trim().length()<1) {
                                        // 替换答案
                                        answer = "__________";
                                    }
                                    // 替换答案
                                    AnswerRange range = problem.getRangeList().get(i);
                                    problem.getContent().replace(range.start, range.end, answer);

                                    // 更新当前的答案范围
                                    AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
                                    problem.getRangeList().set(i, currentRange);

                                    // 答案设置下划线
                                    problem.getContent().setSpan(new UnderlineSpan(),
                                            currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    if( tags[i].trim().length()<1){
                                        // 将答案添加到集合中
                                        problem.getAnswerList().set(i,"");
                                    }else{
                                        problem.getAnswerList().set(i, answer.replace(" ", ""));
                                    }
                                    for (int j = 0; j < problem.getRangeList().size(); j++) {
                                        if (j > i) {
                                            // 获取下一个答案原来的范围
                                            AnswerRange oldNextRange = problem.getRangeList().get(j);
                                            int oldNextAmount = oldNextRange.end - oldNextRange.start;
                                            // 计算新旧答案字数的差值
                                            int difference = currentRange.end - range.end;

                                            // 更新下一个答案的范围
                                            AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                                                    oldNextRange.start + difference + oldNextAmount);
                                            problem.getRangeList().set(j, nextRange);
                                        }
                                    }
                                }
                                break;
                            case "5"://名词解释
                                problem.setUser_answer(User_answer);
                                break;
                            case "6"://案例分析题
                                problem.setUser_answer(User_answer);
                                break;
                            case "7"://公用题干题（A3/A4题）
                                for (Option option:problem.getOptions()) {
                                    if(option.getOption_text().equals(User_answer)){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            case "8"://公用答案题（B型题）
                                for (Option option:problem.getOptions()) {
                                    if(option.getOption_text().equals(User_answer)){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            case "9"://问答题
                                problem.setUser_answer(User_answer);
                                break;
                            case "10"://简答题
                                problem.setUser_answer(User_answer);
                                break;
                            case "11"://公用案例分析题
                                problem.setUser_answer(User_answer);
                                break;
                            case "12"://A2题（单选）
                                problem.setUser_answer(User_answer);
                                break;
                            case "13"://多选题
                                tags = User_answer.split("[$][$]");
                                Map<String,Object> user_answers2 = new HashMap<>();
                                for (String str:  tags) {
                                    user_answers2.put(str,str);
                                }
                                for (Option option:problem.getOptions()) {
                                    if( user_answers2.containsKey(option.getOption_text())  ){
                                        option.setOption_type("1");
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }

                }
            }
        }else{

            for(Problem problem:problems){

                    if(problem.getUser_answer()!=null && problem.getUser_answer().length()>0){
                        //当用户 有网络缓存答案时不作处理
                        String[] tags = null;
                        switch (problem.getQuestion_type()){

                            case "4"://填空题
                                tags = problem.getUser_answer().split("[$][$]");
                                problem.getExamination_paper().getStatus().set(Integer.parseInt(problem.getSerial_number()) - 1, 1);
                                for (int i = 0;i<tags.length;i++) {
//                                problem.getAnswerList().add(i,tags[i]);
                                    String  answer = tags[i];
                                    if(answer.trim().length()<1) {
                                        // 替换答案
                                        answer = "__________";
                                    }
                                    // 替换答案
                                    AnswerRange range = problem.getRangeList().get(i);
                                    problem.getContent().replace(range.start, range.end, answer);

                                    // 更新当前的答案范围
                                    AnswerRange currentRange = new AnswerRange(range.start, range.start + answer.length());
                                    problem.getRangeList().set(i, currentRange);

                                    // 答案设置下划线
                                    problem.getContent().setSpan(new UnderlineSpan(),
                                            currentRange.start, currentRange.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    if( tags[i].trim().length()<1){
                                        // 将答案添加到集合中
                                        problem.getAnswerList().set(i,"");
                                    }else{
                                        problem.getAnswerList().set(i, answer.replace(" ", ""));
                                    }
                                    for (int j = 0; j < problem.getRangeList().size(); j++) {
                                        if (j > i) {
                                            // 获取下一个答案原来的范围
                                            AnswerRange oldNextRange = problem.getRangeList().get(j);
                                            int oldNextAmount = oldNextRange.end - oldNextRange.start;
                                            // 计算新旧答案字数的差值
                                            int difference = currentRange.end - range.end;

                                            // 更新下一个答案的范围
                                            AnswerRange nextRange = new AnswerRange(oldNextRange.start + difference,
                                                    oldNextRange.start + difference + oldNextAmount);
                                            problem.getRangeList().set(j, nextRange);
                                        }
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    } }}
    }

    public void setExamination_paper(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.config =json.has("config")?json.getString("config"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("eid")?json.getString("eid"):"" ;
            JSONObject q_types = json.has("q_types")?json.getJSONObject("q_types"):new JSONObject();
         /*   ArrayList<GroupEntity> groups = new ArrayList<>();
            for (int i = 0; i < groupCount; i++) {
                ArrayList<ChildEntity> children = new ArrayList<>();
                for (int j = 0; j < childrenCount; j++) {
                    children.add(new ChildEntity("第" + (i + 1) + "组第" + (j + 1) + "项"));
                }
                groups.add(new GroupEntity("第" + (i + 1) + "组头部",
                        "第" + (i + 1) + "组尾部", children));
            }
            return groups;*/

            if(json.has("data")){
//                ArrayList<GroupEntity> groups
                Map<String , GroupEntity> map = new HashMap<>();
                List<String> list = new ArrayList<>();
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
//                    Log.e("题目",problemJson.toString());
                        if (problemJson.has("number")) {
                            position.put(problemJson.getString("number"),i );
                            title_num.add(problemJson.getString("number"));
                            status.add(0);

                            //第一步判断题型 13种题型
                            switch (problemJson.getString("type")){
                                case "1":
                                    if(map.containsKey("1")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("1").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("1")?q_types.getString("1"):"A1型题","",children);

                                        map.put("1",groupEntity);
                                        list.add("1");
                                    }
                                    break;
                                case "2":
                                    if(map.containsKey("2")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("2").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("2")?q_types.getString("2"):"X型题","",children);

                                        map.put("2",groupEntity);
                                        list.add("2");
                                    }
                                    break;
                                case "3":
                                    if(map.containsKey("3")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("3").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("3")?q_types.getString("3"):"判断题","",children);

                                        map.put("3",groupEntity);
                                        list.add("3");
                                    }
                                    break;
                                case "4":
                                    if(map.containsKey("4")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("4").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("4")?q_types.getString("4"):"填空题","",children);

                                        map.put("4",groupEntity);
                                        list.add("4");
                                    }
                                    break;
                                case "5":
                                    if(map.containsKey("5")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("5").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("5")?q_types.getString("5"):"名词解释","",children);

                                        map.put("5",groupEntity);
                                        list.add("5");
                                    }
                                    break;
                                case "6":
                                    if(map.containsKey("6")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("6").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("6")?q_types.getString("6"):"案例分析题（主观）","",children);

                                        map.put("6",groupEntity);
                                        list.add("6");
                                    }
                                    break;
                                case "7":
                                    if(map.containsKey("7")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("7").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("7")?q_types.getString("7"):"A3/A4型题","",children);

                                        map.put("7",groupEntity);
                                        list.add("7");
                                    }
                                    break;
                                case "8":
                                    if(map.containsKey("8")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("8").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("8")?q_types.getString("8"):"B型题","",children);

                                        map.put("8",groupEntity);
                                        list.add("8");
                                    }
                                    break;
                                case "9":
                                    if(map.containsKey("9")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("9").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("9")?q_types.getString("9"):"问答题","",children);
                                        map.put("9",groupEntity);
                                        list.add("9");
                                    }
                                    break;
                                case "10":
                                    if(map.containsKey("10")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("10").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("10")?q_types.getString("10"):"简答题","",children);

                                        map.put("10",groupEntity);
                                        list.add("10");
                                    }
                                    break;
                                case "11":
                                    if(map.containsKey("11")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("11").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("11")?q_types.getString("11"):"共用案例分析题","",children);

                                        map.put("11",groupEntity);
                                        list.add("11");
                                    }
                                    break;
                                case "12":
                                    if(map.containsKey("12")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("12").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("12")?q_types.getString("12"):"A2型题","",children);

                                        map.put("12",groupEntity);
                                        list.add("12");
                                    }
                                    break;
                                case "13":
                                    if(map.containsKey("13")){
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        map.get("13").getChildren().add(childEntity);
                                    }else{
                                        //题号数组
                                        ArrayList<ChildEntity>  children = new ArrayList<>();
                                        //题号对象
                                        ChildEntity childEntity = new ChildEntity(""+problemJson.getString("number"));
                                        children.add(childEntity);
                                        //题型对象
                                        GroupEntity groupEntity =   new GroupEntity(q_types.has("13")?q_types.getString("13"):"案例分析题（客观）","",children);

                                        map.put("13",groupEntity);
                                        list.add("13");
                                    }
                                    break;
                            }


                        }
                        Problem problem =new Problem(i,this);
                        problem.setProblem(problemJson);
                        this.problems.add(problem);

                }
                //循环13种题型数组 是否存在 存在及存储到 分题目类型 源数据种
                for(int i = 0 ; i < list.size() ; i++){
                    if(map.containsKey(""+list.get(i))){
                        groups.add(map.get(""+list.get(i)));
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }
    }

    public ArrayList<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<GroupEntity> groups) {
        this.groups = groups;
    }

    public String getRedis_key() {
        return redis_key;
    }

    public void setRedis_key(String redis_key) {
        this.redis_key = redis_key;
    }

    public int getMinss() {
        return minss;
    }

    public void setMinss(int minss) {
        this.minss = minss;
    }

    public int getSs() {
        return ss;
    }

    public void setSs(int ss) {
        this.ss = ss;
    }

    public void setExamination_paper2(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.config =json.has("config")?json.getString("config"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("pid")?json.getString("pid"):this.eid ;

            if(json.has("data")){
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
//                    Log.e("题目",problemJson.toString());
                    if (problemJson.has("number")){
                        position.put(problemJson.getString("number"),i );
                        title_num.add(problemJson.getString("number"));
                        status.add(0);
                    }
                    Problem problem =new Problem(i,this);
                    problem.setProblem2(problemJson);
                    this.problems.add(problem);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }

    }
    public void setExamination_paper3(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.config =json.has("config")?json.getString("config"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("pid")?json.getString("pid"):this.eid ;

            if(json.has("data")){
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
//                    Log.e("题目",problemJson.toString());
                    if (problemJson.has("number")){
                        position.put(problemJson.getString("number"),i );
                        title_num.add(problemJson.getString("number"));
                        status.add(0);
                    }
                    Problem problem =new Problem(i,this);
                    problem.setProblem3(problemJson);
                    this.problems.add(problem);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }

    }
    public void setExamination_paper4(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.config =json.has("config")?json.getString("config"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("pid")?json.getString("pid"):this.eid ;

            if(json.has("data")){
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
//                    Log.e("题目",problemJson.toString());
                    if (problemJson.has("number")){
                        position.put(problemJson.getString("number"),i );
                        title_num.add(problemJson.getString("number"));
                        status.add(0);
                    }
                    Problem problem =new Problem(i,this);
                    problem.setProblem5(problemJson);
                    this.problems.add(problem);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }

    }
    public void setExamination_paper5(JSONObject json){
        try{
            this.examination_instructions_name = json.has("paper_name")?json.getString("paper_name"):""  ;
            this.examination_instructions =  json.has("paper_descrip")?json.getString("paper_descrip"):""   ;
            this.total_score_of_examination =json.has("total_score")?json.getString("total_score"):""   ;
            this.config =json.has("config")?json.getString("config"):""   ;
            this.number_of_topics =json.has("number_of_topics")?json.getInt("number_of_topics"):0  ;
            this.eid =json.has("pid")?json.getString("pid"):this.eid ;

            if(json.has("data")){
                for (int i =0 ;i<json.getJSONArray("data").length();i++){
                    JSONObject problemJson = json.getJSONArray("data").getJSONObject(i);
//                    Log.e("题目",problemJson.toString());
                    if (problemJson.has("number")){
                        position.put(problemJson.getString("number"),i );
                        title_num.add(problemJson.getString("number"));
                        status.add(0);
                    }
                    Problem problem =new Problem(i,this);
                    problem.setProblem6(problemJson);
                    this.problems.add(problem);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
//            Log.e("解析试卷","参数缺失！");
        }

    }

    public List<String> getTitle_num() {
        return title_num;
    }

    public void setTitle_num(List<String> title_num) {
        this.title_num = title_num;
    }

    public String getExamination_instructions_name() {
        return examination_instructions_name;
    }

    public void setExamination_instructions_name(String examination_instructions_name) {
        this.examination_instructions_name = examination_instructions_name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Map<String, Object> getPosition() {
        return position;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public void setPosition(Map<String, Object> position) {
        this.position = position;
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
