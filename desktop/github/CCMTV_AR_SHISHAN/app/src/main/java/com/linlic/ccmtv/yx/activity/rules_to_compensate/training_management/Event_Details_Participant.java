package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.residents;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 参与人员
 */

public class Event_Details_Participant extends BaseActivity{
    private Context context;
    private String fid = "";
    private String id = "";
    private String type = "";
    boolean check_in_bool = false;
    List<Resident> no_sign_in_datas = new ArrayList<>();//未签到 数据集合
    List<Resident> check_in_datas = new ArrayList<>();//已签到数据集合
    @Bind(R.id.selectParticipant)
    LinearLayout selectParticipant;//选择参与人员
    @Bind(R.id.participants)
    GridView participants;//参与人员
    @Bind(R.id.no_sign_in)
    LinearLayout no_sign_in;//未签到
    @Bind(R.id.check_in)
    LinearLayout check_in;//已签到
    @Bind(R.id.no_sign_in_text)
    TextView no_sign_in_text;//未签到 文字
    @Bind(R.id.no_sign_in_view)
    View no_sign_in_view;//未签到 下标
    @Bind(R.id.check_in_text)
    TextView check_in_text;//已签到 文字
    @Bind(R.id.check_in_view)
    View check_in_view;//已签到 下标
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;

    private BaseListAdapter baseListAdapterParticipants;
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
        setContentView(R.layout.event_details_participant);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if(type.equals("1")){
            //可编辑
            selectParticipant.setVisibility(View.VISIBLE);
        }else{
            //不可编辑
            selectParticipant.setVisibility(View.GONE);

            List<Resident> rs = (List<Resident>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
            for (Resident resident : rs) {
                if(resident.getSign().equals("未签到")){
                    no_sign_in_datas.add(resident);
                } else{
                    check_in_datas.add(resident);
                }
            }
        }

        setResultStatus((type.equals("1")?residents.size():no_sign_in_datas.size()) > 0, 200);

        selectParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectParticipant();
            }
        });

        //点击未签到
        no_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换列表数据  type  1代表可编辑  非1不可编辑  使用的集合所属页面不同    一、为1时 使用的集合为发布页面的集合 为2时 使用的自身的数据  二、已签到不需要判断是因为 发布页面不可能有已签到的
                baseListAdapterParticipants.refresh(type.equals("1")?residents:no_sign_in_datas);
                //更换按钮颜色
                no_sign_in_text.setTextColor(Color.parseColor("#4492da"));
                no_sign_in_view.setVisibility(View.VISIBLE);
                check_in_text.setTextColor(Color.parseColor("#333333"));
                check_in_view.setVisibility(View.INVISIBLE);
                check_in_bool = false;
                setResultStatus((type.equals("1")?residents.size():no_sign_in_datas.size()) > 0, 200);
            }
        });
        //点击已签到
        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更换列表数据
                baseListAdapterParticipants.refresh(check_in_datas);
                //更换按钮颜色
                no_sign_in_text.setTextColor(Color.parseColor("#333333"));
                no_sign_in_view.setVisibility(View.INVISIBLE);
                check_in_text.setTextColor(Color.parseColor("#4492da"));
                check_in_view.setVisibility(View.VISIBLE);
                check_in_bool = true;
                setResultStatus(check_in_datas.size() > 0, 200);
            }
        });

        baseListAdapterParticipants = new BaseListAdapter(participants, type.equals("1")?residents:no_sign_in_datas, R.layout.item_participants) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setText(R.id._item_text, map.getName());
                helper.setTag(R.id._item_img, map.getId());
                if(type.equals("1")){
                    helper.setVisibility(R.id._item_img,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_img,View.GONE);
                }
                if(map.getIs_temp().equals("2")){
                    helper.setVisibility(R.id._item_evaluate,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_evaluate,View.GONE);
                }

            }
        };
        participants.setAdapter(baseListAdapterParticipants);
        participants.setSelector(new ColorDrawable(Color.TRANSPARENT));
        participants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long item_id) {
                if(!type.equals("1") && check_in_bool){
                    Resident resident =  check_in_datas.get(position);
                    if(resident.getIs_temp().equals("2")){
                        Intent intent = new Intent(context,Training_Evaluation_in_detail.class);
                        intent.putExtra("detailid",id);
                        intent.putExtra("is_teaching","1");
                        intent.putExtra("s_uid",resident.getId());
                        startActivity(intent);
                    }else{
                        Toast.makeText(context, "该学员未评价！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    public void SelectParticipant(){
        Intent intent = new Intent(context, Select_staff.class);
        intent.putExtra("fid", fid);
        Bundle bundle=new Bundle();
        bundle.putSerializable("select_list", (Serializable)residents);//序列化
        intent.putExtras(bundle);//发送数据
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 2:
                        residents.clear();
                        List<Resident> rs = (List<Resident>) data.getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
                        for (Resident resident : rs) {
                            residents.add(resident);
                        }
                        baseListAdapterParticipants.notifyDataSetChanged();
                        setResultStatus((type.equals("1")?residents.size():no_sign_in_datas.size()) > 0, 200);
                        break;
                    default:
                        break;
                }

            }
        }

    }
    public void deleteresidents(View view) {

        for (int i = 0; i < residents.size(); i++) {
            Resident resident1 = residents.get(i);
            if (resident1.getId().equals(view.getTag().toString())) {
                residents.remove(resident1);
                baseListAdapterParticipants.notifyDataSetChanged();

            }
        }
        setResultStatus((type.equals("1")?residents.size():no_sign_in_datas.size()) > 0, 200);
    }

}
