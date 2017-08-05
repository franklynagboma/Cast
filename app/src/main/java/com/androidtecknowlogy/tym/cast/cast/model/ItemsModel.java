package com.androidtecknowlogy.tym.cast.cast.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.helper.pojo.Settings;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

/**
 * Created by AGBOMA franklyn on 6/25/17.
 */

public class ItemsModel {

    private final String LOG_TAG = ItemsModel.class.getSimpleName();

    private Constant.ItemModelToPresenter itemModelToPresenter;
    private Context context;
    private ValueEventListener valueEventListener;
    private ProgressDialog loading;
    private final String MUST_CONTAIN = "@";//change this to @cousant

    public ItemsModel(Context context) {
        //progress dialog
        this.context = context;
        loading = new ProgressDialog(this.context);
        loading.setCancelable(false);
    }

    private void showProgress(String title,String message) {
        loading.setTitle(title);
        loading.setMessage(message);
        loading.show();
    }
    private void stopProgress(){
        Log.i(LOG_TAG, "Progress stop");
        if(loading.isShowing())
            loading.dismiss();
    }

    public void setModelToPresenter(Constant.ItemModelToPresenter itemModelToPresenter) {
        this.itemModelToPresenter = itemModelToPresenter;
    }

    public void attachedDataListener(final String userEmail) {
        //showProgress("Cast","Loading....");

        if(valueEventListener == null) {
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            Object obj = dataSnap.getValue();

                            //get the cast items
                            if(obj instanceof Map) {
                                Log.e(LOG_TAG, "obj " + obj);

                                //check which map class was seen.
                                String getKeySnap = dataSnap.getKey();
                                //for settings map class.
                                Log.i(LOG_TAG, "settings key: " + getKeySnap);
                                //change the below to .endWith validation.
                                if (getKeySnap.contains(MUST_CONTAIN)) {
                                    //store settings in list
                                    Settings settings = dataSnap.getValue(Settings.class);
                                    AppController.settingMap.put(getKeySnap, settings);
                                }
                                else {
                                    //for castItems map class.
                                    //store castItems in list
                                    CastItems castItems = dataSnap.getValue(CastItems.class);
                                    AppController.detailsCastItems.add(castItems);
                                    /*//check the current user so as not to display
                                    //check if user is guess
                                    if(AppController.isGuest)
                                        AppController.detailsCastItems.add(castItems);
                                    else {
                                        // the user content on the view.
                                        Log.i(LOG_TAG, "userName: " + userName
                                                + " castName: " + castItems.getCastName());
                                        if(!userName.equalsIgnoreCase(castItems.getCastName()))
                                            AppController.detailsCastItems.add(castItems);
                                    }*/
                                }
                            }
                            //get the admin items
                            else if(obj instanceof String) {
                                Log.i(LOG_TAG, "obj String");
                                String admins = dataSnap.getValue(String.class);
                                Log.i(LOG_TAG, admins + " hit server and added");
                            }
                        }
                        //dismiss dialog.
                        //stopProgress();
                        Log.i(LOG_TAG, "list size: " + AppController.detailsCastItems.size());
                        setRecylerView(userEmail);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //dismiss dialog.
                    //stopProgress();
                }
            };
            AppController.castsData.addValueEventListener(valueEventListener);
            AppController.adminsData.addValueEventListener(valueEventListener);
            AppController.settingsData.addValueEventListener(valueEventListener);
        }
    }

    public void detachDataListener() {
        if(valueEventListener != null) {
            AppController.castsData.removeEventListener(valueEventListener);
            AppController.adminsData.removeEventListener(valueEventListener);
            AppController.settingsData.removeEventListener(valueEventListener);
        }
        valueEventListener = null; //set back to null
        /**
         * recyclerView  = false
         * The Application is on pause
         */
        itemModelToPresenter.recyclerView(false);
    }
    private void setRecylerView(String userEmail) {
        /**
         * Observed that fire base returns a null value for each sync,
         * reasons, cannot tell now but to remove the null for the list,
         * loop through and confirm absence of null value both on guest and user.
         */
        for(int count =0; count <AppController.detailsCastItems.size(); count ++) {
            CastItems cast = AppController.detailsCastItems.get(count);
            //check the current user so as not to display
            //check if user is guess
            Log.i(LOG_TAG, "userEmail: " +userEmail +" email: " + cast.getCastEmail());
            if(AppController.isGuest){
                if(cast.getCastEmail() == null)
                    AppController.detailsCastItems.remove(count);
            }
            else {
                if(userEmail.equalsIgnoreCase(cast.getCastEmail()) || cast.getCastEmail() == null)
                    AppController.detailsCastItems.remove(count);
            }
        }
        /*if(!AppController.isGuest) {
            //check is current user is on the list, remove not to show on item
            //but only is not on guest mode.
            for(int count =0; count <AppController.detailsCastItems.size(); count ++) {
                CastItems cast = AppController.detailsCastItems.get(count);
                Log.i(LOG_TAG, "userEmail: " +userEmail +" email: " + cast.getCastEmail());
                if(userEmail.equalsIgnoreCase(cast.getCastEmail()) || cast.getCastEmail() == null)
                    AppController.detailsCastItems.remove(count);
            }
        }*/

        //reload cast adapter
        Log.e(LOG_TAG, "Another ArrayListSize " + AppController.detailsCastItems.size());
        itemModelToPresenter.recyclerView(true);
    }
    public void getPosition(List<CastItems> castItemsList, int itemPosition, int position) {
        itemModelToPresenter.positionDetails(itemPosition,
                castItemsList.get(position));
    }
}
