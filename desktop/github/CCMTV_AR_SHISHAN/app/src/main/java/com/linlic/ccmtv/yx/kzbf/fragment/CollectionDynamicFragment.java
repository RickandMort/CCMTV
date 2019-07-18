package com.linlic.ccmtv.yx.kzbf.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineDetialActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.MyCollectionAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbCollection;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏-药讯动态
 */
public class CollectionDynamicFragment extends BaseFragment {
    private int page = 1;
    private int count;
    private int itemCount;
    private String id;
    private int mPosition;
    private RecyclerView recyclerView;
    private RelativeLayout lt_nodata1;
    private List<DbCollection> list;
    private List<DbCollection> listMore = new ArrayList<>();
    private DbCollection dbCt;
    private MyCollectionAdapter mcAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();
                            recyclerView.setVisibility(View.VISIBLE);
                            lt_nodata1.setVisibility(View.GONE);

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbCt = new DbCollection(2);
                                setBean(customJson);
                                list.add(dbCt);
                            }

                            listMore.addAll(list);
                            itemCount = mcAdapter.getItemCount();//总item数
                            mcAdapter.loadMoreComplete();
                            mcAdapter.notifyDataSetChanged();
                        } else {
                            mcAdapter.loadMoreFail();

                            recyclerView.setVisibility(View.GONE);
                            lt_nodata1.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            listMore.remove(mPosition);
                            mcAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(),"已取消",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection_dynamic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.cl_dynamic_list);
        lt_nodata1 = (RelativeLayout) getActivity().findViewById(R.id.rl_dynamic_nodata1);
    }

    private void initData() {
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getContext()));
        mcAdapter = new MyCollectionAdapter(getContext(), listMore);
        recyclerView.setAdapter(mcAdapter);

        mcAdapter.disableLoadMoreIfNotFullPage(recyclerView);//取消第一次进入加载 下拉加载更多方法
        mcAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.content://条目点击
                        Intent intent = new Intent(getContext(), MedicineDetialActivity.class);
                        intent.putExtra("id", listMore.get(position).getId());
                        startActivity(intent);
                        break;
                    case R.id.right_delete://点击收藏
                        id = listMore.get(position).getId();
                        mPosition = position;
                        setCollection();
                        break;
                }
            }
        });

        //上拉加载更多
        mcAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            mcAdapter.loadMoreEnd();
                        } else {
                            page++;
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleCollectList);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", page);
                    obj.put("cid", "1");

//                    Log.e("看看动态收藏箱数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看动态收藏箱数据", result);

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
        listMore.clear();
        initData();
        setValue();
        super.onResume();
    }

    private void setBean(JSONObject jsonObject) throws JSONException {
        dbCt.setId(jsonObject.getString("id"));
        dbCt.setTitle(jsonObject.getString("title"));
        dbCt.setPosttime(jsonObject.getString("posttime"));
        dbCt.setDrug(jsonObject.getString("drug"));
        dbCt.setImg_url(jsonObject.getString("img_url"));
    }

    //收藏/取消收藏
    private void setCollection() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleCollect);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("id", id);

//                    Log.e("看看收藏数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看收藏数据", result);

                    Message message = new Message();
                    message.what = 2;
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

}
