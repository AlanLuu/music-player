package com.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private List<Song> songs = new ArrayList<>();

    public void add(Song song) {
        songs.add(song);
    }

    public void remove(int index) {
        songs.remove(index);
    }

    public void remove(Song song) {
        songs.remove(song);
    }

    public void clear() {
        songs.clear();
    }

    public Song get(int index) {
        return songs.get(index);
    }

    public int size() {
        return songs.size();
    }

    public void shuffle() {
        Collections.shuffle(songs);
    }

    public void sort() {
        Collections.sort(songs);
    }

    public void sortReverse() {
        Collections.sort(songs, Collections.reverseOrder());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Playlist)) return false;
        Playlist other = (Playlist) obj;
        return songs.equals(other.songs);
    }

    @Override
    public String toString() {
        return "Playlist: " + (songs + "");
    }
}