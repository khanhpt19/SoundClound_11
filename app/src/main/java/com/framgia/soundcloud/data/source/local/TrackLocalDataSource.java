package com.framgia.soundcloud.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;
import com.framgia.soundcloud.data.source.local.sqlite.DbHelper;
import com.framgia.soundcloud.data.source.local.sqlite.TrackDao;
import com.framgia.soundcloud.data.source.local.sqlite.TrackDaoImpl;

import java.util.List;

/**
 * Created by quangnv on 28/07/2018
 */

public class TrackLocalDataSource implements TrackDataSource.LocalDataSource {

    private static TrackLocalDataSource sInstance;
    private TrackDao mTrackDao;

    public TrackLocalDataSource(TrackDao trackDao) {
        mTrackDao = trackDao;
    }

    public static synchronized TrackLocalDataSource getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource(
                    TrackDaoImpl.getInstance(DbHelper.getInstance(context)));
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    @Override
    public boolean insertTrack(Track track) {
        return mTrackDao.insertTrack(track);
    }

    @Override
    public boolean deleteTrack(Track track) {
        return mTrackDao.deleteTrack(track);
    }

    @Override
    public boolean deleteAllTracks() {
        return mTrackDao.deleteAllTracks();
    }


    @Override
    public List<Track> getTracks() {
        return mTrackDao.getTracks();
    }

    @Override
    public List<Track> searchTracksByTitle(String title) {
        return mTrackDao.searchTrackByTitle(title);
    }

    @Override
    public List<Track> searchTracksByGenre(String genre) {
        return mTrackDao.searchTrackByGenre(genre);
    }

    @Override
    public Track getTrack(long trackId) {
        return mTrackDao.getTrack(trackId);
    }
}
