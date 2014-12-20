package me.relex.viewpagerheaderscrolldemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import me.relex.viewpagerheaderscrolldemo.R;
import me.relex.viewpagerheaderscrolldemo.delegate.AbsListViewDelegate;

public class ListViewFragment extends BaseViewPagerFragment
        implements AbsListView.OnItemClickListener {

    private ListView mListView;
    private ListAdapter mAdapter;
    private AbsListViewDelegate mAbsListViewDelegate = new AbsListViewDelegate();

    public static ListViewFragment newInstance(int index) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    public ListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override public boolean isViewBeingDragged(MotionEvent event) {
        return mAbsListViewDelegate.isViewBeingDragged(event, mListView);
    }
}
