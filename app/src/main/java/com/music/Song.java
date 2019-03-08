package com.music;

import android.content.Context;
import android.content.Intent;

/**
 * A boilerplate class that contains data about a song.
 * @author Alan Luu
 */
public class Song implements Comparable<Song> {
    private String title;
    private String artist;
    private String album;
    private int id;

    /**
     * Constructs a Song object with a title, artist, album, and unique identifier in the form of an int.
     * @param title The song's title
     * @param artist The song's artist
     * @param album The song's album
     * @param id a unique identifier for this song
     */
    public Song(String title, String artist, String album, int id) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.id = id;
    }

    /**
     * Gets the current song title
     * @return the current song title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the current song artist
     * @return the current song artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Gets the current song album
     * @return the current song album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Gets the unique song identifier.
     * @return the unique song identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Plays the current song.
     * @param context the current activity context
     */
    public void play(Context context) {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("Song", artist + " - " + title);
        intent.putExtra("Id", id + "");
        context.startActivity(intent);
    }

    /**
     * Compares two songs based on their title, ignoring capitalization.
     * @param other another song to be compared.
     * @return 0 if the argument song has the same title, a value less than 0 if this song's
     *         title is lexicographically less than the other song's title, and a value greater
     *         than 0 if this song's title is lexicographically greater than the other song's
     *         title.
     */
    @Override
    public int compareTo(Song other) {
        return title.compareToIgnoreCase(other.title);
    }

    /**
     * Returns the string representation of this Song object.
     * @return the string representation of this Song object
     */
    @Override
    public String toString() {
        return "Title: " + title + "\n\nArtist: " + artist + "\n\nAlbum: " + album;
    }
}