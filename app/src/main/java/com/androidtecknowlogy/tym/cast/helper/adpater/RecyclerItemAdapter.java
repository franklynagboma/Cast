package com.androidtecknowlogy.tym.cast.helper.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.ItemsFragment;
import com.androidtecknowlogy.tym.cast.helper.io.CustomFilter;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AGBOMA franklyn on 6/18/17.
 */

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.LayoutHolder>
        implements Filterable{

    private final String LOG_TAG = RecyclerItemAdapter.class.getSimpleName();
    private Context context;
    public List<CastItems> castItemsList;
    //private List<CastItems> filteredList;
    private CustomFilter filter;
    private boolean isTab;
    private Constant.ItemsSendItemPositionToPresenter itemPositionToPresenter;
    private ItemsFragment.DynamicFragment dynamicFragment;
    private int lastPosition = -1;
    private List<Integer> circularFrame = new ArrayList<Integer>(){ {
        add(R.drawable.circular_frame_red);
        add(R.drawable.circular_frame_yellow);
        add(R.drawable.circular_frame_brown);
        add(R.drawable.circular_frame_green);
        add(R.drawable.circular_frame_purple);
    }};
    private List<Integer> saveCircularFramePosition;


    public RecyclerItemAdapter(Context context, List<CastItems> castItemsList, boolean isTab,
                               Constant.ItemsSendItemPositionToPresenter itemPositionToPresenter,
                               ItemsFragment.DynamicFragment dynamicFragment) {

        /**
         * The interfaces was send here because of null pointer from
         * the new instance of ItemsFragment when it calls
         * a method to perform logic.
         */
        this.context = context;
        this.castItemsList = castItemsList;
        //this.filteredList = castItemsList;
        this.isTab = isTab;
        saveCircularFramePosition = new ArrayList<>();
        this.itemPositionToPresenter = itemPositionToPresenter;
        this.dynamicFragment = dynamicFragment;
        Log.e(LOG_TAG, "rec.... ");
    }

    @Override
    public LayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.cast_item, parent, false);
        return new LayoutHolder(view);
    }

    @Override
    public void onBindViewHolder(final LayoutHolder holder, final int position) {

        //Set views display
        final CastItems castItems = castItemsList.get(position);

        //Randomize the circular index color and set resource to imageHolder.
        int res = circularFrame.get(new Random().nextInt(circularFrame.size()));
        //show cast view.
        holder.imageHolder.setBackgroundResource(res);
        saveCircularFramePosition.add(res);

        String getImageUrl = castItems.getCastImage();
        if(!TextUtils.isEmpty(getImageUrl))
            Picasso.with(context).load(getImageUrl)
                    .transform(new CircularTransform())
                    .placeholder(R.mipmap.ic_cast_person)
                    .error(R.mipmap.ic_cast_person)
                    .into(holder.castImage);
        holder.castName.setText(castItems.getCastName());
        holder.castGender.setText(castItems.getCastGender());
        holder.castTitle.setText(castItems.getCastTitle());
        holder.castJoin.setText(castItems.getCastJoined());

        Log.i(LOG_TAG, "cast name: " + castItems.getCastName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //arguments = position-> for cast item while true to tell it being clinked
                Log.i(LOG_TAG, "Res position " + position
                        +"name" + castItems.getCastName());
                itemPositionToPresenter.positionItemFragment(castItemsList, "",
                        saveCircularFramePosition.get(position), position, true);
                //show cast details on new Fragment.
                dynamicFragment.changeToDetailsFragment();
            }
        };
        holder.textHolder.setOnClickListener(listener);

        //set font type for views
        holder.castName.setTypeface(AppController.getDroidFace(context));
        holder.castGender.setTypeface(AppController.getProximaFace(context));
        holder.castTitle.setTypeface(AppController.getProximaFace(context));
        holder.castJoin.setTypeface(AppController.getProximaFace(context));

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition)
                        ?android.R.anim.slide_in_left :android.R.anim.slide_in_left);
        holder.textHolder.setAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        //Return ArrayList size of item.
        return castItemsList.size();
    }

    /*public void setFilter(List<CastItems> castItems) {
        //use clear intend of re-initializing so as to remove all item on castItemsList
        Log.i(LOG_TAG, "On filter castItemsList size 1= " + castItems.size());
        castItemsList = new ArrayList<>();
        castItemsList.addAll(castItems);
        Log.i(LOG_TAG, "On filter castItemsList size 2= " + castItemsList.size());
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter() {
        if(null == filter)
            filter = new CustomFilter(this, castItemsList);
        return filter;
    }

    class LayoutHolder extends RecyclerView.ViewHolder{

        private RelativeLayout textHolder;
        private FrameLayout imageHolder;
        private ImageView castImage;
        private TextView castName, castTitle, castGender, castJoin;

        public LayoutHolder(View itemView) {
            super(itemView);

            this.textHolder = (RelativeLayout) itemView.findViewById(R.id.text_holder);
            this.imageHolder = (FrameLayout) itemView.findViewById(R.id.image_holder);
            this.castImage = (ImageView) itemView.findViewById(R.id.cast_image);
            this.castName = (TextView) itemView.findViewById(R.id.cast_name);
            this.castTitle = (TextView) itemView.findViewById(R.id.cast_title);
            this.castGender = (TextView) itemView.findViewById(R.id.cast_gender);
            this.castJoin = (TextView) itemView.findViewById(R.id.cast_join);
        }
    }
}


