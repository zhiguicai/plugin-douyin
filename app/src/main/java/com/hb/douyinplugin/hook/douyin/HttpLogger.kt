package com.hb.douyinplugin.hook.douyin


class HttpLogger {


    companion object {
        fun hook() {
//            val classLoader = DouyinRuntime.instance.getClassLoader()
//            val findClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.utils.ap", classLoader)
//            var list = XposedHelpers.callStaticMethod(findClass, "a", DouyinRuntime.instance.getContext())
//            XLog.i(DouyinRuntime.LOG_TAG, "====>" + JSON.toJSONString(list))
//            val api = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.api.UploadContactsApi", classLoader)


            //----------------------------------------
//            Thread({
//                val result = XposedHelpers.callStaticMethod(api, "a", list, 1)
//
//                val unregisterList = XposedHelpers.getObjectField(result, "contacts")
//                val registerList = XposedHelpers.getObjectField(result, "users")
//
//                Log.i(DouyinRuntime.LOG_TAG, "unregisterList====>" + JSON.toJSONString(unregisterList))
//
//                Log.i(DouyinRuntime.LOG_TAG, "registerList====>" + JSON.toJSONString(registerList))
//            }).start()

//            val aClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.api.a", classLoader)
//            val friendApi = XposedHelpers.callStaticMethod(aClass, "a")
//            val i = XposedHelpers.callMethod(friendApi, "queryContactsFriends", 0, 20, 0)
//            val eClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.ui.e", classLoader)
//            XposedBridge.hookAllMethods(eClass, "then", object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    val result = XposedHelpers.callMethod(param.args[0], "e")
//                    XLog.i(DouyinRuntime.LOG_TAG, "result:" + JSON.toJSONString(result))
//                    val userList = XposedHelpers.getObjectField(result, "friends")
//                    XLog.i(DouyinRuntime.LOG_TAG, "userList:" + JSON.toJSONString(userList))
//                    super.beforeHookedMethod(param)
//                }
//            })
//
//            val aclass = XposedHelpers.findClass("a.i", classLoader)
//            val staticObjectField = XposedHelpers.getStaticObjectField(aclass, "b")
//            XposedHelpers.callMethod(i, "a", XposedHelpers.newInstance(eClass, "0"), staticObjectField)
//            val result = XposedHelpers.callMethod(i, "e")
//            val userList = XposedHelpers.getObjectField(result, "friends")
//            XLog.i(DouyinRuntime.LOG_TAG, "userList:" + JSON.toJSONString(userList))


            //----------------------------------------

//            val digestUtilsClass = XposedHelpers.findClass("com.bytedance.common.utility.DigestUtils", classLoader)
//            val contactModelClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.model.ContactModel", classLoader)
//
//            val hashMap = hashMapOf<String, String>()
//            val instance = MessageDigest.getInstance("SHA-256")
//            val arrayList = arrayListOf<Any>()
//
//            if (null == list) {
//                return
//            }
//
//            for (obj in list as List<Object>) {
//                for (phoneNumber in XposedHelpers.getObjectField(obj, "phoneNumber") as ArrayList<String>) {
//                    var reportMobile = ""
//                    if (phoneNumber != null) {
//                        val nationalNumber = XposedHelpers.callMethod(obj, "nationalNumber", phoneNumber) as String
//                        val digest = instance.digest(nationalNumber.toByteArray(Charset.forName("UTF-8")))
//                        reportMobile = XposedHelpers.callStaticMethod(digestUtilsClass, "toHexString", digest) as String
//                    } else {
//                        reportMobile = ""
//                    }
//
//                    arrayList.add(XposedHelpers.newInstance(contactModelClass, reportMobile, ""))
//                }
//            }
//
//            hashMap.put("contact", JSON.toJSONString(arrayList))
//
//            val netUtilClass = XposedHelpers.findClass("com.ss.android.common.applog.NetUtil", classLoader)
//            XposedHelpers.callStaticMethod(netUtilClass, "putCommonParams", hashMap, true)
//
//            XLog.i(DouyinRuntime.LOG_TAG, "params====>" + JSON.toJSONString(hashMap))
//
//            val httpService = XposedHelpers.getStaticObjectField(api, "b")
//
//            val call = XposedHelpers.callMethod(httpService, "uploadHashContacts", "1", hashMap)
//
//
//            Thread({
//
//                val request = XposedHelpers.callMethod(call, "request")
//
//                XLog.i(DouyinRuntime.LOG_TAG, "request====>" + XposedHelpers.getObjectField(request, "url"))
//
//                val response = XposedHelpers.callMethod(call, "execute")
//
//                val resultObj = XposedHelpers.callMethod(response, "body")
//
//                val resultString = XposedHelpers.callMethod(resultObj, "toString")
//
//                XLog.i(DouyinRuntime.LOG_TAG, "resultString====>" + resultString)
//            }).start()

            //-------------------------------------------------------
//
//            val retrofit = Retrofit.Builder()
//                    .baseUrl("https://aweme.snssdk.com")
//                    .build()
//            val call_a = retrofit.create(UploadContactsService::class.java).uploadHashContacts("1", hashMap)
//            call_a.enqueue(object : Callback<ResponseBody> {
//                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
//                    Log.i(DouyinRuntime.LOG_TAG, "resultString====>   onFailure")
//                }
//
//                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
//                    Log.i(DouyinRuntime.LOG_TAG, "resultString====>" + response.body()!!.string())
//                }
//
//            })


//            val networkClient = XposedHelpers.findClass("okhttp3.RealCall", DouyinRuntime.instance.getClassLoader())
////            val networkClient = XposedHelpers.findClass("com.bytedance.common.utility.NetworkClient", DouyinRuntime.instance.getClassLoader())
//            XposedBridge.hookAllMethods(networkClient, "getResponseWithInterceptorChain", object : XC_MethodHook() {
//                override fun afterHookedMethod(param: MethodHookParam) {
//                    super.afterHookedMethod(param)
//                    val request = XposedHelpers.getObjectField(param.thisObject, "originalRequest")
//                    Log.i(DouyinRuntime.LOG_TAG, "--->" + XposedHelpers.callMethod(request, "toString") as String)
//                }
//            })
//            val activity = XposedHelpers.findClass("com.ss.android.ugc.aweme.friends.ui.ContactsActivity", DouyinRuntime.instance.getClassLoader())
//            XposedHelpers.findAndHookMethod(activity, "a", String::class.java, object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    val param1 = param.args[0]
//                    Log.i(DouyinRuntime.LOG_TAG, "param:$param1")
//                    super.beforeHookedMethod(param)
//                }
//
//            })
//            val call2 = XposedHelpers.findClass("retrofit2.i", DouyinRuntime.instance.getClassLoader())
////            val networkClient = XposedHelpers.findClass("com.bytedance.common.utility.NetworkClient", DouyinRuntime.instance.getClassLoader())
//            XposedBridge.hookAllMethods(call2, "execute", object : XC_MethodHook() {
//                override fun afterHookedMethod(param: MethodHookParam) {
//                    val call = param.thisObject
//                    val request = XposedHelpers.callMethod(call, "request")
//                    Log.i(DouyinRuntime.LOG_TAG, "--->" + XposedHelpers.callMethod(request, "toString") as String)
//                    super.afterHookedMethod(param)
//                }
//            })

        }
    }
}