package com.linlic.ccmtv.yx.activity.medal.bean;

import java.io.Serializable;

/**
 * Created by bentley on 2018/11/26.
 */

public class MedalDetialBean implements Serializable {

    /**
     * status : 1
     * errorMessage : 获取成功
     * data : {"id":"19","name":"成长王者","sort":"2","classify":"5","grade":"1","condition":"72000","integral":"20","experience":"10","about":"购买过VIP，且平均每月观看时长达到20小时","reward":"","status":"1","pcurl":"http://www.ccmtv.cn/ccmtv_obtainintegral.html#ul3","icon":"http://www.ccmtv.cn/do/medal/icon/medal19.png","d_icon":"http://www.ccmtv.cn/do/medal/d_icon/d_medal19.png","count":"1","type":"1","reward_status":"1","completion":"100"}
     */

    private String status;
    private String errorMessage;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 19
         * name : 成长王者
         * sort : 2
         * classify : 5
         * grade : 1
         * condition : 72000
         * integral : 20
         * experience : 10
         * about : 购买过VIP，且平均每月观看时长达到20小时
         * reward :
         * status : 1
         * pcurl : http://www.ccmtv.cn/ccmtv_obtainintegral.html#ul3
         * icon : http://www.ccmtv.cn/do/medal/icon/medal19.png
         * d_icon : http://www.ccmtv.cn/do/medal/d_icon/d_medal19.png
         * count : 1
         * type : 1
         * reward_status : 1
         * completion : 100
         */

        private String id;
        private String name;
        private String sort;
        private String classify;
        private String grade;
        private String condition;
        private String integral;
        private String experience;
        private String about;
        private String reward;
        private String status;
        private String pcurl;
        private String icon;
        private String d_icon;
        private String count;
        private String type;
        private String reward_status;
        private String completion;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPcurl() {
            return pcurl;
        }

        public void setPcurl(String pcurl) {
            this.pcurl = pcurl;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getD_icon() {
            return d_icon;
        }

        public void setD_icon(String d_icon) {
            this.d_icon = d_icon;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReward_status() {
            return reward_status;
        }

        public void setReward_status(String reward_status) {
            this.reward_status = reward_status;
        }

        public String getCompletion() {
            return completion;
        }

        public void setCompletion(String completion) {
            this.completion = completion;
        }
    }
}
