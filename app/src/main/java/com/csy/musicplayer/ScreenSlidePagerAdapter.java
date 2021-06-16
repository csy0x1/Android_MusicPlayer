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
        Fragment fragment = new ScreenFragment();
        Fragment fragment1 = new Fragment2();
        Bundle args = new Bundle();
        args.putInt(ScreenFragment.ARG_OBJECT,position+1);
        fragment.setArguments(args);
        switch (position){  //切换不同的fragment
            case 1: return fragment1;
            case 0:
            case 2:
            default: return fragment;

        }
    }

    @Override
    public int getItemCount() {
        return 3;   //定义页数
    }
}
