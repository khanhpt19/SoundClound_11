package com.framgia.soundcloud.data.model;

import com.framgia.soundcloud.data.source.local.sqlite.DbHelper;
import com.framgia.soundcloud.data.source.remote.RequestConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by quangnv on 28/07/2018
 */

public class Track implements Serializable {
    private long mId;
    private String mTitle;
    private long mDuration;
    private String mArtWorkUrl;
    private boolean mStreamable;
    private String mStreamUrl;
    private boolean mDownloadable;
    private String mDownloadUrl;
    private String mGenre;
    private long mPlaybackCount;
    private String mDescription;
    private String mArtist;

    private Track(Builder builder) {
        mId = builder.mId;
        mTitle = builder.mTitle;
        mDuration = builder.mDuration;
        mArtWorkUrl = builder.mArtWorkUrl;
        mStreamable = builder.mStreamable;
        mStreamUrl = builder.mStreamUrl;
        mDownloadable = builder.mDownloadable;
        mDownloadUrl = builder.mDownloadUrl;
        mGenre = builder.mGenre;
        mPlaybackCount = builder.mPlaybackCount;
        mDescription = builder.mDescription;
        mArtist = builder.mArtist;
    }

    public static class Builder {
        private long mId;
        private String mTitle;
        private long mDuration;
        private String mArtWorkUrl;
        private boolean mStreamable;
        private String mStreamUrl;
        private boolean mDownloadable;
        private String mDownloadUrl;
        private String mGenre;
        private long mPlaybackCount;
        private String mDescription;
        private String mArtist;

        public Builder() {

        }

        public Track build() {
            return new Track(this);
        }

        public Builder setId(long id) {
            mId = id;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setDuration(long duration) {
            mDuration = duration;
            return this;
        }

        public Builder setArtWorkUrl(String artWorkUrl) {
            mArtWorkUrl = artWorkUrl;
            return this;
        }

        public Builder setStreamable(boolean streamable) {
            mStreamable = streamable;
            return this;
        }

        public Builder setStreamUrl(String streamUrl) {
            mStreamUrl = streamUrl;
            return this;
        }

        public Builder setDownloadable(boolean downloadable) {
            mDownloadable = downloadable;
            return this;
        }

        public Builder setDownloadUrl(String downloadUrl) {
            mDownloadUrl = downloadUrl;
            return this;
        }

        public Builder setGenre(String genre) {
            mGenre = genre;
            return this;
        }

        public Builder setPlaybackCount(long playbackCount) {
            mPlaybackCount = playbackCount;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder setArtist(String artist) {
            mArtist = artist;
            return this;
        }
    }

    public Track(String json) {
        try {
            JSONObject object = new JSONObject(json);
            mId = object.optLong(DbHelper.TrackEntry.COLUMN_NAME_ID, 0);
            mTitle = object.optString(DbHelper.TrackEntry.COLUMN_NAME_TITLE, "");
            mDuration = object.optLong(DbHelper.TrackEntry.COLUMN_NAME_DURATION, 0);
            mArtWorkUrl = object.optString(DbHelper.TrackEntry.COLUMN_NAME_ARTWORK_URL, "");
            mStreamable = object.optBoolean(
                    DbHelper.TrackEntry.COLUMN_NAME_STREAMABLE, false);
            mStreamUrl = RequestConfig.genUrlStreamTrack(mId);
            mDownloadable = object.optBoolean(
                    DbHelper.TrackEntry.COLUMN_NAME_DOWNLOADABLE, false);
            mDownloadUrl = object.optString(
                    DbHelper.TrackEntry.COLUMN_NAME_DOWNLOAD_URL, "");
            mGenre = object.optString(DbHelper.TrackEntry.COLUMN_NAME_GENRE, "");
            mPlaybackCount = object.optLong(
                    DbHelper.TrackEntry.COLUMN_NAME_PLAYBACK_COUNT, 0);
            mDescription = object.optString(DbHelper.TrackEntry.COLUMN_NAME_DESCRIPTION, "");
            String publisher = object.optString(RequestConfig.NAME_PUBLISHER_METADATA, "");
            if (!publisher.isEmpty()) {
                JSONObject publisherObject = new JSONObject(publisher);
                mArtist = publisherObject.optString(DbHelper.TrackEntry.COLUMN_NAME_ARTIST, "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getDuration() {
        return mDuration;
    }

    public String getArtWorkUrl() {
        return mArtWorkUrl;
    }

    public boolean isStreamable() {
        return mStreamable;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public boolean isDownloadable() {
        return mDownloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public String getGenre() {
        return mGenre;
    }

    public long getPlaybackCount() {
        return mPlaybackCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getArtist() {
        return mArtist;
    }
}
