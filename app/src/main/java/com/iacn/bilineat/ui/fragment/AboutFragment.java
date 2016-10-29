package com.iacn.bilineat.ui.fragment;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.SwitchPreference;

import com.iacn.bilineat.R;
import com.iacn.bilineat.ui.MainActivity;

/**
 * Created by iAcn on 2016/10/28
 * Emali iAcn0301@foxmail.com
 */

public class AboutFragment extends BaseFragment {

    private PackageManager mManager;
    private ComponentName mComponentName;

    @Override
    protected int getXmlId() {
        return R.xml.pref_about;
    }

    @Override
    protected void initPreference() {
        mManager = getActivity().getPackageManager();
        mComponentName = new ComponentName(getActivity(), MainActivity.class.getName() + "-Alias");

        SwitchPreference hideLauncher = (SwitchPreference) findPreference("hide_launcher");
        hideLauncher.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;

                int state = value ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED;

                mManager.setComponentEnabledSetting(mComponentName, state,
                        PackageManager.DONT_KILL_APP);

                return true;
            }
        });

        boolean isHide = mManager.getComponentEnabledSetting(mComponentName) ==
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        hideLauncher.setChecked(isHide);
    }
}