package me.eagzzycsl.intertent.event;

import android.telecom.Call;

/**
 * Created by eagzzycsl on 4/21/17.
 */

public class CallEvent {
    private String phone_number;
    public CallEvent(String phone_number){
        this.phone_number=phone_number;
    }
    public String getPhone_number(){
        return this.phone_number;
    }
}
