package com.androidtecknowlogy.tym.cast.cast.fragment_view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.cast.presenter.ItemsPresenter;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.helper.adpater.RecyclerItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AGBOMA franklyn on 6/26/17.
 */

public class ItemsFragment extends Fragment implements Constant.PresenterCallsItemRecyclerView,
        SearchView.OnQueryTextListener{

    private final String LOG_TAG = ItemsFragment.class.getSimpleName();
    private Constant.ItemsSendItemPositionToPresenter itemPositionToPresenter;

    @BindView(R.id.item_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.circular_progress_bar)
    ProgressBar progressBar;

    private RecyclerItemAdapter itemAdapter;
    private ItemsPresenter itemsPresenter;
    private Context context;
    private boolean isTab;
    private int orientation;

    public DynamicFragment detailsFragmentScreen;

    public interface DynamicFragment {
        void changeToDetailsFragment ();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            detailsFragmentScreen = (DynamicFragment) context;
        }
        catch (ClassCastException i){
            throw new ClassCastException(context.toString());
        }
    }

    public ItemsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "onActivityCreated");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_recycler, container, false);
        ButterKnife.bind(this,view);
        context = getActivity();
        isTab = context.getResources().getBoolean(R.bool.isTab);
        orientation = getResources().getConfiguration().smallestScreenWidthDp;

        itemAdapter = new RecyclerItemAdapter(context, AppController.detailsCastItems,
                isTab, itemPositionToPresenter, detailsFragmentScreen);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        //this will give null so to avoid, setHasOptionsMenu(true) in onViewCreated.
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView  = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("By name...");
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        //itemAdapter.getFilter().filter(query);
        query = query.toLowerCase();
        Log.i(LOG_TAG, "Previous is empty: " + AppController.detailsCastItems.isEmpty()
                + " Size: " + AppController.detailsCastItems.size());
        List<CastItems> castItems = new ArrayList<>();
        for(CastItems cast : AppController.detailsCastItems) {

            String getName = cast.getCastName().toLowerCase();
            //get the search item on a list
            if(getName.contains(query))
                castItems.add(cast);
        }
        Log.i(LOG_TAG, "castItems is empty = " + castItems.isEmpty());
        //itemAdapter.setFilter(!castItems.isEmpty() ? castItems : AppController.detailsCastItems);
        itemAdapter.setFilter(castItems);
        return true;

    }


    public void setItemPositionToPresenter(Constant.ItemsSendItemPositionToPresenter
                                                   itemPositionToPresenter) {
        this.itemPositionToPresenter = itemPositionToPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        //arguments = 0-> for null int and false -> not to allow click
        itemPositionToPresenter.positionItemFragment(null,0,0, false);
        Log.i(LOG_TAG, "onPause detached listener");
    }


    private void init() {
        Log.e(LOG_TAG, "set view");
        setProgressBar(true);
        //set model data listener
        itemPositionToPresenter.positionItemFragment(null,0,1, false);
        Log.i(LOG_TAG, "onResume attached listener");
        if(!isTab)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else {
            // for 7 and 10 inches Tab.
            if (orientation <= 750)
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            else
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        }
        recyclerView.setHasFixedSize(true);
        //set Recycler adapter
        recyclerView.setAdapter(itemAdapter);
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
    public void resetRecyclerView(boolean value) {
        Log.e(LOG_TAG, "itemFragment reload adapter " + value);
        setProgressBar(false);
        if(!value) {
            //clear list, onPause called.
            AppController.detailsCastItems.clear();
            }
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
