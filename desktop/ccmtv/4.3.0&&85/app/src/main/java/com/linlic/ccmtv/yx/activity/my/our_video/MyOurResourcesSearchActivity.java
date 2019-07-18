package com.linlic.ccmtv.yx.activity.my.our_video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.our_video.adapter.MyOurResourcesSearchAdapter;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOurResourcesSearchActivity extends BaseActivity {

    private Context context;
    private EditText etSearch;
    private TextView tvCancelSearch;
    private ImageView ivDelete;
    private ListView lvSearchResult;
    private TextView tvSearchResult;
    private List<Map<String, Object>> resourcesSearchData = new ArrayList<>();
    private MyOurResourcesSearchAdapter adapter;
    private NodataEmptyLayout lt_nodata1;
    private boolean isBottom = false;
    private int page = 1;
    private boolean loadComplete = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        MyProgressBarDialogTools.hide();
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {
                            JSONObject resourceObject = jsonObject.getJSONObject("resource");
                            if (!resourceObject.get("status").equals("0")) {
                                JSONArray dataArray = resourceObject.getJSONArray("data");
                                if (dataArray.length() < 10) {
                                    loadComplete = true;
                                }
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("aid", dataObject.getString("aid"));
                                    map.put("name", dataObject.getString("name"));
                                    map.put("posttime", dataObject.getString("posttime"));
                                    map.put("type", dataObject.getString("type"));
                                    map.put("size", dataObject.getString("size"));
                                    map.put("typeid", dataObject.getString("typeid"));
                                    if(dataObject.has("video_url")){
                                        map.put("video_url", dataObject.getString("video_url"));
                                    }
                                    resourcesSearchData.add(map);
                                }
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(context, jsonObject.getJSONObject("resource").getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(resourcesSearchData.size() > 0, jsonObject.getInt("status"));
                        tvSearchResult.setText("搜索结果(" + resourcesSearchData.size() + ")");
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        setResultStatus(resourcesSearchData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(resourcesSearchData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvSearchResult.setVisibility(View.VISIBLE);
            tvSearchResult.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvSearchResult.setVisibility(View.GONE);
            tvSearchResult.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_our_resources_search);

        context = this;
        findId();

        initListView();
    }

    public void findId() {
        etSearch = (EditText) findViewById(R.id.id_et_our_resources_search);
        tvCancelSearch = (TextView) findViewById(R.id.id_tv_our_resources_search_cancel);
        ivDelete = (ImageView) findViewById(R.id.id_iv_our_resources_search_delete);
        lvSearchResult = (ListView) findViewById(R.id.id_lv_our_resources_search_list);
        tvSearchResult = (TextView) findViewById(R.id.id_tv_our_resources_search_result);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);

        tvCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MyOurResourcesSearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    resourcesSearchData.clear();
                    page = 1;
                    loadComplete = false;
                    getSearchData();
                }
                return false;
            }
        });

        lvSearchResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!loadComplete && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isBottom) {
                    //页数加1,请求数据
                    page++;
                    getSearchData();
                    isBottom = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断是否滚到最后一行
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    isBottom = true;
                }/*else {
                    isBottom = false;
                }*/
            }
        });
    }

    /*private void initData() {
        *//*for (int i = 0; i <= 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", "http://img1.imgtn.bdimg.com/it/u=3014892180,449401099&fm=27&gp=0.jpg");
            map.put("name", "文件" + i);
            map.put("time", "2018-05-30");
            map.put("size", "100KB");
            map.put("keyword", etSearch.getText().toString());
            resourcesSearchData.add(map);
        }
        adapter.notifyDataSetChanged();*//*
    }*/

    private void getSearchData() {
        if (etSearch.getText().toString().equals("")) {
            Toast.makeText(context, "请输入搜索参数", Toast.LENGTH_SHORT).show();
            return;
        }

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.searchData);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("keywords", etSearch.getText().toString());
                    obj.put("is_new", "1");
                    obj.put("page", page);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training_index, obj.toString());
//                    LogUtil.e("看看本院资源搜索数据", result);

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

    private void initListView() {
        adapter = new MyOurResourcesSearchAdapter(context, resourcesSearchData);
        lvSearchResult.setAdapter(adapter);

        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //typeid  0：其他     1：视频     2：PDF     3：TXT

                //typeid  0：其他     1：视频     2：PDF     3：TXT
                if (resourcesSearchData.get(position).get("typeid").equals("1")) {
                    if( resourcesSearchData.get(position).containsKey("video_url")  ){
                        if( resourcesSearchData.get(position).get("video_url").toString().length()>0){
                            String path1 = resourcesSearchData.get(position).get("video_url").toString();
                            TextView textView = new TextView(context);
                            textView.setText(Html.fromHtml(resourcesSearchData.get(position).get("name").toString()));
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("videoTitle", textView.getText().toString());//传的videoTitle不带后缀
                            intent.putExtra("videoPath", path1);
                            intent.putExtra("last_look_time", 0);
                            String path = path1.hashCode()+"";
                            intent.putExtra("aid", path);
                            intent.putExtra("videoSource", 1);//设置视频来源   0代表本地  1代表网络
                            startActivity(intent);
                        }else{
                            Toast.makeText(context, "该视频无播放地址，请联系管理员！", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "暂未支持打开该文件格式", Toast.LENGTH_SHORT).show();
                    }
                } else if (resourcesSearchData.get(position).get("typeid").equals("2")) {
                    Intent intent = new Intent(MyOurResourcesSearchActivity.this, MyOurResourcesDisplayActivity.class);
                    intent.putExtra("aid", resourcesSearchData.get(position).get("aid").toString());
                    intent.putExtra("typeid", "2");
                    startActivity(intent);
                } else if (resourcesSearchData.get(position).get("typeid").equals("3")) {
                    Intent intent = new Intent(MyOurResourcesSearchActivity.this, MyOurResourcesDisplayActivity.class);
                    intent.putExtra("aid", resourcesSearchData.get(position).get("aid").toString());
                    intent.putExtra("typeid", "3");
                    startActivity(intent);
                } else if (resourcesSearchData.get(position).get("typeid").equals("4")) {
                    Intent intent = new Intent(MyOurResourcesSearchActivity.this, MyOurResourcesDisplayActivity.class);
                    intent.putExtra("aid", resourcesSearchData.get(position).get("aid").toString());
                    intent.putExtra("typeid", "4");
                    startActivity(intent);
                } else if (resourcesSearchData.get(position).get("typeid").equals("5")) {
                    Intent intent = new Intent(MyOurResourcesSearchActivity.this, MyOurResourcesDisplayActivity.class);
                    intent.putExtra("aid", resourcesSearchData.get(position).get("aid").toString());
                    intent.putExtra("typeid", "5");
                    startActivity(intent);
                } else if (resourcesSearchData.get(position).get("typeid").equals("6")) {
                    Intent intent = new Intent(MyOurResourcesSearchActivity.this, MyOurResourcesDisplayActivity.class);
                    intent.putExtra("aid", resourcesSearchData.get(position).get("aid").toString());
                    intent.putExtra("typeid", "6");
                    startActivity(intent);
                } else {
                    /*Intent intent = new Intent(My_Our_Resources_Activity.this, MyOurResourcesDisplayActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(context, "暂未支持打开该文件格式", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }
}
