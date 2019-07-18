package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RelatedRecommendActivity extends BaseActivity {

    @Bind(R.id.id_iv_activity_title_8)
    ImageView idIvActivityTitle8;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.my_recyclerview)
    RecyclerView myRecyclerview;
    private BaseRecyclerViewAdapter adapter;
    private List<ListInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_recommend);
        ButterKnife.bind(this);
        initdata();
    }

    private void initdata(){
        for(int i=0;i<12;i++){
            ListInfo info = new ListInfo();
            info.setName("良性前列腺增生治疗的现状与与思考");
            list.add(info);
        }

        adapter = new BaseRecyclerViewAdapter(R.layout.adapter_relatedrecommend,list){
            @Override
            public void convert(BaseViewHolder helper, Object item) {
                super.convert(helper, item);
                ListInfo listInfo=(ListInfo)item;
                helper.setText(R.id.tv_name,listInfo.getName());
            }
        };
        myRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerview.setAdapter(adapter);

    }

    @OnClick(R.id.arrow_back)
    public void onViewClicked() {
        finish();
    }

    class ListInfo implements Serializable {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
