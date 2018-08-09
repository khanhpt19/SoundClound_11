package com.framgia.soundcloud.screen.search;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.TrackDataSource;

import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {
    private static final int ARGS_LIMIT = 20;
    private static final int ARGS_OFFSET = 0;
    private TrackRepository mRepository;
    private SearchContract.View mView;

    public SearchPresenter(TrackRepository trackRepository, SearchContract.View view) {
        mRepository = trackRepository;
        mView = view;
    }

    @Override
    public void searchTrackOnline(String textSearch) {
        mRepository.searchTracksByTitle(ARGS_LIMIT, ARGS_OFFSET, textSearch,
                new TrackDataSource.Callback<List<Track>>() {
            @Override
            public void onStartLoading() {

            }

            @Override
            public void onLoaded(List<Track> data) {
                mView.showListSearchOnline(data);
            }

            @Override
            public void onDataNotAvailable(Exception exception) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void searchTrackLocal(String textSearch) {
        mView.showListSearchLocal(mRepository.searchTracksByTitle(textSearch));
    }
}
