package me.eagzzycsl.intertent.event;

import me.eagzzycsl.intertent.model.ChatMsg;
import me.eagzzycsl.intertent.model.MsgType;
import me.eagzzycsl.intertent.model.SourceType;

/**
 * Created by eagzzycsl on 5/9/17.
 */

public class MsgEvent {
    private long time;
    private MsgType type;
    private String value;
    private SourceType sourceType;
    public ChatMsg toChatMsg(){
        return new ChatMsg(
                time,
                type,
                value,
                sourceType
        );
    }
}
