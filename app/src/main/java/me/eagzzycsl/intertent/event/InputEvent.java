package me.eagzzycsl.intertent.event;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class InputEvent extends MyEvent {
    public static final String type_input="input";
    public static final String type_cursorLeft="cursorLeft";
    public static final String type_cursorRight="cursorRight";
    public static final String type_backspace="backspace";
    public static final String type_enter="enter";
    public String type;
    public String value;
}
