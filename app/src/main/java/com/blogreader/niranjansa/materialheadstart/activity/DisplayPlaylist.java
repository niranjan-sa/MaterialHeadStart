package com.blogreader.niranjansa.materialheadstart.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.DisplayPlaylistAdapter;
import com.blogreader.niranjansa.materialheadstart.model.MusicService;
import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.ArrayList;

public class DisplayPlaylist extends AppCompatActivity {

    private ArrayList<Song> songsInPlaylist;
    private ListView songView;
    boolean playbackPaused = true;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

   private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songsInPlaylist);
            musicBound = true;

            Toast.makeText(DisplayPlaylist.this, "ok set", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            musicBound = false;

            Toast.makeText(DisplayPlaylist.this, "Probe",Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_playlist);
        int a = getIntent().getIntExtra("id", 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        songsInPlaylist = new ArrayList<Song>();
        getSongsInPlaylist(a);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String playlistTitle=getIntent().getStringExtra("playlistTitle");
        getSupportActionBar().setTitle("" + playlistTitle);
        songView = (ListView) findViewById(R.id.songPlaylistView);
        DisplayPlaylistAdapter songAdt = new DisplayPlaylistAdapter(this, songsInPlaylist);
        songView.setAdapter(songAdt);

    }

    public void getSongsInPlaylist(int playlistID) {
        String[] projection = {
                MediaStore.Audio.Playlists.Members._ID,
                MediaStore.Audio.Playlists.Members.TITLE,
                MediaStore.Audio.Playlists.Members.ARTIST,
                MediaStore.Audio.Playlists.Members.ALBUM_ID,
                MediaStore.Audio.Playlists.Members.ALBUM

        };
        int songs = 0, i = 0;
        Cursor musicCursor = null;
        try {
            Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistID);
            musicCursor = getContentResolver().query(uri, projection, null, null, null);
            if (musicCursor != null && musicCursor.moveToFirst()) {

                int titleColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Playlists.Members.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Playlists.Members._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Playlists.Members.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Playlists.Members.ALBUM_ID);
                int albumIdColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Playlists.Members.ALBUM);


                //retriving the flags to avoid showing the ring tones and other system files


                //add songs to list from the internal memory
                do {

                    long thisId = musicCursor.getLong(idColumn);

                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String album = musicCursor.getString(albumColumn);
                    long albumId = musicCursor.getLong(albumIdColumn);

                    Toast.makeText(this, "Total songs queried :- " + songs + " Int - " + i + " Ext :- " + (songs - i), Toast.LENGTH_LONG).show();
                    songsInPlaylist.add(new Song(thisId, thisTitle, thisArtist, albumId, album));
                    songs++;
                    i++;
                }
                while (musicCursor.moveToNext());
            }
            Toast.makeText(this, "Total songs queried :- " + songs + " Int - " + i + " Ext :- " + (songs - i), Toast.LENGTH_LONG).show();


        } catch (Exception e) {
            Log.e("Media", e.toString());
        } finally {
            if (musicCursor != null) {
                musicCursor.close();
            }
        }

    }


    public void songPicked(View view){
        Toast.makeText(this,""+view.getTag().toString(),Toast.LENGTH_LONG).show();
        musicSrv.setSongByName(((TextView) view.findViewById(R.id.song_title)).getText().toString());
        musicSrv.playSong();
        if(playbackPaused){
            playbackPaused=false;
        }
            /*Starting new Activity*/
        Intent intent=new Intent(this, SongPlayer.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, com.blogreader.niranjansa.materialheadstart.model.MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        }

}

