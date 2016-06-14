//display all playlists
package com.blogreader.niranjansa.materialheadstart.activity;

/**
 * Created by niranjansa on 15/4/16.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.PlayListAdapter;
import com.blogreader.niranjansa.materialheadstart.model.PlayList;

import java.util.ArrayList;


public class TwoFragment extends Fragment{

    private ArrayList<PlayList> playLists;
    private ListView pLView;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //playlist
        playLists=new ArrayList<PlayList>();
        getPlayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=(View)inflater.inflate(R.layout.fragment_two, container, false);
        pLView=(ListView)view.findViewById(R.id.play_list);
        if(playLists!=null) {
            PlayListAdapter pLAdt = new PlayListAdapter(getActivity(), playLists);
            pLView.setAdapter(pLAdt);
        }
        return view;
    }

    public void getPlayList() {
        String[] project = { MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME };

        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                project,
                null,
                null,
                null);

        if (cursor != null) {
            if(cursor.getCount()>0) {
                cursor.moveToFirst();

                int idColumn=cursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
                int nameColumn=cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);

                do {
                    playLists.add(new PlayList(cursor.getLong(idColumn), cursor.getString(nameColumn)));
                }while (cursor.moveToNext());

                Toast.makeText(getActivity(), "" + playLists.size() + " playlists found", Toast.LENGTH_LONG).show();
            }
        }

    }

}
