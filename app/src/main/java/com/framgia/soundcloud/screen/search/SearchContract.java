package com.framgia.soundcloud.screen.search;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public interface SearchContract {
    interface Presenter {
        void searchTrackOnline(String textSearch);
        void searchTrackLocal(String textSearch);

    }

    interface View {
        void showListSearchOnline(List<Track> tracks);
        void showListSearchLocal(List<Track> tracks);
    }
}
