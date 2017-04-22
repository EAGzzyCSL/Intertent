package me.eagzzycsl.intertent.event;

/**
 * Created by eagzzycsl on 4/22/17.
 */

public class ClipboardEvent  extends MyEvent{
    public static final String action_set="set";
    public static final String action_get="get";
    private String action;
    private String value;
    public ClipboardEvent(String action, String value){
        this.action=action;
        this.value=value;
    }
    public String getAction(){
        return this.action;
    }
    public String getValue(){
        return this.value;
    }

}
