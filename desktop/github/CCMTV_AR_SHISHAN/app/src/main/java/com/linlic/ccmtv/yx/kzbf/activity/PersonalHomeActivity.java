package com.linlic.ccmtv.yx.kzbf.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.message.New_message;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.adapter.PersonalHomeAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbPersonal;
import com.linlic.ccmtv.yx.kzbf.widget.RecyclerViewNoBugLinearLayoutManager;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//个人主页
//item_medicine_personal
public class PersonalHomeActivity extends BaseActivity implements View.OnClickListener {
    private TextView personal_home_title;
    Context context;
    private int page = 1;
    private int count;
    private int itemCount;
    private String id;//文章id
    private String is_laud;//是否点赞
    private int zan_num;//点赞数
    private int mPosition;
    private String fuid;
    private DbPersonal dbpl;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private PersonalHomeAdapter phAdapter;
    private PersonalHomeAdapter yesAdapter;
    private List<DbPersonal> list;
    private List<DbPersonal> listMore = new ArrayList<>();//未读列表
    private List<DbPersonal> listYes;
    private List<DbPersonal> listYesMore = new ArrayList<>();//已读列表
    private String no_num, uid, is_focus;
    private ImageView user_img;
    private TextView helper, company, drug;
    private LinearLayout ll_send_message, ll_fouse;
    private NodataEmptyLayout rl_personal_nodata;
    private List<String> messageTypes = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            messageTypes.clear();
                            String is_news_red = jsonObject.getString("is_news_red");
                            ;  //是否显示未读消息(区别于动态)的小红点
                            if ("1".equals(is_news_red)) {
                                message_red_dot.setVisibility(View.VISIBLE);
                            } else {
                                message_red_dot.setVisibility(View.GONE);
                            }

                            no_num = jsonObject.getString("no_num");
                            is_focus = jsonObject.getString("is_focus");

                            JSONObject object = jsonObject.getJSONObject("userinfo");
                            uid = object.getString("uid");
                            RequestOptions options = new RequestOptions().error(R.mipmap.img_default).placeholder(R.mipmap.img_default);
                            Glide.with(context).asBitmap().load(object.getString("user_img")).apply(options).into(user_img);
                            personal_home_title.setText(object.getString("helper") + "个人主页");
                            setTextView(object, "helper", helper);
                            setTextView(object, "company", company);
                            setTextView(object, "drug", drug);


                            if (is_focus.equals("0")) {//未关注
                                ll_fouse.setBackgroundResource(R.mipmap.kzbf_personal_unfoucs);
                            } else {//已关注
                                ll_fouse.setBackgroundResource(R.mipmap.kzbf_personal_foucs);
                            }

                            if (no_num.equals("0")) {//无未读消息
                                recyclerView.setVisibility(View.GONE);
                            } else {//有未读消息
                                String text = "未读动态(" + no_num + "条)";
                                recyclerView.setVisibility(View.VISIBLE);
                                messageTypes.add(text);
                            }

                            list = new ArrayList<>();
                            JSONArray dataArray = jsonObject.getJSONArray("no_list");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                dbpl = new DbPersonal(1);
                                setBean(customJson);
                                list.add(dbpl);
                            }
                            listMore.addAll(list);
                            phAdapter.notifyDataSetChanged();

                            listYes = new ArrayList<>();
                            JSONArray array = jsonObject.getJSONArray("yes_list");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject customJson = array.getJSONObject(i);
                                dbpl = new DbPersonal(1);
                                setBean(customJson);
                                listYes.add(dbpl);
                            }
                            listYesMore.addAll(listYes);
                            yesAdapter.notifyDataSetChanged();
                            int yes_num = jsonObject.getInt("yes_num");
                            if (array.length() > 0) {//无已读消息
                                String text = "已读动态(" + yes_num + "条)";
                                messageTypes.add(text);
                                if ("0".equals(no_num))
                                    recyclerView2.setVisibility(View.VISIBLE);
                            } else {
                                recyclerView2.setVisibility(View.GONE);
                            }
                            //将recyclerview归置为未读状态
                            indicator_message_isread.onPageSelected(0);
                            indicator_message_isread.onPageScrolled(0, 0.0F, 0);
//                            if (array.length() == 0 && no_num.equals("0")) {
//                                tv_has_read.setVisibility(View.GONE);
//                                recyclerView2.setVisibility(View.GONE);
//                                has_read_view.setVisibility(View.GONE);
//                                ll_top_head.setVisibility(View.GONE);
//                                personal_red_dot.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.GONE);
//                                ll_top_view.setVisibility(View.GONE);
//                                rl_personal_nodata.setVisibility(View.VISIBLE);
//                            }
                            commonNavigatorAdapter.notifyDataSetChanged();
                            String code = jsonObject.getString("code");
                            if ("code".equals(code))
                                setResultStatus(array.length() > 0 || !no_num.equals("0"), HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                            else
                                setResultStatus(array.length() > 0 || !no_num.equals("0"), Integer.valueOf(code));
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            mPosition = msg.arg1;
                            if (is_laud.equals("1")) {//变成已点赞
                                zan_num++;
                                listMore.get(mPosition).setIs_laud("1");
                            } else {//变成未点赞
                                if (zan_num > 0) {
                                    zan_num--;
                                }
                                listMore.get(mPosition).setIs_laud("0");
                            }
                            listMore.get(mPosition).setLaud_num(zan_num + "");
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        phAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) {
                            mPosition = msg.arg1;
                            if (is_laud.equals("1")) {//变成已点赞
                                zan_num++;
                                listYesMore.get(mPosition).setIs_laud("1");
                            } else {//变成未点赞
                                if (zan_num > 0) {
                                    zan_num--;
                                }
                                listYesMore.get(mPosition).setIs_laud("0");
                            }
                            listYesMore.get(mPosition).setLaud_num(zan_num + "");
                            yesAdapter.notifyDataSetChanged();
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            ll_fouse.setBackgroundResource(R.mipmap.kzbf_personal_foucs);
                            is_focus = "1";
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 5:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            ll_fouse.setBackgroundResource(R.mipmap.kzbf_personal_unfoucs);
                            is_focus = "0";
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(false, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView message_red_dot;
    private RelativeLayout rl_kzbf_personal_message;

    private void setResultStatus(boolean status, int code) {
        if (status) {
//            tv_has_read.setVisibility(View.VISIBLE);
//            recyclerView2.setVisibility(View.VISIBLE);
//            has_read_view.setVisibility(View.VISIBLE);
//            ll_top_head.setVisibility(View.VISIBLE);
//            personal_red_dot.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.VISIBLE);
//            ll_top_view.setVisibility(View.VISIBLE);
            rl_personal_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_personal_nodata.setNetErrorIcon();
            } else {
                rl_personal_nodata.setLastEmptyIcon();
            }
            recyclerView2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            rl_personal_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_home);
        context = this;
        //沉浸式状态栏
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentView = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            contentView.getChildAt(0).setFitsSystemWindows(false);
        }

        initView();
        initData();
    }

    private LinearLayout ll_personal_home_titlebar;
    private MagicIndicator indicator_message_isread;

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.personal_recyclerView);
        recyclerView2 = (RecyclerView) findViewById(R.id.personal_recyclerView2);
        personal_home_title = (TextView) findViewById(R.id.personal_home_title);
        user_img = (ImageView) findViewById(R.id.personal_home_icon);
        helper = (TextView) findViewById(R.id.personal_helper);
        company = (TextView) findViewById(R.id.personal_company);
        drug = (TextView) findViewById(R.id.personal_drug);
        ll_send_message = (LinearLayout) findViewById(R.id.ll_send_message);
        ll_fouse = (LinearLayout) findViewById(R.id.ll_fouse);
        rl_personal_nodata = (NodataEmptyLayout) findViewById(R.id.rl_personal_nodata);
        ll_personal_home_titlebar = findViewById(R.id.ll_personal_home_titlebar);
        rl_kzbf_personal_message = findViewById(R.id.rl_kzbf_personal_message);

        indicator_message_isread = findViewById(R.id.indicator_personal_message_isread);
        message_red_dot = findViewById(R.id.iv_kzbf_personal_message_red_dot);


        rl_kzbf_personal_message.setOnClickListener(this);
        setTitlebarPadding();
        initMagicIndicator();
    }

    /**
     * 为titlebar设置一个statusbar高度的padding
     */
    private void setTitlebarPadding() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        ll_personal_home_titlebar.setPadding(0, height, 0, 0);
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = indicator_message_isread;
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
    }

    private CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        @Override
        public int getCount() {
            return messageTypes == null ? 0 : messageTypes.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int index) {
            final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
            SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
            simplePagerTitleView.setText(messageTypes.get(index));
            simplePagerTitleView.setNormalColor(Color.parseColor("#000000"));
            simplePagerTitleView.setSelectedColor(Color.parseColor("#3997F9"));
            simplePagerTitleView.setTextSize(12);
            simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index == 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView2.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        recyclerView2.setVisibility(View.VISIBLE);
                    }
                    indicator_message_isread.onPageSelected(index);
                    indicator_message_isread.onPageScrolled(index, 0.0F, 0);
                }
            });

            badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
            if (messageTypes.get(index).startsWith("已读")) return badgePagerTitleView;//已读状态下  不添加小红点

            ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.simple_red_dot_badge_layout, null);
            badgePagerTitleView.setBadgeView(badgeImageView);
                    /*ViewGroup.LayoutParams params = badgeImageView.getLayoutParams();
                    params.height = 20;
                    params.width = 20;
                    badgeImageView.setLayoutParams(params);*/

//                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
            badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, 0));
            badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, 0));
            badgePagerTitleView.setAutoCancelBadge(false);

            return badgePagerTitleView;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            LinePagerIndicator indicator = new LinePagerIndicator(context);
            indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
//                indicator.setYOffset(UIUtil.dip2px(context, 3));
            indicator.setLineWidth(UIUtil.dip2px(context, 50));//滑块宽度
            indicator.setColors(Color.parseColor("#3997F9"));
            return indicator;
        }
    };

    private void setValue() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.memberIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("page", page);
                    obj.put("fuid", fuid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看个人中心数据", result);
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

    private void initData() {
        fuid = getIntent().getStringExtra("fuid");

        ll_send_message.setOnClickListener(this);
        ll_fouse.setOnClickListener(this);

        //未读动态
        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(context));
        phAdapter = new PersonalHomeAdapter(context, listMore);
        recyclerView.setAdapter(phAdapter);

        phAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                id = listMore.get(position).getId();
                is_laud = listMore.get(position).getIs_laud();
                zan_num = Integer.parseInt(listMore.get(position).getLaud_num());
//                switch (view.getId()) {
//                    case R.id.personal_zan_num://item点赞
//                        if (is_laud.equals("0")) {
//                            is_laud = "1";
//                        } else {
//                            is_laud = "0";
//                        }
//                        setPraise(position);
//                        break;
//                }
            }
        });

        phAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                id = listMore.get(position).getId();
                Intent intent = new Intent(context, MedicineDetialActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        //已读动态
        recyclerView2.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(context));
        yesAdapter = new PersonalHomeAdapter(context, listYesMore);
        recyclerView2.setAdapter(yesAdapter);

        yesAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                id = listYesMore.get(position).getId();
                is_laud = listYesMore.get(position).getIs_laud();
                zan_num = Integer.parseInt(listYesMore.get(position).getLaud_num());
//                switch (view.getId()) {
//                    case R.id.personal_zan_img://item点赞
//                        if (is_laud.equals("0")) {
//                            is_laud = "1";
//                        } else {
//                            is_laud = "0";
//                        }
//                        setPraise2(position);
//                        break;
//                }
            }
        });

        yesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                id = listYesMore.get(position).getId();
                Intent intent = new Intent(context, MedicineDetialActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        listMore.clear();
        listYesMore.clear();
        setValue();
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

    //给TextView设置数据
    private void setTextView(JSONObject jsonObject, String s, TextView textView) throws JSONException {
        if (!jsonObject.getString(s).equals("")) {
            textView.setText(jsonObject.getString(s));
        }
    }

    private void setBean(JSONObject jsonObject) throws JSONException {
        dbpl.setId(jsonObject.getString("id"));
        dbpl.setUid(jsonObject.getString("uid"));
        dbpl.setTitle(jsonObject.getString("title"));
        dbpl.setPosttime(jsonObject.getString("posttime"));
        dbpl.setImg_url(jsonObject.getString("img_url"));
        dbpl.setLaud_num(jsonObject.getString("laud_num"));
        dbpl.setIs_laud(jsonObject.getString("is_laud"));
    }

    //赞/取消赞
    private void setPraise(final int position) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleLaud);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看点赞数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 2;
                    message.arg1 = position;
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

    //赞/取消赞
    private void setPraise2(final int position) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.articleLaud);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看点赞数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 3;
                    message.arg1 = position;
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_send_message:
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("addressee_uid", uid);
                intent.putExtra("helper", helper.getText().toString());
                intent.putExtra("assistant", "0");
                startActivity(intent);
                break;

            case R.id.rl_kzbf_personal_message:
                Intent intent1 = new Intent(context, New_message.class);
                intent1.putExtra("uid", uid);
                intent1.putExtra(New_message.FROM_KZBF_KEY, "FROM_KZBF_KEY");
                intent1.putExtra("username", helper.getText().toString());
                startActivity(intent1);
                break;
            case R.id.ll_fouse:
                if (is_focus.equals("0")) {//变成已关注
                    setFouse();
                } else {//变成未关注
                    setCancelFouse();
                }
                break;
            default:
                break;
        }
    }

    private void setFouse() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_id", uid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看关注数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 4;
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

    private void setCancelFouse() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.cancelFocus);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("focus_id", uid);

                    String result = HttpClientUtils.sendPost(context, URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看取消关注数据", result);
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 5;
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
}
