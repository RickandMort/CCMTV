package com.linlic.ccmtv.yx.kzbf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.MessageDetialActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.MessageAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMessage;
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
 * 发件箱
 */
public class OutboxFragment extends BaseFragment {
    private int page = 1;
    private int count;
    private int itemCount;
    private List<DbMessage> list;
    private List<DbMessage> listMore = new ArrayList<>();
    private RecyclerView outbox_list;
    private NodataEmptyLayout lt_nodata1;
    private DbMessage dbMe;
    private MessageAdapter meAdapter;
    private String id;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbMe = new DbMessage(1);
                                setBean(customJson);
                                list.add(dbMe);
                            }
                            listMore.addAll(list);
                            itemCount = meAdapter.getItemCount();//总item数
                            meAdapter.loadMoreComplete();
                            meAdapter.notifyDataSetChanged();
                        } else {
                            meAdapter.loadMoreFail();
//                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        if ("code".equals(code)) {
                            LogUtil.e("meAdapter", "code" + code);
                            setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        } else {
                            setResultStatus(listMore.size() > 0, Integer.valueOf(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            if (listMore.size() == 0) {
                                setResultStatus(listMore.size() > 0, Integer.parseInt(code));
                            }
                        } else {
                            Toast.makeText(getContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            outbox_list.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(getContext(), code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            outbox_list.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outbox, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        outbox_list = (RecyclerView) getActivity().findViewById(R.id.outbox_list);
        lt_nodata1 = (NodataEmptyLayout) getActivity().findViewById(R.id.rl_oubox_nodata1);
    }

    private void initData() {
        outbox_list.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getContext()));
        meAdapter = new MessageAdapter(getContext(), listMore);
        outbox_list.setAdapter(meAdapter);

        meAdapter.disableLoadMoreIfNotFullPage(outbox_list);//取消第一次进入加载 下拉加载更多方法
        meAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.message_detial:
                        Intent intent = new Intent(getContext(), MessageDetialActivity.class);
                        intent.putExtra("type", "1");
                        intent.putExtra("id", listMore.get(position).getId());
                        intent.putExtra("helper", listMore.get(position).getHelper());
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case R.id.right_delete:
                        id = listMore.get(position).getId();
                        deleteMessage(position);
                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        //上拉加载更多
        meAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                outbox_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            meAdapter.loadMoreEnd();
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
                    obj.put("act", URLConfig.alreadyPost);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看发件箱数据", result);

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
        dbMe.setId(jsonObject.getString("id"));
        dbMe.setTitle(jsonObject.getString("title"));
        dbMe.setAddressor_uid(jsonObject.getString("addressee_uid"));
        dbMe.setAddtime(jsonObject.getString("addtime"));
        dbMe.setU_is_look("1");//收件箱不显示是否以查看红点
        dbMe.setHelper(jsonObject.getString("helper"));
        dbMe.setUser_img(jsonObject.getString("user_img"));
    }

    public void deleteMessage(final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.messageDel);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("id", id);

//                    Log.e("看看删除消息数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看删除消息数据", result);

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
