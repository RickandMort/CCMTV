package com.linlic.ccmtv.yx.activity.subscribe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubscribeFragmentNew extends BaseFragment {

    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mDataList = Arrays.asList(CHANNELS);
    private ViewPager mViewPager;

    private ImageView iv_addsub;
    private LinearLayout layout_subnologin, layout_nodata1;
    private List<String> titles = new ArrayList();
    private RelativeLayout layout_keshitype;
    private Button btn_tosub;
    public static List<Followks> followkses;
    List<Followks> nfollowkses;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        titles.clear();
                        JSONObject result = new JSONObject(msg.obj.toString());
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject data = result.getJSONObject("data");
                            followkses = new Gson().fromJson(data.getJSONArray("followks").toString()
                                    , new TypeToken<List<Followks>>() {
                                    }.getType());

                            if (followkses.size() == 0) {
                                layout_nodata1.setVisibility(View.VISIBLE);
                                layout_keshitype.setVisibility(View.GONE);
                            } else {
                                layout_nodata1.setVisibility(View.GONE);
                                layout_keshitype.setVisibility(View.VISIBLE);
                            }
                            nfollowkses = new Gson().fromJson(data.getJSONArray("nfollowks").toString()
                                    , new TypeToken<List<Followks>>() {
                                    }.getType());

                            for (int i = 0; i < followkses.size(); i++) {
                                titles.add(i, followkses.get(i).getName());
                            }

//                            Log.i("Titles", titles.toString());
//                            Log.i("Titles", followkses.toString());
//                            Log.i("Titles", "刷新title");
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(),
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscribe_fragment_new, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        setClick();
    }

    private void initView() {
        mViewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
        iv_addsub = (ImageView) getActivity().findViewById(R.id.iv_addsub);
        layout_subnologin = (LinearLayout) getActivity().findViewById(R.id.layout_subnologin);
        layout_nodata1 = (LinearLayout) getActivity().findViewById(R.id.layout_nodata1);
        btn_tosub = (Button) getActivity().findViewById(R.id.btn_tosub);
        layout_keshitype = (RelativeLayout) getActivity().findViewById(R.id.layout_keshitype);

    }

    public void initData() {
//        Log.e("qqqqq","initdata");
        String uid = SharedPreferencesTools.getUids(getActivity());
        if (uid == null || ("").equals(uid)) {
            layout_subnologin.setVisibility(View.VISIBLE);
        } else {
            layout_subnologin.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.subscribeKsnew);
                        obj.put("uid", SharedPreferencesTools.getUidToLoginClose(getActivity()));
                        String result = HttpClientUtils.sendPost(getActivity(),
                                URLConfig.CCMTVAPP1, obj.toString());
//                        Log.e("subresult", result.toString());
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }
    }

    private void setClick() {
        iv_addsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubscribeManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followkses", (Serializable) followkses);
                bundle.putSerializable("nfollowkses", (Serializable) nfollowkses);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        btn_tosub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubscribeManagerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("followkses", (Serializable) followkses);
                bundle.putSerializable("nfollowkses", (Serializable) nfollowkses);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e("qqqqq","onresume");
        initData();
    }

}
