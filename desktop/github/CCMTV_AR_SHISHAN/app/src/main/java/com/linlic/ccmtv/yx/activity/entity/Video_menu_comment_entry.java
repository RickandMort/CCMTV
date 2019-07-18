package com.linlic.ccmtv.yx.activity.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/24.
 */
public class Video_menu_comment_entry {
    private int isTrue = 0;
    private String video_menu_comment_item_name;
    private String video_menu_comment_item_id;
    private String video_menu_comment_item_cont;
    private String video_menu_comment_item_times;
    private String video_menu_comment_item_img;
    private String video_menu_comment_item_cid;
    private String video_menu_comment_item_aid;
    private String video_menu_comment_item_busername;
    private String video_menu_comment_item_flg;
    private List<String> listString = new ArrayList<String>();
    private int video_menu_comment_array_num;
    private String video_menu_comment_item_cont1;
    private String video_menu_comment_item_cont2;

    @Override
    public String toString() {
        return "Video_menu_comment_entry{" +
                "isTrue=" + isTrue +
                ", video_menu_comment_item_name='" + video_menu_comment_item_name + '\'' +
                ", video_menu_comment_item_id='" + video_menu_comment_item_id + '\'' +
                ", video_menu_comment_item_cont='" + video_menu_comment_item_cont + '\'' +
                ", video_menu_comment_item_times='" + video_menu_comment_item_times + '\'' +
                ", video_menu_comment_item_img='" + video_menu_comment_item_img + '\'' +
                ", video_menu_comment_item_cid='" + video_menu_comment_item_cid + '\'' +
                ", video_menu_comment_item_aid='" + video_menu_comment_item_aid + '\'' +
                ", video_menu_comment_item_busername='" + video_menu_comment_item_busername + '\'' +
                ", video_menu_comment_item_flg='" + video_menu_comment_item_flg + '\'' +
                ", listString=" + listString +
                ", video_menu_comment_array_num=" + video_menu_comment_array_num +
                ", video_menu_comment_item_cont1='" + video_menu_comment_item_cont1 + '\'' +
                ", video_menu_comment_item_cont2='" + video_menu_comment_item_cont2 + '\'' +
                '}';
    }

    public String getVideo_menu_comment_item_cont1() {
        return video_menu_comment_item_cont1;
    }

    public void setVideo_menu_comment_item_cont1(String video_menu_comment_item_cont1) {
        this.video_menu_comment_item_cont1 = video_menu_comment_item_cont1;
    }

    public String getVideo_menu_comment_item_cont2() {
        return video_menu_comment_item_cont2;
    }

    public void setVideo_menu_comment_item_cont2(String video_menu_comment_item_cont2) {
        this.video_menu_comment_item_cont2 = video_menu_comment_item_cont2;
    }

    public int getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(int isTrue) {
        this.isTrue = isTrue;
    }

    public String getVideo_menu_comment_item_name() {
        return video_menu_comment_item_name;
    }

    public void setVideo_menu_comment_item_name(String video_menu_comment_item_name) {
        this.video_menu_comment_item_name = video_menu_comment_item_name;
    }

    public String getVideo_menu_comment_item_id() {
        return video_menu_comment_item_id;
    }

    public void setVideo_menu_comment_item_id(String video_menu_comment_item_id) {
        this.video_menu_comment_item_id = video_menu_comment_item_id;
    }

    public String getVideo_menu_comment_item_cont() {
        return video_menu_comment_item_cont;
    }

    public void setVideo_menu_comment_item_cont(String video_menu_comment_item_cont) {
        this.video_menu_comment_item_cont = video_menu_comment_item_cont;
    }

    public String getVideo_menu_comment_item_times() {
        return video_menu_comment_item_times;
    }

    public void setVideo_menu_comment_item_times(String video_menu_comment_item_times) {
        this.video_menu_comment_item_times = video_menu_comment_item_times;
    }

    public String getVideo_menu_comment_item_img() {
        return video_menu_comment_item_img;
    }

    public void setVideo_menu_comment_item_img(String video_menu_comment_item_img) {
        this.video_menu_comment_item_img = video_menu_comment_item_img;
    }

    public String getVideo_menu_comment_item_cid() {
        return video_menu_comment_item_cid;
    }

    public void setVideo_menu_comment_item_cid(String video_menu_comment_item_cid) {
        this.video_menu_comment_item_cid = video_menu_comment_item_cid;
    }

    public String getVideo_menu_comment_item_aid() {
        return video_menu_comment_item_aid;
    }

    public void setVideo_menu_comment_item_aid(String video_menu_comment_item_aid) {
        this.video_menu_comment_item_aid = video_menu_comment_item_aid;
    }

    public String getVideo_menu_comment_item_busername() {
        return video_menu_comment_item_busername;
    }

    public void setVideo_menu_comment_item_busername(String video_menu_comment_item_busername) {
        this.video_menu_comment_item_busername = video_menu_comment_item_busername;
    }

    public String getVideo_menu_comment_item_flg() {
        return video_menu_comment_item_flg;
    }

    public void setVideo_menu_comment_item_flg(String video_menu_comment_item_flg) {
        this.video_menu_comment_item_flg = video_menu_comment_item_flg;
    }

    public List<String> getListString() {
        return listString;
    }


    public int getVideo_menu_comment_array_num() {
        return video_menu_comment_array_num;
    }

    public void setVideo_menu_comment_array_num(int video_menu_comment_array_num) {
        this.video_menu_comment_array_num = video_menu_comment_array_num;
    }
}
