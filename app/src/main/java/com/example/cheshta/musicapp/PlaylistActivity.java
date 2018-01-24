package com.example.cheshta.musicapp;

import android.content.Intent;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    ListView lvPlaylist;
    String[] items;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent intentMain = getIntent();
        title = intentMain.getStringExtra("title");

        /*Bundle extras = intentMain.getExtras();
        final byte[] b = extras.getByteArray("art");*/

        lvPlaylist = findViewById(R.id.lvPlaylist);

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];

        for(int i=0; i<mySongs.size();i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.song_layout,R.id.tvTitle,items);
        lvPlaylist.setAdapter(arrayAdapter);
        lvPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(PlaylistActivity.this,PlayerActivity.class)
                        .putExtra("pos",i)
                        .putExtra("songlist",mySongs));
            }
        });
    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(title+".mp3")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }
}
