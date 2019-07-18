package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * name：sqlite实体类（上传视频）
 * author：MrSong
 * data：2016/4/22.
 */
@Table(name = "upload")
public class DbUploadVideo {
    @Id
    private int id;
    private String videoName;//视频名字
    private String videoMsg;//视频文件摘要
    private String state;//当前状态
    private String filePath;//视频文件存储路径（SD卡路径）
    private String fileImgPath;//视频文件截图存储路径（SD卡路径）
    private String uploadProgress;//当前进度
    private String total;//当前文件总大小
    private String current;//已下载文件大小

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setVideoMsg(String videoMsg) {
        this.videoMsg = videoMsg;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileImgPath(String fileImgPath) {
        this.fileImgPath = fileImgPath;
    }

    public String getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(String uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoMsg() {
        return videoMsg;
    }

    public String getState() {
        return state;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileImgPath() {
        return fileImgPath;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrent() {
        return current;
    }

    @Override
    public String toString() {
        return "DbUploadVideo{" +
                "id=" + id +
                ", videoName='" + videoName + '\'' +
                ", videoMsg='" + videoMsg + '\'' +
                ", state='" + state + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileImgPath='" + fileImgPath + '\'' +
                ", uploadProgress='" + uploadProgress + '\'' +
                ", total='" + total + '\'' +
                ", current='" + current + '\'' +
                '}';
    }
}
