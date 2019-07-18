package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Table;

import org.xutils.db.annotation.Column;

/**
 * 答卷
 * Created by Administrator on 2017/9/12.
 */
@Table(name = "examination_script_paper")
public class Examination_script_paper {

    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    private String eid;
    private String config;
    private String  datetiem_count;


    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetiem_count() {
        return datetiem_count;
    }

    public void setDatetiem_count(String datetiem_count) {
        this.datetiem_count = datetiem_count;
    }

    @Override
    public String toString() {
        return "Examination_script_paper{" +
                "id=" + id +
                ", eid='" + eid + '\'' +
                ", config='" + config + '\'' +
                ", datetiem_count='" + datetiem_count + '\'' +
                '}';
    }
}
