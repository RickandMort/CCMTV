package com.linlic.ccmtv.yx.activity.conference.databean;

import com.linlic.ccmtv.yx.activity.home.entry.Gg;
import com.linlic.ccmtv.yx.activity.home.entry.ListData;

import java.util.List;

/**
 * Created by yu on 2018/5/16.
 */

public class ConferenceDetailBean {
    private String fid;
    private String ftitle;
    private List<ListData> videos;

    public ConferenceDetailBean() {
    }

    public ConferenceDetailBean(String fid, String ftitle, List<ListData> videos) {
        this.fid = fid;
        this.ftitle = ftitle;
        this.videos = videos;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFtitle() {
        return ftitle;
    }

    public void setFtitle(String ftitle) {
        this.ftitle = ftitle;
    }

    public List<ListData> getVideos() {
        return videos;
    }

    public void setVideos(List<ListData> videos) {
        this.videos = videos;
    }
}
