package com.linlic.ccmtv.yx.activity.AppointmentCourse.entity;

/**
 * Created by bentley on 2019/1/9.
 */

public class YKListEntity {
    /**
     * id : 44
     * title : 标题
     * manage_id : 9610
     * start_time : 开始时间(2019-01-04 13:49:22)
     * end_time : 2019-01-12 00:00:00
     * notice_id : 3
     * max_num : 23
     * username : 传说
     * status_name : 状态名称(报名中)
     * notice_name : 参与类型(科室)
     * num :  报名人数(23/0)
     */

    private String id;
    private String title;
    private String manage_id;
    private String start_time;
    private String end_time;
    private String notice_id;
    private String max_num;
    private String enroll_num;
    private String username;
    private String status_name;
    private String notice_name;
    private String num;
    private String createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManage_id() {
        return manage_id;
    }

    public void setManage_id(String manage_id) {
        this.manage_id = manage_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getMax_num() {
        return max_num;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getNotice_name() {
        return notice_name;
    }

    public void setNotice_name(String notice_name) {
        this.notice_name = notice_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getEnroll_num() {
        return enroll_num;
    }

    public void setEnroll_num(String enroll_num) {
        this.enroll_num = enroll_num;
    }
}
