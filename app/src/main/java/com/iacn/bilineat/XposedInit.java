package com.iacn.bilineat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.iacn.bilineat.hook.LayoutHook;
import com.iacn.bilineat.hook.MethodHook;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class XposedInit implements IXposedHookZygoteInit, IXposedHookLoadPackage, IXposedHookInitPackageResources {

    public static XSharedPreferences xSharedPref;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        xSharedPref = new XSharedPreferences(BuildConfig.APPLICATION_ID, "setting");
        xSharedPref.makeWorldReadable();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!"tv.danmaku.bili".equals(loadPackageParam.packageName)) return;

        Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
        Context context = (Context) callMethod(activityThread, "getSystemContext");

        String currentVersion = context.getPackageManager()
                .getPackageInfo("tv.danmaku.bili", PackageManager.COMPONENT_ENABLED_STATE_DEFAULT).versionName;

        // 判断插件是否支持当前哔哩哔哩版本
        for (String version : Constant.supportVersions) {
            if (version.equals(currentVersion)) {
                xSharedPref.reload();
                new MethodHook().doHook(loadPackageParam.classLoader, currentVersion);
                return;
            }
        }

        // 如果不支持当前哔哩哔哩版本,弹出 Toast 提示
        findAndHookMethod("tv.danmaku.bili.ui.splash.SplashActivity", loadPackageParam.classLoader, "onCreate",
                Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        Toast.makeText((Context) param.thisObject, "哔哩净化暂不支持你的版本哦~", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resParam) throws Throwable {
        if (!"tv.danmaku.bili".equals(resParam.packageName)) return;

        xSharedPref.reload();
        new LayoutHook().doHook(resParam.res);
    }
}