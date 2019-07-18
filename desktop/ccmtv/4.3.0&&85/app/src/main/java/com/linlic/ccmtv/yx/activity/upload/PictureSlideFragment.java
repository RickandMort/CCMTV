package com.linlic.ccmtv.yx.activity.upload;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/1/3.
 */
public class PictureSlideFragment extends BaseFragment {
    private String url;
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;
    // private GestureDetector mGestureDetector;
    private AnimationDrawable anim;
    private ImageView loadingImageView;

    public static PictureSlideFragment newInstance(String url) {
        PictureSlideFragment f = new PictureSlideFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments() != null ? getArguments().getString("url") : "";

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // mGestureDetector = new GestureDetector(getActivity(), new MyOnGestureListener());

        View v = inflater.inflate(R.layout.fragment_picture_slide, container, false);
        imageView = (ImageView) v.findViewById(R.id.iv_main_pic);

        loadingImageView = (ImageView) v.findViewById(R.id.loading_imageView_info);
        anim = (AnimationDrawable) loadingImageView.getBackground();
        anim.start();
        mAttacher = new PhotoViewAttacher(imageView);
        Glide.with(getActivity()).load(url).transition(DrawableTransitionOptions.withCrossFade()).into(
                new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        mAttacher.update();
                        anim.stop();
                    }
                }
//                new GlideDrawableImageViewTarget(imageView) {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                super.onResourceReady(resource, animation);
//                mAttacher.update();
//                anim.stop();
//            }
//        }
        );
        return v;
    }


}
