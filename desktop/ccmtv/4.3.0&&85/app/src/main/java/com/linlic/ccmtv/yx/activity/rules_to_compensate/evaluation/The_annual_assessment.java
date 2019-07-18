package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 年度评优
 * Created by Administrator on 2018/6/21.
 */

public class The_annual_assessment extends BaseActivity {
    private Context context;
    @Bind(R.id._item_grade)
    NiceSpinner _item_grade;
    @Bind(R.id._item_grade2)
    NiceSpinner _item_grade2;//
    @Bind(R.id.listview)
    ListView listview;//
    @Bind(R.id.search_edit)
    EditText search_edit;//
    @Bind(R.id.ss_edit)
    TextView ss_edit;//
    @Bind(R.id.the_annual_nodata)
    NodataEmptyLayout annual_nodata;
    private Dialog dialog;
    private View view;
    private int page = 1;
    JSONObject result, data;
    private int teachers_counts = 0;
    private List<String> allKeshi_list = new ArrayList<>(), allStatus_list = new ArrayList<>();
    private Map<String, Object> allKeshi_map = new HashMap<>();
    private Map<String, Object> allStatus_map = new HashMap<>();
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
    Map<String, Object> curr_map = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray allKeshiJson = dataJson.getJSONObject("data").getJSONArray("allKeshi");
                                JSONArray allStatusJson = dataJson.getJSONObject("data").getJSONArray("allStatus");
                                for (int i = 0; i < allKeshiJson.length(); i++) {
                                    JSONObject dataJson1 = allKeshiJson.getJSONObject(i);
                                    allKeshi_list.add(dataJson1.getString("name"));
                                    allKeshi_map.put(dataJson1.getString("name"), dataJson1.getString("value"));
                                }
                                for (int i = 0; i < allStatusJson.length(); i++) {
                                    JSONObject dataJson1 = allStatusJson.getJSONObject(i);
                                    allStatus_list.add(dataJson1.getString("name"));
                                    allStatus_map.put(dataJson1.getString("name"), dataJson1.getString("value"));
                                }
                                initNiceSpinner();

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 2:
                    curr_map = listData.get(Integer.parseInt(msg.obj.toString()));
                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(The_annual_assessment.this);
                    view = inflater.inflate(R.layout.dialog_item8, null);

                    // 对话框
                    dialog = new Dialog(The_annual_assessment.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth() - 100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(view);
                    dialog.setCancelable(false);
                    final TextView btn_sure = (TextView) view.findViewById(R.id.i_understand);// 取消
                    final EditText _item_edit = (EditText) view.findViewById(R.id._item_edit);// 输入框
                    final TextView i_understand1 = (TextView) view.findViewById(R.id.i_understand1);// 输入框

                    btn_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            view = null;
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });
                    i_understand1.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //提交评价内容
                            if (_item_edit.getText().toString().trim().length() > 14) {
                                yearGoodTeacherDelVote(curr_map.get("id").toString(), curr_map.get("manage_id").toString(), _item_edit.getText().toString());
                            } else {
                                Toast.makeText(getApplicationContext(), "评语不能少于15个字！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray teachersJson = dataJson.getJSONObject("data").getJSONArray("teachers");
                                if (page == 1) {
                                    listData.clear();
                                    teachers_counts = dataJson.getJSONObject("data").getInt("count");
                                }
                                for (int i = 0; i < teachersJson.length(); i++) {
                                    JSONObject dataJson1 = teachersJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("location", listData.size());//所在位置
                                    map.put("manage_id", dataJson1.getString("manage_id"));//带教老师id
                                    map.put("username", dataJson1.getString("username"));//带教老师用户名
                                    map.put("tname", dataJson1.getString("tname"));//带教老师真实姓名
                                    map.put("hospital_kid", dataJson1.getString("hospital_kid"));
                                    map.put("status", dataJson1.getString("status"));//投票状态 1未投票 2已投票
                                    map.put("is_edit", dataJson1.getString("is_edit"));//是否允许修改 0不允许（已投票） 1允许（未投票）
                                    map.put("id", dataJson1.getString("id"));//投票记录表id
                                    map.put("keshi_name", dataJson1.getString("keshi_name"));
                                    map.put("comment", dataJson1.getString("comment"));//评论内容
                                    map.put("year", dataJson1.getString("year"));
                                    map.put("is_edit", dataJson1.getString("is_edit"));
                                    listData.add(map);
                                }
//                                if (listData.size() < 1) {
//                                    layout_nodata.setVisibility(View.VISIBLE);
//                                } else {
//                                    layout_nodata.setVisibility(View.GONE);
//                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                String message=dataJson.getString("errorMessage");
                                if(!message.contains("暂无数据")){
                                   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    }
                    break;
                case 4:
                    curr_map = listData.get(Integer.parseInt(msg.obj.toString()));

                    // 弹出自定义dialog
                    LayoutInflater inflater2 = LayoutInflater.from(The_annual_assessment.this);
                    view = inflater2.inflate(R.layout.dialog_item10, null);

                    // 对话框
                    dialog = new Dialog(The_annual_assessment.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager2 = getWindowManager();
                    Display display2 = windowManager2.getDefaultDisplay();
                    WindowManager.LayoutParams lp2 = dialog.getWindow().getAttributes();
                    lp2.width = (int) (display2.getWidth() - 100); // 设置宽度
                    dialog.getWindow().setAttributes(lp2);
                    dialog.getWindow().setContentView(view);
                    dialog.setCancelable(false);
                    final TextView btn_qx = (TextView) view.findViewById(R.id.i_understand);// 取消
                    final TextView _item_content = (TextView) view.findViewById(R.id._item_content);// 输入框
                    final TextView i_understand3 = (TextView) view.findViewById(R.id.i_understand1);// 输入框
                    _item_content.setText("确定取消" + curr_map.get("tname").toString() + "老师的投票吗？");
                    btn_qx.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            view = null;
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });
                    i_understand3.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //确定删除
                            yearGoodTeacherDelVote(curr_map.get("id").toString());
                        }
                    });

                    break;
                case 5://取消投票
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialog = null;
                                view = null;
                                curr_map.put("is_edit", 1);
                                curr_map.put("status", "未投票");
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 6://投票成功
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                dialog = null;
                                view = null;
                                curr_map.put("id", dataJson.getJSONObject("data").getString("id"));
                                curr_map.put("is_edit", 0);
                                curr_map.put("status", "已投票");
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            listview.setVisibility(View.VISIBLE);
            annual_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                annual_nodata.setNetErrorIcon();
            } else {
                annual_nodata.setLastEmptyIcon();
            }
            listview.setVisibility(View.GONE);
            annual_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.the_annual_assessment);
        context = this;
        ButterKnife.bind(this);
        findId();
        this.setActivity_title_name("年度评优");
        initViews();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //保存进入的日期

    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
        super.onPause();
    }


    public void initNiceSpinner() {
        _item_grade.setTextColor(Color.BLACK);
        _item_grade.attachDataSource(allKeshi_list);
        _item_grade.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        _item_grade.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("什么数据",String.valueOf(allKeshi_list.get(i)));
            }
        });
        _item_grade2.setTextColor(Color.BLACK);
        _item_grade2.setArrowDrawable(getResources().getDrawable(R.mipmap.down_icon));
        _item_grade2.attachDataSource(allStatus_list);
        _item_grade2.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("什么数据",String.valueOf(allStatus_list.get(i)));
            }
        });

        getUrlRulest2();
    }

    private void initViews() {
        ss_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                page = 1;
                listview.setSelection(0);
                getUrlRulest2();
            }
        });
        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(listview, listData, R.layout.item_the_annual_assessment) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map) item;
                helper.setText(R.id._item_id, map.get("id").toString());
                helper.setText(R.id._item_content, map.get("tname").toString() + "(" + map.get("username").toString() + ")");
                helper.setTag(R.id._item_vote, map.get("location").toString());
                helper.setText(R.id._item_vote, map.get("status").toString());

                if (map.get("is_edit").toString().trim().equals("1")) {
                    helper.setBackground_Image(R.id._item_vote, R.drawable.anniu6);
                } else {
                    helper.setBackground_Image(R.id._item_vote, R.drawable.anniu23);
                }

            }
        };
        listview.setAdapter(baseListAdapterVideo);
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listview.getChildAt(listview.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listview.getHeight()) {
                        int sum = page * 20;
                        if (sum < teachers_counts) {
                            page += 1;
                            getUrlRulest2();
                        }
                    }
                }
            }
        });
    }

    public void setvote(View view) {
        Message message = new Message();
        if (listData.get(Integer.parseInt(view.getTag().toString())).get("is_edit").toString().equals("1")) {
            message.what = 2;
        } else {
            message.what = 4;
        }
        message.obj = view.getTag();
        handler.sendMessage(message);

    }

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yearGoodTeacherParam);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("年度评优一：参数接口（所有科室、所有状态）", result);
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

    public void getUrlRulest2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yearGoodTeachersList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("k_hid", allKeshi_map.get(_item_grade.getText().toString()).toString());
                    obj.put("vote_status", allStatus_map.get(_item_grade2.getText().toString()).toString());
                    obj.put("key_word", search_edit.getText().toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("年度评优二：列表接口", result);
                    Message message = new Message();
                    message.what = 3;
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

    public void yearGoodTeacherDelVote(final String id) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yearGoodTeacherDelVote);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("年度评优四：取消投票", result);
                    Message message = new Message();
                    message.what = 5;
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

    public void yearGoodTeacherDelVote(final String id, final String manage_id, final String comment) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.yearGoodTeacherAddVote);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);
                    obj.put("manage_id", manage_id);
                    obj.put("comment", comment);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("年度评优三：投票接口", result);
                    Message message = new Message();
                    message.what = 6;
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
