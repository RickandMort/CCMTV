package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yu 我的消息
 */
public class MyMessageActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ListView message_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private String uid;
    Map<String, Object> map;
    Context context;
    Button btn_get, btn_send, btn_new;
    String FLG = "receiver";
    int page = 1;
    String Str_username;
    private boolean isNoMore = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if (page == 1) {
                        MyProgressBarDialogTools.hide();
                    }
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                if (page == 1) {
                                    showNoData();
                                }
                            } else {
                                hideNoData();
                                if (page != 1 && dataArray.length() < 10) {
                                    isNoMore = true;
                                    Toast.makeText(context, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    map = new HashMap<String, Object>();
                                    map.put("touid", object.getString("touid"));    // 收信人uid
                                    map.put("pusername", object.getString("pusername")); //发信人姓名
                                    map.put("title", object.getString("title")); //标题
                                    map.put("fromuid", object.getString("fromuid")); //发信人uid
                                    String Str_date = object.getString("mdate");
                                    if (!Str_date.isEmpty()) {
                                        map.put("mdate", Str_date.substring(5, 10)); //消息最近发送时间
                                    }
                                    map.put("ifnew", object.getString("ifnew"));//是否为已读
                                    map.put("icon", object.getString("icon")); //发信人头像
                                    map.put("mid", object.getInt("mid"));//邮件唯一ID
                                    map.put("content", Base64utils.getFromBase64(object.getString("content")));//邮件
                                    map.put("musername", object.getString("musername"));
                                    data.add(map);
                                }
                            }
                            baseListAdapter.notifyDataSetChanged();
                        } else {//失败
                            isNoMore = true;
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    MyProgressBarDialogTools.hide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mymessage);
        context = this;
        findId();
        setText();
        onclick();
        setmsgdb();
        Str_username = getIntent().getStringExtra("Str_username");
        message_list.setOnItemClickListener(this);
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String resouce = intent.getStringExtra("resouce");
        data.clear();
        if ("success".equals(resouce)) {
            tohaveSend();
        } else if ("addresser".equals(resouce)) {
            tohaveSend();
        } else {
            FLG = "receiver";
            initData();
            btn_get.setSelected(true);
            btn_send.setSelected(false);
            btn_new.setSelected(false);
        }
    }

    public void findId() {
        super.findId();
        super.setClick();
        setNodata_receiver();
        message_list = (ListView) findViewById(R.id.message_list);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_new = (Button) findViewById(R.id.btn_new);
    }

    //设置无数据---收件箱
    private void setNodata_receiver() {
        super.setActivity_tvnodata("还没有消息呢，去找人聊两句吧");
        super.setActivity_btnnodata("去找人");
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyMessageActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

    }

    //设置无数据---发件箱
    private void setNodata_addresser() {
        super.setActivity_tvnodata("还没有消息呢，快去回复一下小伙伴吧");
        super.setActivity_btnnodata("去回复");
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNodata_receiver();
                data.removeAll(data);
                btn_get.setSelected(true);
                btn_send.setSelected(false);
                btn_new.setSelected(false);
                page = 1;
                FLG = "receiver";
                isNoMore = false;
                initData();
            }
        });

    }

    /**
     * name:导入初始值
     */
    public void initData() {
        if (page == 1) {
            MyProgressBarDialogTools.show(context);
        }
        //判断是否登录
        if (SharedPreferencesTools.getUid(context).equals("")) {
            return;
        } else {
            uid = (SharedPreferencesTools.getUid(context));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("uid", uid);
                    object.put("flg", FLG);
                    object.put("page", page);
                    object.put("act", URLConfig.myMessage);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }

            }
        }).start();

    }


    /**
     * name:设置listview中的值
     */
    public void setmsgdb() {
        btn_get.setSelected(true);
        initData();
    }

    /**
     * 设置写新消息成功后，跳转至已发送1430
     */
    public void tohaveSend() {
        FLG = "addresser";
        btn_send.setSelected(true);
        btn_get.setSelected(false);
        btn_new.setSelected(false);
        page = 1;
        initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_get: // 收件箱
                setNodata_receiver();

                data.removeAll(data);
                btn_get.setSelected(true);
                btn_send.setSelected(false);
                btn_new.setSelected(false);
                page = 1;
                FLG = "receiver";
                isNoMore = false;
                initData();
                break;
            case R.id.btn_send: // 已发送
                setNodata_addresser();

                data.removeAll(data);
                btn_send.setSelected(true);
                btn_get.setSelected(false);
                btn_new.setSelected(false);
                page = 1;
                isNoMore = false;
                FLG = "addresser";
                initData();
                break;
            case R.id.btn_new: // 新建消息
                btn_new.setSelected(true);
                btn_send.setSelected(false);
                btn_get.setSelected(false);
                Intent intent = new Intent(context, BackMessageActivity.class);
                intent.putExtra("Str_username", Str_username);
                startActivity(intent);

                break;
            default:
                break;
        }

    }


    public void onclick() {
        // TODO Auto-generated method stub
        btn_get.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_new.setOnClickListener(this);
    }

    public void setText() {
        super.setActivity_title_name(R.string.my_mymessage);

        baseListAdapter = new BaseListAdapter(message_list, data, R.layout.message_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                if ("addresser".equals(FLG)) {
                    helper.setText(R.id.message_item_pop, ((Map) item).get("musername") + "");
                } else {
                    helper.setText(R.id.message_item_pop, ((Map) item).get("pusername") + "");
                }
                helper.setText(R.id.message_item_date, ((Map) item).get("mdate") + "");
                helper.setText(R.id.message_item_title, ((Map) item).get("title") + "");
                if (((Map) item).get("ifnew").toString().equals("1") && "receiver".equals(FLG)) {
                    helper.setVisibility(R.id.iv_red_dian, View.VISIBLE);
                }
                helper.setImageBitmapGlide(context, R.id.iv_message_item, ((Map) item).get("icon") + "");

            }
        };
        message_list.setAdapter(baseListAdapter);
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list.getChildAt(message_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            initData();
                        }
                    }
                }
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String Str_mid = data.get(position).get("mid").toString();
        final String Str_musername = data.get(position).get("musername").toString();
        final String Str_pusername = data.get(position).get("pusername").toString();
        final String Str_content = data.get(position).get("content").toString();
        final String Str_date = data.get(position).get("mdate").toString();
        final String Str_title = data.get(position).get("title").toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                String result = null;
                String Str_name = SharedPreferencesTools.getUserName(context);
                try {
                    object.put("mid", Str_mid);
                    object.put("act", URLConfig.getMessage);
                    result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    Intent intent = new Intent(context, MyMessageDetails.class);
                    if (FLG.equals("addresser")) {
                        intent.putExtra("Str_musername", Str_musername);    //收信人姓名
                        intent.putExtra("Str_pusername", Str_pusername);         //发信人姓名
                    } else {
                        intent.putExtra("Str_musername", Str_musername);    //收信人姓名
                        intent.putExtra("Str_pusername", Str_pusername);    //发信人姓名
                        // intent.putExtra("Str_pusername", Str_name);    //发信人姓名
                    }
                    intent.putExtra("Str_content", Str_content);
                    intent.putExtra("Str_date", Str_date);
                    intent.putExtra("Str_title", Str_title);
                    intent.putExtra("Str_mid", Str_mid);
                    intent.putExtra("FLG", FLG);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    public void back(View view) {
        backs();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // 处理逻辑
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            backs();
        }
        return true;
    }

    public void backs() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("type", "register");
        startActivity(intent);
        MyMessageActivity.this.finish();
    }

}
