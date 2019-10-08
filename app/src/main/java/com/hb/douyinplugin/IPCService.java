package com.hb.douyinplugin;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.douyinplugin.eventbus.DefaultEventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 与抖音通讯的server
 */
public class IPCService extends Service {

    private final String TAG = "douyin-hook";

    private final int MSG_WHAT_CHANNEL_DY = 1001;//抖音

    private Map<Integer, Messenger> messengerMap = new HashMap<>();

    @Override
    public IBinder onBind(Intent intent) {
        startToForeground();
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "组件已启动", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ipc service create success!");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "IPC-已断开");
        DefaultEventMessage eventMessage = new DefaultEventMessage(JSON.toJSONString(map));
        EventBus.getDefault().postSticky(eventMessage);
        Log.i(TAG, "ipc service onDestroy!");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msgFromClient) {
            handleFromDouyinMessage(msgFromClient);
            super.handleMessage(msgFromClient);
        }
    });

    /**
     * 处理来自抖音的消息
     */
    private void handleFromDouyinMessage(Message msgFromDouyin) {
        //如果是抖音的消息
        if (MSG_WHAT_CHANNEL_DY == msgFromDouyin.what) {
            Bundle extra = msgFromDouyin.getData();
            String jsonMsg = extra.getString("data");
            int action = msgFromDouyin.arg1;
            switch (action) {
                case 0://初始化建立连接成功
                    Messenger messenger = msgFromDouyin.replyTo;
                    if (messengerMap.size() > 0) {
                        messengerMap.clear();
                    }
                    messengerMap.put(MSG_WHAT_CHANNEL_DY, messenger);
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 处理用户的动作
     *
     * @param defMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DefaultEventMessage defMessage) {
        try {
            Message toDouyinMessage = Message.obtain();
            //
            String jsonMsg = defMessage.getJsonMsg();
//            org.json.JSONObject jsonMessage = new org.json.JSONObject(jsonMsg);
//            String action = jsonMessage.optString("action");
//            switch (action){
//
//            }
            Bundle bundle = new Bundle();
            bundle.putString("data", jsonMsg);
            toDouyinMessage.setData(bundle);
//            toDouyinMessage.obj = jsonMsg;
            messengerMap.get(MSG_WHAT_CHANNEL_DY).send(toDouyinMessage);
            Log.i(TAG, "send message Success!");

//        } catch (JSONException ex) {
//            Log.i(TAG, "send message Fail :: 【 JSONException 】");
        } catch (RemoteException ex) {
            Log.i(TAG, "send message Fail :: ipc server offline! 【 RemoteException 】");
        }
    }

    /**
     * 开启为前台服务
     */
    private void startToForeground() {
        Log.i(TAG, "do startToForeground with ipc service!");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Douyin-IPC");
        builder.setContentText("Connection with Douyin was successful ！");
        startForeground(12138, builder.build());

    }
}