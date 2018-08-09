package com.framgia.soundcloud.screen.tracks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.screen.detail.DetailActivity;
import com.framgia.soundcloud.screen.home.HomeFragment;
import com.framgia.soundcloud.service.ServiceManager;
import com.framgia.soundcloud.service.TrackService;

import java.util.List;

public class TracksActivity extends AppCompatActivity implements
        TracksContract.View, TracksAdapter.TrackItemClickListener, ServiceConnection,
        UIPlayerListener.DescriptionListener, UIPlayerListener.ControlListener,
        View.OnClickListener {
    private static final int OFFSET_PAGE = 20;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private RecyclerViewScrollListener mRecyclerViewScrollListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private TracksPresenter mPresenter;
    private TracksAdapter mTracksAdapter;
    private List<Track> mTracks;
    private String mAlbumType;
    private LinearLayout mLinearLayoutPlayer;
    private TextView mTextViewTitle;
    private TextView mTextViewArtist;
    private ImageButton mButtonPlay;
    private ImageButton mButtonPrevious;
    private ImageButton mButtonNext;
    private ImageView mImageViewTrack;
    private ServiceManager mServiceManager;
    private TrackService mTrackService;
    private boolean mBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);
        initView();
        initData();
        showData();
        setOnScrollRecyclerView();
        connectService();
        registerListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceManager.bindService();
        mServiceManager.startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTrackService.removeControlListener(TracksActivity.this);
        mTrackService.removeDescriptionListener(TracksActivity.this);
        mServiceManager.unbindService();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartLoading() {
        mTracksAdapter.addLoadingView();
    }

    @Override
    public void showMusic(List<Track> tracks) {
        mTracksAdapter.removeLoadingView();
        mTracksAdapter.addData(tracks);
    }

    @Override
    public void onDismissLoading() {
        mRecyclerViewScrollListener.resetStateLoading();
    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onOptionClickListener(int position) {
        mTrackService.setTracks(mTracks);
        mTrackService.play(position);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mTrackService = ((TrackService.TrackBinder) iBinder).getService();
        mTrackService.addControlListener(TracksActivity.this);
        mTrackService.addDescriptionListener(TracksActivity.this);
        if (mTrackService.getStatusMedia() != BaseMediaPlayer.StatusPlayerType.IDLE) {
            mTrackService.updateAll();
            mLinearLayoutPlayer.setVisibility(View.VISIBLE);
        }
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBound = false;
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

    private void connectService() {
        ServiceConnection connection = this;
        Intent intent = new Intent(this, TrackService.class);
        mServiceManager = new ServiceManager(getApplicationContext(), intent, connection,
                Context.BIND_AUTO_CREATE);
    }

    private void initData() {
        String title = getIntent().getStringExtra(HomeFragment.EXTRA_TITLE);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        mTracks = (List<Track>) getIntent().getSerializableExtra(HomeFragment.EXTRA_TRACKS);
        mAlbumType = getIntent().getStringExtra(HomeFragment.EXTRA_TYPE);
        TrackRepository repository = TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(this),
                TrackRemoteDataSource.getInstance(this));
        mPresenter = new TracksPresenter(repository, this);
    }

    private void showData() {
        mTracksAdapter = new TracksAdapter(this, mTracks, this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mTracksAdapter);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler_view_more);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        mLinearLayoutPlayer = findViewById(R.id.linear_mini_player);
        mImageViewTrack = findViewById(R.id.image_track);
        mTextViewTitle = findViewById(R.id.text_track_title);
        mTextViewArtist = findViewById(R.id.text_track_artist);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonPlay = findViewById(R.id.button_play_pause);
        mButtonNext = findViewById(R.id.button_next);
    }

    private void setOnScrollRecyclerView() {
        mRecyclerViewScrollListener = new RecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                mPresenter.loadMusic(mAlbumType, page * OFFSET_PAGE);
            }
        };
        mRecyclerView.addOnScrollListener(mRecyclerViewScrollListener);
    }

    private void registerListener() {
        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mLinearLayoutPlayer.setOnClickListener(this);
    }

    private void goToDetail() {
        Intent intent = new Intent(TracksActivity.this, DetailActivity.class);
        startActivity(intent);
    }
}
