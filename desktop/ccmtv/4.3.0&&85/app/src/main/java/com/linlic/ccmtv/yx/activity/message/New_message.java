package com.linlic.ccmtv.yx.activity.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
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

import static com.linlic.ccmtv.yx.config.URLConfig.CompletePrivateLetter;

/**
 * Created by Administrator on 2018/10/22.
 */

public class New_message extends BaseActivity {


    public static final String FROM_KZBF_KEY = "FROM_KZBF_KEY";//标识来自空中拜访
    private Context context;

    @Bind(R.id.message_list)
    ListView message_list;
    @Bind(R.id.editText1)
    EditText editText1;
    //    @Bind(R.id.new_message_nodata)
//    NodataEmptyLayout new_message_nodata;
    private String to_uid = "";
    private String to_username = "";
    private int page = 1;
    private int limit = 5;
    private int count = 0;

    private BaseListAdapter baseListAdapterVideo;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

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
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                if (page == 1) {
                                    listData.clear();
                                }

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("uid", dataJson1.getString("uid"));
                                    map.put("username", dataJson1.getString("username"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("icon", dataJson1.getString("icon"));
                                    map.put("is_left_position", dataJson1.getString("is_left_position"));
                                    map.put("position", listData.size());
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                                message_list.setSelection(listData.size() - 1);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
//                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                editText1.setText("");
                                privateLetter(!TextUtils.isEmpty(getIntent().getStringExtra(FROM_KZBF_KEY)));
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
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 0) {
                            JSONArray dataJson = jsonObject.getJSONArray("data");
                            if (page == 1) {
                                listData.clear();
                            }
                            for (int i = 0; i < dataJson.length(); i++) {
                                JSONObject dataJson1 = dataJson.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("content", dataJson1.getString("content"));
                                map.put("create_time", dataJson1.getString("addtime"));
                                map.put("icon", dataJson1.getString("img"));
                                map.put("is_left_position", dataJson1.getString("direction").equals("right") ? "1" : "0");
                                map.put("position", listData.size());
                                listData.add(map);
                            }
                            baseListAdapterVideo.notifyDataSetChanged();
                            message_list.setSelection(listData.size() - 1);
                        }
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
//                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;

                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (jsonObject.getInt("code") == 0) {
                            editText1.setText("");
                            KzbfPrivateLetter();
                        } else {
                            MyProgressBarDialogTools.hide();
                        }
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
//                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

//    private void setResultStatus(boolean status, int code) {
//        if (true) {
////        if (status) {
//            message_list.setVisibility(View.VISIBLE);
//            new_message_nodata.setVisibility(View.GONE);
//        } else {
//            if (HttpClientUtils.isNetConnectError(context, code)) {
//                new_message_nodata.setNetErrorIcon();
//            } else {
//                new_message_nodata.setLastEmptyIcon();
//            }
//            message_list.setVisibility(View.GONE);
//            new_message_nodata.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_message);
        context = this;
        ButterKnife.bind(this);
        to_username = getIntent().getStringExtra("username");
        to_uid = getIntent().getStringExtra("uid");
        findId();
        super.setActivity_title_name(to_username);
        initView();


        privateLetter(!TextUtils.isEmpty(getIntent().getStringExtra(FROM_KZBF_KEY)));

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

    public void privateLetter(boolean isFromKzbf) {
        if (isFromKzbf) {
            KzbfPrivateLetter();
        } else {
            CompletePrivateLetter();
        }
    }

    public void initView() {
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(New_message.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if (TextUtils.isEmpty(getIntent().getStringExtra(FROM_KZBF_KEY)))
                        sendMyLetter();
                    else sendKzbfMessage();
                }
                return false;
            }
        });


        baseListAdapterVideo = new BaseListAdapter(message_list, listData, R.layout.item_new_message) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setText(R.id._item_time, map.get("create_time").toString());
                if (map.get("is_left_position").toString().trim().equals("1")) {//右边
                    helper.setViewVisibility(R.id._item_right, View.VISIBLE);
                    helper.setViewVisibility(R.id._item_left, View.GONE);
                    helper.setImageBitmap(R.id._item_icon1, map.get("icon").toString());
                    helper.setText(R.id._item_content1, map.get("content").toString());
                } else {
                    helper.setViewVisibility(R.id._item_left, View.VISIBLE);
                    helper.setViewVisibility(R.id._item_right, View.GONE);
                    helper.setImageBitmap(R.id._item_icon, map.get("icon").toString());
                    helper.setText(R.id._item_content, map.get("content").toString());
                }

            }
        };
        message_list.setAdapter(baseListAdapterVideo);


    }


    public void sendMyLetter() {
        if (editText1.getText().toString().trim().length() < 1) {
            Toast.makeText(context, "内容不能为空，请重新输入！", Toast.LENGTH_SHORT).show();
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.sendMyLetter);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("to_uid", to_uid);
                    obj.put("to_username", to_username);
                    obj.put("content", editText1.getText().toString());
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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

    public void CompletePrivateLetter() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", CompletePrivateLetter);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("master_uid", to_uid);
                    obj.put("slave_uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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


    private void sendKzbfMessage() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.postMessage);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("addressee_uid", to_uid);
                    obj.put("title", "");
                    obj.put("content", editText1.getText().toString());

//                        Log.e("看看发消息上行数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                        Log.e("看看发消息数据", result);

                    Message message = new Message();
                    message.what = 4;
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

    public void KzbfPrivateLetter() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userChatList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("f_uid", to_uid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.Skyvisit, obj.toString());
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
}
