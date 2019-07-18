package com.linlic.ccmtv.yx.enums;

/**
 * <pre>
 *     author : 戈传光
 *     e-mail : 1944633835@qq.com
 *     time   : 2019/07/03
 *     desc   :
 *     version:
 * </pre>
 */

public enum ActionEnum {

    EVENT_DETAIL_SELECT_VIDEO("com.linlic.ccmtv.event_seletc_video", "活动详情中录制视频"),
    ;

    ActionEnum(String action, String description) {
        this.action = action;
        this.description = description;
    }

    private String action;
    private String description;

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }
}
