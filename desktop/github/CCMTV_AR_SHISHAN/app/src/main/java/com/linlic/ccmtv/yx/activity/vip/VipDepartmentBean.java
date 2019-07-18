package com.linlic.ccmtv.yx.activity.vip;

/**
 * Created by yu on 2018/5/28.
 */

class VipDepartmentBean {

    private String departmentPinUrl;
    private String departmentName;
    private int isSelect = 0;

    public String getDepartmentPinUrl() {
        return departmentPinUrl;
    }

    public void setDepartmentPinUrl(String departmentPinUrl) {
        this.departmentPinUrl = departmentPinUrl;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }
}
