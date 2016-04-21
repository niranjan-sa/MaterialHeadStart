package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.model.Artist;

import java.util.ArrayList;

/**
 * Created by vintej on 16/4/16.
 */
public class ArtistAdapter extends BaseAdapter {
    private ArrayList<Artist> artistList;
    private LayoutInflater artistListInflator;

    public ArtistAdapter(Context c, ArrayList<Artist> List){
        artistList=List;;
        artistListInflator=LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return artistList.size();
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
        RelativeLayout artistLayout = (RelativeLayout)artistListInflator.inflate
                (R.layout.artist, parent, false);

        TextView playListView = (TextView)artistLayout.findViewById(R.id.artist_name);

        Artist currentArtist= artistList.get(position);
        playListView.setText(currentArtist.getTitle());
        playListView.setSelected(true);
        artistLayout.setTag(currentArtist.getId());
        return artistLayout;
    }
}
