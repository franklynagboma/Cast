package com.androidtecknowlogy.tym.cast.cast.activity_view;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.cast.presenter.EventsPresenter;
import com.androidtecknowlogy.tym.cast.cast.presenter.ItemsPresenter;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.DetailsFragment;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.EventFragment;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.ItemsFragment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class CastActivity extends AppCompatActivity implements ItemsFragment.DynamicFragment{

    private final String LOG_TAG = CastActivity.class.getSimpleName();

    public final static String NONE = "none";
    public static final String IMAGE_URL = "image_url";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String TITLE = "title";
    public static final String GENDER = "gender";
    public static final String SUMMARY = "summary";

    public static int orientation;
    public final static String CAST_ITEM = "cast_item";
    private static final String EVENT_FRAG = "event_frag";
    private final String DETAILS_FRAG = "detail_fragment";
    private String FRAG = "";
    private String pauseFrag;
    private String getInstanceState;
    private ItemsPresenter itemsPresenter;
    private EventsPresenter eventsPresenter;
    private ItemsFragment itemsFragment;
    private DetailsFragment detailsFragment;
    private EventFragment eventFragment;
    private  boolean isTab;
    private boolean fabProcess;
    private boolean fabMenuClick;
    private FabLayoutProcess fabLayoutProcess;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.fab_layout_view)
    LinearLayout fabLayout;
    @Nullable
    @BindView(R.id.fab_menu)
    FloatingActionsMenu fabMenu;
    @Nullable
    @BindView(R.id.fab_events)
    FloatingActionButton fabEvents;
    @Nullable
    @BindView(R.id.fab_settings)
    FloatingActionButton fabSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logCall("onCreated");
        Log.e(LOG_TAG, ""+savedInstanceState);

        setContentView(R.layout.activity_cast);
        ButterKnife.bind(this);

        //Fragment is dynamic,
        itemsFragment = new ItemsFragment();
        detailsFragment = new DetailsFragment();
        eventFragment = new EventFragment();

        //invalidateOptionsMenu(); this is mostly used to redraw menu item.
        //always add a delay after for menu to be redraw on ActionBar.


        isTab = getResources().getBoolean(R.bool.isTab);
        if(!isTab)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Just Tab
        /*else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_details_fragment, detailsFragment, DETAILS_FRAG)
                    .commit();*/

        //For both Mobile and Tab.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.cast_item_screen, itemsFragment, CAST_ITEM)
                .commit();

        //ItemsFragment
        itemsPresenter = new ItemsPresenter(this);
        itemsFragment.setItemPositionToPresenter(itemsPresenter);
        itemsPresenter.setPresenterCallsItemRecyclerView(itemsFragment);

        //DetailsFragment
        itemsPresenter.setPresenterToDetailsFragment(detailsFragment);

        //EventsFragment
        eventsPresenter = new EventsPresenter(this);
        eventFragment.setAddEventsDetailsToPresenter(eventsPresenter);
        eventsPresenter.setPresenterEventsLogicToEventFragment(eventFragment);


        //fabMenuClick = true; //default fab layout to allow fab expansion.


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(LOG_TAG, "onConfigurationChanged()");
    }

    private void init() {

        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_nav);
        if(bottomNavigationView != null) {

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.action_cast:
                                    Log.e(LOG_TAG, "nav cast clicked");
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.cast_item_screen, itemsFragment,
                                                    CAST_ITEM)
                                            .commit();
                                    break;
                                case R.id.action_event:
                                    Log.e(LOG_TAG, "nav event clicked");
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.cast_item_screen, eventFragment, EVENT_FRAG)
                                            .commit();
                                    break;
                                case R.id.action_settings:
                                    Log.e(LOG_TAG, "nav settings clicked");
                                    break;
                            }

                            return true;
                        }
                    });
        }

        /*fabEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialogUploader();
            }
        });*/
    }

    private void logCall(String logMessage) {
        Log.i(LOG_TAG, logMessage);
    }
    /**
     * For Tab Fragment navigation
     */
    //events clicked
    @Optional
    @OnClick(R.id.fab_events)
    public void fabEventsClicked() {
        //collapse fab menu
        fabMenu.collapse();
        //delay to exit fab layout.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabLayoutVisibility("events clicked");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //delay to load next fragment
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.cast_details_fragment, eventFragment, EVENT_FRAG)
                                .commit();
                    }
                },50);
            }
        }, 100);

    }

    //settings clicked
    @Optional
    @OnClick(R.id.fab_settings)
    public void fabSettingsClicked() {
        //collapse fab menu
        fabMenu.collapse();
        //delay to exit fab layout.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabLayoutVisibility("settings clicked");
                //delay to load next fragment
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(LOG_TAG, "Settings Page accessed");
                    }
                },50);
            }
        }, 100);

    }
    private void fabLayoutVisibility(String message) {
        logCall(message);
        fabLayout.setVisibility(fabLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    /*private void dialogUploader() {
        final AlertDialog.Builder uploader = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null, false);
        final EditText adminText = (EditText) dialogView.findViewById(R.id.admin_text);
        //add text watcher to this text to check error.
        uploader.setTitle("Add Admin")
                .setView(adminText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String adminEmail = adminText.getText().toString().trim();
                        if(!TextUtils.isEmpty(adminEmail)) {
                            if(!adminEmail.contains("@") || !adminEmail.contains(".")
                                    || adminEmail.contains(" "))
                                adminText.setError("Invalid email");
                            else {
                                dialogInterface.dismiss();
                                //call loading dialog
                                *//*loading.setMessage(adminEmail + " adding...");
                                loading.show();*//*
                                //add admin email to database
                                AppController.adminsData.push().setValue(adminEmail);
                            }
                        }
                        else
                            adminText.setError("Invalid email");
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog builder = uploader.create();
        builder.setCancelable(false);
        builder.show();
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*getInstanceState = savedInstanceState.getString(FRAG);
        Log.i(LOG_TAG, "onRestoreInstanceState " +getInstanceState);
        //present view to the user from savedInstanceState
        if(null != getInstanceState) {
            if(getInstanceState.equals(CAST_ITEM))
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, itemsFragment, CAST_ITEM)
                        .commit();
            else if(getInstanceState.equals(DETAILS_FRAG))
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, detailsFragment, DETAILS_FRAG)
                        .commit();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        Log.e(LOG_TAG, "onResume");
        //call back paused Fragment.
        if(!isTab) {
            if(FRAG.equals(EVENT_FRAG))
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, eventFragment, EVENT_FRAG)
                        .commit();
        }

        if(isTab) {
            //check if fabProcess is false then make true for AsyncTask
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!fabProcess) {
                        fabProcess = true;
                        fabLayoutProcess = new FabLayoutProcess();
                        fabLayoutProcess.execute();
                    }
                }
            }, 500);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //called from onStop
        Log.e(LOG_TAG, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "onPause");
        if(!isTab) {
            if(getSupportFragmentManager().findFragmentByTag(EVENT_FRAG)
                    != null && getSupportFragmentManager().findFragmentByTag(EVENT_FRAG)
                    .isVisible())
                FRAG = EVENT_FRAG;
        }

        if(null != fabLayoutProcess)
            fabLayoutProcess.cancel(true);
        fabProcess = false;
    }
    //invoke when the activity may be temporarily destroyed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "onSaveInstanceState");
        /*if(getSupportFragmentManager().findFragmentByTag(CAST_ITEM) != null
                && getSupportFragmentManager().findFragmentByTag(CAST_ITEM).isVisible())
            outState.putString(FRAG, CAST_ITEM);
        else if(getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG) != null
                && getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG).isVisible())
            outState.putString(FRAG, DETAILS_FRAG);*/

        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "onDestroy");
        //check if task is running
        if(isTab) {
            if(fabProcess) {
                fabLayoutProcess.cancel(true);
                fabProcess = false;
            }
        }
    }

    /**
     * change the fragment to DetailsFragment
     * to see details for mobile devices
     */
    @Override
    public void changeToDetailsFragment() {
        if(!isTab)
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.cast_item_screen, detailsFragment, DETAILS_FRAG)
                .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_details_fragment, detailsFragment, DETAILS_FRAG)
                    .commit();
        //fabEditVisibility();
    }

    /*private void fabEditVisibility(){
        if(getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG) != null
                && getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG).isVisible()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //delay before show
                    fabEditButton.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
        else
            fabEditButton.setVisibility(View.GONE);
    }*/

    @Override
    public void onBackPressed() {

        if(!isTab) {
            if(getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG) != null
                && getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG).isVisible()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, itemsFragment, CAST_ITEM)
                        .commit();
                //fabEditVisibility();
            }
            else
                this.finish();
        }
        else
            this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cast, menu);
        //inflate menu share
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.edit_details:
                Log.i(LOG_TAG, "edit menu");
                return true;
            case R.id.sign_out:
                Log.i(LOG_TAG, "sign out menu");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class FabLayoutProcess extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //Run AsyncTask
            while (fabProcess) {
                Thread process = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2);
                        }catch (Exception i) {
                            i.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if(null == fabMenu)
                                    Log.i(LOG_TAG, " AsyncTask fabMenu is null");
                                else {
                                    if(fabMenu.isExpanded()) {

                                        //Fab menu was clicked
                                        fabLayout.setVisibility(View.VISIBLE);
                                    }
                                    else {

                                        fabLayout.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        });
                    }
                });process.start();
            }

            return null;
        }
    }
}
