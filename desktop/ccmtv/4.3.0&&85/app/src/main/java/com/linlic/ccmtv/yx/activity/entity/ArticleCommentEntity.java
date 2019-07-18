package com.linlic.ccmtv.yx.activity.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bentley on 2018/12/17.
 */

public class ArticleCommentEntity {

    /**
     * id : 评论的id
     * pid : 父类id
     * aid : 视频/文章对应的id
     * authorid : 作者id
     * uid : 发起评论者id
     * username : 发起评论者 用户名
     * content : 评论的内容
     * createtime : 2018-12-12 17:31:26
     * sourceflg : webphone
     * status : 1
     * oldid : 0
     * p_uid :父_id(评论的对象id)
     * p_username :父_username(评论的对象 名称)
     * lev : 1   当lev>2时，显示‘给谁的回复’
     * children : [{"id":"89440","pid":"89439","aid":"33496","authorid":"8388607","uid":"10149689","username":"xh53","content":"xzxZxZxzxZxZxz xczzzzzzzzzzzzzzzzzzzzzzzzzzzz","createtime":"2018-12-12 17:34:01","sourceflg":"webphone","status":"1","oldid":"0","p_uid":"10149689","p_username":"xh53","lev":2,"icon":"http://www.ccmtv.cn/images/default/noface.gif"}]
     * icon : http://www.ccmtv.cn/images/default/noface.gif
     */

    private String id;
    private String pid;
    private String aid;
    private String authorid;
    private String uid;
    private String username;
    private String content;
    private String createtime;
    private String sourceflg;
    private String status;
    private String oldid;
    private String p_uid;
    private String p_username;
    private int lev;
    private String icon;
    private List<ChildrenBean> children = new ArrayList<ChildrenBean>();
    private List<String> listString = new ArrayList<String>();

    public List<String> getListString() {
        return listString;
    }

    public void setListString(List<String> listString) {
        this.listString = listString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getSourceflg() {
        return sourceflg;
    }

    public void setSourceflg(String sourceflg) {
        this.sourceflg = sourceflg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOldid() {
        return oldid;
    }

    public void setOldid(String oldid) {
        this.oldid = oldid;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getP_username() {
        return p_username;
    }

    public void setP_username(String p_username) {
        this.p_username = p_username;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public class ChildrenBean {
        /**
         * id : 89440
         * pid : 89439
         * aid : 33496
         * authorid : 8388607
         * uid : 10149689
         * username : xh53
         * content : xzxZxZxzxZxZxz xczzzzzzzzzzzzzzzzzzzzzzzzzzzz
         * createtime : 2018-12-12 17:34:01
         * sourceflg : webphone
         * status : 1
         * oldid : 0
         * p_uid : 10149689
         * p_username : xh53
         * lev : 2
         * icon : http://www.ccmtv.cn/images/default/noface.gif
         */

        private String id;
        private String pid;
        private String aid;
        private String authorid;
        private String uid;
        private String username;
        private String content;
        private String createtime;
        private String sourceflg;
        private String status;
        private String oldid;
        private String p_uid;
        private String p_username;
        private int lev;
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getAuthorid() {
            return authorid;
        }

        public void setAuthorid(String authorid) {
            this.authorid = authorid;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getSourceflg() {
            return sourceflg;
        }

        public void setSourceflg(String sourceflg) {
            this.sourceflg = sourceflg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOldid() {
            return oldid;
        }

        public void setOldid(String oldid) {
            this.oldid = oldid;
        }

        public String getP_uid() {
            return p_uid;
        }

        public void setP_uid(String p_uid) {
            this.p_uid = p_uid;
        }

        public String getP_username() {
            return p_username;
        }

        public void setP_username(String p_username) {
            this.p_username = p_username;
        }

        public int getLev() {
            return lev;
        }

        public void setLev(int lev) {
            this.lev = lev;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
