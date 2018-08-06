package com.framgia.soundcloud.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.framgia.soundcloud.data.model.Track;

import java.io.IOException;
import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class MediaPlayerManager extends MediaPlayerSetting implements BaseMediaPlayer {

    private static MediaPlayerManager sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;
    private int mCurrentPosition;
    private int status;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnErrorListener mOnErrorListener;

    private MediaPlayerManager(Context context,
                               MediaPlayer.OnPreparedListener onPreparedListener,
                               MediaPlayer.OnCompletionListener onCompletionListener,
                               MediaPlayer.OnErrorListener onErrorListener) {
        super();
        mContext = context;
        mOnPreparedListener = onPreparedListener;
        mOnCompletionListener = onCompletionListener;
        mOnErrorListener = onErrorListener;
    }

    public static MediaPlayerManager getInstance(Context context,
                                                  MediaPlayer.OnPreparedListener onPreparedListener,
                                                  MediaPlayer.OnCompletionListener onCompletionListener,
                                                  MediaPlayer.OnErrorListener onErrorListener) {
        if (sInstance == null) {
            sInstance = new MediaPlayerManager(context, onPreparedListener, onCompletionListener,
                    onErrorListener);
        }
        return sInstance;
    }

    @Override
    public void initMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        } else {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mMediaPlayer.setOnErrorListener(mOnErrorListener);
    }

    @Override
    public void initPlay(int position) {
        if (!mTracks.isEmpty() && position >= 0) {
            Uri uri = Uri.parse(mTracks.get(position).getStreamUrl());
            try {
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepare();
                status = StatusPlayerType.PREPARING;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        mMediaPlayer.start();
        status = StatusPlayerType.PLAYING;
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        status = StatusPlayerType.PAUSE;
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
        status = StatusPlayerType.IDLE;
    }

    @Override
    public void release() {
        mMediaPlayer.release();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        status = StatusPlayerType.STOP;
    }

    @Override
    public void next() {
        if (mCurrentPosition < mTracks.size() - 1) {
            mCurrentPosition++;
            initMediaPlayer();
            start();
        }
    }

    @Override
    public void previous() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
            initMediaPlayer();
            start();
        }
    }

    @Override
    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
    }

    public Track getTrack() {
        return mTracks.get(mCurrentPosition);
    }
}
