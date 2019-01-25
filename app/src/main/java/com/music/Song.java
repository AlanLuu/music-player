package com.music;

import android.support.annotation.NonNull;

public class Song implements Comparable<Song> {
    private String title;
    private String artist;
    private String album;
    private int id;

    Song(String title, String artist, String album, int id) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    String getArtist() {
        return artist;
    }

    String getAlbum() {
        return album;
    }

    int getId() {
        return id;
    }

    @Override
    public int compareTo(Song other) {
        return title.compareToIgnoreCase(other.title);
    }

    @NonNull
    @Override
    public String toString() {
        return artist + " - " + title + " - " + album + " \nID: 0x" + Integer.toHexString(id).toUpperCase();
    }
}