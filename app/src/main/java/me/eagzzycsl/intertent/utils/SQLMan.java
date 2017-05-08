package me.eagzzycsl.intertent.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import me.eagzzycsl.intertent.model.ChatMsg;

/**
 * Created by eagzzycsl on 5/7/17.
 */

public class SQLMan {
    public static SQLMan instance;
    private SQLiteDatabase myDb;

    public static SQLMan getInstance(Context context) {
        if (instance == null) {
            instance = new SQLMan(context);
        }
        return instance;
    }

    private SQLMan(Context context) {
        this.myDb = new MySQLHelper(context).getWritableDatabase();
    }

    public ArrayList<ChatMsg> getAllChatHis() {
        Cursor c = myDb.rawQuery("select * from "+TableField.ChatHisTable.TAbLE_NAME, new String[]{});
        ArrayList<ChatMsg> arr = new ArrayList<>(10);
        while (c.moveToNext()) {
            arr.add(ChatMsg.fromCursor(c));
        }
        c.close();
        return arr;
    }

    public long addChatHis(ChatMsg chatMsg) {
        return myDb.insert(TableField.ChatHisTable.TAbLE_NAME, null, chatMsg.toContentValues());
    }

    public void deleteChatHis() {

    }
}
