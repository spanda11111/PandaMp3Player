package com.example.satya.pandamp3player;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    ListView listView;
    String[] items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lvPlaylist);
        //it will fetch all files from sd card
        final ArrayList<File> mysongs= findSongs(Environment.getExternalStorageDirectory());
        items= new String[mysongs.size()];
        for (int i=0; i<mysongs.size(); i++)
        {
            toast( mysongs.get(i).getName().toString());
            items[i]=  mysongs.get(i).getName().toString().replace(".wav",".mp3");
        }
        ArrayAdapter<String> adp= new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textview);
        listView.setAdapter(adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mysongs));
            }
        });

    }
    public ArrayList<File>findSongs(File roots)
    {
        ArrayList<File> arrayList=new ArrayList<File>();
        //here File[] will read all files from root folder as array index
        File[] files=roots.listFiles();
        for (File singleFile:files)
        {//if songs are in as a file and in a directory or as not hidden it will read
            if (singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findSongs(singleFile));
            }
            else
            {//if songs files are end with .mp3  format then also it will read
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }
    public  void toast(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

