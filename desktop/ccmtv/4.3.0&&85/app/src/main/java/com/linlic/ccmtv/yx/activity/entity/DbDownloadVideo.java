package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * name：sqlite实体类
 * author：MrSong
 * data：2016/3/30.
 */
@Table(name = "video")
public class DbDownloadVideo {
    @Id
    private int id;
    private String videoId;//视频ID
    private String downURL;//下载地址
    private String videoName;//视频名字
    private String state;//当前状态
    private String downProgress;//当前进度
    private String total;//当前文件总大小
    private String current;//已下载文件大小
    private String speed;//下载速度
    private String filePath;//文件存储路径（SD卡路径）
    private String picUrl;//视频截图
    private String videoClass;//视频类型

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrent() {
        return current;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setDownURL(String downURL) {
        this.downURL = downURL;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDownProgress(String downProgress) {
        this.downProgress = downProgress;
    }

    public String getVideoId() {
        return videoId;
    }

    public int getId() {
        return id;
    }

    public String getDownURL() {
        return downURL;
    }

    public String getState() {
        return state;
    }

    public String getDownProgress() {
        return downProgress;
    }


    public String getVideoClass() {
        return videoClass;
    }

    public void setVideoClass(String videoClass) {
        this.videoClass = videoClass;
    }

    @Override
    public String toString() {
        return "DbDownloadVideo{" +
                "id=" + id +
                ", videoId='" + videoId + '\'' +
                ", downURL='" + downURL + '\'' +
                ", videoName='" + videoName + '\'' +
                ", state='" + state + '\'' +
                ", downProgress='" + downProgress + '\'' +
                ", total='" + total + '\'' +
                ", current='" + current + '\'' +
                ", speed='" + speed + '\'' +
                ", filePath='" + filePath + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", videoClass='" + videoClass + '\'' +
                '}';
    }
}
