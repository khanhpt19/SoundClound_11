package com.framgia.soundcloud.data.source.remote;

import com.framgia.soundcloud.BuildConfig;

/**
 * Created by quangnv on 29/07/2018
 */

public final class RequestConfig {

    public static final String BASE_URL_ALBUM = "https://api-v2.soundcloud.com/charts?kind=top&genre=";
    public static final String BASE_URL_TRACK = "http://api.soundcloud.com/tracks";
    public static final String ALBUM_ALL_AUDIO = "soundcloud%3Agenres%3Aall%2Daudio";
    public static final String ALBUM_ALL_MUSIC = "soundcloud%3Agenres%3Aall%2Dmusic";
    public static final String ALBUM_ALTERNATIVEROCK = "soundcloud%3Agenres%3Aalternativerock";
    public static final String ALBUM_AMBIENT = "soundcloud%3Agenres%3Aambient";
    public static final String ALBUM_CLASSICAL = "soundcloud%3Agenres%3Aclassical";
    public static final String ALBUM_COUNTRY = "soundcloud%3Agenres%3Acountry";
    public static final String ARGS_LIMIT = "&limit=";
    public static final String ARGS_OFFSET = "&offset=";
    public static final String ARGS_CLIENT_ID = "client_id=";
    public static final String ARGS_SEARCH = "&q=";
    public static final String NAME_COLLECTION = "collection";
    public static final String NAME_TRACK = "track";
    public static final String NAME_PUBLISHER_METADATA = "publisher_metadata";
    public static final String NAME_STREAM = "stream";

    public static String genUrlGetTracksByAlbum(String album, int limit, int offset) {
        return BASE_URL_ALBUM + album + "&" + ARGS_CLIENT_ID + BuildConfig.API_KEY
                + ARGS_LIMIT + limit + ARGS_OFFSET + offset;
    }

    public static String genUrlGetTrack(long id) {
        return BASE_URL_TRACK + "/" + id + "?" + ARGS_CLIENT_ID + BuildConfig.API_KEY;
    }

    public static String genUrlSearchTrack(int limit, int offset, String title) {
        return BASE_URL_TRACK + "?" + ARGS_CLIENT_ID + BuildConfig.API_KEY + ARGS_SEARCH + title;
    }

    public static String genUrlStreamTrack(long id) {
        return BASE_URL_TRACK + "/" + id + "/" + RequestConfig.NAME_STREAM + "?" + ARGS_CLIENT_ID +
                BuildConfig.API_KEY;
    }

    public static final class HttpRequestMethod {
        public static final String GET = "GET";
    }
}
