package com.framgia.soundcloud.screen.tracks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public class TracksAdapter extends BaseRecyclerViewAdapter<Track> {
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADING = 2;
    private List<Track> mTracks;
    private Context mContext;
    private TrackItemClickListener mTrackItemClickListener;

    public TracksAdapter(Context context, List<Track> tracks, TrackItemClickListener trackItemClickListener) {
        super(context, tracks);
        this.mContext = context;
        this.mTracks = tracks;
        this.mTrackItemClickListener = trackItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                View viewItem = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_tracks, parent, false);
                return new ViewHolder(viewItem, mContext, mTracks, mTrackItemClickListener);
            case VIEW_TYPE_LOADING:
                View viewLoading = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_progressbar_loadmore, parent, false);
                return new LoadMoreViewHolder(viewLoading);
            default:
                View viewDefault = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_tracks, parent, false);
                return new ViewHolder(viewDefault, mContext, mTracks, mTrackItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Track movie = mTracks.get(position);
            ((ViewHolder) holder).setData(movie);
        } else if (holder instanceof LoadMoreViewHolder) {
            ((LoadMoreViewHolder) holder).setIndeterminateImage();
        }
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mTracks.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTitle;
        private TextView mTextArtist;
        private Context mContext;
        private List<Track> mMovies;
        private ImageView mImageArtwork;

        private TrackItemClickListener mTrackItemClickListener;

        public ViewHolder(final View itemView, Context context, List<Track> movies,
                          TrackItemClickListener trackItemClickListener) {
            super(itemView);
            this.mContext = context;
            this.mMovies = movies;
            this.mTrackItemClickListener = trackItemClickListener;
            mImageArtwork = itemView.findViewById(R.id.image_arwork);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mTrackItemClickListener.onOptionClickListener(getLayoutPosition());
        }

        void setData(Track movie) {
            Glide.with(mContext).load(movie.getArtWorkUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.default_error_image_track)
                            .error(R.drawable.default_error_image_track))
                    .into(mImageArtwork);
            mTextArtist.setText(movie.getArtist());
            mTextTitle.setText(movie.getTitle());
        }
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mLoadMore;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mLoadMore = itemView.findViewById(R.id.progress_loadmore);
        }

        void setIndeterminateImage() {
            mLoadMore.setIndeterminate(true);
        }
    }

    public interface TrackItemClickListener {
        void onOptionClickListener(int position);
    }
}
