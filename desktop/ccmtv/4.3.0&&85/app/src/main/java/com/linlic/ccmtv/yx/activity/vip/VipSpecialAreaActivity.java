package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ArrowKeyMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VipSpecialAreaActivity extends BaseActivity {

    private TextView title_name;
    private Context context;
    private RecyclerView recyclerView;
    private Banner banner;
    private TextView tvVipAreaDepartment;
    private ImageView ivDepartmentTypeSelect;
    private GridView gvVipAreaVideo;
    private VipSpecialAreaDepartmentAdapter departmentAdapter;
    private String[] departmentStrings={"精选","其他","内科","妇科","外科","儿科"};
    private BaseListAdapter baseListAdapter;
    private List<VipSpecialAreaVideoBean> dataVideo=new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private NodataEmptyLayout specialNodata;
    private ScrollView specialData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_special_area);

        context=this;
        findId();
        initRecyclerView();
        initGridView();
        initBanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivDepartmentTypeSelect.setClickable(true);
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivDepartmentTypeSelect = (ImageView) findViewById(R.id.id_iv_vip_special_area_select_department);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview_vip_special_area_department);
        banner = (Banner) findViewById(R.id.id_vip_special_area_banner);
        tvVipAreaDepartment = (TextView) findViewById(R.id.id_tv_vip_special_area_department_title);
        gvVipAreaVideo = (GridView) findViewById(R.id.id_gv_vip_special_area_video);

        specialData = (ScrollView) findViewById(R.id.vip_special_data);
        specialNodata = (NodataEmptyLayout) findViewById(R.id.rl_vip_special_nodata);

        title_name.setText("VIP专区");
        tvVipAreaDepartment.setSelected(true);
        tvVipAreaDepartment.setMovementMethod(ArrowKeyMovementMethod.getInstance());
        tvVipAreaDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VipSpecialAreaActivity.this,VipDepartmentSelectActivity.class);
                startActivityForResult(intent,1);
                ivDepartmentTypeSelect.setClickable(false);
            }
        });
        ivDepartmentTypeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VipSpecialAreaActivity.this,VipDepartmentSelectActivity.class);
                startActivityForResult(intent,1);
                ivDepartmentTypeSelect.setClickable(false);
            }
        });
    }

    private void initRecyclerView() {
        //创建LinearLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //设置
        recyclerView.setLayoutManager(manager);
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //实例化适配器
        departmentAdapter=new VipSpecialAreaDepartmentAdapter(context,departmentStrings);
        //设置适配器
        recyclerView.setAdapter(departmentAdapter);
        departmentAdapter.setOnItemClickListener(new VipSpecialAreaDepartmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                tvVipAreaDepartment.setText(departmentStrings[position]);
            }
        });
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                /*Log.e("会议专题banner位置", position + "  =================");
                String bannerId=conferenceBannerList.get(position).getId();
                Intent intent=new Intent(ConferenceMainActivity.this,ConferenceDetailActivity.class);
                intent.putExtra("conferenceId",bannerId);
                startActivity(intent);*/

            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initGridView() {
        dataVideo.clear();
        for (int i=0; i<10; i++){
            images.add("http://img1.imgtn.bdimg.com/it/u=3014892180,449401099&fm=27&gp=0.jpg");
            VipSpecialAreaVideoBean bean=new VipSpecialAreaVideoBean();
            bean.setVideoTitle("vip专区视频"+i);
            bean.setVideoPicUrl("http://img1.imgtn.bdimg.com/it/u=3014892180,449401099&fm=27&gp=0.jpg");
            dataVideo.add(bean);
        }

        baseListAdapter = new BaseListAdapter(gvVipAreaVideo, dataVideo, R.layout.vip_special_area_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoTitle());
                helper.setImageBitmapGlide(context, R.id.id_iv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoPicUrl(), 100, 80);
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                /*if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if (((Map) item).get("flag").toString().equals("3")) {
                        //VIP
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }*/

            }
        };

        gvVipAreaVideo.setAdapter(baseListAdapter);
        gvVipAreaVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "点击Vip视频"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){
            List<String> strings=data.getStringArrayListExtra("select");
            for (int i=0;i<strings.size(); i++){
//                Log.e(getLocalClassName(), "onActivityResult: "+i+":"+strings.get(i));
            }
        }
    }

    public void back(View view) {
        finish();
    }
}
