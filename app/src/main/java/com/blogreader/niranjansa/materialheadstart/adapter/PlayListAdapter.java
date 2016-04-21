package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.model.PlayList;

import java.util.ArrayList;

/**
 * Created by niranjansa on 12/4/16.
 */
public class PlayListAdapter extends BaseAdapter {
    private ArrayList<PlayList> playList;
    private LayoutInflater playListInf;

    public PlayListAdapter(Context c, ArrayList<PlayList> playList){
        this.playList=playList;
        playListInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return playList.size();
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
        RelativeLayout pLLay = (RelativeLayout)playListInf.inflate
                (R.layout.playlist_view, parent, false);
        //get title of PL views
        TextView playListView = (TextView)pLLay.findViewById(R.id.play_list_title);
        //get PL using position
        PlayList currPL= playList.get(position);
        //get title of PL strings
        playListView.setText(currPL.getTitle());

        //set position as tag
        pLLay.setTag(position);
        return pLLay;
    }
}
