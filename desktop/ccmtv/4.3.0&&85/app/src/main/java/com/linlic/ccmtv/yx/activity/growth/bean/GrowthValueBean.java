package com.linlic.ccmtv.yx.activity.growth.bean;

import java.util.List;

/**
 * Created by bentley on 2018/12/4.
 */

public class GrowthValueBean {

    /**
     * status : 1
     * data : {"username":"xh28","cur_jyz":"178","next_gradejyz":"480","jyz_per":37,"cur_rank_id":1,"cur_hatpic":"http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/career1.png","cur_rank_name":"使唤医生","cur_grade":"从九品"}
     * userRankInfo : [{"id":"1","rank_name":"使唤医生","grade":"从九品","upgradejyz":"30","hatpic":"http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/career1.png"}]
     * userRankRule1 : [{"id":"1","rule_icon":"http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/lock.png","rule_name":"注册","jyz":"30","unit":null,"flag":"reg","explain":null,"rule_type":"0"}]
     * userRankRule2 : [{"id":"6","rule_icon":"http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/lock.png","rule_name":"观看视频","jyz":"2","unit":"次","flag":"watch_video","explain":"需观看5分钟以上","rule_type":"1"}]
     * limit_jyz : 50
     * errorMessage : 用户成长等级信息!
     */

    private String status;
    private DataBean data;
    private String limit_jyz;
    private String errorMessage;
    private List<UserRankInfoBean> userRankInfo;
    private List<UserRankRuleBean> userRankRule1;
    private List<UserRankRuleBean> userRankRule2;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getLimit_jyz() {
        return limit_jyz;
    }

    public void setLimit_jyz(String limit_jyz) {
        this.limit_jyz = limit_jyz;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<UserRankInfoBean> getUserRankInfo() {
        return userRankInfo;
    }

    public void setUserRankInfo(List<UserRankInfoBean> userRankInfo) {
        this.userRankInfo = userRankInfo;
    }

    public List<UserRankRuleBean> getUserRankRule1() {
        return userRankRule1;
    }

    public void setUserRankRule1(List<UserRankRuleBean> userRankRule1) {
        this.userRankRule1 = userRankRule1;
    }

    public List<UserRankRuleBean> getUserRankRule2() {
        return userRankRule2;
    }

    public void setUserRankRule2(List<UserRankRuleBean> userRankRule2) {
        this.userRankRule2 = userRankRule2;
    }

    public static class DataBean {
        /**
         * userface : http:\/\/192.168.30.201\/upload_files
         * username : xh28
         * cur_jyz : 178
         * next_gradejyz : 480
         * jyz_per : 37
         * cur_rank_id : 1
         * cur_hatpic : http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/career1.png
         * cur_rank_name : 使唤医生
         * cur_grade : 从九品
         */

        private String username;
        private String userface;
        private String cur_jyz;
        private String next_gradejyz;
        private int jyz_per;
        private int cur_rank_id;
        private String cur_hatpic;
        private String cur_rank_name;
        private String cur_grade;

        public String getUserface() {
            return userface;
        }

        public void setUserface(String userface) {
            this.userface = userface;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCur_jyz() {
            return cur_jyz;
        }

        public void setCur_jyz(String cur_jyz) {
            this.cur_jyz = cur_jyz;
        }

        public String getNext_gradejyz() {
            return next_gradejyz;
        }

        public void setNext_gradejyz(String next_gradejyz) {
            this.next_gradejyz = next_gradejyz;
        }

        public int getJyz_per() {
            return jyz_per;
        }

        public void setJyz_per(int jyz_per) {
            this.jyz_per = jyz_per;
        }

        public int getCur_rank_id() {
            return cur_rank_id;
        }

        public void setCur_rank_id(int cur_rank_id) {
            this.cur_rank_id = cur_rank_id;
        }

        public String getCur_hatpic() {
            return cur_hatpic;
        }

        public void setCur_hatpic(String cur_hatpic) {
            this.cur_hatpic = cur_hatpic;
        }

        public String getCur_rank_name() {
            return cur_rank_name;
        }

        public void setCur_rank_name(String cur_rank_name) {
            this.cur_rank_name = cur_rank_name;
        }

        public String getCur_grade() {
            return cur_grade;
        }

        public void setCur_grade(String cur_grade) {
            this.cur_grade = cur_grade;
        }
    }

    public static class UserRankInfoBean {

        /**
         * id : 4
         * rank_name : 医生
         * grade : 正八品
         * upgradejyz : 2580
         * cur_hatpic : http://192.168.30.201/upload_files/new_upload_files/ccmtvtp/Public/medal/images/hat4.png
         * hatpic : http://192.168.30.201/upload_files/new_upload_files/ccmtvtp/Public/medal/images/career3.png
         * hat_colour : http://192.168.30.201/upload_files/new_upload_files/ccmtvtp/Public/medal/images/hat_colour4.png
         * hat_gray : http://192.168.30.201/upload_files/new_upload_files/ccmtvtp/Public/medal/images/hat_gray4.png
         */

        private String id;
        private String rank_name;
        private String grade;
        private String upgradejyz;
        private String cur_hatpic;
        private String hatpic;
        private String hat_colour;
        private String hat_gray;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRank_name() {
            return rank_name;
        }

        public void setRank_name(String rank_name) {
            this.rank_name = rank_name;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getUpgradejyz() {
            return upgradejyz;
        }

        public void setUpgradejyz(String upgradejyz) {
            this.upgradejyz = upgradejyz;
        }

        public String getCur_hatpic() {
            return cur_hatpic;
        }

        public void setCur_hatpic(String cur_hatpic) {
            this.cur_hatpic = cur_hatpic;
        }

        public String getHatpic() {
            return hatpic;
        }

        public void setHatpic(String hatpic) {
            this.hatpic = hatpic;
        }

        public String getHat_colour() {
            return hat_colour;
        }

        public void setHat_colour(String hat_colour) {
            this.hat_colour = hat_colour;
        }

        public String getHat_gray() {
            return hat_gray;
        }

        public void setHat_gray(String hat_gray) {
            this.hat_gray = hat_gray;
        }
    }

    public static class UserRankRuleBean {
        /**
         * id : 1
         * rule_icon : http://www.ccmtv.cn/upload_files/new_upload_files/ccmtvtp/Public/medal/images/lock.png
         * rule_name : 注册
         * jyz : 30
         * unit : null
         * flag : reg
         * explain : null
         * rule_type : 0
         */

        private String id;
        private String rule_icon;
        private String rule_name;
        private String jyz;
        private String unit;
        private String flag;
        private String explain;
        private String rule_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRule_icon() {
            return rule_icon;
        }

        public void setRule_icon(String rule_icon) {
            this.rule_icon = rule_icon;
        }

        public String getRule_name() {
            return rule_name;
        }

        public void setRule_name(String rule_name) {
            this.rule_name = rule_name;
        }

        public String getJyz() {
            return jyz;
        }

        public void setJyz(String jyz) {
            this.jyz = jyz;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getRule_type() {
            return rule_type;
        }

        public void setRule_type(String rule_type) {
            this.rule_type = rule_type;
        }
    }
}
