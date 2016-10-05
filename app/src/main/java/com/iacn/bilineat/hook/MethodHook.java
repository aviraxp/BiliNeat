package com.iacn.bilineat.hook;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findMethodsByExactParameters;

/**
 * Created by iAcn on 2016/10/5
 * Emali iAcn0301@foxmail.com
 */

public class MethodHook {

    private ClassLoader mClassLoader;

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