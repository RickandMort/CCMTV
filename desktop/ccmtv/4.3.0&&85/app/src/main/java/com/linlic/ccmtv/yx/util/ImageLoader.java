package com.linlic.ccmtv.yx.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;

/**
 * name：图片加载公共类
 * author：Larry
 * data：2017/7/7.
 */
public class ImageLoader {

    public static void load(Context context, String path, ImageView img) {
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(img);
    }


}
