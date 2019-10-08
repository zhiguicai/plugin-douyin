package com.hb.douyinplugin.hook.douyin

import android.content.Context
import android.widget.Toast
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * createTime: 2019/9/27.14:15
 * updateTime: 2019/9/27.14:15
 * author: singleMan.
 * desc: 抖音初始化hook
 */
class DouyinInit {

    companion object {
        /**
         * 抖音运行时初始化
         */
        fun initDouyinRuntime(classLoader: ClassLoader) {
            val douyinMainClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.main.MainActivity", classLoader)
            XposedHelpers.findAndHookMethod(douyinMainClass, "onResume", object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    //TODO 抖音启动成功
                    val context = param.thisObject
                    if (null != context) {
                        DouyinRuntime.instance.initContext(context as Context)

                        initDouyinDateBase(context)

                        Toast.makeText(context, "抖音-Plugin SUCCESS!", Toast.LENGTH_SHORT).show()

                        //TODO test

                       // LoadFansImpl.queryContacts()


                       // CommentHook.hookComment()
//                        CommentHook.doComment("6740116735640292616","感觉自己的北漂生涯快结束了")

                        //FeedHook.hookFeed()
//                        FeedHook.doFeed("6740116735640292616");

                       // MessageHook.hookSendPhoto();

//                        MessageHook.sendImage("3768748035023923", "/storage/emulated/0/FreeVideo/FreeVideo_share_image.jpg")

//                        MessageHook.hookSendVideo(
//                                "3768748035023923",
//                                "109864890252",
//                                "6693124310501969166",
//                                "分享视频",
//                                "17b4b00084db599b06eaa",
//                                "https://p3-dy.byteimg.com/aweme/300x400/17b4b00084db599b06eaa.jpeg"
//                        )


                    }
                    super.afterHookedMethod(param)
                }
            })

        }

        /**
         * init 数据库操作对象
         */
        private fun initDouyinDateBase(context: Context) {
            val classLoader = context.classLoader
            //find 抖音 SQLiteOpenHelper 的 Class
            val douyinSQLiteOpenHelperClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.h.a.a", classLoader)
            // new 构造
            val newInstance = XposedHelpers.newInstance(douyinSQLiteOpenHelperClass, context, "db_im_xx")
            // 获取数据库操作对象
            val sqLiteDataBase = XposedHelpers.callMethod(newInstance, "getWritableDatabase")
            // 赋值
            DouyinRuntime.instance.initDatabase(sqLiteDataBase)


        }
    }
}