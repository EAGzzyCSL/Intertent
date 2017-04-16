package me.eagzzycsl.intertent.event;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public enum EventList {
    input("input", InputEvent.class);
    public final String name;
    public final Class cls;
    EventList(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }
}
