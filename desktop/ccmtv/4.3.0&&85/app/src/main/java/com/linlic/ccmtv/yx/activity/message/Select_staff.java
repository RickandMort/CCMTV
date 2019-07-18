package com.linlic.ccmtv.yx.activity.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

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
 * Created by Administrator on 2018/10/22. 发私信 人员选择
 */

public class Select_staff extends BaseActivity {

    private Context context;

    @Bind(R.id.select_staff_list_m)
    MyListView select_staff_list_m;
    @Bind(R.id.select_staff_list_x)
    MyListView select_staff_list_x;
    @Bind(R.id.m_text)
    TextView m_text;
    @Bind(R.id.m_down)
    TextView m_down;
    @Bind(R.id.x_text)
    TextView x_text;
    @Bind(R.id.x_down)
    TextView x_down;
    @Bind(R.id.editText1)
    EditText editText1;

    private String catid1 = "";
    private String catid2 = "";
    private int m_page = 1;
    private int x_page = 1;
    private int limit = 5;
    private int m_count = 0;
    private int x_count = 0;

    private BaseListAdapter baseListAdapter_m, baseListAdapter_x;
    private List<Map<String, Object>> listDatax = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listDatam = new ArrayList<Map<String, Object>>();

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
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (m_page == 1) {
//                                    m_count = dataJson.getJSONObject("data").getInt("count");
                                    JSONObject data = dataJson.getJSONObject("data");
                                    try {
                                        m_count = data.getInt("count");
                                    } catch (Exception e) {
                                        m_count = 0;
                                    }
                                    listDatam.clear();
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("position", listDatam.size());
                                    listDatam.add(map);
                                }
                                baseListAdapter_m.notifyDataSetChanged();
                                if (listDatam.size() < 1) {
                                    m_text.setVisibility(View.GONE);
                                    m_down.setVisibility(View.GONE);
                                    select_staff_list_m.setVisibility(View.GONE);
                                } else {
                                    if (m_count > (m_page * limit)) {
                                        m_down.setVisibility(View.VISIBLE);
                                    } else {
                                        m_down.setVisibility(View.GONE);
                                    }
                                    m_text.setVisibility(View.VISIBLE);
                                    select_staff_list_m.setVisibility(View.VISIBLE);
                                }

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
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (x_page == 1) {
//                                    x_count = dataJson.getJSONObject("data").getInt("count");
                                    JSONObject data = dataJson.getJSONObject("data");
                                    try {
                                        x_count = data.getInt("count");
                                    } catch (Exception e) {
                                        x_count = 0;
                                    }
                                    listDatax.clear();
                                }
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("position", listDatax.size());
                                    listDatax.add(map);
                                }
                                baseListAdapter_x.notifyDataSetChanged();
                                if (listDatax.size() < 1) {
                                    x_text.setVisibility(View.GONE);
                                    x_down.setVisibility(View.GONE);
                                    select_staff_list_x.setVisibility(View.GONE);
                                } else {
                                    select_staff_list_x.setVisibility(View.VISIBLE);
                                    x_text.setVisibility(View.VISIBLE);
                                    if (x_count > (x_page * limit)) {
                                        x_down.setVisibility(View.VISIBLE);
                                    } else {
                                        x_down.setVisibility(View.GONE);
                                    }

                                }

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
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.message_select_staff);
        context = this;
        ButterKnife.bind(this);
        findId();
        initView();
        getUrlRulest("1");
        getUrlRulest("2");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Info.html";
        super.onPause();
    }

    public void initView() {

        editText1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Select_staff.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    m_page = 1;
                    x_page = 1;
                    getUrlRulest("1");
                    getUrlRulest("2");
                }
                return false;
            }
        });

        baseListAdapter_m = new BaseListAdapter(select_staff_list_m, listDatam, R.layout.item_message_select_staff) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setImageBitmap(R.id._item_icon, map.get("icon").toString());
                helper.setText(R.id._item_name, map.get("username").toString());


            }
        };
        select_staff_list_m.setAdapter(baseListAdapter_m);
        select_staff_list_m.setOnItemClickListener(new casesharing_listListener());

        baseListAdapter_x = new BaseListAdapter(select_staff_list_x, listDatax, R.layout.item_message_select_staff) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setImageBitmapGlide(context, R.id._item_icon, map.get("icon").toString());
                helper.setText(R.id._item_name, map.get("username").toString());

            }
        };
        select_staff_list_x.setAdapter(baseListAdapter_x);
        select_staff_list_x.setOnItemClickListener(new casesharing_listListener2());


    }


    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listDatam.get(arg2);
            Intent intent = null;
            intent = new Intent(context, New_message.class);
            intent.putExtra("uid", map.get("uid").toString());
            intent.putExtra("username", map.get("username").toString());
            startActivity(intent);

        }

    }

    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listDatax.get(arg2);
            Intent intent = null;
            intent = new Intent(context, New_message.class);
            intent.putExtra("uid", map.get("uid").toString());
            intent.putExtra("username", map.get("username").toString());
            startActivity(intent);

        }

    }

    public void m_page(View view) {
        if (m_count > (m_page * limit)) {
            m_page += 1;
            getUrlRulest("1");
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void x_page(View view) {
        if (x_count > (x_page * limit)) {
            x_page += 1;
            getUrlRulest("2");
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void getUrlRulest(final String type) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myLetterFriends);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", type);
                    obj.put("username", editText1.getText().toString().trim());
                    obj.put("page", type.equals("1") ? m_page : x_page);
                    obj.put("limit", limit);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    Message message = new Message();
                    message.what = type.equals("1") ? 1 : 2;
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
