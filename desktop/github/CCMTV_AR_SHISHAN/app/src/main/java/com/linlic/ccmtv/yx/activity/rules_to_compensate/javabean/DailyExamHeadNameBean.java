package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2018/7/6.
 * 日常考核护士长数据
 */

public class DailyExamHeadNameBean {

    private String id;
    private String name;
    private String hospital_kid;
    private String hosid;
    private String createtime;

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

    public String getHospital_kid() {
        return hospital_kid;
    }

    public void setHospital_kid(String hospital_kid) {
        this.hospital_kid = hospital_kid;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
