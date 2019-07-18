package com.linlic.ccmtv.yx.activity.subscribe.entiy;

import java.io.Serializable;

/**
 * name：我订阅的科室
 * author：Larry
 * data：2017/7/26.
 */
public class Followks implements Serializable{

    private String id;//科室ID
    private String name;//科室名称/病种
    private Boolean istrue = false;//是否选中


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

    @Override
    public String toString() {
        return "Followks{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", istrue=" + istrue +
                '}';
    }

    public Boolean getIstrue() {
        return istrue;
    }

    public void setIstrue(Boolean istrue) {
        this.istrue = istrue;
    }
}
