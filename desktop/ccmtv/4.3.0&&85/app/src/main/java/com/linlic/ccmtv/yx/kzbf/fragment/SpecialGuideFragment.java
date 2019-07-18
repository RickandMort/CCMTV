package com.linlic.ccmtv.yx.kzbf.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.SpecialGuideDetialActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.SpecialGuideAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题指南
 */
//item_literature2
public class SpecialGuideFragment extends KzbfBaseFragment {
    private View mView;
    private RecyclerView recyclerView;
    private TextView medicine_more;
    private String img_num;
    private NodataEmptyLayout lt_nodata1;
    private List<DbMedicine> list;
    private List<DbMedicine> listMore = new ArrayList<>();
    private DbMedicine dbMd;
    private SpecialGuideAdapter sgAdapter;
    private int page = 1;
    private int count;
    private int itemCount;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        if (page == 1) listMore.clear();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                img_num = customJson.getString("img_num");
                                //根据不同图片选择布局
                                /*if (img_num.equals("3")) {
                                    dbMd = new DbMedicine(1);
                                    setBean(customJson);
                                    JSONArray array = customJson.getJSONArray("img_url");
                                    dbMd.setImg_url1((String) array.get(0));
                                    dbMd.setImg_url2((String) array.get(1));
                                    dbMd.setImg_url3((String) array.get(2));
                                } else if (img_num.equals("1")) {
                                    dbMd = new DbMedicine(3);
                                    setBean(customJson);
                                    JSONArray array = customJson.getJSONArray("img_url");
                                    dbMd.setImg_url1((String) array.get(0));
                                } else {
                                    dbMd = new DbMedicine(2);
                                    setBean(customJson);
                                }*/
                                dbMd = new DbMedicine(2);
                                setBean(customJson);
                                list.add(dbMd);
                            }
                            listMore.addAll(list);
                            itemCount = sgAdapter.getItemCount();//总item数
                            sgAdapter.loadMoreComplete();
                            sgAdapter.notifyDataSetChanged();
                        } else {
                            sgAdapter.loadMoreFail();
//                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        if ("code".equals(code)) {
                            setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        } else {
                            setResultStatus(listMore.size() > 0, Integer.valueOf(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 500:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (getActivity() == null) return;
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(getContext(), code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_special_guide, container, false);
        initview();
        initData();
        return mView;
    }


    private void initview() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.guide_list);
        lt_nodata1 = (NodataEmptyLayout) mView.findViewById(R.id.rl_sguide_nodata1);
    }

    @Override
    public void onRealResume(boolean isFirst) {
        super.onRealResume(isFirst);
        page = 1;
        setValue();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.subject);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("看看专题指南数据", result);

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

    private void initData() {
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getContext()));
        sgAdapter = new SpecialGuideAdapter(getContext(), listMore);
        recyclerView.setAdapter(sgAdapter);
//        setHeader(recyclerView);

        sgAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        sgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                listMore.get(position).setIs_show_red("0");
                sgAdapter.notifyItemChanged(position);

                Intent intent = new Intent(getContext(), SpecialGuideDetialActivity.class);
                intent.putExtra("id", listMore.get(position).getId());
                intent.putExtra("uid", listMore.get(position).getUid());
                startActivity(intent);
            }
        });

        sgAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            sgAdapter.loadMoreEnd();
                        } else {
                            page++;
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    private void setBean(JSONObject customJson) throws JSONException {
        dbMd.setId(customJson.getString("id"));
        dbMd.setTitle(customJson.getString("title"));
        dbMd.setDescribe(customJson.getString("describe"));
        dbMd.setUid(customJson.getString("uid"));
        dbMd.setLook_num(customJson.getString("look_num"));
        dbMd.setPosttime(customJson.getString("posttime"));
        dbMd.setAuthor(customJson.getString("author"));
        dbMd.setSource(customJson.getString("source"));
        dbMd.setIs_show_red(customJson.getString("is_show_red"));
    }

    //指南列表添加头部
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.item_medicine_head, view, false);
        medicine_more = (TextView) header.findViewById(R.id.medicine_more);
        medicine_more.setVisibility(View.INVISIBLE);
        sgAdapter.setHeaderView(header);
    }
}
