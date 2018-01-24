 package com.example.cheshta.musicapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cheshta.musicapp.Service.MediaPlayerService;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "H";
    public static MediaPlayer mediaPlayer;
    ArrayList<File> mySongs;
    Uri u;
    Thread updateSeekBar;

    SeekBar seekBar;
    TextView tvSong;
    Button btnFastBack, btnPrevious, btnPlay, btnNext, btnFastFor;
    ImageButton ibPlaylist;
    ImageView ivArt;
    int position;

    public static String songName;

    int[] covers = new int[]{
            R.drawable.album1,
            R.drawable.album2,
            R.drawable.album3,
            R.drawable.album4,
            R.drawable.album5,
            R.drawable.album6,
            R.drawable.album7,
            R.drawable.album8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        startService(intent);

        seekBar = findViewById(R.id.seekBar);
        btnFastBack = findViewById(R.id.btnFastBack);
        btnFastFor = findViewById(R.id.btnFastFor);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvSong = findViewById(R.id.tvSong);
        ibPlaylist = findViewById(R.id.ibPlaylist);
        ivArt = findViewById(R.id.ivArt);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnFastFor.setOnClickListener(this);
        btnFastBack.setOnClickListener(this);
        ibPlaylist.setOnClickListener(this);

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos");
        /*byte[] bt = b.getByteArray("art");
        Bitmap bmp = BitmapFactory.decodeByteArray(bt,0,bt.length);*/

        songName = mySongs.get(position).getName().toString();

        tvSong.setText(songName);

        if(songName.contains("True Romance"))
            ivArt.setImageResource(covers[0]);
        else if(songName.contains("Xscape"))
            ivArt.setImageResource(covers[1]);
        else if(songName.contains("Maroon 5"))
            ivArt.setImageResource(covers[2]);
        else if(songName.contains("Born to Die"))
            ivArt.setImageResource(covers[3]);
        else if(songName.contains("Honeymoon"))
            ivArt.setImageResource(covers[4]);
        else if(songName.contains("I Need a Doctor"))
            ivArt.setImageResource(covers[5]);
        else if(songName.contains("Loud"))
            ivArt.setImageResource(covers[6]);
        else if(songName.contains("Legend"))
            ivArt.setImageResource(covers[7]);


        u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,u);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try {
                        sleep(3000);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        updateSeekBar.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlay:
                if(mediaPlayer.isPlaying()){
                    btnPlay.setText(">");
                    mediaPlayer.pause();
                }
                else {
                    mediaPlayer.start();
                    btnPlay.setText("||");
                }
                break;

            case R.id.btnFastFor:
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
                break;

            case R.id.btnFastBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
                break;

            case R.id.btnNext:
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setText("||");
                position = (position+1) % mySongs.size();
                songName = mySongs.get(position).getName().toString();
                tvSong.setText(songName);
                u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(this,u);
                mediaPlayer.start();
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());

                Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                intent.setAction(MediaPlayerService.ACTION_PLAY);
                startService(intent);
                break;

            case R.id.btnPrevious:
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setText("||");
                position = (position-1<0)? mySongs.size()-1 : position-1;
                songName = mySongs.get(position).getName().toString();
                tvSong.setText(songName);
                u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(this,u);
                mediaPlayer.start();
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());

                intent = new Intent(getApplicationContext(), MediaPlayerService.class);
                intent.setAction(MediaPlayerService.ACTION_PLAY);
                startService(intent);
                break;
                
            case R.id.ibPlaylist:
                startActivityForResult(new Intent(PlayerActivity.this,MainActivity.class),123);
        }
    }
}
