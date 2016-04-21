package com.blogreader.niranjansa.materialheadstart.model;

/**
 * Created by niranjansa on 12/4/16.
 */
public class PlayList {
    private long id;
    private String title;

    PlayList() {}
    public PlayList(long id, String title) {
        this.id=id;
        this.title=title;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
