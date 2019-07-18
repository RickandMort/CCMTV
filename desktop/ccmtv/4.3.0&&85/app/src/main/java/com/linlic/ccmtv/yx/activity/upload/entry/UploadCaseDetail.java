package com.linlic.ccmtv.yx.activity.upload.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yu on 2018/4/19.
 */

public class UploadCaseDetail implements Serializable {

    /**
     * status : 1
     * mvtitle : 测试
     * data : [{"title":"upfileA","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileA_6PTMQ0zv.jpg"]},{"title":"upfileB","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileB_Tl50QNX5.jpg","http://192.168.30.201/upload_files/other/2018-01-10/upfileB_018mZ3Cl.jpg","http://192.168.30.201/upload_files/other/2018-01-10/upfileB_OtZFgXKn.jpg"]},{"title":"upfileC","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileC_NDaDEJ9E.jpg"]},{"title":"upfileD","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileD_ltkIM7p4.jpg"]},{"title":"upfileE","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileE_Ki1KlD5S.jpg"]},{"title":"upfileF","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileF_FZrCmYUf.jpg"]},{"title":"upfileG","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileG_fjRNF07M.jpg"]},{"title":"upfileH","date":["http://192.168.30.201/upload_files/other/2018-01-10/upfileH_wX3j5tnY.gif"]}]
     * errorMessage : 病例图片!
     */

    private String mvtitle;
    private List<DataBean> data;

    public String getMvtitle() {
        return mvtitle;
    }

    public void setMvtitle(String mvtitle) {
        this.mvtitle = mvtitle;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * title : upfileA
         * date : ["http://192.168.30.201/upload_files/other/2018-01-10/upfileA_6PTMQ0zv.jpg"]
         */

        private String title;
        private List<String> date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getDate() {
            return date;
        }

        public void setDate(List<String> date) {
            this.date = date;
        }
    }
}
