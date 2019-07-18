package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019/3/11.、
 * 条件
 */

public class Condition implements Serializable {
    private String id;
    private String title;
    private String select_name;
    private List<Condition> childs;
    private boolean is_select = false;
    private int curr_pos = 0;

    public String getSelect_name() {
        return select_name;
    }

    public void setSelect_name(String select_name) {
        this.select_name = select_name;
    }

    public int getCurr_pos() {
        return curr_pos;
    }

    public void setCurr_pos(int curr_pos) {
        this.curr_pos = curr_pos;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public List<Condition> getChilds() {
        return childs;
    }

    public void setChilds(List<Condition> childs) {
        this.childs = childs;
    }

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
}
