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
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.SongAdapter;
import com.blogreader.niranjansa.materialheadstart.adapter.UserAdapter;
import com.blogreader.niranjansa.materialheadstart.model.Song;
import com.blogreader.niranjansa.materialheadstart.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class SongSuggestion extends AppCompatActivity {

    private ArrayList<User> userArrayList;
    ListView userlist;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userArrayList=new ArrayList<User>();
        setContentView(R.layout.activity_song_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user=FirebaseConnection.getUser();
        if(user==null)
        {
            Toast.makeText(this,"Please Login",Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        userlist = (ListView)findViewById(R.id.UsersList);
         final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.user, R.id.usernameTextview);
            userlist.setAdapter(adapter);

        Firebase ref = new Firebase(FirebaseConnection.getPath() + "/Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user1 = postSnapshot.getValue(User.class);
                    adapter.add(user1.getEmail());
                    Log.i("User", user1.getEmail());

                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



    }

    public void showUserSongList(View view)
    {
        TextView t=(TextView)view.findViewById(R.id.usernameTextview);
        Intent intent= new Intent(this,DisplaySongSuggestion.class);
        intent.putExtra("email",t.getText());
        startActivity(intent);
    };


}
