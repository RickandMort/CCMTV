package com.linlic.ccmtv.yx.activity.medical_database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.kzbf.activity.BigImageActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/12/5.
 */

public class Ppt_lock extends BaseActivity {
    private Context context;
    private BaseListAdapter baseListAdapter;

    @Bind(R.id.list_view)
    ListView list_view;

    List<String> url_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ppt_lock);
        context = this;
        ButterKnife.bind(this);
        url_list = getIntent().getStringArrayListExtra("url_list");
        findId();
        initView();
    }

    public void initView( ){
        baseListAdapter = new BaseListAdapter(list_view, url_list, R.layout.item_ppt_lock) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String str = (String) item;
                    helper.setImageBitmapGlide(context, R.id._image, str);

            }
        };

        list_view.setAdapter(baseListAdapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conferenceId = url_list.get(position);
                Intent intent = new Intent();
                intent.putExtra("image", conferenceId);
                intent.setClass(context, BigImageActivity.class);//BigImageActivity查看大图的类
                startActivity(intent);
            }
        });
    }



    @Override
    public void finish() {
        super.finish();
    }

}
