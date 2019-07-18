/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.linlic.ccmtv.yx.activity.my;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.linlic.ccmtv.yx.R;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/1 下午10:28
 */
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {

        RequestOptions options = new RequestOptions();

        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true);
        Glide.with(activity)
                .load("file://" + path)
                .apply(options)
                .into(
                        new ImageViewTarget<Drawable>(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                imageView.setImageDrawable(resource);
                            }

                            @Override
                            public void setRequest(@Nullable Request request) {
                                imageView.setTag(R.id.adapter_item_tag_key, request);
                            }

                            @Nullable
                            @Override
                            public Request getRequest() {
                                return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                            }
                        }
                );
    }

    @Override
    public void clearMemoryCache() {
    }
}
