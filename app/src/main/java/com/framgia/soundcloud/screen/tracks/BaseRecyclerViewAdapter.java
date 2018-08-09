package com.framgia.soundcloud.screen.tracks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.framgia.soundcloud.data.model.Track;

import java.util.Collection;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int COUNT = 1;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mListData;

    protected BaseRecyclerViewAdapter(@NonNull Context context, List<T> listData) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListData = listData;
    }

    public void addData(List<T> dataViews) {
        mListData.addAll((Collection<? extends T>) dataViews);
        notifyDataSetChanged();
    }

    public void addLoadingView() {
        mListData.add(null);
        notifyItemInserted(mListData.size() - COUNT);
    }

    public void removeLoadingView() {
        mListData.remove(mListData.size() - COUNT);
        notifyItemRemoved(mListData.size());
    }

    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }
}
