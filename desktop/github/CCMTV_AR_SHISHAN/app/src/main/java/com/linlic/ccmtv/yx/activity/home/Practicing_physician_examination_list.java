package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/6.
 */
public class Practicing_physician_examination_list extends BaseActivity {
    Context context;
    BaseListAdapter baseListAdapter;
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    MyGridView mListView;
    JSONObject result;
    JSONArray dataArray;
    private boolean isNoMore = false;
    private TextView activity_title_name;
    private ScrollView scroll;
    private LinearLayout practicing_LinearLayout;
    private Practicing_physician_examination_list mConxt;
    private String practicing_itme_fuid, practicing_itme_img;
    private int page = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        result = new JSONObject(msg.obj + "");
                        boolean status = result.getInt("status") == 1;
                        setResultStatus(status);
                        if (status) { // 成功
                            dataArray = result.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject item = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("aid", item.getString("aid"));
                                map.put("mid", item.getString("mid"));
                                map.put("title", item.getString("title"));
                                map.put("hits", item.getString("hits"));
                                map.put("posttime", item.getString("posttime"));
                                map.put("picurl", item.getString("picurl"));
                                map.put("keywords", item.getString("keywords"));
                                map.put("money", item.getString("money"));
                                map.put("flag", item.getString("flag"));
                                map.put("videopaymoney", item.getString("videopaymoney"));
                                map.put("raid", item.getString("raid"));

                                data.add(map);
                            }
                        } else {
                            isNoMore = true;// 失败
                            Toast.makeText(Practicing_physician_examination_list.this,
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
                        // 将布局加入到当前布局中
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private RelativeLayout rl_nodata;
    private LinearLayout ll_data;

    private void setResultStatus(boolean status) {
        if (status) {
            ll_data.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);
        } else {
            if (page != 1) return;
            ll_data.setVisibility(View.GONE);
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_congress_page);
        context = this;
        mConxt = this;
        findId();
        setText();
        getListView();
    }

    public void findId() {
        mListView = (MyGridView) findViewById(R.id.list1);
        scroll = (ScrollView) findViewById(R.id.scroll);

        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        ll_data = (LinearLayout) findViewById(R.id.ll_medical_congress_data);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_medical_congress_nodata1);

    }

    public void loadImg(ImageView img, String path) {
        ImageLoader.getInstance().displayImage(FirstLetter.getSpells(path), img);
    }

    public void setText() {
        Intent intent = getIntent();
        practicing_itme_fuid = intent.getStringExtra("practicing_itme__fuid");
        practicing_itme_img = intent.getStringExtra("practicing_itme__img");
        activity_title_name.setText(intent.getStringExtra("practicing_itme_tilte"));
        baseListAdapter = new BaseListAdapter(mListView, data, R.layout.medical_congress_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                // 设置IMG URL 视频ID
                helper.setImageBitmapGlide(mConxt, R.id.recommendForMe_img1, ((Map) item).get("picurl").toString());
                helper.setText(R.id.recommendForMe_text1, ((Map) item).get("title").toString());
                helper.setTag(R.id.recommendForMe_text1, ((Map) item).get("aid").toString());
                helper.setVideoOnClick4(R.id.recommendForMe_img1, ((Map) item).get("aid").toString(), Practicing_physician_examination_list.this);
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.recommendForMe_top_img1, R.mipmap.charge);
                    helper.setVisibility(R.id.recommendForMe_top_img1, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.recommendForMe_top_img1, View.GONE);
                    if (((Map) item).get("flag").toString().equals("3")) {
                        //会员
                        helper.setImage(R.id.recommendForMe_top_img1, R.mipmap.vip_img);
                        helper.setVisibility(R.id.recommendForMe_top_img1, View.VISIBLE);
                    }
                }

            }
        };
        mListView.setAdapter(baseListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(context, VideoFive.class);
                intent.putExtra("aid", data.get(i).get("aid").toString());
                context.startActivity(intent);
            }
        });
        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:{
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        //当文本的measureheight 等于scroll滚动的长度+scroll的height
                        if(scroll.getChildAt(0).getMeasuredHeight()<=scroll.getScrollY()+scroll.getHeight()){
                            if (!isNoMore) {
                                page += 1;
                                getListView();
                            }
                        }else{

                        }
                        break;
                    }
                }
                return false;
            }
        });

    }

    public void getListView() {
        if (page == 1) {
            data.removeAll(data);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.examDocQualificationDetails);
                    obj.put("fid", practicing_itme_fuid);
                    obj.put("page", page);
                    String result = HttpClientUtils.sendPost(Practicing_physician_examination_list.this,
                            URLConfig.CCMTVAPP, obj.toString());
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
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
