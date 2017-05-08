package me.eagzzycsl.intertent.model;

/**
 * Created by eagzzycsl on 4/19/17.
 */

public enum SourceType {
    pc(SourceType.type_pc), android(SourceType.type_android);
    public static final int type_pc = 0;
    public static final int type_android = 1;
    private final int typeInt;

    SourceType(int typeInt) {
        this.typeInt = typeInt;
    }

    public int getInt() {
        return this.typeInt;
    }
    public static SourceType fromInt(int typeInt){
        switch (typeInt){
            case type_pc:{
                return pc;
            }
            case type_android:{
                return android;
            }
        }
        return null;
    }
}
