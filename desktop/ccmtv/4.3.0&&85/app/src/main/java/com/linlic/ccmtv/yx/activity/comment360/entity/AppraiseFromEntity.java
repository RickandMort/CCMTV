package com.linlic.ccmtv.yx.activity.comment360.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/1/17.
 */

public class AppraiseFromEntity {
    /**
     * id : 781
     * manage_id : 7381
     * uid : 10565
     * p_id : 560
     * score : 50
     * content : [{"name":"名称1","weight":"5","grade":"10","score":"0"},{"name":"2","weight":"5","grade":"8","score":"8"},{"name":"3","weight":"1","grade":"10","score":"10"}]
     * template_id : 10
     * status : 1
     * createtime : 2018-12-07 18:36:15
     * savetime : 2018-12-19 00:00:00
     * comment : * 满分为100分；80分以下为差评。* 满分为100分；80分以下为差评。
     * return_news : null
     * is_comment : 1
     * seday : null
     */

    private String id;
    private String manage_id;
    private String uid;
    private String p_id;
    private String score;
    private String template_id;
    private String status;
    private String createtime;
    private String savetime;
    private String comment;
    private Object return_news;
    private String is_comment;
    private Object seday;
    private List<ContentBean> content = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManage_id() {
        return manage_id;
    }

    public void setManage_id(String manage_id) {
        this.manage_id = manage_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getReturn_news() {
        return return_news;
    }

    public void setReturn_news(Object return_news) {
        this.return_news = return_news;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public Object getSeday() {
        return seday;
    }

    public void setSeday(Object seday) {
        this.seday = seday;
    }

    public List<ContentBean> getContent() {
        return content;
    }


    public static class ContentBean {
        /**
         * name : 名称1
         * weight : 5
         * grade : 10
         * score : 0
         */

        private String name;
        private String weight;
        private String grade;
        private String score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
