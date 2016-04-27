package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.activity.AlbumArt;
import com.blogreader.niranjansa.materialheadstart.model.Song;

import java.util.ArrayList;

/**
 * Created by niranjansa on 18/3/16.
 */
public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    Context cc;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
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
        //map to song layout
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

        //added album art
        ImageView albumImageView=(ImageView)songLay.findViewById(R.id.album_art);
        AlbumArt albumArt=new AlbumArt();
        Bitmap albumImage=albumArt.getAlbumArt(currSong.getAlbumId(), cc);
        if(albumImage!=null)
        albumImageView.setImageBitmap(albumImage);
        //set position to option button
       // ImageButton ib=(ImageButton)songLay.findViewById(R.id.optionButton);
       // ib.setTag(position);
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }
   }
