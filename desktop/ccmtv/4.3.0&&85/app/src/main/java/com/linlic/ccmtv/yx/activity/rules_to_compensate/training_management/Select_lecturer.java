package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Condition;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/** 选择讲师
 * Created by Administrator on 2018/8/27.
 */

public class Select_lecturer extends BaseActivity {
    private Context context;
    @Bind(R.id.student_list)
    ListView student_list;
    @Bind(R.id.select_all)
    TextView select_all;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.search)//搜索按钮
    TextView search;
    @Bind(R.id.keyword_text)//关键字搜索输入框
    EditText keyword_text;
    @Bind(R.id.condition_text)
    TextView condition_text;
    @Bind(R.id.management_nodata)
    NodataEmptyLayout management_nodata;
    @Bind(R.id.condition_list)
    ListView condition_list;
    @Bind(R.id.condition_reset)
    TextView condition_reset;//重置
    @Bind(R.id.condition_submit)
    TextView condition_submit;//确定
    @Bind(R.id.leave_submit_layout)
    LinearLayout leave_submit_layout;
    //跳转发过来的所选中的数据
    private List<String> select_list = new ArrayList<>();
    //本页面员工的数据 及位置    根据  select_list 传输过来的 数据更改初始选中的成员
    private Map<String,Resident> select_Resident= new  HashMap<>();
    private Map<String,Object> select_map= new  HashMap<>();
    private List<String> spinner_list = new ArrayList<>() ;//类型数据
    Map<String, Object> spinner_map = new HashMap<>();
    private String fid = "";
    private Dialog dialog;
    private View view ;
    JSONObject result, data;
    private int page = 1;
    private int count = 0;
    private int limit = 20;
    private List<Resident> listData = new ArrayList<Resident>();
    private BaseListAdapter baseListAdapterVideo;
    private BaseListAdapter baseListAdapter;
    private List<Condition> conditions = new ArrayList<>();
    private Map<String ,Object> select_condition = new HashMap<>();
    private int curr_pos = -1;
    private Condition item_conditions =new  Condition();
    List<Resident> intent_select_list = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("dataList").getJSONArray("list");
                                count = dataJson.getJSONObject("dataList").getInt("count");
                                if(page == 1){
                                    listData.clear();
                                    select_map.clear();
                                }
                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataJson1.getString("uid"));
                                    resident.setImgUrl(dataJson1.getString("IDphoto"));
                                    resident.setIs_select(false);
                                    resident.setName(dataJson1.getString("realname"));
                                    resident.setUsername(dataJson1.getString("username"));

                                    select_map.put(resident.getId(),listData.size());
                                    listData.add(resident);
                                }

                                //处理传输过来的数据
                                select_list.clear();

                                Set<String> keys = select_Resident.keySet();
                                Iterator<String> iterator = keys.iterator();
                                while (iterator.hasNext()){
                                    String key = iterator.next();
                                    if(select_map.containsKey(key)){
                                        listData.get(Integer.parseInt(select_map.get(key).toString())).setIs_select(true);
                                    }
                                }

                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject dataListObj = dataJson.getJSONObject("dataList");

                                JSONArray user_type = dataListObj.getJSONArray("user_type");
                                Condition user_type_condition = new Condition();
                                user_type_condition.setId("user_type");
                                user_type_condition.setTitle("用户类别");
                                user_type_condition.setIs_select(false);
                                user_type_condition.setCurr_pos(0);
                                List<Condition> user_type_child_list = new ArrayList<>();
                                        for (int j = 0; j < user_type.length(); j++) {
                                            Condition child = new Condition();
                                            child.setId(""+j);
                                            child.setTitle(user_type.getString(j));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            user_type_child_list.add(child);
                                        }
                                user_type_condition.setChilds(user_type_child_list);
                                conditions.add(user_type_condition);

                                JSONArray staff_category = dataListObj.getJSONArray("staff_category");
                                Condition staff_category_condition = new Condition();
                                staff_category_condition.setId("staff_category");
                                staff_category_condition.setTitle("人员类别");
                                staff_category_condition.setIs_select(false);
                                staff_category_condition.setCurr_pos(1);
                                List<Condition> staff_category_child_list = new ArrayList<>();
                                        for (int j = 0; j < staff_category.length(); j++) {

                                            Condition child = new Condition();
                                            child.setId(staff_category.getString(j));
                                            child.setTitle(staff_category.getString(j));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            staff_category_child_list.add(child);
                                        }
                                staff_category_condition.setChilds(staff_category_child_list);
                                conditions.add(staff_category_condition);


                                JSONArray hospital_list = dataListObj.getJSONArray("hospital_list");
                                Condition hospital_list_condition = new Condition();
                                hospital_list_condition.setId("hospital_list");
                                hospital_list_condition.setTitle("轮转科室");
                                hospital_list_condition.setIs_select(false);
                                hospital_list_condition.setCurr_pos(2);
                                List<Condition> hospital_list_child_list = new ArrayList<>();
                                        for (int j = 0; j < hospital_list.length(); j++) {
                                            JSONObject child_ = hospital_list.getJSONObject(j);
                                            Condition child = new Condition();
                                            child.setId(child_.getString("hospital_kid"));
                                            child.setTitle(child_.getString("name"));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            hospital_list_child_list.add(child);
                                        }
                                hospital_list_condition.setChilds(hospital_list_child_list);
                                conditions.add(hospital_list_condition);


                                JSONArray role_list = dataListObj.getJSONArray( "role_list");
                                Condition role_list_condition = new Condition();
                                role_list_condition.setId("role_list");
                                role_list_condition.setTitle("用户组");
                                role_list_condition.setIs_select(false);
                                role_list_condition.setCurr_pos(3);
                                List<Condition> role_list_child_list = new ArrayList<>();
                                        for (int j = 0; j < role_list.length(); j++) {
                                            JSONObject child_ = role_list.getJSONObject(j);
                                            Condition child = new Condition();
                                            child.setId(child_.getString("id"));
                                            child.setTitle(child_.getString("name"));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            role_list_child_list.add(child);
                                        }
                                role_list_condition.setChilds(role_list_child_list);
                                conditions.add(role_list_condition);

                                JSONArray base_list = dataListObj.getJSONArray( "base_list");
                                Condition base_list_condition = new Condition();
                                base_list_condition.setId("base_list");
                                base_list_condition.setTitle("所有基地");
                                base_list_condition.setIs_select(false);
                                base_list_condition.setCurr_pos(4);
                                List<Condition> base_list_child_list = new ArrayList<>();
                                        for (int j = 0; j < base_list.length(); j++) {
                                            JSONObject child_ = base_list.getJSONObject(j);
                                            Condition child = new Condition();
                                            child.setId(child_.getString("base_id"));
                                            child.setTitle(child_.getString("name"));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            base_list_child_list.add(child);
                                        }
                                base_list_condition.setChilds(base_list_child_list);
                                conditions.add(base_list_condition);

                                JSONArray ls_enrollment_year = dataListObj.getJSONArray( "ls_enrollment_year");
                                Condition ls_enrollment_year_condition = new Condition();
                                ls_enrollment_year_condition.setId("ls_enrollment_year");
                                ls_enrollment_year_condition.setTitle("入培时间");
                                ls_enrollment_year_condition.setIs_select(false);
                                ls_enrollment_year_condition.setCurr_pos(5);
                                List<Condition> ls_enrollment_year_child_list = new ArrayList<>();
                                        for (int j = 0; j < ls_enrollment_year.length(); j++) {
                                            Condition child = new Condition();
                                            child.setId(ls_enrollment_year.getString(j));
                                            child.setTitle(ls_enrollment_year.getString(j));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            ls_enrollment_year_child_list.add(child);
                                        }
                                ls_enrollment_year_condition.setChilds(ls_enrollment_year_child_list);
                                conditions.add(ls_enrollment_year_condition);

                                JSONArray ls_training_years = dataListObj.getJSONArray( "ls_training_years");
                                Condition ls_training_years_condition = new Condition();
                                ls_training_years_condition.setId("ls_training_years");
                                ls_training_years_condition.setTitle("规赔年限");
                                ls_training_years_condition.setIs_select(false);
                                ls_training_years_condition.setCurr_pos(6);
                                List<Condition> ls_training_years_child_list = new ArrayList<>();
                                        for (int j = 0; j < ls_training_years.length(); j++) {
                                            Condition child = new Condition();
                                            child.setId(ls_training_years.getString(j));
                                            child.setTitle(ls_training_years.getString(j));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            ls_training_years_child_list.add(child);
                                        }
                                ls_training_years_condition.setChilds(ls_training_years_child_list);

                                conditions.add(ls_training_years_condition);

                                JSONArray exam_situation_is_ep = dataListObj.getJSONArray( "exam_situation_is_ep");
                                Condition exam_situation_is_ep_condition = new Condition();
                                exam_situation_is_ep_condition.setId("exam_situation_is_ep");
                                exam_situation_is_ep_condition.setTitle("执业医师证");
                                exam_situation_is_ep_condition.setIs_select(false);
                                exam_situation_is_ep_condition.setCurr_pos(7);
                                List<Condition> exam_situation_is_ep_child_list = new ArrayList<>();
                                        for (int j = 0; j < exam_situation_is_ep.length(); j++) {
                                            Condition child = new Condition();
                                            child.setId(""+j);
                                            child.setTitle(exam_situation_is_ep.getString(j));
                                            child.setIs_select(false);
                                            child.setCurr_pos(j);
                                            exam_situation_is_ep_child_list.add(child);
                                        }
                                exam_situation_is_ep_condition.setChilds(exam_situation_is_ep_child_list);
                                conditions.add(exam_situation_is_ep_condition);


                                initthe_activity_type();
                                MyProgressBarDialogTools.hide();

                                select_condition = new HashMap<>();
                                getstudentList();
                            } else {
                                MyProgressBarDialogTools.hide();
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };
    private void setResultStatus(boolean status, int code) {
        if (status) {
            management_nodata.setVisibility(View.GONE);
            student_list.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                management_nodata.setNetErrorIcon();
            } else {
                management_nodata.setLastEmptyIcon();
            }

            student_list.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            management_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_staff5);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();
        getBase();
    }

    public void initthe_activity_type(){

    }
    private void initViews() {
        List<Resident> rs =   (List<Resident>) getIntent().getSerializableExtra("lecturer_data");//获取list方式
        for (Resident resident : rs) {
            intent_select_list.add(resident);
            select_Resident.put(resident.getId(),resident);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                getstudentList();
            }
        });

        baseListAdapterVideo = new BaseListAdapter(student_list, listData, R.layout.item_select_staff) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setImageBitmap(R.id._item_img,map.getImgUrl());
                helper.setText(R.id._item_name,map.getName()+"("+map.getUsername()+")");
                if (map.is_select()){
                    helper.setImage(R.id._item_select,R.mipmap.training_11);
                }else{
                    helper.setImage(R.id._item_select,R.mipmap.training_12);
                }

            }
        };
        student_list.setAdapter(baseListAdapterVideo);
        // listview点击事件
        student_list.setOnItemClickListener(new  casesharing_listListener());

        baseListAdapterVideo.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = student_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = student_list.getChildAt(student_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == student_list.getHeight()) {
                        if ((page*limit)<count) {
                            page += 1;
                            getstudentList();
                        }
                    }
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                List<Resident> list = new ArrayList<>();
                Collection<Resident> collection = select_Resident.values();
                Iterator<Resident> iterator = collection.iterator();
                while (iterator.hasNext()){
                    Resident resident = iterator.next();
                    if(resident.is_select()){
                        list.add(resident);
                    }
                }
                //把返回数据存入Intent
                Bundle bundle=new Bundle();
                bundle.putSerializable("list",(Serializable)list);//序列化,要注意转化(Serializable)
                intent.putExtras(bundle);//发送数据
                //设置返回数据
                Select_lecturer.this.setResult( Activity.RESULT_OK, intent);
                finish();
            }
        });

        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i = 0 ;
                for (Resident resident:listData){
                    if(!resident.is_select()){
                        i = 1;
                    }
                }

                if(i == 1){
                    for (Resident resident:listData){
                        resident.setIs_select(true);
                        select_Resident.put(resident.getId(),resident);
                    }
                }else{
                    for (Resident resident:listData){
                        resident.setIs_select(false);
                        select_Resident.remove(resident.getId());
                    }
                }

                baseListAdapterVideo.notifyDataSetChanged();
            }
        });


        condition_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leave_submit_layout.getVisibility() ==View.VISIBLE){
                    leave_submit_layout.setVisibility(View.GONE);
                }else{
                    for (int i = 0;i<conditions.size();i++){
                        if(select_condition.containsKey(conditions.get(i).getId())){
                            for(int j = 0;j<conditions.get(i).getChilds().size();j++){
                                if(select_condition.get(conditions.get(i).getId()).equals(conditions.get(i).getChilds().get(j).getId())){
                                    conditions.get(i).getChilds().get(j).setIs_select(true);
                                    conditions.get(i).setSelect_name(conditions.get(i).getChilds().get(j).getTitle());
                                }else{
                                    conditions.get(i).getChilds().get(j).setIs_select(false);
                                }
                            }
                        }else {
                            conditions.get(i).setSelect_name("");
                            for(int j = 0;j<conditions.get(i).getChilds().size();j++){
                                conditions.get(i).getChilds().get(j).setIs_select(false);
                            }
                        }

                    }
                    leave_submit_layout.setVisibility(View.VISIBLE);
                    baseListAdapter.notifyDataSetChanged();
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
                helper.setText(R.id._item_text2, map.getSelect_name());
                helper.setVisibility(R.id._item_all_election,View.GONE);

                if(map.getCurr_pos() == curr_pos){
                    if(item_conditions.getChilds().size()>0){
                        helper.setImage(R.id._item_img, R.mipmap.drop_down_icon3);
                        helper.setVisibility(R.id._item_add_layout,View.VISIBLE);
                        helper.setAdapter2(R.id.noscrollgridview,item_conditions,baseListAdapter);
                    }else{
                        helper.setVisibility(R.id._item_add_layout,View.GONE);
                        helper.setImage(R.id._item_img, R.mipmap.drop_down_icon1);
                    }
                }else{
                    helper.setImage(R.id._item_img, R.mipmap.drop_down_icon1);
                    helper.setVisibility(R.id._item_add_layout,View.GONE);
                    helper.setAdapter2(R.id.noscrollgridview,new Condition(),baseListAdapter);
                }


            }
        };
        condition_list.setAdapter(baseListAdapter);
        condition_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(  curr_pos == i){
                    curr_pos = -1;
                    item_conditions = new Condition();
                }else {
                    curr_pos = i;
                    item_conditions = conditions.get(i) ;
                }


                baseListAdapter.notifyDataSetChanged();

            }
        });

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
                    for(int j = 0;j<conditions.get(i).getChilds().size();j++){
                        if( conditions.get(i).getChilds().get(j).is_select()){
                            select_condition.put( conditions.get(i).getId(), conditions.get(i).getChilds().get(j).getId());
                        }
                    }
                }

                //重置内容
                page = 1;
                getstudentList();
            }
        });
        condition_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
                //清除已选中的 数据
                select_condition = new HashMap<>();

                //重置内容
                page = 1;
                curr_pos = -1;
                getstudentList();
            }
        });
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            if(listData.get(arg2).is_select()){
                listData.get(arg2).setIs_select(false);
                select_Resident.remove(listData.get(arg2).getId());
            }else{
                listData.get(arg2).setIs_select(true);
                select_Resident.put(listData.get(arg2).getId(),listData.get(arg2));
            }
            baseListAdapterVideo.notifyDataSetChanged();
        }

    }



    public void getstudentList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getSpeakerNew);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid",fid);
                    obj.put("page",page);
                    obj.put("limit",limit);

                    JSONObject data = new JSONObject();
                    data.put("base_id",select_condition.containsKey("base_list")?select_condition.get("base_list"):"");
                    data.put("hospital_kid",select_condition.containsKey("hospital_list")?select_condition.get("hospital_list"):"");
                    data.put("user_type",select_condition.containsKey("user_type")?select_condition.get("user_type"):"");
                    data.put("rid",select_condition.containsKey("role_list")?select_condition.get("role_list"):"");
                    data.put("ls_enrollment_year",select_condition.containsKey("ls_enrollment_year")?select_condition.get("ls_enrollment_year"):"");
                    data.put("ls_training_years",select_condition.containsKey("ls_training_years")?select_condition.get("ls_training_years"):"");
                    data.put("ls_hoase_physician_category",select_condition.containsKey("staff_category")?select_condition.get("staff_category"):"");
                    data.put("exam_situation_is_ep",select_condition.containsKey("exam_situation_is_ep")?select_condition.get("exam_situation_is_ep"):"");
                    data.put("keyword",keyword_text.getText()!=null?keyword_text.getText().toString():"");


                    obj.put("data",data);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("获取可选择的学员", result);

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
    public void getBase() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getScreenData);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("is_type", 1);

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());


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
