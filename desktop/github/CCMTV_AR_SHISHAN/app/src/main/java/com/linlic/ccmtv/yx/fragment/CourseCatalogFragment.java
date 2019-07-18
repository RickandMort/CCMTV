package com.linlic.ccmtv.yx.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.CourseCatalogBean;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.ItemInfo;
import com.linlic.ccmtv.yx.adapter.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CourseCatalogFragment extends Fragment {

    @Bind(R.id.my_recyclerview)
    RecyclerView myRecyclerview;
    private SectionAdapter adapter;
    private List<CourseCatalogBean> listInfos = new ArrayList<>();
    private List<ItemInfo> infos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_catalog, container, false);
        ButterKnife.bind(this, view);
        initdata();
        return view;
    }

    private void initdata(){
        for(int i=0;i<6;i++){
//            CourseCatalogBean listInfo=new CourseCatalogBean();
//            listInfo.setName("第二章：手术操作（共10节）");
//            for(int j=0;j<3;j++){
//                ItemInfo info = new ItemInfo();
//                info.setTitle("彭波：前列腺纽扣电极剜除术 ");
//                info.setNum("第1节");
//                infos.add(info);
//            }
//            listInfo.set
//            listInfos.add(listInfo);
            listInfos.add(new CourseCatalogBean(true,"第二章：手术操作（共10节）"));
            listInfos.add(new CourseCatalogBean(new ItemInfo("第1节","彭波：前列腺纽扣电极剜除术")));
        }
        adapter= new SectionAdapter(R.layout.adapter_item,R.layout.adapter_header,listInfos){
            @Override
            protected void convertHead(BaseViewHolder helper, CourseCatalogBean item) {
                super.convertHead(helper, item);
                helper.setText(R.id.tv_title, item.header);
            }

            @Override
            protected void convert(BaseViewHolder helper, CourseCatalogBean item) {
                super.convert(helper, item);
                ItemInfo homeListBean = item.t;
                helper.setText(R.id.tv_jie_order,homeListBean.getNum());
                helper.setText(R.id.tv_name,homeListBean.getTitle());
            }
        };
        myRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerview.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
