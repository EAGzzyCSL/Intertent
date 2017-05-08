package me.eagzzycsl.intertent.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by eagzzycsl on 5/7/17.
 */

public class MySQLHelper extends SQLiteOpenHelper{


    public MySQLHelper(Context context){
        super(context,TableField.db_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TableField.ChatHisTable.TAbLE_NAME
                +"("
                +TableField.ChatHisTable._id +" Integer primary key autoincrement,"
                +TableField.ChatHisTable.time +" Integer,"
                +TableField.ChatHisTable.sourceType+" Integer,"
                +TableField.ChatHisTable.type +" Integer,"
                +TableField.ChatHisTable.value + " string"
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
