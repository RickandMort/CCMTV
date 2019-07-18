package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**评价项
 * Created by Administrator on 2018/6/20.
 */

public class Evaluation_of_item implements Serializable {
    private String id;
    private String content;//内容
    private int the_weight;//权重
    private int grade;//得分
    private int maxgrade;//得分
    private List<String> grades = new ArrayList<>();

    @Override
    public String toString() {
        return "Evaluation_of_item{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", the_weight=" + the_weight +
                ", grade=" + grade +
                ", maxgrade=" + maxgrade +
                ", grades=" + grades +
                '}';
    }

    public Evaluation_of_item(String id, String content, int the_weight, int grade, int maxgrade, List<String> grades) {
        this.id = id;
        this.content = content;
        this.the_weight = the_weight;
        this.grade = grade;
        this.maxgrade = maxgrade;
        this.grades = grades;
    }

    public Evaluation_of_item() {
    }

    public int getMaxgrade() {
        return maxgrade;
    }

    public void setMaxgrade(int maxgrade) {
        this.maxgrade = maxgrade;
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
}
