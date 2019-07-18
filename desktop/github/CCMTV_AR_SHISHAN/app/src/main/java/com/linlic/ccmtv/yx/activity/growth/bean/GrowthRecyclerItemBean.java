package com.linlic.ccmtv.yx.activity.growth.bean;

/**
 * Created by bentley on 2018/12/5.
 */

public class GrowthRecyclerItemBean {
    int type;
    Object object;

    public GrowthRecyclerItemBean(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
