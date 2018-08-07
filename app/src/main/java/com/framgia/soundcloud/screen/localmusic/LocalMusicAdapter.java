package com.framgia.soundcloud.screen.localmusic;

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

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> {
    private List<Track> mTracks;
    private ItemLocalMusicClickListener mItemLocalMusicClickListener;

    public LocalMusicAdapter(List<Track> tracks, ItemLocalMusicClickListener itemLocalMusicClickListener) {
        mTracks = tracks;
        mItemLocalMusicClickListener = itemLocalMusicClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitleItemLocalMusic;
        private TextView textArtistItemLocalMusic;
        private ImageView imageAtworkItemLocalMusic;
        private ImageButton imageVertItemLocalMusic;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitleItemLocalMusic = itemView.findViewById(R.id.text_title_item_local_music);
            textArtistItemLocalMusic = itemView.findViewById(R.id.text_artist_item_local_music);
            imageAtworkItemLocalMusic = itemView.findViewById(R.id.image_arwork_item_local_music);
            imageVertItemLocalMusic = itemView.findViewById(R.id.image_vert_item_local_music);
        }

        public void bindView(Track track, final int position,
                             final ItemLocalMusicClickListener itemLocalMusicClickListener) {
            textTitleItemLocalMusic.setText(track.getTitle());
            textArtistItemLocalMusic.setText(track.getArtist());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.default_error_image_track);
            requestOptions.error(R.drawable.default_error_image_track);
            Glide.with(itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(track.getArtWorkUrl())
                    .into(imageAtworkItemLocalMusic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemLocalMusicClickListener.onItemClick(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_local_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = mTracks.get(position);
        holder.bindView(track, position, mItemLocalMusicClickListener);
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public interface ItemLocalMusicClickListener {
        void onItemClick(int position);
    }
}
