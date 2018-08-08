package com.framgia.soundcloud.screen.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Track> mTracks;
    private ItemSearchClickListener mItemSearchClickListener;

    public SearchAdapter(List<Track> tracks, ItemSearchClickListener itemSearchClickListener) {
        mTracks = tracks;
        mItemSearchClickListener = itemSearchClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitleItemLocalMusic;
        private TextView textArtistItemLocalMusic;
        private ImageView mImageAtworkItemLocalMusic;
        private ImageButton mImageVertItemLocalMusic;
        private ItemSearchClickListener mItemSearchClickListener;
        public ViewHolder(View itemView, ItemSearchClickListener itemSearchClickListener) {
            super(itemView);
            mItemSearchClickListener = itemSearchClickListener;
            textTitleItemLocalMusic = itemView.findViewById(R.id.text_title_item_local_music);
            textArtistItemLocalMusic = itemView.findViewById(R.id.text_artist_item_local_music);
            mImageAtworkItemLocalMusic = itemView.findViewById(R.id.image_arwork_item_local_music);
            mImageVertItemLocalMusic = itemView.findViewById(R.id.image_vert_item_local_music);
        }

        public void bindView(Track track,
                             final ItemSearchClickListener itemSearchClickListener) {
            textTitleItemLocalMusic.setText(track.getTitle());
            textArtistItemLocalMusic.setText(track.getArtist());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_error_image_track);
            requestOptions.error(R.drawable.default_error_image_track);
            Glide.with(itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(track.getArtWorkUrl())
                    .into(mImageAtworkItemLocalMusic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemSearchClickListener.onItemClick(getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_local_music, parent, false);
        return new ViewHolder(view, mItemSearchClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mTracks.get(position);
        holder.bindView(track, mItemSearchClickListener);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }


    public interface ItemSearchClickListener {
        void onItemClick(int position);
    }
}
