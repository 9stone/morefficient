package com.ninestone.morefficient.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ninestone.morefficient.R;
import com.ninestone.morefficient.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页的Fragment的适配器
 * Created by zhenglei on 2018/9/14.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private String[] mTitles;


    public MainFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mTitles = context.getResources().getStringArray(R.array.task_tab_titles);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        if(CommonUtil.isEmptyList(mFragments)) {
            return new Fragment();
        }

        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
