package com.linlic.ccmtv.yx.activity.home.willowcup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.adapter.ChooseTypeAdapter;

import java.util.List;

/**
 * name：选择展播类型
 * author：Larry
 * data：2017/3/28.
 */
public class ShowTypeActivity extends BaseActivity {
    Context context;
    private GridView gv_type;
    private ChooseTypeAdapter adapter;
    private List<String> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtype);
        context = this;
        findId();

        initData();

        setAdapter();

        setOnClick();
    }


    public void findId() {
        super.findId();
        super.setActivity_title_name("请选择您要查找的类型");
        gv_type = (GridView) findViewById(R.id.gv_type);
    }

    private void initData() {
        types = getIntent().getStringArrayListExtra("types");
    }

    private void setAdapter() {
        adapter = new ChooseTypeAdapter(this, types);
        gv_type.setAdapter(adapter);
    }

    private void setOnClick() {
        gv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent();
                mIntent.putExtra("type", types.get(position));
                // 设置结果，并进行传送
                setResult(RESULT_OK, mIntent);
                ShowTypeActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra("type", "全部类型");
        // 设置结果，并进行传送
        setResult(RESULT_OK, mIntent);
        ShowTypeActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public void back(View view) {
        Intent mIntent = new Intent();
        mIntent.putExtra("type", "全部类型");
        // 设置结果，并进行传送
        setResult(RESULT_OK, mIntent);
        ShowTypeActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}
