package com.framgia.soundcloud.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.mediaplayer.MediaPlayerManager;
import com.framgia.soundcloud.mediaplayer.MediaPlayerSetting;
import com.framgia.soundcloud.screen.detail.DetailActivity;

import java.util.List;

/**
 * Created by quangnv on 02/08/2018
 */

public class TrackService extends Service implements UIPlayerListener.DescriptionListener,
        UIPlayerListener.ControlListener {
    private static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    private static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String ACTION_NEXT = "ACTION_NEXT";
    private static final String ACTION_CLOSE = "ACTION_CLOSE";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_CODE = 1000;
    private final IBinder mBinder = new TrackBinder();
    private MediaPlayerManager mMediaPlayerManager;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private NotificationTarget mNotificationTarget;
    private RemoteViews mRemoteViews;
    private Intent mIntentNotification;
    private Intent mIntentRemoteView;
    private PendingIntent mPendingIntentNotification;

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
        initNotification();
        addDescriptionListener(this);
        addControlListener(this);
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

    @Override
    public void onTrackChanged(Track track) {
        if (mNotificationTarget != null) {
            updateDescriptionNotification(track);
        }
    }

    @Override
    public void notifyShuffleChanged(int shuffleType) {

    }

    @Override
    public void notifyLoopChanged(int loopType) {

    }

    @Override
    public void notifyStateChanged(int statusType) {
        switch (statusType) {
            case BaseMediaPlayer.StatusPlayerType.PAUSE:
                updatePlayNotification();
                break;
            case BaseMediaPlayer.StatusPlayerType.PLAYING:
                updatePauseNotification();
                break;
        }
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
        createNotification();
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
        if (intent == null || intent.getAction() == null) {
            return;
        }
        switch (intent.getAction()) {
            case ACTION_PREVIOUS:
                previous();
                break;
            case ACTION_PLAY:
                changePlayPauseStatus();
                break;
            case ACTION_PAUSE:
                changePlayPauseStatus();
                break;
            case ACTION_NEXT:
                next();
                break;
            case ACTION_CLOSE:
                closeNotification();
                break;
        }
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mIntentRemoteView = new Intent(this, TrackService.class);
        mIntentNotification = new Intent(this, DetailActivity.class);
        mIntentNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntentNotification = PendingIntent.getActivity(this, REQUEST_CODE,
                mIntentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
    }

    private void createNotification() {
        setPreviousRemoteView(mIntentRemoteView);
        setPauseRemoteView(mIntentRemoteView);
        setNextRemoteView(mIntentRemoteView);
        setCloseRemoteView(mIntentRemoteView);
        mNotification = buildNotification();
        mNotificationTarget = buildNotificationTarget();
        updateDescriptionNotification(mMediaPlayerManager.getTrack());
    }

    private void setPreviousRemoteView(Intent intent) {
        intent.setAction(ACTION_PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button_previous, previousPendingIntent);
    }

    private void setPauseRemoteView(Intent intent) {
        intent.setAction(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button_play_pause, pausePendingIntent);
        mRemoteViews.setImageViewResource(R.id.button_play_pause, R.drawable.ic_pause_white_48dp);
    }

    private void setNextRemoteView(Intent intent) {
        intent.setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
    }

    private void setCloseRemoteView(Intent intent) {
        intent.setAction(ACTION_CLOSE);
        PendingIntent closePendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button_close, closePendingIntent);
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(mPendingIntentNotification)
                .setSmallIcon(R.drawable.default_error_image_track)
                .setContent(mRemoteViews)
                .build();
    }

    private NotificationTarget buildNotificationTarget() {
        return new NotificationTarget(this,R.id.image_track, mRemoteViews, mNotification,
                NOTIFICATION_ID);
    }

    private void updatePauseNotification() {
        mIntentRemoteView.setAction(ACTION_PAUSE);
        PendingIntent pendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                mIntentRemoteView, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setImageViewResource(R.id.button_play_pause, R.drawable.ic_pause_white_48dp);
        mRemoteViews.setOnClickPendingIntent(R.id.button_play_pause, pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    private void updatePlayNotification() {
        mIntentRemoteView.setAction(ACTION_PLAY);
        PendingIntent pendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                mIntentRemoteView, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setImageViewResource(R.id.button_play_pause,
                R.drawable.ic_play_arrow_white_48dp);
        mRemoteViews.setOnClickPendingIntent(R.id.button_play_pause, pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    private void updateDescriptionNotification(Track track) {
        mRemoteViews.setTextViewText(R.id.text_track_title, track.getTitle());
        mRemoteViews.setTextViewText(R.id.text_track_artist, track.getArtist());
        Glide.with(this)
                .asBitmap()
                .load(track.getArtWorkUrl())
                .into(mNotificationTarget);
        startForeground(NOTIFICATION_ID, mNotification);
    }

    private void closeNotification() {
        if (mMediaPlayerManager.getStatus() == BaseMediaPlayer.StatusPlayerType.PLAYING) {
            pause();
        }
        stopForeground(true);
    }
}
