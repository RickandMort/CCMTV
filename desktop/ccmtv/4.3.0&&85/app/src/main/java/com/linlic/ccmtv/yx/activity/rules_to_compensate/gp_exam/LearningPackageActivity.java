package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.fragment.CommonProblemsFragment;
import com.linlic.ccmtv.yx.fragment.CourseCatalogFragment;
import com.linlic.ccmtv.yx.fragment.CourseIntroductionFragment;
import com.linlic.ccmtv.yx.fragment.QuestionnaireSurveyFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LearningPackageActivity extends FragmentActivity {

    @Bind(R.id.id_iv_activity_title_8)
    ImageView idIvActivityTitle8;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.id_iv_activity_title_8_right)
    ImageView idIvActivityTitle8Right;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_jie_num)
    TextView tvJieNum;
    @Bind(R.id.tv_zhang_num)
    TextView tvZhangNum;
    @Bind(R.id.tv_people_num)
    TextView tvPeopleNum;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.ll_recommend)
    LinearLayout llRecommend;
    @Bind(R.id.ll_cang)
    LinearLayout llCang;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.ll_pay)
    LinearLayout llPay;
    private String[] title = {
            "课程介绍",
            "课程目录",
            "常见问题",
            "调查问卷"
    };
    private MyPagerAdapter adapter;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_package);
        ButterKnife.bind(this);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    @OnClick({R.id.arrow_back, R.id.ll_recommend, R.id.ll_cang,R.id.ll_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.arrow_back:
                finish();
                break;
            case R.id.ll_recommend:
                Intent intent = new Intent(this, RelatedRecommendActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_cang:
                break;
            case R.id.ll_pay:
                pay_dialog();
                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (position == 0) {
                fragment = new CourseIntroductionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title[position]);
                fragment.setArguments(bundle);
            } else if (position == 1) {
                fragment = new CourseCatalogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title[position]);
                fragment.setArguments(bundle);
            } else if (position == 2) {
                fragment = new CommonProblemsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title[position]);
                fragment.setArguments(bundle);
            } else if (position == 3) {
                fragment = new QuestionnaireSurveyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", title[position]);
                fragment.setArguments(bundle);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return title.length;
        }
    }

   private void pay_dialog(){
       //R.style.ActionSheetDialogStyle
       dialog = new Dialog(LearningPackageActivity.this,0);
       LayoutInflater inflater = LayoutInflater.from(this);
       View view1 = inflater.inflate(R.layout.adapter_pay_dialog, null);
       dialog.setContentView(view1);
       TextView tv_confirm = (TextView) view1.findViewById(R.id.tv_confirm);
       TextView tv_cancle = (TextView) view1.findViewById(R.id.tv_cancle);
       tv_confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               dialog.dismiss();
           }
       });
       tv_cancle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
       dialog.show();
   }


}
