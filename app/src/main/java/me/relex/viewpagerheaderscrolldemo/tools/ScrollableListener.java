package me.relex.viewpagerheaderscrolldemo.tools;

import android.view.MotionEvent;

public interface ScrollableListener {
    public boolean isViewBeingDragged(MotionEvent event);
}
