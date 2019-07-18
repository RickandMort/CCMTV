package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**督查任务
 * Created by Administrator on 2019/5/7.
 */

public class Supervision_task implements Serializable {
    private String title;
    private String is_year;
    private String id;
    private String term;
    private int curr_pos;
    private String num;
    private String curr_num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCurr_num() {
        return curr_num;
    }

    public void setCurr_num(String curr_num) {
        this.curr_num = curr_num;
    }

    public int getCurr_pos() {
        return curr_pos;
    }

    public void setCurr_pos(int curr_pos) {
        this.curr_pos = curr_pos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_year() {
        return is_year;
    }

    public void setIs_year(String is_year) {
        this.is_year = is_year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
