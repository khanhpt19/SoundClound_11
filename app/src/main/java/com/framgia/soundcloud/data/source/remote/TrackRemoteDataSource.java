package com.framgia.soundcloud.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;

import java.util.List;

/**
 * Created by quangnv on 29/07/2018
 */

public class TrackRemoteDataSource implements TrackDataSource.RemoteDataSource {

    private static TrackRemoteDataSource sInstance;

    public static synchronized TrackRemoteDataSource getInstance(@NonNull Context context) {
        if ((sInstance == null)) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getTracks(String album, int limit, int offset,
                          final Callback<List<Track>> callback) {
        new GenreTrackAsyncTask(new Callback<List<Track>>() {
            @Override
            public void onStartLoading() {
                callback.onStartLoading();
            }

            @Override
            public void onLoaded(List<Track> tracks) {
                callback.onLoaded(tracks);
            }

            @Override
            public void onDataNotAvailable(Exception exception) {
                callback.onDataNotAvailable(exception);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        }).execute(RequestConfig.genUrlGetTracksByAlbum(album, limit, offset));
    }

    @Override
    public void searchTracksByTitle(int limit, int offset,
                                    String title, final Callback<List<Track>> callback) {
        new SearchTrackAsyncTask(new Callback<List<Track>>() {
            @Override
            public void onStartLoading() {
                callback.onStartLoading();
            }

            @Override
            public void onLoaded(List<Track> tracks) {
                callback.onLoaded(tracks);
            }

            @Override
            public void onDataNotAvailable(Exception exception) {
                callback.onDataNotAvailable(exception);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        }).execute(RequestConfig.genUrlSearchTrack(limit, offset, title));
    }

    @Override
    public void getTrack(long trackId, final Callback<Track> callback) {
        new TrackAsyncTask(new Callback<Track>() {
            @Override
            public void onStartLoading() {
                callback.onStartLoading();
            }

            @Override
            public void onLoaded(Track track) {
                callback.onLoaded(track);
            }

            @Override
            public void onDataNotAvailable(Exception exception) {
                callback.onDataNotAvailable(exception);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        }).execute(RequestConfig.genUrlGetTrack(trackId));
    }
}
