//package com.linlic.ccmtv.yx.activity.my.personal_profile;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//import android.view.Window;
//import android.widget.LinearLayout;
//
//import com.baidu.mobstat.StatService;
//import com.linlic.ccmtv.yx.R;
//
///**
// * Created by Administrator on 2017/8/17.
// */
//public class Personal_profile_Main  extends FragmentActivity {
//
//    //个人中心
//    private Personal_profile personal_profile;
//    //点赞
//    private  Personal_profile_assist personal_profile_assist;
//    //评论
//    private Personal_profile_comment personal_profile_comment;
//    //关注
//    private Personal_profile_attention personal_profile_attention;
//    //acitvity 数组
//    private static Fragment[] fragments;
//    // button点击的index  当前fragment的index
//    private int index, currentTabIndex;
//    private LinearLayout[] mTabs;
//    Context context;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.personal_profile_main);
//        context = this;
//
//        personal_profile = new Personal_profile();
//        personal_profile_assist = new Personal_profile_assist();
//        personal_profile_comment = new Personal_profile_comment();
//        personal_profile_attention = new Personal_profile_attention();
//
//        fragments = new Fragment[]{personal_profile, personal_profile_assist, personal_profile_comment, personal_profile_attention};
//        mTabs = new LinearLayout[4];
//        mTabs[0] = (LinearLayout) findViewById(R.id.layout_1);
//        mTabs[1] = (LinearLayout) findViewById(R.id.layout_2);
//        mTabs[2] = (LinearLayout) findViewById(R.id.layout_3);
////        mTabs[3] = (LinearLayout) findViewById(R.id.layout_4);
//        // 把第一个tab设为选中状态
//        mTabs[0].setSelected(true);
//
//        getSupportFragmentManager().beginTransaction().add(R.id.s_fragments, personal_profile)
//                .add(R.id.s_fragments, personal_profile_assist).add(R.id.s_fragments, personal_profile_comment).add(R.id.s_fragments, personal_profile_attention)
//                .commit();
//
//    }
//
//    private void setButtomImg() {
//        int selectColor = Color.parseColor("#4A5578");               //Tab字体选中之后的字体颜色
//        int unselectColor = Color.parseColor("#4A5578");            //Tab字体未选中的字体颜色
//    }
//
//    /**
//     * 对TextView设置不同状态时其文字颜色。
//     * <p/>
//     * http://blog.csdn.net/jdsjlzx/article/details/7645004
//     */
//    private ColorStateList createColorStateList(int normal, int focused) {
//        int[] colors = new int[]{normal, focused};
//        int[][] states = new int[2][];
//        states[0] = new int[]{-android.R.attr.state_selected};
//        states[1] = new int[]{android.R.attr.state_selected};
//        ColorStateList colorList = new ColorStateList(states, colors);
//        return colorList;
//    }
//
//    /**
//     * name：底部button点击事件
//     * <p/>
//     * author: Mr.song
//     * 时间：2016-1-29 下午2:32:37
//     *
//     * @param view
//     */
//    public void onTabClicked(View view) {
//        switch (view.getId()) {
//            case R.id.layout_1:
//                index = 0;
//                break;
//            case R.id.layout_2:
//                index = 1;
//                break;
//            case R.id.layout_3:
//                index = 2;
//                break;
//            /*case R.id.layout_4:
//                index = 3;
//                break;*/
//
//        }
//        if (currentTabIndex != index) {
//            FragmentTransaction trx = getSupportFragmentManager()
//                    .beginTransaction();
//            trx.hide(fragments[0]);
//            trx.hide(fragments[1]);
//            trx.hide(fragments[2]);
//            trx.hide(fragments[3]);
//            if (!fragments[index].isAdded()) {
//                trx.add(R.id.s_fragments, fragments[index]);
//            }
//            trx.show(fragments[index]).commit();
//        }
//        mTabs[currentTabIndex].setSelected(false);
//        // 把当前tab设为选中状态
//        mTabs[index].setSelected(true);
//        currentTabIndex = index;
//    }
//
//    /**
//     * name：跳转至fragment
//     * author：Larry
//     * data：2016/4/5 18:07
//     */
//    public void ToFragment(int index) {
//        FragmentTransaction trx = getSupportFragmentManager()
//                .beginTransaction();
//        if (currentTabIndex != index) {
//            trx.hide(fragments[0]);
//            trx.hide(fragments[1]);
//            trx.hide(fragments[2]);
//            trx.hide(fragments[3]);
//            if (!fragments[index].isAdded()) {
//                trx.add(R.id.s_fragments, fragments[index]);
//            }
//            trx.show(fragments[index]).commit();
//        }
//        mTabs[currentTabIndex].setSelected(false);
//        // 把当前tab设为选中状态
//        mTabs[index].setSelected(true);
//        currentTabIndex = index;
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        /**
//         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
//        StatService.onResume(this);
//    }
//
//
//    public void onPause() {
//        super.onPause();
//        /**
//         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
//         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
//         */
//        StatService.onPause(this);
//    }
//
//
//
//}
