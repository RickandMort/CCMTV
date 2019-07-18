package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2019/1/18.
 */

public class BaseDetailEntity {
    /**
     * is_comment : 1
     * createtime : 2018-12-06 14:43:02
     * comment :
     * score :
     * config : [{"name":"住院医师仪容仪表","weight":"1","grade":"10"},{"name":"住院医师服务态度","weight":"2","grade":"10"},{"name":"住院医师对病情的解释及交流沟通","weight":"1","grade":"10"},{"name":"住院医师查房情况","weight":"1","grade":"10"},{"name":"住院医师的医疗操作技术","weight":"1","grade":"10"},{"name":"病情变化及及时处理","weight":"1","grade":"10"},{"name":"对饮食、康复、用药、特殊检查、出院注意事项的指导","weight":"3","grade":"10"}]
     * base_id : 21
     * master_id : 20
     * detail_id : 0
     */

    private String is_comment;
    private String createtime;
    private String comment;
    private String score;
    private String base_id;
    private String master_id;
    private String detail_id;
    private List<ConfigBean> config = new ArrayList<>();

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }

    public List<ConfigBean> getConfig() {
        return config;
    }


    public static class ConfigBean {
        /**
         * name : 住院医师仪容仪表
         * weight : 1
         * grade : 10
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
