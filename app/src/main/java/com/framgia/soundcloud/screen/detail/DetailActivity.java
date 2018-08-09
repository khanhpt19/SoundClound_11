package com.framgia.soundcloud.screen.detail;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.mediaplayer.MediaPlayerSetting;
import com.framgia.soundcloud.service.ServiceManager;
import com.framgia.soundcloud.service.TrackService;
import com.framgia.soundcloud.util.TimeUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailActivity extends AppCompatActivity implements UIPlayerListener.ControlListener,
        UIPlayerListener.TimerListener, UIPlayerListener.DescriptionListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, ServiceConnection {
    private static final int RADIUS_BLUR = 20;
    private static final int SAMPLING_BLUR = 10;
    /**
     * The time to update track's current time
     */
    private static final int TIME_DELAY = 500;

    private ImageButton mButtonPlay;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonNext;
    private ImageButton mButtonShuffle;
    private ImageButton mButtonLoop;
    private ImageButton mButtonDownload;
    private ImageButton mButtonFavorite;
    private ImageView mImageBackground;
    private ImageView mImageArtwork;
    private TextView mTextCurrentTime;
    private TextView mTextTotalTime;
    private SeekBar mSeekBar;
    private Toolbar mToolbar;

    private ServiceManager mServiceManager;
    private TrackService mTrackService;
    private Intent mIntent;
    private boolean mIsBound;
    private boolean mSeekBarTracking;
    private Handler mHandler;
    private Runnable mRunnableTimer;
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mConnection = this;
        mIntent = new Intent(this, TrackService.class);
        mServiceManager = new ServiceManager(getApplicationContext(), mIntent, mConnection,
                Context.BIND_AUTO_CREATE);
        initView();
        registerListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceManager.bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mServiceManager.unbindService();
        mTrackService.removeControlListener(DetailActivity.this);
        mTrackService.removeTimerListener(DetailActivity.this);
        mTrackService.removeDescriptionListener(DetailActivity.this);
        mHandler.removeCallbacks(mRunnableTimer);
        mIsBound = false;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mTrackService = ((TrackService.TrackBinder) iBinder).getService();
        mTrackService.addControlListener(DetailActivity.this);
        mTrackService.addTimerListener(DetailActivity.this);
        mTrackService.addDescriptionListener(DetailActivity.this);
        if (mTrackService.getStatusMedia() != BaseMediaPlayer.StatusPlayerType.IDLE) {
            mTrackService.updateAll();
            updateTimer();
        }
        mIsBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIsBound = false;
    }

    @Override
    public void notifyShuffleChanged(int shuffleType) {
        switch (shuffleType) {
            case MediaPlayerSetting.ShuffleType.ON:
                mButtonShuffle.setImageResource(R.drawable.ic_shuffle_clicked_24dp);
                break;
            case MediaPlayerSetting.ShuffleType.OFF:
                mButtonShuffle.setImageResource(R.drawable.ic_shuffle_none_24dp);
                break;
        }
    }

    @Override
    public void notifyLoopChanged(int loopType) {
        switch (loopType) {
            case MediaPlayerSetting.LoopType.NONE:
                mButtonLoop.setImageResource(R.drawable.ic_loop_none_24dp);
                break;
            case MediaPlayerSetting.LoopType.ONE:
                mButtonLoop.setImageResource(R.drawable.ic_loop_one_24dp);
                break;
            case MediaPlayerSetting.LoopType.ALL:
                mButtonLoop.setImageResource(R.drawable.ic_loop_all_24dp);
                break;
        }
    }

    @Override
    public void notifyStateChanged(int statusType) {
        switch (statusType) {
            case BaseMediaPlayer.StatusPlayerType.PLAYING:
                mButtonPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case BaseMediaPlayer.StatusPlayerType.PAUSE:
                mButtonPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                break;
        }
    }

    @Override
    public void onCurrentTimeChanged(int currentTime) {
        mTextCurrentTime.setText(TimeUtil.convertMilisecondToFormatTime(currentTime));
        if (!mSeekBarTracking) {
            mSeekBar.setProgress(currentTime);
        }
    }

    @Override
    public void onTotalTimeChanged(int totalTime) {
        mTextTotalTime.setText(TimeUtil.convertMilisecondToFormatTime(totalTime));
        mSeekBar.setMax(totalTime);
    }

    @Override
    public void onTrackChanged(Track track) {
        mToolbar.setTitle(track.getTitle());
        mToolbar.setSubtitle(track.getArtist());
        Glide.with(this)
                .load(track.getArtWorkUrl())
                .apply(new RequestOptions().placeholder(R.drawable.default_error_image_track)
                .error(R.drawable.default_error_image_track)
                .circleCrop())
                .into(mImageArtwork);
        Glide.with(this)
                .load(track.getArtWorkUrl())
                .apply(RequestOptions.bitmapTransform(
                        new BlurTransformation(RADIUS_BLUR, SAMPLING_BLUR))
                .placeholder(R.drawable.default_error_image_track)
                .error(R.drawable.default_error_image_track))
                .into(mImageBackground);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_shuffle:
                setShuffle();
                break;
            case R.id.button_previous:
                mTrackService.previous();
                break;
            case R.id.button_play_pause:
                mTrackService.changePlayPauseStatus();
                break;
            case R.id.button_next:
                mTrackService.next();
                break;
            case R.id.button_loop:
                setLoop();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mSeekBarTracking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mSeekBarTracking = false;
        mTrackService.seekTo(seekBar.getProgress());
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mButtonShuffle = findViewById(R.id.button_shuffle);
        mButtonDownload = findViewById(R.id.button_download_detail);
        mButtonPlay = findViewById(R.id.button_play_pause);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonNext = findViewById(R.id.button_next);
        mButtonLoop = findViewById(R.id.button_loop);
        mButtonFavorite = findViewById(R.id.button_favorite_detail);
        mImageArtwork = findViewById(R.id.image_artwork_detail);
        mImageBackground = findViewById(R.id.image_artwork_background_detail);
        mTextCurrentTime = findViewById(R.id.text_progress_detail);
        mTextTotalTime = findViewById(R.id.text_max_detail);
        mSeekBar = findViewById(R.id.seekbar_detail);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        setSupportActionBar(mToolbar);
    }

    private void registerListener() {
        mButtonShuffle.setOnClickListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mButtonLoop.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShuffle() {
        switch (mTrackService.getShuffle()) {
            case MediaPlayerSetting.ShuffleType.OFF:
                mTrackService.setShuffle(MediaPlayerSetting.ShuffleType.ON);
                break;
            case MediaPlayerSetting.ShuffleType.ON:
                mTrackService.setShuffle(MediaPlayerSetting.ShuffleType.OFF);
                break;
        }
    }

    private void setLoop() {
        switch (mTrackService.getLoop()) {
            case MediaPlayerSetting.LoopType.NONE:
                mTrackService.setLoop(MediaPlayerSetting.LoopType.ONE);
                break;
            case MediaPlayerSetting.LoopType.ONE:
                mTrackService.setLoop(MediaPlayerSetting.LoopType.ALL);
                break;
            case MediaPlayerSetting.LoopType.ALL:
                mTrackService.setLoop(MediaPlayerSetting.LoopType.NONE);
                break;
        }
    }

    private void updateTimer() {
        mHandler = new Handler();
        mRunnableTimer = new Runnable() {
            @Override
            public void run() {
                mTrackService.updateTimer();
                mHandler.postDelayed(this, TIME_DELAY);
            }
        };
        mHandler.post(mRunnableTimer);
    }
}
