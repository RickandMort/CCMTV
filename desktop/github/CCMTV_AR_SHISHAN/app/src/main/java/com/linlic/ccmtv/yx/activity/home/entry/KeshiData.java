package com.linlic.ccmtv.yx.activity.home.entry;


import java.util.ArrayList;
import java.util.List;

/**
 * name：首页列表按照科室分
 * author：Larry
 * data：2017/7/6.
 */
public class KeshiData {
    private String id;
    private String title;
    private Gg gg;
    private List<ListData> listdata ;

    public KeshiData() {
    }

    public KeshiData(String id, String title, Gg gg, List<ListData> listdata) {
        this.id = id;
        this.title = title;
        this.gg = gg;
        this.listdata = listdata;
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

    public Gg getGg() {
        return gg;
    }

    public void setGg(Gg gg) {
        this.gg = gg;
    }

    public List<ListData> getListdata() {
        return listdata;
    }

    public void setListdata(List<ListData> listdata) {
        this.listdata = listdata;
    }

    @Override
    public String toString() {
        return "KeshiData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", gg=" + gg +
                ", listdata=" + listdata +
                '}';
    }
}