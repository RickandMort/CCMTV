package com.linlic.ccmtv.yx.activity.bigcase.entity;

/**
 * Created by bentley on 2019/1/16.
 */

public class BigcaseUserEntity {
    /**
     * case_id : 14
     * realname : 张国
     * uid : 10579
     */

    private String case_id;
    private String realname;
    private String uid;
    private String sex;
    private String count;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
