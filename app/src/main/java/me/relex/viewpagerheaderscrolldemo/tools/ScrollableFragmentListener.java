package me.relex.viewpagerheaderscrolldemo.tools;

public interface ScrollableFragmentListener {

    public void onFragmentAttached(ScrollableListener fragment, int position);

    public void onFragmentDetached(ScrollableListener fragment, int position);
}
