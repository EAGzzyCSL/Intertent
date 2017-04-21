package me.eagzzycsl.intertent.event;

import android.app.usage.UsageEvents;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public enum EventList {
    input(EventList.event_input, InputEvent.class),
    call(EventList.event_call,CallEvent.class);
    public static final String event_input="input";
    public static final String event_call="call";

    public final String name;
    public final Class cls;
    EventList(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }
}
