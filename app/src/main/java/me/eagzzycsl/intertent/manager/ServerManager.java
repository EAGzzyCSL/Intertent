package me.eagzzycsl.intertent.manager;

import android.app.usage.UsageEvents;
import android.content.ClipData;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;

import org.greenrobot.eventbus.EventBus;

import me.eagzzycsl.intertent.event.CallEvent;
import me.eagzzycsl.intertent.event.ClipboardEvent;
import me.eagzzycsl.intertent.event.EventList;
import me.eagzzycsl.intertent.event.EventPackForJs;
import me.eagzzycsl.intertent.event.InputEvent;
import me.eagzzycsl.intertent.event.MyEvent;
import me.eagzzycsl.intertent.utils.MyLog;
import me.eagzzycsl.intertent.utils.SingleManager;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class ServerManager {
    private static ServerManager serverManager = new ServerManager();

    private static boolean listened = false;
    private WebSocket webSocket;
    private AsyncHttpServer serverInstance = new AsyncHttpServer();

    private ServerManager() {
    }

    public static ServerManager getInstance() {
        return serverManager;
    }

    public AsyncHttpServer getServer() {
        if (!listened) {
            serverInstance.listen(1995);
            listened = true;
        }
        return serverInstance;
    }

    public void initWebSocket() {
        this.getServer().websocket("/ws",
                new AsyncHttpServer.WebSocketRequestCallback() {
                    @Override
                    public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {
                        MyLog.i("websocket", "connnected");
                        ServerManager.this.webSocket = webSocket;
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
                                MyLog.i("serverManager", s);
                                String[] ss = s.split("\\|", 2);
                                String json = ss[1];
                                switch (ss[0]) {
                                    case EventList.event_input: {
                                        InputEvent inputEvent = (InputEvent) SingleManager
                                                .getGson().fromJson(json, EventList.input.cls);
                                        MyLog.i("servermanager", EventList.input.name);
                                        EventBus.getDefault().post(inputEvent);
                                        break;
                                    }
                                    case EventList.event_call: {
                                        CallEvent callEvent = (CallEvent) SingleManager
                                                .getGson().fromJson(json, EventList.call.cls);
                                        EventBus.getDefault().post(callEvent);
                                        break;
                                    }
                                    case EventList.event_clipboard: {
                                        ClipboardEvent clipBoardEvent = (ClipboardEvent) SingleManager
                                                .getGson().fromJson(json, EventList.clipboard.cls);
                                        EventBus.getDefault().post(clipBoardEvent);
                                        break;
                                    }
                                }

                                MyLog.i("ws receive", s);
                            }
                        });

                    }

                });
    }
    public void sendClipboardEvent(ClipboardEvent clipboardEvent) {
        this.webSocket.send(
                EventPackForJs.getInstance()
                        .setType(EventList.clipboard)
                        .setEvent(clipboardEvent)
                        .pack()
        );
    }
}
