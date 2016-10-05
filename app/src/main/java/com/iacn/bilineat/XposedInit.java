package com.iacn.bilineat;

import com.iacn.bilineat.hook.LayoutHook;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class XposedInit implements IXposedHookLoadPackage, IXposedHookInitPackageResources {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (!"tv.danmaku.bili".equals(resParam.packageName)) return;

        new LayoutHook().doHook(resParam.res);
    }
}