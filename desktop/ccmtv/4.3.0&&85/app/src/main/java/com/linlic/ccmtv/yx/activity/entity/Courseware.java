package com.linlic.ccmtv.yx.activity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/23.
 */

public class Courseware implements Serializable {

    private String id;
    private String http;
    private String file_name;
    private String file_path;
    private boolean is_upload;
    private int current_progress =100;


    public Courseware(String id, String file_name, String file_path, boolean is_upload, int current_progress) {
        this.id = id;
        this.file_name = file_name;
        this.file_path = file_path;
        this.is_upload = is_upload;
        this.current_progress = current_progress;
    }

    public Courseware() {
    }

    @Override
    public String toString() {
        return "Courseware{" +
                "id='" + id + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_path='" + file_path + '\'' +
                ", is_upload=" + is_upload +
                ", current_progress=" + current_progress +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public boolean is_upload() {
        return is_upload;
    }

    public void setIs_upload(boolean is_upload) {
        this.is_upload = is_upload;
    }

    public int getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(int current_progress) {
        this.current_progress = current_progress;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }
}
