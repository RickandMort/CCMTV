package com.linlic.ccmtv.yx.activity.entity;

/**
 * Created by Administrator on 2019/5/13.
 */

public class Tutor_students_bean {
    private String uid;
    private String username;
    private String realname;
    private String sex;
    private String basename;
    private String mobphone;
    private String ls_enrollment_year;
    private String photo;
    private String ksname;
    private String teacher;
    private String student_id;
    private int curr_pos;

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getCurr_pos() {
        return curr_pos;
    }

    public void setCurr_pos(int curr_pos) {
        this.curr_pos = curr_pos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getMobphone() {
        return mobphone;
    }

    public void setMobphone(String mobphone) {
        this.mobphone = mobphone;
    }

    public String getLs_enrollment_year() {
        return ls_enrollment_year;
    }

    public void setLs_enrollment_year(String ls_enrollment_year) {
        this.ls_enrollment_year = ls_enrollment_year;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKsname() {
        return ksname;
    }

    public void setKsname(String ksname) {
        this.ksname = ksname;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
