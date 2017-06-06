package me.eagzzycsl.intertent.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.eagzzycsl.intertent.MainActivity;
import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.event.CallEvent;
import me.eagzzycsl.intertent.event.ClipboardEvent;
import me.eagzzycsl.intertent.event.MsgEvent;
import me.eagzzycsl.intertent.manager.ServerManager;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.utils.MyLog;
import me.eagzzycsl.intertent.utils.SQLMan;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class MainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void iniNotification() {

        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentInfo("补充内容");
        builder.setContentText("主内容区");
        builder.setContentTitle("通知标题");
        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_laptop_black);
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
        startForeground(1, notification);
//        MyLog.i("服务", "onCreate() executed");
    }

    private void initServer() {
        ServerManager.getInstance().initWebSocket(new ServerManager.OnConnectedCallBack(){
            @Override
            public void callBack() {
//                Log.i("in connected callback","callback");
                ArrayList<ChatMsg> msgList = SQLMan.getInstance(getApplicationContext()).getAllChatHis();
                ServerManager.getInstance().sendAllChatHis(msgList);
            }
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
//        iniNotification();
        initServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(CallEvent callEvent) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callEvent.getPhone_number()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
    private void dealClipboardEvent(ClipboardEvent clipboardEvent){
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        switch (clipboardEvent.getAction()){
            case ClipboardEvent.action_get:{
                if(clipboardManager.getPrimaryClipDescription().hasMimeType(
                        ClipDescription.MIMETYPE_TEXT_PLAIN
                )){
                    ClipData clipData=clipboardManager.getPrimaryClip();
                    ClipData.Item item=clipData.getItemAt(0);
                    ServerManager.getInstance().sendClipboardEvent(
                            new ClipboardEvent(ClipboardEvent.action_get,
                                    item.getText().toString())
                    );
                }
                break;
            }
            case ClipboardEvent.action_set:{
                ClipData text = ClipData.newPlainText("label",clipboardEvent.getValue());
                clipboardManager.setPrimaryClip(text);
                break;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ClipboardEvent clipboardEvent){
        dealClipboardEvent(clipboardEvent);
    }


}
