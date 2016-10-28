package com.iacn.bilineat.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by iAcn on 2016/10/28
 * Emali iAcn0301@foxmail.com
 */

public abstract class BaseFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 统一所有 Preference 的设置文件名
        getPreferenceManager().setSharedPreferencesName("setting");
        // 使设置文件全局可读
//        getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);

        addPreferencesFromResource(getXmlId());
        initPreference();
    }

    protected void initPreference() {

    }

    protected abstract int getXmlId();
}