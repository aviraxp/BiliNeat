#保持IXposedHookLoadPackage的实现类
-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
  public void *(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam);
}

#保持IXposedHookInitPackageResources的实现类
-keep class * implements de.robv.android.xposed.IXposedHookInitPackageResources {
  public void *(de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam);
}