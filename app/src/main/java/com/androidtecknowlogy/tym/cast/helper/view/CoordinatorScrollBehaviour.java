package com.androidtecknowlogy.tym.cast.helper.view;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by AGBOMA franklyn on 7/25/17.
 */

public class ScrollBehaviour extends CoordinatorLayout.Behavior<BottomNavigationView> {

    public ScrollBehaviour() {
        super();
    }

    public ScrollBehaviour(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child,
                                   View dependency) {
        return (dependency instanceof FrameLayout);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       BottomNavigationView child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child,
                                  View target, int dx, int dy, int[] consumed) {

        if(dy < 0)
            showBottomNav(child);
        else
            hindBottomNav(child);
    }

    private void showBottomNav(BottomNavigationView child) {
        child.animate().translationY(0);
    }

    private void hindBottomNav(BottomNavigationView child) {
        child.animate().translationY(child.getHeight());
    }
}
