package com.blogreader.niranjansa.materialheadstart.model;

/**
 * Created by vintej on 16/4/16.
 */
public class Album {
    private long id;
    private String name;
    private int noOfSongs;

    Album() {}
    public Album(long id, String title,int no) {
        this.id=id;
        this.name=title;
        this.noOfSongs=no;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return name;
    }
    public int getNoOfSongs() {
        return noOfSongs;
    }


}
