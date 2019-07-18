package com.linlic.ccmtv.yx.activity.AppointmentCourse.entity;

import java.util.ArrayList;

/**
 * Created by bentley on 2019/1/11.
 */

public class FromEntity {
    /**
     * entering : 录入方式  (form表单录入 grade分数录入)
     * marking : 打分方式  (deduct_marks扣分 score得分)
     * deduct_marks_account : 扣分理由  (1需要 0不需要)
     * item : [{"name":"4234234234","config":[{"name":"11111","grade":"100"}]}]
     */

    private String entering;
    private String marking;
    private String deduct_marks_account;
    private ArrayList<FromItem> item = new ArrayList<>();
    //
    private String name;

    public String getEntering() {
        return entering;
    }

    public void setEntering(String entering) {
        this.entering = entering;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

    public String getDeduct_marks_account() {
        return deduct_marks_account;
    }

    public void setDeduct_marks_account(String deduct_marks_account) {
        this.deduct_marks_account = deduct_marks_account;
    }

    public ArrayList<FromItem> getItem() {
        return item;
    }

    public class FromItem {
        //标题
        private String name;

        private ArrayList<Config> config = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Config> getConfig() {
            return config;
        }

    }

    public class Config {
        //打分标题
        private String name;
        //分数项
        private String grade;
        //用户选择的分数  打分后存在
        private String score;
        //扣分理由  打分后存在
        private String deduct_marks_account;

        private String itemName;

        private boolean isEditShow;

        private String editTxt;

        private int id;

        private String dataType;

        public String getDataType() {
            return dataType;
        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isEditShow() {
            return isEditShow;
        }

        public void setEditShow(boolean editShow) {
            isEditShow = editShow;
        }

        public String getEditTxt() {
            return editTxt;
        }

        public void setEditTxt(String editTxt) {
            this.editTxt = editTxt;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getDeduct_marks_account() {
            return deduct_marks_account;
        }

        public void setDeduct_marks_account(String deduct_marks_account) {
            this.deduct_marks_account = deduct_marks_account;
        }
    }
}
