package com.hb.douyinplugin.hook.douyin;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * createTime: 2019/9/27.19:00
 * updateTime: 2019/9/27.19:00
 * author: singleMan.
 * desc: 点赞
 */

public class FeedHook {

    public static void hookFeed() {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> feedApi = XposedHelpers.findClass("com.ss.android.ugc.aweme.feed.h.aa", classLoader);
        XposedBridge.hookAllMethods(feedApi, "sendRequest", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object[] params = param.args;

                Log.i(DouyinRuntime.Companion.getLOG_TAG(), "feed-request:" + JSON.toJSONString(params));
                super.afterHookedMethod(param);
            }
        });
    }

    /**
     * 执行点赞
     *
     * @param videoId
     */
    public static void doFeed(String videoId) {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> feedApi = XposedHelpers.findClass("com.ss.android.ugc.aweme.feed.api.FeedApi", classLoader);
        Object retrofitApi = XposedHelpers.getStaticObjectField(feedApi, "c");
        Class<?> getChannelIdClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.app.constants.a", classLoader);
        /* homepage_fresh  single_song homepage_follow single_song_fresh personal_homepage other_fans.
           discovery fans poi_page others_homepage general_search challenge search_result challenge_fresh
           other_following homepage_hot hot_search_video_board
       */
        int channelId = (int) XposedHelpers.callStaticMethod(getChannelIdClass, "a", "homepage_hot");
        /*
        param1: 视频ID
        param2: 1 点赞 0 取消
        param3: 页面channel
         */
        Object api = XposedHelpers.callMethod(retrofitApi, "diggItem", videoId, 1, channelId);
        //执行点赞
        Object response = XposedHelpers.callMethod(api, "get");
        //获取点赞服务器返回结果
        String resultString = (String) XposedHelpers.callMethod(response, "toString");
        Log.i(DouyinRuntime.Companion.getLOG_TAG(), "主动点赞结果:" + resultString);
        //解析判断是否成功
        if (null != resultString && resultString.contains("status_code=0")) {
            Log.i(DouyinRuntime.Companion.getLOG_TAG(), "点赞成功！");
        } else {
            Log.i(DouyinRuntime.Companion.getLOG_TAG(), "点赞失败！");
        }


    }
}
