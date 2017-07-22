package com.androidtecknowlogy.tym.cast.helper.adpater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.EventFragment;
import com.androidtecknowlogy.tym.cast.helper.pojo.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AGBOMA franklyn on 7/12/17.
 */

public class EventListAdapter extends ArrayAdapter<Events> {

    private Context context;
    private ArrayList<Events> eventList;
    private ListView listItem;
    private int resPosition = -1;
    private int lastPosition = -1;
    private EventFragment eventFragment;

    public EventListAdapter(Context context, ArrayList<Events> eventList, ListView listItem,
                            EventFragment eventFragment){
        super(context, R.layout.events_item, eventList);
        this.context = context;
        this.eventList = eventList;
        this.listItem = listItem;
        this.eventFragment = eventFragment;
    }

    private static class ItemHolder {
        CardView cardItem;
        TextView castName, createdDate,eventHeader, eventText, eventDate;
    }

    /*@Override
    public int getCount() {
        //reverse list item to show newest events but still not working,
        //Come back to this and clear the comment notifying this after done.
        //Collections.reverse(eventList);
        return super.getCount();
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Events events = eventList.get(position);
        final ItemHolder holder;

        if(null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.events_item, parent, false);
            holder = new ItemHolder();

            holder.cardItem = (CardView) convertView.findViewById(R.id.card_item);
            holder.castName = (TextView) convertView.findViewById(R.id.cast_name);
            holder.createdDate = (TextView) convertView.findViewById(R.id.created_date);
            holder.eventHeader = (TextView) convertView.findViewById(R.id.event_header);
            holder.eventText = (TextView) convertView.findViewById(R.id.event_text);
            holder.eventDate = (TextView) convertView.findViewById(R.id.event_date);

            convertView.setTag(holder);
        }
        else
            holder = (ItemHolder) convertView.getTag();

        holder.castName.setText(events.getPosterName());
        holder.createdDate.setText(events.getCreatedDate());
        holder.eventHeader.setText(events.getEventTitle().toUpperCase());
        holder.eventText.setText(events.getEventText());
        holder.eventDate.setText(events.getEventDay_Time());

        /*AdapterView.OnItemLongClickListener longItemClick = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int post, long id) {
                highLightText(post, view);
                return true;
            }
        };

        listItem.setOnItemLongClickListener(longItemClick);*/

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up : R.anim.down);
        holder.cardItem.setAnimation(animation);
        lastPosition = position;

        return convertView;
    }

    /*private void highLightText(int position, View itemView){
        //use share as a button click to stop the color showing on more than one item.
        listItem.getChildAt(position).setBackgroundResource(R.color.liteAccent);
        *//**
         * check if resPosition by child is not equal to both -1 and position
         * if true set to initial color else set to selector color (litePrimary).
         *//*
        if(resPosition != -1 && resPosition != position) {
            listItem.getChildAt(resPosition).setBackgroundResource(android.R.color.transparent);
        }
        resPosition = position;
        //get item view and send text String to EventFragment setItemSelected
        eventFragment.setItemSelected(((TextView) itemView.findViewById(R.id.event_header))
                .getText().toString(), ((TextView) itemView.findViewById(R.id.event_text))
                .getText().toString(),((TextView) itemView.findViewById(R.id.event_date))
                .getText().toString());
    }*/
}
