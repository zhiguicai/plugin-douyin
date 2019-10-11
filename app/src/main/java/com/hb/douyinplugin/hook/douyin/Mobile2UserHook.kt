package com.hb.douyinplugin.hook.douyin

import android.util.Log
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.hb.douyinplugin.hook.douyin.entity.DouyinUserEntity
import com.hb.douyinplugin.hook.douyin.entity.MobileEntity
import com.hb.douyinplugin.utils.XLog
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import org.json.JSONObject
import java.util.concurrent.Executors

/**
 * createTime: 2019/10/11.13:35
 * updateTime: 2019/10/11.13:35
 * author: singleMan.
 * desc: 手机号转抖音【第一步：先上传手机号到抖音服务器；第二步：查询数据】
 */
class Mobile2UserHook {

    interface CallBack {
        fun onSuccess()
        fun onError()
    }

    companion object {

        //返回给插件的data
        val reportData = arrayListOf<DouyinUserEntity>()

        //上传手机号
        private fun uploadMobile(list: List<MobileEntity>, callback: CallBack) {
            val classLoader = DouyinRuntime.instance.getClassLoader()
            val hashMap = hashMapOf<String, String>()
            hashMap.put("contact", Gson().toJson(list))
            val api = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.api.UploadContactsApi", classLoader)
            val httpService = XposedHelpers.getStaticObjectField(api, "b")
            val call = XposedHelpers.callMethod(httpService, "uploadContacts", "1", hashMap)
            val executor = Executors.newSingleThreadExecutor()
            executor.execute({
                //                val request = XposedHelpers.callMethod(call, "request")
//                XLog.i(DouyinRuntime.LOG_TAG, "request====>" + XposedHelpers.getObjectField(request, "url"))

                val response = XposedHelpers.callMethod(call, "execute")

                val resultObj = XposedHelpers.callMethod(response, "body")

                val resultString = XposedHelpers.callMethod(resultObj, "toString")

                XLog.i(DouyinRuntime.LOG_TAG, "resultString====>" + resultString)
                val resultJson = JSONObject(resultString as String)
                if (0 == resultJson.optInt("status_code", -1)) {
                    callback?.onSuccess()
                } else {
                    callback?.onError()
                }
            })
            executor.shutdown()
        }


        /**
         * 上传完成后，调用此方法查询已上传通讯录的手机号对应的抖音用户
         */
        private fun queryContactsFriends(page: Int) {
            val classLoader = DouyinRuntime.instance.getClassLoader()
            val fiendApiClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.api.a", classLoader)
            val friendApi = XposedHelpers.callStaticMethod(fiendApiClass, "a")
            val i = XposedHelpers.callMethod(friendApi, "queryContactsFriends", page, 20, 0)
            val eClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.ui.e", classLoader)
            val aclass = XposedHelpers.findClass("a.i", classLoader)
            val staticObjectField = XposedHelpers.getStaticObjectField(aclass, "b")
            XposedHelpers.callMethod(i, "a", XposedHelpers.newInstance(eClass, "0"), staticObjectField)
        }

        /**
         * 加入钩子代码，等待执行
         */
        fun addHookQueryContactsFriends() {
            val classLoader = DouyinRuntime.instance.getClassLoader()
            val eClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.ui.e", classLoader)
            XposedBridge.hookAllMethods(eClass, "then", object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val result = XposedHelpers.callMethod(param.args[0], "e")
//                    XLog.i(DouyinRuntime.LOG_TAG, "result:" + JSON.toJSONString(result))
                    //列表全部数据
                    val userList = XposedHelpers.getObjectField(result, "friends") ?: return
                    //保留关键字段数据
                    val simpleList = arrayListOf<DouyinUserEntity>()
                    for (friend in userList as ArrayList<Any>) {
                        val douyinUser = DouyinUserEntity()
                        douyinUser.uid = XposedHelpers.getObjectField(friend, "uid") as String
                        douyinUser.secUid = XposedHelpers.getObjectField(friend, "secUid") as String
                        douyinUser.nickname = XposedHelpers.getObjectField(friend, "nickname") as String
                        douyinUser.thirdName = XposedHelpers.getObjectField(friend, "thirdName") as String
                        simpleList.add(douyinUser)
                    }
//                    XLog.i(DouyinRuntime.LOG_TAG, "请求结果:" + JSON.toJSONString(simpleList))
                    reportData.addAll(simpleList)

                    val hasMore = XposedHelpers.getBooleanField(result, "hasMore")
                    val cursor = XposedHelpers.getIntField(result, "cursor")

//                    XLog.i(DouyinRuntime.LOG_TAG, "hasMore:" + hasMore + "==cursor：" + cursor)
                    if (hasMore && cursor != 0) {
                        queryContactsFriends(cursor)
                    } else {
                        XLog.i(DouyinRuntime.LOG_TAG, "simpleUserList:" + JSON.toJSONString(reportData))
                        DouyinRuntime.instance.postMessageToPlugin(JSON.toJSONString(reportData), 1001)
                    }
//                    XLog.i(DouyinRuntime.LOG_TAG, "userList:" + JSON.toJSONString(userList))
                    super.beforeHookedMethod(param)
                }
            })
        }

        /**
         * 手机号转抖音号
         */
        fun mobileToDouyinUser(list: List<MobileEntity>) {
            reportData.clear()
            uploadMobile(list, object : CallBack {
                override fun onSuccess() {
                    Log.i(DouyinRuntime.LOG_TAG, "上传通讯录成功")
                    queryContactsFriends(0)
                }

                override fun onError() {
                    Log.i(DouyinRuntime.LOG_TAG, "上传通讯录失败")
                    DouyinRuntime.instance.postMessageToPlugin("{\"error\":\"上传通讯录失败\"}", -1)
                }

            })

        }
    }
}