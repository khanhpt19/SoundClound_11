package com.framgia.soundcloud.screen.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.soundcloud.R;
import com.framgia.soundcloud.screen.home.HomeFragment;
import com.framgia.soundcloud.screen.localmusic.LocalMusicFragment;
import com.framgia.soundcloud.screen.search.SearchActivity;

public class MainFragment extends Fragment {
    private Toolbar mToolbar;

    public static Fragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewpager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        MainViewPagerAdapter mainViewPagerAdapter =
                new MainViewPagerAdapter(getChildFragmentManager());
        mainViewPagerAdapter.addFragment(HomeFragment.newInstance(), getString(R.string.title_home));
        mainViewPagerAdapter.addFragment(LocalMusicFragment.newInstance(),
                getString(R.string.title_my_music));
        viewpager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        mToolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return false;
    }
}
