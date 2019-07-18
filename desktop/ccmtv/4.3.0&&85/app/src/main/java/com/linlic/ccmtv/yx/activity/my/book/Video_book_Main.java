package com.linlic.ccmtv.yx.activity.my.book;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_Entity;
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

/**
 * Created by Administrator on 2018/4/12.
 */

public class Video_book_Main extends BaseActivity {

    private GridView video_book_grid;
    private TextView activity_title_name,activity_title_name2,order;
    private EditText editText1;

    BaseListAdapter baseListAdapter;
    private int type = 1;
    private int page = 1;
    private int limit = 10;

    private List<Video_book_Entity> video_book_entities = new ArrayList<>();
    Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if(page ==1){
                                video_book_entities.clear();
                            }
                            if(type == 1){
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Video_book_Entity video_book_entity = new Video_book_Entity();
                                    video_book_entity.setId(dataObject.getString("id"));
                                    video_book_entity.setName(dataObject.getString("title"));
                                    video_book_entity.setEstate(dataObject.has("jianjie")?dataObject.getString("jianjie"):"");
                                    video_book_entity.setCover_chart(dataObject.getString("banner"));
                                    video_book_entity.setAuthor(dataObject.has("zz")?dataObject.getString("zz"):"");
                                    video_book_entity.setMoney(dataObject.has("money")?dataObject.getDouble("money"):0D);
                                    video_book_entities.add(video_book_entity);
                                }
                            }else{
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Video_book_Entity video_book_entity = new Video_book_Entity();
                                    video_book_entity.setId(dataObject.getString("id"));
                                    video_book_entity.setName(dataObject.getString("name"));
                                    video_book_entity.setEstate(dataObject.has("jianjie")?dataObject.getString("jianjie"):"");
                                    video_book_entity.setCover_chart(dataObject.getString("imgurl"));
                                    video_book_entity.setAuthor(dataObject.has("zz")?dataObject.getString("zz"):"");
                                    video_book_entity.setMoney(dataObject.has("money")?dataObject.getDouble("money"):0D);
                                    video_book_entities.add(video_book_entity);
                                }
                            }

                            baseListAdapter.notifyDataSetChanged();
                        } else {//失败
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(video_book_entities.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(video_book_entities.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(video_book_entities.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };
    private NodataEmptyLayout book_nodata;

    private void setResultStatus(boolean status, int code) {
        if (status) {
            video_book_grid.setVisibility(View.VISIBLE);
            book_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                book_nodata.setNetErrorIcon();
            } else {
                book_nodata.setLastEmptyIcon();
            }
            video_book_grid.setVisibility(View.GONE);
            book_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_book_main);
        context = this;
        SharedPreferencesTools.getUid(context);
        findId();
        initView();

        setmsgdb();
    }

    @Override
    public void findId() {
        super.findId();
        video_book_grid = (GridView) findViewById(R.id.video_book_grid);
        book_nodata = (NodataEmptyLayout) findViewById(R.id.lt_book_nodata);
        activity_title_name2 = (TextView) findViewById(R.id.activity_title_name2);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        order = (TextView) findViewById(R.id.order);
        editText1 = (EditText) findViewById(R.id.editText1);


    }




    public void initView() {

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,My_order.class);
                startActivity(intent);
            }
        });
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(Video_book_Main.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    page = 1;
                    setmsgdb();
                }
                return false;
            }
        });
        activity_title_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type == 2){
                    type = 1;
                    activity_title_name.setTextColor(Color.parseColor("#3997F9"));
                    activity_title_name2.setTextColor(Color.parseColor("#333333"));
                    page = 1;
                    setmsgdb();
                }


            }
        });
        activity_title_name2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == 1){
                    type = 2;
                    activity_title_name2.setTextColor(Color.parseColor("#3997F9"));
                    activity_title_name.setTextColor(Color.parseColor("#333333"));
                    page = 1;
                    setmsgdb();
                }

            }
        });
        video_book_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色

        baseListAdapter = new BaseListAdapter(video_book_grid, video_book_entities, R.layout.video_book_main_gridadapter_item) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                super.convert(helper, item, isScrolling, position);
                Video_book_Entity map = (Video_book_Entity)item;

                helper.setImageBitmap(R.id.cover_chart, map.getCover_chart());
                helper.setTag(R.id.cover_chart,map.getId() );
                helper.setText(R.id.name, map.getName());
                helper.setText(R.id.estate, map.getEstate());
                helper.setViewVisibility(R.id.estate, View.GONE);
                if(type == 1){
                    helper.setText(R.id.money,"￥"+ map.getMoney());
                    helper.setViewVisibility(R.id.money, View.VISIBLE);
                }else {
                    helper.setViewVisibility(R.id.money, View.GONE);
                }



            }
        };

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = video_book_grid.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = video_book_grid.getChildAt(video_book_grid.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == video_book_grid.getHeight()) {
                        if (video_book_entities.size()>(page*limit)) {
                            page += 1;
                            setmsgdb();
                        }
                    }
                }
            }
        });


        video_book_grid.setAdapter(baseListAdapter);
        video_book_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                ImageView textView = (ImageView) view.findViewById(R.id.cover_chart);
                Intent intent ;
                if(type == 1){
                    intent = new Intent(view.getContext(), Video_book2.class);
                }else{
                    intent = new Intent(view.getContext(), Video_book.class);
                }

                intent.putExtra("type", "0");
                intent.putExtra("book_id", textView.getTag().toString());
                intent.putExtra("book_name", video_book_entities.get(arg2).getName());
                intent.putExtra("book_img", video_book_entities.get(arg2).getCover_chart());
                view.getContext().startActivity(intent);
            }
        });
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
                    obj.put("act", URLConfig.getBooksList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", type);
                    obj.put("page", page);
                    obj.put("limit", limit);
                    obj.put("keywrods", editText1.getText().toString());

                    String result = HttpClientUtils.sendPost(context, URLConfig.book_url, obj.toString());
//                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("电子书",result);

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
