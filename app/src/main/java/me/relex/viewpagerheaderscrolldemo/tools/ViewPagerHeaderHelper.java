package me.relex.viewpagerheaderscrolldemo.tools;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class ViewPagerHeaderHelper {

    private int mHeaderHeight;

    private VelocityTracker mVelocityTracker;

    private boolean mIsBeingMove;

    private float mInitialMotionY;
    private float mInitialMotionX;
    private float mLastMotionY;
    private boolean mHandlingTouchEventFromDown;

    private boolean mIsHeaderExpand = true;

    private OnViewPagerTouchListener mListener;

    private int mTouchSlop;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private ViewPagerHeaderHelper() {
    }

    public ViewPagerHeaderHelper(Context context, OnViewPagerTouchListener listener) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

        mListener = listener;
    }

    public boolean onLayoutInterceptTouchEvent(MotionEvent event, int headerHeight) {
        mHeaderHeight = headerHeight;

        final float x = event.getX(), y = event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                boolean isViewBeingDragged = mListener.isViewBeingDragged(event);

                if (isViewBeingDragged && !mIsHeaderExpand || mIsHeaderExpand) {

                    if (mIsHeaderExpand && y < headerHeight) {
                        return mIsBeingMove;
                    }

                    mInitialMotionX = x;
                    mInitialMotionY = y;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mInitialMotionY > 0f && !mIsBeingMove) {
                    final float yDiff = y - mInitialMotionY;
                    final float xDiff = x - mInitialMotionX;
                    if ((!mIsHeaderExpand && yDiff > mTouchSlop)  // header fold , pull
                            || (mIsHeaderExpand && yDiff < mTouchSlop))// header expand, push
                    {
                        if (Math.abs(yDiff) > Math.abs(xDiff)) {
                            mIsBeingMove = true;
                            mListener.onMoveStarted(y);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsBeingMove) {
                    mListener.onMoveEnded(false, 0f);
                }
                resetTouch();
                break;
        }

        return mIsBeingMove;
    }

    public boolean onLayoutTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mHandlingTouchEventFromDown = true;
        }

        if (mHandlingTouchEventFromDown) {
            if (mIsBeingMove) {
                mLastMotionY = event.getY();
            } else {
                onLayoutInterceptTouchEvent(event, mHeaderHeight);
                return true;
            }
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action = event.getAction();
        final int count = event.getPointerCount();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                if (mIsBeingMove && y != mLastMotionY) {
                    final float yDx = mLastMotionY == -1 ? 0 : y - mLastMotionY;
                    mListener.onMove(y, yDx);
                    mLastMotionY = y;
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // Check the dot product of current velocities.
                // If the pointer that left was opposing another velocity vector, clear.
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                final int upIndex = event.getActionIndex();
                final int id1 = event.getPointerId(upIndex);
                final float x1 = mVelocityTracker.getXVelocity(id1);
                final float y1 = mVelocityTracker.getYVelocity(id1);
                for (int i = 0; i < count; i++) {
                    if (i == upIndex) continue;
                    final int id2 = event.getPointerId(i);
                    final float vx = x1 * mVelocityTracker.getXVelocity(id2);
                    final float vy = y1 * mVelocityTracker.getYVelocity(id2);

                    final float dot = vx + vy;
                    if (dot < 0) {
                        mVelocityTracker.clear();
                        break;
                    }
                }

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsBeingMove) {

                    boolean isFling = false;
                    float velocityY = 0;

                    if (action == MotionEvent.ACTION_UP) {
                        final VelocityTracker velocityTracker = mVelocityTracker;
                        final int pointerId = event.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                        velocityY = velocityTracker.getYVelocity(pointerId);
                        if ((Math.abs(velocityY) > mMinimumFlingVelocity)) {
                            isFling = true;
                        }
                    }

                    mListener.onMoveEnded(isFling, velocityY);
                }
                resetTouch();

                break;
        }

        return true;
    }

    private void resetTouch() {
        mIsBeingMove = false;
        mHandlingTouchEventFromDown = false;
        mInitialMotionY = mLastMotionY = -1f;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void setHeaderExpand(boolean isHeaderExpand) {
        this.mIsHeaderExpand = isHeaderExpand;
    }

    public float getInitialMotionY() {
        return mInitialMotionY;
    }

    public float getLastMotionY() {
        return mLastMotionY;
    }

    public interface OnViewPagerTouchListener {

        public boolean isViewBeingDragged(MotionEvent event);

        public void onMoveStarted(float eventY);

        public void onMove(float eventY, float yDx);

        public void onMoveEnded(boolean isFling, float flingVelocityY);
    }
}
