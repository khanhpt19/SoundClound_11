package com.framgia.soundcloud.mediaplayer;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by quangnv on 02/08/2018
 */

public class MediaPlayerSetting {

    protected int mLoopType;
    protected int mShuffleType;

    @LoopType
    public int getLoopType() {
        return mLoopType;
    }

    public void setLoopType(@LoopType int loopType) {
        mLoopType = loopType;
    }

    @ShuffleType
    public int getShuffleType() {
        return mShuffleType;
    }

    public void setShuffleType(@LoopType int shuffleType) {
        mShuffleType = shuffleType;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LoopType.NONE, LoopType.ONE, LoopType.ALL})
    public @interface LoopType {
        int NONE = 0;
        int ONE = 1;
        int ALL = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ShuffleType.ON, ShuffleType.OFF})
    public @interface ShuffleType {
        int OFF = 0;
        int ON = 1;
    }
}
