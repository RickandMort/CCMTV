package com.linlic.ccmtv.yx.activity.subscribe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * name：订阅管理
 * author：Larry
 * data：2017/6/16.
 */
public class SubscribeManagerActivity extends BaseActivity   {
//    @Bind(R.id.activity_title_name)
    private TextView activity_title_name;
//    @Bind(R.id.activity_title_name_view)
    private View activity_title_name_view;
//    @Bind(R.id.activity_title_name2)
    private TextView activity_title_name2;
    private TextView id_tv_practice_department_begin;
//    @Bind(R.id.activity_title_name2_view)
    private View activity_title_name2_view;
//    @Bind(R.id.department_list)
    private ListView department_list;
//    @Bind(R.id.department_list2)
    private ListView department_list2;
    Context context;
    private List<Followks> followkses = new ArrayList<>();
    private List<Followks> nfollowkses = new ArrayList<>();//一级数据
    private List<Followks> nfollowksemins = new ArrayList<>();//二级
    private BaseListAdapter baseListAdaptermax,baseListAdaptermin;
    private Map<String,Object> select_followkse_map = new HashMap<>();
    private Map<String,Object> select_followkse_map2 = new HashMap<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(SubscribeManagerActivity.this, "已取消订阅", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubscribeManagerActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            if(id_tv_practice_department_begin.getText().toString().trim().equals("订阅")){
                                Followks followk_item  = null;
                                for (int i = 0;i<nfollowkses.size();i++){
                                    if(nfollowkses.get(i).getIstrue()){
                                        followk_item = nfollowkses.get(i);
                                    }
                                }
                                int k = 0;
                                for (int i = 0;i<followkses.size();i++){
                                    if(followkses.get(i).getId().equals(followk_item.getId())){
                                       k++;
                                    }
                                }
                                if(k<1){
                                    followkses.add(followk_item);
                                }
                            }else{ //取消订阅成功
                                int k = 0;
                                //循环 小科室
                                for (int i = 0;i<nfollowksemins.size();i++){
                                   if(!nfollowksemins.get(i).getIstrue()){
                                       k++;
                                   }
                                }
                                if(k<1){ //当前科室没有任何子项被订阅
                                    for (int i = 0;i<followkses.size();i++){
                                        if(followkses.get(i).getIstrue()){
                                            followkses.remove(followkses.get(i));

                                        }
                                    }
                                }
                                if(followkses.size()>0){
                                    //设置默认第一个选中
                                    followkses.get(0).setIstrue(true);
                                    setIllness(   followkses.get(0));
                                    id_tv_practice_department_begin.setVisibility(View.VISIBLE);
                                }else{
                                    id_tv_practice_department_begin.setVisibility(View.GONE);
                                    baseListAdaptermax.refresh(new ArrayList<Followks>());
                                    baseListAdaptermin.refresh(new ArrayList<Followks>());
                                }

                           }

                            baseListAdaptermin.notifyDataSetChanged();
                            baseListAdaptermax.notifyDataSetChanged();

                            Toast.makeText(SubscribeManagerActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubscribeManagerActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                        id_tv_practice_department_begin.setClickable(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            JSONArray dataArray = object.getJSONArray("data");
                            Followks illness;
                            nfollowksemins.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                illness = new Followks();
                                JSONObject customJson = dataArray.getJSONObject(i);
                                illness.setName(customJson.getString("name"));
                                illness.setId(customJson.getString("id"));
                                if(id_tv_practice_department_begin.getText().toString().trim().equals("订阅")){
                                    if(customJson.getString("subscribe").equals("0")){
                                        illness.setIstrue(false);
                                        nfollowksemins.add(illness);
                                    }else{
                                        illness.setIstrue(true);
                                        nfollowksemins.add(illness);
                                    }
                                }else{
                                    if(customJson.getString("subscribe").equals("0")){
                                        illness.setIstrue(true);
//                                        nfollowksemins.add(illness);
                                    }else{
                                        illness.setIstrue(false);
                                        nfollowksemins.add(illness);
                                    }
                                }
                                baseListAdaptermin.refresh(nfollowksemins);
                            }
                            baseListAdaptermin.notifyDataSetChanged();
                            baseListAdaptermax.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(SubscribeManagerActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                case 501:
                    id_tv_practice_department_begin.setClickable(true);
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(SubscribeManagerActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_manager2);
        context = this;
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public void initView() {
            activity_title_name = findViewById(R.id.activity_title_name);
            activity_title_name_view =  findViewById(R.id.activity_title_name_view);
          activity_title_name2 = findViewById(R.id.activity_title_name2);
          activity_title_name2_view = findViewById(R.id.activity_title_name2_view);
          department_list = findViewById(R.id.department_list);
          department_list2 = findViewById(R.id.department_list2);
          LayoutInflater inflater = LayoutInflater.from(this);
        View headerView = inflater.inflate(R.layout.activity_subscribe_manager2_buttom,null);
        department_list2.addFooterView(headerView);

        id_tv_practice_department_begin = findViewById(R.id.id_tv_practice_department_begin);

        id_tv_practice_department_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeCase( );
            }
        });
        activity_title_name.setOnClickListener(new View.OnClickListener() {//全部订阅
            @Override
            public void onClick(View v) {
                activity_title_name.setTextColor(Color.parseColor("#4492da"));
                activity_title_name_view.setVisibility(View.VISIBLE);
                activity_title_name2.setTextColor(Color.parseColor("#333333"));
                activity_title_name2_view.setVisibility(View.GONE);
                id_tv_practice_department_begin.setText("订阅");
                for (int i = 0;i<nfollowkses.size();i++){
                    nfollowkses.get(i).setIstrue(false);
                }
                //设置默认第一个选中
                if(nfollowkses.size()>0){
                    nfollowkses.get(0).setIstrue(true);
                    baseListAdaptermax.refresh(nfollowkses);
                    setIllness(   nfollowkses.get(0));
                    id_tv_practice_department_begin.setVisibility(View.VISIBLE);
                }else{
                    baseListAdaptermax.refresh(new ArrayList<Followks>());
                    baseListAdaptermin.refresh(new ArrayList<Followks>());
                    id_tv_practice_department_begin.setVisibility(View.GONE);
                }

            }
        });
        activity_title_name2.setOnClickListener(new View.OnClickListener() {//我的订阅
            @Override
            public void onClick(View v) {
                activity_title_name2.setTextColor(Color.parseColor("#4492da"));
                activity_title_name2_view.setVisibility(View.VISIBLE);
                activity_title_name.setTextColor(Color.parseColor("#333333"));
                activity_title_name_view.setVisibility(View.GONE);
                id_tv_practice_department_begin.setText("取消订阅");
                for (int i = 0;i<followkses.size();i++){
                    followkses.get(i).setIstrue(false);
                }
                //设置默认第一个选中
                if(followkses.size()>0){
                    followkses.get(0).setIstrue(true);
                    baseListAdaptermax.refresh(followkses);
                    setIllness(   followkses.get(0));
                    id_tv_practice_department_begin.setVisibility(View.VISIBLE);
                }else{
                    baseListAdaptermax.refresh(new ArrayList<Followks>());
                    baseListAdaptermin.refresh(new ArrayList<Followks>());
                    id_tv_practice_department_begin.setVisibility(View.GONE);
                }


            }
        });
        baseListAdaptermax = new BaseListAdapter(department_list, nfollowkses, R.layout.item_subscribe1) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Followks map = (Followks) item;
                helper.setText(R.id._item_text,map.getName());

                if(map.getIstrue()){
                    helper.setVisibility(R.id._item_view,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_view,View.GONE);
                }
            }
        };
        department_list.setAdapter(baseListAdaptermax);
        // listview点击事件
        department_list.setOnItemClickListener(new casesharing_listListener());

        baseListAdaptermin = new BaseListAdapter(department_list2, nfollowksemins, R.layout.item_subscribe2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Followks map = (Followks) item;
                helper.setText(R.id._item_text,map.getName());
                if(map.getIstrue()){
                    helper.setImage(R.id._item_img,R.mipmap.subscribe_icon2);
                }else{
                    helper.setImage(R.id._item_img,R.mipmap.subscribe_icon1);
                }
            }
        };
        department_list2.setAdapter(baseListAdaptermin);
        // listview点击事件
        department_list2.setOnItemClickListener(new casesharing_listListener2());
        //设置默认第一个选中
        if(nfollowkses.size()>0){
            nfollowkses.get(0).setIstrue(true);
            setIllness(nfollowkses.get(0));
            id_tv_practice_department_begin.setVisibility(View.VISIBLE);
        }else{
            baseListAdaptermax.refresh(new ArrayList<Followks>());
            baseListAdaptermin.refresh(new ArrayList<Followks>());
            id_tv_practice_department_begin.setVisibility(View.GONE);
        }

    }


    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            if(activity_title_name2_view.getVisibility() == View.VISIBLE){
                //如果当前处于 我的订阅 下
                for (int i = 0;i<followkses.size();i++){
                    followkses.get(i).setIstrue(false);
                }
                followkses.get(arg2).setIstrue(true);
                baseListAdaptermax.notifyDataSetChanged();
                setIllness( followkses.get(arg2));
            }else{
                for (int i = 0;i<nfollowkses.size();i++){
                    nfollowkses.get(i).setIstrue(false);
                }
                nfollowkses.get(arg2).setIstrue(true);
                baseListAdaptermax.notifyDataSetChanged();
                setIllness( nfollowkses.get(arg2));
            }

        }

    }
    private class casesharing_listListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            if(arg2<nfollowksemins.size()){
                if ( nfollowksemins.get(arg2).getIstrue()){
                    nfollowksemins.get(arg2).setIstrue(false);
                    baseListAdaptermin.notifyDataSetChanged();
                }else{
                    nfollowksemins.get(arg2).setIstrue(true);
                    baseListAdaptermin.notifyDataSetChanged();
                }
            }


        }

    }

    private void initData() {
        followkses = (List<Followks>) getIntent().getSerializableExtra("followkses");
        nfollowkses = (List<Followks>) getIntent().getSerializableExtra("nfollowkses");
    }

    private void setIllness(final Followks position) {
        //访问疾病接口
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getJbKs);
                    obj.put("id",  position.getId());
                    obj.put("uid",  SharedPreferencesTools.getUid(context));
//                    Log.e("resultobj", obj.toString());
                    String result = HttpClientUtils.sendPost(SubscribeManagerActivity.this, URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("result555", result.toString());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }
    private void subscribeCase( ) {
        //订阅 & 取消订阅
        MyProgressBarDialogTools.show(context);
        id_tv_practice_department_begin.setClickable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.subscribeCase);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    if(id_tv_practice_department_begin.getText().toString().trim().equals("订阅")){
                        obj.put("subscribe","1");
                        JSONObject kidstr = new JSONObject();
                        for (int i = 0;i<nfollowkses.size();i++){
                            if(nfollowkses.get(i).getIstrue()){
                                kidstr.put("kid",nfollowkses.get(i).getId());
                            }
                        }
                        JSONArray jbid = new JSONArray();
                        for (int i = 0;i<nfollowksemins.size();i++){
                            if(nfollowksemins.get(i).getIstrue()){
                                jbid.put( nfollowksemins.get(i).getId());
                            }
                        }
                        if(jbid.length()<1){
                            Message message = new Message();
                            message.what = 501;
                            message.obj = "请选择订阅科室！";
                            handler.sendMessage(message);
                            return;
                        }
                        kidstr.put("jbid",jbid);
                        obj.put("kidstr",kidstr);
                        //判断上次提交是否有值
                        if(select_followkse_map.size()>0){
                            int k = 0;
                            for (int i = 0 ;i<jbid.length();i++){
                                if(select_followkse_map.containsKey(jbid.getString(i))){
                                    k++;
                                }
                            }
                            if(k!=select_followkse_map.size()){//与上次提交内容不一致
                                select_followkse_map.clear();
                                for (int i = 0 ;i<jbid.length();i++){
                                    select_followkse_map.put(jbid.getString(i),jbid.getString(i));
                                }
                            }else{
                                Message message = new Message();
                                message.what = 501;
                                message.obj = "请勿重复提交！";
                                handler.sendMessage(message);

                                return;
                            }
                        }

                    }else{
                        obj.put("subscribe","0");
                        JSONObject kidstr = new JSONObject();
                        for (int i = 0;i<followkses.size();i++){
                            if(followkses.get(i).getIstrue()){
                                kidstr.put("kid",followkses.get(i).getId());
                            }
                        }
                        JSONArray jbid = new JSONArray();
                        for (int i = 0;i<nfollowksemins.size();i++){
                            if(nfollowksemins.get(i).getIstrue()){
                                jbid.put( nfollowksemins.get(i).getId());
                            }
                        }
                        if(jbid.length()==0){
                            Message message = new Message();
                            message.what = 501;
                            message.obj = "请选择订阅科室！";
                            handler.sendMessage(message);
                            return;
                        }
                        kidstr.put("jbid",jbid);
                        obj.put("kidstr",kidstr);
                        //判断上次提交是否有值
                        if(select_followkse_map2.size()>0){
                            int k = 0;
                            for (int i = 0 ;i<jbid.length();i++){
                                if(select_followkse_map2.containsKey(jbid.getString(i))){
                                    k++;
                                }
                            }
                            if(k!=select_followkse_map2.size()){//与上次提交内容不一致
                                select_followkse_map2.clear();
                                for (int i = 0 ;i<jbid.length();i++){
                                    select_followkse_map2.put(jbid.getString(i),jbid.getString(i));
                                }
                            }else{
                                Message message = new Message();
                                message.what = 501;
                                message.obj = "请勿重复提交！";
                                handler.sendMessage(message);
                                return;
                            }
                        }
                    }
                    String result = HttpClientUtils.sendPost(SubscribeManagerActivity.this, URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
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
