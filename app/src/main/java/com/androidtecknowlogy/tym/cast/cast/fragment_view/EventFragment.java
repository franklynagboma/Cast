package com.androidtecknowlogy.tym.cast.cast.fragment_view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.faces.Constant;
import com.androidtecknowlogy.tym.cast.helper.adpater.EventListAdapter;
import com.androidtecknowlogy.tym.cast.helper.io.CustomCalendar;
import com.androidtecknowlogy.tym.cast.helper.io.CustomDataFormat;
import com.androidtecknowlogy.tym.cast.helper.io.CustomTextWatcher;
import com.androidtecknowlogy.tym.cast.helper.pojo.Events;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

/**
 * Created by AGBOMA franklyn on 6/18/17.
 */

public class EventFragment extends Fragment implements Constant.PresenterEventsLogicToEventFragment{

    private final String LOG_TAG = EventFragment.class.getSimpleName();

    private final String HASHTAG = "\n# *CAST*";
    private String shareText = "Download Tym CAST link";
    private Context context;
    private SharedPreferences pref;
    private String getPrefName = "";
    private String getPrefPhoto = "";
    private String getDateFromView = "";
    public final String TITLE = "title";
    public final String TEXT = "text";
    public final String DATE = "date";
    public final String TIME = "time";
    private EventListAdapter eventListAdapter;
    private String[] monthReadable = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    private Constant.AddEventsDetailsToPresenter addEventsDetailsToPresenter;

    //views
    @BindView(R.id.circular_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cast_image)
    ImageView castImage;
    @BindView(R.id.cast_name)
    TextView castName;
    @BindView(R.id.add_event)
    ImageView addEventButton;
    @BindView(R.id.events_lists)
    ListView listView;


    public EventFragment(){
        //set options menu to inflate share menu
        //setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View eventView = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, eventView);
        context  = getActivity();

        //get users name and image.
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        getPrefName = pref.getString("name", "");
        getPrefPhoto = pref.getString("image", "");

        eventListAdapter = new EventListAdapter(context, new ArrayList<Events>(), listView, this);
        listView.setAdapter(eventListAdapter);

        return eventView;
    }

    public void setAddEventsDetailsToPresenter(Constant.AddEventsDetailsToPresenter
                                                       addEventsDetailsToPresenter) {
        this.addEventsDetailsToPresenter = addEventsDetailsToPresenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        setProgressBar(true);
        //start EventListener.
        addEventsDetailsToPresenter.initializeChildListener(true);
        if(!getPrefPhoto.isEmpty())
            Picasso.with(context).load(getPrefPhoto)
                    .transform(new CircularTransform())
                    .placeholder(R.mipmap.ic_cast_person)
                    .error(R.mipmap.ic_cast_person)
                    .into(castImage);
        castName.setText(getPrefName);
    }

    private void setProgressBar(boolean mes){
        //set progressBar and indeterminate.
        if(!mes){
            progressBar.setVisibility(View.GONE);
            progressBar.setIndeterminate(false);
        }
        else {
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clearAdapter();
    }

    private void clearAdapter() {
        //stop EventListener
        addEventsDetailsToPresenter.initializeChildListener(false);
        eventListAdapter.clear();
        eventListAdapter.notifyDataSetChanged();
    }
    /**
     * message contains the response from FireBase on creating Events
     * @param message
     */
    @Override
    public void sendToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEventsClassToEventFragment(Events events) {
        setProgressBar(false);
        //add events and notify data set changed.
        eventListAdapter.add(events);
        eventListAdapter.notifyDataSetChanged();
    }

    /**
     * if dataBase content is/was deleted, perform this interface.
     */
    @Override
    public void receiveDataBaseDeletedResponse() {
        //clear adapter and re-initialize.
        clearAdapter();
        init();
    }

    @OnClick(R.id.add_event)
    public void addEventButtonClicked() {
        Log.i(LOG_TAG, "add event button");
        //getSystemService on inflater.
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //get resource views.
        View view = layoutInflater.inflate(R.layout.dialog, null, false);

        TextInputLayout inputTitle = (TextInputLayout) view.findViewById(R.id.input_title);
        final EditText eventTitle = (EditText) view.findViewById(R.id.event_title);
        eventTitle.addTextChangedListener(new CustomTextWatcher(eventTitle, TITLE));

        TextInputLayout inputText = (TextInputLayout) view.findViewById(R.id.input_text);
        final EditText eventText = (EditText) view.findViewById(R.id.event_text);
        eventText.addTextChangedListener(new CustomTextWatcher(eventText, inputText, TEXT));

        TextView inputDate = (TextView) view.findViewById(R.id.input_date);
        final CustomCalendar calendar = (CustomCalendar) view.findViewById(R.id.calender_picker);
        //set initial data format
        getDateFromView = new CustomDataFormat().getDateFormat(new Date(calendar.getDate()), "m/d/y");
        //get data format form users if changed.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                //get month as string
                getDateFromView = monthReadable[month] +" " +dayOfMonth +", " + year;
            }
        });
        /*ImageView calender = (ImageView) view.findViewById(R.id.img_calendar);
        final EditText eventCalender = (EditText) view.findViewById(R.id.event_calendar);
        eventCalender.addTextChangedListener(new CustomTextWatcher(eventCalender, DATE));*/

        TextView inputTime = (TextView) view.findViewById(R.id.input_time);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(false);
        /*ImageView time = (ImageView) view.findViewById(R.id.img_time);
        final EditText eventTime = (EditText) view.findViewById(R.id.event_time);
        eventTime.addTextChangedListener(new CustomTextWatcher(eventTime, TIME));*/

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.autoDismiss(false)
                .customView(view, true);

        dialog.positiveText("Create event")
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i(LOG_TAG, "positive button clicked");

                        //perform logic on EditTex
                        String getTitle = eventTitle.getText().toString();
                        String getText = eventText.getText().toString();
                        int getHour = timePicker.getCurrentHour();
                        int getMin = timePicker.getCurrentMinute();
                        String getTime = getHour +":" +getMin;
                        Log.i(LOG_TAG, "Calender: " +getDateFromView +"\n Time: " +getTime);

                        if(TextUtils.isEmpty(getTitle) || TextUtils.isEmpty(getText)
                                || TextUtils.isEmpty(getDateFromView) || TextUtils.isEmpty(getTime))
                            Toast.makeText(context, "Incomplete event credentials",
                                    Toast.LENGTH_SHORT).show();
                        else {
                            if(getText.length() > 200)
                                eventText.setError("Above 200 words");
                            else
                                eventText.setError(null);

                            dialog.dismiss();
                            //add time of creation of event.
                            addEventsDetailsToPresenter
                                    .sendAddEventsDetailsToPresenter(getPrefName,
                                            getTitle, getText, getDateFromView, getTime);
                        }
                    }
                });
        dialog.negativeText("Discard")
                .negativeColor(getResources().getColor(R.color.colorAccent))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.i(LOG_TAG, "negative button clicked");
                        dialog.dismiss();
                    }
                });

        //show material dialog view to get user details to add.
        MaterialDialog builder = dialog.build();
        builder.show();
        builder.setCancelable(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getPrefName.equals(""))
            getPrefName = "AN0NYMOUS";
        Log.i(LOG_TAG, "Name: " + getPrefName);
    }

    /*public void setItemSelected(String header, String text, String date) {
        shareText = header +"\n"+ text +"\n"+ date + " ";
        //recall OnCreateOptionsMenu to reset createShareIntent with new content of shareText.
        //getActivity().invalidateOptionsMenu(); this part crashes the system.
    }*/


    private Intent createShareIntent() {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, shareText + HASHTAG);
        return share;

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        //inflate share menu
        inflater.inflate(R.menu.menu_share, menu);
        //retrieve menu item id
        MenuItem menuItem = menu.findItem(R.id.share);
        //get the share intent providers on the device
        ShareActionProvider shareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(menuItem);
        //check if the intent are available the attach to the share menu intent provider
        if(null != shareActionProvider)
            shareActionProvider.setShareIntent(createShareIntent());
        else
            Toast.makeText(context, "Share application not found", Toast.LENGTH_SHORT).show();
    }*/
}
