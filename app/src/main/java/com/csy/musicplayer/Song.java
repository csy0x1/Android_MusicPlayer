package com.csy.musicplayer;

public class Song {
    private String Title;
    private String Artist;

    public Song(String Title,String Artist){
        this.Title = Title;
        this.Artist = Artist;
    }


    public String getTitle(){
        return Title;
    }

    public String getArtist(){
        return Artist;
    }
}

