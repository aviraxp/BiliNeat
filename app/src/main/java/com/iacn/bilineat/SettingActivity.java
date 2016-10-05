package com.iacn.bilineat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingActivity extends Activity {

    private static SharedPreferences mSharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.pink));

        getFragmentManager().beginTransaction().replace(R.id.fl_content, new SettingFragment()).commit();
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        private ListPreference lspPage;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 设置SharePreference文件为全局可读
            getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
            addPreferencesFromResource(R.xml.pref_settings);

            mSharePref = getPreferenceManager().getSharedPreferences();
            showExplainDialog();

            lspPage = (ListPreference) findPreference("lsp_default_page");
            lspPage.setOnPreferenceChangeListener(this);

            int index = Integer.parseInt(mSharePref.getString("lsp_default_page", "1"));
            lspPage.setValueIndex(index);
            lspPage.setSummary("当前打开后进入的页面是：" + lspPage.getEntries()[index]);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            int index = Integer.parseInt((String) newValue);
            lspPage.setSummary("当前打开后进入的页面是：" + lspPage.getEntries()[index]);
            return true;
        }

        /**
         * 显示说明提示框
         */
        private void showExplainDialog() {
            if (mSharePref.getBoolean("isShowExplain", true)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("说明");
                builder.setMessage("1.本应用仅供技术交流，免费无广告，请勿用于商业及非法用途，如产生法律纠纷与作者无关\n\n2.私自修改本应用、利用本应用牟利等行为，后果由修改/传播者承担\n\n3.使用本应用所造成的一切后果自负\n\n4.如果确定，即你已默认同意以上条款");

                builder.setNegativeButton("不再提醒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharePref.edit().putBoolean("isShowExplain", false).apply();
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
    }
}