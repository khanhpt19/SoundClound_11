package com.framgia.soundcloud.screen.tracks;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final int VISIBLE_THRESHOLD = 1;
    private int mCurrentPage = 1;
    private boolean mIsLoading;
    private RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        int totalItemCount = mLayoutManager.getItemCount();
        if (!mIsLoading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount) {
            mCurrentPage++;
            onLoadMore(mCurrentPage);
            mIsLoading = true;
        }
    }

    public void resetStateLoading() {
        this.mIsLoading = false;
    }

    public abstract void onLoadMore(int page);
}
