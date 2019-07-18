package com.linlic.ccmtv.yx.activity.assginks.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/1/15.
 */

public class AssginKsDetailEntity {
    /**
     * plan_starttime : 计划轮转开始时间   (2018-12-01)
     * plan_endtime : 计划轮转结束时间    (2019-01-31)
     * status : 0 有取消&确认按钮  1 没有取消&确认按钮&所有表单框不能编辑只能查看
     * assign_config : [{"teacher":"","starttime":"","endtime":"","time":""}]
     * createtime : 2019-01-01 00:01:02
     * realname : 李小强
     * edu_highest_education : 学历   (硕士研究生)
     * ls_training_years : 三年
     * cycle_year : 3
     * ls_enrollment_year : 入院时间    (2015)
     * basename : 基地   (内科)
     * base_id : 64
     * standard_kid : 315
     * hosid : 21
     * current_hospital_kid : 若有值 将该科室在下拉框中默认选中   137
     * hospital_list : 分配科室下拉框中的内容
     *                  [{"hospital_kid":"136","pid":"0","name":"消化内科","level":"1","type":"三级科室","hosid":"21","is_exam":"1","createtime":"2018-04-16 19:51:39"},{"hospital_kid":"175","pid":"0","name":"内科","level":"1","type":"三级科室","hosid":"21","is_exam":"0","createtime":"2018-08-21 13:36:38"}]
     * details_id : 10853
     */

    private String id;
    private String uid;
    private String into_ks_id;
    private String plan_starttime;
    private String plan_endtime;
    private String day;
    private String status;
    private String hos_area;
    private String attendance_kid;
    private String assign_config;
    private String is_leave;
    private String createtime;
    private String is_be;
    private String is_assign;
    private String username;
    private String realname;
    private String edu_highest_education;
    private String ls_training_years;
    private String cycle_year;
    private String ls_enrollment_year;
    private String basename;
    private String base_id;
    private String year;
    private String month;
    private String week;
    private String standard_kid;
    private String hosid;
    private String current_hospital_kid;
    private String details_id;
    private List<HospitalListBean> hospital_list = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInto_ks_id() {
        return into_ks_id;
    }

    public void setInto_ks_id(String into_ks_id) {
        this.into_ks_id = into_ks_id;
    }

    public String getPlan_starttime() {
        return plan_starttime;
    }

    public void setPlan_starttime(String plan_starttime) {
        this.plan_starttime = plan_starttime;
    }

    public String getPlan_endtime() {
        return plan_endtime;
    }

    public void setPlan_endtime(String plan_endtime) {
        this.plan_endtime = plan_endtime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHos_area() {
        return hos_area;
    }

    public void setHos_area(String hos_area) {
        this.hos_area = hos_area;
    }

    public String getAttendance_kid() {
        return attendance_kid;
    }

    public void setAttendance_kid(String attendance_kid) {
        this.attendance_kid = attendance_kid;
    }

    public String getAssign_config() {
        return assign_config;
    }

    public void setAssign_config(String assign_config) {
        this.assign_config = assign_config;
    }

    public String getIs_leave() {
        return is_leave;
    }

    public void setIs_leave(String is_leave) {
        this.is_leave = is_leave;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getIs_be() {
        return is_be;
    }

    public void setIs_be(String is_be) {
        this.is_be = is_be;
    }

    public String getIs_assign() {
        return is_assign;
    }

    public void setIs_assign(String is_assign) {
        this.is_assign = is_assign;
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

    public String getEdu_highest_education() {
        return edu_highest_education;
    }

    public void setEdu_highest_education(String edu_highest_education) {
        this.edu_highest_education = edu_highest_education;
    }

    public String getLs_training_years() {
        return ls_training_years;
    }

    public void setLs_training_years(String ls_training_years) {
        this.ls_training_years = ls_training_years;
    }

    public String getCycle_year() {
        return cycle_year;
    }

    public void setCycle_year(String cycle_year) {
        this.cycle_year = cycle_year;
    }

    public String getLs_enrollment_year() {
        return ls_enrollment_year;
    }

    public void setLs_enrollment_year(String ls_enrollment_year) {
        this.ls_enrollment_year = ls_enrollment_year;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
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

    public String getStandard_kid() {
        return standard_kid;
    }

    public void setStandard_kid(String standard_kid) {
        this.standard_kid = standard_kid;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public String getCurrent_hospital_kid() {
        return current_hospital_kid;
    }

    public void setCurrent_hospital_kid(String current_hospital_kid) {
        this.current_hospital_kid = current_hospital_kid;
    }

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }

    public List<HospitalListBean> getHospital_list() {
        return hospital_list;
    }

    public static class HospitalListBean {
        /**
         * hospital_kid : 136
         * pid : 0
         * name : 消化内科
         * level : 1
         * type : 三级科室
         * hosid : 21
         * is_exam : 1
         * createtime : 2018-04-16 19:51:39
         */

        private String hospital_kid;
        private String pid;
        private String name;
        private String level;
        private String type;
        private String hosid;
        private String is_exam;
        private String createtime;

        public String getHospital_kid() {
            return hospital_kid;
        }

        public void setHospital_kid(String hospital_kid) {
            this.hospital_kid = hospital_kid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHosid() {
            return hosid;
        }

        public void setHosid(String hosid) {
            this.hosid = hosid;
        }

        public String getIs_exam() {
            return is_exam;
        }

        public void setIs_exam(String is_exam) {
            this.is_exam = is_exam;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
