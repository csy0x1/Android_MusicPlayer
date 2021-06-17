package com.csy.musicplayer;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends FragmentActivity {

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    String filepath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));

    public static MediaPlayer mediaPlayer = new MediaPlayer();  //java中的全局变量是由public修饰的static成员变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager2);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1,false);    //默认界面为第二页(中间页)

        requestForPermission();

        MediaScanner mediaScanner = new MediaScanner(this);
        File file = new File(filepath);
        mediaScanner.scanFile(file,"");

        Button start = (Button) findViewById(R.id.start);
        Button pause = (Button) findViewById(R.id.pause);
        Button stop = (Button) findViewById(R.id.stop);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        });
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

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    //初始化MediaPlayer
    public void initMediaPlayer(Song song) {
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());  //指定音频文件的路径
            mediaPlayer.prepare();  //让MediaPlayer进入到准备状态
            mediaPlayer.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}