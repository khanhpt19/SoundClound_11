package com.framgia.soundcloud.screen.home;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

public interface LoadMusicContract {
    interface Presenter {
        void start();
        void stop();
        void loadMusic();
        void loadAudio();
        void loadAlternativerock();
        void loadAmbient();
        void loadClassical();
        void loadCountry();
    }

    interface View {
        void showMusic(List<Track> tracks);
        void showAudio(List<Track> tracks);
        void showAlternativerock(List<Track> tracks);
        void showAmbient(List<Track> tracks);
        void showClassical(List<Track> tracks);
        void showCountry(List<Track> tracks);
        void startLoadingMusic();
        void loadMusicSuccess();
    }
}
