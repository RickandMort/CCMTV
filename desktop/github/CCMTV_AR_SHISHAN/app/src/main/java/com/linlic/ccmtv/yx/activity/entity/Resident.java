package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/28.
 */

public class Resident   implements Serializable {
    private String id;
    private String name;
    private boolean is_select;
    private String imgUrl;
    private String username;
    private String mobphone;
    private String ls_enrollment_year;
    private String ksname;
    private String base_name;
    private String sign;
    private String hospital_kid;
    private String is_temp;
    private List<Files_info> files_infos = new ArrayList<>();

    @Override
    public String toString() {
        return "Resident{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", is_select=" + is_select +
                ", imgUrl='" + imgUrl + '\'' +
                ", username='" + username + '\'' +
                ", mobphone='" + mobphone + '\'' +
                ", ls_enrollment_year='" + ls_enrollment_year + '\'' +
                ", ksname='" + ksname + '\'' +
                ", base_name='" + base_name + '\'' +
                ", sign='" + sign + '\'' +
                ", hospital_kid='" + hospital_kid + '\'' +
                ", is_temp='" + is_temp + '\'' +
                ", files_infos=" + files_infos +
                '}';
    }

    public Resident() {
    }

    public Resident(String id, String name, boolean is_select, String imgUrl, String username, String mobphone, String ls_enrollment_year, String ksname, String base_name, String sign, String hospital_kid, String is_temp, List<Files_info> files_infos) {
        this.id = id;
        this.name = name;
        this.is_select = is_select;
        this.imgUrl = imgUrl;
        this.username = username;
        this.mobphone = mobphone;
        this.ls_enrollment_year = ls_enrollment_year;
        this.ksname = ksname;
        this.base_name = base_name;
        this.sign = sign;
        this.hospital_kid = hospital_kid;
        this.is_temp = is_temp;
        this.files_infos = files_infos;
    }

    public List<Files_info> getFiles_infos() {
        return files_infos;
    }

    public void setFiles_infos(List<Files_info> files_infos) {
        this.files_infos = files_infos;
    }

    public String getIs_temp() {
        return is_temp;
    }

    public void setIs_temp(String is_temp) {
        this.is_temp = is_temp;
    }

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHospital_kid() {
        return hospital_kid;
    }

    public void setHospital_kid(String hospital_kid) {
        this.hospital_kid = hospital_kid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobphone() {
        return mobphone;
    }

    public void setMobphone(String mobphone) {
        this.mobphone = mobphone;
    }

    public String getLs_enrollment_year() {
        return ls_enrollment_year;
    }

    public void setLs_enrollment_year(String ls_enrollment_year) {
        this.ls_enrollment_year = ls_enrollment_year;
    }

    public String getKsname() {
        return ksname;
    }

    public void setKsname(String ksname) {
        this.ksname = ksname;
    }

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

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
