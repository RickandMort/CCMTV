package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 已结束 学习任务
 */
public class ExpiredFragment extends BaseFragment {
    private ListView expired_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private boolean isNoMore = false;
    private int page = 1;
    private int pause = 0;
    private int pages = 0;
    private NodataEmptyLayout lt_nodata3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("tasktitle", customJson.getString("tasktitle"));
                                map.put("starttime", customJson.getString("starttime"));
                                map.put("endtime", customJson.getString("endtime"));
                                map.put("taskid", customJson.getString("tid"));
                                map.put("cid", customJson.getString("cid"));
                                map.put("status", customJson.getString("status"));
                                map.put("is_look", customJson.getString("is_look"));
                                map.put("rate_of_progress", customJson.getInt("rate_of_progress"));
                                try {
                                    map.put("is_miss", customJson.getString("is_miss"));
                                    map.put("complete", customJson.getString("complete"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    map.put("complete", "");
                                    map.put("is_miss", "0");
                                }
                                data.add(map);
                            }
                            pages = jsonObject.getInt("pages");
//                            if (data.size() > 0) {
//                                expired_list.setVisibility(View.VISIBLE);
//                                lt_nodata3.setVisibility(View.GONE);
//                            } else {
//                                expired_list.setVisibility(View.GONE);
//                                lt_nodata3.setVisibility(View.VISIBLE);
//                            }
                            /*if(dataArray.length()<10){
                                isNoMore = true;
                            }*/

                        } else {
                            isNoMore = true;
//                            if (data.size() > 0) {
//                                expired_list.setVisibility(View.VISIBLE);
//                                lt_nodata3.setVisibility(View.GONE);
//                            } else {
//                                expired_list.setVisibility(View.GONE);
//                                lt_nodata3.setVisibility(View.VISIBLE);
//                            }
//                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }

        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            expired_list.setVisibility(View.VISIBLE);
            lt_nodata3.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(getContext(), code)) {
                lt_nodata3.setNetErrorIcon();
            } else {
                lt_nodata3.setLastEmptyIcon();
            }
            expired_list.setVisibility(View.GONE);
            lt_nodata3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expired, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getListEndTask);
                    obj.put("uid", SharedPreferencesTools.getUid(getActivity()));
                    obj.put("page", page);
                    obj.put("title", LearningTaskActivity.keyword.length() > 0 ? LearningTaskActivity.keyword : "");
                    obj.put("type", LearningTaskActivity.poptype.length() > 0 ? LearningTaskActivity.poptype : "");
                    obj.put("qualified", LearningTaskActivity.popqualified.length() > 0 ? LearningTaskActivity.popqualified : "");
                    obj.put("version", "1");
//                    Log.e("看看expired1数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.Learning_task, obj.toString());
//                    Log.e("看看expired数据", result);

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

    private void initView() {
        expired_list = (ListView) getActivity().findViewById(R.id.expired_list);
        lt_nodata3 = (NodataEmptyLayout) getActivity().findViewById(R.id.lt_nodata3);
    }

    private void initData() {
        /*baseListAdapter = new BaseListAdapter(expired_list, data, R.layout.item_learning_task) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String title = ((Map) item).get("tasktitle") + "";
                *//*if (title.length() <= 11) {
                    helper.setText(R.id.task_title1, Html.fromHtml(title));
                    helper.setText(R.id.task_title2, "");
                } else {
                    helper.setText(R.id.task_title1, Html.fromHtml(title.substring(0, 11)));
                    helper.setText(R.id.task_title2, Html.fromHtml(title.substring(11)));
                }*//*
                //helper.setText(R.id.task_title1, Html.fromHtml(((Map) item).get("tasktitle") + ""));
                helper.setVisibility(R.id.schedule_layout, View.GONE);
                helper.setText(R.id.task_starttime, ((Map) item).get("starttime") + "");
                helper.setText(R.id.task_endtime, ((Map) item).get("endtime") + "");
                helper.setText(R.id.learning_task_id, ((Map) item).get("taskid") + "");
                helper.setText(R.id.learning_task_cid, ((Map) item).get("cid") + "");
                helper.setText(R.id.learning_task_pages, ((Map) item).get("pages") + "");
                helper.setRoundProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"), ((Map) item).get("complete") + "");
                //helper.setVisibility(R.id.btn_stauts, View.INVISIBLE);
                helper.setVisibility(R.id.is_qualified, View.VISIBLE);
                if (Integer.parseInt(((Map) item).get("is_look").toString()) > 0) {
                    helper.setVisibility(R.id.is_look, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.is_look, View.GONE);
                }
                String qualified = (String) ((Map) item).get("status");
                if (qualified.equals("合格")) {
                    helper.setImageResource(R.id.is_qualified, R.mipmap.qualified1);
                } else if (qualified.equals("不合格")) {
                    helper.setImageResource(R.id.is_qualified, R.mipmap.qualified2);
                } else {
                    helper.setVisibility(R.id.is_qualified, View.INVISIBLE);
                }
                int progress = (Integer) ((Map) item).get("rate_of_progress");
                *//*if (progress == 0) {
                    helper.setText(R.id.btn_stauts, "去完成");
                    helper.setText(R.id.task_is_complete, "还未开始任务");
                } else if (0 < progress && progress < 100) {
                    helper.setText(R.id.btn_stauts, "继续任务");
                    helper.setText(R.id.task_is_complete, "已完成" + progress + "%");
                } else if (progress == 100) {
                    helper.setText(R.id.btn_stauts, "再做一次");
                    helper.setText(R.id.task_is_complete, "已完成" + progress + "%");
                }*//*
                *//*switch (((Map) item).get("cid").toString()) {
                    case "1":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task01);
                        helper.setText(R.id.task_catename, "类型：视频");
                        break;
                    case "5":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task02);
                        helper.setText(R.id.task_catename, "类型：PPT");
                        break;
                    case "3":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task03);
                        helper.setText(R.id.task_catename, "类型：图文");
                        break;
                    default:
                        break;
                }*//*

                switch (((Map) item).get("cid").toString()) {
                    case "1":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task01);
//                        helper.setText(R.id.task_catename, "类型：视频");

                        SpannableString spannableStringVideo = new SpannableString("类型：视频"+title);
                        spannableStringVideo.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringVideo);
                        break;
                    case "5":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task02);
//                        helper.setText(R.id.task_catename, "类型：文档");

                        SpannableString spannableStringFile = new SpannableString("类型：文档"+title);
                        spannableStringFile.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringFile);
                        break;
                    case "3":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task03);
//                        helper.setText(R.id.task_catename, "类型：图文");

                        SpannableString spannableStringTxt = new SpannableString("类型：图文"+title);
                        spannableStringTxt.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringTxt);
                        break;
                    case "6":
                        //helper.setText(R.id.task_catename, "类型：音频");

                        SpannableString spannableStringAudio = new SpannableString("类型：音频"+title);
                        spannableStringAudio.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringAudio);
                        break;
                    default:
                        break;
                }
            }
        };*/

        baseListAdapter = new BaseListAdapter(expired_list, data, R.layout.item_learning_task_1) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                String title = ((Map) item).get("tasktitle") + "";
                /*if (title.length() <= 11) {
                    helper.setText(R.id.task_title1, Html.fromHtml(title));
                    helper.setText(R.id.task_title2, "");
                } else {
                    helper.setText(R.id.task_title1, Html.fromHtml(title.substring(0, 11)));
                    helper.setText(R.id.task_title2, Html.fromHtml(title.substring(11)));
                }*/
                helper.setText(R.id.task_title1, Html.fromHtml(((Map) item).get("tasktitle") + ""));
                helper.setVisibility(R.id.schedule_layout, View.GONE);
                helper.setText(R.id.task_starttime, ((Map) item).get("starttime") + "");
//                helper.setText(R.id.task_endtime, ((Map) item).get("endtime") + "");
                helper.setText(R.id.learning_task_id, ((Map) item).get("taskid") + "");
                helper.setText(R.id.learning_task_cid, ((Map) item).get("cid") + "");
                helper.setText(R.id.learning_task_pages, ((Map) item).get("pages") + "");
//                helper.setRoundProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"), ((Map) item).get("complete") + "");
                helper.setProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"));
                //helper.setVisibility(R.id.btn_stauts, View.INVISIBLE);
                helper.setVisibility(R.id.btn_stauts, View.GONE);
                helper.setVisibility(R.id.is_qualified, View.VISIBLE);
                if (Integer.parseInt(((Map) item).get("is_look").toString()) > 0) {
                    helper.setVisibility(R.id.is_look, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.is_look, View.GONE);
                }
                String qualified = (String) ((Map) item).get("status");
                if (qualified.equals("合格")) {
                    helper.setImageResource(R.id.is_qualified, R.mipmap.ic_learning_qualified);
                } else if (qualified.equals("不合格")) {
                    helper.setImageResource(R.id.is_qualified, R.mipmap.ic_learning_unqualified);
                } else {
                    helper.setVisibility(R.id.is_qualified, View.INVISIBLE);
                }
                int progress = (Integer) ((Map) item).get("rate_of_progress");
                if (progress == 0) {
//                    helper.setText(R.id.btn_stauts, "去完成");
                    helper.setText(R.id.task_is_complete, "已完成0%");
                } else if (0 < progress && progress < 100) {
//                    helper.setText(R.id.btn_stauts, "继续任务");
                    helper.setText(R.id.task_is_complete, "已完成" + progress + "%");
                } else if (progress == 100) {
//                    helper.setText(R.id.btn_stauts, "再做一次");
                    helper.setText(R.id.task_is_complete, "已完成" + progress + "%");
                }
                /*switch (((Map) item).get("cid").toString()) {
                    case "1":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task01);
                        helper.setText(R.id.task_catename, "类型：视频");
                        break;
                    case "5":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task02);
                        helper.setText(R.id.task_catename, "类型：PPT");
                        break;
                    case "3":
                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task03);
                        helper.setText(R.id.task_catename, "类型：图文");
                        break;
                    default:
                        break;
                }*/

                switch (((Map) item).get("cid").toString()) {
                    case "1":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_video);
                        helper.setText(R.id.task_catename, "视频");
                        break;
                    case "5":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_wrod);
                        helper.setText(R.id.task_catename, "文档");
                        break;
                    case "3":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_file);
                        helper.setText(R.id.task_catename, "图文");
                        break;
                    case "6":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_audio);
                        helper.setText(R.id.task_catename, "音频");
                        break;
                    default:
                        break;
                }
            }
        };

        expired_list.setAdapter(baseListAdapter);
        // listview点击事件
        expired_list.setOnItemClickListener(new learning_task_listListener());

        //分页加载
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isNoMore && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && page < pages) {
                    //页数加1,请求数据
                    page++;
                    setValue();
//                    Log.e("page:", page + "");

                    isNoMore = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //判断是否滚到最后一行
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    isNoMore = true;
                }
            }
        });
    }

    /**
     * name: 点击查看某个任务详细
     */
    private class learning_task_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
            TextView textView = (TextView) view.findViewById(R.id.learning_task_id);
            String tid = textView.getText().toString();
            TextView textView2 = (TextView) view.findViewById(R.id.learning_task_cid);
            String cid = textView2.getText().toString();
            // MyProgressBarDialogTools.show(context);
            //跳转到任务详情
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            intent.putExtra("taskid", tid);
            intent.putExtra("cid", cid);
            intent.putExtra("expired", "1");
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        page = 1;
        data.removeAll(data);
        baseListAdapter.notifyDataSetChanged();
        setValue();
        super.onResume();
    }

    @Override
    public void onPause() {
        pause++;
        super.onPause();
    }
}
