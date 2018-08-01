package com.framgia.soundcloud.data.source.remote;

import android.os.AsyncTask;

import com.framgia.soundcloud.data.source.TrackDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by quangnv on 02/08/2018
 */

public abstract class BaseAsyncTask<T> extends AsyncTask<String, String, T> {

    protected Exception mException;
    private TrackDataSource.Callback<T> mCallback;

    public BaseAsyncTask(TrackDataSource.Callback<T> callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        mCallback.onStartLoading();
    }

    @Override
    protected T doInBackground(String... strings) {
        String urlRequest =strings[0];
        String response = request(urlRequest);
        return genData(response);
    }

    @Override
    protected void onPostExecute(T data) {
        if (mCallback == null) {
            return;
        }
        if (mException != null) {
            mCallback.onDataNotAvailable(mException);
        } else {
            mCallback.onLoaded(data);
            mCallback.onComplete();
        }
    }

    protected abstract T genData(String response);

    private String request(String urlRequest) {
        String result = null;
        try {
            URL url = new URL(urlRequest);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(RequestConfig.HttpRequestMethod.GET);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            result = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            mException = e;
            mCallback.onDataNotAvailable(mException);
        }
        return result;
    }
}
