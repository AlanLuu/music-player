package com.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.songs = new ArrayList<>(songs);
        songInf = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, viewGroup, false);
        TextView songView = songLayout.findViewById(R.id.song_title);
        TextView artistView = songLayout.findViewById(R.id.song_artist);
        TextView albumView = songLayout.findViewById(R.id.song_album);

        Song currentSong = songs.get(pos);
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        albumView.setText(currentSong.getAlbum());
        songLayout.setTag(pos);
        return songLayout;
    }
}