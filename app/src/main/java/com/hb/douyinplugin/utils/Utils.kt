package com.hb.douyinplugin.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import java.util.*
import java.util.regex.Pattern

/**
 * createTime: 2019/9/29.13:54
 * updateTime: 2019/9/29.13:54
 * author: singleMan.
 * desc:
 */
class Utils {

    companion object {
        /**
         * 判断服务是否开启
         *
         * @return
         */
        fun isLiving(context: Context, ServiceName: String): Boolean {
            try {
                if (TextUtils.isEmpty(ServiceName)) {
                    return false
                }
                val myManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningService = myManager.getRunningServices(1000) as ArrayList<ActivityManager.RunningServiceInfo>
                for (i in runningService.indices) {
                    if (runningService[i].service.className.toString().contains(ServiceName)) {
                        return true
                    }
                }
            } catch (e: Exception) {
            }

            return false
        }

        fun stripUrl(shareUrl: String): String? {
            val pattern = Patterns.WEB_URL
            val matcher = pattern.matcher(shareUrl)
            return if (matcher.find()) matcher.group(0) else null
        }

        /**
         * 提取tk
         */
        internal fun parseDytk(html: String): String {
            if (html.contains("dytk")) {
                val pattern = Pattern.compile("dytk: '(.+?)'", Pattern.CASE_INSENSITIVE)
                val matcher = pattern.matcher(html)
                return if (matcher.find()) {
                    matcher.group(1)
                } else {
                    throw RuntimeException("解析【dytk参数】失败")
                }
            }
            throw RuntimeException("解析【dytk参数】失败")
        }
    }
}