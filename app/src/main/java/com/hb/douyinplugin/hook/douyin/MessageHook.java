package com.hb.douyinplugin.hook.douyin;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * createTime: 2019/9/28.14:03
 * updateTime: 2019/9/28.14:03
 * author: singleMan.
 * desc:
 */

public class MessageHook {


    public static void hookSendPhoto() {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> aClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.net.s", classLoader);
        XposedHelpers.findAndHookMethod(aClass, "a", String.class, List.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String toUser = (String) param.args[0];

                Log.e(DouyinRuntime.Companion.getLOG_TAG(), "TO::" + toUser);


                List imgs = (List) param.args[1];

                for (Object image : imgs) {
                    Log.e(DouyinRuntime.Companion.getLOG_TAG(), "image ::" + XposedHelpers.callMethod(image, "toString"));
                }

                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod(aClass, "b", String.class, List.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String toUser = (String) param.args[0];

                Log.i(DouyinRuntime.Companion.getLOG_TAG(), "TO::" + toUser);


                List imgs = (List) param.args[1];

                for (Object image : imgs) {
                    Log.i(DouyinRuntime.Companion.getLOG_TAG(), "image ::" + XposedHelpers.callMethod(image, "toString"));
                }

                super.afterHookedMethod(param);
            }
        });


        Class<?> sendClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.utils.ba", classLoader);
        XposedHelpers.findAndHookMethod(sendClass, "b", List.class, List.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List toUsers = (List) param.args[0];

                Log.i(DouyinRuntime.Companion.getLOG_TAG(), "TO::" + toUsers);


                List msgs = (List) param.args[1];

                for (Object msg : msgs) {
                    Log.i(DouyinRuntime.Companion.getLOG_TAG(), "msg ::" + JSON.toJSONString(msg));
                }

                super.afterHookedMethod(param);
            }
        });
    }

    /**
     * 发送图片
     *
     * @param toUID
     * @param imagePath
     * @throws RuntimeException
     */
    public static void doSendImage(String toUID, String imagePath) throws RuntimeException {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        //第一步 初始化图片发送类
        Class<?> imageHandleClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.net.s", classLoader);
        Object imageHandle = XposedHelpers.callStaticMethod(imageHandleClass, "a");
        //模拟图片发送数据
        List imageList = new ArrayList();
        Class<?> imageEntityClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.input.photo.j", classLoader);
        Object imageEntity = XposedHelpers.newInstance(imageEntityClass);
        int[] imageSize = getImageSize(imagePath);
        //设置图片详细数据
        XposedHelpers.callMethod(imageEntity, "setPath", imagePath);
        XposedHelpers.callMethod(imageEntity, "setWith", imageSize[0]);
        XposedHelpers.callMethod(imageEntity, "setHeight", imageSize[1]);
        imageList.add(imageEntity);
        //获取自己的UID
        Class<?> c_class = XposedHelpers.findClass("com.bytedance.im.core.a.c", classLoader);
        Object a_obj = XposedHelpers.callStaticMethod(c_class, "a");
        Object currentUser = XposedHelpers.getObjectField(a_obj, "c");
        long currentUID = (long) XposedHelpers.callMethod(currentUser, "a");

        //拼接会话ID
        String conversationID = String.format("0:1:%s:%s", String.valueOf(currentUID), toUID);
        Log.i(DouyinRuntime.Companion.getLOG_TAG(), "conversationID::" + conversationID);

        //执行发送图片动作
        XposedHelpers.callMethod(imageHandle, "b", conversationID, imageList);


    }

    /**
     * 获取图片的宽高
     *
     * @param imagePath
     * @return
     * @throws RuntimeException
     */
    private static int[] getImageSize(String imagePath) throws RuntimeException {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new RuntimeException("image don't exists!");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        return new int[]{imageWidth, imageHeight};
    }


    /**
     * @param toUID        目标UID
     * @param videoFormUID 视频来源UID  109864890252
     * @param videoID      视频ID   6693124310501969166
     * @param title        分享视频
     * @param coverUri     封面URI 17b4b00084db599b06eaa
     * @param coverUrl     封面url_list https://p3-dy.byteimg.com/aweme/300x400/17b4b00084db599b06eaa.jpeg
     */
    public static void doSendVideo(String toUID, String videoFormUID, String videoID, String title, String coverUri, String coverUrl) {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> videoEntityClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.model.ShareAwemeContent", classLoader);
        Object videoEntity = XposedHelpers.newInstance(videoEntityClass);
        XposedHelpers.callMethod(videoEntity, "setUser", videoFormUID);
        XposedHelpers.callMethod(videoEntity, "setItemId", videoID);
        XposedHelpers.callMethod(videoEntity, "setContentName", title);
        XposedHelpers.callMethod(videoEntity, "setAwemeType", 0);
        XposedHelpers.callMethod(videoEntity, "setType", 800);


        Class<?> coverUrlClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.base.model.UrlModel", classLoader);
        Object coverUrlEntity = XposedHelpers.newInstance(coverUrlClass);
        XposedHelpers.callMethod(coverUrlEntity, "setUri", coverUri);
        List<String> list = new ArrayList<>();
        list.add(coverUrl);
        XposedHelpers.callMethod(coverUrlEntity, "setUrlList", list);

        XposedHelpers.callMethod(videoEntity, "setCoverUrl", coverUrlEntity);


        Class<?> videoHandleClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.utils.ba", classLoader);
        Object videoHandle = XposedHelpers.callStaticMethod(videoHandleClass, "a");
        XposedHelpers.callMethod(videoHandle, "a", toUID, videoEntity);
    }


    /**
     * 发送商品信息
     *
     * @param toUID
     * @param title
     * @param formUID
     * @param avatarUri
     * @param avatarUrl
     */
    public static void doSendGood(String toUID, String formUID, String avatarUri, String avatarUrl, String title, String promotionId, String productId,long userCount) {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> goodEntityClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.model.ShareGoodContent", classLoader);
        Object goodEntity = XposedHelpers.newInstance(goodEntityClass);
        XposedHelpers.callMethod(goodEntity, "setTitle", title);
        XposedHelpers.callMethod(goodEntity, "setUserId", formUID);
//        XposedHelpers.callMethod(goodEntity, "setSecUserId", secUserId);
        XposedHelpers.callMethod(goodEntity, "setPromotionId", promotionId);
        XposedHelpers.callMethod(goodEntity, "setProductId", productId);
        XposedHelpers.callMethod(goodEntity, "setUserCount", userCount);
        XposedHelpers.callMethod(goodEntity, "setType", 0);


        Class<?> coverUrlClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.base.model.UrlModel", classLoader);
        Object avatarUrlEntity = XposedHelpers.newInstance(coverUrlClass);
        XposedHelpers.callMethod(avatarUrlEntity, "setUri", avatarUri);
        List<String> list = new ArrayList<>();
        list.add(avatarUrl);
        XposedHelpers.callMethod(avatarUrlEntity, "setUrlList", list);

        XposedHelpers.callMethod(goodEntity, "setAvatar", avatarUrlEntity);

//        //获取自己的UID
//        Class<?> c_class = XposedHelpers.findClass("com.bytedance.im.core.a.c", classLoader);
//        Object a_obj = XposedHelpers.callStaticMethod(c_class, "a");
//        Object currentUser = XposedHelpers.getObjectField(a_obj, "c");
//        long currentUID = (long) XposedHelpers.callMethod(currentUser, "a");
//        //拼接会话ID
//        String conversationID = String.format("0:1:%s:%s", String.valueOf(currentUID), toUID);
//        Log.i(DouyinRuntime.Companion.getLOG_TAG(), "conversationID::" + conversationID);

        Class<?> videoHandleClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.utils.ba", classLoader);
        Object videoHandle = XposedHelpers.callStaticMethod(videoHandleClass, "a");
        XposedHelpers.callMethod(videoHandle, "a", toUID, goodEntity);
    }


    /**
     * 发送用户
     * @param toUID
     * @param uid
     * @param name
     * @param desc
     * @param avatarUri
     * @param avatarUrl
     */
    public static void doSendUser(String toUID, String uid, String name, String desc, String avatarUri, String avatarUrl) {
        ClassLoader classLoader = DouyinRuntime.Companion.getInstance().getClassLoader();
        Class<?> userEntityClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.chat.model.ShareUserContent", classLoader);
        Object userEntity = XposedHelpers.newInstance(userEntityClass);
        XposedHelpers.callMethod(userEntity, "setUid", uid);
        XposedHelpers.callMethod(userEntity, "setName", name);
        XposedHelpers.callMethod(userEntity, "setDesc", desc);
        XposedHelpers.callMethod(userEntity, "setPushDetail", name);
        XposedHelpers.callMethod(userEntity, "setType", 0);


        Class<?> coverUrlClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.base.model.UrlModel", classLoader);
        Object avatarUrlEntity = XposedHelpers.newInstance(coverUrlClass);
        XposedHelpers.callMethod(avatarUrlEntity, "setUri", avatarUri);
        List<String> list = new ArrayList<>();
        list.add(avatarUrl);
        XposedHelpers.callMethod(avatarUrlEntity, "setUrlList", list);

        XposedHelpers.callMethod(userEntity, "setAvatar", avatarUrlEntity);

        Class<?> videoHandleClass = XposedHelpers.findClass("com.ss.android.ugc.aweme.im.sdk.utils.ba", classLoader);
        Object videoHandle = XposedHelpers.callStaticMethod(videoHandleClass, "a");
        XposedHelpers.callMethod(videoHandle, "a", toUID, userEntity);
    }
}
