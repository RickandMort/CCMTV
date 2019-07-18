package com.linlic.ccmtv.yx.kzbf.bean;

import java.util.List;

/**
 * Project: SqDemo01
 * Author:  Niklaus
 * Version:  1.0
 * Date:    2018/1/17
 * Copyright notice:
 */
public class SearchDataBean {


    /**
     * code : 0
     * data : [{"cid":"2","id":"84","title":"附件测试"},{"cid":"1","id":"68","title":"短信测试文章"},{"cid":"2","id":"83","title":"测试测试测试测试测试测试测试测试测试测试"},{"cid":"1","id":"57","title":"测试测试测试测试测试测试测试测试测试测试"},{"cid":"2","id":"92","title":"测试测试测试测试"},{"cid":"1","id":"97","title":"测试测试测试上传"},{"cid":"1","id":"55","title":"测试测试"},{"cid":"2","id":"23","title":"测试文章测试1"},{"cid":"1","id":"24","title":"测试数据"},{"cid":"4","company":"测试测试测试","drug":"测试测试测试","helper":"测试帐号12","uid":"10158805","user_img":"http://www.ccmtv.cn/images/default/noface.gif"}]
     * msg :
     * r_list : ["测试","小赛","测试测试"]
     */

    private String code;
    private String msg;
    private String count;
    private List<DataBean> data;
    private List<String> r_list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<String> getR_list() {
        return r_list;
    }

    public void setR_list(List<String> r_list) {
        this.r_list = r_list;
    }


    public static class DataBean {
        /**
         * cid : 2
         * id : 84
         * title : 附件测试
         * company : 测试测试测试
         * drug : 测试测试测试
         * helper : 测试帐号12
         * uid : 10158805
         * user_img : http://www.ccmtv.cn/images/default/noface.gif
         */

        private String cid;
        private String id;
        private String title;
        private String posttime;
        private String company;
        private String drug;
        private String helper;
        private String uid;
        private String user_img;
        private String is_foncus;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPosttime() {
            return posttime;
        }

        public void setPosttime(String posttime) {
            this.posttime = posttime;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDrug() {
            return drug;
        }

        public void setDrug(String drug) {
            this.drug = drug;
        }

        public String getHelper() {
            return helper;
        }

        public void setHelper(String helper) {
            this.helper = helper;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getIs_foncus() {
            return is_foncus;
        }

        public void setIs_foncus(String is_foncus) {
            this.is_foncus = is_foncus;
        }
    }
}
