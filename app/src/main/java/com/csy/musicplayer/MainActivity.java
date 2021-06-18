package com.csy.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.csy.musicplayer.NowPlayingFragment.t;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    String filepath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));

    public static MediaPlayer mediaPlayer = new MediaPlayer();  //java中的全局变量是由public修饰的static成员变量

    public static List<Song> songList = new ArrayList<>();

    public static int currentPlaying = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager2);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1,false);    //默认界面为第二页(中间页)
        viewPager.setOffscreenPageLimit(3);

        requestForPermission(); //请求权限

        MediaScanner mediaScanner = new MediaScanner(this);
        File file = new File(filepath);
        mediaScanner.scanFile(file,"");

        Button playPause = (Button) findViewById(R.id.PlayPause);
        Button Previous = (Button) findViewById(R.id.Previous);
        Button Next = (Button) findViewById(R.id.Next);


        //播放暂停
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                                Song song = songList.get(currentPlaying);
                                initMediaPlayer(song);
                                playPause.setBackgroundResource(R.drawable.ic_pause_circle);
                                return true;
                            }
                        });
                    mediaPlayer.start();
                    playPause.setBackgroundResource(R.drawable.ic_pause_circle);
                }
                else{
                    mediaPlayer.pause();
                    playPause.setBackgroundResource(R.drawable.ic_play_circle);
                }
            }
        });

        //上一首
        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song;
                int nextPlay = 0;
                if(currentPlaying-1<0){
                    nextPlay = songList.size()-1;
                }
                else{
                    nextPlay = currentPlaying-1;
                }
                song = songList.get(nextPlay);
                initMediaPlayer(song);
                playPause.setBackgroundResource(R.drawable.ic_pause_circle);

            }
        });


        //下一首
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song;
                int nextPlay = 0;
                if(currentPlaying+1>songList.size()-1){
                    nextPlay = 0;
                }
                else{
                    nextPlay = currentPlaying+1;
                }
                song = songList.get(nextPlay);
                initMediaPlayer(song);
                playPause.setBackgroundResource(R.drawable.ic_pause_circle);

            }
        });

        //连播，播放完一首歌曲后自动播放下一首
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Song song;
                int nextPlay = 0;
                if(currentPlaying+1> songList.size()-1){
                    song = songList.get(nextPlay);
                }
                else{
                    nextPlay = currentPlaying+1;
                    song = songList.get(nextPlay);
                }
                initMediaPlayer(song);
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
            currentPlaying = songList.indexOf(song);
            mediaPlayer.start();
            if(!t.isAlive()){
                t.start();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}