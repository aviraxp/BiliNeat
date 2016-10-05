package com.iacn.bilineat.hook;

import android.content.res.XResources;
import android.view.View;
import android.widget.RelativeLayout;

import com.iacn.bilineat.SettingActivity;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class LayoutHook {

    /**
     * 布局Hook的具体实现方法
     *
     * @param res Xposed 的 XResources
     */
    public void doHook(XResources res) {
        XSharedPreferences xSharedPref = new XSharedPreferences(SettingActivity.class.getPackage().getName());

        // 去除发现里的兴趣圈
        if (xSharedPref.getBoolean("cbp_group", false)) {
            res.hookLayout("tv.danmaku.bili", "layout", "bili_app_fragment_discover", new XC_LayoutInflated() {
                @Override
                public void handleLayoutInflated(LayoutInflatedParam layoutInflatedParam) throws Throwable {
                    RelativeLayout rl = (RelativeLayout) layoutInflatedParam.view.findViewById(layoutInflatedParam.res
                            .getIdentifier("group", "id", "tv.danmaku.bili"));
                    rl.setVisibility(View.GONE);
                }
            });
        }
    }
}