package me.relex.viewpagerheaderscrolldemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import me.relex.viewpagerheaderscrolldemo.R;
import me.relex.viewpagerheaderscrolldemo.delegate.ScrollViewDelegate;

public class ScrollViewFragment extends BaseViewPagerFragment {

    private ScrollView mScrollView;
    private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();

    public static ScrollViewFragment newInstance(int index) {
        ScrollViewFragment fragment = new ScrollViewFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroll_view, container, false);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollview);
        TextView textView = (TextView) view.findViewById(R.id.text1);
        textView.setText(loadContentString());
        return view;
    }

    @Override public boolean isViewBeingDragged(MotionEvent event) {
        return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
    }

    private String loadContentString() {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[8 * 1024];
        int len;
        String content = "";
        try {
            inputStream = getActivity().getAssets().open("lorem.txt");
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            content = outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSilently(inputStream);
            closeSilently(outputStream);
        }

        return content;
    }

    public void closeSilently(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
            // do nothing
        }
    }
}
