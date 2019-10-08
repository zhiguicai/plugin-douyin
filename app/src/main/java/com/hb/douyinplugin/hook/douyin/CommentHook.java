package com.hb.douyinplugin.hook.douyin;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * createTime: 2019/9/27.18:46
 * updateTime: 2019/9/27.18:46
 * author: singleMan.
 * desc: 评论
 */

public class CommentHook {

    public static void hookComment() {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> commentApi = XposedHelpers.findClass("com.ss.android.ugc.aweme.comment.api.CommentApi", classLoader);
        XposedBridge.hookAllMethods(commentApi, "a", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object[] params = param.args;
                Object response = param.getResult();

                Log.i(DouyinRuntime.Companion.getLOG_TAG(), "comment-request:" + JSON.toJSONString(params));
                Log.i(DouyinRuntime.Companion.getLOG_TAG(), "comment-response:" + XposedHelpers.callMethod(response, "toString"));
                super.afterHookedMethod(param);
            }
        });
    }

    /**
     * 执行评论
     */
    public static void doComment(String targetId, String commentString) {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> commentApi = XposedHelpers.findClass("com.ss.android.ugc.aweme.comment.api.CommentApi", classLoader);
        Object response = XposedHelpers.callStaticMethod(commentApi, "a", targetId, commentString, null, new ArrayList<>(), null, 0);
        //获取评论服务器返回结果
        String resultString = (String) XposedHelpers.callMethod(response, "toString");
        Log.i(DouyinRuntime.Companion.getLOG_TAG(), "主动评论结果:" + resultString);
        //解析判断是否成功
        if (null != resultString && resultString.contains("status_code=0")) {
            Log.i(DouyinRuntime.Companion.getLOG_TAG(), "评论成功！");
        } else {
            Log.i(DouyinRuntime.Companion.getLOG_TAG(), "评论失败！");
        }
    }
}
