package com.linlic.ccmtv.yx.kzbf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.ProtocolActivity;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

public class SkyProtocolActivity extends BaseActivity implements View.OnClickListener{
    Context context;
    private TextView protocol_leave, protocol_yes, protocol_content;
    private String content = "我们为您提供更新、更准确、更具学术价值的临床药品信息，点击确定同意“专区协议”，即可了解更多。";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context,MedicineMessageActivity.class));
                        } else {
                            startActivity(new Intent(context,MedicineMessageActivity.class));
                        }
                        finish();
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
        setContentView(R.layout.activity_sky_protocol);
        context = this;

        initView();
        initData();
    }

    private void initView() {
        protocol_leave = (TextView) findViewById(R.id.protocol_leave);
        protocol_yes = (TextView) findViewById(R.id.protocol_yes);
        protocol_content = (TextView) findViewById(R.id.protocol_content);

        protocol_leave.setOnClickListener(this);
        protocol_yes.setOnClickListener(this);
    }

    private void initData() {
        //设置富文本
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(content);
        //设置字体颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);
        spannable.setSpan(foregroundColorSpan, 0, spannable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //设置下划线
        UnderlineSpan sizeSpan = new UnderlineSpan();
        spannable.setSpan(sizeSpan, 34, 38, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        //设置点击事件
        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View widget) {
                                  startActivity(new Intent(context, ProtocolActivity.class));
                              }
                          },
                34, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        protocol_content.setMovementMethod(LinkMovementMethod.getInstance());
        protocol_content.setText(spannable);
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

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.subscribe);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看签署协议数据", result);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.protocol_leave:
                finish();
                break;
            case R.id.protocol_yes:
                setValue();
                break;
            default:
                break;
        }
    }

    @Override
    public void back(View view) {
        finish();
    }
}
