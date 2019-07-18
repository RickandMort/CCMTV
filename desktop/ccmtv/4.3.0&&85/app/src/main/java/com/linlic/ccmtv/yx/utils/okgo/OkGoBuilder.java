package com.linlic.ccmtv.yx.utils.okgo;

/**
 * Created by 蔡超玄 on 2019/5/31.
 * okgo网络请求工具类
 */

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.List;

public class OkGoBuilder<T> {
    /**
     * get请求
     */
    public static final int GET = 1;
    /**
     * post请求
     */
    public static final int PSOT = 2;

    /**
     *
     */
    public static final int UPLOAD = 3;

    private Activity activity;
    /**
     * 请求网址
     */
    private String url;
    /**
     * 参数
     */
    private HttpParams params;

    /**
     * 上传文件
     */
    private List<File> file;

    /**
     * 上传文件key
     */
    private String key;

    /**
     * 实体类
     */
    private Class<T> clazz;
    /**
     * 回调
     */
    private Callback<T> callback;

    private int methodType;

    /**
     * 单列模式
     **/
    private static OkGoBuilder mOkGoBuilder = null;

    /**
     * 构造函数私有化
     **/
    private OkGoBuilder() {
    }

    /**
     * 公有的静态函数，对外暴露获取单例对象的接口
     **/
    public static OkGoBuilder getInstance() {
        if (mOkGoBuilder == null) {
            synchronized (OkGoBuilder.class) {
                if (mOkGoBuilder == null) {
                    mOkGoBuilder = new OkGoBuilder();
                }
            }
        }
        return mOkGoBuilder;
    }


    public OkGoBuilder Builder(Activity activity) {
        this.activity = activity;
        return this;
    }

    public OkGoBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OkGoBuilder method(int methodType) {
        this.methodType = methodType;
        return this;
    }

    public OkGoBuilder params(HttpParams params) {
        this.params = params;
        return this;
    }

    public OkGoBuilder addFileParams(String key,List<File> file) {
        this.key = key;
        this.file = file;
        return this;
    }

    public OkGoBuilder cls(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public OkGoBuilder callback(Callback<T> callback) {
        this.callback = callback;
        return this;
    }


    public OkGoBuilder build() {
        if (methodType == 1) {
            getRequest();//get请求
        } else if(methodType == 2){
            postRequest();//post请求
        }else if(methodType == 3){
            uploadRequest();
        }
        return this;
    }

    /**
     * post请求
     */
    private void postRequest() {
        OkGo
                // 请求方式和请求url
                .<T>post(url)
                .params(params)
                // 请求的 tag, 主要用于取消对应的请求
                .tag(this)
                // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheKey("cacheKey")
                // 缓存模式，详细请看缓存介绍
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback<T>(activity, clazz) {
                    @Override
                    public void onSuccess(Response<T> response) {
                        callback.onSuccess(response.body(), 1);
                    }

                    @Override
                    public void onError(Response<T> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null) {
                            throwable.printStackTrace();
                            callback.onError(throwable, 2);
                        }
                    }

                });
    }

    /**
     * get请求
     */
    private void getRequest() {
        OkGo
                // 请求方式和请求url
                .<T>get(url)
                .params(params)
                // 请求的 tag, 主要用于取消对应的请求
                .tag(this)
                // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheKey("cacheKey")
                // 缓存模式，详细请看缓存介绍
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback<T>(activity, clazz) {
                    @Override
                    public void onSuccess(Response<T> response) {
                        callback.onSuccess(response.body(), 1);
                    }

                    @Override
                    public void onError(Response<T> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null) {
                            throwable.printStackTrace();
                            callback.onError(throwable, 2);
                        }
                    }

                });
    }

    /**
     * 多文件上传
     */
    private void uploadRequest() {
           OkGo
                // 请求方式和请求url
                .<T>post(url)
                .addFileParams(key,file)
                // 请求的 tag, 主要用于取消对应的请求
                .tag(this)
                // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheKey("cacheKey")
                // 缓存模式，详细请看缓存介绍
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback<T>(activity, clazz) {
                    @Override
                    public void onSuccess(Response<T> response) {
                        callback.onSuccess(response.body(), 1);
                    }

                    @Override
                    public void onError(Response<T> response) {
                        super.onError(response);
                        Throwable throwable = response.getException();
                        if (throwable != null) {
                            throwable.printStackTrace();
                            callback.onError(throwable, 2);
                        }
                    }

                });
    }


}
