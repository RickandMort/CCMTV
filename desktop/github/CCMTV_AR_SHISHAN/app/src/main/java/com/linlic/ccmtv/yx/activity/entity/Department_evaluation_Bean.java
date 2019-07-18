package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**科室评估
 * Created by Administrator on 2019/5/7.
 */

public class Department_evaluation_Bean implements Serializable {
    private String title;
    private String id;
    private String status;
    private String score;
    private int curr_pos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
