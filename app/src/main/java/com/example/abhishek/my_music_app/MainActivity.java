package com.example.abhishek.my_music_app;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lvplaylist);
        final ArrayList<File> mysongs = findSongs(Environment.getExternalStorageDirectory());
        items=new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            //toast(mysongs.get(i).getName());
            items[i]=mysongs.get(i).getName().toString().replace(".mp3","");
        }

        ArrayAdapter<String> adp=new ArrayAdapter<>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songsList",mysongs));
            }
        });
    }

    public ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singlefile : files) {
            if (singlefile.isDirectory() && !singlefile.isHidden()) {
                al.addAll(findSongs(singlefile));

            } else {
                if (singlefile.getName().endsWith(".mp3")) {
                    al.add(singlefile);
                }
            }

        }
        return al;
    }

    //public void toast(String text) {
    //    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

   // }

}

