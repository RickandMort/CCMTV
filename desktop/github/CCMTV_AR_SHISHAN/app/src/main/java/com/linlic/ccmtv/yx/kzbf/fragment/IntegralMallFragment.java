package com.linlic.ccmtv.yx.kzbf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Commodity_module;
import com.linlic.ccmtv.yx.activity.integral_mall.Exchange_record;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMallClassification;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bentley on 2018/11/6.
 */

public class IntegralMallFragment extends KzbfBaseFragment implements View.OnClickListener {
    private static final String TAG = "IntegralMallFragment";
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private Banner banner;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn/Member/Index.html";
    private LinearLayout integral_layout, exchange_record_layout, souvenir, electronic_products, medical_data, more;
    private TextView integral;
    private MyListView gift_list;
    BaseListAdapter baseListAdapter;
    private List<Commodity_module> data = new ArrayList<>();
    private int pause = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            //设置积分
                            integral.setText(jsonObject.getString("usermoney"));
                            JSONArray lunboimgArray = jsonObject.getJSONArray("lunboimg");
                            for (int i = 0; i < lunboimgArray.length(); i++) {
                                images.add(lunboimgArray.getJSONObject(i).getString("img"));
                            }

                            initbanner();

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                Commodity_module commodity_module = new Commodity_module(dataArray.getJSONObject(i));
                                data.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(mContext, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(data.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(mContext, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout gift_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            gift_list.setVisibility(View.VISIBLE);
            gift_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(mContext, code)) {
                gift_nodata.setNetErrorIcon();
            } else {
                gift_nodata.setLastEmptyIcon();
            }
            gift_list.setVisibility(View.GONE);
            gift_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRealResume(boolean isFirst) {
        if (isFirst) {
            LogUtil.e(TAG, "第一次调用");
            setValue();
        } else {
            LogUtil.e(TAG, "除了第一次都调用");
//            initbanner();
        }
        LogUtil.e(TAG, "每次都调用");
    }

    @Override
    public void onRealPause() {
        LogUtil.e(TAG, "RealPause");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initdata();
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始轮播
        if (pause > 0) {
            images.removeAll(images);
            data.removeAll(data);
            pause = 0;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        pause++;

    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        isUserVisibleHinted = false;
        super.onDestroyView();
    }

    public void initdata() {
        baseListAdapter = new BaseListAdapter(gift_list, data, R.layout.gift_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.gift_list_name, ((Commodity_module) item).getTitle());
                helper.setCommodityGridView(R.id.gift_list_gridView, ((Commodity_module) item).getCommodities());
                helper.setIntegral_mallOnClick(R.id.more, ((Commodity_module) item).getTitle());
            }
        };
        gift_list.setAdapter(baseListAdapter);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.integral_mall, container, false);
        banner = (Banner) mView.findViewById(R.id.banner);
        integral = (TextView) mView.findViewById(R.id.integral);
        integral_layout = (LinearLayout) mView.findViewById(R.id.integral_layout);
        exchange_record_layout = (LinearLayout) mView.findViewById(R.id.exchange_record_layout);
        souvenir = (LinearLayout) mView.findViewById(R.id.souvenir);
        electronic_products = (LinearLayout) mView.findViewById(R.id.electronic_products);
        medical_data = (LinearLayout) mView.findViewById(R.id.medical_data);
        more = (LinearLayout) mView.findViewById(R.id.more);
        gift_list = (MyListView) mView.findViewById(R.id.gift_list);
        gift_nodata = (NodataEmptyLayout) mView.findViewById(R.id.gift_nodata);
        return mView;
    }

    public void initbanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public void setValue() {
//        MyProgressBarDialogTools.show(mContext);
        images.clear();
        data.clear();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.jifenIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(mContext));
                    String result = HttpClientUtils.sendPost(mContext, URLConfig.CCMTVAPP_Commodity, obj.toString());

//                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
        more.setOnClickListener(this);
        souvenir.setOnClickListener(this);
        electronic_products.setOnClickListener(this);
        medical_data.setOnClickListener(this);
        exchange_record_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        ArrayList<String> titles = null;
        //判断是否登录
        if (SharedPreferencesTools.getUids(mContext) == null) {
            intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("source", "my");
            startActivity(intent);
            return;
        }
        switch (v.getId()) {
            case R.id.more://更多
                intent = new Intent(mContext, IntegralMallClassification.class);
                intent.putExtra("position", 0);
                break;
            case R.id.souvenir://纪念品
                intent = new Intent(mContext, IntegralMallClassification.class);
                intent.putExtra("position", 1);
                break;
            case R.id.electronic_products://电子产品
                intent = new Intent(mContext, IntegralMallClassification.class);
                intent.putExtra("position", 2);
                break;
            case R.id.medical_data://医学资料
                intent = new Intent(mContext, IntegralMallClassification.class);
                intent.putExtra("position", 3);
                break;
            case R.id.exchange_record_layout:
                intent = new Intent(mContext, Exchange_record.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
