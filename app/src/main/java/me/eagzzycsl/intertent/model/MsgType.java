package me.eagzzycsl.intertent.model;

import java.util.Arrays;

/**
 * Created by eagzzycsl on 4/19/17.
 */

public enum MsgType {
    text(MsgType.type_text), img(MsgType.type_img), file(MsgType.type_file);
    public static final int type_text = 0;
    public static final int type_img = 1;
    public static final int type_file = 2;
    private int typeInt;

    MsgType(int typeInt) {
        this.typeInt = typeInt;
    }

    public int getInt() {
        return this.typeInt;
    }

}
