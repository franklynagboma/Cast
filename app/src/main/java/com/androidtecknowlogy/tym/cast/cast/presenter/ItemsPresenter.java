package com.androidtecknowlogy.tym.cast.cast.presenter;

import android.content.Context;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.cast.model.ItemsModel;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;

import java.util.List;

/**
 * Created by AGBOMA franklyn on 6/25/17.
 */

public class ItemsPresenter implements Constant.ItemsSendItemPositionToPresenter,
        Constant.ItemModelToPresenter{

    private final String LOG_TAG = ItemsPresenter.class.getSimpleName();
    private final String toCousantEmail = "franklyn1great@gmail.com";
    private final String toCousantNumber = "08068046013";

    ItemsModel model;
    private Context context;
    private Constant.PresenterCallsItemRecyclerView presenterCallsItemRecyclerView;
    private Constant.PresenterSendToCastDetailsFragment presenterToDetailsFragment;

    public ItemsPresenter(){}
    public ItemsPresenter(Context context){
        this.context = context;
    }
    public void setPresenterCallsItemRecyclerView(Constant.PresenterCallsItemRecyclerView
                                                          presenterCallsItemRecyclerView) {
        this.presenterCallsItemRecyclerView = presenterCallsItemRecyclerView;
        //model = new ItemsModel(context);
        model = new ItemsModel(context);
        model.setModelToPresenter(this);
    }

    public void setPresenterToDetailsFragment(Constant.PresenterSendToCastDetailsFragment
                                                      presenterToDetailsFragment) {
        this.presenterToDetailsFragment = presenterToDetailsFragment;
    }

    /**
     * from ItemsFragment sending position to presenter
     * @param position
     */
    @Override
    public void positionItemFragment(List<CastItems> castItemsList, String userEmail,
                                     int itemPosition, int position, boolean value) {
        if(!value) {
            //call attach and detach data listener.
            if(position == 1)
                model.attachedDataListener(userEmail);

            else
                model.detachDataListener();
        }
        //get position clicked.
        else
            model.getPosition(castItemsList,itemPosition, position);

    }

    @Override
    public void positionDetails(int itemPosition, CastItems items) {
        Log.e(LOG_TAG, "call details fragment with castItems");
        presenterToDetailsFragment.details(itemPosition, items.getCastImage(),
                !AppController.isGuest ?items.getCastEmail() : toCousantEmail,
                items.getCastGender(), items.getCastJoined(), items.getCastName(),
                !AppController.isGuest ?items.getCastMobile() :toCousantNumber,
                items.getCastTitle(), items.getCastDob(), items.getCastSummary());
    }

    @Override
    public void recyclerView(boolean value) {
        presenterCallsItemRecyclerView.resetRecyclerView(value);
    }
}
