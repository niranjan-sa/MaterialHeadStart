package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.activity.AlbumArt;
import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.ArrayList;

/**
 * Created by vintej on 18/4/16.
 */
public class ArtistSongAdapter  extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    Context cc;

    public ArtistSongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
        cc=c;
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

        //add album art
        ImageView albumImageView=(ImageView)songLay.findViewById(R.id.album_art);
        AlbumArt albumArt=new AlbumArt();
        Bitmap albumImage=albumArt.getAlbumArt(currSong.getAlbumId(), cc);
        if(albumImage!=null)
            albumImageView.setImageBitmap(albumImage);


        //set position as tag
        songLay.setTag(position);
       Log.i("cc",currSong.getTitle());
        return songLay;
    }
}
