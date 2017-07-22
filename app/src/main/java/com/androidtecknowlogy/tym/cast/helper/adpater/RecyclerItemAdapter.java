package com.androidtecknowlogy.tym.cast.helper.adpater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.fragment_view.ItemsFragment;
import com.androidtecknowlogy.tym.cast.faces.Constant;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AGBOMA franklyn on 6/18/17.
 */

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.LayoutHolder>{

    private final String LOG_TAG = RecyclerItemAdapter.class.getSimpleName();
    private Context context;
    private boolean isTab;
    private Constant.ItemsSendItemPositionToPresenter itemPositionToPresenter;
    private ItemsFragment.DynamicFragment dynamicFragment;
    private int countLeft = 1;
    private int countRight = 2;
    private int lastPosition = -1;
    private List<Integer> circularFrame = new ArrayList<Integer>(){ {
        add(R.drawable.circular_frame_red);
        add(R.drawable.circular_frame_yellow);
        add(R.drawable.circular_frame_brown);
        add(R.drawable.circular_frame_green);
        add(R.drawable.circular_frame_purple);
    }};
    private List<Integer> saveCircularFramePosition;


    public RecyclerItemAdapter(Context context, boolean isTab,
                               Constant.ItemsSendItemPositionToPresenter itemPositionToPresenter,
                               ItemsFragment.DynamicFragment dynamicFragment) {

        /**
         * The interfaces was send here because of null pointer from
         * the new instance of ItemsFragment when it calls
         * a method to perform logic.
         */
        this.context = context;
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

        CastItems castItems = AppController.detailsCastItems.get(position);
        Log.i(LOG_TAG, "castItems " + castItems.toString());

        //Set views display
        /*RelativeLayout.LayoutParams positionView = (RelativeLayout.LayoutParams)
                holder.layoutHolder.getLayoutParams();
        if(countLeft == position){
            positionView.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            holder.layoutHolder.setLayoutParams(positionView);
        }
        else if(countRight == position) {
            positionView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            holder.layoutHolder.setLayoutParams(positionView);
        }*/

        //Randomize the circular index color and set resource to imageHolder.
        int res = circularFrame.get(new Random().nextInt(circularFrame.size()));
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
        holder.castJoined.setText(castItems.getCastJoined());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //arguments = position-> for cast item while true to tell it being clinked
                Log.i(LOG_TAG, "Res position " + position);
                itemPositionToPresenter.positionItemFragment(
                        saveCircularFramePosition.get(position), position, true);
                //show cast details on new Fragment.
                dynamicFragment.changeToDetailsFragment();
            }
        };
        holder.textHolder.setOnClickListener(listener);

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up : R.anim.down);
        holder.textHolder.setAnimation(animation);
        lastPosition = position;

        //add position count left
        //countLeft += position;
        //add position count right
        //countRight += position;
    }

    @Override
    public int getItemCount() {
        //Return ArrayList size of item.
        return AppController.detailsCastItems.size();
    }


    class LayoutHolder extends RecyclerView.ViewHolder{

        private LinearLayout layoutHolder;
        private RelativeLayout textHolder;
        private FrameLayout imageHolder;
        private ImageView castImage;
        private TextView castName, castTitle, castGender, castJoined;

        public LayoutHolder(View itemView) {
            super(itemView);

            this.layoutHolder = (LinearLayout) itemView.findViewById(R.id.layout_holder);
            this.textHolder = (RelativeLayout) itemView.findViewById(R.id.text_holder);
            this.imageHolder = (FrameLayout) itemView.findViewById(R.id.image_holder);
            this.castImage = (ImageView) itemView.findViewById(R.id.cast_image);
            this.castName = (TextView) itemView.findViewById(R.id.cast_name);
            this.castTitle = (TextView) itemView.findViewById(R.id.cast_title);
            this.castGender = (TextView) itemView.findViewById(R.id.cast_gender);
            this.castJoined = (TextView) itemView.findViewById(R.id.cast_joined);
        }
    }
}


