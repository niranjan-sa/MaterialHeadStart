/**
 Separate activity to display songs of choosen artist.

 */


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
import com.blogreader.niranjansa.materialheadstart.adapter.ArtistSongAdapter;
import com.blogreader.niranjansa.materialheadstart.model.MusicService;
import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.ArrayList;

public class ArtistSongListActivity extends AppCompatActivity {

    private ArrayList<Song> artistSongList;
    private ListView songView;
    private Intent playIntent;
    private MusicService musicSrv;
    private boolean musicBound=false;
    //
    private boolean paused=false, playbackPaused=false;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            //musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_artist_song_list);
        int a=getIntent().getIntExtra("id",0);
        artistSongList=new ArrayList<Song>();
        getArtistSongs(a);
        setContentView(R.layout.activity_artist_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String artistTitle=getIntent().getStringExtra("artistTitle");  ///getArtistTitle(a);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("" + artistTitle);
        songView = (ListView)findViewById(R.id.artistSongListView);
        ArtistSongAdapter songAdt = new ArtistSongAdapter(this,artistSongList);
       songView.setAdapter(songAdt);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    /*
    * fetch all songs using artistid*/
    public void getArtistSongs(int artistId)
    {
        String selection = "is_music != 0";

        if (artistId > 0) {
            selection = selection + " and artist_id = " + artistId;
        }

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM

        };
        Cursor musicCursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            musicCursor = getContentResolver().query(uri, projection, selection, null, null);
            if(musicCursor!=null && musicCursor.moveToFirst()){

                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM);
                int albumIdColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);

                do {

                    long thisId = musicCursor.getLong(idColumn);

                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String album=musicCursor.getString(albumColumn);
                    long albumId=musicCursor.getLong(albumIdColumn);

                    artistSongList.add(new Song(thisId, thisTitle, thisArtist,albumId,album));
                }
                while (musicCursor.moveToNext());
            }


        } catch (Exception e) {
            Log.e("Media", e.toString());
        } finally {
            if (musicCursor != null) {
                musicCursor.close();
            }
        }


    }

    /*
    * onclick method to play selected song*/
    public void songPicked(View view){
        Toast.makeText(this,""+view.getTag().toString(),Toast.LENGTH_LONG).show();
        musicSrv.setSongByName(((TextView)view.findViewById(R.id.song_title)).getText().toString());
        musicSrv.playSong();
        if(playbackPaused){
            playbackPaused=false;
        }
            /*Starting new Activity*/
        Intent intent=new Intent(this, SongPlayer.class);
        startActivity(intent);
    }
}
