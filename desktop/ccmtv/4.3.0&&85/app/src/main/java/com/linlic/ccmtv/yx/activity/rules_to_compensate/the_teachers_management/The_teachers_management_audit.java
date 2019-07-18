package com.linlic.ccmtv.yx.activity.rules_to_compensate.the_teachers_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity2;
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
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**师资管理 审核列表
 * Created by Administrator on 2018/6/25.
 */

public class The_teachers_management_audit extends BaseActivity {

    private Context context;
   @Bind(R.id.periodical_nodata)
   NodataEmptyLayout periodical_nodata;;
    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.submit_o)
    TextView submit_o;
    @Bind(R.id.submit_x)
    TextView submit_x;
    @Bind(R.id.audit_msg)
    TextView audit_msg;
    @Bind(R.id.submit_status)
    TextView submit_status;
    @Bind(R.id.teacherss)
    ListView teacherss;//
    @Bind(R.id.data_layout)
    LinearLayout data_layout;//
    @Bind(R.id.submit_layout)//提交审核 退回原因 容器
    LinearLayout submit_layout;//
    @Bind(R.id.submit_layout2)//提交审核 退回原因 容器
    LinearLayout submit_layout2;//
    @Bind(R.id.submit_layout3_text)
    EditText submit_layout3_text;//
    @Bind(R.id.submit_text)
    TextView submit_text;//
    @Bind(R.id.close_text)
    TextView close_text;//
    @Bind(R.id.submit_layout3)
    LinearLayout submit_layout3;//
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
    private String id = "";//教学活动ID
    private String w_id = "";//审核活动ID
    private String fid = "";//
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {

                                //赋值 教秘所在科室
                                department.setText( data.getString("ksname"));

                                    //判断审核状态 0:审核中  1：审核通过  2：审核退回
                                    switch (data.getString("chkstatus")){
                                        case "0"://审核中

                                            submit_layout2.setVisibility(View.GONE);
                                            submit_layout.setVisibility(View.VISIBLE);
                                            break;
                                        case "1"://审核通过
                                            submit_status.setText("审核通过");
                                            submit_layout2.setVisibility(View.VISIBLE);
                                            submit_layout.setVisibility(View.GONE);
                                            break;
                                        case "2"://审核退回
                                            submit_status.setText("审核不通过");
                                            audit_msg.setText("退回原因："+data.getString("reason"));
                                            submit_layout2.setVisibility(View.VISIBLE);
                                            submit_layout.setVisibility(View.GONE);
                                            break;
                                    }

                                    listData.clear();
                                JSONArray dataArray = data.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("manage_id", dataObjectDetail.getString("manage_id"));
                                    map.put("username", dataObjectDetail.getString("username"));
                                    map.put("realname", dataObjectDetail.getString("realname"));
                                    map.put("keshiname", dataObjectDetail.getString("keshiname"));
                                    map.put("zil_status", dataObjectDetail.getString("zil_status"));
                                    map.put("zil", dataObjectDetail.getString("zil"));
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();


                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, result.getInt("code"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject Event_json = new JSONObject();
                                Event_json.put(w_id, "1");
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());
                                finish();
                            } else {
                                submit_o.setClickable(true);
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                                JSONObject Event_json = new JSONObject();
                                Event_json.put(w_id, "2");
                                SharedPreferencesTools.saveEvent_details_status(context, Event_json.toString());
                                finish();
                            } else {
                                submit_x.setClickable(true);
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
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
    private void setResultStatus(boolean status, int code) {
        if (status) {
            data_layout.setVisibility(View.VISIBLE);
            periodical_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                periodical_nodata.setNetErrorIcon();
            } else {
                periodical_nodata.setLastEmptyIcon();
            }
            data_layout.setVisibility(View.GONE);
            periodical_nodata.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.the_teachers_management_audit);
        context = this;
        ButterKnife.bind(this);
        findId();
        this.setActivity_title_name("师资管理");
        initViews();
        getfacultyListApi();

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

    @Override
    public void findId() {
        super.findId();

    }



    private void initViews() {
        submit_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.VISIBLE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(submit_layout3_text.getText().toString().trim().length()>0){
                    facultycheckApi(submit_layout3_text.getText().toString().trim());
                }else{
                    Toast.makeText(context, "请填写退回原因", Toast.LENGTH_SHORT).show();
                }
            }
        });
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_layout3.setVisibility(View.GONE);
            }
        });
        submit_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.facultycheckApi);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("id", w_id);
                            obj.put("status", 1);
                            obj.put("reason", "");

                            final String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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
        });
        id = getIntent().getStringExtra("id");
        w_id = getIntent().getStringExtra("w_id");
        fid = getIntent().getStringExtra("fid");
        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(teacherss, listData, R.layout.item_the_teachers_management) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String,Object> map = (Map) item;
                helper.setText(R.id._item_teaching,map.get("realname").toString()+"("+map.get("username").toString()+")","html");
                helper.setText(R.id._item_evaluation,map.get("zil").toString(),"html");

            }
        };
        teacherss.setAdapter(baseListAdapterVideo);
        teacherss.setSelector(new ColorDrawable(Color.TRANSPARENT));
        teacherss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map  = listData.get(position);
                Intent   intent = new Intent(context, GpPersonalInformationActivity2.class);
                intent.putExtra("uid",map.get("manage_id").toString());
                startActivity(intent);
            }
        });
    }

    public void facultycheckApi(final String str ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.facultycheckApi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", w_id);
                    obj.put("status", 2);
                    obj.put("reason", str);

                    final String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    Message message = new Message();
                    message.what = 4;
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
    public void getfacultyListApi( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getfacultyListApi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", w_id);
                    obj.put("primaryId", id);

                    final String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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
