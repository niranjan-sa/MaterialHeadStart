package com.blogreader.niranjansa.materialheadstart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.model.Song;
import com.blogreader.niranjansa.materialheadstart.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class DisplaySongSuggestion extends AppCompatActivity {

    ArrayList<Song> songList=null;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_song_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         email=getIntent().getStringExtra("email");
        Log.i("extra", email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String s=email.substring(0,email.lastIndexOf("."));
        ListView listView=(ListView)findViewById(R.id.listViewDSS);
        Firebase ref = new Firebase(FirebaseConnection.getPath() + "/Userdata/"+s+"/songs");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.song_title, R.id.songtitleDSS);
        listView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

             // songList= snapshot.getValue(ArrayList.class);
               for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                  Song song=postSnapshot.getValue(Song.class);
               adapter.add(song.getTitle() + "\nAlbum:" + song.getAlbumName()+"\n Artist:"+song.getArtist());
                 Log.i("User", song.getTitle());
             }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

}
