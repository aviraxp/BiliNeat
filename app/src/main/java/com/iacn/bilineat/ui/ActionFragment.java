package com.iacn.bilineat.ui;

import android.preference.ListPreference;
import android.preference.Preference;

import com.iacn.bilineat.R;

/**
 * Created by iAcn on 2016/10/28
 * Emali iAcn0301@foxmail.com
 */

public class ActionFragment extends BaseFragment implements Preference.OnPreferenceChangeListener {

    private ListPreference defaultPage;

    @Override
    protected int getXmlId() {
        return R.xml.pref_action;
    }

    @Override
    protected void initPreference() {
        defaultPage = (ListPreference) findPreference("default_page");
        defaultPage.setOnPreferenceChangeListener(this);

        int index = Integer.parseInt(getPreferenceManager()
                .getSharedPreferences().getString("default_page", "1"));
        defaultPage.setValueIndex(index);
        defaultPage.setSummary("当前打开后进入的页面是：" + defaultPage.getEntries()[index]);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int index = Integer.parseInt((String) newValue);
        defaultPage.setSummary("当前打开后进入的页面是：" + defaultPage.getEntries()[index]);
        return true;
    }
}