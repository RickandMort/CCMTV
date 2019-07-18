package com.linlic.ccmtv.yx.kzbf.bean;

import org.json.JSONObject;

/**
 * Created by bentley on 2018/11/12.
 * 空中拜访  调研问卷类型
 */

public class InvestigationTypeBean {
    private String q_id;
    private int type;
    private String question;
    private Object tag;
    private JSONObject object;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
