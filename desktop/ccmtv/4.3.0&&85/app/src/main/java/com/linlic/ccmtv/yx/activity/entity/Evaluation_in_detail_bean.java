package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**评价bean
 * Created by Administrator on 2018/6/20.
 */

public class Evaluation_in_detail_bean implements Serializable {

    private String id;
    private int score = 0;//得分
    private String introduce_left;//左边介绍内容
    private String introduce_right;//右边介绍内容
    private String prompt;//提示内容
    private String comments;//评语
    private List<Evaluation_of_item> evaluation_of_items = new ArrayList<>();
    private int status;//状态 0为编辑 1为查看 2.未评分
    public Evaluation_in_detail_bean() {
        super();
    }

    @Override
    public String toString() {
        return "Evaluation_in_detail_bean{" +
                "id='" + id + '\'' +
                ", score=" + score +
                ", introduce_left='" + introduce_left + '\'' +
                ", introduce_right='" + introduce_right + '\'' +
                ", prompt='" + prompt + '\'' +
                ", comments='" + comments + '\'' +
                ", evaluation_of_items=" + evaluation_of_items +
                ", status=" + status +
                '}';
    }

    public Evaluation_in_detail_bean(String id, int score, String introduce_left, String introduce_right, String prompt, String comments, List<Evaluation_of_item> evaluation_of_items, int status) {
        this.id = id;
        this.score = score;
        this.introduce_left = introduce_left;
        this.introduce_right = introduce_right;
        this.prompt = prompt;
        this.comments = comments;
        this.evaluation_of_items = evaluation_of_items;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getIntroduce_left() {
        return introduce_left;
    }

    public void setIntroduce_left(String introduce_left) {
        this.introduce_left = introduce_left;
    }

    public String getIntroduce_right() {
        return introduce_right;
    }

    public void setIntroduce_right(String introduce_right) {
        this.introduce_right = introduce_right;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Evaluation_of_item> getEvaluation_of_items() {
        return evaluation_of_items;
    }

    public void setEvaluation_of_items(List<Evaluation_of_item> evaluation_of_items) {
        this.evaluation_of_items = evaluation_of_items;
    }
}
