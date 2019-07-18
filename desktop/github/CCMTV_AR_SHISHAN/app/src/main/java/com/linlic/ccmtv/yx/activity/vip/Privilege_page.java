package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.activity.vip.adapter.VipRecyclerViewAdapter4;
import com.linlic.ccmtv.yx.adapter.home_videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**特权介绍页
 * Created by Administrator on 2018/6/4.
 */

public class Privilege_page extends BaseActivity {

    private Context context;

    @Bind(R.id.privilege_to_introduce)
    MZBannerView privilege_to_introduce;//
    @Bind(R.id.show_layout)
    LinearLayout show_layout;//
    @Bind(R.id.icon_x)
    ImageView icon_x;//
    @Bind(R.id.vip_item_horv)
    RecyclerView vip_item_horv;
    @Bind(R.id.xf_text)
    TextView xf_text;
    @Bind(R.id.content)
    TextView birthdayContent;
    private VipRecyclerViewAdapter4 adapter;
    private List<Map<String,Object>> vip_item_horv_data = new ArrayList<>();
    private List<JSONObject> data = new ArrayList<>();
    private home_videos_GridAdapter home_videos_gridAdapter;
    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            //这里的view就是我们点击的view  position就是点击的position
//            Toast.makeText(context," 点击了 "+position,Toast.LENGTH_SHORT).show();
            adapter.setSelectIndex(position);
            privilege_to_introduce.dispatchUnhandledMove(privilege_to_introduce.getChildAt(position),View.SCROLL_INDICATOR_LEFT);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataJson = jsonObject.getJSONArray("data");
                            data.clear();
                            vip_item_horv_data.clear();
                            for(int i = 0 ; i< dataJson.length();i++){
                                JSONObject daJson =   dataJson.getJSONObject(i);
                                JSONObject wordJson =   daJson.getJSONObject("word");

                                Map<String,Object> map = new HashMap<>();
                                map.put("name",daJson.getString("name"));
                                map.put("show",daJson.has("type")?daJson.getString("type"):"0");
                                vip_item_horv_data.add(map);

                                JSONObject json1 = new JSONObject();
                                json1.put("name",daJson.getString("name"));
                                json1.put("icon",daJson.getString("icon"));
                                json1.put("show",daJson.has("type")?daJson.getString("type"):"0");
                                JSONArray jsonArray = new JSONArray();
                                Iterator<String> iterator= wordJson.keys();
                                while (iterator.hasNext()){
                                    String str = iterator.next();
                                    JSONObject jsonSZ1 = new JSONObject();
                                    jsonSZ1.put("title",str);
                                    jsonSZ1.put("text",wordJson.getString(str));
                                    jsonArray.put(jsonSZ1);
                                }
                                json1.put("content",jsonArray);
                                data.add(json1);
                            }
                            initVip_item_horv();
                            initMZBannerView();
                        } else {
                            Toast.makeText(Privilege_page.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            if (jsonObject.has("type")){
                                //type专用来判断是否已经领取过了,有这个字段则表示已经领取过，没有则表示未领取
                                Toast.makeText(Privilege_page.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }else {
                                show_layout.setVisibility(View.VISIBLE);
                                birthdayContent.setText(jsonObject.getString("errorMessage"));
                                getUrlRulest();
                            }
                        }else {
                            Toast.makeText(Privilege_page.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.privilege_page);
        context = this;
        ButterKnife.bind(this);
        findId();
        if(SharedPreferencesTools.getVipFlag(context)>0){
            xf_text.setText("续费VIP");
        }else{
            xf_text.setText("开通VIP");
        }
        click();
        getUrlRulest();
    }

    public void clickView(View view){

            Intent intent = new Intent(context, MyOpenMenberActivity.class);
            startActivity(intent);

    }

    public void initVip_item_horv(){

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        vip_item_horv.setLayoutManager(linearLayoutManager);
        adapter = new VipRecyclerViewAdapter4(context, vip_item_horv_data,onRecyclerviewItemClickListener);
        adapter.setSelectIndex(0);
        vip_item_horv.setAdapter(adapter);
    }

    public void click(){
        icon_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_layout.setVisibility(View.GONE);
            }
        });
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.privilege);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("特权接口", result);

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


    private void initMZBannerView() {



        privilege_to_introduce.setIndicatorVisible(false);
        // 设置数据
        privilege_to_introduce.setPages(data, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new  BannerViewHolder();
            }
        });

        // 设置页面改变监听器
        privilege_to_introduce. addPageChangeLisnter(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.setSelectIndex(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    public   class BannerViewHolder implements MZViewHolder<JSONObject> {
        private ImageView mImageView;
        private ImageView icon;
        private LinearLayout add_layout;
        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.privilege_page_item,null);
            icon = (ImageView) view.findViewById(R.id.icon);
            mImageView = (ImageView) view.findViewById(R.id.privilege_img);
            add_layout = (LinearLayout) view.findViewById(R.id.add_layout);
            return view;
        }

        @Override
        public void onBind(Context context, int position, JSONObject  data) {
            try {

                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(data.getString("icon")), icon);
                // 数据绑定
                if(data.getString("show").equals("1")){
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(R.mipmap.privilege_page_icon4);
                    mImageView.setClickable(true);
                }else if (data.getString("show").equals("3")){
                    mImageView.setVisibility(View.VISIBLE);
                    mImageView.setImageResource(R.mipmap.privilege_page_icon4_gray);
                    mImageView.setClickable(false);
                }else{
                    mImageView.setVisibility(View.GONE);
                }
                JSONArray jsonArray = data.getJSONArray("content");
                add_layout.removeAllViews();
                for (int i  = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    TextView textView = new TextView(context);
                    textView.setText(jsonObject.getString("title"));
                    textView.setTextColor(Color.parseColor("#333333"));
                    textView.setTextSize(18);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 15, 0, 0);
                    textView.setLayoutParams(lp);
                    add_layout.addView(textView);
                    TextView textView2 = new TextView(context);
                    textView2.setText(jsonObject.getString("text"));
                    textView2.setTextColor(Color.parseColor("#666666"));
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(0, 5, 0, 0);
                    textView2.setLayoutParams(lp2);
                    add_layout.addView(textView2);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void birthday_gift_bag(View view){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.receiveGift);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    LogUtil.e("VIP生日礼包领取反馈",result);

                    Message message = new Message();
                    message.what = 2;
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/channel-122.html";
        super.onPause();
    }

}
