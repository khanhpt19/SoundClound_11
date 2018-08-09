package com.framgia.soundcloud.screen.tracks;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public interface TracksContract {
    interface Presenter {
        void loadMusic(String type, int offset);
    }

    interface View {
        void onStartLoading();
        void showMusic(List<Track> tracks);
        void onDismissLoading();
        void onError(Exception exception);
    }
}
