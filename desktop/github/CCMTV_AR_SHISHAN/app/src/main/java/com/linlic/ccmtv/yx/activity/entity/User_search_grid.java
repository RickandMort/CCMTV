package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2017/7/6.
 */
@Table(name = "user_search_grid")
public class User_search_grid {

    @Id
    private int id;
    private String User_search_name;//搜索关键字
    private int search_num;//搜索次数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_search_name() {
        return User_search_name;
    }

    public void setUser_search_name(String user_search_name) {
        User_search_name = user_search_name;
    }

    public int getSearch_num() {
        return search_num;
    }

    public void setSearch_num(int search_num) {
        this.search_num = search_num;
    }

    @Override
    public String toString() {
        return "User_search_grid{" +
                "id=" + id +
                ", User_search_name='" + User_search_name + '\'' +
                ", search_num=" + search_num +
                '}';
    }
}
