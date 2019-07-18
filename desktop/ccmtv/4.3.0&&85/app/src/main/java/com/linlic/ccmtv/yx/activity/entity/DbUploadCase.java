package com.linlic.ccmtv.yx.activity.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * name：上传病例
 * author：Larry
 * data：2016/5/4.
 */
@Table(name = "uploadCase")
public class DbUploadCase implements Serializable {
    @Id
    private int id;
    private String caseTitle;//病例标题
    private String upfileA_1;//患者信息第一张图片路径（SD卡路径）
    private String upfileA_2;//患者信息第一张图片路径（SD卡路径）
    private String upfileA_3;//患者信息第一张图片路径（SD卡路径）

    private String upfileB_1;//病史第一张图片路径（SD卡路径）
    private String upfileB_2;//病史第一张图片路径（SD卡路径）
    private String upfileB_3;//病史第一张图片路径（SD卡路径）

    private String upfileC_1;//临床表现第一张图片路径（SD卡路径）
    private String upfileC_2;//临床表现第一张图片路径（SD卡路径）
    private String upfileC_3;//临床表现第一张图片路径（SD卡路径）

    private String upfileD_1;//辅助检查第一张图片路径（SD卡路径）
    private String upfileD_2;//辅助检查第一张图片路径（SD卡路径）
    private String upfileD_3;//辅助检查第一张图片路径（SD卡路径）

    private String upfileE_1;//特殊检查第一张图片路径（SD卡路径）
    private String upfileE_2;//特殊检查第一张图片路径（SD卡路径）
    private String upfileE_3;//特殊检查第一张图片路径（SD卡路径）

    private String upfileF_1;//诊断第一张图片路径（SD卡路径）
    private String upfileF_2;//诊断第一张图片路径（SD卡路径）
    private String upfileF_3;//诊断第一张图片路径（SD卡路径）

    private String upfileG_1;//治疗第一张图片路径（SD卡路径）
    private String upfileG_2;//治疗第一张图片路径（SD卡路径）
    private String upfileG_3;//治疗第一张图片路径（SD卡路径）

    private String upfileH_1;//手术第一张图片路径（SD卡路径）
    private String upfileH_2;//手术第一张图片路径（SD卡路径）
    private String upfileH_3;//手术第一张图片路径（SD卡路径）

    private String state;//当前状态
    private String uploadProgress;//当前进度
    private String total;//当前文件总大小sd
    private String current;//已下载文件大小
    private String uptime;//上传成功的时间

    public void setId(int id) {
        this.id = id;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public void setUpfileA_1(String upfileA_1) {
        this.upfileA_1 = upfileA_1;
    }

    public void setUpfileA_2(String upfileA_2) {
        this.upfileA_2 = upfileA_2;
    }

    public void setUpfileA_3(String upfileA_3) {
        this.upfileA_3 = upfileA_3;
    }

    public void setUpfileB_1(String upfileB_1) {
        this.upfileB_1 = upfileB_1;
    }

    public void setUpfileB_2(String upfileB_2) {
        this.upfileB_2 = upfileB_2;
    }

    public void setUpfileB_3(String upfileB_3) {
        this.upfileB_3 = upfileB_3;
    }

    public void setUpfileC_1(String upfileC_1) {
        this.upfileC_1 = upfileC_1;
    }

    public void setUpfileC_2(String upfileC_2) {
        this.upfileC_2 = upfileC_2;
    }

    public void setUpfileC_3(String upfileC_3) {
        this.upfileC_3 = upfileC_3;
    }

    public void setUpfileD_1(String upfileD_1) {
        this.upfileD_1 = upfileD_1;
    }

    public void setUpfileD_2(String upfileD_2) {
        this.upfileD_2 = upfileD_2;
    }

    public void setUpfileD_3(String upfileD_3) {
        this.upfileD_3 = upfileD_3;
    }

    public void setUpfileE_1(String upfileE_1) {
        this.upfileE_1 = upfileE_1;
    }

    public void setUpfileE_2(String upfileE_2) {
        this.upfileE_2 = upfileE_2;
    }

    public void setUpfileE_3(String upfileE_3) {
        this.upfileE_3 = upfileE_3;
    }

    public void setUpfileF_1(String upfileF_1) {
        this.upfileF_1 = upfileF_1;
    }

    public void setUpfileF_2(String upfileF_2) {
        this.upfileF_2 = upfileF_2;
    }

    public void setUpfileF_3(String upfileF_3) {
        this.upfileF_3 = upfileF_3;
    }

    public void setUpfileG_1(String upfileG_1) {
        this.upfileG_1 = upfileG_1;
    }

    public void setUpfileG_2(String upfileG_2) {
        this.upfileG_2 = upfileG_2;
    }

    public void setUpfileG_3(String upfileG_3) {
        this.upfileG_3 = upfileG_3;
    }

    public void setUpfileH_1(String upfileH_1) {
        this.upfileH_1 = upfileH_1;
    }

    public void setUpfileH_2(String upfileH_2) {
        this.upfileH_2 = upfileH_2;
    }

    public void setUpfileH_3(String upfileH_3) {
        this.upfileH_3 = upfileH_3;
    }

    public void setState(String state) {
        this.state = state;
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

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public int getId() {
        return id;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public String getUpfileA_1() {
        return upfileA_1;
    }

    public String getUpfileA_2() {
        return upfileA_2;
    }

    public String getUpfileA_3() {
        return upfileA_3;
    }

    public String getUpfileB_1() {
        return upfileB_1;
    }

    public String getUpfileB_2() {
        return upfileB_2;
    }

    public String getUpfileB_3() {
        return upfileB_3;
    }

    public String getUpfileC_1() {
        return upfileC_1;
    }

    public String getUpfileC_2() {
        return upfileC_2;
    }

    public String getUpfileC_3() {
        return upfileC_3;
    }

    public String getUpfileD_1() {
        return upfileD_1;
    }

    public String getUpfileD_2() {
        return upfileD_2;
    }

    public String getUpfileD_3() {
        return upfileD_3;
    }

    public String getUpfileE_1() {
        return upfileE_1;
    }

    public String getUpfileE_2() {
        return upfileE_2;
    }

    public String getUpfileE_3() {
        return upfileE_3;
    }

    public String getUpfileF_1() {
        return upfileF_1;
    }

    public String getUpfileF_2() {
        return upfileF_2;
    }

    public String getUpfileF_3() {
        return upfileF_3;
    }

    public String getUpfileG_1() {
        return upfileG_1;
    }

    public String getUpfileG_2() {
        return upfileG_2;
    }

    public String getUpfileG_3() {
        return upfileG_3;
    }

    public String getUpfileH_1() {
        return upfileH_1;
    }

    public String getUpfileH_2() {
        return upfileH_2;
    }

    public String getUpfileH_3() {
        return upfileH_3;
    }

    public String getState() {
        return state;
    }

    public String getUploadProgress() {
        return uploadProgress;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrent() {
        return current;
    }

    public String getUptime() {
        return uptime;
    }

    @Override
    public String toString() {
        return "DbUploadCase{" +
                "id=" + id +
                ", caseTitle='" + caseTitle + '\'' +
                ", upfileA_1='" + upfileA_1 + '\'' +
                ", upfileA_2='" + upfileA_2 + '\'' +
                ", upfileA_3='" + upfileA_3 + '\'' +
                ", upfileB_1='" + upfileB_1 + '\'' +
                ", upfileB_2='" + upfileB_2 + '\'' +
                ", upfileB_3='" + upfileB_3 + '\'' +
                ", upfileC_1='" + upfileC_1 + '\'' +
                ", upfileC_2='" + upfileC_2 + '\'' +
                ", upfileC_3='" + upfileC_3 + '\'' +
                ", upfileD_1='" + upfileD_1 + '\'' +
                ", upfileD_2='" + upfileD_2 + '\'' +
                ", upfileD_3='" + upfileD_3 + '\'' +
                ", upfileE_1='" + upfileE_1 + '\'' +
                ", upfileE_2='" + upfileE_2 + '\'' +
                ", upfileE_3='" + upfileE_3 + '\'' +
                ", upfileF_1='" + upfileF_1 + '\'' +
                ", upfileF_2='" + upfileF_2 + '\'' +
                ", upfileF_3='" + upfileF_3 + '\'' +
                ", upfileG_1='" + upfileG_1 + '\'' +
                ", upfileG_2='" + upfileG_2 + '\'' +
                ", upfileG_3='" + upfileG_3 + '\'' +
                ", upfileH_1='" + upfileH_1 + '\'' +
                ", upfileH_2='" + upfileH_2 + '\'' +
                ", upfileH_3='" + upfileH_3 + '\'' +
                ", state='" + state + '\'' +
                ", uploadProgress='" + uploadProgress + '\'' +
                ", total='" + total + '\'' +
                ", current='" + current + '\'' +
                ", uptime='" + uptime + '\'' +
                '}';
    }
}
