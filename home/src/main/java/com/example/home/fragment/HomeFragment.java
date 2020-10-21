package com.example.home.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.base.fragment.BaseFragment;
import com.example.base.util.YWLogUtil;
import com.example.home.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {
    public static final int TEXT_MAX_SIZE = 15;
    public static final int TEXT_MIN_SIZE = 12;

    private int[] mUnSelectTabRes = new int[]{R.drawable.navigation_homebutton_normal_new, R.drawable.navigation_typebutton_normal_new, R.drawable.navigation_find_normal_new, R.drawable.navigation_cartbutton_normal_new, R.drawable.navigation_userbutton_normal_new, R.drawable.navigation_homebutton_normal_new, R.drawable.navigation_typebutton_normal_new, R.drawable.navigation_find_normal_new, R.drawable.navigation_cartbutton_normal_new, R.drawable.navigation_userbutton_normal_new};
    private int[] mSelectTabRes = new int[]{R.drawable.navigation_homebutton_press_new, R.drawable.navigation_typebutton_press_new, R.drawable.navigation_find_press_new, R.drawable.navigation_cartbutton_press_new, R.drawable.navigation_userbutton_press_new, R.drawable.navigation_homebutton_press_new, R.drawable.navigation_typebutton_press_new, R.drawable.navigation_find_press_new, R.drawable.navigation_cartbutton_press_new, R.drawable.navigation_userbutton_press_new};
    private String[] mTitle = new String[]{"子页面1", "子页面2", "子页面3", "子页面4", "子页面5", "子页面6", "子页面7", "子页面8", "子页面9", "子页面10"};
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private TabLayoutMediator mTabLayoutMediator;
    private List<Fragment> mBaseFragmentArrays = new ArrayList<>();

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mBaseFragmentArrays.add(new Page1Fragment());
        mBaseFragmentArrays.add(new Page2Fragment());
        mBaseFragmentArrays.add(new Page3Fragment());
        mBaseFragmentArrays.add(new Page4Fragment());
        mBaseFragmentArrays.add(new Page5Fragment());
        mBaseFragmentArrays.add(new Page6Fragment());
        mBaseFragmentArrays.add(new Page7Fragment());
        mBaseFragmentArrays.add(new Page8Fragment());
        mBaseFragmentArrays.add(new Page9Fragment());
        mBaseFragmentArrays.add(new Page10Fragment());
    }


    @Override
    protected void findView(View view) {
        mViewPager = view.findViewById(R.id.viewpager_content_view);
        mTabLayout = view.findViewById(R.id.tab_layout);

        mViewPager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                //FragmentStateAdapter内部自己会管理已实例化的fragment对象。
                // 所以不需要考虑复用的问题
                YWLogUtil.e("tag", "createFragment: ---position=" + position);
                return mBaseFragmentArrays.get(position);
            }

            @Override
            public int getItemCount() {
                return mTitle.length;
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < mTitle.length; i++) {
                    if (tab == mTabLayout.getTabAt(i)) {
                        TabLayout.Tab layoutTabAt = mTabLayout.getTabAt(i);
                        if (layoutTabAt != null && layoutTabAt.getCustomView() instanceof TextView) {
                            TextView tabView = (TextView) layoutTabAt.getCustomView();
                            tabView.setCompoundDrawablesWithIntrinsicBounds(0, mSelectTabRes[i], 0, 0);
                            tabView.setTextSize(TEXT_MAX_SIZE);
                            tabView.setTextColor(Color.BLUE);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                YWLogUtil.e("tag", "onTabUnselected: ----tab=" + tab.getCustomView());
                for (int i = 0; i < mTitle.length; i++) {
                    if (tab == mTabLayout.getTabAt(i)) {
                        TabLayout.Tab layoutTabAt = mTabLayout.getTabAt(i);
                        if (layoutTabAt != null && layoutTabAt.getCustomView() instanceof TextView) {
                            TextView tabView = (TextView) layoutTabAt.getCustomView();
                            tabView.setCompoundDrawablesWithIntrinsicBounds(0, mUnSelectTabRes[i], 0, 0);
                            tabView.setTextSize(TEXT_MIN_SIZE);
                            tabView.setTextColor(Color.BLACK);
                        }
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里可以自定义TabView
                YWLogUtil.e("tag", "onConfigureTab: -----position=" + position);
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tab_view, null);
                textView.setText(mTitle[position]);
                textView.setCompoundDrawablesWithIntrinsicBounds(0, mUnSelectTabRes[position], 0, 0);
                tab.setCustomView(textView);

            }
        });
        //要执行这一句才是真正将两者绑定起来
        mTabLayoutMediator.attach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTabLayoutMediator != null) {
            mTabLayoutMediator.detach();
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_home;
    }
}
