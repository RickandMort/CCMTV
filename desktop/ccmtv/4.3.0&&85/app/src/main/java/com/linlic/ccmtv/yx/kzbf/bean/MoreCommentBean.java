package com.linlic.ccmtv.yx.kzbf.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: CCMTV_AR_SHISHAN
 * Author:  左冬至
 * Version:  1.0
 * Date:    2018/1/8
 * Copyright notice:
 */
public class MoreCommentBean {

    /**
     * article : {"addtime":"2017-12-19 13:28:47","aid":"64","company":"加快建立空间来看","drug":"刻录机","helper":"测试","title":"第三级风力可达数据来看"}
     * code : 0
     * count : 18
     * data : [{"addtime":"1星期前","aid":"64","content":"dufivkvkvjfufjjvhchchcjfif小姐超级","elite":"1","id":"85","is_del":"0","is_laud":"0","is_look":"0","laud_num":"3","pid":"0","stick":"1","two_comment":[{"addtime":"2018-01-05 14:49:23","aid":"64","content":"测试测试测试","id":"129","is_del":"0","pid":"85","uid":"10069873","username":"test14"}],"uid":"10074024","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"xh13"},{"addtime":"1星期前","aid":"64","content":"夏季次次次次次夫妇嘘嘘从坚持坚持看超级烦从坚持坚持","elite":"1","id":"84","is_del":"0","is_laud":"0","is_look":"0","laud_num":"1","pid":"0","stick":"1","two_comment":[],"uid":"10074024","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"xh13"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"60","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"59","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"58","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"57","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"56","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"55","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"54","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"},{"addtime":"2星期前","aid":"64","content":"回复测试","elite":"0","id":"53","is_del":"0","is_laud":"0","is_look":"0","laud_num":"0","pid":"0","stick":"0","two_comment":[],"uid":"10069873","user_img":"http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg","username":"test14"}]
     * msg :
     */

    private ArticleBean article;
    private String code;
    private String count;
    private String msg;
    private List<DataBean> data;

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ArticleBean {
        /**
         * addtime : 2017-12-19 13:28:47
         * aid : 64
         * company : 加快建立空间来看
         * drug : 刻录机
         * helper : 测试
         * title : 第三级风力可达数据来看
         */

        private String addtime;
        private String aid;
        private String company;
        private String drug;
        private String helper;
        private String title;

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class DataBean {
        /**
         * addtime : 1星期前
         * aid : 64
         * content : dufivkvkvjfufjjvhchchcjfif小姐超级
         * elite : 1
         * id : 85
         * is_del : 0
         * is_laud : 0
         * is_look : 0
         * laud_num : 3
         * pid : 0
         * stick : 1
         * two_comment : [{"addtime":"2018-01-05 14:49:23","aid":"64","content":"测试测试测试","id":"129","is_del":"0","pid":"85","uid":"10069873","username":"test14"}]
         * uid : 10074024
         * user_img : http://img.ccmtv.cn/upload_files/new_upload_files/20170602hx/2丁嘉安：疑难气管、隆凸疾病的外科治疗.jpg!190x102.jpg
         * username : xh13
         */

        private String addtime;
        private String aid;
        private String content;
        private String elite;
        private String id;
        private String is_del;
        private String is_laud;
        private String is_look;
        private String laud_num;
        private String pid;
        private String stick;
        private String uid;
        private String user_img;
        private String username;
        private List<TwoCommentBean> two_comment = new ArrayList<>();

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getElite() {
            return elite;
        }

        public void setElite(String elite) {
            this.elite = elite;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getIs_laud() {
            return is_laud;
        }

        public void setIs_laud(String is_laud) {
            this.is_laud = is_laud;
        }

        public String getIs_look() {
            return is_look;
        }

        public void setIs_look(String is_look) {
            this.is_look = is_look;
        }

        public String getLaud_num() {
            return laud_num;
        }

        public void setLaud_num(String laud_num) {
            this.laud_num = laud_num;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getStick() {
            return stick;
        }

        public void setStick(String stick) {
            this.stick = stick;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<TwoCommentBean> getTwo_comment() {
            return two_comment;
        }

        public void setTwo_comment(List<TwoCommentBean> two_comment) {
            this.two_comment = two_comment;
        }

        public static class TwoCommentBean {
            /**
             * addtime : 2018-01-05 14:49:23
             * aid : 64
             * content : 测试测试测试
             * id : 129
             * is_del : 0
             * pid : 85
             * uid : 10069873
             * username : test14
             */

            private String addtime;
            private String aid;
            private String content;
            private String id;
            private String is_del;
            private String pid;
            private String uid;
            private String username;

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getAid() {
                return aid;
            }

            public void setAid(String aid) {
                this.aid = aid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIs_del() {
                return is_del;
            }

            public void setIs_del(String is_del) {
                this.is_del = is_del;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
