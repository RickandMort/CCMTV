package com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities.New_College_level_activities.residents_college;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 参与人员
 */

public class College_level_activities_Participant2 extends BaseActivity{
    private Context context;
    private String id = "";

    @Bind(R.id.participants)
    GridView participants;//参与人员
    @Bind(R.id.selectParticipant)
    LinearLayout selectParticipant;//参与人员

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
        setContentView(R.layout.college_level_activities_participant2);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        id = getIntent().getStringExtra("id");


        setResultStatus( residents_college.size()  > 0, 200);

        selectParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, Select_staff_College_level_activities.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("select_list", (Serializable)residents_college);//序列化
                    intent.putExtras(bundle);//发送数据
                    startActivityForResult(intent, 2);
            }
        });



        baseListAdapterParticipants = new BaseListAdapter(participants,  residents_college, R.layout.item_participants4) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setTag(R.id._item_img, map.getId());
                helper.setText(R.id._item_text, map.getName()+"("+map.getUsername()+")");

            }
        };
        participants.setAdapter(baseListAdapterParticipants);
        participants.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 2:
                        residents_college.clear();
                        List<Resident> rs = (List<Resident>) data.getSerializableExtra("list");//获取list方式
                    LogUtil.e("返回的数据",rs.toString());
                        for (Resident resident : rs) {
                            LogUtil.e("返回的数据",resident.toString());
                            residents_college.add(resident);
                        }
                        LogUtil.e("返回的数据",residents_college.toString());
                        baseListAdapterParticipants.notifyDataSetChanged();
                        setResultStatus( residents_college.size()  > 0, 200);
                        break;
                    default:
                        break;
                }

            }
        }

    }
    public void deleteresidents(View view) {

        for (int i = 0; i < residents_college.size(); i++) {
            Resident resident1 = residents_college.get(i);
            if (resident1.getId().equals(view.getTag().toString())) {
                residents_college.remove(resident1);
                baseListAdapterParticipants.notifyDataSetChanged();

            }
        }
        setResultStatus( residents_college.size()  > 0, 200);
    }

}
