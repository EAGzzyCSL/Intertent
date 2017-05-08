package me.eagzzycsl.intertent.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.TabLayout;

import java.util.Date;
import java.util.GregorianCalendar;

import me.eagzzycsl.intertent.utils.TableField;

/**
 * Created by eagzzycsl on 4/19/17.
 */

public class ChatMsg {
    private long _id;
    // 或许直接存时间戳
    private long time;
    private MsgType type;
    private String value;
    private SourceType sourceType;
    public ChatMsg(long time,MsgType type,String value,SourceType sourceType){
        this.time=time;
        this.type=type;
        this.value=value;
        this.sourceType=sourceType;
    }
    private ChatMsg(long _id,long time,MsgType type,String value,SourceType sourceType){
        this(time,type,value,sourceType);
        this._id =_id;

    }
    public int getTypeInt(){
        return this.type.getInt();
    }
    public ContentValues toContentValues(){
        ContentValues cv=new ContentValues();
        // 添加的时候是不能有id的，而删除和修改的时候是需要id的
//        cv.put(TableField.ChatHisTable._id,this._id);
        cv.put(TableField.ChatHisTable.time,this.time);
        cv.put(TableField.ChatHisTable.type,this.type.getInt());
        cv.put(TableField.ChatHisTable.value,this.value);
        cv.put(TableField.ChatHisTable.sourceType,this.sourceType.getInt());
        return cv;
    }
    public static ChatMsg fromCursor(Cursor cursor){
        return new ChatMsg(
                cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable._id)),
                cursor.getLong(cursor.getColumnIndex(TableField.ChatHisTable.time)),
                MsgType.fromInt(cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable.type))),
                cursor.getString(cursor.getColumnIndex(TableField.ChatHisTable.value)),
                SourceType.fromInt(cursor.getInt(cursor.getColumnIndex(TableField.ChatHisTable.sourceType)))
        );
    }
    private static GregorianCalendar millis2Calendar(long millis){
        GregorianCalendar calendar=new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
}
