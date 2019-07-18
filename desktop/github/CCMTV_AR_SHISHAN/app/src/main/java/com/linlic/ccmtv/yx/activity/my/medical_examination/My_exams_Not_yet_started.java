package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.linlic.ccmtv.yx.utils.CalendarReminderUtils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.permission.PermissionCheckLinstenterImpl;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试列表-未开始
 * Created by Administrator on 2017/9/6.
 */
public class My_exams_Not_yet_started extends BaseFragment {
    private Context context;
    private ListView exams_list2;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private boolean isNoMore = false;
    private int page = 1;
    private int pause = 0;
    private NodataEmptyLayout rl_nodata2;
    //    private SearchView editText1;
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
                                map.put("my_exams_name", customJson.getString("paper_name"));
                                map.put("my_exams_take", customJson.getString("paper_status"));
                                map.put("my_exams_time", customJson.getString("paper_time"));
                                map.put("my_exams_id", customJson.getString("pid"));
                                map.put("calendar_mth", customJson.getString("calendar_mth"));
                                map.put("calendar_day", customJson.getString("calendar_day"));
                                map.put("calendar_new", customJson.getString("show_type"));
                                map.put("type", customJson.getString("type"));
                                map.put("type_name", customJson.getString("type_name"));
                                map.put("msg_title", customJson.getString("msg_title"));
                                map.put("msg_content", customJson.getString("msg_content"));
                                map.put("start_end_time", customJson.getString("start_end_time"));
                                map.put("is_exercise", customJson.getString("is_exercise"));//1为模拟考试 0为正式考试
                                map.put("status", customJson.getString("status"));//为1时表示补考考试
                                map.put("is_cheat", customJson.has("is_cheat")?customJson.getString("is_cheat"):0);//为1时表示作弊
                                data.add(map);
                            }
//                            if (data.size() > 0) {
//                                exams_list2.setVisibility(View.VISIBLE);
//                                rl_nodata2.setVisibility(View.GONE);
//                            } else {
//                                exams_list2.setVisibility(View.GONE);
//                                rl_nodata2.setVisibility(View.VISIBLE);
//                            }
                            if (dataArray.length() < 10) {
                                isNoMore = true;
                            }
                        } else {
                            isNoMore = true;
//                            if (data.size() > 0) {
//                                exams_list2.setVisibility(View.VISIBLE);
//                                rl_nodata2.setVisibility(View.GONE);
//                            } else {
//                                exams_list2.setVisibility(View.GONE);
//                                rl_nodata2.setVisibility(View.VISIBLE);
//                            }
//                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                        setResultStatus(data.size() > 0, jsonObject.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    setResultStatus(data.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;

                default:
                    break;
            }

        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            exams_list2.setVisibility(View.VISIBLE);
            rl_nodata2.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_nodata2.setNetErrorIcon();
            } else {
                rl_nodata2.setLastEmptyIcon();
            }
            exams_list2.setVisibility(View.GONE);
            rl_nodata2.setVisibility(View.VISIBLE);
        }
    }

    private PermissionCheckLinstenterImpl onPermissionCheckListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_exams2, container, false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getContext();
        findId();
        initdata();
        setValue2();
    }

    /***
     * 添加日程
     */
    public void Add_schedule() {
//        Log.e("调用日程","新增日程");

        long startMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 3, 8, 15, 30);  //注意，月份的下标是从0开始的
        startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
        CalendarReminderUtils.addCalendarEvent(context, "见导师", "去实验室见研究生导师", startMillis, 1);


    }


    @Override
    public void findId() {
        super.findId();
        exams_list2 = (ListView) getActivity().findViewById(R.id.exams_list2);
        rl_nodata2 = (NodataEmptyLayout) getActivity().findViewById(R.id.rl_nodata2);

//        editText1 = (SearchView) getActivity().findViewById(R.id.editText1);
    }

    public void initdata() {
        baseListAdapter = new BaseListAdapter(exams_list2, data, R.layout.exams_list_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);

                helper.setText(R.id.my_exams_name, ((Map) item).get("my_exams_name") + "", "html");
                helper.setText(R.id.my_exams_time, ((Map) item).get("my_exams_time") + "");
                helper.setText(R.id.my_exams_id, ((Map) item).get("my_exams_id") + "");
                helper.setText(R.id.my_exams_time, ((Map) item).get("start_end_time") + "");
                helper.setText(R.id.fraction, ((Map) item).get("fraction") + "");//分数

                helper.setVisibility(R.id.schedule_layout, View.VISIBLE);
                helper.setVisibility(R.id.fraction_layout, View.GONE);
                //测试使用  增加点击事件 添加日程
                onPermissionCheckListener = new PermissionCheckLinstenterImpl(context);
                helper.setAdd_scheduleOnClick(R.id.schedule, ((Map) item).get("my_exams_time") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("my_exams_id") + "", ((Map) item).get("msg_content").toString(), R.id.schedule_img, R.id.schedule_layout, onPermissionCheckListener);
                helper.findCalendarReminder(R.id.schedule, ((Map) item).get("my_exams_time") + "", ((Map) item).get("msg_title").toString(), ((Map) item).get("my_exams_id") + "", R.id.schedule_img, R.id.schedule_layout);

                if (Integer.parseInt(((Map) item).get("calendar_new").toString()) > 0) {
                    helper.setVisibility(R.id.calendar_new, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.calendar_new, View.GONE);
                }
                if ( ((Map) item).get("status").toString().equals("1") ) {
                    helper.setVisibility(R.id.exams_type3, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.exams_type3, View.GONE);
                }

                if ( ((Map) item).get("is_exercise").toString().equals("1") ) {
                    helper.setVisibility(R.id.exams_type2, View.VISIBLE);
                    helper.setVisibility(R.id.exams_type1, View.GONE);
                } else {
                    helper.setVisibility(R.id.exams_type2, View.GONE);
                    helper.setVisibility(R.id.exams_type1, View.VISIBLE);
                }

                    helper.setVisibility(R.id.leantextview, View.GONE);

            }
        };
        exams_list2.setAdapter(baseListAdapter);
        // listview点击事件
        exams_list2.setOnItemClickListener(new casesharing_listListener());

        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {

                }
//                System.out.println("当前条目位置："+firstVisibleItem + "   当前屏幕容纳条目数：" + visibleItemCount + "      总条目数:" + totalItemCount);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = exams_list2.getChildAt(0);

                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                        Log.d("ListView", "<----滚动到顶部----->");
                        isNoMore = false;
                    }

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

                    View lastVisibleItemView = exams_list2.getChildAt(exams_list2.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == exams_list2.getHeight()) {
//                        System.out.println("#####滚动到底部######" + !isNoMore);
                        if (!isNoMore) {
                            page += 1;
                            setValue2();
                        }
                    }
                }
            }
        });


//        editText1.setOnKeyListener(new View.OnKeyListener() {
//
//            @Override
//
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    //以下方法防止两次发送请求
//
//                    // 先隐藏键盘
//                    ((InputMethodManager)getActivity(). getSystemService(getActivity().INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
//                    //存储数据
//                    page = 1;
//                    data.removeAll(data);
//                    setValue2();
//                }
//                return false;
//            }
//        });
//
//        editText1.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
//                Drawable drawable = editText1.getCompoundDrawables()[2];
//                //如果右边没有图片，不再处理
//                if (drawable == null)
//                    return false;
//                //如果不是按下事件，不再处理
//                if (event.getAction() != MotionEvent.ACTION_UP)
//                    return false;
//                if (event.getX() > editText1.getWidth()
//                        - editText1.getPaddingRight()
//                        - drawable.getIntrinsicWidth()) {
//                    editText1.setText("");
//                    editText1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                    editText1.clearFocus();
//
//                    page = 1;
//                    data.removeAll(data);
//                    setValue2();
//                }
//                return false;
//            }
//        });
//
//        editText1.addTextChangedListener( mTextWatcher);
    }


    /**
     * name: 点击查看某个考试详细
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.my_exams_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            //跳转到考试详情
            Intent intent = new Intent(context, Examination_instructions.class);
            intent.putExtra("my_exams_id", id);
            startActivity(intent);

        }

    }

    public void setValue2() {
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getPaperList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("type", "2");//未开始
                    obj.put("keyWord", My_exams_LearningTaskActivity.keyword.length() > 0 ? My_exams_LearningTaskActivity.keyword : "");

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    Log.e("看看考试列表数据2", result);

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

    @Override
    public void onResume() {
        if (pause > 0) {
            if (My_exams_LearningTaskActivity.istop) {
                page = 1;
                data.removeAll(data);
                setValue2();
            }
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

    /*  TextWatcher mTextWatcher = new TextWatcher()  {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 10;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           *//* if (DEBUG)
                Log.i(TAG, "输入文本之前的状态");
            ;*//*
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
         *//*   if (DEBUG)
                Log.i(TAG, "输入文字中的状态，count是一次性输入字符数");
            mTvAvailableCharNum.setText("还能输入" + (charMaxNum - s.length()) + "字符");*//*

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().trim().length()>0) {
                editText1.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.search_clear_normal),null);
            }else{
                editText1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            }

        }
    };*/
}
