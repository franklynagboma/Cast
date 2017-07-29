package com.androidtecknowlogy.tym.cast.helper.io;

import android.widget.Filter;

import com.androidtecknowlogy.tym.cast.helper.adpater.RecyclerItemAdapter;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AGBOMA franklyn on 7/27/17.
 */

public class CustomFilter  extends Filter {

    private RecyclerItemAdapter itemAdapter;
    private List<CastItems> castItemsList;

    public CustomFilter() {

    }

    public CustomFilter(RecyclerItemAdapter itemAdapter, List<CastItems> castItemsList) {
        this.itemAdapter = itemAdapter;
        this.castItemsList = castItemsList;
    }


    /**
     * constraint charSequence contains the proposed  filter string
     * that is query to search for.
      * @param constraint
     * @return
     */
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        //check constraint validity and if length getter than 0
        //to know if users typed in a query
        if(null != constraint && constraint.length() >0) {

            //change the constraint to lower cast for consistences.
            constraint = constraint.toString().toLowerCase();

            //to store the filtered list.
            List<CastItems> filteredCastItemList = new ArrayList<>();

            //loop through list for query
            for (CastItems cast : castItemsList) {
                //add filtered to filtered list.
                if(cast.getCastName().toLowerCase().contains(constraint))
                    filteredCastItemList.add(cast);
            }

            //get the filtered size
            filterResults.count = filteredCastItemList.size();
            //get the filtered objects.
            filterResults.values = filteredCastItemList;
        }
        else {
            //if constraint is non
            //get the initial size and objects.
            filterResults.count = castItemsList.size();
            filterResults.values = castItemsList;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        itemAdapter.castItemsList = (ArrayList<CastItems>) results.values;
        itemAdapter.notifyDataSetChanged();//refresh adapter.
    }
}
