package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.kzbf.adapter.MyDownloadAdapter;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.util.ArrayList;
import java.util.List;

//我的下载
//item_my_download
public class MyDownloadActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener {
    Context context;
    private MyDownloadAdapter adapter, adapter2;
    private OkDownload okDownload;
    private RecyclerView recyclerView, recyclerView2;
    private RelativeLayout rl_head, rl_head2;
    private NodataEmptyLayout rl_download_nodata;
    private List<DownloadTask> values;
    private List<DownloadTask> videoValues = new ArrayList<>();
    private List<DownloadTask> values1;
    private List<DownloadTask> videoValues1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download);
        context = this;

        initView();
        initData();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.my_download_list);
        recyclerView2 = (RecyclerView) findViewById(R.id.my_download_list2);
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        rl_head2 = (RelativeLayout) findViewById(R.id.rl_head2);
        rl_download_nodata = (NodataEmptyLayout) findViewById(R.id.rl_download_nodata);
    }

    private void initData() {
        getDownloadingList();
        getFinishList();

        isShowNoData();

        if (videoValues.size() == 0) {
            rl_head.setVisibility(View.GONE);
        } else {
            rl_head.setVisibility(View.VISIBLE);
        }

        if (videoValues1.size() == 0) {
            rl_head2.setVisibility(View.GONE);
        } else {
            rl_head2.setVisibility(View.VISIBLE);
        }

        okDownload = OkDownload.getInstance();

        adapter = new MyDownloadAdapter(context);
        adapter.updateData(MyDownloadAdapter.TYPE_ING);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.OnAddItemClickListener(new MyDownloadAdapter.OnItemClickListener() {
            @Override
            public void plusNum() {
                adapter2.updateData(MyDownloadAdapter.TYPE_FINISH);
                IsShowHead();
            }

            @Override
            public void taskRemove() {
                IsShowHead();
            }
        });

        adapter2 = new MyDownloadAdapter(context);
        adapter2.updateData(MyDownloadAdapter.TYPE_FINISH);
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        recyclerView2.setAdapter(adapter2);

        adapter2.OnAddItemClickListener(new MyDownloadAdapter.OnItemClickListener() {
            @Override
            public void plusNum() {
                IsShowHead();
            }

            @Override
            public void taskRemove() {
                IsShowHead();
            }
        });

        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        okDownload.addOnAllTaskEndListener(this);
    }

    private void isShowNoData() {
        if (videoValues.size() == 0 && videoValues1.size() == 0) {
            rl_head.setVisibility(View.GONE);
            rl_head2.setVisibility(View.GONE);
            rl_download_nodata.setVisibility(View.VISIBLE);
        } else {
            rl_download_nodata.setVisibility(View.GONE);
        }
    }

    private void IsShowHead() {
        getDownloadingList();
        videoValues = changeValues(values);
        if (videoValues.size() == 0) {
            rl_head.setVisibility(View.GONE);
        } else {
            rl_head.setVisibility(View.VISIBLE);
        }
        getFinishList();
        if (videoValues1.size() == 0) {
            rl_head2.setVisibility(View.GONE);
        } else {
            rl_head2.setVisibility(View.VISIBLE);
        }
        if (videoValues.size() == 0 && videoValues1.size() == 0) {
            rl_download_nodata.setVisibility(View.VISIBLE);
        } else {
            rl_download_nodata.setVisibility(View.GONE);
        }
    }

    private void getDownloadingList() {
        values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        videoValues = changeValues(values);
    }

    private void getFinishList() {
        values1 = OkDownload.restore(DownloadManager.getInstance().getFinished());
        videoValues1 = changeValues(values1);
    }

    @Override
    protected void onResume() {
        initData();
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    public void onAllTaskEnd() {

    }

    @Override
    public void back(View view) {
        finish();
    }

    private List<DownloadTask> changeValues(List<DownloadTask> values) {
        List<DownloadTask> list = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {//type为video表示视频
            if (values.get(i).progress.extra2 != null && 1 == 1) {
                VideoModel model = (VideoModel) values.get(i).progress.extra2;
                if (model == null) {
                    return list;
                }
                if (model.getType().equals("article")) {
                    list.add(values.get(i));
                }
            }
        }
        return list;
    }

    private List<Progress> changeValues1(List<Progress> download) {
        List<Progress> list = new ArrayList<>();
        for (int i = 0; i < download.size(); i++) {
            if (download.get(i).extra2 != null && 1 == 1) {
                VideoModel model = (VideoModel) download.get(i).extra2;
                if (model == null) {
                    return list;
                }
                if (model.getType().equals("article")) {
                    list.add(download.get(i));
                }
            }
        }
        return list;
    }

}
