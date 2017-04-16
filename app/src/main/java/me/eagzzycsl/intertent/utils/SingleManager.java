package me.eagzzycsl.intertent.utils;

import com.google.gson.Gson;

/**
 * Created by eagzzycsl on 4/16/17.
 */

public class SingleManager {
    private static Gson gsonInstance =new Gson();
    public static Gson getGson(){
        return gsonInstance;
    }
}
