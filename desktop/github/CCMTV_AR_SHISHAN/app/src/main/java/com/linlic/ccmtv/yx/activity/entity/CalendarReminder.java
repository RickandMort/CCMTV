package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Table;

import org.xutils.db.annotation.Column;

/**
 * Created by Administrator on 2018/4/9.
 */
@Table(name = "calendarreminder")
public class CalendarReminder {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    private String aid;
    private String calendar_id;
    private String uri;

    @Override
    public String toString() {
        return "CalendarReminder{" +
                "id=" + id +
                ", aid='" + aid + '\'' +
                ", calendar_id='" + calendar_id + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public CalendarReminder( ) {
    }

    public CalendarReminder(int id, String aid, String calendar_id, String uri) {
        this.id = id;
        this.aid = aid;
        this.calendar_id = calendar_id;
        this.uri = uri;
    }
}
