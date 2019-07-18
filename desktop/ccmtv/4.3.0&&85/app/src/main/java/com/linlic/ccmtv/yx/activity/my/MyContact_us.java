package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/30.
 */
public class MyContact_us extends BaseActivity {
    Context context;
    private TextView tv_arcache, tv_arcache2;
    private LinearLayout layout_clear_asset, layout_sync_asset;
    private MyGridView product_problem, technical_issues, customer_service_hotline;//产品方面的问题
    private Button btnContactCP1, btnContactCP2;//产品客服按钮
    private Button btnContactJS1, btnContactJS2;//技术客服按钮
    private LinearLayout llContactTel1, llContactTel2;//电话客服按钮
    private List<Map<String, Object>> product_problem_data = new ArrayList<Map<String, Object>>();
    BaseListAdapter product_problem_Adapter;
    private List<Map<String, Object>> technical_issues_data = new ArrayList<Map<String, Object>>();
    BaseListAdapter technical_issues_Adapter;
    private List<String> customer_service_hotline_data = new ArrayList<>();
    BaseListAdapter customer_service_hotline_Adapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray proArray = jsonObject.getJSONObject("data").getJSONArray("pro");
                            for (int i = 0; i < proArray.length(); i++) {
                                JSONObject proJson = proArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ser", proJson.getString("ser"));
                                map.put("qq", proJson.getString("qq"));
                                product_problem_data.add(map);
                            }
                            product_problem_Adapter.notifyDataSetChanged();
                            JSONArray tecArray = jsonObject.getJSONObject("data").getJSONArray("tec");
                            for (int i = 0; i < tecArray.length(); i++) {
                                JSONObject tecJson = tecArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("ser", tecJson.getString("ser"));
                                map.put("qq", tecJson.getString("qq"));
                                technical_issues_data.add(map);
                            }
                            technical_issues_Adapter.notifyDataSetChanged();
                            JSONArray telArray = jsonObject.getJSONObject("data").getJSONArray("tel");
                            for (int i = 0; i < telArray.length(); i++) {
                                String telJson = telArray.getString(i);
                                customer_service_hotline_data.add(telJson);
                            }
                            customer_service_hotline_Adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
//                    Log.e("111111111",msg.obj+"");
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + msg.obj);
                        intent.setData(data);
                        startActivity(intent);

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
        setContentView(R.layout.activity_contact_us);
        context = this;

        findId();
        setText();
        initViews();
        setmsgdb();
    }

    public void findId() {
        super.findId();

        product_problem = (MyGridView) findViewById(R.id.product_problem);
        customer_service_hotline = (MyGridView) findViewById(R.id.customer_service_hotline);
        technical_issues = (MyGridView) findViewById(R.id.technical_issues);


    }

    public void initViews() {
        product_problem.setSelector(new ColorDrawable(Color.TRANSPARENT));
//取消GridView中Item选中时默认的背景色
        product_problem_Adapter = new BaseListAdapter(product_problem, product_problem_data, R.layout.item_activity_contact_us1) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id._item_button, ((Map) item).get("ser") + "");

            }
        };
        product_problem.setAdapter(product_problem_Adapter);
        // listview点击事件
        product_problem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = product_problem_data.get(position);
                startQQ(map.get("qq").toString());
            }
        });
        technical_issues.setSelector(new ColorDrawable(Color.TRANSPARENT));
        technical_issues_Adapter = new BaseListAdapter(technical_issues, technical_issues_data, R.layout.item_activity_contact_us1) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id._item_button, ((Map) item).get("ser") + "");

            }
        };
        technical_issues.setAdapter(technical_issues_Adapter);
        // listview点击事件
        technical_issues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = technical_issues_data.get(position);
                startQQ(map.get("qq").toString());
            }
        });
        customer_service_hotline.setSelector(new ColorDrawable(Color.TRANSPARENT));
        customer_service_hotline_Adapter = new BaseListAdapter(customer_service_hotline, customer_service_hotline_data, R.layout.item_activity_contact_us2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id._item_button, item.toString());

            }
        };
        customer_service_hotline.setAdapter(customer_service_hotline_Adapter);
        // listview点击事件
        customer_service_hotline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Message message = new Message();
                message.what = 2;
                message.obj = customer_service_hotline_data.get(position);
                handler.sendMessage(message);
            }
        });
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Map<String, Object> map = product_problem_data.get(arg2);
            startQQ(map.get("qq").toString());
        }

    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            Map<String, Object> map = technical_issues_data.get(arg2);
            startQQ(map.get("qq").toString());
        }

    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            callService(customer_service_hotline_data.get(arg2));
        }

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

    private void setText() {
        super.setActivity_title_name("联系我们");
    }

    /*public  void onClick(){
        layout_sync_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:021-51082567-8674"));
                startActivity(intent);
            }
        });

        layout_clear_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//用intent启动拨打电话
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + URLConfig.qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }*/

    private void startQQ(String qqNumber) {
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }


    private void callService(String phoneNumber) {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
        context.startActivity(intent);
    }

    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myClient);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}
