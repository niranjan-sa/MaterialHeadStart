package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.activity.AlbumArt;
import com.blogreader.niranjansa.materialheadstart.model.Album;
import com.blogreader.niranjansa.materialheadstart.model.Artist;

import java.util.ArrayList;

/**
 * Created by vintej on 16/4/16.
 */
public class AlbumAdapter extends BaseAdapter {
    private ArrayList<Album> albumList;
    private LayoutInflater albumListInflator;
    Context cc;

    public AlbumAdapter(Context c, ArrayList<Album> List){
        albumList=List;;
        albumListInflator=LayoutInflater.from(c);
        cc=c;
    }
    @Override
    public int getCount() {
        return albumList.size();
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
        RelativeLayout albumLayout = (RelativeLayout)albumListInflator.inflate
                (R.layout.album, parent, false);

        TextView album_nameTextView = (TextView)albumLayout.findViewById(R.id.album_name);
        TextView countTextView = (TextView)albumLayout.findViewById(R.id.no_of_songs);


        Album currentAlbum= albumList.get(position);

        album_nameTextView.setText(currentAlbum.getTitle());
        album_nameTextView.setSelected(true);
        int count=currentAlbum.getNoOfSongs();
        // added album art
        ImageView albumImageView=(ImageView)albumLayout.findViewById(R.id.album_image);
        AlbumArt albumArt=new AlbumArt();
        Bitmap albumImage=albumArt.getAlbumArt(currentAlbum.getId(), cc);
        if(albumImage!=null)
            albumImageView.setImageBitmap(albumImage);

        if(count==1)
            countTextView.setText(""+count+" song");
        else
            countTextView.setText(""+count + " song");

        albumLayout.setTag(currentAlbum.getId());
        return albumLayout;
    }
}