package me.eagzzycsl.intertent.event;


/**
 * Created by eagzzycsl on 4/16/17.
 */

public enum EventList {
    input(EventList.event_input, InputEvent.class),
    call(EventList.event_call,CallEvent.class),
    clipboard(EventList.event_clipboard,ClipboardEvent.class);
    public static final String event_input="input";
    public static final String event_call="call";
    public static final String event_clipboard="clipboard";

    public final String name;
    public final Class cls;
    EventList(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }
}
