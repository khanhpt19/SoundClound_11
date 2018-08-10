package com.framgia.soundcloud.data.source.remote;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;
import com.framgia.soundcloud.util.StringUtil;

/**
 * Created by quangnv on 29/07/2018
 */

public class TrackAsyncTask extends BaseAsyncTask<Track> {

    public TrackAsyncTask(TrackDataSource.Callback<Track> callback) {
        super(callback);
    }

    @Override
    protected Track genData(String response) {
        if (StringUtil.isEmpty(response)) {
            return null;
        }
        return new Track(response);
    }
}
