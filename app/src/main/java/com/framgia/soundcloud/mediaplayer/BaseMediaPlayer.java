package com.framgia.soundcloud.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by quangnv on 02/08/2018
 */

public interface BaseMediaPlayer {

    void initMediaPlayer();

    void initPlay(int position);

    void start();

    void pause();

    void reset();

    void release();

    void stop();

    void next();

    void previous();

    void seekTo(int msec);

    int getStatus();

    int getDuration();

    int getCurrentPosition();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({StatusPlayerType.IDLE, StatusPlayerType.PREPARING, StatusPlayerType.PLAYING,
            StatusPlayerType.PAUSE, StatusPlayerType.STOP})
    @interface StatusPlayerType {
        int IDLE = 0;
        int PREPARING = 1;
        int PLAYING = 2;
        int PAUSE = 3;
        int STOP = 4;
    }
}
