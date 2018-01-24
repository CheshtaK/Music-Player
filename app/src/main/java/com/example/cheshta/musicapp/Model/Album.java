package com.example.cheshta.musicapp.Model;

/**
 * Created by chesh on 1/1/2018.
 */

public class Album {
    private String name;
    private int noOfSongs;
    private int thumbnail;

    public Album(String name, int noOfSongs, int thumbnail) {
        this.name = name;
        this.noOfSongs = noOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfSongs() {
        return noOfSongs;
    }

    public void setNoOfSongs(int noOfSongs) {
        this.noOfSongs = noOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}
