package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.CommentTypeEntity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 评价
 * Created by Administrator on 2018/6/19.
 */

public class Evaluation extends BaseActivity {
    private Context context;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.id_spinner_examination_manage)
    NiceSpinner spinnerEduType;
    @Bind(R.id.activity_title_name)
    TextView activity_title_name;
//    @Bind(R.id.monthly_evaluation_img)
//    ImageView monthly_evaluation_img;
    @Bind(R.id.evaliation_nodata)
    NodataEmptyLayout evaliation_nodata;


    JSONObject result, data;
    private String fid = "";
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
    private List<String> typeStringList = new ArrayList<>();
    private List<CommentTypeEntity> typeList = new ArrayList<>();

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
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                if (!dateJson.isNull("type")){
                                    typeStringList.clear();
                                    typeList.clear();
                                    JSONArray types = dateJson.getJSONArray("type");
                                    for (int i = 0; i < types.length(); i++){
                                        JSONObject typeObject = types.getJSONObject(i);
                                        CommentTypeEntity typeEntity = new CommentTypeEntity();
                                        typeEntity.setName(typeObject.getString("name"));
                                        typeEntity.setType(typeObject.getString("type"));
                                        typeList.add(typeEntity);
                                        String name = typeObject.getString("name");
                                        if(name.equals("所有评价")){
                                            typeStringList.add("月度评价");
                                        }else {
                                            typeStringList.add(typeObject.getString("name"));
                                        }
                                    }
                                    setSpinner();
                                }
                                if (!dateJson.isNull("data")) {
                                    JSONArray datas = dateJson.getJSONArray("data");
                                    listData.clear();
                                    for (int i = 0; i < datas.length(); i++) {
                                        JSONObject dataJson1 = datas.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("title", dataJson1.getString("title"));
                                        if (!dataJson1.isNull("key")) {
                                            map.put("key", dataJson1.getString("key"));
                                        }
                                        map.put("month", dataJson1.getString("month"));
                                        if (!dataJson1.isNull("gps_ids")) {
                                            map.put("gps_ids", dataJson1.getString("gps_ids"));
                                        }
                                        if (!dataJson1.isNull("s_score")) {
                                            map.put("s_score", dataJson1.getString("s_score"));
                                        }
                                        if (!dataJson1.isNull("c_num")) {
                                            map.put("c_num", dataJson1.getString("c_num"));
                                        }
                                        if (!dataJson1.isNull("t_num")) {
                                            map.put("t_num", dataJson1.getString("t_num"));
                                        }
                                        if (!dataJson1.isNull("v_score")) {
                                            map.put("v_score", dataJson1.getString("v_score"));
                                        }
                                        if (!dataJson1.isNull("other_ids")) {
                                            map.put("other_ids", dataJson1.getString("other_ids"));
                                        }
                                        if (!dataJson1.isNull("tag")){
                                            map.put("tag", dataJson1.getString("tag"));
                                        }
                                        if (!dataJson1.isNull("createtime")){
                                            map.put("createtime", dataJson1.getString("createtime"));
                                        }
                                        if (!dataJson1.isNull("id")){
                                            map.put("id", dataJson1.getString("id"));
                                        }
                                        if (!dataJson1.isNull("hosid")){
                                            map.put("hosid", dataJson1.getString("hosid"));
                                        }
                                        if (!dataJson1.isNull("year")){
                                            map.put("year", dataJson1.getString("year"));
                                        }
                                        if (!dataJson1.isNull("url")){
                                            map.put("url", dataJson1.getString("url"));
                                        }
                                        if (!dataJson1.isNull("url")){
                                            map.put("url", dataJson1.getString("url"));
                                        }

                                        listData.add(map);
                                    }
                                    baseListAdapterVideo.notifyDataSetChanged();
                                    //listData=invertOrderList(listData);
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                if (!dateJson.isNull("data")) {
                                    JSONArray datas = dateJson.getJSONArray("data");
                                    listData.clear();
                                    for (int i = 0; i < datas.length(); i++) {
                                        JSONObject dataJson1 = datas.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("title", dataJson1.getString("title"));
                                        if (!dataJson1.isNull("key")) {
                                            map.put("key", dataJson1.getString("key"));
                                        }
                                        map.put("month", dataJson1.getString("month"));
                                        if (!dataJson1.isNull("gps_ids")) {
                                            map.put("gps_ids", dataJson1.getString("gps_ids"));
                                        }
                                        if (!dataJson1.isNull("s_score")) {
                                            map.put("s_score", dataJson1.getString("s_score"));
                                        }
                                        if (!dataJson1.isNull("c_num")) {
                                            map.put("c_num", dataJson1.getString("c_num"));
                                        }
                                        if (!dataJson1.isNull("t_num")) {
                                            map.put("t_num", dataJson1.getString("t_num"));
                                        }
                                        if (!dataJson1.isNull("v_score")) {
                                            map.put("v_score", dataJson1.getString("v_score"));
                                        }
                                        if (!dataJson1.isNull("other_ids")) {
                                            map.put("other_ids", dataJson1.getString("other_ids"));
                                        }
                                        if (!dataJson1.isNull("tag")){
                                            map.put("tag", dataJson1.getString("tag"));
                                        }
                                        if (!dataJson1.isNull("createtime")){
                                            map.put("createtime", dataJson1.getString("createtime"));
                                        }
                                        if (!dataJson1.isNull("id")){
                                            map.put("id", dataJson1.getString("id"));
                                        }
                                        if (!dataJson1.isNull("hosid")){
                                            map.put("hosid", dataJson1.getString("hosid"));
                                        }
                                        if (!dataJson1.isNull("year")){
                                            map.put("year", dataJson1.getString("year"));
                                        }
                                        if (!dataJson1.isNull("url")){
                                            map.put("url", dataJson1.getString("url"));
                                        }
//                                        if (!dataJson1.isNull("url")){
//                                            map.put("url", dataJson1.getString("url"));
//                                        }
                                        listData.add(map);
                                    }
                                    baseListAdapterVideo.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };
     /*
       按时间倒叙排列
      */
//    @SuppressLint("SimpleDateFormat")
//    private List<Map<String,Object>> invertOrderList(List<Map<String,Object>> L){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d1;
//        Date d2;
//        Map<String,Object> map=new HashMap<String, Object>();
//        //String temp_r ="";
//        //做一个冒泡排序，大的在数组的前列
//        for(int i=0; i<L.size()-1; i++){
//            for(int j=i+1; j<L.size();j++){
//                ParsePosition pos1 = new ParsePosition(0);
//                ParsePosition pos2 = new ParsePosition(0);
//                d1 = sdf.parse(L.get(i).get("createtime").toString(), pos1);
//                d2 = sdf.parse(L.get(j).get("createtime").toString(), pos2);
//                if(d1.before(d2)){//如果队前日期靠前，调换顺序
//                    map = L.get(i);
//                    L.set(i, L.get(j));
//                    L.set(j, map);
//                }
//            }
//        }
//
//        return L;
//    }

    private void setResultStatus(boolean status, int code) {
        if (status) {
            listView.setVisibility(View.VISIBLE);
            evaliation_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                evaliation_nodata.setNetErrorIcon();
            } else {
                evaliation_nodata.setLastEmptyIcon();
            }
            listView.setVisibility(View.GONE);
            evaliation_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.evaluation);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        initView();
        initDatas();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
        super.onPause();
    }

    /***
     * 年度评优
     * @param view
     */
    public void The_annual_assessment(View view) {
        startActivity(new Intent(context, The_annual_assessment.class));
        //跳转页面
    }

    private void initDatas() {
        listData.clear();
        baseListAdapterVideo.notifyDataSetChanged();
    }


    public void initView() {
        activity_title_name.setText(getIntent().getStringExtra("name"));
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_the_evaluation_of_teaching) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_tiem, map.get("month").toString());
                helper.setText(R.id._item_text, map.get("title").toString());
                if (map.get("gps_ids") != null) {
                    helper.setText(R.id._item_id, map.get("gps_ids").toString());//可能没有
                }
            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new casesharing_listListener());
    }


    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {

            Map map = listData.get(arg2);
            Intent intent = null;
            switch (map.get("tag").toString()) {
                //tag值判断
                case "Teacher"://评价带教
                    intent = new Intent(context, The_evaluation_of_teaching_list.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("gps_ids", map.get("gps_ids").toString());
                    break;
                case "Sufferer"://患者
                case "Nurse"://护理
                    intent = new Intent(context, QRCordActivity.class);
                    intent.putExtra("url", map.get("url").toString());
                    break;
                case "Base"://基地
                    intent = new Intent(context, EvaluationBase.class);
                    intent.putExtra("fid", fid);
                    intent.putExtra("id", map.get("id").toString());
                    intent.putExtra("flag","Base");
                    break;
                case "ks"://评价科室
                    intent = new Intent(context, EvaluationBase.class);
                    intent.putExtra("flag","ks");
                    intent.putExtra("year",map.get("year").toString());
                    intent.putExtra("month",map.get("month").toString());
                    break;
            }

            if (intent != null) {
                startActivity(intent);
            }

        }

    }

    private void setSpinner() {
        spinnerEduType.attachDataSource(typeStringList);
        spinnerEduType.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getUrlRulest(typeList.get(position).getType());
            }
        });
    }


    public void getUrlRulest(final String type) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allAppraiseIndex);
                    //type=1--所有评价,type=2--评价带教,type=3--患者评价,type=4--护理评价,type=5--评价基地,type=6--评价科室
                    obj.put("type", type);
                    obj.put("fid", fid);
                    obj.put("new", 1);//新版本标志
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
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

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allAppraiseIndex);
                    //type=1--所有评价,type=2--评价带教,type=3--患者评价,type=4--护理评价,type=5--评价基地
                    obj.put("type", "1");
                    obj.put("fid", fid);
                    obj.put("new", 1);//新版本标志
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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


    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

}
