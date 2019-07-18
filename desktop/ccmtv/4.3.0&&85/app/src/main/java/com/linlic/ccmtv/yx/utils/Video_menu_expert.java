package com.linlic.ccmtv.yx.utils;

/**
 * Created by Administrator on 2016/3/14.
 */
public class Video_menu_expert {


    private String video_menu_expert_id;
    private String video_menu_expert_name;
    private String video_menu_expert_cont;
    private String video_menu_expert_smalltitle;
    private String video_menu_expert_keywords;
    private String video_menu_expert_img;

    public String getVideo_menu_expert_id() {
        return video_menu_expert_id;
    }

    public String getVideo_menu_expert_name() {
        return video_menu_expert_name;
    }


    public String getVideo_menu_expert_img() {
        return video_menu_expert_img;
    }

    public String getVideo_menu_expert_smalltitle() {
        return video_menu_expert_smalltitle;
    }

    public void setVideo_menu_expert_smalltitle(String video_menu_expert_smalltitle) {
        this.video_menu_expert_smalltitle = video_menu_expert_smalltitle;
    }

    public String getVideo_menu_expert_keywords() {
        return video_menu_expert_keywords;
    }

    public void setVideo_menu_expert_keywords(String video_menu_expert_keywords) {
        this.video_menu_expert_keywords = video_menu_expert_keywords;
    }

    public void setVideo_menu_expert_id(String video_menu_expert_id) {
        this.video_menu_expert_id = video_menu_expert_id;
    }

    public void setVideo_menu_expert_name(String video_menu_expert_name) {
        this.video_menu_expert_name = video_menu_expert_name;
    }

    public void setVideo_menu_expert_cont(String video_menu_expert_cont) {
        this.video_menu_expert_cont = video_menu_expert_cont;
    }

    public String getVideo_menu_expert_cont() {
        return video_menu_expert_cont;
    }

    public void setVideo_menu_expert_img(String video_menu_expert_img) {
        this.video_menu_expert_img = video_menu_expert_img;
    }

    @Override
    public String toString() {
        return "Video_menu_expert{" +
                "video_menu_expert_id='" + video_menu_expert_id + '\'' +
                ", video_menu_expert_name='" + video_menu_expert_name + '\'' +
                ", video_menu_expert_cont='" + video_menu_expert_cont + '\'' +
                ", video_menu_expert_img='" + video_menu_expert_img + '\'' +
                '}';
    }
}
