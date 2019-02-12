package com.graduation.yau.bigsweet;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    protected ViewPager mShiftViewPager;
    private ShiftViewPagerAdapter mShiftViewPagerAdapter;
    protected BottomNavigationView mBottomNavigationView;
    protected MenuItem mTabMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        mShiftViewPager = findViewById(R.id.shift_main_viewPager);
        mBottomNavigationView = findViewById(R.id.tab_main_bottomNavigationView);
    }

    private void initEvent() {
        mShiftViewPagerAdapter = new ShiftViewPagerAdapter(getSupportFragmentManager());
        mShiftViewPagerAdapter.addFragment(new HomeFragment());
        mShiftViewPagerAdapter.addFragment(new ShopFragment());
        mShiftViewPagerAdapter.addFragment(new PersonFragment());

        mShiftViewPager.setAdapter(mShiftViewPagerAdapter);
        mShiftViewPager.addOnPageChangeListener(mOnPageChangeListener);

        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mTabMenuItem != null) {
                mTabMenuItem.setChecked(false);
            } else {
                mBottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            mTabMenuItem = mBottomNavigationView.getMenu().getItem(position);
            mTabMenuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            mTabMenuItem = menuItem;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    mShiftViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_shop:
                    mShiftViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_person:
                    mShiftViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
}