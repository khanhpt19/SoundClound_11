package com.framgia.soundcloud.data.repository;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;

import java.util.List;

/**
 * Created by quangnv on 31/07/2018
 */

public class TrackRepository implements TrackDataSource.LocalDataSource,
        TrackDataSource.RemoteDataSource {

    private static TrackRepository sInstance;
    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    private TrackRepository(LocalDataSource localDataSource,
                            RemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;
        mRemoteDataSource = remoteDataSource;
    }

    public static synchronized TrackRepository getInstance(LocalDataSource localDataSource,
                                                           RemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new TrackRepository(localDataSource, remoteDataSource);
        }
        return sInstance;
    }

    @Override
    public List<Track> getTracks() {
        return mLocalDataSource.getTracks();
    }

    @Override
    public List<Track> searchTracksByTitle( String title) {
        return mLocalDataSource.searchTracksByTitle(title);
    }

    @Override
    public List<Track> searchTracksByGenre(String genre) {
        return mLocalDataSource.searchTracksByGenre(genre);
    }

    @Override
    public Track getTrack(long trackId) {
        return mLocalDataSource.getTrack(trackId);
    }

    @Override
    public boolean insertTrack(Track track) {
        return mLocalDataSource.insertTrack(track);
    }

    @Override
    public boolean deleteTrack(Track track) {
        return mLocalDataSource.deleteTrack(track);
    }

    @Override
    public boolean deleteAllTracks() {
        return mLocalDataSource.deleteAllTracks();
    }

    @Override
    public void getTracks(String album, int limit, int offset, Callback<List<Track>> callback) {
        mRemoteDataSource.getTracks(album, limit, offset, callback);
    }

    @Override
    public void searchTracksByTitle(int limit, int offset, String title,
                                    Callback<List<Track>> callback) {
        mRemoteDataSource.searchTracksByTitle(limit, offset, title, callback);
    }

    @Override
    public void getTrack(long trackId, Callback<Track> callback) {
        mRemoteDataSource.getTrack(trackId, callback);
    }
}
