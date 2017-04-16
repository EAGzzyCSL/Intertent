package me.eagzzycsl.intertent.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.eagzzycsl.intertent.MainActivity;
import me.eagzzycsl.intertent.R;
import me.eagzzycsl.intertent.event.InputEvent;
import me.eagzzycsl.intertent.event.MyEvent;
import me.eagzzycsl.intertent.manager.ServerManager;
import me.eagzzycsl.intertent.utils.MyLog;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class MainService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void iniNotification(){

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
        ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).notify(0,notification);
        startForeground(1, notification);
        MyLog.i("服务" , "onCreate() executed");
    }
    private void initServer(){
        ServerManager.initWebSocket();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        iniNotification();
        initServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onPostEvent(MyEvent myEvent){

    }
}
