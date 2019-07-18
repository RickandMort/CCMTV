package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/4/19.
 */

public class GpGraduateExamStudent1 {
   //private String week;
    private String current_week_msg;
    private String disabled;
    private String is_current_week;
    private String week;
    private String type;
    private String type_name;
    private String errormsg;
    private List<DailyStudent> user_list = new ArrayList<>();
    private String flow_name;

    public String getFlow_name() {
        return flow_name;
    }

    public void setFlow_name(String flow_name) {
        this.flow_name = flow_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getCurrent_week_msg() {
        return current_week_msg;
    }

    public void setCurrent_week_msg(String current_week_msg) {
        this.current_week_msg = current_week_msg;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getIs_current_week() {
        return is_current_week;
    }

    public void setIs_current_week(String is_current_week) {
        this.is_current_week = is_current_week;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<DailyStudent> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<DailyStudent> user_list) {
        this.user_list = user_list;
    }
}
