package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Event_Details_Leave_bean;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.leave_data;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 请假
 */

public class Event_Details_Leave extends BaseActivity{
    private Context context;
    private String fid = "";
    private String id = "";
    private String type = "";
    private String pos = "1";//1未处理 2.已拒绝 3.已成功
    List<Event_Details_Leave_bean> untreated_datas = new ArrayList<>();//未处理 数据集合
    List<Event_Details_Leave_bean> rejected_datas = new ArrayList<>();//已拒绝数据集合
    List<Event_Details_Leave_bean> have_passed_datas = new ArrayList<>();//已通过数据集合
    Event_Details_Leave_bean leave_select =new Event_Details_Leave_bean();//当前审核 学员请假
    @Bind(R.id.leave_grid)
    GridView leave_grid;//请假

    @Bind(R.id.untreated)
    LinearLayout untreated;//未处理
    @Bind(R.id.rejected)
    LinearLayout rejected;//已拒绝
    @Bind(R.id.have_passed)
    LinearLayout have_passed;//已通过

    @Bind(R.id.untreated_text)
    TextView untreated_text;//未处理 文字
    @Bind(R.id.untreated_view)
    View untreated_view;//未签到 下标

    @Bind(R.id.rejected_text)
    TextView rejected_text;//已拒绝 文字
    @Bind(R.id.rejected_view)
    View rejected_view;//已拒绝 下标

    @Bind(R.id.have_passed_text)
    TextView have_passed_text;//已拒绝 文字
    @Bind(R.id.have_passed_view)
    View have_passed_view;//已拒绝 下标

    @Bind(R.id.leave_submit_layout)
    LinearLayout leave_submit_layout;//请假 审核 弹窗 容器
    @Bind(R.id.leave_name)
    TextView leave_name;//请假 审核 弹窗 名字
    @Bind(R.id.leave_msg)
    TextView leave_msg;//请假 审核 弹窗 请假理由
    @Bind(R.id.leave_button_layout)
    LinearLayout leave_button_layout;//请假 审核 弹窗 按钮容器
    @Bind(R.id.leave_close_text)
    TextView leave_close_text;//请假 审核 弹窗 拒绝
    @Bind(R.id.leave_submit_text)
    TextView leave_submit_text;//请假 审核 弹窗 确认
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    private BaseListAdapter baseListAdapterLeaves;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 7:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        MyProgressBarDialogTools.hide();
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                leave_select.setSign( "5");
                                for (int i = 0 ; i<leave_data.size();i++){
                                    if(leave_data.get(i).getUid().equals(leave_select.getUid())){
                                        leave_data.set(i,leave_select);
                                    }
                                }
                                untreated_datas.remove(leave_select);
                                rejected_datas.add(leave_select);
                                baseListAdapterLeaves.notifyDataSetChanged();
                                leave_submit_layout.setVisibility(View.GONE);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            setResultStatus(untreated_datas.size() > 0, 200);

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        MyProgressBarDialogTools.hide();
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                leave_select.setSign( "3");
                                for (int i = 0 ; i<leave_data.size();i++){
                                    if(leave_data.get(i).getUid().equals(leave_select.getUid())){
                                        leave_data.set(i,leave_select);
                                    }
                                }
                                leave_data.set(leave_data.indexOf(leave_select),leave_select);
                                untreated_datas.remove(leave_select);
                                have_passed_datas.add(leave_select);
                                baseListAdapterLeaves.notifyDataSetChanged();
                                leave_submit_layout.setVisibility(View.GONE);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                            setResultStatus(untreated_datas.size() > 0, 200);
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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
            tranining2_nodata.setVisibility(View.GONE);
            leave_grid.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }

            leave_grid.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details_leave);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");


            List<Event_Details_Leave_bean> rs = (List<Event_Details_Leave_bean>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
            for (Event_Details_Leave_bean event_details_leave_bean : rs) {
               switch (event_details_leave_bean.getSign()){
                   case "3":
                       have_passed_datas.add(event_details_leave_bean);
                       break;
                   case "4":
                       untreated_datas.add(event_details_leave_bean);
                       break;
                   case "5":
                       rejected_datas.add(event_details_leave_bean);
                       break;

                   default:

                       break;
               }
             }


        //点击未处理
        untreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换列表数据  type  1代表可编辑  非1不可编辑  使用的集合所属页面不同    一、为1时 使用的集合为发布页面的集合 为2时 使用的自身的数据  二、已签到不需要判断是因为 发布页面不可能有已签到的
                baseListAdapterLeaves.refresh(untreated_datas);
                //更换按钮颜色
                untreated_text.setTextColor(Color.parseColor("#4492da"));
                untreated_view.setVisibility(View.VISIBLE);
                rejected_text.setTextColor(Color.parseColor("#333333"));
                rejected_view.setVisibility(View.INVISIBLE);
                have_passed_text.setTextColor(Color.parseColor("#333333"));
                have_passed_view.setVisibility(View.INVISIBLE);
                pos = "1";
                setResultStatus(untreated_datas.size() > 0, 200);
            }
        });
        //点击已拒绝
        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换列表数据
                baseListAdapterLeaves.refresh(rejected_datas);
                //更换按钮颜色
                untreated_text.setTextColor(Color.parseColor("#333333"));
                untreated_view.setVisibility(View.INVISIBLE);
                rejected_text.setTextColor(Color.parseColor("#4492da"));
                rejected_view.setVisibility(View.VISIBLE);
                have_passed_text.setTextColor(Color.parseColor("#333333"));
                have_passed_view.setVisibility(View.INVISIBLE);
                pos = "2";
                setResultStatus(rejected_datas.size() > 0, 200);
            }
        });
        //点击已通过
        have_passed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换列表数据
                baseListAdapterLeaves.refresh(have_passed_datas);
                //更换按钮颜色
                untreated_text.setTextColor(Color.parseColor("#333333"));
                untreated_view.setVisibility(View.INVISIBLE);
                rejected_text.setTextColor(Color.parseColor("#333333"));
                rejected_view.setVisibility(View.INVISIBLE);
                have_passed_text.setTextColor(Color.parseColor("#4492da"));
                have_passed_view.setVisibility(View.VISIBLE);
                pos = "3";
                setResultStatus(have_passed_datas.size() > 0, 200);
            }
        });

        baseListAdapterLeaves = new BaseListAdapter(leave_grid, untreated_datas, R.layout.item_teaching_activities_leave) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Event_Details_Leave_bean map = (Event_Details_Leave_bean) item;
                helper.setText(R.id._item_text, map.getRealname().toString());
            }
        };
        leave_grid.setAdapter(baseListAdapterLeaves);
        leave_grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        leave_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long item_id) {
                Event_Details_Leave_bean map = null;
                switch (pos){
                    case "1":
                        map =  untreated_datas.get(position);
                        leave_select =  untreated_datas.get(position);
                        break;
                    case "2":
                        map =  rejected_datas.get(position);
                        leave_select =  rejected_datas.get(position);
                        break;
                    case "3":
                        map =  have_passed_datas.get(position);
                        leave_select =  have_passed_datas.get(position);
                        break;

                    default:
                        return;
                }

                leave_submit_layout.setVisibility(View.VISIBLE);
                leave_name.setText(map.getRealname() );
                leave_msg.setText(Html.fromHtml("<font color=#333333>事由：</font>"+map.getLeave_msg() ));
                if(type.equals("1")){
                    if(map.getSign().equals("3") || map.getSign().equals("5")){
                        ///已经审核过 所以讲按钮隐藏
                        leave_button_layout.setVisibility(View.GONE);
                    }else{
                        leave_button_layout.setVisibility(View.VISIBLE);
                    }
                }else{
                    leave_button_layout.setVisibility(View.GONE);
                }

            }
        });

        leave_submit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_submit_layout.setVisibility(View.GONE);
            }
        });

        leave_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressBarDialogTools.show(context);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.activitiesSubmitLeave);
                            obj.put("user_id", leave_select.getUid());
                            obj.put("id", id);
                            obj.put("status", 2);
                            obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                            LogUtil.e("发布人 操作 学员的请假", result);
                            Message message = new Message();
                            message.what = 7;
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
        leave_submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressBarDialogTools.show(context);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.activitiesSubmitLeave);
                            obj.put("user_id", leave_select.getUid());
                            obj.put("id", id);
                            obj.put("status", 1);
                            obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                            LogUtil.e("发布人 操作 学员的请假", result);
                            Message message = new Message();
                            message.what = 8;
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
        setResultStatus(untreated_datas.size() > 0, 200);
    }




}
