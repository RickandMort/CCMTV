package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * name：记录视频播放时间
 * author：Larry
 * data：2017/4/7.
 */
@Table(name = "recordvideo")
public class RecordVideo {
    @Id
    private int id;
    private String videoId;//视频ID
    private long viewingTime;//播放进度

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public long getViewingTime() {
        return viewingTime;
    }

    public void setViewingTime(long viewingTime) {
        this.viewingTime = viewingTime;
    }

    @Override
    public String toString() {
        return "RecordVideo{" +
                "id=" + id +
                ", videoId='" + videoId + '\'' +
                ", viewingTime='" + viewingTime + '\'' +
                '}';
    }
}
