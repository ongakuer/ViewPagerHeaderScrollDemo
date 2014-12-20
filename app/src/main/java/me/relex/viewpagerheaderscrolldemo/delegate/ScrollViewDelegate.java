package me.relex.viewpagerheaderscrolldemo.delegate;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class ScrollViewDelegate implements ViewDelegate {
    private final int[] mViewLocationResult = new int[2];
    private final Rect mRect = new Rect();

    public boolean isViewBeingDragged(MotionEvent event, ScrollView view) {
        if (view.getChildCount() == 0) {
            return true;
        }

        view.getLocationOnScreen(mViewLocationResult);
        final int viewLeft = mViewLocationResult[0], viewTop = mViewLocationResult[1];
        mRect.set(viewLeft, viewTop, viewLeft + view.getWidth(), viewTop + view.getHeight());
        final int rawX = (int) event.getRawX(), rawY = (int) event.getRawY();
        if (mRect.contains(rawX, rawY)) {
            return isReadyForPull(view, rawX - mRect.left, rawY - mRect.top);
        }

        return false;
    }

    @Override public boolean isReadyForPull(View view, float x, float y) {
        return view.getScrollY() <= 0;
    }
}
