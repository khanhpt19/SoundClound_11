package com.framgia.soundcloud.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class MediaPlayerManager extends MediaPlayerSetting implements BaseMediaPlayer,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private static MediaPlayerManager sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private List<Track> mTracks;
    private int mTrackCurrentPosition;
    private int mStatus;
    private List<UIPlayerListener.ControlListener> mControlListeners;
    private List<UIPlayerListener.TimerListener> mTimerListeners;
    private List<UIPlayerListener.DescriptionListener> mDescriptionListeners;

    private MediaPlayerManager(Context context) {
        super();
        mContext = context;
        mControlListeners = new ArrayList<>();
        mTimerListeners = new ArrayList<>();
        mDescriptionListeners = new ArrayList<>();
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MediaPlayerManager(context);
        }
        return sInstance;
    }

    @Override
    public void initMediaPlayer() {
        if (mMediaPlayer != null && getStatus() != StatusPlayerType.RELEASE) {
            mMediaPlayer.reset();
        } else {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (getLoopType()) {
            case MediaPlayerSetting.LoopType.NONE:
                next();
                break;
            case MediaPlayerSetting.LoopType.ONE:
                initMediaPlayer();
                initPlay(getTrackCurrentPosition());
                break;
            case MediaPlayerSetting.LoopType.ALL:
                if (getTracksSize() != 0 && getTracksSize() - 1
                        == getTrackCurrentPosition()) {
                    initMediaPlayer();
                    setTrackCurrentPosition(0);
                    initPlay(0);
                } else {
                    next();
                }
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        reset();
        return false;
    }

    @Override
    public void initPlay(int position) {
        if (!mTracks.isEmpty() && position >= 0) {
            Uri uri = Uri.parse(mTracks.get(position).getStreamUrl());
            try {
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepare();
                setStatus(StatusPlayerType.PREPARING);
                notifyTrackChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        mMediaPlayer.start();
        setStatus(StatusPlayerType.PLAYING);
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        setStatus(StatusPlayerType.PAUSE);
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
        setStatus(StatusPlayerType.IDLE);
    }

    @Override
    public void release() {
        mMediaPlayer.release();
        setStatus(StatusPlayerType.RELEASE);
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        setStatus(StatusPlayerType.STOP);
    }

    @Override
    public void next() {
        if (mTrackCurrentPosition < mTracks.size() - 1) {
            mTrackCurrentPosition++;
            initMediaPlayer();
            initPlay(mTrackCurrentPosition);
            start();
        }
    }

    @Override
    public void previous() {
        if (mTrackCurrentPosition > 0) {
            mTrackCurrentPosition--;
            initMediaPlayer();
            initPlay(mTrackCurrentPosition);
            start();
        }
    }

    @Override
    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
        notifyTimeChanged();
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void setLoopType(int loopType) {
        super.setLoopType(loopType);
        notifySettingChanged();
    }

    @Override
    public void setShuffleType(int shuffleType) {
        super.setShuffleType(shuffleType);
        notifySettingChanged();
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    public void setTrackCurrentPosition(int trackCurrentPosition) {
        mTrackCurrentPosition = trackCurrentPosition;
    }

    public int getTrackCurrentPosition() {
        return mTrackCurrentPosition;
    }

    public Track getTrack() {
        return mTracks.get(mTrackCurrentPosition);
    }

    public int getTracksSize() {
        return mTracks.size();
    }

    private void setStatus(int status) {
        mStatus = status;
        notifyStatusChanged(status);
        notifyTimeChanged();
    }

    private void notifyStatusChanged(int status) {
        for (UIPlayerListener.ControlListener controlListener: mControlListeners) {
            controlListener.notifyStateChanged(status);
        }
    }

    private void notifySettingChanged() {
        for (UIPlayerListener.ControlListener controlListener: mControlListeners) {
            controlListener.notifyLoopChanged(getLoopType());
            controlListener.notifyShuffleChanged(getShuffleType());
        }
    }

    private void notifyTrackChanged() {
        for (UIPlayerListener.DescriptionListener descriptionListener: mDescriptionListeners) {
            descriptionListener.onTrackChanged(getTrack());
        }
    }

    private void notifyTimeChanged() {
        for (UIPlayerListener.TimerListener timerListener: mTimerListeners) {
            timerListener.onCurrentTimeChanged(getCurrentPosition());
            timerListener.onTotalTimeChanged(getDuration());
        }
    }

    public void addControlListener(UIPlayerListener.ControlListener controlListener) {
        mControlListeners.add(controlListener);
    }

    public void addTimerListener(UIPlayerListener.TimerListener timerListener) {
        mTimerListeners.add(timerListener);
    }

    public void addDescriptionListener(UIPlayerListener.DescriptionListener descriptionListener) {
        mDescriptionListeners.add(descriptionListener);
    }

    public void removeControlListener(UIPlayerListener.ControlListener controlListener) {
        mControlListeners.remove(controlListener);
    }

    public void removeTimerListener(UIPlayerListener.TimerListener timerListener) {
        mTimerListeners.remove(timerListener);
    }

    public void removeDescriptionListener(UIPlayerListener.DescriptionListener descriptionListener) {
        mDescriptionListeners.remove(descriptionListener);
    }

    public void updateAllListener() {
        notifyTrackChanged();
        notifyStatusChanged(getStatus());
        notifyTimeChanged();
        notifySettingChanged();
    }

    public void updateTimer() {
        notifyTimeChanged();
    }
}
