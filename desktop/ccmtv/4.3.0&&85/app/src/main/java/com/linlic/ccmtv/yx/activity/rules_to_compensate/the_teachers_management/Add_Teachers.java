package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_teachers_management;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/**新增带教
 * Created by Administrator on 2018/6/25.
 */

public class Add_Teachers  extends BaseActivity{
    private Context context;

    @Bind(R.id.student_list)
    ListView student_list;
    @Bind(R.id.submit)
    LinearLayout submit;

    @Bind(R.id.management_nodata)
    NodataEmptyLayout management_nodata;
    //跳转发过来的所选中的数据
    private List<String> select_list = new ArrayList<>();
    //本页面员工的数据 及位置    根据  select_list 传输过来的 数据更改初始选中的成员
    private Map<String,Resident> select_Resident= new  HashMap<>();
    private Map<String,Object> select_map= new  HashMap<>();
    private List<Resident> listData = new ArrayList<Resident>();
    private BaseListAdapter baseListAdapterVideo;
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
                                JSONArray dateJson = dataJson.getJSONArray("data");

                                    listData.clear();
                                    select_map.clear();

                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataJson1.getString("manage_id"));
                                    resident.setKsname(dataJson1.getString("keshiname"));
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
                                Toast.makeText(context, dataJson.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, UNKONW_EXCEPTION_CODE);
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        MyProgressBarDialogTools.hide();
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("msg"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(context, dataJson.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }else{
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
        setContentView(R.layout.add_teachers);
        context = this;
        ButterKnife.bind(this);
        findId();
        initViews();
        getBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Faculty/index.html";
        super.onPause();
    }


    private void initViews() {
        List<Resident> rs =   (List<Resident>) getIntent().getSerializableExtra("lecturer_data");//获取list方式
        for (Resident resident : rs) {
            intent_select_list.add(resident);
            select_Resident.put(resident.getId(),resident);
        }


        baseListAdapterVideo = new BaseListAdapter(student_list, listData, R.layout.item_add_teachers) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;

                helper.setText(R.id._item_name,map.getName());
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




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyProgressBarDialogTools.show(context);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.addManageApi);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            String manage_id = "";
                            Collection<Resident> collection = select_Resident.values();
                            Iterator<Resident> iterator = collection.iterator();
                            while (iterator.hasNext()){
                                Resident resident = iterator.next();
                                if(resident.is_select()){
                                    if(manage_id.length()>0){
                                        manage_id += ","+ resident.getId();
                                    }else{
                                        manage_id = resident.getId();
                                    }
                                }
                            }
                            obj.put("manage_id",manage_id);
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



    public void getBase() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.manageListApi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));

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


}
