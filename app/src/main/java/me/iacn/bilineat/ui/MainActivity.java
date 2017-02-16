package me.iacn.bilineat.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.iacn.bilineat.R;
import me.iacn.bilineat.ui.fragment.AboutFragment;
import me.iacn.bilineat.ui.fragment.ActionFragment;
import me.iacn.bilineat.ui.fragment.NeatFragment;
import me.iacn.bilineat.util.ReflectUtils;
import me.iacn.bilineat.util.StatusBarUtils;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager;
    private SharedPreferences mSharePref;

    private BottomNavigationItemView[] mButtons;
    private View.OnClickListener mOnClickListener;
    private BottomNavigationView mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.pink));

        findView();
        initActionBar();
        initData();
        setListener();
        showExplainDialog();
    }

    private void findView() {
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);
    }

    private void initActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData() {
        mSharePref = getSharedPreferences("setting", MODE_WORLD_READABLE);

        BottomNavigationMenuView mMenuView = ReflectUtils.getObjectField(mBottomBar,
                "mMenuView", BottomNavigationMenuView.class);

        mButtons = ReflectUtils.getObjectField(mMenuView,
                "mButtons", BottomNavigationItemView[].class);

        mOnClickListener = ReflectUtils.getObjectField(mMenuView,
                "mOnClickListener", View.OnClickListener.class);

        final List<Fragment> pageList = new ArrayList<>();
        pageList.add(new NeatFragment());
        pageList.add(new ActionFragment());
        pageList.add(new AboutFragment());

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

    private void setListener() {
        mPager.addOnPageChangeListener(this);
        mBottomBar.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        mOnClickListener.onClick(mButtons[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_neat:
                mPager.setCurrentItem(0);
                break;

            case R.id.menu_action:
                mPager.setCurrentItem(1);
                break;

            case R.id.menu_about:
                mPager.setCurrentItem(2);
                break;

            default:
                return false;
        }

        return true;
    }

    private void showExplainDialog() {
        if (mSharePref.getBoolean("showed_explain", false)) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.first_open_title)
                .setMessage(R.string.first_open_hint_message)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.no_longer_remind, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharePref.edit().putBoolean("showed_explain", true).apply();
                    }
                })
                .show();
    }
}