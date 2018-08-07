package com.framgia.soundcloud.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.soundcloud.R;
import com.framgia.soundcloud.screen.home.HomeFragment;
import com.framgia.soundcloud.screen.localmusic.LocalMusicFragment;

public class MainFragment extends Fragment {

    public static Fragment newInstance(){
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
        mainViewPagerAdapter.addFragment(LocalMusicFragment.newInstance(), getString(R.string.title_my_music));
        viewpager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }
}
