package com.csy.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.animation.Positioning;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PlayListFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    String filepath = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.playlist, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContentResolver mResolver = getActivity().getContentResolver();

        getNativeAudio(mResolver, view);
    }

    public void getNativeAudio(ContentResolver mResolver,View view) {   //获取本地歌曲
        Cursor cursor = mResolver.query(    //通过MediaStore获取音乐文件信息
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int counter = cursor.getCount();

        List<Song> songList = new ArrayList<>();
        ListView listView = (ListView) view.findViewById(R.id.PlayListView);

        for(int i = 0; i<counter;i++){
            //获取歌曲标题
            String Title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            //String path = title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //获取歌手
            String Artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            //获取文件路径
            String Path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //构造Listview
            Song song = new Song(Title,Artist,Path);
            songList.add(song);
            cursor.moveToNext();
        }
        SongAdapter adapter = new SongAdapter(
                getActivity(), R.layout.song_item, songList);
        listView.setAdapter(adapter);
        cursor.close();
        selectMusic(songList, view);
    }

    public void selectMusic(List<Song> songList, View view){
        /*
            选择音乐，返回音乐文件路径，可供播放
         */
        ListView listView = (ListView) view.findViewById(R.id.PlayListView);
        MainActivity mainActivity = new MainActivity();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = songList.get(i);
                mainActivity.initMediaPlayer(song);
            }
        });
    }
}

