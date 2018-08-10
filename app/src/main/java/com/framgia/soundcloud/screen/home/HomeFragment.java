package com.framgia.soundcloud.screen.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.soundcloud.ItemTrackClickListener;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Album;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.RequestConfig;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;
import com.framgia.soundcloud.screen.tracks.TracksActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LoadMusicContract.View,
        ItemTrackClickListener, AlbumAdapter.MoreClickListener {
    public static final String EXTRA_TITLE = "com.framgia.soundcloud.EXTRA_TITLE";
    public static final String EXTRA_TRACKS = "com.framgia.soundcloud.EXTRA_TRACKS";
    public static final String EXTRA_TYPE = "com.framgia.soundcloud.EXTRA_TYPE";
    private LoadMusicContract.Presenter mMusicPresenter;
    private RecyclerView mRecyclerMain;
    private List<Album> mAlbums = new ArrayList<>();
    private PlayTrackListener mPlayTrackListener;

    public static Fragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof PlayTrackListener) {
            mPlayTrackListener = (PlayTrackListener) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerMain = view.findViewById(R.id.recycler_album);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerMain.setLayoutManager(layoutManager);
        loadAllAlbum();
    }

    @Override
    public void onDestroyView() {
        mPlayTrackListener = null;
        super.onDestroyView();
    }

    private void loadAllAlbum() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(getActivity()),
                TrackRemoteDataSource.getInstance(getActivity()));
        mMusicPresenter = new LoadMusicPresenter(repository, this);
        mMusicPresenter.start();
        mMusicPresenter.loadMusic();
        mMusicPresenter.loadAudio();
        mMusicPresenter.loadAlternativerock();
        mMusicPresenter.loadAmbient();
        mMusicPresenter.loadClassical();
        mMusicPresenter.loadCountry();
    }

    private void updateRecyclerView(String title, List<Track> tracks, String type) {
        mAlbums.add(new Album(title, tracks, type));
        AlbumAdapter albumAdapter = new AlbumAdapter(mAlbums,
                this, this);
        mRecyclerMain.setAdapter(albumAdapter);
    }

    @Override
    public void showMusic(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_all_music), tracks,
                RequestConfig.ALBUM_ALL_MUSIC);
    }

    @Override
    public void showAudio(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_all_audio), tracks,
                RequestConfig.ALBUM_ALL_AUDIO);
    }

    @Override
    public void showAlternativerock(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_alternativerock), tracks, RequestConfig.ALBUM_ALTERNATIVEROCK);
    }

    @Override
    public void showAmbient(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_ambient), tracks, RequestConfig.ALBUM_AMBIENT);
    }

    @Override
    public void showClassical(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_classical), tracks, RequestConfig.ALBUM_CLASSICAL);
    }

    @Override
    public void showCountry(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_country), tracks, RequestConfig.ALBUM_COUNTRY);
    }

    @Override
    public void startLoadingMusic() {

    }

    @Override
    public void loadMusicSuccess() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicPresenter.stop();
    }

    @Override
    public void onItemClick(int index, int trackPosition) {
        mPlayTrackListener.play(mAlbums.get(index).getTracks(), trackPosition);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = getTrackIntent(getContext(), mAlbums, position);
        startActivity(i);
    }

    public static Intent getTrackIntent(Context context,List<Album> albums, int position ){
        Intent intent = new Intent(context, TracksActivity.class);
        intent.putExtra(EXTRA_TITLE, albums.get(position).getTitle());
        intent.putExtra(EXTRA_TYPE, albums.get(position).getType());
        intent.putExtra(EXTRA_TRACKS, (Serializable) albums.get(position).getTracks());
        return intent;
    }

    public interface PlayTrackListener {

        void play(List<Track> tracks, int trackPosition);
    }
}
