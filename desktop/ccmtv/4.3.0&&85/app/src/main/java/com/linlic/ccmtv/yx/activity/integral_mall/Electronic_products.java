package com.linlic.ccmtv.yx.activity.integral_mall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.entity.Commodity;
import com.linlic.ccmtv.yx.adapter.CommodityGridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name：电子产品
 * author：Larry
 * data：2017/8/1 11:49
 */

public class Electronic_products extends BaseFragment {
    private GridView electronic_products_gridView;
    private Context context;
    private CommodityGridAdapter adapter;
    private int pause = 0;
    private List<Commodity> commodities = new ArrayList<>();
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject result = null;
                    commodities.clear();
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONArray dataArray = result.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                commodities.add(new Commodity(dataArray.getJSONObject(i)));
                            }
                            adapter = new CommodityGridAdapter(getActivity(), commodities);
                            electronic_products_gridView.setAdapter(adapter);
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(),
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(commodities.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(commodities.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout electronic_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            electronic_products_gridView.setVisibility(View.VISIBLE);
            electronic_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                electronic_nodata.setNetErrorIcon();
            } else {
                electronic_nodata.setLastEmptyIcon();
            }
            electronic_products_gridView.setVisibility(View.GONE);
            electronic_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gift_list_item4, container, false);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getContext();
        findId();
        setValue2();

    }


    @Override
    public void findId() {
        super.findId();
        electronic_products_gridView = (GridView) getActivity().findViewById(R.id.gift_list_gridView4);
        electronic_nodata = (NodataEmptyLayout) getActivity().findViewById(R.id.electronic_nodata);

    }


    public void setValue2() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.morelist);
                    obj.put("type", "dz");

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP_Commodity, obj.toString());
//                    Log.e("积分商城 电子", result);

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
    }

    @Override
    public void onResume() {
        if (pause > 0) {
            commodities.removeAll(commodities);
            setValue2();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        pause++;
        super.onPause();
    }

}