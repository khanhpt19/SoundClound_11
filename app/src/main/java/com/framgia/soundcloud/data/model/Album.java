package com.framgia.soundcloud.data.model;

import java.util.List;

public class Album {
    private String mTitle;
    private List<Track> mTracks;
    private String mType;

    public Album(String title, List<Track> tracks, String type) {
        mTitle = title;
        mTracks = tracks;
        mType = type;
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
