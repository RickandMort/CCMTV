package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
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
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDynamicAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicine;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 药讯动态更多
 */
public class MedicineMoreActivity extends BaseActivity {
    Context context;
    private RecyclerView recyclerView;
    private RelativeLayout lt_nodata1;
    private int page = 1;
    private int count;
    private int itemCount;
    private String delete_aid = "";
    private String img_num;
    private List<DbMedicine> list;
    private List<DbMedicine> listMore = new ArrayList<>();
    private DbMedicine dbMd;
    private MedicineDynamicAdapter mdAdapter;
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
                                img_num = customJson.getString("img_num");
                                String img_type = customJson.getString("img_type");
                                String is_show_survey_btn = customJson.getString("is_show_survey_btn");
                                //根据不同图片选择布局
//                                if (img_num.equals("3")) {
//                                    dbMd = new DbMedicine(1);
//                                    setBean(customJson);
//                                    JSONArray array = customJson.getJSONArray("img_url");
//                                    dbMd.setImg_url1((String) array.get(0));
//                                    dbMd.setImg_url2((String) array.get(1));
//                                    dbMd.setImg_url3((String) array.get(2));
//                                } else if (img_num.equals("1")) {
//                                    dbMd = new DbMedicine(3);
//                                    setBean(customJson);
//                                    JSONArray array = customJson.getJSONArray("img_url");
//                                    dbMd.setImg_url1((String) array.get(0));
//                                } else {
//                                    dbMd = new DbMedicine(2);
//                                    setBean(customJson);
//                                }
                                if (img_num.equals("1")) {
                                    if ("1".equals(img_type))
                                        dbMd = new DbMedicine(DbMedicine.TYPE_PIC_1);//大图
                                    else
                                        dbMd = new DbMedicine(DbMedicine.TYPE_PIC_1_SMALL);//小图
                                    setBean(customJson);
                                    String img_url = customJson.getString("img_url");
                                    dbMd.setImg_url1(img_url);
                                } else if (img_num.equals("0")) {
                                    if ("1".equals(is_show_survey_btn)) {
                                        dbMd = new DbMedicine(DbMedicine.TYPE_PIC_0_PARTICIPATE);//调研
                                    } else {
                                        dbMd = new DbMedicine(DbMedicine.TYPE_PIC_0);//纯文章或者投票
                                    }
                                    setBean(customJson);
                                    String img_url = customJson.getString("img_url");
                                    dbMd.setImg_url1(img_url);
                                }
                                list.add(dbMd);
                            }
                            listMore.addAll(list);
                            itemCount = mdAdapter.getItemCount();//总item数
                            mdAdapter.loadMoreComplete();
                            mdAdapter.notifyDataSetChanged();
                        } else {
                            mdAdapter.loadMoreFail();
                            recyclerView.setVisibility(View.GONE);
                            lt_nodata1.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.activity_medicine_more);
        context = this;

        initView();
        initData();
        setValue();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.medicine_more_list);
        lt_nodata1 = (RelativeLayout) findViewById(R.id.rl_medicine_more_nodata1);
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mdAdapter = new MedicineDynamicAdapter(context, listMore);
        recyclerView.setAdapter(mdAdapter);

        mdAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context, MedicineDetialActivity.class);
                intent.putExtra("id", listMore.get(position).getId());
                intent.putExtra("uid", listMore.get(position).getUid());
                startActivity(intent);
            }
        });
        //上拉加载更多
        mdAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            mdAdapter.loadMoreEnd();
                        } else {
                            setValue();
                        }
                    }
                }, 1500);
            }
        });
    }

    private void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trends);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("aid", delete_aid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看更多数据", result);
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

    public void back(View v) {
        finish();
    }

    private void setBean(JSONObject customJson) throws JSONException {
        dbMd.setId(customJson.getString("id"));
        dbMd.setTitle(customJson.getString("title"));
        dbMd.setDescribe(customJson.getString("describe"));
        dbMd.setUid(customJson.getString("uid"));
        dbMd.setPosttime(customJson.getString("posttime"));
        dbMd.setLook_num(customJson.getString("look_num"));
        dbMd.setLaud_num(customJson.getString("laud_num"));
        dbMd.setHelper(customJson.getString("helper"));
        dbMd.setCompany(customJson.getString("company"));
        dbMd.setDrug(customJson.getString("drug"));
        dbMd.setUser_img(customJson.getString("user_img"));
        dbMd.setIs_show_red(customJson.getString("is_show_red"));
        dbMd.setImg_type(customJson.getInt("img_type"));
        dbMd.setIs_show_survey_btn(customJson.getInt("is_show_survey_btn"));
        if (dbMd.getIs_show_survey_btn() == 1) {//只有显示参与按钮的时候才显示用户是否参与（返回的数据才有该字段）
            dbMd.setIs_show_user_partake(customJson.getInt("is_show_user_partake"));
            dbMd.setSurvey_integral(customJson.getInt("survey_integral"));
        }
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
