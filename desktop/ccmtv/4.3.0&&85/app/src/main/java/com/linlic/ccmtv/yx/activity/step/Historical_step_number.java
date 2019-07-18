package com.linlic.ccmtv.yx.activity.step;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.StepData;
import com.linlic.ccmtv.yx.activity.step.config.Constant;
import com.linlic.ccmtv.yx.activity.step.utils.DbUtils;
import com.linlic.ccmtv.yx.adapter.Historical_step_numberAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7.
 */

public class Historical_step_number extends BaseActivity {
    private Context context;
    private Historical_step_numberAdapter historical_step_numberAdapter;
    private RecyclerView recycler_view_test_rv;
    private List<StepData> data = new ArrayList<StepData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.historical_step_number);
        context = this;
        findId();
        init();
    }

    @Override
    public void findId() {
        super.findId();
        recycler_view_test_rv = (RecyclerView) findViewById(R.id.recycler_view_test_rv);
    }

    public void init(){
        //在创建方法中有判断，如果数据库已经创建了不会二次创建的
        DbUtils.createDb(this, Constant.DB_NAME);
        data = DbUtils.getQueryAll2(StepData.class);
        recycler_view_test_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_view_test_rv.setLayoutManager(layoutManager);
        historical_step_numberAdapter = new Historical_step_numberAdapter(data);
        recycler_view_test_rv.setAdapter(historical_step_numberAdapter);
        historical_step_numberAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}
