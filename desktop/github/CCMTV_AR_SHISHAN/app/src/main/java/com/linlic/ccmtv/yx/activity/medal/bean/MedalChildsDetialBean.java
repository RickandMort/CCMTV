package com.linlic.ccmtv.yx.activity.medal.bean;

/**
 * Created by bentley on 2018/11/27.
 */

public class MedalChildsDetialBean {

    /**
     * id : 4
     * name : 成长新星
     * status : 1
     * grade : 18
     * mymedal : {"id":"18","name":"成长新星","classify":"4","condition":"3600","integral":"5","experience":"3","about":"新用户观看视频满1小时","status":"1","icon":"http://www.ccmtv.cn/do/medal/icon/medal18.png","d_icon":"http://www.ccmtv.cn/do/medal/d_icon/d_medal18.png","grade":"1"}
     * mygrade : {"id":"2086","uid":"10074039","classify":"4","mid":"18","completion1":"1","completion2":"0","status":"0","reach_time":null,"reward_status":"1","completion":"1"}
     * cont : 7
     */

    private String id;
    private String name;
    private String status;
    private String grade;
    private MymedalBean mymedal;
    private MygradeBean mygrade;
    private String cont;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public MymedalBean getMymedal() {
        return mymedal;
    }

    public void setMymedal(MymedalBean mymedal) {
        this.mymedal = mymedal;
    }

    public MygradeBean getMygrade() {
        return mygrade;
    }

    public void setMygrade(MygradeBean mygrade) {
        this.mygrade = mygrade;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public static class MymedalBean {
        /**
         * id : 18
         * name : 成长新星
         * classify : 4
         * condition : 3600
         * integral : 5
         * experience : 3
         * about : 新用户观看视频满1小时
         * status : 1
         * icon : http://www.ccmtv.cn/do/medal/icon/medal18.png
         * d_icon : http://www.ccmtv.cn/do/medal/d_icon/d_medal18.png
         * grade : 1
         */

        private String id;
        private String name;
        private String classify;
        private String condition;
        private String integral;
        private String experience;
        private String about;
        private String status;
        private String icon;
        private String d_icon;
        private String grade;

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

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }

    public static class MygradeBean {

        /**
         * id : 2668
         * uid : 10074030
         * classify : 4
         * mid : 18
         * completion1 : 0
         * completion2 : 0
         * status : 0
         * reach_time : null
         * reward_status :  0为未领取奖励 1 为已领取奖励
         * completion : 0
         * achieve : 0
         */

        private String id;
        private String uid;
        private String classify;
        private String mid;
        private String completion1;
        private String completion2;
        private String status;
        private Object reach_time;
        private String reward_status;
        private String completion;
        private String achieve;

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

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getCompletion1() {
            return completion1;
        }

        public void setCompletion1(String completion1) {
            this.completion1 = completion1;
        }

        public String getCompletion2() {
            return completion2;
        }

        public void setCompletion2(String completion2) {
            this.completion2 = completion2;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getReach_time() {
            return reach_time;
        }

        public void setReach_time(Object reach_time) {
            this.reach_time = reach_time;
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

        public String getAchieve() {
            return achieve;
        }

        public void setAchieve(String achieve) {
            this.achieve = achieve;
        }
    }
}
