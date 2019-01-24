package com.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Song implements Playable, Comparable<Song> {
    private String title;
    private String artist;
    private String album;
    private long id;

    public Song(String title, String artist, String album, long id) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public Song(String title, String artist, String album) {
        this(title, artist, album, 0);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getId() {
        return id;
    }

    @Override
    public void play(Context context, Activity activity) {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("Song", artist + " - " + title);
        intent.putExtra("Id", id + "");
        activity.startActivity(intent);
    }

    @Override
    public int compareTo(Song song) {
        return title.compareToIgnoreCase(song.title);
    }

    @Override
    public String toString() {
        return artist + " - " + title + " - " + album + " \nID: " + id;
    }
}