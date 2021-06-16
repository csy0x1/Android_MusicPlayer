package com.csy.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager2);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1,false);    //默认界面为第二页(中间页)
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 1) {   //第二页可响应返回按钮，关闭应用
            super.onBackPressed();
        }
        else if (viewPager.getCurrentItem() == 2){  //第三页响应返回按钮，返回第二页
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
        else {  //第一页响应返回按钮，返回第二页
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

}