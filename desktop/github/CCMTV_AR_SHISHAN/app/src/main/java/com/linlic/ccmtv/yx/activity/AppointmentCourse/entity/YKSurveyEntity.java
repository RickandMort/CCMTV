package com.linlic.ccmtv.yx.activity.AppointmentCourse.entity;

/**
 * Created by bentley on 2019/1/11.
 */

public class YKSurveyEntity {
    /**
     * id : 45
     * is_edit : 编辑状态  1 不能编辑  0可能编辑
     * score :已打分分数
     * form : {"entering":"form","marking":"deduct_marks","deduct_marks_account":"1","item":"[{\"name\":\"4234234234\",\"config\":[{\"name\":\"11111\",\"grade\":\"100\"}]}]"}
     */

    private String id;
    private String is_edit;
    private String score;
    private FromEntity form;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(String is_edit) {
        this.is_edit = is_edit;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public FromEntity getForm() {
        return form;
    }

    public void setForm(FromEntity form) {
        this.form = form;
    }

}
