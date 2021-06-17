package com.csy.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private int resourceId;

    public SongAdapter(Context context, int textViewResourceId, List<Song> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Song song = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView Order = (TextView) view.findViewById(R.id.Order);
        TextView Title = (TextView) view.findViewById(R.id.Title);
        TextView Artist = (TextView) view.findViewById(R.id.Artist);
        Order.setText(Integer.toString(position+1));
        Title.setText(song.getTitle());
        Artist.setText(song.getArtist());
        return view;
    }
}
