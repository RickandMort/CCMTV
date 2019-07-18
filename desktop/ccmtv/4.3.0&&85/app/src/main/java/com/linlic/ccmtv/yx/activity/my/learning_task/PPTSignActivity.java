package com.linlic.ccmtv.yx.activity.my.learning_task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.NotSignDialog;
import com.linlic.ccmtv.yx.activity.my.medical_examination.Examination_instructions2;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PPTSignActivity extends BaseActivity {

    private Context context;
    private TimerTextView mTextView;
    private TextView title_name, ppt_title, current_page, total_page, start_time, end_time, ppt_time,
            sign_t1, sign_t2, sign_t3, sign_t4, video_s3, video_s1, time_empty;
    private ImageView ppt_image, ppt_left, ppt_right;
    private LinearLayout ll_s1, video_sign_i1;
    private String tid = "";
    private String pptid = "";
    public String signnum = "";
    public String is_sign = "";
    public String sign_num = "";
    public String be_sign_number = "";
    private String task_type, task_fixed, task_sign;
    private static EditText ppt_signNum;
    private ViewPager pptViewPager;
    private PptItemAdapter adapter;
    private List<String> list = new ArrayList();
    List<Map<String, Object>> data;
    List<Map<String, Object>> slist = new ArrayList();
    public ArrayList urls_teshujiancha = new ArrayList();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private int page;

    private List<Map<String, Object>> signTimeList=new ArrayList<>();
    private String is_miss = "0";
    //private JSONArray jsonArraySignTime = new JSONArray();
    private JSONObject Json = new JSONObject();
    private ListView lv_ppt;
    private int signCount = 0;
    private boolean isClickSign = false;
    private List<CountDownTimer> timerList=new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");

                        if (jsonObject.getInt("status") == 0) {
                            Toast.makeText(PPTSignActivity.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject taskObject = jsonObject.getJSONObject("task_info");
                        ppt_title.setText(taskObject.getString("tasktitle"));
                        start_time.setText(taskObject.getString("starttime"));
                        end_time.setText(taskObject.getString("endtime"));
                        ppt_time.setText(taskObject.getString("ppt_time"));
                        video_s1.setText(taskObject.getString("sign_num"));
                        video_s3.setText(jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0");
                        SharedPreferencesTools.saveSign(context, jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0");
                        SharedPreferencesTools.saveSign_in_num(context, "0");
                        SharedPreferencesTools.saveSign_Time_Json(context,"");
                        sign_num = taskObject.getString("sign_num");
                        is_sign = jsonObject.has("sign") ? jsonObject.getString("sign") : "2";
                        be_sign_number = jsonObject.has("be_sign_number") ? jsonObject.getString("be_sign_number") : "0";
                        is_miss = taskObject.getString("is_miss");
                        String type = taskObject.getString("task_ppt_type");
//                        Log.e("type", type);
                        if (type.equals("3")) {//签到+测试
                            video_sign_i1.setVisibility(View.VISIBLE);
                            if (video_s1.getText().toString().equals(video_s3.getText().toString())) {
                                video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                                video_sign_i1.setClickable(true);
                                video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Examination_instructions2.class);
                                        intent.putExtra("aid", pptid);
                                        intent.putExtra("tid", tid);
                                        intent.putExtra("type", "ppt");
                                        intent.putExtra("pptid", pptid);
                                        startActivity(intent);
                                    }
                                });
                            } else {
                                video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                                video_sign_i1.setClickable(false);
                            }
                        } else if (type.equals("2")) {//不显示签到
                            ll_s1.setVisibility(View.GONE);//签到
                            video_sign_i1.setVisibility(View.VISIBLE);//测试
                            video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                            video_sign_i1.setClickable(true);
                            video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, Examination_instructions2.class);
                                    intent.putExtra("aid", pptid);
                                    intent.putExtra("tid", tid);
                                    intent.putExtra("type", "ppt");
                                    intent.putExtra("pptid", pptid);
                                    startActivity(intent);
                                }
                            });

                        } else if (type.equals("1")) {//不显示测试
                            video_sign_i1.setVisibility(View.GONE);
                        }

                        JSONArray urlArray = jsonObject.getJSONArray("ppt_img_url");
                        if (urlArray.length() > 0) {
                            for (int i = 0; i < urlArray.length(); i++) {
                                String customJson = urlArray.getString(i);
                                list.add(customJson);
                                urls_teshujiancha.add(customJson);
                            }
                        } else {
                            Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                        }

                        if (is_sign.equals("0")) {
                            try {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                Iterator<String> keys = dataObject.keys();
                                while (keys.hasNext()) {
                                    Map<String, Object> map = new HashMap<>();
                                    String str = keys.next();
                                    map.put(str, dataObject.getString(str));
                                    slist.add(map);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //list获取完成后加载数据
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);

                    break;
                case 2:
                    //子线程请求数据是耗时操作
                    //请求完毕后执行下面的方法
                    String expired1 = getIntent().getStringExtra("expired");
                    String expired = expired1.equals("1") ? expired1 : "2";
                    initData();
                    //判断是不是从已结束列表跳转
                    if (expired.equals("1")) {//是：只能看ppt没有其他功能
                        time_empty.setVisibility(View.VISIBLE);
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                        ll_s1.setClickable(false);
                        video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                        video_sign_i1.setClickable(false);
                    } else {
                        if (is_sign.equals("0")) {
                            startTimer();
                            time_empty.setVisibility(View.GONE);
                        } else {
                            time_empty.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        String type = jsonObject.getString("type" + "");
                        if (jsonObject.getInt("status") == 1) {
//                            SharedPreferencesTools.saveSign_in_num(context,(Integer.parseInt(SharedPreferencesTools.getSign_in_num(context))-1)+"");
                            SharedPreferencesTools.saveSign_in_num(context, "0");
                            if (type.equals("1")) {//显示自测
                                int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                                if (num > Integer.parseInt(sign_num)) {
                                    video_s3.setText(sign_num);
                                } else {
                                    video_s3.setText(num + "");
                                }
                                SharedPreferencesTools.saveSign(context, num + "");
                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_s1.setClickable(false);
                                video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                                video_sign_i1.setClickable(true);
                                video_sign_i1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, Examination_instructions2.class);
                                        intent.putExtra("aid", pptid);
                                        intent.putExtra("tid", tid);
                                        intent.putExtra("type", "ppt");
                                        intent.putExtra("pptid", pptid);
                                        startActivity(intent);
                                    }
                                });
                            } else if (type.equals("0")) {//不显示自测
                                int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                                video_s3.setText(num + "");
                                SharedPreferencesTools.saveSign(context, num + "");
                                Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                                ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                ll_s1.setClickable(false);
                                video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                                video_sign_i1.setClickable(false);
                            }
                        } else {
                            Toast.makeText(context, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        int num = Integer.parseInt(video_s3.getText().toString()) + 1;
                        video_s3.setText(num + "");
                        SharedPreferencesTools.saveSign(context, num + "");
                        SharedPreferencesTools.saveSign_in_num(context, "0");
                        Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                        ll_s1.setClickable(false);
                        video_sign_i1.setBackgroundResource(R.mipmap.video_sign2);
                        video_sign_i1.setClickable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };
    private CountDownTimer timer1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pptsign);

        context = this;
        initView();
        setValue();
    }

    private void setValue() {
        tid = getIntent().getStringExtra("tid");
        pptid = getIntent().getStringExtra("pptid");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getTaskPptInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("tid", tid);
                    obj.put("pptid", pptid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                    LogUtil.e("看看ppt数据", result);

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

    private void initData() {
        data = getData();
        page = 1;
        title_name.setText("PPT详情");
        ppt_image.setImageResource(R.mipmap.learning_task02);
        try {
            current_page.setText(page + "");
            setEditTextRange(context, ppt_signNum, 1, list.size());
            total_page.setText(list.size() + "");
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher) // 在ImageView加载过程中显示图片
                    .showImageForEmptyUri(R.drawable.ic_launcher) // image连接地址为空时
                    .showImageOnFail(R.drawable.ic_launcher) // image加载失败
                    .cacheInMemory(true) // 加载图片时会在内存中加载缓存
                    .cacheOnDisc(true) // 加载图片时会在磁盘中加载
                    .build();
//            Log.e("list11:", data.toString());
            adapter = new PptItemAdapter(list);
            lv_ppt.setAdapter(adapter);
            lv_ppt.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    LogUtil.e("滑动：",firstVisibleItem+" 和 "+visibleItemCount+ " 和"+totalItemCount);
                    current_page.setText(""+(firstVisibleItem+visibleItemCount));
                }
            });
            lv_ppt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, PicViewerActivity.class);
                    intent.putExtra("urls_case", urls_teshujiancha);
                    intent.putExtra("current_index", position);
                    intent.putExtra("sign_num", sign_num);
                    intent.putExtra("be_sign_number", be_sign_number);
                    intent.putExtra("tid", tid);
                    intent.putExtra("type", "ppt");
                    intent.putExtra("is_miss",is_miss);
                    /*urls_teshujiancha.add(viewLists.get(position).get("url").toString());
                    intent.putExtra("urls_case", urls_teshujiancha);
                    intent.putExtra("current_index", 0);*/
                    startActivityForResult(intent, 0);
                }
            });
            //pptViewPager.setAdapter(adapter);
            //ViewPager中图片点击放大
            /*pptViewPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PicViewerActivity.class);
                    ArrayList urls_teshujiancha = new ArrayList();
                    urls_teshujiancha.add(list.get(page));//图片url
                    intent.putExtra("urls_case", urls_teshujiancha);
                    intent.putExtra("current_index", 0);
                    intent.putExtra("sign_num", sign_num);
                    intent.putExtra("be_sign_number", be_sign_number);
                    intent.putExtra("tid", tid);
                    intent.putExtra("type", "my_case");
                    startActivity(intent);
                }
            });*/
            //ViewPager的左右滑动事件监听
            /*pptViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private int currentPosition = 0;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position > currentPosition) {
                        //右滑
                        Log.e("direction", "right");
                        currentPosition = position;
                    } else if (position < currentPosition) {
                        //左滑
                        Log.e("direction", "left");
                        currentPosition = position;
                    }
                    int num = pptViewPager.getCurrentItem() + 1;
                    current_page.setText(num + "");
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });*/

            //点击系统键盘的事件监听
            ppt_signNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String s = ppt_signNum.getText().toString();
                    if (!TextUtils.isEmpty(s)) {
                        final int p = Integer.parseInt(s);
//                        Log.e("page值：", "actionId"+actionId);
                        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                            current_page.setText(s);
                            //pptViewPager.setCurrentItem(p - 1);
                            //lv_ppt.requestFocusFromTouch();//获取焦点
                            lv_ppt.setSelection(p - 1);
                            //adapter.notifyDataSetChanged();

                            /*lv_ppt.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    lv_ppt.requestFocusFromTouch();//获取焦点
                                    lv_ppt.setSelection(p - 1);
                                }
                            },500);*/
                        }
                        //隐藏软键盘
//                        Log.e("page值：", page + "");
                        return false;
                    } else {
                        return false;
                    }
                }
            });

            //点击“<”往前一页
            /*ppt_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pptViewPager.getCurrentItem() != 0) {
                        pptViewPager.setCurrentItem(pptViewPager.getCurrentItem() - 1);
                    } else {
                        Toast.makeText(context, "当前为第一页", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //点击“>”往后一页
            ppt_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pptViewPager.getCurrentItem() != (list.size() - 1)) {
                        pptViewPager.setCurrentItem(pptViewPager.getCurrentItem() + 1);
                    } else {
                        Toast.makeText(context, "当前为最后一页", Toast.LENGTH_SHORT).show();
                    }
                }
            });*/

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "暂无数据，请稍后尝试", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mTextView = (TimerTextView) findViewById(R.id.timer);
        lv_ppt = (ListView) findViewById(R.id.id_lv_ppt_sign);
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ppt_title = (TextView) findViewById(R.id.ppt_sign_title);
        ppt_image = (ImageView) findViewById(R.id.ppt_sign_image);
        ppt_signNum = (EditText) findViewById(R.id.ppt_sign_pageNum);
        //pptViewPager = (ViewPager) findViewById(R.id.pptViewPager);
        current_page = (TextView) findViewById(R.id.ppt_pageNum);
        total_page = (TextView) findViewById(R.id.ppt_total_page);
        start_time = (TextView) findViewById(R.id.ppt_sign_starttime);
        end_time = (TextView) findViewById(R.id.ppt_sign_endtime);
        ppt_time = (TextView) findViewById(R.id.ppt_time);
        //ppt_left = (ImageView) findViewById(R.id.ppt_left);
        //ppt_right = (ImageView) findViewById(R.id.ppt_right);
        ll_s1 = (LinearLayout) findViewById(R.id.ll_s1);
        video_sign_i1 = (LinearLayout) findViewById(R.id.video_sign_i1);
        sign_t1 = (TextView) findViewById(R.id.sign_t1);
        sign_t2 = (TextView) findViewById(R.id.sign_t2);
        sign_t3 = (TextView) findViewById(R.id.sign_t3);
        sign_t4 = (TextView) findViewById(R.id.sign_t4);
        video_s1 = (TextView) findViewById(R.id.video_s1);
        video_s3 = (TextView) findViewById(R.id.video_s3);
        time_empty = (TextView) findViewById(R.id.time_empty);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        super.onPause();
    }

    public void back(View view) {
        finish();
    }

    static public void setEditTextRange(final Context context, final EditText editText, final int min, final int max) {
        ppt_signNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入之前
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int num = Integer.parseInt(s.toString());
                    //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                    if (num > max) {
                        s = String.valueOf(max);//如果大于max，则内容为max
                        ppt_signNum.setText(s);
                    } else if (num < min) {
                        s = String.valueOf(min);//如果小于min,则内容为min
                        ppt_signNum.setText(s);
                    }
                } catch (NumberFormatException e) {
//                    Log.e("ontextchanged=", e.toString());
                }
                //edittext中的数字在max和min之间，则不做处理
                return;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入之后
            }
        });
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", list.get(i));
            //map.put("view", new ImageView(this));
            mdata.add(map);
        }
        return mdata;
    }

    public class PptItemAdapter extends BaseAdapter {

        private List<String> viewLists;

        public PptItemAdapter(List<String> viewLists) {
            this.viewLists = viewLists;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public Object getItem(int position) {
            return viewLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_layout_ppt_sign, null);
                holder.ivPpt = (ImageView) convertView.findViewById(R.id.id_iv_item_ppt_sign);
                convertView.setTag(holder);
            } else {
                //直接通过holder获取下面三个子控件，不必使用findviewbyid，加快了 UI 的响应速度
                holder = (ViewHolder) convertView.getTag();
            }

            if (viewLists.get(position) != null) {
                RequestOptions options = new RequestOptions().error(R.mipmap.img_default);
                Glide.with(context)
                        .load(viewLists.get(position).toString())
                        .apply(options)
                        .into(holder.ivPpt);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView ivPpt;
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        List<Map<String, Object>> viewLists;

        public ViewPagerAdapter(List<Map<String, Object>> lists) {
            viewLists = lists;
        }

        @Override
        public int getCount() {                                                                 //获得size
            // TODO Auto-generated method stub
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View view, int position, Object object) { //销毁Item
            ImageView x = (ImageView) viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ((ViewPager) view).removeView(x);
        }

        @Override
        public Object instantiateItem(View view, final int position) { //实例化Item
            ImageView x = (ImageView) viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //点击放大图片
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PicViewerActivity.class);
                    intent.putExtra("urls_case", urls_teshujiancha);
                    intent.putExtra("current_index", position);
                    intent.putExtra("sign_num", sign_num);
                    intent.putExtra("be_sign_number", be_sign_number);
                    intent.putExtra("tid", tid);
                    intent.putExtra("is_miss",is_miss);
                    intent.putExtra("type", "ppt");
                    /*urls_teshujiancha.add(viewLists.get(position).get("url").toString());
                    intent.putExtra("urls_case", urls_teshujiancha);
                    intent.putExtra("current_index", 0);*/
                    startActivityForResult(intent, 0);
                }
            });
            imageLoader.displayImage(viewLists.get(position).get("url").toString(), x, options);
            ((ViewPager) view).addView(x, 0);

            return viewLists.get(position).get("view");
        }

    }

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 0) {
                int position = data.getIntExtra("position", 0);
                //设置结果显示框的显示数值
                //pptViewPager.setCurrentItem(position);
//                Log.e("签到", "1");
            }
//            Log.e("签到", "2");
        } else if (resultCode == 2) {
            try {
                String jsonString = SharedPreferencesTools.getSign_Time_Json(context);
                if (jsonString != null && !"".equals(jsonString)) {
                    Json = new JSONObject(jsonString);
                }else {
                    Json = new JSONObject();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (requestCode == 0) {

                //pptViewPager.setCurrentItem(data.getIntExtra("position", 0));
                //先判断倒计时时候还在继续
                if (mTextView.isRun()) {
                    //判断查看大图页面是否有点击签到成功
                    if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) < 1) {
                        ll_s1.setClickable(false);
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                    } else {
                        ll_s1.setClickable(true);
                        ll_s1.setBackgroundResource(R.mipmap.learning_task04);
                    }
                }
//                Log.e("签到时间", SharedPreferencesTools.getSign(context) + " - " + video_s3.getText().toString());
                video_s3.setText(SharedPreferencesTools.getSign(context));
                if (data.getStringExtra("be_sign_number").equals(video_s1.getText().toString())) {
                    ll_s1.setClickable(false);
                    ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                    video_sign_i1.setClickable(true);
                    video_sign_i1.setBackgroundResource(R.mipmap.learning_task04);
                    video_sign_i1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, Examination_instructions2.class);
                            intent.putExtra("aid", pptid);
                            intent.putExtra("tid", tid);
                            intent.putExtra("type", "ppt");
                            intent.putExtra("pptid", pptid);
                            startActivity(intent);
                        }
                    });
//                    Log.e("签到", "4");
                }
//                Log.e("签到", "3");
            }
//            Log.e("签到", "5");
        }
//        Log.e("签到", "6");
    }


    private void getTaskPptInfo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    signCount= Integer.parseInt(SharedPreferencesTools.getSign(PPTSignActivity.this))+1;
                    Json.put(signCount + "", System.currentTimeMillis());//JSONObject对象中添加键值对
                    SharedPreferencesTools.saveSign_Time_Json(context,Json.toString());
//                    Log.e("PPTSignActivity", "getTaskPptInfo: signCount"+signCount+"    is_miss"+is_miss);
                    if (is_miss.equals("0")) {
                        //String result = SendSignInfo.sendSign(PPTSignActivity.this, tid, pptid, signnum);
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", pptid);
                        obj.put("signnum", signnum);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);

                        Message message = new Message();
                        message.what = 3;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else if (signCount >= Integer.parseInt(sign_num)) {
                        JSONArray jsonArraySignTime = new JSONArray();
                        jsonArraySignTime.put(Json);//将JSONObject对象添加到Json数组中
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.taskVideoSign);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("tid", tid);
                        obj.put("aid", pptid);
                        obj.put("signnum", jsonArraySignTime);
//                        Log.e("看看sign数据", obj.toString());
                        String result = HttpClientUtils.sendPost(context, URLConfig.Learning_task, obj.toString());
//                        Log.e("看看sign数据", result);

                        Message message = new Message();
                        message.what = 3;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 4;
                        message.obj = 0;
                        handler.sendMessage(message);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void startTimer() {
        try {
            String str1 = ppt_time.getText().toString();
            long time = Long.parseLong(str1);

            long diff = 60 * 1000 * time;
//            Log.e("diff:", diff + "");
            mTextView = (TimerTextView) findViewById(R.id.timer);
            //设置时间
            mTextView.setTimes(diff);

            /**
             * 开始倒计时
             */
            if (!mTextView.isRun()) {
                mTextView.start();
                long tf = time * 1000 * 60;
//                Log.e("时间11", tf + "");
                CountDownTimer t = new CountDownTimer(tf, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                        setColorGray();
                        ll_s1.setClickable(false);
                        NotSignShowDialog();
                    }
                }.start();
                timerList.add(t);
                //签到点倒计时
//                Log.e("slist:", slist.toString());
                for (Map<String, Object> map : slist) {
                    for (String str : map.keySet()) {
                        if (map.containsKey(str)) {
                            double d = Double.parseDouble(map.get(str).toString());
                            long l = (long) (1000 * d);
                            final String signnum_text = str;
                            CountDownTimer timer = new CountDownTimer(l, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    Intent intent = new Intent();
                                    intent.setAction("sign");
                                    intent.putExtra("mes", "1");
                                    intent.putExtra("tid", tid);
                                    intent.putExtra("pptid", pptid);
                                    intent.putExtra("signnum", signnum_text);
                                    intent.putExtra("sign_num", sign_num);
                                    sendBroadcast(intent);

                                    //到签到点，判断是否点击签到
                                    ll_s1.setClickable(true);
                                    setColorWhite();
                                    ll_s1.setBackgroundResource(R.mipmap.learning_task04);

                                    ll_s1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isClickSign = true;
                                            ll_s1.setClickable(false);
                                            getTaskPptInfo();
                                            signnum = signnum_text;
                                        }
                                    });

                                    SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) + 1) + "");
                                    /*if (timer1!=null){
                                        timer1.cancel();
                                        NotSignShowDialog();
                                    }*/
                                    timer1 = new CountDownTimer(60 * 1000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
//                                            Log.e("签到时间倒计时", millisUntilFinished + "");
                                        }

                                        @Override
                                        public void onFinish() {
                                            NotSignShowDialog();
//                                            Log.e("PPTSignActivity", "onFinish--修改前: Sign_in_num"+SharedPreferencesTools.getSign_in_num(context));
                                            if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) > 0) {
                                                SharedPreferencesTools.saveSign_in_num(context, (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) - 1) + "");
                                            } else {
                                                SharedPreferencesTools.saveSign_in_num(context, "0");
                                            }
//                                            Log.e("PPTSignActivity", "onFinish--修改后: Sign_in_num"+SharedPreferencesTools.getSign_in_num(context));
                                            if (Integer.parseInt(SharedPreferencesTools.getSign_in_num(context)) < 1) {
                                                ll_s1.setBackgroundResource(R.mipmap.video_sign2);
                                                setColorGray();
                                                ll_s1.setClickable(false);
                                            }
                                        }
                                    }.start();
                                    timerList.add(timer1);
                                }
                            }.start();
                            timerList.add(timer);
                    }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NotSignDialog notSignDialog;

    private void NotSignShowDialog() {
        if (!isClickSign && is_miss.equals("1")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (notSignDialog == null) {
                            notSignDialog = new NotSignDialog(PPTSignActivity.this);
                        }
                        notSignDialog.show();
                        notSignDialog.setCancelButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notSignDialog.dismiss();
                            }
                        });
                        notSignDialog.setSureButtonOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    notSignDialog.dismiss();
                                    Intent intent = new Intent(PPTSignActivity.this, PPTSignActivity.class);
                                    intent.putExtra("tid", getIntent().getStringExtra("tid"));
                                    intent.putExtra("pptid", getIntent().getStringExtra("pptid"));
                                    intent.putExtra("expired", getIntent().getStringExtra("expired"));
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            isClickSign = false;
//            Log.e("PPTSignActivity", "onFinish: 未签到");
        }else {
            isClickSign = false;
        }

    }

    public void setColorWhite() {
        sign_t1.setTextColor(Color.WHITE);
        sign_t2.setTextColor(Color.WHITE);
        sign_t3.setTextColor(Color.WHITE);
        sign_t4.setTextColor(Color.WHITE);
        video_s1.setTextColor(Color.WHITE);
        video_s3.setTextColor(Color.WHITE);
    }

    public void setColorGray() {
        sign_t1.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t2.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t3.setTextColor(Color.parseColor("#6c6c6c"));
        sign_t4.setTextColor(Color.parseColor("#6c6c6c"));
        video_s1.setTextColor(Color.parseColor("#6c6c6c"));
        video_s3.setTextColor(Color.parseColor("#6c6c6c"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notSignDialog != null) {
            notSignDialog.dismiss();
        }
        for (int i=0;i<timerList.size();i++){
            if (timerList.get(i)!=null){
                timerList.get(i).cancel();
            }
        }
    }
}
