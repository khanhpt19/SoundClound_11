package com.framgia.soundcloud.screen.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.soundcloud.ItemTrackClickListener;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Album;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LoadMusicContract.View, ItemTrackClickListener {
    private static final String TAG = "home";
    private LoadMusicContract.Presenter mMusicPresenter;
    private RecyclerView mRecyclerMain;
    private List<Album> mAlbums = new ArrayList<>();

    public static Fragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
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

    private void updateRecyclerView(String title, List<Track> tracks) {
        mAlbums.add(new Album(title, tracks));
        AlbumAdapter albumAdapter = new AlbumAdapter(mAlbums, this);
        mRecyclerMain.setAdapter(albumAdapter);
    }

    @Override
    public void showMusic(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_all_music), tracks);
    }

    @Override
    public void showAudio(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_all_audio), tracks);
    }

    @Override
    public void showAlternativerock(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_alternativerock), tracks);
    }

    @Override
    public void showAmbient(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_ambient), tracks);
    }

    @Override
    public void showClassical(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_classical), tracks);
    }

    @Override
    public void showCountry(List<Track> tracks) {
        updateRecyclerView(getString(R.string.title_country), tracks);
    }

    @Override
    public void startLoadingMusic() {
        Log.d(TAG, getString(R.string.text_log_start));
    }

    @Override
    public void loadMusicSuccess() {
        Log.d(TAG, getString(R.string.text_log_success));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicPresenter.stop();
    }

    @Override
    public void onItemClick(int index, int trackPosition) {
        Toast.makeText(getActivity(), ""+index, Toast.LENGTH_SHORT).show();
        Log.d(TAG, mAlbums.get(index).getTracks().size() + "");
        Log.d(TAG, trackPosition + "");
    }
}
