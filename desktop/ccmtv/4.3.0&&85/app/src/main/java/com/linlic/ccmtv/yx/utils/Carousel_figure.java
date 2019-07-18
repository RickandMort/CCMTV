package com.linlic.ccmtv.yx.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.home.SpecialActivity;
import com.linlic.ccmtv.yx.activity.home.apricotcup.ApricotActivity;
import com.linlic.ccmtv.yx.activity.home.hxsl.BreatheActivity;
import com.linlic.ccmtv.yx.activity.home.willowcup.WillowCupActivity;
import com.linlic.ccmtv.yx.activity.my.MyInvitationFriend;
import com.linlic.ccmtv.yx.utils.carouselFigure.CycleViewPager;
import com.linlic.ccmtv.yx.utils.carouselFigure.CycleViewPager.ImageCycleViewListener;
import com.linlic.ccmtv.yx.utils.carouselFigure.ViewFactory;
import com.linlic.ccmtv.yx.utils.carouselFigure.bean.ADInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name:轮播图
 * author:Tom
 * 2016-3-2下午7:07:48
 */
public class Carousel_figure {
    private Context context;
    /***************************
     * 滚动图片start
     *****************************************/
//    private List<RoundCornerImageView> views = new ArrayList<RoundCornerImageView>();
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;
    ImageLoader imageLoader = ImageLoader.getInstance();

    /***************************
     * 滚动图片end
     *****************************************/

    public Carousel_figure(Context context) {
        this.context = context;

    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                Log.i("AAA", "info.getTitle()" + info.toString());
                Intent intent = null;
                if (info.isActivity()) {
                    intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("title", info.getTitle());
                } else {
                    if ("邀请好友".equals(info.getTitle())) {
                        intent = new Intent(context, MyInvitationFriend.class);
                    } else if ("呼吸时令".equals(info.getTitle())) {
                        intent = new Intent(context, BreatheActivity.class);
                    } else if (info.getTitle().contains("柳叶杯")) {
                        intent = new Intent(context, WillowCupActivity.class);
                        intent.putExtra("title", info.getTitle());
                    } else if (info.getTitle().contains("杏林杯")) {
                        intent = new Intent(context, ApricotActivity.class);
                        intent.putExtra("title", info.getTitle());
                    }  else {
                        intent = new Intent(context, SpecialActivity.class);
                    }
                }
                intent.putExtra("aid", info.getAid());
                intent.putExtra("type","home");
                context.startActivity(intent);
            }
        }


    };


    /**
     * 配置ImageLoder
     */
    public void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.img_default) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.img_default) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.img_default) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


    public void initialize(String jsonArray, CycleViewPager cycleViewPagers) {
        this.cycleViewPager = cycleViewPagers;
        try {
            JSONArray a = new JSONArray(jsonArray);
            infos.clear();
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                ADInfo info = new ADInfo();
                info.setAid(o.getString("aid"));
                info.setPicurl(o.getString("picurl"));
                info.setTitle(o.getString("title"));
                if (o.has("isActivity")) {
                    info.setActivity(o.getBoolean("isActivity"));
                } else {
                    info.setActivity(false);
                }
                infos.add(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ADInfo img_yq = new ADInfo();
        img_yq.setAid("");
        img_yq.setPicurl("drawable://" + R.drawable.yaoqinghaoyou);
        img_yq.setTitle("邀请好友");
        infos.add(img_yq);
        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(infos.size() - 1).getPicurl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(context, infos.get(i).getPicurl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(context, infos.get(0).getPicurl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // System.out.println("infos:" + infos.size() + infos.toString());
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
        //清空一下list(如果不清空就会有多个底部导航点，无限增加哈哈)
        views.clear();
    }

    public void loadImageNoCache(String url, ImageView imageView) {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        imageLoader.init(configuration);
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
        imageLoader.displayImage(url, imageView);
    }

    public void loadBackgroundNoCache(String url, LinearLayout linearLayout) {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        imageLoader.init(configuration);
        imageLoader.clearMemoryCache();
        imageLoader.clearDiscCache();
        imageLoader.displayImage(url, (ImageAware) linearLayout);
    }
}
