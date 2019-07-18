package com.linlic.ccmtv.yx.activity.home.willowcup;

/**
 * name：柳叶杯 类型
 * 1代表首页 2代表精彩展播 3代表作品要求 4参赛要求
 * author：Larry
 * data：2017/3/29.
 */
public enum Type {
    HOME("首页", 1), JCZHB("精彩展播 ", 2), ZPYQ("作品要求", 3), CSSHM("参赛说明", 4), PHB("排行榜", 5);


    private String name;
    private int index;

    private Type(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
