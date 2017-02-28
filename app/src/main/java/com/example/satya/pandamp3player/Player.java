package com.example.satya.pandamp3player;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends ActionBarActivity implements View.OnClickListener {
    static MediaPlayer mediaPlayer;
    ArrayList<File> mysongs;
    SeekBar seekBar;
    Button btplay, btfastforward, btnext, btprevious, btfastbackward;
    int position;
    Uri uri;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //initialize all  button variable
        btplay = (Button) findViewById(R.id.btplay);
        btfastbackward = (Button) findViewById(R.id.btfastbackward);
        btfastforward = (Button) findViewById(R.id.btfastforward);
        btnext = (Button) findViewById(R.id.btnext);
        btprevious = (Button) findViewById(R.id.btprevious);

        btplay.setOnClickListener(this);
        btfastbackward.setOnClickListener(this);
        btnext.setOnClickListener(this);
        btprevious.setOnClickListener(this);
        btfastforward.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread() {
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                //seekBar.setMax(totalDuration);

                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mysongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        int position = bundle.getInt("pos", 0);
        uri = Uri.parse(mysongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

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
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch(id){
            case R.id.btplay:

                if (mediaPlayer.isPlaying())
                {
                    btplay.setText(">");
                    mediaPlayer.pause();
                }
                else
                {
                    btplay.setText("||");
                    mediaPlayer.start();

                }
                break;
            case R.id.btfastforward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;
            case R.id.btfastbackward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;
            case R.id.btnext:
                //media player stop
                mediaPlayer.stop();
                //media player releases
                mediaPlayer.release();
                //new Uri generated and media player started
                //here we used %mysongs.size() because if 10 songs are there after we click next button it will come to index number 0,1,2... like that
                position= (position+1)%mysongs.size();
                uri = Uri.parse(mysongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;
            case R.id.btprevious:
                mediaPlayer.stop();
                //media player releases
                mediaPlayer.release();
                //new Uri generated and media player started
                //here we used %mysongs.size() because if 10 songs are there after we click next button it will come to index number 0,1,2... like that
                position= (position-1<0)? mysongs.size():position-1;
               /* if (position-1 <0)
                {
                    position=mysongs.size()-1;
                }
                else
                {
                    position=position-1;
                } */
                uri = Uri.parse(mysongs.get(position).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                break;

        }

    }

}
