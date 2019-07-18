package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Video_book_chapter implements Serializable {
    private String name;
    private String pdfStr;
    private String id;
    private int position;

    public Video_book_chapter() {
        super();
    }

    @Override
    public String toString() {
        return "Video_book_chapter{" +
                "name='" + name + '\'' +
                ", pdfStr='" + pdfStr + '\'' +
                ", id='" + id + '\'' +
                ", position=" + position +
                '}';
    }

    public Video_book_chapter(String name, String pdfStr, String id, int position) {
        this.name = name;
        this.pdfStr = pdfStr;
        this.id = id;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getPdfStr() {
        return pdfStr;
    }

    public void setPdfStr(String pdfStr) {
        this.pdfStr = pdfStr;
    }
}
