package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.message.New_message;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.fragment.MyFocusDataFragment;
import com.linlic.ccmtv.yx.fragment.MyFocusVideoFragment;
import com.linlic.ccmtv.yx.kzbf.utils.SkyVisitUtils;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ViewPagerForScrollView;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的关注详情
 *
 * @author yu
 */
public class MyFollowDetails extends FragmentActivity{
    @Bind(R.id.layout_loading)
    LinearLayout layoutLoading;
    @Bind(R.id.img_back)
    LinearLayout imgBack;
    @Bind(R.id.my_hishead)
    CircleImageView myHishead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_zhiwu)
    TextView tvZhiwu;
    @Bind(R.id.tv_yiyuan_name)
    TextView tvYiyuanName;
    @Bind(R.id.tv_ke_shi)
    TextView tvKeShi;
    @Bind(R.id.tv_care)
    TextView tvCare;
    @Bind(R.id.tv_send_msg)
    TextView tvSendMsg;
    @Bind(R.id.tv_fang)
    TextView tvFang;
    @Bind(R.id.tv_integral)
    TextView tvIntegral;
    @Bind(R.id.tv_fen)
    TextView tvFen;
    @Bind(R.id.ll_fen)
    LinearLayout llFen;
    @Bind(R.id.tv_guan)
    TextView tvGuan;
    @Bind(R.id.ll_guan)
    LinearLayout llGuan;
    @Bind(R.id.v_line1)
    View vLine1;
    @Bind(R.id.rl_data)
    RelativeLayout rlData;
    @Bind(R.id.v_line2)
    View vLine2;
    @Bind(R.id.rl_video)
    RelativeLayout rlVideo;
    @Bind(R.id.mainViewPager)
    ViewPagerForScrollView mainViewPager;
    @Bind(R.id.item_ongoing)
    TextView itemOngoing;
    @Bind(R.id.item_completed)
    TextView itemCompleted;
    @Bind(R.id.myscroll)
    ScrollView myscroll;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    private MyFocusDataFragment dataFragment;
    private MyFocusVideoFragment videoFragment;
    private List<Fragment> mFragmentList;
    private FragmentAdapter mFragmentAdapter;
    ImageView img_back;
    Button btn_his_jifen, but_follow, but_sendmes;
    private TextView tv_moreratevoides, tv_his_username, tv_his_bigkeshi, tv_his_smallkeshi, tv_his_hosp, tv_keshi, tv_regist_date, tv_his_truename, tv_hosname, tv_hissex;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> data_hisfollow = new ArrayList<Map<String, Object>>();
    LinearLayout layout_follow, layout_workmate, layout_hisfollow;
    LinearLayout relative_buttom;
    Context context;
    //用户统计
    private String entertime, leavetime;
    public static String enterUrl = "http://www.ccmtv.cn/Member/Index.html";
    String hisuid, Str_his_hyleibie, Str_his_bigkeshi, Str_his_smallkeshi, Str_his_zhicheng, Str_his_truename, Str_his_sexName, Str_his_IsYZ, Str_his_icon, Str_his_address, Str_his_idcard, Str_his_sex, Str_his_phonenum, Str_his_username;
    String Str_username, Str_isfollow;
    CircleImageView iv_onehead, iv_twohead, iv_threehead, iv_fourhead;
    LinearLayout layout_hisvideo, layout_showhisvideo, linearLayout_hisfollow, layout_followandwork;
    TextView tv_shownovideo, tv_shownofollow, tv_morefollow;
    String uid, isMyColleague;
    int Str_vipflg;
    ImageView iv_showVip;
    Boolean ismywork;
    View view_id;
    String myuid, username,guanzhu_count;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            JSONObject object = result.getJSONObject("data");
                            loadImg(myHishead, object.getString("icon"));//显示图片
                            String visit_count = object.getString("visit_count");//访问量
                            String fensi_count = object.getString("fensi_count");//粉丝数
                            guanzhu_count = object.getString("guanzhu_count");//关注数
                            String money = object.getString("money");
                            tvFang.setText(visit_count);
                            tvFen.setText(fensi_count);
                            tvGuan.setText(guanzhu_count);
                            tvIntegral.setText(money);
                            tvName.setText(object.getString("username"));
                            username = object.getString("username");
                            JSONObject object1 = object.getJSONObject("hosName");
                            tvYiyuanName.setText(object1.getString("name"));
                            tvKeShi.setText(object.getString("keshilb") + " " + object.getString("keshi"));
                            String attentionflg = object.getString("attentionflg");
                            if (attentionflg.equals("1")) {
                                tvCare.setBackground(getResources().getDrawable(R.drawable.send_msg));
                                tvCare.setText("已关注");
                                tvCare.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                tvCare.setBackground(getResources().getDrawable(R.drawable.focus));
                                tvCare.setText("关注");
                                tvCare.setTextColor(getResources().getColor(R.color.black));
                            }
                            myuid = object.getString("uid");
                            initview(object);
                        } else {//失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    data.clear();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            int x = dataArray.length();
                            int i;
                            if (x == 0) {
                                layout_showhisvideo.setVisibility(View.GONE);
                                tv_shownovideo.setVisibility(View.VISIBLE);
                            } else {
                                layout_showhisvideo.setVisibility(View.VISIBLE);
                                tv_shownovideo.setVisibility(View.GONE);

                                if (x <= 4 && x >= 1) {
                                    for (i = 0; i < x; i++) {
                                        View view = LayoutInflater.from(context).inflate(R.layout.item_hisvideo,
                                                layout_hisvideo, false);
                                        final ImageView img = (ImageView) view.findViewById(R.id.img_item_hisvideo);
                                        TextView tv = (TextView) view.findViewById(R.id.tv_item_hisvideo);
                                        JSONObject object = dataArray.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("aid", object.getString("aid"));
                                        map.put("title", object.getString("title"));
                                        map.put("picurl", object.getString("picurl"));
                                        map.put("list", object.getString("list"));

                                        data.add(map);
                                        img.setTag(R.id.tag_one, data.get(i).get("aid").toString());
                                        img.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub
                                                String aid = img.getTag(R.id.tag_one).toString();
                                                getVideoRulest(aid);
                                            }
                                        });
                                        //显示头像
                                        //   Picasso.with(context).load(data.get(i).get("picurl").toString()).into();
                                        loadImg(img, data.get(i).get("picurl").toString());
                                        tv.setText(data.get(i).get("title").toString());
                                        layout_hisvideo.addView(view);
                                    }
                                } else {
                                    //超过4条  只取前4条
                                    for (i = 0; i < 4; i++) {
                                        View view = LayoutInflater.from(context).inflate(R.layout.item_hisvideo,
                                                layout_hisvideo, false);
                                        final ImageView img = (ImageView) view
                                                .findViewById(R.id.img_item_hisvideo);
                                        TextView tv = (TextView) view.findViewById(R.id.tv_item_hisvideo);
                                        JSONObject object = dataArray.getJSONObject(i);
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("aid", object.getString("aid"));
                                        map.put("title", object.getString("title"));
                                        map.put("picurl", object.getString("picurl"));
                                        map.put("list", object.getString("list"));
                                        data.add(map);
                                        img.setTag(R.id.tag_one, data.get(i).get("aid").toString());
                                        img.setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method stub
                                                String aid = img.getTag(R.id.tag_one).toString();
                                                getVideoRulest(aid);
                                            }
                                        });
                                        //显示头像
                                        loadImg(img, data.get(i).get("picurl").toString());
                                        tv.setText(data.get(i).get("title").toString());
                                        layout_hisvideo.addView(view);
                                    }

                                }

                            }
                        } else {//失败
                            layout_showhisvideo.setVisibility(View.GONE);
                            tv_shownovideo.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功

                            JSONArray dataArray = result
                                    .getJSONArray("data");
                            if (dataArray.length() == 0) {
                                tv_shownofollow.setVisibility(View.VISIBLE);
                                linearLayout_hisfollow.setVisibility(View.GONE);
                            } else {
                                tv_shownofollow.setVisibility(View.GONE);
                                linearLayout_hisfollow.setVisibility(View.GONE);
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject object = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("uid", object.getString("uid"));
                                    map.put("username", object.getString("username"));
                                    map.put("icon", object.getString("icon"));
                                    map.put("name", object.getString("name"));
                                    map.put("attflg", object.getString("attflg"));
                                    data_hisfollow.add(map);
                                }
                                if (data_hisfollow.size() == 1) {
                                    loadImg(iv_onehead, data_hisfollow.get(0).get("icon").toString());
                                    iv_twohead.setVisibility(View.GONE);
                                    iv_threehead.setVisibility(View.GONE);
                                    iv_fourhead.setVisibility(View.GONE);
                                    iv_onehead.setTag(data_hisfollow.get(0).get("uid"));
                                } else if (data_hisfollow.size() == 2) {
                                    loadImg(iv_onehead, data_hisfollow.get(0).get("icon").toString());
                                    loadImg(iv_twohead, data_hisfollow.get(1).get("icon").toString());
                                    iv_threehead.setVisibility(View.GONE);
                                    iv_fourhead.setVisibility(View.GONE);
                                    iv_onehead.setTag(data_hisfollow.get(0).get("uid"));
                                    iv_twohead.setTag(data_hisfollow.get(1).get("uid"));
                                } else if (data_hisfollow.size() == 3) {
                                    loadImg(iv_onehead, data_hisfollow.get(0).get("icon").toString());
                                    loadImg(iv_twohead, data_hisfollow.get(1).get("icon").toString());
                                    loadImg(iv_threehead, data_hisfollow.get(2).get("icon").toString());
                                    iv_fourhead.setVisibility(View.GONE);
                                    iv_onehead.setTag(data_hisfollow.get(0).get("uid"));
                                    iv_twohead.setTag(data_hisfollow.get(1).get("uid"));
                                    iv_threehead.setTag(data_hisfollow.get(2).get("uid"));
                                } else if (data_hisfollow.size() >= 4) {
                                    loadImg(iv_onehead, data_hisfollow.get(0).get("icon").toString());
                                    loadImg(iv_twohead, data_hisfollow.get(1).get("icon").toString());
                                    loadImg(iv_threehead, data_hisfollow.get(2).get("icon").toString());
                                    loadImg(iv_fourhead, data_hisfollow.get(3).get("icon").toString());
                                    iv_onehead.setTag(data_hisfollow.get(0).get("uid"));
                                    iv_twohead.setTag(data_hisfollow.get(1).get("uid"));
                                    iv_threehead.setTag(data_hisfollow.get(2).get("uid"));
                                    iv_fourhead.setTag(data_hisfollow.get(3).get("uid"));
                                }


                            }
                        } else {//失败
                            tv_shownofollow.setVisibility(View.VISIBLE);
                            linearLayout_hisfollow.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            if ("1".equals(result.getString("data"))) {
                                tvCare.setBackground(getResources().getDrawable(R.drawable.send_msg));
                                tvCare.setText("已关注");
                                tvCare.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                tvCare.setBackground(getResources().getDrawable(R.drawable.focus));
                                tvCare.setText("关注");
                                tvCare.setTextColor(getResources().getColor(R.color.black));
                            }
                        } else {// 失败
                            Toast.makeText(context, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_followdetails);
        ButterKnife.bind(this);
        context = this;
        hisuid = getIntent().getStringExtra("Hisuid");
        isMyColleague = getIntent().getStringExtra("isMyColleague");
        Str_username = getIntent().getStringExtra("Str_username");
        ismywork = getIntent().getBooleanExtra("ismywork", false);
        uid = SharedPreferencesTools.getUid(context);
        //findId();
        initData();
//        if (ismywork) {
//            layout_hisfollow.setVisibility(View.GONE);
//            layout_followandwork.setVisibility(View.GONE);
//            view_id.setVisibility(View.GONE);
//        }
//        if ("1".equals(isMyColleague)) {
//            layout_hisfollow.setVisibility(View.GONE);
//            layout_followandwork.setVisibility(View.GONE);
//            view_id.setVisibility(View.GONE);
//        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        hisuid = intent.getStringExtra("Hisuid");
        uid = SharedPreferencesTools.getUid(context);
        initData();
    }

    private void initview(JSONObject object) throws JSONException {

        mFragmentList = new ArrayList<Fragment>();
        FragmentManager my_manager = getSupportFragmentManager();

        Bundle bundle = new Bundle();//向fragment传递数据
        bundle.putString("nickname", object.getString("username"));
        bundle.putString("sexName", object.getString("sexName"));
        bundle.putString("hyleibie", object.getString("hyleibie"));
        bundle.putString("my_694", object.getString("my_694"));
        bundle.putString("name", object.getJSONObject("hosName").getString("name"));
        bundle.putString("keshilb", object.getString("keshilb") + "-" + object.getString("keshi"));

        dataFragment = new MyFocusDataFragment();
        dataFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("Hisuid", hisuid);
        videoFragment = new MyFocusVideoFragment();
        videoFragment.setArguments(bundle1);

        mFragmentList.add(dataFragment);
        mFragmentList.add(videoFragment);
        mFragmentAdapter = new FragmentAdapter(my_manager, mFragmentList);
        mainViewPager.setAdapter(mFragmentAdapter);
        mainViewPager.setCurrentItem(0);//初始设置ViewPager选中第一帧
        mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*此方法在页面被选中时调用*/
//                mPosition = position;
                changeTextColor(position);
                mFragmentList.get(mainViewPager.getCurrentItem()).onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        });
    }

    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */
    private void changeTextColor(int position) {
        if (position == 0) {
            //字体颜色
            itemOngoing.setTextColor(Color.parseColor("#3797FB"));
            itemCompleted.setTextColor(Color.parseColor("#000000"));

            //滑块
            vLine1.setVisibility(View.VISIBLE);
            vLine2.setVisibility(View.INVISIBLE);

        } else if (position == 1) {
            itemCompleted.setTextColor(Color.parseColor("#3797FB"));
            itemOngoing.setTextColor(Color.parseColor("#000000"));

            vLine1.setVisibility(View.INVISIBLE);
            vLine2.setVisibility(View.VISIBLE);

        }
    }

    @OnClick({R.id.img_back, R.id.tv_care, R.id.tv_send_msg, R.id.ll_fen, R.id.ll_guan, R.id.rl_data, R.id.rl_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_care:
                queryIsFollow();
                break;
            case R.id.tv_send_msg:
                Intent intent1 = new Intent(this, New_message.class);
                intent1.putExtra("username", username);
                intent1.putExtra("uid", myuid);
                startActivity(intent1);
                break;
            case R.id.ll_fen:
                break;
            case R.id.ll_guan:
                Intent intent = new Intent(this, FocusListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("myuid", myuid);
                bundle.putString("guanzhu_count",guanzhu_count);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_data:
                mainViewPager.setCurrentItem(0, true);
                break;
            case R.id.rl_video:
                mainViewPager.setCurrentItem(1, true);
                break;
        }
    }



    public class FragmentAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    public void initData() {
        //判断是否是本人
//        if (hisuid.equals(uid)) {
//            relative_buttom.setVisibility(View.GONE);
//        } else {
//            relative_buttom.setVisibility(View.VISIBLE);
//        }
        if (uid == null || ("").equals(uid)) {
            startActivity(new Intent(context, LoginActivity.class));
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", hisuid);
                        object.put("auid", uid);
                        object.put("act", URLConfig.getUserInfo);
                        String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();

        }
    }


    /**
     * name：点击查看某个视频的详细
     * author：Larry
     * data：2016/4/5 16:41
     */
    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);

    }

    /**
     * 关注、、、取消关注
     */
    public void queryIsFollow() {
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("uid", uid);
                    object.put("auid", hisuid);
                    object.put("act", URLConfig.attention);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, object.toString());
                    Message message = new Message();
                    message.what = 5;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getApplicationContext());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    public void onResume() {
        //保存进入的日期
        entertime = SkyVisitUtils.getCurrentTime();
        super.onResume();
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //保存推出的日期
        leavetime = SkyVisitUtils.getCurrentTime();
        //保存日期到服务器
        SkyVisitUtils.OnlineStatistical(context, enterUrl, entertime, leavetime);
        super.onPause();
    }

}
