package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

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
import android.widget.TextView;

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

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.teaching_data;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 带教
 */

public class Event_Details_Teaching extends BaseActivity{
    private Context context;
    private String fid = "";
    private String id = "";
    private String type = "";
    boolean check_in_bool = false;
    List<Resident> lecturer_data2 = new ArrayList<>();//带教 数据
    @Bind(R.id.selectParticipant)
    LinearLayout selectParticipant;//选择参与人员
    @Bind(R.id.lecturers)
    GridView lecturers;//带教
    @Bind(R.id.tranining2_nodata)
    NodataEmptyLayout tranining2_nodata;
    @Bind(R.id.activity_title_name)
    TextView activity_title_name;
    private BaseListAdapter baseListAdapterLecturers;


    private void setResultStatus(boolean status, int code) {
        if (status) {
            tranining2_nodata.setVisibility(View.GONE);
            lecturers.setVisibility(View.VISIBLE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                tranining2_nodata.setNetErrorIcon();
            } else {
                tranining2_nodata.setLastEmptyIcon();
            }

            lecturers.setVisibility(View.GONE);
            tranining2_nodata.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details_lecturer);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        activity_title_name.setText("带教");
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
            for (Resident resident : rs) {
                    lecturer_data2.add(resident);
            }
        }

        selectParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Selectlecturer();
            }
        });

        setResultStatus((type.equals("1")?teaching_data.size():lecturer_data2.size()) > 0, 200);

        baseListAdapterLecturers = new BaseListAdapter(lecturers, type.equals("1")?teaching_data:lecturer_data2, R.layout.item_lecturers) {

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
                    helper.setVisibility(R.id._item_img, View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_img, View.GONE);
                }
            }
        };
        lecturers.setAdapter(baseListAdapterLecturers);
        lecturers.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    public void Selectlecturer(){
        Intent intent = new Intent(context, Select_teaching.class);
        intent.putExtra("fid", fid);
        Bundle bundle=new Bundle();
        bundle.putSerializable("teaching_data", (Serializable)teaching_data);//序列化
        intent.putExtras(bundle);//发送数据
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            LogUtil.e("数据", "requestCode:" + requestCode + "   resultCode:" + resultCode + "   data:" + data.toString());
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case 3:
                        teaching_data.clear();
                        List<Resident> le = (List<Resident>) data.getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
                        for (Resident resident : le) {
                            teaching_data.add(resident);
                        }

                            baseListAdapterLecturers.refresh(teaching_data);
                        setResultStatus(teaching_data.size() > 0, 200);
                        break;
                    default:
                        break;
                }

            }
        }

    }
    public void deleteresidents(View view) {
        for (int i = 0; i < teaching_data.size(); i++) {
            Resident resident1 = teaching_data.get(i);
            if (resident1.getId().equals(view.getTag().toString())) {
                teaching_data.remove(resident1);
                baseListAdapterLecturers.notifyDataSetChanged();
            }
        }
        setResultStatus((type.equals("1")?teaching_data.size():lecturer_data2.size()) > 0, 200);
    }

}
