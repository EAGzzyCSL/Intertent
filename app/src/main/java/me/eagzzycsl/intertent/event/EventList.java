package me.eagzzycsl.intertent.event;


import android.app.usage.UsageEvents;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public enum EventList {
    input(EventList.event_input, InputEvent.class),
    call(EventList.event_call,CallEvent.class),
    clipboard(EventList.event_clipboard,ClipboardEvent.class),
    mouse(EventList.event_mouse,MouseEvent.class),
    msg(EventList.event_msg,MsgEvent.class),
    allMsgHis(EventList.event_all_msg_his,null),
    getFileList(EventList.event_get_file_list,GetFileListEvent.class),
    sendFileList(EventList.event_send_file_list,null);

    public static final String event_input="input";
    public static final String event_call="call";
    public static final String event_clipboard="clipboard";
    public static final String event_mouse="mouse";
    public static final String event_msg="msg";
    public static final String event_all_msg_his="all_msg_his";
    public static final String event_get_file_list="get_file_list";
    public static final String event_send_file_list="send_file_list";

    public final String name;
    public final Class cls;
    EventList(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }
}
