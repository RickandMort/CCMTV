package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAssistantAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineAssistant;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注
 */
public class MyFouseActivity extends BaseActivity {
    Context context;
    private List<DbMedicineAssistant> list_fouse;
    private List<DbMedicineAssistant> listFouseMore = new ArrayList<>();
    private MedicineAssistantAdapter maAdapter;
    private RecyclerView recyclerView;
    private NodataEmptyLayout lt_nodata1;
    private DbMedicineAssistant dbMa;
    private int page = 1;
    private int count;
    private int itemCount;
    private String focus_id = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        if (page == 1)
                            listFouseMore.clear();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list_fouse = new ArrayList<>();
//                            recyclerView.setVisibility(View.VISIBLE);
//                            lt_nodata1.setVisibility(View.GONE);

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbMa = new DbMedicineAssistant(1);
                                dbMa.setUser_img(customJson.getString("user_img"));
                                dbMa.setUid(customJson.getString("uid"));
                                dbMa.setHelper(customJson.getString("helper"));
                                dbMa.setCompany(customJson.getString("company"));
                                dbMa.setDrug(customJson.getString("drug"));
                                dbMa.setArticle_title(customJson.getString("article"));
//                                dbMa.setLook_num(customJson.getString("no_look_num"));
                                dbMa.setLook_num(customJson.getString("look_num"));
                                list_fouse.add(dbMa);
                            }
                            listFouseMore.addAll(list_fouse);
                            itemCount = maAdapter.getItemCount();//总item数
                            maAdapter.loadMoreComplete();
                            maAdapter.notifyDataSetChanged();
                        } else {
                            maAdapter.loadMoreFail();
//                            recyclerView.setVisibility(View.GONE);
//                            lt_nodata1.setVisibility(View.VISIBLE);
//                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        String code = jsonObject.getString("code");
                        if ("code".equals(code)) {
                            setResultStatus(listFouseMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        } else {
                            setResultStatus(listFouseMore.size() > 0, Integer.valueOf(code));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            int removePosition = msg.arg1;
                            maAdapter.remove(removePosition);
                            onResume();
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        maAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(listFouseMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclerView.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            recyclerView.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fouse);
        context = this;

        initView();
        initData();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_fouse_list);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.rl_my_focus_nodata1);
    }

    private void initData() {
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(context));
        maAdapter = new MedicineAssistantAdapter(context, listFouseMore);
        recyclerView.setAdapter(maAdapter);

        maAdapter.disableLoadMoreIfNotFullPage(recyclerView);//取消第一次进入加载 下拉加载更多方法
        maAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                focus_id = listFouseMore.get(position).getUid();
                switch (view.getId()) {
                    case R.id.right://取消关注
                        setCancelFouse(position);
                        break;
                    case R.id.fouse_consult://咨询，跳转发送消息
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("addressee_uid", listFouseMore.get(position).getUid());
                        intent.putExtra("helper", listFouseMore.get(position).getHelper());
                        intent.putExtra("assistant", "0");
                        startActivity(intent);
                        break;
                    case R.id.rl_consult_content:
                        Intent intent1 = new Intent(context, PersonalHomeActivity.class);
                        intent1.putExtra("fuid", listFouseMore.get(position).getUid());
                        startActivity(intent1);
                        recordUserHelper();
                        break;
                }
            }
        });


        maAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            maAdapter.loadMoreEnd();
                        } else {
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    private void recordUserHelper() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recordUserHelper);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_uid", focus_id);
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onResume() {
        page = 1;
        setValue();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userFocusList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看我的关注数据", result);

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
    public void back(View view) {
        finish();
    }

    private void setCancelFouse(final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cancelFocus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_id", focus_id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看取消关注数据", result);

                    Message message = new Message();
                    message.what = 3;
                    message.arg1 = position;
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
