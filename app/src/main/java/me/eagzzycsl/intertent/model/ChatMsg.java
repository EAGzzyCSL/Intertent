package me.eagzzycsl.intertent.model;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by eagzzycsl on 4/19/17.
 */

public class ChatMsg {
    private long _id;
    private GregorianCalendar time;
    private MsgType type;
    private String value;
    private SourceType sourceType;
    public ChatMsg(long _id,GregorianCalendar time,MsgType type,String value,SourceType sourceType){
        this._id =_id;
        this.time=time;
        this.type=type;
        this.value=value;
        this.sourceType=sourceType;
    }
    public int getTypeInt(){
        return this.type.getInt();
    }
}
