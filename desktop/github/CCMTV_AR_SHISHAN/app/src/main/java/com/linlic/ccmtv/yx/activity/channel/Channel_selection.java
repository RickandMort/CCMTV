package com.linlic.ccmtv.yx.activity.channel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Max_Channel;
import com.linlic.ccmtv.yx.activity.entity.Min_Channel;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.adapter.Min_ChannelGridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/5/8.
 */

public class Channel_selection extends BaseActivity {
    private Context context;
    @Bind(R.id.max_channel)
    ListView max_channel;
    @Bind(R.id.min_channel)
    GridView min_channel;
    @Bind(R.id.rl_selection_nodata)
    NodataEmptyLayout rl_selection_nodata;
    private List<Max_Channel> data = new ArrayList<>();
    BaseListAdapter baseListAdapter;
    Min_ChannelGridAdapter min_channelGridAdapter;
    public int position1 = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        boolean status = jsonObject.getInt("status") == 1;
                        if (status) { // 成功
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                if (getIntent().getStringExtra("kid").equals(customJson.getString("kid"))) {
                                    position1 = i;
                                }
                                Max_Channel max_channel = new Max_Channel(customJson.getString("kid"), customJson.getString("kname"), customJson.getString("id"), false, null);
                                List<Min_Channel> min_channels = new ArrayList<>();
                                JSONArray minJsons = customJson.getJSONArray("min_channels");
                                for (int j = 0; j < minJsons.length(); j++) {
                                    JSONObject minJson = minJsons.getJSONObject(j);
                                    min_channels.add(new Min_Channel(minJson.getString("kid"), minJson.getString("kname"), minJson.getString("icon"), minJson.getString("url"), minJson.getString("isurl")));
                                }
                                max_channel.setMin_channels(min_channels);
                                data.add(max_channel);
                            }
                            baseListAdapter.notifyDataSetChanged();
                            min_channelGridAdapter.notifyDataSetChanged();
                            data.get(position1).setSelect(true);
                            initmin_channel(data.get(position1).getMin_channels());
                        } else {
                            Toast.makeText(Channel_selection.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }

        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            min_channel.setVisibility(View.VISIBLE);
            rl_selection_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_selection_nodata.setNetErrorIcon();
            } else {
                rl_selection_nodata.setLastEmptyIcon();
            }
            min_channel.setVisibility(View.GONE);
            rl_selection_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.channel_selection);
        context = this;
        ButterKnife.bind(this);
        findId();
        //测试调用
//        inittext();
        initdata();
        setValue2();

    }


    public void initdata() {
      /*
        myGridAdapter = new MyGridAdapter(context,img_text,imgs);
        gridview.setAdapter(myGridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                TextView textView = (TextView) view.findViewById(R.id.tv_item);
                Intent intent = new Intent(Answering_card.this, Check_the_answer_sheet.class);
                intent.putExtra("my_exams_id",my_exams_id);
                intent.putExtra("my_exams_eid",my_exams_eid);
                intent.putExtra("position",textView.getText().toString());

                if (textView.getText().length() > 0) {
                    startActivity(intent);
                }
            }
        });*/
        baseListAdapter = new BaseListAdapter(max_channel, data, R.layout.max_channel_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Max_Channel max_channel = (Max_Channel) item;
                helper.setText(R.id.max_channel_title, max_channel.getTitle());
                helper.setText(R.id.max_channel_id, max_channel.getId());
                if (data.get(position1).getId().equals(max_channel.getId())) {
                    helper.setVisibility(R.id.line_01, View.INVISIBLE);
                    helper.setVisibility(R.id.line_02, View.VISIBLE);
                    helper.setBackground_color(R.id.max_channel_bg, Color.parseColor("#ffffff"));
                    helper.setTextColor2(R.id.max_channel_title, Color.parseColor("#54A5F1"));
                } else {
                    helper.setVisibility(R.id.line_01, View.VISIBLE);
                    helper.setVisibility(R.id.line_02, View.INVISIBLE);
                    helper.setBackground_color(R.id.max_channel_bg, Color.parseColor("#FAFAFA"));
                    helper.setTextColor2(R.id.max_channel_title, Color.parseColor("#333333"));
                }

            }
        };
        max_channel.setAdapter(baseListAdapter);
        // listview点击事件
        max_channel.setOnItemClickListener(new casesharing_listListener());
        min_channelGridAdapter = new Min_ChannelGridAdapter(context, new ArrayList<Min_Channel>());
        min_channel.setAdapter(min_channelGridAdapter);
        min_channel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                TextView textView = (TextView) view.findViewById(R.id.min_channel_id);
                TextView min_channel_title = (TextView) view.findViewById(R.id.min_channel_title);
                if( min_channelGridAdapter.getDatas().get(arg2).getIsurl().equals("0") ){
                    Intent intent = new Intent(context, Channel_Main.class);
                    if (textView.getText().length() > 0) {
                        intent.putExtra("min_channel_id", textView.getText());
                        intent.putExtra("min_channel_title", min_channel_title.getText());
                        startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(context, ActivityWebActivity.class);
                    intent.putExtra("title", min_channel_title.getText());
                    intent.putExtra("aid", min_channelGridAdapter.getDatas().get(arg2).getUrl());
                    startActivity(intent);
                }

            }
        });

    }

    public void initmin_channel(List<Min_Channel> min_channels) {
        min_channelGridAdapter.setMin_channels(min_channels);
        min_channelGridAdapter.notifyDataSetChanged();
    }

    /**
     * name: 点击查看单个详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            position1 = arg2;
            baseListAdapter.notifyDataSetChanged();
            min_channelGridAdapter.setMin_channels(data.get(arg2).getMin_channels());
            min_channelGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void findId() {
        super.findId();
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.pdkeshi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.PDCCMTVAPP, obj.toString());
//                    Log.e("看看科室2", result);

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/channel-122.html";
        super.onPause();
    }
}
