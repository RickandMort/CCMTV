package com.linlic.ccmtv.yx.activity.entrance_edu.entity;

/**
 * Created by bentley on 2018/12/27.
 * 入科教育模板
 */

public class EntranceEduInfoEntity {
    /**
     * id : 1
     * hospital_kid : 137
     * title : 新增测试问题修改问题
     * content : <p>发撒旦法师打发是的发生发生发发的撒</p><p>123412341241234123412412341234</p><p>阿士大夫撒打发</p>
     * hosid : 21
     * is_display : 是否显示按钮   1  显示  0  不显示
     */

    private String id;
    private String hospital_kid;
    private String title;
    private String content;
    private String hosid;
    private int is_display;
    private String username;
    private String createtime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospital_kid() {
        return hospital_kid;
    }

    public void setHospital_kid(String hospital_kid) {
        this.hospital_kid = hospital_kid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHosid() {
        return hosid;
    }

    public void setHosid(String hosid) {
        this.hosid = hosid;
    }

    public int getIs_display() {
        return is_display;
    }

    public void setIs_display(int is_display) {
        this.is_display = is_display;
    }
}
