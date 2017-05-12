package me.eagzzycsl.intertent.event;

import me.eagzzycsl.intertent.model.ChatMsg;

/**
 * Created by eagzzycsl on 5/9/17.
 */

public class MsgEvent {
    private long time;
    private int msgType;
    private String value;
    private int sourceType;
    public MsgEvent(long time,int msgType,String value,int sourceType){
        this.time=time;
        this.msgType=msgType;
        this.value=value;
        this.sourceType=sourceType;
    }
    public ChatMsg toChatMsg(){
        return new ChatMsg(
                time,
                msgType,
                value,
                sourceType
        );
    }
}
