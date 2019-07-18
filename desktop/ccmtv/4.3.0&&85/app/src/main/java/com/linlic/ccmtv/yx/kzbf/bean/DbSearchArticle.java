package com.linlic.ccmtv.yx.kzbf.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by Niklaus on 2018/1/18.
 */

@Table(name = "article")
public class DbSearchArticle {
    @Id
    private int id;
    private String articleName;//视频名字
    private int search_num;//搜索次数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getSearch_num() {
        return search_num;
    }

    public void setSearch_num(int search_num) {
        this.search_num = search_num;
    }

    @Override
    public String toString() {
        return "DbSearchArticle{" +
                "id=" + id +
                ", articleName='" + articleName + '\'' +
                ", search_num=" + search_num +
                '}';
    }
}
