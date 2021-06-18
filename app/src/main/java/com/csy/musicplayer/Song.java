package com.csy.musicplayer;

public class Song {
    private String Title;
    private String Artist;
    private String Path;
    private String Album;

    public Song(String Title,String Artist,String Path,String Album){
        this.Title = Title;
        this.Artist = Artist;
        this.Path = Path;
        this.Album = Album;
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

    public String getAlbum(){
        return Album;
    }
}

