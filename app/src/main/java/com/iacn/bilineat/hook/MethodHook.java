package com.iacn.bilineat.hook;

import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static com.iacn.bilineat.XposedInit.xSharedPref;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodsByExactParameters;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setBooleanField;
import static de.robv.android.xposed.XposedHelpers.setIntField;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class MethodHook {

    private ClassLoader mClassLoader;

    /**
     * Method Hook的具体实现方法
     *
     * @param classLoader    类加载器
     * @param currentVersion 当前版本
     */
    public void doHook(ClassLoader classLoader, String currentVersion) {
        mClassLoader = classLoader;

        boolean isShowCategory = !xSharedPref.getBoolean("cbp_category", true);
        boolean isShowFound = !xSharedPref.getBoolean("cbp_found", true);
        boolean isShowToolBar = !xSharedPref.getBoolean("cbp_toolbar", true);
        boolean isShowDraw = !xSharedPref.getBoolean("cbp_draw", true);
        boolean isDisMyVip = !xSharedPref.getBoolean("disable_my_vip", false);

        int homeIndex = Integer.parseInt(xSharedPref.getString("lsp_default_page", "1"));

        // 根据当前版本决定要Hook的类和方法名
        switch (currentVersion) {
            case "4.27.0":
                hookResult("cdj", "f", boolean.class, isShowCategory);
                hookResult("cdj", "g", boolean.class, isShowToolBar);
                hookResult("cdj", "h", boolean.class, isShowDraw);
                hookResult("cdj", "i", boolean.class, isShowFound);
                hookResult("cdj", "j", false);

                hookResult("cdj", "s", isDisMyVip);

                hookTheme("ffi", "bco");
                break;

            case "4.26.3":
                hookResult("cag", "f", boolean.class, isShowCategory);
                hookResult("cag", "g", boolean.class, isShowToolBar);
                hookResult("cag", "h", isShowDraw);
                hookResult("cag", "i", isShowFound);
                hookResult("cag", "j", false);

                hookTheme("fbm", "bch");
                break;

            case "4.25.0":
                hookResult("caa", "f", boolean.class, isShowCategory);
                hookResult("caa", "g", boolean.class, isShowToolBar);
                hookResult("caa", "h", isShowDraw);
                hookResult("caa", "i", isShowFound);

                hookTheme("fbi", "bcf");
                break;
        }

        hookThemeDialog();
        hookBangumi();
        hookMovie();
        hookPage(homeIndex);
    }

    /**
     * 处理侧滑菜单、发现、Toolbar、分类里的广告开关
     *
     * @param className  广告开关类名
     * @param methodName 具体项目的方法名
     * @param state      开关状态
     */
    private void hookResult(String className, String methodName, boolean state) {
        findAndHookMethod("bl." + className, mClassLoader, methodName,
                XC_MethodReplacement.returnConstant(state));
    }

    /**
     * 使用返回值类型查找并处理侧滑菜单、发现、Toolbar、分类里的广告开关
     */
    private void hookResult(String className, String methodName, Class<?> returnType, boolean state) {
        hookMethodByReturnType("bl." + className, methodName, returnType, state);
    }

    /**
     * 破解主题免费
     *
     * @param className Hook主题的类名
     * @param paramName Hook主题的参数类型
     */
    private void hookTheme(String className, String paramName) {
        findAndHookMethod("bl." + className, mClassLoader, "a", "bl." + paramName, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0] == null) return;

                List list = (List) getObjectField(param.args[0], "mList");

                if (list != null) {
                    for (Object theme : list) {
                        setBooleanField(theme, "mIsFree", true);
                        setIntField(theme, "mPrice", 0);
                    }
                }
            }
        });
    }

    /**
     * 去除已购买主题过期的提示框
     */
    private void hookThemeDialog() {
        findAndHookMethod("tv.danmaku.bili.MainActivity", mClassLoader, "c", XC_MethodReplacement.DO_NOTHING);
    }

    /**
     * 破解版权电影下载
     */
    private void hookMovie() {
        hookMethodByReturnType("com.bilibili.api.BiliVideoDetail", "c", boolean.class, true);
    }

    /**
     * 破解版权番下载
     */
    private void hookBangumi() {
        Method[] methods = findMethodsByExactParameters(
                findClass("com.bilibili.api.bangumi.BiliBangumiSeason", mClassLoader), boolean.class);

        XposedBridge.hookMethod(methods[0], new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                setBooleanField(param.thisObject, "mDownloadable", true);
            }
        });
    }

    /**
     * 设置默认进入页面
     *
     * @param pageIndex 页面的角标值
     */
    private void hookPage(final int pageIndex) {
        if (pageIndex != 1) {
            findAndHookMethod("tv.danmaku.bili.ui.main.HomeFragment", mClassLoader,
                    "onViewCreated", View.class, Bundle.class, new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            setIntField(param.thisObject, "b", pageIndex);
                        }
                    });
        }
    }

    /**
     * 根据返回值类型来进行 Hook
     */
    private void hookMethodByReturnType(String className, String methodName, Class<?> returnType, boolean value) {
        Method[] methods = findMethodsByExactParameters(findClass(className, mClassLoader), returnType);

        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(value));
            }
        }
    }
}