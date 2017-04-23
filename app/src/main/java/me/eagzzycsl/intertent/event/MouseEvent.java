package me.eagzzycsl.intertent.event;

import android.view.MotionEvent;

/**
 * Created by eagzzycsl on 4/23/17.
 */

public class MouseEvent {
    public static final String action_click="click";
    public static final String action_move="move";
    private String action;
    private  float x;
    private float y;
    public MouseEvent(int x,int y){
        this.x=x;
        this.y=y;
    }
    public String getAction(){
        return this.action;
    }
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
}
