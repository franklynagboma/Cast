package com.androidtecknowlogy.tym.cast.helper.io;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.CalendarView;


/**
 * Created by AGBOMA franklyn on 7/17/17.
 */

public class CustomCalendar extends CalendarView {


    public CustomCalendar(@NonNull Context context) {
        super(context);
    }

    public CustomCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCalendar(@NonNull Context context, @Nullable AttributeSet attrs,
                          @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * ev = gets the MotionEvent on calendar to check scrolling effect
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent parent = getParent();
            if(null != parent) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }
}
