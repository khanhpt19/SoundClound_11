package com.framgia.soundcloud.screen.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.ItemTrackClickListener;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<Track> mTracks;
    private int mIndex;
    private ItemTrackClickListener mItemTrackClickListener;

    public TrackAdapter(List<Track> tracks, int index, ItemTrackClickListener itemTrackClickListener) {
        mTracks = tracks;
        mIndex = index;
        mItemTrackClickListener = itemTrackClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle;
        private TextView mTextArtist;
        private TextView mTextPlaybackCount;
        private ImageView mImageTrack;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.text_item_music_title);
            mTextArtist = itemView.findViewById(R.id.text_item_music_artist);
            mTextPlaybackCount = itemView.findViewById(R.id.text_item_music_playback_count);
            mImageTrack = itemView.findViewById(R.id.image_item_music_track);
        }

        public void bindView(Track track, final ItemTrackClickListener itemTrackClickListener,
                             final int index, final int trackPosition) {
            mTextTitle.setText(track.getTitle());
            mTextArtist.setText(track.getArtist());
            mTextPlaybackCount.setText(String.valueOf(track.getPlaybackCount()));
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_error_image_track);
            requestOptions.error(R.drawable.default_error_image_track);
            Glide.with(itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(track.getArtWorkUrl())
                    .into(mImageTrack);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemTrackClickListener.onItemClick(index, trackPosition);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_all_music, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Track track = mTracks.get(position);
        holder.bindView(track, mItemTrackClickListener, mIndex, position);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }
}
