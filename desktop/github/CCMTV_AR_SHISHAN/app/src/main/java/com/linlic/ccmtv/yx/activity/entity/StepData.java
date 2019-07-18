package com.linlic.ccmtv.yx.activity.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.math.BigDecimal;

/**
 * 数据库中的表类
 * Created by lenovo on 2017/1/17.
 */
@Table("step")   //标记为表名  使用了第三方框架
public class StepData {
    //@*** 加入的jar包里的功能，方便对于数据库的管理
    //指定自增，每个对象需要一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("today")
    private String today;
    @Column("step")
    private String step;
    @Column("previousStep")
    private String previousStep;
    private String calorie;
    private String time;

    public String getTime() {
        return time;
    }


    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
//        0.043512*步数=消耗能量（千卡）
        BigDecimal bg = new BigDecimal(0.043512*Integer.parseInt(this.step));
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        this.calorie = "消耗了"+ f1+"卡路里";

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreviousStep() {
        return previousStep;
    }

    public void setPreviousStep(String previousStep) {
        this.previousStep = previousStep;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }


    @Override
    public String toString() {
        return "StepData{" +
                "id=" + id +
                ", today='" + today + '\'' +
                ", step='" + step + '\'' +
                ", previousStep='" + previousStep + '\'' +
                ", calorie='" + calorie + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
