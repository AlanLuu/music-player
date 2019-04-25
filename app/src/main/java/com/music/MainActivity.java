package com.music;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
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

import com.music.util.ComparableList;
import com.music.util.Util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ComparableList<Song> songs = new ComparableList<>();
    private Map<String, Map<Integer, String>> map = new LinkedHashMap<>();
    private ListView songView;

    private static Mode mode = Mode.ASCENDING;

    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, Util.REQUEST_PERMISSION);
            return;
        }
        start();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Util.REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permissions granted
                start();
            } else {
                //Permissions not granted
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
        map.put("Refresh", null);
        map.put("About this app", null);
        map.put("Share this app", null);

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
                    mode = Mode.ASCENDING;
                    break;
                case 1:
                    songs.sortReverse();
                    mode = Mode.DESCENDING;
                    break;
                case 2:
                    songs.shuffle();
                    mode = Mode.RANDOM;
                    break;
                case 3:
                    if (songs.size() > 0) {
                        playSong(Util.randomInt(0, songs.size() - 1));
                    } else {
                        Toast.makeText(getApplicationContext(), "You don't have any songs!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    getSongList();
                    songs.sort();
                    Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    startActivity(new Intent(this, AppInfoActivity.class));
                    break;
                case 6:
                    Util.share(this, "Music", "Hey, come check out this open source music app at " + Util.REPO);
                    break;
            }
            ((ListView) findViewById(R.id.song_list)).setAdapter(new SongAdapter(this, songs));
        }
        return super.onOptionsItemSelected(item);
    }

    private void start() {
        songView = findViewById(R.id.song_list);
        getSongList();
        switch (mode) {
            case ASCENDING:
                songs.sort();
                break;
            case DESCENDING:
                songs.sortReverse();
                break;
            case RANDOM:
                songs.shuffle();
                break;
        }

        songView.setOnItemClickListener((adapterView, view, position, id) -> playSong(position));

        songView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Options");

            String[] items = {"Play", "Share", "Song info", "Delete"};
            builder.setItems(items, (dialogInterface, index) -> {
                switch (index) {
                    case 0:
                        playSong(position);
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("audio/*");
                        intent.putExtra(Intent.EXTRA_STREAM, ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                songs.get(position).getId()));
                        startActivity(Intent.createChooser(intent, "Share"));
                        break;
                    case 2:
                        Util.alert(MainActivity.this, "Song info", songs.get(position) + "", null);
                        break;
                    case 3:
                        Util.confirm(MainActivity.this, "Delete",
                                songs.get(position).getTitle() + " will be permanently deleted.", (dialogInterface1, i) -> {
                                    ContentResolver resolver = getContentResolver();
                                    Cursor cursor = resolver.query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                            null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        Uri deleteUri = ContentUris.withAppendedId(
                                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                                songs.get(position).getId());
                                        resolver.delete(deleteUri, null, null);
                                        cursor.close();
                                        getSongList();
                                        Toast.makeText(getApplicationContext(), "File deleted", Toast.LENGTH_SHORT).show();
                                    }
                        });
                        break;
                }
            });
            builder.create().show();
            return true;
        });
    }

    private void playSong(int position) {
        songs.get(position).play(this);
    }

    private void getSongList() {
        songs.clear();
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
        if (actionBar != null) actionBar.setTitle("Music - " + size + " " + (size == 1 ? "song" : "songs"));

        SongAdapter adapter = new SongAdapter(this, songs);
        songView.setAdapter(adapter);
    }

    private boolean hasPermissions(String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private enum Mode {
        ASCENDING, DESCENDING, RANDOM
    }
}