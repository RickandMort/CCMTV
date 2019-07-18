package com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean;

/**
 * Created by bentley on 2019/6/6.
 */

public class ItemInfo {
    private String num;
    private String title;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemInfo(String num,String title) {
        this.num = num;
        this.title = title;
    }


}
