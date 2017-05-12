package me.eagzzycsl.intertent.model;

import android.content.ContentValues;
import android.database.Cursor;

import me.eagzzycsl.intertent.event.MsgEvent;
import me.eagzzycsl.intertent.utils.TableField;

/**
 * Created by eagzzycsl on 4/19/17.
 */

public class ChatMsg {
    public interface MsgType {
        int type_text = 0;
        int type_img = 1;
        int type_file = 2;
    }
    public interface SourceType{
        int type_pc = 0;
        int type_android = 1;
    }
    private long id;
    // 或许直接存时间戳
    private long time;
    private int msgType;
    private String value;
    private int sourceType;
    public ChatMsg(long time, int msgType, String value, int sourceType){
        this.time=time;
        this.msgType = msgType;
        this.value=value;
        this.sourceType=sourceType;
    }
    private ChatMsg(long id, long time, int msgType, String value, int sourceType){
        this(time, msgType,value,sourceType);
        this.id =id;

    }
    public void setId(long id){
        this.id=id;
    }
    public int getSourceType(){
        return this.sourceType;
    }
    public int getMsgType(){
        return this.msgType;
    }
    public String getValue(){
        return this.value;
    }
    public ContentValues toContentValues(){
        ContentValues cv=new ContentValues();
        // 添加的时候是不能有id的，而删除和修改的时候是需要id的
//        cv.put(TableField.ChatHisTable._id,this._id);
        cv.put(TableField.ChatHisTable.time,this.time);
        cv.put(TableField.ChatHisTable.type,this.msgType);
        cv.put(TableField.ChatHisTable.value,this.value);
        cv.put(TableField.ChatHisTable.sourceType,this.sourceType);
        return cv;
    }
    public static ChatMsg fromCursor(Cursor cursor){
        return new ChatMsg(
                cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable._id)),
                cursor.getLong(cursor.getColumnIndex(TableField.ChatHisTable.time)),
                cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable.type)),
                cursor.getString(cursor.getColumnIndex(TableField.ChatHisTable.value)),
                cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable.sourceType))
        );
    }
    public MsgEvent toMsgEvent(){
        return new MsgEvent(
                time,
                msgType,
                value,
                sourceType
        );
    }
}
