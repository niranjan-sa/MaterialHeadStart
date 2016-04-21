package com.blogreader.niranjansa.materialheadstart.model;

/**
 * Created by vintej on 16/4/16.
 */
public class Artist {
    private long id;
    private String name;

    Artist() {}
    public Artist(long id, String title) {
        this.id=id;
        this.name=title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return name;
    }


}
