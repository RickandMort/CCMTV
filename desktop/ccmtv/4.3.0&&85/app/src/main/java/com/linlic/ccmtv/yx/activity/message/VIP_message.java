package com.linlic.ccmtv.yx.activity.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
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
 * Created by Administrator on 2018/10/18.
 */

public class VIP_message extends BaseActivity {

    private Context context;

    @Bind(R.id.message_list)
    ListView message_list;
    private String catid1 = "";
    private String catid2 = "";
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
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                if (page == 1) {
                                    count = dataJson.getJSONObject("data").getInt("count");
                                    listData.clear();
                                }

                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", dataJson1.getString("name"));
                                    map.put("catid1", dataJson1.getString("catid1"));
                                    map.put("catname", dataJson1.getString("catname"));
                                    map.put("cat_icon", dataJson1.getString("cat_icon"));
                                    map.put("master_id", dataJson1.getString("master_id"));
                                    map.put("slave_id", dataJson1.getString("slave_id"));
                                    map.put("content", dataJson1.getString("content"));
                                    map.put("create_time", dataJson1.getString("create_time"));
                                    map.put("master_uid", dataJson1.getString("master_uid"));
                                    map.put("master_username", dataJson1.getString("master_username"));
                                    map.put("slave_username", dataJson1.getString("slave_username"));
                                    map.put("is_mutual_attention", dataJson1.getString("is_mutual_attention"));
                                    map.put("position", listData.size());
                                    listData.add(map);
                                }
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
        setContentView(R.layout.vip_message);
        context = this;
        ButterKnife.bind(this);
        catid1 = getIntent().getStringExtra("catid1");
        catid2 = getIntent().getStringExtra("catid2");
        findId();
        setActivity_title_name(getIntent().getStringExtra("name"));
        initView();
        getUrlRulest();
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

        baseListAdapterVideo = new BaseListAdapter(message_list, listData, R.layout.item_vip_message) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;

                helper.setImageBitmap(R.id._item_icon, map.get("cat_icon").toString());
                helper.setText(R.id._item_time, map.get("create_time").toString());
                helper.setText(R.id._item_type, map.get("catname").toString());
                helper.setText(R.id._item_content, map.get("content").toString());


            }
        };
        message_list.setAdapter(baseListAdapterVideo);
        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = message_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = message_list.getChildAt(message_list.getChildCount() - 1);
                    LogUtil.e("lastVisibleItemView", lastVisibleItemView + "");
                    LogUtil.e(" lastVisibleItemView.getBottom()", lastVisibleItemView.getBottom() + "");
                    LogUtil.e("message_list2.getHeight()", message_list.getHeight() + "");
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == message_list.getHeight()) {
                        if (count > (page * limit)) {
                            page += 1;
                            getUrlRulest();
                        }
                    }
                }
            }
        });


    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.systemInfoDetail);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("catid1", catid1);
                    obj.put("catid2", catid2);
                    obj.put("page", page);
                    obj.put("limit", limit);
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

}
