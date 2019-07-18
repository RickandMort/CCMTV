package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Special;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题页面
 * Created by Administrator on 2016/3/22.
 */
public class SpecialActivity extends BaseActivity {
    private ListView special_list;// 数据加载
    private boolean isNoMore = false;
    BaseListAdapter baseListAdapter;
    private List<Special> data = new ArrayList<Special>();
    private String title;
    private String aid;
    private int currPage = 1;
    Context context;
    TextView activity_title_name;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            title = jsonObject.getString("specialname");
                            activity_title_name.setText(title);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Special special = new Special();
                                special.setTitle_max(dataObject.getString("name"));
                                special.setTitle_max_id(dataObject.getString("fid"));
                                JSONArray listArray = dataObject.getJSONArray("listdata");
                                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                for (int j = 0; j < listArray.length(); j++) {
                                    JSONObject listObject = listArray.getJSONObject(j);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("special_text", listObject.getString("title"));
                                    map.put("special_img", listObject.getString("picurl"));
                                    map.put("special_aid", listObject.getString("aid"));
                                    map.put("money", listObject.getString("money"));
                                    map.put("flag", listObject.getString("flag"));
                                    map.put("videopaymoney", listObject.getString("videopaymoney"));
                                    list.add(map);
                                }
                                special.setSpecials(list);
                                data.add(special);
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {//失败
                            activity_title_name.setText("专题页面");   //获取不到数据时，标题为空
                            isNoMore = true;
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (currPage == 1) {
                            MyProgressBarDialogTools.hide();
                        }
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            //存贮对象声明(把视频ID存起来)
                            // SharedPreferencesTools.saveVideoId(getApplicationContext(), dataArray.getString("aid"));
                            String uid = SharedPreferencesTools.getUidToLoginClose(context);
                            //跳转页面
                            Intent intent = new Intent(context, VideoFive.class);
                            intent.putExtra("aid", dataArray.getString("aid"));
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    MyProgressBarDialogTools.hide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_max);
        context = this;
        Intent intent = getIntent();
        aid = intent.getExtras().getString("aid");
        findId();
        setText();
        setmsgdb();
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(SpecialActivity.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        if (currPage == 1) {
            MyProgressBarDialogTools.show(context);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.indexCarouselList);
                    obj.put("page", currPage);
                    obj.put("fid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

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
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb2() {
        if (currPage == 1) {
            MyProgressBarDialogTools.show(context);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.indexCarouselList);
                    obj.put("page", currPage);
                    obj.put("fid", aid);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        title = jsonObject.getString("specialname");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            Special special = new Special();
                            special.setTitle_max(dataObject.getString("name"));
                            special.setTitle_max_id(dataObject.getString("fid"));
                            JSONArray listArray = dataObject.getJSONArray("listdata");
                            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                            for (int j = 0; j < listArray.length(); j++) {
                                JSONObject listObject = listArray.getJSONObject(j);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("special_text", listObject.getString("title"));
                                map.put("special_img", listObject.getString("picurl"));
                                map.put("special_aid", listObject.getString("aid"));
                                map.put("money", listObject.getString("money"));
                                map.put("flag", listObject.getString("flag"));
                                map.put("videopaymoney", listObject.getString("videopaymoney"));
                                list.add(map);
                            }
                            special.setSpecials(list);
                            data.add(special);
                        }
                    }
                    baseListAdapter.refresh(data);
//                    baseListAdapter.notifyDataSetChanged();
                    Message message = new Message();
                    message.what = 3;
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

    public void findId() {
        super.findId();
        special_list = (ListView) findViewById(R.id.special_list);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
    }

    public void setText() {
        super.setActivity_title_name(title);
        baseListAdapter = new BaseListAdapter(special_list, data, R.layout.special_max_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.title_max, ((Special) item).getTitle_max());
                helper.setTag(R.id.special_more, ((Special) item).getTitle_max_id());
                helper.setSpecialOnClick(R.id.special_more, ((Special) item).getTitle_max());
                if (((Special) item).getSpecials().size() > 0) {
                    helper.setTag(R.id.special_text1, (((Special) item).getSpecials().get(0).get("special_aid").toString()));
                    helper.setText(R.id.special_text1, (((Special) item).getSpecials().get(0).get("special_text").toString()));
                    helper.setImageBitmaps(R.id.special_img1, (((Special) item).getSpecials().get(0).get("special_img").toString()));
                    helper.setTag(R.id.special_img1, (((Special) item).getSpecials().get(0).get("special_aid").toString()));
                    helper.setVideoOnClick(R.id.special_text1);
                    helper.setVideoOnClick(R.id.special_img1);
                    //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                    if (!((Special) item).getSpecials().get(0).get("videopaymoney").toString().equals("0")) {
                        //收费
                        helper.setImage(R.id.special_top_img1, R.mipmap.charge);
                        helper.setVisibility(R.id.special_top_img1, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id.special_top_img1, View.GONE);
                        if (((Special) item).getSpecials().get(0).get("money").toString().equals("3")) {
                            //会员
                            helper.setImage(R.id.special_top_img1, R.mipmap.vip_img);
                            helper.setVisibility(R.id.special_top_img1, View.VISIBLE);
                        }
                    }
                    helper.setVisibility(R.id.special_text1, View.VISIBLE);
                    helper.setVisibility(R.id.special_img1, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.special_text1, View.INVISIBLE);
                    helper.setVisibility(R.id.special_img1, View.INVISIBLE);
                    helper.setVisibility(R.id.special_top_img1, View.INVISIBLE);
                }
                if (((Special) item).getSpecials().size() > 1) {
                    helper.setTag(R.id.special_text2, (((Special) item).getSpecials().get(1).get("special_aid").toString()));
                    helper.setText(R.id.special_text2, (((Special) item).getSpecials().get(1).get("special_text").toString()));
                    helper.setImageBitmaps(R.id.special_img2, (((Special) item).getSpecials().get(1).get("special_img").toString()));
                    helper.setTag(R.id.special_img2, (((Special) item).getSpecials().get(1).get("special_aid").toString()));
                    helper.setVideoOnClick(R.id.special_text2);
                    helper.setVideoOnClick(R.id.special_img2);
                    //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                    if (!((Special) item).getSpecials().get(1).get("videopaymoney").toString().equals("0")) {
                        //收费
                        helper.setImage(R.id.special_top_img2, R.mipmap.charge);
                        helper.setVisibility(R.id.special_top_img2, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id.special_top_img2, View.GONE);
                        if (((Special) item).getSpecials().get(1).get("money").toString().equals("3")) {
                            //会员
                            helper.setImage(R.id.special_top_img2, R.mipmap.vip_img);
                            helper.setVisibility(R.id.special_top_img2, View.VISIBLE);
                        }
                    }
                    helper.setVisibility(R.id.special_text2, View.VISIBLE);
                    helper.setVisibility(R.id.special_img2, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.special_text2, View.INVISIBLE);
                    helper.setVisibility(R.id.special_img2, View.INVISIBLE);
                    helper.setVisibility(R.id.special_top_img2, View.INVISIBLE);
                }

                if (((Special) item).getSpecials().size() > 2) {
                    helper.setTag(R.id.special_text3, (((Special) item).getSpecials().get(2).get("special_aid").toString()));
                    helper.setText(R.id.special_text3, (((Special) item).getSpecials().get(2).get("special_text").toString()));

                    helper.setImageBitmaps(R.id.special_img3, (((Special) item).getSpecials().get(2).get("special_img").toString()));
                    helper.setTag(R.id.special_img3, (((Special) item).getSpecials().get(2).get("special_aid").toString()));
                    helper.setVideoOnClick(R.id.special_text3);
                    helper.setVideoOnClick(R.id.special_img3);
                    //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                    if (!((Special) item).getSpecials().get(2).get("videopaymoney").toString().equals("0")) {
                        //收费
                        helper.setImage(R.id.special_top_img3, R.mipmap.charge);
                        helper.setVisibility(R.id.special_top_img3, View.VISIBLE);
                    } else {
                        helper.setVisibility(R.id.special_top_img3, View.GONE);
                        if (((Special) item).getSpecials().get(2).get("money").toString().equals("3")) {
                            //会员
                            helper.setImage(R.id.special_top_img3, R.mipmap.vip_img);
                            helper.setVisibility(R.id.special_top_img3, View.VISIBLE);
                        }
                    }
                    helper.setVisibility(R.id.special_text3, View.VISIBLE);
                    helper.setVisibility(R.id.special_img3, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.special_text3, View.INVISIBLE);
                    helper.setVisibility(R.id.special_img3, View.INVISIBLE);
                    helper.setVisibility(R.id.special_top_img3, View.INVISIBLE);
                }

            }
        };
        special_list.setAdapter(baseListAdapter);
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = special_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = special_list.getChildAt(special_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == special_list.getHeight()) {
                        if (!isNoMore) {
                            currPage += 1;
                            setmsgdb2();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }
}
