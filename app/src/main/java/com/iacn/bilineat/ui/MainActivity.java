package com.iacn.bilineat.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.iacn.bilineat.R;
import com.iacn.bilineat.ui.fragment.NeatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private BottomNavigationView mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.pink));

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);

        final List<Fragment> pageList = new ArrayList<>();
        pageList.add(new NeatFragment());
        pageList.add(new SettingsFragment());

        mPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return pageList.get(position);
            }

            @Override
            public int getCount() {
                return pageList.size();
            }
        });
    }
}