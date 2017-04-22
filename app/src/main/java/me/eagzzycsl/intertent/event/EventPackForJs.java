package me.eagzzycsl.intertent.event;

import me.eagzzycsl.intertent.utils.SingleManager;

/**
 * Created by eagzzycsl on 4/22/17.
 */

public class EventPackForJs {
    private static  EventPackForJs instance=new EventPackForJs();
    private String type;
    private MyEvent event;
    private EventPackForJs(){

    }
    public static EventPackForJs getInstance(){
        return EventPackForJs.instance;
    }
    public EventPackForJs setType(EventList eventList){
        this.type=eventList.name;
        return this;
    }
    public EventPackForJs setEvent(MyEvent myEvent){
        this.event=myEvent;
        return this;
    }
    public String pack(){
        return SingleManager.getGson().toJson(this);
    }
}
