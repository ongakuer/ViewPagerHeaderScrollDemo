package me.relex.viewpagerheaderscrolldemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import me.relex.viewpagerheaderscrolldemo.delegate.AbsListViewDelegate;

public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    private static final String BUNDLE_FRAGMENT_INDEX = "ItemFragment.BUNDLE_FRAGMENT_INDEX";

    private int mFragmentIndex;

    private ListView mListView;
    private ListAdapter mAdapter;

    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();

    public static ItemFragment newInstance(int index) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mFragmentIndex = bundle.getInt(BUNDLE_FRAGMENT_INDEX, 0);
        }

        String[] listArrays = null;

        switch (mFragmentIndex) {
            case 1:
                listArrays = getResources().getStringArray(R.array.continents);
                break;
            case 2:
                listArrays = getResources().getStringArray(R.array.cities);
                break;
            default:
                listArrays = getResources().getStringArray(R.array.countries);
                break;
        }

        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, listArrays);

        if (mListener != null) {
            mListener.onFragmentAttached(this, mFragmentIndex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnFragmentInteractionListener");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentAttached(ItemFragment fragment, int position);

        public void onFragmentDetached(ItemFragment fragment, int position);
    }

    public boolean isViewBeingDragged(MotionEvent event) {
        return mAbsListViewDelegate.isViewBeingDragged(event, mListView);
    }
}
