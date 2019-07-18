package com.linlic.ccmtv.yx.activity.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.medical_database.Article_details;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.adapter.Videos_expertAdapter2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CustomImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.utils.permission.SpacesItemDecoration;
import com.linlic.ccmtv.yx.widget.FoldTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * name：视频播放--专家
 * author：Larry
 * data：2017/5/27 12:04
 */

public class PlayVideo_expert extends BaseActivity {
    private String aid;
//    private ImageView mybackImg;
    private CustomImageView myimg;
    private TextView myName,my_Department,my_Hospital;
    private FoldTextView expert_introduce;
    private RecyclerView recyclerview_home_new;
    private LinearLayout medical_database_layout;
    private MyListView video_menu_comment_list;
    private List<ConferenceBean> conferenceBeanList = new ArrayList<>();
    private List<Home_Video> experts2 = new ArrayList<Home_Video>();//最新
    Videos_expertAdapter2 videos_expertAdapter2;
    public int currPage = 1;
    private boolean isNoMore = false;
    private Context context;
    private BaseListAdapter baseListAdapter;
    //用户统计
    private String fid;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LinearLayout dibuView = (LinearLayout) findViewById(R.id.detail_header);
            //全屏后底部点赞view
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            if (!dataObject.isNull("data")) {
                                isNoMore = true;
                            } else {
                                if (!dataObject.isNull("picurl")) {
                                    loadImg(myimg, dataObject.getString("picurl"));
                                    returnBitmap(dataObject.getString("picurl"));
                                }
                                if (!dataObject.isNull("title")) {
                                    myName.setText(dataObject.getString("title"));
                                }
                                if (!dataObject.isNull("description")) {
                                    my_Department.setText(dataObject.getString("description"));
                                }else{
                                    my_Department.setVisibility(View.GONE);
                                }
                                if (!dataObject.isNull("keywords")) {
                                    my_Hospital.setText(dataObject.getString("keywords")+dataObject.getString("smalltitle"));
                                }else{
                                    my_Hospital.setVisibility(View.GONE);
                                }
                                if (!dataObject.isNull("content")) {
                                    expert_introduce.setText(dataObject.getString("content"));
                                }
                                if (!dataObject.isNull("relatedinfo")) {
                                    JSONArray dataArray = dataObject.getJSONArray("relatedinfo");
                                    for (int i = 0; i < dataArray.length(); i ++) {
                                        JSONObject expertJson = dataArray.getJSONObject(i);


                                        experts2.add(new Home_Video(expertJson.getString("aid"), expertJson.getString("title"), expertJson.getString("picurl"), expertJson.getString("flag"), expertJson.getString("videopaymoney"), expertJson.getString("money")));

                                    }
                                }
                                JSONArray dataConference = dataObject.getJSONArray("article");
                                for (int i = 0; i < dataConference.length(); i++) {
                                    JSONObject conferenceBannerObject = dataConference.getJSONObject(i);
                                    ConferenceBean conferenceBean = new ConferenceBean();
                                    conferenceBean.setId(conferenceBannerObject.getString("aid"));
                                    conferenceBean.setTitle(conferenceBannerObject.getString("title"));
                                    conferenceBean.setIconUrl(conferenceBannerObject.has("picurl")?conferenceBannerObject.getString("picurl"):"");
                                    conferenceBean.setTime(conferenceBannerObject.getString("posttime"));
                                    conferenceBean.setCollectStatus(conferenceBannerObject.has("videoclass")?conferenceBannerObject.getString("videoclass"):"");
                                    conferenceBeanList.add(conferenceBean);
                                }
                                if(conferenceBeanList.size()>0){
                                    medical_database_layout.setVisibility(View.VISIBLE);
                                }else{
                                    medical_database_layout.setVisibility(View.GONE);
                                }

                                baseListAdapter.notifyDataSetChanged();

                            }

                        } else {
                            isNoMore = true;
                            Toast.makeText(PlayVideo_expert.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        videos_expertAdapter2.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    private void returnBitmap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL fileUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mybackImg.setImageDrawable(new BitmapDrawable(context.getResources(), blurBitmap(bitmap)));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public Bitmap blurBitmap(Bitmap bitmap) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context.getApplicationContext());
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //Set the radius of the blur
        blurScript.setRadius(25.f);
        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        //recycle the original bitmap
        bitmap.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_menu_expert2);
        context = this;
        super.findId();
        Intent intent = getIntent();
        aid = intent.getStringExtra("aid");
        fid = getIntent().getStringExtra("fid");
        findId();
        setValue();
        getExpertRulest();
    }

    public void setValue() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview_home_new.setLayoutManager(linearLayoutManager);
        recyclerview_home_new.addItemDecoration(new SpacesItemDecoration(15));
        videos_expertAdapter2 = new Videos_expertAdapter2(context, experts2, onRecyclerviewItemClickListener);
        recyclerview_home_new.setAdapter(videos_expertAdapter2);

        baseListAdapter = new BaseListAdapter(video_menu_comment_list, conferenceBeanList, R.layout.item_medical_database) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                ConferenceBean conferenceBean = (ConferenceBean) item;
                helper.setVisibility(R.id._item_type_img, View.GONE);
                helper.setText(R.id._item_title, ((ConferenceBean) item).getTitle());

                helper.setText(R.id._item_time, ((ConferenceBean) item).getTime());

                if ( conferenceBean.getCollectStatus().length()>0) {
                    helper.setVisibility(R.id._item_type, View.VISIBLE);
                    helper.setText(R.id._item_type, ((ConferenceBean) item).getCollectStatus());
                } else {
                    helper.setVisibility(R.id._item_type, View.GONE);
                }
                if ( conferenceBean.getIconUrl().length()>0) {
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                    helper.setImageBitmapGlide(context, R.id._item_img, ((ConferenceBean) item).getIconUrl());
                } else {
                    helper.setVisibility(R.id._item_img, View.GONE);
                }
            }
        };

        video_menu_comment_list.setAdapter(baseListAdapter);
        video_menu_comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conferenceId = conferenceBeanList.get(position).getId();
                Intent intent = new Intent(context, Article_details.class);
                intent.putExtra("aid", conferenceId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void findId() {
        super.findId();

        recyclerview_home_new = (RecyclerView) findViewById(R.id.recyclerview_home_new);
        video_menu_comment_list = (MyListView) findViewById(R.id.video_menu_comment_list);
        medical_database_layout = (LinearLayout) findViewById(R.id.medical_database_layout);


//        mybackImg = (ImageView) findViewById(R.id.mybackImg);
        myimg = (CustomImageView) findViewById(R.id.myimg);
        myName = (TextView) findViewById(R.id.myName);
        my_Department = (TextView) findViewById(R.id.my_Department);
        my_Hospital = (TextView) findViewById(R.id.my_Hospital);
        expert_introduce = (FoldTextView) findViewById(R.id.expert_introduce);
    }

    public void getExpertRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getExpertOnly);
                    obj.put("aid", aid);
                    obj.put("page", currPage);
                    String result = HttpClientUtils.sendPost(PlayVideo_expert.this,
                            URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(PlayVideo_expert.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/video/" +
                fid + "/" + aid + ".html";//拼接url
        super.onPause();
    }

    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
             TextView view = ( TextView) v.findViewById(R.id.video_aid);
             Intent intent = null;
             intent = new Intent(context, VideoFive.class);
             intent.putExtra("aid", view.getText().toString());
            if(SharedPreferencesTools.getUidONnull(context).equals("")){
                startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                LocalApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                if (intent != null) {
                    startActivity(intent);
                }

            }

        }
    };
}

