package me.eagzzycsl.intertent.manager;


import android.util.EventLog;
import android.util.Log;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.koushikdutta.async.stream.FileDataSink;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import me.eagzzycsl.intertent.event.CallEvent;
import me.eagzzycsl.intertent.event.ClipboardEvent;
import me.eagzzycsl.intertent.event.EventList;
import me.eagzzycsl.intertent.event.EventPackForJs;
import me.eagzzycsl.intertent.event.GetFileListEvent;
import me.eagzzycsl.intertent.event.InputEvent;
import me.eagzzycsl.intertent.event.MouseEvent;
import me.eagzzycsl.intertent.event.MsgEvent;
import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.utils.MyLog;
import me.eagzzycsl.intertent.utils.SQLMan;
import me.eagzzycsl.intertent.utils.SingleManager;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class ServerManager {
    private static ServerManager serverManager = new ServerManager();

    private static boolean listened = false;
    private WebSocket webSocket;
    private AsyncHttpServer serverInstance = new AsyncHttpServer();
    private WebSocket.StringCallback webSocketStringCallback = new WebSocket.StringCallback() {
        @Override
        public void onStringAvailable(String s) {
            MyLog.i("serverManager", s);
            String[] ss = s.split("\\|", 2);
            String json = ss[1];
            switch (ss[0]) {
                case EventList.event_input: {
                    InputEvent inputEvent = (InputEvent) SingleManager
                            .getGson().fromJson(json, EventList.input.cls);
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
                case EventList.event_mouse: {
                    MouseEvent mouseEvent = (MouseEvent) SingleManager
                            .getGson().fromJson(json, EventList.mouse.cls);
                    EventBus.getDefault().post(mouseEvent);
                    break;
                }
                case EventList.event_msg: {
                    MsgEvent msgEvent = (MsgEvent) SingleManager.getGson()
                            .fromJson(json, EventList.msg.cls);
                    EventBus.getDefault().post(msgEvent);
                    Log.i("MSG", json);
                    break;
                }
                case EventList.event_get_file_list:{
                    Log.i("wsfilepath",json);
                    GetFileListEvent getFileListEvent =(GetFileListEvent)SingleManager.getGson()
                            .fromJson(json, EventList.getFileList.cls);
                    File f=new File(getFileListEvent.path);
                    Log.i("filefile",f.getPath());
                    Log.i("filefile2",f.isDirectory()?"dir":"file");
                    if(f.isDirectory()) {
                        ArrayList<FileDsc> fileDscList = new ArrayList<>(f.listFiles().length);
                        for (File file : f.listFiles()) {
                            fileDscList.add(new FileDsc(file));
                        }
                        sendFileList(fileDscList);
                    }
                    break;
                }
            }
        }
    };

    private ServerManager() {

    }

    public static ServerManager getInstance() {
        return serverManager;
    }
    public void sendFileList(ArrayList<FileDsc> fileDscList){
        this.webSocket.send(
                EventPackForJs.getInstance()
                        .setType(EventList.sendFileList)
                        .setEvent(fileDscList)
                        .pack()
        );
    }
    public AsyncHttpServer getServer() {
        if (!listened) {
            serverInstance.listen(1995);
            listened = true;
            serverInstance.get("/fl", new HttpServerRequestCallback() {
                @Override
                public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                    String path=request.getQuery().getString("file");
                    Log.i("request",path);
                    File f=new File(path);
                    Log.i("fixed file",f.getPath());
//                response.send("hello world");
                    response.sendFile(f);
                }
            });
//            serverInstance.get("/ph", new HttpServerRequestCallback() {
//                @Override
//                public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
//                    String path=request.getQuery().getString("path");
//                    Log.i("reqpath",path);
//                    File f=new File(path);
//                    ArrayList<FileDsc> fileDscList =new ArrayList<FileDsc>(f.listFiles().length);
//                    for (File file : f.listFiles()) {
//                        fileDscList.add(new FileDsc(file));
//                    }
//                    if(f.isDirectory()){
//                        String dirJson = SingleManager.getGson().toJson(fileDscList);
//                        response.send(dirJson);
//                    }
//                    response.send("[]");
//                }
//            });
        }
        return serverInstance;
    }
    private static class FileDsc{
        String name;
        boolean dir;
        public FileDsc(File f){
            this.name=f.getName();
            this.dir=f.isDirectory();
        }
    }
    public void initWebSocket(final OnConnectedCallBack onConnectedCallBack) {
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
                        webSocket.setStringCallback(webSocketStringCallback);
                        onConnectedCallBack.callBack();
                    }

                });
    }
    public void disConnectWebSocket() {
        if (this.webSocket != null) {
            webSocket.close();
        }
    }

    public void sendClipboardEvent(ClipboardEvent clipboardEvent) {
        this.webSocket.send(
                EventPackForJs.getInstance()
                        .setType(EventList.clipboard)
                        .setEvent(clipboardEvent)
                        .pack()
        );
    }

    public void sendMsgEvent(MsgEvent msgEvent) {
        if(this.webSocket==null){
            return;
        }
        this.webSocket.send(
                EventPackForJs.getInstance()
                        .setType(EventList.msg)
                        .setEvent(msgEvent)
                        .pack()
        );
    }

    public void sendAllChatHis(ArrayList<ChatMsg> chatMsgList) {

        this.webSocket.send(
                EventPackForJs.getInstance()
                        .setType(EventList.allMsgHis)
                        .setEvent(chatMsgList)
                        .pack()
        );
    }

    public abstract static class OnConnectedCallBack {
        public abstract void callBack();
    }
}
