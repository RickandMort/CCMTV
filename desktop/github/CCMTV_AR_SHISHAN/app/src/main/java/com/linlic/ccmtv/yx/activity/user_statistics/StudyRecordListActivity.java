package com.linlic.ccmtv.yx.activity.user_statistics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.hospital_training.Details_of_policies_and_regulations;
import com.linlic.ccmtv.yx.activity.user_statistics.javabean.StudyRecordVideoBean;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*历史记录 视频
* */
public class StudyRecordListActivity extends BaseActivity {

    private TextView title_name;
    private ImageView ivTitleRight;
    private ListView lvStudyRecord;
    private LinearLayout llStudyRecordList;
    private NodataEmptyLayout lt_nodata1;
    private String intentType;
    private BaseListAdapter baseListAdapter;

    private List<StudyRecordVideoBean> studyRecordVideoBeanList = new ArrayList<>();
    private Context context;
    private int page = 1;
    private int pause = 0;
    private String dataType;
    private boolean isNoMore = false;
    private boolean isBottom = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                if (jsonObjectData.getInt("status") == 1) {
                                    if (jsonObjectData.has("msg")) {
                                        JSONArray jsonArrayMsg = jsonObjectData.getJSONArray("msg");
                                        if (jsonArrayMsg != null && jsonArrayMsg.length() >= 10) {
                                            isNoMore = false;
                                        } else {
                                            isNoMore = true;
                                        }
                                        if (jsonArrayMsg != null && jsonArrayMsg.length() > 0) {
                                            for (int i = 0; i < jsonArrayMsg.length(); i++) {
                                                JSONObject jsonMsgData = jsonArrayMsg.getJSONObject(i);
                                                StudyRecordVideoBean studyRecordVideoBean = new StudyRecordVideoBean();
                                                studyRecordVideoBean.setAid(jsonMsgData.getString("aid"));
                                                studyRecordVideoBean.setVideoTitle(jsonMsgData.getString("title"));
                                                studyRecordVideoBean.setLast_look_time(jsonMsgData.getString("time"));
                                                studyRecordVideoBean.setPicUrl(jsonMsgData.getString("picUrl"));
                                                studyRecordVideoBean.setMoney(jsonMsgData.getString("money"));
                                                studyRecordVideoBean.setVideopaymoney(jsonMsgData.getString("videopaymoney"));
                                                studyRecordVideoBeanList.add(studyRecordVideoBean);
                                            }
                                        }
                                        baseListAdapter.notifyDataSetChanged();
                                    }
                                    MyProgressBarDialogTools.hide();
//                                    if (studyRecordVideoBeanList.size() > 0) {
//                                        llStudyRecordList.setVisibility(View.VISIBLE);
//                                        lt_nodata1.setVisibility(View.GONE);
//                                    } else {
//                                        llStudyRecordList.setVisibility(View.GONE);
//                                        lt_nodata1.setVisibility(View.VISIBLE);
//                                    }
                                } else {
                                    MyProgressBarDialogTools.hide();
                                    isNoMore = true;// 失败
//                                    if (studyRecordVideoBeanList.size() > 0) {
//                                        llStudyRecordList.setVisibility(View.VISIBLE);
//                                        lt_nodata1.setVisibility(View.GONE);
//                                    } else {
//                                        llStudyRecordList.setVisibility(View.GONE);
//                                        lt_nodata1.setVisibility(View.VISIBLE);
//                                    }
                                }
                            }
                        } else {
                            MyProgressBarDialogTools.hide();
                            isNoMore = true;// 失败
//                            if (studyRecordVideoBeanList.size() > 0) {
//                                llStudyRecordList.setVisibility(View.VISIBLE);
//                                lt_nodata1.setVisibility(View.GONE);
//                            } else {
//                                llStudyRecordList.setVisibility(View.GONE);
//                                lt_nodata1.setVisibility(View.VISIBLE);
//                            }
                        }
                        setResultStatus(studyRecordVideoBeanList.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(studyRecordVideoBeanList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            llStudyRecordList.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            llStudyRecordList.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_record_list);

        context = this;
        findId();
        initData();
        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intentType = intent.getStringExtra("type");
        if (dataType != null && !dataType.equals(intentType)) {
            studyRecordVideoBeanList.removeAll(studyRecordVideoBeanList);
        }
        if (intentType.equals("video")) {
            dataType = "video";
            title_name.setText("视频记录");
        } else if (intentType.equals("audio")) {
            dataType = "audio";
            title_name.setText("音频记录");
        } else if (intentType.equals("article")) {
            dataType = "article";
            title_name.setText("文章记录");
        }
        initListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause > 0) {
            page = 1;
            studyRecordVideoBeanList.removeAll(studyRecordVideoBeanList);
            isBottom = false;
            isNoMore = false;
            getStudyRecordData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause++;
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        lvStudyRecord = (ListView) findViewById(R.id.id_lv_study_record_list);
        llStudyRecordList = (LinearLayout) findViewById(R.id.id_ll_study_record_list);
        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);

        title_name.setTextSize(20);

        ivTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRecordListActivity.this, StudyRecordTypeSelectActivity.class);
                startActivity(intent);
            }
        });

        lvStudyRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataType.equals("video")) {
                    Intent intent = new Intent(context, VideoFive.class);
                    intent.putExtra("aid", studyRecordVideoBeanList.get(position).getAid());
                    startActivity(intent);
                } else if (dataType.equals("article")) {
                    Intent intent = new Intent(context, Details_of_policies_and_regulations.class);
                    intent.putExtra("aid", studyRecordVideoBeanList.get(position).getAid());
                    intent.putExtra("nocollect", "yes");
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        ivTitleRight.setImageResource(R.mipmap.study_record_type_select);
        ivTitleRight.setVisibility(View.VISIBLE);

        getStudyRecordData();
    }

    private void initListView() {
        if (dataType.equals("video")) {
            baseListAdapter = new BaseListAdapter(lvStudyRecord, studyRecordVideoBeanList, R.layout.item_study_record_list) {
                @Override
                public void refresh(Collection datas) {
                    super.refresh(datas);
                }

                @Override
                public void convert(ListHolder helper, Object item, boolean isScrolling) {
                    super.convert(helper, item, isScrolling);
                    helper.setText(R.id.id_tv_item_study_record_title, ((StudyRecordVideoBean) item).getVideoTitle());
                    helper.setText(R.id.id_tv_item_study_record_last_look_time, ((StudyRecordVideoBean) item).getLast_look_time());
                    helper.setImageBitmapGlide(context, R.id.id_iv_item_study_record_pic, ((StudyRecordVideoBean) item).getPicUrl());
                }
            };
        } else {
            baseListAdapter = new BaseListAdapter(lvStudyRecord, studyRecordVideoBeanList, R.layout.item_study_record_list2) {
                @Override
                public void refresh(Collection datas) {
                    super.refresh(datas);
                }

                @Override
                public void convert(ListHolder helper, Object item, boolean isScrolling) {
                    super.convert(helper, item, isScrolling);
                    helper.setText(R.id.id_tv_item_study_record_title, ((StudyRecordVideoBean) item).getVideoTitle());
                    helper.setText(R.id.id_tv_item_study_record_last_look_time, ((StudyRecordVideoBean) item).getLast_look_time());
                }
            };
        }

        //StudyRecordListAdapter adapter=new StudyRecordListAdapter(this,studyRecordVideoBeanList);
        lvStudyRecord.setAdapter(baseListAdapter);
        baseListAdapter.notifyDataSetChanged();

        lvStudyRecord.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!isNoMore && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isBottom) {
                    //页数加1,请求数据
                    page++;
                    getStudyRecordData();

                    isNoMore = true;
                    isBottom = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断是否滚到最后一行
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    isBottom = true;
                }/*else {
                    isBottom = false;
                }*/
            }
        });
    }

    private void getStudyRecordData() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.dataRecord);
                    obj.put("uid", SharedPreferencesTools.getUids(context));
                    obj.put("page", page);
                    obj.put("type", dataType);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    Log.e("看看具体学习记录：", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    MyProgressBarDialogTools.hide();
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void back(View view) {
        finish();
    }
}
