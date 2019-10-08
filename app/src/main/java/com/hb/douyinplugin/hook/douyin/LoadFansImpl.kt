package com.hb.douyinplugin.hook.douyin

import android.database.Cursor
import android.util.Log
import de.robv.android.xposed.XposedHelpers
import org.json.JSONArray
import org.json.JSONObject

/**
 * createTime: 2019/9/27.14:08
 * updateTime: 2019/9/27.14:08
 * author: singleMan.
 * desc: 加载抖音联系人
 */
class LoadFansImpl {

    companion object {
        fun queryContacts() {

            val database = DouyinRuntime.instance.getDatabase()

            //
            val sql_queryAll = "select * from SIMPLE_USER"

            val mCursor = XposedHelpers.callMethod(database, "rawQuery", sql_queryAll, arrayOf<String>())

            if (mCursor == null) {
                Log.i(DouyinRuntime.LOG_TAG, "--null--")
                return
            }
            val cursor = mCursor as Cursor

            val contacts = JSONArray()

            while (cursor.moveToNext()) {
                val uid = cursor.getString(cursor.getColumnIndex("UID"))
                val nickName = cursor.getString(cursor.getColumnIndex("NICK_NAME"))
//                val signature = cursor.getString(cursor.getColumnIndex("SIGNATURE"))
//                val avatarThumb = cursor.getString(cursor.getColumnIndex("AVATAR_THUMB"))
//                val followStatus = cursor.getString(cursor.getColumnIndex("FOLLOW_STATUS"))
                val uniqueId = cursor.getString(cursor.getColumnIndex("UNIQUE_ID"))
                val remarkName = cursor.getString(cursor.getColumnIndex("REMARK_NAME"))
//                val sortWeight = cursor.getString(cursor.getColumnIndex("SORT_WEIGHT"))
                val shortId = cursor.getString(cursor.getColumnIndex("SHORT_ID"))

                val contactInfo = JSONObject()
                contactInfo.put("uid", uid)
                contactInfo.put("nickName", nickName)
//                contactInfo.put("signature", signature)
//                contactInfo.put("avatarThumb", avatarThumb)
//                contactInfo.put("followStatus", followStatus)
                contactInfo.put("uniqueId", uniqueId)
                contactInfo.put("remarkName", remarkName)
//                contactInfo.put("sortWeight", sortWeight)
                contactInfo.put("shortId", shortId)

                contacts.put(contactInfo)

            }

            Log.i(DouyinRuntime.LOG_TAG, "联系人：" + contacts.toString())
        }
    }

}