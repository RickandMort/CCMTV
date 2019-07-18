package com.linlic.ccmtv.yx.utils.okgo;

/**
 * Created by 蔡超玄 on 2019/5/31.
 */

public interface Callback<T> {
    /**
     * 数据成功时候回调
     * @param response 成功回调接口
     * @param id       成功码
     */
    void onSuccess(T response, int id);

    /**
     * 数据失败时候回调
     * @param e    失败回调异常
     * @param id   失败码
     */
    void onError(Throwable e, int id);

}
