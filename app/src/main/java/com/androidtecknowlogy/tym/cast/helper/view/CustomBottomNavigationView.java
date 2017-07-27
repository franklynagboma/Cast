package com.androidtecknowlogy.tym.cast.helper.view;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

/**
 * Created by AGBOMA franklyn on 7/25/17.
 */

@CoordinatorLayout.DefaultBehavior(CoordinatorScrollBehaviour.class)
public class CustomBottomNavigationView extends BottomNavigationView {


    public CustomBottomNavigationView(Context context) {
        super(context);
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
