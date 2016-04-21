package com.blogreader.niranjansa.materialheadstart.activity;

/**
 * Created by niranjansa on 15/4/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.SongAdapter;


public class SongsFragment extends Fragment{

    private Intent playIntent;
    private ListView songView;


    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Music - player related code starts from here
                * */
        //Here I go
        // songView = (ListView)findViewById(R.id.song_list);
        //only querying

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=(View)inflater.inflate(R.layout.fragment_one, container, false);
        songView = (ListView)view.findViewById(R.id.song_list);
        //Song adapter is called by default

        SongAdapter songAdt = new SongAdapter(getActivity(),MainActivity.getList());
        songView.setAdapter(songAdt);
        return view;
    }

    ///--------------------------------------------------------------------------------

}
