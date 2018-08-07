package com.framgia.soundcloud.screen.localmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;

import java.util.List;

public class LocalMusicFragment extends Fragment implements LocalMusicContract.View,
        LocalMusicAdapter.ItemLocalMusicClickListener {
    private static final String TAG = "mymusicfragment";
    private RecyclerView mRecyclerView;
    private LocalMusicContract.Presenter mPresenter;

    public static Fragment newInstance(){
        LocalMusicFragment myMusicFragment = new LocalMusicFragment();
        return myMusicFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_local_music);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        TrackRepository repository = TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(getActivity()),
                TrackRemoteDataSource.getInstance(getActivity()));
        mPresenter = new LocalMusicPresenter(repository, this);
        mPresenter.loadMusic();
    }

    private void updateView(List<Track> tracks) {
        LocalMusicAdapter adapter = new LocalMusicAdapter(tracks, this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showMusic(List<Track> tracks) {
        updateView(tracks);
    }

    @Override
    public void onItemClick(int position) {

    }
}
