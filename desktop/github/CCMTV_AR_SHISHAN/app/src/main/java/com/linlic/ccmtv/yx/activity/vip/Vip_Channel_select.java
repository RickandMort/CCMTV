package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.Videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 小科室选择页
 * Created by Administrator on 2018/6/4.
 */

public class Vip_Channel_select extends BaseActivity {

    private Context context;

    @Bind(R.id.videos)
    MyGridView videos;//视频集合
    @Bind(R.id.videos2)
    MyGridView videos2;//视频集合
    @Bind(R.id.videos3)
    MyGridView videos3;//视频集合
    @Bind(R.id.videos4)
    MyGridView videos4;//视频集合
    private Videos_GridAdapter videos_gridAdapter;
    private Videos_GridAdapter videos_gridAdapter2;
    private Videos_GridAdapter videos_gridAdapter3;
    private Videos_GridAdapter videos_gridAdapter4;
    public JSONObject ks_data = new JSONObject();
    public JSONObject ks_data2 = new JSONObject();
    public JSONObject ks_data3 = new JSONObject();
    public JSONObject ks_data4 = new JSONObject();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray data = jsonObject.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                switch (i) {
                                    case 0:
                                        ks_data = data.getJSONObject(i);
                                        break;
                                    case 1:
                                        ks_data2 = data.getJSONObject(i);
                                        break;
                                    case 2:
                                        ks_data3 = data.getJSONObject(i);
                                        break;
                                    case 3:
                                        ks_data4 = data.getJSONObject(i);
                                        break;
                                    default:
                                        break;
                                }

                            }
                            initVideos();
                        } else {
                            Toast.makeText(Vip_Channel_select.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            MyProgressBarDialogTools.hide();
                            finish();
                        } else {
                            Toast.makeText(Vip_Channel_select.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

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
                    MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.vip_channel_select);
        context = this;
        ButterKnife.bind(this);
        findId();

        getUrlRulest();
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.selKeshi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("VIP-小科室数据", result);

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


    public void initVideos() {

        try {
            videos_gridAdapter = new Videos_GridAdapter(context, ks_data.getJSONArray("min_channels"));
            videos.setAdapter(videos_gridAdapter);
            videos.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
            videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (!ks_data.getJSONArray("min_channels").getJSONObject(position).has("type") || !ks_data.getJSONArray("min_channels").getJSONObject(position).getString("type").equals("1")) {

                            ks_data.getJSONArray("min_channels").getJSONObject(position).put("type", "1");
                        } else {
                            ks_data.getJSONArray("min_channels").getJSONObject(position).remove("type");
                        }

                        videos_gridAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            videos_gridAdapter2 = new Videos_GridAdapter(context, ks_data2.getJSONArray("min_channels"));
            videos2.setAdapter(videos_gridAdapter2);
            videos2.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
            videos2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (!ks_data2.getJSONArray("min_channels").getJSONObject(position).has("type") || !ks_data2.getJSONArray("min_channels").getJSONObject(position).getString("type").equals("1")) {

                            ks_data2.getJSONArray("min_channels").getJSONObject(position).put("type", "1");
                        } else {
                            ks_data2.getJSONArray("min_channels").getJSONObject(position).remove("type");
                        }

                        videos_gridAdapter2.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            videos_gridAdapter3 = new Videos_GridAdapter(context, ks_data3.getJSONArray("min_channels"));
            videos3.setAdapter(videos_gridAdapter3);
            videos3.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
            videos3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (!ks_data3.getJSONArray("min_channels").getJSONObject(position).has("type") || !ks_data3.getJSONArray("min_channels").getJSONObject(position).getString("type").equals("1")) {

                            ks_data3.getJSONArray("min_channels").getJSONObject(position).put("type", "1");
                        } else {
                            ks_data3.getJSONArray("min_channels").getJSONObject(position).remove("type");
                        }

                        videos_gridAdapter3.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            videos_gridAdapter4 = new Videos_GridAdapter(context, ks_data4.getJSONArray("min_channels"));
            videos4.setAdapter(videos_gridAdapter4);
            videos4.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
            videos4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (!ks_data4.getJSONArray("min_channels").getJSONObject(position).has("type") || !ks_data4.getJSONArray("min_channels").getJSONObject(position).getString("type").equals("1")) {

                            ks_data4.getJSONArray("min_channels").getJSONObject(position).put("type", "1");
                        } else {
                            ks_data4.getJSONArray("min_channels").getJSONObject(position).remove("type");
                        }

                        videos_gridAdapter4.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close_upload(View view) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.doKeshi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    getselect(obj, ks_data);
                    getselect(obj, ks_data2);
                    getselect(obj, ks_data3);
                    getselect(obj, ks_data4);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("VIP-提交小科室数据", result);

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

    public void getselect(JSONObject obj, JSONObject jsonObject) {
        String str = "";
        try {
            switch (jsonObject.getString("kname")) {
                case "内科":
                    for (int i = 0; i < jsonObject.getJSONArray("min_channels").length(); i++) {
                        if (jsonObject.getJSONArray("min_channels").getJSONObject(i).has("type") && jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("type").equals("1")) {
                            if (str.length() > 0) {
                                str += "," + jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            } else {
                                str = jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            }
                        }
                    }
                    obj.put("neikids", str);
                    break;
                case "外科":
                    for (int i = 0; i < jsonObject.getJSONArray("min_channels").length(); i++) {
                        if (jsonObject.getJSONArray("min_channels").getJSONObject(i).has("type") && jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("type").equals("1")) {
                            if (str.length() > 0) {
                                str += "," + jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            } else {
                                str = jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            }
                        }
                    }
                    obj.put("waikids", str);
                    break;
                case "妇儿科":
                    for (int i = 0; i < jsonObject.getJSONArray("min_channels").length(); i++) {
                        if (jsonObject.getJSONArray("min_channels").getJSONObject(i).has("type") && jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("type").equals("1")) {
                            if (str.length() > 0) {
                                str += "," + jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            } else {
                                str = jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            }
                        }
                    }
                    obj.put("fukids", str);
                    break;
                case "其他":
                    for (int i = 0; i < jsonObject.getJSONArray("min_channels").length(); i++) {
                        if (jsonObject.getJSONArray("min_channels").getJSONObject(i).has("type") && jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("type").equals("1")) {
                            if (str.length() > 0) {
                                str += "," + jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            } else {
                                str = jsonObject.getJSONArray("min_channels").getJSONObject(i).getString("kid");
                            }
                        }
                    }
                    obj.put("qikids", str);
                    break;
                default:

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
