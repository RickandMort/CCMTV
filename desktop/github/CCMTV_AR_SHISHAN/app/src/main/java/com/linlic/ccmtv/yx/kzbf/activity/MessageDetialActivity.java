package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

/**
 * 消息详情
 */
public class MessageDetialActivity extends BaseActivity {
    Context context;
    private String id;// 消息id
    private String type;//0 收件箱 1 发件箱
    private String helper;// 发件人/收件人姓名
    private String addressee_uid;//用户id
    private TextView tv_message_inbox, tv_message_theme, tv_message_helper, tv_message_addtime, tv_message_content;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            JSONObject object = jsonObject.getJSONObject("data");
                            tv_message_theme.setText(object.getString("title"));
                            tv_message_addtime.setText(object.getString("addtime"));
                            tv_message_content.setText(object.getString("content"));
                            if (type.equals("1")) {
                                addressee_uid = object.getString("addressee_uid");
                            } else {
                                addressee_uid = object.getString("addressor_uid");
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            finish();
                            Intent intent = new Intent(context,MessageActivity.class);
                            intent.putExtra("type",type);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
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
        setContentView(R.layout.activity_message_detial);
        context = this;

        initView();
        initData();
        setValue();
    }

    private void initView() {
        tv_message_inbox = (TextView) findViewById(R.id.tv_message_inbox);
        tv_message_theme = (TextView) findViewById(R.id.tv_message_theme);
        tv_message_helper = (TextView) findViewById(R.id.tv_message_helper);
        tv_message_addtime = (TextView) findViewById(R.id.tv_message_addtime);
        tv_message_content = (TextView) findViewById(R.id.tv_message_content);
    }

    private void initData() {
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        helper = getIntent().getStringExtra("helper");

        if (type.equals("0")) {
            tv_message_inbox.setText("发件人：");
        } else {
            tv_message_inbox.setText("收件人：");
        }

        tv_message_helper.setText(helper);
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.messageInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

//                    Log.e("看看消息详细上行数据", obj.toString());
                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看消息详细数据", result);

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
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            Intent intent = new Intent(context,MessageActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //删除按钮
    public void deleteMessage(View v) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.messageDel);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
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

    //回复按钮
    public void replyMessage(View v) {
        Intent intent = new Intent(context,MessageActivity.class);
        intent.putExtra("assistant","1");
        intent.putExtra("addressee_uid",addressee_uid);
        intent.putExtra("helper", helper);
        startActivity(intent);
        finish();
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
