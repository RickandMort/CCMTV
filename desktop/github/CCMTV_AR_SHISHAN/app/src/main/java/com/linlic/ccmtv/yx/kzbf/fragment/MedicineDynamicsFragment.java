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
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineDetialActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MedicineMoreActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineDynamicAdapter;
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
 * 药讯动态
 */
//item_medicine
public class MedicineDynamicsFragment extends BaseFragment {
    private View mView;
    private RecyclerView recyclerView;
    private TextView medicine_more;
    private String img_num;
    private String delete_aid = "";
    private NodataEmptyLayout lt_nodata1;
    private List<DbMedicine> list;
    private List<DbMedicine> listMore = new ArrayList<>();
    private DbMedicine dbMd;
    private MedicineDynamicAdapter mdAdapter;
    private int page = 1;
    private int count;
    private int itemCount;
    private StringBuilder str = new StringBuilder();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        if (page == 1) {
                            listMore.clear();
                        }
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String code = jsonObject.getString("code");
                        if (code.equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();

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
//                                    String img_url = customJson.getString("img_url");
////                                    dbMd.setImg_url1((String) array.get(0));
////                                    dbMd.setImg_url2((String) array.get(1));
////                                    dbMd.setImg_url3((String) array.get(2));
//                                } else if (img_num.equals("1")) {
//                                    dbMd = new DbMedicine(3);
//                                    setBean(customJson);
//                                    String img_url = customJson.getString("img_url");
//                                    dbMd.setImg_url1(img_url);
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
//                                    JSONArray array = customJson.getJSONArray("img_url");
//                                    dbMd.setImg_url1(array.length() > 0 ? (String) array.get(0) : "");
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
//                                    JSONArray array = customJson.getJSONArray("img_url");
//                                    dbMd.setImg_url1(array.length() > 0 ? (String) array.get(0) : "");
                                }
                                list.add(dbMd);
                            }
                            listMore.addAll(list);
                            itemCount = mdAdapter.getItemCount();//总item数
                            mdAdapter.loadMoreComplete();
                            mdAdapter.notifyDataSetChanged();
                        } else {
                            mdAdapter.loadMoreFail();
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
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(listMore.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_medicine_dynamics, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.dynamics_list);
        lt_nodata1 = (NodataEmptyLayout) mView.findViewById(R.id.rl_mdynamic_nodata1);
//        medicine_more = (TextView) mView.findViewById(R.id.medicine_more);
//        medicine_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getContext(), MedicineMoreActivity.class));
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        setValue();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recommend);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", page);
                    obj.put("aid", str.toString());

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("看看药讯动态数据", result);

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
        mdAdapter = new MedicineDynamicAdapter(getContext(), listMore);
        recyclerView.setAdapter(mdAdapter);
        setHeader(recyclerView);

        mdAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        mdAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), MedicineDetialActivity.class);
                intent.putExtra("id", listMore.get(position).getId());
                intent.putExtra("uid", listMore.get(position).getUid());
                startActivity(intent);
            }
        });
        //删除条目按钮点击事件
        mdAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.medicine_delete1:
                    case R.id.medicine_delete2:
                    case R.id.medicine_delete3:
                        delete_aid = listMore.get(position).getId();
                        if (str.length() == 0) {
                            str.append(delete_aid);
                        } else {
                            str.append(",").append(delete_aid);
                        }
                        mdAdapter.remove(position);
                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        //上拉加载更多
        mdAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemCount >= count) {
                            //数据全部加载完毕
                            mdAdapter.loadMoreEnd();
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
        dbMd.setPosttime(customJson.getString("posttime"));
        dbMd.setLook_num(customJson.getString("look_num"));
        dbMd.setLaud_num(customJson.getString("laud_num"));
        dbMd.setHelper(customJson.getString("helper"));
        dbMd.setCompany(customJson.getString("company"));
        dbMd.setDrug(customJson.getString("drug"));
        dbMd.setUser_img(customJson.getString("user_img"));
        dbMd.setIs_show_red(customJson.getString("is_show_red"));
        dbMd.setImg_type(Integer.parseInt(customJson.getString("img_type")));
        dbMd.setIs_show_survey_btn(customJson.getInt("is_show_survey_btn"));
        if (dbMd.getIs_show_survey_btn() == 1) {//只有显示参与按钮的时候才显示用户是否参与（返回的数据才有该字段）
            dbMd.setIs_show_user_partake(customJson.getInt("is_show_user_partake"));
            dbMd.setSurvey_integral(customJson.getInt("survey_integral"));
        }
    }

    //动态列表添加头部
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.item_medicine_head, view, false);
        medicine_more = (TextView) header.findViewById(R.id.medicine_more);
        medicine_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MedicineMoreActivity.class));
            }
        });
        mdAdapter.setHeaderView(header);
    }

}
