package com.framgia.soundcloud.screen.tracks;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.TrackDataSource;

import java.util.List;

public class TracksPresenter implements TracksContract.Presenter {
    private static final int ARGS_LIMIT = 20;
    private TrackRepository mRepository;
    private TracksContract.View mView;

    public TracksPresenter(TrackRepository trackRepository, TracksContract.View view) {
        mRepository = trackRepository;
        mView = view;
    }

    @Override
    public void loadMusic(String type, int offset) {
        mRepository.getTracks(type, ARGS_LIMIT, offset, new TrackDataSource.Callback<List<Track>>() {
            @Override
            public void onStartLoading() {
                mView.onStartLoading();
            }

            @Override
            public void onLoaded(List<Track> data) {
                mView.showMusic(data);

            }

            @Override
            public void onDataNotAvailable(Exception exception) {
                mView.onError(exception);
            }

            @Override
            public void onComplete() {
                mView.onDismissLoading();
            }
        });
    }
}
