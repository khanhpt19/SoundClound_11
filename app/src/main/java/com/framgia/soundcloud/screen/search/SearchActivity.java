package com.framgia.soundcloud.screen.search;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.R;
import com.framgia.soundcloud.UIPlayerListener;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;
import com.framgia.soundcloud.mediaplayer.BaseMediaPlayer;
import com.framgia.soundcloud.screen.detail.DetailActivity;
import com.framgia.soundcloud.service.ServiceManager;
import com.framgia.soundcloud.service.TrackService;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        SearchContract.View, SearchAdapter.ItemSearchClickListener, TextWatcher, ServiceConnection,
        UIPlayerListener.DescriptionListener, UIPlayerListener.ControlListener,
        View.OnClickListener {
    private static final String TAG = "textchange";
    private RecyclerView mRecyclerView;
    private SearchContract.Presenter mPresenter;
    private String mStringSearch;
    private EditText mTextSearch;
    private List<Track> mTracks;
    private int mStatusSearch = 0;
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
    private ServiceConnection mConnection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        registerListener();
        TrackRepository repository = TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(this),
                TrackRemoteDataSource.getInstance(this));
        mPresenter = new SearchPresenter(repository, this);

        mConnection = this;
        mIntent = new Intent(this, TrackService.class);
        mServiceManager = new ServiceManager(getApplicationContext(), mIntent, mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        mServiceManager.bindService();
        mServiceManager.startService();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mTrackService.removeControlListener(SearchActivity.this);
        mTrackService.removeDescriptionListener(SearchActivity.this);
        mServiceManager.unbindService();
        super.onStop();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mTrackService = ((TrackService.TrackBinder) iBinder).getService();
        mTrackService.addControlListener(SearchActivity.this);
        mTrackService.addDescriptionListener(SearchActivity.this);
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
    public void onTrackChanged(Track track) {
        Glide.with(getApplicationContext())
                .load(track.getArtWorkUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_error_image_track)
                        .error(R.drawable.default_error_image_track))
                .into(mImageViewTrack);
        mTextViewTitle.setText(track.getTitle());
        mTextViewArtist.setText(track.getArtist());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public void showListSearchOnline(List<Track> tracks) {
        SearchAdapter adapter = new SearchAdapter(tracks, this);
        mRecyclerView.setAdapter(adapter);
        mTracks = tracks;
    }

    @Override
    public void showListSearchLocal(List<Track> tracks) {
        SearchAdapter adapter = new SearchAdapter(tracks, this);
        mRecyclerView.setAdapter(adapter);
        mTracks = tracks;
    }

    @Override
    public void onItemClick(int position) {
        mTrackService.setTracks(mTracks);
        mTrackService.play(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_clear:
                mTextSearch.setText("");
                return true;
            case R.id.action_search_online:
                handleSearchOnline();
                return true;
            case R.id.action_search_local:
                handleSearchLocal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view_track);
        mTextSearch = findViewById(R.id.text_search);
        mLinearLayoutPlayer = findViewById(R.id.linear_mini_player);
        mImageViewTrack = findViewById(R.id.image_track);
        mTextViewTitle = findViewById(R.id.text_track_title);
        mTextViewArtist = findViewById(R.id.text_track_artist);
        mButtonPrevious = findViewById(R.id.button_previous);
        mButtonPlay = findViewById(R.id.button_play_pause);
        mButtonNext = findViewById(R.id.button_next);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        setSupportActionBar(toolbar);
    }

    private void registerListener() {
        mTextSearch.addTextChangedListener(this);
        mButtonPrevious.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mLinearLayoutPlayer.setOnClickListener(this);
    }

    private void handleSearchLocal() {
        mStatusSearch = 1;
        mTextSearch.setHint(getString(R.string.text_hint_local));
        if (mStringSearch != null) {
            mPresenter.searchTrackLocal(mStringSearch);
            hideKeyboard(this);
        }
    }

    private void handleSearchOnline() {
        mStatusSearch = 0;
        mTextSearch.setHint(getString(R.string.text_hint_online));
        if (mStringSearch != null) {
            mPresenter.searchTrackOnline(mStringSearch);
            hideKeyboard(this);
        }
    }

    private void handleSearch() {
        if (mStatusSearch == 0)
            mPresenter.searchTrackOnline(mStringSearch);
        else if (mStatusSearch == 1)
            mPresenter.searchTrackLocal(mStringSearch);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mStringSearch = charSequence.toString();
        if (mStringSearch != null && !mStringSearch.isEmpty())
            handleSearch();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void goToDetail() {
        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
        startActivity(intent);
    }
}
