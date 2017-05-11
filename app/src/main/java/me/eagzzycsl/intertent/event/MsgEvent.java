package me.eagzzycsl.intertent.event;

import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.model.MsgType;
import me.eagzzycsl.intertent.model.SourceType;

/**
 * Created by eagzzycsl on 5/9/17.
 */

public class MsgEvent extends MyEvent {
    private long time;
    private int type;
    private String value;
    private int sourceType;
    public MsgEvent(long time,int type,String value,int sourceType){
        this.time=time;
        this.type=type;
        this.value=value;
        this.sourceType=sourceType;
    }
    public ChatMsg toChatMsg(){
        return new ChatMsg(
                time,
                MsgType.fromInt(type),
                value,
                SourceType.fromInt(sourceType)
        );
    }
}
