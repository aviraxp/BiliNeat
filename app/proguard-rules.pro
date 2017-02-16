#保持 IXposedHookLoadPackage 的实现类
-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
  public void *(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam);
}

#保持 IXposedHookInitPackageResources 的实现类
-keep class * implements de.robv.android.xposed.IXposedHookInitPackageResources {
  public void *(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam);
}

# 保持反射变量
-keepclassmembers class android.support.design.widget.BottomNavigationView {
   android.support.design.internal.BottomNavigationMenuView mMenuView;
}

-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
   android.support.design.internal.BottomNavigationItemView[] mButtons;
   android.view.View$OnClickListener mOnClickListener;
}