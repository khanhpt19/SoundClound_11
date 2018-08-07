package com.framgia.soundcloud;

import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.mediaplayer.MediaPlayerSetting;

/**
 * Created by quangnv on 02/08/2018
 */

public interface UIPlayerListener {

    interface ControlListener {

        void notifyShuffleChanged(@MediaPlayerSetting.ShuffleType int shuffleType);

        void notifyLoopChanged(@MediaPlayerSetting.LoopType int loopType);

        void notifyStateChanged(@BaseMediaPlayer.StatusPlayerType int statusType);
    }

    interface TimerListener {

        void onCurrentTimeChanged(int currentTime);

        void onTotalTimeChanged(int totalTime);
    }

    interface DescriptionListener {

        void onTrackChanged(Track track);
    }
}
