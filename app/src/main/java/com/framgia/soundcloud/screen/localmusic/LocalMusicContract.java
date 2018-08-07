package com.framgia.soundcloud.screen.localmusic;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public interface LocalMusicContract {
    interface Presenter {
        void loadMusic();
    }

    interface View {
        void showMusic(List<Track> tracks);
    }
}
