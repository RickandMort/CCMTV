package com.linlic.ccmtv.yx.activity.direct_broadcast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Direct_broadcast;
import com.linlic.ccmtv.yx.activity.entity.Live_broadcast;
import com.linlic.ccmtv.yx.adapter.Direct_broadcast_list_itemAdapter;
import com.linlic.ccmtv.yx.utils.CategoryView2;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 直播介绍页
 * Created by Administrator on 2018/3/13.
 */

public class Live_broadcast_introduction extends BaseActivity {

    private Context context;
    private MyRecyclerView direct_broadcast_item_myrecyclerview;
    private Direct_broadcast direct_broadcast ;
    private TabLayout tabLayout;
    private Map<String,List<Live_broadcast>> map =new HashMap<>();
    private  Direct_broadcast_list_itemAdapter myGridAdapter;
    private TextView brief_introduction_of_live_broadcast;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        //测试暂时封掉
                      /*  final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < 6; i++) {
//                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("direct_broadcast_item_type", "1");
                                map.put("direct_broadcast_item_title", "测试测试测试title"+i);
                                map.put("direct_broadcast_item_icon", "1");
                                map.put("direct_broadcast_item_banner", images.get(i));

                                data.add(map);
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(Direct_broadcast_main.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_broadcast_introduction);
        context = this;
        findId();
        initParent_Values();
        initRecyclerView();
    }

    public void findId() {
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        brief_introduction_of_live_broadcast = (TextView) findViewById(R.id.brief_introduction_of_live_broadcast);
    }

    public void initParent_Values(){
        direct_broadcast = (Direct_broadcast) getIntent().getSerializableExtra("direct_broadcast");
        if(direct_broadcast!=null && 1==1){
            //判断是否有简介内容
            if(direct_broadcast.getAbout()!=null&& direct_broadcast.getAbout().trim().length()>0){
                brief_introduction_of_live_broadcast.setVisibility(View.VISIBLE);
            }else{
                brief_introduction_of_live_broadcast.setVisibility(View.GONE);
            }

            for (Live_broadcast live_broadcast:direct_broadcast.getLive_broadcasts()){
                if(map.containsKey(live_broadcast.getDename())){
                    map.get(live_broadcast.getDename()).add(live_broadcast);
                }else{
                    List<Live_broadcast> list = new ArrayList<Live_broadcast>();
                    list.add(live_broadcast);
                    map.put(live_broadcast.getDename(),list);
                }
            }
            Iterator<String> iterable =   map.keySet().iterator ();
            while (iterable.hasNext()){
                tabLayout.addTab(tabLayout.newTab().setText(iterable.next()));
            }
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override

                public void onTabSelected(TabLayout.Tab tab) {

                    //选中了tab的逻辑
//                    Log.e("选中了tab的逻辑",tab.getText()+"");
                    myGridAdapter =  new Direct_broadcast_list_itemAdapter(direct_broadcast_item_myrecyclerview.getContext(),map.get(tab.getText())  );
                    direct_broadcast_item_myrecyclerview.setAdapter(myGridAdapter);
//                myGridAdapter.notifyDataSetChanged();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                    //未选中tab的逻辑
//                    Log.e("未选中tab的逻辑",tab.getText()+"");
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                    //再次选中tab的逻辑
//                    Log.e("再次选中tab的逻辑",tab.getText()+"");
                }

            });
        }

    }

    public void initRecyclerView(){
        direct_broadcast_item_myrecyclerview = (MyRecyclerView) findViewById(R.id.direct_broadcast_item_myrecyclerview);
        RecyclerView.LayoutManager   mLayoutManager = new LinearLayoutManager(direct_broadcast_item_myrecyclerview.getContext()
                , LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        direct_broadcast_item_myrecyclerview.setLayoutManager(mLayoutManager);
          myGridAdapter =  new Direct_broadcast_list_itemAdapter(direct_broadcast_item_myrecyclerview.getContext(),map.get(map.keySet().iterator().next())  );
        direct_broadcast_item_myrecyclerview.setAdapter(myGridAdapter);
    }

    public void  onclick_brief_introduction_of_live_broadcast(View view){
        Intent intent = new Intent(context,Brief_introduction_of_live_broadcast.class);
        intent.putExtra("direct_broadcast",  direct_broadcast);
        startActivity(intent);
    }
}
