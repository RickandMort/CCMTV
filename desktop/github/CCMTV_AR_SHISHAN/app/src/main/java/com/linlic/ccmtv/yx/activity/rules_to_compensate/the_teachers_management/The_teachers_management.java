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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**师资管理
 * Created by Administrator on 2018/6/25.
 */

public class The_teachers_management extends BaseActivity {

    private Context context;
   @Bind(R.id.periodical_nodata)
   NodataEmptyLayout periodical_nodata;;
   @Bind(R.id.start_time)
    TextView start_time;
    @Bind(R.id.department)
    TextView department;
    @Bind(R.id.status)
    TextView status;
    @Bind(R.id.submit)
    TextView submit;
    @Bind(R.id.audit_msg)
    TextView audit_msg;
    @Bind(R.id.teacherss)
    ListView teacherss;//
    @Bind(R.id.data_layout)
    LinearLayout data_layout;//
    @Bind(R.id.arrow_add)
    LinearLayout arrow_add;//
    @Bind(R.id.submit_layout)//提交审核 退回原因 容器
    LinearLayout submit_layout;//
    private List<Resident> listData = new ArrayList<Resident>();
    private BaseListAdapter baseListAdapterVideo;
    private String primaryId = "";
    private String wid = "";

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
                                    listData.clear();
                                    start_time.setText("带教申请开放时间："+data.getString("starttime")+"至"+data.getString("endtime"));
                                JSONArray dataArray = data.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(dataObjectDetail.getString("manage_id"));
                                    resident.setKsname(dataObjectDetail.getString("keshiname"));
                                    resident.setSign( dataObjectDetail.getString("zil_status"));
                                    resident.setBase_name(dataObjectDetail.getString("zil"));
                                    resident.setIs_select(true);
                                    resident.setName(dataObjectDetail.getString("realname"));
                                    resident.setUsername(dataObjectDetail.getString("username"));


                                    listData.add(resident);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();


                            } else {
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
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
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                JSONObject centerData = data.getJSONObject("data");
                                //赋值 教秘所在科室
                                department.setText( centerData.getString("keshiname"));
                                wid = centerData.has("wid")?centerData.getString("wid"):"";
                                primaryId = centerData.getString("primaryId");

                                //判断 是否提交  0:已提交  1：未提交
                                if(centerData.getString("tj").equals("0")){
                                    JSONObject lastData = centerData.getJSONObject("data");
                                    //判断审核状态 0:审核中  1：审核通过  2：审核退回
                                    switch (lastData.getString("status")){
                                        case "0"://审核中
                                            status.setText("审核中");
                                            arrow_add.setVisibility(View.GONE);
                                            submit_layout.setVisibility(View.GONE);
                                            break;
                                        case "1"://审核通过
                                            arrow_add.setVisibility(View.GONE);
                                            status.setText("审核通过");
                                            submit_layout.setVisibility(View.GONE);
                                            break;
                                        case "2"://审核退回
                                            arrow_add.setVisibility(View.VISIBLE);
                                            status.setText("审核退回");
                                            submit_layout.setVisibility(View.VISIBLE);
                                            audit_msg.setText("退回原因："+lastData.getJSONObject("message").getString("errormsg"));
                                            break;
                                    }
                                }else{
                                    status.setText("未提交");
                                }



                            } else {
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
                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                Toast.makeText(context, data.getString("msg"), Toast.LENGTH_SHORT).show();
                                djListApi();
                                checkStatusApi();
                            } else {
                                submit.setClickable(true);
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
        setContentView(R.layout.the_teachers_management);
        context = this;
        ButterKnife.bind(this);
        findId();
        this.setActivity_title_name("师资管理");
        initViews();

    }

    @Override
    public void findId() {
        super.findId();
       /* //增加底部
        item_the_teachers_management_bottom = (LinearLayout) View.inflate(this, R.layout.item_the_teachers_management_bottom, null);
        teacherss.addFooterView(item_the_teachers_management_bottom);
        _item_bottom_text = (TextView) item_the_teachers_management_bottom.findViewById(R.id._item_bottom_text);

        _item_bottom_text.setText(Html.fromHtml("<font  color=\"red\">*科室教秘新建帐号属于未激活状态，新增带教老师维护个人信息后有科教秘书提交、科室主任和规陪办管理员进行审核。<br/> &nbsp;&nbsp;&nbsp;&nbsp;带教老师员工编号为“带教老师名字小写字母+数字编号”；初始密码为123456 </font>"));
*/
    }



    private void initViews() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.tjcheckApi);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("wid", wid);
                            obj.put("primaryId", primaryId);
                            String zil_status = "";
                            for (int i = 0 ;i<listData.size();i++){
                                Resident resident  =   listData.get(i);
                                if(zil_status.length()>0){
                                    zil_status += ","+resident.getSign() ;
                                }else {
                                    zil_status =resident.getSign();
                                }
                            }
                            obj.put("zil_status", zil_status);
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
        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(teacherss, listData, R.layout.item_the_teachers_management) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident resident  = (Resident) item;
                helper.setText(R.id._item_teaching,resident.getName()+"("+resident.getUsername()+")","html");
                helper.setText(R.id._item_evaluation,resident.getBase_name(),"html");

            }
        };

        teacherss.setSelector(new ColorDrawable(Color.TRANSPARENT));
        teacherss.setAdapter(baseListAdapterVideo);
        teacherss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Resident resident  = listData.get(position);
                Intent   intent = new Intent(context, GpPersonalInformationActivity2.class);
                intent.putExtra("uid",resident.getId());
                startActivity(intent);
            }
        });
    }

    public void djListApi( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.djListApi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
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
    public void checkStatusApi( ) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.checkStatusApi);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    final String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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
    public void add_teachers(View view){
     //增加带教
        Intent intent =new Intent(context,Add_Teachers.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("lecturer_data", (Serializable)listData);//序列化
        intent.putExtras(bundle);//发送数据
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        djListApi();
        checkStatusApi();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Faculty/index.html";
        super.onPause();
    }
}
