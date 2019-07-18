package com.linlic.ccmtv.yx.activity.entity;

/**
 * Created by Administrator on 2017/12/11.
 */

public class Date_Weekdays {

    private String name;
    private String date;
    private int is = 0;
    private int stepnum = 0;

    public int getStepnum() {
        return stepnum;
    }

    public void setStepnum(int stepnum) {
        this.stepnum = stepnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIs() {
        return is;
    }

    public void setIs(int is) {
        this.is = is;
    }

    @Override
    public String toString() {
        return "Date_Weekdays{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", is=" + is +
                ", stepnum=" + stepnum +
                '}';
    }
}
