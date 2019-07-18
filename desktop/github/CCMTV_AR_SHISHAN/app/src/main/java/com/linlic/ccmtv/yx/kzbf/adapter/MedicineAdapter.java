package com.linlic.ccmtv.yx.kzbf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Niklaus on 2017/12/20.
 */

public class MedicineAdapter extends FragmentPagerAdapter {

    private List<String> dataList;
    private List<Fragment> fragmentList;

    public MedicineAdapter(FragmentManager fm,List<String> dataList,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.dataList = dataList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataList.get(position);
    }
}
