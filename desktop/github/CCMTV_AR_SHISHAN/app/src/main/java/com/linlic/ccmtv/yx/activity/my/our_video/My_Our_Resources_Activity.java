package com.linlic.ccmtv.yx.activity.my.our_video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Condition;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.activity.my.newDownload.LogDownloadListener;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NoScrollGridView;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
/*本院资源*/
public class My_Our_Resources_Activity extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView title_name;
    @Bind(R.id.search_recall)
    TextView search_recall;
    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.title_layout2)
    LinearLayout title_layout2;
    @Bind(R.id.title_layout1)
    LinearLayout title_layout1;
    @Bind(R.id.search_layout)
    LinearLayout search_layout;
    @Bind(R.id.id_lv_our_resources)
    ListView lvResources;
    @Bind(R.id.file_text)
    TextView file_text;
    @Bind(R.id.video_text)
    TextView video_text;
    @Bind(R.id.condition_text)
    TextView condition_text;
    @Bind(R.id.leave_submit_layout)
    LinearLayout leave_submit_layout;
    @Bind(R.id.condition_reset)
    TextView condition_reset;//重置
    @Bind(R.id.condition_submit)
    TextView condition_submit;//确定
    @Bind(R.id.condition_list)
    ListView condition_list;
    @Bind(R.id.lt_nodata1)
    NodataEmptyLayout lt_nodata1;
    private BaseListAdapter baseListAdapter,baseListAdapter_files;
    private boolean is_file = false;
    private boolean is_video = false;
    private List<Condition> conditions = new ArrayList<>();
    private Map<String ,Object> select_condition = new HashMap<>();
    private int curr_pos = -1;
    private List<Map<String, Object>> resourcesData = new ArrayList<>();
    private Context context;
    private int page = 1;
    private int pages = 1;
    private int limit = 30;
    private List<Condition> item_conditions = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {
                            JSONObject resourceObject = jsonObject.getJSONObject("data");
                            file_text.setText(resourceObject.getString("article"));
                            video_text.setText(resourceObject.getString("video"));
                            JSONArray cuts =resourceObject.getJSONArray("cuts");
                            for (int i = 0; i < cuts.length(); i++) {
                                JSONObject dataObject = cuts.getJSONObject(i);
                                Condition condition = new Condition();
                                condition.setId(dataObject.getString("cutid"));
                                condition.setTitle(dataObject.getString("cutname"));
                                condition.setIs_select(false);
                                condition.setCurr_pos(i);
                                if(dataObject.getJSONArray("child").length()>0){
                                    JSONArray child_json = dataObject.getJSONArray("child");
                                    List<Condition> child_list = new ArrayList<>();
                                    for (int j = 0; j < child_json.length(); j++) {
                                        JSONObject child_ = child_json.getJSONObject(j);
                                        Condition child = new Condition();
                                        child.setId(child_.getString("cutid"));
                                        child.setTitle(child_.getString("cutname"));
                                        child.setIs_select(false);
                                        child.setCurr_pos(j);
                                        child_list.add(child);
                                    }
                                    condition.setChilds(child_list);
                                }else{
                                    condition.setChilds(new ArrayList<Condition>());
                                }
                                conditions.add(condition);
                            }
                            initdata();
                        }else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            pages = dataJson.getInt("pages");
                            if(page == 1){
                                resourcesData.clear();
                            }
                            JSONArray list_json = dataJson.getJSONArray("list");
                            for (int i = 0; i < list_json.length(); i++) {
                                JSONObject dataObject = list_json.getJSONObject(i);
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", dataObject.getString("id"));
                                map.put("title", dataObject.getString("title"));
                                map.put("aid", dataObject.getString("filename"));
                                map.put("type", dataObject.getString("type"));
                                map.put("createtime", dataObject.getString("createtime"));
                                map.put("keywords", dataObject.getString("keywords"));
                                if (dataObject.has("video_url")) {
                                    map.put("video_url", dataObject.getString("video_url"));
                                }
                                resourcesData.add(map);

                            }
                            baseListAdapter_files.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        setResultStatus(resourcesData.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        setResultStatus(resourcesData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(resourcesData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvResources.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvResources.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_our_resources);
        context = this;
        ButterKnife.bind(this);
        findId();
        initListView();
        getParentMap();
    }
    @Override
    public void findId() {
        title_name.setText("本院资源");
        lvResources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //
                if (resourcesData.get(position).get("type").equals("视频")) {
                    if( resourcesData.get(position).containsKey("video_url")  ){
                        if(resourcesData.get(position).get("video_url")!=null && !resourcesData.get(position).get("video_url").equals("null") && resourcesData.get(position).get("video_url").toString().length()>0){
                            String path1 = resourcesData.get(position).get("video_url").toString();
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("videoTitle", resourcesData.get(position).get("title").toString());//传的videoTitle不带后缀
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
                } else if (resourcesData.get(position).get("type").equals("文档")) {
                    if(  resourcesData.get(position).get("aid").toString().substring(resourcesData.get(position).get("aid").toString().lastIndexOf("."), resourcesData.get(position).get("aid").toString().length()).equals("pdf")){
                        Intent intent = new Intent(My_Our_Resources_Activity.this, MyOurResourcesDisplayActivity2.class);
                        intent.putExtra("aid", resourcesData.get(position).get("aid").toString());
                        intent.putExtra("typeid", "2");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(My_Our_Resources_Activity.this, MyOurResourcesDisplayActivity2.class);
                        intent.putExtra("aid", resourcesData.get(position).get("aid").toString());
                        intent.putExtra("typeid", "3");
                        startActivity(intent);
                    }
                } else {
                    /*Intent intent = new Intent(My_Our_Resources_Activity.this, MyOurResourcesDisplayActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(context, "暂未支持打开该文件格式", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void initListView() {



        baseListAdapter_files = new BaseListAdapter(lvResources, resourcesData, R.layout.item_our_resources_folder2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String,Object> map = (Map) item;
                helper.setText(R.id.id_tv_our_resources_folder, map.get("title").toString());
                helper.setText(R.id.keyword_text,"关键字:  "+ map.get("keywords").toString());
                helper.setText(R.id.item_year, map.get("createtime").toString());
                if(map.get("type").toString().equals("视频")){
                    helper.setImage(R.id.id_iv_our_resources_folder, R.mipmap.ic_learning_task_list_video);
                }else{
                    helper.setImage(R.id.id_iv_our_resources_folder, R.mipmap.ic_learning_task_list_file);
                }
            }
        };
        lvResources.setAdapter(baseListAdapter_files);
        baseListAdapter_files.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = lvResources.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = lvResources.getChildAt(lvResources.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == lvResources.getHeight()) {
                        if (pages>page) {
                            page += 1;
                            initdata();
                        }
                    }
                }
            }
        });
        baseListAdapter = new BaseListAdapter(condition_list, conditions, R.layout.item_our_resources) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;
                helper.setText(R.id._item_text, map.getTitle());
                helper.setTag(R.id._item_all_election, map.getCurr_pos()+"");
                if(map.getCurr_pos() == curr_pos){
                    if(item_conditions.size()>0){
                        helper.setImage(R.id._item_img, R.mipmap.drop_down_icon3);
                        helper.setVisibility(R.id._item_add_layout,View.VISIBLE);
                        helper.setAdapter(R.id.noscrollgridview,item_conditions);
                    }else{
                        helper.setVisibility(R.id._item_add_layout,View.GONE);
                        helper.setImage(R.id._item_img, R.mipmap.drop_down_icon1);
                    }
                }else{
                    helper.setImage(R.id._item_img, R.mipmap.drop_down_icon1);
                    helper.setVisibility(R.id._item_add_layout,View.GONE);
                    helper.setAdapter(R.id.noscrollgridview,new ArrayList<Condition>());
                }
                if(map.is_select()){
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#3897F9"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu18);
                }else{
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#333333"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu33);
                }

            }
        };
        condition_list.setAdapter(baseListAdapter);
        condition_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    curr_pos = i;
                item_conditions = conditions.get(i).getChilds();

                baseListAdapter.notifyDataSetChanged();

            }
        });
     /*   item_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(conditions.get(curr_pos).getChilds().get(i).is_select()){
                    conditions.get(curr_pos).getChilds().get(i).setIs_select(false);
                }else{
                    conditions.get(curr_pos).getChilds().get(i).setIs_select(true);
                }
                baseListAdapter_item.notifyDataSetChanged();
            }
        });*/
        leave_submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
            }
        });
        condition_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
                //更改已选条件显示文字
                select_condition = new HashMap<>();
                for (int i = 0;i<conditions.size();i++){
                    if(conditions.get(i).is_select()){
                        select_condition.put(conditions.get(i).getTitle(),conditions.get(i).getId());
                    }
                    for(int j = 0;j<conditions.get(i).getChilds().size();j++){
                        if( conditions.get(i).getChilds().get(j).is_select()){
                            select_condition.put( conditions.get(i).getChilds().get(j).getTitle(), conditions.get(i).getChilds().get(j).getId());
                        }
                    }
                }
                if(select_condition.keySet().size()>0){
                    Iterator<String> iterator = select_condition.keySet().iterator();
                    condition_text.setText( iterator.next());
                }else{
                    condition_text.setText("全部");
                }

                //重置内容
                page = 1;
                initdata();
            }
        });
        condition_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
                //清除已选中的 数据
                select_condition = new HashMap<>();
                //更改已选条件显示文字
                condition_text.setText("全部");
                //重置内容
                page = 1;
                curr_pos = -1;
                initdata();
            }
        });
        file_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_file){
                    is_file = false;
                    file_text.setTextColor(Color.parseColor("#333333"));
                    file_text.setBackgroundResource(R.drawable.anniu33);
                }else{
                    is_file = true;
                    file_text.setTextColor(Color.parseColor("#3897F9"));
                    file_text.setBackgroundResource(R.drawable.anniu18);
                }
                leave_submit_layout.setVisibility(View.GONE);
                page = 1;
                initdata();
            }
        });
        video_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_video){
                    is_video = false;
                    video_text.setTextColor(Color.parseColor("#333333"));
                    video_text.setBackgroundResource(R.drawable.anniu33);
                }else{
                    is_video = true;
                    video_text.setTextColor(Color.parseColor("#3897F9"));
                    video_text.setBackgroundResource(R.drawable.anniu18);
                }
                leave_submit_layout.setVisibility(View.GONE);
                page = 1;
                initdata();
            }
        });
        condition_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leave_submit_layout.getVisibility() ==View.VISIBLE){
                    leave_submit_layout.setVisibility(View.GONE);
                }else{
                    for (int i = 0;i<conditions.size();i++){
                        if(select_condition.containsKey( conditions.get(i).getTitle())){
                            conditions.get(i).setIs_select(true);
                        }else{
                            conditions.get(i).setIs_select(false);
                        }
                        for(int j = 0;j<conditions.get(i).getChilds().size();j++){
                            if(select_condition.containsKey(conditions.get(i).getChilds().get(j).getTitle())){
                                conditions.get(i).getChilds().get(j).setIs_select(true);
                            }else{
                                conditions.get(i).getChilds().get(j).setIs_select(false);
                            }
                        }
                    }
                    leave_submit_layout.setVisibility(View.VISIBLE);
                    baseListAdapter.notifyDataSetChanged();
                }
            }
        });
        /*设置取消按钮点击事件*/
        search_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.setText("");
                title_layout2.setVisibility(View.GONE);
                title_layout1.setVisibility(View.VISIBLE);
                leave_submit_layout.setVisibility(View.GONE);
                //调用接口
                page = 1;
                initdata();
            }
        });
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(My_Our_Resources_Activity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    page = 1;
                    initdata();
                }
                return false;
            }
        });

        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_layout1.setVisibility(View.GONE);
                title_layout2.setVisibility(View.VISIBLE);
            }
        });


    }


    public void select_condition(View view){
        Condition condition =  conditions.get(Integer.parseInt(view.getTag().toString()));
        if(condition.is_select()){
            condition.setIs_select(false);
            for (int i = 0;i<condition.getChilds().size();i++){
                condition.getChilds().get(i).setIs_select(false);
            }
        }else{
            condition.setIs_select(true);
            for (int i = 0;i<condition.getChilds().size();i++){
                condition.getChilds().get(i).setIs_select(true);
            }
        }
        baseListAdapter.notifyDataSetChanged();
    }


    private void getParentMap( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getSearchInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training_index, obj.toString());
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
    private void initdata( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getResourceBySear);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("limit", limit);
                    obj.put("keywrod", editText1.getText().toString());
                    String cutStr = "";
                    Iterator<Object> iterator = select_condition.values().iterator();
                    while (iterator.hasNext()){
                        if(cutStr.length()>0){
                            cutStr += ","+ iterator.next().toString();
                        }else{
                            cutStr += iterator.next().toString();
                        }
                    }
                    obj.put("cut", cutStr);
                    if(is_file&&is_video){
                        obj.put("type", "");
                    }else if(!is_video&&!is_file){
                        obj.put("type", "");
                    }else{
                        obj.put("type", is_file?"article":"video");
                    }


                    String result = HttpClientUtils.sendPost(context, URLConfig.Hospital_training_index, obj.toString());
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



}
