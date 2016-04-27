package com.blogreader.niranjansa.materialheadstart.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.activity.AlbumArt;
import com.blogreader.niranjansa.materialheadstart.model.Song;
import com.blogreader.niranjansa.materialheadstart.model.User;

import java.util.ArrayList;

/**
 * Created by vintej on 26/4/16.
 */
public class UserAdapter  extends BaseAdapter {
    private ArrayList<User> users;
    private LayoutInflater userInf;
    Context cc;

    public UserAdapter(Context c, ArrayList<User> user){
        users=user;
        userInf=LayoutInflater.from(c);
        cc=c;
    }
    @Override
    public int getCount() {
        return 0;
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
        RelativeLayout linearLayout = (RelativeLayout)userInf.inflate
                (R.layout.user, parent, false);
        //get title and artist views
        TextView usernameView = (TextView)linearLayout.findViewById(R.id.usernameTextview);
       // TextView emailView = (TextView)linearLayout.findViewById(R.id.emailTextview);
        //get song using position
        User currUser = users.get(position);
        //get title and artist strings
        usernameView.setText(currUser.getUsername());
       // usernameView.setSelected(true);
       // emailView.setText(currUser.getEmail());
        Toast.makeText(cc,"",Toast.LENGTH_LONG).show();
        linearLayout.setTag(position);
        return linearLayout;

    }
}
