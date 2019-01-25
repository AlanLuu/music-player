package com.music;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private List<Song> songs = new ArrayList<>();

    void add(Song song) {
        songs.add(song);
    }

    void remove(int index) {
        songs.remove(index);
    }

    void remove(Song song) {
        songs.remove(song);
    }

    Song get(int index) {
        return songs.get(index);
    }

    List<Song> getPlaylist() {
        return new ArrayList<>(songs);
    }

    int size() {
        return songs.size();
    }

    void shuffle() {
        Collections.shuffle(songs);
    }

    void sort() {
        Collections.sort(songs);
    }

    void sortReverse() {
        Collections.sort(songs, Collections.reverseOrder());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Playlist)) return false;
        Playlist other = (Playlist) obj;
        return songs.equals(other.songs);
    }

    @NonNull
    @Override
    public String toString() {
        return "Playlist: " + (songs + "");
    }
}