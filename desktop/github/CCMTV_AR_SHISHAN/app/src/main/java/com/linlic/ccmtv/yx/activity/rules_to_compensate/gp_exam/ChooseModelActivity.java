package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.linlic.ccmtv.yx.R.id.ll_point;

public class ChooseModelActivity extends BaseActivity {

    @Bind(R.id.my_viewpage)
    ViewPager myViewpage;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.tv_confirm)
    TextView tvConfirm;
    @Bind(ll_point)
    LinearLayout llPoint;
    private BaseListAdapter listAdapter;//listview适配器
    private int mposition = 0;
    private List<ModelList> modelLists;
    private List<View> dotViewList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        JSONObject data = result.getJSONObject("data");
                        if (data.getInt("status") == 1) { //成功
                            modelLists = JsonUtils.fromJsonArray(data.getJSONArray("data").toString(), ModelList.class);
                            if (modelLists == null || modelLists.isEmpty())
                                return;
                            myViewpage.setPageMargin(30);//设置item间距
                            myViewpage.setAdapter(new Adapter(modelLists, ChooseModelActivity.this));
                            myViewpage.setCurrentItem(0);
                            initdots();
                            myViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    mposition = position;
                                    for (int i = 0; i < dotViewList.size(); i++) {
                                        if (i == position) {
                                            dotViewList.get(i).setBackgroundResource(R.drawable.dot_blue);
                                        } else {
                                            dotViewList.get(i).setBackgroundResource(R.drawable.dot_gray);
                                        }
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                            MyProgressBarDialogTools.hide();
                        } else {//失败
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void initdots(){
        dotViewList = new ArrayList<View>();
        llPoint.removeAllViews();
        View pointView;
        //循环遍历图片资源，然后保存到集合中
        for (int i = 0; i < modelLists.size(); i++){
            //加小白点，指示器（这里的小圆点定义在了drawable下的选择器中了，也可以用小图片代替）
            pointView = new View(ChooseModelActivity.this);
            pointView.setBackgroundResource(R.drawable.dot_gray); //使用选择器设置背景
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(12,12);
            if (i != 0){
                //如果不是第一个点，则设置点的左边距
                layoutParams.leftMargin = 17;
            }
            if (i==0){
                pointView.setBackgroundResource(R.drawable.dot_blue);
            }else {
                pointView.setBackgroundResource(R.drawable.dot_gray);
            }
            //pointView.setEnabled(false); //默认都是暗色的
            llPoint.addView(pointView, layoutParams);
            dotViewList.add(pointView);
        }
    }

    public class ModelList {

        public String title;
        public String pid;
        public List<ModelInfoList> item_list;

    }

    public class ModelInfoList {
        public String pid;
        private String item_id;
        private String weight;
        private String item_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_model);
        ButterKnife.bind(this);
        initdata();

        //myDots.setIndicatorViewNum(colors.length);
        //myDots.setGravity(Gravity.CENTER);
    }

    /*
     请求数据
     */
    private void initdata() {
        MyProgressBarDialogTools.show(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("fid", getIntent().getExtras().getString("fid"));
                    obj.put("uid", SharedPreferencesTools.getUid(ChooseModelActivity.this));
                    obj.put("act", "leaveKsItemsArr");
                    obj.put("standard_kid", getIntent().getExtras().getString("standard_kid"));
                    String result = HttpClientUtils.sendPost(ChooseModelActivity.this, URLConfig.getModelList, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    //handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    @OnClick({R.id.arrow_back, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                back(view);
                break;
            case R.id.tv_confirm:
                Intent intent=new Intent(this,GpExamSubjectActivity.class);
                intent.putExtra("pid",modelLists.get(mposition).pid);
                intent.putExtra("fid",getIntent().getExtras().getString("fid"));
                intent.putExtra("gp_uid",getIntent().getExtras().getString("gp_uid"));  //1 是审核通过(不可点击) 2 是从新审核（可点击） 3 是提交成绩（可点击） 4是等待审核（不可点击） 5不显示按钮
                intent.putExtra("standard_kid",getIntent().getExtras().getString("standard_kid"));
                intent.putExtra("leave_ks_id",getIntent().getExtras().getString("leave_ks_id"));
                intent.putExtra("year",getIntent().getExtras().getString("year"));
                intent.putExtra("month",getIntent().getExtras().getString("month"));
                intent.putExtra("week",getIntent().getExtras().getString("week"));
                intent.putExtra("disabled",getIntent().getExtras().getString("disabled"));
                startActivity(intent);
                finish();
                break;
        }
    }


    class Adapter extends PagerAdapter {

        private List<ModelList> mylist;
        private Context context;

        public Adapter(List<ModelList> mylist, Context context) {
            this.mylist = mylist;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mylist.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ChooseModelActivity.this).inflate(R.layout.adapter_vp_choose_model, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            MyListView my_listview = (MyListView) view.findViewById(R.id.my_listview);
            tv_title.setText(mylist.get(position).title);

            listAdapter = new BaseListAdapter(my_listview, mylist.get(position).item_list, R.layout.adaper_listview_choose_model) {

                @Override
                public void refresh(Collection datas) {
                    super.refresh(datas);
                }

                @Override
                public void convert(ListHolder helper, Object item, boolean isScrolling) {
                    super.convert(helper, item, isScrolling);
                    ModelInfoList infoList = (ModelInfoList) item;

                    helper.setText(R.id.tv_content, infoList.item_name);
                    helper.setText(R.id.tv_score, infoList.weight);

                }
            };

            my_listview.setAdapter(listAdapter);


            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
             super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

    }

}

