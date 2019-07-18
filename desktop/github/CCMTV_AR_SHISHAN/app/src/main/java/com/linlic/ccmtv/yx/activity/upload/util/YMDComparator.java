package com.linlic.ccmtv.yx.activity.upload.util;


import com.linlic.ccmtv.yx.activity.entity.VideoInfo;

import java.util.Comparator;

/**
 * Created by yu on 2018/4/4.
 */

public class YMDComparator implements Comparator<VideoInfo> {

    @Override
    public int compare(VideoInfo o1, VideoInfo o2) {
        return o2.getTime().compareTo(o1.getTime());
    }

}