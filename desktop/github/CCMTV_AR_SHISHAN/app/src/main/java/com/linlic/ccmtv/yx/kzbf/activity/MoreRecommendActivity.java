package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAssistantAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineAssistant;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 更多推荐
 */
public class MoreRecommendActivity extends BaseActivity {
    private RecyclerView more_recommend_list;
    Context context;
    private RelativeLayout lt_nodata1;
    private List<DbMedicineAssistant> list;
    private List<DbMedicineAssistant> listMore = new ArrayList<>();
    private DbMedicineAssistant dbRa;
    private MedicineAssistantAdapter maAdapter;
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
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();
                            more_recommend_list.setVisibility(View.VISIBLE);
                            lt_nodata1.setVisibility(View.GONE);

                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbRa = new DbMedicineAssistant(2);
                                dbRa.setUid(customJson.getString("uid"));
                                dbRa.setHelper(customJson.getString("helper"));
                                dbRa.setCompany(customJson.getString("company"));
                                dbRa.setDrug(customJson.getString("drug"));
                                dbRa.setArticle_title(customJson.getString("article_title"));
                                dbRa.setUser_img(customJson.getString("user_img"));
                                list.add(dbRa);
                            }
                            listMore.addAll(list);
                            itemCount = maAdapter.getItemCount();//总item数
                            maAdapter.loadMoreComplete();
                        } else {
                            maAdapter.loadMoreFail();
                            more_recommend_list.setVisibility(View.GONE);
                            lt_nodata1.setVisibility(View.VISIBLE);
//                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            maAdapter.remove(msg.arg1);
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_recommend);
        context = this;

        intiView();
        initData();
        setValue();
    }

    private void intiView() {
        more_recommend_list = (RecyclerView) findViewById(R.id.more_recommend_list);
        lt_nodata1 = (RelativeLayout) findViewById(R.id.rl_more_recommend_nodata1);
    }

    private void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focusMany);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);

//                    Log.e("看看更多关注数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看更多关注数据", result);
                    MyProgressBarDialogTools.hide();

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
        more_recommend_list.setLayoutManager(new LinearLayoutManager(context));
        maAdapter = new MedicineAssistantAdapter(context, listMore);
        more_recommend_list.setAdapter(maAdapter);

        maAdapter.disableLoadMoreIfNotFullPage(more_recommend_list);
        maAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                focus_id = listMore.get(position).getUid();
                setFouse(position);
            }
        });
        maAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                more_recommend_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            maAdapter.loadMoreEnd();
                        } else {
                            page++;
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    public void back(View v) {
        finish();
    }

    private void setFouse(final int position) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_id", focus_id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看关注数据", result);

                    Message message = new Message();
                    message.what = 2;
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
