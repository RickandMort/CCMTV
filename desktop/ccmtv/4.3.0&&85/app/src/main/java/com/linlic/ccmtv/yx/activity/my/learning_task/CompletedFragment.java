package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
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
import com.linlic.ccmtv.yx.utils.permission.PermissionCheckLinstenterImpl;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 未开始 学习任务
 */
public class CompletedFragment extends BaseFragment {
    private ListView completed_list;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private boolean isNoMore = false;
    private int page = 1;
    private int pause = 0;
    private int pages = 0;
    private NodataEmptyLayout lt_nodata2;
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
                                map.put("is_look", customJson.getString("is_look"));
                                map.put("msg_title", customJson.getString("msg_title"));
                                map.put("msg_content", customJson.getString("msg_content"));
                                map.put("rate_of_progress", customJson.has("rate_of_progress") ? customJson.getInt("rate_of_progress") : 0);
                                data.add(map);
                            }

                            pages = jsonObject.getInt("pages");
//                            if (data.size() > 0) {
//                                completed_list.setVisibility(View.VISIBLE);
//                                lt_nodata2.setVisibility(View.GONE);
//                            } else {
//                                completed_list.setVisibility(View.GONE);
//                                lt_nodata2.setVisibility(View.VISIBLE);
//                            }
                            /*if(dataArray.length()<10){
                                isNoMore = true;
                            }*/

                        } else {
                            isNoMore = true;
//                            if (data.size() > 0) {
//                                completed_list.setVisibility(View.VISIBLE);
//                                lt_nodata2.setVisibility(View.GONE);
//                            } else {
//                                completed_list.setVisibility(View.GONE);
//                                lt_nodata2.setVisibility(View.VISIBLE);
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
            completed_list.setVisibility(View.VISIBLE);
            lt_nodata2.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata2.setNetErrorIcon();
            } else {
                lt_nodata2.setLastEmptyIcon();
            }
            completed_list.setVisibility(View.GONE);
            lt_nodata2.setVisibility(View.VISIBLE);
        }
    }

    private Context context;
    private PermissionCheckLinstenterImpl onPermissionCheckListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        initView();
        setValue();
        initData();
    }

    private void setValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getNoStartTask);
                    obj.put("uid", SharedPreferencesTools.getUid(getActivity()));
                    obj.put("page", page);
                    obj.put("title", LearningTaskActivity.keyword.length() > 0 ? LearningTaskActivity.keyword : "");
                    obj.put("type", LearningTaskActivity.poptype.length() > 0 ? LearningTaskActivity.poptype : "");
                    obj.put("version", "1");
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.Learning_task, obj.toString());
//                    Log.e("看看completed数据", result);

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
        completed_list = (ListView) getActivity().findViewById(R.id.completed_list);
        lt_nodata2 = (NodataEmptyLayout) getActivity().findViewById(R.id.lt_nodata2);
    }

    private void initData() {
//        baseListAdapter = new BaseListAdapter(completed_list, data, R.layout.item_learning_task) {
//            @Override
//            public void refresh(Collection datas) {
//                super.refresh(datas);
//
//            }
//
//            @Override
//            public void convert(ListHolder helper, Object item, boolean isScrolling) {
//                super.convert(helper, item, isScrolling);
//                String title = ((Map) item).get("tasktitle") + "";
//                /*if (title.length() <= 11) {
//                    helper.setText(R.id.task_title1, Html.fromHtml(title));
//                    helper.setText(R.id.task_title2, "");
//                } else {
//                    helper.setText(R.id.task_title1, Html.fromHtml(title.substring(0, 11)));
//                    helper.setText(R.id.task_title2, Html.fromHtml(title.substring(11)));
//                }*/
//                //helper.setText(R.id.task_title1, Html.fromHtml(((Map) item).get("tasktitle") + ""));
//                helper.setText(R.id.task_starttime, ((Map) item).get("starttime") + "");
//                helper.setText(R.id.task_endtime, ((Map) item).get("endtime") + "");
//                helper.setText(R.id.learning_task_id, ((Map) item).get("taskid") + "");
//                helper.setText(R.id.learning_task_cid, ((Map) item).get("cid") + "");
//                helper.setText(R.id.learning_task_pages, ((Map) item).get("pages") + "");
//                helper.setRoundProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"), "未开始");
//                helper.setVisibility(R.id.schedule_layout, View.VISIBLE);
//                //测试使用  增加点击事件 添加日程
//                helper.setAdd_scheduleOnClick(R.id.schedule, ((Map) item).get("starttime") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("taskid") + "", ((Map) item).get("msg_content").toString(), R.id.schedule_img, R.id.schedule_layout);
//                helper.findCalendarReminder(R.id.schedule, ((Map) item).get("starttime") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("taskid") + "", R.id.schedule_img, R.id.schedule_layout);
//                if (Integer.parseInt(((Map) item).get("is_look").toString()) > 0) {
//                    helper.setVisibility(R.id.is_look, View.VISIBLE);
//                } else {
//                    helper.setVisibility(R.id.is_look, View.GONE);
//                }
//                //helper.setBackground_Image(R.id.btn_stauts, R.mipmap.video_sign2);
//
//                switch (((Map) item).get("cid").toString()) {
//                    case "1":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task01);
////                        helper.setText(R.id.task_catename, "类型：视频");
//
//                        SpannableString spannableStringVideo = new SpannableString("类型：视频"+title);
//                        spannableStringVideo.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        helper.setText(R.id.task_title1, spannableStringVideo);
//                        break;
//                    case "5":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task02);
////                        helper.setText(R.id.task_catename, "类型：文档");
//
//                        SpannableString spannableStringFile = new SpannableString("类型：文档"+title);
//                        spannableStringFile.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        helper.setText(R.id.task_title1, spannableStringFile);
//                        break;
//                    case "3":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task03);
////                        helper.setText(R.id.task_catename, "类型：图文");
//
//                        SpannableString spannableStringTxt = new SpannableString("类型：图文"+title);
//                        spannableStringTxt.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        helper.setText(R.id.task_title1, spannableStringTxt);
//                        break;
//                    case "6":
//                        //helper.setText(R.id.task_catename, "类型：音频");
//
//                        SpannableString spannableStringAudio = new SpannableString("类型：音频"+title);
//                        spannableStringAudio.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        helper.setText(R.id.task_title1, spannableStringAudio);
//                        break;
//                    default:
//                        break;
//                }
//                /*switch (((Map) item).get("cid").toString()) {
//                    case "1":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task01);
//                        helper.setText(R.id.task_catename, "类型：视频");
//                        break;
//                    case "5":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task02);
//                        helper.setText(R.id.task_catename, "类型：PPT");
//                        break;
//                    case "3":
//                        //helper.setImageResource(R.id.task_image, R.mipmap.learning_task03);
//                        helper.setText(R.id.task_catename, "类型：图文");
//                        break;
//                    default:
//                        break;
//                }*/
//            }
//        };

        baseListAdapter = new BaseListAdapter(completed_list, data, R.layout.item_learning_task_1) {
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
                helper.setText(R.id.task_starttime, ((Map) item).get("starttime") + "至" + ((Map) item).get("endtime"));
//                helper.setText(R.id.task_endtime, ((Map) item).get("endtime") + "");
                helper.setText(R.id.learning_task_id, ((Map) item).get("taskid") + "");
                helper.setText(R.id.learning_task_cid, ((Map) item).get("cid") + "");
                helper.setText(R.id.learning_task_pages, ((Map) item).get("pages") + "");
//                helper.setRoundProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"), "未开始");
                helper.setProgress(R.id.progress_bar, (Integer) ((Map) item).get("rate_of_progress"));
                helper.setVisibility(R.id.btn_stauts, View.GONE);
                helper.setVisibility(R.id.ll_task_progress, View.GONE);
                helper.setVisibility(R.id.schedule_layout, View.VISIBLE);

                //测试使用  增加点击事件 g
                onPermissionCheckListener = new PermissionCheckLinstenterImpl(context);
                helper.setAdd_scheduleOnClick2(R.id.schedule, ((Map) item).get("starttime") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("taskid") + "", ((Map) item).get("msg_content").toString(), R.id.schedule_img, R.id.schedule_layout, onPermissionCheckListener);
                helper.findCalendarReminder2(R.id.schedule, ((Map) item).get("starttime") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("taskid") + "", R.id.schedule_img, R.id.schedule_layout);
                if (Integer.parseInt(((Map) item).get("is_look").toString()) > 0) {
                    helper.setVisibility(R.id.is_look, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.is_look, View.GONE);
                }
                //helper.setBackground_Image(R.id.btn_stauts, R.mipmap.video_sign2);

                switch (((Map) item).get("cid").toString()) {
                    case "1":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_video);
                        helper.setText(R.id.task_catename, "视频");

                        /*SpannableString spannableStringVideo = new SpannableString("类型：视频"+title);
                        spannableStringVideo.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringVideo);*/
                        break;
                    case "5":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_wrod);
                        helper.setText(R.id.task_catename, "文档");

                        /*SpannableString spannableStringFile = new SpannableString("类型：文档"+title);
                        spannableStringFile.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringFile);*/
                        break;
                    case "3":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_file);
                        helper.setText(R.id.task_catename, "图文");

                        /*SpannableString spannableStringTxt = new SpannableString("类型：图文"+title);
                        spannableStringTxt.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringTxt);*/
                        break;
                    case "6":
                        helper.setImageResource(R.id.task_image, R.mipmap.ic_learning_task_list_audio);
                        helper.setText(R.id.task_catename, "音频");


                        /*SpannableString spannableStringAudio = new SpannableString("类型：音频"+title);
                        spannableStringAudio.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#3897F9"), Color.parseColor("#3897F9")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        helper.setText(R.id.task_title1, spannableStringAudio);*/
                        break;
                    default:
                        break;
                }
            }
        };

        completed_list.setAdapter(baseListAdapter);
        // listview点击事件
        completed_list.setOnItemClickListener(new learning_task_listListener());

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
            intent.putExtra("expired", "2");
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        if (pause > 0) {
            page = 1;
            data.removeAll(data);
            setValue();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        pause++;
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (onPermissionCheckListener != null) onPermissionCheckListener.closeCheckDialog();
        super.onDestroyView();
    }
}