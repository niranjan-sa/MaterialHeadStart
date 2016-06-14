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
import com.blogreader.niranjansa.materialheadstart.adapter.AlbumAdapter;
import com.blogreader.niranjansa.materialheadstart.model.Album;

import java.util.ArrayList;


public class FourFragment extends Fragment{
    private ArrayList<Album> albumList;
    private ListView albumListView;
    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumList=new ArrayList<Album>();
        getAlbumList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=(View)inflater.inflate(R.layout.fragment_four, container, false);
        albumListView=(ListView)view.findViewById(R.id.album_list);
        if(albumList!=null) {
            AlbumAdapter albumAdapter = new AlbumAdapter(getActivity(), albumList);
            albumListView.setAdapter(albumAdapter);
        }
        return view;
    }

    //get list of all albums and add to array
    public void getAlbumList() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(uri, null,null,null,null);
        if (cursor != null) {
            if(cursor.getCount()>0) {
                cursor.moveToFirst();

                int idColumn=cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                int nameColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                int countColumn=cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
                do {
                    albumList.add(new Album(cursor.getLong(idColumn), cursor.getString(nameColumn),cursor.getInt(countColumn)));
                }while (cursor.moveToNext());

            }
        }


    }
}