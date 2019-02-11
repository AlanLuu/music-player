package com.music;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Playlist songs = new Playlist();
    private Map<String, Map<Integer, String>> map = new LinkedHashMap<>();

    private static final int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            return;
        }
        start();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                start();
            } else {
                //Permission not granted
                Toast.makeText(getApplicationContext(), "You must enable the storage permission for this app to work.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
            Local variables defined in enclosing scopes must be final or effectively final,
            so an array with a single element is used instead.
        */
        int[] id = new int[1];
        map.put("Sort", new LinkedHashMap<Integer, String>() {{
            String[] items = {
                    "By name ascending",
                    "By name descending",
                    "Randomly",
            };
            for (String item : items) {
                put(id[0]++, item);
            }
        }});
        map.put("Play random song", null);
        map.put("About this app", null);

        for (Map.Entry<String, Map<Integer, String>> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                menu.add(Menu.NONE, id[0]++, Menu.NONE, entry.getKey());
                continue;
            }
            SubMenu subMenu = menu.addSubMenu(entry.getKey());
            for (Map.Entry<Integer, String> innerEntry : entry.getValue().entrySet()) {
                subMenu.add(Menu.NONE, innerEntry.getKey(), Menu.NONE, innerEntry.getValue());
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.hasSubMenu()) {
            switch (item.getItemId()) {
                case 0:
                    songs.sort();
                    break;
                case 1:
                    songs.sortReverse();
                    break;
                case 2:
                    songs.shuffle();
                    break;
                case 3:
                    if (songs.size() > 0) {
                        playSong((int) (Math.random() * songs.size()));
                    } else {
                        Toast.makeText(getApplicationContext(), "You don't have any songs!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    startActivity(new Intent(this, AppInfoActivity.class));
                    break;
            }
            ((ListView) findViewById(R.id.song_list)).setAdapter(new SongAdapter(this, songs.getPlaylist()));
        }
        return super.onOptionsItemSelected(item);
    }

    private void start() {
        ListView songView = findViewById(R.id.song_list);
        getSongList();
        songs.sort();

        SongAdapter adapter = new SongAdapter(this, songs.getPlaylist());
        songView.setAdapter(adapter);

        songView.setOnItemClickListener((adapterView, view, position, id) -> playSong(position));

        songView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            Toast.makeText(getApplicationContext(), songs.get(position) + "", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void playSong(int position) {
        Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
        intent.putExtra("Song", songs.get(position).getArtist() + " - " + songs.get(position).getTitle());
        intent.putExtra("Id", songs.get(position).getId() + "");
        startActivity(intent);
    }

    private void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            do {
                int id = musicCursor.getInt(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                String album = musicCursor.getString(albumColumn);
                songs.add(new Song(title, artist, album, id));
            } while (musicCursor.moveToNext());

            musicCursor.close();
        }

        ActionBar actionBar = getSupportActionBar();
        int size = songs.size();
        if (actionBar != null) actionBar.setTitle(actionBar.getTitle() + " - " + size + " " + (size == 1 ? "song" : "songs"));
    }
}