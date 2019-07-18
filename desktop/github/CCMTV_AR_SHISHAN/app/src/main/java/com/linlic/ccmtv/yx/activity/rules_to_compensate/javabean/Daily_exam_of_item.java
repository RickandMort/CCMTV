package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**日常考核项
 * Created by Administrator on 2018/6/20.
 */

public class Daily_exam_of_item implements Serializable {
    private String id;
    private String content;//内容
    private int the_weight;//权重
    private int grade;//得分
    private List<String> grades = new ArrayList<>();
    private String maxScore;//每一项最高分

    @Override
    public String toString() {
        return "Daily_exam_of_item{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", the_weight=" + the_weight +
                ", grade=" + grade +
                ", grades=" + grades +
                '}';
    }

    public Daily_exam_of_item() {
    }

    public Daily_exam_of_item(String id, String content, int the_weight, int grade, List<String> grades) {
        this.id = id;
        this.content = content;
        this.the_weight = the_weight;
        this.grade = grade;
        this.grades = grades;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getThe_weight() {
        return the_weight;
    }

    public void setThe_weight(int the_weight) {
        this.the_weight = the_weight;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<String> getGrades() {
        return grades;
    }

    public void setGrades(List<String> grades) {
        this.grades = grades;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }
}
