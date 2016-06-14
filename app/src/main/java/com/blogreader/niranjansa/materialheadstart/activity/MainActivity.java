package com.blogreader.niranjansa.materialheadstart.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.adapter.DatabaseConnection;
import com.blogreader.niranjansa.materialheadstart.model.MusicService;
import com.blogreader.niranjansa.materialheadstart.model.PlayList;
import com.blogreader.niranjansa.materialheadstart.model.Song;
import com.blogreader.niranjansa.materialheadstart.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//vin
public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private static ArrayList<Song> songList;
    private ArrayList<PlayList> playLists;
    private ListView songView;

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    //
    private boolean paused=false, playbackPaused=false;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;

            Toast.makeText(MainActivity.this, "ok set",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            musicBound = false;

            Toast.makeText(MainActivity.this, "Probe",Toast.LENGTH_LONG).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Get Permissions for MM SD card read
        * to solve Issue with marsh mellow
        * */

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
        if(permissionCheck== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        //Getting the runtime permissions over!!

        songList = new ArrayList<Song>();

        //Getting the songs
        getSongList();

        //Sorting Alphabetically
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Changed the title
        setTitle("Music Player");


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Toolbar Setup ends here

        //Tabs setup
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Tabs setup end

        //Drawer
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        //Firebase

        Firebase.setAndroidContext(getBaseContext());
       FirebaseConnection.setPath(getResources().getString(R.string.firebaselink));
        Firebase firebase=new Firebase(FirebaseConnection.getPath());
        AuthData authData=firebase.getAuth();
        FirebaseConnection.setAuthData(authData);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, com.blogreader.niranjansa.materialheadstart.model.MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

    }

    @Override
    protected void onDestroy() {
        //Stopping the service
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            //setController();
            paused=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           Intent intent=new Intent(this,Settings.class);
            startActivity(intent);
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id==R.id.sync)
        {
            syncWithFirebase();
        }

        return super.onOptionsItemSelected(item);
    }
    public void syncWithFirebase()
    {
        List mostplayed=new DatabaseConnection(this).getMostPlayedSongs();
        AuthData a= FirebaseConnection.getAuthData();
        if(a==null || a.getAuth()==null)
        {
            Toast.makeText(this,"Please login",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            User user=FirebaseConnection.getUser();
            if(user==null)
            {
                Toast.makeText(this,"Please login",Toast.LENGTH_LONG).show();
                return;
            }

            Firebase firebase=new Firebase(FirebaseConnection.getPath());
            String s=user.getEmail();
            s= s.substring(0,s.lastIndexOf("."));
            Firebase ref=firebase.child("/Userdata/"+s+"/songs");
            ref.setValue(mostplayed);
            //ref.setValue();


        }
    }
    //Listener for the drawer events start activity directly
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    public static ArrayList<Song> getList() {
        return songList;
    }

    public void getSongList() {
        //retrieve song info

        /*
        * Getting the songs from the external storage
        *
        * */

        boolean isSdPresent=android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);


        ContentResolver musicResolver;
        Uri musicUri;
        Cursor musicCursor;
        int songs=0;
        musicResolver = getContentResolver();


        //Checking sd card presence
        if(isSdPresent) {

            //Uri for the internal storage
            musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            //Cursor for ext storage
            musicCursor = musicResolver.query(musicUri, null, null, null, null);


            if(musicCursor!=null && musicCursor.moveToFirst()){
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM);
                int albumIdColumn = musicCursor.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);


                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String album=musicCursor.getString(albumColumn);
                    long albumId=musicCursor.getLong(albumIdColumn);
                    songList.add(new Song(thisId, thisTitle, thisArtist,albumId,album));

                }
                while (musicCursor.moveToNext());
            }
        }

        //Quering the internal memory
        //Changed my git username

        musicUri=MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        musicCursor = musicResolver.query(musicUri, null, null, null, null);
        int i=0;

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int albumIdColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);


            int dataColumn=musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);

            //retriving the flags to avoid showing the ring tones and other system files


            //add songs to list from the internal memory
            do {
                String path=musicCursor.getString(dataColumn);
                if(path.endsWith("mp3")) {
                    long thisId = musicCursor.getLong(idColumn);

                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String album=musicCursor.getString(albumColumn);
                    long albumId=musicCursor.getLong(albumIdColumn);

                    songList.add(new Song(thisId, thisTitle, thisArtist,albumId,album));
                    songs++;
                    i++;

                }
            }
            while (musicCursor.moveToNext());
        }


        if(i==0) {
            Toast.makeText(this, "No songs present on the inp device :- ",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Total songs queried :- " + songs + " Internal - " + i + " External :- " + (songs - i), Toast.LENGTH_LONG).show();

        }
    }


    //Init View pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongsFragment(), "All Songs");
        adapter.addFragment(new TwoFragment(), "Playlists");
        adapter.addFragment(new ThreeFragment(), "Artists");
        adapter.addFragment(new FourFragment(), "Albums");

        viewPager.setAdapter(adapter);
    }

    //ViewPager Adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void displayView(int position) {
        Fragment fragment = null;
        Intent intent=null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                 intent =new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case 1: intent =new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case 2:intent =new Intent(this, SongSuggestion.class);
                startActivity(intent);


                break;
            default:
                break;
        }

        /*if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }*/
    }

    //onclick method invoke when clicked on song name from fragment 1
    public void songPicked(View view){
            musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
            musicSrv.playSong();
            if(playbackPaused){

                playbackPaused=false;
            }
            /*Starting new Activity*/
            Intent intent=new Intent(this, SongPlayer.class);
            startActivity(intent);
    }

    //onclick method invoke when clicked on album name from fragment 2
    public void albumPicked(View view){
        Intent intent= new Intent(this, AlbumSongListActivity.class);

        int a=0;
        a=Integer.parseInt(view.getTag().toString());
        TextView t= (TextView) view.findViewById(R.id.album_name);
        intent.putExtra("id",a);
        intent.putExtra("albumTitle", "" + t.getText());

        startActivity(intent);
    }


    //onclick method invoke when clicked on artist name from fragment 3
    public void artistPicked(View view){
        Intent intent=new Intent(this, ArtistSongListActivity.class);
        TextView t = (TextView) view.findViewById(R.id.artist_name);
        int a=0;
        a=Integer.parseInt(view.getTag().toString());
        try{

        }
        catch (Exception e){};;

        intent.putExtra("id",a);
        intent.putExtra("artistTitle", "" + t.getText());
        startActivity(intent);
    }


    //onclick method invoke when clicked on playlist name from fragment 4
    public void displayPlaylist(View view){
        Intent intent=new Intent(this, DisplayPlaylist.class);
        int a=0;
        a=Integer.parseInt(view.getTag().toString());
        Log.i("playlist",""+a);
        intent.putExtra("id",a);
        TextView t = (TextView) view.findViewById(R.id.play_list_title);
        intent.putExtra("playlistTitle", "" + t.getText());
        startActivity(intent);
    }
   public void start() {
        musicSrv.go();
    }

    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }


    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }


    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;

    }

    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }


    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }


    public int getBufferPercentage() {
        return 0;
    }


    public boolean canPause() {
        //initially false
        return true;
    }


    public boolean canSeekBackward() {
        /*Originally false*/
        return true;
    }


    public boolean canSeekForward() {
        /*Originally false*/
        return true;
    }


    public int getAudioSessionId() {
        return 0;
    }

    /*Service triggering methods*/
    
    //play next
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            //setController();
            playbackPaused=false;
        }
        //controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            //setController();
            playbackPaused=false;
        }
        //controller.show(0);
    }
    //added popupmenu to three dot button
   /* public  void popOptionMenu(View view)
    {
        final ImageButton ib=(ImageButton)view.findViewById(R.id.optionButton);
        PopupMenu popup=new PopupMenu(MainActivity.this,ib);
        popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
        Toast.makeText(this, "" + ib.getTag(), Toast.LENGTH_LONG).show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                //    Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId() == R.id.one) {
                    addToExistingPlaylist((int) ib.getTag());
                } else if (item.getItemId() == R.id.two) {
                    Toast.makeText(MainActivity.this, "You Clicked : two", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });
        popup.show();

    }*/

    public void openUserInfoActivity(View view)
    {

        if(FirebaseConnection.getAuthData()!=null &&FirebaseConnection.getAuthData().getAuth()!=null &&FirebaseConnection.getUser()!=null)
        {
            Intent intent=new Intent(this,UserInfoActivity.class);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
            return;

        }


    }
   public void addToExistingPlaylist(int position)
   {
      Song song=(Song)songList.get(position);
       song.getTitle();
   }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }
}
