package com.iacn.bilineat.hook;

import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

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