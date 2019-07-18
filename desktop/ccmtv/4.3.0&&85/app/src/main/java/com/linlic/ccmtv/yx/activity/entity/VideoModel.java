package com.linlic.ccmtv.yx.activity.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Niklaus on 2017/11/15.
 */

public class VideoModel implements Serializable {
    private static final long serialVersionUID = 2072893447591548402L;

    public String type;

    public String name;
    public String url;
    public String iconUrl;
    public long last_look_time;
    public String vtime = "00:00:00";
    public String checkBoxStatus = "0";
    public boolean isChecked = false;
    public String download_state = "1";
    public int checkNum = 0;
    public String aid;
    public String isVisibility = "1";

    public  VideoModel(JSONObject json){
        try {
            this.aid = json.has("aid")?json.getString("aid"):"";
            this.name = json.has("title")?json.getString("title"):"";
            this.iconUrl = json.has("picurl")?json.getString("picurl"):"";
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public VideoModel(){
        super();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public long getLast_look_time() {
        return last_look_time;
    }

    public void setLast_look_time(long last_look_time) {
        this.last_look_time = last_look_time;
    }

    public String getVtime() {
        return vtime;
    }

    public void setVtime(String vtime) {
        this.vtime = vtime;
    }

    public String getCheckBoxStatus() {
        return checkBoxStatus;
    }

    public void setCheckBoxStatus(String checkBoxStatus) {
        this.checkBoxStatus = checkBoxStatus;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDownload_state() {
        return download_state;
    }

    public void setDownload_state(String download_state) {
        this.download_state = download_state;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIsVisibility() {
        return isVisibility;
    }

    public void setIsVisibility(String isVisibility) {
        this.isVisibility = isVisibility;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String articleSize;
    public String articleName;
    public String articleUrl;
    public String articleIsShowPro = "1";

    public String getArticleIsShowPro() {
        return articleIsShowPro;
    }

    public void setArticleIsShowPro(String articleIsShowPro) {
        this.articleIsShowPro = articleIsShowPro;
    }

    public String getArticleSize() {
        return articleSize;
    }

    public void setArticleSize(String articleSize) {
        this.articleSize = articleSize;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
