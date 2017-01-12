package com.iacn.bilineat.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iacn.bilineat.BuildConfig;
import com.iacn.bilineat.Constant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;

import static com.iacn.bilineat.XposedInit.xSharedPref;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findFirstFieldByExactType;
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

        boolean isShowCategory = !xSharedPref.getBoolean("category_game", true);
        boolean isShowFound = !xSharedPref.getBoolean("found_game", true);
        boolean isShowToolBar = !xSharedPref.getBoolean("toolbar_game", true);
        boolean isShowDraw = !xSharedPref.getBoolean("drawer_promote", true);

        int homeIndex = Integer.parseInt(xSharedPref.getString("default_page", "1"));

        // 根据当前版本决定要Hook的类和方法名
        switch (currentVersion) {
            case "4.33.3":
                hookResult("cez", "e", boolean.class, isShowCategory);
                hookResult("cez", "f", boolean.class, isShowToolBar);
                hookResult("cez", "g", boolean.class, isShowDraw);
                hookResult("cez", "h", boolean.class, isShowFound);
                hookResult("cez", "i", boolean.class, false);

                hookTheme("dmv", "ase");
                removeFoundMall("cwa");
                break;

            case "4.33.0":
                hookResult("cez", "e", boolean.class, isShowCategory);
                hookResult("cez", "f", boolean.class, isShowToolBar);
                hookResult("cez", "g", boolean.class, isShowDraw);
                hookResult("cez", "h", boolean.class, isShowFound);
                hookResult("cez", "i", boolean.class, false);

                hookTheme("dmv", "ase");
                removeFoundMall("cwa");
                break;

            case "4.32.0":
                hookResult("ceu", "f", boolean.class, isShowCategory);
                hookResult("ceu", "g", boolean.class, isShowToolBar);
                hookResult("ceu", "h", boolean.class, isShowDraw);
                hookResult("ceu", "i", boolean.class, isShowFound);
                hookResult("ceu", "j", boolean.class, false);

                hookTheme("dlz", "asa");
                removeFoundMall("cvt");
                break;
        }

        removeDrawerVip();
        hookThemeDialog();
        hookBangumi();
        hookMovie();
        hookCover();
        hookPage(homeIndex);
        addNeatEntrance();
    }

    private void hookCover() {
        Class<?> videoClass = findClass("tv.danmaku.bili.ui.video.BaseVideoDetailsActivity", mClassLoader);

        findAndHookMethod(videoClass, "onCreateOptionsMenu", Menu.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Menu menu = (Menu) param.args[0];
                        menu.add("保存封面图");
                    }
                });

        findAndHookMethod(videoClass, "onOptionsItemSelected", MenuItem.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                MenuItem item = (MenuItem) param.args[0];

                if (item.getItemId() == 0) {
                    // 这是一个自定义选项
                    Object obj = getObjectField(param.thisObject, "mCover");
                    Drawable drawable = (Drawable) callMethod(obj, "getDrawable");

                    if (drawable != null) {
                        saveDrawableToLocal(drawable);
                    }
                }
            }
        });
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
     * 去除发现里的周边商城
     */
    private void removeFoundMall(String className) {
        if (!xSharedPref.getBoolean("found_mall", false)) return;

        findAndHookMethod("bl." + className, mClassLoader, "onViewCreated", View.class,
                Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        View view = (View) param.args[0];

                        int id = view.getContext().getResources()
                                .getIdentifier("bmall", "id", Constant.biliPackageName);

                        if (id != 0) {
                            view.findViewById(id).setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * 去除侧边栏我的大会员
     */
    private void removeDrawerVip() {
        final boolean myVip = !xSharedPref.getBoolean("drawer_my_vip", false);
        final boolean vipPoint = !xSharedPref.getBoolean("drawer_vip_point", false);

        // Q：为什么要使用这种方式来判断？
        // A：因为 Hook 是一项很占资源的操作，这里为了节省资源
        //    只在两个选项中有某个开启时才进行挂钩，而不是每次根据 Value 值去设置
        //    其他地方同理

        if (myVip && vipPoint) return;

        findAndHookMethod("tv.danmaku.bili.ui.main.NavigationFragment", mClassLoader, "onViewCreated",
                View.class, Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class clazz = findClass("android.support.design.widget.NavigationView", mClassLoader);
                        Field field = findFirstFieldByExactType(param.thisObject.getClass(), clazz);

                        Menu menu = (Menu) callMethod(field.get(param.thisObject), "getMenu");

                        // 取得第二个 Menu，为 我的大会员
                        menu.getItem(1).setVisible(myVip);

                        // 取得第三个 Menu，为 会员积分
                        menu.getItem(2).setVisible(vipPoint);
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

    private void addNeatEntrance() {
        findAndHookMethod("tv.danmaku.bili.preferences.BiliPreferencesActivity$a", mClassLoader,
                "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        final Activity activity = (Activity) callMethod(param.thisObject, "getActivity");

                        Preference preference = new Preference(activity);
                        preference.setTitle("净化设置");
                        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                            @Override
                            public boolean onPreferenceClick(Preference preference) {
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(BuildConfig.APPLICATION_ID,
                                        BuildConfig.APPLICATION_ID + ".ui.MainActivity"));

                                activity.startActivity(intent);
                                activity.overridePendingTransition(0, 0);

                                return true;
                            }
                        });

                        PreferenceScreen screen = (PreferenceScreen)
                                callMethod(param.thisObject, "getPreferenceScreen");

                        PreferenceCategory category = (PreferenceCategory)
                                ((List) getObjectField(screen, "mPreferenceList")).get(1);

                        category.addPreference(preference);
                    }
                });
    }
}