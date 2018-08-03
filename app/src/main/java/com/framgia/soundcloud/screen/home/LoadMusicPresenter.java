package com.framgia.soundcloud.screen.home;

import android.os.Handler;
import android.util.Log;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.TrackDataSource;
import com.framgia.soundcloud.data.source.remote.RequestConfig;

import java.util.List;

public class LoadMusicPresenter implements LoadMusicContract.Presenter {
    private static final int ARGS_LIMIT = 20;
    private static final int ARGS_OFFSET = 0;
    private LoadMusicContract.View mView;
    private TrackRepository mRepository;

    public LoadMusicPresenter(TrackRepository repository, LoadMusicContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void start() {
        // TODO: 03/08/2018 start loading
    }

    @Override
    public void stop() {
        // TODO: 03/08/2018 stop loading
    }

    @Override
    public void loadMusic() {
        mRepository.getTracks(RequestConfig.ALBUM_ALL_MUSIC, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        mView.startLoadingMusic();
                    }

                    @Override
                    public void onLoaded(List<Track> data) {
                        mView.showMusic(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        mView.loadMusicSuccess();
                    }
                });
    }

    @Override
    public void loadAudio() {
        mRepository.getTracks(RequestConfig.ALBUM_ALL_AUDIO, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onLoaded(final List<Track> data) {
                        mView.showAudio(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        // TODO: 02/08/2018
                    }
                });
    }

    @Override
    public void loadAlternativerock() {
        mRepository.getTracks(RequestConfig.ALBUM_ALTERNATIVEROCK, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onLoaded(List<Track> data) {
                        mView.showAlternativerock(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        // TODO: 02/08/2018
                    }
                });
    }

    @Override
    public void loadAmbient() {
        mRepository.getTracks(RequestConfig.ALBUM_AMBIENT, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onLoaded(List<Track> data) {
                        mView.showAmbient(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        // TODO: 02/08/2018
                    }
                });
    }

    @Override
    public void loadClassical() {
        mRepository.getTracks(RequestConfig.ALBUM_CLASSICAL, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onLoaded(List<Track> data) {
                        mView.showClassical(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        // TODO: 02/08/2018
                    }
                });
    }

    @Override
    public void loadCountry() {
        mRepository.getTracks(RequestConfig.ALBUM_COUNTRY, ARGS_LIMIT, ARGS_OFFSET,
                new TrackDataSource.Callback<List<Track>>() {
                    @Override
                    public void onStartLoading() {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onLoaded(List<Track> data) {
                        mView.showCountry(data);
                    }

                    @Override
                    public void onDataNotAvailable(Exception exception) {
                        // TODO: 02/08/2018
                    }

                    @Override
                    public void onComplete() {
                        // TODO: 03/08/2018
                    }
                });
    }

}
