//display all artist
package com.blogreader.niranjansa.materialheadstart.activity;

/**
 * Created by niranjansa on 15/4/16.
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.ArtistAdapter;
import com.blogreader.niranjansa.materialheadstart.model.Artist;

import java.util.ArrayList;


public class ThreeFragment extends Fragment {
    private ArrayList<Artist> artistList;
    private ListView artistListView;
    public ThreeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistList=new ArrayList<Artist>();
        getArtistList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=(View)inflater.inflate(R.layout.fragment_three, container, false);
        artistListView=(ListView)view.findViewById(R.id.artist_list);
        if(artistList!=null) {
            ArtistAdapter artistAdapter = new ArtistAdapter(getActivity(), artistList);
            artistListView.setAdapter(artistAdapter);
        }
        return view;
    }

    public void getArtistList() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(uri, null,null,null,null);
        String[] project = { MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST};
        if (cursor != null) {
            if(cursor.getCount()>0) {
                cursor.moveToFirst();

                int idColumn=cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
                int nameColumn=cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

                do {
                    artistList.add(new Artist(cursor.getLong(idColumn), cursor.getString(nameColumn)));
                }while (cursor.moveToNext());

                Toast.makeText(getActivity(), "" + artistList.size() + " playlists found", Toast.LENGTH_LONG).show();
            }
        }


        

    }
}
