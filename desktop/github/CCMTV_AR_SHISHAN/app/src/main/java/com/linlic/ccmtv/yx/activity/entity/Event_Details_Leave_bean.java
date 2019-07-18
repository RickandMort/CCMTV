package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by tom on 2019/2/20.
 */

public class Event_Details_Leave_bean implements Serializable {

    private String uid;
    private String realname;
    private String sign;
    private String leave_msg;

    @Override
    public String toString() {
        return "Leave{" +
                "uid='" + uid + '\'' +
                ", realname='" + realname + '\'' +
                ", sign='" + sign + '\'' +
                ", leave_msg='" + leave_msg + '\'' +
                '}';
    }

    public Event_Details_Leave_bean() {
    }

    public Event_Details_Leave_bean(String uid, String realname, String sign, String leave_msg) {
        this.uid = uid;
        this.realname = realname;
        this.sign = sign;
        this.leave_msg = leave_msg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLeave_msg() {
        return leave_msg;
    }

    public void setLeave_msg(String leave_msg) {
        this.leave_msg = leave_msg;
    }
}
