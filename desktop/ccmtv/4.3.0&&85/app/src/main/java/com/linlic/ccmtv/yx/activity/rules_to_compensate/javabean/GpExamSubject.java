package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

/**
 * Created by yu on 2018/6/22.
 */

public class GpExamSubject {
    /**
     * id : 46
     * standard_kid : 4
     * item_id : 46
     * form_id : 95
     * weight : 0.10
     * hosid : 1
     * createtime : 2018-04-18 13:23:46
     * name : 日常考核
     * item_status : 1
     * score : 100
     * daily_exam_details_id : 302
     */

    private String id;
    private String standard_kid;
    private String item_id;
    private String form_id;
    private String weight;
    private String hosid;
    private String createtime;
    private String name;
    private String item_status;  //item_status 1正常 2缺考
    private String score;
    private String daily_exam_details_id;
    private String type;
    private String url;
    private String related_examinations;//关联考试
    private String disabled;

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStandard_kid() {
        return standard_kid;
    }

    public void setStandard_kid(String standard_kid) {
        this.standard_kid = standard_kid;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDaily_exam_details_id() {
        return daily_exam_details_id;
    }

    public void setDaily_exam_details_id(String daily_exam_details_id) {
        this.daily_exam_details_id = daily_exam_details_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRelated_examinations() {
        return related_examinations;
    }

    public void setRelated_examinations(String related_examinations) {
        this.related_examinations = related_examinations;
    }
}
