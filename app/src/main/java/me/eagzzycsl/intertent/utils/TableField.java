package me.eagzzycsl.intertent.utils;

import java.util.GregorianCalendar;

import me.eagzzycsl.intertent.model.MsgType;
import me.eagzzycsl.intertent.model.SourceType;

/**
 * Created by eagzzycsl on 5/7/17.
 */

public class TableField {
    public static final String db_name="db_internent";
    public  interface ChatHisTable {
        String TAbLE_NAME ="ChatHisTable";
        String _id = "_id";
        String time = "time";
        String type = "type";
        String value = "value";
        String sourceType = "sourceType";
    }
}
