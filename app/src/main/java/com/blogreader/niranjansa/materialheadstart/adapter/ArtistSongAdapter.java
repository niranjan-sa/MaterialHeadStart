package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.ArrayList;

/**
 * Created by vintej on 18/4/16.
 */
public class ArtistSongAdapter  extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public ArtistSongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout songLay = (RelativeLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        songView.setSelected(true);
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
       Log.i("cc",currSong.getTitle());
        return songLay;
    }
}
