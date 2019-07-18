package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.activity.entity.Live;
import com.linlic.ccmtv.yx.activity.home.adapter.UserLiveAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name：用户直播
 * author：Larry
 * data：2017/2/14 14:51
 */

public class UserLiveFragment extends BaseLazyFragment {
    private static final String TAG = "UserLiveFragment";
    private boolean isInit = false;
    private MyListView lv_userlive;
    private LinearLayout btn_morelive, btn_morehistorylive, layout_nodata, layout_jingcaihuigu;
    private List<Live> liveLists;
    private List<Live> liveList;
    private UserLiveAdapter adapter;
    private ImageView iv_top;
    //为我推荐
    private List<ImageView> live_history_img = new ArrayList<ImageView>();
    private List<TextView> tv_live_history = new ArrayList<TextView>();
    private ImageView live_history_1, live_history_2, live_history_3, live_history_4, live_history_5, live_history_6;
    private TextView tv_live_history_1, tv_live_history_2, tv_live_history_3, tv_live_history_4, tv_live_history_5, tv_live_history_6;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            liveList.clear();
                            liveLists.clear();
                            JSONArray jsonArray = result.getJSONArray("data");
                            if (jsonArray.length() == 1) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                liveLists = new Gson().fromJson(jsonObject.getJSONArray("listdata").toString(),
                                        new TypeToken<List<Live>>() {
                                        }.getType());
                                if (jsonObject.getInt("style") == 1) {
                                    if (liveLists.size() == 0) {
                                        layout_nodata.setVisibility(View.VISIBLE);
                                        btn_morelive.setVisibility(View.GONE);
                                    } else {
                                        layout_nodata.setVisibility(View.GONE);
                                        if (liveLists.size() >= 4) {
                                            for (int i = 0; i < 4; i++) {
                                                liveList.add(liveLists.get(i));
                                            }
                                            adapter = new UserLiveAdapter(getActivity(), liveList);
                                            lv_userlive.setAdapter(adapter);
                                        } else {
                                            adapter = new UserLiveAdapter(getActivity(), liveLists);
                                            lv_userlive.setAdapter(adapter);
                                            btn_morelive.setVisibility(View.GONE);
                                        }
                                    }
                                    layout_jingcaihuigu.setVisibility(View.GONE);
                                } else if (jsonObject.getInt("style") == 2) {
                                    if (liveLists.size() == 0) {
                                        layout_jingcaihuigu.setVisibility(View.GONE);
                                        layout_nodata.setVisibility(View.VISIBLE);
                                        btn_morelive.setVisibility(View.GONE);
                                    } else {
                                        layout_jingcaihuigu.setVisibility(View.VISIBLE);
                                        layout_nodata.setVisibility(View.VISIBLE);
                                        btn_morelive.setVisibility(View.GONE);
                                        setImgandText(jsonObject.getJSONArray("listdata"), live_history_img, tv_live_history);
                                    }

                                }
                            } else if (jsonArray.length() >= 2) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONObject jsonObject1 = jsonArray.getJSONObject(1);
                                liveLists = new Gson().fromJson(jsonObject.getJSONArray("listdata").toString(),
                                        new TypeToken<List<Live>>() {
                                        }.getType());
                                if (jsonObject.getInt("style") == 1) {
                                    if (liveLists.size() == 0) {
                                        layout_nodata.setVisibility(View.VISIBLE);
                                    } else {
                                        layout_nodata.setVisibility(View.GONE);
                                        if (liveLists.size() >= 4) {
                                            for (int i = 0; i < 4; i++) {
                                                liveList.add(liveLists.get(i));
                                            }
                                            adapter = new UserLiveAdapter(getActivity(), liveList);
                                            lv_userlive.setAdapter(adapter);
                                        } else {
                                            adapter = new UserLiveAdapter(getActivity(), liveLists);
                                            lv_userlive.setAdapter(adapter);
                                            btn_morelive.setVisibility(View.GONE);
                                        }

                                    }
                                }
                                if (jsonObject1.getInt("style") == 2) {
                                    if (jsonObject1.getJSONArray("listdata").length() > 0) {
                                        layout_jingcaihuigu.setVisibility(View.VISIBLE);
                                        setImgandText(jsonObject1.getJSONArray("listdata"), live_history_img, tv_live_history);
                                    } else {
                                        layout_jingcaihuigu.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                btn_morelive.setVisibility(View.GONE);
                                layout_nodata.setVisibility(View.VISIBLE);
                                layout_jingcaihuigu.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // LayoutInflater:http://blog.sina.com.cn/s/blog_629b701e0100rg4d.html
        View view = inflater.inflate(R.layout.layout_userlive, container, false);
        liveList = new ArrayList<>();
        liveLists = new ArrayList<>();
        lv_userlive = (MyListView) view.findViewById(R.id.lv_userlive);
        btn_morelive = (LinearLayout) view.findViewById(R.id.btn_morelive);
        layout_nodata = (LinearLayout) view.findViewById(R.id.layout_nodata);
        layout_jingcaihuigu = (LinearLayout) view.findViewById(R.id.layout_jingcaihuigu);
        live_history_1 = (ImageView) view.findViewById(R.id.live_history_1);
        live_history_2 = (ImageView) view.findViewById(R.id.live_history_2);
        live_history_3 = (ImageView) view.findViewById(R.id.live_history_3);
        live_history_4 = (ImageView) view.findViewById(R.id.live_history_4);
        live_history_5 = (ImageView) view.findViewById(R.id.live_history_5);
        live_history_6 = (ImageView) view.findViewById(R.id.live_history_6);
        tv_live_history_1 = (TextView) view.findViewById(R.id.tv_live_history_1);
        tv_live_history_2 = (TextView) view.findViewById(R.id.tv_live_history_2);
        tv_live_history_3 = (TextView) view.findViewById(R.id.tv_live_history_3);
        tv_live_history_4 = (TextView) view.findViewById(R.id.tv_live_history_4);
        tv_live_history_5 = (TextView) view.findViewById(R.id.tv_live_history_5);
        tv_live_history_6 = (TextView) view.findViewById(R.id.tv_live_history_6);
        btn_morehistorylive = (LinearLayout) view.findViewById(R.id.btn_morehistorylive);
        iv_top = (ImageView) view.findViewById(R.id.iv_top);
        isInit = true;
        iv_top.requestFocus();
        LazyLoad();    //让第一个页面加载数据
        addArrary();
        onClick();
        return view;
    }

    /**
     * name: 将控件放入到list中
     * author:Tom
     * 2016-3-2下午2:15:17
     */
    public void addArrary() {
        //热门推荐
        live_history_img.add(live_history_1);
        live_history_img.add(live_history_2);
        live_history_img.add(live_history_3);
        live_history_img.add(live_history_4);
        live_history_img.add(live_history_5);
        live_history_img.add(live_history_6);
        tv_live_history.add(tv_live_history_1);
        tv_live_history.add(tv_live_history_2);
        tv_live_history.add(tv_live_history_3);
        tv_live_history.add(tv_live_history_4);
        tv_live_history.add(tv_live_history_5);
        tv_live_history.add(tv_live_history_6);
    }

    private void onClick() {
        btn_morelive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                       //更多预告
                Intent intent = new Intent(getActivity(), MedicalLiveRoomTrailerAcitvity.class);
                intent.putExtra("type", "1");                      //用户直播
                intent.putExtra("style", "1");                     //1是预告，2是历史回顾
                startActivity(intent);
            }
        });
        btn_morehistorylive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                 //更多回顾
                Intent intent = new Intent(getActivity(), MedicalLiveRoomReviewAcitvity.class);
                intent.putExtra("type", "1");
                intent.putExtra("style", "2");
                startActivity(intent);
            }
        });
        lv_userlive.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Medical_live_RoomWebActivity.class);
                intent.putExtra("title", liveLists.get(position).getLivename());
                intent.putExtra("aid", liveLists.get(position).getLiveurl());
                startActivity(intent);
            }
        });
    }


    public void setImgandText(JSONArray jsonArray, List<ImageView> imageViews, List<TextView> textViews) {
        try {
            // 循环每一个元素添加到首页中
            for (int i = 0; i < 6; i++) {
                // 获得对应的IMG
                ImageView liveimg = imageViews
                        .get(i);
                // 获取对应的Text
                TextView livetext = textViews
                        .get(i);
                // 判断是否存在不存在则将相对应的占位 隐藏
                if (jsonArray.length() > i) {
                    final JSONObject liveInfo = jsonArray.getJSONObject(i);

                    // 设置IMG URL 视频ID
                    loadImg(liveimg,
                            liveInfo.get("imgurl").toString());
                    // 设置Text 视频ID
                    livetext.setText(liveInfo.get(
                            "livename").toString());
                    liveimg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Medical_live_RoomWebActivity.class);
                            try {
                                intent.putExtra("title", liveInfo.getString("livename"));
                                intent.putExtra("aid", liveInfo.getString("liveurl"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    livetext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), Medical_live_RoomWebActivity.class);
                            try {
                                intent.putExtra("title", liveInfo.getString("livename"));
                                intent.putExtra("aid", liveInfo.getString("liveurl"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    liveimg.setVisibility(View.GONE);
                    livetext.setVisibility(View.GONE);
                    btn_morehistorylive.setVisibility(View.GONE);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void LazyLoad() {
        if (isInit && isVisible) {
            final String uid = SharedPreferencesTools.getUid(getActivity());
            if (uid == null || ("").equals(uid)) {
                return;
            } else {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.directSeeding);
                            obj.put("uid", uid);
                            obj.put("type", "1");
                            String result = HttpClientUtils.sendPost(getActivity(),
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
            isInit = true;      //若为false数据仅加载一次
        }
    }

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getActivity());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }
}
