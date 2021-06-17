package com.csy.musicplayer;

public class Song {
    private String Title;
    private String Artist;
    private String Path;

    public Song(String Title,String Artist,String Path){
        this.Title = Title;
        this.Artist = Artist;
        this.Path = Path;
    }


    public String getTitle(){
        return Title;
    }

    public String getArtist(){
        return Artist;
    }

    public String getPath(){
        return Path;
    }
}

