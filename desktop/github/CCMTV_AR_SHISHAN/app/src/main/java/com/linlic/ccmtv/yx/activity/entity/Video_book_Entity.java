package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class Video_book_Entity  implements Serializable {
    private String id;
    private String name;
    private String cover_chart;
    private String estate;
    private String author;
    private String currently;
    private Double money;
    private int current_position;
    private String pageid;
    private List<Video_book_chapter> book_url = new ArrayList<>();

    @Override
    public String toString() {
        return "Video_book_Entity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover_chart='" + cover_chart + '\'' +
                ", estate='" + estate + '\'' +
                ", author='" + author + '\'' +
                ", currently='" + currently + '\'' +
                ", money=" + money +
                ", current_position=" + current_position +
                ", pageid='" + pageid + '\'' +
                ", book_url=" + book_url +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_chart() {
        return cover_chart;
    }

    public void setCover_chart(String cover_chart) {
        this.cover_chart = cover_chart;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCurrently() {
        return currently;
    }

    public void setCurrently(String currently) {
        this.currently = currently;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(int current_position) {
        this.current_position = current_position;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public List<Video_book_chapter> getBook_url() {
        return book_url;
    }

    public void setBook_url(List<Video_book_chapter> book_url) {
        this.book_url = book_url;
    }
}
