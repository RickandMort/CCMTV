package com.linlic.ccmtv.yx.activity.tutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Tutor_students_bean;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity4;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/5/7.
 */

public class Tutor_students extends BaseActivity {
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    @Bind(R.id.recyclelistview)
    RecyclerView recyclelistview;
    private Context context;
    private List<Tutor_students_bean> listData = new ArrayList<>();
    BaseRecyclerViewAdapter baseRecyclerViewAdapter ;

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
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Tutor_students_bean bean = new Tutor_students_bean();
                                    bean.setCurr_pos(listData.size());
                                    bean.setUid(dataJson1.getString("uid"));
                                    bean.setBasename(dataJson1.getString("basename"));
                                    bean.setKsname(dataJson1.getString("ksname"));
                                    bean.setLs_enrollment_year(dataJson1.getString("ls_enrollment_year"));
                                    bean.setMobphone(dataJson1.getString("mobphone"));
                                    bean.setPhoto(dataJson1.getString("photo"));
                                    bean.setRealname(dataJson1.getString("realname"));
                                    bean.setSex(dataJson1.getString("sex"));
                                    bean.setTeacher(dataJson1.getString("teacher"));
                                    bean.setUsername(dataJson1.getString("username"));
                                    bean.setStudent_id(dataJson1.getString("student_id"));
                                    listData.add(bean);
                                }
                                baseRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(listData.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    } finally {
//                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(listData.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;

                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recyclelistview.setVisibility(View.VISIBLE);
            tranining2_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }
            recyclelistview.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tutor_students);
        context = this;
        ButterKnife.bind(this);
        activityTitleName.setText(getIntent().getStringExtra("title"));

        initview();
        setVideos2();
    }


    public void initview(){
        baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(R.layout.item_tutor_students,listData){
            @Override
            public void convert(BaseViewHolder helper, Object item) {
                Tutor_students_bean bean = (Tutor_students_bean) item;
                helper.setText(R.id.item_name,bean.getRealname()+"("+bean.getUsername()+")  ");
                helper.setText(R.id.item_basename,"基地："+bean.getBasename() );
                helper.setText(R.id.item_mobphone,"手机号："+bean.getMobphone() );
                helper.setText(R.id.item_ksname,"当前科室："+bean.getKsname() );
                helper.setText(R.id.item_ls_enrollment_year,"招收年度："+bean.getLs_enrollment_year() );
                helper.setText(R.id.item_teacher,"带教老师："+bean.getTeacher() );
                if(bean.getSex().equals("0")){
                    helper.setGone(R.id.item_sex,false);
                }else if(bean.getSex().equals("1")){
                    helper.setImageResource(R.id.item_sex,R.mipmap.sex_icon02);
                }else if(bean.getSex().equals("2")){
                    helper.setImageResource(R.id.item_sex,R.mipmap.sex_icon01);
                }else{
                    helper.setGone(R.id.item_sex,false);
                }
                CircleImageView view = helper.getView(R.id.my_myhead);
                Picasso.with(context).load(bean.getPhoto()).error(R.mipmap.img_default).
                        placeholder(R.mipmap.img_default).into(view);
            }
        };
        baseRecyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclelistview.setAdapter(baseRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//设置布局管理器
        recyclelistview.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        baseRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context,GpPersonalInformationActivity4.class);
                intent.putExtra("id",listData.get(position).getStudent_id());
                startActivity(intent);
            }
        });
    }
    public void setVideos2() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.TutorGetUserList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP_GpApi, obj.toString());
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
