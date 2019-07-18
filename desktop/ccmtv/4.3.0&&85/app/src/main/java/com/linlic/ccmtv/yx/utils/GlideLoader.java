package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.yuyh.library.imgsel.common.ImageLoader;
//import com.yancy.imageselector.ImageLoader;

/**
 * GlideLoader
 * Created by Yancy on 2015/12/6.
 */
public class GlideLoader implements ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.imageselector_photo)
                .centerCrop();
        Glide.with(context)
                .load(path)
                .apply(options)
                .into(imageView);
    }

}
