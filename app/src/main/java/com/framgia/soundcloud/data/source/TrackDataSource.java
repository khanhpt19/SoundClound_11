package com.framgia.soundcloud.data.source;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

/**
 * Created by quangnv on 28/07/2018
 */

public interface TrackDataSource {

    interface Callback<T> {

        void onStartLoading();

        void onLoaded(T data);

        void onDataNotAvailable(Exception exception);

        void onComplete();
    }

    interface LocalDataSource extends TrackDataSource {

        List<Track> getTracks();

        List<Track> searchTracksByTitle(String title);

        List<Track> searchTracksByGenre(String genre);

        Track getTrack(long trackId);

        boolean insertTrack(Track track);

        boolean deleteTrack(Track track);

        boolean deleteAllTracks();
    }

    interface RemoteDataSource extends TrackDataSource {

        void getTracks(String album, int limit, int offset, Callback<List<Track>> callback);

        void searchTracksByTitle(int limit, int offset, String title,
                                 Callback<List<Track>> callback);

        void getTrack(long trackId, Callback<Track> callback);
    }
}
