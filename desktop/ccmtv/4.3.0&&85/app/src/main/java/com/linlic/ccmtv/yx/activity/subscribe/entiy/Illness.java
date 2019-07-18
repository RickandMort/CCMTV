package com.linlic.ccmtv.yx.activity.subscribe.entiy;

import android.widget.CheckBox;

/**
 * Created by yu on 2017/11/29.
 */

public class Illness {
    private CheckBox checkBox;
    private String illness;
    private String illnessid;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public String getIllnessid() {
        return illnessid;
    }

    public void setIllnessid(String illnessid) {
        this.illnessid = illnessid;
    }
}
