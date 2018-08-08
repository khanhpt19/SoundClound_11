package com.framgia.soundcloud.screen.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.framgia.soundcloud.R;
import com.framgia.soundcloud.data.model.Track;
import com.framgia.soundcloud.data.repository.TrackRepository;
import com.framgia.soundcloud.data.source.local.TrackLocalDataSource;
import com.framgia.soundcloud.data.source.remote.TrackRemoteDataSource;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements
        SearchContract.View, SearchAdapter.ItemSearchClickListener, TextWatcher {
    private static final String TAG = "textchange";
    private RecyclerView mRecyclerView;
    private SearchContract.Presenter mPresenter;
    private String mStringSearch;
    private EditText mTextSearch;
    private List<Track> mTracks;
    private int mStatusSearch = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view_track);
        mTextSearch = findViewById(R.id.text_search);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        setSupportActionBar(toolbar);
        TrackRepository repository = TrackRepository.getInstance(
                TrackLocalDataSource.getInstance(this),
                TrackRemoteDataSource.getInstance(this));
        mPresenter = new SearchPresenter(repository, this);
        mTextSearch.addTextChangedListener(this);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                if (mStringSearch != null)
                    handleSearch();
                hideKeyboard(this);
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
        if (mStringSearch != null)
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

}
