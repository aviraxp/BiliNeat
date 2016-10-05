package com.iacn.bilineat.hook;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodsByExactParameters;

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