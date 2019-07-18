package com.linlic.ccmtv.yx.kzbf.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Niklaus on 2017/12/20.
 * Describe:药讯动态,专题指南,相关文献
 */

public class DbMedicine implements MultiItemEntity {
    public static final int TYPE_PIC_3 = 1;//3张图片
    public static final int TYPE_PIC_0 = 2;//没有图片
    public static final int TYPE_PIC_1 = 3;//1张图片
    public static final int TYPE_PIC_1_SMALL = 5;//1张图片（小图）
    public static final int TYPE_PIC_0_PARTICIPATE = 4;//0张图片

    private int itemType;//item类型

    public DbMedicine(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String id;
    public String title;
    public String describe;
    public String uid;
    public String posttime;
    public String look_num;
    public String laud_num;
    public String username;
    public String helper;
    public String company;
    public String drug;
    public String author;
    public String source;
    public String user_img;
    public String img_num;
    public String img_url1;
    public String img_url2;
    public String img_url3;
    public String is_show_red = "0";
    public int is_show_user_partake;
    public int is_show_survey_btn;
    public int survey_integral;
    public int img_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getLook_num() {
        return look_num;
    }

    public void setLook_num(String look_num) {
        this.look_num = look_num;
    }

    public String getLaud_num() {
        return laud_num;
    }

    public void setLaud_num(String laud_num) {
        this.laud_num = laud_num;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getImg_num() {
        return img_num;
    }

    public void setImg_num(String img_num) {
        this.img_num = img_num;
    }

    public String getImg_url1() {
        return img_url1;
    }

    public void setImg_url1(String img_url1) {
        this.img_url1 = img_url1;
    }

    public String getImg_url2() {
        return img_url2;
    }

    public void setImg_url2(String img_url2) {
        this.img_url2 = img_url2;
    }

    public String getImg_url3() {
        return img_url3;
    }

    public void setImg_url3(String img_url3) {
        this.img_url3 = img_url3;
    }

    public String getIs_show_red() {
        return is_show_red;
    }

    public void setIs_show_red(String is_show_red) {
        this.is_show_red = is_show_red;
    }

    public int getIs_show_user_partake() {
        return is_show_user_partake;
    }

    public void setIs_show_user_partake(int is_show_user_partake) {
        this.is_show_user_partake = is_show_user_partake;
    }

    public int getIs_show_survey_btn() {
        return is_show_survey_btn;
    }

    public void setIs_show_survey_btn(int is_show_survey_btn) {
        this.is_show_survey_btn = is_show_survey_btn;
    }

    public int getSurvey_integral() {
        return survey_integral;
    }

    public void setSurvey_integral(int survey_integral) {
        this.survey_integral = survey_integral;
    }

    public int getImg_type() {
        return img_type;
    }

    public void setImg_type(int img_type) {
        this.img_type = img_type;
    }
}
