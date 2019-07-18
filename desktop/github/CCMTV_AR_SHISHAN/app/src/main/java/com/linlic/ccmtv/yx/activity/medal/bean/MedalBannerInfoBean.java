package com.linlic.ccmtv.yx.activity.medal.bean;

/**
 * Created by bentley on 2018/11/26.
 */

public class MedalBannerInfoBean {


    /**
     * id : 1
     * name : 特别成就勋章
     * status : 1
     * grade : 4,5,6,7,8,9
     * cont : 该大分类该用户 已完成勋章
     * allnum : 6
     */

    private String id;
    private String name;
    private String status;
    private String grade;
    private String cont;
    private int allnum;

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

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }
}
