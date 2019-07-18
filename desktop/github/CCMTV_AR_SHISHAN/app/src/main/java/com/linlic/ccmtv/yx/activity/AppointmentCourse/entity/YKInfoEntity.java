package com.linlic.ccmtv.yx.activity.AppointmentCourse.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/1/10.
 */

public class YKInfoEntity {
    /**
     * id : 45(报名时用到)
     * title : 约课名称
     * manage_id : 9610
     * start_time : 开始时间(2019-01-08 00:00:00)
     * end_time : 结束时间(2019-01-12 00:00:00)
     * file : 文件列表
     * [{"file_name":"1月PHP计划附表 .xlsx","url":"http://192.168.30.201:8083/upload/yueke/21/1546947765137085.xlsx"},{"file_name":"【附件】表3：字段对应.xls","url":"http://192.168.30.201:8083/upload/yueke/21/1546947768631500.xls"}]
     * max_num : 15
     * teacher_desc : 主讲人介绍
     * content_desc : 课程介绍
     * address : 开课地址
     * status : 1
     * kaike : 1
     * createtime : 创建时间(2019-01-08 19:40:32)
     * survey : 1
     * username : 作者姓名
     * status_name : 约课状态(进行中)
     * sign_up : 是否报名      (1已报名 0未报名 2调研)
     * sign_up_name : 报名按钮文字
     * num : 报名人数(15/1)
     */

    private String id;
    private String title;
    private String manage_id;
    private String start_time;
    private String end_time;
    private String max_num;
    private String teacher_desc;
    private String content_desc;
    private String address;
    private String status;
    private String kaike;
    private String createtime;
    private String survey;
    private String username;
    private String enroll_num;
    private String notice_name;
    private String status_name;
    private int sign_up;
    private String sign_up_name;
    private String num;
    private List<FileBean> file = new ArrayList<>();

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

    public String getMax_num() {
        return max_num;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public String getTeacher_desc() {
        return teacher_desc;
    }

    public void setTeacher_desc(String teacher_desc) {
        this.teacher_desc = teacher_desc;
    }

    public String getContent_desc() {
        return content_desc;
    }

    public void setContent_desc(String content_desc) {
        this.content_desc = content_desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKaike() {
        return kaike;
    }

    public void setKaike(String kaike) {
        this.kaike = kaike;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
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

    public int getSign_up() {
        return sign_up;
    }

    public void setSign_up(int sign_up) {
        this.sign_up = sign_up;
    }

    public String getSign_up_name() {
        return sign_up_name;
    }

    public void setSign_up_name(String sign_up_name) {
        this.sign_up_name = sign_up_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<FileBean> getFile() {
        return file;
    }

    public String getNotice_name() {
        return notice_name;
    }

    public void setNotice_name(String notice_name) {
        this.notice_name = notice_name;
    }

    public String getEnroll_num() {
        return enroll_num;
    }

    public void setEnroll_num(String enroll_num) {
        this.enroll_num = enroll_num;
    }

    public static class FileBean {
        /**
         * file_name : 1月PHP计划附表 .xlsx
         * url : http://192.168.30.201:8083/upload/yueke/21/1546947765137085.xlsx
         */

        private String file_name;
        private String url;

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
