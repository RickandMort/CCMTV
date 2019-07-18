package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Resident;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2019/2/19.
 * 教学活动 随堂笔记 学员列表
 */

public class Event_Details_Accompanying_notes_Students extends BaseActivity{
    private Context context;
    private String http = "";
    private String id = "";
    List<Resident> students_datas = new ArrayList<>();//学生

    @Bind(R.id.students)
    GridView students;//

    private BaseListAdapter baseListAdapterLecturers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details_accompanying_notes_students);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        http = getIntent().getStringExtra("http");
        id = getIntent().getStringExtra("id");
        List<Resident> rs = (List<Resident>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
        for (Resident resident : rs) {
           students_datas.add(resident);
        }

        baseListAdapterLecturers = new BaseListAdapter(students, students_datas, R.layout.item_accompanying_notes) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Resident map = (Resident) item;
                helper.setText(R.id._item_text, map.getName());
                if(map.getFiles_infos().size()>0){
                    helper.setVisibility(R.id._item_evaluate,View.VISIBLE);
                }else{
                    helper.setVisibility(R.id._item_evaluate,View.GONE);
                }

            }
        };
        students.setAdapter(baseListAdapterLecturers);
        students.setSelector(new ColorDrawable(Color.TRANSPARENT));
        students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long item_id) {

                    Resident resident =  students_datas.get(position);
                    if(resident.getFiles_infos().size()>0){
                        Intent intent = new Intent(context,Event_Details_Accompanying_notes.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("http",http);
                        bundle.putString("id",id);
                        bundle.putSerializable("list",(Serializable)resident.getFiles_infos());//序列化,要注意转化(Serializable)
                        intent.putExtras(bundle);//发送数据
                        startActivity(intent);
                    }else{
                        Toast.makeText(context, "该学员未上传随堂笔记！", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }




}
