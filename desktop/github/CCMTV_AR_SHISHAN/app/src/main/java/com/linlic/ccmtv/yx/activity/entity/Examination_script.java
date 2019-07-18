package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import org.xutils.db.annotation.Column;

/**
 * 答卷
 * Created by Administrator on 2017/9/12.
 */
@Table(name = "examination_script")
public class Examination_script {

    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    private String eid;
    private String option_id;
    private String option_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }


    @Override
    public String toString() {
        return "Examination_script{" +
                "id=" + id +
                ", eid='" + eid + '\'' +
                ", option_id='" + option_id + '\'' +
                ", option_name='" + option_name + '\'' +
                '}';
    }
}
