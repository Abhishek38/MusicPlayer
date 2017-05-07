package com.example.abhishek.my_music_app;

import android.content.Intent;

import android.media.MediaPlayer;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

import static android.R.drawable.ic_media_pause;

public class Player extends AppCompatActivity implements View.OnClickListener {
   static MediaPlayer mp;
    ArrayList<File> mysongs;
    SeekBar sb;
    Thread updateSeekBar;
    Uri u;
    int position;
    ImageButton btplay, btFaF, btrev, btnxt, btpre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btplay = (ImageButton) findViewById(R.id.btPlay);
        btFaF = (ImageButton) findViewById(R.id.btFF);
        btnxt = (ImageButton) findViewById(R.id.btNext);
        btpre = (ImageButton) findViewById(R.id.btPre);
        btrev = (ImageButton) findViewById(R.id.btRev);

        btplay.setOnClickListener(this);
        btrev.setOnClickListener(this);
        btpre.setOnClickListener(this);
        btnxt.setOnClickListener(this);
        btFaF.setOnClickListener(this);

        sb = (SeekBar) findViewById(R.id.seekBar2);
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                sb.setMax(totalDuration);
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                 //super.run();
            }
        };




        if(mp!=null){
            mp.stop();
            mp.release();
        }


        Intent i = getIntent();
        Bundle b = i.getExtras();

        mysongs = (ArrayList) b.getParcelableArrayList("songsList");
        position= b.getInt("pos", 0);
        u = Uri.parse(mysongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        updateSeekBar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        switch (id){

            case R.id.btPlay:
                if (mp.isPlaying()){
                    btplay.setImageResource(R.drawable.ic_action_pause);
                    mp.pause();

                }
                else
                {btplay.setImageResource(R.drawable.ic_media_play);
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btNext:
                mp.stop();
                mp.release();
                position=(position+1)%mysongs.size();
                 u = Uri.parse(mysongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                break;
            case R.id.btPre:
                mp.stop();
                mp.release();
                position=(position-1<0)?mysongs.size()-1:position-1;
                 u = Uri.parse(mysongs.get((position-1)%mysongs.size()).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                break;


            case R.id.btRev:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
        }


    }
}


