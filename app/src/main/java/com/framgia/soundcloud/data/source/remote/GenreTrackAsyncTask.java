package com.framgia.soundcloud.data.source.remote;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.source.TrackDataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class GenreTrackAsyncTask extends BaseAsyncTask<List<Track>> {

    public GenreTrackAsyncTask(TrackDataSource.Callback<List<Track>> callback) {
        super(callback);
    }

    @Override
    protected List<Track> genData(String response) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            JSONArray array =
                    new JSONArray(object.optString(RequestConfig.NAME_COLLECTION, ""));
            for (int i = 0; i < array.length(); i++) {
                JSONObject collectionObject = array.getJSONObject(i);
                tracks.add(
                        new Track(collectionObject.optString(RequestConfig.NAME_TRACK, "")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mException = e;
        }
        return tracks;
    }
}
