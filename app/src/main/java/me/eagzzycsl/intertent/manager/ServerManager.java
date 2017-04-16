package me.eagzzycsl.intertent.manager;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import org.greenrobot.eventbus.EventBus;

import me.eagzzycsl.intertent.event.EventList;
import me.eagzzycsl.intertent.event.InputEvent;
import me.eagzzycsl.intertent.utils.MyLog;
import me.eagzzycsl.intertent.utils.SingleManager;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class ServerManager {
    private static  AsyncHttpServer serverInstance = new AsyncHttpServer();
    private static  boolean listened =false;
    private ServerManager(){
    }
    public static AsyncHttpServer getServer(){
        if(!listened) {
            serverInstance.listen(1995);
            listened = true;
        }
        return serverInstance;
    }
    public static void initWebSocket(){
        ServerManager.getServer().websocket("/ws",
                new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                MyLog.i("websocket","connnected");
                webSocket.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        try {
                            if (ex != null)
                                MyLog.i("WebSocket", "Error");
                        } finally {
                        }
                    }
                });

                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        MyLog.i("serverManager",s);
                        String[] ss=s.split("\\|",2);
                        if(ss[0].equals(EventList.input.name)){
                            InputEvent inputEvent=(InputEvent)SingleManager
                                    .getGson().fromJson(ss[1],EventList.input.cls);
                            MyLog.i("servermanager",EventList.input.name);
                            EventBus.getDefault().post(inputEvent);
                        }else {

                        }
                        MyLog.i("ws receive",s);
                    }
                });

            }

        });
    }
}
