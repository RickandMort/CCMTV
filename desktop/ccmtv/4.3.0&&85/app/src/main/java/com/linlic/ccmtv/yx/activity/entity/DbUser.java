package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * name：
 * author：Larry
 * data：2016/4/13.
 */
@Table(name = "user")
public class DbUser {
    @Id
    private int id;
    public String username;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "DbUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
