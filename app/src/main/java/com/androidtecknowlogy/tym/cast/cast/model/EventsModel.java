package com.androidtecknowlogy.tym.cast.cast.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.helper.pojo.Events;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AGBOMA franklyn on 7/15/17.
 */

public class EventsModel {

    private final String LOG_TAG = EventsModel.class.getSimpleName();

    Constant.ModelEventsToPresenter modelEventsToPresenter;
    private Context context;
    private ProgressDialog loading;
    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;
    private boolean isCreating;

    public void setModelEventsToPresenter(Constant.ModelEventsToPresenter modelEventsToPresenter,
                                          Context context) {
        this.modelEventsToPresenter = modelEventsToPresenter;
        this.context = context;
    }

    public void sendEventSDetailsToDataBase(String name, String timeCreated, String eventTitle,
                                            String eventText, String eventDay_eventTime){

        Log.i(LOG_TAG, "Events\n" + name +"\n"+ timeCreated  +"\n"+ eventTitle
                +"\n"+ eventText +"\n"+ eventDay_eventTime);

        startProgress();
        isCreating = true;
        //push event online.
        //send new event created to dataBase.
        AppController.eventList.push().setValue(new Events(name, timeCreated, eventTitle,
                eventText, eventDay_eventTime));

    }

    private void startProgress() {
        if(null == loading) {
            loading = new ProgressDialog(context);
            loading.setCancelable(false);
            loading.setMessage("Creating...");
        }
        if(!loading.isShowing())
            loading.show();
    }
    private void stopProgress() {
        if(null != loading && loading.isShowing())
            loading.dismiss();
    }


    public void attachOnlineEventsListener(){
        if(null == childEventListener) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if(null != dataSnapshot){

                        if(isCreating) //send response to user about event
                            modelEventsToPresenter
                                    .sendEventsCreatedResponse("Event created successfully");
                        isCreating = false; //set back to false.
                        //send dataSnapshot to presenter
                        modelEventsToPresenter
                                .sendEventsFromDataBase(dataSnapshot.getValue(Events.class));
                    }
                    else {
                        Log.i(LOG_TAG, "Event not Created, null on dataSnapshot");
                        modelEventsToPresenter.sendEventsCreatedResponse("No data yet");
                    }

                    //stop loading for new events created.
                    stopProgress();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //if value is removed
                    modelEventsToPresenter.sendDataBaseDeleted();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    stopProgress();
                    modelEventsToPresenter.sendEventsCreatedResponse("creating event fails," +
                            " check connection");
                }
            };
            //listener to eventList
            AppController.eventList.addChildEventListener(childEventListener);
        }
    }
    public void detachOnlineEventsListener(){
        if(null != childEventListener)
            AppController.eventList.removeEventListener(childEventListener);
        childEventListener = null; //set back to null.
    }
}
