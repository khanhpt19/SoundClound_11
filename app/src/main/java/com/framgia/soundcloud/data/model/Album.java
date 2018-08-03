package com.framgia.soundcloud.data.model;

import java.util.List;

public class Album {
    private String mTitle;
    private List<Track> mTracks;

    public Album(String title, List<Track> tracks) {
        mTitle = title;
        mTracks = tracks;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }
}
