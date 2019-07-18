package com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.utils.HttpClientUtils.UNKONW_EXCEPTION_CODE;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 参与人员
 */

public class College_level_activities_Participant extends BaseActivity{
    private Context context;
    private String id = "";
    List<Resident> no_sign_in_datas = new ArrayList<>();//未签到 数据集合
    @Bind(R.id.participants)
    GridView participants;//参与人员

    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;

    private BaseListAdapter baseListAdapterParticipants;

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

                                for (int i = 0; i<dateJson.length();i++){
                                   JSONObject resident_json =  dateJson.getJSONObject(i);
                                    Resident resident = new Resident();
                                    resident.setId(resident_json.getString("uid"));
                                    resident.setUsername(resident_json.getString("username"));
                                    resident.setName(resident_json.getString("realname"));
                                    resident.setSign(resident_json.getString("sign"));
                                    resident.setIs_select(resident_json.getString("out").equals("1")?true:false);
                                    resident.setImgUrl(resident_json.getString("IDphoto"));
                                    no_sign_in_datas.add(resident);
                                }
                                setResultStatus( no_sign_in_datas.size() > 0, 200);
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                setResultStatus( no_sign_in_datas.size() > 0, 200);
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(no_sign_in_datas.size() > 0, jsonObject.getInt("code"));
                        baseListAdapterParticipants.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(no_sign_in_datas.size() > 0, UNKONW_EXCEPTION_CODE);
                    } finally {
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
            tranining2_nodata.setVisibility(View.GONE);
            participants.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }

            participants.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.college_level_activities_participant);
        context = this;
        ButterKnife.bind(this);
        initView();
        getUrlRulest();

    }

    public void initView(){
        id = getIntent().getStringExtra("id");



       /*     List<Resident> rs = (List<Resident>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
            for (Resident resident : rs) {
                if(resident.getSign().equals("未签到")){
                    no_sign_in_datas.add(resident);
                } else{
                    check_in_datas.add(resident);
                }
            } */

//        setResultStatus((type.equals("1")?residents.size():no_sign_in_datas.size()) > 0, 200);





        baseListAdapterParticipants = new BaseListAdapter(participants,  no_sign_in_datas, R.layout.item_participants3) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setImageBitmap(R.id._item_img,map.getImgUrl());
                helper.setText(R.id.item_name, map.getName()+"("+map.getUsername()+")");
                if(map.getSign().equals("1")){
                    helper.setImage(R.id.item_icon1, R.mipmap.delete_item_select2);
                }else{
                    helper.setImage(R.id.item_icon1, R.mipmap.training_10);
                }
                if(map.is_select()){
                    helper.setImage(R.id.item_icon2, R.mipmap.delete_item_select2);
                }else{
                    helper.setImage(R.id.item_icon2, R.mipmap.training_10);
                }

            }
        };
        participants.setAdapter(baseListAdapterParticipants);
        participants.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }
    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitysUserSignList);
                    obj.put("id", id);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("活动详细签到情况", result);
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
