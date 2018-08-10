package com.framgia.soundcloud.data.source.remote;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;
import com.framgia.soundcloud.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class SearchTrackAsyncTask extends BaseAsyncTask<List<Track>> {

    public SearchTrackAsyncTask(TrackDataSource.Callback<List<Track>> callback) {
        super(callback);
    }

    @Override
    protected List<Track> genData(String response) {
        List<Track> tracks = new ArrayList<>();
        if (StringUtil.isEmpty(response)) {
            return tracks;
        }
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                tracks.add(new Track(array.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mException = e;
        }
        return tracks;
    }
}
