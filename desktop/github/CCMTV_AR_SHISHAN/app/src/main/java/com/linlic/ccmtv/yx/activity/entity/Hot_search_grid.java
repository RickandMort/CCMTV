package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/7/6.
 */
@Table(name = "hot_search_grid")
public class Hot_search_grid {

    @Id
    private int id;
    private String Hot_search_name;//搜索关键字
    private int search_num;//搜索次数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHot_search_name() {
        return Hot_search_name;
    }

    public void setHot_search_name(String hot_search_name) {
        Hot_search_name = hot_search_name;
    }

    public int getSearch_num() {
        return search_num;
    }

    public void setSearch_num(int search_num) {
        this.search_num = search_num;
    }

    @Override
    public String toString() {
        return "Hot_search_grid{" +
                "id=" + id +
                ", Hot_search_name='" + Hot_search_name + '\'' +
                ", search_num=" + search_num +
                '}';
    }
}
