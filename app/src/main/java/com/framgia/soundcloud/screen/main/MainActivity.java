package com.framgia.soundcloud.screen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.screen.detail.DetailActivity;
import com.framgia.soundcloud.screen.home.HomeFragment;
import com.framgia.soundcloud.service.ServiceManager;
import com.framgia.soundcloud.service.TrackService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UIPlayerListener.ControlListener,
        UIPlayerListener.DescriptionListener, View.OnClickListener, HomeFragment.PlayTrackListener {

    private LinearLayout mLinearLayoutPlayer;
    private TextView mTextViewTitle;
    private TextView mTextViewArtist;
    private ImageButton mButtonPlay;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonNext;
    private ImageView mImageViewTrack;

    private ServiceManager mServiceManager;
    private TrackService mTrackService;
    private Intent mIntent;
    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mTrackService = ((TrackService.TrackBinder) iBinder).getService();
            mTrackService.addControlListener(MainActivity.this);
            mTrackService.addDescriptionListener(MainActivity.this);
            if (mTrackService.getStatusMedia() != BaseMediaPlayer.StatusPlayerType.IDLE &&
                    mTrackService.getStatusMedia() != BaseMediaPlayer.StatusPlayerType.RELEASE) {
                mLinearLayoutPlayer.setVisibility(View.VISIBLE);
                mTrackService.updateAll();
            }
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(MainFragment.newInstance(), false);

        mIntent = new Intent(this, TrackService.class);
        mServiceManager = new ServiceManager(getApplicationContext(), mIntent, mConnection,
                Context.BIND_AUTO_CREATE);

        initViews();
    }

    @Override
    protected void onStart() {
        mServiceManager.bindService();
        mServiceManager.startService();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mTrackService.removeControlListener(MainActivity.this);
        mTrackService.removeDescriptionListener(MainActivity.this);
        mServiceManager.unbindService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mTrackService.getStatusMedia() != BaseMediaPlayer.StatusPlayerType.PLAYING) {
            mServiceManager.stopService();
        }
        super.onDestroy();
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
            case BaseMediaPlayer.StatusPlayerType.IDLE:
                mLinearLayoutPlayer.setVisibility(View.GONE);
                break;
            case BaseMediaPlayer.StatusPlayerType.PREPARING:
                mLinearLayoutPlayer.setVisibility(View.VISIBLE);
                break;
            case BaseMediaPlayer.StatusPlayerType.PLAYING:
                mButtonPlay.setImageResource(R.drawable.ic_pause_black_48dp);
                break;
            case BaseMediaPlayer.StatusPlayerType.PAUSE:
                mButtonPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp);
                break;
        }
    }

    @Override
    public void onTrackChanged(Track track) {
        Glide.with(getApplicationContext())
                .load(track.getArtWorkUrl())
                .into(mImageViewTrack);
        mTextViewTitle.setText(track.getTitle());
        mTextViewArtist.setText(track.getArtist());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                mTrackService.previous();
                break;
            case R.id.button_play_pause:
                mTrackService.changePlayPauseStatus();
                break;
            case R.id.button_next:
                mTrackService.next();
                break;
            case R.id.linear_mini_player:
                goToDetail();
                break;
        }
    }

    @Override
    public void play(List<Track> tracks, int trackPosition) {
        setTracks(tracks);
        playTrack(trackPosition);
    }

    public void replaceFragment(Fragment fragment, boolean isAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        if (isAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private void initViews() {
        mLinearLayoutPlayer = findViewById(R.id.linear_mini_player);
        mImageViewTrack = findViewById(R.id.image_track);
        mTextViewTitle = findViewById(R.id.text_track_title);
        mTextViewArtist = findViewById(R.id.text_track_artist);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonPlay = findViewById(R.id.button_play_pause);
        mButtonNext = findViewById(R.id.button_next);
        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mLinearLayoutPlayer.setOnClickListener(this);
    }

    private void setTracks(List<Track> tracks) {
        mTrackService.setTracks(tracks);
    }

    private void playTrack(int position) {
        mTrackService.play(position);
    }

    private void goToDetail() {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);
    }
}
