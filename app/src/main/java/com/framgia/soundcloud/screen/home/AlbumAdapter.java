package com.framgia.soundcloud.screen.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.soundcloud.ItemTrackClickListener;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Album;
import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> mAlbums;
    private ItemTrackClickListener mItemTrackClickListener;
    private MoreClickListener mMoreClickListener;

    public AlbumAdapter(List<Album> albums, ItemTrackClickListener itemTrackClickListener,
                        MoreClickListener moreClickListener) {
        mAlbums = albums;
        mItemTrackClickListener = itemTrackClickListener;
        mMoreClickListener = moreClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextTitle;
        private TextView mTextMore;
        private RecyclerView mRecyclerItemAlbum;
        private MoreClickListener mMoreClickListener;
        public ViewHolder(@NonNull View itemView,MoreClickListener moreClickListener) {
            super(itemView);
            mMoreClickListener = moreClickListener;
            mTextTitle = itemView.findViewById(R.id.text_item_all_album);
            mTextMore = itemView.findViewById(R.id.text_more);
            mRecyclerItemAlbum = itemView.findViewById(R.id.recycler_item_all_album);
        }

        public void bindView(Album album, int i, ItemTrackClickListener itemTrackClickListener) {
            mTextTitle.setText(album.getTitle());
            List<Track> tracks = album.getTracks();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            mRecyclerItemAlbum.setLayoutManager(linearLayoutManager);
            mRecyclerItemAlbum.setAdapter(new TrackAdapter(tracks, i, itemTrackClickListener));
            mTextMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMoreClickListener.onItemClick(getLayoutPosition());
        }
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_home, viewGroup,
                false);
        return new ViewHolder(v, mMoreClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder viewHolder, int i) {
        Album album = mAlbums.get(i);
        viewHolder.bindView(album, i, mItemTrackClickListener);
    }

    @Override
    public int getItemCount() {
        return mAlbums != null ? mAlbums.size() : 0;
    }

    public interface MoreClickListener {
        void onItemClick(int position);
    }
}
