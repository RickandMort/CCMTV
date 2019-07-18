package com.linlic.ccmtv.yx.activity.home.entry;

import java.util.List;

/**
 * name：
 * author：Larry
 * data：2017/7/7.
 */
public class Data {
    private List<CCMTVActivity> ccmtvactivity;
    private List<IndexInfo> indexInfo;
    private List<IconArr> iconArr;
    private List<FloatArrInfo> floatArrInfo;
    private List<KeshiData> keshiData;

    public List<CCMTVActivity> getCcmtvactivity() {
        return ccmtvactivity;
    }

    public void setCcmtvactivity(List<CCMTVActivity> ccmtvactivity) {
        this.ccmtvactivity = ccmtvactivity;
    }

    public List<IndexInfo> getIndexInfo() {
        return indexInfo;
    }

    public void setIndexInfo(List<IndexInfo> indexInfo) {
        this.indexInfo = indexInfo;
    }

    public List<IconArr> getIconArr() {
        return iconArr;
    }

    public void setIconArr(List<IconArr> iconArr) {
        this.iconArr = iconArr;
    }

    public List<FloatArrInfo> getFloatArrInfo() {
        return floatArrInfo;
    }

    public void setFloatArrInfo(List<FloatArrInfo> floatArrInfo) {
        this.floatArrInfo = floatArrInfo;
    }

    public List<KeshiData> getKeshiData() {
        return keshiData;
    }

    public void setKeshiData(List<KeshiData> keshiData) {
        this.keshiData = keshiData;
    }

    @Override
    public String toString() {
        return "Data{" +
                "ccmtvactivity=" + ccmtvactivity +
                ", indexInfo=" + indexInfo +
                ", iconArr=" + iconArr +
                ", floatArrInfo=" + floatArrInfo +
                ", keshiData=" + keshiData +
                '}';
    }
}
