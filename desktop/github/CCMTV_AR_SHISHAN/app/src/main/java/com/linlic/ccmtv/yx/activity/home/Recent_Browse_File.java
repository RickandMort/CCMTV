package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name:最近浏览
 * author:Tom
 * 2016-3-2下午7:01:48
 */
public class Recent_Browse_File extends BaseActivity {

    private ListView recent_browse_file_list;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;
    Context context;
    //用户统计
    private String type;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONArray dataArray = result.getJSONArray("data");
                            if (dataArray.length() == 0) {
                                showNoData();
                            } else {
                                hideNoData();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("Recent_Browse_File_item_title", jsonObject.getString("title"));
                                    map.put("Recent_Browse_File_id", jsonObject.getString("aid"));
                                    map.put("Recent_Browse_File_on_demand", "播放数：" + jsonObject.getString("hits"));
                                    map.put("Recent_Browse_File_times", jsonObject.getString("posttime"));
                                    map.put("Recent_Browse_File_item_img", jsonObject.getString("picurl"));
                                    map.put("money", jsonObject.getString("flag") + "-" + jsonObject.getString("videopaymoney"));
                                    data.add(map);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.recent_browse_file);
        context = this;

        type = getIntent().getStringExtra("type");

        findById();
        setText();
        setmsgdb();
    }

    /**
     * name:查询XML控件 author:Tom 2016-2-2上午11:05:23
     */
    public void findById() {
        super.findId();
        super.setClick();
        super.btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recent_Browse_File.this, CustomActivity.class);
                intent.putExtra("type", "my");
                startActivity(intent);
            }
        });
        recent_browse_file_list = (ListView) findViewById(R.id.recent_browse_file_list);
    }

    /**
     * name:设置控件的文本值
     * author:Tom
     * 2016-2-2下午2:14:08
     */
    public void setText() {
        super.setActivity_title_name(R.string.activity_title_name_recent);
        super.setActivity_btnnodata(R.string.btnnodata_tosee);
        super.setActivity_tvnodata(R.string.tvnodata_recent);
        // listview点击事件
        recent_browse_file_list.setOnItemClickListener(new casesharing_listListener());
        adapter = new SimpleAdapter(context, data,
                R.layout.recent_browse_file_item, new String[]{
                "Recent_Browse_File_item_title",
                "Recent_Browse_File_id",
                "Recent_Browse_File_on_demand",
                "Recent_Browse_File_times",
                "Recent_Browse_File_item_img",
                "money"}, new int[]{
                R.id.recent_item_title,
                R.id.recent_id,
                R.id.recent_on_demand,
                R.id.recent_times,
                R.id.recent_item_img,
                R.id.recent_top_item_img});
        adapter.setViewBinder(new ViewBinder() {

            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView) {
                    if (view.getId() == R.id.recent_top_item_img) {
                        int money = Integer.parseInt(data.toString().split("-")[0]);
                        String videopaymoney = data.toString().split("-")[1];
                        //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                        if (!videopaymoney.equals("0")) {
                            //收费
                            ((ImageView) view).setImageResource(R.mipmap.charge);
                            view.setVisibility(View.VISIBLE);
                        } else {
                            if (money == 3) {
                                //会员
                                ((ImageView) view).setImageResource(R.mipmap.vip_img);
                                view.setVisibility(View.VISIBLE);
                            } else {
                                view.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        ImageView iv = (ImageView) view;
                        loadImg(iv, data.toString());
                    }
                    return true;
                }
                return false;
            }
        });
        recent_browse_file_list.setAdapter(adapter);
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recentlyWatchVideo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("历史记录",result);
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

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.recent_id);
            String id = textView.getText().toString();
            getVideoRulest(id);
        }
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Glide.with(context)
                .load(FirstLetter.getSpells(path))
                .apply(options)
                .into(img);
    }


    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

}
