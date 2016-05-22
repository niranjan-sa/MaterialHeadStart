package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vintej on 22/5/16.
 */
public class DatabaseConnection extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SOCIOMUSIC";
    // Song Table Columns names
    private static final String TABLE_SONG = "MOSTPLAYEDSONG";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_COUNTER = "counter";


    private static final String[] COLUMNS = {KEY_TITLE,KEY_ALBUM,KEY_ARTIST,KEY_COUNTER};
    public DatabaseConnection(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_TABLE = "CREATE TABLE MOSTPLAYEDSONG ( title TEXT PRIMARY KEY , album TEXT, artist TEXT, counter INTEGER )";

        // create books table
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e)
        {

            Log.i("database", "failed" + e);
        }
        finally {
            db.close();
        }

    }

    //add song to database
    public void addSong(Song song){
        Log.d("addsong", song.toString());


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, song.getTitle()); // get title
        values.put(KEY_ALBUM, song.getAlbumName()); // get album
        values.put(KEY_ARTIST, song.getArtist());
        values.put(KEY_COUNTER, 0);
        try
        {
            db.insert(TABLE_SONG, null, values);

        }
        catch (SQLiteConstraintException e)
        {

        }
        finally {
            db.close();
        }
        updateCounter(song);


    }

    // used to increament counter of song
    public void updateCounter(Song song)
    {
        Log.i("database","updating");
        SQLiteDatabase db = this.getWritableDatabase();
        String s="UPDATE "+TABLE_SONG+" SET counter=counter+1 WHERE title='"+song.getTitle()+"'";
        try
        {
            db.execSQL(s);
            db.close();
        }
        catch (Exception e)
        {
            Log.i("database","Update failed"+e);
        }

    }

    //get all songs from database
    public List<Song> getAllSongs() {
        List<Song> songs = new LinkedList<Song>();
        String query = "SELECT  * FROM " + TABLE_SONG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Song song = null;
        if (cursor.moveToFirst()) {
            do {
                song = new Song();
                song.setTitle(cursor.getString(0));
                song.setAlbumName(cursor.getString(1));
                song.setArtist(cursor.getString(2));
                song.setCounter(Integer.parseInt(cursor.getString(3)));
                songs.add(song);
            } while (cursor.moveToNext());
        }
        return songs;
    }

    //get Most played songs from database
    public List<Song> getMostPlayedSongs() {
        List<Song> songs = new LinkedList<Song>();
        String query = "SELECT * FROM " +TABLE_SONG+ " ORDER BY counter DESC LIMIT 5";

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            Song song = null;
            if (cursor.moveToFirst()) {
                do {
                    song = new Song();
                    song.setTitle(cursor.getString(0));
                    song.setAlbumName(cursor.getString(1));
                    song.setArtist(cursor.getString(2));
                    song.setCounter(Integer.parseInt(cursor.getString(3)));
                    songs.add(song);
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
        return songs;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
