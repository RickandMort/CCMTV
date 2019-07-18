package com.linlic.ccmtv.yx.activity.subscribe.smallfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.subscribe.SubscribeFragment;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.RecyclerviewAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.TitleAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Illness;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.SubKeshiData;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name：公共Fragment
 * author：Larry
 * data：2017/8/1 11:49
 */

public class PublicFragment extends BaseFragment {
    private ListView list_sub;
    private TitleAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private List<SubKeshiData> keshiDatas = new ArrayList<>();
    private List<SubKeshiData> keshiDatas1 = new ArrayList<>();
    private List<Illness> illnessDatas = new ArrayList<>();
    private JSONArray jsonArray;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject result = null;
                    keshiDatas.clear();
                    keshiDatas1.clear();
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray dataArray = result.getJSONArray("data");
                            keshiDatas = new Gson().fromJson(dataArray.toString()
                                    , new TypeToken<List<SubKeshiData>>() {
                                    }.getType());
                            if (illnessDatas.size() != 0) {//选择的科室有疾病
                                for (int i = 0; i < keshiDatas.size(); i++) {
                                    if (illnessDatas.get(0).getIllness().equals(keshiDatas.get(i).getTitle())) {
                                        SubKeshiData subKeshiData = keshiDatas.get(i);
                                        keshiDatas1.add(subKeshiData);
                                    }
//                                    Log.i("科室数据111", keshiDatas.get(i).toString());
                                }
//                                Log.e("titlesIllness", SubscribeFragment.titlesIllness.toString());
                                adapter = new TitleAdapter(getActivity(), keshiDatas1);
                                list_sub.setAdapter(adapter);

                                MyProgressBarDialogTools.show(getContext());
                                //设置布局管理器
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                for (int i = 0; i < illnessDatas.size(); i++) {
//                                    Log.e("illnessDatas", illnessDatas.get(i) + "");
                                }
                                recyclerviewAdapter = new RecyclerviewAdapter(getContext(), illnessDatas);
//                                Log.e("illnessDatas", illnessDatas.toString());
                                //横向recyclerview点击事件
                                recyclerviewAdapter.setOnRecyclerViewListener(new RecyclerviewAdapter.OnRecyclerViewListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        for (int i = 0; i < illnessDatas.size(); i++) {
                                            if (illnessDatas.get(i).isChecked()) {
                                                illnessDatas.get(i).setChecked(false);
                                            }
                                        }
                                        illnessDatas.get(position).setChecked(true);
                                        recyclerviewAdapter.notifyDataSetChanged();
                                        keshiDatas1.clear();
                                        for (int i = 0; i < keshiDatas.size(); i++) {
                                            if (illnessDatas.get(position).getIllness().equals(keshiDatas.get(i).getTitle())) {
                                                SubKeshiData subKeshiData = keshiDatas.get(i);
                                                keshiDatas1.add(subKeshiData);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public boolean onItemLongClick(int position) {
                                        return false;
                                    }
                                });
                                recyclerView.setAdapter(recyclerviewAdapter);
                                MyProgressBarDialogTools.hide();
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                adapter = new TitleAdapter(getActivity(), keshiDatas);
                                list_sub.setAdapter(adapter);
                            }
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(),
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("sub")) {
                initData1(intent.getIntExtra("position", 0));
//                Log.e("position111", intent.getIntExtra("position", 0) + "");
            }
        }

    };

    public void initData1(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("keshiDatas:", "加载了" + SubscribeFragment.followkses.get(index).getId() + SubscribeFragment.followkses.get(index).getName());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("act", URLConfig.getSubscribeData);
                    jsonObject.put("id", SubscribeFragment.followkses.get(index).getId());
//                    Log.e("keshishuju", jsonObject.toString());
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, jsonObject.toString());
//                    Log.e("keshishuju", result.toString());
                    illnessDatas.clear();
                    Illness illness;
                    jsonArray = SubscribeFragment.titlesIllness.get(index);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == 0) {
                                illness = new Illness();
                                JSONObject jsonObject1 = (JSONObject) SubscribeFragment.titlesIllness.get(index).get(i);
                                illness.setIllness(jsonObject1.getString("name"));
                                illness.setChecked(true);
                                illnessDatas.add(illness);
                            } else {
                                illness = new Illness();
                                JSONObject jsonObject1 = (JSONObject) SubscribeFragment.titlesIllness.get(index).get(i);
                                illness.setIllness(jsonObject1.getString("name"));
                                illness.setChecked(false);
                                illnessDatas.add(illness);
                            }
                        }
                    }
//                    Log.e("illnessDatas1", illnessDatas.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public int setLiayoutId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_fragment_one, container, false);
        Log.i("keshiDatas:", "onCreateView");
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("keshiDatas:", "onActivityCreated");
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("sub");
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
        initData1(0);
    }


    @Override
    public void onDestroyNew() {
        Log.i("keshiDatas:", "销毁了");
    }

    public void initView(View view) {
        list_sub = (ListView) view.findViewById(R.id.list_sub);
        recyclerView = (RecyclerView) view.findViewById(R.id.public_recyclerview);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("keshiDatas:", "onPause");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("keshiDatas:", "onDestroy");
        try {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}