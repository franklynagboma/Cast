package com.androidtecknowlogy.tym.cast.cast.activity_view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.complete_signup.fragment.CompleteSignUpFragment;
import com.androidtecknowlogy.tym.cast.helper.view.CustomBottomNavigationView;
import com.androidtecknowlogy.tym.cast.login.LoginActivity;
import com.androidtecknowlogy.tym.cast.cast.presenter.EventsPresenter;
import com.androidtecknowlogy.tym.cast.cast.presenter.ItemsPresenter;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.DetailsFragment;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.EventFragment;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.ItemsFragment;
import com.androidtecknowlogy.tym.cast.cast.settings.HostFragment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class CastActivity extends AppCompatActivity implements ItemsFragment.DynamicFragment,
        CompleteSignUpFragment.KnowActivity{

    private final String LOG_TAG = CastActivity.class.getSimpleName();

    public final static String NONE = "none";
    public static final String IMAGE_URL = "image_url";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String TITLE = "title";
    public static final String GENDER = "gender";
    public static final String SUMMARY = "summary";

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    public static int orientation;
    private CustomBottomNavigationView bottomNavigationView;
    //private BottomNavigationView bottomNavigationView;
    public final static String CAST_ITEM = "cast_item";
    private final String DETAILS_FRAG = "detail_fragment";
    private static final String SETTINGS_FRAG = "settings_frag";
    private static final String EVENT_FRAG = "event_frag";
    private boolean menuCheck;
    public final static String COMPLETE = "complete";
    private String FRAG = "";
    private String pauseFrag;
    private String getInstanceState;
    private ItemsPresenter itemsPresenter;
    private EventsPresenter eventsPresenter;
    private ItemsFragment itemsFragment;
    private DetailsFragment detailsFragment;
    private HostFragment settingsFragment;
    private EventFragment eventFragment;
    private  boolean isTab;
    private boolean quit;
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
    @BindView(R.id.fab_profile)
    FloatingActionButton fabProfile;
    @Nullable
    @BindView(R.id.fab_cast)
    FloatingActionButton fabCast;
    @Nullable
    @BindView(R.id.fab_settings)
    FloatingActionButton fabSettings;
    @Nullable
    @BindView(R.id.cast_details_fragment)
    FrameLayout detailsFrame;
    //FrameLayout detailsFrame;

    private CastUserToDetailFragment castUserToDetailFragment;
    public interface CastUserToDetailFragment {
        void CastSendUsersToDetailFragment(String image, String name, String title,
                                           String dob, String gender, String mobile,
                                           String email, String summary);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logCall("onCreated");
        Log.e(LOG_TAG, ""+savedInstanceState);

        setContentView(R.layout.activity_cast);
        ButterKnife.bind(this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        //Fragment is dynamic,
        itemsFragment = new ItemsFragment();
        detailsFragment = new DetailsFragment();
        settingsFragment = new HostFragment();
        //eventFragment = new EventFragment();

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
        //set up cast interface to detailsFragment
        setCastUserToDetailFragment(detailsFragment);

        //EventsFragment
        /*eventsPresenter = new EventsPresenter(this);
        eventFragment.setAddEventsDetailsToPresenter(eventsPresenter);
        eventsPresenter.setPresenterEventsLogicToEventFragment(eventFragment);*/


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(LOG_TAG, "onConfigurationChanged()");
    }

    private void init() {

        setSupportActionBar(toolbar);

        bottomNavigationView = (CustomBottomNavigationView)
                findViewById(R.id.bottom_nav);
        if(!isTab) {

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.action_cast:
                                    //call ItemFragment.
                                    castCallsItemFragment(R.id.cast_item_screen);
                                    break;
                                case R.id.action_profile:
                                    //call CompleteSignUpFragment.
                                    profileCallsDetailsFragment();
                                    break;
                                /*case R.id.action_event:
                                    Log.e(LOG_TAG, "nav event clicked");
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.cast_item_screen, eventFragment, EVENT_FRAG)
                                            .commit();
                                    break;*/
                                case R.id.action_settings:
                                    Log.e(LOG_TAG, "nav settings clicked");
                                    settingsCallsSettingsFragment(R.id.cast_item_screen);
                                    break;
                            }

                            return true;
                        }
                    });
        }
    }

    private void castCallsItemFragment(int res) {
        //check if it from tab then, hide the detail fragment
        menuCheck = false;
        quit = false;

        if(isTab)
            detailsFrame.setVisibility(View.VISIBLE);
        if(getSupportFragmentManager().findFragmentByTag(CAST_ITEM) == null )
            getSupportFragmentManager().beginTransaction()
                    .replace(res, itemsFragment,
                            CAST_ITEM).commit();
    }

    public void setCastUserToDetailFragment(CastUserToDetailFragment castUserToDetailFragment) {
        this.castUserToDetailFragment = castUserToDetailFragment;
    }

    private void profileCallsDetailsFragment() {
        //call detailsFragment for mobile devices.
        menuCheck = true;
        quit = false;

        if(!isTab)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_item_screen, detailsFragment)
                    .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_details_fragment, detailsFragment)
                    .commit();

        //send details to details fragment
        castUserToDetailFragment.CastSendUsersToDetailFragment(pref.getString("image", ""),
                pref.getString("name", ""), pref.getString("title", ""),
                pref.getString("dob", ""),pref.getString("gender", ""),
                pref.getString("mobile", ""), pref.getString("email", ""),
                pref.getString("summary","") );


    }
    private void editCallsCompleteSignUpFragment(int res){

        menuCheck = false;
        quit = false;

        //send bundle to fragment to hide passwords text field.
        //firstly, clear arguments
        CompleteSignUpFragment completeSignUpFragment = new CompleteSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putString("password", pref.getString("password", ""));
        bundle.putString("uid", pref.getString("uid", ""));
        bundle.putString("name", pref.getString("name", ""));
        bundle.putString("image", pref.getString("image", ""));
        bundle.putString("email", pref.getString("email", ""));
        bundle.putString("mobile", pref.getString("mobile", ""));
        bundle.putString("dob", pref.getString("dob", ""));
        bundle.putString("gender", pref.getString("gender", ""));
        bundle.putString("title", pref.getString("title", ""));
        bundle.putString("summary", pref.getString("summary",""));
        completeSignUpFragment.setArguments(bundle);
        //check if it from tab then, hide the detail fragment
        if(isTab)
            detailsFrame.setVisibility(View.GONE);
        if(getSupportFragmentManager().findFragmentByTag(COMPLETE) == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(res, completeSignUpFragment,
                            COMPLETE).commit();
    }

    private void settingsCallsSettingsFragment(int res) {
        //check if it from tab then, hide the detail fragment
        menuCheck = false;
        quit = false;

        if(isTab)
            detailsFrame.setVisibility(View.GONE);
        if(getSupportFragmentManager().findFragmentByTag(SETTINGS_FRAG) == null )
            getSupportFragmentManager().beginTransaction()
                    .replace(res, settingsFragment,
                            SETTINGS_FRAG).commit();
    }
    @Override
    public void activity() {
        //activity bottom nav cast item for mobile while call cast item with detail visible
        Log.i(LOG_TAG, "activity called");
        //check if mobile then, activity cast item page.
        Log.i(LOG_TAG, "bottomNav null = " + (bottomNavigationView == null));
        /**
         * if mobile activity cast bottom nav
         * else make detail visible and replace fragment container with itemsFragment
         */
        if(!isTab)
            bottomNavigationView.findViewById(R.id.action_cast).performClick();
        else {
            detailsFrame.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_item_screen, itemsFragment,
                            CAST_ITEM).commit();
        }
    }

    private void logCall(String logMessage) {
        Log.i(LOG_TAG, logMessage);
    }
    /**
     * For Tab Fragment navigation
     */
    //cast clicked
    @Optional
    @OnClick(R.id.fab_cast)
    public void fabCastClicked() {
        //collapse fab menu
        fabMenu.collapse();
        //delay to exit fab layout.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabLayoutVisibility();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //delay to load.
                        castCallsItemFragment(R.id.cast_item_screen);
                    }
                },50);
            }
        }, 100);

    }
    //events clicked
    @Optional
    @OnClick(R.id.fab_profile)
    public void fabEditClicked() {
        //collapse fab menu
        fabMenu.collapse();
        //delay to exit fab layout.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabLayoutVisibility();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //delay to load.
                        profileCallsDetailsFragment();
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
                fabLayoutVisibility();
                //delay to load next fragment
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //delay to load.
                        settingsCallsSettingsFragment(R.id.cast_item_screen);
                    }
                },50);
            }
        }, 100);

    }
    private void fabLayoutVisibility() {
        fabLayout.setVisibility(fabLayout.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        Log.e(LOG_TAG, "onResume");
        //call back paused Fragment.
        /*if(!isTab) {
            if(FRAG.equals(EVENT_FRAG))
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, eventFragment, EVENT_FRAG)
                        .commit();
        }*/

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
        /*if(!isTab) {
            if(getSupportFragmentManager().findFragmentByTag(EVENT_FRAG)
                    != null && getSupportFragmentManager().findFragmentByTag(EVENT_FRAG)
                    .isVisible())
                FRAG = EVENT_FRAG;
        }*/

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
        menuCheck = false;

        if(!isTab)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_item_screen, detailsFragment, DETAILS_FRAG)
                    .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cast_details_fragment, detailsFragment, DETAILS_FRAG)
                    .commit();
    }

    @Override
    public void onBackPressed() {


        if(!isTab) {
            if(getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG) != null
                && getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG).isVisible()) {
                quit = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.cast_item_screen, itemsFragment, CAST_ITEM)
                        .commit();
            }
            else {
                performQuit();
            }
        }

        else {
            performQuit();
        }
    }

    private void performQuit() {
        if(!quit) {
            quit = true;
            //delay for 5seconds for user to access quit true else reset quit to false.
            new Runnable() {
                @Override
                public void run() {
                    new CountDownTimer(5000,5000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            quit = false;
                        }
                    }.start();
                }
            }.run();
            Toast.makeText(this, "Again to quit", Toast.LENGTH_SHORT).show();
        }
        else
            this.finish();
    }
    /**
     * The is called before onCreateOptionMenu is called.
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_edit);

        if(menuCheck)
            menuItem.setVisible(true);
        else
            menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cast, menu);
        //get menu item for edit and hide if not on person detail
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
            case R.id.action_edit:
                Log.i(LOG_TAG, "edit menu");
                editCallsCompleteSignUpFragment(R.id.cast_item_screen);
                return true;
            case R.id.action_logout:
                Log.i(LOG_TAG, "logout menu");
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        openGoogleSignInActivity("logout");
    }
    private void openGoogleSignInActivity(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        //save login to false
        prefEdit = pref.edit();
        prefEdit.putBoolean("login", false);
        prefEdit.apply();
        this.finish();
        startActivity(new Intent(CastActivity.this, LoginActivity.class));
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
