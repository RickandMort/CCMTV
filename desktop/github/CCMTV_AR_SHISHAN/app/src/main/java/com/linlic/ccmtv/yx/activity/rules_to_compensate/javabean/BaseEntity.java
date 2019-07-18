package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

/**
 * Created by bentley on 2019/1/18.
 */

public class BaseEntity {
    /**
     * base_id : 64
     * name : 内科
     * hosid : 21
     * master_id : 20
     * score :
     * detail_id : 0
     */

    private String base_id;
    private String name;
    private String hosid;
    private String master_id;
    private String score;
    private String detail_id;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(String detail_id) {
        this.detail_id = detail_id;
    }
}
