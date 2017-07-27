package com.androidtecknowlogy.tym.cast.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.androidtecknowlogy.tym.cast.helper.pojo.Events;

import java.util.List;

/**
 * Created by AGBOMA franklyn on 6/25/17.
 */

public interface Constant {

    interface OnCastItemClicked {
        void onCastClicked(View view, int position);
    }

    /**
     * CastItemFragment-Presenter-CastDetailsFragment
     */
    //get item position from recycler-ItemsFragment-Presenter
    interface ItemsSendItemPositionToPresenter {
        void positionItemFragment(List<CastItems> castItemsList, String userName,
                                  int itemPosition, int position, boolean value);
    }
    //call ItemFragment recycler view.
    interface PresenterCallsItemRecyclerView {
        void resetRecyclerView(boolean value);
    }

    /**
     * CastDetailsModel-Presenter-CastItemFragmentModel
     */
    interface ItemModelToPresenter {
        void positionDetails(int itemPosition, CastItems items);
        void recyclerView(boolean value);
    }

    //next

    /**
     * completeSignUpFragment send the user inputs to presenter for business logic
     */
    interface CompleteSignUpToPresenter {
        void userSignUp(String uId, String photo, String email, String gender, EditText mobile,
                        EditText sunName, EditText otherNames, EditText title, EditText password,
                        EditText confirmPassword, EditText dob, EditText summary, Context context);
    }

    /**
     * Presenter sends to CompleteSignUpFragment to start activity
     * if message is true or show TextInput error with message.
     */
    interface PresenterSendsSignUpCompletedToCompleteSignUpFragment {
        //this will serve as to startActivity will message true or error with other wise.
        void startActivity(String message);
    }
    /**
     * Presenter sends the true and complete user information to model to save online.
     */
    interface PresenterSendSignUpToModel {
        void signUpInfo(String uId, String photo, String email, String gender,  String month_year,
                        String name, String mobile, String title, String password,
                        String dob, String summary, Context context);
    }

    /**
     * Send a response to presenter that sign up is completed
     */
    interface ModelSendToPresentSignUpSaved {
        void saving(boolean value);
    }

    /**
     * get details from presenter send castDetailsFragment
     */
    interface PresenterSendToCastDetailsFragment{
        void details(int itemPosition, String photo, String email, String gender,  String month_year,
                     String name, String mobile, String title, String dob, String summary);
    }


    //Event Fragment

    /**
     * get event credentials from dialog on EventFragment
     */
    interface AddEventsDetailsToPresenter {
        //start or stop EventListener
        void initializeChildListener(boolean value);
        void sendAddEventsDetailsToPresenter(String name, String eventTitle,
                                             String eventText, String eventDay, String eventTime);
    }

    /**
     * send responses from event model to presenter
     */
    interface ModelEventsToPresenter {
        //send toast message about creating events.
        void sendEventsCreatedResponse(String message);
        //send event class from online dataBase to Presenter
        void sendEventsFromDataBase (Events events);
        //if dataBase data is/are deleted, perform
        void sendDataBaseDeleted();
    }

    /**
     *
     */
    interface PresenterEventsLogicToEventFragment {
        //send toast message response
        void sendToastMessage(String message);
        //send event class from Presenter to EventFragment
        void setEventsClassToEventFragment(Events events);
        //if dataBase data is/are deleted, perform
        void receiveDataBaseDeletedResponse();
    }
}
