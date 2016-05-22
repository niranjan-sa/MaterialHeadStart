package com.blogreader.niranjansa.materialheadstart.model;

/**
 * Created by niranjansa on 18/3/16.
 *
 * This class represents a single sound track!!
 * This will represent a song.
 */
public class Song {

    private long id;
    private String title;
    private String artist;
    private long albumId;
    private String albumName;
    private int counter;






    //Constructors
    public Song() {}
    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }
    public Song(long songID, String songTitle, String songArtist, long alb_id, String album) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        albumId=alb_id;
        albumName=album;
    }
    //get methods
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public long getAlbumId() {
        return albumId;
    }
    public String getAlbumName() {
        return albumName;
    }

    public int getCounter() {
        return counter;
    }

    //setter methods
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
       String s=title+albumName+artist+counter;
        return s;
    }
}
