package com.linlic.ccmtv.yx.activity.hospital_training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Section;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter3;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter4;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 医院培训 首页
 * Created by tom on 2017/11/20.
 */

public class Hospital_training_main extends BaseActivity {
    private List<String> images = new ArrayList<>();
    private Banner banner;
    private Context context;
    private MyListView gift_list;
    private String title_type = "";
    private MyGridView learning_video;
    private MyGridAdapter3 myGridAdapter3;
    private MyGridAdapter4 myGridAdapter4;
    private ImageView practice_img;
    private LinearLayout gift_layout, test_questions;
    private Map<String, Object> index_map = new HashMap<>();
    private List<VideoModel> videos = new ArrayList<>();
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<>();
    private List<Section> sections = new ArrayList<>();
    private int pause = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("video_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                VideoModel commodity_module = new VideoModel(dataArray.getJSONObject(i));
                                videos.add(commodity_module);
                            }
                        } else {
                            Toast.makeText(Hospital_training_main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        myGridAdapter3.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            if (jsonObject.getInt("code") == 1) {
                                test_questions.setVisibility(View.VISIBLE);
                            } else {
                                test_questions.setVisibility(View.GONE);
                            }
                            JSONArray index_img = jsonObject.getJSONArray("index_img");
//                            LogUtil.e("图片数据", index_img.toString());
                            for (int i = 0; i < index_img.length(); i++) {
                                JSONObject imgs = index_img.getJSONObject(i);
                                images.add(imgs.getString("img"));
                                index_map.put(imgs.getString("img"), imgs.getString("type"));
                                index_map.put(imgs.getString("type"), imgs.getString("type_name"));
                            }


                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(jsonObject.getString("banner_img")), practice_img);


                            JSONArray test_list = jsonObject.getJSONArray("test_list");
                            for (int i = 0; i < test_list.length(); i++) {
                                sections.add(new Section(test_list.getJSONObject(i)));
                            }
                            JSONArray dataArray = jsonObject.getJSONArray("artic_list");
                            if (dataArray.length() > 0) {
                                gift_layout.setVisibility(View.VISIBLE);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject customJson = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("departemnt_item_title", customJson.getString("title"));
                                    map.put("department_id", customJson.getString("aid"));
                                    map.put("is_digg", customJson.getString("is_digg"));
                                    map.put("department_on_demand", customJson.getString("digg_num"));
                                    map.put("department_times", customJson.getString("posttime"));
                                    map.put("departemnt_item_img", customJson.getString("picurl"));
                                    data.add(map);
                                }
                            } else {
                                gift_layout.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(Hospital_training_main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        initbanner();
                        myGridAdapter4.notifyDataSetChanged();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
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
        setContentView(R.layout.hospital_training_main);
        context = this;
        findId();
        initdata();
        setVideos();
        setPolicies_and_regulations();

    }


    @Override
    public void findId() {
        super.findId();
        banner = (Banner) findViewById(R.id.banner);
        gift_layout = (LinearLayout) findViewById(R.id.gift_layout);
        test_questions = (LinearLayout) findViewById(R.id.test_questions);
        learning_video = (MyGridView) findViewById(R.id.learning_video);
        gift_list = (MyListView) findViewById(R.id.gift_list);
        practice_img = (ImageView) findViewById(R.id.practice_img);
/*        integral = (TextView) findViewById(R.id.integral);
        integral_layout = (LinearLayout) findViewById(R.id.integral_layout);
        exchange_record_layout = (LinearLayout) findViewById(R.id.exchange_record_layout);
        souvenir = (LinearLayout) findViewById(R.id.souvenir);
        electronic_products = (LinearLayout) findViewById(R.id.electronic_products);
        medical_data = (LinearLayout) findViewById(R.id.medical_data);
        more = (LinearLayout) findViewById(R.id.more);
        gift_list = (MyListView) findViewById(R.id.gift_list);*/
    }


    public void practice(View view){
        Intent intent = new Intent(context,Practice_Main.class);
        startActivity(intent);
    }

    /**
     * name: 点击查看某个政策法规的详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            final String uid = SharedPreferencesTools.getUidToLoginClose(context);
            if (uid == null || ("").equals(uid)) {
                return;
            }
            Intent intent = new Intent(context, Details_of_policies_and_regulations.class);
            intent.putExtra("aid", id);
            intent.putExtra("img", title_type);
            startActivity(intent);

        }

    }

    public void initdata() {

        super.setActivity_title_name(getIntent().getStringExtra("title"));
        title_type = getIntent().getStringExtra("title_type");
        myGridAdapter3 = new MyGridAdapter3(context, videos);
        learning_video.setAdapter(myGridAdapter3);
        learning_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.video_id);
                String video_id = textView.getText().toString();
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(context, VideoSignActivity.class);
                intent.putExtra("aid", video_id);
                intent.putExtra("my_our_video", "my_our_video");
                intent.putExtra("bigType", title_type);
                startActivity(intent);
            }
        });

        myGridAdapter4 = new MyGridAdapter4(context, sections);


        baseListAdapter = new BaseListAdapter(gift_list, data, R.layout.hospital_training_main_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id.departemnt_item_title, ((Map) item).get("departemnt_item_title") + "");
                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("department_on_demand") + "");
                helper.setText(R.id.department_times, ((Map) item).get("department_times") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                if (((Map) item).get("is_digg").toString().equals("0")) {
                    helper.setImage(R.id.department_on_demand_img, R.mipmap.assist_icon02);
                } else {
                    helper.setImage(R.id.department_on_demand_img, R.mipmap.assist_icon03);
                }

            }
        };

        gift_list.setAdapter(baseListAdapter);
        // listview点击事件
        gift_list
                .setOnItemClickListener(new casesharing_listListener());
    }

    public void initbanner() {

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Log.e("位置", position + "  =================");
                try {
                    Intent intent = null;
                    intent = new Intent(context, Learning_pachage.class);
                    intent.putExtra("aid", index_map.get(images.get(position)).toString());
                    intent.putExtra("bigType", title_type);
                    intent.putExtra("title", index_map.get(index_map.get(images.get(position))).toString());
                    if (!index_map.get(images.get(position)).toString().equals("0")) {
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    /**
     * name: 点击查看某个考试详细
     */
    public void clickCommodity(View view) {
        TextView textView = (TextView) view
                .findViewById(R.id.commodity_id);
        String id = textView.getText().toString();
        // MyProgressBarDialogTools.show(context);
        //跳转到考试详情
        Intent intent = new Intent(context, Policies_and_regulations.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }


    public void setVideos() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainVideoIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("bigType", title_type);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    Log.e("医院培训首页视频数据", result);
//                    MyProgressBarDialogTools.hide();
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

    public void setPolicies_and_regulations() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.trainArticleIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("bigType", title_type);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Hospital_training, obj.toString());
//                    LogUtil.e("医院培训首页政策法规", result);
//                    MyProgressBarDialogTools.hide();
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

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }


    @Override
    public void onResume() {
        if (pause > 0) {
            data.removeAll(data);
            videos.removeAll(videos);
            sections.removeAll(sections);
            images.removeAll(images);
            index_map.clear();
            setVideos();
            setPolicies_and_regulations();
            pause = 0;
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        //将数据保存到服务器
        switch (title_type) {
            case "basic"://医学三基
                enterUrl = "http://yy.ccmtv.cn/basic_index/index.html";
                break;
            case "train"://住院医师规培
                enterUrl = "http://yy.ccmtv.cn/train_index/index.html";
                break;
            case "practicing"://执医考试
                enterUrl = "http://yy.ccmtv.cn/Practicing_exam.html";
                break;
            case "general"://全科培训
                enterUrl = "http://yy.ccmtv.cn/General_practice/index.html";
                break;
        }
        pause++;
        super.onPause();
    }

    public void clickMore_video(View view) {
        Intent intent = new Intent(context, More_video.class);
        intent.putExtra("bigType", title_type);
        startActivity(intent);
    }

    public void clickPolicies_and_regulations(View view) {
        Intent intent = new Intent(context, Policies_and_regulations.class);
        intent.putExtra("bigType", title_type);
        startActivity(intent);
    }

    public void clickAnother_batch(View view) {
        videos.clear();
        setVideos();
    }

    public void back(View v) {
        finish();
    }

}
