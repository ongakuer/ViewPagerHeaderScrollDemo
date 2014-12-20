package me.relex.viewpagerheaderscrolldemo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import me.relex.viewpagerheaderscrolldemo.tools.ScrollableFragmentListener;
import me.relex.viewpagerheaderscrolldemo.tools.ScrollableListener;

public abstract class BaseViewPagerFragment extends Fragment implements ScrollableListener {

    protected ScrollableFragmentListener mListener;
    protected static final String BUNDLE_FRAGMENT_INDEX = "BaseFragment.BUNDLE_FRAGMENT_INDEX";
    protected int mFragmentIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mFragmentIndex = bundle.getInt(BUNDLE_FRAGMENT_INDEX, 0);
        }

        if (mListener != null) {
            mListener.onFragmentAttached(this, mFragmentIndex);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ScrollableFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement ScrollableFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        if (mListener != null) {
            mListener.onFragmentDetached(this, mFragmentIndex);
        }

        super.onDetach();
        mListener = null;
    }
}
