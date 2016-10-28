package com.iacn.bilineat.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.iacn.bilineat.R;
import com.iacn.bilineat.ui.fragment.AboutFragment;
import com.iacn.bilineat.ui.fragment.ActionFragment;
import com.iacn.bilineat.ui.fragment.NeatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private BottomNavigationView mBottomBar;
    private SharedPreferences mSharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.pink));

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mBottomBar = (BottomNavigationView) findViewById(R.id.bottom_bar);

        mSharePref = getSharedPreferences("setting", MODE_WORLD_READABLE);

        if (mSharePref.getBoolean("show_explain", true)) showExplainDialog();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    /**
     * 显示说明提示框
     */
    private void showExplainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("说明");
        builder.setMessage("1.本应用仅供技术交流，免费无广告，请勿用于商业及非法用途，如产生法律纠纷与作者无关\n\n2.私自修改本应用、利用本应用牟利等行为，后果由修改/传播者承担\n\n3.使用本应用所造成的一切后果自负\n\n4.如果确定，即你已默认同意以上条款");

        builder.setNegativeButton("不再提醒", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSharePref.edit().putBoolean("show_explain", false).apply();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}