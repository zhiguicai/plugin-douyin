package com.hb.douyinplugin.hook

import android.text.TextUtils
import com.hb.douyinplugin.hook.douyin.DouyinInit
import com.hb.douyinplugin.hook.douyin.DouyinRuntime
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * createTime: 2019/9/27.13:58
 * updateTime: 2019/9/27.13:58
 * author: singleMan.
 * desc: hook 入口类
 */
class MainHook : IXposedHookLoadPackage {


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classLoader = lpparam.classLoader
        val packageName = lpparam.packageName

        if (TextUtils.equals(packageName, DouyinRuntime.PACKAGE_NAME)) {
            //TODO douyin hook impl
            DouyinInit.initDouyinRuntime(classLoader)


        }

    }

}