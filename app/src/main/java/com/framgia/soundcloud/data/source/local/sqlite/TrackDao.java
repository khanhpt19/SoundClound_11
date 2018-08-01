package com.framgia.soundcloud.data.source.local.sqlite;

import com.framgia.soundcloud.data.model.Track;

import java.util.List;

/**
 * Created by quangnv on 28/07/2018
 */

public interface TrackDao {

    List<Track> getTracks();

    Track getTrack(long trackId);

    List<Track> searchTrackByGenre(String genre);

    boolean insertTrack(Track track);

    boolean deleteTrack(Track track);

    boolean deleteAllTracks();

    List<Track> searchTrackByTitle(String trackTitle);
}
