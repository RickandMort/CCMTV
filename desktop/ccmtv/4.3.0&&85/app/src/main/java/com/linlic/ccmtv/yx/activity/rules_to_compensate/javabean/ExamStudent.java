package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

/**
 * Created by yu on 2018/6/21.
 *
 * 考核学员列表
 */

public class ExamStudent {

    private String realname;
    private String score;
    private String uid;
    private String status;
    private String gp_exam_id;
    private String standard_kid;
    private String standard_kname;
    private String is_update;
    private String year;
    private String month;
    private String week;
    private String gpyear_id;//规培年限id；
    private String base_id;//代表专业(基地)id
    private String is_need_temp;
    private String is_allow_enter;
    private String noallow_reason;

    public String getIs_allow_enter() {
        return is_allow_enter;
    }

    public void setIs_allow_enter(String is_allow_enter) {
        this.is_allow_enter = is_allow_enter;
    }

    public String getNoallow_reason() {
        return noallow_reason;
    }

    public void setNoallow_reason(String noallow_reason) {
        this.noallow_reason = noallow_reason;
    }

    public String getIs_need_temp() {
        return is_need_temp;
    }

    public void setIs_need_temp(String is_need_temp) {
        this.is_need_temp = is_need_temp;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGp_exam_id() {
        return gp_exam_id;
    }

    public void setGp_exam_id(String daily_exam_id) {
        this.gp_exam_id = daily_exam_id;
    }

    public String getStandard_kid() {
        return standard_kid;
    }

    public void setStandard_kid(String standard_kid) {
        this.standard_kid = standard_kid;
    }

    public String getStandard_kname() {
        return standard_kname;
    }

    public void setStandard_kname(String standard_kname) {
        this.standard_kname = standard_kname;
    }

    public String getIs_update() {
        return is_update;
    }

    public void setIs_update(String is_update) {
        this.is_update = is_update;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getGpyear_id() {
        return gpyear_id;
    }

    public void setGpyear_id(String gpyear_id) {
        this.gpyear_id = gpyear_id;
    }

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
    }
}
