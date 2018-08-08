package com.framgia.soundcloud.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.mediaplayer.MediaPlayerManager;
import com.framgia.soundcloud.mediaplayer.MediaPlayerSetting;

import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class TrackService extends Service {

    private final IBinder mBinder = new TrackBinder();
    private MediaPlayerManager mMediaPlayerManager;

    public class TrackBinder extends Binder {
        public TrackService getService() {
            return TrackService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = MediaPlayerManager.getInstance(this);
        mMediaPlayerManager.initMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayerManager.getStatus() == BaseMediaPlayer.StatusPlayerType.PLAYING) {
            mMediaPlayerManager.stop();
        }
        mMediaPlayerManager.release();
    }

    public void addControlListener(UIPlayerListener.ControlListener controlListener) {
        mMediaPlayerManager.addControlListener(controlListener);
    }

    public void addTimerListener(UIPlayerListener.TimerListener timerListener) {
        mMediaPlayerManager.addTimerListener(timerListener);
    }

    public void addDescriptionListener(UIPlayerListener.DescriptionListener descriptionListener) {
        mMediaPlayerManager.addDescriptionListener(descriptionListener);
    }

    public void removeControlListener(UIPlayerListener.ControlListener controlListener) {
        mMediaPlayerManager.removeControlListener(controlListener);
    }

    public void removeTimerListener(UIPlayerListener.TimerListener timerListener) {
        mMediaPlayerManager.removeTimerListener(timerListener);
    }

    public void removeDescriptionListener(UIPlayerListener.DescriptionListener descriptionListener) {
        mMediaPlayerManager.removeDescriptionListener(descriptionListener);
    }

    public void setTracks(List<Track> tracks) {
        mMediaPlayerManager.setTracks(tracks);
    }

    public void play(int position) {
        mMediaPlayerManager.setTrackCurrentPosition(position);
        mMediaPlayerManager.initMediaPlayer();
        mMediaPlayerManager.initPlay(position);
    }

    public void start() {
        mMediaPlayerManager.start();
    }

    public void pause() {
        mMediaPlayerManager.pause();
    }

    public void next() {
        mMediaPlayerManager.next();
    }

    public void previous() {
        mMediaPlayerManager.previous();
    }

    public void seekTo(int msec) {
        mMediaPlayerManager.seekTo(msec);
    }

    public void setShuffle(@MediaPlayerSetting.ShuffleType int shuffleType) {
        mMediaPlayerManager.setShuffleType(shuffleType);
    }

    @BaseMediaPlayer.StatusPlayerType
    public int getShuffle() {
        return mMediaPlayerManager.getShuffleType();
    }

    public void setLoop(@MediaPlayerSetting.LoopType int loopType) {
        mMediaPlayerManager.setLoopType(loopType);
    }

    @MediaPlayerSetting.LoopType
    public int getLoop() {
        return mMediaPlayerManager.getLoopType();
    }

    public void changePlayPauseStatus() {
        if (getStatusMedia() == BaseMediaPlayer.StatusPlayerType.PAUSE) {
            start();
        } else {
            pause();
        }
    }

    public int getStatusMedia() {
        return mMediaPlayerManager.getStatus();
    }

    public void updateAll() {
        mMediaPlayerManager.updateAllListener();
    }

    public void updateTimer() {
        mMediaPlayerManager.updateTimer();
    }

    private void handleIntent(Intent intent) {

    }
}
