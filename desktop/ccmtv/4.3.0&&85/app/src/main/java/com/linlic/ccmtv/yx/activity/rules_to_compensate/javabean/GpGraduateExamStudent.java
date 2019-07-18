package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2018/8/6.
 */

public class GpGraduateExamStudent {
    private String man_type;
    private List<ExamStudent> user_list = new ArrayList<>();
    private String step;
    private String current_node;
    private String message;
    private String disabled;
    private String week;

    public String getMan_type() {
        return man_type;
    }

    public void setMan_type(String man_type) {
        this.man_type = man_type;
    }

    public List<ExamStudent> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<ExamStudent> user_list) {
        this.user_list.addAll(user_list);
//        this.user_list = user_list;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCurrent_node() {
        return current_node;
    }

    public void setCurrent_node(String current_node) {
        this.current_node = current_node;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
