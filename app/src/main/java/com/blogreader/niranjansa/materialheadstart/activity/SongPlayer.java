package com.blogreader.niranjansa.materialheadstart.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blogreader.niranjansa.materialheadstart.R;
import com.blogreader.niranjansa.materialheadstart.model.MusicService;

public class SongPlayer extends AppCompatActivity {

   // private static Button play, pause, next, prev;
    private static TextView position, duration;
    private static SeekBar seek;
    private Intent playIntent;
    private MusicService musicSrv;
    private boolean musicBound=false;
    private Thread seekbarUpdater;
    private volatile boolean threadController=true;
    private boolean playing=true;
    private static ImageButton iplay, inext, iprev, info, like;
    private SeekBar sound;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            //musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //back button enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        * Initializing the views
        * */

        /*play=(Button)findViewById(R.id.pl);
        pause=(Button)findViewById(R.id.ps);
        next=(Button)findViewById(R.id.nx);
        prev=(Button)findViewById(R.id.pre);*/
        /**/

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onStop() {
        threadController=false;
        seekbarUpdater.interrupt();
        super.onStop();
    }

    @Override
    protected void onResume() {
        /*This is a very important bug fix here!!*/
        super.onResume();
        iplay=(ImageButton)findViewById(R.id.play_b);
        inext=(ImageButton)findViewById(R.id.next_b);
        iprev=(ImageButton)findViewById(R.id.prev_b);
        //sound=(SeekBar)findViewById(R.id.sound_sk);

        if(MusicService.isPlayingOn()) {
            iplay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            iplay.setImageResource(android.R.drawable.ic_media_play);
        }

        position=(TextView)findViewById(R.id.pos);
        duration=(TextView)findViewById(R.id.dur);

        seek=(SeekBar)findViewById(R.id.seekBar);

        /*Init TextViews*/
        position.setText("");
        duration.setText("");

        /*Initializing thread*/


        seekbarUpdater=new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        duration.setText(getHms(MusicService.getDurn()));
                    }
                });
                while (threadController) {
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long pos = MusicService.getPosnn();
                            /*Seekbar Update on song change issue is fixed here*/
                                if (pos >= MusicService.getPosnn()) {
                                    duration.setText(getHms(MusicService.getDurn()));
                                    getHms(MusicService.getDurn());
                                    seek.setMax(MusicService.getDurn());
                                }
                                String hms_stat = getHms(pos);
                                position.setText(getHms(pos));
                                seek.setProgress((int) (pos));
                            }
                        });
                    } catch (Exception e) {
                        threadController=false;
                    }


                }
            }
        });
        threadInit();
        seekbarUpdater.start();

        /*
        * Addin Seek bar action listener
        * */
        seek.setMax(MusicService.getDurn());
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //musicSrv.seek(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicSrv.seek(seekBar.getProgress());
            }
        });
        /**/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    public void setSound(View view) {


    }

    public String getHms(long millsec) {
        int h, m, s;
        millsec=millsec/1000;
        s=(int)millsec%60;
        m=((int)millsec/60)%60;
        h=((int)millsec/(60*60))%60;
        return h==0?String.format("%02d:%02d", m, s):String.format("%d:%02d:%02d", h, m, s);
    }

    public void changeDuration(String durn) {
        duration.setText(durn);
    }

    public void stopThread() {
        threadController=false;
    }

    public void threadInit() {
        threadController=true;
    }

    /*
        * Control Methods
        * */
    public void play(View view) {


        /*
        * To add play pause controls Remaining
        * */


        if(musicBound) {
            if(musicSrv.isPng()) {
                iplay.setImageResource(android.R.drawable.ic_media_play);
                musicSrv.pausePlayer();
            } else {
                iplay.setImageResource(android.R.drawable.ic_media_pause);
                musicSrv.go();
            }
        }


        /*threadInit();
        if(seekbarUpdater.isAlive())
            seekbarUpdater.interrupt();
        seekbarUpdater.start();*/
    }

    public void playNext(View view) {
        if(musicBound)
            musicSrv.playNext();
    }
    public void playPrevious(View view) {
        if(musicBound)
            musicSrv.playPrev();
    }
    public void pause(View view) {
        if(musicBound)
            musicSrv.pausePlayer();
        //stopThread();
    }
}
