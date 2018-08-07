package com.framgia.soundcloud.screen.localmusic;

import com.framgia.soundcloud.data.repository.TrackRepository;

public class LocalMusicPresenter implements LocalMusicContract.Presenter {
    private TrackRepository mRepository;
    private LocalMusicContract.View mView;

    public LocalMusicPresenter(TrackRepository trackRepository, LocalMusicContract.View view) {
        mRepository = trackRepository;
        mView = view;
    }

    @Override
    public void loadMusic() {
        mView.showMusic(mRepository.getTracks());
    }
}
