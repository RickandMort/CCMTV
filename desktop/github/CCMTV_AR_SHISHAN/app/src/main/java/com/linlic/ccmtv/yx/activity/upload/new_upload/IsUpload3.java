package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.UploadModel;
import com.linlic.ccmtv.yx.activity.entity.UploadedBean;
import com.linlic.ccmtv.yx.activity.entity.VideoModel;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;
import com.linlic.ccmtv.yx.activity.upload.adapter.UploadFeesAdapter;
import com.linlic.ccmtv.yx.activity.upload.entry.UploadCaseDetail3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsUpload3 extends BaseActivity implements XExecutor.OnAllTaskEndListener, View.OnClickListener {

    private Context context;
    //    private UploadAdapter3 adapter;
    private OkUpload okUpload;
    private RecyclerView recyclerView;
    private TextView activity_title_name, upload_topRightTxt, uploading_num, upload_down_item_size, up_item_group_title, tvUploadedCount, tvUploadedFilter;
    private LinearLayout ll_caching, upload_bottom;
    //内存信息
    private TextView memory_tv, upload_prog, first_uploading_time, tv_iscaching, delete_video;
    private ProgressBar memory_progress;
    private RelativeLayout upload_memory;
    private UploadTask uploadTask;
    //下载中progress信息
    List<Progress> uploading = UploadManager.getInstance().getUploading();
    List<Progress> uploadingNew = new ArrayList<>();
    //已完成progress信息
    List<Progress> uploadfinish = UploadManager.getInstance().getFinished();
    List<Progress> uploadfinishNew = new ArrayList<>();
    //全部下载progress信息
    List<Progress> upload = UploadManager.getInstance().getAll();
    List<Progress> uploadNew = new ArrayList<>();
    private List<UploadTask<?>> values;
    private List<UploadTask<?>> videoValues = new ArrayList<>();
    private List<UploadTask<?>> values1;
    private List<UploadTask<?>> videoValues1 = new ArrayList<>();
    //从服务器获取的上传完成数据
    private List<UploadedBean> uploadedBeanList = new ArrayList<>();
    private double totalSize = 0;
    private double currentSize = 0;
    private double csAll = 0;
    private double csFirst = 0;
    private double csSecond = 0;
    private double csThird = 0;
    private ImageView upload_down_item_img, iv_caching;
    private ProgressBar upload_progress;
    private TextView deleteVideo_tv;//删除按钮
    private TextView selectAllCheck_tv;//全选按钮
    private UploadModel uploadModel;
    private int checkNum = 0;
    private View bottom_view;
    private boolean isNoMore = false;
    private List<UploadCaseDetail3> uploadCaseBeanList;
    private View contentView;
    private GridView gvSelectFees;
    private GridView gvVerifyStatus;
    private List<Map<String, String>> verifyStatusListMap = new ArrayList<>();
    private List<String> verifyStatusList = new ArrayList<>();
    private List<String> feesList = new ArrayList<>();
    private UploadFeesAdapter feesAdapter;
    private UploadFeesAdapter verifyStatusAdapter;
    private ListView lvUploaded;
    private BaseListAdapter adapter;
    private Button btnCancel;
    private Button btnSure;
    private PopupWindow popupWindow;
    private String selectVerifyStatus = "";
    private String selectPayType = "";
    private String mvTitle;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            if (dataObject.has("statuteArr")) {
                                verifyStatusList.clear();
                                JSONArray statusArray = dataObject.getJSONArray("statuteArr");
                                for (int i = 0; i < statusArray.length(); i++) {
                                    Map<String, String> map = new HashMap<>();
                                    JSONObject statusObject = statusArray.getJSONObject(i);
                                    String str = statusObject.getString("status");
                                    map.put("id", statusObject.getString("id"));
                                    map.put("status", statusObject.getString("status"));
                                    verifyStatusList.add(str);
                                    verifyStatusListMap.add(map);
                                }
                                verifyStatusAdapter.notifyDataSetChanged();
                            }
                            if (dataObject.has("costArr")) {
                                feesList.clear();
                                JSONArray feesArray = dataObject.getJSONArray("costArr");
                                for (int i = 0; i < feesArray.length(); i++) {
                                    String str = feesArray.getString(i);
                                    if (Integer.parseInt(str) > 0) {
                                        feesList.add(str + "元");
                                    } else {
                                        feesList.add("免费");
                                    }
                                }
                                feesAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {
                            // 成功
                            uploadCaseBeanList = new ArrayList<>();
                            mvTitle = jsonObject.getString("mvtitle");
                            if (!jsonObject.isNull("list")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    if (data != null) {
                                        UploadCaseDetail3 dataBean = new UploadCaseDetail3();
                                        List<String> urlList = new ArrayList<>();
                                        dataBean.setInfo(data.getString("info"));
                                        dataBean.setIcon(data.getString("icon"));
                                        JSONArray urlArray = data.getJSONArray("img");
                                        for (int j = 0; j < urlArray.length(); j++) {
                                            urlList.add(urlArray.getString(j));
                                        }
                                        dataBean.setUrlList(urlList);
                                        uploadCaseBeanList.add(dataBean);
                                    }
                                }
                                Intent intent = new Intent(context, MyCaseActivity3.class);
                                Bundle bundle = new Bundle();
//                                intent.putExtra("mvTitle",mvTitle);
                                bundle.putSerializable("uploadCaseDetail", (Serializable) uploadCaseBeanList);
                                bundle.putString("mvTitle", mvTitle);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_upload3);
        context = this;
             findId();
        initView();
        /*initData();
        isShowNoData();*/
        //memoryUtils();
        initPopupView();
        setFeesValue();
    }

    private void isShowNoData() {
        upload = UploadManager.getInstance().getUploading();
        uploadNew = upload;
        if (uploadNew.size() == 0 && isNoMore) {
            layout_nodata.setVisibility(View.VISIBLE);
            lvUploaded.setVisibility(View.GONE);
            //upload_memory.setVisibility(View.VISIBLE);
        } else {
            layout_nodata.setVisibility(View.GONE);
            lvUploaded.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.id_upload_recyclerView1);
        lvUploaded = (ListView) findViewById(R.id.id_upload_recyclerView1);
        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        upload_topRightTxt = (TextView) findViewById(R.id.id_upload_topRightTxt1);
        //memory_tv = (TextView) findViewById(R.id.memory_tv);
        //memory_progress = (ProgressBar) findViewById(R.id.memory_progress);
        //upload_memory = (RelativeLayout) findViewById(R.id.download_memory);
        ll_caching = (LinearLayout) findViewById(R.id.id_upload_ll_caching1);
        uploading_num = (TextView) findViewById(R.id.id_uploading_num1);
        upload_down_item_size = (TextView) findViewById(R.id.id_upload_down_item_size1);
        upload_down_item_img = (ImageView) findViewById(R.id.id_upload_down_item_img1);
        upload_progress = (ProgressBar) findViewById(R.id.id_upload_progress1);
        up_item_group_title = (TextView) findViewById(R.id.id_up_item_group_title1);
//        layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
        upload_bottom = (LinearLayout) findViewById(R.id.id_upload_bottom1);
        deleteVideo_tv = (TextView) findViewById(R.id.id_uoload_delete_video1);
        selectAllCheck_tv = (TextView) findViewById(R.id.id_upload_selectAllCheck1);
        upload_prog = (TextView) findViewById(R.id.id_upload_prog1);
//        btn_nodata = (Button) findViewById(R.id.btn_nodata);
//        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        first_uploading_time = (TextView) findViewById(R.id.id_first_uploading_time);
        iv_caching = (ImageView) findViewById(R.id.id_upload_iv_caching);
        tv_iscaching = (TextView) findViewById(R.id.id_upload_tv_iscaching);
        delete_video = (TextView) findViewById(R.id.id_uoload_delete_video1);
        bottom_view = findViewById(R.id.id_upload_bottom_view);
        tvUploadedCount = (TextView) findViewById(R.id.id_tv_is_upload3_uploaded_count);
        tvUploadedFilter = (TextView) findViewById(R.id.id_tv_is_upload3_uploaded_filter);

        btn_nodata.setText("去上传");
        btn_nodata.setVisibility(View.GONE);

        upload_topRightTxt.setOnClickListener(this);
        ll_caching.setOnClickListener(this);
        deleteVideo_tv.setOnClickListener(this);
        selectAllCheck_tv.setOnClickListener(this);
        tvUploadedFilter.setOnClickListener(this);

        btn_nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        getUploadedData("", "");
        activity_title_name.setText("上传");
        tv_nodata.setText("暂无记录");
        values = OkUpload.restore(UploadManager.getInstance().getUploading());
        videoValues = values;
        values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
        videoValues1 = values1;
        is_caching();

        okUpload = OkUpload.getInstance();
        /*adapter = new UploadAdapter3(this, uploadedBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new UploadAdapter3.OnItemClickListener() {

            @Override
            public void onClick(int position) {
                try {
                    if (uploadedBeanList.get(position).getPlaystate().equals("2") || !uploadedBeanList.get(position).getMvstatus().equals("2")) {
                        //Toast.makeText(context, "此资源暂时不支持", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (uploadedBeanList.get(position).getStyletype().equals("video")) {
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("videoPath", uploadedBeanList.get(position).getMvurl());
                        intent.putExtra("videoSource", 1);//设置视频来源   0代表本地  1代表网络
                        intent.putExtra("last_look_time", 0);
                        context.startActivity(intent);
                    } else {
                        getUploadedCaseDetail(uploadedBeanList.get(position).getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        adapter = new BaseListAdapter(lvUploaded, uploadedBeanList, R.layout.item_upload_manager3) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                UploadedBean uploadedBean = (UploadedBean) item;
                if (uploadedBean != null) {
                    helper.setText(R.id.name, uploadedBean.getMvtitle());
                    if (uploadedBean.getMvstatus().equals("1")) {
                        helper.setTextColor(R.id.id_tv_item_upload_verify_status3, R.color.black);
                        helper.setText(R.id.id_tv_item_upload_verify_status3, "审核中");
                    } else if (uploadedBean.getMvstatus().equals("2")) {
                        helper.setTextColor(R.id.id_tv_item_upload_verify_status3, R.color.layout_bg2);
                        helper.setText(R.id.id_tv_item_upload_verify_status3, "已审核");
                    } else {
                        helper.setTextColor(R.id.id_tv_item_upload_verify_status3, R.color.exams_list_item_text_color8);
                        helper.setText(R.id.id_tv_item_upload_verify_status3, "审核未通过");
                    }
                    helper.setText(R.id.date, uploadedBean.getRow_add_time().substring(0, 10));
                    if (uploadedBean.getStyletype().equals("video")) {
                        helper.setImageBitmapGlide(context, R.id.icon, uploadedBean.getImgurl());
                        if (!TextUtils.isEmpty(uploadedBean.getVtime())&&(!"null".equals(uploadedBean.getVtime()))) {
                            helper.setText(R.id.item_time, uploadedBean.getVtime());
                            helper.setVisibility(R.id.item_time,View.VISIBLE);
                        } else {
                            helper.setVisibility(R.id.item_time,View.GONE);
                        }
                    } else {
                        helper.setImageResource(R.id.icon, R.mipmap.ic_upload_case_pic);
                        if (!TextUtils.isEmpty(uploadedBean.getVtime())&&(!"null".equals(uploadedBean.getVtime()))) {
                            helper.setText(R.id.item_time, uploadedBean.getVtime() + "");
                            helper.setVisibility(R.id.item_time,View.VISIBLE);
                        } else {
                            helper.setVisibility(R.id.item_time,View.GONE);
                        }
                    }
                    if (uploadedBean.getMvMoney().equals("0")) {
                        helper.setText(R.id.pay_type, "免费");
                        helper.setTextColor2(R.id.pay_type, Color.parseColor("#3897f9"));
                    } else {
                        helper.setText(R.id.pay_type, "¥" + uploadedBean.getMvMoney());
                        helper.setTextColor2(R.id.pay_type, Color.RED);
                    }
                }
            }
        };
        lvUploaded.setAdapter(adapter);

        lvUploaded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    if (uploadedBeanList.get(position).getPlaystate().equals("2") || !uploadedBeanList.get(position).getMvstatus().equals("2")) {
                        //Toast.makeText(context, "此资源暂时不支持", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (uploadedBeanList.get(position).getStyletype().equals("video")) {
                        Intent intent = new Intent(context, VideoPlayerActivity.class);
                        intent.putExtra("videoPath", uploadedBeanList.get(position).getMvurl());
                        intent.putExtra("videoSource", 1);//设置视频来源   0代表本地  1代表网络
                        intent.putExtra("last_look_time", (long) 0);
                        context.startActivity(intent);
                    } else {
                        getUploadedCaseDetail(uploadedBeanList.get(position).getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        for (int i = 0; i < videoValues1.size(); i++) {
            UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
            if (uploadModel != null) {
                uploadModel.setIsVisibility("0");
            }
        }
        adapter.notifyDataSetChanged();
        okUpload.addOnAllTaskEndListener(this);
    }

    private void getUploadedCaseDetail(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.casedetail);
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadVideo, obj.toString());
                    LogUtil.e("IsUpload3", "getUploadedCaseDetail:获取病例详情记录：" + result);

                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void getUploadedData(final String mvStatus, final String mvMoney) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myUploadCase);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("status", mvStatus);
                    obj.put("money", mvMoney);
                    String result = HttpClientUtils.sendPost(context, URLConfig.ccmtvapp_uploadVideo, obj.toString());
                    LogUtil.e("IsUpload3", "getUploadedData:获取所有上传记录：" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {                                         // 成功
                        JSONArray dataArray = new JSONArray();
                        if (!jsonObject.isNull("data")) {
                            dataArray = jsonObject.getJSONArray("data");
                        }
                        uploadedBeanList.clear();

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);
                            UploadedBean uploadedBean = new UploadedBean();
                            uploadedBean.setId(data.getString("id"));
                            uploadedBean.setImgurl(data.getString("imgurl"));
                            uploadedBean.setMvtitle(data.getString("mvtitle"));
                            uploadedBean.setMvstatus(data.getString("mvstatus"));
                            uploadedBean.setRow_add_time(data.getString("row_add_time"));
                            uploadedBean.setStyletype(data.getString("styletype"));
                            uploadedBean.setMvurl(data.getString("mvurl"));
                            uploadedBean.setVtime(data.getString("vtime"));
                            uploadedBean.setPlaystate(data.getString("playstate"));
                            uploadedBean.setMvMoney(data.getString("mvmoney"));
                            uploadedBeanList.add(uploadedBean);
                        }
                    } else {
                        isNoMore = true;
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (uploadedBeanList.size() <= 0) {
                                layout_nodata.setVisibility(View.VISIBLE);
                                lvUploaded.setVisibility(View.GONE);
                                //upload_memory.setVisibility(View.VISIBLE);
                            } else {
                                layout_nodata.setVisibility(View.GONE);
                                lvUploaded.setVisibility(View.VISIBLE);
                            }
                            tvUploadedCount.setText("上传完成(" + uploadedBeanList.size() + ")");
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void is_caching() {//是否显示进入下载中界面
        List<Progress> uploading = UploadManager.getInstance().getUploading();
        uploadingNew = uploading;
        if (uploadingNew.size() != 0) {
            ll_caching.setVisibility(View.VISIBLE);
            uploading_num.setText(uploadingNew.size() + "");//正在下载的视频个数
            tv_iscaching.setText("正在上传(" + uploadingNew.size() + "个文件)");
            progressSize();
            setMSize();
        } else {
            ll_caching.setVisibility(View.GONE);
            tv_iscaching.setText("正在上传");
        }
    }

    /*private void memoryUtils() {
        String cachePath = SharedPreferencesTools.getCachePath(context);
        if (cachePath.contains("emulated")) {//手机内存
            String phoneMemory = FileSizeUtil.FormetFileSize(SDCardUtils.getTotalInternalMemorySize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(SDCardUtils.getAvailableInternalMemorySize());
            memory_tv.setText("总空间" + phoneMemory);

            double resultDouble = ((double) SDCardUtils.getAvailableInternalMemorySize()) / ((double) SDCardUtils.getTotalInternalMemorySize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        } else {//SD卡
            String sdcardMemory = FileSizeUtil.FormetFileSize(getAllSize()) + "，" + "当前可用空间" + FileSizeUtil.FormetFileSize(getAvailableExternalMemorySize());
            memory_tv.setText("总空间" + sdcardMemory);

            double resultDouble = ((double) getAvailableExternalMemorySize()) / ((double) getAllSize());
            int resultInt = (int) (resultDouble * 100);
            memory_progress.setProgress(100 - resultInt);
        }
    }*/

    public long getAllSize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }

    public long getAvailableExternalMemorySize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(context, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    @Override
    public void onAllTaskEnd() {
        ll_caching.setVisibility(View.GONE);
        initData();//全部任务下载完成后刷新界面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okUpload.removeOnAllTaskEndListener(this);
    }

    @Override
    protected void onResume() {
        initData();
//        isShowNoData();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    public void back(View view) {
        values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
        videoValues1 = values1;
        for (int i = 0; i < videoValues1.size(); i++) {
            UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
            if (uploadModel.isChecked()) {
                uploadModel.setChecked(false);
            }
            uploadModel.setCheckBoxStatus("0");
        }
        adapter.notifyDataSetChanged();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_upload_topRightTxt1://右上角编辑按钮
                finish();
                /*checkNum = 0;
                values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
                videoValues1 = values1;
                if (videoValues1.size() > 0) {
                    for (int i = 0; i < videoValues1.size(); i++) {
                        deleteVideo_tv.setTextColor(Color.parseColor("#000000"));
                        uploadTask = videoValues1.get(i);
                        uploadModel = (UploadModel) uploadTask.progress.extra1;
                        uploading = UploadManager.getInstance().getUploading();
                        uploadingNew = uploading;
                        if (uploadModel.getCheckBoxStatus().equals("0")) {
                            uploadModel.setCheckBoxStatus("1");
                            upload_topRightTxt.setText("取消");
                            upload_memory.setVisibility(View.GONE);
                            upload_bottom.setVisibility(View.VISIBLE);
                            bottom_view.setVisibility(View.VISIBLE);
                            if (uploadingNew.size() > 0) {
                                ll_caching.setVisibility(View.GONE);
                            }
                            adapter.notifyDataSetChanged();
                        } else if (uploadModel.getCheckBoxStatus().equals("1")) {
                            uploadModel.setCheckBoxStatus("0");
                            upload_bottom.setVisibility(View.GONE);
                            bottom_view.setVisibility(View.GONE);
                            upload_topRightTxt.setText("编辑");
                            if (uploadingNew.size() > 0) {
                                ll_caching.setVisibility(View.VISIBLE);
                            }
                            //清空选中的信息
                            for (int j = 0; j < videoValues1.size(); j++) {//清空checkbox选中的状态
                                UploadModel uploadModel = (UploadModel) videoValues1.get(j).progress.extra1;
                                if (uploadModel.isChecked()) {
                                    uploadModel.setChecked(false);
                                }
                            }
                            selectAllCheck_tv.setText("全选");
                            deleteVideo_tv.setText("删除");
                            upload_memory.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }*/
                break;
            case R.id.id_upload_ll_caching1://正在上传条目
                startActivity(new Intent(context, UploadingActivity3.class));
                break;
//            case R.id.btn_nodata:
//                /*Intent intent = new Intent(context, CustomActivity.class);
//                intent.putExtra("type","my");
//                startActivity(intent);*/
//                finish();
//                break;
            case R.id.id_upload_selectAllCheck1://全选按钮
                values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
                videoValues1 = values1;
                if (selectAllCheck_tv.getText().toString().equals("全选")) {
                    selectAllCheck_tv.setText("取消全选");
                    for (int i = 0; i < videoValues1.size(); i++) {
                        UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
                        if (!uploadModel.isChecked()) {
                            uploadModel.setChecked(true);
                            checkNum++;
                            delete_video.setText("删除(" + checkNum + ")");
                            delete_video.setTextColor(Color.RED);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else if (selectAllCheck_tv.getText().toString().equals("取消全选")) {
                    selectAllCheck_tv.setText("全选");
                    for (int i = 0; i < videoValues1.size(); i++) {
                        UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
                        if (uploadModel.isChecked()) {
                            uploadModel.setChecked(false);
                            checkNum--;
                            if (checkNum != 0) {
                                delete_video.setText("删除(" + checkNum + ")");
                            } else if (checkNum == 0) {
                                delete_video.setText("删除");
                                selectAllCheck_tv.setText("全选");
                                delete_video.setTextColor(Color.BLACK);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.id_uoload_delete_video1://删除按钮
                values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
                videoValues1 = values1;
                for (int i = 0; i < videoValues1.size(); i++) {
                    UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
                    if (uploadModel.isChecked()) {
                        videoValues1.get(i).remove();
                        uploadModel.setUpload_state("1");
                    }
                }
                upload_bottom.setVisibility(View.GONE);
                bottom_view.setVisibility(View.GONE);
                upload_topRightTxt.setText("编辑");
                values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
                videoValues1 = values1;
                for (int i = 0; i < videoValues1.size(); i++) {
                    uploadTask = videoValues1.get(i);
                    uploadModel = (UploadModel) uploadTask.progress.extra1;
                    uploadModel.setCheckBoxStatus("0");
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
//                isShowNoData();
                initData();
                uploadfinish = UploadManager.getInstance().getFinished();
                uploadfinishNew = uploadfinish;
                if (uploadfinishNew.size() == 0) {
                    layout_nodata.setVisibility(View.VISIBLE);
                    lvUploaded.setVisibility(View.GONE);
                    //upload_memory.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.id_tv_is_upload3_uploaded_filter:
                showPopupWindow();
                break;
        }
    }

    public void setFeesValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.upmoney);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("new", "new");
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
//                    Log.e(getLocalClassName(), "获取筛选收费数据: " + result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void initPopupView() {
        // 用于PopupWindow的View
        contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_uploaded_filter, null, false);
        gvSelectFees = (GridView) contentView.findViewById(R.id.id_gv_popwindow_pay_type);
        gvVerifyStatus = (GridView) contentView.findViewById(R.id.id_gv_popwindow_verify_status);
        btnCancel = (Button) contentView.findViewById(R.id.id_btn_popup_uploaded_filter_cancel);
        btnSure = (Button) contentView.findViewById(R.id.id_btn_popup_uploaded_filter_sure);
        /*verifyStatusList.add("通过");
        verifyStatusList.add("未通过");
        verifyStatusList.add("待审核");*/
        /*int num = 0;
        for (int i=0;i<11;i++) {
            if (i==0){
                feesList.add("免费");
            }else {
                feesList.add(num + "元");
            }
            num = num + 10;
        }*/

        feesAdapter = new UploadFeesAdapter(context, feesList);
        verifyStatusAdapter = new UploadFeesAdapter(context, verifyStatusList);
        gvSelectFees.setAdapter(feesAdapter);
        gvVerifyStatus.setAdapter(verifyStatusAdapter);

        gvSelectFees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    if (feesList.get(position).contains("免费")) {
                        selectPayType = "0";
                    } else {
                        selectPayType = feesList.get(position).split("元")[0];
                    }
                    feesAdapter.setSelected(position);
                }
            }
        });

        gvVerifyStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != -1) {
                    selectVerifyStatus = verifyStatusListMap.get(position).get("id");
                    verifyStatusAdapter.setSelected(position);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (verifyStatusAdapter.getSelected()>=0){
                    selectVerifyStatus = verifyStatusList.get(verifyStatusAdapter.getSelected());
                }

                if (feesAdapter.getSelected()>=0) {
                    selectPayType = feesList.get(feesAdapter.getSelected());
                }*/
//                Toast.makeText(context, "选择审核状态：" + selectVerifyStatus + "\t选择收费类型：" + selectPayType, Toast.LENGTH_SHORT).show();
                getUploadedData(selectVerifyStatus, selectPayType);
                popupWindow.dismiss();
            }
        });
    }

    /**
     * name：选择收费标准
     * author：bentley
     * data：2018/7/17 10:27
     */
    private void showPopupWindow() {
        feesAdapter.notifyDataSetChanged();
        verifyStatusAdapter.notifyDataSetChanged();
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(contentView);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void progressSize() {
        values = OkUpload.restore(UploadManager.getInstance().getUploading());
        videoValues = values;
        for (int i = 0; i < videoValues.size(); i++) {
            UploadTask task = videoValues.get(i);
            String tag = task.progress.tag;
            task.register(new UploadingListener());
        }
    }

    public class UploadingListener<T> extends UploadListener<T> {


        public UploadingListener() {
            super("LogUploadListener");
        }

        @Override
        public void onStart(Progress progress) {
            //Log.e("IsUpload2", "onStart: 开始上传");
        }

        @Override
        public void onProgress(Progress progress) {
            values = OkUpload.restore(UploadManager.getInstance().getUploading());
            videoValues = values;
            DecimalFormat df = new DecimalFormat(".00");
            for (int i = 0; i < videoValues.size(); i++) {
                csFirst = videoValues.get(i).progress.currentSize / 1024.0 / 1024.0;
                csAll = csAll + csFirst;
            }

            //Log.e("IsUpload2", "onProgress: 正在上传" + progress.toString());
            //MyNotificationCase(context, "上传任务已添加", "正在上传" + progress.extra2, df.format(csAll) + "M/" + df.format(totalSize) + "M", false);
            MyNotificationCase(context, "上传任务已添加", "正在上传" + progress.extra2, "当前进度:" + (int) ((csAll / totalSize) * 100) + "%", false);
            upload_progress.setProgress((int) ((csAll / totalSize) * 100));
            upload_prog.setText("已上传" + (int) ((csAll / totalSize) * 100) + "%");
            upload_down_item_size.setText(df.format(csAll) + "M/" + df.format(totalSize) + "M");
            csAll = 0;

            is_caching();

        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
            //Log.e("IsUpload2", "onError: 上传出错" + progress.extra2 + progress.exception);
            MyNotificationCase(context, "您有任务上传失败", progress.extra2 + "上传失败", "上传失败", true);
        }

        @Override
        public void onFinish(Object o, Progress progress) {
            //Log.e("IsUpload2", "onFinish: 上传完成" + progress.extra2);
            MyNotificationCase(context, "1个任务上传完成", progress.extra2 + "上传成功", "上传完成", true);
        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

    public static void MyNotificationCase(Context context, String tan, String title, String cont, boolean isProgress) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, IsUpload3.class);
        intent.putExtra("TAG", "Case");
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 通过Notification.Builder来创建通知，注意API Level
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(tan)
                .setContentTitle(title)
                .setContentText(cont)
                .setContentIntent(pendingIntent3).setNumber(1).build();

        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        if (isProgress == true) {
            //添加声音
            notify.defaults |= Notification.DEFAULT_SOUND;
            //添加震动
            notify.defaults |= Notification.DEFAULT_VIBRATE;
        }

        manager.notify(3, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
    }

    /**
     * 设置下载中的视频总大小和已下载的视频的总大小
     */
    private void setMSize() {
        DecimalFormat df = new DecimalFormat(".00");
        currentSize = 0;
        totalSize = 0;
        uploading = UploadManager.getInstance().getUploading();
        uploadingNew = uploading;
        for (int i = 0; i < uploadingNew.size(); i++) {
            totalSize = totalSize + (uploadingNew.get(i).totalSize / 1024.0 / 1024.0);//视频总大小
            currentSize = currentSize + (uploadingNew.get(i).currentSize / 1024.0 / 1024.0);//视频已上传大小
        }
        upload_progress.setProgress((int) ((currentSize / totalSize) * 100));

        values = OkUpload.restore(UploadManager.getInstance().getUploading());
        videoValues = values;
        Progress progress = null;
        try {
            progress = videoValues.get(0).progress;
            uploadModel = (UploadModel) progress.extra1;
            if (Util.isOnMainThread()) {//防止界面销毁时Glide报错闪退
                RequestOptions options = new RequestOptions().error(R.mipmap.ic_launcher);
                Glide.with(getApplication()).load(uploadModel.iconUrl).apply(options).into(upload_down_item_img);
            }
            first_uploading_time.setText(uploadModel.getVtime());
            up_item_group_title.setText(uploadModel.name);
            upload_down_item_size.setText(df.format(currentSize) + "M/" + df.format(totalSize) + "M");
            upload_prog.setText("已上传" + (int) ((currentSize / totalSize) * 100) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            values1 = OkUpload.restore(UploadManager.getInstance().getFinished());
            videoValues1 = values1;
            for (int i = 0; i < videoValues1.size(); i++) {
                UploadModel uploadModel = (UploadModel) videoValues1.get(i).progress.extra1;
                if (uploadModel.isChecked()) {
                    uploadModel.setChecked(false);
                }
                uploadModel.setCheckBoxStatus("0");
            }
            adapter.notifyDataSetChanged();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void showNotifictionIcon(Context context, Progress progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(context, MainActivity.class);//将要跳转的界面
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.app_icon);//设置通知栏消息标题的头像
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        //2018-03-28 增加下载完成振动提示
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        builder.setTicker("临床频道");
        builder.setContentText(progress.fileName);//通知内容
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    /*private List<DownloadTask> changeValues(List<DownloadTask> values) {
        List<DownloadTask> list = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {//type为video表示视频
            boolean is = values.get(i).progress.extra1!=null?true:false;
            if (is) {
                VideoModel model = (VideoModel) values.get(i).progress.extra1;
//                return list;
                if (model.getType() == null) {
                    model.setType("video");
                }
                if (model.getType().equals("video")) {
                    list.add(values.get(i));
                }
            }

        }
        return list;
    }*/

    private List<Progress> changeValues1(List<Progress> download) {
        List<Progress> list = new ArrayList<>();
        for (int i = 0; i < download.size(); i++) {
            boolean is = download.get(i).extra1 != null ? true : false;
            if (is) {
                VideoModel model = (VideoModel) download.get(i).extra1;
//                return list;
                if (model.getType() == null) {
                    model.setType("video");
                }
                if (model.getType().equals("video")) {
                    list.add(download.get(i));
                }
            } else {

            }

        }
        return list;
    }
}
