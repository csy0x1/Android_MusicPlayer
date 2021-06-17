package com.csy.musicplayer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(FragmentActivity fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment playList = new PlayListFragment();
        Fragment nowPlaying = new NowPlayingFragment();
        Fragment Setting = new SettingFragment();
        Bundle args = new Bundle();
        args.putInt(PlayListFragment.ARG_OBJECT,position+1);
        playList.setArguments(args);
        switch (position){  //切换不同的fragment
            case 0: return playList;
            default:
            case 1: return nowPlaying;
            case 2: return Setting;

        }
    }

    @Override
    public int getItemCount() {
        return 3;   //定义页数
    }
}
