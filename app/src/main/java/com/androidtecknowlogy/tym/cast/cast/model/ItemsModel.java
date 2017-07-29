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

    public void attachedDataListener(final String userName) {
        //showProgress("Cast","Loading....");

        if(valueEventListener == null) {
            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {

                        for(DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            Object obj = dataSnap.getValue();

                            Log.i(LOG_TAG, "obj 1 " + obj);

                            //get the cast items
                            if(obj instanceof Map) {
                                Log.e(LOG_TAG, "obj " + obj);
                                //store castItems in list
                                CastItems castItems = dataSnap.getValue(CastItems.class);
                                //store settings in list
                                Settings settings = dataSnap.getValue(Settings.class);

                                //check which map class was seen.
                                String getKeySnap = dataSnap.getKey();
                                //for settings map class.
                                if (getKeySnap.contains("@gmail")) {
                                    AppController.settingMap.put(getKeySnap, settings);
                                }
                                else {
                                    //for castItems map class.
                                    //check the current user so as not to display
                                    // the user content on the view.
                                    if(!userName.equalsIgnoreCase(castItems.getCastName()))
                                        AppController.detailsCastItems.add(castItems);
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
                        setRecylerView();
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
    private void setRecylerView() {
        //reload cast adapter
        Log.e(LOG_TAG, "Another ArrayListSize " + AppController.detailsCastItems.size());
        itemModelToPresenter.recyclerView(true);
    }
    public void getPosition(List<CastItems> castItemsList, int itemPosition, int position) {
        itemModelToPresenter.positionDetails(itemPosition,
                castItemsList.get(position));
    }
}
