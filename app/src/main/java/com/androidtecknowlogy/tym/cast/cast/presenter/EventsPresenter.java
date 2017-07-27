package com.androidtecknowlogy.tym.cast.cast.presenter;

import android.content.Context;

import com.androidtecknowlogy.tym.cast.cast.model.EventsModel;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.helper.io.CustomDataFormat;
import com.androidtecknowlogy.tym.cast.helper.pojo.Events;

import java.util.Date;

/**
 * Created by AGBOMA franklyn on 7/15/17.
 */

public class EventsPresenter implements Constant.AddEventsDetailsToPresenter,
        Constant.ModelEventsToPresenter {
    private EventsModel model;
    private Context context;

    private Constant.PresenterEventsLogicToEventFragment presenterEventsLogicToEventFragment;

    public EventsPresenter(Context context){
        model = new EventsModel();
        model.setModelEventsToPresenter(this, context);
        this.context = context;
    }

    public void setPresenterEventsLogicToEventFragment
            (Constant.PresenterEventsLogicToEventFragment presenterEventsLogicToEventFragment) {
        this.presenterEventsLogicToEventFragment = presenterEventsLogicToEventFragment;
    }

    @Override
    public void sendAddEventsDetailsToPresenter(String name, String eventTitle, String eventText,
                                                String eventDay, String eventTime) {
        String combineDay_time = "On: " +eventDay +".    Time:" +eventTime;
        model.sendEventSDetailsToDataBase(name,
                new CustomDataFormat().getDateFormat(new Date(), "m/d/y"),
                eventTitle, eventText, combineDay_time);


    }

    @Override
    public void initializeChildListener(boolean value) {

        if(value) //start EventListener Fragment is visible
            model.attachOnlineEventsListener();
        else //stop EventListener Fragment is invisible
            model.detachOnlineEventsListener();
    }

    @Override
    public void sendEventsCreatedResponse(String message) {
        presenterEventsLogicToEventFragment.sendToastMessage(message);
    }

    /**
     * events = contains dataSnapshots of events from dataBase.
     * @param events
     */
    @Override
    public void sendEventsFromDataBase(Events events) {
        presenterEventsLogicToEventFragment.setEventsClassToEventFragment(events);
    }

    /**
     * send response to EventFragment of dataBase content is/was deleted.
     */
    @Override
    public void sendDataBaseDeleted() {
        presenterEventsLogicToEventFragment.receiveDataBaseDeletedResponse();
    }
}
