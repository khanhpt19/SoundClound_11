package com.framgia.soundcloud.screen.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.soundcloud.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailActivity extends Activity {
    private ImageButton mButtonPlay, mButtonPrevious, mButtonNext,
            mButonShuffle, mButtonLoop, mButtonDownload, mButtonFavorite;
    private ImageView mImageBackground, mImageArtwork;
    private SeekBar mSeekBar;
    private TextView mTextProgress, mTextMax;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar_detail);
        mButonShuffle = findViewById(R.id.button_shuffle_detail);
        mButtonDownload = findViewById(R.id.button_download_detail);
        mButtonPlay = findViewById(R.id.button_play_detail);
        mButtonPrevious = findViewById(R.id.button_previous_detail);
        mButtonNext = findViewById(R.id.button_next_detail);
        mButtonLoop = findViewById(R.id.button_loop_detail);
        mButtonFavorite = findViewById(R.id.button_favorite_detail);
        mImageArtwork = findViewById(R.id.image_artwork_detail);
        mImageBackground = findViewById(R.id.image_artwork_background_detail);
        mTextProgress = findViewById(R.id.text_progress_detail);
        mTextMax = findViewById(R.id.text_max_detail);
        mSeekBar = findViewById(R.id.seekbar_detail);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
    }

}
