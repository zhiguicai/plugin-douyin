package com.hb.douyinplugin.hook.douyin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import org.json.JSONObject
import java.util.concurrent.Executors

/**
 * createTime: 2019/9/27.14:00
 * updateTime: 2019/9/27.14:00
 * author: singleMan.
 * desc: 抖音运行时
 */
class DouyinRuntime private constructor() {

    companion object {
        val LOG_TAG = "hb_douyin"
        val PACKAGE_NAME = "com.ss.android.ugc.aweme"

        val instance: DouyinRuntime by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DouyinRuntime()
        }
    }

    // context
    private var dyContext: Context? = null
    // database
    private var dyDatabase: Any? = null


    fun initContext(context: Context) {
        if (null != dyContext) return
        dyContext = context
        if (null != dyContext) {
            bindIPCService(dyContext!!)
        }
    }

    fun initDatabase(database: Any) {
        dyDatabase = database
    }


    fun getContext(): Context {
        if (null == dyContext)
            throw NullPointerException("douyin context not init")
        return dyContext as Context
    }

    fun getClassLoader(): ClassLoader {
        return getContext().classLoader
    }

    fun getDatabase(): Any {
        if (null == dyDatabase)
            throw NullPointerException("douyin SQLiteOpenHelper not init")
        return dyDatabase as Any
    }


    // IPC
    private var mService: Messenger? = null
    private var isConnection: Boolean = false
    private val mExecutor = Executors.newSingleThreadExecutor()

    private val mMessenger = Messenger(object : Handler() {
        override fun handleMessage(msgFromPlugin: Message) {
//            val jsonMsg = msgFromPlugin.obj
            val bundle = msgFromPlugin.data
            val jsonMsg = bundle.getString("data")
            Log.i(LOG_TAG, "command :  $jsonMsg")

            //
            if (null != jsonMsg) {
                handleEvent(jsonMsg as String)
            } else {
                postMessageToPlugin("{\"error\":\"数据格式错误\"}", -1)
            }
            super.handleMessage(msgFromPlugin)
        }
    })


    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = Messenger(service)
            isConnection = true
            Log.i(LOG_TAG, "ipc service : 【 Connected 】")

            //TODO 初始化 通知插件app 建立连接成功
            postMessageToPlugin("", 0)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            isConnection = false
            Log.i(LOG_TAG, "ipc service  : 【 Disconnected 】")
        }
    }

    /**
     * 绑定服务
     */
    private fun bindIPCService(context: Context) {
        if (isConnection) {
            return
        }
        val intent = Intent()
        intent.action = "douyin-hook"
        intent.setPackage("com.hb.douyinplugin")
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        Log.i(LOG_TAG, "【 bindIPCService 】 invoked!")
    }

    /**
     * 向Plugin-APP发送消息
     */
    fun postMessageToPlugin(jsonStr: String, action: Int) {
        try {
            var extra = Bundle()
            extra.putString("data", jsonStr)
            var msgFromClient = Message()
            msgFromClient.what = 1001
            msgFromClient.arg1 = action
            msgFromClient.data = extra
            msgFromClient.replyTo = mMessenger;
            if (isConnection) {
                //往服务端发送消息
                mService!!.send(msgFromClient);
                Log.i(LOG_TAG, "send message TO Plugin-App :: success")
            }
        } catch (ex: Exception) {
            Log.i(LOG_TAG, "send message TO Plugin-App :: Fail：" + Log.getStackTraceString(ex))
        }

    }


    /**
     *
     */
    private fun handleEvent(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        val action = jsonObject.optString("action", "")
        mExecutor.execute(object : Runnable {
            override fun run() {
                when (action) {
                    "doFeed" -> {
                        val vid = jsonObject.optString("videoId", "")
                        if (TextUtils.isEmpty(vid)) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "参数错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        FeedHook.doFeed(vid)

                    }
                    "doComment" -> {
                        val vid = jsonObject.optString("videoId", "")
                        val commentText = jsonObject.optString("commentText", "")
                        if (TextUtils.isEmpty(vid) || TextUtils.isEmpty(commentText)) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "参数错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        CommentHook.doComment(vid,commentText)
                    }
                    "img" -> {
                        val toUID = jsonObject.optString("toUID", "")
                        val imagePath = jsonObject.optString("imagePath", "")
                        if (TextUtils.isEmpty(imagePath) || TextUtils.isEmpty(toUID)) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "参数错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        MessageHook.doSendImage(toUID, imagePath)

                    }
                    "video" -> {
                        val toUID = jsonObject.optString("toUID", "")
                        val videoFormUID = jsonObject.optString("formUID", "")
                        val videoID = jsonObject.optString("videoID", "")
                        val title = jsonObject.optString("title", "")
                        val coverUri = jsonObject.optString("coverUri", "")
                        val coverUrl = jsonObject.optString("coverUrl", "")
                        if (TextUtils.isEmpty(toUID) || TextUtils.isEmpty(videoFormUID)
                                || TextUtils.isEmpty(title) || TextUtils.isEmpty(videoID)
                                || TextUtils.isEmpty(coverUri) || TextUtils.isEmpty(coverUrl)
                        ) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "数据错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        MessageHook.doSendVideo(toUID, videoFormUID, videoID, title, coverUri, coverUrl)

                    }
                    "good" -> {
                        val toUID = jsonObject.optString("toUID", "")
                        val formUID = jsonObject.optString("formUID", "")
                        val avatarUri = jsonObject.optString("avatarUri", "")
                        val avatarUrl = jsonObject.optString("avatarUrl", "")
                        val title = jsonObject.optString("title", "")
                        val promotionId = jsonObject.optString("promotionId", "")
                        val productId = jsonObject.optString("productId", "")
                        val userCount = jsonObject.optLong("userCount", 0)
                        if (TextUtils.isEmpty(toUID) || TextUtils.isEmpty(formUID)
                                || TextUtils.isEmpty(avatarUri) || TextUtils.isEmpty(avatarUrl)
                                || TextUtils.isEmpty(title) || TextUtils.isEmpty(promotionId)
                                || TextUtils.isEmpty(productId)
                        ) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "数据错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        MessageHook.doSendGood(toUID, formUID,
                                avatarUri, avatarUrl,
                                title, promotionId,
                                productId, userCount
                        )

                    }
                    "card" -> {
                        val toUID = jsonObject.optString("toUID", "")
                        val formUID = jsonObject.optString("formUID", "")
                        val name = jsonObject.optString("name", "")
                        val desc = jsonObject.optString("desc", "")
                        val avatarUri = jsonObject.optString("avatarUri", "")
                        val avatarUrl = jsonObject.optString("avatarUrl", "")
                        if (TextUtils.isEmpty(toUID) || TextUtils.isEmpty(formUID)
                                || TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)
                                || TextUtils.isEmpty(avatarUri) || TextUtils.isEmpty(avatarUrl)
                        ) {
                            val params = hashMapOf<String, String>()
                            params.put("error", "数据错误")
                            postMessageToPlugin(JSON.toJSONString(params), -1)
                            return
                        }
                        MessageHook.doSendUser(toUID, formUID,
                                name, desc,
                                avatarUri, avatarUrl
                        )
                    }
                }
            }
        })
    }

}