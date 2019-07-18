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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.MessageActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MoreRecommendActivity;
import com.linlic.ccmtv.yx.kzbf.activity.PersonalHomeActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.MedicineAssistantAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineAssistant;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 药讯助手
 */
//item_fouse_consult
public class MedicineAssistantFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private TextView more, medicine_assistant_more, load_more;
    private RecyclerView assistant_list, assistant_fouse_list;
    private List<DbMedicineAssistant> list_fouse;
    private List<DbMedicineAssistant> listFouseMore = new ArrayList<>();
    private int focusCount, focusItemCount;
    private List<DbMedicineAssistant> list;
    private List<DbMedicineAssistant> listMore = new ArrayList<>();
    private int count, itemCount;
    private DbMedicineAssistant dbMa;
    private DbMedicineAssistant dbMaf;
    private MedicineAssistantAdapter maAdapter;
    private MedicineAssistantAdapter mafAdapter;
    private int focusPage = 1;
    private int recommendPage = 1;
    private String focus_id;
    private String isShow = "1";
    private View footer;
    private ImageView assistant_hide_list;
    private RelativeLayout focusHead, recommendHead;
    private InformationCenterFragment medicineMessageActivity;
    private boolean pauseTime = false; //进入onResume方法后判断是否需要重新设置activity中的magicindicator状态
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            focusCount = Integer.parseInt(jsonObject.getString("count"));

                            list_fouse = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("focus_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbMa = new DbMedicineAssistant(1);
                                dbMa.setUser_img(customJson.getString("user_img"));
                                dbMa.setUid(customJson.getString("uid"));
                                dbMa.setHelper(customJson.getString("helper"));
                                dbMa.setCompany(customJson.getString("company"));
                                dbMa.setDrug(customJson.getString("drug"));
                                dbMa.setArticle_title(customJson.getString("article_title"));
                                dbMa.setLook_num(customJson.getString("look_num"));
                                list_fouse.add(dbMa);
                            }
                            listFouseMore.addAll(list_fouse);
                            maAdapter.notifyDataSetChanged();
                            focusHead.setVisibility(View.VISIBLE);
                            assistant_fouse_list.setVisibility(View.VISIBLE);
                        } else {
                            focusHead.setVisibility(View.GONE);
                            assistant_fouse_list.setVisibility(View.GONE);
//                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            count = Integer.parseInt(jsonObject.getString("count"));

                            list = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("rec_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbMaf = new DbMedicineAssistant(2);
                                dbMaf.setUser_img(customJson.getString("user_img"));
                                dbMaf.setUid(customJson.getString("uid"));
                                dbMaf.setHelper(customJson.getString("helper"));
                                dbMaf.setCompany(customJson.getString("company"));
                                dbMaf.setDrug(customJson.getString("drug"));
                                dbMaf.setArticle_title(customJson.getString("article_title"));
                                dbMaf.setIsShowRedDot(customJson.getString("is_show_red"));
                                list.add(dbMaf);
                            }
                            listMore.addAll(list);
                            mafAdapter.notifyDataSetChanged();
                            if (mafAdapter.getItemCount() > 0) {
                                recommendHead.setVisibility(View.VISIBLE);
                            } else {
                                recommendHead.setVisibility(View.GONE);
                            }
                        } else {
//                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
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
                            mafAdapter.remove(removePosition);
                            onResume();
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        mafAdapter.notifyDataSetChanged();
                        maAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            int removePosition = msg.arg1;
                            maAdapter.remove(removePosition);
                            onResume();
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        mafAdapter.notifyDataSetChanged();
                        maAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_medicine_assistant, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        medicineMessageActivity = (InformationCenterFragment) getParentFragment();
        initView();
    }

    private void initView() {
        assistant_list = (RecyclerView) mView.findViewById(R.id.assistant_list);
        focusHead = (RelativeLayout) mView.findViewById(R.id.focus_head);
        recommendHead = (RelativeLayout) mView.findViewById(R.id.recommemd_head);
        assistant_fouse_list = (RecyclerView) mView.findViewById(R.id.assistant_fouse_list);
        medicine_assistant_more = (TextView) mView.findViewById(R.id.medicine_assistant_more);
        assistant_hide_list = (ImageView) mView.findViewById(R.id.assistant_hide_list);
    }

    @Override
    public void onResume() {
        listFouseMore.clear();
        listMore.clear();
        initData();
        setFocusValue();
        setRecommendValue();

        if (pauseTime) {
            medicineMessageActivity.setViewpagerItem();
            medicineMessageActivity.getIsShowRedDot();
            pauseTime = false;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initData() {
        medicine_assistant_more.setOnClickListener(this);
        assistant_hide_list.setOnClickListener(this);

        assistant_fouse_list.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getContext()));
        maAdapter = new MedicineAssistantAdapter(getContext(), listFouseMore);
        assistant_fouse_list.setAdapter(maAdapter);
        setFooter(assistant_fouse_list);

        maAdapter.disableLoadMoreIfNotFullPage(assistant_fouse_list);
        maAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                focus_id = listFouseMore.get(position).getUid();
                switch (view.getId()) {
                    case R.id.right://取消关注
                        pauseTime = true;
                        setCancelFouse(position);
                        break;
                    case R.id.fouse_consult://咨询，跳转发送消息
                        Intent intent = new Intent(getContext(), MessageActivity.class);
                        intent.putExtra("addressee_uid", listFouseMore.get(position).getUid());
                        intent.putExtra("helper", listFouseMore.get(position).getHelper());
                        intent.putExtra("assistant", "0");
                        startActivity(intent);
                        break;
                    case R.id.rl_consult_content:
                        pauseTime = true;
                        Intent intent1 = new Intent(getContext(), PersonalHomeActivity.class);
                        intent1.putExtra("fuid", focus_id);
                        startActivity(intent1);
                        recordUserHelper();
                        break;
                }
            }
        });

        assistant_list.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getContext()));
        mafAdapter = new MedicineAssistantAdapter(getContext(), listMore);
        assistant_list.setAdapter(mafAdapter);

        mafAdapter.disableLoadMoreIfNotFullPage(assistant_list);
        mafAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                focus_id = listMore.get(position).getUid();
                switch (view.getId()) {
                    case R.id.rl_fouse://关注
                        pauseTime = true;
                        setFouse(position);
                        isShow = "1";
                        assistant_hide_list.setImageResource(R.mipmap.icon_hide_list);
                        assistant_fouse_list.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rl_focus_content:
                        pauseTime = true;
                        Intent intent1 = new Intent(getContext(), PersonalHomeActivity.class);
                        intent1.putExtra("fuid", focus_id);
                        startActivity(intent1);
                        recordUserHelper();
                        break;
                }
            }
        });
    }

    private void setFocusValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.helper);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", focusPage + "");
                    obj.put("type", "focus");

//                    Log.e("看看助手关注数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看助手关注数据", result);

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

    private void setRecommendValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.helper);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("page", recommendPage + "");
                    obj.put("type", "recommend");

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看助手推荐数据", result);

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

    private void setFouse(final int position) {
        MyProgressBarDialogTools.show(getContext());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focus);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("focus_id", focus_id);

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看关注数据", result);
                    MyProgressBarDialogTools.hide();

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

    private void setCancelFouse(final int position) {
        MyProgressBarDialogTools.show(getContext());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cancelFocus);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("focus_id", focus_id);

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看取消关注数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 4;
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


    private void recordUserHelper() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recordUserHelper);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    obj.put("focus_uid", focus_id);
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    //关注列表添加底部
    private void setFooter(RecyclerView recyclerView) {
        footer = LayoutInflater.from(getContext()).inflate(R.layout.item_fouse_footer, recyclerView, false);
        load_more = (TextView) footer.findViewById(R.id.tv_fouse_more);
        load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusItemCount = maAdapter.getItemCount();
                if (focusItemCount >= focusCount) {
                    load_more.setText("没有更多数据");
                } else {
                    focusPage++;
                    setFocusValue();
                }
            }
        });
        maAdapter.setFooterView(footer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.medicine_assistant_more:
                startActivity(new Intent(getContext(), MoreRecommendActivity.class));
                break;
            case R.id.assistant_hide_list:
                if (isShow.equals("1")) {
                    isShow = "0";
                    assistant_hide_list.setImageResource(R.mipmap.icon_show_list);
                    assistant_fouse_list.setVisibility(View.GONE);
                    maAdapter.notifyDataSetChanged();
                } else {
                    isShow = "1";
                    assistant_hide_list.setImageResource(R.mipmap.icon_hide_list);
                    assistant_fouse_list.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        focusPage = 1;
        recommendPage = 1;
    }
}
