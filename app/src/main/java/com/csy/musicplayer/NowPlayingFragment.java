package com.csy.musicplayer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;

import static com.csy.musicplayer.MainActivity.currentPlaying;
import static com.csy.musicplayer.MainActivity.mediaPlayer;
import static com.csy.musicplayer.MainActivity.songList;

public class NowPlayingFragment extends Fragment {

    public static final int UPDATE_TEXT = 1;

    public TextView totalProg;
    public TextView currentProg;
    public TextView songName;
    public TextView Artist;
    public TextView Album;
    public TextView playListProg;
    public SeekBar progressBar;
    public ImageView AlbumArt;

    public static Thread t;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TEXT:
                    int total = MainActivity.mediaPlayer.getDuration();
                    int current = MainActivity.mediaPlayer.getCurrentPosition();
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    String totaltime = sdf.format(total);
                    String currentTime = sdf.format(current);
                    Song song = songList.get(currentPlaying);
                    String currentPlayIndex = String.valueOf(currentPlaying+1);
                    String playListCount = String.valueOf(songList.size());

                    currentProg.setText(currentTime);
                    totalProg.setText(totaltime);
                    songName.setText(song.getTitle());
                    Artist.setText(song.getArtist());
                    Album.setText(song.getAlbum());
                    playListProg.setText(currentPlayIndex+" / "+playListCount);

                    progressBar.setMax(MainActivity.mediaPlayer.getDuration());
                    progressBar.setProgress(MainActivity.mediaPlayer.getCurrentPosition());

                    AlbumArt.setImageBitmap(getAlbumImage(song.getPath()));

                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.nowplaying,container,false);
        return view;
        //创建Fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        totalProg = (TextView) view.findViewById(R.id.totalProg);
        currentProg = (TextView) view.findViewById(R.id.currentProg);
        progressBar = (SeekBar) view.findViewById(R.id.progressBar);
        songName = (TextView) view.findViewById(R.id.songName);
        Artist = (TextView) view.findViewById(R.id.ArtistName);
        Album = (TextView) view.findViewById(R.id.Album);
        playListProg = (TextView) view.findViewById(R.id.playListProg);
        AlbumArt = (ImageView) view.findViewById(R.id.albumArt);
        t = new Thread(){
            public void run(){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                        handler.postDelayed(this, 1000);
                    }
                });
            }
        };

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                String progressTime = sdf.format(progress);
                currentProg.setText(progressTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
            }
        });


    }

    private Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }


}
